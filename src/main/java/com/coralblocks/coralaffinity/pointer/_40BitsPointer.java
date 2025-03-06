package com.coralblocks.coralaffinity.pointer;

public class _40BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 5;

	public _40BitsPointer() {
		this(0L);
	}
	
	public _40BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		int i = getPointer().getInt(0);
		byte b = getPointer().getByte(4);
		value[0] = (((long) b & 0xFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}