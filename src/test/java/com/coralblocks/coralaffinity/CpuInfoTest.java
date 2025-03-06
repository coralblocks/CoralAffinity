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
}