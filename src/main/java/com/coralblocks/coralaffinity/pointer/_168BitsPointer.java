package com.coralblocks.coralaffinity.pointer;

public class _168BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 21;

	public _168BitsPointer() {
		this(0L);
	}
	
	public _168BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[3];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		int i = getPointer().getInt(16);
		byte b = getPointer().getByte(20);
		value[0] = l1;
		value[1] = l2;
		value[2] = (((long) b & 0xFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}