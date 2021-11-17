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
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
    b.setImage("bi-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconBi.html"), formattedResult());
  }

  @Test
  public void iconBiJpg() throws IOException {
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
    b.setImage("fa-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconFa.html"), formattedResult());
  }

  @Test
  public void iconFx() throws IOException {
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
    b.setImage("far fa-code");

    b.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/button/iconFar.html"), formattedResult());
  }
}
