package org.apache.myfaces.tobago.internal.component;

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

import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.compat.InvokeOnComponent;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.ColumnEvent;
import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Sorter;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.event.PageActionEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeListener;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractUISheet extends javax.faces.component.UIData
    implements SheetStateChangeSource, SortActionSource, InvokeOnComponent, OnComponentPopulated,
    LayoutContainer, LayoutComponent, SupportsRenderedPartially{

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUISheet.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Data";

  /**
   * @see org.apache.myfaces.tobago.component.Facets
   * @deprecated Please use Facets instead. Will be removed after Tobago 1.5.0 */
  @Deprecated
  public static final String FACET_SORTER = "sorter";
  public static final String SORTER_ID = "sorter";
  /**
   * @see org.apache.myfaces.tobago.component.Attributes
   * @deprecated Please use Attributes instead. Will be removed after Tobago 1.5.0 */
  @Deprecated
  public static final String ATTR_SCROLL_POSITION = "attrScrollPosition";

  public static final String NONE = "none";
  public static final String SINGLE = "single";
  public static final String MULTI = "multi";

  private SheetState sheetState;
  private List<Integer> widthList;
  private transient LayoutTokens columnLayout;

  private transient int ajaxResponseCode;

  private transient List<LayoutComponent> layoutComponents;

  private transient Boolean needVerticalScrollbar;

  public LayoutComponentRenderer getLayoutComponentRenderer(FacesContext context) {
    return (LayoutComponentRenderer) getRenderer(context);
  }

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {
    SheetState state = getSheetState(facesContext);
    if (state.getFirst() > -1 && (!hasRowCount() || state.getFirst() < getRowCount())) {
      if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.FIRST)) {
        FacesUtils.setValueOfBindingOrExpression(facesContext, state.getFirst(), this, Attributes.FIRST);
      } else {
        setFirst(state.getFirst());
      }
    }
    super.encodeBegin(facesContext);
  }

  public void setState(SheetState state) {
    this.sheetState = state;
  }

  public SheetState getState() {
    return getSheetState(FacesContext.getCurrentInstance());
  }

  public SheetState getSheetState(FacesContext facesContext) {
    if (sheetState != null) {
      return sheetState;
    } else {
      if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.STATE)) {
        SheetState state = (SheetState)
            FacesUtils.getValueFromValueBindingOrValueExpression(facesContext, this, Attributes.STATE);
        if (state == null) {
          state = new SheetState();
          FacesUtils.setValueOfBindingOrExpression(facesContext, state, this, Attributes.STATE);
        }
        return state;
      } else {
        sheetState = new SheetState();
        return sheetState;
      }
    }
  }

  public abstract String getColumns();

  public LayoutTokens getColumnLayout() {
    if (columnLayout == null) {
      String columns = getColumns();
      if (columns != null) {
        columnLayout = LayoutTokens.parse(columns);
      }
    }
    return columnLayout;
  }

  /**
   * Remove the (by user) resized column widths. An application may provide a button to access it.
   * Since 1.0.26.
   */
  public void resetColumnWidths() {
    SheetState state = getState();
    if (state != null) {
      state.setColumnWidths(null);
    }
    getAttributes().remove(Attributes.WIDTH_LIST_STRING);
  }

  /**
   * @deprecated The name of this method is ambiguous.
   * You may use {@link #getLastRowIndexOfCurrentPage()}. Deprecated since 1.5.5.
   */
  public int getLast() {
    int last = getFirst() + getRows();
    return last < getRowCount() ? last : getRowCount();
  }

  /**
   * The rowIndex of the last row on the current page plus one (because of zero based iterating).
   * @throws IllegalArgumentException If the number of rows in the model returned
   * by {@link #getRowCount()} is -1 (undefined).
   */
  public int getLastRowIndexOfCurrentPage() {
    if (!hasRowCount()) {
      throw new IllegalArgumentException(
          "Can't determine the last row, because the row count of the model is unknown.");
    }
    if (isRowsUnlimited()) {
      return getRowCount();
    }
    int last = getFirst() + getRows();
    return last < getRowCount() ? last : getRowCount();
  }

  /**
   * @return returns the current page (based by 0).
   */
  public int getCurrentPage() {
    int rows = getRows();
    if (rows == 0) {
      // if the rows are unlimited, there is only one page
      return 0;
    }
    int first = getFirst();
    if (hasRowCount() && first >= getRowCount()) {
      return getPages() - 1; // last page
    } else {
      return (first / rows);
    }
  }
  
  /**
   * @return returns the current page (based by 1).
   * @deprecated Please use {@link #getCurrentPage()} which returns the value zero-based. Deprecated since 1.5.5.
   */
  @Deprecated
  public int getPage() {
    return getCurrentPage() + 1;
  }

  /**
   * The number of pages to render.
   * @throws IllegalArgumentException If the number of rows in the model returned
   * by {@link #getRowCount()} is -1 (undefined).
   */
  public int getPages() {
    if (isRowsUnlimited()) {
      return 1;
    }
    if (!hasRowCount()) {
      throw new IllegalArgumentException(
          "Can't determine the number of pages, because the row count of the model is unknown.");
    }
    return (getRowCount() - 1) / getRows() + 1;
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

  /**
   * @return Is the interval to display starting with the first row?
   */
  public boolean isAtBeginning() {
    return getFirst() == 0;
  }

  /**
   * @return Does the data model knows the number of rows?
   */
  public boolean hasRowCount() {
    return getRowCount() != -1;
  }

  /**
   * @deprecated The name of this method is ambiguous.
   * You may use {@link #isRowsUnlimited()}. Deprecated since 1.5.5.
   */
  @Deprecated
  public boolean hasRows() {
    return getRows() != 0;
  }

  /**
   * @return Is the (maximum) number of rows to display set to zero?
   */
  public boolean isRowsUnlimited() {
    return getRows() == 0;
  }

  /**
   * @return Should the paging controls be rendered? Either because of the need of paging or because
   * the show is enforced by {@link #isShowPagingAlways()}
   */
  public boolean isPagingVisible() {
    return isShowPagingAlways() || needMoreThanOnePage();
  }

  /**
   * @return Is panging needed to display all rows? If the number of rows is unknown this method returns true.
   */
  public boolean needMoreThanOnePage() {
    if (isRowsUnlimited()) {
      return false;
    } else if (!hasRowCount()) {
      return true;
    } else {
      return getRowCount() > getRows();
    }
  }

  public abstract boolean isShowPagingAlways();

  public boolean isAtEnd() {
    if (!hasRowCount()) {
      final int old = getRowIndex();
      setRowIndex(getFirst() + getRows() + 1);
      final boolean atEnd = !isRowAvailable();
      setRowIndex(old);
      return atEnd;
    } else {
      return getFirst() >= getFirstRowIndexOfLastPage();
    }
  }

  /**
   * @deprecated The name of this method is ambiguous.
   * You may use {@link #getFirstRowIndexOfLastPage()}. Deprecated since 1.5.5.
   */
  @Deprecated
  public int getLastPageIndex() {
    if (hasRowCount()) {
      return getFirstRowIndexOfLastPage();
    } else {
      return 0;
    }
  }

  /**
   * Determines the beginning of the last page in the model.
   * If the number of rows to display on one page is unlimited, the value is 0 (there is only one page).
   * @return The index of the first row of the last paging page.
   * @throws IllegalArgumentException If the number of rows in the model returned
   * by {@link #getRowCount()} is -1 (undefined).
   */
  public int getFirstRowIndexOfLastPage() {
    if (isRowsUnlimited()) {
      return 0;
    } else if (!hasRowCount()) {
      throw new IllegalArgumentException(
          "Can't determine the last page, because the row count of the model is unknown.");
    } else {
      int rows = getRows();
      int rowCount = getRowCount();
      int tail = rowCount % rows;
      return rowCount - (tail != 0 ? tail : rows);
    }
  }

  @Override
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
      final List<Integer> list = (List<Integer>) attributes.get(Attributes.SELECTED_LIST_STRING);
      state.setSelectedRows(list != null ? list : Collections.<Integer>emptyList());
      state.setColumnWidths((String) attributes.get(Attributes.WIDTH_LIST_STRING));
      state.setScrollPosition((Integer[]) attributes.get(Attributes.SCROLL_POSITION));
      attributes.remove(Attributes.SELECTED_LIST_STRING);
      attributes.remove(Attributes.SCROLL_POSITION);
    }
  }


  @Override
  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[2];
    saveState[0] = super.saveState(context);
    saveState[1] = sheetState;
    return saveState;
  }

  @Override
  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    sheetState = (SheetState) values[1];
  }

  public List<UIColumn> getAllColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>) getChildren()) {
      if (kid instanceof UIColumn && !(kid instanceof ColumnEvent)) {
        columns.add((UIColumn) kid);
      }
    }
    return columns;
  }

  public List<UIColumn> getRenderedColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>) getChildren()) {
      if (kid instanceof UIColumn && kid.isRendered() && !(kid instanceof ColumnEvent)) {
        columns.add((UIColumn) kid);
      }
    }
    return columns;
  }

