package org.apache.myfaces.tobago.layout.grid;

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

/*
 * Date: 24.01.2008 16:31:58
 */
public class GridUnitTest extends TestCase {

  public void test1x1() {

    Grid grid = new Grid(1, 1);
    assertEquals(1, grid.getColumnCount());
    assertEquals(1, grid.getRowCount());
    assertEquals(""
        + "┌─┐\n"
        + "│◌│\n"
        + "└─┘\n", grid.toString());

    grid.add(new RealCell(null), 1, 1);
    assertEquals(1, grid.getColumnCount());
    assertEquals(1, grid.getRowCount());
    assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┗━┛\n", grid.toString());

    grid.add(new RealCell(null), 1, 1);
    assertEquals(1, grid.getColumnCount());
    assertEquals(2, grid.getRowCount());
    assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┗━┛\n", grid.toString());

    grid.add(new RealCell(null), 1, 2);
    assertEquals(1, grid.getColumnCount());
    assertEquals(4, grid.getRowCount());
    assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┠─┨\n"
        + "┃⬇┃\n"
        + "┗━┛\n", grid.toString());

    // with warning
    grid.add(new RealCell(null), 2, 1);
    assertEquals(1, grid.getColumnCount());
    assertEquals(5, grid.getRowCount());
    assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┠─┨\n"
        + "┃⬇┃\n"
        + "┣━┫\n"
        + "┃✖┃\n"
        + "┗━┛\n", grid.toString());
  }

  public void test2x1() {

    Grid grid = new Grid(2, 1);
    assertEquals(2, grid.getColumnCount());
    assertEquals(1, grid.getRowCount());
    assertEquals(""
        + "┌─┬─┐\n"
        + "│◌│◌│\n"
        + "└─┴─┘\n", grid.toString());

    grid.add(new RealCell(null), 1, 1);
    assertEquals(2, grid.getColumnCount());
    assertEquals(1, grid.getRowCount());
    assertEquals(""
        + "┏━┱─┐\n"
        + "┃█┃◌│\n"
        + "┗━┹─┘\n", grid.toString());

    grid.add(new RealCell(null), 1, 1);
    assertEquals(2, grid.getColumnCount());
    assertEquals(1, grid.getRowCount());
    assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┗━┻━┛\n", grid.toString());

    grid.add(new RealCell(null), 2, 2);
    assertEquals(2, grid.getColumnCount());
    assertEquals(3, grid.getRowCount());
    assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┣━╇━┫\n"
        + "┃█│➞┃\n"
        + "┠─┼─┨\n"
        + "┃⬇│⬇┃\n"
        + "┗━┷━┛\n", grid.toString());

    grid.add(new RealCell(null), 1, 2);
    assertEquals(2, grid.getColumnCount());
    assertEquals(5, grid.getRowCount());
    assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┣━╇━┫\n"
        + "┃█│➞┃\n"
        + "┠─┼─┨\n"
        + "┃⬇│⬇┃\n"
        + "┣━╈━┩\n"
        + "┃█┃◌│\n"
        + "┠─╂─┤\n"
        + "┃⬇┃◌│\n"
        + "┗━┹─┘\n", grid.toString());

    grid.add(new RealCell(null), 1, 1);
    assertEquals(2, grid.getColumnCount());
    assertEquals(5, grid.getRowCount());
    assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┣━╇━┫\n"
        + "┃█│➞┃\n"
        + "┠─┼─┨\n"
        + "┃⬇│⬇┃\n"
        + "┣━╈━┫\n"
        + "┃█┃█┃\n"
        + "┠─╊━┩\n"
        + "┃⬇┃◌│\n"
        + "┗━┹─┘\n", grid.toString());

    grid.add(new RealCell(null), 1, 2);
    assertEquals(2, grid.getColumnCount());
    assertEquals(6, grid.getRowCount());
    assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┣━╇━┫\n"
        + "┃█│➞┃\n"
        + "┠─┼─┨\n"
        + "┃⬇│⬇┃\n"
        + "┣━╈━┫\n"
        + "┃█┃█┃\n"
        + "┠─╊━┫\n"
        + "┃⬇┃█┃\n"
        + "┡━╉─┨\n"
        + "│◌┃⬇┃\n"
        + "└─┺━┛\n", grid.toString());

    grid.add(new RealCell(null), 2, 1);
    // fehler
    assertEquals(2, grid.getColumnCount());
    assertEquals(6, grid.getRowCount());
  }

  public void test5x5() {

    Grid grid = new Grid(5, 5);
    assertEquals(5, grid.getColumnCount());
    assertEquals(5, grid.getRowCount());
    assertEquals(""
        + "┌─┬─┬─┬─┬─┐\n"
        + "│◌│◌│◌│◌│◌│\n"
        + "├─┼─┼─┼─┼─┤\n"
        + "│◌│◌│◌│◌│◌│\n"
        + "├─┼─┼─┼─┼─┤\n"
        + "│◌│◌│◌│◌│◌│\n"
        + "├─┼─┼─┼─┼─┤\n"
        + "│◌│◌│◌│◌│◌│\n"
        + "├─┼─┼─┼─┼─┤\n"
        + "│◌│◌│◌│◌│◌│\n"
        + "└─┴─┴─┴─┴─┘\n", grid.toString());

    grid.add(new RealCell(null), 1, 2);
    grid.add(new RealCell(null), 1, 3);
    grid.add(new RealCell(null), 1, 1);
    grid.add(new RealCell(null), 2, 1);
    grid.add(new RealCell(null), 3, 1);
    grid.add(new RealCell(null), 1, 1);
    grid.add(new RealCell(null), 1, 1);
    grid.add(new RealCell(null), 1, 3);
    grid.add(new RealCell(null), 1, 1);
    grid.add(new RealCell(null), 3, 1);
    grid.add(new RealCell(null), 1, 2);
    grid.add(new RealCell(null), 2, 1);
    grid.add(new RealCell(null), 1, 1);
    assertEquals(5, grid.getColumnCount());
    assertEquals(5, grid.getRowCount());
    assertEquals(""
        + "┏━┳━┳━┳━┯━┓\n"
        + "┃█┃█┃█┃█│➞┃\n"
        + "┠─╂─╊━╇━┿━┫\n"
        + "┃⬇┃⬇┃█│➞│➞┃\n"
        + "┣━╉─╊━╈━╈━┫\n"
        + "┃█┃⬇┃█┃█┃█┃\n"
        + "┣━╇━╇━╉─╊━┫\n"
        + "┃█│➞│➞┃⬇┃█┃\n"
        + "┣━┿━╈━╉─╂─┨\n"
        + "┃█│➞┃█┃⬇┃⬇┃\n"
        + "┗━┷━┻━┻━┻━┛\n", grid.toString());
  }
}
