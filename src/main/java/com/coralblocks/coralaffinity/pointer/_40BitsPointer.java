package com.coralblocks.coralaffinity.pointer;

public class _40BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 5;

	public _40BitsPointer() {
		this((int) 0, (byte) 0);
	}
	
	public _40BitsPointer(int i, byte b) {
		super(SIZE_IN_BYTES);
		set(i, b);
	}
	
	@Override
	public void reset() {
		set((int) 0, (byte) 0);
	}
	
	private void set(int i, byte b) {
		getPointer().setInt(0, i);
		getPointer().setByte(4, b);
	}
	
	@Override
	public final long getValue() {
		return getPointer().getInt(0);
	}
}