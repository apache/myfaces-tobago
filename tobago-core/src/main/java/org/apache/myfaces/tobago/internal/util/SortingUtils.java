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

package org.apache.myfaces.tobago.internal.util;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.MessageUtils;
import org.apache.myfaces.tobago.util.ValueExpressionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIColumn;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {

  private static final Logger LOG = LoggerFactory.getLogger(SortingUtils.class);

  private SortingUtils() {
  }

  public static void sort(final AbstractUISheet sheet, final Comparator comparator) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    Object data = sheet.getValue();
    if (data instanceof DataModel) {
      data = ((DataModel) data).getWrappedData();
    }
    final SheetState sheetState = sheet.getSheetState(facesContext);

    final String sortedColumnId = sheetState.getSortedColumnId();
    LOG.debug("sorterId = '{}'", sortedColumnId);

    boolean success = false;
    if (sortedColumnId != null) {
      final UIColumn column = (UIColumn) sheet.findComponent(sortedColumnId);
      if (column != null) {
        success = sort(facesContext, sheet, data, column, sheetState, comparator);
      } else {
        LOG.error("No column to sort found, sorterId = '{}'!", sortedColumnId);
        addNotSortableMessage(facesContext, null);
      }
    } else {
      LOG.debug("No sorterId!");
    }

    if (!success) {
      sheetState.resetSortState();
    }
  }

  private static boolean sort(
      final FacesContext facesContext, final AbstractUISheet sheet, final Object data, final UIColumn column,
      final SheetState sheetState, Comparator comparator) {

    final Comparator actualComparator;

    if (data instanceof List || data instanceof Object[]) {
      try {
        final UIComponent child = getFirstSortableChild(column.getChildren());
        if (child != null) {
          final boolean descending = !sheetState.isAscending();
          final String attribute = (child instanceof AbstractUICommand ? Attributes.label : Attributes.value).getName();
          final ValueExpression expression = child.getValueExpression(attribute);
          if (expression != null) {
            final String var = sheet.getVar();
            if (var == null) {
                LOG.error("No sorting performed. Property var of sheet is not set!");
                addNotSortableMessage(facesContext, column);
                return false;
            }
            actualComparator = new ValueExpressionComparator(facesContext, var, expression, descending, comparator);
          } else {
            LOG.error("No sorting performed, because no expression found for "
                    + "attribute '{}' in component '{}' with id='{}'! You may check the type of the component!",
                attribute, child.getClass().getName(), child.getClientId());
            addNotSortableMessage(facesContext, column);
            return false;
          }
        } else {
          LOG.error("No sorting performed. Value is not instanceof List or Object[]!");
          addNotSortableMessage(facesContext, column);
          return false;
        }
      } catch (final Exception e) {
        LOG.error("Error while extracting sortMethod :" + e.getMessage(), e);
        addNotSortableMessage(facesContext, column);
        return false;
      }

      // memorize selected rows
      List<Object> selectedDataRows = null;
      if (sheetState.getSelectedRows().size() > 0) {
        selectedDataRows = new ArrayList<>(sheetState.getSelectedRows().size());
        Object dataRow;
        for (final Integer index : sheetState.getSelectedRows()) {
          if (data instanceof List) {
            dataRow = ((List) data).get(index);
          } else {
            dataRow = ((Object[]) data)[index];
          }
          selectedDataRows.add(dataRow);
        }
      }

      // do sorting
      if (data instanceof List) {
        Collections.sort((List) data, actualComparator);
      } else { // value is instanceof Object[]
        Arrays.sort((Object[]) data, actualComparator);
      }

      // restore selected rows
      if (selectedDataRows != null) {
        sheetState.getSelectedRows().clear();
        for (final Object dataRow : selectedDataRows) {
          int index = -1;
          if (data instanceof List) {
            for (int i = 0; i < ((List) data).size() && index < 0; i++) {
              if (dataRow == ((List) data).get(i)) {
                index = i;
              }
            }
          } else {
            for (int i = 0; i < ((Object[]) data).length && index < 0; i++) {
              if (dataRow == ((Object[]) data)[i]) {
                index = i;
              }
            }
          }
          if (index >= 0) {
            sheetState.getSelectedRows().add(index);
          }
        }
      }

    } else {  // DataModel?, ResultSet, Result or Object
      LOG.warn("Sorting not supported for type '{}'.", data != null ? data.getClass().toString() : "null");
      addNotSortableMessage(facesContext, column);
      return false;
    }
    return true;
  }

  private static void addNotSortableMessage(final FacesContext facesContext, final UIColumn column) {
    MessageUtils.addMessage(facesContext, column, FacesMessage.SEVERITY_WARN,
        AbstractUISheet.NOT_SORTABLE_MESSAGE_ID, new Object[]{MessageUtils.getLabel(facesContext, column)});
  }

  private static UIComponent getFirstSortableChild(final List<UIComponent> children) {
    UIComponent result = null;

    for (UIComponent child : children) {
      result = child;
      if (child instanceof UISelectMany
          || child instanceof UISelectOne
          || child instanceof UISelectBoolean
          || child instanceof AbstractUICommand && child.getChildren().isEmpty()
          || child instanceof UIInput && RendererTypes.HIDDEN.equals(child.getRendererType())) {
        continue;
        // look for a better component if any
      }
      if (child instanceof UIOutput) {
        break;
      }
      if (child instanceof UICommand
          || child instanceof javax.faces.component.UIPanel) {
        child = getFirstSortableChild(child.getChildren());
        if (child instanceof UIOutput) {
          break;
        }
      }
    }
    return result;
  }
}
