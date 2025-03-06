package com.coralblocks.coralaffinity.pointer;

public class _320BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 40;

	public _320BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (long) 0, (long) 0);
	}
	
	public _320BitsPointer(long l1, long l2, long l3, long l4, long l5) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, l4, l5);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (long) 0, (long) 0, (long) 0);
	}
	
	private void set(long l1, long l2, long l3, long l4, long l5) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setLong(24, l4);
		getPointer().setLong(32, l5);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[5];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		long l4 = getPointer().getLong(24);
		long l5 = getPointer().getLong(32);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = l4;
		value[4] = l5;
		return value;
	}
}