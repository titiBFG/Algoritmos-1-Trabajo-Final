// OperationTimer.java - clase o interfaz de utils
package utils;

public class OperationTimer {
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    public double getElapsedSeconds() {
        return (endTime - startTime) / 1_000_000_000.0;
    }

    public String getFormattedElapsedTime() {
        long elapsedNanos = getElapsedTime();
        return String.format("%d ms", elapsedNanos / 1_000_000);
    }
}

