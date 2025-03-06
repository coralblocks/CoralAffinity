package com.coralblocks.coralaffinity.pointer;

import org.junit.Assert;
import org.junit.Test;

public class PointerTest {
	
	@Test
	public void testSplitLong() {
		
		String _64bits = "0010000000000000000000000001000000000000000000000000000000000010";
		
		String _8bits_0 = "00000010";
		String _8bits_1 = "00000000";
		String _8bits_2 = "00000000";
		String _8bits_3 = "00000000";
		String _8bits_4 = "00010000";
		String _8bits_5 = "00000000";
		String _8bits_6 = "00000000";
		String _8bits_7 = "00100000";
		
		byte l1 = Byte.parseByte(_8bits_0, 2);
		byte l2 = Byte.parseByte(_8bits_1, 2);
		byte l3 = Byte.parseByte(_8bits_2, 2);
		byte l4 = Byte.parseByte(_8bits_3, 2);
		byte l5 = Byte.parseByte(_8bits_4, 2);
		byte l6 = Byte.parseByte(_8bits_5, 2);
		byte l7 = Byte.parseByte(_8bits_6, 2);
		byte l8 = Byte.parseByte(_8bits_7, 2);
		
		byte[] expected = { l1, l2, l3, l4, l5, l6, l7, l8 };
		
		long mask = Long.parseLong(_64bits, 2);
		
		byte[] result = Pointer.splitLongIntoBytes(mask);
		
		Assert.assertArrayEquals(expected, result);
	}
}