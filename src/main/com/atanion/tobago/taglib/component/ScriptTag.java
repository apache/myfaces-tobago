/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Oct 1, 2002 1:28:42 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIPage;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class ScriptTag extends BodyTagSupport {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String file;

  private String onload;

  private boolean i18n;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int doStartTag() throws JspException {
    return EVAL_BODY_BUFFERED;
  }

  public int doEndTag() throws JspException {

    PageTag pageTag = PageTag.findPageTag(pageContext); // todo: find uiPage directly
    if (pageTag == null) {
      throw new JspException("Use of Script outside of Page not allowed");
    }

    UIPage page = (UIPage) pageTag.getComponentInstance();

    if (file != null) {
      page.getScriptFiles().add(file, i18n);
    }
    if (onload != null) {
      page.getOnloadScripts().add(onload);
    }
    if (bodyContent != null) {
      String script = bodyContent.getString();
      bodyContent.clearBody();
      page.getScriptBlocks().add(script);
    }

    return EVAL_PAGE;
  }

// ///////////////////////////////////////////// bean getter + setter

  public void setFile(String file) {
    this.file = file;
  }

  public void setOnload(String onload) {
    this.onload = onload;
  }

  public void setI18n(boolean i18n) {
    this.i18n = i18n;
  }
}

