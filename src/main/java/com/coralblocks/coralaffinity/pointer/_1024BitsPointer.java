package com.coralblocks.coralaffinity.pointer;

public class _1024BitsPointer extends Pointer {
	
	public static final int SIZE_IN_BYTES = 128;

	public _1024BitsPointer() {
		this(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
	}
	
	public _1024BitsPointer(long l1, long l2, long l3, long l4, long l5, long l6, long l7, long l8, long l9, long l10, long l11, long l12, long l13, long l14, long l15, long l16) {
		super(SIZE_IN_BYTES);
		set(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15, l16);
	}
	
	@Override
	public void reset() {
		set(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
	}
	
	private void set(long l1, long l2, long l3, long l4, long l5, long l6, long l7, long l8, long l9, long l10, long l11, long l12, long l13, long l14, long l15, long l16) {
		getPointer().setLong(0, l1);
		getPointer().setLong(8, l2);
		getPointer().setLong(16, l3);
		getPointer().setLong(24, l4);
		getPointer().setLong(32, l5);
		getPointer().setLong(40, l6);
		getPointer().setLong(48, l7);
		getPointer().setLong(56, l8);
		getPointer().setLong(64, l9);
		getPointer().setLong(72, l10);
		getPointer().setLong(80, l11);
		getPointer().setLong(88, l12);
		getPointer().setLong(96, l13);
		getPointer().setLong(104, l14);
		getPointer().setLong(112, l15);
		getPointer().setLong(120, l16);
	}
	
	@Override
	public final long[] getValue() {
		long[] value = new long[16];
		long l1 = getPointer().getLong(0);
		long l2 = getPointer().getLong(8);
		long l3 = getPointer().getLong(16);
		long l4 = getPointer().getLong(24);
		long l5 = getPointer().getLong(32);
		long l6 = getPointer().getLong(40);
		long l7 = getPointer().getLong(48);
		long l8 = getPointer().getLong(56);
		long l9 = getPointer().getLong(64);
		long l10 = getPointer().getLong(72);
		long l11 = getPointer().getLong(80);
		long l12 = getPointer().getLong(88);
		long l13 = getPointer().getLong(96);
		long l14 = getPointer().getLong(104);
		long l15 = getPointer().getLong(112);
		long l16 = getPointer().getLong(120);
		value[0] = l1;
		value[1] = l2;
		value[2] = l3;
		value[3] = l4;
		value[4] = l5;
		value[5] = l6;
		value[6] = l7;
		value[7] = l8;
		value[8] = l9;
		value[9] = l10;
		value[10] = l11;
		value[11] = l12;
		value[12] = l13;
		value[13] = l14;
		value[14] = l15;
		value[15] = l16;
		return value;
	}
}