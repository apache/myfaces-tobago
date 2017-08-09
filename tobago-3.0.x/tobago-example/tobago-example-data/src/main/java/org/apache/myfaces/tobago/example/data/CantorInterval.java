/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.data;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.math.BigInteger;
import java.util.Enumeration;

/**
 * Builds a tree with the iterations of the <a href="http://en.wikipedia.org/wiki/Cantor_set">Cantor set</a>.
 * The tree is infinitive, so it will be created on the fly.
 */
public class CantorInterval extends DefaultMutableTreeNode {

  private Fraction begin;
  private Fraction end;
  private boolean initialized;

  public CantorInterval() {
    begin = Fraction.ZERO;
    end = Fraction.ONE;
  }

  private CantorInterval(final Fraction begin, final Fraction end) {
    this.begin = begin;
    this.end = end;
  }

  // init must be lazy, because the tree is infinite
  private void init() {
    if (!initialized) {
      initialized = true; // add() will call getChildCount()!
      final Fraction subtract = end.subtract(begin);
      final Fraction multiply = subtract.multiply(Fraction.ONE_THIRD);
      final Fraction oneThird = multiply.add(begin);
      add(new CantorInterval(begin, oneThird));

      final Fraction twoThird = end.subtract(begin).multiply(Fraction.TWO_THIRDS).add(begin);
      add(new CantorInterval(twoThird, end));
    }
  }

  @Override
  public int getChildCount() {
    init();
    return super.getChildCount();
  }

  @Override
  public TreeNode getChildAt(final int i) {
    init();
    return super.getChildAt(i);
  }

  @Override
  public Enumeration children() {
    init();
    return super.children();
  }

  public String getLabel() {
    final StringBuilder builder = new StringBuilder();
    builder.append("[");
    builder.append(begin);
    builder.append(", ");
    builder.append(end);
    builder.append("]");
    return builder.toString();
  }

  public String toString() {
    return getLabel();
  }

  public static final class Fraction {

    private final BigInteger numerator;
    private final BigInteger denominator;

    public static final Fraction ZERO = new Fraction(0);
    public static final Fraction ONE = new Fraction(1);
    public static final BigInteger THREE = BigInteger.valueOf(3);
    public static final Fraction ONE_THIRD = new Fraction(BigInteger.ONE, THREE);
    public static final Fraction TWO_THIRDS = new Fraction(BigInteger.valueOf(2), THREE);

    private Fraction(final long i) {
      numerator = BigInteger.valueOf(i);
      denominator = BigInteger.ONE;
    }

    private Fraction(BigInteger numerator, BigInteger denominator) {
      while (numerator.remainder(THREE).equals(BigInteger.ZERO)
          && denominator.remainder(THREE).equals(BigInteger.ZERO)) {
        numerator = numerator.divide(THREE);
        denominator = denominator.divide(THREE);
      }
      this.numerator = numerator;
      this.denominator = denominator;
    }

    public Fraction add(final Fraction value) {
      return new Fraction(
          numerator.multiply(value.denominator).add(denominator.multiply(value.numerator)),
          denominator.multiply(value.denominator));
    }

    public Fraction subtract(final Fraction value) {
      return new Fraction(
          numerator.multiply(value.denominator).subtract(denominator.multiply(value.numerator)),
          denominator.multiply(value.denominator));
    }

    public Fraction multiply(final Fraction value) {
      return new Fraction(
          numerator.multiply(value.numerator),
          denominator.multiply(value.denominator));
    }

    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder();
      if (denominator.equals(BigInteger.ONE)) {
        builder.append(numerator);
      } else {
        builder.append(numerator);
        builder.append("/");
        builder.append(denominator);
      }
      return builder.toString();
    }
  }
}
