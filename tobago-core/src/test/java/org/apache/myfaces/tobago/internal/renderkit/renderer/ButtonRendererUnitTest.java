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

import jakarta.faces.component.behavior.AjaxBehavior;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

public class ButtonRendererUnitTest extends RendererTestBase {

  @Test
  public void button() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/button.html"), formattedResult());
  }

  @Test
  public void iconBi() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");
    b.setIcon("bi-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconBi.html"), formattedResult());
  }

  @Test
  public void imageBi() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");
    b.setImage("bi-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconBi.html"), formattedResult());
  }

  @Test
  public void imageBiJpg() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");
    b.setImage("bi-code.jpg");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconBiJpg.html"), formattedResult());
  }

  @Test
  public void iconFa() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");
    b.setIcon("fa fa-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconFa.html"), formattedResult());
  }

  @Test
  public void imageFa() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");
    b.setImage("fa-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconFa.html"), formattedResult());
  }

  @Test
  public void imageFx() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");
    b.setImage("fx-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconFx.html"), formattedResult());
  }

  @Test
  public void iconFar() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");
    b.setIcon("far fa-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconFar.html"), formattedResult());
  }

  @Test
  public void imageFar() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");
    b.setImage("far fa-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconFar.html"), formattedResult());
  }

  @Test
  public void ajax() throws IOException {
    final AjaxBehavior behavior =
        (AjaxBehavior) facesContext.getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
    behavior.setExecute(Collections.singletonList("id"));
    behavior.setRender(Collections.singletonList("id"));
    behavior.setResetValues(true);
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("button");
    b.addClientBehavior("click", behavior);
    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/ajax.html"), formattedResult());
  }

  @Test
  public void dropdown() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("Dropdown");
    b.setOmit(true);

    final UILink entry0 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "entry0");
    entry0.setLabel("Entry 0");
    entry0.setOmit(true);
    final UILink entry1 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "entry1");
    entry1.setLabel("Entry 1");
    entry1.setOmit(true);

    b.getChildren().add(entry0);
    b.getChildren().add(entry1);

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/dropdown.html"), formattedResult());
  }

  @Test
  public void dropdownSubmenu() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("Dropdown");
    b.setOmit(true);

    final UILink entry0 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "entry0");
    entry0.setLabel("Entry 0");
    entry0.setOmit(true);

    final UILink entry00 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "entry00");
    entry00.setLabel("Entry 0-0");
    entry00.setOmit(true);
    final UILink entry01 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "entry01");
    entry01.setLabel("Entry 0-1");
    entry01.setOmit(true);

    entry0.getChildren().add(entry00);
    entry0.getChildren().add(entry01);

    final UILink entry1 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "entry1");
    entry1.setLabel("Entry 1");
    entry1.setOmit(true);

    b.getChildren().add(entry0);
    b.getChildren().add(entry1);

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/dropdown-submenu.html"), formattedResult());
  }

  @Test
  public void panelFacet() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");
    b.setLabel("Panel-Facet");
    b.setOmit(true);

    final UIOut o = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    o.setValue("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut"
        + " labore et dolore magna aliquyam erat, sed diam voluptua.");
    b.getFacets().put("panel", o);

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/panel-facet.html"), formattedResult());
  }
}
