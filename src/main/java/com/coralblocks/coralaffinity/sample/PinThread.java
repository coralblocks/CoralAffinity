package com.coralblocks.coralaffinity.sample;

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
				
				while(true) {
					count = (count == Long.MAX_VALUE ? 0 : count + 1);
				}
            }
			
		}, "TestThread");

		t.start();
	}
}
