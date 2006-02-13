package org.apache.myfaces.tobago.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.util.MessageFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * User: weber
 * Date: May 31, 2005
 * Time: 7:47:11 PM
 */
public class UISelectOne extends javax.faces.component.UISelectOne {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.SelectOne";
  public static final String MESSAGE_VALUE_REQUIRED = "tobago.SelectOne.MESSAGE_VALUE_REQUIRED";

  public void encodeBegin(FacesContext facesContext) throws IOException {
    // TODO change this should be renamed to DimensionUtils.prepare!!!
    //UILayout.getLayout(this).layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
  }

  public void validate(FacesContext facesContext) {
    if (isRequired()) {

      Object localValue = getLocalValue();
      if (localValue == null || "".equals(localValue)) {
        FacesMessage facesMessage = MessageFactory.createFacesMessage(
            facesContext, MESSAGE_VALUE_REQUIRED, FacesMessage.SEVERITY_ERROR);
        facesContext.addMessage(getClientId(facesContext), facesMessage);
        setValid(false);
      }
    }
    super.validate(facesContext);
  }

}
