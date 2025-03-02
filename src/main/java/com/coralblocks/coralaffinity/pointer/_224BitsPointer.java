package com.coralblocks.coralaffinity.pointer;

public class _224BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 28;

	public _224BitsPointer() {
		this((long) 0, (long) 0, (long) 0, (int) 0);
	}
	
	public _224BitsPointer(long l1, long l2, long l3, int i) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, i);
	}
	
	@Override
	public void reset() {
		set((long) 0, (long) 0, (long) 0, (int) 0);
	}
	
	private void set(long l1, long l2, long l3, int i) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setInt(24, i);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}