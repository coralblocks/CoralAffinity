package com.coralblocks.coralaffinity.pointer;

public class _184BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 23;

	public _184BitsPointer() {
		this((long) 0, (long) 0, (int) 0, (short) 0, (byte) 0);
	}
	
	public _184BitsPointer(long l1, long l2, int i, short s, byte b) {
		super(SIZE_IN_BYTES);
		set(l1, l2, i, s, b);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (int) 0, (short) 0, (byte) 0);
	}
	
	private void set(long l1, long l2, int i, short s, byte b) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setInt(16, i);
		getPointer().setShort(20, s);
		getPointer().setByte(22, b);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[4];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		int i = getPointer().getInt(16);
		short s = getPointer().getShort(20);
		byte b = getPointer().getByte(22);
		value[0] = l1;
		value[1] = l2;
		value[2] = (((long) b & 0xFFL) << 48) | (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}