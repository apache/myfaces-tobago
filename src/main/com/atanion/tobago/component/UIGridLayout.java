/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 20, 2002 at 11:39:23 AM.
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.LayoutUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class UIGridLayout extends UIComponentBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UIGridLayout.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.GridLayout";
  public static final String COMPONENT_FAMILY = "com.atanion.tobago.GridLayout";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getFamily() {
    return COMPONENT_FAMILY;
  }

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeChildren(FacesContext context)
      throws IOException {
    // do nothing here
  }

  public List createRows() {
    List rows = new ArrayList();
    int columnCount = ComponentUtil.getIntAttribute(this, TobagoConstants.ATTR_COLUMN_COUNT, 1);
    Vector children = LayoutUtil.addChildren(new Vector(), getParent());

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
      ((Row) rows.get(r)).fill(c + 1, c + spanX);

      for (int i = r + 1; i < r + spanY; i++) {

        if (i == rows.size()) {
          rows.add(new Row(columnCount));
        }
        ((Row) rows.get(i)).fill(c, c + spanX);
      }
    }
    getAttributes().put(TobagoConstants.ATTR_LAYOUT_ROWS, rows);
    return rows;
  }

  public List ensureRows() {
    List rows = (List) getAttributes().get(TobagoConstants.ATTR_LAYOUT_ROWS);
    if (rows == null) {
      rows = createRows();
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
/*
  public void layout(FacesContext facesContext) {

    String width = determineWidth();
    String widthRatio = getAttribute(TobagoConstants.ATTR_WIDTH_RATIO);
    StringTokenizer tokenizer = new StringTokenizer(widthRatio, ":");
    List list = new ArrayList();
    while (tokenizer.hasMoreTokens()) {
      list.add(tokenizer.nextToken());
    }
    int[] partWidths = new int[list.size()];
    int sum = 0;
    for (int i = 0; i < partWidths.length; i++) {
      partWidths[i] = Integer.parseInt((String) list.get(i));
      sum += partWidths[i];
    }


    ClientProperties client = ClientProperties.getInstanceFromSession(
        (HttpServletRequest)facesContext.getServletRequest());
    Theme theme = client.getTheme();




    int tabOffset = theme == Theme.SCARBOROUGH ? 24 : 18;
    int labelWidth = theme == Theme.SCARBOROUGH ? 144 : 125;
    int labelOffset = theme == Theme.SCARBOROUGH ? 0 : 10;

    int sum = tabOffset + labelWidth + labelOffset;



    for (Iterator i = getChildren(); i.hasNext(); ) {
      UIComponent child = (UIComponent) i.next();
      String width = (String) getParent().getAttributes().get(TobagoConstants.ATTR_WIDTH);
      width = Integer.toString(Integer.parseInt(
          width.substring(0,3)) - sum) + "px";
      child.setAttribute(TobagoConstants.ATTR_WIDTH, width);
      String style = (String) child.getAttributes().get(TobagoConstants.ATTR_STYLE);
      style = style != null ? style : "";
      child.setAttribute(
          TobagoConstants.ATTR_STYLE, style + " width: " + width + ";");
    }
  }

  private String determineWidth() {
    String width;
    width = (String) getAttribute(TobagoConstants.ATTR_WIDTH);
    if (width == null) {
      UIComponent parent = getParent();
      width = (String) parent.getAttributes().get(TobagoConstants.ATTR_WIDTH);
    }
    return width;
  }

*/

  public static int getSpanX(UIComponent component) {
    return ComponentUtil.getIntValueBindingOrAttribute(
        component, TobagoConstants.ATTR_SPAN_X, 1);
  }

  public static int getSpanY(UIComponent component) {
    return ComponentUtil.getIntValueBindingOrAttribute(
        component, TobagoConstants.ATTR_SPAN_Y, 1);
  }

// ///////////////////////////////////////////// bean getter + setter

// ///////////////////////////////////////////////////////////////
// ///////////////////////////////////////////// class Row
// ///////////////////////////////////////////////////////////////

  public static class Row implements Serializable {

    private int columns;

    List cells;

    public Row(int columns) {
      setColumns(columns);
    }

    private void addControl(UIComponent component, int spanX) {

      int i = nextFreeColumn();

      cells.set(i, component);
      fill(i + 1, i + spanX);
    }

    private void fill(int start, int end) {

      if (end > columns) {
        LOG.error("Error in Jsp (end > columns). " +
            "Try to insert more spanX as possible.");
        LOG.error("start:   " + start);
        LOG.error("end:     " + end);
        LOG.error("columns: " + columns);
        LOG.error("Actual cells:");
        for (Iterator i = cells.iterator(); i.hasNext();) {
          UIComponent component = (UIComponent) i.next();
          LOG.error("Cell-ID: " + component.getId());
        }

        end = columns; // fix the "end" parameter to continue the processing.
      }

      for (int i = start; i < end; i++) {
        cells.set(i, Marker.USED);
      }
    }

    private int nextFreeColumn() {
      for (int i = 0; i < columns; i++) {
        if (Marker.FREE.equals(cells.get(i))) return i;
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
        cells.add(Marker.FREE);
      }
    }
  }

  public static class Marker implements Serializable {

    public static final Marker FREE = new Marker("free");
    public static final Marker USED = new Marker("used");

    private final String name;

    private Marker(String name) {
      this.name = name;
    }

    public String toString() {
      return name;
    }
  }
}
