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
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.util.FacesVersion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public abstract class TobagoTag extends UIComponentTag {

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
  protected void encodeBegin() throws IOException {
    // in jsf 1.1 the method component.encodeBegin is called in doEndTag ensure a LayoutManager for UIPage
    if (getComponentInstance() instanceof AbstractUIPage && !FacesVersion.supports12()) {
      onComponentPopulated(getComponentInstance());
    }
    super.encodeBegin();
  }

  @Override
  public int doEndTag() throws JspException {
    UIComponent component = getComponentInstance();
    int result = super.doEndTag();
    // in jsf 1.1 the method component.encodeBegin is called in doEndTag ensure a LayoutManager for UIPage
    if (!(component instanceof AbstractUIPage && !FacesVersion.supports12())) {
       onComponentPopulated(component);
    }
    return result;
  }

  private void onComponentPopulated(UIComponent component) {
    if (component instanceof OnComponentPopulated
        && component.getAttributes().get(OnComponentPopulated.MARKER) == null) {
      component.getAttributes().put(OnComponentPopulated.MARKER, Boolean.TRUE);
      FacesContext facesContext = FacesContext.getCurrentInstance();
      ((OnComponentPopulated) component).onComponentPopulated(facesContext, component.getParent());
    }
  }

  public String[] splitList(String renderers) {
    return StringUtils.split(renderers, ", ");
  }
}
