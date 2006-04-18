package org.apache.myfaces.tobago.facelets;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.ELAdaptor;
import com.sun.facelets.el.LegacyMethodBinding;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.tag.TagHandler;

public final class AttributeHandler extends TagHandler {
  private static final Class [] VALIDATOR =
      new Class[] {FacesContext.class, UIComponent.class, Object.class};

  private final TagAttribute name;

  private final TagAttribute value;

  public AttributeHandler(TagConfig config) {
    super(config);
    this.name = this.getRequiredAttribute("name");
    this.value = this.getRequiredAttribute("value");
  }

  public void apply(FaceletContext ctx, UIComponent parent)
      throws FacesException, ELException {
    if (parent == null) {
      throw new TagException(tag, "Parent UIComponent was null");
    }

    if (parent.getParent() == null) {
      String nameValue = name.getValue(ctx);
      if ("rendered".equals(nameValue)) {
        parent.setRendered(value.getBoolean(ctx));
      } else if (parent instanceof EditableValueHolder
          && "validator".equals(nameValue)) {
        MethodExpression methodExpression = value.getMethodExpression(ctx, null, VALIDATOR);
        ((EditableValueHolder) parent).setValidator(new LegacyMethodBinding(methodExpression));
      } else if (!parent.getAttributes().containsKey(nameValue)) {
        if (value.isLiteral()) {
          parent.getAttributes().put(nameValue, value.getValue());
        } else {
          ELAdaptor.setExpression(parent, nameValue, value
              .getValueExpression(ctx, Object.class));
        }
      }
    }
  }
}
