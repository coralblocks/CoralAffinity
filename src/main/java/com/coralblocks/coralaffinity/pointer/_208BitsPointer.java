package com.coralblocks.coralaffinity.pointer;

public class _208BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 26;

	public _208BitsPointer() {
		this(0L, 0L, 0L, (short) 0);
	}
	
	public _208BitsPointer(long l1, long l2, long l3, short s) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, s);
	}
	
	@Override
	public void reset() {
		set(0L, 0L, 0L, (short) 0);
	}
	
	private void set(long l1, long l2, long l3, short s) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setShort(24, s);
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