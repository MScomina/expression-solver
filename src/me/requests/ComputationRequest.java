package me.requests;

import me.exceptions.ComputationException;
import me.expression.Node;
import me.expression.VariableValues;

import java.util.ArrayList;
import java.util.Arrays;

public class ComputationRequest implements Request {
    private final ComputationKind computationKind;
    private final ValuesKind valuesKind;
    private final VariableValues[] variableValues;
    private final Node[] expressions;
    @Override
    public double[] process() throws ComputationException {
        int startingTime = (int) System.currentTimeMillis();
        double output = 0;
        switch (valuesKind) {
            case GRID:
                switch (computationKind) {
                    case MIN:
                    case MAX:
                    case AVG:
                    case COUNT:
                        int calc = 1;
                        for(VariableValues variableValue : variableValues) {
                            calc *= variableValue.getNumberOfValues();
                        }
                        output = calc;
                }
            case LIST:
                switch (computationKind) {
                    case MIN:
                    case MAX:
                    case AVG:
                    case COUNT:
                        int lengthCheck = variableValues[0].getNumberOfValues();
                        for (VariableValues variableValue : variableValues) {
                            if (variableValue.getNumberOfValues() != lengthCheck) {
                                throw new ComputationException("The number of values must be the same for all VariableValues.");
                            }
                        }
                        output = lengthCheck;
                }
        }
        double secondsElapsed = ((double)(System.currentTimeMillis() - startingTime))/1000.0d;
        return new double[]{secondsElapsed, output};
    }

    /**
     * Calculates the LIST values (element-wise merging of values from all VariableValues).
     * @return A 2D array of doubles, containing the tuples of all VariableValues.
     * @throws ComputationException if the number of values is not the same for all VariableValues.
     */
    private ArrayList<double[]> getListValues() throws ComputationException {
        int lengthCheck = variableValues[0].getNumberOfValues();
        for (VariableValues variableValue : variableValues) {
            if (variableValue.getNumberOfValues() != lengthCheck) {
                throw new ComputationException("The number of values must be the same for all VariableValues.");
            }
        }
        double[][] listValues = new double[lengthCheck][variableValues.length];
        for (int k = 0; k < variableValues.length; k++) {
            double[] values = variableValues[k].getValues();
            for(int i = 0; i < values.length; i++) {
                listValues[i][k] = values[i];
            }
        }
        return new ArrayList<>(Arrays.asList(listValues));
    }
    public ComputationRequest(ComputationKind computationKind, ValuesKind valuesKind, VariableValues[] variableValues, Node[] expressions) {
        this.computationKind = computationKind;
        this.valuesKind = valuesKind;
        this.variableValues = variableValues;
        this.expressions = expressions;
    }

    /**
     * Calculates the GRID values (cartesian product of all VariableValues).
     * @return A 2D array of doubles, containing the cartesian product of all VariableValues.
     */
    private ArrayList<double[]> getGridValues() {
        int n = variableValues.length;
        double[][] arrays = new double[n][];
        for(int k = 0; k < n; k++) {
            arrays[k] = variableValues[k].getValues();
        }
        ArrayList<double[]> result = new ArrayList<>();
        //Cartesian product.
        int[] indices = new int[n];
        int[] lengths = new int[n];
        for (int i = 0; i < n; i++) {
            lengths[i] = arrays[i].length;
        }
        boolean done = false;
        while (!done) {
            //Creates a new array to add with the current indices.
            double[] current = new double[n];
            for (int i = 0; i < n; i++) {
                current[i] = arrays[i][indices[i]];
            }
            result.add(current);
            //Checks if the indices are at the end of the arrays, from the left, and resets them if so.
            //Example: if lengths are [3,2,4], indexes will go [0,0,0], [1,0,0], [2,0,0], [0,1,0], [1,1,0] etc. until [2,1,3], similar to a positional number system.
            //This will inevitably create all possible combinations of the arrays, using all the indexes.
            int k = 0;
            while (k <= n-1 && indices[k] == lengths[k] - 1) {
                indices[k] = 0;
                k++;
            }
            if (k == n) {
                done = true;
            } else {
                indices[k]++;
            }
        }
        return result;
    }

    /**
     * This is used to determine what kind of computation is being requested.<br>
     * Also used in {@link me.utils.RequestParseUtils} for checking command list.
     */
    public enum ComputationKind {
        MIN,
        MAX,
        AVG,
        COUNT
    }

    /**
     * This is used to determine what kind of values are being requested. <br>
     * Also used in {@link me.utils.RequestParseUtils} for checking command list.
     */
    public enum ValuesKind {
        GRID,
        LIST
    }
    public VariableValues[] getVariableValues() {
        return variableValues;
    }
    public Node[] getExpressions() {
        return expressions;
    }
    public ComputationKind getComputationKind() {
        return computationKind;
    }
    public ValuesKind getValuesKind() {
        return valuesKind;
    }
}
