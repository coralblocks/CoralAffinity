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

import java.util.Arrays;

import com.coralblocks.coralaffinity.pointer.Pointer;
import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;

/**
 * The main singleton class used to set the thread affinity. 
 */
public class Affinity {
	
	/**
	 * This class represents the result of a call to the native method <code>sched_setaffinity</code>. 
	 */
	public static class SchedResult {
		
		/**
		 * The status of the call. The only good/success status is <code>OK</code>. 
		 */
		public static enum Status { OK, 
									NOT_LINUX, 
									NOT_ENABLED, 
									NOT_INITIALIZED, 
									NOT_AVAILABLE, 
									RET_VALUE_NEGATIVE, 
									EXCEPTION, 
									BAD_ARGUMENT }
		
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
		
		/**
		 * Returns true if the call was successful.
		 * 
		 * @return true if sucessful
		 */
		public boolean isOk() {
			return status == Status.OK;
		}
		
		/**
		 * Returns the status of the call
		 * 
		 * @return the status of the call
		 */
		public Status getStatus() {
			return status;
		}
		
		/**
		 * If the status for the call was <code>EXCEPTION</code> then you can
		 * use this method to get the exception that was thrown.
		 * 
		 * @return the exception thrown by the call
		 */
		public Throwable getException() {
			return exception;
		}

		@Override
		public String toString() {
			if (status == Status.EXCEPTION) {
				StringBuilder sb = new StringBuilder();
				sb.append("EXCEPTION=\"").append(exception.getMessage()).append("\"");
				return sb.toString();
			} else {
				return status.toString();
			}
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
	
	/**
	 * Sets the thread affinity of the calling thread to be any schedulable CPU logical processor which is not isolated
	 * from the kernel scheduler. This method is useful when a thread which is already pinned to an isolated CPU logical
	 * processor wants to spawn a new thread but does not want this new thread to be pinned to the same isolated CPU logical
	 * processor, which is what happens by default.
	 * 
	 * @return a {@see SchedResult} object with the result of the call
	 */
	public synchronized static final SchedResult setSchedulableCpus() {
		
		SchedResult schedResult = check();
		if (schedResult != null) return schedResult;
		
		int[] nonIsolatedCpus = CpuInfo.getNonIsolatedCpus();
		
		return set(nonIsolatedCpus);
	}
	
	/**
	 * Returns the affinity bitmask of the current thread as a list of integers
	 * corresponding to the CPU logical processors.
	 * 
	 * Note that it can be just one, for the case that the thread was pinned to a single CPU logical processor.
	 * Also note that in Linux the thread affinity can never be empty (i.e. bitmask zero), in other words,
	 * this method can never return an empty array.
	 * 
	 * @return the list of CPU logical processors from the affinity bitmask
	 */
	public synchronized static final int[] get() {
		
		SchedResult schedResult = check();
		if (schedResult != null) return null;
		
		int sizeInBytes = CpuInfo.getChosenCpuBitmaskSizeInBits() / 8;
		
		Pointer p = Pointer.get(sizeInBytes);
		p.reset();
		
		final CLibrary lib = Affinity.getLib();
		
		try {
			
			int ret = lib.sched_getaffinity(0, p.getSizeInBytes(), p);
			
			if (ret >= 0) {
				return CpuInfo.getProcIdsFromCpuBitmask(CpuInfo.getNumberOfProcessorsHolder(), p.getValue());
			} else {
				return null;
			}
			
		} catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	/**
	 * Sets the thread affinity of the calling thread to be the given list of CPU logical processors.
	 * Of course the list can contain only a single CPU processor id, to pin a critical thread to a single
	 * isolated CPU core.
	 * 
	 * @param procIds the affinity you want to set as a list of CPU logical processor ids (can be just a single id)
	 * @return a {@see ShedResult} object with the result of the call
	 */
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
	
	/**
	 * Returns the JNA native library to be used for the native calls.
	 * 
	 * @return the JNA native library
	 */
	public static CLibrary getLib() {
		return CLibrary.INSTANCE;
	}
}