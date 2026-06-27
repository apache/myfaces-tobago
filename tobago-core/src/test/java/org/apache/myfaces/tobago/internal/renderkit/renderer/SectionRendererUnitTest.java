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
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UISection;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SectionRendererUnitTest extends RendererTestBase {

  @Test
  public void sectionLabel() throws IOException {
    final UISection c = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "id");
    c.setLabel("label");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/section/section-label.html"), formattedResult());
  }

  @Test
  public void sectionLabelFacet() throws IOException {
    final UISection c = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "id");
    final UIOut o = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    o.setValue("label");
    o.setPlain(true);
    c.getFacets().put("label", o);
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/section/section-label-facet.html"), formattedResult());
  }

  @Test
  public void simple() throws IOException {
    final UISection c = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "id");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/section/simple.html"), formattedResult());
  }

  @Test
  public void sectionWithIcon() throws IOException {
    final UISection c = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "id");
    c.setLabel("label");
    c.setIcon("icon-name");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/section/section-icon.html"), formattedResult());
  }

  @Test
  public void sectionLevel5() throws IOException {
    final UISection s = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "id");
    s.setLabel("label");
    s.setLevel(5);
    s.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/section/section-level5.html"), formattedResult());
  }

  @Test
  public void cascading() throws IOException {
    final UISection s1 = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "s1");
    s1.setLabel("label");
    final UISection s2 = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "s2");
    s2.setLabel("label");
    final UISection s3 = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "s3");
    s3.setLabel("label");
    final UISection s4 = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "s4");
    s4.setLabel("label");
    final UISection s5 = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "s5");
    s5.setLabel("label");
    final UISection s6 = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "s6");
    s6.setLabel("label");
    final UISection s7 = (UISection) ComponentUtils.createComponent(
        facesContext, Tags.section.componentType(), RendererTypes.Section, "s7");
    s7.setLabel("label");

    s6.getChildren().add(s7);
    s5.getChildren().add(s6);
    s4.getChildren().add(s5);
    s3.getChildren().add(s4);
    s2.getChildren().add(s3);
    s1.getChildren().add(s2);
    s1.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/section/cascading.html"), formattedResult());
  }
}
