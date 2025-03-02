package com.coralblocks.coralaffinity.pointer;

public class _32BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 4;

	public _32BitsPointer() {
		this((int) 0);
	}
	
	public _32BitsPointer(int i) {
		super(SIZE_IN_BYTES);
		getPointer().setInt(0, i);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getInt(0);
	}
}