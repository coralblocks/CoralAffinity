package com.coralblocks.coralaffinity.pointer;

public class _208BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 26;

	public _208BitsPointer() {
		this(0L);
	}
	
	public _208BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		short s = getPointer().getShort(24);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = s & 0xFFFFL;
		return value;
	}
}