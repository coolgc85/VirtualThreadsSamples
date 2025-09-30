@echo off
REM Compiles and runs the thread comparison application.
REM Usage: run.cmd <platform|virtual> <numberOfThreads> <sleepSeconds>
REM Example: run.cmd virtual 20000 30
REM Example: run.cmd platform 5000 30 (might fail with more threads)

set THREAD_TYPE=%1
set NUM_THREADS=%2
set SLEEP_SECONDS=%3

if [%1]==[] (
    echo "Usage: run.cmd <platform|virtual> <numberOfThreads> <sleepSeconds>"
    goto :eof
)
if [%2]==[] (
    echo "Usage: run.cmd <platform|virtual> <numberOfThreads> <sleepSeconds>"
    goto :eof
)
if [%3]==[] (
    echo "Usage: run.cmd <platform|virtual> <numberOfThreads> <sleepSeconds>"
    goto :eof
)


echo "Compiling the project with Maven..."
call mvnw.cmd clean package
if %errorlevel% neq 0 (
    echo "Maven build failed."
    exit /b %errorlevel%
)

echo "Running the application..."
java -cp target/VirtualThreadsSamples-1.0-SNAPSHOT.jar dev.coolgc.ThreadHandler %THREAD_TYPE% %NUM_THREADS% %SLEEP_SECONDS%
