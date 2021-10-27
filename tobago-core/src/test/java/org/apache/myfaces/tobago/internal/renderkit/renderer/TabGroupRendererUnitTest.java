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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TabGroupRendererUnitTest extends RendererTestBase {

  @Test
  public void tabGroup() throws IOException {
    final UITabGroup tg = (UITabGroup) ComponentUtils.createComponent(
        facesContext, Tags.tabGroup.componentType(), RendererTypes.TabGroup, "id");

    final UITab t1 = (UITab) ComponentUtils.createComponent(
        facesContext, Tags.tab.componentType(), RendererTypes.Tab, "t1");
    t1.setLabel("T1");

    final UITab t2 = (UITab) ComponentUtils.createComponent(
        facesContext, Tags.tab.componentType(), RendererTypes.Tab, "t2");
    t2.setLabel("T2");

    tg.getChildren().add(t1);
    tg.getChildren().add(t2);

    tg.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/tabGroup/tabGroup.html"), formattedResult());
  }

  @Test
  public void withBar() throws IOException {
    final UITabGroup tg = (UITabGroup) ComponentUtils.createComponent(
        facesContext, Tags.tabGroup.componentType(), RendererTypes.TabGroup, "id");

    final UITab t1 = (UITab) ComponentUtils.createComponent(
        facesContext, Tags.tab.componentType(), RendererTypes.Tab, "t1");
    t1.setLabel("T1");

    final UITab t2 = (UITab) ComponentUtils.createComponent(
        facesContext, Tags.tab.componentType(), RendererTypes.Tab, "t2");
    t2.setLabel("T2");
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "b");
    b.setLabel("B");
    t2.getFacets().put("bar", b);

    tg.getChildren().add(t1);
    tg.getChildren().add(t2);

    tg.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/tabGroup/tabGroup-bar.html"), formattedResult());
  }

  @Test
  public void withLabel() throws IOException {
    final UITabGroup tg = (UITabGroup) ComponentUtils.createComponent(
        facesContext, Tags.tabGroup.componentType(), RendererTypes.TabGroup, "id");

    final UITab t1 = (UITab) ComponentUtils.createComponent(
        facesContext, Tags.tab.componentType(), RendererTypes.Tab, "t1");
    t1.setLabel("T1");

    final UITab t2 = (UITab) ComponentUtils.createComponent(
        facesContext, Tags.tab.componentType(), RendererTypes.Tab, "t2");
    t2.setLabel("T2");
    final UIOut o = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "o");
    o.setLabel("Label");
    t2.getFacets().put("label", o);

    tg.getChildren().add(t1);
    tg.getChildren().add(t2);

    tg.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/tabGroup/tabGroup-label.html"), formattedResult());
  }

}
