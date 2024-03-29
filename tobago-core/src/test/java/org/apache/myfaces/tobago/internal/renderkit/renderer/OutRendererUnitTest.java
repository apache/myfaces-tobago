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

import jakarta.faces.component.UIParameter;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.renderkit.css.CustomClass;
import org.apache.myfaces.tobago.sanitizer.SanitizeMode;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class OutRendererUnitTest extends RendererTestBase {

  @Test
  public void out() throws IOException {
    final UIOut c = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "id");
    c.setValue("out");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/out/out.html"), formattedResult());
  }

  @Test
  public void outLabel() throws IOException {
    final UIOut c = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "id");
    c.setValue("out");
    c.setLabel("label");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/out/outLabel.html"), formattedResult());
  }

  @Test
  public void outLabelCustomClass() throws IOException {
    final UIOut c = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "id");
    c.setValue("out");
    c.setLabel("label");
    c.setCustomClass(new CustomClass("custom-class"));
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/out/outLabelCustomClass.html"), formattedResult());
  }

  @Test
  public void outSanitizeAuto() throws IOException {
    final UIOut c = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "id");
    c.setValue("Hello <span style='display: none'>Peter</span>!");
    c.setEscape(false);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/out/outSanitizeAuto.html"), formattedResult());
  }

  @Test
  public void outSanitizeNever() throws IOException {
    final UIOut c = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "id");
    c.setValue("Hello <span style='display: none'>Peter</span>!");
    c.setEscape(false);
    c.setSanitize(SanitizeMode.never);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/out/outSanitizeNever.html"), formattedResult());
  }

  @Test
  public void outFormat() throws IOException {
    final UIOut c = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "id");
    c.setValue("Hello {0} {1}!");
    c.setMessageFormat(true);

    final UIParameter p0 = (UIParameter) ComponentUtils.createComponent(
        facesContext, UIParameter.COMPONENT_TYPE, null, "p0");
    p0.setValue("Mrs");
    c.getChildren().add(p0);

    final UIParameter p1 = (UIParameter) ComponentUtils.createComponent(
        facesContext, UIParameter.COMPONENT_TYPE, null, "p1");
    p1.setValue("Smith");
    c.getChildren().add(p1);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/out/outFormat.html"), formattedResult());
  }

}
