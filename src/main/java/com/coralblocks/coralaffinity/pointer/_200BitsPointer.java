package com.coralblocks.coralaffinity.pointer;

public class _200BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 25;

	public _200BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (byte) 0);
	}
	
	public _200BitsPointer(long l1, long l2, long l3, byte b) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setByte(24, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}