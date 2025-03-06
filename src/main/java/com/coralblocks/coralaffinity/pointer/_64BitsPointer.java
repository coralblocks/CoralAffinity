package com.coralblocks.coralaffinity.pointer;

public class _64BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 8;

	public _64BitsPointer() {
		this(0L);
	}
	
	public _64BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		long l = getPointer().getLong(0);
		value[0] = l;
		return value;
	}
}