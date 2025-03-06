package com.coralblocks.coralaffinity.pointer;

public class _224BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 28;

	public _224BitsPointer() {
		this(0L, 0L, 0L, (int) 0);
	}
	
	public _224BitsPointer(long l1, long l2, long l3, int i) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, i);
	}
	
	@Override
	public void reset() {
		set(0L, 0L, 0L, (int) 0);
	}
	
	private void set(long l1, long l2, long l3, int i) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setInt(24, i);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		int i = getPointer().getInt(24);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = i & 0xFFFFFFFFL;
		return value;
	}
}