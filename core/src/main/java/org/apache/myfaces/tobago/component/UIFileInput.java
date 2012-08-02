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

import org.apache.commons.fileupload.FileItem;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ENCTYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TAB_INDEX;
import org.apache.myfaces.tobago.util.MessageFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/*
 * Date: 10.02.2006
 * Time: 19:02:13
 */
public class UIFileInput extends javax.faces.component.UIInput {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.FileInput";

  private Integer tabIndex;

  public void setParent(UIComponent uiComponent) {
    super.setParent(uiComponent);
    UIPage form = ComponentUtil.findPage(getFacesContext(), uiComponent);
    if (form != null) {
      form.getAttributes().put(ATTR_ENCTYPE, "multipart/form-data");
    } else {
      FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(UIPage.ENCTYPE_KEY,
          "multipart/form-data");
    }
  }

  public void validate(FacesContext facesContext) {
    if (isRequired()) {
      if (getSubmittedValue() instanceof FileItem) {
        FileItem file = (FileItem) getSubmittedValue();
        if (file == null || file.getName().length() == 0) {
          addErrorMessage(facesContext);
          setValid(false);
        }
      } else {
        addErrorMessage(facesContext);
        setValid(false);
      }
    }
    super.validate(facesContext);
  }

  private void addErrorMessage(FacesContext facesContext) {
    FacesMessage facesMessage = MessageFactory.createFacesMessage(
        facesContext, REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR);
    facesContext.addMessage(getClientId(facesContext), facesMessage);
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    tabIndex = (Integer) values[1];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = tabIndex;
    return values;
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
}
