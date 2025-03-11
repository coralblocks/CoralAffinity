/* 
 * Copyright 2015-2025 (c) CoralBlocks LLC - http://www.coralblocks.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.coralblocks.coralaffinity;

/**
 * The contract of a CPU bitmask. 
 */
public interface CpuMask {
	
	/**
	 * Returns the size of this CPU bitmask in bits.
	 * 
	 * @return the size in bits
	 */
	public int getSizeInBits();
	
	/**
	 * Returns the size of this CPU bitmask in bytes.
	 * 
	 * @return the sizer in bytes
	 */
	public int getSizeInBytes();
	
	/**
	 * Returns the value of this CPU bitmask as a list of longs
	 * 
	 * @return the value as a list of longs
	 */
	public long[] getValue();
	
}
