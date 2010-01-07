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

import org.apache.myfaces.tobago.internal.layout.MathUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.junit.Assert;
import org.junit.Test;

public class CombinationEquationUnitTest {

  @Test
  public void testToString() {
    
    Assert.assertEquals("CombinationEquation:  x_0 = x_4 (test)",
        new CombinationEquation(4, 0, 1, Measure.ZERO, null, "test").toString());
    Assert.assertEquals("CombinationEquation:  x_0 + x_1 = x_4 (test)",
        new CombinationEquation(4, 0, 2, Measure.ZERO, null, "test").toString());
    Assert.assertEquals("CombinationEquation:  x_0 + ... + x_2 = x_4 (test)",
        new CombinationEquation(4, 0, 3, Measure.ZERO, null, "test").toString());

    Assert.assertEquals("CombinationEquation:  x_0 = x_4 (test)",
        new CombinationEquation(4, 0, 1, px(5), null, "test").toString());
    Assert.assertEquals("CombinationEquation:  x_0 + x_1 + 5px = x_4 (test)",
        new CombinationEquation(4, 0, 2, px(5), null, "test").toString());
    Assert.assertEquals("CombinationEquation:  x_0 + ... + x_2 + 2 * 5px = x_4 (test)",
        new CombinationEquation(4, 0, 3, px(5), null, "test").toString());
  }

  @Test
  public void testFillRow() {
    int length = 8;
    double[] row;

    row = new CombinationEquation(4, 0, 1, Measure.ZERO, null, "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, 0, 0, 0, 1, 0, 0, 0}, row, MathUtils.EPSILON);
    row = new CombinationEquation(4, 0, 2, Measure.ZERO, null, "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, -1, 0, 0, 1, 0, 0, 0}, row, MathUtils.EPSILON);
    row = new CombinationEquation(4, 0, 3, Measure.ZERO, null, "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, -1, -1, 0, 1, 0, 0, 0}, row, MathUtils.EPSILON);

    row = new CombinationEquation(4, 0, 1, px(5), null, "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, 0, 0, 0, 1, 0, 0, 0}, row, MathUtils.EPSILON);
    row = new CombinationEquation(4, 0, 2, px(5), null, "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, -1, 0, 0, 1, 0, 0, 5}, row, MathUtils.EPSILON);
    row = new CombinationEquation(4, 0, 3, px(5), null, "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, -1, -1, 0, 1, 0, 0, 10}, row, MathUtils.EPSILON);
  }

  private Measure px(int pixel) {
    return Measure.valueOf(pixel);
  }
  
}
