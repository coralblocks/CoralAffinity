package com.coralblocks.coralaffinity.pointer;

public class _240BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 30;

	public _240BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (int) 0, (short) 0);
	}
	
	public _240BitsPointer(long l1, long l2, long l3, int i, short s) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setInt(24, i);
		getPointer().setShort(28, s);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}