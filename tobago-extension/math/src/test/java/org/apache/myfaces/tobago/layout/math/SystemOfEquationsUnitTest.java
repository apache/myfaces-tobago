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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.junit.Assert;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.Arrays;

public class SystemOfEquationsUnitTest {

  private static final Logger LOG = LoggerFactory.getLogger(SystemOfEquationsUnitTest.class);

  @Test
  public void test2To3() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(3);
    system.addEqualsEquation(new FixedEquation(0, px(1005), "test"));
    system.addEqualsEquation(new PartitionEquation(1, 2, 0, px(5), px(0), px(0), "test"));
    system.addEqualsEquation(new ProportionEquation(1, 2, 2, 3, "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(1005), px(400), px(600)}, result);
  }

  @Test
  public void test1To2To3To4() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(5);
    system.addEqualsEquation(new FixedEquation(0, px(1015), "test"));
    system.addEqualsEquation(new PartitionEquation(1, 4, 0, px(5), px(0), px(0), "test"));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 2, "test"));
    system.addEqualsEquation(new ProportionEquation(2, 3, 2, 3, "test"));
    system.addEqualsEquation(new ProportionEquation(3, 4, 3, 4, "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(1015), px(100), px(200), px(300),
        px(400)}, result);
  }

  @Test
  public void test1To2To3To4To5To6To7To8To9To10() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(11);
    system.addEqualsEquation(new FixedEquation(0, px(1100), "test"));
    system.addEqualsEquation(new PartitionEquation(1, 10, 0, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 2, "test"));
    system.addEqualsEquation(new ProportionEquation(2, 3, 2, 3, "test"));
    system.addEqualsEquation(new ProportionEquation(3, 4, 3, 4, "test"));
    system.addEqualsEquation(new ProportionEquation(4, 5, 4, 5, "test"));
    system.addEqualsEquation(new ProportionEquation(5, 6, 5, 6, "test"));
    system.addEqualsEquation(new ProportionEquation(6, 7, 6, 7, "test"));
    system.addEqualsEquation(new ProportionEquation(7, 8, 7, 8, "test"));
    system.addEqualsEquation(new ProportionEquation(8, 9, 8, 9, "test"));
    system.addEqualsEquation(new ProportionEquation(9, 10, 9, 10, "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(1100), px(20), px(40), px(60),
        px(80), px(100), px(120), px(140),
        px(160), px(180), px(200)}, result);
  }

  @Test
  public void test1To___To100() {

    long begin = System.nanoTime();

    int n = 100;
    int sum = n * (n + 1) / 2;

    SystemOfEquations system = new SystemOfEquations(n + 1);
    system.addEqualsEquation(new FixedEquation(0, px(sum), "test"));
    system.addEqualsEquation(new PartitionEquation(1, n, 0, px(0), px(0), px(0), "test"));
    for (int i = 1; i < n; i++) {
      system.addEqualsEquation(new ProportionEquation(i, i + 1, (double) i, (double) i + 1, "test"));
    }
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");
    Measure[] expected = new Measure[n + 1];
    expected[0] = px(sum);
    for (int i = 0; i < n; i++) {
      expected[i + 1] = px(i + 1);
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

    SystemOfEquations system = new SystemOfEquations(10);
    system.addEqualsEquation(new FixedEquation(0, px(900), "test"));
    system.addEqualsEquation(new PartitionEquation(1, 3, 0, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new CombinationEquation(5, 2, 2, px(0), new RelativeLayoutToken(1), "test"));
    system.addEqualsEquation(new PartitionEquation(6, 3, 5, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(1, 3, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(6, 7, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(6, 8, 1, 1, "test"));
    system.addEqualsEquation(new RemainderEquation(4, tokens(), "test"));
    system.addEqualsEquation(new RemainderEquation(9, tokens(), "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(900), px(300), px(300), px(300), px(0),
        px(600), px(200), px(200), px(200), px(0)},
        result);
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

    SystemOfEquations system = new SystemOfEquations(10);
//    system.addEqualsEquation(new FixedEquation(0, 900)); // e. g. this is the missing equation
    system.addEqualsEquation(new PartitionEquation(1, 3, 0, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new CombinationEquation(5, 2, 2, px(0), new RelativeLayoutToken(1), "test"));
    system.addEqualsEquation(new PartitionEquation(6, 3, 5, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(1, 3, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(6, 7, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(6, 8, 1, 1, "test"));
    system.addEqualsEquation(new RemainderEquation(4, tokens(), "test"));
    system.addEqualsEquation(new RemainderEquation(9, tokens(), "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(450), px(150), px(150), px(150), px(0), px(300), px(100), px(100), px(100), px(0)},
        result);
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
    system.addEqualsEquation(new FixedEquation(0, px(100), "test"));
    system.addEqualsEquation(new PartitionEquation(1, 2, 0, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new FixedEquation(1, px(50), "test"));
    system.addEqualsEquation(new FixedEquation(2, px(50), "test"));
    system.addEqualsEquation(new PartitionEquation(3, 1, 2, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new FixedEquation(3, px(50), "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(100), px(50), px(50), px(50)}, result);
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

    SystemOfEquations system = new SystemOfEquations(10);
//    system.addEqualsEquation(new FixedEquation(0, 900));
    system.addEqualsEquation(new PartitionEquation(1, 3, 0, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new CombinationEquation(5, 2, 2, px(0), new RelativeLayoutToken(1), "test"));
    system.addEqualsEquation(new PartitionEquation(6, 3, 5, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(1, 3, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(6, 7, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(6, 8, 1, 1, "test"));
    system.addEqualsEquation(new RemainderEquation(4, tokens(), "test"));
    system.addEqualsEquation(new RemainderEquation(9, tokens(), "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(450), px(150), px(150), px(150), px(0),
        px(300), px(100), px(100), px(100), px(0)},
        result);
  }

  @Test
  public void testRound() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(4);
    system.addEqualsEquation(new FixedEquation(0, px(1001), "test"));
    system.addEqualsEquation(new PartitionEquation(1, 3, 0, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1, "test"));
    system.addEqualsEquation(new ProportionEquation(2, 3, 1, 1, "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(1001), px(333), px(334), px(334)}, result);
  }

  @Test
  public void testRoundWithCombination() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(8);
    system.addEqualsEquation(new FixedEquation(0, px(310), "test"));
    system.addEqualsEquation(new PartitionEquation(1, 2, 0, px(5), px(0), px(0), "test"));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1, "test"));
    system.addEqualsEquation(new CombinationEquation(4, 1, 1, px(0), new RelativeLayoutToken(1), "test"));
    system.addEqualsEquation(new CombinationEquation(5, 1, 1, px(0), new RelativeLayoutToken(1), "test"));
    system.addEqualsEquation(new CombinationEquation(6, 2, 1, px(0), new RelativeLayoutToken(1), "test"));
    system.addEqualsEquation(new CombinationEquation(7, 2, 1, px(0), new RelativeLayoutToken(1), "test"));
    system.addEqualsEquation(new RemainderEquation(3, tokens(), "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(310), px(152), px(153), px(0), px(152), px(152), px(153), px(153)}, result);
  }

  @Test
  public void testRoundWithPartitionToPartition() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(6);
    system.addEqualsEquation(new FixedEquation(0, px(11), "test"));
    system.addEqualsEquation(new PartitionEquation(1, 2, 0, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 1, "test"));
    system.addEqualsEquation(new PartitionEquation(4, 1, 1, px(0), px(0), px(0), "test"));
    system.addEqualsEquation(new RemainderEquation(3, tokens(), "test"));
    system.addEqualsEquation(new RemainderEquation(5, tokens(), "test"));
    Measure[] result = system.solve();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    Assert.assertArrayEquals(new Measure[]{
        px(11), px(5), px(6), px(0), px(5), px(0)}, result);
  }

  /*
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

  private Measure px(int pixel) {
    return Measure.valueOf(pixel);
  }

  private LayoutTokens tokens() {
    return LayoutTokens.parse("*");
  }

}
