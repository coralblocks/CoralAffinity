package com.coralblocks.coralaffinity.pointer;

public class _48BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 6;

	public _48BitsPointer() {
		this((int) 0, (short) 0);
	}
	
	public _48BitsPointer(int i, short s) {
		super(SIZE_IN_BYTES);
		getPointer().setInt(0, i);
		getPointer().setShort(4, s);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getInt(0);
	}
}