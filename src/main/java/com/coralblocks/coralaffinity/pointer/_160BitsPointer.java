package com.coralblocks.coralaffinity.pointer;

public class _160BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 20;

	public _160BitsPointer() {
		this((long) 0, (long) 0, (int) 0);
	}
	
	public _160BitsPointer(long l1, long l2, int i) {
		super(SIZE_IN_BYTES);
		set(l1, l2, i);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (int) 0);
	}
	
	private void set(long l1, long l2, int i) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setInt(16, i);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[3];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		int i = getPointer().getInt(16);
		value[0] = l1;
		value[1] = l2;
		value[2] = i & 0xFFFFFFFFL;
		return value;
	}
}