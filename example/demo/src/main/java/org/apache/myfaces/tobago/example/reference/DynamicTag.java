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

package org.apache.myfaces.tobago.example.reference;

import org.apache.myfaces.tobago.util.VariableResolverUtil;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.faces.context.FacesContext;

public class DynamicTag extends TagSupport {

  private String controllerName;
  private TagSupport tag;

  public int doStartTag() throws JspException {
    // fixme: session?
    Controller controller =
        (Controller) VariableResolverUtil.resolveVariable(FacesContext.getCurrentInstance(), controllerName);
//    Controller controller = (Controller) pageContext.getSession().getAttribute(controllerName);
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
