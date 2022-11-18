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

import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.event.TabChangeSource;

import jakarta.faces.component.EditableValueHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.Validator;
import jakarta.faces.view.facelets.ComponentConfig;
import jakarta.faces.view.facelets.ComponentHandler;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.MetaRuleset;

import java.util.HashSet;
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
    if (TabChangeSource.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(TabChangeSourceRule.INSTANCE);
    }
    if (SheetStateChangeSource.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SheetStateChangeSourceRule.INSTANCE);
    }
    if (Visual.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SupportsMarkupRule.INSTANCE);
    }

    return metaRuleset;
  }

  @Override
  public void onComponentPopulated(
      final FaceletContext context, final UIComponent component, final UIComponent parent) {

    // TODO call only if component was created
    if (component instanceof EditableValueHolder) {
      addDefaultValidators(context.getFacesContext(), (EditableValueHolder) component);
    }
  }

  private void addDefaultValidators(final FacesContext facesContext, final EditableValueHolder component) {
    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);
    final Map<String, String> validatorInfoMap = tobagoConfig.getDefaultValidatorInfo();
    if (validatorInfoMap.isEmpty()) {
      return;
    }
    final Validator[] validators = component.getValidators();
    if (validators.length > 0) {
      final Set<String> classNames = new HashSet<>();
      // collect classNames of validators
      for (final Validator validator : validators) {
        classNames.add(validator.getClass().getName());
      }
      validatorInfoMap.forEach((key, value) -> {
        if (!classNames.contains(value)) {
          component.addValidator(facesContext.getApplication().createValidator(key));
        }
      });
    } else {
      for (final String next : validatorInfoMap.keySet()) {
        component.addValidator(facesContext.getApplication().createValidator(next));
      }
    }
  }
}
