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
import org.apache.myfaces.tobago.component.UIPopover;
import org.apache.myfaces.tobago.layout.PopoverTriggers;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PopoverRendererUnitTest extends RendererTestBase {

  @Test
  public void simplePopover() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "buttonId");
    b.setLabel("Popover");
    b.setOmit(true);

    final UIPopover p = (UIPopover) ComponentUtils
        .createComponent(facesContext, Tags.popover.componentType(), RendererTypes.Popover, "popoverId");
    p.setLabel("Popover title");
    p.setValue("Popover content");

    b.getFacets().put("popover", p);
    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/popover/popover.html"), formattedResult());
  }

  @Test
  public void triggerFocus() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "buttonId");
    b.setLabel("Popover");
    b.setOmit(true);

    final UIPopover p = (UIPopover) ComponentUtils
        .createComponent(facesContext, Tags.popover.componentType(), RendererTypes.Popover, "popoverId");
    p.setLabel("Popover title");
    p.setValue("Popover content");
    p.setTrigger(PopoverTriggers.parse("focus"));

    b.getFacets().put("popover", p);
    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/popover/popover.html"), formattedResult());
  }

  @Test
  public void triggerHoverUppercase() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "buttonId");
    b.setLabel("Popover");
    b.setOmit(true);

    final UIPopover p = (UIPopover) ComponentUtils
        .createComponent(facesContext, Tags.popover.componentType(), RendererTypes.Popover, "popoverId");
    p.setLabel("Popover title");
    p.setValue("Popover content");
    p.setTrigger(PopoverTriggers.parse("HOVER"));

    b.getFacets().put("popover", p);
    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/popover/hoverUppercase.html"), formattedResult());
  }

  @Test
  public void triggerFOCUSClickFocusHover() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "buttonId");
    b.setLabel("Popover");
    b.setOmit(true);

    final UIPopover p = (UIPopover) ComponentUtils
        .createComponent(facesContext, Tags.popover.componentType(), RendererTypes.Popover, "popoverId");
    p.setLabel("Popover title");
    p.setValue("Popover content");
    p.setTrigger(PopoverTriggers.parse("FOCUS click focus hover"));

    b.getFacets().put("popover", p);
    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/popover/clickFocusHover.html"), formattedResult());
  }
}
