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

package org.apache.myfaces.tobago.facelets.extension;

import com.sun.facelets.tag.jsf.ComponentHandler;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentSupport;
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.ELAdaptor;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIOutput;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.el.ELException;
import javax.el.ValueExpression;
import java.io.IOException;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.component.UISeparator;

public class SeparatorExtensionHandler extends ComponentHandler {
  private TagAttribute labelAttribute;

  public SeparatorExtensionHandler(ComponentConfig config) {
    super(config);
    labelAttribute = getAttribute(TobagoConstants.ATTR_LABEL);
  }

  protected void applyNextHandler(FaceletContext faceletContext, UIComponent separator)
      throws IOException, FacesException, ELException {
    if (ComponentSupport.isNew(separator)) {
      UIComponent component = (UIComponent) separator.getFacets().remove(TobagoConstants.FACET_LABEL);
      nextHandler.apply(faceletContext, component);
      separator.getFacets().put(TobagoConstants.FACET_LABEL, component);
    } else {
      nextHandler.apply(faceletContext, separator.getFacet(TobagoConstants.FACET_LABEL));
    }
  }

  protected void onComponentCreated(FaceletContext faceletContext, UIComponent separator, UIComponent parent) {
    Application application = faceletContext.getFacesContext().getApplication();
    UIViewRoot root = ComponentSupport.getViewRoot(faceletContext, parent);
    UIOutput label = (UIOutput) application.createComponent(UILabel.COMPONENT_TYPE);
    label.setId(root.createUniqueId());
    label.setRendererType("Label");
    setAttributes(faceletContext, label);
    separator.getFacets().put(TobagoConstants.FACET_LABEL, label);
    if (labelAttribute != null) {
      if (labelAttribute.isLiteral()) {
        label.setValue(labelAttribute.getValue(faceletContext));
      } else {
        ValueExpression expression = labelAttribute.getValueExpression(faceletContext, String.class);
        ELAdaptor.setExpression(label, TobagoConstants.ATTR_VALUE, expression);
      }
    }
  }

  protected MetaRuleset createMetaRuleset(Class aClass) {
    MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    if (UISeparator.class.isAssignableFrom(aClass)) {
      metaRuleset.ignore(TobagoConstants.ATTR_LABEL);
      return metaRuleset;
    } else {
      TagAttribute[] attrs = tag.getAttributes().getAll();
      for (int i = 0; i < attrs.length; i++) {
        TagAttribute attr = attrs[i];
        metaRuleset.ignore(attr.getLocalName());
      }
      return metaRuleset;
    }
  }
}

