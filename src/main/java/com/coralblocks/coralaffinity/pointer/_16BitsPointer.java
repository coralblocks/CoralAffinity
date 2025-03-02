package com.coralblocks.coralaffinity.pointer;

public class _16BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 2;

	public _16BitsPointer() {
		this((short) 0);
	}
	
	public _16BitsPointer(short s) {
		super(SIZE_IN_BYTES);
		getPointer().setShort(0, s);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getShort(0);
	}
}