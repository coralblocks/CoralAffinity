package com.coralblocks.coralaffinity.pointer;

public class _256BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 32;

	public _256BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (long) 0);
	}
	
	public _256BitsPointer(long l1, long l2, long l3, long l4) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setLong(24, l4);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}