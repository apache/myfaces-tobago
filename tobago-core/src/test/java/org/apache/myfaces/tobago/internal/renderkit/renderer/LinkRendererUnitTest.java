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
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectManyCheckbox;
import org.apache.myfaces.tobago.component.UISelectOneRadio;
import org.apache.myfaces.tobago.component.UISeparator;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LinkRendererUnitTest extends RendererTestBase {

  @Test
  public void booleanInsideLink() throws IOException {
    final UILink c = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");
    c.setLabel("dropdown");

    final UISelectBooleanCheckbox s = (UISelectBooleanCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectBooleanCheckbox.componentType(), RendererTypes.SelectBooleanCheckbox, "id");
    s.setLabel("boolean");

    c.getChildren().add(s);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/link/booleanInsideLink.html"), formattedResult());
  }

  @Test
  public void link() throws IOException {
    final UILink c = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");
    c.setLabel("label");
    c.setLink("https://www.apache.org/");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/link/link.html"), formattedResult());
  }

  @Test
  public void linkWithImage() throws IOException {
    final UILink c = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");
    c.setLabel("label");
    c.setImage("url");
    c.setLink("https://www.apache.org/");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/link/linkWithImage.html"), formattedResult());
  }

  @Test
  public void linkWithEmptyImage() throws IOException {
    final UILink c = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");
    c.setLabel("label");
    c.setImage("");
    c.setLink("https://www.apache.org/");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/link/link.html"), formattedResult());
  }

  @Test
  public void manyInsideLink() throws IOException {
    final UILink c = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");
    c.setLabel("dropdown");

    final UISelectManyCheckbox s = (UISelectManyCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectManyCheckbox.componentType(), RendererTypes.SelectManyCheckbox, "many");
    s.setLabel("many");

    c.getChildren().add(s);

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Stratocaster");
    s.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Telecaster");
    s.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/link/manyInsideLink.html"), formattedResult());
  }

  @Test
  public void radioInsideLink() throws IOException {
    final UILink c = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");
    c.setLabel("dropdown");

    final UISelectOneRadio s = (UISelectOneRadio) ComponentUtils.createComponent(
        facesContext, Tags.selectOneRadio.componentType(), RendererTypes.SelectOneRadio, "radio");
    s.setLabel("radio");

    c.getChildren().add(s);

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Stratocaster");
    s.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Telecaster");
    s.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/link/radioInsideLink.html"), formattedResult());
  }

  @Test
  public void separatorInsideLink() throws IOException {
    final UILink c = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");
    c.setLabel("dropdown");

    final UILink l1 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "link1");
    l1.setLabel("Link 1");
    l1.setLink("https://www.apache.org/");

    final UISeparator s = (UISeparator) ComponentUtils.createComponent(
        facesContext, Tags.separator.componentType(), RendererTypes.Separator, "separator");

    final UILink l2 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "link2");
    l2.setLabel("Link 2");
    l2.setLink("https://www.apache.org/");

    c.getChildren().add(l1);
    c.getChildren().add(s);
    c.getChildren().add(l2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/link/separatorInsideLink.html"), formattedResult());
  }

}
