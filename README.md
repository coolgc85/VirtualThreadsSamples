# Virtual vs. Platform Threads Comparison

This project demonstrates the difference between Java's platform threads and virtual threads (introduced in Project Loom). It's designed to show how virtual threads can handle a massive number of concurrent blocking tasks with low overhead, while platform threads are limited by OS resources.

## Code Structure

- `pom.xml`: Maven project file, configured for Java 21.
- `src/main/java/org/example/ThreadHandler.java`: The main application logic. It creates and runs a specified number of either platform or virtual threads. Each thread simulates a blocking I/O operation by sleeping for a configured duration.

## How to Run

You need Java 21 and Maven installed.

### 1. Build and Run the Application

Use the provided `run.cmd` (for Windows) or `run.sh` (for Linux/macOS) scripts.

**Usage:**
```shell
# For Windows
run.cmd <platform|virtual> <numberOfThreads> <sleepSeconds>

# For Linux/macOS
./run.sh <platform|virtual> <numberOfThreads> <sleepSeconds>
```

**Examples:**

- **Run with 20,000 virtual threads:**
  This should run without issues.
  ```shell
  # Windows
  run.cmd virtual 20000 30

  # Linux/macOS
  ./run.sh virtual 20000 30
  ```

- **Run with 20,000 platform threads:**
  This will likely fail with an `OutOfMemoryError`, demonstrating the limitation of platform threads.
  ```shell
  # Windows
  run.cmd platform 20000 30

  # Linux/macOS
  ./run.sh platform 20000 30
  ```

### 2. Analyze Threads with Thread Dumps

While the application is running (especially during the sleep period), you can take thread dumps to inspect the state of the threads. The application will print its Process ID (PID) when it starts.

Use the `threaddump.cmd` (for Windows) or `threaddump.sh` (for Linux/macOS) scripts.

**Usage:**
```shell
# For Windows
threaddump.cmd <PID>

# For Linux/macOS
./threaddump.sh <PID>
```

If you don't provide a PID, the script will try to list running Java processes using `jps`.

**Interpreting the Dumps:**

- **Platform Threads:** A standard thread dump (`Thread.print`) will show a large number of threads, most of them in a `TIMED_WAITING` (sleeping) state. Each of these threads corresponds to an OS thread, consuming significant memory.
- **Virtual Threads:** The standard thread dump will show a small number of platform threads (the "carrier" threads). To see the virtual threads, you should inspect the JSON-formatted dump that the script also generates (`threaddump_pid_...json`). In this file, you can see all the virtual threads, most of which will be "parked" and not consuming an OS thread. This highlights the efficiency of virtual threads.
