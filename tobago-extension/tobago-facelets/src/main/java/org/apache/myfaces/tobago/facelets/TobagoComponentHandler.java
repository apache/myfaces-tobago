package org.apache.myfaces.tobago.facelets;

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
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;
import org.apache.myfaces.tobago.component.DeprecatedDimension;
import org.apache.myfaces.tobago.component.InputSuggest;
import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.internal.component.AbstractUIFlowLayout;
import org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.internal.config.TobagoConfigImpl;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TobagoComponentHandler extends ComponentHandler {

  public TobagoComponentHandler(ComponentConfig componentConfig) {
    super(componentConfig);
  }

  protected MetaRuleset createMetaRuleset(Class aClass) {
    MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    if (SortActionSource.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SortActionSourceRule.INSTANCE);
    }
    if (DeprecatedDimension.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(DeprecatedDimensionRule.INSTANCE);
    }
    if (AbstractUIPopup.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(PositionRule.INSTANCE);
    }
    if (AbstractUIGridLayout.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(GridLayoutRule.INSTANCE);
    }
    if (AbstractUIFlowLayout.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(FlowLayoutRule.INSTANCE);
    }
    if (TabChangeSource.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(TabChangeSourceRule.INSTANCE);
    }
    if (SheetStateChangeSource.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SheetStateChangeSourceRule.INSTANCE);
    }
    if (SupportsMarkup.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SupportsMarkupRule.INSTANCE);
    }
    if (InputSuggest.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SuggestMethodRule.INSTANCE);
    }
    if (SupportsRenderedPartially.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SupportsRenderedPartiallyRule.INSTANCE);
    }
    return metaRuleset;
  }

  protected void onComponentCreated(FaceletContext context, UIComponent component, UIComponent parent) {
    if (component instanceof OnComponentCreated
        && component.getAttributes().get(OnComponentCreated.MARKER) == null) {
      component.getAttributes().put(OnComponentCreated.MARKER, Boolean.TRUE);
      ((OnComponentCreated) component).onComponentCreated(context.getFacesContext(), parent);
    }
  }

  protected void onComponentPopulated(FaceletContext context, UIComponent component, UIComponent parent) {
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

  public static void addDefaultValidators(FacesContext context, EditableValueHolder component) {
    TobagoConfigImpl tobagoConfig = (TobagoConfigImpl) TobagoConfig.getInstance(context);
    Map validatorInfoMap = tobagoConfig.getDefaultValidatorInfo();
    if (validatorInfoMap.isEmpty()) {
      return;
    }
    Validator[] validators = component.getValidators();
    if (validators.length > 0) {
      Set classNames = new HashSet();
      // collect classNames of validators
      for (int i = 0; i < validators.length; i++) {
        classNames.add(validators[i].getClass().getName());
      }
      Iterator it = validatorInfoMap.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        if (!classNames.contains(entry.getValue())) {
          component.addValidator(context.getApplication().createValidator((String) entry.getKey()));
        }
      }
    } else {
      Iterator it = validatorInfoMap.keySet().iterator();
      while (it.hasNext()) {
        component.addValidator(context.getApplication().createValidator((String) it.next()));
      }
    }
  }
}
