/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 27.04.2004 at 18:33:04.
  * $Id$
  */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.model.SheetState;
import com.atanion.tobago.renderkit.html.scarborough.standard.tag.SheetRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class UIData extends javax.faces.component.UIData {

  private static final Log LOG = LogFactory.getLog(UIData.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.Data";


  public void processUpdates(FacesContext context) {
    super.processUpdates(context);
    updateSheetState(context);
  }

  public void updateSheetState(FacesContext facesContext) {
    SheetState state = getSheetState(facesContext);
    if (state != null) {
      SheetRenderer.Sorter sorter =  (SheetRenderer.Sorter)
          getAttributes().get(TobagoConstants.ATTR_SHEET_SORTER);
      state.setFirst(getFirst());
      state.setSortedColumn(sorter != null ? sorter.getColumn() : -1);
      state.setAscending(sorter != null && sorter.isAscending());
      state.setSelected((String)
          getAttributes().get(TobagoConstants.ATTR_SELECTED_LIST_STRING));
      state.setColumnWidths((String)
          getAttributes().get(TobagoConstants.ATTR_WIDTH_LIST_STRING));
    }
    else {
      LOG.info("stateBinding == null");
    }
  }

  public SheetState getSheetState(FacesContext facesContext) {
    ValueBinding stateBinding
        = getValueBinding(TobagoConstants.ATTR_STATE_BINDING);
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

}