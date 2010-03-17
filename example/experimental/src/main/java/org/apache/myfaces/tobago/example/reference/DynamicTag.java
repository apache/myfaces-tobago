package org.apache.myfaces.tobago.example.reference;

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

import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DynamicTag extends TagSupport {

  private String controllerName;
  private TagSupport tag;

  public int doStartTag() throws JspException {
    // fixme: session?
    DynamicController controller =
        (DynamicController) VariableResolverUtils.resolveVariable(FacesContext.getCurrentInstance(), controllerName);
    //DynamicController controller = (DynamicController) pageContext.getSession().getAttribute(controllerName);
    if (controller != null) {
      tag = controller.createTag();
      tag.setPageContext(pageContext);
      tag.setParent(getParent());
      tag.doStartTag();
    }
    return super.doStartTag();
  }

  public int doEndTag() throws JspException {
    if (tag != null) {
      tag.doEndTag();
    }
    return super.doEndTag();
  }

  public String getController() {
    return controllerName;
  }

  public void setController(String controller) {
    this.controllerName = controller;
  }
}
