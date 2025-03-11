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

public abstract class Pointer extends ByReference implements CpuMask {
	
	private final int sizeInBytes;

	protected Pointer(int sizeInBytes) {
		super(sizeInBytes);
		this.sizeInBytes = sizeInBytes;
	}
	
	static byte[] splitLongIntoBytes(long value) {
	    byte[] result = new byte[8];
	    for (int i = 0; i < 8; i++) {
	        result[i] = (byte) ((value >> (8 * i)) & 0xFF);
	    }
	    return result;
	}
	
	static byte[] extractBytes(long[] longs, int numBytes) {
	    byte[] result = new byte[numBytes];
	    int bytesExtracted = 0;
	    
	    for (long l : longs) {
	        if (bytesExtracted >= numBytes) {
	            break;
	        }
	        byte[] bytes = splitLongIntoBytes(l);
	        for (int i = 0; i < 8 && bytesExtracted < numBytes; i++) {
	            result[bytesExtracted++] = bytes[i];
	        }
	    }
	    
	    return result;
	}
	
	public void set(long ... l) {
		byte[] bytes = extractBytes(l, sizeInBytes);
		int index = 0;
		for(byte b : bytes) {
			getPointer().setByte(index++, b);
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
	
	public void reset() {
		set(0L);
	}
	
	public static final List<Pointer> ALL = new ArrayList<Pointer>(32);
	
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