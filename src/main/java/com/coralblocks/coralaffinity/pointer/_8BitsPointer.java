package com.coralblocks.coralaffinity.pointer;

public class _8BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 1;

	public _8BitsPointer() {
		this((byte) 0);
	}
	
	public _8BitsPointer(byte b) {
		super(SIZE_IN_BYTES);
		getPointer().setByte(0, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getByte(0);
	}
}