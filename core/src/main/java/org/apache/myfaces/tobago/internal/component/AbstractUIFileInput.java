package org.apache.myfaces.tobago.internal.component;

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

import org.apache.commons.fileupload.FileItem;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.util.MessageFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/*
 * Date: 10.02.2006
 * Time: 19:02:13
 */
public abstract class AbstractUIFileInput 
    extends javax.faces.component.UIInput implements LayoutComponent {

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

}
