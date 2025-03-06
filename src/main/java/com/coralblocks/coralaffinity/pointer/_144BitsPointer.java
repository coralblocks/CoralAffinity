package com.coralblocks.coralaffinity.pointer;

public class _144BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 18;

	public _144BitsPointer() {
		this(0L, 0L, (short) 0);
	}
	
	public _144BitsPointer(long l1, long l2, short s) {
		super(SIZE_IN_BYTES);
		set(l1, l2, s);
	}
	
	@Override
	public void reset() {
		set(0L, 0L, (short) 0);
	}
	
	private void set(long l1, long l2, short s) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setShort(16, s);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[3];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		short s = getPointer().getShort(16);
		value[0] = l1;
		value[1] = l2;
		value[2] = s & 0xFFFFL;
		return value;
	}
}