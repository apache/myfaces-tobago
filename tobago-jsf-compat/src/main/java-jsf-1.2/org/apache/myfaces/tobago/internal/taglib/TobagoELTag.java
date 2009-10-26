package org.apache.myfaces.tobago.internal.taglib;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.OnComponentCreated;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;


public abstract class TobagoELTag extends UIComponentELTag {

  private static final Log LOG = LogFactory.getLog(TobagoELTag.class);
 
  public int doEndTag() throws JspException {
    UIComponent component = getComponentInstance();
    if (component instanceof OnComponentCreated
        && component.getAttributes().get("org.apache.myfaces.tobago.CREATION_MARKER") == null) {
      component.getAttributes().put("org.apache.myfaces.tobago.CREATION_MARKER", Boolean.TRUE);
      ((OnComponentCreated) component).onComponentCreated(getFacesContext(), getComponentInstance());
    }
    return super.doEndTag();
  }

  public String[] splitList(String renderers) {
    return StringUtils.split(renderers, ", ");
  }

  protected String getBodyContentStr() {
    String content = bodyContent.getString();
    bodyContent.clearBody();
    return content;
  }

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
