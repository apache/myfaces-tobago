package org.apache.myfaces.tobago.component;

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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ENCTYPE;
import org.apache.myfaces.tobago.util.MessageFactory;
import org.apache.commons.fileupload.FileItem;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 10.02.2006
 * Time: 19:02:13
 */
public class UIFileInput extends javax.faces.component.UIInput {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.FileInput";

  public void setParent(UIComponent uiComponent) {
    super.setParent(uiComponent);
    UIPage form = ComponentUtil.findPage(getFacesContext(), uiComponent);
    if (form != null) {
      form.getAttributes().put(ATTR_ENCTYPE, "multipart/form-data");
    }
  }

  public void validate(FacesContext facesContext) {
    if (isRequired()) {
      FileItem file = (FileItem) getSubmittedValue();
      if (file == null || file.getName().length() == 0) {
        FacesMessage facesMessage = MessageFactory.createFacesMessage(
            facesContext, REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR);
        facesContext.addMessage(getClientId(facesContext), facesMessage);
        setValid(false);
      }
    }
    super.validate(facesContext);
  }
}
