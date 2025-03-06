package com.coralblocks.coralaffinity.pointer;

public class _384BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 48;

	public _384BitsPointer() {
		this(0L);
	}
	
	public _384BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[6];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		long l4 = getPointer().getLong(24);
		long l5 = getPointer().getLong(32);
		long l6 = getPointer().getLong(40);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = l4;
		value[4] = l5;
		value[5] = l6;
		return value;
	}
}