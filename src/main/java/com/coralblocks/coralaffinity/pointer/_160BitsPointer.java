package com.coralblocks.coralaffinity.pointer;

public class _160BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 20;

	public _160BitsPointer() {
		this((long) 0, (long) 0, (int) 0);
	}
	
	public _160BitsPointer(long l1, long l2, int i) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setInt(16, i);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}