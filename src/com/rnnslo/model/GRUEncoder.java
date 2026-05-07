package com.rnnslo.model;

public final class GRUEncoder {
    private final GRUParameters parameters;
    private final GRUCell cell;

    public GRUEncoder(GRUParameters parameters) {
        this.parameters = parameters;
        this.cell = new GRUCell(parameters);
    }

    public double[] encode(double[][] sequence) {
        double[] hidden = new double[parameters.getHiddenSize()];
        for (double[] input : sequence) {
            hidden = cell.forward(input, hidden);
        }
        return hidden;
    }
}
