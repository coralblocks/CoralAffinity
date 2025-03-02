package com.coralblocks.coralaffinity.pointer;

public class _216BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 27;

	public _216BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (short) 0, (byte) 0);
	}
	
	public _216BitsPointer(long l1, long l2, long l3, short s, byte b) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, s, b);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (long) 0, (short) 0, (byte) 0);
	}
	
	private void set(long l1, long l2, long l3, short s, byte b) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setShort(24, s);
		getPointer().setByte(26, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}