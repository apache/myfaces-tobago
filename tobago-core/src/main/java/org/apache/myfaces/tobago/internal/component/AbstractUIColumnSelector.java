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

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.ColumnSelectorTagDeclaration}
 */
public abstract class AbstractUIColumnSelector extends AbstractUIColumnBase implements ClientBehaviorHolder {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private transient AbstractUISheet abstractUISheet;

/* TBD
  @Override
  public Measure getWidth() {
    return null;
  }
*/

  @Override
  public void processUpdates(final FacesContext facesContext) {
    final AbstractUISheet sheet = getSheet();
    final SheetState sheetState = sheet.getSheetState(facesContext);
    processSelectedRows(sheet, sheetState);
  }

  public static void processSelectedRows(final AbstractUISheet sheet, final SheetState sheetState) {
    if (sheetState != null) {
      final List<Integer> list = (List<Integer>) ComponentUtils.getAttribute(sheet, Attributes.selectedListString);
      sheetState.setSelectedRows(list != null ? list : Collections.emptyList());
      ComponentUtils.removeAttribute(sheet, Attributes.selectedListString);
    }
  }

  public abstract boolean isDisabled();

  public abstract Selectable getSelectable();

  public AbstractUISheet getSheet() {
    if (abstractUISheet == null) {
      final UIComponent parent = this.getParent();
      if (parent instanceof AbstractUISheet) {
        abstractUISheet = (AbstractUISheet) parent;
      } else {
        LOG.warn("Component of type '{}' must be placed inside a sheet.", RendererTypes.COLUMN_SELECTOR);
      }
    }
    return abstractUISheet;
  }
}
