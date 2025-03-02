package com.coralblocks.coralaffinity.pointer;

public class _80BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 10;

	public _80BitsPointer() {
		this((long) 0, (short) 0);
	}
	
	public _80BitsPointer(long l, short s) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l);
		getPointer().setShort(8, s);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}