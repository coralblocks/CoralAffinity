package com.coralblocks.coralaffinity.pointer;

import com.coralblocks.coralaffinity.CpuMask;
import com.sun.jna.ptr.ByReference;

public abstract class Pointer extends ByReference implements CpuMask {
	
	private final int sizeInBytes;

	protected Pointer(int sizeInBytes) {
		super(sizeInBytes);
		this.sizeInBytes = sizeInBytes;
	}
	
	@Override
	public final int getSizeInBytes() {
		return sizeInBytes;
	}
}