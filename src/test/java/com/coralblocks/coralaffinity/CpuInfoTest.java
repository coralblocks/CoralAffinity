package com.coralblocks.coralaffinity;

import org.junit.Assert;
import org.junit.Test;

public class CpuInfoTest {
	
	@Test
	public void testBitmasks() {
		
		final int numberOfProcessors = 16;
		
		int[] procs = { 1,2,3,4,9,10,11,12 };
		
		long[] bitmasks = CpuInfo.getBitmask(procs, numberOfProcessors);
		
		Assert.assertEquals(1, bitmasks.length);
		
		Assert.assertEquals(57825, bitmasks[0]);
		
		int[] procsBack = CpuInfo.getProcsFromBitmask(bitmasks[0], numberOfProcessors);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		long[] array = new long[2];
		array[0] = bitmasks[0];
		
		procsBack = CpuInfo.getProcsFromBitmask(array, numberOfProcessors);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[3];
		array[0] = bitmasks[0];
		
		procsBack = CpuInfo.getProcsFromBitmask(array, numberOfProcessors);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[4];
		array[0] = bitmasks[0];
		
		procsBack = CpuInfo.getProcsFromBitmask(array, numberOfProcessors);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		// Test passing extra bitmask
		
		array = new long[2];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(array, numberOfProcessors);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[3];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		array[2] = 0xFFFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(array, numberOfProcessors);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[4];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		array[2] = 0xFFFFL;
		array[3] = 0xFFFFFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(array, numberOfProcessors);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[5];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		array[2] = 0xFFFFL;
		array[3] = 0xFFFFFFL;
		array[4] = 0xFFFFFFFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(array, numberOfProcessors);
		
		Assert.assertArrayEquals(procs, procsBack);
		
		array = new long[6];
		array[0] = bitmasks[0];
		array[1] = 0xFFL;
		array[2] = 0xFFFFL;
		array[3] = 0xFFFFFFL;
		array[4] = 0xFFFFFFFFL;
		array[5] = 0xFFFFFFFFFFL;
		
		procsBack = CpuInfo.getProcsFromBitmask(array, numberOfProcessors);
		
		Assert.assertArrayEquals(procs, procsBack);
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