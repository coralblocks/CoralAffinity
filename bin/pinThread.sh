#!/bin/bash

VERBOSE=${2:-false}

CMD=(java "-DcoralAffinityVerbose=$VERBOSE" -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.sample.PinThread "$1")

printf '%q ' "${CMD[@]}"
printf '\n'

"${CMD[@]}"
