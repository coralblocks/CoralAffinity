package com.coralblocks.coralaffinity;

import java.util.Arrays;

import com.coralblocks.coralaffinity.pointer.Pointer;
import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;

public class Affinity {
	
	public static class SchedResult {
		
		public static enum Status { OK, NOT_LINUX, NOT_ENABLED, NOT_INITIALIZED, NOT_AVAILABLE, RET_VALUE_NEGATIVE, EXCEPTION, BAD_ARGUMENT }
		
		private final Status status;
		private final Throwable exception;
		
		SchedResult(Status status) {
			this(status, null);
			if (status == Status.EXCEPTION) throw new IllegalArgumentException();
		}
		
		SchedResult(Throwable exception) {
			this(Status.EXCEPTION, exception);
		}
		
		SchedResult(Status status, Throwable exception) {
			this.status = status;
			this.exception = exception;
		}
		
		public boolean isOk() {
			return status == Status.OK;
		}
		
		public Status getStatus() {
			return status;
		}
		
		public Throwable getException() {
			return exception;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[status=").append(status);
			if (status == Status.EXCEPTION) {
				sb.append(", exception=\"").append(exception.getMessage()).append("\"]");
			} else {
				sb.append("]");
			}
			return sb.toString();
		}
	}
	
	private static final SchedResult SCHED_RESULT_OK = new SchedResult(SchedResult.Status.OK);
	private static final SchedResult SCHED_RESULT_NOT_LINUX = new SchedResult(SchedResult.Status.NOT_LINUX);
	private static final SchedResult SCHED_RESULT_NOT_AVAILABLE = new SchedResult(SchedResult.Status.NOT_AVAILABLE);
	private static final SchedResult SCHED_RESULT_NOT_ENABLED = new SchedResult(SchedResult.Status.NOT_ENABLED);
	private static final SchedResult SCHED_RESULT_RET_VALUE_NEGATIVE = new SchedResult(SchedResult.Status.RET_VALUE_NEGATIVE);
	private static final SchedResult SCHED_RESULT_BAD_ARGUMENT = new SchedResult(SchedResult.Status.BAD_ARGUMENT);
	private static final SchedResult SCHED_RESULT_NOT_INITIALIZED = new SchedResult(SchedResult.Status.NOT_INITIALIZED);
	
	private Affinity() {
		
	}
	
	private static SchedResult check() {
		
		if (!CpuInfo.isLinux()) {
			return SCHED_RESULT_NOT_LINUX;
		}
		
		if (!CpuInfo.isEnabled()) {
			return SCHED_RESULT_NOT_ENABLED;
		}
		
		if (!CpuInfo.isInitialized()) {
			return SCHED_RESULT_NOT_INITIALIZED;
		}
		
		if (!CpuInfo.isAvailable()) {
			return SCHED_RESULT_NOT_AVAILABLE;
		}
		
		return null;
	}
	
	private static SchedResult checkProcIds(int[] procIds) {
		
		if (procIds == null || procIds.length == 0) return SCHED_RESULT_BAD_ARGUMENT;
		
		for(int i : procIds) {
			if (i < 0 || i >= CpuInfo.getNumberOfProcessors()) {
				return SCHED_RESULT_BAD_ARGUMENT;
			}
		}
		
		return null;
	}
	
	public synchronized static final SchedResult setIsolatedCpus() {
		
		SchedResult schedResult = check();
		if (schedResult != null) return schedResult;
		
		int[] isolcpus = CpuInfo.getIsolatedCpus();
		
		return set(isolcpus);
	}
	
	public synchronized static final SchedResult setNonIsolatedCpus() {
		
		SchedResult schedResult = check();
		if (schedResult != null) return schedResult;
		
		int[] nonIsolatedCpus = CpuInfo.getNonIsolatedCpus();
		
		return set(nonIsolatedCpus);
	}
	
	public synchronized static final SchedResult set(int ... procIds) {
		
		SchedResult schedResult = check();
		if (schedResult != null) return schedResult;
		
		schedResult = checkProcIds(procIds);
		if (schedResult != null) return schedResult;
		
		int sizeInBytes = CpuInfo.getChosenCpuBitmaskSizeInBits() / 8;
		
		Pointer pointer = Pointer.get(sizeInBytes);
		if (pointer == null) { // should never happen
			throw new IllegalStateException("Got a null pointer for sizeInBytes: " + sizeInBytes);
		}
		
		long[] cpuBitmask = CpuInfo.getCpuBitmaskFromProcIds(procIds);
		if (cpuBitmask == null || cpuBitmask.length == 0) { // should never happen
			throw new IllegalStateException("Got an invalid cpuBitMask: " + Arrays.toString(procIds));
		}
		
		pointer.reset();
		pointer.set(cpuBitmask);
		
		try {
			
			final CLibrary lib = getLib();
		
			int retValue = lib.sched_setaffinity(0, sizeInBytes, pointer);
			
			if (retValue < 0) return SCHED_RESULT_RET_VALUE_NEGATIVE;
			
			return SCHED_RESULT_OK;
			
		} catch(Throwable t) {
			
			return new SchedResult(t);
		}
	}
	
	static interface CLibrary extends Library {

		public static final CLibrary INSTANCE = (CLibrary) Native.load("c", CLibrary.class);
		
		public int sched_getaffinity(final int pid, final int cpusetsize, final PointerType cpuset) throws LastErrorException;
		
		public int sched_setaffinity(final int pid, final int cpusetsize, final PointerType cpuset) throws LastErrorException;
	}
	
	public static CLibrary getLib() {
		return CLibrary.INSTANCE;
	}
}