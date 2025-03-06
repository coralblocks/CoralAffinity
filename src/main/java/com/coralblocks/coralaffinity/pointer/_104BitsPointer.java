package com.coralblocks.coralaffinity.pointer;

public class _104BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 13;

	public _104BitsPointer() {
		this(0L);
	}
	
	public _104BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public long[] getValue() {
		long[] value = new long[2];
		long l = getPointer().getLong(0);
		int i = getPointer().getInt(8);
		byte b = getPointer().getByte(12);
		value[0] = l;
		value[1] = (((long) b & 0xFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value; 
	}
}