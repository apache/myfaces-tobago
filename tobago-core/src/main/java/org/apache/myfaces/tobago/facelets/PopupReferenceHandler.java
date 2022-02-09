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
import org.apache.myfaces.tobago.event.PopupActionListener;
import org.apache.myfaces.tobago.event.ValueExpressionPopupActionListener;

import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;

public class PopupReferenceHandler extends TagHandler {

  private final TagAttribute forComponent;

  public PopupReferenceHandler(final TagConfig config) {
    super(config);
    forComponent = getAttribute(Attributes.FOR);
  }

  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException {
    if (parent instanceof ActionSource) {
      if (ComponentHandler.isNew(parent)) {
        final ActionSource actionSource = (ActionSource) parent;
        if (forComponent.isLiteral()) {
          actionSource.addActionListener(new PopupActionListener(forComponent.getValue()));
        } else {
          final ValueExpression forValueExpression = forComponent.getValueExpression(faceletContext, String.class);
          actionSource.addActionListener(new ValueExpressionPopupActionListener(forValueExpression));
        }
      }
    } else {
      throw new TagException(tag, "Parent is not of type ActionSource, type is: " + parent);
    }
  }
}
