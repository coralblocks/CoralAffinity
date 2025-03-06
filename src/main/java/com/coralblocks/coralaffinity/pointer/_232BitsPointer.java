package com.coralblocks.coralaffinity.pointer;

public class _232BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 29;

	public _232BitsPointer() {
		this(0L);
	}
	
	public _232BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		int i = getPointer().getInt(24);
		byte b = getPointer().getByte(28);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = (((long) b & 0xFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}