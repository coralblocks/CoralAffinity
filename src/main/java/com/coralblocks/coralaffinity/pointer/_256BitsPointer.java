package com.coralblocks.coralaffinity.pointer;

public class _256BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 32;

	public _256BitsPointer() {
		this(0L);
	}
	
	public _256BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		long l4 = getPointer().getLong(24);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = l4;
		return value;
	}
}