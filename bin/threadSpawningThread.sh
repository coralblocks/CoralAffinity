#!/bin/bash

CMD=(java -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.sample.ThreadSpawningThread "$1")
if (( $# > 1 )); then
    CMD+=("$2")
fi

printf '%q ' "${CMD[@]}"
printf '\n'

"${CMD[@]}"
