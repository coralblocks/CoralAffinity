# CoralAffinity

Comprehensive and straightforward Linux thread affinity in Java so that you can pin your critical threads to isolated CPU cores for maximum performance. To pin a thread to a CPU logical processor id (or CPU core id) all you have to do is:

```java
int procId = 1;
Affinity.set(procId);
```

You can also set the affinity to more than one processor id and the kernel will decide where to pin the thread:

```java
int[] procIds = { 1, 2, 3 };
Affinity.set(procIds);
```

## Features

If you run the included script `./bin/cpuInfo.sh` you get:

<pre>
$ ./bin/cpuInfo.sh
java -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.CpuInfo

isEnabled: true
isAvailable: true
numberOfProcessors: 16 => procIds=0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
isHyperthreadingOn: true
hyperthreadedPairs: [0, 8], [1, 9], [2, 10], [3, 11], [4, 12], [5, 13], [6, 14], [7, 15]
cpuBitmasksFound: 9 => 64, 128, 192, 256, 320, 384, 448, 512, 1024 (in bits)
chosenCpuBitmaskSize: 64 bits
nonIsolatedCpuBitmask: 57825 (1110000111100001) => procIds=0,5,6,7,8,13,14,15
isolatedCpuBitmask: 7710 (1111000011110) => procIds=1,2,3,4,9,10,11,12
</pre>

- _isEnabled_: You can easily disable CoralAffinity during testing and development through `-DcoralAffinityEnabled=false` or `export coralAffinityEnabled=false`.

- _isAvailable_: CoralAffinity automatically checks if it is available and can run in the machine it is being used. If it is not available your code will still run without any problems, like if CoralAffinity was disabled.

- _numberOfProcessors_: The number of logical processors in the machine. If the machine is using hyperthreading, the number of logical processors will be twice the number of physical CPU cores.

- _isHyperthreadingOn_: If the machine is using hyperthreading, each physical CPU core will be able to run two threads at the same time.

- _hyperthreadedPairs_: With hyperthreading, each physical CPU core will have two logical processors. These are the pairs of logical processors per CPU core.

- _cpuBitmasksFound_: The different sizes (in bits) that can be used for the CPU affinity mask. CoralAffinity supports up to 1024 logical processors and can be easily extended to support more.

- _chosenCpuBitmaskSize_: The chosen CPU affinity mask size (in bits) to be used by CoralAffinity. This will always be greather or equal the number of logical processors, as each processor is represented by one bit.

- _nonIsolatedCpuBitmask_: The CPU mask and the processor ids that are _not isolated_ from the kernel scheduler. If no thread affinity is set, the kernel will execute the thread on one of these logical processors.

- _isolatedCpuBitmask_: The CPU mask and the processor ids that are _isolated_ from the kernel scheduler. The kernel scheduler will not touch these processors unless affinity is set.

## SchedResult

When calling the `Affinity.set(int ... procIds)` method, you can check its return value, which is the [Affinity.SchedResult](https://github.com/coralblocks/CoralAffinity/blob/c44b49a18f8a5e9b1945a5463a25c0c42d4349e7/src/main/java/com/coralblocks/coralaffinity/Affinity.java#L15) object. See the example below:

```java
int procId = 1;

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
```
