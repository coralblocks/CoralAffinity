package com.coralblocks.coralaffinity.pointer;

import java.util.ArrayList;
import java.util.List;

import com.coralblocks.coralaffinity.CpuMask;
import com.sun.jna.ptr.ByReference;

public abstract class Pointer extends ByReference implements CpuMask {
	
	private final int sizeInBytes;

	protected Pointer(int sizeInBytes) {
		super(sizeInBytes);
		this.sizeInBytes = sizeInBytes;
	}
	
	@Override
	public final int getSizeInBytes() {
		return sizeInBytes;
	}
	
	@Override
	public final int getSizeInBits() {
		return sizeInBytes * 8;
	}
	
	public static final List<Pointer> ALL = new ArrayList<Pointer>(32);
	
	static {
		ALL.add(new _8BitsPointer());
		ALL.add(new _16BitsPointer());
		ALL.add(new _24BitsPointer());
		ALL.add(new _32BitsPointer());
		ALL.add(new _40BitsPointer());
		ALL.add(new _48BitsPointer());
		ALL.add(new _56BitsPointer());
		ALL.add(new _64BitsPointer());
		ALL.add(new _72BitsPointer());
		ALL.add(new _80BitsPointer());
		ALL.add(new _88BitsPointer());
		ALL.add(new _96BitsPointer());
		ALL.add(new _104BitsPointer());
		ALL.add(new _112BitsPointer());
		ALL.add(new _120BitsPointer());
		ALL.add(new _128BitsPointer());
		ALL.add(new _136BitsPointer());
		ALL.add(new _144BitsPointer());
		ALL.add(new _152BitsPointer());
		ALL.add(new _160BitsPointer());
		ALL.add(new _168BitsPointer());
		ALL.add(new _176BitsPointer());
		ALL.add(new _184BitsPointer());
		ALL.add(new _192BitsPointer());
		ALL.add(new _200BitsPointer());
		ALL.add(new _208BitsPointer());
		ALL.add(new _216BitsPointer());
		ALL.add(new _224BitsPointer());
		ALL.add(new _232BitsPointer());
		ALL.add(new _240BitsPointer());
		ALL.add(new _248BitsPointer());
		ALL.add(new _256BitsPointer());
	}
}