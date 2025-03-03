package com.coralblocks.coralaffinity.pointer;

public class _240BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 30;

	public _240BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (int) 0, (short) 0);
	}
	
	public _240BitsPointer(long l1, long l2, long l3, int i, short s) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, i ,s);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (long) 0, (int) 0, (short) 0);
	}
	
	private void set(long l1, long l2, long l3, int i, short s) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setInt(24, i);
		getPointer().setShort(28, s);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		int i = getPointer().getInt(24);
		short s = getPointer().getShort(28);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}