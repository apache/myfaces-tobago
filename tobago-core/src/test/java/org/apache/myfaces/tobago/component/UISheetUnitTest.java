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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.faces.model.ListDataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UISheetUnitTest extends AbstractTobagoTestBase {

  private static final String[] DATA = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
  private List<String> nineRows;
  private UISheet data;
  private UISheet unknown;

  @Override
  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
    nineRows = new ArrayList<>();
    Collections.addAll(nineRows, DATA);

    data = new UISheet();
    data.setValue(new ListDataModel(nineRows));

    unknown = new UISheet();
    unknown.setValue(new DataModelWithUnknownRows(nineRows));
  }

  @Test
  public void test5RowsPerPage() {
    data.setRows(5);
    // having rowCount 9 and 5 rows to display it looks like this (rotated, X X X X X means one page with 5 rows):
    // X X X X X
    // X X X X
    Assertions.assertEquals(2, data.getPages(), "number of pages");
  }

  @Test
  public void test9RowsPerPage() {
    data.setRows(9);
    // X X X X X X X X X
    Assertions.assertEquals(1, data.getPages(), "number of pages");
  }

  @Test
  public void test2RowsPerPage() {
    data.setRows(2);
    // X X
    // X X
    // X X
    // X X
    // X
    Assertions.assertEquals(5, data.getPages(), "number of pages");
  }

  @Test
  public void test3RowsPerPage() {
    data.setRows(3);
    // X X X
    // X X X
    // X X X
    Assertions.assertEquals(3, data.getPages(), "number of pages");
  }

  @Test
  public void test1RowPerPage() {
    data.setRows(1);
    // X
    // X
    // X
    // X
    // X
    // X
    // X
    // X
    // X
    Assertions.assertEquals(9, data.getPages(), "number of pages");
  }

  @Test
  public void testAllRowsPerPage() {
    data.setRows(0); // zero means all
    // X X X X X X X X X
    Assertions.assertEquals(1, data.getPages(), "number of pages");
  }

  @Test
  public void testCurrentPageRows5() {
    data.setRows(5);
    // initially first = 0
    // * X X X X
    // X X X X
    Assertions.assertEquals(0, data.getCurrentPage(), "current page");
    Assertions.assertEquals(true, data.isAtBeginning(), "is at beginning");

    data.setFirst(5);
    // now we set the first (show as an F)
    // X X X X X
    // * X X X
    Assertions.assertEquals(1, data.getCurrentPage(), "current page");
    Assertions.assertEquals(false, data.isAtBeginning(), "is at beginning");

    data.setFirst(0);
    // * X X X X
    // X X X X
    Assertions.assertEquals(0, data.getCurrentPage(), "current page");
    Assertions.assertEquals(true, data.isAtBeginning(), "is at beginning");

    data.setFirst(4);
    // X X X X *
    // X X X X
    Assertions.assertEquals(0, data.getCurrentPage(), "current page");
    Assertions.assertEquals(false, data.isAtBeginning(), "is at beginning");

    data.setFirst(100);
    // out of rage
    Assertions.assertEquals(1, data.getCurrentPage(), "current page");
    Assertions.assertEquals(false, data.isAtBeginning(), "is at beginning");
  }

  @Test
  public void testCurrentPageRows20() {
    data.setRows(20); // more rows than data entries
    // initially first = 0
    // * X X X X X X X X
    Assertions.assertEquals(0, data.getCurrentPage(), "current page");
    Assertions.assertEquals(true, data.isAtBeginning(), "is at beginning");

    data.setFirst(8);
    // now we set the first (show as an F)
    // X X X X X X X X *
    Assertions.assertEquals(0, data.getCurrentPage(), "current page");
    Assertions.assertEquals(false, data.isAtBeginning(), "is at beginning");

    data.setFirst(0);
    // * X X X X X X X X
    Assertions.assertEquals(0, data.getCurrentPage(), "current page");
    Assertions.assertEquals(true, data.isAtBeginning(), "is at beginning");

    data.setFirst(100);
    // out of range
    Assertions.assertEquals(0, data.getCurrentPage(), "current page");
    Assertions.assertEquals(false, data.isAtBeginning(), "is at beginning");
  }

  @Test
  public void testRowData() {
    data.setRowIndex(0);
    Assertions.assertEquals("one", data.getRowData());
    data.setRowIndex(8);
    Assertions.assertEquals("nine", data.getRowData());
    data.setRowIndex(9);
    try {
      data.getRowData();
      Assertions.fail();
    } catch (final IllegalArgumentException e) {
      // okay: row is unavailable
    }
  }

  @Test
  public void testHasRowCount() {
    Assertions.assertEquals(true, data.hasRowCount(), "has row count");
    Assertions.assertEquals(false, unknown.hasRowCount(), "has row count");
  }

  @Test
  public void testRowsUnlimited() {
    data.setRows(5);
    unknown.setRows(5);

    Assertions.assertEquals(false, data.isRowsUnlimited(), "unlimited rows");
    Assertions.assertEquals(false, unknown.isRowsUnlimited(), "unlimited rows");

    data.setRows(0);
    unknown.setRows(0);

    Assertions.assertEquals(true, data.isRowsUnlimited(), "unlimited rows");
    Assertions.assertEquals(true, unknown.isRowsUnlimited(), "unlimited rows");
  }

  @Test
  public void testNeedMoreThanOnePage() {

    // known length:

    data.setRows(0);
    // * X X X X X X X X
    Assertions.assertEquals(false, data.needMoreThanOnePage(), "need more than one page");

    data.setRows(5);
    // * X X X X
    // X X X X
    Assertions.assertEquals(true, data.needMoreThanOnePage(), "need more than one page");

    data.setRows(20);
    // * X X X X X X X X
    Assertions.assertEquals(false, data.needMoreThanOnePage(), "need more than one page");

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    Assertions.assertEquals(false, unknown.needMoreThanOnePage(), "need more than one page");

    unknown.setRows(5);
    // * X X X X
    // X X X X
    Assertions.assertEquals(true, unknown.needMoreThanOnePage(), "need more than one page");

    unknown.setRows(20);
    // * X X X X X X X X
    Assertions.assertEquals(true, unknown.needMoreThanOnePage(), "need more than one page");
  }

  @Test
  public void testFirstRowIndexOfLastPage() {

    // known length:

    data.setRows(0);
    // * X X X X X X X X
    Assertions.assertEquals(0, data.getFirstRowIndexOfLastPage(), "first row index of last page");

    data.setRows(5);
    // * X X X X
    // X X X X
    Assertions.assertEquals(5, data.getFirstRowIndexOfLastPage(), "first row index of last page");

    data.setRows(20);
    // * X X X X X X X X
    Assertions.assertEquals(0, data.getFirstRowIndexOfLastPage(), "first row index of last page");

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    Assertions.assertEquals(0, unknown.getFirstRowIndexOfLastPage(), "first row index of last page");

    unknown.setRows(5);
    // * X X X X
    // X X X X
    try {
      unknown.getFirstRowIndexOfLastPage();
      Assertions.fail("first row index of last page");
    } catch (final IllegalArgumentException e) {
      // okay: last page can't determined
    }

    unknown.setRows(20);
    // * X X X X X X X X
    try {
      unknown.getFirstRowIndexOfLastPage();
      Assertions.fail("first row index of last page");
    } catch (final IllegalArgumentException e) {
      // okay: last page can't determined
    }
  }

  @Test
  public void testLastRowIndexOfCurrentPage() {

    // known length:

    data.setRows(0);
    // * X X X X X X X X
    Assertions.assertEquals(9, data.getLastRowIndexOfCurrentPage(), "last row index of current page");

    data.setRows(5);
    // * X X X X
    // X X X X
    Assertions.assertEquals(5, data.getLastRowIndexOfCurrentPage(), "last row index of current page");

    data.setRows(20);
    // * X X X X X X X X
    Assertions.assertEquals(9, data.getLastRowIndexOfCurrentPage(), "last row index of current page");

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    try {
      unknown.getLastRowIndexOfCurrentPage();
      Assertions.fail("last row index of current page");
    } catch (final IllegalArgumentException e) {
      // okay: last row index of current page can't determined
    }

    unknown.setRows(5);
    // * X X X X
    // X X X X
    try {
      unknown.getLastRowIndexOfCurrentPage();
      Assertions.fail("last row index of current page");
    } catch (final IllegalArgumentException e) {
      // okay: last row index of current page can't determined
    }

    unknown.setRows(20);
    // * X X X X X X X X
    try {
      unknown.getLastRowIndexOfCurrentPage();
      Assertions.fail("last row index of current page");
    } catch (final IllegalArgumentException e) {
      // okay: last row index of current page can't determined
    }
  }

  @Test
  public void testGetCurrentPageOnUnknown() {

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    Assertions.assertEquals(0, unknown.getCurrentPage(), "current page");

    unknown.setRows(5);
    // * X X X X
    // X X X X
    Assertions.assertEquals(0, unknown.getCurrentPage(), "current page");

    unknown.setFirst(5);
    // X X X X X
    // * X X X
    Assertions.assertEquals(1, unknown.getCurrentPage(), "current page");
  }

  @Test
  public void testGetPagesOnUnknown() {

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    Assertions.assertEquals(1, unknown.getPages(), "pages");

    unknown.setRows(5);
    // * X X X X
    // X X X X
    try {
      unknown.getPages();
      Assertions.fail("pages");
    } catch (final IllegalArgumentException e) {
      // okay: pages can't determined
    }
  }

  @Test
  public void testDynamicRemoval() {
    nineRows.remove(0);
    nineRows.remove(0);
    nineRows.remove(0);
    data.setRows(5);
    Assertions.assertEquals(2, data.getPages());
    nineRows.remove(0);
    nineRows.remove(0);
    nineRows.remove(0);
    Assertions.assertEquals(1, data.getPages());
  }

  @Test
  public void testStripRowIndex() {
    Assertions.assertEquals("comp1:comp2", new UISheet().stripRowIndex("123:comp1:comp2"));
    Assertions.assertEquals("comp1:comp2", new UISheet().stripRowIndex("comp1:comp2"));
  }

  private static class DataModelWithUnknownRows extends ListDataModel {

    DataModelWithUnknownRows(final List list) {
      super(list);
    }

    @Override
    public int getRowCount() {
      return -1;
    }
  }

}
