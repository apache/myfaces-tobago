package org.apache.myfaces.tobago.facelets;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.ActionSource;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.ELAdaptor;
import com.sun.facelets.el.LegacyMethodBinding;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.jsf.ComponentSupport;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.SupportsMarkup;

public final class AttributeHandler extends TagHandler {

  private final TagAttribute name;

  private final TagAttribute value;

  public AttributeHandler(TagConfig config) {
    super(config);
    this.name = getRequiredAttribute(TobagoConstants.ATTR_NAME);
    this.value = getRequiredAttribute(TobagoConstants.ATTR_VALUE);
  }

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws FacesException, ELException {
    if (parent == null) {
      throw new TagException(tag, "Parent UIComponent was null");
    }

    if (ComponentSupport.isNew(parent)) {
      String nameValue = name.getValue(faceletContext);
      if (TobagoConstants.ATTR_RENDERED.equals(nameValue)) {
        // TODO expression
        if (value.isLiteral()) {
          parent.setRendered(value.getBoolean(faceletContext));
        } else {
          ELAdaptor.setExpression(parent, nameValue, value.getValueExpression(faceletContext, Object.class));
        }
      } else if (TobagoConstants.ATTR_RENDERED_PARTIALLY.equals(nameValue) && parent instanceof UICommand) {
        // TODO test expression
        ComponentUtil.setRenderedPartially((UICommand) parent, value.getValue());
      } else if (TobagoConstants.ATTR_STYLE_CLASS.equals(nameValue)) {
        // TODO test expression
        ComponentUtil.setStyleClasses(parent, value.getValue());
      } else if (TobagoConstants.ATTR_MARKUP.equals(nameValue) && parent instanceof SupportsMarkup) {
        // TODO test expression
        ComponentUtil.setMarkup(parent, value.getValue());
      } else if (parent instanceof EditableValueHolder && TobagoConstants.ATTR_VALIDATOR.equals(nameValue)) {
        MethodExpression methodExpression =
            value.getMethodExpression(faceletContext, null, ComponentUtil.VALIDATOR_ARGS);
        ((EditableValueHolder) parent).setValidator(new LegacyMethodBinding(methodExpression));
      } else if (parent instanceof ActionSource && TobagoConstants.ATTR_ACTION.equals(nameValue)) {
        MethodExpression action = getActionMethodExpression(faceletContext, ComponentUtil.ACTION_ARGS, String.class);
        if (action != null) {
          // TODO jsf 1.2
          ((ActionSource)parent).setAction(new LegacyMethodBinding(action));
        }
      } else if (parent instanceof ActionSource && TobagoConstants.ATTR_ACTION_LISTENER.equals(nameValue)) {
        MethodExpression action = getActionMethodExpression(faceletContext, ComponentUtil.ACTION_LISTENER_ARGS, null);
        if (action != null) {
          // TODO jsf 1.2
          ((ActionSource)parent).setActionListener(new LegacyMethodBinding(action));
        }
      } else if (!parent.getAttributes().containsKey(nameValue)) {
        if (value.isLiteral()) {
          parent.getAttributes().put(nameValue, value.getValue());
        } else {
          ELAdaptor.setExpression(parent, nameValue, value.getValueExpression(faceletContext, Object.class));
        }
      }
    }
  }

  private MethodExpression getActionMethodExpression(FaceletContext faceletContext, Class [] args, Class returnType) {
    if (value.getValue().startsWith("$")) {
      Object obj = value.getValueExpression(faceletContext, String.class).getValue(faceletContext);
      if (obj != null && obj instanceof String && ((String)obj).length() > 0) {
        TagAttribute attribute = new TagAttribute(value.getLocation(), value.getNamespace(),
            value.getLocalName(), value.getQName(), (String) obj);
        return attribute.getMethodExpression(faceletContext, returnType, args);
      }
    } else {
      return value.getMethodExpression(faceletContext, returnType, args);
    }
    return null;
  }
}
