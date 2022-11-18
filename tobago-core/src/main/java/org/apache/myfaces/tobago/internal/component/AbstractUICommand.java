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
import org.apache.myfaces.tobago.component.SupportsAccessKey;
import org.apache.myfaces.tobago.component.SupportsAutoSpacing;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.util.ComponentUtils;

import jakarta.faces.component.UICommand;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;

/**
 * Base class for commands.
 */
public abstract class AbstractUICommand extends AbstractUICommandBase
    implements SupportsAutoSpacing, SupportsAccessKey, Visual, ClientBehaviorHolder, SupportFieldId {

  enum PropertyKeys {
    disabled,
  }

  // todo: transient
  private Boolean parentOfCommands;

  public boolean isParentOfCommands() {
    if (parentOfCommands == null) {
      parentOfCommands = false;
      for (final UIComponent child : getChildren()) {
        if (child.isRendered()
            && !(child instanceof AbstractUIEvent)
            && (child instanceof UICommand || child instanceof UIInput)) {
          parentOfCommands = true;
          break;
        }
      }
    }
    return parentOfCommands;
  }

  public abstract java.lang.String getImage();

  @Override
  public abstract String getLabel();

  @Override
  public String getFieldId(final FacesContext facesContext) {
    if (isParentOfCommands()) {
      return getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "command";
    } else {
      return getClientId(facesContext);
    }
  }
}
