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
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.Metadata;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;
import com.sun.facelets.tag.jsf.ComponentSupport;
import org.apache.myfaces.tobago.component.Attributes;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import java.io.IOException;


/**
 * Base class of the Facelets handlers for the &lt;tx:menuXXX /> extension tags.
 */
public abstract class TobagoMenuExtensionHandler extends ComponentHandler {

  private Class subComponentLastType = Object.class;
  private Metadata subComponentMapper;

  public TobagoMenuExtensionHandler(ComponentConfig config) {
    super(config);
  }

  protected abstract String getSubComponentType();

  protected abstract String getSubRendererType();

  protected abstract String getFacetName();

  protected void applyNextHandler(FaceletContext faceletContext, UIComponent menuCommand)
      throws IOException, FacesException, ELException {
    if (ComponentSupport.isNew(menuCommand)) {
      UIComponent component = (UIComponent) menuCommand.getFacets().remove(getFacetName());
      nextHandler.apply(faceletContext, component);
      menuCommand.getFacets().put(getFacetName(), component);
    } else {
      nextHandler.apply(faceletContext, menuCommand.getFacet(getFacetName()));
    }
  }

  protected void onComponentCreated(FaceletContext faceletContext, UIComponent menuCommand, UIComponent parent) {

    Application application = faceletContext.getFacesContext().getApplication();
    UIViewRoot root = ComponentSupport.getViewRoot(faceletContext, parent);
    UIComponent component = application.createComponent(getSubComponentType());
    component.setId(root.createUniqueId());
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