/*  public MethodBinding getSortActionListener() {
    if (sortActionListener != null) {
      return sortActionListener;
    } else {
      return new Sorter();
    }
  }*/

  @Override
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
        LOG.info("queueEvent = '" + facesEvent + "'");
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

  @Override
  public void broadcast(FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if (facesEvent instanceof SheetStateChangeEvent) {
      FacesUtils.invokeMethodBinding(getFacesContext(), getStateChangeListener(), facesEvent);
    } else if (facesEvent instanceof PageActionEvent) {
      if (facesEvent.getComponent() == this) {
        FacesUtils.invokeMethodBinding(
            getFacesContext(), getStateChangeListener(), new SheetStateChangeEvent(this));
        performPaging((PageActionEvent) facesEvent);
      }
    } else if (facesEvent instanceof SortActionEvent) {
      MethodBinding methodBinding = getSortActionListener();
      if (methodBinding!= null) {
        // TODO should be first invokeMethodBinding and the update state
        getSheetState(getFacesContext()).updateSortState((SortActionEvent) facesEvent);
        FacesUtils.invokeMethodBinding(getFacesContext(), methodBinding, facesEvent);
      } else {
        getSheetState(getFacesContext()).updateSortState((SortActionEvent) facesEvent);
        new Sorter().perform((SortActionEvent) facesEvent);
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

  public List<Integer> getWidthList() {
    return widthList;
  }

  public void setWidthList(List<Integer> widthList) {
    this.widthList = widthList;
  }



  public Integer[] getScrollPosition() {
    Integer[] scrollPosition = (Integer[]) getAttributes().get(Attributes.SCROLL_POSITION);
    if (scrollPosition == null) {
      scrollPosition = getSheetState(FacesContext.getCurrentInstance()).getScrollPosition();
    }
    return scrollPosition;
  }


  @Override
  public UIComponent findComponent(String searchId) {
    return super.findComponent(stripRowIndex(searchId));
  }

  public String stripRowIndex(String searchId) {
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

  // todo: after removing jsf 1.1: @Override
  public boolean invokeOnComponent(FacesContext facesContext, String clientId, ContextCallback callback)
      throws FacesException {
    // we may need setRowIndex on UISheet
    int oldRowIndex = getRowIndex();
    try {
      String sheetId = getClientId(facesContext);
      if (clientId.startsWith(sheetId)) {
        String idRemainder = clientId.substring(sheetId.length());
        if (LOG.isDebugEnabled()) {
          LOG.debug("idRemainder = '" + idRemainder + "'");
        }
        if (idRemainder.matches("^:\\d+:.*")) {
          idRemainder = idRemainder.substring(1);
          int idx = idRemainder.indexOf(":");
          try {
            int rowIndex = Integer.parseInt(idRemainder.substring(0, idx));
            if (LOG.isDebugEnabled()) {
              LOG.debug("set rowIndex = '" + rowIndex + "'");
            }
            setRowIndex(rowIndex);
          } catch (NumberFormatException e) {
            LOG.warn("idRemainder = '" + idRemainder + "'", e);
          }
        } else {
          if (LOG.isDebugEnabled()) {
            LOG.debug("no match for '^:\\d+:.*'");
          }
        }
      }

      return FacesUtils.invokeOnComponent(facesContext, this, clientId, callback);

    } finally {
      // we should reset rowIndex on UISheet
      setRowIndex(oldRowIndex);
    }
  }

  public void performPaging(PageActionEvent pageEvent) {

    int first;

    if (LOG.isDebugEnabled()) {
      LOG.debug("action = '" + pageEvent.getAction().name() + "'");
    }

    switch (pageEvent.getAction()) {
      case FIRST:
        first = 0;
        break;
      case PREV:
        first = getFirst() - getRows();
        first = first < 0 ? 0 : first;
        break;
      case NEXT:
        if (hasRowCount()) {
          first = getFirst() + getRows();
          first = first > getRowCount() ? getFirstRowIndexOfLastPage() : first;
        } else {
          if (isAtEnd()) {
            first = getFirst();
          } else {
            first = getFirst() + getRows();
          }
        }
        break;
      case LAST:
        first = getFirstRowIndexOfLastPage();
        break;
      case TO_ROW:
        first = pageEvent.getValue() - 1;
        if (hasRowCount() && first > getFirstRowIndexOfLastPage()) {
          first = getFirstRowIndexOfLastPage();
        } else if (first < 0) {
          first = 0;
        }
        break;
      case TO_PAGE:
        int pageIndex = pageEvent.getValue() - 1;
        first = pageIndex * getRows();
        if (hasRowCount() && first > getFirstRowIndexOfLastPage()) {
          first = getFirstRowIndexOfLastPage();
        } else if (first < 0) {
          first = 0;
        }
        break;
      default:
        // may not happen
        first = -1;
    }

    if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.FIRST)) {
      FacesUtils.setValueOfBindingOrExpression(FacesContext.getCurrentInstance(), first, this, Attributes.FIRST);
    } else {
      setFirst(first);
    }

    getState().setFirst(first);
//      sheet.queueEvent(new SheetStateChangeEvent(sheet));
  }

  public List<LayoutComponent> getComponents() {
    if (layoutComponents != null) {
      return layoutComponents;
    }
    layoutComponents = new ArrayList<LayoutComponent>();
    for (UIComponent column : (List<UIComponent>) getChildren()) {
      if (column instanceof AbstractUIColumnSelector) {
        layoutComponents.add(null); // XXX UIColumnSelector is currently not an instance of LayoutComponent
      } else if (column instanceof ColumnEvent) {
        // ignore
      } else if (column instanceof UIColumn) {
        LayoutComponent layoutComponent = null;
        for (UIComponent component : (List<UIComponent>) column.getChildren()) {
          if (component instanceof LayoutComponent) {
            if (layoutComponent == null) {
              layoutComponent = (LayoutComponent) component;
            } else {
              LOG.warn(
                  "Found more than one layout components inside of a UIColumn: column id='{}' renderer-type='{}'",
                  column.getClientId(FacesContext.getCurrentInstance()),
                  component.getRendererType());
            }
          }
        }
        if (layoutComponent != null) {
          layoutComponents.add(layoutComponent);
        } else {
          final FacesContext facesContext = FacesContext.getCurrentInstance();
          final AbstractUIOut dummy = (AbstractUIOut) CreateComponentUtils.createComponent(
              facesContext, ComponentTypes.OUT, RendererTypes.OUT, facesContext.getViewRoot().createUniqueId());
          dummy.setTransient(true);
          column.getChildren().add(dummy);
          layoutComponents.add(dummy);
          LOG.warn(
              "Found no component inside of a UIColumn: column id='{}'. Creating a dummy with id='{}'!",
              column.getClientId(facesContext), dummy.getClientId(facesContext));
        }
      }
    }
    return layoutComponents;
  }

  public void onComponentPopulated(FacesContext facesContext, UIComponent parent) {
    if (getLayoutManager() == null) {
      setLayoutManager(CreateComponentUtils.createAndInitLayout(
          facesContext, ComponentTypes.SHEET_LAYOUT, RendererTypes.SHEET_LAYOUT, parent));
    }
  }
  
  public LayoutManager getLayoutManager() {
    return (LayoutManager) getFacet(Facets.LAYOUT);
  }

  public void setLayoutManager(LayoutManager layoutManager) {
    getFacets().put(Facets.LAYOUT, (AbstractUILayoutBase) layoutManager);
  }

  public boolean isLayoutChildren() {
    return true;
  }

  public abstract boolean isShowHeader();

  public Boolean getNeedVerticalScrollbar() {
    return needVerticalScrollbar;
  }

  public void setNeedVerticalScrollbar(Boolean needVerticalScrollbar) {
    this.needVerticalScrollbar = needVerticalScrollbar;
  }
}
