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

import org.apache.myfaces.test.el.MockValueExpression;
import org.apache.myfaces.tobago.component.LabelLayout;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UIPaginatorList;
import org.apache.myfaces.tobago.component.UIPaginatorPanel;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.layout.Arrows;
import org.apache.myfaces.tobago.layout.PaginatorMode;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PaginatorListRendererUnitTest extends RendererTestBase {

  @Test
  public void arrowsShow() throws IOException {
    final UISheet sheet = createSampleUISheet(10);
    sheet.setRows(2);
    sheet.setPaginator(PaginatorMode.custom);

    final UIPaginatorPanel panel = (UIPaginatorPanel) ComponentUtils.createComponent(
        facesContext, Tags.paginatorPanel.componentType(), RendererTypes.PaginatorPanel, "panel");

    final UIPaginatorList list = (UIPaginatorList) ComponentUtils.createComponent(
        facesContext, Tags.paginatorList.componentType(), RendererTypes.PaginatorList, "list");
    list.setArrows(Arrows.show);
    list.setAlwaysVisible(true);

    panel.getChildren().add(list);
    sheet.getFacets().put("before", panel);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/paginatorList/arrowsShow.html"), formattedResult());
  }

  @Test
  public void arrowsHide() throws IOException {
    final UISheet sheet = createSampleUISheet(10);
    sheet.setRows(2);
    sheet.setPaginator(PaginatorMode.custom);

    final UIPaginatorPanel panel = (UIPaginatorPanel) ComponentUtils.createComponent(
        facesContext, Tags.paginatorPanel.componentType(), RendererTypes.PaginatorPanel, "panel");

    final UIPaginatorList list = (UIPaginatorList) ComponentUtils.createComponent(
        facesContext, Tags.paginatorList.componentType(), RendererTypes.PaginatorList, "list");
    list.setArrows(Arrows.hide);
    list.setAlwaysVisible(true);

    panel.getChildren().add(list);
    sheet.getFacets().put("before", panel);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/paginatorList/arrowsHide.html"), formattedResult());
  }

  @Test
  public void arrowsAuto() throws IOException {
    final UISheet sheet = createSampleUISheet(10);
    sheet.setRows(2);
    sheet.setPaginator(PaginatorMode.custom);

    final UIPaginatorPanel panel = (UIPaginatorPanel) ComponentUtils.createComponent(
        facesContext, Tags.paginatorPanel.componentType(), RendererTypes.PaginatorPanel, "panel");

    final UIPaginatorList list = (UIPaginatorList) ComponentUtils.createComponent(
        facesContext, Tags.paginatorList.componentType(), RendererTypes.PaginatorList, "list");
    list.setArrows(Arrows.auto);
    list.setAlwaysVisible(true);

    panel.getChildren().add(list);
    sheet.getFacets().put("before", panel);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/paginatorList/arrowsAuto.html"), formattedResult());
  }

  @Test
  public void arrowsAuto2() throws IOException {
    final UISheet sheet = createSampleUISheet(10);
    sheet.setRows(2);
    sheet.setPaginator(PaginatorMode.custom);
    sheet.setFirst(2);

    final UIPaginatorPanel panel = (UIPaginatorPanel) ComponentUtils.createComponent(
        facesContext, Tags.paginatorPanel.componentType(), RendererTypes.PaginatorPanel, "panel");

    final UIPaginatorList list = (UIPaginatorList) ComponentUtils.createComponent(
        facesContext, Tags.paginatorList.componentType(), RendererTypes.PaginatorList, "list");
    list.setArrows(Arrows.auto);
    list.setAlwaysVisible(true);

    panel.getChildren().add(list);
    sheet.getFacets().put("before", panel);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/paginatorList/arrowsAuto2.html"), formattedResult());
  }

  @Test
  public void arrowsAuto8() throws IOException {
    final UISheet sheet = createSampleUISheet(10);
    sheet.setRows(2);
    sheet.setPaginator(PaginatorMode.custom);
    sheet.setFirst(8);

    final UIPaginatorPanel panel = (UIPaginatorPanel) ComponentUtils.createComponent(
        facesContext, Tags.paginatorPanel.componentType(), RendererTypes.PaginatorPanel, "panel");

    final UIPaginatorList list = (UIPaginatorList) ComponentUtils.createComponent(
        facesContext, Tags.paginatorList.componentType(), RendererTypes.PaginatorList, "list");
    list.setArrows(Arrows.auto);
    list.setAlwaysVisible(true);

    panel.getChildren().add(list);
    sheet.getFacets().put("before", panel);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/paginatorList/arrowsAuto8.html"), formattedResult());
  }

  private UISheet createSampleUISheet(final int count) {
    final UISheet sheet = (UISheet) ComponentUtils.createComponent(
        facesContext, Tags.sheet.componentType(), RendererTypes.Sheet, "sheet");
    sheet.setVar("item");
    sheet.setValue(getSheetSample(count));

    final UIColumn column = (UIColumn) ComponentUtils.createComponent(
        facesContext, Tags.column.componentType(), RendererTypes.Column, "column");
    column.setLabel("Alphabet");
    sheet.getChildren().add(column);

    final UIOut out = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    out.setValueExpression("value", new MockValueExpression("#{item.name}", String.class));
    out.setLabelLayout(LabelLayout.skip);
    column.getChildren().add(out);
    return sheet;
  }

}
