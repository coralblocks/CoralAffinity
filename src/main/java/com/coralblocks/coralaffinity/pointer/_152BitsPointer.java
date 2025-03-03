package com.coralblocks.coralaffinity.pointer;

public class _152BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 19;

	public _152BitsPointer() {
		this((long) 0, (long) 0, (short) 0, (byte) 0);
	}
	
	public _152BitsPointer(long l1, long l2, short s, byte b) {
		super(SIZE_IN_BYTES);
		set(l1, l2, s, b);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (short) 0, (byte) 0);
	}
	
	private void set(long l1, long l2, short s, byte b) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setShort(16, s);
		getPointer().setByte(18, b);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		short s = getPointer().getShort(16);
		byte b = getPointer().getByte(18);
		value[0] = l1;
		value[1] = l2;
		value[2] = (((long) b & 0xFFL) << 16) | (((long) s) & 0xFFFFL);
		return value;
	}
}