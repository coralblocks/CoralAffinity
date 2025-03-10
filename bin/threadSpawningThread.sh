#!/bin/bash

CMD="java -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.sample.ThreadSpawningThread $1 $2"

echo $CMD

$CMD

