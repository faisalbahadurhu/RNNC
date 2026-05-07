package com.rnnslo.config;

public final class ControllerConfiguration {
    private final int hardwareThreads;
    private final int minPoolSize;
    private final int maxPoolSize;
    private final int historyLength;
    private final int samplingPeriodSeconds;
    private final int controlIntervalSeconds;
    private final double p99SloMillis;
    private final double quantileLevel;

    public ControllerConfiguration(
            int hardwareThreads,
            int minPoolSize,
            int maxPoolSize,
            int historyLength,
            int samplingPeriodSeconds,
            int controlIntervalSeconds,
            double p99SloMillis,
            double quantileLevel) {
        if (hardwareThreads <= 0) {
            throw new IllegalArgumentException("hardwareThreads must be positive");
        }
        if (minPoolSize <= 0 || maxPoolSize < minPoolSize) {
            throw new IllegalArgumentException("invalid pool-size range");
        }
        if (historyLength <= 0) {
            throw new IllegalArgumentException("historyLength must be positive");
        }
        this.hardwareThreads = hardwareThreads;
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
        this.historyLength = historyLength;
        this.samplingPeriodSeconds = samplingPeriodSeconds;
        this.controlIntervalSeconds = controlIntervalSeconds;
        this.p99SloMillis = p99SloMillis;
        this.quantileLevel = quantileLevel;
    }

    public static ControllerConfiguration forCurrentMachine() {
        int h = Math.max(1, Runtime.getRuntime().availableProcessors());
        int min = Math.max(1, 2 * h);
        int max = Math.max(min, 32 * h);
        return new ControllerConfiguration(h, min, max, 30, 1, 1, 1000.0, 0.95);
    }

    public ControllerConfiguration withHistoryLength(int value) {
        return new ControllerConfiguration(hardwareThreads, minPoolSize, maxPoolSize, value,
                samplingPeriodSeconds, controlIntervalSeconds, p99SloMillis, quantileLevel);
    }

    public ControllerConfiguration withP99SloMillis(double value) {
        return new ControllerConfiguration(hardwareThreads, minPoolSize, maxPoolSize, historyLength,
                samplingPeriodSeconds, controlIntervalSeconds, value, quantileLevel);
    }

    public ControllerConfiguration withSamplingPeriodSeconds(int value) {
        return new ControllerConfiguration(hardwareThreads, minPoolSize, maxPoolSize, historyLength,
                value, controlIntervalSeconds, p99SloMillis, quantileLevel);
    }

    public ControllerConfiguration withControlIntervalSeconds(int value) {
        return new ControllerConfiguration(hardwareThreads, minPoolSize, maxPoolSize, historyLength,
                samplingPeriodSeconds, value, p99SloMillis, quantileLevel);
    }

    public int initialPoolSize() {
        return Math.max(minPoolSize, hardwareThreads);
    }

    public int clampPoolSize(int value) {
        return Math.max(minPoolSize, Math.min(maxPoolSize, value));
    }

    public int getHardwareThreads() {
        return hardwareThreads;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getHistoryLength() {
        return historyLength;
    }

    public int getSamplingPeriodSeconds() {
        return samplingPeriodSeconds;
    }

    public int getControlIntervalSeconds() {
        return controlIntervalSeconds;
    }

    public double getP99SloMillis() {
        return p99SloMillis;
    }

    public double getQuantileLevel() {
        return quantileLevel;
    }
}
