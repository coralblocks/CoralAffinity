package com.coralblocks.coralaffinity;

public interface CpuMask {
	
	public int getSizeInBits();
	
	public int getSizeInBytes();
	
	public long[] getValue();
	
}
