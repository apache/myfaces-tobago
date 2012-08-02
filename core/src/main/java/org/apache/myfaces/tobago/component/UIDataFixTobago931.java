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

package org.apache.myfaces.tobago.component;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.jstl.sql.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This component is an alternative to its parent.
 * It was written as an workaround for problems with Sun RI 1.1_02.
 * See bug TOBAGO-931 in the bug tracker.
 * To use it, define it in you faces-config.xml.
 */
public class UIDataFixTobago931 extends org.apache.myfaces.tobago.component.UIData {

  private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

  private int rowIndex = -1;
  private final Map dataModelMap = new HashMap();

  // Holds for each row the states of the child components of this UIData.
  // Note that only "partial" component state is saved: the component fields
  // that are expected to vary between rows.
  private final Map rowStates = new HashMap();
  private Object initialDescendantComponentState = null;
  private boolean isValidChilds = true;

  public String getClientId(FacesContext facesContext) {
    String clientId = super.getClientId(facesContext);
    if (getRowIndex() >= 0) {
      return (clientId + NamingContainer.SEPARATOR_CHAR + getRowIndex());
    } else {
      return clientId;
    }
  }

  public void processValidators(FacesContext context) {
    super.processValidators(context);
    // check if an validation error forces the render response for our data
    if (context.getRenderResponse()) {
      isValidChilds = false;
    }
  }

  public void processUpdates(FacesContext context) {
    super.processUpdates(context);
    if (context.getRenderResponse()) {
      isValidChilds = false;
    }
  }

  public void setValue(Object value) {
    super.setValue(value);
    dataModelMap.clear();
    rowStates.clear();
    isValidChilds = true;
  }

  public void setValueBinding(String name, ValueBinding binding) {
    if (name == null) {
      throw new NullPointerException("name");
    } else if (name.equals("value")) {
      dataModelMap.clear();
    } else if (name.equals("var") || name.equals("rowIndex")) {
      throw new IllegalArgumentException("You can never set the 'rowIndex' or the 'var' attribute as a value-binding. "
          + "Set the property directly instead. Name " + name);
    }
    super.setValueBinding(name, binding);
  }

  /**
   * Perform necessary actions when rendering of this component starts,
   * before delegating to the inherited implementation which calls the
   * associated renderer's encodeBegin method.
   */
  public void encodeBegin(FacesContext context) throws IOException {
    initialDescendantComponentState = null;
    if (isValidChilds && !hasErrorMessages(context)) {
      // Clear the data model so that when rendering code calls
      // getDataModel a fresh model is fetched from the backing
      // bean via the value-binding.
      dataModelMap.clear();

      // When the data model is cleared it is also necessary to
      // clear the saved row state, as there is an implicit 1:1
      // relation between objects in the rowStates and the
      // corresponding DataModel element.
      rowStates.clear();
    }
    super.encodeBegin(context);
  }

  private boolean hasErrorMessages(FacesContext context) {
    for (Iterator iter = context.getMessages(); iter.hasNext();) {
      FacesMessage message = (FacesMessage) iter.next();
      if (FacesMessage.SEVERITY_ERROR.compareTo(message.getSeverity()) <= 0) {
        return true;
      }
    }
    return false;
  }

  public boolean isRowAvailable() {
    return getDataModel().isRowAvailable();
  }

  public int getRowCount() {
    return getDataModel().getRowCount();
  }

