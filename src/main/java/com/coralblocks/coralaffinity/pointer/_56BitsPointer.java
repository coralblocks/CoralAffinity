package com.coralblocks.coralaffinity.pointer;

public class _56BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 7;

	public _56BitsPointer() {
		this((int) 0, (short) 0, (byte) 0);
	}
	
	public _56BitsPointer(int i, short s, byte b) {
		super(SIZE_IN_BYTES);
		set(i, s, b);
	}
	
	@Override
	public void reset() {
		set((int) 0, (short) 0, (byte) 0);
	}
	
	private void set(int i, short s, byte b) {
		getPointer().setInt(0, i);
		getPointer().setShort(4, s);
		getPointer().setByte(6, b);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		int i = getPointer().getInt(0);
		short s = getPointer().getShort(4);
		byte b = getPointer().getByte(6);
		value[0] = (((long) b & 0xFFL) << 48) | (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}