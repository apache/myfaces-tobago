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

import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.event.ValueExpressionTabChangeListener;

import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagAttributeException;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagException;
import jakarta.faces.view.facelets.TagHandler;

import java.io.IOException;

public class TabChangeListenerHandler extends TagHandler {

  private Class listenerType;

  private final TagAttribute type;

  private final TagAttribute binding;

  public TabChangeListenerHandler(final TagConfig config) {
    super(config);
    binding = getAttribute("binding");
    type = getAttribute("type");
    if (type != null) {
      if (!type.isLiteral()) {
        throw new TagAttributeException(tag, type, "Must be literal");
      }
      try {
        this.listenerType = Class.forName(type.getValue());
      } catch (final Exception e) {
        throw new TagAttributeException(tag, type, e);
      }
    }
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException {
    if (parent instanceof TabChangeSource) {
      // only process if parent was just created
      if (parent.getParent() == null) {
        final TabChangeSource changeSource = (TabChangeSource) parent;
        TabChangeListener listener = null;
        ValueExpression valueExpression = null;
        if (binding != null) {
          valueExpression = binding.getValueExpression(faceletContext, TabChangeListener.class);
          listener = (TabChangeListener) valueExpression.getValue(faceletContext);
        }
        if (listener == null) {
          try {
            listener = (TabChangeListener) listenerType.newInstance();
          } catch (final Exception e) {
            throw new TagAttributeException(tag, type, e);
          }
          if (valueExpression != null) {
            valueExpression.setValue(faceletContext, listener);
          }
        }
        if (valueExpression != null) {
          changeSource.addTabChangeListener(
              new ValueExpressionTabChangeListener(type.getValue(), valueExpression));
        } else {
          changeSource.addTabChangeListener(listener);
        }
      }
    } else {
      throw new TagException(tag, "Parent is not of type TabChangeSource, type is: " + parent);
    }
  }
}
