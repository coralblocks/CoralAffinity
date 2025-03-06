package com.coralblocks.coralaffinity.pointer;

public class _16BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 2;

	public _16BitsPointer() {
		this(0L);
	}
	
	public _16BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		short s = getPointer().getShort(0);
		value[0] = s & 0xFFFFL;
		return value;
	}
}