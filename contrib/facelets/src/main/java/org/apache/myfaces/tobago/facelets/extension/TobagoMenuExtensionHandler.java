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

import com.sun.facelets.tag.jsf.ComponentHandler;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentSupport;
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.FaceletContext;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.el.ELException;
import java.io.IOException;

import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.TobagoConstants;

/*
 * Date: Aug 10, 2007
 * Time: 9:40:24 PM
 */
public abstract class TobagoMenuExtensionHandler extends ComponentHandler {

  public TobagoMenuExtensionHandler(ComponentConfig config) {
    super(config);
  }

  protected abstract String getSubComponentType();

  protected void applyNextHandler(FaceletContext faceletContext, UIComponent menuCommand)
      throws IOException, FacesException, ELException {
    nextHandler.apply(faceletContext, menuCommand.getFacet(TobagoConstants.FACET_ITEMS));
  }

  protected void onComponentCreated(FaceletContext faceletContext, UIComponent menuCommand, UIComponent parent) {

    Application application = faceletContext.getFacesContext().getApplication();
    UIViewRoot root = ComponentSupport.getViewRoot(faceletContext, parent);
    UIComponent component = application.createComponent(getSubComponentType());
    component.setId(root.createUniqueId());
    setAttributes(faceletContext, component);
    menuCommand.getFacets().put(TobagoConstants.FACET_ITEMS, component);
  }

  protected MetaRuleset createMetaRuleset(Class aClass) {
    MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    if (UIMenuCommand.class.isAssignableFrom(aClass)) {
      metaRuleset.ignore(TobagoConstants.ATTR_CONVERTER);
      metaRuleset.ignore(TobagoConstants.ATTR_VALUE);
      return metaRuleset;
    } else {
      for (TagAttribute attr : tag.getAttributes().getAll()) {
        if (!(attr.getLocalName().equals(TobagoConstants.ATTR_CONVERTER)
            || attr.getLocalName().equals(TobagoConstants.ATTR_VALUE))) {
          metaRuleset.ignore(attr.getLocalName());
        }
      }
      return metaRuleset;
    }
  }
}
