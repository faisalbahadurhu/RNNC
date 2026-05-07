package com.rnnslo.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rnnslo.config.ControllerConfiguration;

public final class HistoryWindow {
    private final List<MetricVector> metrics;

    public HistoryWindow(List<MetricVector> metrics) {
        if (metrics == null || metrics.isEmpty()) {
            throw new IllegalArgumentException("history window must not be empty");
        }
        this.metrics = Collections.unmodifiableList(new ArrayList<>(metrics));
    }

    public List<MetricVector> getMetrics() {
        return metrics;
    }

    public MetricVector latest() {
        return metrics.get(metrics.size() - 1);
    }

    public MetricVector oldest() {
        return metrics.get(0);
    }

    public int length() {
        return metrics.size();
    }

    public double[][] toNormalizedMatrix(ControllerConfiguration configuration) {
        double[][] matrix = new double[metrics.size()][];
        for (int i = 0; i < metrics.size(); i++) {
            matrix[i] = metrics.get(i).toNormalizedFeatureArray(configuration);
        }
        return matrix;
    }

    public double averageRequestRate() {
        return metrics.stream().mapToDouble(MetricVector::getRequestRateRps).average().orElse(0.0);
    }

    public double averageServiceTimeMillis() {
        return metrics.stream().mapToDouble(MetricVector::getAverageServiceTimeMillis).average().orElse(0.0);
    }

    public double averageQueueLength() {
        return metrics.stream().mapToDouble(MetricVector::getQueueLength).average().orElse(0.0);
    }

    public double requestRateTrend() {
        if (metrics.size() < 2) {
            return 0.0;
        }
        int mid = metrics.size() / 2;
        double first = metrics.subList(0, mid).stream().mapToDouble(MetricVector::getRequestRateRps).average().orElse(0.0);
        double second = metrics.subList(mid, metrics.size()).stream().mapToDouble(MetricVector::getRequestRateRps).average().orElse(0.0);
        return second - first;
    }
}
