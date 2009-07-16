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

import org.junit.Assert;
import org.junit.Test;

public class GridUnitTest {

  @Test
  public void test1x1() {

    Grid grid = new Grid(1, 1);
    Assert.assertEquals(1, grid.getColumnCount());
    Assert.assertEquals(1, grid.getRowCount());
    Assert.assertEquals(""
        + "┌─┐\n"
        + "│◌│\n"
        + "└─┘\n", grid.toString());

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(1, grid.getColumnCount());
    Assert.assertEquals(1, grid.getRowCount());
    Assert.assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┗━┛\n", grid.toString());

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(1, grid.getColumnCount());
    Assert.assertEquals(2, grid.getRowCount());
    Assert.assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┗━┛\n", grid.toString());

    grid.add(new OriginCell(null), 1, 2);
    Assert.assertEquals(1, grid.getColumnCount());
    Assert.assertEquals(4, grid.getRowCount());
    Assert.assertEquals(""
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
    grid.add(new OriginCell(null), 2, 1);
    Assert.assertEquals(1, grid.getColumnCount());
    Assert.assertEquals(5, grid.getRowCount());
    Assert.assertEquals(""
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

  @Test
  public void test2x1() {

    Grid grid = new Grid(2, 1);
    Assert.assertEquals(2, grid.getColumnCount());
    Assert.assertEquals(1, grid.getRowCount());
    Assert.assertEquals(""
        + "┌─┬─┐\n"
        + "│◌│◌│\n"
        + "└─┴─┘\n", grid.toString());

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(2, grid.getColumnCount());
    Assert.assertEquals(1, grid.getRowCount());
    Assert.assertEquals(""
        + "┏━┱─┐\n"
        + "┃█┃◌│\n"
        + "┗━┹─┘\n", grid.toString());

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(2, grid.getColumnCount());
    Assert.assertEquals(1, grid.getRowCount());
    Assert.assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┗━┻━┛\n", grid.toString());

    grid.add(new OriginCell(null), 2, 2);
    Assert.assertEquals(2, grid.getColumnCount());
    Assert.assertEquals(3, grid.getRowCount());
    Assert.assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┣━╇━┫\n"
        + "┃█│➞┃\n"
        + "┠─┼─┨\n"
        + "┃⬇│⬇┃\n"
        + "┗━┷━┛\n", grid.toString());

    grid.add(new OriginCell(null), 1, 2);
    Assert.assertEquals(2, grid.getColumnCount());
    Assert.assertEquals(5, grid.getRowCount());
    Assert.assertEquals(""
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

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(2, grid.getColumnCount());
    Assert.assertEquals(5, grid.getRowCount());
    Assert.assertEquals(""
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

    grid.add(new OriginCell(null), 1, 2);
    Assert.assertEquals(2, grid.getColumnCount());
    Assert.assertEquals(6, grid.getRowCount());
    Assert.assertEquals(""
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

    grid.add(new OriginCell(null), 2, 1);
    // fehler
    Assert.assertEquals(2, grid.getColumnCount());
    Assert.assertEquals(6, grid.getRowCount());
  }

  @Test
  public void test5x5() {

    Grid grid = new Grid(5, 5);
    Assert.assertEquals(5, grid.getColumnCount());
    Assert.assertEquals(5, grid.getRowCount());
    Assert.assertEquals(""
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

    grid.add(new OriginCell(null), 1, 2);
    grid.add(new OriginCell(null), 1, 3);
    grid.add(new OriginCell(null), 1, 1);
    grid.add(new OriginCell(null), 2, 1);
    grid.add(new OriginCell(null), 3, 1);
    grid.add(new OriginCell(null), 1, 1);
    grid.add(new OriginCell(null), 1, 1);
    grid.add(new OriginCell(null), 1, 3);
    grid.add(new OriginCell(null), 1, 1);
    grid.add(new OriginCell(null), 3, 1);
    grid.add(new OriginCell(null), 1, 2);
    grid.add(new OriginCell(null), 2, 1);
    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(5, grid.getColumnCount());
    Assert.assertEquals(5, grid.getRowCount());
    Assert.assertEquals(""
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
