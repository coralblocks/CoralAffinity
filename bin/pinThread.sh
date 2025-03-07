#!/bin/bash

VERBOSE=${2:-false}

CMD="java -DcoralAffinityVerbose=$VERBOSE -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.sample.PinThread $1"

echo $CMD

$CMD

