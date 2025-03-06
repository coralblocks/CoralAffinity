package com.coralblocks.coralaffinity.pointer;

public class _136BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 17;

	public _136BitsPointer() {
		this(0L);
	}
	
	public _136BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public long[] getValue() {
		long[] value = new long[3];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		byte b = getPointer().getByte(16);
		value[0] = l1;
		value[1] = l2;
		value[2] = b & 0xFFL;
		return value;
	}
}