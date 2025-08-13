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
 * A thread spawned by another thread, which already has its affinity set, will be created with the same affinity
 * of the parent thread. You can actually use that to your advantage to spawn a third library thread with the affinity
 * that you want. You can do that by changing the affinity of the parent thread temporarily to spawn the new thread and
 * then switching the parent thread affinity back to its original value.
 */
public class ThreadSpawning3rdLibThread {
	
	public static void main(String[] args) {
		
		final int procIdMainThread = Integer.parseInt(args[0]);
		
		final Integer procIdSecondThread = args.length > 1 ? Integer.parseInt(args[1]) : null;
		
		System.out.println("Parent thread procId=" + procIdMainThread);
		if (procIdSecondThread == null) {
			System.out.println("No affinity will be set for the child thread");
		} else if (procIdSecondThread.intValue() == -1) {
			System.out.println("Child thread will use Affinity.setSchedulableCpus()");
		} else {
			System.out.println("Child thread procId=" + procIdSecondThread);
		}
		
		Thread parent = new Thread(new Runnable() {
			
			long count1 = 0;

			@Override
			public void run() {
				
				Affinity.set(procIdMainThread);
				
				System.out.println("Starting parent thread with affinity " + procIdMainThread + "...");
				
				while(true) {
					if (count1++ == 100_000_000) break; // run for a bit
				}
				
				System.out.println("Changing parent thread affinity to " + procIdSecondThread + "...");
				
				if (procIdSecondThread != null) {
					if (procIdSecondThread.intValue() == -1) {
						Affinity.setSchedulableCpus();
					} else {
						Affinity.set(procIdSecondThread.intValue());
					}
				}
				
				Thread child = new Thread(new Runnable() {
					
					long count2 = 0;

					@Override
					public void run() {
						
						System.out.println("===> The affinity of the child thread was set by inheritance to " 
												+ Arrays.toString(Affinity.get()));
						
						while(true) {
							count2 = (count2 == Long.MAX_VALUE ? 0 : count2 + 1);
						}
					}
					
				}, "ChildThread");
				
				child.start();
				
				System.out.println("Switching back parent thread affinity to " + procIdMainThread + "...");
				
				Affinity.set(procIdMainThread);
				
				while(true) {
					count1 = (count1 == Long.MAX_VALUE ? 0 : count1 + 1);
				}
				
			}
			
		}, "ParentThread");
		
		parent.start();
	}
}