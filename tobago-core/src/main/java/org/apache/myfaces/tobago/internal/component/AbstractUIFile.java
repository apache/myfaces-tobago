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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.SupportFieldId;
import org.apache.myfaces.tobago.component.SupportsLabelLayout;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.MessageUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractUIFile extends UIInput implements SupportsLabelLayout, Visual, ClientBehaviorHolder,
        SupportFieldId {

  // todo generate
  private static final Collection<String> EVENT_NAMES = Arrays.asList("change");

  // todo generate
  @Override
  public String getDefaultEventName() {
    return "change";
  }

  // todo generate
  @Override
  public Collection<String> getEventNames() {
    return EVENT_NAMES;
  }

  @Override
  public void validate(final FacesContext facesContext) {
    if (isRequired()) {
      if (getSubmittedValue() instanceof Part) {
        final Part file = (Part) getSubmittedValue();
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

  private void addErrorMessage(final FacesContext facesContext) {
    MessageUtils.addMessage(
        facesContext, this, FacesMessage.SEVERITY_ERROR, REQUIRED_MESSAGE_ID, new Object[]{getId()});
  }

  public abstract boolean isDisabled();

  public abstract boolean isReadonly();

  public abstract Integer getTabIndex();

  @Override
  public String getFieldId(final FacesContext facesContext) {
    return getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "real";
  }
}
