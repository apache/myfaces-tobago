package org.apache.myfaces.tobago.layout.grid;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.PixelLayoutToken;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.layout.math.EquationManager;

import java.util.Arrays;
import java.util.List;

public class GridLayoutManager implements LayoutManager {

  private static final Log LOG = LogFactory.getLog(GridLayoutManager.class);

  private LayoutContainer container;
  private Grid grid;

  private LayoutTokens columnTokens;
  private LayoutTokens rowTokens;

  public GridLayoutManager(LayoutContainer container, String columnsString, String rowsString) {

    this.container = container;
    columnTokens = LayoutTokens.parse(columnsString);
    rowTokens = LayoutTokens.parse(rowsString);

    grid = new Grid(columnTokens.getSize(), rowTokens.getSize());
  }

  public void layout() {

    List<LayoutComponent> components = container.getComponents();

    distributeCells(components);

    // fixme: 1000 -> container.get...
    PixelMeasure horizontal = new PixelMeasure(1000);

    distributePixel(horizontal, columnTokens, grid.getColumns(), components);
// todo   distributeFixed(components);
// todo   distributeAuto(components);
    distributeAsterisk(horizontal, columnTokens, grid.getColumns(), components);

    LOG.info("columns = " + Arrays.toString(grid.getColumns()));

    // fixme: 1000 -> container.get...
    PixelMeasure vertical = new PixelMeasure(1000);

    distributePixel(vertical, rowTokens, grid.getRows(), components);
// todo   distributeFixed(components);
// todo   distributeAuto(components);
    distributeAsterisk(vertical, rowTokens, grid.getRows(), components);

    LOG.info("rows    = " + Arrays.toString(grid.getRows()));
  }

  public void layout(LayoutContext layoutContext) {

    List<LayoutComponent> components = container.getComponents();

    registerVariables(layoutContext);

    distributeCells(components);

    addFixedConstraints(layoutContext);
    addRelativeConstraints(layoutContext);

    for (int i = 0; i < grid.getColumnCount(); i++) {
      for (int j = 0; j < grid.getRowCount(); j++) {
        Cell cell = grid.get(i, j);
        if (cell instanceof ComponentCell) {
          LayoutComponent component = cell.getComponent();
          if (component instanceof LayoutContainer) {
            LayoutManager layoutManager = ((LayoutContainer) component).getLayoutManager();
            EquationManager horizontal = layoutContext.getHorizontal();
            EquationManager vertial = layoutContext.getVertical();
            horizontal.descend(i);
            vertial.descend(j);
            layoutManager.layout(layoutContext);
            horizontal.ascend();
            vertial.ascend();
          }
        }
      }
    }
  }

  private void registerVariables(LayoutContext layoutContext) {
    // horizontal
    layoutContext.getHorizontal().addSubTree(columnTokens.getSize());
    // vertial
    layoutContext.getVertical().addSubTree(rowTokens.getSize());
  }

  private void addFixedConstraints(LayoutContext layoutContext) {
    // horizontal
    for (int i = 0; i < columnTokens.getSize(); i++) {
      LayoutToken layoutToken = columnTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
        int pixel = ((PixelLayoutToken) layoutToken).getPixel();
        layoutContext.getHorizontal().setFixedLength(i, pixel);
      }
    }
    // vertial
    for (int i = 0; i < rowTokens.getSize(); i++) {
      LayoutToken layoutToken = rowTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
        int pixel = ((PixelLayoutToken) layoutToken).getPixel();
        layoutContext.getVertical().setFixedLength(i, pixel);
      }
    }
  }

  private void addRelativeConstraints(LayoutContext layoutContext) {
    // horizontal
    Integer first = null;
    Integer firstIndex = null;
    for (int i = 0; i <  columnTokens.getTokens().size(); i++) {
      LayoutToken token = columnTokens.getTokens().get(i);
      if (token instanceof RelativeLayoutToken) {
        int factor = ((RelativeLayoutToken) token).getFactor();
        if (first == null) {
          first = factor;
          firstIndex = i;
        } else {
          layoutContext.getHorizontal().setProportion(firstIndex, i, first, factor);
        }
      }
    }
    // vertial
    first = null;
    firstIndex = null;
    for (int i = 0; i <  rowTokens.getTokens().size(); i++) {
      LayoutToken token = rowTokens.getTokens().get(i);
      if (token instanceof RelativeLayoutToken) {
        int factor = ((RelativeLayoutToken) token).getFactor();
        if (first == null) {
          first = factor;
          firstIndex = i;
        } else {
          layoutContext.getVertical().setProportion(firstIndex, i, first, factor);
        }
      }
    }
  }

  private void distributeCells(List<LayoutComponent> components) {
    for (LayoutComponent component : components) {

      GridComponentConstraints constraints = GridComponentConstraints.getConstraints(component);
      grid.add(new ComponentCell(component), constraints.getColumnSpan(), constraints.getRowSpan());

      LOG.debug("\n" + grid);
    }
  }

  // todo: may use not only pixel but also other measures
  private void distributePixel(
      PixelMeasure space, LayoutTokens tokens, PixelMeasure[] measures, List<LayoutComponent> components) {

    for (int i = 0; i < measures.length; i++) {
      if (tokens.get(i) instanceof PixelLayoutToken) {
        PixelLayoutToken token = (PixelLayoutToken) tokens.get(i);
        measures[i] = new PixelMeasure(token.getPixel());
        space = (PixelMeasure) space.substractNotNegative(measures[i]);
      }
    }
  }

  private void distributeAsterisk(
      PixelMeasure space, LayoutTokens tokens, PixelMeasure[] measures, List<LayoutComponent> components) {
    int sum = 0;
    int spreadingSpace = space.getPixel();
    for (int i = 0; i < measures.length; i++) {
      if (tokens.get(i) instanceof RelativeLayoutToken) {
        RelativeLayoutToken token = (RelativeLayoutToken) tokens.get(i);
        sum += token.getFactor();
      }
    }
    for (int i = 0; i < measures.length; i++) {
      if (tokens.get(i) instanceof RelativeLayoutToken) {
        RelativeLayoutToken token = (RelativeLayoutToken) tokens.get(i);
        int measure = spreadingSpace * token.getFactor() / sum;
        space = (PixelMeasure) space.substractNotNegative(new PixelMeasure(measure));
        measures[i] = new PixelMeasure(measure);
      }
    }
    LOG.info("rest: " + space);
    // todo: distribute the rest (might be some pixels because of rounding errors)
  }
}
