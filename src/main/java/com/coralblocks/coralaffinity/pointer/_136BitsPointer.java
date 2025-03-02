package com.coralblocks.coralaffinity.pointer;

public class _136BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 17;

	public _136BitsPointer() {
		this((long) 0, (long) 0, (byte) 0);
	}
	
	public _136BitsPointer(long l1, long l2, byte b) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setByte(16, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}