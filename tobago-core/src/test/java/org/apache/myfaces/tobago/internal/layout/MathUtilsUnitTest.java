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

package org.apache.myfaces.tobago.internal.layout;

import org.junit.Assert;
import org.junit.Test;

public class MathUtilsUnitTest {

  @Test
  public void testAdjust() {
    double[] d = {6.3, 7.9, 8.7, 9.2, 10.3, 11.6};
    MathUtils.adjustRemainders(d, 0.0);
    Assert.assertArrayEquals("mixed", new double[]{6, 8, 9, 9, 10, 12}, d, MathUtils.EPSILON);
  }

  @Test
  public void testAdjust999() {
    double[] d = {9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9, 9.9};
    MathUtils.adjustRemainders(d, 0.0);
    Assert.assertArrayEquals(
        "9.9, ...", new double[]{9, 10, 10, 10, 10, 10, 10, 10, 10, 10}, d, MathUtils.EPSILON);
  }

  @Test
  public void testAdjust111() {
    double[] d = {1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1};
    MathUtils.adjustRemainders(d, 0.0);
    Assert.assertArrayEquals(
        "1.1, ...", new double[]{1, 2, 1, 1, 1, 1, 1, 1, 1, 1}, d, MathUtils.EPSILON);
  }

  @Test
  public void testAdjust133() {
    double[] d = {1, 1, 1, 1.333333333, 1.333333333, 1.333333333, 1, 1, 1};
    MathUtils.adjustRemainders(d, 0.0);
    Assert.assertArrayEquals(
        "1, ..., 1.333...", new double[]{1, 1, 1, 1, 2, 1, 1, 1, 1}, d, MathUtils.EPSILON);
  }

  @Test
  public void testInitialBias() {
    double[] d = {5.5};
    MathUtils.adjustRemainders(d, 0.5);
    Assert.assertArrayEquals("initial bias", new double[]{5}, d, MathUtils.EPSILON);
  }


  @Test
  public void testIsZero() {
    Assert.assertTrue(MathUtils.isZero(0));
    Assert.assertFalse(MathUtils.isZero(1.0/1000.0));
    Assert.assertTrue(MathUtils.isZero(1.0/1000000000.0));
    Assert.assertTrue(MathUtils.isZero(-1.0/1000000000.0));
  }

  @Test
  public void testIsNotZero() {
    Assert.assertFalse(MathUtils.isNotZero(0));
    Assert.assertTrue(MathUtils.isNotZero(1.0/1000.0));
    Assert.assertFalse(MathUtils.isNotZero(1.0/1000000000.0));
    Assert.assertFalse(MathUtils.isNotZero(-1.0/1000000000.0));
  }

  @Test
  public void testIsInteger() {
    Assert.assertTrue(MathUtils.isInteger(1.0));
    Assert.assertTrue(MathUtils.isInteger(0.0));
    Assert.assertTrue(MathUtils.isInteger(1.0/1000000000.0));
    Assert.assertFalse(MathUtils.isInteger(1.0/1000.0));
  }

  @Test
  public void testIsNotInteger() {
    Assert.assertFalse(MathUtils.isNotInteger(1.0));
    Assert.assertFalse(MathUtils.isNotInteger(0.0));
    Assert.assertFalse(MathUtils.isNotInteger(1.0/1000000000.0));
    Assert.assertTrue(MathUtils.isNotInteger(1.0/1000.0));
  }
}
