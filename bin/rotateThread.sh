#!/bin/bash

ISOLATED=${1:-false}
VERBOSE=${2:-false}

CMD=(java "-DcoralAffinityVerbose=$VERBOSE" -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.sample.RotateThread "$ISOLATED")

printf '%q ' "${CMD[@]}"
printf '\n'

"${CMD[@]}"
