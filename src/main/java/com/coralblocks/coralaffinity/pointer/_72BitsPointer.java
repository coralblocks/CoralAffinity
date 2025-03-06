package com.coralblocks.coralaffinity.pointer;

public class _72BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 9;

	public _72BitsPointer() {
		this(0L);
	}
	
	public _72BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[2];
		long l = getPointer().getLong(0);
		byte b = getPointer().getByte(8);
		value[0] = l;
		value[1] = b & 0xFFL;
		return value;
	}
}