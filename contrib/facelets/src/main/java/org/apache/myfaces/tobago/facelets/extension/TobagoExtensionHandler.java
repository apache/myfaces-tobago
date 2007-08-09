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
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;
import com.sun.facelets.tag.jsf.ComponentSupport;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.facelets.SupportsMarkupRule;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import java.io.IOException;

/*
 * Date: Jul 31, 2007
 * Time: 6:14:34 PM
 */
public abstract class TobagoExtensionHandler extends ComponentHandler {
  private TagAttribute labelWidthAttribute;
  private TagAttribute tipAttribute;
  private TagAttribute labelAttribute;

  public TobagoExtensionHandler(ComponentConfig config) {
    super(config);
    labelWidthAttribute = getAttribute("labelWidth");
    tipAttribute = getAttribute(TobagoConstants.ATTR_TIP);
    labelAttribute = getAttribute(TobagoConstants.ATTR_LABEL);
  }

  protected abstract String getSubComponentType();

  protected abstract String getSubRendererType();

  protected String getColumns(String first) {
    return first + ";*";
  }

  protected void applyNextHandler(FaceletContext ctx, UIComponent panel)
            throws IOException, FacesException, ELException {
    nextHandler.apply(ctx, (UIComponent) panel.getChildren().get(1));
  }

  protected void onComponentCreated(FaceletContext faceletContext, UIComponent panel, UIComponent parent) {

    Application application = faceletContext.getFacesContext().getApplication();
    UIViewRoot root = ComponentSupport.getViewRoot(faceletContext, parent);

    addGridLayout(faceletContext, panel, root);

    addLabel(faceletContext, panel, root);

    UIComponent input = application.createComponent(getSubComponentType());
    input.setRendererType(getSubRendererType());
    String uid = root.createUniqueId();
    input.setId(uid);

    setAttributes(faceletContext, input);

    panel.getChildren().add(input);
  }


  private void addLabel(FaceletContext faceletContext, UIComponent panel, UIViewRoot root) {
    Application application = faceletContext.getFacesContext().getApplication();
    UILabel label = (UILabel) application.createComponent(UILabel.COMPONENT_TYPE);
    label.setRendererType(TobagoConstants.RENDERER_TYPE_LABEL);
    label.setId(root.createUniqueId());
    if (tipAttribute != null) {
      label.setTip(tipAttribute.getValue(faceletContext));
    }
    if (labelAttribute != null) {
      label.setValue(labelAttribute.getValue(faceletContext));
    }
    panel.getChildren().add(label);
  }

  private void addGridLayout(FaceletContext faceletContext, UIComponent panel, UIViewRoot root) {
    Application application = faceletContext.getFacesContext().getApplication();
    UIGridLayout gridLayout = (UIGridLayout) application.createComponent(UIGridLayout.COMPONENT_TYPE);
    gridLayout.setRendererType(TobagoConstants.RENDERER_TYPE_GRID_LAYOUT);
    if (labelWidthAttribute != null) {
      gridLayout.setColumns(getColumns(labelWidthAttribute.getValue(faceletContext)));
    } else {
      gridLayout.setColumns(getColumns("fixed"));
    }

    gridLayout.setId(root.createUniqueId());
    panel.getFacets().put(TobagoConstants.FACET_LAYOUT, gridLayout);
  }

  protected MetaRuleset createMetaRuleset(Class aClass) {
    MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    if (UIPanel.class.isAssignableFrom(aClass)) {
      for (TagAttribute attr: tag.getAttributes().getAll()) {
        if (!attr.getLocalName().equals("rendered")) {
          metaRuleset.ignore(attr.getLocalName());
        }
      }
      return metaRuleset;
    } else {
      metaRuleset.ignore(TobagoConstants.ATTR_LABEL);
      metaRuleset.ignore(TobagoConstants.ATTR_TIP);
      metaRuleset.ignore("labelWidth");
    }
    if (SupportsMarkup.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SupportsMarkupRule.INSTANCE);
    }
    return metaRuleset;
  }
}
