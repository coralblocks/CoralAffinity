package com.coralblocks.coralaffinity.pointer;

public class _80BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 10;

	public _80BitsPointer() {
		this(0L);
	}
	
	public _80BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
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