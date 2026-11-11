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
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UISegmentLayout;
import org.apache.myfaces.tobago.component.UIStyle;
import org.apache.myfaces.tobago.layout.SegmentMeasureList;
import org.apache.myfaces.tobago.renderkit.css.CustomClass;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.event.PostAddToViewEvent;
import java.io.IOException;

public class SegmentLayoutRendererUnitTest extends RendererTestBase {

  @Test
  public void segmentLayout() throws IOException {
    final UISegmentLayout c = createSegmentLayout();
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/segmentLayout/segmentLayout.html"), formattedResult());
  }

  @Test
  public void segmentLayoutStyleClass() throws IOException {
    final UISegmentLayout c = createSegmentLayout();
    c.setStyleClass(new CustomClass("my-style-class"));
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/segmentLayout/segmentLayoutStyleClass.html"), formattedResult());
  }

  @Test
  public void segmentLayoutTcStyle() throws IOException {
    final UISegmentLayout c = createSegmentLayout();
    final UIStyle style = (UIStyle) ComponentUtils.createComponent(
        facesContext, Tags.style.componentType(), RendererTypes.Style, "style");
    style.setCustomClass(new CustomClass("my-style-class"));
    c.getChildren().add(0, style);
    style.processEvent(new PostAddToViewEvent(style));
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/segmentLayout/segmentLayoutStyleClass.html"), formattedResult());
  }

  private UISegmentLayout createSegmentLayout() {
    final UISegmentLayout c = (UISegmentLayout) ComponentUtils.createComponent(
        facesContext, Tags.segmentLayout.componentType(), RendererTypes.SegmentLayout, "id");
    c.setMedium(SegmentMeasureList.parse("4seg"));

    final UIPanel panel = (UIPanel) ComponentUtils.createComponent(
        facesContext, Tags.panel.componentType(), RendererTypes.Panel, "panel");
    c.getChildren().add(panel);
    return c;
  }
}
