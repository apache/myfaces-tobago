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

import jakarta.faces.component.UISelectItemGroups;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectOneList;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class SelectItemUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void simple() {
    UISelectOneList oneList = new UISelectOneList();
    Iterable<SelectItem> itemIterator = SelectItemUtils.getItemIterator(facesContext, oneList);
    Assertions.assertFalse(itemIterator.iterator().hasNext());

    UISelectItem item = new UISelectItem();
    oneList.getChildren().add(item);
    itemIterator = SelectItemUtils.getItemIterator(facesContext, oneList);
    Iterator<SelectItem> iterator = itemIterator.iterator();
    Assertions.assertTrue(iterator.hasNext());
    Assertions.assertNotNull(iterator.next());
    Assertions.assertFalse(iterator.hasNext());
  }

  @Test
  void groups() {
    UISelectOneList oneList = new UISelectOneList();
    UISelectItemGroups groups = new UISelectItemGroups();
    UISelectItem item = new UISelectItem();
    groups.getChildren().add(item);
    oneList.getChildren().add(groups);
    Iterable<SelectItem> itemIterator = SelectItemUtils.getItemIterator(facesContext, oneList);
    Iterator<SelectItem> iterator = itemIterator.iterator();
    Assertions.assertTrue(iterator.hasNext());
    Object object = iterator.next();
    Assertions.assertNotNull(object);
    Assertions.assertInstanceOf(SelectItemGroup.class, object);
    SelectItemGroup selectItemGroup = (SelectItemGroup) object;
    SelectItem[] items = selectItemGroup.getSelectItems();
    Assertions.assertNotNull(items);
    Assertions.assertEquals(1, items.length);
    Assertions.assertFalse(iterator.hasNext());
  }
}
