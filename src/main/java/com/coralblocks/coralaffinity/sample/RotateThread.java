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
		
		boolean useIsolatedCpus = args.length > 0 ? Boolean.parseBoolean(args[0]) : false;
		
		int[] procIds = useIsolatedCpus ? CpuInfo.getIsolatedCpus() : CpuInfo.getNonIsolatedCpus();
		
		System.out.println("List of procIds to rotate => " + Arrays.toString(procIds));
		
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
