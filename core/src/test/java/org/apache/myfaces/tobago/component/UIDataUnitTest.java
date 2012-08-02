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

import junit.framework.TestCase;

import javax.faces.model.ListDataModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author bommel (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIDataUnitTest extends TestCase {

  String [] nineRows =
      { "one", "two", "three", "four", "five",
          "six", "seven" , "eight", "nine" };

  public void testPage() {
    List<String> list = new ArrayList<String>();
    list.addAll(Arrays.asList(nineRows));
    UIData data = new UIData();
    data.setValue(new ListDataModel(list));
    data.setRows(5);
    assertEquals(1, data.getPage());
    assertEquals(2, data.getPages());

    data.setRows(9);
    assertEquals(1, data.getPage());
    assertEquals(1, data.getPages());

    data.setRows(2);
    assertEquals(1, data.getPage());
    assertEquals(5, data.getPages());

    data.setRows(3);
    assertEquals(1, data.getPage());
    assertEquals(3, data.getPages());


    data.setRows(1);
    assertEquals(1, data.getPage());
    assertEquals(9, data.getPages());

    data.setRows(5);
    data.setFirst(5);
    assertEquals(2, data.getPage());

    data.setRows(9);
    data.setFirst(6);
    assertEquals(1, data.getPage());

    data.setRows(2);
    data.setFirst(0);
    assertEquals(1, data.getPage());

    data.setRows(2);
    data.setFirst(1);
    assertEquals(1, data.getPage());

    data.setRows(2);
    data.setFirst(2);
    assertEquals(2, data.getPage());

    data.setRows(2);
    data.setFirst(3);
    assertEquals(2, data.getPage());

    data.setRows(2);
    data.setFirst(6);
    assertEquals(4, data.getPage());
     //TODO enable this
    /*data.setRows(1);
    data.setFirst(8);
    data.setRowIndex(8);
    assertEquals(data.getRowData(), list.get(8));
    assertEquals(9, data.getPage());
    assertEquals(9, data.getPages());

    list.remove(8);
    assertEquals(list.size(), data.getRowCount());
    data.setFirst(0);
    assertEquals(1, data.getPage());
    assertEquals(8, data.getPages());
      */
  }

  public void testStripRowIndex() {
    assertEquals("comp1:comp2", new UIData().stripRowIndex("123:comp1:comp2"));
    assertEquals("comp1:comp2", new UIData().stripRowIndex("comp1:comp2"));
  }

}
