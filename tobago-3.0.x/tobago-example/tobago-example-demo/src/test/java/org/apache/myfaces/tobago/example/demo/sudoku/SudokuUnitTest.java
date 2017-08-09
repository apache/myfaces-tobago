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

package org.apache.myfaces.tobago.example.demo.sudoku;

import org.junit.Assert;
import org.junit.Test;

public class SudokuUnitTest {

  private static final Sudoku TRIVIAL = new Sudoku(new byte[]{
      0, 1, 2, 3, 4, 5, 6, 7, 8,
      3, 4, 5, 6, 7, 8, 0, 1, 2,
      6, 7, 8, 0, 1, 2, 3, 4, 5,

      1, 2, 3, 4, 5, 6, 7, 8, 0,
      4, 5, 6, 7, 8, 0, 1, 2, 3,
      7, 8, 0, 1, 2, 3, 4, 5, 6,

      2, 3, 4, 5, 6, 7, 8, 0, 1,
      5, 6, 7, 8, 0, 1, 2, 3, 4,
      8, 0, 1, 2, 3, 4, 5, 6, 7,
  });

  private static final Sudoku WRONG = new Sudoku(new byte[]{
      0, -1, -1, -1, -1, -1, -1, -1, 0,
      -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1,

      -1, -1, -1, -1, 0, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, 0, -1, -1, -1,

      -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, 0, -1, -1, -1,
  });

  @Test
  public void testCheckRowRules() {
    Assert.assertTrue(TRIVIAL.checkRowRules());
    Assert.assertFalse(WRONG.checkRowRules());
  }

  @Test
  public void testCheckColumnRules() {
    Assert.assertTrue(TRIVIAL.checkColumnRules());
    Assert.assertFalse(WRONG.checkColumnRules());
  }

  @Test
  public void testCheckSquareRules() {
    Assert.assertTrue(TRIVIAL.checkSquareRules());
    Assert.assertFalse(WRONG.checkSquareRules());
  }

}
