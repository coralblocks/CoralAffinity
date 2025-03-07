#!/bin/bash

VERBOSE=${2:-false}

java -DcoralAffinityVerbose=$VERBOSE -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.sample.PinThread $1

