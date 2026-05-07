package com.rnnslo.metrics;

import com.rnnslo.server.ServerSimulator;

public final class MetricsCollector {
    private final ServerSimulator serverSimulator;

    public MetricsCollector(ServerSimulator serverSimulator) {
        this.serverSimulator = serverSimulator;
    }

    public MetricVector measureCurrentMetrics(long timeStep) {
        return serverSimulator.step(timeStep);
    }
}
