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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class PointerTest {

	@Test
	public void testAllIsUnmodifiable() {
		Assert.assertThrows(UnsupportedOperationException.class, () -> Pointer.ALL.clear());
	}

	@Test
	public void testSetUsesNativeByteOrder() {
		long value = 0x1122334455667788L;
		Pointer pointer = Pointer.get(Long.BYTES);
		pointer.set(value);
		
		byte[] expected = ByteBuffer.allocate(Long.BYTES).order(ByteOrder.nativeOrder()).putLong(value).array();
		Assert.assertArrayEquals(expected, pointer.getPointer().getByteArray(0, Long.BYTES));
	}

	@Test
	public void testSetAndGetValueRoundTripForAllPointerSizes() {
		long[] values = new long[16];
		for(int i = 0; i < values.length; i++) {
			values[i] = 0x0123456789ABCDEFL ^ (0x1111111111111111L * i);
		}
		
		for(Pointer pointer : Pointer.ALL) {
			pointer.set(values);

			int chunks = (pointer.getSizeInBytes() + Long.BYTES - 1) / Long.BYTES;
			long[] expected = Arrays.copyOf(values, chunks);
			int trailingBytes = pointer.getSizeInBytes() % Long.BYTES;
			if (trailingBytes != 0) {
				expected[chunks - 1] &= (1L << (trailingBytes * Byte.SIZE)) - 1;
			}

			Assert.assertArrayEquals(pointer.getClass().getSimpleName(), expected, pointer.getValue());
		}
	}
}
