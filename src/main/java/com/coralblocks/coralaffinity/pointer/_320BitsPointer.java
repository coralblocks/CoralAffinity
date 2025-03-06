package com.coralblocks.coralaffinity.pointer;

public class _320BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 40;

	public _320BitsPointer() {
		this(0L);
	}
	
	public _320BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[5];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		long l4 = getPointer().getLong(24);
		long l5 = getPointer().getLong(32);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = l4;
		value[4] = l5;
		return value;
	}
}