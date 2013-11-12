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

import javax.el.ELException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.TagAttribute;
import java.io.IOException;


/**
 * Base class of the Facelets handlers for the &lt;tx:menuXXX /> extension tags.
 */
public abstract class TobagoMenuExtensionHandler extends ComponentHandler {

  private Class subComponentLastType = Object.class;
  private Metadata subComponentMapper;
  private TagAttribute fieldIdAttribute;

  public TobagoMenuExtensionHandler(ComponentConfig config) {
    super(config);
    fieldIdAttribute = getAttribute("fieldId");
  }

  protected abstract String getSubComponentType();

  protected abstract String getSubRendererType();

  protected abstract String getFacetName();

  public void applyNextHandler(FaceletContext faceletContext, UIComponent menuCommand)
      throws IOException, ELException {
    if (ComponentHandler.isNew(menuCommand)) {
      UIComponent component = (UIComponent) menuCommand.getFacets().remove(getFacetName());
      nextHandler.apply(faceletContext, component);
      menuCommand.getFacets().put(getFacetName(), component);
    } else {
      nextHandler.apply(faceletContext, menuCommand.getFacet(getFacetName()));
    }
  }

  public void onComponentCreated(FaceletContext faceletContext, UIComponent menuCommand, UIComponent parent) {

    final Application application = faceletContext.getFacesContext().getApplication();
    final UIComponent component = application.createComponent(getSubComponentType());
    final String uid = fieldIdAttribute != null
        ? fieldIdAttribute.getValue(faceletContext)
        : "_tx_" + faceletContext.generateUniqueId("sub");
    component.setId(uid);
    component.setRendererType(getSubRendererType());
    setSubComponentAttributes(faceletContext, component);
    menuCommand.getFacets().put(getFacetName(), component);
  }

  private void setSubComponentAttributes(FaceletContext ctx, Object instance) {
    if (instance != null) {
      Class type = instance.getClass();
      if (subComponentMapper == null || !subComponentLastType.equals(type)) {
        subComponentLastType = type;
        subComponentMapper = createSubComponentMetaRuleset(type).finish();
      }
      subComponentMapper.applyMetadata(ctx, instance);
    }
  }

  protected MetaRuleset createSubComponentMetaRuleset(Class aClass) {
    MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    TagAttribute [] attrs = tag.getAttributes().getAll();
    for (int i = 0; i < attrs.length; i++) {
      TagAttribute attr = attrs[i];
      if (!(attr.getLocalName().equals(Attributes.CONVERTER)
          || attr.getLocalName().equals(Attributes.VALUE))) {
        metaRuleset.ignore(attr.getLocalName());
      }
    }
    return metaRuleset;
  }

  protected MetaRuleset createMetaRuleset(Class aClass) {
    MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    metaRuleset.ignore(Attributes.CONVERTER);
    metaRuleset.ignore(Attributes.VALUE);
    return metaRuleset;
  }
}
