package me.requests;

import me.expression.Node;
import me.expression.VariableValues;

import java.util.List;

public class ComputationRequest implements Request {
    private final ComputationKind computationKind;
    private final ValuesKind valuesKind;
    private final List<VariableValues> variableValues;
    private final List<Node> expressions;
    /**
     * This is used to determine what kind of computation is being requested.<br>
     * MIN: The minimum value of all the expressions. May fail if not all variables are specified. <br>
     * MAX: The maximum value of all the expressions. May fail if not all variables are specified. <br>
     * AVG: The average value of the first expression. May fail if not all variables are specified. <br>
     * COUNT: The amount of values obtained.
     */
    public enum ComputationKind {
        MIN,
        MAX,
        AVG,
        COUNT;
    }
    /**
     * This is used to determine what kind of values are being requested. <br>
     * GRID: A cartesian product of all the values. <br>
     * LIST: A list of all the values. May fail if the variables are not of the same length.<br>
     * Example: <br>
     * GRID of [1,2] and [3,4] will return [1,3], [1,4], [2,3], [2,4]. <br>
     * LIST of [1,2] and [3,4] will return [1,3], [2,4].
     */
    public enum ValuesKind {
        GRID,
        LIST;
    }
    public ComputationRequest(ComputationKind computationKind, ValuesKind valuesKind, List<VariableValues> variableValues, List<Node> expressions) {
        this.computationKind = computationKind;
        this.valuesKind = valuesKind;
        this.variableValues = variableValues;
        this.expressions = expressions;
    }
    @Override
    public double process() {
        return 0;
    }
    public List<VariableValues> getVariableValues() {
        return variableValues;
    }
    public List<Node> getExpressions() {
        return expressions;
    }
    public ComputationKind getComputationKind() {
        return computationKind;
    }
    public ValuesKind getValuesKind() {
        return valuesKind;
    }
}
