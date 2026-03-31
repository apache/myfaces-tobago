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
import org.apache.myfaces.tobago.component.UISelectManyShuttle;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.application.FacesMessage;
import java.io.IOException;

public class SelectManyShuttleRendererUnitTest extends RendererTestBase {

  @Test
  public void simple() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");

    addItem(c, "i1", "Value 1");
    addItem(c, "i2", "Value 2");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/simple.html"), formattedResult());
  }

  @Test
  public void label() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setLabel("label");

    addItem(c, "i1", "Value 1");
    addItem(c, "i2", "Value 2");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/label.html"), formattedResult());
  }
  //help

  @Test
  public void errorMessage() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setValid(false);
    facesContext.addMessage("id",
        new FacesMessage(FacesMessage.SEVERITY_ERROR, "test", "a test"));

    addItem(c, "i1", "Value 1");
    addItem(c, "i2", "Value 2");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/error-message.html"), formattedResult());
  }

  @Test
  public void help() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setHelp("Help!");

    addItem(c, "i1", "Value 1");
    addItem(c, "i2", "Value 2");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/help.html"), formattedResult());
  }

  @Test
  public void readonly() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setReadonly(true);

    addItem(c, "i1", "Value 1");
    addItem(c, "i2", "Value 2");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/readonly.html"), formattedResult());
  }

  @Test
  public void disabled() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setDisabled(true);

    addItem(c, "i1", "Value 1");
    addItem(c, "i2", "Value 2");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/disabled.html"), formattedResult());
  }

  @Test
  public void orderable() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");

    addItem(c, "i1", "Value 1", "1");
    addItem(c, "i2", "Value 2", "2");
    addItem(c, "i3", "Value 3", "3");
    addItem(c, "i4", "Value 4", "4");
    addItem(c, "i5", "Value 5", "5");
    addItem(c, "i6", "Value 6", "6");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/orderable.html"), formattedResult());
  }

  private void addItem(UISelectManyShuttle shuttle, String itemId, String itemLabel) {
    addItem(shuttle, itemId, itemLabel, null);
  }

  private void addItem(UISelectManyShuttle shuttle, String id, String label, String value) {
    final UISelectItem item = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, id);
    item.setItemLabel(label);
    item.setItemValue(value);
    shuttle.getChildren().add(item);
  }
}
