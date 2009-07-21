package org.apache.myfaces.tobago.layout.math;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.junit.Assert;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.Arrays;

public class SystemOfEquationsUnitTest {

  private static final Log LOG = LogFactory.getLog(SystemOfEquationsUnitTest.class);

  @Test
  public void test2To3() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(3);
    system.addEqualsEquation(new FixedEquation(0, new PixelMeasure(1005)));
    system.addEqualsEquation(new PartitionEquation(1, 2, 0, new PixelMeasure(5)));
    system.addEqualsEquation(new ProportionEquation(1, 2, 2, 3));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(1005), new PixelMeasure(400), new PixelMeasure(600)}, result);
  }

  @Test
  public void test1To2To3To4() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(5);
    system.addEqualsEquation(new FixedEquation(0, new PixelMeasure(1015)));
    system.addEqualsEquation(new PartitionEquation(1, 4, 0, new PixelMeasure(5)));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 2));
    system.addEqualsEquation(new ProportionEquation(2, 3, 2, 3));
    system.addEqualsEquation(new ProportionEquation(3, 4, 3, 4));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(1015), new PixelMeasure(100), new PixelMeasure(200), new PixelMeasure(300),
        new PixelMeasure(400)}, result);
  }

  @Test
  public void test1To2To3To4To5To6To7To8To9To10() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(11);
    system.addEqualsEquation(new FixedEquation(0, new PixelMeasure(1100)));
    system.addEqualsEquation(new PartitionEquation(1, 10, 0, PixelMeasure.ZERO));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 2));
    system.addEqualsEquation(new ProportionEquation(2, 3, 2, 3));
    system.addEqualsEquation(new ProportionEquation(3, 4, 3, 4));
    system.addEqualsEquation(new ProportionEquation(4, 5, 4, 5));
    system.addEqualsEquation(new ProportionEquation(5, 6, 5, 6));
    system.addEqualsEquation(new ProportionEquation(6, 7, 6, 7));
    system.addEqualsEquation(new ProportionEquation(7, 8, 7, 8));
    system.addEqualsEquation(new ProportionEquation(8, 9, 8, 9));
    system.addEqualsEquation(new ProportionEquation(9, 10, 9, 10));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(1100), new PixelMeasure(20), new PixelMeasure(40), new PixelMeasure(60),
        new PixelMeasure(80), new PixelMeasure(100), new PixelMeasure(120), new PixelMeasure(140),
        new PixelMeasure(160), new PixelMeasure(180), new PixelMeasure(200)}, result);
  }

  @Test
  public void test1To___To100() {

    long begin = System.nanoTime();

    int n = 100;
    int sum = n * (n + 1) / 2;

    SystemOfEquations system = new SystemOfEquations(n + 1);
    system.addEqualsEquation(new FixedEquation(0, new PixelMeasure(sum)));
    system.addEqualsEquation(new PartitionEquation(1, n, 0, PixelMeasure.ZERO));
    for (int i = 1; i < n; i++) {
      system.addEqualsEquation(new ProportionEquation(i, i + 1, (double) i, (double) i + 1));
    }
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");
    Measure[] expected = new Measure[n + 1];
    expected[0] = new PixelMeasure(sum);
    for (int i = 0; i < n; i++) {
      expected[i + 1] = new PixelMeasure(i + 1);
    }
    Assert.assertArrayEquals(expected, result);
  }

  /**
   * <pre>
   * |               900px              |
   * |     *     |     *     |     *     |
   * |           |   *   |   *   |   *   |
   * </pre>
   */
  @Test
  public void testSubPartition() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(8);
    system.addEqualsEquation(new FixedEquation(0, new PixelMeasure(900)));
    system.addEqualsEquation(new PartitionEquation(1, 3, 0, PixelMeasure.ZERO));
    system.addEqualsEquation(new CombinationEquation(4, 2, 2, PixelMeasure.ZERO));
    system.addEqualsEquation(new PartitionEquation(5, 3, 4, PixelMeasure.ZERO));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1));
    system.addEqualsEquation(new ProportionEquation(1, 3, 1, 1));
    system.addEqualsEquation(new ProportionEquation(5, 6, 1, 1));
    system.addEqualsEquation(new ProportionEquation(5, 7, 1, 1));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(900), new PixelMeasure(300), new PixelMeasure(300), new PixelMeasure(300),
        new PixelMeasure(600), new PixelMeasure(200), new PixelMeasure(200), new PixelMeasure(200)}, result);
  }

  @Test
  public void testAddVariables() {

    SystemOfEquations equations = new SystemOfEquations(0);
    Assert.assertEquals(0, equations.getNumberOfVariables());
    equations.addVariables(1);
    Assert.assertEquals(1, equations.getNumberOfVariables());
    equations.addVariables(4);
    Assert.assertEquals(5, equations.getNumberOfVariables());
  }

  /**
   * <pre>
   * |                ? px               |
   * |     *     |     *     |     *     |
   * |           |   *   |   *   |   *   |
   * </pre>
   */
  @Test
  public void testTooManyVariables() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(8);
