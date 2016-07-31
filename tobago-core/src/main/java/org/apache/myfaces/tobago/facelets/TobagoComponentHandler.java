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

package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.component.InputSuggest;
import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.SupportsAjaxBehaviorHolder;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.internal.component.AbstractUISegmentLayout;
import org.apache.myfaces.tobago.internal.config.TobagoConfigImpl;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TobagoComponentHandler extends ComponentHandler {

  public TobagoComponentHandler(final ComponentConfig componentConfig) {
    super(componentConfig);
  }

  @Override
  protected MetaRuleset createMetaRuleset(final Class aClass) {
    final MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    if (SortActionSource.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SortActionSourceRule.INSTANCE);
    }
    if (AbstractUISegmentLayout.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SegmentLayoutRule.INSTANCE);
    }
    if (TabChangeSource.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(TabChangeSourceRule.INSTANCE);
    }
    if (SheetStateChangeSource.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SheetStateChangeSourceRule.INSTANCE);
    }
    if (Visual.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SupportsMarkupRule.INSTANCE);
    }
    if (InputSuggest.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SuggestMethodRule.INSTANCE);
    }
    if (SupportsAjaxBehaviorHolder.class.isAssignableFrom(aClass)
        || SupportsRenderedPartially.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SupportsAjaxBehaviorHolderRule.INSTANCE);
    }

    return metaRuleset;
  }

  @Override
  public void onComponentCreated(final FaceletContext context, final UIComponent component, final UIComponent parent) {
    if (component instanceof OnComponentCreated
        && component.getAttributes().get(OnComponentCreated.MARKER) == null) {
      component.getAttributes().put(OnComponentCreated.MARKER, Boolean.TRUE);
      ((OnComponentCreated) component).onComponentCreated(context.getFacesContext(), parent);
    }
  }

  @Override
  public void onComponentPopulated(
      final FaceletContext context, final UIComponent component, final UIComponent parent) {
    if (component instanceof OnComponentPopulated
        && component.getAttributes().get(OnComponentPopulated.MARKER) == null) {
      component.getAttributes().put(OnComponentPopulated.MARKER, Boolean.TRUE);
      ((OnComponentPopulated) component).onComponentPopulated(context.getFacesContext(), parent);
    }
    // TODO call only if component was created
    if (component instanceof EditableValueHolder) {
      addDefaultValidators(context.getFacesContext(), (EditableValueHolder) component);
    }
  }

  public static void addDefaultValidators(final FacesContext context, final EditableValueHolder component) {
    final TobagoConfigImpl tobagoConfig = (TobagoConfigImpl) TobagoConfig.getInstance(context);
    final Map validatorInfoMap = tobagoConfig.getDefaultValidatorInfo();
    if (validatorInfoMap.isEmpty()) {
      return;
    }
    final Validator[] validators = component.getValidators();
    if (validators.length > 0) {
      final Set classNames = new HashSet();
      // collect classNames of validators
      for (int i = 0; i < validators.length; i++) {
        classNames.add(validators[i].getClass().getName());
      }
      final Iterator it = validatorInfoMap.entrySet().iterator();
      while (it.hasNext()) {
        final Map.Entry entry = (Map.Entry) it.next();
        if (!classNames.contains(entry.getValue())) {
          component.addValidator(context.getApplication().createValidator((String) entry.getKey()));
        }
      }
    } else {
      final Iterator it = validatorInfoMap.keySet().iterator();
      while (it.hasNext()) {
        component.addValidator(context.getApplication().createValidator((String) it.next()));
      }
    }
  }
}
