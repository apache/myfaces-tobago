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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COLUMNS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTED_LIST_STRING;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_HEADER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH_LIST_STRING;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_OUT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DIRECT_LINK_COUNT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_DIRECT_LINKS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_PAGE_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ROW_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FIRST;
import org.apache.myfaces.tobago.ajax.api.AjaxComponent;
import org.apache.myfaces.tobago.ajax.api.AjaxPhaseListener;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.event.PageActionEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeListener;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.SheetRendererWorkaround;
import org.apache.myfaces.tobago.util.LayoutInfo;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.util.StringUtil;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UIData extends javax.faces.component.UIData
    implements SheetStateChangeSource, SortActionSource, AjaxComponent {

  private static final Log LOG = LogFactory.getLog(UIData.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Data";

  public static final String FACET_SORTER = "sorter";
  public static final String SORTER_ID = "sorter";
  public static final String ATTR_SCROLL_POSITION = "attrScrollPosition";

  public static final String NONE = "none";
  public static final String SINGLE = "single";
  public static final String MULTI = "multi";
  public static final int DEFAULT_DIRECT_LINK_COUNT = 9;
  public static final String ROW_IDX_REGEX = "^\\d+" + SEPARATOR_CHAR + ".*";  
  private static final String DEFAULT_SELECTABLE = MULTI;

  private MethodBinding stateChangeListener;
  private List<Integer> widthList;
  private MethodBinding sortActionListener;
  private SheetState sheetState;
  private Boolean showHeader;
  private String showRowRange;
  private String showPageRange;
  private String showDirectLinks;
  private String columns;
  private Integer directLinkCount;

  private String selectable;

  public void encodeBegin(FacesContext facesContext) throws IOException {
    UILayout.prepareDimension(facesContext, this);
    SheetState state = getSheetState(facesContext);
    if (state.getFirst() > -1 && state.getFirst() < getRowCount()) {
      ValueBinding valueBinding = getValueBinding(ATTR_FIRST);
      if (valueBinding != null) {
        valueBinding.setValue(facesContext, state.getFirst());
      } else {
        setFirst(state.getFirst());
      }
    }
    super.encodeBegin(facesContext);
  }

  public void encodeEnd(FacesContext facesContext) throws IOException {
    setupState(facesContext);
    prepareDimensions(facesContext);
    super.encodeEnd(facesContext);
  }

  public String getShowRowRange() {
    if (showRowRange != null) {
      return showRowRange;
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_ROW_RANGE);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return NONE;
    }
  }

  public void setShowRowRange(String showRowRange) {
    this.showRowRange = showRowRange;
  }

  public String getShowPageRange() {
    if (showPageRange != null) {
      return showPageRange;
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_PAGE_RANGE);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return NONE;
    }
  }

  public void setShowPageRange(String showPageRange) {
    this.showPageRange = showPageRange;
  }

  public String getColumns() {
    if (columns != null) {
      return columns;
    }
    ValueBinding vb = getValueBinding(ATTR_COLUMNS);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getShowDirectLinks() {
    if (showDirectLinks != null) {
      return showDirectLinks;
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_DIRECT_LINKS);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return NONE;
    }
  }

  public void setShowDirectLinks(String showDirectLinks) {
    this.showDirectLinks = showDirectLinks;
  }

  public String getSelectable() {
    if (selectable != null) {
      return selectable;
    }
    ValueBinding vb = getValueBinding(ATTR_SELECTABLE);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return DEFAULT_SELECTABLE;
    }
  }

  public void setSelectable(String selectable) {
    this.selectable = selectable;
  }

  public Integer getDirectLinkCount() {
    if (directLinkCount != null) {
      return directLinkCount;
    }
    ValueBinding vb = getValueBinding(ATTR_DIRECT_LINK_COUNT);
    if (vb != null) {
      return (Integer) vb.getValue(getFacesContext());
    } else {
      return DEFAULT_DIRECT_LINK_COUNT;
    }
  }

  public void setDirectLinkCount(Integer directLinkCount) {
    this.directLinkCount = directLinkCount;
  }

  private void setupState(FacesContext facesContext) {
    SheetState state = getSheetState(facesContext);
    ensureColumnWidthList(facesContext, state);
  }

  public void setState(SheetState state) {
    this.sheetState = state;
  }

  public SheetState getSheetState(FacesContext facesContext) {
    if (sheetState != null) {
      return sheetState;
    } else {
      ValueBinding stateBinding = getValueBinding(ATTR_STATE);
      if (stateBinding != null) {
        SheetState state = (SheetState) stateBinding.getValue(facesContext);
        if (state == null) {
          state = new SheetState();
          stateBinding.setValue(facesContext, state);
        }
        return state;
      } else {
        sheetState = new SheetState();
        return sheetState;
      }
    }
  }

  private void ensureColumnWidthList(FacesContext facesContext, SheetState state) {
    List<Integer> currentWidthList = null;
    List<UIColumn> columns = getRendererdColumns();

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
      currentWidthList = StringUtil.parseIntegerList(widthListString);
    }
    if (currentWidthList != null && currentWidthList.size() != columns.size()) {
      currentWidthList = null;
    }


    if (currentWidthList == null) {
      String columnLayout =
          (String) attributes.get(ATTR_COLUMNS);
      List<UIColumn> allColumns = getAllColumns();

      if (columnLayout == null && allColumns.size() > 0) {
        StringBuilder sb = new StringBuilder();
        for (UIColumn allColumn : allColumns) {
          sb.append("1*;");
        }
        columnLayout = sb.deleteCharAt(sb.lastIndexOf(";")).toString();
        if (LOG.isWarnEnabled()) {
          LOG.warn(
              "No columns found! Using created layout tokens: " + columnLayout);
        }
      }
      String[] layoutTokens
          = LayoutInfo.createLayoutTokens(columnLayout, allColumns.size(), "1*");
      if (layoutTokens.length != allColumns.size()) {
        LOG.warn("Count of columnLayout tokens in '" + columnLayout
            + "' != count of columns (" + allColumns.size() + ")! "
            + "Using default token '1*' for all columns");
        layoutTokens
            = LayoutInfo.createLayoutTokens(null, allColumns.size(), "1*");
      }

      // here we have layoutTokens for all columns
      // now remove tokens for unrendered columns
      boolean changed = false;
      for (int i = 0; i < layoutTokens.length; i++) {
        if (!allColumns.get(i).isRendered()) {
          layoutTokens[i] = null;
          changed = true;
        }
      }
      if (changed) {
        String[] allTokens = layoutTokens;
        layoutTokens = new String[columns.size()];
        int j = 0;
        for (String allToken : allTokens) {
          if (allToken != null) {
            layoutTokens[j] = allToken;
            j++;
          }
        }
      }



      int space = LayoutUtil.getInnerSpace(facesContext, this, true);
      SheetRendererWorkaround renderer
          = (SheetRendererWorkaround) ComponentUtil.getRenderer(facesContext, this);
      space -= renderer.getContentBorder(facesContext, this);
      if (renderer.needVerticalScrollbar(facesContext, this)) {
        space -= renderer.getScrollbarWidth(facesContext, this);
      }
      LayoutInfo layoutInfo = new LayoutInfo(getRendererdColumns().size(),
          space, layoutTokens, false);
      parseFixedWidth(facesContext, layoutInfo);
      layoutInfo.parseColumnLayout(space);
      currentWidthList = layoutInfo.getSpaceList();
    }

    if (currentWidthList != null) {
      if (columns.size() != currentWidthList.size()) {
        LOG.warn("widthList.size() = " + currentWidthList.size()
            + " != columns.size() = " + columns.size() + "  widthList : "
            + LayoutInfo.listToTokenString(currentWidthList));
      } else {
        this.widthList = currentWidthList;
      }
    }
  }

  private void parseFixedWidth(FacesContext facesContext, LayoutInfo layoutInfo) {
    String[] tokens = layoutInfo.getLayoutTokens();
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i].equals("fixed")) {
        int width = 0;
        final List<UIColumn> columns = getRendererdColumns();
        if (!columns.isEmpty()) {
          if (i < columns.size()) {
            UIColumn column = columns.get(i);
            if (column instanceof UIColumnSelector) {
                RendererBase renderer
                    = ComponentUtil.getRenderer(facesContext, column);
              if (renderer == null) {
                LOG.warn("can't find renderer for " + column.getClass().getName());
                renderer = ComponentUtil.getRenderer(facesContext, UIPanel.COMPONENT_FAMILY, RENDERER_TYPE_OUT);
              }
              width = renderer.getFixedWidth(facesContext, column);

            } else {
              for (UIComponent component : (List<UIComponent>) column.getChildren()) {
                RendererBase renderer
                    = ComponentUtil.getRenderer(facesContext, component);
                width += renderer.getFixedWidth(facesContext, component);
              }
            }
            layoutInfo.update(width, i);
          } else {
            layoutInfo.update(0, i);
            if (LOG.isWarnEnabled()) {
              LOG.warn("More LayoutTokens found than rows! skipping!");
            }
          }
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("set column " + i + " from fixed to with " + width);
        }
      }
    }
  }


  private void prepareDimensions(FacesContext facesContext) {
    // prepare width's in column's children components

    List<Integer> columnWidths = getWidthList();
    int i = 0;
    for (UIColumn column : getRendererdColumns()) {
      if (i < columnWidths.size()) {
        Integer width = columnWidths.get(i);
        if (!(column instanceof UIColumnSelector)) {
          if (column.getChildCount() == 1) {
            UIComponent child = (UIComponent) column.getChildren().get(0);
            int cellPaddingWidth = ((RendererBase) getRenderer(facesContext))
                .getConfiguredValue(facesContext, this, "cellPaddingWidth");
            child.getAttributes().put(
                ATTR_LAYOUT_WIDTH, width - cellPaddingWidth);
            child.getAttributes().remove(ATTR_INNER_WIDTH);
           } else {
            LOG.warn("More or less than 1 child in column! "
                + "Can't set width for column " + i + " to " + width);
          }
        }
      } else {
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
    if (rows == 0) {
      // avoid division by zero
      return 0;
    }
    if ((first % rows) > 0) {
      return (first / rows) + 1;
    } else {
      return (first / rows);
    }
  }

  public int getPages() {
    int rows = getRows();
    if (rows == 0) {
      return 0;
    }
    return getRowCount() / rows + (getRowCount() % rows == 0 ? 0 : 1);
  }

  public List<UIComponent> getRenderedChildrenOf(UIColumn column) {
    List<UIComponent> children = new ArrayList<UIComponent>();
    for (Object o : column.getChildren()) {
      UIComponent kid = (UIComponent) o;
      if (kid.isRendered()) {
        children.add(kid);
      }
    }
    return children;
  }

  public boolean isAtBeginning() {
    return getFirst() == 0;
  }

  public boolean hasRowCount() {
    return getRowCount() != -1;
  }

  public boolean isAtEnd() {
    if (!hasRowCount()) {
      setRowIndex(getFirst()+getRows()+1);
      return !isRowAvailable();
    } else {
      return getFirst() >= getLastPageIndex();
    }
  }

  public int getLastPageIndex() {
    int rows = getRows();
    if (rows == 0) {
      // avoid division by zero
      return 0;
    }
    int rowCount = getRowCount();
    int tail = rowCount % rows;
    return rowCount - (tail != 0 ? tail : rows);
  }

  public void processUpdates(FacesContext context) {
    super.processUpdates(context);
    updateSheetState(context);
  }

  private void updateSheetState(FacesContext facesContext) {
    SheetState state = getSheetState(facesContext);
    if (state != null) {
      // ensure sortActionListener
//      getSortActionListener();
//      state.setSortedColumn(sortActionListener != null ? sortActionListener.getColumn() : -1);
//      state.setAscending(sortActionListener != null && sortActionListener.isAscending());
      Map attributes = getAttributes();
      //noinspection unchecked
      state.setSelectedRows((List<Integer>) attributes.get(ATTR_SELECTED_LIST_STRING));
      state.setColumnWidths((String) attributes.get(ATTR_WIDTH_LIST_STRING));
      state.setScrollPosition((Integer[]) attributes.get(ATTR_SCROLL_POSITION));
      attributes.remove(ATTR_SCROLL_POSITION);
    }
  }



  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[11];
    saveState[0] = super.saveState(context);
    saveState[1] = sheetState;
    saveState[2] = saveAttachedState(context, sortActionListener);
    saveState[3] = saveAttachedState(context, stateChangeListener);
    saveState[4] = showHeader;
    saveState[5] = showRowRange;
    saveState[6] = showPageRange;
    saveState[7] = showDirectLinks;
    saveState[8] = directLinkCount;
    saveState[9] = selectable;
    saveState[10] = columns;
    return saveState;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    sheetState = (SheetState) values[1];
    sortActionListener = (MethodBinding) restoreAttachedState(context, values[2]);
    stateChangeListener = (MethodBinding) restoreAttachedState(context, values[3]);
    showHeader = (Boolean) values[4];
    showRowRange = (String) values[5];
    showPageRange = (String) values[6];
    showDirectLinks = (String) values[7];
    directLinkCount = (Integer) values[8];
    selectable = (String) values[9];
    columns = (String) values[10];
  }


  public List<UIColumn> getAllColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>) getChildren()) {
      if (kid instanceof UIColumn) {
        columns.add((UIColumn) kid);
      }
    }
    return columns;
  }

  public List<UIColumn> getRendererdColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>) getChildren()) {
      if (kid instanceof UIColumn && kid.isRendered()) {
        columns.add((UIColumn) kid);
      }
    }
    return columns;
  }

  public MethodBinding getSortActionListener() {
    if (sortActionListener != null) {
      return sortActionListener;
    } else {
      return new Sorter();
    }
  }

  public void setSortActionListener(MethodBinding sortActionListener) {
    this.sortActionListener = sortActionListener;
  }

  public void queueEvent(FacesEvent facesEvent) {
    UIComponent parent = getParent();
    if (parent == null) {
      throw new IllegalStateException(
          "component is not a descendant of a UIViewRoot");
    }

    if (facesEvent.getComponent() == this
        && (facesEvent instanceof SheetStateChangeEvent
        || facesEvent instanceof PageActionEvent)) {
      parent.queueEvent(facesEvent);
    } else {
      UIComponent source = facesEvent.getComponent();
      UIComponent sourceParent = source.getParent();
      if (sourceParent.getParent() == this
          && source.getId() != null && source.getId().endsWith(SORTER_ID)) {
        parent.queueEvent(new SortActionEvent(this, (UIColumn) sourceParent));
      } else {
        super.queueEvent(facesEvent);
      }
    }
  }

  public void broadcast(FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if (facesEvent instanceof SheetStateChangeEvent) {
      invokeMethodBinding(getStateChangeListener(), facesEvent);
    } else if (facesEvent instanceof PageActionEvent) {
      invokeMethodBinding(new Pager(), facesEvent);
      invokeMethodBinding(getStateChangeListener(), new SheetStateChangeEvent(this));
    } else if (facesEvent instanceof SortActionEvent) {
      getSheetState(getFacesContext()).updateSortState((SortActionEvent) facesEvent);
      invokeMethodBinding(getSortActionListener(), facesEvent);
    }
  }

  private void invokeMethodBinding(MethodBinding methodBinding, FacesEvent event) {
    if (methodBinding != null && event != null) {
      try {
        Object[] objects = new Object [] {event};
        methodBinding.invoke(getFacesContext(), objects);
      } catch (EvaluationException e) {
        Throwable cause = e.getCause();
        if (cause instanceof AbortProcessingException) {
          throw (AbortProcessingException) cause;
        } else {
          throw e;
        }
      }
    }
  }

  public void addStateChangeListener(SheetStateChangeListener listener) {
    addFacesListener(listener);
  }

  public SheetStateChangeListener[] getStateChangeListeners() {
    return  (SheetStateChangeListener[]) getFacesListeners(SheetStateChangeListener.class);
  }

  public void removeStateChangeListener(SheetStateChangeListener listener) {
    removeFacesListener(listener);
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
    if (showHeader != null) {
      return showHeader;
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_HEADER);
    if (vb != null) {
      return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
    } else {
      return true;
    }
  }

  public void setShowHeader(boolean showHeader) {
    this.showHeader = showHeader;
  }

  public void encodeAjax(FacesContext facesContext) throws IOException {
    setupState(facesContext);
    prepareDimensions(facesContext);
    AjaxUtils.encodeAjaxComponent(facesContext, this);
  }

  public void processAjax(FacesContext facesContext) throws IOException {
    final String ajaxId = (String) facesContext.getExternalContext()
        .getRequestParameterMap().get(AjaxPhaseListener.AJAX_COMPONENT_ID);
    if (ajaxId.equals(getClientId(facesContext))) {
      AjaxUtils.processActiveAjaxComponent(facesContext, this);
    } else {
      AjaxUtils.processAjaxOnChildren(facesContext, this);
    }
  }

  public Integer[] getScrollPosition() {
    Integer[] scrollPosition = (Integer[]) getAttributes().get(ATTR_SCROLL_POSITION);
    if (scrollPosition == null) {
      scrollPosition = getSheetState(FacesContext.getCurrentInstance()).getScrollPosition();
    }
    return scrollPosition;
  }


  public UIComponent findComponent(String searchId) {
    if (searchId.matches(ROW_IDX_REGEX)) {
      searchId = searchId.substring(searchId.indexOf(SEPARATOR_CHAR) + 1);
    }
    return super.findComponent(searchId);
  }
}
