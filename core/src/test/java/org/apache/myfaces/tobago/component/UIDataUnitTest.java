/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import junit.framework.TestCase;

import javax.faces.model.ArrayDataModel;

/**
 *
 *
 * @author bommel (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIDataUnitTest extends TestCase {
  String [] nineRows =
      { "one", "two", "three", "four", "five",
          "six", "seven" , "eight", "nine" };

  public void testPage() {
    UIData data = new UIData();
    data.setValue(new ArrayDataModel(nineRows));
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

    data.setRows(1);
    data.setFirst(8);
    assertEquals(9, data.getPage());
  }
}
