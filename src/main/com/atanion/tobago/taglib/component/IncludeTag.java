/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Oct 31, 2002 at 1:24:42 PM.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.context.TobagoResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class IncludeTag extends TagSupport {
// ----------------------------------------------------------- class attributes

  private static final Log LOG = LogFactory.getLog(IncludeTag.class);

// ----------------------------------------------------------------- attributes

  private String value;

  private boolean i18n;

// ----------------------------------------------------------- business methods

  public int doStartTag() throws JspException {
    String pageName = null;
    try {
      if (UIComponentTag.isValueReference(value)) {
        ValueBinding valueBinding = FacesContext.getCurrentInstance()
            .getApplication().createValueBinding(value);
        pageName = (String)
            valueBinding.getValue(FacesContext.getCurrentInstance());
      } else {
        pageName = value;
      }

      if (i18n) {
        pageName = TobagoResource.getJsp(FacesContext.getCurrentInstance(), pageName);
      }

      if (LOG.isDebugEnabled()) {
        LOG.debug("include start pageName = '" + pageName + "'");
      }
      pageContext.include(pageName);
      if (LOG.isDebugEnabled()) {
        LOG.debug("include end   pageName = '" + pageName + "'");
      }
    } catch (Throwable e) {
      LOG.error("pageName = '" + pageName + "' " + e, e);
      throw new JspException(e);
    }
    return super.doStartTag();
  }

  public void release() {
    value = null;
    i18n = false;
    super.release();
  }

// ------------------------------------------------------------ getter + setter

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isI18n() {
    return i18n;
  }

  public void setI18n(boolean i18n) {
    this.i18n = i18n;
  }
}

