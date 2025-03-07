#!/bin/bash

ISOLATED=${1:-false}
VERBOSE=${2:-false}

CMD="java -DcoralAffinityVerbose=$VERBOSE -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.sample.RotateThread $ISOLATED"

echo $CMD

$CMD

