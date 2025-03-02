package com.coralblocks.coralaffinity.pointer;

public class _24BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 3;

	public _24BitsPointer() {
		this((short) 0, (byte) 0);
	}
	
	public _24BitsPointer(short s, byte b) {
		super(SIZE_IN_BYTES);
		getPointer().setShort(0, s);
		getPointer().setByte(2, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getShort(0);
	}
}