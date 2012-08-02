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
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/*
 * $Id$
 */

/**
 * This tag add client side script to the rendered page.
 */
@Tag(name = "script", bodyContent = BodyContent.JSP)
//    @Tag(name="script", bodyContent=BodyContent.TAGDEPENDENT)
//    @BodyContentDescription(contentType="javascript")
public class ScriptTag extends BodyTagSupport {

  private static final long serialVersionUID = 3253751129824779272L;

  private String file;
  private String onload;
  private String onunload;
  private String onexit;
  private String onsubmit;

  @Override
  public int doEndTag() throws JspException {

    FacesContext facesContext = FacesContext.getCurrentInstance();
    UIPage page = ComponentUtil.findPage(facesContext);
    if (page == null) {
      throw new JspException("The ScriptTag cannot find UIPage. "
          + "Check you have defined the ScriptTag inside of the PageTag!");
    }

    if (file != null) {
      page.getScriptFiles().add(ComponentUtil.getValueFromEl(file));
    }
    if (onload != null) {
      page.getOnloadScripts().add(ComponentUtil.getValueFromEl(onload));
    }
    if (onunload != null) {
      page.getOnunloadScripts().add(ComponentUtil.getValueFromEl(onunload));
    }
    if (onexit != null) {
      page.getOnexitScripts().add(ComponentUtil.getValueFromEl(onexit));
    }
    if (onsubmit != null) {
      page.getOnsubmitScripts().add(ComponentUtil.getValueFromEl(onsubmit));
    }
    if (bodyContent != null) {
      String script = bodyContent.getString();
      bodyContent.clearBody();
      page.getScriptBlocks().add(ComponentUtil.getValueFromEl(script));
    }

    return EVAL_PAGE;
  }

  @Override
  public int doStartTag() throws JspException {
    return EVAL_BODY_BUFFERED;
  }

  @Override
  public void release() {
    super.release();
    file = null;
    onload = null;
    onunload = null;
    onexit = null;
    onsubmit = null;
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

  /**
   * A script function which is invoked during onUnload Handler on the client,
   * if the action is a normal submit inside of Tobago.
   */
  @TagAttribute
  public void setOnunload(String onunload) {
    this.onunload = onunload;
  }

  /**
   * A script function which is invoked during onUnload Handler on the client,
   * when the unload is invoked to a non Tobago page.
   * E.g. close-button, back-button, entering new url, etc.
   */
  @TagAttribute
  public void setOnexit(String onexit) {
    this.onexit = onexit;
  }

  /**
   * A script function which is invoked on client just before submitting the action.
   * This should be a single function call. If the result is typeof 'boolean' and false
   * the further processing is canceled and the page is not submitted.
   */
  @TagAttribute
  public void setOnsubmit(String onsubmit) {
    this.onsubmit = onsubmit;
  }
}

