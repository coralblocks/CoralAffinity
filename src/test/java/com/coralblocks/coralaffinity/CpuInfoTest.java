package com.coralblocks.coralaffinity;

import org.junit.Assert;
import org.junit.Test;

import com.coralblocks.coralaffinity.CpuInfo.IntHolder;

public class CpuInfoTest {
	
	@Test
	public void testBitmasks() {
		
		final IntHolder numberOfProcessors = new IntHolder(16);
		
		String bits = "1111000011110";
		
		int[] procs = { 1,2,3,4,9,10,11,12 };
		
		long[] bitmasks = CpuInfo.getBitmask(numberOfProcessors, procs);
		
		Assert.assertEquals(1, bitmasks.length);
		
		Assert.assertEquals(7710, bitmasks[0]);
		
		Assert.assertEquals(bits, Long.toBinaryString(7710));
		
		long inverted = CpuInfo.invertBits(bitmasks[0], numberOfProcessors.getValue());
		
		Assert.assertEquals(57825, inverted);
		
		String invertedBits = "1110000111100001";
		
		Assert.assertEquals(invertedBits, Long.toBinaryString(inverted));
		
		int[] procsBack = CpuInfo.getProcsFromBitmask(numberOfProcessors, bitmasks[0]);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		long[] array = new long[2];
		array[0] = bitmasks[0];
		
		procsBack = CpuInfo.getProcsFromBitmask(numberOfProcessors, array);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[3];
		array[0] = bitmasks[0];
		
		procsBack = CpuInfo.getProcsFromBitmask(numberOfProcessors, array);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[4];
		array[0] = bitmasks[0];
		
		procsBack = CpuInfo.getProcsFromBitmask(numberOfProcessors, array);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		// Test passing extra bitmask
		
		array = new long[2];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(numberOfProcessors, array);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[3];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		array[2] = 0xFFFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(numberOfProcessors, array);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[4];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		array[2] = 0xFFFFL;
		array[3] = 0xFFFFFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(numberOfProcessors, array);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[5];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		array[2] = 0xFFFFL;
		array[3] = 0xFFFFFFL;
		array[4] = 0xFFFFFFFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(numberOfProcessors, array);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[6];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		array[2] = 0xFFFFL;
		array[3] = 0xFFFFFFL;
		array[4] = 0xFFFFFFFFL;
		array[5] = 0xFFFFFFFFFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(numberOfProcessors, array);
		
		Assert.assertArrayEquals(procs, procsBack);
	}
	
	@Test
	public void testBitsNoMultipleOf64() {
		
		String binary = "0011";
		
		long mask = Long.parseLong(binary, 2);
		
		int[] expected = new int[] { 0, 1 };
		
		int[] procs = CpuInfo.getProcsFromBitmask(new IntHolder(10), mask);
		
		Assert.assertArrayEquals(expected, procs);
		
		binary = "1100";
		
		mask = Long.parseLong(binary, 2);
		
		expected = new int[] { 2, 3 };
		
		procs = CpuInfo.getProcsFromBitmask(new IntHolder(11), mask);
		
		Assert.assertArrayEquals(expected, procs);
		
		// 64 bits just to test
		binary = "0010000000000000000000000001000000000000000000000000000000000010";
		
		mask = Long.parseLong(binary, 2);
		
		expected = new int[] { 1, 36 , 61 };
		
		procs = CpuInfo.getProcsFromBitmask(new IntHolder(101), mask);
		
		Assert.assertArrayEquals(expected, procs);
		
		// now two longs
		binary = "0010000000000000000000000001000000000000000000000000000000000010";
		String binary2 = "01101011";
		
		mask = Long.parseLong(binary, 2);
		long mask2 = Long.parseLong(binary2, 2);
		
		long[] doubleMask = new long[] { mask, mask2 };
		
		expected = new int[] { 1, 36 , 61 , 64, 65, 67, 69, 70 };
		
		procs = CpuInfo.getProcsFromBitmask(new IntHolder(150), doubleMask);
		
		Assert.assertArrayEquals(expected, procs);
	}
	
	@Test
	public void testInvertBits() {
		
		String s1 = "1010101010";
		
		long l1 = Long.parseLong(s1, 2);
		
		String s2 = Long.toBinaryString(l1);
		
		Assert.assertEquals(s1, s2);
		
		long l2 = CpuInfo.invertBits(l1, s1.length());
		
		String inverted = "0101010101";
		
		String s3 = Long.toBinaryString(l2);
		
		Assert.assertEquals(inverted, "0" + s3);
	}
	
	@Test
	public void testInvertBits2() {
		
		String s1 = "1010101010";
		
		long l1 = Long.parseLong(s1, 2);
		
		String s2 = Long.toBinaryString(l1);
		
		Assert.assertEquals(s1, s2);
		
		long l2 = CpuInfo.invertBits(l1, s1.length() + 2);
		
		String inverted = "110101010101";
		
		String s3 = Long.toBinaryString(l2);
		
		Assert.assertEquals(inverted, s3);
	}
	
	@Test
	public void testInvertBits3() {
		
		String s1 = "1010101010";
		
		long l1 = Long.parseLong(s1, 2);
		
		String s2 = Long.toBinaryString(l1);
		
		Assert.assertEquals(s1, s2);
		
		long l2 = CpuInfo.invertBits(l1, s1.length() - 2);
		
		String inverted = "01010101";
		
		String s3 = Long.toBinaryString(l2);
		
		Assert.assertEquals(inverted, "0" + s3);
	}
}