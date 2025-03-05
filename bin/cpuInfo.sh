#!/bin/bash

VERBOSE=${1:-true}

java -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.CpuInfo $1

