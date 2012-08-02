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

import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagAttributeException;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.LegacyValueBinding;
import com.sun.facelets.el.LegacyMethodBinding;

import java.io.IOException;

import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeListenerValueBindingDelegate;
import org.apache.myfaces.tobago.event.TabChangeEvent;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.el.ValueExpression;
import javax.el.ELException;

/*
 * User: bommel
 * Date: 20.04.2006
 * Time: 18:14:11
 */
public class TabChangeListenerHandler extends TagHandler {

  private static final Class[] TAB_CHANGE_LISTENER_ARGS = new Class[] {TabChangeEvent.class};

  private Class listenerType;

  private final TagAttribute type;

  private final TagAttribute binding;

  private final TagAttribute listener;


  public TabChangeListenerHandler(TagConfig config) {
    super(config);
    binding = getAttribute("binding");
    type = getAttribute("type");
    listener = getAttribute("listener");
    if (type != null) {
      if (!type.isLiteral()) {
        throw new TagAttributeException(tag, type, "Must be literal");
      }
      try {
        this.listenerType = Class.forName(type.getValue());
      } catch (Exception e) {
        throw new TagAttributeException(tag, type, e);
      }
    }
  }

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws IOException, FacesException, ELException {
    if (parent instanceof TabChangeSource) {
      // only process if parent was just created
      if (parent.getParent() == null) {
        TabChangeSource changeSource = (TabChangeSource) parent;
        TabChangeListener changeListener = null;
        ValueExpression valueExpression = null;
        if (binding != null) {
          valueExpression = binding.getValueExpression(faceletContext, TabChangeListener.class);
          changeListener = (TabChangeListener) valueExpression.getValue(faceletContext);
        }
        if (changeListener == null) {
          try {
            changeListener = (TabChangeListener) listenerType.newInstance();
          } catch (Exception e) {
            throw new TagAttributeException(tag, type, e.getCause());
          }
          if (valueExpression != null) {
            valueExpression.setValue(faceletContext, changeListener);
          }
        }
        if (valueExpression != null) {
          changeSource.addTabChangeListener(new TabChangeListenerValueBindingDelegate(type.getValue(),
              new LegacyValueBinding(valueExpression)));
        } else {
          changeSource.addTabChangeListener(changeListener);
        }
        if (listener != null && !listener.isLiteral()) {
          changeSource.setTabChangeListener(new LegacyMethodBinding(
              listener.getMethodExpression(faceletContext, null, TAB_CHANGE_LISTENER_ARGS)));
        }

      }
    } else {
      throw new TagException(tag, "Parent is not of type TabChangeSource, type is: " + parent);
    }
  }
}
