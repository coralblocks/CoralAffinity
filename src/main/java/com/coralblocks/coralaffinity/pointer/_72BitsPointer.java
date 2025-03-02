package com.coralblocks.coralaffinity.pointer;

public class _72BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 9;

	public _72BitsPointer() {
		this((long) 0, (byte) 0);
	}
	
	public _72BitsPointer(long l, byte b) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l);
		getPointer().setByte(8, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}