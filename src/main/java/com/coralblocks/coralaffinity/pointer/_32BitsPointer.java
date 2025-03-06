package com.coralblocks.coralaffinity.pointer;

public class _32BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 4;

	public _32BitsPointer() {
		this(0L);
	}
	
	public _32BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		int i = getPointer().getInt(0);
		value[0] = i & 0xFFFFFFFFL;
		return value;
	}
}