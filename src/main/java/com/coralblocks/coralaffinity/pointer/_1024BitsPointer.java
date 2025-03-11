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

public class _1024BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 128;

	public _1024BitsPointer() {
		this(0L);
	}
	
	public _1024BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[16];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		long l4 = getPointer().getLong(24);
		long l5 = getPointer().getLong(32);
		long l6 = getPointer().getLong(40);
		long l7 = getPointer().getLong(48);
		long l8 = getPointer().getLong(56);
		long l9 = getPointer().getLong(64);
		long l10 = getPointer().getLong(72);
		long l11 = getPointer().getLong(80);
		long l12 = getPointer().getLong(88);
		long l13 = getPointer().getLong(96);
		long l14 = getPointer().getLong(104);
		long l15 = getPointer().getLong(112);
		long l16 = getPointer().getLong(120);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = l4;
		value[4] = l5;
		value[5] = l6;
		value[6] = l7;
		value[7] = l8;
		value[8] = l9;
		value[9] = l10;
		value[10] = l11;
		value[11] = l12;
		value[12] = l13;
		value[13] = l14;
		value[14] = l15;
		value[15] = l16;
		return value;
	}
}