package com.coralblocks.coralaffinity.pointer;

public class _512BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 64;

	public _512BitsPointer() {
		this(0L);
	}
	
	public _512BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[8];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		long l4 = getPointer().getLong(24);
		long l5 = getPointer().getLong(32);
		long l6 = getPointer().getLong(40);
		long l7 = getPointer().getLong(48);
		long l8 = getPointer().getLong(56);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = l4;
		value[4] = l5;
		value[5] = l6;
		value[6] = l7;
		value[7] = l8;
		return value;
	}
}