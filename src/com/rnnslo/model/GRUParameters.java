package com.rnnslo.model;

import java.util.Random;

public final class GRUParameters {
    private final int inputSize;
    private final int hiddenSize;
    private final double[][] updateInputWeights;
    private final double[][] updateHiddenWeights;
    private final double[] updateBias;
    private final double[][] resetInputWeights;
    private final double[][] resetHiddenWeights;
    private final double[] resetBias;
    private final double[][] candidateInputWeights;
    private final double[][] candidateHiddenWeights;
    private final double[] candidateBias;

    private GRUParameters(int inputSize, int hiddenSize, Random random) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.updateInputWeights = randomMatrix(hiddenSize, inputSize, random);
        this.updateHiddenWeights = randomMatrix(hiddenSize, hiddenSize, random);
        this.updateBias = randomVector(hiddenSize, random);
        this.resetInputWeights = randomMatrix(hiddenSize, inputSize, random);
        this.resetHiddenWeights = randomMatrix(hiddenSize, hiddenSize, random);
        this.resetBias = randomVector(hiddenSize, random);
        this.candidateInputWeights = randomMatrix(hiddenSize, inputSize, random);
        this.candidateHiddenWeights = randomMatrix(hiddenSize, hiddenSize, random);
        this.candidateBias = randomVector(hiddenSize, random);
    }

    public static GRUParameters initialize(int inputSize, int hiddenSize, long seed) {
        return new GRUParameters(inputSize, hiddenSize, new Random(seed));
    }

    private static double[][] randomMatrix(int rows, int cols, Random random) {
        double[][] matrix = new double[rows][cols];
        double scale = 1.0 / Math.sqrt(Math.max(1, cols));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = (random.nextDouble() * 2.0 - 1.0) * scale;
            }
        }
        return matrix;
    }

    private static double[] randomVector(int size, Random random) {
        double[] vector = new double[size];
        for (int i = 0; i < size; i++) {
            vector[i] = (random.nextDouble() * 2.0 - 1.0) * 0.05;
        }
        return vector;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getHiddenSize() {
        return hiddenSize;
    }

    public double[][] getUpdateInputWeights() {
        return updateInputWeights;
    }

    public double[][] getUpdateHiddenWeights() {
        return updateHiddenWeights;
    }

    public double[] getUpdateBias() {
        return updateBias;
    }

    public double[][] getResetInputWeights() {
        return resetInputWeights;
    }

    public double[][] getResetHiddenWeights() {
        return resetHiddenWeights;
    }

    public double[] getResetBias() {
        return resetBias;
    }

    public double[][] getCandidateInputWeights() {
        return candidateInputWeights;
    }

    public double[][] getCandidateHiddenWeights() {
        return candidateHiddenWeights;
    }

    public double[] getCandidateBias() {
        return candidateBias;
    }
}
