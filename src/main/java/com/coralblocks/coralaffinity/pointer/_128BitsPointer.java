package com.coralblocks.coralaffinity.pointer;

public class _128BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 16;

	public _128BitsPointer() {
		this(0L, 0L);
	}
	
	public _128BitsPointer(long l1, long l2) {
		super(SIZE_IN_BYTES);
		set(l1, l2);
	}

	@Override
	public void reset() {
		set(0L, 0L);
	}
	
	private void set(long l1, long l2) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
	}
	
	@Override
	public long[] getValue() {
		long[] value = new long[2];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		value[0] = l1;
		value[1] = l2;
		return value;
	}
}