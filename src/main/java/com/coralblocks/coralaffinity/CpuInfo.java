package com.coralblocks.coralaffinity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coralblocks.coralaffinity.Affinity.CLibrary;
import com.coralblocks.coralaffinity.pointer.Pointer;

public class CpuInfo {
	
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String VERBOSE_PREFIX = "---CoralAffinity---> ";
	
	public static class Bitmask {
		public int sizeInBits;
		public int sizeInBytes;
		public long[] defaultCpuMask;
	}
	
	private static boolean isInitialized = false;
	private static int numberOfProcessors = -1;
	private static boolean isEnabled = true;
	private static Boolean isAvailable = null;
	private static int[] isolcpus = null;
	private static Bitmask[] bitmasks = null;
	private static Bitmask chosenBitmask = null;
	private static Boolean areBitmasksEqual = null;
	private static Boolean isHyperthreadingOn = null;
	
	static {
		final String isEnabledConfig = "coralAffinityEnabled";
		String s1 = System.getProperty(isEnabledConfig);
		String s2 = System.getenv(isEnabledConfig);
		if (s1 != null && s1.equalsIgnoreCase("false")) {
			isEnabled = false;
		} else if (s2 != null && s2.equalsIgnoreCase("false")) {
			isEnabled = false;
		}
	}
	
	private CpuInfo() {
		
	}
	
	public static boolean init() {
		return init(false);
	}
	
	public synchronized static boolean init(boolean verbose) {
		
		if (isInitialized) {
			return isAvailable(false);
		}
		
		boolean isAvailable = isAvailable(verbose);
		
		if (isAvailable) {
			
			numberOfProcessors = getLogicalProcessors();
			if (verbose) System.out.println(VERBOSE_PREFIX + "Number of processors: " + numberOfProcessors);
			
			if (numberOfProcessors <= 0) throw new RuntimeException("Got an invalid number of processors: " + numberOfProcessors);
			
			isHyperthreadingOn = isHyperthreadingOn(verbose);
			if (verbose) System.out.println(VERBOSE_PREFIX + "Is hyperthreading on: " + (isHyperthreadingOn == null ? "UNKNOWN" : isHyperthreadingOn));
			
			isolcpus = getIsolcpusNumbers(readProcCmdline());
			if (verbose) System.out.println(VERBOSE_PREFIX + "Isolcpus: " + (isolcpus != null ? arrayToString(isolcpus) : "NOT_DEFINED"));
			
			bitmasks = scan(verbose);
			
			areBitmasksEqual = allEqual(bitmasks);
			if (verbose) System.out.println(VERBOSE_PREFIX + "Bitmasks are equal: " + areBitmasksEqual);
			
			for(Bitmask bm : bitmasks) {
				if (numberOfProcessors <= bm.sizeInBits) {
					chosenBitmask = bm;
					break;
				}
			}
			
			if (chosenBitmask == null) {
				
				if (verbose) {
					
					for(Bitmask r : bitmasks) {
						printlnGreen(VERBOSE_PREFIX + "sizeInBytes: " + r.sizeInBytes
							+ " (" + r.sizeInBits + " bits) => defaultCpuMask: " + toString(r.defaultCpuMask)
							+ " (" + toBinaryString(r.defaultCpuMask) + ")"
							+ " procs=" + arrayToString(getProcsFromBitmask(r.defaultCpuMask, numberOfProcessors)));
					}
					
					System.out.println();
					
					printlnRed(VERBOSE_PREFIX + "Could not choose a bitmask size to use!"
							+ " numberOfProcessors=" + numberOfProcessors
							+ " sizeInBits=" + getSizeInBits(bitmasks));
				}
				
				throw new RuntimeException("Could not choose a bitmask size to use!"
						+ " numberOfProcessors=" + numberOfProcessors
						+ " sizeInBits=" + getSizeInBits(bitmasks));
			}
			
			if (verbose) System.out.println(VERBOSE_PREFIX + "Bitmask chosen: sizeInBytes=" + chosenBitmask.sizeInBytes
					+ " sizeInBits=" + chosenBitmask.sizeInBits + " defaultCpuMask=" + toString(chosenBitmask.defaultCpuMask)
					+ "-(" + toBinaryString(chosenBitmask.defaultCpuMask) + ")"
					+ " procs=" + arrayToString(getProcsFromBitmask(chosenBitmask.defaultCpuMask, numberOfProcessors)));
		}
		
		isInitialized = true;
		
		return isAvailable;
	}
	
