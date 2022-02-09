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
import org.apache.myfaces.tobago.event.SheetStateChangeSource2;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.event.SortActionSource2;
import org.apache.myfaces.tobago.internal.layout.Grid;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PhaseId;
import javax.faces.event.PreRenderComponentEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public abstract class AbstractUISheet extends AbstractUIData
    implements SheetStateChangeSource2, SortActionSource2, OnComponentPopulated,
    LayoutContainer, LayoutComponent, SupportsRenderedPartially {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUISheet.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Data";

  /**
   * @see org.apache.myfaces.tobago.component.Facets
   * @deprecated Please use Facets instead. Will be removed after Tobago 1.5.0
   */
  @Deprecated
  public static final String FACET_SORTER = "sorter";
  public static final String SORTER_ID = "sorter";
  /**
   * @see org.apache.myfaces.tobago.component.Attributes
   * @deprecated Please use Attributes instead. Will be removed after Tobago 1.5.0
   */
  @Deprecated
  public static final String ATTR_SCROLL_POSITION = "attrScrollPosition";

  public static final String NONE = "none";
  public static final String SINGLE = "single";
  public static final String MULTI = "multi";

  private SheetState state;
  private List<Integer> widthList;
  private transient LayoutTokens columnLayout;

  private transient List<LayoutComponent> layoutComponents;

  private transient Boolean needVerticalScrollbar;

  private transient Grid headerGrid;

  public LayoutComponentRenderer getLayoutComponentRenderer(final FacesContext context) {
    return (LayoutComponentRenderer) getRenderer(context);
  }

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    final SheetState sheetState = getSheetState(facesContext);
    final int first = sheetState.getFirst();
    if (first > -1 && (!hasRowCount() || first < getRowCount())) {
      final ValueExpression expression = getValueExpression(Attributes.FIRST);
      if (expression != null) {
        expression.setValue(facesContext.getELContext(), first);
      } else {
        setFirst(first);
      }
    }

    super.encodeBegin(facesContext);
  }

  public void setState(final SheetState state) {
    this.state = state;
  }

  public SheetState getState() {
    return getSheetState(FacesContext.getCurrentInstance());
  }

  public SheetState getSheetState(final FacesContext facesContext) {
    if (state != null) {
      return state;
    }

    final ValueExpression expression = getValueExpression(Attributes.STATE);
    if (expression != null) {
      final ELContext elContext = facesContext.getELContext();
      SheetState sheetState = (SheetState) expression.getValue(elContext);
      if (sheetState == null) {
        sheetState = new SheetState();
        expression.setValue(elContext, sheetState);
      }
      return sheetState;
    }

    state = new SheetState();
    return state;
  }

  public abstract String getColumns();

  public LayoutTokens getColumnLayout() {
    if (columnLayout == null) {
      final String columns = getColumns();
      if (columns != null) {
        columnLayout = LayoutTokens.parse(columns);
      }
    }
    return columnLayout;
  }

  /**
   * Remove the (by user) resized column widths. An application may provide a button to access it. Since 1.0.26.
   */
  public void resetColumnWidths() {
    final SheetState sheetState = getState();
    if (sheetState != null) {
      sheetState.setColumnWidths(null);
    }
    getAttributes().remove(Attributes.WIDTH_LIST_STRING);
  }

  /**
   * @deprecated The name of this method is ambiguous. You may use {@link #getLastRowIndexOfCurrentPage()}. Deprecated
   * since 1.5.5.
   */
  @Deprecated
  public int getLast() {
    final int last = getFirst() + getRows();
    return last < getRowCount() ? last : getRowCount();
  }

  /**
   * The rowIndex of the last row on the current page plus one (because of zero based iterating).
   *
   * @throws IllegalArgumentException If the number of rows in the model returned by {@link #getRowCount()} is -1
   * (undefined).
   */
  public int getLastRowIndexOfCurrentPage() {
    if (!hasRowCount()) {
      throw new IllegalArgumentException(
          "Can't determine the last row, because the row count of the model is unknown.");
    }
    if (isRowsUnlimited()) {
      return getRowCount();
    }
    final int last = getFirst() + getRows();
    return last < getRowCount() ? last : getRowCount();
  }

  /**
   * @return returns the current page (based by 0).
   */
  public int getCurrentPage() {
    final int rows = getRows();
    if (rows == 0) {
      // if the rows are unlimited, there is only one page
      return 0;
    }
    final int first = getFirst();
    if (hasRowCount() && first >= getRowCount()) {
      return getPages() - 1; // last page
    } else {
      return first / rows;
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
   *
   * @throws IllegalArgumentException If the number of rows in the model returned by {@link #getRowCount()} is -1
   * (undefined).
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

  public List<UIComponent> getRenderedChildrenOf(final UIColumn column) {
    final List<UIComponent> children = new ArrayList<UIComponent>();
    for (final UIComponent kid : column.getChildren()) {
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
   * @return Should the paging controls be rendered? Either because of the need of paging or because the show is
   * enforced by {@link #isShowPagingAlways()}
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
   * @deprecated The name of this method is ambiguous. You may use {@link #getFirstRowIndexOfLastPage()}. Deprecated
   * since 1.5.5.
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
   * Determines the beginning of the last page in the model. If the number of rows to display on one page is unlimited,
   * the value is 0 (there is only one page).
   *
   * @return The index of the first row of the last paging page.
   * @throws IllegalArgumentException If the number of rows in the model returned by {@link #getRowCount()} is -1
   * (undefined).
   */
  public int getFirstRowIndexOfLastPage() {
    if (isRowsUnlimited()) {
      return 0;
    } else if (!hasRowCount()) {
      throw new IllegalArgumentException(
          "Can't determine the last page, because the row count of the model is unknown.");
    } else {
      final int rows = getRows();
      final int rowCount = getRowCount();
      final int tail = rowCount % rows;
      return rowCount - (tail != 0 ? tail : rows);
    }
  }

  @Override
  public void processUpdates(final FacesContext context) {
    super.processUpdates(context);
    updateSheetState(context);
  }

  private void updateSheetState(final FacesContext facesContext) {
    final SheetState sheetState = getSheetState(facesContext);
    if (sheetState != null) {
      // ensure sortActionListener
//      getSortActionListener();
//      state.setSortedColumn(sortActionListener != null ? sortActionListener.getColumn() : -1);
//      state.setAscending(sortActionListener != null && sortActionListener.isAscending());
      final Map attributes = getAttributes();
      //noinspection unchecked
      final List<Integer> list = (List<Integer>) attributes.get(Attributes.SELECTED_LIST_STRING);
      sheetState.setSelectedRows(list != null ? list : Collections.<Integer>emptyList());
      sheetState.setColumnWidths((String) attributes.get(Attributes.WIDTH_LIST_STRING));
      sheetState.setScrollPosition((Integer[]) attributes.get(Attributes.SCROLL_POSITION));
      attributes.remove(Attributes.SELECTED_LIST_STRING);
      attributes.remove(Attributes.SCROLL_POSITION);
    }
  }


  @Override
  public Object saveState(final FacesContext context) {
    final Object[] saveState = new Object[2];
    saveState[0] = super.saveState(context);
    saveState[1] = state;
    return saveState;
  }

  @Override
  public void restoreState(final FacesContext context, final Object savedState) {
    final Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    state = (SheetState) values[1];
  }

  public List<AbstractUIColumn> getAllColumns() {
    ArrayList<AbstractUIColumn> result = new ArrayList<AbstractUIColumn>();
    findColumns(this, result, true);
    return result;
  }

  public List<AbstractUIColumn> getRenderedColumns() {
    ArrayList<AbstractUIColumn> result = new ArrayList<AbstractUIColumn>();
    findColumns(this, result, false);
    return result;
  }

  private void findColumns(final UIComponent component, final List<AbstractUIColumn> result, final boolean all) {
    for (final UIComponent child : component.getChildren()) {
      if (all || child.isRendered()) {
        if (child instanceof AbstractUIColumn) {
          result.add((AbstractUIColumn) child);
        } else if (child instanceof AbstractUIData) {
          // ignore nested sheets
        } else {
          findColumns(child, result, all);
        }
      }
    }
  }

/*  public MethodBinding getSortActionListener() {
    if (sortActionListener != null) {
      return sortActionListener;
    } else {
      return new Sorter();
    }
  }*/

  @Override
  public void queueEvent(final FacesEvent facesEvent) {
    final UIComponent parent = getParent();
    if (parent == null) {
      throw new IllegalStateException("Component is not a descendant of a UIViewRoot");
    }

    if (facesEvent.getComponent() == this
        && (facesEvent instanceof SheetStateChangeEvent
        || facesEvent instanceof PageActionEvent)) {
      facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
      parent.queueEvent(facesEvent);
    } else {
      final UIComponent source = facesEvent.getComponent();
      final UIComponent sourceParent = source.getParent();
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
  public void broadcast(final FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if (facesEvent instanceof SheetStateChangeEvent) {
      final MethodExpression listener = getStateChangeListenerExpression();
      listener.invoke(getFacesContext().getELContext(), new Object[]{facesEvent});
    } else if (facesEvent instanceof PageActionEvent) {
      if (facesEvent.getComponent() == this) {
        final MethodExpression listener = getStateChangeListenerExpression();
        if (listener != null) {
          listener.invoke(getFacesContext().getELContext(), new Object[]{facesEvent});
        }
        performPaging((PageActionEvent) facesEvent);
      }
    } else if (facesEvent instanceof SortActionEvent) {
      getSheetState(getFacesContext()).updateSortState((SortActionEvent) facesEvent);
      sort(getFacesContext(), (SortActionEvent) facesEvent);
    }
  }

  public void processEvent(ComponentSystemEvent event) {
    super.processEvent(event);
    if (event instanceof PreRenderComponentEvent) {
      sort(getFacesContext(), null);
    }
  }

  protected void sort(final FacesContext facesContext, final SortActionEvent eventParameter) {
    SortActionEvent event = eventParameter;
    final SheetState sheetState = getSheetState(getFacesContext());
    if (sheetState.isToBeSorted()) {
      final MethodExpression expression = getSortActionListenerExpression();
      if (expression != null) {
        try {
          if (event == null) {
            event =
                new SortActionEvent(this, (UIColumn) findComponent(getSheetState(facesContext).getSortedColumnId()));
          }
          expression.invoke(facesContext.getELContext(), new Object[]{event});
        } catch (Exception e) {
          LOG.warn("Sorting not possible!", e);
        }
      } else {
        new Sorter().perform(this);
      }
      sheetState.setToBeSorted(false);
    }
  }

  public void addStateChangeListener(final SheetStateChangeListener listener) {
    addFacesListener(listener);
  }

  public SheetStateChangeListener[] getStateChangeListeners() {
    return (SheetStateChangeListener[]) getFacesListeners(SheetStateChangeListener.class);
  }

  public void removeStateChangeListener(final SheetStateChangeListener listener) {
    removeFacesListener(listener);
  }

  public List<Integer> getWidthList() {
    return widthList;
  }

  public void setWidthList(final List<Integer> widthList) {
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
  public UIComponent findComponent(final String searchId) {
    return super.findComponent(stripRowIndex(searchId));
  }

  public String stripRowIndex(final String searchIdParameter) {
    String searchId = searchIdParameter;
    if (searchId.length() > 0 && Character.isDigit(searchId.charAt(0))) {
      for (int i = 1; i < searchId.length(); ++i) {
        final char c = searchId.charAt(i);
        if (c == UINamingContainer.getSeparatorChar(getFacesContext())) {
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

  public void performPaging(final PageActionEvent pageEvent) {

    int first;

    if (LOG.isDebugEnabled()) {
      LOG.debug("action = '" + pageEvent.getAction().name() + "'");
    }

    Integer[] scrollPosition = getScrollPosition();
    if (scrollPosition == null) {
      scrollPosition = new Integer[]{0, 0};
    }
    scrollPosition[1] = 0;
    switch (pageEvent.getAction()) {
      case FIRST:
        first = 0;
        break;
      case PREV:
        first = getFirst() - getRows();
        first = first < 0 ? 0 : first;
        scrollPosition[1] = Integer.MAX_VALUE;
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
        final int pageIndex = pageEvent.getValue() - 1;
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

    final ValueExpression expression = getValueExpression(Attributes.FIRST);
    if (expression != null) {
      expression.setValue(getFacesContext().getELContext(), first);
    } else {
      setFirst(first);
    }

    SheetState sheetState = getState();
    sheetState.setFirst(first);
    getAttributes().put(Attributes.SCROLL_POSITION, scrollPosition);
    sheetState.setScrollPosition(scrollPosition);
//      sheet.queueEvent(new SheetStateChangeEvent(sheet));
  }

  public List<LayoutComponent> getComponents() {
    if (layoutComponents != null) {
      return layoutComponents;
    }
    layoutComponents = new ArrayList<LayoutComponent>();
    for (final UIComponent column : getChildren()) {
      if (column instanceof AbstractUIColumnSelector) {
        layoutComponents.add(null); // XXX UIColumnSelector is currently not an instance of LayoutComponent
      } else if (column instanceof ColumnEvent) {
        // ignore
      } else if (column instanceof AbstractUIColumnNode) {
        layoutComponents.add((AbstractUIColumnNode) column);
      } else if (column instanceof UIColumn) {
        LayoutComponent layoutComponent = null;
        for (final UIComponent component : column.getChildren()) {
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

  public void onComponentPopulated(final FacesContext facesContext, final UIComponent parent) {
    if (getLayoutManager() instanceof AbstractUIGridLayout) {
      // ugly, but it seems that some old pages have this problem
      LOG.warn("Found a GridLayout as layout facet in sheet. Will be ignored! Please remove it."
          + " The id of the sheet is: '" + getClientId(facesContext) + "'");
      getFacets().remove(Facets.LAYOUT);
    }
    if (getLayoutManager() == null) {
      setLayoutManager(CreateComponentUtils.createAndInitLayout(
          facesContext, ComponentTypes.SHEET_LAYOUT, RendererTypes.SHEET_LAYOUT, parent));
    }
  }

  public LayoutManager getLayoutManager() {
    return (LayoutManager) getFacet(Facets.LAYOUT);
  }

  public void setLayoutManager(final LayoutManager layoutManager) {
    getFacets().put(Facets.LAYOUT, (AbstractUILayoutBase) layoutManager);
  }

  public boolean isLayoutChildren() {
    return isRendered();
  }

  public boolean isRendersRowContainer() {
    return true;
  }

  public abstract boolean isShowHeader();

  public Boolean getNeedVerticalScrollbar() {
    return needVerticalScrollbar;
  }

  public void setNeedVerticalScrollbar(final Boolean needVerticalScrollbar) {
    this.needVerticalScrollbar = needVerticalScrollbar;
  }

  @Override
  public ExpandedState getExpandedState() {
    return getState().getExpandedState();
  }

  @Override
  public SelectedState getSelectedState() {
    return getState().getSelectedState();
  }

  public Grid getHeaderGrid() {
    return headerGrid;
  }

  public void setHeaderGrid(final Grid headerGrid) {
    this.headerGrid = headerGrid;
  }
}
