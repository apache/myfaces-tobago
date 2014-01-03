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
import org.apache.myfaces.tobago.component.InputSuggest;
import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.facelets.SuggestMethodRule;
import org.apache.myfaces.tobago.facelets.SupportsMarkupRule;
import org.apache.myfaces.tobago.facelets.TobagoComponentHandler;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELException;
import javax.el.ValueExpression;
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

public abstract class TobagoLabelExtensionHandler extends ComponentHandler {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoLabelExtensionHandler.class);

  private static final String DEFAULT_COLUMNS = "auto;*";

  private TagAttribute labelWidthAttribute;
  private TagAttribute tipAttribute;
  private TagAttribute labelAttribute;
  private TagAttribute markupAttribute;
  private TagAttribute fieldIdAttribute;
  private Class subComponentLastType = Object.class;
  private Metadata subComponentMapper;

  public TobagoLabelExtensionHandler(final ComponentConfig config) {
    super(config);
    labelWidthAttribute = getAttribute("labelWidth");
    tipAttribute = getAttribute(Attributes.TIP);
    labelAttribute = getAttribute(Attributes.LABEL);
    markupAttribute = getAttribute(Attributes.MARKUP);
    fieldIdAttribute = getAttribute("fieldId");
  }

  protected abstract String getSubComponentType();

  protected abstract String getSubRendererType();

  protected String getRows() {
    return "auto";
  }

  protected String getColumns(final String first) {
    return first + ";*";
  }

  public void applyNextHandler(final FaceletContext ctx, final UIComponent panel)
      throws IOException, ELException {
    if (ComponentHandler.isNew(panel)) {
      // ensure that input has no parent (isNew)
      final UIComponent input = panel.getChildren().remove(1);
      try {
        input.getAttributes().put("tobago.panel", panel);
        nextHandler.apply(ctx, input);
      } finally {
        input.getAttributes().remove("tobago.panel");
      }
      UIComponent date = null;
      if (panel.getChildCount() > 1) {
        date = panel.getChildren().get(1);
      }
      panel.getChildren().add(input);
      if (date != null) {
        panel.getChildren().add(date);
      }
    } else {
      final UIComponent input = panel.getChildren().get(1);
      nextHandler.apply(ctx, input);
    }
  }

  public void onComponentCreated(
      final FaceletContext faceletContext, final UIComponent panel, final UIComponent parent) {

    final Application application = faceletContext.getFacesContext().getApplication();

    addGridLayout(faceletContext, panel, application);
    addLabel(faceletContext, (UIPanel) panel, application);

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
  }

  private void addLabel(final FaceletContext faceletContext, final UIPanel panel, final Application application) {
    final String uid = panel.getId() + "_tx_label";
    if (checkForAlreadyCreated(panel, uid)) {
      return;
    }
    final UILabel label = (UILabel) application.createComponent(UILabel.COMPONENT_TYPE);
    label.setRendererType(RendererTypes.LABEL);
    label.setId(uid);
    label.getAttributes().put(Attributes.FOR, "@auto");
    if (tipAttribute != null) {
      if (tipAttribute.isLiteral()) {
        panel.setTip(tipAttribute.getValue(faceletContext));
      } else {
        final ValueExpression expression = tipAttribute.getValueExpression(faceletContext, String.class);
        panel.setValueExpression(Attributes.TIP, expression);
      }
    }
    if (labelAttribute != null) {
      if (labelAttribute.isLiteral()) {
        label.setValue(labelAttribute.getValue(faceletContext));
      } else {
        final ValueExpression expression = labelAttribute.getValueExpression(faceletContext, String.class);
        label.setValueExpression(Attributes.VALUE, expression);
      }
    }
    if (markupAttribute != null) {
      if (markupAttribute.isLiteral()) {
        label.setMarkup(Markup.valueOf(markupAttribute.getValue()));
      } else {
        final ValueExpression expression = markupAttribute.getValueExpression(faceletContext, Object.class);
        label.setValueExpression(Attributes.MARKUP, expression);
      }
    }
    panel.getChildren().add(label);
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

  public void onComponentPopulated(
      final FaceletContext faceletContext, final UIComponent component, final UIComponent parent) {
    super.onComponentPopulated(faceletContext, component, parent);

    if (component.getChildren().size() > 1) {
      final UIComponent input = component.getChildren().get(1);
      if (input instanceof EditableValueHolder) {
        TobagoComponentHandler.addDefaultValidators(faceletContext.getFacesContext(), (EditableValueHolder) input);
      }
      if (input instanceof OnComponentPopulated) {
        ((OnComponentPopulated) input).onComponentPopulated(faceletContext.getFacesContext(), component);
      }
    }
  }

  private void addGridLayout(
      final FaceletContext faceletContext, final UIComponent panel, final Application application) {
    final UIGridLayout gridLayout = (UIGridLayout) application.createComponent(UIGridLayout.COMPONENT_TYPE);
    gridLayout.setRendererType(RendererTypes.GRID_LAYOUT);
    if (labelWidthAttribute != null) {
      String columns = getColumns(labelWidthAttribute.getValue(faceletContext));
      if (!LayoutUtils.checkTokens(columns)) {
        LOG.warn("Illegal value for columns = \"" + columns + "\" replacing with default: \"" + DEFAULT_COLUMNS + "\"");
        columns = DEFAULT_COLUMNS;
      }
      gridLayout.setColumns(columns);
    } else {
      gridLayout.setColumns(getColumns("auto"));
    }
    gridLayout.setRows(getRows());
    gridLayout.setId(panel.getId() + "_tx_layout");
    if (gridLayout instanceof OnComponentCreated) {
      ((OnComponentCreated) gridLayout).onComponentCreated(faceletContext.getFacesContext(), panel);
    }
    panel.getFacets().put(Facets.LAYOUT, gridLayout);
    if (gridLayout instanceof OnComponentPopulated) {
      ((OnComponentPopulated) gridLayout).onComponentPopulated(faceletContext.getFacesContext(), panel);
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
    //metaRuleset.ignore(Attributes.LABEL);
    metaRuleset.ignore(Attributes.TIP);
    metaRuleset.ignore("labelWidth");
    if (SupportsMarkup.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SupportsMarkupRule.INSTANCE);
    }
    if (InputSuggest.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SuggestMethodRule.INSTANCE);
    }
    return metaRuleset;
  }

  protected MetaRuleset createMetaRuleset(final Class aClass) {
    final MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    final TagAttribute[] attrs = tag.getAttributes().getAll();
    for (int i = 0; i < attrs.length; i++) {
      final TagAttribute attr = attrs[i];
      if (!attr.getLocalName().equals("rendered")) {
        metaRuleset.ignore(attr.getLocalName());
      }
    }
    return metaRuleset;
  }
}
