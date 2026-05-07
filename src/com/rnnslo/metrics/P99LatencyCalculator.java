package com.rnnslo.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class P99LatencyCalculator {
    private P99LatencyCalculator() {
    }

    public static double empiricalP99Millis(List<Double> latenciesMillis) {
        if (latenciesMillis == null || latenciesMillis.isEmpty()) {
            return 0.0;
        }
        List<Double> sorted = new ArrayList<>(latenciesMillis);
        Collections.sort(sorted);
        int index = (int) Math.ceil(0.99 * sorted.size()) - 1;
        index = Math.max(0, Math.min(sorted.size() - 1, index));
        return sorted.get(index);
    }
}
