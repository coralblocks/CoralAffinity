package com.coralblocks.coralaffinity.pointer;

public class _200BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 25;

	public _200BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (byte) 0);
	}
	
	public _200BitsPointer(long l1, long l2, long l3, byte b) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, b);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (long) 0, (byte) 0);
	}
	
	private void set(long l1, long l2, long l3, byte b) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setByte(24, b);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		byte b = getPointer().getByte(24);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = b & 0xFFL;
		return value;
	}
}