//    system.addEqualsEquation(new FixedEquation(0, 900));
    system.addEqualsEquation(new PartitionEquation(1, 3, 0, PixelMeasure.ZERO));
    system.addEqualsEquation(new CombinationEquation(4, 2, 2, PixelMeasure.ZERO));
    system.addEqualsEquation(new PartitionEquation(5, 3, 4, PixelMeasure.ZERO));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1));
    system.addEqualsEquation(new ProportionEquation(1, 3, 1, 1));
    system.addEqualsEquation(new ProportionEquation(5, 6, 1, 1));
    system.addEqualsEquation(new ProportionEquation(5, 7, 1, 1));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(450), new PixelMeasure(150), new PixelMeasure(150), new PixelMeasure(150),
        new PixelMeasure(300), new PixelMeasure(100), new PixelMeasure(100), new PixelMeasure(100)}, result);
  }

  /**
   * <pre>
   * |            100 px           |
   * |     50px     |     50px     |
   * </pre>
   */
  @Test
  public void testTooManyEquations() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(4);
    system.addEqualsEquation(new FixedEquation(0, new PixelMeasure(100)));
    system.addEqualsEquation(new PartitionEquation(1, 2, 0, PixelMeasure.ZERO));
    system.addEqualsEquation(new FixedEquation(1, new PixelMeasure(50)));
    system.addEqualsEquation(new FixedEquation(2, new PixelMeasure(50)));
    system.addEqualsEquation(new PartitionEquation(3, 1, 2, PixelMeasure.ZERO));
    system.addEqualsEquation(new FixedEquation(3, new PixelMeasure(50)));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(100), new PixelMeasure(50), new PixelMeasure(50), new PixelMeasure(50)}, result);
  }

  /**
   * <pre>
   * |          500 px       |
   * |   100px   |     *     |
   * |   100px   |     *     |
   * </pre>
   */
  @Test
  public void testTwoSubs() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(8);
//    system.addEqualsEquation(new FixedEquation(0, 900));
    system.addEqualsEquation(new PartitionEquation(1, 3, 0, PixelMeasure.ZERO));
    system.addEqualsEquation(new CombinationEquation(4, 2, 2, PixelMeasure.ZERO));
    system.addEqualsEquation(new PartitionEquation(5, 3, 4, PixelMeasure.ZERO));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1));
    system.addEqualsEquation(new ProportionEquation(1, 3, 1, 1));
    system.addEqualsEquation(new ProportionEquation(5, 6, 1, 1));
    system.addEqualsEquation(new ProportionEquation(5, 7, 1, 1));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(450), new PixelMeasure(150), new PixelMeasure(150), new PixelMeasure(150),
        new PixelMeasure(300), new PixelMeasure(100), new PixelMeasure(100), new PixelMeasure(100)}, result);
  }

  @Test
  public void testRound() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(4);
    system.addEqualsEquation(new FixedEquation(0, new PixelMeasure(1001)));
    system.addEqualsEquation(new PartitionEquation(1, 3, 0, PixelMeasure.ZERO));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1));
    system.addEqualsEquation(new ProportionEquation(2, 3, 1, 1));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(1001), new PixelMeasure(334), new PixelMeasure(333), new PixelMeasure(334)}, result);
  }

  @Test
  public void testRoundWithCombination() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(7);
    system.addEqualsEquation(new FixedEquation(0, new PixelMeasure(310)));
    system.addEqualsEquation(new PartitionEquation(1, 2, 0, new PixelMeasure(5)));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1));
    system.addEqualsEquation(new CombinationEquation(3, 1, 1, PixelMeasure.ZERO));
    system.addEqualsEquation(new CombinationEquation(4, 1, 1, PixelMeasure.ZERO));
    system.addEqualsEquation(new CombinationEquation(5, 2, 1, PixelMeasure.ZERO));
    system.addEqualsEquation(new CombinationEquation(6, 2, 1, PixelMeasure.ZERO));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(310), new PixelMeasure(153), new PixelMeasure(152),
        new PixelMeasure(153), new PixelMeasure(153), new PixelMeasure(152), new PixelMeasure(152)}, result);
  }

  /**
   * todo later: inequations
   * <pre>≤\u2264
   * |             x₀ = 1000px        |
   * | 10 ≤ x₁ ≤ 1000 | 70 ≤ x₂ ≤ 700 |
   * </pre>
   *
   *  1  0  0 = 1000
   * -1  1  1 =    0
   *  0 -1  0 ≤  -10
   *  0  1  0 ≤ 1000
   *  0  0 -1 ≤  -70
   *  0  0  1 ≤  700
   *
   */


}
