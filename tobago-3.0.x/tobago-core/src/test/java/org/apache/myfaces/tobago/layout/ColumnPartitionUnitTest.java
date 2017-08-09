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

package org.apache.myfaces.tobago.layout;

import org.junit.Assert;
import org.junit.Test;

public class ColumnPartitionUnitTest {

  @Test
  public void testArray() {
    Assert.assertArrayEquals(
        new Integer[]{12}, new ColumnPartition().getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{12}, new ColumnPartition(12).getParts());
    Assert.assertArrayEquals(
        new Integer[]{1, 11}, new ColumnPartition(1, 11).getParts());
    Assert.assertArrayEquals(
        new Integer[]{1, 11}, new ColumnPartition(1, 12).getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{1, 2, 3, 4, 2}, new ColumnPartition(1, 2, 3, 4, 5).getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{1, 1, 1, 1, 1, 7}, new ColumnPartition(-1, -2, -3, -4, -5).getParts()); // will be fixed
  }

  @Test
  public void testString() {
    Assert.assertArrayEquals(
        new Integer[]{12}, ColumnPartition.valueOf("").getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{12}, ColumnPartition.valueOf("12").getParts());
    Assert.assertArrayEquals(
        new Integer[]{1, 11}, ColumnPartition.valueOf("1;11").getParts());
    Assert.assertArrayEquals(
        new Integer[]{1, 11}, ColumnPartition.valueOf("1;12").getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{1, 2, 3, 4, 2}, ColumnPartition.valueOf("1;2;3;4;5").getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{1, 1, 1, 1, 1, 7}, ColumnPartition.valueOf("-1;-2;-3;-4;-5").getParts()); // will be fixed
  }

}
