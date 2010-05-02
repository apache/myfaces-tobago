package org.apache.myfaces.tobago.component;

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

import javax.faces.model.ListDataModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UISheetUnitTest {
  private String[] nineRows =
      {"one", "two", "three", "four", "five",
          "six", "seven", "eight", "nine"};

  @Test
  public void testPage() {
    List<String> list = new ArrayList<String>();
    list.addAll(Arrays.asList(nineRows));
    UISheet data = new UISheet();
    data.setValue(new ListDataModel(list));
    data.setRows(5);
    Assert.assertEquals(1, data.getPage());
    Assert.assertEquals(2, data.getPages());

    data.setRows(9);
    Assert.assertEquals(1, data.getPage());
    Assert.assertEquals(1, data.getPages());

    data.setRows(2);
    Assert.assertEquals(1, data.getPage());
    Assert.assertEquals(5, data.getPages());

    data.setRows(3);
    Assert.assertEquals(1, data.getPage());
    Assert.assertEquals(3, data.getPages());


    data.setRows(1);
    Assert.assertEquals(1, data.getPage());
    Assert.assertEquals(9, data.getPages());

    data.setRows(5);
    data.setFirst(5);
    Assert.assertEquals(2, data.getPage());

    data.setRows(9);
    data.setFirst(6);
    Assert.assertEquals(1, data.getPage());

    data.setRows(2);
    data.setFirst(0);
    Assert.assertEquals(1, data.getPage());

    data.setRows(2);
    data.setFirst(1);
    Assert.assertEquals(1, data.getPage());

    data.setRows(2);
    data.setFirst(2);
    Assert.assertEquals(2, data.getPage());

    data.setRows(2);
    data.setFirst(3);
    Assert.assertEquals(2, data.getPage());

    data.setRows(2);
    data.setFirst(6);
    Assert.assertEquals(4, data.getPage());
    //TODO enable this
    /*data.setRows(1);
    data.setFirst(8);
    data.setRowIndex(8);
    Assert.assertEquals(data.getRowData(), list.get(8));
    Assert.assertEquals(9, data.getPage());
    Assert.assertEquals(9, data.getPages());

    list.remove(8);
    Assert.assertEquals(list.size(), data.getRowCount());
    data.setFirst(0);
    Assert.assertEquals(1, data.getPage());
    Assert.assertEquals(8, data.getPages());
      */
  }

  @Test
  public void testStripRowIndex() {
    Assert.assertEquals("comp1:comp2", new UISheet().stripRowIndex("123:comp1:comp2"));
    Assert.assertEquals("comp1:comp2", new UISheet().stripRowIndex("comp1:comp2"));
  }

}
