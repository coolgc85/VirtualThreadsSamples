@echo off
REM Takes a thread dump of a running Java process.
REM Usage: threaddump.cmd <pid>
REM You can find the PID from the output of the 'run.cmd' script.

set PID=%1
if [%1]==[] (
    echo "Usage: threaddump.cmd <pid>"
    echo "Finding Java processes..."
    jps
    goto :eof
)

echo "Taking thread dump for PID: %PID%"
jcmd %PID% Thread.print > threaddump_pid_%PID%.txt
echo "Thread dump saved to threaddump_pid_%PID%.txt"

echo "For virtual threads, a JSON format dump is more informative."
echo "You can use: jcmd %PID% Thread.dump_to_file -format=json threaddump_pid_%PID%.json"
jcmd %PID% Thread.dump_to_file -format=json threaddump_pid_%PID%.json
echo "JSON thread dump saved to threaddump_pid_%PID%.json"
