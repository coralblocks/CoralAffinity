package com.coralblocks.coralaffinity.pointer;

public class _56BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 7;

	public _56BitsPointer() {
		this(0L);
	}
	
	public _56BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		int i = getPointer().getInt(0);
		short s = getPointer().getShort(4);
		byte b = getPointer().getByte(6);
		value[0] = (((long) b & 0xFFL) << 48) | (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}