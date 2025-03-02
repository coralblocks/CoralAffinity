package com.coralblocks.coralaffinity.pointer;

public class _88BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 11;

	public _88BitsPointer() {
		this((long) 0, (short) 0, (byte) 0);
	}
	
	public _88BitsPointer(long l, short s, byte b) {
		super(SIZE_IN_BYTES);
		set(l, s, b);
	}
	
	@Override
	public void reset() {
		set((long) 0, (short) 0, (byte) 0);
	}
	
	private void set(long l, short s, byte b) {
		getPointer().setLong(0, l);
		getPointer().setShort(8, s);
		getPointer().setByte(10, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}