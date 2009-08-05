package org.apache.myfaces.tobago.layout.math;

import java.util.Comparator;

public class EquationComparator implements Comparator<Equation> {

  public int compare(Equation x, Equation y) {

    return y.priority() - x.priority();
  }
}
