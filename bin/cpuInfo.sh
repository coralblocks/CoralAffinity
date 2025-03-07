#!/bin/bash

VERBOSE=${1:-false}

CMD="java -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.CpuInfo $VERBOSE"

echo $CMD

$CMD

