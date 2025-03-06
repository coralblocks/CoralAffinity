package com.coralblocks.coralaffinity.pointer;

public class _112BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 14;

	public _112BitsPointer() {
		this(0L);
	}
	
	public _112BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public long[] getValue() {
		long[] value = new long[2];
		long l = getPointer().getLong(0);
		int i = getPointer().getInt(8);
		short s = getPointer().getShort(12);
		value[0] = l;
		value[1] = (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}