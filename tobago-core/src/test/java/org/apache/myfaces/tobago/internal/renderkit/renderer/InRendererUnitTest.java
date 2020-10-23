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
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UISegmentLayout;
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectOneChoice;
import org.apache.myfaces.tobago.layout.SegmentMeasureList;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class InRendererUnitTest extends RendererTestBase {

  @Test
  public void inputGroupButtonAfter() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");

    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "button");
    b.setLabel("button");
    c.getFacets().put("after", b);

    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/input-group-button-after.html"), formattedResult());
  }

  @Test
  public void inputGroupButtonBefore() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");

    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "button");
    b.setLabel("button");
    c.getFacets().put("before", b);

    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/input-group-button-before.html"), formattedResult());
  }

  @Test
  public void inputGroupChoiceAfter() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");

    final UISelectOneChoice choice = (UISelectOneChoice) ComponentUtils.createComponent(
        facesContext, Tags.selectOneChoice.componentType(), RendererTypes.SelectOneChoice, "choice");
    c.getFacets().put("after", choice);

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Stratocaster");
    choice.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Telecaster");
    choice.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/input-group-choice-after.html"), formattedResult());
  }

  @Test
  public void inputGroupDropdownAfter() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");

    final UIButton d = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "button");
    d.setLabel("dropdown");
    d.setOmit(true);
    c.getFacets().put("after", d);

    final UILink l1 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "l1");
    l1.setLabel("Link 1");
    d.getChildren().add(l1);

    final UILink l2 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "l2");
    l2.setLabel("Link 2");
    d.getChildren().add(l2);

    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/input-group-dropdown-after.html"), formattedResult());
  }

  @Test
  public void inputGroupDropdownBefore() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");

    final UIButton d = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "button");
    d.setLabel("dropdown");
    d.setOmit(true);
    c.getFacets().put("before", d);

    final UILink l1 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "l1");
    l1.setLabel("Link 1");
    d.getChildren().add(l1);

    final UILink l2 = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "l2");
    l2.setLabel("Link 2");
    d.getChildren().add(l2);

    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/input-group-dropdown-before.html"), formattedResult());
  }

  @Test
  public void inputGroupOutAfter() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");

    final UIOut o = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    o.setValue("out");
    c.getFacets().put("after", o);

    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/input-group-out-after.html"), formattedResult());
  }

  @Test
  public void inputGroupOutBefore() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");

    final UIOut o = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    o.setValue("out");
    c.getFacets().put("before", o);

    c.encodeAll(facesContext);

    Assert.assertEquals(loadHtml("renderer/in/input-group-out-before.html"), formattedResult());
  }

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

}
