package com.coralblocks.coralaffinity.pointer;

public class _48BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 6;

	public _48BitsPointer() {
		this((int) 0, (short) 0);
	}
	
	public _48BitsPointer(int i, short s) {
		super(SIZE_IN_BYTES);
		set(i, s);
	}
	
	@Override
	public void reset() {
		set((int) 0, (short) 0);
	}
	
	private void set(int i, short s) {
		getPointer().setInt(0, i);
		getPointer().setShort(4, s);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		int i = getPointer().getInt(0);
		short s = getPointer().getShort(4);
		value[0] = (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}