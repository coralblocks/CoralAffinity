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

import com.coralblocks.coralaffinity.Affinity;

/**
 * A thread spawned by another thread, which already has its affinity set, will be created with the same affinity
 * of the parent thread. To avoid this you can bind the child thread to another procId or use the convenient method
 * <code>Affinity.setSchedulableCpus()</code> to tell the kernel to schedule the child thread to any non-isolated CPU
 * processor. That allows a critical thread to spawn another thread that will not compete with it for the CPU processor.
 */
public class ThreadSpawningThread {
	
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
				
				Thread child = new Thread(new Runnable() {
					
					long count2 = 0;

					@Override
					public void run() {
						
						if (procIdSecondThread != null) {
							if (procIdSecondThread.intValue() == -1) {
								Affinity.setSchedulableCpus();
							} else {
								Affinity.set(procIdSecondThread.intValue());
							}
						}

						while(true) {
							count2 = (count2 == Long.MAX_VALUE ? 0 : count2 + 1);
						}
					}
					
				}, "ChildThread");
				
				child.start();
				
				while(true) {
					count1 = (count1 == Long.MAX_VALUE ? 0 : count1 + 1);
				}
				
			}
			
		}, "ParentThread");
		
		parent.start();
	}
}