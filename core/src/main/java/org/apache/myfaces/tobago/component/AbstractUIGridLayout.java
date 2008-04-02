package org.apache.myfaces.tobago.component;

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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SPAN_X;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SPAN_Y;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.layout.LayoutTokens;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUIGridLayout extends UILayout {

  private static final Log LOG = LogFactory.getLog(AbstractUIGridLayout.class);

  public static final Marker FREE = new Marker("free");
  public static final String USED = "used";

  private boolean ignoreFree;
  private transient LayoutTokens columnLayout;
  private transient LayoutTokens rowLayout;

  private List<Row> layoutRows;

  public LayoutTokens getRowLayout() {
    if (rowLayout == null) {
      rowLayout = LayoutTokens.parse(getRows());
    }
    return rowLayout;
  }

  public LayoutTokens getColumnLayout() {
    if (columnLayout == null) {
      columnLayout = LayoutTokens.parse(getColumns());
    }
    return columnLayout;
  }


  public abstract String getRows();

  public abstract String getColumns();


  public Object saveState(FacesContext context) {
    clearRows();
    return super.saveState(context);
  }


  @Override
  public boolean getRendersChildren() {
    return false;
  }

  @Override
  public void encodeChildren(FacesContext context)
      throws IOException {
    // do nothing here
  }

  @Override
  public void encodeChildrenOfComponent(
      FacesContext facesContext, UIComponent component) throws IOException {
    super.encodeChildrenOfComponent(facesContext, component);
    clearRows();
  }

  private void clearRows() {
    layoutRows = null;
  }

  public int getColumnCount() {
    return getColumnLayout().getSize();
  }

  public List<Row> ensureRows() {
    if (layoutRows == null) {
      layoutRows = createRows();
    }
    return layoutRows;
  }

  private List<Row> createRows() {
    List<Row> rows = new ArrayList<Row>();
    int columnCount = getColumnCount();
    List<UIComponent> children
        = LayoutUtil.addChildren(new ArrayList<UIComponent>(), getParent());

    for (UIComponent component : children) {
      int spanX = getSpanX(component);
      int spanY = getSpanY(component);

      int r = nextFreeRow(rows);
      if (r == rows.size()) {
        rows.add(new Row(columnCount));
      }
      int c = rows.get(r).nextFreeColumn();
      rows.get(r).addControl(component, spanX);
      rows.get(r).fill(c + 1, c + spanX, component.isRendered());

      for (int i = r + 1; i < r + spanY; i++) {

        if (i == rows.size()) {
          rows.add(new Row(columnCount));
        }
        rows.get(i).fill(c, c + spanX, component.isRendered());
      }
    }
    return rows;
  }

  private int nextFreeRow(List rows) {
    int i = 0;
    for (; i < rows.size(); i++) {
      if (((Row) rows.get(i)).nextFreeColumn() != -1) {
        return i;
      }
    }
    return i;
  }

  public static int getSpanX(UIComponent component) {
    return ComponentUtil.getIntAttribute(
        component, ATTR_SPAN_X, 1);
  }

  public static int getSpanY(UIComponent component) {
    return ComponentUtil.getIntAttribute(
        component, ATTR_SPAN_Y, 1);
  }

  public boolean isIgnoreFree() {
    return ignoreFree;
  }

  public void setIgnoreFree(boolean ignoreFree) {
    this.ignoreFree = ignoreFree;
  }

  public static class Row implements Serializable {
    private static final long serialVersionUID = 1511693677519052045L;
    private int columns;
    private List cells;
    private boolean hidden;

    public Row(int columns) {
      setColumns(columns);
    }

    private void addControl(UIComponent component, int spanX) {

      int i = nextFreeColumn();

      cells.set(i, component);
      fill(i + 1, i + spanX, component.isRendered());
    }

    private void fill(int start, int end, boolean rendered) {

      if (end > columns) {
        LOG.error("Error in Jsp (end > columns). "
            + "Try to insert more spanX as possible.");
        LOG.error("start:   " + start);
        LOG.error("end:     " + end);
        LOG.error("columns: " + columns);
        LOG.error("Actual cells:");
        for (Object component : cells) {
          if (component instanceof UIComponent) {
            LOG.error("Cell-ID: " + ((UIComponent) component).getId()
                + " " + ((UIComponent) component).getRendererType());
          } else {
            LOG.error("Cell:    " + component); // e.g. marker
          }
        }

        end = columns; // fix the "end" parameter to continue the processing.
      }

      for (int i = start; i < end; i++) {
        cells.set(i, new Marker(USED, rendered));
      }
    }

    private int nextFreeColumn() {
      for (int i = 0; i < columns; i++) {
        if (FREE.equals(cells.get(i))) {
          return i;
        }
      }
      return -1;
    }

    public List getElements() {
      return cells;
    }

    public int getColumns() {
      return columns;
    }

    private void setColumns(int columns) {
      this.columns = columns;
      cells = new ArrayList(columns);
      for (int i = 0; i < columns; i++) {
        cells.add(FREE);
      }
    }

    public boolean isHidden() {
      return hidden;
    }

    public void setHidden(boolean hidden) {
      this.hidden = hidden;
    }
  }

  public static class Marker implements Serializable {
    private static final long serialVersionUID = 2505999420762504893L;
    private final String name;
    private boolean rendered;

    private Marker(String name) {
      this.name = name;
    }

    public Marker(String name, boolean rendered) {
      this.name = name;
      this.rendered = rendered;
    }

    @Override
    public String toString() {
      return name;
    }

    public boolean isRendered() {
      return rendered;
    }
  }
}
