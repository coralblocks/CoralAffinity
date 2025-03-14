#!/bin/bash

VERBOSE=${1:-false}
CPUINFO=${2:-/proc/cpuinfo}
CMDLINE=${3:-/proc/cmdline}

CMD="java -DcoralAffinityCpuInfoFile=$CPUINFO -DcoralAffinityCmdLineFile=$CMDLINE -DcoralAffinityVerbose=$VERBOSE -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.CpuInfo $2 $3"

echo $CMD

$CMD

