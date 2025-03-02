package com.coralblocks.coralaffinity.pointer;

public class _128BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 16;

	public _128BitsPointer() {
		this((long) 0, (long) 0);
	}
	
	public _128BitsPointer(long l1, long l2) {
		super(SIZE_IN_BYTES);
		set(l1, l2);
	}

	@Override
	public void reset() {
		set((long) 0, (long) 0);
	}
	
	private void set(long l1, long l2) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
	}
	
	@Override
	public long getValue() {
		return getPointer().getLong(0);
	}
}