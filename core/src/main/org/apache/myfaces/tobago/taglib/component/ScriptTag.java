/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * This tag add client side script to the rendered page.
 */
@Tag(name = "script", bodyContent = BodyContent.JSP)
//    @Tag(name="script", bodyContent=BodyContent.TAGDEPENDENT)
//    @BodyContentDescription(contentType="javascript")
public class ScriptTag extends BodyTagSupport {

  private String file;
  private String onload;

  public int doEndTag() throws JspException {

    PageTag pageTag = (PageTag) findAncestorWithClass(this, PageTag.class);
    if (pageTag == null) {
      throw new JspException("Use of Script outside of Page not allowed");
    }

    UIPage page = (UIPage) pageTag.getComponentInstance();

    if (file != null) {
      page.getScriptFiles().add(ComponentUtil.getValueFromEl(file));
    }
    if (onload != null) {
      page.getOnloadScripts().add(ComponentUtil.getValueFromEl(onload));
    }
    if (bodyContent != null) {
      String script = bodyContent.getString();
      bodyContent.clearBody();
      page.getScriptBlocks().add(ComponentUtil.getValueFromEl(script));
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

  public String getFile() {
    return file;
  }


  /**
   * Absolute url to script file or script name to lookup in tobago resource path
   */
  @TagAttribute
  public void setFile(String file) {
    this.file = file;
  }

  public String getOnload() {
    return onload;
  }


  /**
   * A script function which is invoked during onLoad Handler on the client.
   */
  @TagAttribute
  public void setOnload(String onload) {
    this.onload = onload;
  }
}

