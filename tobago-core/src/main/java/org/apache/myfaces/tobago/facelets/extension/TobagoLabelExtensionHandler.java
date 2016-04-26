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
import org.apache.myfaces.tobago.component.InputSuggest;
import org.apache.myfaces.tobago.component.LabelLayout;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.facelets.SuggestMethodRule;
import org.apache.myfaces.tobago.facelets.SupportsMarkupRule;
import org.apache.myfaces.tobago.facelets.TobagoComponentHandler;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELException;
import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.TagAttribute;
import java.io.IOException;

/**
 * @deprecated since Tobago 3.0. The tx-library is deprecated, please use the tc-library.
 */
@Deprecated
public abstract class TobagoLabelExtensionHandler extends ComponentHandler {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoLabelExtensionHandler.class);

  private TagAttribute fieldIdAttribute;
  private Class subComponentLastType = Object.class;
  private Metadata subComponentMapper;

  public TobagoLabelExtensionHandler(final ComponentConfig config) {
    super(config);
    fieldIdAttribute = getAttribute(Attributes.fieldId.getName());
  }

  protected abstract String getSubComponentType();

  protected abstract String getSubRendererType();

  @Override
  public void applyNextHandler(final FaceletContext ctx, final UIComponent panel)
      throws IOException, ELException {
    if (ComponentHandler.isNew(panel)) {
      Deprecation.LOG.warn("The tx library is deprecated, please use the tc library. "
          + "See 'Migration to 3.0' on the web site.");
      // ensure that input has no parent (isNew)
      final UIComponent input = panel.getChildren().remove(0);
      try {
        input.getAttributes().put("tobago.panel", panel);
        nextHandler.apply(ctx, input);
      } finally {
        input.getAttributes().remove("tobago.panel");
      }
      UIComponent date = null;
      if (panel.getChildCount() > 1) {
        date = panel.getChildren().get(0);
      }
      panel.getChildren().add(input);
      if (date != null) {
        panel.getChildren().add(date);
      }
    } else {
      final UIComponent input = panel.getChildren().get(0);
      nextHandler.apply(ctx, input);
    }
  }

  @Override
  public void onComponentCreated(
      final FaceletContext faceletContext, final UIComponent panel, final UIComponent parent) {

    final Application application = faceletContext.getFacesContext().getApplication();

    final String uid;
    if (fieldIdAttribute != null) {
      uid = fieldIdAttribute.getValue(faceletContext);
    } else {
      uid = panel.getId() + "_tx_field";
    }
    if (checkForAlreadyCreated(panel, uid)) {
      return;
    }

    final UIComponent input = application.createComponent(getSubComponentType());
    input.setRendererType(getSubRendererType());
    input.setId(uid);

    setSubComponentAttributes(faceletContext, input);
    enrichInput(faceletContext, input);

    panel.getChildren().add(input);
  }

  protected void enrichInput(final FaceletContext faceletContext, final UIComponent input) {
    input.getAttributes().put(Attributes.labelLayout.getName(), LabelLayout.flexLeft);
  }

  private boolean checkForAlreadyCreated(final UIComponent panel, final String uid) {
    if (panel.getChildCount() > 0) {
      for (final UIComponent child : panel.getChildren()) {
        if (uid.equals(child.getId())) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void onComponentPopulated(
      final FaceletContext faceletContext, final UIComponent component, final UIComponent parent) {
    super.onComponentPopulated(faceletContext, component, parent);

    if (component.getChildren().size() > 1) {
      final UIComponent input = component.getChildren().get(0);
      if (input instanceof EditableValueHolder) {
        TobagoComponentHandler.addDefaultValidators(faceletContext.getFacesContext(), (EditableValueHolder) input);
      }
      if (input instanceof OnComponentPopulated) {
        ((OnComponentPopulated) input).onComponentPopulated(faceletContext.getFacesContext(), component);
      }
    }
  }

  private void setSubComponentAttributes(final FaceletContext ctx, final Object instance) {
    if (instance != null) {
      final Class type = instance.getClass();
      if (subComponentMapper == null || !subComponentLastType.equals(type)) {
        subComponentLastType = type;
        subComponentMapper = createSubComponentMetaRuleset(type).finish();
      }
      subComponentMapper.applyMetadata(ctx, instance);
    }
  }

  protected MetaRuleset createSubComponentMetaRuleset(final Class aClass) {
    final MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    metaRuleset.ignore(Attributes.tip.getName());
    metaRuleset.ignore(Attributes.labelWidth.getName());
    if (Visual.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SupportsMarkupRule.INSTANCE);
    }
    if (InputSuggest.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SuggestMethodRule.INSTANCE);
    }
    return metaRuleset;
  }

  @Override
  protected MetaRuleset createMetaRuleset(final Class aClass) {
    final MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    final TagAttribute[] attrs = tag.getAttributes().getAll();
    for (int i = 0; i < attrs.length; i++) {
      final TagAttribute attr = attrs[i];
      if (!attr.getLocalName().equals(Attributes.rendered.getName())) {
        metaRuleset.ignore(attr.getLocalName());
      }
    }
    return metaRuleset;
  }
}
