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

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(IncludeTag.class);

// ///////////////////////////////////////////// attribute

  protected String value;

  protected boolean i18n;
  
// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

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
      LOG.error("pageName = '" + pageName + "'", e);
      throw new JspException(e);
    }
    return super.doStartTag();
  }

  public void release() {
    value = null;
    i18n = false;
    super.release();
  }

// ///////////////////////////////////////////// bean getter + setter

  public void setValue(String value) {
    this.value = value;
  }

  public void setI18n(boolean i18n) {
    this.i18n = i18n;
  }
}
