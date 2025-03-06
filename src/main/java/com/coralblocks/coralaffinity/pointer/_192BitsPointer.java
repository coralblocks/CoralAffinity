package com.coralblocks.coralaffinity.pointer;

public class _192BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 24;

	public _192BitsPointer() {
		this(0L);
	}
	
	public _192BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[3];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		return value;
	}
}