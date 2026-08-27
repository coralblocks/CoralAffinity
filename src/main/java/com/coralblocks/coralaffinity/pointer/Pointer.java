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
package com.coralblocks.coralaffinity.pointer;

import java.util.List;

import com.coralblocks.coralaffinity.CpuMask;
import com.sun.jna.ptr.ByReference;

/**
 * A JNA pointer to be used to set and get the CPU affinity bitmask. 
 */
public abstract class Pointer extends ByReference implements CpuMask {
	
	private final int sizeInBytes;

	protected Pointer(int sizeInBytes) {
		super(sizeInBytes);
		this.sizeInBytes = sizeInBytes;
	}
	
	/**
	 * Set the pointer bitmask with the given longs in native byte order.
	 * 
	 * <p>For example: [0,5,6,7,8,13,14,15] =&gt; 57825 (1110000111100001)
	 * 
	 * @param l the bitmask as a list of longs
	 */
	public void set(long ... l) {
		int offset = 0;
		int valueIndex = 0;
		while(offset < sizeInBytes) {
			int bytesToWrite = Math.min(Long.BYTES, sizeInBytes - offset);
			long value = valueIndex < l.length ? l[valueIndex] : 0L;
			writeNative(value, offset, bytesToWrite);
			offset += bytesToWrite;
			valueIndex++;
		}
	}

	private void writeNative(long value, int offset, int bytesToWrite) {
		if (bytesToWrite == Long.BYTES) {
			getPointer().setLong(offset, value);
			return;
		}
		if (bytesToWrite >= Integer.BYTES) {
			getPointer().setInt(offset, (int) value);
			value >>>= Integer.SIZE;
			offset += Integer.BYTES;
			bytesToWrite -= Integer.BYTES;
		}
		if (bytesToWrite >= Short.BYTES) {
			getPointer().setShort(offset, (short) value);
			value >>>= Short.SIZE;
			offset += Short.BYTES;
			bytesToWrite -= Short.BYTES;
		}
		if (bytesToWrite == Byte.BYTES) {
			getPointer().setByte(offset, (byte) value);
		}
	}
	
	@Override
	public final int getSizeInBytes() {
		return sizeInBytes;
	}
	
	@Override
	public final int getSizeInBits() {
		return sizeInBytes * 8;
	}
	
	/**
	 * Reset the pointer bitmask, in other words, set it to 0L (all bits will be set to zero).
	 */
	public void reset() {
		set(0L);
	}
	
	/**
	 * The unmodifiable list of all pointers, with a variety of sizes in bytes, from 8 to 512 bytes.
	 */
	public static final List<Pointer> ALL = List.of(
			new _8BitsPointer(),
			new _16BitsPointer(),
			new _24BitsPointer(),
			new _32BitsPointer(),
			new _40BitsPointer(),
			new _48BitsPointer(),
			new _56BitsPointer(),
			new _64BitsPointer(),
			new _72BitsPointer(),
			new _80BitsPointer(),
			new _88BitsPointer(),
			new _96BitsPointer(),
			new _104BitsPointer(),
			new _112BitsPointer(),
			new _120BitsPointer(),
			new _128BitsPointer(),
			new _136BitsPointer(),
			new _144BitsPointer(),
			new _152BitsPointer(),
			new _160BitsPointer(),
			new _168BitsPointer(),
			new _176BitsPointer(),
			new _184BitsPointer(),
			new _192BitsPointer(),
			new _200BitsPointer(),
			new _208BitsPointer(),
			new _216BitsPointer(),
			new _224BitsPointer(),
			new _232BitsPointer(),
			new _240BitsPointer(),
			new _248BitsPointer(),
			new _256BitsPointer(),
			new _320BitsPointer(),
			new _384BitsPointer(),
			new _448BitsPointer(),
			new _512BitsPointer(),
			new _1024BitsPointer());
	
	/**
	 * Get the pointer for the given size in bytes.
	 * 
	 * @param sizeInBytes the size in bytes
	 * @return the pointer for that size in bytes
	 */
	public static final Pointer get(int sizeInBytes) {
		for(Pointer p : ALL) {
			if (p.getSizeInBytes() == sizeInBytes) return p;
		}
		return null;
	}
}
