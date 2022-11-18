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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.component.UIComponent;

import java.util.Arrays;

public class AbstractUIGridLayoutUnitTest extends AbstractTobagoTestBase {

  private static final UIComponent X = AbstractUIGridLayout.SPAN;
  private static final UIComponent N = null;

  @Test
  public void test1() {
    final AbstractUIGridLayout grid = (AbstractUIGridLayout) ComponentUtils.createComponent(
        facesContext, Tags.gridLayout.componentType(), RendererTypes.GridLayout, null);

    final UIComponent a = createComponent("a");
    final UIComponent b = createComponent("b");
    final UIComponent c = createComponent("c");
    c.getAttributes().put(Attributes.columnSpan.getName(), 2);
    final UIComponent d = createComponent("d");
    final UIComponent e = createComponent("e");
    e.getAttributes().put(Attributes.gridColumn.getName(), 1);
    e.getAttributes().put(Attributes.gridRow.getName(), 3);

    final UIComponent[][] cells = grid.layout(2, 3, Arrays.asList(a, b, c, d, e));

    Assertions.assertEquals("┏━┳━┓\n"
        + "┃a┃b┃\n"
        + "┣━╋━┫\n"
        + "┃c┃█┃\n"
        + "┣━╋━┫\n"
        + "┃e┃d┃\n"
        + "┗━┻━┛\n", toString(cells));

    Assertions.assertEquals(a, cells[0][0]);
    Assertions.assertEquals(b, cells[0][1]);
    Assertions.assertEquals(c, cells[1][0]);
    Assertions.assertEquals(X, cells[1][1]);
    Assertions.assertEquals(e, cells[2][0]);
    Assertions.assertEquals(d, cells[2][1]);
  }

  @Test
  public void test2() {
    final AbstractUIGridLayout grid = (AbstractUIGridLayout) ComponentUtils.createComponent(
        facesContext, Tags.gridLayout.componentType(), RendererTypes.GridLayout, null);

    final UIComponent a = createComponent("a");
    final UIComponent b = createComponent("b");
    final UIComponent c = createComponent("c");
    c.getAttributes().put(Attributes.rowSpan.getName(), 2);
    final UIComponent d = createComponent("d");
    final UIComponent e = createComponent("e");
    e.getAttributes().put(Attributes.gridColumn.getName(), 1);
    e.getAttributes().put(Attributes.gridRow.getName(), 1);

    final UIComponent[][] cells = grid.layout(2, 3, Arrays.asList(a, b, c, d, e));

    Assertions.assertEquals("┏━┳━┓\n"
        + "┃e┃a┃\n"
        + "┣━╋━┫\n"
        + "┃b┃c┃\n"
        + "┣━╋━┫\n"
        + "┃d┃█┃\n"
        + "┗━┻━┛\n", toString(cells));

    Assertions.assertEquals(e, cells[0][0]);
    Assertions.assertEquals(a, cells[0][1]);
    Assertions.assertEquals(b, cells[1][0]);
    Assertions.assertEquals(c, cells[1][1]);
    Assertions.assertEquals(d, cells[2][0]);
    Assertions.assertEquals(X, cells[2][1]);
  }

  @Test
  public void test3() {
    final AbstractUIGridLayout grid = (AbstractUIGridLayout) ComponentUtils.createComponent(
        facesContext, Tags.gridLayout.componentType(), RendererTypes.GridLayout, null);

    final UIComponent a = createComponent("a");
    a.getAttributes().put(Attributes.rowSpan.getName(), 7);
    final UIComponent b = createComponent("b");
    final UIComponent c = createComponent("c");
    final UIComponent d = createComponent("d");
    final UIComponent e = createComponent("e");

    final UIComponent[][] cells = grid.layout(2, 3, Arrays.asList(a, b, c, d, e));

    Assertions.assertEquals("┏━┳━┓\n"
        + "┃a┃b┃\n"
        + "┣━╋━┫\n"
        + "┃█┃c┃\n"
        + "┣━╋━┫\n"
        + "┃█┃d┃\n"
        + "┣━╋━┫\n"
        + "┃█┃e┃\n"
        + "┣━╋━┫\n"
        + "┃█┃◌┃\n"
        + "┣━╋━┫\n"
        + "┃█┃◌┃\n"
        + "┣━╋━┫\n"
        + "┃█┃◌┃\n"
        + "┣━╋━┫\n"
        + "┃◌┃◌┃\n"
        + "┗━┻━┛\n", toString(cells));

    Assertions.assertEquals(a, cells[0][0]);
    Assertions.assertEquals(b, cells[0][1]);
    Assertions.assertEquals(X, cells[1][0]);
    Assertions.assertEquals(c, cells[1][1]);
    Assertions.assertEquals(X, cells[2][0]);
    Assertions.assertEquals(d, cells[2][1]);
    Assertions.assertEquals(X, cells[3][0]);
    Assertions.assertEquals(e, cells[3][1]);
    Assertions.assertEquals(X, cells[4][0]);
    Assertions.assertEquals(N, cells[4][1]);
    Assertions.assertEquals(X, cells[5][0]);
    Assertions.assertEquals(N, cells[5][1]);
    Assertions.assertEquals(X, cells[6][0]);
    Assertions.assertEquals(N, cells[6][1]);
    Assertions.assertEquals(N, cells[7][0]);
    Assertions.assertEquals(N, cells[7][1]);
  }

