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
import org.apache.myfaces.tobago.component.UIFile;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FileRendererUnitTest extends RendererTestBase {

  @Test
  public void fileLabel() throws IOException {
    final UIFile c = (UIFile) ComponentUtils.createComponent(
        facesContext, Tags.file.componentType(), RendererTypes.File, "id");
    c.setLabel("label");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/file/file-label.html"), formattedResult());
  }

  @Test
  public void fileDropZoneThis() throws IOException {
    final UIPanel panel = (UIPanel) ComponentUtils.createComponent(
        facesContext, Tags.panel.componentType(), RendererTypes.Panel, "drop-zone");

    final UIFile c = (UIFile) ComponentUtils.createComponent(
        facesContext, Tags.file.componentType(), RendererTypes.File, "id");
    c.setDropZone("drop-zone");

    panel.getChildren().add(c);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/file/file-drop-zone.html"), formattedResult());
  }

  @Test
  public void fileDropZoneId() throws IOException {
    final UIPanel panel = (UIPanel) ComponentUtils.createComponent(
        facesContext, Tags.panel.componentType(), RendererTypes.Panel, "drop-zone");

    final UIFile c = (UIFile) ComponentUtils.createComponent(
        facesContext, Tags.file.componentType(), RendererTypes.File, "id");
    c.setDropZone("drop-zone");

    panel.getChildren().add(c);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/file/file-drop-zone.html"), formattedResult());
  }

  @Test
  public void fileDropZoneParent() throws IOException {
    final UIPanel panel = (UIPanel) ComponentUtils.createComponent(
        facesContext, Tags.panel.componentType(), RendererTypes.Panel, "drop-zone");

    final UIFile c = (UIFile) ComponentUtils.createComponent(
        facesContext, Tags.file.componentType(), RendererTypes.File, "id");
    c.setDropZone("drop-zone");

    panel.getChildren().add(c);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/file/file-drop-zone.html"), formattedResult());
  }
}
