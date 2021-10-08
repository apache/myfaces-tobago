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

import org.apache.myfaces.tobago.component.SupportsAutoSpacing;
import org.apache.myfaces.tobago.component.SupportsHelp;
import org.apache.myfaces.tobago.component.SupportsLabelLayout;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.util.MessageUtils;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;

/**
 * Base class for select one.
 */
public abstract class AbstractUISelectOneBase extends jakarta.faces.component.UISelectOne
    implements SupportsAutoSpacing, Visual, SupportsLabelLayout, ClientBehaviorHolder, SupportsHelp {

  public static final String MESSAGE_VALUE_REQUIRED = "org.apache.myfaces.tobago.UISelectOne.REQUIRED";

  private transient boolean nextToRenderIsLabel;

  @Override
  public void validate(final FacesContext facesContext) {
    if (isRequired() && !isReadonly()) {
      final Object submittedValue = getSubmittedValue();
      if (submittedValue == null || "".equals(submittedValue)) {
        if (getRequiredMessage() != null) {
          final String requiredMessage = getRequiredMessage();
          facesContext.addMessage(getClientId(facesContext), new FacesMessage(FacesMessage.SEVERITY_ERROR,
              requiredMessage, requiredMessage));
        } else {
          facesContext.addMessage(getClientId(facesContext),
              MessageUtils.getMessage(facesContext, FacesMessage.SEVERITY_ERROR, MESSAGE_VALUE_REQUIRED,
                  MessageUtils.getLabel(facesContext, this)));
        }
        setValid(false);
      }
    }
    super.validate(facesContext);
  }

  public abstract boolean isReadonly();

  public abstract boolean isDisabled();

  public boolean isError() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return !isValid()
        || !facesContext.getMessageList(getClientId(facesContext)).isEmpty();
  }

  public abstract boolean isFocus();

  public abstract Integer getTabIndex();

  @Override
  public boolean isNextToRenderIsLabel() {
    return nextToRenderIsLabel;
  }

  @Override
  public void setNextToRenderIsLabel(final boolean nextToRenderIsLabel) {
    this.nextToRenderIsLabel = nextToRenderIsLabel;
  }
}
