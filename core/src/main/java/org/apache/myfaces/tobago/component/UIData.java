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
import org.apache.myfaces.tobago.renderkit.LayoutInformationProvider;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.SheetRendererWorkaround;
import org.apache.myfaces.tobago.util.LayoutInfo;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.util.StringUtils;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COLUMNS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DIRECT_LINK_COUNT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FIRST;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ROWS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTED_LIST_STRING;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_DIRECT_LINKS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_HEADER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_PAGE_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ROW_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH_LIST_STRING;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_RELOAD;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_OUT;

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
  public static final int DEFAULT_ROW_COUNT = 100;
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
  private Integer rows;

  private String selectable;

  private transient LayoutTokens columnLayout;

  /**
   * Remove the (by user) resized column widths. An application may provide a button to access it.
   * Since 1.0.26.
   */
  public void resetColumnWidths() {
    SheetState state = getSheetState(FacesContext.getCurrentInstance());
    if (state != null) {
      state.setColumnWidths(null);
    }
    getAttributes().remove(ATTR_WIDTH_LIST_STRING);
  }

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

  public void processDecodes(FacesContext context) {
    final String ajaxId = (String) context.getExternalContext()
        .getRequestParameterMap().get(AjaxPhaseListener.AJAX_COMPONENT_ID);
    if (ajaxId !=null && ajaxId.equals(getClientId(context))) {
      if (getFacet(FACET_RELOAD) != null && getFacet(FACET_RELOAD) instanceof UIReload
          && getFacet(FACET_RELOAD).isRendered()
          && ((UIReload) getFacet(FACET_RELOAD)).isImmediate()
          && ajaxId.equals(ComponentUtil.findPage(context, this).getActionId())) {
        UIReload reload = (UIReload) getFacet(FACET_RELOAD);
        if (!reload.getUpdate()) {
          if (context.getExternalContext().getResponse() instanceof HttpServletResponse) {
             ((HttpServletResponse) context.getExternalContext().getResponse())
                 .setStatus(HttpServletResponse.SC_NOT_MODIFIED);
          }
          context.responseComplete();
          return;
        }
      }
    }
    super.processDecodes(context);
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

  public LayoutTokens getColumnLayout() {
    if (columnLayout == null) {
      String columns = getColumns();
      if (columns != null) {
        columnLayout = LayoutTokens.parse(columns);
      }
    }
    return columnLayout;
  }

  private void ensureColumnWidthList(FacesContext facesContext, SheetState state) {
    List<Integer> currentWidthList = null;
    List<UIColumn> rendererdColumns = getRenderedColumns();

    final Map attributes = getAttributes();
    String widthListString = null;

    if (state != null) {
      widthListString = state.getColumnWidths();
    }
    if (widthListString == null) {
      widthListString = (String) attributes.get(ATTR_WIDTH_LIST_STRING);
    }

    if (widthListString != null) {
      currentWidthList = StringUtils.parseIntegerList(widthListString);
    }
    if (currentWidthList != null && currentWidthList.size() != rendererdColumns.size()) {
      currentWidthList = null;
    }


    if (currentWidthList == null) {
      LayoutTokens tokens = getColumnLayout();
      List<UIColumn> allColumns = getAllColumns();
      LayoutTokens newTokens = new LayoutTokens();
      if (allColumns.size() > 0) {
        for (int i = 0; i < allColumns.size(); i++) {
          UIColumn column = allColumns.get(i);
          if (column.isRendered()) {
            if (tokens == null) {
              if (column instanceof org.apache.myfaces.tobago.component.UIColumn) {
                newTokens.addToken(
                    LayoutTokens.parseToken(((org.apache.myfaces.tobago.component.UIColumn) column).getWidth()));
              } else {
                newTokens.addToken(RelativeLayoutToken.DEFAULT_INSTANCE);
              }
            } else {
              if (i < tokens.getSize()) {
                newTokens.addToken(tokens.get(i));
              } else {
                newTokens.addToken(RelativeLayoutToken.DEFAULT_INSTANCE);
              }
            }
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
      LayoutInfo layoutInfo = new LayoutInfo(newTokens.getSize(), space, newTokens, getClientId(facesContext), false);
      parseFixedWidth(facesContext, layoutInfo, rendererdColumns);
      layoutInfo.parseColumnLayout(space);
      currentWidthList = layoutInfo.getSpaceList();
    }

    if (currentWidthList != null) {
      if (rendererdColumns.size() != currentWidthList.size()) {
        LOG.warn("widthList.size() = " + currentWidthList.size()
            + " != columns.size() = " + rendererdColumns.size() + "  widthList : "
            + LayoutInfo.listToTokenString(currentWidthList));
      } else {
        this.widthList = currentWidthList;
      }
    }
  }

  private void parseFixedWidth(FacesContext facesContext, LayoutInfo layoutInfo, List<UIColumn> rendereredColumns) {
    LayoutTokens tokens = layoutInfo.getLayoutTokens();
    for (int i = 0; i < tokens.getSize(); i++) {
      LayoutToken token = tokens.get(i);
      if (token instanceof FixedLayoutToken) {
        int width = 0;
        if (!rendereredColumns.isEmpty()) {
          if (i < rendereredColumns.size()) {
            UIColumn column = rendereredColumns.get(i);
            if (column instanceof UIColumnSelector) {
              LayoutInformationProvider renderer
                  = ComponentUtil.getRenderer(facesContext, column);
              if (renderer == null) {
                LOG.warn("can't find renderer for " + column.getClass().getName());
                renderer = ComponentUtil.getRenderer(facesContext, UIPanel.COMPONENT_FAMILY, RENDERER_TYPE_OUT);
              }
              width = renderer.getFixedWidth(facesContext, column);

            } else {
              for (UIComponent component : (List<UIComponent>) column.getChildren()) {
                LayoutInformationProvider renderer
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
    for (UIColumn column : getRenderedColumns()) {
      if (i < columnWidths.size()) {
        Integer width = columnWidths.get(i);
        if (!(column instanceof UIColumnSelector)) {
          if (column.getChildCount() == 1) {
            UIComponent child = (UIComponent) column.getChildren().get(0);
            int cellPaddingWidth = ((LayoutableRendererBase) getRenderer(facesContext))
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
      setRowIndex(getFirst() + getRows() + 1);
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
      attributes.remove(ATTR_SELECTED_LIST_STRING);
      attributes.remove(ATTR_SCROLL_POSITION);
    }
  }


  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[12];
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
    saveState[11] = rows;
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
    rows = (Integer) values[11];
  }


  public List<UIColumn> getAllColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>) getChildren()) {
      if (kid instanceof UIColumn && !(kid instanceof UIColumnEvent)) {
        columns.add((UIColumn) kid);
      }
    }
    return columns;
  }

  public List<UIColumn> getRenderedColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>) getChildren()) {
      if (kid instanceof UIColumn && kid.isRendered() && !(kid instanceof UIColumnEvent)) {
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
      facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
      if (LOG.isInfoEnabled()) {
        LOG.info("queueEvent = \"" + facesEvent + "\"");
      }
      parent.queueEvent(facesEvent);
    } else {
      UIComponent source = facesEvent.getComponent();
      UIComponent sourceParent = source.getParent();
      if (sourceParent.getParent() == this
          && source.getId() != null && source.getId().endsWith(SORTER_ID)) {
        facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
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
        Object[] objects = new Object[]{event};
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
    return (SheetStateChangeListener[]) getFacesListeners(SheetStateChangeListener.class);
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

  public int getRows() {
    if (rows != null) {
      return rows;
    }
    ValueBinding vb = getValueBinding(ATTR_ROWS);
    if (vb != null) {
      return (Integer) vb.getValue(getFacesContext());
    } else {
      return DEFAULT_ROW_COUNT;
    }
  }

  public void setRows(int rows) {
    this.rows = rows;
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
    // TODO neets more testing!!!
    //if (!facesContext.getRenderResponse() && !ComponentUtil.hasErrorMessages(facesContext)) {
    // in encodeBegin of superclass is some logic which clears the DataModel
    // this must here also done.
    // in RI and myfaces this could done via setValue(null)
    ValueBinding binding = getValueBinding("value");
    if (binding != null) {
      setValue(null);
    } else {
      setValue(getValue());
    }
    //}
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
    return super.findComponent(stripRowIndex(searchId));
  }

  String stripRowIndex(String searchId) {
    if (searchId.length() > 0 && Character.isDigit(searchId.charAt(0))) {
      for (int i = 1; i < searchId.length(); ++i) {
        char c = searchId.charAt(i);
        if (c == SEPARATOR_CHAR) {
          searchId = searchId.substring(i + 1);
          break;
        }
        if (!Character.isDigit(c)) {
          break;
        }
      }
    }
    return searchId;
  }
}
