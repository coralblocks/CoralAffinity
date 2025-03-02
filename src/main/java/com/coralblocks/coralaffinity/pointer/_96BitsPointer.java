package com.coralblocks.coralaffinity.pointer;

public class _96BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 12;

	public _96BitsPointer() {
		this((long) 0, (int) 0);
	}
	
	public _96BitsPointer(long l, int i) {
		super(SIZE_IN_BYTES);
		set(l, i);
	}
	
	@Override
	public void reset() {
		set((long) 0, (int) 0);
	}
	
	private void set(long l, int i) {
		getPointer().setLong(0, l);
		getPointer().setInt(8, i);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getLong(0);
	}
}