  public Object getRowData() {
    return getDataModel().getRowData();
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public void setRowIndex(int rowIndex) {
    if (rowIndex < -1) {
      throw new IllegalArgumentException("rowIndex is less than -1");
    }

    if (this.rowIndex == rowIndex) {
      return;
    }

    FacesContext facesContext = getFacesContext();

    if (this.rowIndex == -1) {
      if (initialDescendantComponentState == null) {
        // Create a template that can be used to initialise any row
        // that we haven't visited before, ie a "saved state" that can
        // be pushed to the "restoreState" method of all the child
        // components to set them up to represent a clean row.
        initialDescendantComponentState = saveDescendantComponentStates(getChildren().iterator(), false);
      }
    } else {
      // We are currently positioned on some row, and are about to
      // move off it, so save the (partial) state of the components
      // representing the current row. Later if this row is revisited
      // then we can restore this state.
      rowStates.put(getClientId(facesContext), saveDescendantComponentStates(getChildren().iterator(), false));
    }

    this.rowIndex = rowIndex;

    DataModel dataModel = getDataModel();
    dataModel.setRowIndex(rowIndex);

    String var = getVar();
    if (rowIndex == -1) {
      if (var != null) {
        facesContext.getExternalContext().getRequestMap().remove(var);
      }
    } else {
      if (var != null) {
        if (isRowAvailable()) {
          Object rowData = dataModel.getRowData();
          facesContext.getExternalContext().getRequestMap().put(var, rowData);
        } else {
          facesContext.getExternalContext().getRequestMap().remove(var);
        }
      }
    }

    if (this.rowIndex == -1) {
      // reset components to initial state
      restoreDescendantComponentStates(getChildren().iterator(), initialDescendantComponentState, false);
    } else {
      Object rowState = rowStates.get(getClientId(facesContext));
      if (rowState == null) {
        // We haven't been positioned on this row before, so just
        // configure the child components of this component with
        // the standard "initial" state
        restoreDescendantComponentStates(getChildren().iterator(), initialDescendantComponentState, false);
      } else {
        // We have been positioned on this row before, so configure
        // the child components of this component with the (partial)
        // state that was previously saved. Fields not in the
        // partial saved state are left with their original values.
        restoreDescendantComponentStates(getChildren().iterator(), rowState, false);
      }
    }
  }

  /**
   * Overwrite the state of the child components of this component
   * with data previously saved by method saveDescendantComponentStates.
   * <p/>
   * The saved state info only covers those fields that are expected to
   * vary between rows of a table. Other fields are not modified.
   */
  private void restoreDescendantComponentStates(Iterator childIterator, Object state, boolean restoreChildFacets) {
    Iterator descendantStateIterator = null;
    while (childIterator.hasNext()) {
      if (descendantStateIterator == null && state != null) {
        descendantStateIterator = ((Collection) state).iterator();
      }
      UIComponent component = (UIComponent) childIterator.next();

      // reset the client id (see spec 3.1.6)
      component.setId(component.getId());
      if (!component.isTransient()) {
        Object childState = null;
        Object descendantState = null;
        if (descendantStateIterator != null && descendantStateIterator.hasNext()) {
          Object[] object = (Object[]) descendantStateIterator.next();
          childState = object[0];
          descendantState = object[1];
        }
        if (component instanceof EditableValueHolder) {
          ((EditableValueHolderState) childState).restoreState((EditableValueHolder) component);
        }
        Iterator childsIterator;
        if (restoreChildFacets) {
          childsIterator = component.getFacetsAndChildren();
        } else {
          childsIterator = component.getChildren().iterator();
        }
        restoreDescendantComponentStates(childsIterator, descendantState, true);
      }
    }
  }

  /**
   * Walk the tree of child components of this UIData, saving the parts of
   * their state that can vary between rows.
   * <p/>
   * This is very similar to the process that occurs for normal components
   * when the view is serialized. Transient components are skipped (no
   * state is saved for them).
   * <p/>
   * If there are no children then null is returned. If there are one or
   * more children, and all children are transient then an empty collection
   * is returned; this will happen whenever a table contains only read-only
   * components.
   * <p/>
   * Otherwise a collection is returned which contains an object for every
   * non-transient child component; that object may itself contain a collection
   * of the state of that child's child components.
   */
  private Object saveDescendantComponentStates(Iterator childIterator, boolean saveChildFacets) {
    Collection childStates = null;
    while (childIterator.hasNext()) {
      if (childStates == null) {
        childStates = new ArrayList();
      }
      UIComponent child = (UIComponent) childIterator.next();
      if (!child.isTransient()) {
        // Add an entry to the collection, being an array of two
        // elements. The first element is the state of the children
        // of this component; the second is the state of the current
        // child itself.

        Iterator childsIterator;
        if (saveChildFacets) {
          childsIterator = child.getFacetsAndChildren();
        } else {
          childsIterator = child.getChildren().iterator();
        }
        Object descendantState = saveDescendantComponentStates(childsIterator, true);
        Object state = null;
        if (child instanceof EditableValueHolder) {
          state = new EditableValueHolderState((EditableValueHolder) child);
        }
        childStates.add(new Object[]{state, descendantState});
      }
    }
    return childStates;
  }

  private class EditableValueHolderState {
    private final Object value;
    private final boolean localValueSet;
    private final boolean valid;
    private final Object submittedValue;

    public EditableValueHolderState(EditableValueHolder evh) {
      value = evh.getLocalValue();
      localValueSet = evh.isLocalValueSet();
      valid = evh.isValid();
      submittedValue = evh.getSubmittedValue();
    }

    public void restoreState(EditableValueHolder evh) {
      evh.setValue(value);
      evh.setLocalValueSet(localValueSet);
      evh.setValid(valid);
      evh.setSubmittedValue(submittedValue);
    }
  }

  /**
   * Return the datamodel for this table, potentially fetching the data from
   * a backing bean via a value-binding if this is the first time this method
   * has been called.
   * <p/>
   * This is complicated by the fact that this table may be nested within
   * another table. In this case a different datamodel should be fetched
   * for each row. When nested within a parent table, the parent reference
   * won't change but parent.getClientId() will, as the suffix changes
   * depending upon the current row index. A map object on this component
   * is therefore used to cache the datamodel for each row of the table.
   * In the normal case where this table is not nested inside a component
   * that changes its id (like a table does) then this map only ever has
   * one entry.
   */
  private DataModel getDataModel() {
    DataModel dataModel = null;
    String clientID = "";

    UIComponent parent = getParent();
    if (parent != null) {
      clientID = parent.getClientId(getFacesContext());
    }
    dataModel = (DataModel) dataModelMap.get(clientID);
    if (dataModel == null) {
      dataModel = createDataModel();
      dataModelMap.put(clientID, dataModel);
    }
    return dataModel;
  }

  /**
   * Evaluate this object's value property and convert the result into a
   * DataModel. Normally this object's value property will be a value-binding
   * which will cause the value to be fetched from some backing bean.
   * <p/>
   * The result of fetching the value may be a DataModel object, in which
   * case that object is returned directly. If the value is of type
   * List, Array, ResultSet, Result, other object or null then an appropriate
   * wrapper is created and returned.
   * <p/>
   * Null is never returned by this method.
   */
  private DataModel createDataModel() {
    Object value = getValue();
    if (value == null) {
      return EMPTY_DATA_MODEL;
    } else if (value instanceof DataModel) {
      return (DataModel) value;
    } else if (value instanceof List) {
      return new ListDataModel((List) value);
    } else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass())) {
      return new ArrayDataModel((Object[]) value);
    } else if (value instanceof ResultSet) {
      return new ResultSetDataModel((ResultSet) value);
    } else if (value instanceof Result) {
      return new ResultDataModel((Result) value);
    } else {
      return new ScalarDataModel(value);
    }
  }

  private static final DataModel EMPTY_DATA_MODEL = new DataModel() {
    public boolean isRowAvailable() {
      return false;
    }

    public int getRowCount() {
      return 0;
    }

    public Object getRowData() {
      throw new IllegalArgumentException();
    }

    public int getRowIndex() {
      return -1;
    }

    public void setRowIndex(int i) {
      if (i < -1) {
        throw new IllegalArgumentException();
      }
    }

    public Object getWrappedData() {
      return null;
    }

    public void setWrappedData(Object obj) {
      if (obj == null) {
        return; //Clearing is allowed
      }
      throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
    }
  };
}
