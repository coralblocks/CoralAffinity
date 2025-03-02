package com.coralblocks.coralaffinity.pointer;

public class _144BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 18;

	public _144BitsPointer() {
		this((long) 0, (long) 0, (short) 0);
	}
	
	public _144BitsPointer(long l1, long l2, short s) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setShort(16, s);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}