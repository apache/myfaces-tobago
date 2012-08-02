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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TAB_INDEX;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import org.apache.myfaces.tobago.util.MessageFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;

/*
 * Date: May 31, 2005
 * Time: 7:47:11 PM
 */
public class UISelectOne extends javax.faces.component.UISelectOne implements SupportsMarkup {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.SelectOne";
  public static final String MESSAGE_VALUE_REQUIRED = "tobago.SelectOne.MESSAGE_VALUE_REQUIRED";

  private String[] markup;
  private Integer tabIndex;
  private Boolean readonly;

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    markup = (String[]) values[1];
    tabIndex = (Integer) values[2];
    readonly = (Boolean) values[3];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[4];
    values[0] = super.saveState(context);
    values[1] = markup;
    values[2] = tabIndex;
    values[3] = readonly;
    return values;
  }

  public void encodeBegin(FacesContext facesContext) throws IOException {
    // TODO change this should be renamed to DimensionUtils.prepare!!!
    UILayout.getLayout(this).layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
  }

  public void validate(FacesContext facesContext) {
    if (isRequired() && !isReadonly()) {

      Object submittedValue = getSubmittedValue();
      if (submittedValue == null || "".equals(submittedValue)) {
        FacesMessage facesMessage = MessageFactory.createFacesMessage(
            facesContext, MESSAGE_VALUE_REQUIRED, FacesMessage.SEVERITY_ERROR);
        facesContext.addMessage(getClientId(facesContext), facesMessage);
        setValid(false);
      }
    }
    super.validate(facesContext);
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

  public Integer getTabIndex() {
    if (tabIndex != null) {
      return tabIndex;
    }
    ValueBinding vb = getValueBinding(ATTR_TAB_INDEX);
    if (vb != null) {
      Number number = (Number) vb.getValue(getFacesContext());
      if (number != null) {
        return Integer.valueOf(number.intValue());
      }
    }
    return null;
  }

  public void setTabIndex(Integer tabIndex) {
    this.tabIndex = tabIndex;
  }

  public boolean isReadonly() {
    if (readonly != null) {
       return readonly;
    }
    javax.faces.el.ValueBinding vb = getValueBinding(ATTR_READONLY);
    if (vb != null) {
      Boolean bool = (Boolean) vb.getValue(getFacesContext());
      if (bool != null) {
        return bool;
      }
    }
    return false;
  }

  public void setReadonly(boolean readonly) {
    this.readonly = readonly;
  }
}
