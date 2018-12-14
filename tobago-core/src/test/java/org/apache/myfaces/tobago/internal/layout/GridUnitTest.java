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

import org.apache.myfaces.tobago.layout.MeasureList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GridUnitTest {

  @Test
  public void test1x1() {

    final Grid grid = new Grid(MeasureList.parse("*"), MeasureList.parse("*"));
    Assertions.assertEquals(1, grid.getColumns().getSize());
    Assertions.assertEquals(1, grid.getRows().getSize());
    Assertions.assertEquals(""
        + "┌─┐\n"
        + "│◌│\n"
        + "└─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assertions.assertEquals(1, grid.getColumns().getSize());
    Assertions.assertEquals(1, grid.getRows().getSize());
    Assertions.assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┗━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assertions.assertEquals(1, grid.getColumns().getSize());
    Assertions.assertEquals(2, grid.getRows().getSize());
    Assertions.assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┗━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 2);
    Assertions.assertEquals(1, grid.getColumns().getSize());
    Assertions.assertEquals(4, grid.getRows().getSize());
    Assertions.assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┠─┨\n"
        + "┃⬇┃\n"
        + "┗━┛\n", grid.gridAsString());

    // with warning
    grid.add(new OriginCell(null), 2, 1);
    Assertions.assertEquals(1, grid.getColumns().getSize());
    Assertions.assertEquals(5, grid.getRows().getSize());
    Assertions.assertEquals(""
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
        + "┗━┛\n", grid.gridAsString());
  }

  @Test
  public void test2x1() {

    final Grid grid = new Grid(MeasureList.parse("*;*"), MeasureList.parse("*"));
    Assertions.assertEquals(2, grid.getColumns().getSize());
    Assertions.assertEquals(1, grid.getRows().getSize());
    Assertions.assertEquals(""
        + "┌─┬─┐\n"
        + "│◌│◌│\n"
        + "└─┴─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assertions.assertEquals(2, grid.getColumns().getSize());
    Assertions.assertEquals(1, grid.getRows().getSize());
    Assertions.assertEquals(""
        + "┏━┱─┐\n"
        + "┃█┃◌│\n"
        + "┗━┹─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assertions.assertEquals(2, grid.getColumns().getSize());
    Assertions.assertEquals(1, grid.getRows().getSize());
    Assertions.assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┗━┻━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 2, 2);
    Assertions.assertEquals(2, grid.getColumns().getSize());
    Assertions.assertEquals(3, grid.getRows().getSize());
    Assertions.assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┣━╇━┫\n"
        + "┃█│➞┃\n"
        + "┠─┼─┨\n"
        + "┃⬇│⬇┃\n"
        + "┗━┷━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 2);
    Assertions.assertEquals(2, grid.getColumns().getSize());
    Assertions.assertEquals(5, grid.getRows().getSize());
    Assertions.assertEquals(""
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
        + "┗━┹─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assertions.assertEquals(2, grid.getColumns().getSize());
    Assertions.assertEquals(5, grid.getRows().getSize());
    Assertions.assertEquals(""
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
        + "┗━┹─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 2);
    Assertions.assertEquals(2, grid.getColumns().getSize());
    Assertions.assertEquals(6, grid.getRows().getSize());
    Assertions.assertEquals(""
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
        + "└─┺━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 2, 1);
    // fehler
    Assertions.assertEquals(2, grid.getColumns().getSize());
    Assertions.assertEquals(6, grid.getRows().getSize());
  }

  @Test
  public void test5x5() {

    final Grid grid = new Grid(MeasureList.parse("*;*;*;*;*"), MeasureList.parse("*;*;*;*;*"));
    Assertions.assertEquals(5, grid.getColumns().getSize());
    Assertions.assertEquals(5, grid.getRows().getSize());
    Assertions.assertEquals(""
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
        + "└─┴─┴─┴─┴─┘\n", grid.gridAsString());

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
    Assertions.assertEquals(5, grid.getColumns().getSize());
    Assertions.assertEquals(5, grid.getRows().getSize());
    Assertions.assertEquals(""
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
        + "┗━┷━┻━┻━┻━┛\n", grid.gridAsString());
  }
}


