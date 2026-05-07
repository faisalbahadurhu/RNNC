package com.rnnslo.model;

public final class GRUCell {
    private final GRUParameters parameters;

    public GRUCell(GRUParameters parameters) {
        this.parameters = parameters;
    }

    public double[] forward(double[] input, double[] previousHiddenState) {
        double[] updateGate = sigmoid(add(add(matVec(parameters.getUpdateInputWeights(), input),
                matVec(parameters.getUpdateHiddenWeights(), previousHiddenState)), parameters.getUpdateBias()));
        double[] resetGate = sigmoid(add(add(matVec(parameters.getResetInputWeights(), input),
                matVec(parameters.getResetHiddenWeights(), previousHiddenState)), parameters.getResetBias()));
        double[] resetHidden = multiply(resetGate, previousHiddenState);
        double[] candidateState = tanh(add(add(matVec(parameters.getCandidateInputWeights(), input),
                matVec(parameters.getCandidateHiddenWeights(), resetHidden)), parameters.getCandidateBias()));
        double[] hidden = new double[previousHiddenState.length];
        for (int i = 0; i < hidden.length; i++) {
            hidden[i] = (1.0 - updateGate[i]) * previousHiddenState[i] + updateGate[i] * candidateState[i];
        }
        return hidden;
    }

    private static double[] matVec(double[][] matrix, double[] vector) {
        double[] result = new double[matrix.length];
        for (int r = 0; r < matrix.length; r++) {
            double sum = 0.0;
            for (int c = 0; c < vector.length; c++) {
                sum += matrix[r][c] * vector[c];
            }
            result[r] = sum;
        }
        return result;
    }

    private static double[] add(double[] left, double[] right) {
        double[] result = new double[left.length];
        for (int i = 0; i < left.length; i++) {
            result[i] = left[i] + right[i];
        }
        return result;
    }

    private static double[] multiply(double[] left, double[] right) {
        double[] result = new double[left.length];
        for (int i = 0; i < left.length; i++) {
            result[i] = left[i] * right[i];
        }
        return result;
    }

    private static double[] sigmoid(double[] values) {
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = 1.0 / (1.0 + Math.exp(-values[i]));
        }
        return result;
    }

    private static double[] tanh(double[] values) {
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Math.tanh(values[i]);
        }
        return result;
    }
}
