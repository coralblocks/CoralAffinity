package com.coralblocks.coralaffinity.pointer;

public class _512BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 64;

	public _512BitsPointer() {
		this(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
	}
	
	public _512BitsPointer(long l1, long l2, long l3, long l4, long l5, long l6, long l7, long l8) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, l4, l5, l6, l7, l8);
	}
	
	@Override
	public void reset() {
		set(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
	}
	
	private void set(long l1, long l2, long l3, long l4, long l5, long l6, long l7, long l8) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setLong(24, l4);
		getPointer().setLong(32, l5);
		getPointer().setLong(40, l6);
		getPointer().setLong(48, l7);
		getPointer().setLong(56, l8);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[8];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		long l4 = getPointer().getLong(24);
		long l5 = getPointer().getLong(32);
		long l6 = getPointer().getLong(40);
		long l7 = getPointer().getLong(48);
		long l8 = getPointer().getLong(56);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = l4;
		value[4] = l5;
		value[5] = l6;
		value[6] = l7;
		value[7] = l8;
		return value;
	}
}