  @Test
  public void test4() {
    final AbstractUIGridLayout grid = (AbstractUIGridLayout) ComponentUtils.createComponent(
        facesContext, Tags.gridLayout.componentType(), RendererTypes.GridLayout, null);

    final UIComponent a = createComponent("a");
    final UIComponent b = createComponent("b");
    b.getAttributes().put(Attributes.columnSpan.getName(), 2);

    final UIComponent[][] cells = grid.layout(2, 1, Arrays.asList(a, b));

    Assertions.assertEquals("┏━┳━┓\n"
        + "┃a┃b┃\n"
        + "┗━┻━┛\n", toString(cells));

    Assertions.assertEquals(a, cells[0][0]);
    Assertions.assertEquals(b, cells[0][1]);

    // remark: columnSpan = 2 not valid, but should only log a warning.
  }

  @Test
  public void test5() {
    final AbstractUIGridLayout grid = (AbstractUIGridLayout) ComponentUtils.createComponent(
        facesContext, Tags.gridLayout.componentType(), RendererTypes.GridLayout, null);

    final UIComponent a = createComponent("a");
    final UIComponent b = createComponent("b");

    final UIComponent c = createComponent("c");
    final UIComponent d = createComponent("d");

    final UIComponent e = createComponent("e");
    e.getAttributes().put(Attributes.columnSpan.getName(), 2);

    final UIComponent f = createComponent("f");
    final UIComponent g = createComponent("g");

    final UIComponent h = createComponent("h");
    final UIComponent i = createComponent("i");

    final UIComponent j = createComponent("j");
    j.getAttributes().put(Attributes.columnSpan.getName(), 2);

    final UIComponent[][] cells = grid.layout(2, 5, Arrays.asList(a, b, c, d, e, f, g, h, i, j));

    Assertions.assertEquals("┏━┳━┓\n"
        + "┃a┃b┃\n"
        + "┣━╋━┫\n"
        + "┃c┃d┃\n"
        + "┣━╋━┫\n"
        + "┃e┃█┃\n"
        + "┣━╋━┫\n"
        + "┃f┃g┃\n"
        + "┣━╋━┫\n"
        + "┃h┃i┃\n"
        + "┣━╋━┫\n"
        + "┃j┃█┃\n"
        + "┣━╋━┫\n"
        + "┃◌┃◌┃\n"
        + "┣━╋━┫\n"
        + "┃◌┃◌┃\n"
        + "┣━╋━┫\n"
        + "┃◌┃◌┃\n"
        + "┣━╋━┫\n"
        + "┃◌┃◌┃\n"
        + "┗━┻━┛\n", toString(cells));
  }

  @Test
  public void testExpand() {
    final AbstractUIGridLayout grid = (AbstractUIGridLayout) ComponentUtils.createComponent(
        facesContext, Tags.gridLayout.componentType(), RendererTypes.GridLayout, null);

    final UIComponent a = createComponent("a");
    final UIComponent b = createComponent("b");

    final UIComponent[][] array = new UIComponent[3][5];
    array[0][0] = a;
    array[2][4] = b;

    Assertions.assertEquals(a, array[0][0]);
    Assertions.assertEquals(b, array[2][4]);
    Assertions.assertEquals(3, array.length);
    Assertions.assertEquals(5, array[0].length);

    final UIComponent[][] expand = grid.expand(array, 7);

    Assertions.assertEquals(array[0][0], expand[0][0]);
    Assertions.assertEquals(array[2][4], expand[2][4]);
    Assertions.assertEquals(7, expand.length);
    Assertions.assertEquals(5, expand[0].length);

    Assertions.assertNull(expand[1][1]);
    Assertions.assertNull(expand[6][1]);
  }

  private UIComponent createComponent(final String id) {
    return ComponentUtils.createComponent(facesContext, Tags.panel.componentType(), RendererTypes.Panel, id);
  }

  private static String toString(final UIComponent[][] cells) {

    final StringBuilder builder = new StringBuilder();

    // top of grid
    for (int i = 0; i < cells[0].length; i++) {
      if (i == 0) {
        builder.append("┏");
      } else {
        builder.append("┳");
      }
      builder.append("━");
    }
    builder.append("┓");
    builder.append("\n");

    for (int j = 0; j < cells.length; j++) {

      // between the cells
      if (j != 0) {
        for (int i = 0; i < cells[0].length; i++) {
          if (i == 0) {
            builder.append("┣");
          } else {
            builder.append("╋");
          }
          builder.append("━");
        }
        builder.append("┫");
        builder.append("\n");
      }

      // cell
      for (int i = 0; i < cells[0].length; i++) {
        builder.append("┃");
        if (cells[j][i] == X) {
          builder.append("█");
        } else if (cells[j][i] != null) {
          builder.append(cells[j][i].getClientId());
        } else {
          builder.append("◌");
        }
      }
      builder.append("┃");
      builder.append("\n");
    }

    //last bottom
    for (int i = 0; i < cells[0].length; i++) {
      if (i == 0) {
        builder.append("┗");
      } else {
        builder.append("┻");
      }
      builder.append("━");
    }
    builder.append("┛");
    builder.append("\n");

    return builder.toString();
  }

}
