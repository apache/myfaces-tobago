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

public final class MathUtils {

  /**
   * Values smaller than this EPSILON should be treated as zero.
   */
  public static final double EPSILON = 0.0000001;

  private MathUtils() {
  }

  /**
   * Adjusts the list of double values to rounded values with the same sum.
   * E. g. 2.3, 2.4, 2.5, 2.8 -> 2.0, 2.0, 3.0, 3.0
   */
  public static void adjustRemainders(final double[] list, double bias) {
    for (final double ignored : list) {
      final double lastBias;
      if (bias < 0.0) {
        lastBias = findAndAdjustMaxRemainder(list);
      } else {
        lastBias = findAndAdjustMinRemainder(list);
      }
      if (isZero(lastBias)) {
        break;
      }
      bias += lastBias;
    }

    assert isZero(bias);
  }

  public static double findAndAdjustMaxRemainder(final double[] list) {
    double max = 0.0;
    Integer indexOfMax = null;
    for (int i = 0; i < list.length; i++) {
      double remainder = remainder(list[i]);
      if (remainder > max + EPSILON) {
        max = remainder;
        indexOfMax = i;
      }
    }
    if (indexOfMax != null) {
      list[indexOfMax] += 1.0 - max;
      return 1.0 - max;
    }
    return 0.0;
  }

  public static double findAndAdjustMinRemainder(final double[] list) {
    double min = 1.0;
    Integer indexOfMin = null;
    for (int i = 0; i < list.length; i++) {
      final double remainder = remainder(list[i]);
      if (remainder == 0) { // is zero
        continue;
      }
      if (remainder < min - EPSILON) {
        min = remainder;
        indexOfMin = i;
      }
    }
    if (indexOfMin != null) {
      list[indexOfMin] -= min;
      return -min;
    }
    return 0.0;
  }

  public static double remainder(final double v) {
    return v - Math.floor(v);
  }

  public static boolean isZero(final double factor) {
    return Math.abs(factor) < EPSILON;
  }

  public static boolean isNotZero(final double factor) {
    return Math.abs(factor) >= EPSILON;
  }

  public static boolean isInteger(final double value) {
    return isZero(value - Math.round(value));
  }

  public static boolean isNotInteger(final double value) {
    return isNotZero(value - Math.round(value));
  }
}
