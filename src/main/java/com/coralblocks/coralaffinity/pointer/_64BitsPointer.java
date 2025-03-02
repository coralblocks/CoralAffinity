package com.coralblocks.coralaffinity.pointer;

public class _64BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 8;

	public _64BitsPointer() {
		this((long) 0);
	}
	
	public _64BitsPointer(long l) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}