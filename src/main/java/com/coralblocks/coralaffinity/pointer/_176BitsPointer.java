package com.coralblocks.coralaffinity.pointer;

public class _176BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 22;

	public _176BitsPointer() {
		this((long) 0, (long) 0, (int) 0, (short) 0);
	}
	
	public _176BitsPointer(long l1, long l2, int i, short s) {
		super(SIZE_IN_BYTES);
		set(l1, l2, i ,s);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (int) 0, (short) 0);
	}
	
	private void set(long l1, long l2, int i, short s) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setInt(16, i);
		getPointer().setShort(20, s);		
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[3];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		int i = getPointer().getInt(16);
		short s = getPointer().getShort(20);
		value[0] = l1;
		value[1] = l2;
		value[2] = (((long) s & 0xFFFFL) << 32) | (((long) i) & 0xFFFFFFFFL);
		return value;
	}
}