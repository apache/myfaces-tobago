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
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectManyCheckbox;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SelectManyCheckboxRendererUnitTest extends RendererTestBase {

  @Test
  public void itemLabel() throws IOException {
    final UISelectManyCheckbox c = (UISelectManyCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectManyCheckbox.componentType(), RendererTypes.SelectManyCheckbox, "id");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Entry One");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Entry Two");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyCheckbox/selectManyCheckboxItemLabel.html"),
        formattedResult());
  }

  @Test
  public void messageFatal() throws IOException {
    final UISelectManyCheckbox c = (UISelectManyCheckbox) ComponentUtils.createComponent(
        facesContext, Tags.selectManyCheckbox.componentType(), RendererTypes.SelectManyCheckbox, "id");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Entry One");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Entry Two");
    c.getChildren().add(i2);

    final FacesMessage message = new FacesMessage(
        FacesMessage.SEVERITY_FATAL, "Custom fatal", "This is a custom fatal error");
    facesContext.addMessage(c.getClientId(facesContext), message);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyCheckbox/selectManyCheckboxFatal.html"),
        formattedResult());
  }
}
