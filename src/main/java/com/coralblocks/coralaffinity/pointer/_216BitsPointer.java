package com.coralblocks.coralaffinity.pointer;

public class _216BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 27;

	public _216BitsPointer() {
		this(0L);
	}
	
	public _216BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		short s = getPointer().getShort(24);
		byte b = getPointer().getByte(26);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = (((long) b & 0xFFL) << 16) | (((long) s) & 0xFFFFL);
		return value;
	}
}