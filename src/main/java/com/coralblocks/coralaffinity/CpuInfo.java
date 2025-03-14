/* 
 * Copyright 2015-2025 (c) CoralBlocks LLC - http://www.coralblocks.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.coralblocks.coralaffinity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coralblocks.coralaffinity.Affinity.CLibrary;
import com.coralblocks.coralaffinity.pointer.Pointer;

public class CpuInfo {
	
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String VERBOSE_PREFIX = "---CoralAffinity---> ";
	
	private static class CpuBitmask {
		public int sizeInBits;
		public long[] cpuMask;
	}
	
	static class IntHolder {
		
		private final int value;
		
		IntHolder(int value) {
			this.value = value;
		}
		
		int getValue() {
			return value;
		}
	}
	
	private static boolean isInitialized = false;
	private static int numberOfProcessors = -1;
	private static IntHolder numberOfProcessorsHolder = null;
	private static boolean isEnabled = true;
	private static Boolean isAvailable = null;
	private static int[] isolatedCpus = null;
	private static CpuBitmask[] cpuBitmasks = null;
	private static int chosenCpuBitmaskSizeInBits = -1;
	private static int[] nonIsolatedCpus = null;
	private static Boolean isHyperthreadingOn = null;
	private static boolean isLinux = false;
	private static boolean isVerbose = false;
	private static boolean isPrintInfo = false;
	private static boolean isVerboseColors = true;
	private static List<List<Integer>> hyperthreadedPairs = null;
	private static List<List<Integer>> chipProcessors = null;
	private static int suggestedCpuBitmaskSizeInBits = -1;
	private static String cpuInfoFile = "/proc/cpuinfo";
	private static String cmdLineFile = "/proc/cmdline";
	
	private static boolean getBooleanConfig(String configName, boolean defValue) {
		String s1 = System.getProperty(configName);
		String s2 = System.getenv(configName);
		if (s1 != null) {
			return Boolean.parseBoolean(s1);
		} else if (s2 != null) {
			return Boolean.parseBoolean(s2);
		} else {
			return defValue;
		}
	}
	
	private static int getIntConfig(String configName, int defValue) {
		String s1 = System.getProperty(configName);
		String s2 = System.getenv(configName);
		if (s1 != null) {
			return Integer.parseInt(s1);
		} else if (s2 != null) {
			return Integer.parseInt(s2);
		} else {
			return defValue;
		}
	}
	
	private static String getStringConfig(String configName, String defValue) {
		String s1 = System.getProperty(configName);
		String s2 = System.getenv(configName);
		if (s1 != null) {
			return s1;
		} else if (s2 != null) {
			return s2;
		} else {
			return defValue;
		}
	}
	
	static {
		
		isEnabled = getBooleanConfig("coralAffinityEnabled", isEnabled);
		
		String OS = System.getProperty("os.name").toLowerCase();
		isLinux = OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
		
		isVerboseColors = getBooleanConfig("coralAffinityVerboseColors", isVerboseColors);
		
		suggestedCpuBitmaskSizeInBits = getIntConfig("coralAffinitySuggestedCpuBitmaskSizeInBits", suggestedCpuBitmaskSizeInBits);
		
		cpuInfoFile = getStringConfig("coralAffinityCpuInfoFile", cpuInfoFile);
		
		cmdLineFile = getStringConfig("coralAffinityCmdLineFile", cmdLineFile);
	}
	
	static {
		
		isVerbose = getBooleanConfig("coralAffinityVerbose", isVerbose);
		
		if (isVerbose) System.out.println();
		CpuInfo.init(isVerbose);
		if (isVerbose) System.out.println();
		
		isPrintInfo = getBooleanConfig("coralAffinityPrintInfo", isPrintInfo);
		
		if (isPrintInfo) {
			if (!isVerbose) System.out.println();
			CpuInfo.printInfo();
			System.out.println();
		}
	}
	
	private CpuInfo() {
		
	}
	
	private synchronized static boolean init(boolean verbose) {
		
		if (isInitialized) {
			return isAvailable(false);
		}
		
		boolean isAvailable = isAvailable(verbose);
		
		if (isAvailable) {
			
			numberOfProcessors = getLogicalProcessors();
			numberOfProcessorsHolder = new IntHolder(numberOfProcessors);
			if (verbose) System.out.println(VERBOSE_PREFIX + "Number of processors: " + numberOfProcessors);
			
			if (numberOfProcessors <= 0) throw new RuntimeException("Got an invalid number of processors: " + numberOfProcessors);
			
			chipProcessors = getLogicalProcessorsByPhysicalChipList();
			if (verbose) System.out.println(VERBOSE_PREFIX + "Processors per chip: " + chipProcessors);
			
			isHyperthreadingOn = isHyperthreadingOn(verbose);
			if (verbose) System.out.println(VERBOSE_PREFIX + "Is hyperthreading on: " + (isHyperthreadingOn == null ? "UNKNOWN" : isHyperthreadingOn));
			
			if (isHyperthreadingOn) {
				hyperthreadedPairs = getHyperthreadPairs();
				if (verbose) System.out.println(VERBOSE_PREFIX + "Hyperthreaded pairs: " + hyperthreadedPairs);
			}
			
			isolatedCpus = getIsolcpusProcIds(readProcCmdline());
			if (verbose) System.out.println(VERBOSE_PREFIX + "Isolcpus: " + (isolatedCpus != null ? arrayToString(isolatedCpus) : "NOT_DEFINED"));
			
			cpuBitmasks = scan(verbose);
			
			boolean areCpuBitmasksEqual = allEqual(cpuBitmasks);
			if (verbose) System.out.println(VERBOSE_PREFIX + "Cpu bitmasks are equal: " + areCpuBitmasksEqual);
			
			CpuBitmask chosenBitmask = null;
			
			if (suggestedCpuBitmaskSizeInBits > 0) {
				
				for(CpuBitmask bm : cpuBitmasks) {
					if (suggestedCpuBitmaskSizeInBits == bm.sizeInBits) {
						chosenBitmask = bm;
						break;
					}
				}
			}
			
			if (chosenBitmask == null) for(CpuBitmask bm : cpuBitmasks) {
				if (numberOfProcessors <= bm.sizeInBits) {
					chosenBitmask = bm;
					break;
				}
			}
			
			if (chosenBitmask == null) {
				
				if (verbose) {
					
					for(CpuBitmask r : cpuBitmasks) {
						printlnGreen(VERBOSE_PREFIX + "sizeInBits: " + r.sizeInBits
							+ " => cpuMask: " + toString(r.cpuMask)
							+ " (" + toBinaryString(r.cpuMask) + ")"
							+ " procIds=" + arrayToString(getProcIdsFromCpuBitmask(numberOfProcessorsHolder, r.cpuMask)));
					}
					
					System.out.println();
					
					printlnRed(VERBOSE_PREFIX + "Could not choose a bitmask size to use!"
							+ " numberOfProcessors=" + numberOfProcessors
							+ " sizeInBits=" + getSizeInBits(cpuBitmasks));
				}
				
				throw new RuntimeException("Could not choose a bitmask size to use!"
						+ " numberOfProcessors=" + numberOfProcessors
						+ " sizeInBits=" + getSizeInBits(cpuBitmasks));
			}
			
			chosenCpuBitmaskSizeInBits = chosenBitmask.sizeInBits;
			nonIsolatedCpus = getProcIdsFromCpuBitmask(numberOfProcessorsHolder, chosenBitmask.cpuMask);
			
			if (verbose) System.out.println(VERBOSE_PREFIX + "Bitmask chosen: sizeInBits=" + chosenBitmask.sizeInBits
					+ " cpuMask=" + toString(chosenBitmask.cpuMask)
					+ "-(" + toBinaryString(chosenBitmask.cpuMask) + ")"
					+ " procIds=" + arrayToString(nonIsolatedCpus));
		}
		
		isInitialized = true;
		
		return isAvailable;
	}
	
	/**
	 * Prints all the information regarding affinity in this machine.
	 */
	public static void printInfo() {
		
		System.out.println("isEnabled: " + isEnabled);
		System.out.println("isAvailable: " + isAvailable);

		String n;
		if (numberOfProcessors <= 0) {
			n = "NOT_AVAILABLE";
		} else if (numberOfProcessors == 1) {
			n = "1 (0)";
		} else {
			
			StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < numberOfProcessors; i++) {
	        	if (sb.length() > 0) sb.append(",");
	            sb.append(i);
	        }
			
			n = String.valueOf(numberOfProcessors) + " => procIds=" + sb.toString();
		}
		
		System.out.println("numberOfProcessors: " + n);
		
		String ppc;
		if (chipProcessors == null) {
			ppc = "NOT_AVAILABLE";
		} else {
			
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < chipProcessors.size(); i++) {
				List<Integer> procs = chipProcessors.get(i);
				if (i > 0) sb.append(" ");
				sb.append("chip").append(i).append("=");
				StringBuilder sb2 = new StringBuilder();
				for(int procId : procs) {
					if (sb2.length() > 0) sb2.append(",");
					sb2.append(procId);
				}
				sb.append(sb2);
			}
			
			ppc = sb.toString();
		}
		
		System.out.println("processorsPerChip: " + ppc);
		
		System.out.println("isHyperthreadingOn: " + (isHyperthreadingOn == null ? "UNKNOWN" : isHyperthreadingOn));
		
		if (isHyperthreadingOn()) {
			List<List<Integer>> hpairs = getHyperthreadedPairs();
			StringBuilder sb = new StringBuilder();
			for (List<Integer> pair : hpairs) {
				if (sb.length() > 0) sb.append(", ");
				sb.append(pair);
            }
			
			System.out.println("hyperthreadedPairs: " + sb.toString());
		}
		
		String r;
		if (cpuBitmasks == null) {
			r = "NOT_AVAILABLE";
		} else {
			
			StringBuilder sb = new StringBuilder();
			for(CpuBitmask cp : cpuBitmasks) {
				if (sb.length() > 0) sb.append(", ");
				sb.append(cp.sizeInBits);
			}
			sb.append(" (in bits)");
			
			r = String.valueOf(cpuBitmasks.length) + " => " + sb.toString();
		}
		
		System.out.println("cpuBitmasksFound: " + r);
		
		String c;
		if (chosenCpuBitmaskSizeInBits < 0 || numberOfProcessors <= 0) {
			c = "NOT_AVAILABLE";
		} else {
			c = String.valueOf(chosenCpuBitmaskSizeInBits) + " bits";
		}
		
		System.out.println("chosenCpuBitmaskSize: " + c);
		
		String a;
		if (nonIsolatedCpus == null || numberOfProcessors <= 0) {
			a = "NOT_AVAILABLE";
		} else {
			
			long[] nonIsolatedCpuBitmask = getCpuBitmaskFromProcIds(numberOfProcessorsHolder, nonIsolatedCpus);
			
			a = toString(nonIsolatedCpuBitmask)
					+ " (" + toBinaryString(nonIsolatedCpuBitmask) + ")"
					+ " => procIds=" + arrayToString(nonIsolatedCpus);
		}
		
		System.out.println("nonIsolatedCpuBitmask: " + a);
		
		String ic;
		if (isolatedCpus == null || numberOfProcessors <= 0) {
			ic = "NOT_AVAILABLE";
		} else if (isolatedCpus.length == 0) {
			ic = "NOT_DEFINED";
		} else {
			
			long[] isolatedCpuBitmask = getCpuBitmaskFromProcIds(numberOfProcessorsHolder, isolatedCpus);
			
			ic = toString(isolatedCpuBitmask)
					+ " (" + toBinaryString(isolatedCpuBitmask) + ")"
					+ " => procIds=" + arrayToString(isolatedCpus);
		}
		
		System.out.println("isolatedCpuBitmask: " + ic);
	}
	
	private static String getSizeInBits(CpuBitmask[] bitmasks) {
		StringBuilder sb = new StringBuilder();
		for(CpuBitmask bm : bitmasks) {
			if (sb.length() > 0) sb.append(",");
			sb.append(bm.sizeInBits);
		}
		return sb.toString();
	}
	
	private synchronized static boolean isAvailable(boolean verbose) {
		
		if (isAvailable == null) {
			
			if (!isLinux) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "CoralAffinity is only available on Linux!");
				isAvailable = false;
				return false;
			}
			
			File fileCpuInfo = new File(cpuInfoFile);
			if (!fileCpuInfo.exists()) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "Cannot find " + cpuInfoFile + " file!");
				isAvailable = false;
				return false;
			}
			
			File fileCmdLine = new File(cmdLineFile);
			if (!fileCmdLine.exists()) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "Cannot find " + cmdLineFile + " file!");
				isAvailable = false;
				return false;
			}
			
			try {
				Class.forName("com.sun.jna.Platform");
			} catch (Exception ignored) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "Cannot load JNA!");
				isAvailable = false;
				return false;
			}
			
			if (verbose) System.out.println(VERBOSE_PREFIX + "CoralAffinity is available!");
			
			isAvailable = true;
		}
		
		return isAvailable;
	}
	
	static boolean isLinux() {
		return isLinux;
	}
	
	/**
	 * Returns true if CoralAffinity is available in this machine.
	 * 
	 * @return true if available
	 */
	public static boolean isAvailable() {
		return isAvailable(false);
	}
	
	/**
	 * Returns true if CoralAffinity is enabled in this machine.
	 * CoralAffinity will be enabled by default unless disabled through
	 * -DcoralAffinityEnabled=false or through an environment variable.
	 * 
	 * @return true if enabled
	 */
	public static boolean isEnabled() {
		return isEnabled;
	}
	
	public static boolean isInitialized() {
		return isInitialized;
	}
	
	/**
	 * Returns the number of CPU logical processors available in this machine, according to
	 * the file <code>/proc/cpuinfo</code>.
	 * 
	 * @return the number of CPU logical processors
	 */
	public static int getNumberOfProcessors() {
		return numberOfProcessors;
	}
	
	/**
	 * Returns the list of the isolated CPU logical processors in this machine,
	 * according to the kernel configuration done through <code>isolcpus</code>.
	 * It can return an empty array if there is no <code>isolcpus</code> configuration on this
	 * machine, in other words, if there are no isolated CPU logical processors configured.
	 * It can also return <code>null</code> if CoralAffinity is not available.
	 * 
	 * @return the list of isolated CPU logical processors
	 */
	public static int[] getIsolatedCpus() {
		return isolatedCpus;
	}
	
	/**
	 * Returns the list of the non-isolated CPU logical processors in this machine,
	 * according to the kernel configuration done through <code>isolcpus</code>.
	 * It can return a list with all CPU logical processors in this machine for the case
	 * that <code>isolcpus</code> is not configured. In that case all CPU logical processors
	 * in this machine are be non-isolated.
	 * 
	 * @return the list of isolated CPU logical processors
	 */
	public static int[] getNonIsolatedCpus() {
		return nonIsolatedCpus;
	}
	
	/**
	 * Returns true if hyperthreading is being used by this machine.
	 * 
	 * @return true if hyperthreading is being used by this machine
	 */
	public static boolean isHyperthreadingOn() {
		if (isHyperthreadingOn != null) {
			return isHyperthreadingOn.booleanValue();
		}
		return false;
	}
	
	/**
	 * Returns the pairs of CPU logical processors representing a single CPU physical core.
	 * When the machine is using hyperthreading, each physical CPU core will contain two CPU logical processors.
	 * Note that it can return <code>null</code> is hyperthreading is not being used by the machine.
	 * 
	 * @return a list of pairs of CPU logical processors, one for each CPU physical core or <code>null</code> if hyperthreading is off
	 */
	public static List<List<Integer>> getHyperthreadedPairs() {
		return hyperthreadedPairs;
	}
	
	/**
	 * Returns the chosen CPU bitmask size in bits for this machine.
	 * 
	 * @return the size in bits of the chosen CPU bitmask
	 */
	public static int getChosenCpuBitmaskSizeInBits() {
		return chosenCpuBitmaskSizeInBits;
	}
	
	private static int getLogicalProcessors() {
		BufferedReader reader = null;
		Process process = null;
	    try {
	        process = Runtime.getRuntime().exec("grep -c ^processor " + cpuInfoFile);
	        reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
	        String line = reader.readLine();
	        return Integer.parseInt(line.trim());
	    } catch (Exception e) {
	        throw new RuntimeException("Cannot read number of processors from " + cpuInfoFile, e);
	    } finally {
	    	if (reader != null) try { reader.close(); } catch(Exception e) { throw new RuntimeException(e); }
	    	if (process != null) try { process.destroyForcibly(); } catch(Exception e) { throw new RuntimeException(e); }
	    }
	}
	
	private static String readProcCmdline() {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(cmdLineFile));
			return reader.readLine();
		} catch (Exception e) {
			throw new RuntimeException("Cannot read " + cmdLineFile, e);
		} finally {
			if (reader != null) try { reader.close(); } catch(Exception e) { throw new RuntimeException(e); }
		}
	}
	
	private static int[] getIsolcpusProcIds(final String cmdline) {
		
        Pattern pattern = Pattern.compile("\\bisolcpus=([^\\s]+)");
        Matcher matcher = pattern.matcher(cmdline);

        if (!matcher.find()) {
            return new int[0];
        }
        
        String value = matcher.group(1);
        
        List<Integer> cpuList = new ArrayList<>();
        String[] tokens = value.split(",");
        for (String token : tokens) {
            token = token.trim();
            if (token.contains("-")) {
                String[] rangeParts = token.split("-");
                int start = Integer.parseInt(rangeParts[0].trim());
                int end = Integer.parseInt(rangeParts[1].trim());
                for (int i = start; i <= end; i++) {
                    if (!cpuList.contains(i)) cpuList.add(i);
                }
            } else {
            	int i = Integer.parseInt(token);
            	if (!cpuList.contains(i)) cpuList.add(i);
            }
        }
        
        Collections.sort(cpuList);
        
        int[] toReturn = new int[cpuList.size()];
        for (int i = 0; i < cpuList.size(); i++) {
            toReturn[i] = cpuList.get(i);
        }
        
        return toReturn;
	}
	
	private static Boolean isHyperthreadingOn(boolean verbose) {
		
		if (verbose) System.out.println(VERBOSE_PREFIX + "Will check for hyperthreading...");
		
		Process process = null;

		try {

			process = new ProcessBuilder("lscpu").start();

			int exitCode = process.waitFor();

			if (exitCode != 0) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "lscpu returned bad exit code: " + exitCode);
				return null;
			}

			BufferedReader reader = null;

			try {

				reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line;

				while ((line = reader.readLine()) != null) {
					
					if (line.trim().startsWith("Thread(s) per core:")) {
						
						String[] parts = line.split(":\\s+");
						
						if (parts.length == 2) {
							
							int threadsPerCore = Integer.parseInt(parts[1].trim());
							
							if (verbose) System.out.println(VERBOSE_PREFIX + "Threads per core: " + threadsPerCore);
							
							return threadsPerCore > 1;
							
						} else {
							
							if (verbose) System.out.println(VERBOSE_PREFIX + "Bad line: " + line);
							
							return null;
						}
					}
				}
				
				if (verbose) System.out.println(VERBOSE_PREFIX + "Could not find Threads per core!");
				
				return null;
				
			} catch (Exception e) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "Exception: " + e.getMessage());
				return null;
			} finally {
				if (reader != null) try { reader.close(); } catch(Exception e) { throw new RuntimeException(e); }
			}

		} catch (Exception e) {
			if (verbose) System.out.println(VERBOSE_PREFIX + "Exception: " + e.getMessage());
			return null;
		} finally {
			if (process != null) try { process.destroyForcibly(); } catch(Exception e) { throw new RuntimeException(e); }
		}
	}
	
	static long[] getCpuBitmaskFromProcIds(int ... procIds) {
		if (!isInitialized || !isAvailable()) {
			return null;
		}
		return getCpuBitmaskFromProcIds(numberOfProcessorsHolder, procIds);
	}
	
	private static void ensureValidProcIds(int[] procIds, int numberOfProcessors) {
		for(int i : procIds) {
			if (i < 0 || i >= numberOfProcessors) {
				throw new IllegalArgumentException("Invalid procId! procId=" + i + " numberOfProcessors=" + numberOfProcessors);
			}
		}
	}
	
	static long invertBits(long value, int numberOfBits) {
		
	    if (numberOfBits < 0 || numberOfBits > 64) {
	        throw new IllegalArgumentException("numberOfBits must be between 0 and 64 (inclusive).");
	    }
	    
	    if (numberOfBits == 64) {
	        return ~value;
	    }
	    
	    long mask = (1L << numberOfBits) - 1;
	    
	    long truncated = value & mask;
	    
	    return truncated ^ mask;
	}
	
	static long[] getCpuBitmaskFromProcIds(IntHolder numberOfProcessorsHolder, int ... procIds) {
		
		final int numberOfProcessors = numberOfProcessorsHolder.getValue();
		
		ensureValidProcIds(procIds, numberOfProcessors);
		
		int numberOfLongs = (numberOfProcessors - 1) / 64 + 1;
		
		long[] bitmask = new long[numberOfLongs];
		
		for (int proc : procIds) {
			
	        int longIndex = proc / 64;
	        int bitIndex = proc % 64;
	        
	        bitmask[longIndex] |= (1L << bitIndex);
		}
        
        return bitmask;
	}
	
	private static int[] getSetBitPositions(long bitmask, int sizeInBits) {
		
	    if (sizeInBits < 1 || sizeInBits > 64) {
	        throw new IllegalArgumentException("Invalid size: " + sizeInBits + ". Must be between 1 and 64 (inclusive)!");
	    }
	    
	    List<Integer> clearedPositions = new ArrayList<>();

	    for (int i = 0; i < sizeInBits; i++) {
	        if ((bitmask & (1L << i)) != 0) {
	            clearedPositions.add(i);
	        }
	    }

	    int[] toReturn = new int[clearedPositions.size()];
	    for (int i = 0; i < clearedPositions.size(); i++) {
	        toReturn[i] = clearedPositions.get(i);
	    }

	    return toReturn;
	}
	
	static int[] getProcIdsFromCpuBitmask(IntHolder numberOfProcessorsHolder, long ... cpuBitmask) {
		
		List<Integer> list = new ArrayList<Integer>(256);
		
		int remainingBits = numberOfProcessorsHolder.getValue();

		for(int i = 0; i < cpuBitmask.length; i++) {
			
			long bm = cpuBitmask[i];
			
			int sizeInBits;
			if (remainingBits - 64 >= 0) {
				sizeInBits = 64;
			} else {
				sizeInBits = remainingBits;
			}
			
			int[] procIds = getSetBitPositions(bm, sizeInBits);
			
			int toAdd = i * 64;
			for(int proc : procIds) {
				list.add(proc + toAdd);
			}

			remainingBits -= 64;
			if (remainingBits <= 0) break;
		}
		
		int[] toReturn = new int[list.size()];
		for(int i = 0; i < list.size(); i++) {
			toReturn[i] = list.get(i).intValue();
		}
		
		return toReturn;
	}
	
	private static CpuBitmask[] scan(boolean verbose) {
		
		final CLibrary lib = Affinity.getLib();
		
		List<CpuBitmask> bitmasks = new ArrayList<CpuBitmask>(Pointer.ALL.size());
		
		if (verbose) System.out.println(VERBOSE_PREFIX + "Will begin scan: thread=" + Thread.currentThread().getName());
		
		for(Pointer p : Pointer.ALL) {
			
			if (verbose) System.out.print(VERBOSE_PREFIX + "Trying " + p.getClass().getSimpleName());
			
			try {
			
				int ret = lib.sched_getaffinity(0, p.getSizeInBytes(), p);
				
				if (ret >= 0) {
					CpuBitmask bm = new CpuBitmask();
					bm.cpuMask = p.getValue();
					bm.sizeInBits = p.getSizeInBytes() * 8;
					bitmasks.add(bm);
					if (verbose) printlnGreen(" ---> SUCCESS: ret=" + ret 
							+ " cpuMask=" + toString(bm.cpuMask)
							+ " (" + toBinaryString(bm.cpuMask) + ")");
				} else {
					if (verbose) printlnRed(" ---> FAILURE: ret=" + ret);
				}
				
				
			} catch(Throwable t) {
				if (verbose) printlnRed(" ---> FAILURE: exception=\"" + t.getMessage() + "\"");
			}
		}
		
		if (verbose) {
			int n = bitmasks.size();
			if (n == 0) {
				printlnRed(VERBOSE_PREFIX + "Finished without finding any bitmasks!");
			} else {
				printlnGreen(VERBOSE_PREFIX + "Finished with " + n + " bitmask" + (n > 1 ? "s!" : "!"));
			}
		}
		
		if (bitmasks.isEmpty()) throw new RuntimeException("Could not find any bitmasks for the cpu!");
		
		CpuBitmask[] array = new CpuBitmask[bitmasks.size()];
		return bitmasks.toArray(array);
	}
	
	private static String arrayToString(int[] arr) {
	    if (arr == null || arr.length == 0) {
	        return "";
	    }
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < arr.length; i++) {
	        sb.append(arr[i]);
	        if (i < arr.length - 1) {
	            sb.append(",");
	        }
	    }
	    return sb.toString();
	}
	
	private static void printlnGreen(String s) {
		if (isVerboseColors) {
			System.out.println(ANSI_GREEN + s + ANSI_RESET);
		} else {
			System.out.println(s);
		}
	}
	
	private static void printlnRed(String s) {
		if (isVerboseColors) {
			System.out.println(ANSI_RED + s + ANSI_RESET);
		} else {
			System.out.println(s);
		}
	}
	
	private static String toString(long[] value) {
		StringBuilder sb = new StringBuilder();
		for(long l : value) {
			if (sb.length() > 0) sb.append("|");
			sb.append(l);
		}
		return sb.toString();
	}
	
	static String toBinaryString(long[] value) {
		StringBuilder sb = new StringBuilder();
		for(long l : value) {
			if (sb.length() > 0) sb.append("|");
			if (l == 0) sb.append("0");
			else sb.append(Long.toBinaryString(l));
		}
		return sb.toString();
	}
	
	private static boolean allEqual(CpuBitmask[] bitmasks) {
		int[] procIds = null;
		for(CpuBitmask r : bitmasks) {
			if (procIds == null) {
				procIds = getProcIdsFromCpuBitmask(new IntHolder(r.sizeInBits), r.cpuMask);
			} else {
				int[] other = getProcIdsFromCpuBitmask(new IntHolder(r.sizeInBits), r.cpuMask);
				if (!Arrays.equals(other, procIds)) return false;
			}
		}
		return true;
	}
	
    private static class ProcessorInfo {
        int processor = -1;
        int physicalId = -1;
        int coreId = -1;

        boolean hasProcessor() {
            return processor != -1;
        }
        
        boolean hasProcessorAndChip() {
        	return processor != -1 && physicalId != -1;
        }
    }
    
    private static List<List<Integer>> getLogicalProcessorsByPhysicalChipList() {
    	
        List<ProcessorInfo> processors = new ArrayList<>();
        
        BufferedReader reader = null;
        
        try {
        	
        	reader = new BufferedReader(new FileReader(cpuInfoFile));
        	
            String line;
            ProcessorInfo info = new ProcessorInfo();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (info.hasProcessorAndChip()) {
                        processors.add(info);
                    }
                    info = new ProcessorInfo();
                } else {
                    String[] parts = line.split(":", 2);
                    if (parts.length < 2) {
                        continue;
                    }
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    if ("processor".equals(key)) {
                        info.processor = Integer.parseInt(value);
                    } else if ("physical id".equals(key)) {
                        info.physicalId = Integer.parseInt(value);
                    }
                }
            }
            
            if (info.hasProcessorAndChip()) {
                processors.add(info);
            }
            
        } catch(Exception e) {
        	throw new RuntimeException("Cannot read logical processors per chip from " + cpuInfoFile, e);
        } finally {
	    	if (reader != null) try { reader.close(); } catch(Exception e) { throw new RuntimeException(e); }
	    }
        
        Map<Integer, List<Integer>> chipMap = new HashMap<>();
        for (ProcessorInfo p : processors) {
            chipMap.computeIfAbsent(p.physicalId, k -> new ArrayList<>()).add(p.processor);
        }
        
        for (List<Integer> list : chipMap.values()) {
            Collections.sort(list);
        }
        
        int maxPhysicalId = -1;
        for (int chipId : chipMap.keySet()) {
            if (chipId > maxPhysicalId) {
                maxPhysicalId = chipId;
            }
        }
        
        List<List<Integer>> result = new ArrayList<>(maxPhysicalId + 1);
        for (int i = 0; i <= maxPhysicalId; i++) {
            result.add(new ArrayList<>());
        }
        
        for (Map.Entry<Integer, List<Integer>> entry : chipMap.entrySet()) {
            int chipId = entry.getKey();
            result.set(chipId, entry.getValue());
        }
        
        return result;
    }

    private static List<List<Integer>> getHyperthreadPairs() {
    	
        List<ProcessorInfo> processors = new ArrayList<>();
        
        BufferedReader reader = null;
        
        try {
        	
        	reader = new BufferedReader(new FileReader(cpuInfoFile));
        	
            String line;
            ProcessorInfo info = new ProcessorInfo();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                	
                    if (info.hasProcessor()) {
                        processors.add(info);
                    }
                    info = new ProcessorInfo();
                    
                } else {
                	
                    String[] parts = line.split(":", 2);
                    
                    if (parts.length < 2) {
                        continue;
                    }
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    
                    if ("processor".equals(key)) {
                        info.processor = Integer.parseInt(value);
                    } else if ("physical id".equals(key)) {
                        info.physicalId = Integer.parseInt(value);
                    } else if ("core id".equals(key)) {
                        info.coreId = Integer.parseInt(value);
                    }
                }
            }

            if (info.hasProcessor()) {
                processors.add(info);
            }
            
        } catch(Exception e) {
        	throw new RuntimeException("Cannot read hyperthreading pairs from " + cpuInfoFile, e);
        } finally {
	    	if (reader != null) try { reader.close(); } catch(Exception e) { throw new RuntimeException(e); }
	    }

        Map<String, List<Integer>> coreMap = new HashMap<>();
        for (ProcessorInfo p : processors) {
            String key = p.physicalId + "-" + p.coreId;
            coreMap.computeIfAbsent(key, k -> new ArrayList<>()).add(p.processor);
        }

        List<List<Integer>> hyperthreadPairs = new ArrayList<>();
        for (List<Integer> siblings : coreMap.values()) {
            if (siblings.size() >= 2) {
                Collections.sort(siblings);
                if (siblings.size() == 2) {
                    hyperthreadPairs.add(new ArrayList<>(siblings));
                } else {
                    for (int i = 0; i < siblings.size() - 1; i += 2) {
                        hyperthreadPairs.add(Arrays.asList(siblings.get(i), siblings.get(i + 1)));
                    }
                }
            }
        }
        
        return hyperthreadPairs;
    }
	
	public static void main(String[] args) {
		
		if (!isVerbose) System.out.println();
		
		if (!isPrintInfo) {
			CpuInfo.printInfo();
			System.out.println();
		}
	}
}