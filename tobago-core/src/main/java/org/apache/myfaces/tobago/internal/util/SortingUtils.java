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
import org.apache.myfaces.tobago.model.SortedColumn;
import org.apache.myfaces.tobago.util.MessageUtils;
import org.apache.myfaces.tobago.util.ValueExpressionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ValueExpression;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIColumn;
import jakarta.faces.component.UICommand;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.component.UIOutput;
import jakarta.faces.component.UISelectBoolean;
import jakarta.faces.component.UISelectMany;
import jakarta.faces.component.UISelectOne;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.DataModel;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private SortingUtils() {
  }

  public static void sort(final AbstractUISheet sheet, final Comparator comparator) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Object value = sheet.getValue();
    final Object data = value instanceof DataModel ? ((DataModel) value).getWrappedData() : value;
    final SheetState sheetState = sheet.getSheetState(facesContext);

    boolean success = false;
    if (sheetState.getToBeSortedLevel() > 0) {
      UIColumn column = null;
      try {
        for (int i = sheetState.getToBeSortedLevel() - 1; i >= 0; i--) {
          final SortedColumn sortedColumn = sheetState.getSortedColumnList().get(i);
          column = (UIColumn) sheet.findComponent(sortedColumn.getId());
          if (column != null) {
            success = sort(facesContext, sheet, data, column, sortedColumn.isAscending(), sheetState, comparator);
          } else {
            LOG.error("No column to sort found, sorterId = '{}'!", sortedColumn.getId());
            addNotSortableMessage(facesContext, null);
          }
        }
      } catch (Exception e) {
        LOG.error("Error while extracting sortMethod :" + e.getMessage(), e);
        addNotSortableMessage(facesContext, column);
      }
    } else {
      LOG.debug("Not to be sorted!");
    }

    if (!success) {
      sheetState.resetSortState();
    }
  }

  private static boolean sort(
      final FacesContext facesContext, final AbstractUISheet sheet, final Object data, final UIColumn column,
      final boolean ascending, final SheetState sheetState, Comparator comparator) {

    final Comparator actualComparator;

    if (data instanceof List || data instanceof Object[]) {
      try {
        final UIComponent child = getFirstSortableChild(column.getChildren());
        if (child != null) {
          final String attribute = (child instanceof AbstractUICommand ? Attributes.label : Attributes.value).getName();
          final ValueExpression expression = child.getValueExpression(attribute);
          if (expression != null) {
            final String var = sheet.getVar();
            if (var == null) {
              LOG.error("No sorting performed. Property var of sheet is not set!");
              addNotSortableMessage(facesContext, column);
              return false;
            }
            actualComparator = new ValueExpressionComparator(facesContext, var, expression, !ascending, comparator);
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
    if (column != null) {
      final String label = MessageUtils.getLabel(facesContext, column);
      facesContext.addMessage(column.getClientId(facesContext),
          MessageUtils.getMessage(
              facesContext, FacesMessage.SEVERITY_WARN, AbstractUISheet.NOT_SORTABLE_COL_MESSAGE_ID, label));
    } else {
      facesContext.addMessage(null,
          MessageUtils.getMessage(
              facesContext, FacesMessage.SEVERITY_WARN, AbstractUISheet.NOT_SORTABLE_MESSAGE_ID));
    }
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
          || child instanceof jakarta.faces.component.UIPanel) {
        child = getFirstSortableChild(child.getChildren());
        if (child instanceof UIOutput) {
          break;
        }
      }
    }
    return result;
  }
}
