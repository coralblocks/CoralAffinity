package com.coralblocks.coralaffinity.pointer;

public class _208BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 26;

	public _208BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (short) 0);
	}
	
	public _208BitsPointer(long l1, long l2, long l3, short s) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, s);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (long) 0, (short) 0);
	}
	
	private void set(long l1, long l2, long l3, short s) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setShort(24, s);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}