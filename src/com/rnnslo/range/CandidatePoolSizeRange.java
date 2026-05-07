package com.rnnslo.range;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.rnnslo.config.ControllerConfiguration;

public final class CandidatePoolSizeRange {
    private final int minPoolSize;
    private final int maxPoolSize;

    public CandidatePoolSizeRange(int minPoolSize, int maxPoolSize) {
        if (minPoolSize <= 0 || maxPoolSize < minPoolSize) {
            throw new IllegalArgumentException("invalid candidate range");
        }
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    public static CandidatePoolSizeRange fromConfiguration(ControllerConfiguration configuration) {
        return new CandidatePoolSizeRange(configuration.getMinPoolSize(), configuration.getMaxPoolSize());
    }

    public List<Integer> boundedSearchCandidates(int currentPoolSize) {
        TreeSet<Integer> values = new TreeSet<>();
        int width = maxPoolSize - minPoolSize;
        int coarseStep = Math.max(1, width / 16);
        for (int candidate = minPoolSize; candidate <= maxPoolSize; candidate += coarseStep) {
            values.add(candidate);
        }
        values.add(maxPoolSize);
        values.add(clamp(currentPoolSize));
        values.add(clamp(currentPoolSize / 2));
        values.add(clamp(currentPoolSize * 2));
        int localStep = Math.max(1, coarseStep / 4);
        for (int offset = -4; offset <= 4; offset++) {
            values.add(clamp(currentPoolSize + offset * localStep));
        }
        return new ArrayList<>(values);
    }

    public int clamp(int value) {
        return Math.max(minPoolSize, Math.min(maxPoolSize, value));
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }
}
