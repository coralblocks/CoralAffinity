package com.coralblocks.coralaffinity.pointer;

public class _112BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 14;

	public _112BitsPointer() {
		this((long) 0, (int) 0, (short) 0);
	}
	
	public _112BitsPointer(long l, int i, short s) {
		super(SIZE_IN_BYTES);
		set(l, i, s);
	}
	
	@Override
	public void reset() {
		set((long) 0, (int) 0, (short) 0);
	}
	
	private void set(long l, int i, short s) {
		getPointer().setLong(0, l);
		getPointer().setInt(8, i);
		getPointer().setShort(12, s);
	}
	
	@Override
	public long getValue() {
		return getPointer().getLong(0);
	}
}