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
package com.coralblocks.coralaffinity.sample;

import java.util.Arrays;

import com.coralblocks.coralaffinity.Affinity;

/**
 * Pin some threads to a set of CPU processors.
 * 
 * <p>This sample illustrates how you can pin to a single CPU processor (a single int) or to multiple ones (an int array).</p>
 * 
 * <p>When pinning a thread to multiple CPU processors, the kernel scheduler will decide to which one the thread ends up going.</p>
 * 
 * <p>It is interesting to note that if you only pass isolated CPU processors (defined through the <i>isolcpus</i> Linux configuration)
 * as procIds, the kernel scheduler will not schedule anything and assign all threads to the first (smallest) processor id in the list.</p>
 * 
 * <p>It is also interesting to note that if you pass schedulable (i.e. non-isolated) CPU processors together with isolated CPU processors,
 * the kernel scheduler will ignore the isolated ones and only schedule the threads to run on the schedulable (i.e. non-isolated) CPU processors,
 * ignoring the isolated ones in the list.</p>
 */
public class PinMultipleThreads {
	
	public static void main(String[] args) throws Exception {
		
		int numberOfThreads = Integer.parseInt(args[0]);
		
		final int[] procIds = parseIntArray(args[1]); // one procId or multiple ones
		
		System.out.println("Creating " + numberOfThreads + " threads... procIds=" + Arrays.toString(procIds));
		
		Thread[] t = new Thread[numberOfThreads];
		
		for(int i = 0 ; i < t.length; i++) {
			
			t[i] = new Thread(new Runnable() {

				long count = 0;
				
				@Override
	            public void run() {

					Affinity.set(procIds);
					
					while(true) {
						count = (count == Long.MAX_VALUE ? 0 : count + 1);
					}
	            }
				
			}, "TestThread" + i);
			
			System.out.println("Starting thread " + i + "...");
			
			t[i].start();
		}
	}
	
	private static int[] parseIntArray(String intArrayString) {
	    String[] parts = intArrayString.split(",");
	    int[] result = new int[parts.length];
	    for (int i = 0; i < parts.length; i++) {
	        result[i] = Integer.parseInt(parts[i]);
	    }
	    return result;
	}
}
