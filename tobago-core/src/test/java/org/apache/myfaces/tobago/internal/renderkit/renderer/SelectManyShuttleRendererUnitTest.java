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
import org.apache.myfaces.tobago.component.UISelectManyShuttle;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SelectManyShuttleRendererUnitTest extends RendererTestBase {

  @Test
  public void simple() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/simple.html"), formattedResult());
  }

  @Test
  public void label() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setLabel("label");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

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

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/error-message.html"), formattedResult());
  }

  @Test
  public void help() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setHelp("Help!");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/help.html"), formattedResult());
  }

  @Test
  public void readonly() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setReadonly(true);

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/readonly.html"), formattedResult());
  }

  @Test
  public void disabled() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setDisabled(true);

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/disabled.html"), formattedResult());
  }
}
