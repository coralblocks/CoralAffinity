package com.coralblocks.coralaffinity;

import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;

public class Affinity {
	
	static interface CLibrary extends Library {

		public static final CLibrary INSTANCE = (CLibrary) Native.load("c", CLibrary.class);
		
		public int sched_getaffinity(final int pid, final int cpusetsize, final PointerType cpuset) throws LastErrorException;
	}
	
	public static CLibrary getLib() {
		return CLibrary.INSTANCE;
	}
}