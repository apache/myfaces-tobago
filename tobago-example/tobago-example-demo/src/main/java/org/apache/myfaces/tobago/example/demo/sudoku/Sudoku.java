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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * This is a demo of the logic of a sudoku game.
 *
 * The basic idea is not to write yet an other sudoku, but to demonstrate application specific controls.
 */
public class Sudoku {

  private static final Logger LOG = LoggerFactory.getLogger(Sudoku.class);

  private static final Random RANDOM = new Random(System.currentTimeMillis());

  private byte[] field;
  private Stack<Byte> undefined;

  private int depth;
  private int maxDepth;

  public Sudoku() {
    field = new byte[]{
        0, 1, 2, 3, 4, 5, 6, 7, 8,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,

        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,

        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,
    };
    final RandomList randomList = new RandomList((byte) 81);
    randomList.removeSmallest(9);
    depth = 9;
    undefined = randomList.asStack();

  }

  public Sudoku(final byte[] field) {
    this.field = field;
    //XXX  undefined = new RandomList((byte) 81).asStack();
  }

  public Result solve() {
    if (undefined.isEmpty()) {
      LOG.debug("--------------- result ");
      LOG.debug(this.toString());
      LOG.debug("--------------- result ");
      return Result.UNIQUE;
    }
    final byte position = undefined.pop();
    final RandomList list = new RandomList((byte) 9);
    boolean foundOne = false;
    while (!list.isEmpty()) {
      field[position] = list.next();
//      LOG.debug(depth);
//      LOG.debug(this);
      if (checkRules()) {
//        LOG.debug("ok");
        final Result result = solve2();
        switch (result) {
          case ERROR:
            break;
          case MULTIPLE:
            return Result.MULTIPLE;
          case UNIQUE:
            if (foundOne) {
              return Result.MULTIPLE;
            } else {
              foundOne = true;
            }
            break;
          default:
            assert(false);
        }
      }
    }
    undefined.push(position);
    field[position] = -1;

    return Result.ERROR;
  }

  private Result solve2() {
    depth++;
    if (depth > maxDepth) {
      maxDepth = depth;
      LOG.debug("new max depth: " + maxDepth);
    }
    final Result result = solve();
    depth--;
    return result;
  }


  private boolean checkRules() {
    return checkRowRules() && checkColumnRules() && checkSquareRules();
  }

  protected boolean checkRowRules() {
    for (int i = 0; i < 9; i++) {
      final BitSet xxx = new BitSet();
      for (int j = 0; j < 9; j++) {
        final byte value = field[i * 9 + j];
        if (value != -1) {
          if (xxx.get(value)) {
//            LOG.debug("fail h " + i);
            return false;
          }
          xxx.set(value);
        }
      }
    }
    return true;
  }

  protected boolean checkColumnRules() {
    for (int i = 0; i < 9; i++) {
      final BitSet xxx = new BitSet();
      for (int j = 0; j < 9; j++) {
        final byte value = field[j * 9 + i];
        if (value != -1) {
          if (xxx.get(value)) {
//            LOG.debug("fail v " + i);
            return false;
          }
          xxx.set(value);
        }
      }
    }
    return true;
  }

  protected boolean checkSquareRules() {
    for (int i = 0; i < 9; i++) {
      final BitSet xxx = new BitSet();
      for (int j = 0; j < 9; j++) {
        final int i1 = (i % 3) * 3 + i / 3 * 27 + j % 3 + j / 3 * 9;
        final byte value = field[i1];
        if (value != -1) {
          if (xxx.get(value)) {
//            LOG.debug("fail 3 " + i);
            return false;
          }
          xxx.set(value);
        }
      }
    }
    return true;
  }

  public static void main(final String[] args) {
    LOG.debug("" + new RandomList((byte) 9).list);
    final Sudoku sudoku = new Sudoku();
    LOG.debug("---------------------------------------------------------------------------------------------");
    final Result result = sudoku.solve();
    LOG.debug("" + result);
    LOG.debug("---------------------------------------------------------------------------------------------");
    LOG.debug("" + sudoku);
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < field.length; i++) {
      if (field[i] == -1) {
        builder.append('-');
      } else {
        builder.append(field[i] + 1);
      }
      if (i % 9 != 8) {
        builder.append(' ');
      } else {
        builder.append('\n');
      }
    }
    return builder.toString();
  }

  /**
   * Creates a random list of the numbers 1, 2, 3, ..., n like 4, n-1, 3, ... n-3.
   */
  private static class RandomList {

    private byte n;
    private List<Byte> list;

    private RandomList(final byte n) {
      this.n = n;
      list = new ArrayList<Byte>(n);
      shuffle();
    }

    public void shuffle() {
      final List<Byte> temp = new ArrayList<Byte>(n);
      for (byte i = 0; i < n; i++) {
        temp.add(i, i);
      }
      for (byte i = n; i > 0; i--) {
        final byte index = (byte) RANDOM.nextInt(i);
        list.add(temp.remove(index));
      }
    }

    public byte next() {
      return list.remove(0);
    }

    public boolean isEmpty() {
      return list.isEmpty();
    }

    public Stack<Byte> asStack() {
      final Stack<Byte> stack = new Stack<Byte>();
      for (final Byte aByte : list) {
        stack.add(aByte);
      }
      return stack;
    }

    public void removeSmallest(final int n) {
      for (byte i = 0; i < n; i++) {
        list.remove((Byte) i);
      }
    }
  }

  private enum Result {
    UNIQUE,
    MULTIPLE,
    ERROR
  }
}
