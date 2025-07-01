package org.example;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadHandler {

    private static final Logger LOG = Logger.getLogger(ThreadHandler.class.getName());

    public static void execute(String type, int numberOfThreads, int sleepSeconds) {
        long pid = ProcessHandle.current().pid();
        LOG.info("Process ID: " + pid + ". Starting " + numberOfThreads + " " + type + " threads. Each will sleep for " + sleepSeconds + " seconds.");
        LOG.info("Take a thread dump now to see the threads state. PID: " + pid);

        long startTime = System.currentTimeMillis();

        try {
            if ("virtual".equalsIgnoreCase(type)) {
                executeVirtualThreads(numberOfThreads, sleepSeconds);
            } else if ("platform".equalsIgnoreCase(type)) {
                executePlatformThreads(numberOfThreads, sleepSeconds);
            } else {
                LOG.warning("Unknown thread type: " + type + ". Use 'platform' or 'virtual'.");
                return;
            }
        } catch (OutOfMemoryError | InterruptedException e) {
            LOG.log(Level.SEVERE, "OutOfMemoryError: Failed to create threads. This is expected for a large number of platform threads.", e);
        }

        long endTime = System.currentTimeMillis();
        LOG.info("Completed in " + (endTime - startTime) + " ms.");
    }

    private static void executeVirtualThreads(int numberOfThreads, int sleepSeconds) throws InterruptedException {
        long pid = ProcessHandle.current().pid();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < numberOfThreads; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        Thread.sleep(Duration.ofSeconds(sleepSeconds));
                        LOG.info("[VirtualThread] TaskID: " + taskId + ", related to PID: " + pid);
                    } catch (InterruptedException e) {
                        LOG.log(Level.WARNING, "Virtual thread task " + taskId + " interrupted: " + e.getMessage(), e);
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Exception in virtual thread task " + taskId + ": " + e.getMessage(), e);
                    }
                });
            }
            LOG.info("Waiting for completion...");
            Thread.sleep(Duration.ofMinutes(1));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception in executeVirtualThreads: " + e.getMessage(), e);
        }
        LOG.info("All virtual threads finished.");
    }

    private static void executePlatformThreads(int numberOfThreads, int sleepSeconds) throws InterruptedException {
        long pid = ProcessHandle.current().pid();
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            for (int i = 0; i < numberOfThreads; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        Thread.sleep(Duration.ofSeconds(sleepSeconds));
                        LOG.info("[PlatformThread] TaskID: " + taskId + ", PID: " + pid);
                    } catch (InterruptedException e) {
                        LOG.log(Level.WARNING, "Platform thread task " + taskId + " interrupted: " + e.getMessage(), e);
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Exception in platform thread task " + taskId + ": " + e.getMessage(), e);
                    }
                });
            }
            LOG.info("Waiting for completion...");
            Thread.sleep(Duration.ofMinutes(1));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception in executePlatformThreads: " + e.getMessage(), e);
        }
        LOG.info("All platform threads finished.");
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            LOG.severe("Usage: java org.example.ThreadHandler <platform|virtual> <numberOfThreads> <sleepSeconds>");
            LOG.severe("Example: java org.example.ThreadHandler virtual 20000 30");
            return;
        }

        String type = args[0];
        int numberOfThreads;
        int sleepSeconds;
        try {
            numberOfThreads = Integer.parseInt(args[1]);
            sleepSeconds = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            LOG.severe("Invalid number format for numberOfThreads or sleepSeconds: " + e.getMessage());
            return;
        }
        execute(type, numberOfThreads, sleepSeconds);
    }
}
