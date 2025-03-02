package com.coralblocks.coralaffinity.pointer;

public class _72BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 9;

	public _72BitsPointer() {
		this((long) 0, (byte) 0);
	}
	
	public _72BitsPointer(long l, byte b) {
		super(SIZE_IN_BYTES);
		set(l, b);
	}
	
	@Override
	public void reset() {
		set((long) 0, (byte) 0);
	}
	
	private void set(long l, byte b) {
		getPointer().setLong(0, l);
		getPointer().setByte(8, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}