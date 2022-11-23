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

package org.apache.myfaces.tobago.internal.util;

import org.apache.myfaces.test.el.MockValueExpression;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SortingUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testSheetValueNull() {
    final UISheet sheet = new UISheet();
    final UIColumn column = new UIColumn();
    sheet.getChildren().add(column);
    SortingUtils.sort(sheet, null);
  }

  @Test
  public void testNoChild() {
    final List<Fruit> list = Fruit.getFreshFruits();

    final UISheet sheet = new UISheet();
    sheet.getState().updateSortState("id");
    final UIColumn column = new UIColumn();
    column.setId("id");
    sheet.getChildren().add(column);
    sheet.setValue(list);
    SortingUtils.sort(sheet, null);

    // not sorted
    Assertions.assertEquals(Apple.GOLDEN_DELICIOUS, list.get(0));
    Assertions.assertEquals(Apple.SCHOENER_AUS_BOSKOOP, list.get(1));
    Assertions.assertEquals(Pear.WILLIAMS_CHRIST, list.get(2));
    Assertions.assertEquals(Pear.KOESTLICHE_AUS_CHARNEUX, list.get(3));
  }

  /**
   * @deprecated
   */
  @Test
  @Deprecated
  public void testUIOutDeprected() {
    final List<Fruit> list = Fruit.getFreshFruits();
    final UISheet sheet = new UISheet();
    sheet.setMaxSortColumns(1);
    sheet.getState().setSortedColumnId("id");
    sheet.setVar("var");
    final UIColumn column = new UIColumn();
    column.setId("id");
    sheet.getChildren().add(column);
    sheet.setValue(list);
    final UIOut out = new UIOut();
    column.getChildren().add(out);
    out.setValueExpression(Attributes.value.getName(),
        new MockValueExpression("#{var.name}", String.class));
    Assertions.assertNotNull(out.getValueExpression(Attributes.value.getName()));

    SortingUtils.sort(sheet, null);

    Assertions.assertEquals(Apple.GOLDEN_DELICIOUS, list.get(0));
    Assertions.assertEquals(Pear.KOESTLICHE_AUS_CHARNEUX, list.get(1));
    Assertions.assertEquals(Apple.SCHOENER_AUS_BOSKOOP, list.get(2));
    Assertions.assertEquals(Pear.WILLIAMS_CHRIST, list.get(3));

    sheet.getState().setAscending(false);
    SortingUtils.sort(sheet, null);

    Assertions.assertEquals(Pear.WILLIAMS_CHRIST, list.get(0));
    Assertions.assertEquals(Apple.SCHOENER_AUS_BOSKOOP, list.get(1));
    Assertions.assertEquals(Pear.KOESTLICHE_AUS_CHARNEUX, list.get(2));
    Assertions.assertEquals(Apple.GOLDEN_DELICIOUS, list.get(3));
  }

  @Test
  public void testUIOut() {
    final List<Fruit> list = Fruit.getFreshFruits();
    final UISheet sheet = new UISheet();
    sheet.setMaxSortColumns(1);
    sheet.getState().updateSortState("id");
    sheet.setVar("var");
    final UIColumn column = new UIColumn();
    column.setId("id");
    sheet.getChildren().add(column);
    sheet.setValue(list);
    final UIOut out = new UIOut();
    column.getChildren().add(out);
    out.setValueExpression(Attributes.value.getName(),
        new MockValueExpression("#{var.name}", String.class));
    Assertions.assertNotNull(out.getValueExpression(Attributes.value.getName()));

    SortingUtils.sort(sheet, null);

    Assertions.assertEquals(Apple.GOLDEN_DELICIOUS, list.get(0));
    Assertions.assertEquals(Pear.KOESTLICHE_AUS_CHARNEUX, list.get(1));
    Assertions.assertEquals(Apple.SCHOENER_AUS_BOSKOOP, list.get(2));
    Assertions.assertEquals(Pear.WILLIAMS_CHRIST, list.get(3));

    sheet.getState().updateSortState("id");
    SortingUtils.sort(sheet, null);

    Assertions.assertEquals(Pear.WILLIAMS_CHRIST, list.get(0));
    Assertions.assertEquals(Apple.SCHOENER_AUS_BOSKOOP, list.get(1));
    Assertions.assertEquals(Pear.KOESTLICHE_AUS_CHARNEUX, list.get(2));
    Assertions.assertEquals(Apple.GOLDEN_DELICIOUS, list.get(3));
  }

  @Test
  public void testUILink() {
    final List<Fruit> list = Fruit.getFreshFruits();
    final UISheet sheet = new UISheet();
    sheet.getState().updateSortState("id");
    final UIColumn column = new UIColumn();
    column.setId("id");
    sheet.getChildren().add(column);
    sheet.setValue(list);
    final UILink link = new UILink();
    column.getChildren().add(link);
    link.setValueExpression(Attributes.label.getName(),
        new MockValueExpression("#{var.name}", String.class));

    Assertions.assertNotNull(link.getValueExpression(Attributes.label.getName()));

    SortingUtils.sort(sheet, null);

    Assertions.assertEquals(Apple.GOLDEN_DELICIOUS, list.get(0));
    Assertions.assertEquals(Apple.SCHOENER_AUS_BOSKOOP, list.get(1));
    Assertions.assertEquals(Pear.WILLIAMS_CHRIST, list.get(2));
    Assertions.assertEquals(Pear.KOESTLICHE_AUS_CHARNEUX, list.get(3));
  }

}
