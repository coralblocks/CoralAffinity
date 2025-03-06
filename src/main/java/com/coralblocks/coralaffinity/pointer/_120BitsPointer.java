package com.coralblocks.coralaffinity.pointer;

public class _120BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 15;

	public _120BitsPointer() {
		this((long) 0, (int) 0, (short) 0, (byte) 0);
	}
	
	public _120BitsPointer(long l, int i, short s, byte b) {
		super(SIZE_IN_BYTES);
		set(l, i, s, b);
	}
	
	@Override
	public void reset() {
		set((long) 0, (int) 0, (short) 0, (byte) 0);
	}
	
	private void set(long l, int i, short s, byte b) {
		getPointer().setLong(0, l);
		getPointer().setInt(8, i);
		getPointer().setShort(12, s);
		getPointer().setByte(14, b);
	}
	
	@Override
	public long[] getValue() {
		long[] value = new long[2];
		long l = getPointer().getLong(0);
		int i = getPointer().getInt(8);
		short s = getPointer().getShort(12);
		byte b = getPointer().getByte(14);
		value[0] = l;
		value[1] = (((long) b & 0xFFL) << 48) | (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}