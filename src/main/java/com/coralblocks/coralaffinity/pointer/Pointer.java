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

import java.util.ArrayList;
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
	 * The list of all pointers, with a variety of sizes in bytes, from 8 to 512 bytes.
	 */
	public static final List<Pointer> ALL = new ArrayList<Pointer>(32);
	
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
	
	static {
		ALL.add(new _8BitsPointer());
		ALL.add(new _16BitsPointer());
		ALL.add(new _24BitsPointer());
		ALL.add(new _32BitsPointer());
		ALL.add(new _40BitsPointer());
		ALL.add(new _48BitsPointer());
		ALL.add(new _56BitsPointer());
		ALL.add(new _64BitsPointer());
		ALL.add(new _72BitsPointer());
		ALL.add(new _80BitsPointer());
		ALL.add(new _88BitsPointer());
		ALL.add(new _96BitsPointer());
		ALL.add(new _104BitsPointer());
		ALL.add(new _112BitsPointer());
		ALL.add(new _120BitsPointer());
		ALL.add(new _128BitsPointer());
		ALL.add(new _136BitsPointer());
		ALL.add(new _144BitsPointer());
		ALL.add(new _152BitsPointer());
		ALL.add(new _160BitsPointer());
		ALL.add(new _168BitsPointer());
		ALL.add(new _176BitsPointer());
		ALL.add(new _184BitsPointer());
		ALL.add(new _192BitsPointer());
		ALL.add(new _200BitsPointer());
		ALL.add(new _208BitsPointer());
		ALL.add(new _216BitsPointer());
		ALL.add(new _224BitsPointer());
		ALL.add(new _232BitsPointer());
		ALL.add(new _240BitsPointer());
		ALL.add(new _248BitsPointer());
		ALL.add(new _256BitsPointer());
		ALL.add(new _320BitsPointer());
		ALL.add(new _384BitsPointer());
		ALL.add(new _448BitsPointer());
		ALL.add(new _512BitsPointer());
		ALL.add(new _1024BitsPointer());
	}
}
