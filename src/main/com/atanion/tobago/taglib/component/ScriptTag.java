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

// ----------------------------------------------------------------- attributes

  private String file;

  private String onload;

// ----------------------------------------------------------- business methods

  public int doEndTag() throws JspException {
    PageTag pageTag = PageTag.findPageTag(pageContext); // todo: find uiPage directly
    if (pageTag == null) {
      throw new JspException("Use of Script outside of Page not allowed");
    }

    UIPage page = (UIPage) pageTag.getComponentInstance();

    if (file != null) {
      page.getScriptFiles().add(file);
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

  public int doStartTag() throws JspException {
    return EVAL_BODY_BUFFERED;
  }

  public void release() {
    super.release();
    file = null;
    onload = null;
  }

// ------------------------------------------------------------ getter + setter

// ///////////////////////////////////////////// bean getter + setter

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getOnload() {
    return onload;
  }

  public void setOnload(String onload) {
    this.onload = onload;
  }
}

