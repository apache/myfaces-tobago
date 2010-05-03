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

public class PartitionEquationUnitTest {

  @Test
  public void testToString() {
    Assert.assertEquals("PartitionEquation:    x_0 = x_4 + x_5 (test)",
        new PartitionEquation(4, 1, 0, px(0), px(0), px(0), "test").toString());
    Assert.assertEquals("PartitionEquation:    x_0 = x_4 + x_5 + x_6 (test)",
        new PartitionEquation(4, 2, 0, px(0), px(0), px(0), "test").toString());
    Assert.assertEquals("PartitionEquation:    x_0 = x_4 + ... + x_6 + x_7 (test)",
        new PartitionEquation(4, 3, 0, px(0), px(0), px(0), "test").toString());

    Assert.assertEquals("PartitionEquation:    x_0 = x_4 + x_5 (test)",
        new PartitionEquation(4, 1, 0, px(5), px(0), px(0), "test").toString());
    Assert.assertEquals("PartitionEquation:    x_0 = x_4 + x_5 + 5px + x_6 (test)",
        new PartitionEquation(4, 2, 0, px(5), px(0), px(0), "test").toString());
    Assert.assertEquals("PartitionEquation:    x_0 = x_4 + ... + x_6 + 2 * 5px + x_7 (test)",
        new PartitionEquation(4, 3, 0, px(5), px(0), px(0), "test").toString());
  }

  @Test
  public void testFillRow() {

    int length = 9;
    double[] row;

    row = new PartitionEquation(4, 1, 0, px(0), px(0), px(0), "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, 0, 0, 0, 1, 1, 0, 0, 0}, row, MathUtils.EPSILON);
    row = new PartitionEquation(4, 2, 0, px(0), px(0), px(0), "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, 0, 0, 0, 1, 1, 1, 0, 0}, row, MathUtils.EPSILON);
    row = new PartitionEquation(4, 3, 0, px(0), px(0), px(0), "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, 0, 0, 0, 1, 1, 1, 1, 0}, row, MathUtils.EPSILON);

    row = new PartitionEquation(4, 1, 0, px(5), px(0), px(0), "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, 0, 0, 0, 1, 1, 0, 0, 0}, row, MathUtils.EPSILON);
    row = new PartitionEquation(4, 2, 0, px(5), px(0), px(0), "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, 0, 0, 0, 1, 1, 1, 0, -5}, row, MathUtils.EPSILON);
    row = new PartitionEquation(4, 3, 0, px(5), px(0), px(0), "test").fillRow(length);
    Assert.assertArrayEquals(new double[] {-1, 0, 0, 0, 1, 1, 1, 1, -10}, row, MathUtils.EPSILON);
  }
  
  private Measure px(int pixel) {
    return Measure.valueOf(pixel);
  }
  
}