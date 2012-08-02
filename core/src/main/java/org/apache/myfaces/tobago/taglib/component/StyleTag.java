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

package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.taglib.decl.HasId;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Add a style tag.
 * Collected bodyContent is rendered as content into a style tag.
 */
@Tag(name = "style", bodyContent = BodyContent.TAGDEPENDENT)
@BodyContentDescription(contentType = "css")
public class StyleTag extends BodyTagSupport implements HasId {

  private static final long serialVersionUID = -2201525304632479403L;

  private String style;

  public int doEndTag() throws JspException {

    FacesContext facesContext = FacesContext.getCurrentInstance();
    UIPage page = ComponentUtil.findPage(facesContext);
    if (page == null) {
      throw new JspException("The StyleTag cannot find the UIPage. "
          + "Check you have defined the StyleTag inside of the PageTag!");
    }

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

  public String getStyle() {
    return style;
  }

  /**
   * Name of the stylsheet file to add to page.
   */
  @TagAttribute
  public void setStyle(String style) {
    this.style = style;
  }
}

