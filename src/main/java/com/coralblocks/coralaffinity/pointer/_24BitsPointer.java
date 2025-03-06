package com.coralblocks.coralaffinity.pointer;

public class _24BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 3;

	public _24BitsPointer() {
		this(0L);
	}
	
	public _24BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		short s = getPointer().getShort(0);
		byte b = getPointer().getByte(2);
		value[0] = (((long) b & 0xFFL) << 16) | (((long) s) & 0xFFFFL);
		return value;
	}
}