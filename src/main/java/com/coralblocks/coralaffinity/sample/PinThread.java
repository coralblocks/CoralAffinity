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
import com.coralblocks.coralaffinity.Affinity.SchedResult;

public class PinThread {
	
	public static void main(String[] args) throws Exception {
		
		final int procId = Integer.parseInt(args[0]);
		
		Thread t = new Thread(new Runnable() {

			long count = 0;
			
			@Override
            public void run() {

				SchedResult schedResult = Affinity.set(procId);
				
				if (schedResult.isOk()) {
					
					System.out.println("Thread pinned!" 
							+ " threadName="+ Thread.currentThread().getName() 
							+ " procId=" + procId);
				} else {
					
					System.out.println("Could not pin thread!"
							+ " threadName="+ Thread.currentThread().getName() 
							+ " procId=" + procId
							+ " schedResult=" + schedResult);
				}
				
				int[] procIds = Affinity.get();
				
				if (procIds != null) {
					
					System.out.println("Affinity.get() returned " + Arrays.toString(procIds)
					+ " for thread " + Thread.currentThread().getName());
					
				} else {
					
					System.out.println("Affinity.get() returned null for thread " + Thread.currentThread().getName());
				}
				
				while(true) {
					count = (count == Long.MAX_VALUE ? 0 : count + 1);
				}
            }
			
		}, "TestThread");

		t.start();
	}
}
