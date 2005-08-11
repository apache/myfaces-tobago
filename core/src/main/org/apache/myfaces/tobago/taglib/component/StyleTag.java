/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 12, 2002 2:55:37 PM
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Add a style tag.
 * Collected bodyContent is rendered as content into a style tag.
 *
 */
@Tag(name="style", bodyContent=BodyContent.TAGDEPENDENT)
@BodyContentDescription(contentType="css")
public class StyleTag extends BodyTagSupport implements HasId {
  
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
      page.getStyleFiles().add(ComponentUtil.getValueFromEl(style));
    }

    if (bodyContent != null) {
      String classes = bodyContent.getString();
      bodyContent.clearBody();
      page.getStyleBlocks().add(ComponentUtil.getValueFromEl(classes));
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


  /**
   *
   * Name of the stylsheet file to add to page.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(expression=DynamicExpression.NONE)
  public void setStyle(String style) {
    this.style = style;
  }
}

