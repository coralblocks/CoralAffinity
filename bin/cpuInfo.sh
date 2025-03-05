#!/bin/bash

VERBOSE=${1:-false}

java -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.CpuInfo $VERBOSE

