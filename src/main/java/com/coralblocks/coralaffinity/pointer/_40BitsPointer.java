package com.coralblocks.coralaffinity.pointer;

public class _40BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 5;

	public _40BitsPointer() {
		this((int) 0, (byte) 0);
	}
	
	public _40BitsPointer(int i, byte b) {
		super(SIZE_IN_BYTES);
		set(i, b);
	}
	
	@Override
	public void reset() {
		set((int) 0, (byte) 0);
	}
	
	private void set(int i, byte b) {
		getPointer().setInt(0, i);
		getPointer().setByte(4, b);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		int i = getPointer().getInt(0);
		byte b = getPointer().getByte(4);
		value[0] = (((long) b & 0xFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}