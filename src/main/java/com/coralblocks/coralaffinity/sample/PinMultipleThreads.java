package com.coralblocks.coralaffinity.sample;

import java.util.Arrays;

import com.coralblocks.coralaffinity.Affinity;

/**
 * Pin some threads to a set of CPU processors.
 * 
 * This sample illustrates how you can pin to a single CPU processor (a single int) or to multiple ones (an int array).
 * 
 * When pinning a thread to multiple CPU processors, the kernel will decide to which one the thread ends up going.
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
