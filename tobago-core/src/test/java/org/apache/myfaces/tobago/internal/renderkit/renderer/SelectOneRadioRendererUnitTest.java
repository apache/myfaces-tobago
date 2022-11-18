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
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectOneRadio;
import org.apache.myfaces.tobago.component.UISelectReference;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.component.UIPanel;

import java.io.IOException;

public class SelectOneRadioRendererUnitTest extends RendererTestBase {

  @Test
  public void label() throws IOException {
    final UISelectOneRadio c = (UISelectOneRadio) ComponentUtils.createComponent(
        facesContext, Tags.selectOneRadio.componentType(), RendererTypes.SelectOneRadio, "id");
    c.setLabel("label");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Stratocaster");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Telecaster");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectOneRadio/selectOneRadioLabel.html"), formattedResult());
  }

  @Test
  public void reference() throws IOException {
    final UIPanel panel = (UIPanel) ComponentUtils.createComponent(
        facesContext, Tags.panel.componentType(), RendererTypes.Panel, "panel");

    final UISelectOneRadio c = (UISelectOneRadio) ComponentUtils.createComponent(
        facesContext, Tags.selectOneRadio.componentType(), RendererTypes.SelectOneRadio, "id");
    c.setLabel("label");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Stratocaster");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Telecaster");
    c.getChildren().add(i2);
    c.setRenderRange("0");
    panel.getChildren().add(c);

    final UISelectReference r = (UISelectReference) ComponentUtils.createComponent(
        facesContext, Tags.selectReference.componentType(), RendererTypes.SelectReference, "ref");
    r.setFor("id");
    r.setRenderRange("1");
    panel.getChildren().add(r);

    panel.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectOneRadio/selectReference.html"), formattedResult());
  }

}
