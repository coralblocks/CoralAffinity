package com.coralblocks.coralaffinity.pointer;

public class _16BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 2;

	public _16BitsPointer() {
		this((short) 0);
	}
	
	public _16BitsPointer(short s) {
		super(SIZE_IN_BYTES);
		set(s);
	}
	
	@Override
	public void reset() {
		set((short) 0);
	}
	
	private void set(short s) {
		getPointer().setShort(0, s);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		short s = getPointer().getShort(0);
		value[0] = s & 0xFFFFL;
		return value;
	}
}