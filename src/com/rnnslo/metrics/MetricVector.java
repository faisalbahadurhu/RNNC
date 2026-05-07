package com.rnnslo.metrics;

import java.util.Locale;

import com.rnnslo.config.ControllerConfiguration;

public final class MetricVector {
    private final long timeStep;
    private final double requestRateRps;
    private final double averageServiceTimeMillis;
    private final int currentPoolSize;
    private final double p99LatencyMillis;
    private final double cpuUtilizationPercent;
    private final double memoryUtilizationPercent;
    private final int queueLength;

    public MetricVector(
            long timeStep,
            double requestRateRps,
            double averageServiceTimeMillis,
            int currentPoolSize,
            double p99LatencyMillis,
            double cpuUtilizationPercent,
            double memoryUtilizationPercent,
            int queueLength) {
        this.timeStep = timeStep;
        this.requestRateRps = requestRateRps;
        this.averageServiceTimeMillis = averageServiceTimeMillis;
        this.currentPoolSize = currentPoolSize;
        this.p99LatencyMillis = p99LatencyMillis;
        this.cpuUtilizationPercent = cpuUtilizationPercent;
        this.memoryUtilizationPercent = memoryUtilizationPercent;
        this.queueLength = queueLength;
    }

    public static MetricVector defaultMetric(int poolSize) {
        return new MetricVector(0, 0, 1, poolSize, 0, 0, 0, 0);
    }

    public double[] toNormalizedFeatureArray(ControllerConfiguration configuration) {
        return new double[] {
                safeDivide(requestRateRps, 2000.0),
                safeDivide(averageServiceTimeMillis, 1000.0),
                safeDivide(currentPoolSize - configuration.getMinPoolSize(),
                        configuration.getMaxPoolSize() - configuration.getMinPoolSize()),
                safeDivide(p99LatencyMillis, Math.max(1.0, configuration.getP99SloMillis())),
                cpuUtilizationPercent / 100.0,
                memoryUtilizationPercent / 100.0,
                safeDivide(queueLength, 2000.0)
        };
    }

    private static double safeDivide(double numerator, double denominator) {
        if (Math.abs(denominator) < 1.0e-9) {
            return 0.0;
        }
        return numerator / denominator;
    }

    public long getTimeStep() {
        return timeStep;
    }

    public double getRequestRateRps() {
        return requestRateRps;
    }

    public double getAverageServiceTimeMillis() {
        return averageServiceTimeMillis;
    }

    public int getCurrentPoolSize() {
        return currentPoolSize;
    }

    public double getP99LatencyMillis() {
        return p99LatencyMillis;
    }

    public double getCpuUtilizationPercent() {
        return cpuUtilizationPercent;
    }

    public double getMemoryUtilizationPercent() {
        return memoryUtilizationPercent;
    }

    public int getQueueLength() {
        return queueLength;
    }

    public String toCsvRow(int selectedPoolSize, boolean sloSafe, double predictedUpperP99) {
        return String.format(Locale.US, "%d,%.4f,%.4f,%d,%.4f,%.4f,%.4f,%d,%d,%s,%.4f",
                timeStep,
                requestRateRps,
                averageServiceTimeMillis,
                currentPoolSize,
                p99LatencyMillis,
                cpuUtilizationPercent,
                memoryUtilizationPercent,
                queueLength,
                selectedPoolSize,
                Boolean.toString(sloSafe),
                predictedUpperP99);
    }
}
