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

import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Orientation;
import org.junit.Assert;
import org.junit.Test;

public class GridUnitTest {

  @Test
  public void test1x1() {

    final Grid grid = new Grid(LayoutTokens.parse("*"), LayoutTokens.parse("*"));
    Assert.assertEquals(1, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(1, grid.getBankHeads(Orientation.VERTICAL).length);
    Assert.assertEquals(""
        + "┌─┐\n"
        + "│◌│\n"
        + "└─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(1, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(1, grid.getBankHeads(Orientation.VERTICAL).length);
    Assert.assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┗━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(1, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(2, grid.getBankHeads(Orientation.VERTICAL).length);
    Assert.assertEquals(""
        + "┏━┓\n"
        + "┃█┃\n"
        + "┣━┫\n"
        + "┃█┃\n"
        + "┗━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 2);
    Assert.assertEquals(1, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(4, grid.getBankHeads(Orientation.VERTICAL).length);
    Assert.assertEquals(""
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
    Assert.assertEquals(1, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(5, grid.getBankHeads(Orientation.VERTICAL).length);
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
        + "┗━┛\n", grid.gridAsString());
  }

  @Test
  public void test2x1() {

    final Grid grid = new Grid(LayoutTokens.parse("*;*"), LayoutTokens.parse("*"));
    Assert.assertEquals(2, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(1, grid.getBankHeads(Orientation.VERTICAL).length);
    Assert.assertEquals(""
        + "┌─┬─┐\n"
        + "│◌│◌│\n"
        + "└─┴─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(2, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(1, grid.getBankHeads(Orientation.VERTICAL).length);
    Assert.assertEquals(""
        + "┏━┱─┐\n"
        + "┃█┃◌│\n"
        + "┗━┹─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(2, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(1, grid.getBankHeads(Orientation.VERTICAL).length);
    Assert.assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┗━┻━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 2, 2);
    Assert.assertEquals(2, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(3, grid.getBankHeads(Orientation.VERTICAL).length);
    Assert.assertEquals(""
        + "┏━┳━┓\n"
        + "┃█┃█┃\n"
        + "┣━╇━┫\n"
        + "┃█│➞┃\n"
        + "┠─┼─┨\n"
        + "┃⬇│⬇┃\n"
        + "┗━┷━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 2);
    Assert.assertEquals(2, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(5, grid.getBankHeads(Orientation.VERTICAL).length);
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
        + "┗━┹─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 1);
    Assert.assertEquals(2, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(5, grid.getBankHeads(Orientation.VERTICAL).length);
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
        + "┗━┹─┘\n", grid.gridAsString());

    grid.add(new OriginCell(null), 1, 2);
    Assert.assertEquals(2, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(6, grid.getBankHeads(Orientation.VERTICAL).length);
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
        + "└─┺━┛\n", grid.gridAsString());

    grid.add(new OriginCell(null), 2, 1);
    // fehler
    Assert.assertEquals(2, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(6, grid.getBankHeads(Orientation.VERTICAL).length);
  }

  @Test
  public void test5x5() {

    final Grid grid = new Grid(LayoutTokens.parse("*;*;*;*;*"), LayoutTokens.parse("*;*;*;*;*"));
    Assert.assertEquals(5, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(5, grid.getBankHeads(Orientation.VERTICAL).length);
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
    Assert.assertEquals(5, grid.getBankHeads(Orientation.HORIZONTAL).length);
    Assert.assertEquals(5, grid.getBankHeads(Orientation.VERTICAL).length);
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
        + "┗━┷━┻━┻━┻━┛\n", grid.gridAsString());
  }
}


