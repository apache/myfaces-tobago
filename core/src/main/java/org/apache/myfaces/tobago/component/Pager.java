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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.event.PageActionEvent;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FIRST;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ValueBinding;

public class Pager extends MethodBinding {

  private static final Log LOG = LogFactory.getLog(Pager.class);

  public Class getType(FacesContext facescontext)
      throws MethodNotFoundException {
    return String.class;
  }

  public Object invoke(FacesContext facesContext, Object[] aobj)
      throws EvaluationException {
    if (aobj[0] instanceof PageActionEvent) {
      PageActionEvent pageEvent = (PageActionEvent) aobj[0];

      UIData sheet = pageEvent.getSheet();
      int first = -1;

      if (LOG.isDebugEnabled()) {
        LOG.debug("action = '" + pageEvent.getAction().name() + "'");
      }

      int start;
      switch (pageEvent.getAction()) {
        case FIRST:
          first = 0;
          break;
        case PREV:
          start = sheet.getFirst() - sheet.getRows();
          first = start < 0 ? 0 : start;
          break;
        case NEXT:
          if (sheet.hasRowCount()) {
            start = sheet.getFirst() + sheet.getRows();
            first = start > sheet.getRowCount() ? sheet.getLastPageIndex() : start;
          } else {
            if (sheet.isAtEnd()) {
              first = sheet.getFirst();
            } else {
              first = sheet.getFirst() + sheet.getRows();
            }
          }
          break;
        case LAST:
          first = sheet.getLastPageIndex();
          break;
        case TO_ROW:
          start = pageEvent.getValue() - 1;
          if (start > sheet.getLastPageIndex()) {
            start = sheet.getLastPageIndex();
          } else if (start < 0) {
            start = 0;
          }
          first = start;
          break;
        case TO_PAGE:
          start = pageEvent.getValue() - 1;
          if (LOG.isDebugEnabled()) {
            LOG.debug("start = " + start + "  sheet.getRows() = "
                + sheet.getRows() + " => start = " + (start * sheet.getRows()));
          }
          start = start * sheet.getRows();
          if (start > sheet.getLastPageIndex()) {
            start = sheet.getLastPageIndex();
          } else if (start < 0) {
            start = 0;
          }
          first = start;
          break;
        default:
          // can't happen
      }
      ValueBinding valueBinding = sheet.getValueBinding(ATTR_FIRST);
      if (valueBinding != null) {
        valueBinding.setValue(facesContext, first);
      } else {
        sheet.setFirst(first);
      }

      sheet.getSheetState(facesContext).setFirst(first);
//      sheet.queueEvent(new SheetStateChangeEvent(sheet));
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("aobj[0] instanceof '" + aobj[0] + "'");
      }
    }

    return null;
  }
}

