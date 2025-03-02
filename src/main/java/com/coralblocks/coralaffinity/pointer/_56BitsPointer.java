package com.coralblocks.coralaffinity.pointer;

public class _56BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 7;

	public _56BitsPointer() {
		this((int) 0, (short) 0, (byte) 0);
	}
	
	public _56BitsPointer(int i, short s, byte b) {
		super(SIZE_IN_BYTES);
		getPointer().setInt(0, i);
		getPointer().setShort(4, s);
		getPointer().setByte(6, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getInt(0);
	}
}