	public static void printInfo() {
		
		System.out.println("isInitialized: " + isInitialized);
		System.out.println("isEnabled: " + isEnabled);
		System.out.println("isAvailable: " + isAvailable);

		String n;
		if (numberOfProcessors <= 0) {
			n = "NOT_AVAILABLE";
		} else if (numberOfProcessors == 1) {
			n = "1 (0)";
		} else {
			n = String.valueOf(numberOfProcessors) + " (0-" + (numberOfProcessors - 1) + ")";
		}
		
		System.out.println("numberOfProcessors: " + n);
		
		System.out.println("isHyperthreadingOn: " + (isHyperthreadingOn == null ? "UNKNOWN" : isHyperthreadingOn));
		
		String ic;
		if (isolcpus == null || numberOfProcessors <= 0) {
			ic = "NOT_AVAILABLE";
		} else if (isolcpus.length == 0) {
			ic = "NOT_DEFINED";
		} else {
			ic = arrayToString(isolcpus);
		}
		
		System.out.println("isolcpus: " + ic);
		
		String r;
		if (bitmasks == null) {
			r = "NOT_AVAILABLE";
		} else {
			r = String.valueOf(bitmasks.length);
		}
		
		System.out.println("bitmasksFound: " + r);
		
		System.out.println("areBitmasksEqual: " + (areBitmasksEqual == null ? false : areBitmasksEqual.booleanValue()));
		
		String c;
		if (chosenBitmask == null || numberOfProcessors <= 0) {
			c = "NOT_AVAILABLE";
		} else {
			c = "sizeInBytes=" + chosenBitmask.sizeInBytes
					+ " sizeInBits=" + chosenBitmask.sizeInBits + " defaultCpuMask=" + toString(chosenBitmask.defaultCpuMask)
					+ "-(" + toBinaryString(chosenBitmask.defaultCpuMask) + ")"
					+ " procs=" + arrayToString(getProcsFromBitmask(chosenBitmask.defaultCpuMask, numberOfProcessors));
		}
		
		System.out.println("chosenBitmaskSize: " + c);
		
		String a;
		if (chosenBitmask == null || numberOfProcessors <= 0) {
			a = "NOT_AVAILABLE";
		} else {
			a = toString(chosenBitmask.defaultCpuMask)
					+ "-(" + toBinaryString(chosenBitmask.defaultCpuMask) + ")"
					+ " procs=" + arrayToString(getProcsFromBitmask(chosenBitmask.defaultCpuMask, numberOfProcessors));
		}
		
		System.out.println("allowedCpusBitmask: " + a);
	}
	
	private static String getSizeInBits(Bitmask[] bitmasks) {
		StringBuilder sb = new StringBuilder();
		for(Bitmask bm : bitmasks) {
			if (sb.length() > 0) sb.append(",");
			sb.append(bm.sizeInBits);
		}
		return sb.toString();
	}
	
	private synchronized static boolean isAvailable(boolean verbose) {
		
		if (isAvailable == null) {
			
			String OS = System.getProperty("os.name").toLowerCase();
			boolean isLinux = OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
			if (!isLinux) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "CoralAffinity is only available on Linux!");
				isAvailable = false;
				return false;
			}
			
