/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 27.04.2004 at 18:33:04.
  * $Id$
  */
package com.atanion.tobago.component;

import com.atanion.tobago.model.SheetState;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.html.scarborough.standard.tag.SheetRenderer;
import com.atanion.tobago.util.LayoutInfo;
import com.atanion.tobago.util.LayoutUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.atanion.tobago.TobagoConstants.*;

public class UIData extends javax.faces.component.UIData {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(UIData.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.Data";

// ----------------------------------------------------------------- attributes

  private MethodBinding stateChangeListener;

  private List<Integer> widthList;

  private Sorter sorter;

  private boolean showHeader = true;
  private boolean showHeaderSet = false;

// ----------------------------------------------------------- business methods

  public void encodeBegin(FacesContext facesContext) throws IOException {
    UILayout.prepareDimension(facesContext, this);
    super.encodeBegin(facesContext);
  }

  public void encodeEnd(FacesContext facesContext) throws IOException {
    setupState(facesContext);
    prepareDimensions(facesContext);
    super.encodeEnd(facesContext);
  }

  private void setupState(FacesContext facesContext) {
    SheetState state = getSheetState(facesContext);
    ensureColumnWidthList(facesContext, state);
  }

  public SheetState getSheetState(FacesContext facesContext) {
    ValueBinding stateBinding = getValueBinding(ATTR_STATE);
    if (stateBinding != null) {
      SheetState state = (SheetState) stateBinding.getValue(facesContext);
      if (state == null) {
        state = new SheetState();
        stateBinding.setValue(facesContext, state);
      }
      return state;
    } else {
      return null;
    }
  }

  private void ensureColumnWidthList(FacesContext facesContext, SheetState state) {
    List<Integer> widthList = null;
    List columns = getColumns();

    final Map attributes = getAttributes();
    String widthListString = null;

    if (state != null) {
      widthListString = state.getColumnWidths();
    }
    if (widthListString == null) {
      widthListString =
          (String) attributes.get(ATTR_WIDTH_LIST_STRING);
    }

    if (widthListString != null) {
      widthList = SheetState.parse(widthListString);
    }
    if (widthList != null && widthList.size() != columns.size()) {
      widthList = null;
    }


    if (widthList == null) {
      String columnLayout =
          (String) attributes.get(ATTR_COLUMNS);

      if (columnLayout == null && columns.size() > 0) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columns.size(); i++) {
          sb.append("1*;");
        }
        columnLayout = sb.deleteCharAt(sb.lastIndexOf(";")).toString();
        if (LOG.isWarnEnabled()) {
          LOG.warn(
              "No columns found! Using created layout tokens: " + columnLayout);
        }
      }

      if (widthList == null) {
        int space = LayoutUtil.getInnerSpace(facesContext, this, true);
        SheetRenderer renderer
            = (SheetRenderer) ComponentUtil.getRenderer(facesContext, this);
        space -= renderer.getContentBorder(facesContext, this);
        if (renderer.needVerticalScrollbar(facesContext, this)) {
          space -= renderer.getScrollbarWidth(facesContext, this);
        }
        LayoutInfo layoutInfo = new LayoutInfo(getColumns().size(),
            space, columnLayout);
        layoutInfo.parseColumnLayout(space);
        widthList = layoutInfo.getSpaceList();
      }
    }

    if (widthList != null) {
      if (columns.size() != widthList.size()) {
        LOG.warn("widthList.size() = " + widthList.size() +
            " != columns.size() = " + columns.size() + "  widthList : "
            + LayoutInfo.listToTokenString(widthList));
      } else {
        this.widthList = widthList;
      }
    }
  }

  private void prepareDimensions(FacesContext facesContext) {
    // prepare width's in column's children components

    List columnWidths = getWidthList();
    int i = 0;
    for (UIColumn column : getColumns()) {
      if (i < columnWidths.size()) {
        Integer width = (Integer) columnWidths.get(i);
        if (!(column instanceof UIColumnSelector)) {
          if (column.getChildCount() == 1) {
            UIComponent child = (UIComponent) column.getChildren().get(0);
            int cellPaddingWidth = ((RendererBase) getRenderer(facesContext))
                .getConfiguredValue(facesContext, this, "cellPaddingWidth");
            child.getAttributes().put(ATTR_LAYOUT_WIDTH,
                new Integer(width.intValue() - cellPaddingWidth));
          } else {
            LOG.warn("More or less than 1 child in column! "
                + "Can't set width for column " + i + " to " + width);
          }
        }
      }
      else {
        LOG.warn("More columns than columnSizes! "
            + "Can't set width for column " + i);
      }
      i++;
    }
  }

  public int getLast() {
    int last = getFirst() + getRows();
    return last < getRowCount() ? last : getRowCount();
  }

  public int getPage() {
    int first = getFirst() + 1;
    int rows = getRows();
    if ((first % rows) == 1) {
      return (first / rows) + 1;
    } else {
      return (first / rows) + 2;
    }
  }

  public int getPages() {
    return getRowCount() / getRows() + (getRowCount() % getRows() == 0 ? 0 : 1);
  }

  public List<UIComponent> getRenderedChildrenOf(UIColumn column) {
    List<UIComponent> children = new ArrayList<UIComponent>();
    for (Iterator kids = column.getChildren().iterator(); kids.hasNext();) {
      UIComponent kid = (UIComponent) kids.next();
      if (kid.isRendered()) {
        children.add(kid);
      }
    }
    return children;
  }

  public boolean isAtBeginning() {
    return getFirst() == 0;
  }

  public boolean isAtEnd() {
    return getFirst() >= getLastPageIndex();
  }

  public int getLastPageIndex() {
    int tail = getRowCount() % getRows();
    return getRowCount() - (tail != 0 ? tail : getRows());
  }

  public void processUpdates(FacesContext context) {
    super.processUpdates(context);
    updateSheetState(context);
  }

  public void updateSheetState(FacesContext facesContext) {
    SheetState state = getSheetState(facesContext);
    if (state != null) {
      // ensure sorter
      getSorter();
      state.setSortedColumn(sorter != null ? sorter.getColumn() : -1);
      state.setAscending(sorter != null && sorter.isAscending());
      state.setSelected((String)
          getAttributes().get(ATTR_SELECTED_LIST_STRING));
      state.setColumnWidths((String)
          getAttributes().get(ATTR_WIDTH_LIST_STRING));
    }
  }

// ------------------------------------------------------------ getter + setter

  public List<UIColumn> getColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>)getChildren()) {
      if (kid instanceof UIColumn && kid.isRendered()) {
        columns.add((UIColumn)kid);
      }
    }
    return columns;
  }

  public Sorter getSorter() {
    if (sorter == null) {
      sorter = new Sorter(this);
    }
    return sorter;
  }

  public void setSorter(Sorter sorter) {
    this.sorter = sorter;
  }

  public MethodBinding getStateChangeListener() {
    return stateChangeListener;
  }

  public void setStateChangeListener(MethodBinding stateChangeListener) {
    this.stateChangeListener = stateChangeListener;
  }

  public List<Integer> getWidthList() {
    return widthList;
  }

  public boolean isShowHeader() {
    if (showHeaderSet) {
        return (showHeader);
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_HEADER);
    if (vb != null) {
        return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
    } else {
        return (this.showHeader);
    }
  }

  public void setShowHeader(boolean showHeader) {
    this.showHeader = showHeader;
    this.showHeaderSet = true;
  }


}