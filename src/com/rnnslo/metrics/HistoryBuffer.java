package com.rnnslo.metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import com.rnnslo.config.ControllerConfiguration;

public final class HistoryBuffer {
    private final int capacity;
    private final Deque<MetricVector> samples = new ArrayDeque<>();

    public HistoryBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
    }

    public static HistoryBuffer withDefaultMetrics(ControllerConfiguration configuration) {
        HistoryBuffer buffer = new HistoryBuffer(configuration.getHistoryLength());
        for (int i = 0; i < configuration.getHistoryLength(); i++) {
            buffer.append(MetricVector.defaultMetric(configuration.initialPoolSize()));
        }
        return buffer;
    }

    public void append(MetricVector metricVector) {
        samples.addLast(metricVector);
        while (samples.size() > capacity) {
            samples.removeFirst();
        }
    }

    public boolean isReady() {
        return samples.size() >= capacity;
    }

    public HistoryWindow extractHistoryWindow() {
        return new HistoryWindow(new ArrayList<>(samples));
    }

    public int size() {
        return samples.size();
    }
}
