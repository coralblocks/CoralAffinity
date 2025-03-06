package com.coralblocks.coralaffinity.pointer;

public class _32BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 4;

	public _32BitsPointer() {
		this((int) 0);
	}
	
	public _32BitsPointer(int i) {
		super(SIZE_IN_BYTES);
		set(i);
	}
	
	@Override
	public void reset() {
		set((int) 0);
	}
	
	private void set(int i) {
		getPointer().setInt(0, i);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[1];
		int i = getPointer().getInt(0);
		value[0] = i & 0xFFFFFFFFL;
		return value;
	}
}