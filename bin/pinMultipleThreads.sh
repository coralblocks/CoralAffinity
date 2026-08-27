#!/bin/bash

CMD=(java -cp target/coralaffinity-all.jar com.coralblocks.coralaffinity.sample.PinMultipleThreads "$1" "$2")

printf '%q ' "${CMD[@]}"
printf '\n'

"${CMD[@]}"
