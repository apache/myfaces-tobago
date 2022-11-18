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

package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.event.ResetFormActionListener;
import org.apache.myfaces.tobago.event.ResetInputActionListener;
import org.apache.myfaces.tobago.event.ValueExpressionResetInputActionListener;
import org.apache.myfaces.tobago.util.ComponentUtils;

import jakarta.el.ValueExpression;
import jakarta.faces.component.ActionSource;
import jakarta.faces.component.UIComponent;
import jakarta.faces.view.facelets.ComponentHandler;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagException;
import jakarta.faces.view.facelets.TagHandler;

import java.io.IOException;

public class ResetInputActionListenerHandler extends TagHandler {

  private final TagAttribute execute;

  public ResetInputActionListenerHandler(final TagConfig config) {
    super(config);
    execute = getAttribute(Attributes.execute.getName());
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException {
    if (parent instanceof ActionSource) {
      if (ComponentHandler.isNew(parent)) {
        final ActionSource actionSource = (ActionSource) parent;
        if (execute == null) {
          actionSource.addActionListener(new ResetFormActionListener());
        } else if (execute.isLiteral()) {
          actionSource.addActionListener(new ResetInputActionListener(ComponentUtils.splitList(execute.getValue())));
        } else {
          final ValueExpression forValueExpression = execute.getValueExpression(faceletContext, String.class);
          actionSource.addActionListener(new ValueExpressionResetInputActionListener(forValueExpression));
        }
      }
    } else {
      throw new TagException(tag, "Parent is not of type ActionSource, type is: " + parent);
    }
  }
}


