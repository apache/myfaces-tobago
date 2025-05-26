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

import org.apache.myfaces.tobago.component.LabelLayout;
import org.apache.myfaces.tobago.component.SupportFieldId;
import org.apache.myfaces.tobago.component.SupportsAccessKey;
import org.apache.myfaces.tobago.component.SupportsAutoSpacing;
import org.apache.myfaces.tobago.component.SupportsHelp;
import org.apache.myfaces.tobago.component.SupportsLabelLayout;
import org.apache.myfaces.tobago.component.SupportsDecorationPosition;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.MessageUtils;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UISelectBoolean;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;

public abstract class AbstractUISelectBoolean extends UISelectBoolean
    implements SupportsAutoSpacing, Visual, ClientBehaviorHolder, SupportFieldId, SupportsAccessKey,
    SupportsLabelLayout, SupportsHelp, SupportsDecorationPosition {

  private transient boolean nextToRenderIsLabel;

  @Override
  public boolean isSelected() {
    Object value = getSubmittedValue();
    if (value == null) {
      value = getValue();
    }
    if (value instanceof Boolean) {
      return (Boolean) value;
    } else {
      return value != null && Boolean.valueOf(value.toString());
    }
  }

  @Override
  protected void validateValue(final FacesContext facesContext, final Object convertedValue) {
    if (isRequired()) {
      if (convertedValue instanceof Boolean && !((Boolean) convertedValue)
          // String: e.g. if there is no ValueExpression
          || convertedValue instanceof String && !Boolean.parseBoolean((String) convertedValue)) {
        facesContext.addMessage(getClientId(facesContext),
            MessageUtils.getMessage(facesContext, FacesMessage.SEVERITY_ERROR, REQUIRED_MESSAGE_ID, getId()));
        setValid(false);
        return;
      }
    }
    super.validateValue(facesContext, convertedValue);
  }

  public abstract boolean isDisabled();

  public abstract boolean isReadonly();

  public boolean isError() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return !isValid()
        || !facesContext.getMessageList(getClientId(facesContext)).isEmpty();
  }

  public abstract boolean isFocus();

  public abstract Integer getTabIndex();

  public abstract String getItemLabel();

  public abstract void setItemLabel(String itemLabel);

  public abstract String getItemImage();

  @Override
  public abstract String getLabel();

  @Override
  public String getFieldId(final FacesContext facesContext) {
    return getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "field";
  }

  public boolean isLabelLayoutSkip() {
    return getLabelLayout() == LabelLayout.skip;
  }

  @Override
  public boolean isNextToRenderIsLabel() {
    return nextToRenderIsLabel;
  }

  @Override
  public void setNextToRenderIsLabel(final boolean nextToRenderIsLabel) {
    this.nextToRenderIsLabel = nextToRenderIsLabel;
  }
}
