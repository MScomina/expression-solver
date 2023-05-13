package me.expression;

import java.util.Objects;

public class VariableValues {
    private final Variable variable;
    private final double start;
    private final double end;
    private final double step;
    public VariableValues(String[] s) {
        if(s.length != 4) {
            throw new IllegalArgumentException("VariableValues constructor takes 4 arguments.");
        }
        try {
            this.variable = new Variable(s[0]);
            this.start = Double.parseDouble(s[1]);
            this.step = Double.parseDouble(s[2]);
            this.end = Double.parseDouble(s[3]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Could not parse String to double.");
        }
        if(this.step <= 0) {
            throw new IllegalArgumentException("Step must be greater than 0.");
        }
        if(this.start > this.end) {
            throw new IllegalArgumentException("Start must be smaller than end.");
        }
    }
    public Variable getVariable() {
        return variable;
    }
    public double[] getValues() {
        int size = (int) ((end - start) / step) + 1;
        double[] values = new double[size];
        for (int i = 0; i < size; i++) {
            values[i] = start + i * step;
        }
        return values;
    }
    public int getNumberOfValues() {
        return (int) ((end - start) / step) + 1;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableValues variableValues = (VariableValues) o;
        return Objects.equals(this.variable, variableValues.variable) && this.start == variableValues.start && this.end == variableValues.end && this.step == variableValues.step;
    }
    @Override
    public String toString() {
        return variable.toString() + ":" + start + ":" + step + ":" + end;
    }
}
