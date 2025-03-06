package com.coralblocks.coralaffinity.pointer;

public class _152BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 19;

	public _152BitsPointer() {
		this(0L);
	}
	
	public _152BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[3];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		short s = getPointer().getShort(16);
		byte b = getPointer().getByte(18);
		value[0] = l1;
		value[1] = l2;
		value[2] = (((long) b & 0xFFL) << 16) | (((long) s) & 0xFFFFL);
		return value;
	}
}