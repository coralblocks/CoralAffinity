package com.coralblocks.coralaffinity;

import java.util.ArrayList;
import java.util.List;

import com.coralblocks.coralaffinity.pointer.Pointer;
import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;

public class CpuMaskScanner {
	
	private interface CLibrary extends Library {

		public static final CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);
		
		public int sched_getaffinity(final int pid, final int cpusetsize, final PointerType cpuset) throws LastErrorException;
	}
	
	public static class Result {
		public int sizeInBytes;
		public long defaultCpuMask;
	}
	
	public List<Result> scan() {
		
		final CLibrary lib = CLibrary.INSTANCE;
		
		List<Result> results = new ArrayList<Result>(Pointer.ALL.size());
		
		for(Pointer p : Pointer.ALL) {
			
			int ret = lib.sched_getaffinity(0, p.getSizeInBytes(), p);
			
			if (ret >= 0) {
				Result result = new Result();
				result.sizeInBytes = p.getSizeInBytes();
				result.defaultCpuMask = p.getValue();
				results.add(result);
			}
		}
		
		return results;
	}
	
	public static void main(String[] args) {
		
		CpuMaskScanner scanner = new CpuMaskScanner();
		List<Result> results = scanner.scan();
		
		if (results.isEmpty()) {
			System.out.println("Could not find any cpu mask!");
			return;
		}
		
		for(Result r : results) {
			System.out.println("sizeInBytes: " + r.sizeInBytes + " / defaultCpuMask: " + r.defaultCpuMask);
		}
	}
}