package com.coralblocks.coralaffinity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	public static class Result {
		public int sizeInBits;
		public int sizeInBytes;
		public long[] defaultCpuMask;
	}
	
	private static boolean isInitialized = false;
	private static int numberOfProcessors = -1;
	private static boolean isEnabled = true;
	private static Boolean isAvailable = null;
	private static int[] isolcpus = null;
	private static Result[] results = null;
	
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
			
			isolcpus = getIsolcpusNumbers(readProcCmdline());
			if (verbose) System.out.println(VERBOSE_PREFIX + "Isolcpus: " + (isolcpus != null ? Arrays.toString(isolcpus) : "NOT_DEFINED"));
			
			results = scan(verbose);
		}
		
		isInitialized = true;
		
		return isAvailable;
	}
	
	private synchronized static boolean isAvailable(boolean verbose) {
		
		if (isAvailable == null) {
			
			if (!isEnabled) {
				if (verbose) System.out.println(VERBOSE_PREFIX + "CoralAffinity is disabled!");
				isAvailable = false;
				return false;
			}
			
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
	
	public static Result[] getResults() {
		return results;
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
	        return -1;
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
            return null;
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
        
        int[] result = new int[cpuList.size()];
        for (int i = 0; i < cpuList.size(); i++) {
            result[i] = cpuList.get(i);
        }
        
        return result;
	}

	
	private static Result[] scan(boolean verbose) {
		
		final CLibrary lib = Affinity.getLib();
		
		List<Result> results = new ArrayList<Result>(Pointer.ALL.size());
		
		if (verbose) System.out.println(VERBOSE_PREFIX + "Will begin scan: thread=" + Thread.currentThread().getName());
		
		for(Pointer p : Pointer.ALL) {
			
			if (verbose) System.out.print(VERBOSE_PREFIX + "Trying " + p.getClass().getSimpleName());
			
			try {
			
				int ret = lib.sched_getaffinity(0, p.getSizeInBytes(), p);
				
				if (ret >= 0) {
					Result result = new Result();
					result.sizeInBytes = p.getSizeInBytes();
					result.defaultCpuMask = p.getValue();
					result.sizeInBits = p.getSizeInBytes() * 8;
					results.add(result);
					if (verbose) printGreen(VERBOSE_PREFIX + "SUCCESS: ret=" + ret 
							+ " defaultMask=" + toString(result.defaultCpuMask)
							+ " (" + toBinaryString(result.defaultCpuMask) + ")");
				} else {
					if (verbose) printlnRed(VERBOSE_PREFIX + "FAILURE: ret=" + ret);
				}
				
				
			} catch(Throwable t) {
				if (verbose) printlnRed(VERBOSE_PREFIX + "FAILURE: exception=\"" + t.getMessage() + "\"");
			}
		}
		
		if (verbose) {
			int n = results.size();
			if (n == 0) {
				printlnRed(VERBOSE_PREFIX + "Finished without finding any results!");
			} else {
				printGreen(VERBOSE_PREFIX + "Finished with " + n + " result" + (n > 1 ? "s!" : "!"));
			}
		}
		
		Result[] array = new Result[results.size()];
		return results.toArray(array);
	}
	
	private static void printGreen(String s) {
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
	
	private static String toBinaryString(long[] value) {
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
	
	private static boolean allEqual(Result[] results) {
		long[] array = null;
		for(Result r : results) {
			if (array == null) {
				array = r.defaultCpuMask;
			} else if (!equal(array, r.defaultCpuMask)) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		boolean verbose = true;
		
		if (args.length > 0) verbose = Boolean.parseBoolean(args[0]);
		
		boolean available = CpuInfo.init(verbose);
		
		if (!available) {
			printlnRed("\nCoralAffinity is not available!\n");
			return;
		}
		
		System.out.println();
		
		Result[] results = getResults();
		
		if (results.length == 0) {
			
			printlnRed("Could not find any cpu mask!");
			
		} else {
			
			int procs = getLogicalProcessors();
			int[] isolcpus = getIsolCpus();
			
			printGreen("RESULTS: allEqual=" + allEqual(results) 
						+ " numberOfProcessors=" + procs
						+ " isolcpus=" + (isolcpus.length != 0 ? Arrays.toString(isolcpus) : "NOT_DEFINED")
						+ "\n");
			
			for(Result r : results) {
				printGreen("sizeInBytes: " + r.sizeInBytes
						+ " (" + r.sizeInBits + " bits) => defaultCpuMask: " + toString(r.defaultCpuMask)
						+ " (" + toBinaryString(r.defaultCpuMask) + ")");
			}
		}
		
		System.out.println();
	}
}