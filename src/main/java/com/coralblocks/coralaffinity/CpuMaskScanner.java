package com.coralblocks.coralaffinity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coralblocks.coralaffinity.pointer.Pointer;
import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;

public class CpuMaskScanner {
	
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
	
	private interface CLibrary extends Library {

		public static final CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);
		
		public int sched_getaffinity(final int pid, final int cpusetsize, final PointerType cpuset) throws LastErrorException;
	}
	
	public static class Result {
		public int sizeInBytes;
		public long defaultCpuMask;
	}
	
	private static void printGreen(String s) {
		System.out.println(ANSI_GREEN + s + ANSI_RESET);
	}
	
	private static void printlnRed(String s) {
		System.out.println(ANSI_RED + s + ANSI_RESET);
	}
	
	public List<Result> scan(boolean debug) {
		
		final CLibrary lib = CLibrary.INSTANCE;
		
		List<Result> results = new ArrayList<Result>(Pointer.ALL.size());
		
		for(Pointer p : Pointer.ALL) {
			
			if (debug) System.out.print("-----> Trying " + p.getClass().getSimpleName());
			
			try {
			
				int ret = lib.sched_getaffinity(0, p.getSizeInBytes(), p);
				
				if (ret >= 0) {
					Result result = new Result();
					result.sizeInBytes = p.getSizeInBytes();
					result.defaultCpuMask = p.getValue();
					results.add(result);
					if (debug) printGreen(" => SUCCESS: ret=" + ret 
							+ " defaultMask=" + result.defaultCpuMask
							+ " (" + Long.toBinaryString(result.defaultCpuMask) + ")");
				} else {
					if (debug) printlnRed(" => FAILURE: ret=" + ret);
				}
				
				
			} catch(Throwable t) {
				if (debug) printlnRed(" => FAILURE: exception=\"" + t.getMessage() + "\"");
			}
		}
		
		if (debug) {
			int n = results.size();
			if (n == 0) {
				printlnRed("-----> Finished without finding any results!");
			} else {
				printGreen("-----> Finished with " + n + " result" + (n > 1 ? "s!" : "!"));
			}
		}
		
		return results;
	}
	
	private static boolean allEqual(List<Result> results) {
		
		Long mask = null;
		
		for(Result r : results) {
			if (mask == null) {
				mask = r.defaultCpuMask;
			} else if (mask.longValue() != r.defaultCpuMask) {
				return false;
			}
		}
		
		return true;
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
	
	private static String getIsolcpus() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/proc/cmdline"));
			String cmdline = reader.readLine();
			
			Pattern pattern = Pattern.compile("\\bisolcpus=[^\\s]+");
	        Matcher matcher = pattern.matcher(cmdline);

	        if (matcher.find()) {
	        	return matcher.group();
	        }

	        return "isolcpus=NOT_DEFINED";
	        
		} catch(Exception e) {
			return "isolcpus=ERROR";
		} finally {
			if (reader != null) try { reader.close(); } catch(Exception e) { throw new RuntimeException(e); }
		}
	}
	
	public static void main(String[] args) {
		
		CpuMaskScanner scanner = new CpuMaskScanner();
		List<Result> results = scanner.scan(true);
		
		if (results.isEmpty()) {
			
			printlnRed("\nCould not find any cpu mask!");
			
		} else {
			
			int procs = getLogicalProcessors();
			
			printGreen("\nRESULTS: allEqual=" + allEqual(results) 
						+ " numberOfProcessors=" + procs
						+ " " + getIsolcpus()
						+ "\n");
			
			for(Result r : results) {
				printGreen("sizeInBytes: " + r.sizeInBytes
						+ " (" + r.sizeInBytes * 8 + " bits) => defaultCpuMask: " + r.defaultCpuMask
						+ " (" + Long.toBinaryString(r.defaultCpuMask) + ")");
			}
		}
		
		System.out.println();
	}
}