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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.component.UISeparator;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import java.io.IOException;

public class SeparatorExtensionHandler extends ComponentHandler {
  private TagAttribute labelAttribute;

  public SeparatorExtensionHandler(ComponentConfig config) {
    super(config);
    labelAttribute = getAttribute(Attributes.LABEL);
  }

  public void applyNextHandler(FaceletContext faceletContext, UIComponent separator)
      throws IOException, ELException {
    if (ComponentHandler.isNew(separator)) {
      UIComponent component = (UIComponent) separator.getFacets().remove(Facets.LABEL);
      nextHandler.apply(faceletContext, component);
      separator.getFacets().put(Facets.LABEL, component);
    } else {
      nextHandler.apply(faceletContext, separator.getFacet(Facets.LABEL));
    }
  }

  public void onComponentCreated(FaceletContext faceletContext, UIComponent separator, UIComponent parent) {
    Application application = faceletContext.getFacesContext().getApplication();
    UIViewRoot root = ComponentUtils.findViewRoot(faceletContext, parent);
    UIOutput label = (UIOutput) application.createComponent(UILabel.COMPONENT_TYPE);
    label.setId("_tx_" + faceletContext.generateUniqueId("label"));
    label.setRendererType("Label");
    setAttributes(faceletContext, label);
    separator.getFacets().put(Facets.LABEL, label);
    if (labelAttribute != null) {
      if (labelAttribute.isLiteral()) {
        label.setValue(labelAttribute.getValue(faceletContext));
      } else {
        ValueExpression expression = labelAttribute.getValueExpression(faceletContext, String.class);
        label.setValueExpression(Attributes.VALUE, expression);
      }
    }
  }

  protected MetaRuleset createMetaRuleset(Class aClass) {
    MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    if (UISeparator.class.isAssignableFrom(aClass)) {
      metaRuleset.ignore(Attributes.LABEL);
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
