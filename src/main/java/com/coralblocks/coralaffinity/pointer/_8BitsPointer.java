package com.coralblocks.coralaffinity.pointer;

public class _8BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 1;

	public _8BitsPointer() {
		this((byte) 0);
	}
	
	public _8BitsPointer(byte b) {
		super(SIZE_IN_BYTES);
		set(b);
	}
	
	@Override
	public void reset() {
		set((byte) 0);
	}
	
	private void set(byte b) {
		getPointer().setByte(0, b);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		byte b = getPointer().getByte(0);
		value[0] = b & 0xFFL;
		return value;
	}
}