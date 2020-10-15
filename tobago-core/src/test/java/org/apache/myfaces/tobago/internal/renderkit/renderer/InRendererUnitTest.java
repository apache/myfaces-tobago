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

import org.apache.myfaces.tobago.component.LabelLayout;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UISegmentLayout;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.layout.SegmentMeasureList;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class InRendererUnitTest extends AbstractTobagoTestBase {

  @Test
  public void simple() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/simple.html"), formattedResult());
  }

  @Test
  public void labelLayoutFlexLeft() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.flexLeft); // same as default
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-flexLeft.html"), formattedResult());
  }

  @Test
  public void labelLayoutFlexRight() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.flexRight);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-flexRight.html"), formattedResult());
  }

  @Test
  public void labelLayoutFlowLeft() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.flowLeft);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-flowLeft.html"), formattedResult());
  }

  @Test
  public void labelLayoutFlowRight() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.flowRight);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-flowRight.html"), formattedResult());
  }

  @Test
  public void labelLayoutTop() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.top);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-top.html"), formattedResult());
  }

  @Test
  public void labelLayoutGridLeft() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.gridLeft);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-gridLeft.html"), formattedResult());
  }

  @Test
  public void labelLayoutGridRight() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.gridRight);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-gridRight.html"), formattedResult());
  }

  @Test
  public void labelLayoutGridTop() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.gridTop);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-gridTop.html"), formattedResult());
  }

  @Test
  public void labelLayoutGridBottom() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.gridBottom);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-gridBottom.html"), formattedResult());
  }

  @Test
  public void labelLayoutSegmentLeft() throws IOException {

    final UISegmentLayout l = (UISegmentLayout) ComponentUtils.createComponent(
        facesContext, Tags.segmentLayout.componentType(), RendererTypes.SegmentLayout, "segid");
    l.setMedium(SegmentMeasureList.parse("3seg 9seg"));

    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.segmentLeft);

    l.getChildren().add(c);
    l.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-segmentLeft.html"), formattedResult());
  }

  @Test
  public void labelLayoutSegmentRight() throws IOException {

    final UISegmentLayout l = (UISegmentLayout) ComponentUtils.createComponent(
        facesContext, Tags.segmentLayout.componentType(), RendererTypes.SegmentLayout, "segid");
    l.setMedium(SegmentMeasureList.parse("9seg 3seg"));

    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.segmentRight);

    l.getChildren().add(c);
    l.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-segmentRight.html"), formattedResult());
  }

  @Test
  public void labelSkip() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.skip);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-skip.html"), formattedResult());
  }

  @Test
  public void labelNone() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.setLabel("label");
    c.setLabelLayout(LabelLayout.none);
    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/label-none.html"), formattedResult());
  }

  private String formattedResult() throws IOException {
    return format1To2Indent(getLastWritten());
  }

  private String loadHtml(final String fileName) throws IOException {
    final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    try (InputStream is = classLoader.getResourceAsStream(fileName)) {
      if (is == null) {
        throw new FileNotFoundException(fileName);
      }
      try (final InputStreamReader isr = new InputStreamReader(is);
           final BufferedReader reader = new BufferedReader(isr)) {
        return reader.lines().collect(Collectors.joining(System.lineSeparator()))
            .replaceAll("<!--[^>]*-->", "")
            .replaceAll("^\n\n", "");
      }
    }
  }

  private String format1To2Indent(final String xml) {
    return xml.replaceAll("^\n", "")
    .replaceAll("\n <", "\n\t<")
    .replaceAll("\n  <", "\n\t\t<")
    .replaceAll("\n   <", "\n\t\t\t<")
    .replaceAll("\n    <", "\n\t\t\t\t<")
    .replaceAll("\n     <", "\n\t\t\t\t\t<")
    .replaceAll("\n      <", "\n\t\t\t\t\t\t<")
    .replaceAll("\n       <", "\n\t\t\t\t\t\t\t<")
        .replaceAll("\t", "  ");
  }
}