			File fileCpuInfo = new File("/proc/cpuinfo");
			if (!fileCpuInfo.exists()) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "Cannot find /proc/cpuinfo file!");
				isAvailable = false;
				return false;
			}
			
			File fileCmdLine = new File("/proc/cmdline");
			if (!fileCmdLine.exists()) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "Cannot find /proc/cmdline file!");
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
	
	public static boolean isAvailable() {
		return isAvailable(false);
	}
	
	public static boolean isEnabled() {
		return isEnabled;
	}
	
	public static boolean isInitialized() {
		return isInitialized;
	}
	
	public static int getNumberOfProcessors() {
		return numberOfProcessors;
	}
	
	public static int[] getIsolCpus() {
		return isolcpus;
	}
	
	public static Bitmask[] getBitmasks() {
		return bitmasks;
	}
	
	public static Bitmask getChosenBitmask() {
		return chosenBitmask;
	}
	
	public static boolean areBitmasksEqual() {
		if (areBitmasksEqual != null) {
			return areBitmasksEqual.booleanValue();
		}
		return false;
	}
	
	public static boolean isHyperthreadingOn() {
		if (isHyperthreadingOn != null) {
			return isHyperthreadingOn.booleanValue();
		}
		return false;
	}
	
	private static int getLogicalProcessors() {
		BufferedReader reader = null;
		Process process = null;
	    try {
	        process = Runtime.getRuntime().exec("grep -c ^processor /proc/cpuinfo");
	        reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
	        String line = reader.readLine();
	        return Integer.parseInt(line.trim());
	    } catch (Exception e) {
	        throw new RuntimeException("Cannot read number of processors from /proc/cpuinfo!", e);
	    } finally {
	    	if (reader != null) try { reader.close(); } catch(Exception e) { throw new RuntimeException(e); }
	    	if (process != null) try { process.destroyForcibly(); } catch(Exception e) { throw new RuntimeException(e); }
	    }
	}
	
	private static String readProcCmdline() {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/proc/cmdline"));
			return reader.readLine();
		} catch (Exception e) {
			throw new RuntimeException("Cannot read /proc/cmdline!", e);
		} finally {
			if (reader != null) try { reader.close(); } catch(Exception e) { throw new RuntimeException(e); }
		}
	}
	
	private static int[] getIsolcpusNumbers(final String cmdline) {
		
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
	
	public static long[] getBitmask(int ... bitsToSetToZero) {
		if (!isInitialized || !isAvailable()) {
			return null;
		}
		return getBitmask(bitsToSetToZero, numberOfProcessors);
	}
	
	private static void ensureValidBits(int[] bitsToSetToZero, int numberOfProcessors) {
		for(int i : bitsToSetToZero) {
			if (i < 0 || i >= numberOfProcessors) {
				throw new IllegalArgumentException("Invalid bit! bit=" + i + " numberOfProcessors=" + numberOfProcessors);
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
	
	static long[] getBitmask(int[] bitsToSetToOne, int numberOfProcessors) {
		
		ensureValidBits(bitsToSetToOne, numberOfProcessors);
		
		int numberOfLongs = (numberOfProcessors - 1) / 64 + 1;
		
		long[] bitmask = new long[numberOfLongs];
		
		for(int i = 0; i < bitmask.length; i++) {
			
	        long value = 0L;

	        for (int num : bitsToSetToOne) {
	            value |= (1L << num); // Set the bit at position num
	        }
	        
	        bitmask[i] = value;
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
	
	public static int[] getProcsFromBitmask(long bitmask) {
		if (!isInitialized || !isAvailable()) {
			return null;
		}
		return getProcsFromBitmask(bitmask, numberOfProcessors);
	}
	
	static int[] getProcsFromBitmask(long bitmask, int numberOfProcessors) {
		long[] array = new long[1];
		array[0] = bitmask;
		return getProcsFromBitmask(array, numberOfProcessors);
	}
	
	public static int[] getProcsFromBitmask(long[] bitmask) {
		if (!isInitialized || !isAvailable()) {
			return null;
		}
		return getProcsFromBitmask(bitmask, numberOfProcessors);
	}
	
	static int[] getProcsFromBitmask(long[] bitmask, int numberOfProcessors) {
		
		List<Integer> list = new ArrayList<Integer>(256);
		
		int remainingBits = numberOfProcessors;

		for(int i = 0; i < bitmask.length; i++) {
			
			long _64bitmask = bitmask[i];
			
			int sizeInBits;
			if (remainingBits - 64 >= 0) {
				sizeInBits = 64;
			} else {
				sizeInBits = remainingBits;
			}
			
			int[] procs = getSetBitPositions(_64bitmask, sizeInBits);
			
			int toAdd = i * 64;
			for(int proc : procs) {
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
	
	private static Bitmask[] scan(boolean verbose) {
		
		final CLibrary lib = Affinity.getLib();
		
		List<Bitmask> bitmasks = new ArrayList<Bitmask>(Pointer.ALL.size());
		
		if (verbose) System.out.println(VERBOSE_PREFIX + "Will begin scan: thread=" + Thread.currentThread().getName());
		
		for(Pointer p : Pointer.ALL) {
			
			if (verbose) System.out.print(VERBOSE_PREFIX + "Trying " + p.getClass().getSimpleName());
			
			try {
			
				int ret = lib.sched_getaffinity(0, p.getSizeInBytes(), p);
				
				if (ret >= 0) {
					Bitmask bm = new Bitmask();
					bm.sizeInBytes = p.getSizeInBytes();
					bm.defaultCpuMask = p.getValue();
					bm.sizeInBits = p.getSizeInBytes() * 8;
					bitmasks.add(bm);
					if (verbose) printlnGreen(" ---> SUCCESS: ret=" + ret 
							+ " defaultMask=" + toString(bm.defaultCpuMask)
							+ " (" + toBinaryString(bm.defaultCpuMask) + ")");
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
		
		Bitmask[] array = new Bitmask[bitmasks.size()];
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
		System.out.println(ANSI_GREEN + s + ANSI_RESET);
	}
	
	private static void printlnRed(String s) {
		System.out.println(ANSI_RED + s + ANSI_RESET);
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
	
	private static boolean equal(long[] a1, long[] a2) {
		if (a1.length != a2.length) return false;
		for(int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i]) return false;
		}
		return true;
	}
	
	private static boolean allEqual(Bitmask[] bitmasks) {
		long[] array = null;
		for(Bitmask r : bitmasks) {
			if (array == null) {
				array = r.defaultCpuMask;
			} else if (!equal(array, r.defaultCpuMask)) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		final boolean verbose = args.length > 0 ? Boolean.parseBoolean(args[0]) : true;
		
		if (verbose) System.out.println();
		
		CpuInfo.init(verbose);
		
		System.out.println();
		
		CpuInfo.printInfo();
		
		System.out.println();
	}
}