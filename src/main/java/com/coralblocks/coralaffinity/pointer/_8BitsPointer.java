package com.coralblocks.coralaffinity.pointer;

public class _8BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 1;

	public _8BitsPointer() {
		this(0L);
	}
	
	public _8BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		byte b = getPointer().getByte(0);
		value[0] = b & 0xFFL;
		return value;
	}
}