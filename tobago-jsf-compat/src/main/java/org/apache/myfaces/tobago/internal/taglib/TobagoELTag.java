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

package org.apache.myfaces.tobago.internal.taglib;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.component.OnComponentPopulated;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;


public abstract class TobagoELTag extends UIComponentELTag {

  @Override
  public int doStartTag() throws JspException {
    int result = super.doStartTag();
    UIComponent component = getComponentInstance();
    if (component instanceof OnComponentCreated
        && component.getAttributes().get(OnComponentCreated.MARKER) == null) {
      component.getAttributes().put(OnComponentCreated.MARKER, Boolean.TRUE);
      ((OnComponentCreated) component).onComponentCreated(getFacesContext(), component.getParent());
    }
    return result;
  }

  @Override
  public int doEndTag() throws JspException {
    UIComponent component = getComponentInstance();
    int result = super.doEndTag();
    if (component instanceof OnComponentPopulated
        && component.getAttributes().get(OnComponentPopulated.MARKER) == null) {
      component.getAttributes().put(OnComponentPopulated.MARKER, Boolean.TRUE);
      ((OnComponentPopulated) component).onComponentPopulated(getFacesContext(), component.getParent());
    }
    return result;
  }

  public String[] splitList(String renderers) {
    return StringUtils.split(renderers, ", ");
  }

  /**
   *
   * @deprecated Not supported anymore
   */
  @Deprecated
  protected String getBodyContentStr() {
    String content = bodyContent.getString();
    bodyContent.clearBody();
    return content;
  }

  /**
   *
   * @deprecated Not supported anymore
   */
  @Deprecated
  protected boolean isBodyContentEmpty() {
    if (bodyContent != null) {
      String content = bodyContent.getString();
      String tmp = content.replace('\n', ' ');
      if (tmp.trim().length() > 0) { // if there are only whitespaces: drop bodyContent
        return false;
      }
    }
    return true;
  }
}
