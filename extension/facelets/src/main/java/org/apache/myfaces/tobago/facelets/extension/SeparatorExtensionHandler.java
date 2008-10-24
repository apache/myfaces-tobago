package org.apache.myfaces.tobago.facelets.extension;

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

import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.ELAdaptor;
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;
import com.sun.facelets.tag.jsf.ComponentSupport;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.component.UISeparator;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import java.io.IOException;

public class SeparatorExtensionHandler extends ComponentHandler {
  private TagAttribute labelAttribute;

  public SeparatorExtensionHandler(ComponentConfig config) {
    super(config);
    labelAttribute = getAttribute(Attributes.LABEL);
  }

  protected void applyNextHandler(FaceletContext faceletContext, UIComponent separator)
      throws IOException, FacesException, ELException {
    if (ComponentSupport.isNew(separator)) {
      UIComponent component = (UIComponent) separator.getFacets().remove(Facets.LABEL);
      nextHandler.apply(faceletContext, component);
      separator.getFacets().put(Facets.LABEL, component);
    } else {
      nextHandler.apply(faceletContext, separator.getFacet(Facets.LABEL));
    }
  }

  protected void onComponentCreated(FaceletContext faceletContext, UIComponent separator, UIComponent parent) {
    Application application = faceletContext.getFacesContext().getApplication();
    UIViewRoot root = ComponentSupport.getViewRoot(faceletContext, parent);
    UIOutput label = (UIOutput) application.createComponent(UILabel.COMPONENT_TYPE);
    label.setId(root.createUniqueId());
    label.setRendererType("Label");
    setAttributes(faceletContext, label);
    separator.getFacets().put(Facets.LABEL, label);
    if (labelAttribute != null) {
      if (labelAttribute.isLiteral()) {
        label.setValue(labelAttribute.getValue(faceletContext));
      } else {
        ValueExpression expression = labelAttribute.getValueExpression(faceletContext, String.class);
        ELAdaptor.setExpression(label, Attributes.VALUE, expression);
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
