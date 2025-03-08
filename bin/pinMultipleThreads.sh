#!/bin/bash

VERBOSE=${3:-false}

CMD="java -DcoralAffinityVerbose=$VERBOSE -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.sample.PinMultipleThreads $1 $2"

echo $CMD

$CMD

