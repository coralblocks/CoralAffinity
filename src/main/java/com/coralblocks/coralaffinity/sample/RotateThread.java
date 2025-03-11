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
import com.coralblocks.coralaffinity.CpuInfo;

public class RotateThread {
	
	public static void main(String[] args) throws Exception {
		
		class TestThread extends Thread {
		
			private int procId;
			
			public TestThread(String name, int procId) {
				super(name);
				this.procId = procId;
			}
			
			public synchronized void changeProcId(int newProcId) {
				this.procId = newProcId;
			}
			
			@Override
			public void run() {
				
				Affinity.set(procId);

				int currProcToBind = procId;
				
				while(true) {
					synchronized(this) {
						if (currProcToBind != procId) {
							Affinity.set(procId);
							currProcToBind = procId;
						}
					}
				}
			}
		}
		
		boolean useIsolated = args.length > 0 ? Boolean.parseBoolean(args[0]) : false;
		
		int[] procIds = useIsolated ? CpuInfo.getIsolatedCpus() : CpuInfo.getNonIsolatedCpus();
		
		System.out.println("List of procIds => " 
						+ Arrays.toString(procIds) 
						+ " (" + (useIsolated ? "isolatedCpus" : "nonIsolatedCpus") + ")");
		
		if (procIds == null || procIds.length == 0) {
			System.out.println("Nothing to rotate, list of procIds is empty!");
			return;
		}
		
		TestThread t = new TestThread("TestThread", procIds[0]);
		t.start();
		
		for(int i = 1; i < procIds.length; i++) {
			
			Thread.sleep(10000);
		
			int procId = procIds[i];
			
			System.out.println("Changing thread to processor " + procId + "...");
			
			t.changeProcId(procId);
			
			if (i == procIds.length - 1) i = -1; // back to start...
		}
	}
}
