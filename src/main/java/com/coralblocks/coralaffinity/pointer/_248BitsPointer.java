package com.coralblocks.coralaffinity.pointer;

public class _248BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 31;

	public _248BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (int) 0, (short) 0, (byte) 0);
	}
	
	public _248BitsPointer(long l1, long l2, long l3, int i, short s, byte b) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setInt(24, i);
		getPointer().setShort(28, s);
		getPointer().setByte(30, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}