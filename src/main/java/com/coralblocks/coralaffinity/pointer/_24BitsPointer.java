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

public class _24BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 3;

	public _24BitsPointer() {
		this(0L);
	}
	
	public _24BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		short s = getPointer().getShort(0);
		byte b = getPointer().getByte(2);
		value[0] = (((long) b & 0xFFL) << 16) | (((long) s) & 0xFFFFL);
		return value;
	}
}