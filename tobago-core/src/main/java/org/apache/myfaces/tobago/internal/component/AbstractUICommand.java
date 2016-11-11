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
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

public abstract class AbstractUICommand extends AbstractUICommandBase
    implements SupportsAccessKey, Visual, ClientBehaviorHolder, SupportFieldId {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUICommand.class);

  enum PropertyKeys {
    disabled,
  }

  // todo: transient
  private Boolean parentOfCommands;

  public boolean isParentOfCommands() {
    if (parentOfCommands == null) {
      parentOfCommands = false;
      for (UIComponent child : getChildren()) {
        if (child instanceof UICommand || child instanceof UIInput) {
          parentOfCommands = true;
          break;
        }
      }
    }
    return parentOfCommands;
  }

  @Override
  public abstract String getLabel();

  @Override
  public String getFieldId(final FacesContext facesContext) {
    return getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "command";
  }
}
