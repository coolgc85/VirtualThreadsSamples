#!/bin/bash
# Compiles and runs the thread comparison application.
# Usage: ./run.sh <platform|virtual> <numberOfThreads> <sleepSeconds>
# Example: ./run.sh virtual 20000 30
# Example: ./run.sh platform 5000 30 (might fail with more threads)

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <platform|virtual> <numberOfThreads> <sleepSeconds>"
    exit 1
fi

THREAD_TYPE=$1
NUM_THREADS=$2
SLEEP_SECONDS=$3

echo "Compiling the project with Maven..."
# Assuming mvnw is present. If not, use 'mvn'
if [ -f "./mvnw" ]; then
    ./mvnw clean package
else
    mvn clean package
fi


if [ $? -ne 0 ]; then
    echo "Maven build failed."
    exit 1
fi

echo "Running the application..."
java -cp target/VirtualThreadsSamples-1.0-SNAPSHOT.jar org.example.ThreadHandler $THREAD_TYPE $NUM_THREADS $SLEEP_SECONDS
