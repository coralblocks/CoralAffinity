package com.coralblocks.coralaffinity.pointer;

public class _184BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 23;

	public _184BitsPointer() {
		this((long) 0, (long) 0, (int) 0, (short) 0, (byte) 0);
	}
	
	public _184BitsPointer(long l1, long l2, int i, short s, byte b) {
		super(SIZE_IN_BYTES);
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setInt(16, i);
		getPointer().setShort(20, s);
		getPointer().setByte(22, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}