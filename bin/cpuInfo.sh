#!/bin/bash

VERBOSE=${1:-false}

CMD="java -DcoralAffinityVerbose=$VERBOSE -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.CpuInfo"

echo $CMD

$CMD

