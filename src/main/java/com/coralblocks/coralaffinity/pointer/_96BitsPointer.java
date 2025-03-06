package com.coralblocks.coralaffinity.pointer;

public class _96BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 12;

	public _96BitsPointer() {
		this(0L);
	}
	
	public _96BitsPointer(long ... l) {
		super(SIZE_IN_BYTES);
		set(l);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[2];
		long l = getPointer().getLong(0);
		int i = getPointer().getInt(8);
		value[0] = l;
		value[1] = i & 0xFFFFFFFFL;
		return value;
	}
}