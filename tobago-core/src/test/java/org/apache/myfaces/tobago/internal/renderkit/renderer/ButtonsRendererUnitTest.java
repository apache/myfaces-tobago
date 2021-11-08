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
import org.apache.myfaces.tobago.component.UIBadge;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UIButtons;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UISeparator;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ButtonsRendererUnitTest extends RendererTestBase {

  @Test
  public void separatorInsideButtons() throws IOException {
    final UIButtons l = (UIButtons) ComponentUtils.createComponent(
        facesContext, Tags.buttons.componentType(), RendererTypes.Buttons, "list");
    final UIButton c = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    c.setLabel("button");
    l.getChildren().add(c);

    final UILink sub1 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "sub1");
    sub1.setLabel("sub1");
    final UISeparator separator1 = (UISeparator) ComponentUtils.createComponent(
        facesContext, Tags.separator.componentType(), RendererTypes.Separator, "separator1");
    final UILink sub2 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "sub2");
    sub2.setLabel("sub2");
    final UISeparator separator2 = (UISeparator) ComponentUtils.createComponent(
        facesContext, Tags.separator.componentType(), RendererTypes.Separator, "separator2");
    final UILink sub3 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "sub3");
    sub3.setLabel("sub3");

    c.getChildren().add(sub1);
    c.getChildren().add(separator1);
    c.getChildren().add(sub2);
    c.getChildren().add(separator2);
    c.getChildren().add(sub3);

    l.getChildren().add(c);

    l.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/buttons/separator-inside-buttons.html"), formattedResult());
  }

  @Test
  public void badgeInsideButtons() throws IOException {
    final UIButtons l = (UIButtons) ComponentUtils.createComponent(
        facesContext, Tags.buttons.componentType(), RendererTypes.Buttons, "list");
    final UIBadge b = (UIBadge) ComponentUtils.createComponent(
        facesContext, Tags.badge.componentType(), RendererTypes.Badge, "badge");
    final UIButton c = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    c.setLabel("button");

    l.getChildren().add(b);
    l.getChildren().add(c);

    l.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/buttons/badge-inside-buttons.html"), formattedResult());
  }

  @Test
  public void badgeWarningInsideButtons() throws IOException {
    final UIButtons l = (UIButtons) ComponentUtils.createComponent(
        facesContext, Tags.buttons.componentType(), RendererTypes.Buttons, "list");
    final UIBadge b = (UIBadge) ComponentUtils.createComponent(
        facesContext, Tags.badge.componentType(), RendererTypes.Badge, "badge");
    b.setMarkup(Markup.WARNING);
    final UIButton c = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    c.setLabel("button");

    l.getChildren().add(b);
    l.getChildren().add(c);

    l.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/buttons/badgeWarning-inside-buttons.html"), formattedResult());
  }

}
