package com.coralblocks.coralaffinity.pointer;

public class _136BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 17;

	public _136BitsPointer() {
		this((long) 0, (long) 0, (byte) 0);
	}
	
	public _136BitsPointer(long l1, long l2, byte b) {
		super(SIZE_IN_BYTES);
		set(l1, l2, b);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (byte) 0);
	}
	
	private void set(long l1, long l2, byte b) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setByte(16, b);
	}
	
	@Override
	public long[] getValue() {
		long[] value = new long[3];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		byte b = getPointer().getByte(16);
		value[0] = l1;
		value[1] = l2;
		value[2] = b & 0xFFL;
		return value;
	}
}