/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 20, 2002 at 11:39:23 AM.
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.LayoutUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UIGridLayout extends UILayout {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UIGridLayout.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.GridLayout";
  public static final String COMPONENT_FAMILY = "com.atanion.tobago.GridLayout";

  public static final Marker FREE = new Marker("free");
  public static final String USED = "used";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getFamily() {
    return COMPONENT_FAMILY;
  }

  private boolean ignoreFree;

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeChildren(FacesContext context)
      throws IOException {
    // do nothing here
  }

  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component) throws IOException {
    clearRows();
    super.encodeChildrenOfComponent(facesContext, component);
  }

  private void clearRows() {
    getAttributes().remove(TobagoConstants.ATTR_LAYOUT_ROWS);
  }

  public int getColumnCount() {
    String columns =
        ComponentUtil.getStringAttribute(this, TobagoConstants.ATTR_COLUMNS);
    int columnCount;
    if (columns != null) {
      columnCount = 1 + StringUtils.countMatches(columns, ";");
    } else {
      columnCount = 1;
    }
    return columnCount;
  }

  public List<Row> ensureRows() {
    List<Row> rows = (List<Row>) getAttributes().get(TobagoConstants.ATTR_LAYOUT_ROWS);
    if (rows == null) {
      rows = createRows();
    }
    return rows;
  }

  private List<Row> createRows() {
    List<Row> rows = new ArrayList<Row>();
    int columnCount = getColumnCount();
    List children = LayoutUtil.addChildren(new ArrayList(), getParent());

    for (Iterator iterator = children.iterator(); iterator.hasNext();) {
      UIComponent component = (UIComponent) iterator.next();
      int spanX = getSpanX(component);
      int spanY = getSpanY(component);

      int r = nextFreeRow(rows);
      if (r == rows.size()) {
        rows.add(new Row(columnCount));
      }
      int c = ((Row) rows.get(r)).nextFreeColumn();
      ((Row) rows.get(r)).addControl(component, spanX);
      ((Row) rows.get(r)).fill(c + 1, c + spanX, component.isRendered());

      for (int i = r + 1; i < r + spanY; i++) {

        if (i == rows.size()) {
          rows.add(new Row(columnCount));
        }
        ((Row) rows.get(i)).fill(c, c + spanX, component.isRendered());
      }
    }
    getAttributes().put(TobagoConstants.ATTR_LAYOUT_ROWS, rows);
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
        component, TobagoConstants.ATTR_SPAN_X, 1);
  }

  public static int getSpanY(UIComponent component) {
    return ComponentUtil.getIntAttribute(
        component, TobagoConstants.ATTR_SPAN_Y, 1);
  }

// ///////////////////////////////////////////// bean getter + setter

  public boolean isIgnoreFree() {
    return ignoreFree;
  }

  public void setIgnoreFree(boolean ignoreFree) {
    this.ignoreFree = ignoreFree;
  }

  // ///////////////////////////////////////////////////////////////
// ///////////////////////////////////////////// class Row
// ///////////////////////////////////////////////////////////////

  public static class Row implements Serializable {

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
        LOG.error("Error in Jsp (end > columns). " +
            "Try to insert more spanX as possible.");
        LOG.error("start:   " + start);
        LOG.error("end:     " + end);
        LOG.error("columns: " + columns);
        LOG.error("Actual cells:");
        for (Iterator i = cells.iterator(); i.hasNext();) {
          Object component = i.next();
          if (component instanceof UIComponent) {
            LOG.error("Cell-ID: " + ((UIComponent)component).getId()
                + " " + ((UIComponent)component).getRendererType());
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
        if (FREE.equals(cells.get(i))) return i;
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

    private final String name;

    private boolean rendered;

    private Marker(String name) {
      this.name = name;
    }

    public Marker(String name, boolean rendered) {
      this.name = name;
      this.rendered = rendered;
    }

    public String toString() {
      return name;
    }

    public boolean isRendered() {
      return rendered;
    }
  }
}
