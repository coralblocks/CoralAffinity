package com.coralblocks.coralaffinity.pointer;

public class _112BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 14;

	public _112BitsPointer() {
		this(0L, (int) 0, (short) 0);
	}
	
	public _112BitsPointer(long l, int i, short s) {
		super(SIZE_IN_BYTES);
		set(l, i, s);
	}
	
	@Override
	public void reset() {
		set(0L, (int) 0, (short) 0);
	}
	
	private void set(long l, int i, short s) {
		getPointer().setLong(0, l);
		getPointer().setInt(8, i);
		getPointer().setShort(12, s);
	}
	
	@Override
	public long[] getValue() {
		long[] value = new long[2];
		long l = getPointer().getLong(0);
		int i = getPointer().getInt(8);
		short s = getPointer().getShort(12);
		value[0] = l;
		value[1] = (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}