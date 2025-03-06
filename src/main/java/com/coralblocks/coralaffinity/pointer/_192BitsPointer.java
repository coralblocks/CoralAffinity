package com.coralblocks.coralaffinity.pointer;

public class _192BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 24;

	public _192BitsPointer() {
		this((long) 0, (long) 0, (long) 0);
	}
	
	public _192BitsPointer(long l1, long l2, long l3) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (long) 0);
	}
	
	private void set(long l1, long l2, long l3) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[3];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		return value;
	}
}