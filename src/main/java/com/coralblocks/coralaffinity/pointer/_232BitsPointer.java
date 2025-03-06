package com.coralblocks.coralaffinity.pointer;

public class _232BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 29;

	public _232BitsPointer() {
		this(0L, 0L, 0L, (int) 0, (byte) 0);
	}
	
	public _232BitsPointer(long l1, long l2, long l3, int i, byte b) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, i, b);
	}
	
	@Override
	public void reset() {
		set(0L, 0L, 0L, (int) 0, (byte) 0);
	}
	
	private void set(long l1, long l2, long l3, int i, byte b) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setInt(24, i);
		getPointer().setByte(28, b);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		int i = getPointer().getInt(24);
		byte b = getPointer().getByte(28);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = (((long) b & 0xFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}