package me.expression;

import me.exceptions.ComputationException;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Operator extends Node {

  public enum Type {
    SUM('+', a -> a[0] + a[1]),
    SUBTRACTION('-', a -> a[0] - a[1]),
    MULTIPLICATION('*', a -> a[0] * a[1]),
    DIVISION('/', a -> a[0] / a[1]),
    POWER('^', a -> Math.pow(a[0], a[1]));
    private final char symbol;
    private final Function<double[], Double> function;

    Type(char symbol, Function<double[], Double> function) {
      this.symbol = symbol;
      this.function = function;
    }

    public char getSymbol() {
      return symbol;
    }

    public Function<double[], Double> getFunction() {
      return function;
    }
  }

  private final Type type;

  public Operator(Type type, List<Node> children) {
    super(children);
    this.type = type;
  }

  @Override
  public double solve(Variable[] variables, double[] values) throws ComputationException {
    double out = type.getFunction().apply(new double[]{
            getChildren().get(0).solve(variables, values),
            getChildren().get(1).solve(variables, values)
    });
    //NOTE: If the Infinite and NaN results are not acceptable, uncomment the following line.
    //if(Double.isInfinite(out) || Double.isNaN(out)) throw new ComputationException("Invalid computation: expression " + this + " returns Infinite/NaN with the values " + Arrays.toString(values) + ".");
    return out;
  }

  public Type getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Operator operator = (Operator) o;
    return type == operator.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }

  @Override
  public String toString() {
    String sb = "(" +
            getChildren().stream()
                    .map(Node::toString)
                    .collect(Collectors.joining(" " + type.symbol + " ")) +
            ")";
    return sb;
  }
}
