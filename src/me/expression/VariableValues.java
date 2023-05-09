package me.expression;

public class VariableValues {
    private final Variable variable;
    private final double start;
    private final double end;
    private final double step;
    public VariableValues(Variable variable, double start, double end, double step) {
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.step = step;
    }
    public Variable getVariable() {
        return variable;
    }
    public double getStart() {
        return start;
    }
    public double getEnd() {
        return end;
    }
    public double getStep() {
        return step;
    }
    public double[] getValues() {
        int size = (int) ((end - start) / step) + 1;
        double[] values = new double[size];
        for (int i = 0; i < size; i++) {
            values[i] = start + i * step;
        }
        return values;
    }
}
