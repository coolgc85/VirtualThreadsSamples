#!/bin/bash
# Takes a thread dump of a running Java process.
# Usage: ./threaddump.sh <pid>
# You can find the PID from the output of the 'run.sh' script.

if [ -z "$1" ]; then
    echo "Usage: $0 <pid>"
    echo "Finding Java processes..."
    jps
    exit 1
fi

PID=$1

echo "Taking thread dump for PID: $PID"
jcmd $PID Thread.print > threaddump_pid_${PID}.txt
echo "Thread dump saved to threaddump_pid_${PID}.txt"

echo "For virtual threads, a JSON format dump is more informative."
echo "You can use: jcmd $PID Thread.dump_to_file -format=json threaddump_pid_${PID}.json"
jcmd $PID Thread.dump_to_file -format=json threaddump_pid_${PID}.json
echo "JSON thread dump saved to threaddump_pid_${PID}.json"
