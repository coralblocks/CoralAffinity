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
	public final long getValue() {
		return getPointer().getLong(0);
	}
}