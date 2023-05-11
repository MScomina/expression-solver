package me.requests;

import me.expression.Node;
import me.expression.VariableValues;

import java.util.List;

public class ComputationRequest implements Request {
    private final ComputationKind computationKind;
    private final ValuesKind valuesKind;
    private final VariableValues[] variableValues;
    private final Node[] expressions;
    /**
     * This is used to determine what kind of computation is being requested.<br>
     * Also used in {@link me.utils.RequestParseUtils} for checking command list.
     */
    public enum ComputationKind {
        MIN,
        MAX,
        AVG,
        COUNT;
    }
    /**
     * This is used to determine what kind of values are being requested. <br>
     * Also used in {@link me.utils.RequestParseUtils} for checking command list.
     */
    public enum ValuesKind {
        GRID,
        LIST;
    }
    public ComputationRequest(ComputationKind computationKind, ValuesKind valuesKind, VariableValues[] variableValues, Node[] expressions) {
        this.computationKind = computationKind;
        this.valuesKind = valuesKind;
        this.variableValues = variableValues;
        this.expressions = expressions;
    }
    @Override
    public double process() {
        return 0;
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
