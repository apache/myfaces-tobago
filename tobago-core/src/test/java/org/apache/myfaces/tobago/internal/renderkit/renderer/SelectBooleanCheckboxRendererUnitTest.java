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

import jakarta.faces.application.FacesMessage;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SelectBooleanCheckboxRendererUnitTest extends RendererTestBase {

  @Test
  public void label() throws IOException {
    final UISelectBooleanCheckbox c = (UISelectBooleanCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectBooleanCheckbox.componentType(), RendererTypes.SelectBooleanCheckbox, "id");
    c.setLabel("label");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectBooleanCheckbox/selectBooleanCheckboxLabel.html"),
        formattedResult());
  }

  @Test
  public void itemLabel() throws IOException {
    final UISelectBooleanCheckbox c = (UISelectBooleanCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectBooleanCheckbox.componentType(), RendererTypes.SelectBooleanCheckbox, "id");
    c.setItemLabel("label");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectBooleanCheckbox/selectBooleanCheckboxItemLabel.html"),
        formattedResult());
  }

  @Test
  public void messageFatal() throws IOException {
    final UISelectBooleanCheckbox c = (UISelectBooleanCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectBooleanCheckbox.componentType(), RendererTypes.SelectBooleanCheckbox, "id");

    final FacesMessage message = new FacesMessage(
        FacesMessage.SEVERITY_FATAL, "Custom fatal", "This is a custom fatal error");
    facesContext.addMessage(c.getClientId(facesContext), message);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectBooleanCheckbox/selectBooleanCheckboxFatal.html"),
        formattedResult());
  }

  @Test
  public void messageError() throws IOException {
    final UISelectBooleanCheckbox c = (UISelectBooleanCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectBooleanCheckbox.componentType(), RendererTypes.SelectBooleanCheckbox, "id");

    final FacesMessage message = new FacesMessage(
        FacesMessage.SEVERITY_ERROR, "Custom error", "This is a custom error");
    facesContext.addMessage(c.getClientId(facesContext), message);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectBooleanCheckbox/selectBooleanCheckboxError.html"),
        formattedResult());
  }

  @Test
  public void messageWarning() throws IOException {
    final UISelectBooleanCheckbox c = (UISelectBooleanCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectBooleanCheckbox.componentType(), RendererTypes.SelectBooleanCheckbox, "id");

    final FacesMessage message = new FacesMessage(
        FacesMessage.SEVERITY_WARN, "Custom warning", "This is a custom warning");
    facesContext.addMessage(c.getClientId(facesContext), message);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectBooleanCheckbox/selectBooleanCheckboxWarning.html"),
        formattedResult());
  }

  @Test
  public void messageInfo() throws IOException {
    final UISelectBooleanCheckbox c = (UISelectBooleanCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectBooleanCheckbox.componentType(), RendererTypes.SelectBooleanCheckbox, "id");

    final FacesMessage message = new FacesMessage(
        FacesMessage.SEVERITY_INFO, "Custom info", "This is a custom information");
    facesContext.addMessage(c.getClientId(facesContext), message);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectBooleanCheckbox/selectBooleanCheckboxInfo.html"),
        formattedResult());
  }
}
