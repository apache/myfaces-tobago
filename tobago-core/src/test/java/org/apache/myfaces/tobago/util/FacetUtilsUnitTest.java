package org.apache.myfaces.tobago.util;

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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIPanel;
import org.junit.Assert;
import org.junit.Test;

public class FacetUtilsUnitTest {

  @Test
  public void testContextMenu() {
    UIPanel panel = new UIPanel();
    Assert.assertNull(FacetUtils.getContextMenu(panel));
    UIMenu menu = new UIMenu();
    FacetUtils.setContextMenu(panel, menu);
    Assert.assertEquals(menu, FacetUtils.getContextMenu(panel));
  }

  @Test
  public void testDropDownMenu() {
    UIPanel panel = new UIPanel();
    Assert.assertNull(FacetUtils.getContextMenu(panel));
    UIMenu menu = new UIMenu();
    FacetUtils.setDropDownMenu(panel, menu);
    Assert.assertEquals(menu, FacetUtils.getDropDownMenu(panel));
  }

  /**
   * @deprecated since 1.5.0
   */
  @Test
  @Deprecated
  public void testMenupopup() {
    UIPanel panel = new UIPanel();
    Assert.assertNull(FacetUtils.getContextMenu(panel));
    UIMenu menu = new UIMenu();
    panel.getFacets().put(Facets.MENUPOPUP, menu);
    Assert.assertEquals(menu, FacetUtils.getDropDownMenu(panel));
  }
}
