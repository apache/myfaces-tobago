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

package org.apache.myfaces.tobago.component;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ESCAPE;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/*
* Created by IntelliJ IDEA.
* User: bommel
* Date: 09.02.2006
* Time: 23:53:37
*/
public class UIOutput extends javax.faces.component.UIOutput implements SupportsMarkup {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Output";
  private Boolean escape;
  private String[] markup;
  private String tip;
  private boolean createSpan = true;

  @Override
  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    escape = (Boolean) values[1];
    markup = (String[]) values[2];
    tip = (String) values[3];
    createSpan = (Boolean) values[4];
  }

  @Override
  public Object saveState(FacesContext context) {
    Object[] values = new Object[5];
    values[0] = super.saveState(context);
    values[1] = escape;
    values[2] = markup;
    values[3] = tip;
    values[4] = createSpan;
    return values;
  }

  public boolean isEscape() {
    if (escape != null) {
      return escape;
    }
    ValueBinding vb = getValueBinding(ATTR_ESCAPE);
    if (vb != null) {
      return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
    } else {
      return true;
    }
  }

  public void setEscape(boolean escape) {
    this.escape = escape;
  }

  public String[] getMarkup() {
    if (markup != null) {
      return markup;
    }
    return ComponentUtil.getMarkupBinding(getFacesContext(), this);
  }

  public void setMarkup(String[] markup) {
    this.markup = markup;
  }

  public String getTip() {
    if (tip != null) {
      return tip;
    }
    ValueBinding vb = getValueBinding(ATTR_TIP);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public boolean isCreateSpan() {
    return createSpan;
  }

  public void setCreateSpan(boolean createSpan) {
    this.createSpan = createSpan;
  }

}
