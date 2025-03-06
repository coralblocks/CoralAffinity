package com.coralblocks.coralaffinity.pointer;

public class _88BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 11;

	public _88BitsPointer() {
		this(0L);
	}
	
	public _88BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[2];
		long l = getPointer().getLong(0);
		short s = getPointer().getShort(8);
		byte b = getPointer().getByte(10);
		value[0] = l;
		value[1] = (((long) b & 0xFFL) << 16) | (((long) s) & 0xFFFFL);
		return value;
	}
}