package org.apache.myfaces.tobago.taglib.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/*
 * $Id$
 */

/**
 * This tag add client side script to the rendered page.
 */
//@Tag(name = "script", bodyContent = BodyContent.JSP)
//    @Tag(name="script", bodyContent=BodyContent.TAGDEPENDENT)
//    @BodyContentDescription(contentType="javascript")
//@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.ScriptTag")
public abstract class ScriptTag extends BodyTagSupport {

  private static final long serialVersionUID = 3253751129824779272L;

  public abstract boolean isFileSet();

  @TagAttribute(name = "file")
  public abstract String getFileValue();

  public abstract boolean isOnloadSet();
  @TagAttribute(name = "onload")
  public abstract String getOnloadValue();

  public abstract boolean isOnunloadSet();
  @TagAttribute(name = "onunload")
  public abstract String getOnunloadValue();

  public abstract boolean isOnexitSet();
  @TagAttribute(name = "onexit")
  public abstract String getOnexitValue();

  public abstract boolean isOnsubmitSet();

  @TagAttribute(name = "onsubmit")
  public abstract String getOnsubmitValue();

  @Override
  public int doEndTag() throws JspException {

    FacesContext facesContext = FacesContext.getCurrentInstance();
    AbstractUIPage page = ComponentUtils.findPage(facesContext);
    if (page == null) {
      throw new JspException("The ScriptTag cannot find UIPage. "
          + "Check you have defined the ScriptTag inside of the PageTag!");
    }

    if (isFileSet()) {
    //  page.getScriptFiles().add(getFileValue());
    }
    if (isOnloadSet()) {
    //  page.getOnloadScripts().add(getOnloadValue());
    }
    if (isOnunloadSet())  {
    //  page.getOnunloadScripts().add(TagUtils.getValueFromEl(getOnunloadValue()));
    }
    if (isOnexitSet()) {
    //  page.getOnexitScripts().add(TagUtils.getValueFromEl(getOnexitValue()));
    }
    if (isOnsubmitSet()) {
    //  page.getOnsubmitScripts().add(getOnsubmitValue());
    }
    if (bodyContent != null) {
      String script = bodyContent.getString();
      bodyContent.clearBody();
     // page.getScriptBlocks().add(TagUtils.getValueFromEl(script));
    }

    return EVAL_PAGE;
  }

  @Override
  public int doStartTag() throws JspException {
    return EVAL_BODY_BUFFERED;
  }

}

