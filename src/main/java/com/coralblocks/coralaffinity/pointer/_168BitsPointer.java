package com.coralblocks.coralaffinity.pointer;

public class _168BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 21;

	public _168BitsPointer() {
		this((long) 0, (long) 0, (int) 0, (byte) 0);
	}
	
	public _168BitsPointer(long l1, long l2, int i, byte b) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setInt(16, i);
		getPointer().setByte(20, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}