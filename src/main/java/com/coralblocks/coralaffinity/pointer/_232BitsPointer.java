package com.coralblocks.coralaffinity.pointer;

public class _232BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 29;

	public _232BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (int) 0, (byte) 0);
	}
	
	public _232BitsPointer(long l1, long l2, long l3, int i, byte b) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, i, b);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (long) 0, (int) 0, (byte) 0);
	}
	
	private void set(long l1, long l2, long l3, int i, byte b) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setInt(24, i);
		getPointer().setByte(28, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}