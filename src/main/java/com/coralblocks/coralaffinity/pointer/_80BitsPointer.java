package com.coralblocks.coralaffinity.pointer;

public class _80BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 10;

	public _80BitsPointer() {
		this(0L, (short) 0);
	}
	
	public _80BitsPointer(long l, short s) {
		super(SIZE_IN_BYTES);
		set(l, s);
	}
	
	@Override
	public void reset() {
		set(0L, (short) 0);
	}
	
	private void set(long l, short s) {
		getPointer().setLong(0, l);
		getPointer().setShort(8, s);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[2];
		long l = getPointer().getLong(0);
		short s = getPointer().getShort(8);
		value[0] = l;
		value[1] = s & 0xFFFFL;
		return value;
	}
}