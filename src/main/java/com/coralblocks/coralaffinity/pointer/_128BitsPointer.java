package com.coralblocks.coralaffinity.pointer;

public class _128BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 16;

	public _128BitsPointer() {
		this(0L);
	}
	
	public _128BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public long[] getValue() {
		long[] value = new long[2];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		value[0] = l1;
		value[1] = l2;
		return value;
	}
}