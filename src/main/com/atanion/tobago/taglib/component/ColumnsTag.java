/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Oct 31, 2002 at 1:24:42 PM.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.component.UIData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIColumn;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;
import java.util.Iterator;

public class ColumnsTag extends TagSupport {
// ----------------------------------------------------------- class attributes

  private static final Log LOG = LogFactory.getLog(ColumnsTag.class);

// ----------------------------------------------------------------- attributes

  private String value;


// ----------------------------------------------------------- business methods

  public int doStartTag() throws JspException {
    try {
      if (UIComponentTag.isValueReference(value)) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding valueBinding = facesContext
            .getApplication().createValueBinding(value);
        List columns = (List) valueBinding.getValue(facesContext);
        UIData sheet = (UIData) ((UIComponentTag)getParent()).getComponentInstance();
        for (Iterator i = columns.iterator(); i.hasNext();) {
          sheet.getChildren().add((UIColumn)i.next());
        }

      } else {
        throw new IllegalArgumentException("Value for ColumnsTag must be ValueReference!");
      }
    } catch (Throwable e) {
      LOG.error("value = '" + value + "' " + e, e);
      throw new JspException(e);
    }
    return super.doStartTag();
  }

  public void release() {
    value = null;
    super.release();
  }

// ------------------------------------------------------------ getter + setter

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}

