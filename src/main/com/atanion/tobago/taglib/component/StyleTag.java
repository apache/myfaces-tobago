/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 12, 2002 2:55:37 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIPage;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class StyleTag extends BodyTagSupport {
  
// ----------------------------------------------------------------- attributes

  public String style;

// ----------------------------------------------------------- business methods

  public int doEndTag() throws JspException {
    PageTag pageTag = PageTag.findPageTag(pageContext);
    if (pageTag == null) {
      throw new JspException("Use of Script outside of Page not allowed");
    }

    UIPage page = (UIPage) pageTag.getComponentInstance();

    if (style != null) {
      page.getStyleFiles().add(style);
    }

    if (bodyContent != null) {
      String classes = bodyContent.getString();
      bodyContent.clearBody();
      page.getStyleBlocks().add(classes);
    }

    return EVAL_PAGE;
  }

  public int doStartTag() throws JspException {
    return EVAL_BODY_BUFFERED;
  }

  public void release() {
    super.release();
    style = null;
  }

// ------------------------------------------------------------ getter + setter

  public String getStyle() {
    return style;
  }

  public void setStyle(String style) {
    this.style = style;
  }
}

