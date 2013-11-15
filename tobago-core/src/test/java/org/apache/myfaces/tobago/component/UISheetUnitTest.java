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

import org.apache.myfaces.tobago.internal.mock.faces.AbstractTobagoTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.faces.model.ListDataModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UISheetUnitTest extends AbstractTobagoTestBase {

  private static final String[] DATA = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
  private List<String> nineRows;
  private UISheet data;
  private UISheet unknown;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    nineRows = new ArrayList<String>();
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
    Assert.assertEquals("number of pages", 2, data.getPages());
  }

  @Test
  public void test9RowsPerPage() {
    data.setRows(9);
    // X X X X X X X X X
    Assert.assertEquals("number of pages", 1, data.getPages());
  }

  @Test
  public void test2RowsPerPage() {
    data.setRows(2);
    // X X
    // X X
    // X X
    // X X
    // X
    Assert.assertEquals("number of pages", 5, data.getPages());
  }

  @Test
  public void test3RowsPerPage() {
    data.setRows(3);
    // X X X
    // X X X
    // X X X
    Assert.assertEquals("number of pages", 3, data.getPages());
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
    Assert.assertEquals("number of pages", 9, data.getPages());
  }

  @Test
  public void testAllRowsPerPage() {
    data.setRows(0); // zero means all
    // X X X X X X X X X
    Assert.assertEquals("number of pages", 1, data.getPages());
  }

  @Test
  public void testCurrentPageRows5() {
    data.setRows(5);
    // initially first = 0
    // * X X X X
    // X X X X
    Assert.assertEquals("current page", 0, data.getCurrentPage());
    Assert.assertEquals("is at beginning", true, data.isAtBeginning());

    data.setFirst(5);
    // now we set the first (show as an F)
    // X X X X X
    // * X X X
    Assert.assertEquals("current page", 1, data.getCurrentPage());
    Assert.assertEquals("is at beginning", false, data.isAtBeginning());

    data.setFirst(0);
    // * X X X X
    // X X X X
    Assert.assertEquals("current page", 0, data.getCurrentPage());
    Assert.assertEquals("is at beginning", true, data.isAtBeginning());

    data.setFirst(4);
    // X X X X *
    // X X X X
    Assert.assertEquals("current page", 0, data.getCurrentPage());
    Assert.assertEquals("is at beginning", false, data.isAtBeginning());

    data.setFirst(100);
    // out of rage
    Assert.assertEquals("current page", 1, data.getCurrentPage());
    Assert.assertEquals("is at beginning", false, data.isAtBeginning());
  }

  @Test
  public void testCurrentPageRows20() {
    data.setRows(20); // more rows than data entries
    // initially first = 0
    // * X X X X X X X X
    Assert.assertEquals("current page", 0, data.getCurrentPage());
    Assert.assertEquals("is at beginning", true, data.isAtBeginning());

    data.setFirst(8);
    // now we set the first (show as an F)
    // X X X X X X X X *
    Assert.assertEquals("current page", 0, data.getCurrentPage());
    Assert.assertEquals("is at beginning", false, data.isAtBeginning());

    data.setFirst(0);
    // * X X X X X X X X
    Assert.assertEquals("current page", 0, data.getCurrentPage());
    Assert.assertEquals("is at beginning", true, data.isAtBeginning());

    data.setFirst(100);
    // out of range
    Assert.assertEquals("current page", 0, data.getCurrentPage());
    Assert.assertEquals("is at beginning", false, data.isAtBeginning());
  }

  @Test
  public void testRowData() {
    data.setRowIndex(0);
    Assert.assertEquals("one", data.getRowData());
    data.setRowIndex(8);
    Assert.assertEquals("nine", data.getRowData());
    data.setRowIndex(9);
    try {
      data.getRowData();
      Assert.fail();
    } catch (final IllegalArgumentException e) {
      // okay: row is unavailable
    }
  }

  @Test
  public void testHasRowCount() {
    Assert.assertEquals("has row count", true, data.hasRowCount());
    Assert.assertEquals("has row count", false, unknown.hasRowCount());
  }

  @Test
  public void testRowsUnlimited() {
    data.setRows(5);
    unknown.setRows(5);

    Assert.assertEquals("unlimited rows", false, data.isRowsUnlimited());
    Assert.assertEquals("unlimited rows", false, unknown.isRowsUnlimited());

    data.setRows(0);
    unknown.setRows(0);

    Assert.assertEquals("unlimited rows", true, data.isRowsUnlimited());
    Assert.assertEquals("unlimited rows", true, unknown.isRowsUnlimited());
  }

  @Test
  public void testNeedMoreThanOnePage() {

    // known length:

    data.setRows(0);
    // * X X X X X X X X
    Assert.assertEquals("need more than one page", false, data.needMoreThanOnePage());

    data.setRows(5);
    // * X X X X
    // X X X X
    Assert.assertEquals("need more than one page", true, data.needMoreThanOnePage());

    data.setRows(20);
    // * X X X X X X X X
    Assert.assertEquals("need more than one page", false, data.needMoreThanOnePage());

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    Assert.assertEquals("need more than one page", false, unknown.needMoreThanOnePage());

    unknown.setRows(5);
    // * X X X X
    // X X X X
    Assert.assertEquals("need more than one page", true, unknown.needMoreThanOnePage());

    unknown.setRows(20);
    // * X X X X X X X X
    Assert.assertEquals("need more than one page", true, unknown.needMoreThanOnePage());
  }

  @Test
  public void testFirstRowIndexOfLastPage() {

    // known length:

    data.setRows(0);
    // * X X X X X X X X
    Assert.assertEquals("first row index of last page", 0, data.getFirstRowIndexOfLastPage());

    data.setRows(5);
    // * X X X X
    // X X X X
    Assert.assertEquals("first row index of last page", 5, data.getFirstRowIndexOfLastPage());

    data.setRows(20);
    // * X X X X X X X X
    Assert.assertEquals("first row index of last page", 0, data.getFirstRowIndexOfLastPage());

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    Assert.assertEquals("first row index of last page", 0, unknown.getFirstRowIndexOfLastPage());

    unknown.setRows(5);
    // * X X X X
    // X X X X
    try {
      unknown.getFirstRowIndexOfLastPage();
      Assert.fail("first row index of last page");
    } catch (final IllegalArgumentException e) {
      // okay: last page can't determined
    }

    unknown.setRows(20);
    // * X X X X X X X X
    try {
      unknown.getFirstRowIndexOfLastPage();
      Assert.fail("first row index of last page");
    } catch (final IllegalArgumentException e) {
      // okay: last page can't determined
    }
  }

  @Test
  public void testLastRowIndexOfCurrentPage() {

    // known length:

    data.setRows(0);
    // * X X X X X X X X
    Assert.assertEquals("last row index of current page", 9, data.getLastRowIndexOfCurrentPage());

    data.setRows(5);
    // * X X X X
    // X X X X
    Assert.assertEquals("last row index of current page", 5, data.getLastRowIndexOfCurrentPage());

    data.setRows(20);
    // * X X X X X X X X
    Assert.assertEquals("last row index of current page", 9, data.getLastRowIndexOfCurrentPage());

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    try {
      unknown.getLastRowIndexOfCurrentPage();
      Assert.fail("last row index of current page");
    } catch (final IllegalArgumentException e) {
      // okay: last row index of current page can't determined
    }

    unknown.setRows(5);
    // * X X X X
    // X X X X
    try {
      unknown.getLastRowIndexOfCurrentPage();
      Assert.fail("last row index of current page");
    } catch (final IllegalArgumentException e) {
      // okay: last row index of current page can't determined
    }

    unknown.setRows(20);
    // * X X X X X X X X
    try {
      unknown.getLastRowIndexOfCurrentPage();
      Assert.fail("last row index of current page");
    } catch (final IllegalArgumentException e) {
      // okay: last row index of current page can't determined
    }
  }

  @Test
  public void testGetCurrentPageOnUnknown() {

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    Assert.assertEquals("current page", 0, unknown.getCurrentPage());

    unknown.setRows(5);
    // * X X X X
    // X X X X
    Assert.assertEquals("current page", 0, unknown.getCurrentPage());

    unknown.setFirst(5);
    // X X X X X
    // * X X X
    Assert.assertEquals("current page", 1, unknown.getCurrentPage());
  }

  @Test
  public void testGetPagesOnUnknown() {

    // unknown length:

    unknown.setRows(0);
    // * X X X X X X X X
    Assert.assertEquals("pages", 1, unknown.getPages());

    unknown.setRows(5);
    // * X X X X
    // X X X X
    try {
      unknown.getPages();
      Assert.fail("pages");
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
    Assert.assertEquals(2, data.getPages());
    nineRows.remove(0);
    nineRows.remove(0);
    nineRows.remove(0);
    Assert.assertEquals(1, data.getPages());
  }

  @Test
  public void testStripRowIndex() {
    Assert.assertEquals("comp1:comp2", new UISheet().stripRowIndex("123:comp1:comp2"));
    Assert.assertEquals("comp1:comp2", new UISheet().stripRowIndex("comp1:comp2"));
  }

  private static class DataModelWithUnknownRows extends ListDataModel {

    public DataModelWithUnknownRows(final List list) {
      super(list);
    }

    @Override
    public int getRowCount() {
      return -1;
    }
  }
  
}
