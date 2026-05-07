package com.rnnslo.model;

public final class Prediction {
    private final int candidatePoolSize;
    private final double predictedMeanP99Millis;
    private final double predictedUpperBoundP99Millis;
    private final double predictedCpuUtilizationPercent;
    private final double predictedMemoryUtilizationPercent;

    public Prediction(
            int candidatePoolSize,
            double predictedMeanP99Millis,
            double predictedUpperBoundP99Millis,
            double predictedCpuUtilizationPercent,
            double predictedMemoryUtilizationPercent) {
        this.candidatePoolSize = candidatePoolSize;
        this.predictedMeanP99Millis = predictedMeanP99Millis;
        this.predictedUpperBoundP99Millis = predictedUpperBoundP99Millis;
        this.predictedCpuUtilizationPercent = predictedCpuUtilizationPercent;
        this.predictedMemoryUtilizationPercent = predictedMemoryUtilizationPercent;
    }

    public int getCandidatePoolSize() {
        return candidatePoolSize;
    }

    public double getPredictedMeanP99Millis() {
        return predictedMeanP99Millis;
    }

    public double getPredictedUpperBoundP99Millis() {
        return predictedUpperBoundP99Millis;
    }

    public double getPredictedCpuUtilizationPercent() {
        return predictedCpuUtilizationPercent;
    }

    public double getPredictedMemoryUtilizationPercent() {
        return predictedMemoryUtilizationPercent;
    }
}
