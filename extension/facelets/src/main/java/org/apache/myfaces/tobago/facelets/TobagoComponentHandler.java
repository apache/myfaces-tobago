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

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.component.UIMessages;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.event.TabChangeSource;

import javax.faces.component.UIComponent;

/*
 * Date: 15.04.2006
 * Time: 13:31:39
 */
public class TobagoComponentHandler extends ComponentHandler {

  public TobagoComponentHandler(ComponentConfig componentConfig) {
    super(componentConfig);
  }

  protected MetaRuleset createMetaRuleset(Class aClass) {
    MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    if (SortActionSource.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SortActionSourceRule.INSTANCE);
    }
    if (UIPage.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(PageRule.INSTANCE);
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
    if (UIInput.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(SuggestMethodRule.INSTANCE);
    }
    if (UIMessages.class.isAssignableFrom(aClass)) {
        metaRuleset.addRule(OrderByRule.INSTANCE);
    }
    return metaRuleset;
  }
  protected void onComponentCreated(FaceletContext context, UIComponent component,
      UIComponent parent) {
    if (component instanceof OnComponentCreated
        && component.getAttributes().get(TobagoConstants.TOBAGO_COMPONENT_CREATED) == null) {
      component.getAttributes().put(TobagoConstants.TOBAGO_COMPONENT_CREATED, Boolean.TRUE);
      ((OnComponentCreated) component).onComponentCreated();
    }
  }
}
