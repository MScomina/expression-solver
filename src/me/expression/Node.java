package me.expression;

import me.exceptions.ComputationException;

import java.util.List;
import java.util.Objects;

public abstract class Node {
  private final List<Node> children;

  public Node(List<Node> children) {
    this.children = children;
  }

  public List<Node> getChildren() {
    return children;
  }

  //NOTE: This method is always meant to be overridden.
  public double solve(Variable[] variables, double[] values) throws ComputationException {
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return Objects.equals(children, node.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(children);
  }

}
