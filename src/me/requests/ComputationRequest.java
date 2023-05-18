package me.requests;

import me.exceptions.ComputationException;
import me.expression.Node;
import me.expression.Variable;
import me.expression.VariableValues;

public class ComputationRequest implements Request {
    /**
     * This variable marks if the computation is complete or not. It is set to false at the start and it only can turn true if there aren't any more values to compute from {@link #getNextValues()}.
     */
    private boolean computationDone;
    private final int[] lengths;
    private final int[] indexes;
    private final ComputationKind computationKind;
    private final ValuesKind valuesKind;
    private final VariableValues[] variableValues;
    private final Node[] expressions;

    public ComputationRequest(ComputationKind computationKind, ValuesKind valuesKind, VariableValues[] variableValues, Node[] expressions) {
        this.computationKind = computationKind;
        this.valuesKind = valuesKind;
        this.variableValues = variableValues;
        this.expressions = expressions;

        this.computationDone = false;
        this.lengths = new int[variableValues.length];
        this.indexes = new int[variableValues.length];

        for (int i = 0; i < variableValues.length; i++) {
            this.lengths[i] = variableValues[i].getNumberOfValues();
            this.indexes[i] = 0;
        }
    }

    /**
     * Starts the processing of the computation request.
     * <p>
     * NOTE: This method is meant to be called only once per instance.
     *
     * @return The result of the computation of the request.
     * @throws ComputationException If the computation fails at any point. Examples may include malformed requests, missing variables or non-computable expressions.
     */
    @Override
    public double process() throws ComputationException {
        double output = 0.0d;
        Variable[] variables = new Variable[variableValues.length];
        for (int i = 0; i < variables.length; i++) {
            variables[i] = variableValues[i].getVariable();
        }
        switch (computationKind) {
            case MIN -> output = this.minimum(variables);
            case MAX -> output = this.maximum(variables);
            case AVG -> output = this.average(variables);
            case COUNT -> {
                switch (valuesKind) {
                    case GRID -> {
                        long calc = 1;
                        for (VariableValues variableValue : variableValues) {
                            calc *= variableValue.getNumberOfValues();
                        }
                        output = calc;
                    }
                    case LIST -> {
                        int lengthCheck = variableValues[0].getNumberOfValues();
                        for (VariableValues variableValue : variableValues) {
                            if (variableValue.getNumberOfValues() != lengthCheck) {
                                throw new ComputationException("Cannot compute LIST: the number of values must be the same for all VariableValues.");
                            }
                        }
                        output = lengthCheck;
                    }
                }
            }
        }
        return output;
    }

    /**
     * Gives the next values for the computation, if any.
     *
     * @return GRID: Returns the next values for the computation in all the cartesian product of the variable values.
     * <p>
     * LIST: Returns the next values for the computation in the element-wise merging of the variable values.
     * </p>
     * <p>
     * {@code null} if the computation is done.
     * </p>
     * @throws ComputationException if, in a LIST computation, the number of values is not the same for all VariableValues.
     */
    private double[] getNextValues() throws ComputationException {
        if (computationDone) return null;
        double[] values = new double[variableValues.length];
        switch (valuesKind) {
            case GRID -> {
                int n = variableValues.length;
                for (int i = 0; i < n; i++) {
                    values[i] = variableValues[i].getValueAt(indexes[i]);
                }
                //Checks if the indices are at the end of the arrays, from the left, and resets them if so.
                //Example: if lengths are [3,2,4], indexes will go [0,0,0], [1,0,0], [2,0,0], [0,1,0], [1,1,0] etc. until [2,1,3], similar to a positional number system.
                //This will inevitably create all possible combinations of the arrays, using all the possible indexes.
                int k = 0;
                while (k <= n - 1 && indexes[k] == lengths[k] - 1) {
                    indexes[k] = 0;
                    k++;
                }
                if (k == n) {
                    computationDone = true;
                } else {
                    indexes[k]++;
                }
            }
            case LIST -> {
                for (int length : lengths) {
                    if (length != lengths[0]) {
                        throw new ComputationException("Cannot compute LIST: the number of values must be the same for all VariableValues.");
                    }
                }
                for (int i = 0; i < variableValues.length; i++) {
                    values[i] = variableValues[i].getValueAt(indexes[0]);
                }
                indexes[0]++;
                if (indexes[0] == variableValues[0].getNumberOfValues()) {
                    computationDone = true;
                }
            }
        }
        return values;
    }

    /**
     * Calculates the minimum of all expressions.
     *
     * @param variables All the variables to be used in the expressions.
     * @return The minimum of all expressions, for all values depending on {@link #getNextValues()} and {@link #computationDone}.
     * @throws ComputationException if the expression cannot be resolved.: E.g. x/0.
     */
    private double minimum(Variable[] variables) throws ComputationException {
        double output = Double.MAX_VALUE;
        for (Node expression : expressions) {
            while (!computationDone) {
                double result = expression.solve(variables, this.getNextValues());
                if (result < output) {
                    output = result;
                }
            }
        }
        return output;
    }

    /**
     * Calculates the maximum of all expressions.
     *
     * @param variables All the variables to be used in the expressions.
     * @return The maximum of all expressions, for all values depending on {@link #getNextValues()} and {@link #computationDone}.
     * @throws ComputationException if the expression cannot be resolved. E.g.: x/0.
     */
    private double maximum(Variable[] variables) throws ComputationException {
        double output = -Double.MAX_VALUE;
        for (Node expression : expressions) {
            while (!computationDone) {
                double result = expression.solve(variables, this.getNextValues());
                if (result > output) {
                    output = result;
                }
            }
        }
        return output;
    }

    /**
     * Calculates the average of the first expression.
     *
     * @param variables All the variables to be used in the expression.
     * @return The average of the first expression, for all values depending on {@link #getNextValues()} and {@link #computationDone}.
     * @throws ComputationException if the expression cannot be resolved. E.g.: x/0.
     */
    private double average(Variable[] variables) throws ComputationException {
        double output = 0.0d;
        long size = 0;
        Node expression = expressions[0];
        while (!computationDone) {
            output += expression.solve(variables, this.getNextValues());
            size++;
        }
        return output / size;
    }

    /**
     * This is used to determine what kind of computation is being requested.<br>
     * It is also used in {@linkplain  me.utils.RequestParseUtils RequestParseUtils} for checking the command list.
     */
    public enum ComputationKind {
        MIN,
        MAX,
        AVG,
        COUNT
    }

    /**
     * This is used to determine what kind of values are being requested. <br>
     * It is also used in {@linkplain  me.utils.RequestParseUtils RequestParseUtils} for checking the command list.
     */
    public enum ValuesKind {
        GRID,
        LIST
    }
}
