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

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DecimalFormat;
import java.util.Arrays;

public class SystemOfEquationsUnitTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(SystemOfEquationsUnitTest.class);

  public void test2To3() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(3);
    system.addEqualsEquation(new FixedEquation(0, 1000));
    system.addEqualsEquation(new PartitionEquation(1, 3, 0));
    system.addEqualsEquation(new ProportionEquation(1, 2, 2, 3));
    system.prepare();
    system.gauss();
    system.step2();
    double[] result = system.result();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    assertEquals(new double[]{1000, 400, 600}, result, 0.000001);
  }

  public void test1To2To3To4() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(5);
    system.addEqualsEquation(new FixedEquation(0, 1000));
    system.addEqualsEquation(new PartitionEquation(1, 5, 0));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 2));
    system.addEqualsEquation(new ProportionEquation(2, 3, 2, 3));
    system.addEqualsEquation(new ProportionEquation(3, 4, 3, 4));
    system.prepare();
    system.gauss();
    system.step2();
    double[] result = system.result();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    assertEquals(new double[]{1000, 100, 200, 300, 400}, result, 0.000001);
  }

  public void test1To2To3To4To5To6To7To8To9To10() {

    long begin = System.nanoTime();

    SystemOfEquations system = new SystemOfEquations(11);
    system.addEqualsEquation(new FixedEquation(0, 1100));
    system.addEqualsEquation(new PartitionEquation(1, 11, 0));
    system.addEqualsEquation(new ProportionEquation(1, 2, 1, 2));
    system.addEqualsEquation(new ProportionEquation(2, 3, 2, 3));
    system.addEqualsEquation(new ProportionEquation(3, 4, 3, 4));
    system.addEqualsEquation(new ProportionEquation(4, 5, 4, 5));
    system.addEqualsEquation(new ProportionEquation(5, 6, 5, 6));
    system.addEqualsEquation(new ProportionEquation(6, 7, 6, 7));
    system.addEqualsEquation(new ProportionEquation(7, 8, 7, 8));
    system.addEqualsEquation(new ProportionEquation(8, 9, 8, 9));
    system.addEqualsEquation(new ProportionEquation(9, 10, 9, 10));

    system.prepare();
    system.gauss();
    system.step2();
    double[] result = system.result();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");

    assertEquals(new double[]{1100, 20, 40, 60, 80, 100, 120, 140, 160, 180, 200}, result, 0.000001);
  }

  public void test1To___To100() {

    long begin = System.nanoTime();

    int n = 100;
    int sum = n * (n + 1) / 2;

    SystemOfEquations system = new SystemOfEquations(n + 1);
    system.addEqualsEquation(new FixedEquation(0, sum));
    system.addEqualsEquation(new PartitionEquation(1, n + 1, 0));
    for (int i = 1; i < n; i++) {
      system.addEqualsEquation(new ProportionEquation(i, i + 1, (double) i, (double) i + 1));
//      system.addEqualsEquation(new int[]{i, i + 1}, new double[]{(double) i + 1, (double) -i}, 0.0);
    }

    system.prepare();
    system.gauss();
    system.step2();
    double[] result = system.result();

    long end = System.nanoTime();

    LOG.info("result: " + Arrays.toString(result));
    LOG.info("Duration: " + new DecimalFormat().format(end - begin) + " ns");
    double[] expected = new double[n + 1];
    expected[0] = sum;
    for (int i = 0; i < n; i++) {
      expected[i + 1] = i + 1;
    }
    assertEquals(expected, result, 0.000001);
  }

  public void testTime() {
    for (int i = 0; i < 100; i++) {
      test1To___To100();
    }
  }

  public static void assertEquals(double[] expected, double[] result, double delta) {
    assertEquals(expected.length, result.length);
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], result[i], delta);
    }
  }

  public void testAddVariables() {

    SystemOfEquations equations = new SystemOfEquations(0);
    assertEquals(0, equations.getNumberOfVariables());
    equations.addVariables(1);
    assertEquals(1, equations.getNumberOfVariables());
    equations.addVariables(4);
    assertEquals(5, equations.getNumberOfVariables());
  }
}
