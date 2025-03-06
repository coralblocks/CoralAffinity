package com.coralblocks.coralaffinity.pointer;

public class _120BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 15;

	public _120BitsPointer() {
		this(0L);
	}
	
	public _120BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public long[] getValue() {
		long[] value = new long[2];
		long l = getPointer().getLong(0);
		int i = getPointer().getInt(8);
		short s = getPointer().getShort(12);
		byte b = getPointer().getByte(14);
		value[0] = l;
		value[1] = (((long) b & 0xFFL) << 48) | (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}