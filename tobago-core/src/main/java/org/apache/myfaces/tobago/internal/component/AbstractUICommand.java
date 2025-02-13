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

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

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
      FacesContext facesContext = FacesContext.getCurrentInstance();
      ParentOfCommandVisitor visitor = new ParentOfCommandVisitor(facesContext, getClientId(facesContext));
      visitTree(VisitContext.createVisitContext(facesContext, null, ComponentUtils.SET_SKIP_UNRENDERED), visitor);
      parentOfCommands = visitor.isParentOfCommands();
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
  private static class ParentOfCommandVisitor implements VisitCallback {
    private boolean parentOfCommands = false;
    private final FacesContext facesContext;
    private final String clientId;

    private ParentOfCommandVisitor(FacesContext facesContext, String clientId) {
      this.facesContext = facesContext;
      this.clientId = clientId;
    }

    @Override
    public VisitResult visit(VisitContext context, UIComponent target) {
      if (!target.getClientId(facesContext).equals(clientId)
          && (target instanceof Visual && !((Visual) target).isPlain()
          || target.getRendererType() != null && target.getRendererType().startsWith("javax.faces"))) {
         if (!(target instanceof AbstractUIEvent)
             && (target instanceof UICommand || target instanceof UIInput)) {
           parentOfCommands = true;
           return VisitResult.COMPLETE;
         } else {
           return VisitResult.ACCEPT;
         }
      } else {
        return VisitResult.ACCEPT;
      }
    }
    public boolean isParentOfCommands() {
      return parentOfCommands;
    }
  }
}
