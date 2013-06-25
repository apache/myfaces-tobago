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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.InputSuggest2;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public abstract class AbstractUIIn extends AbstractUIInput implements OnComponentPopulated, InputSuggest2 {

  public void onComponentPopulated(FacesContext facesContext, UIComponent parent) {
    if (getSuggestMethodExpression() != null) {
      if (getSuggest() == null) {
        AbstractUISuggest suggest = (AbstractUISuggest) CreateComponentUtils.createComponent(
            facesContext, AbstractUISuggest.COMPONENT_TYPE, RendererTypes.SUGGEST, null);
        getChildren().add(suggest);
        suggest.setSuggestMethodExpression(getSuggestMethodExpression());
        suggest.setMinimumCharacters(getSuggestMinChars());
        suggest.setDelay(getSuggestDelay());
      }
    }
  }

  public AbstractUISuggest getSuggest() {
    return ComponentUtils.findDescendant(this, AbstractUISuggest.class);
  }

  public abstract Integer getSuggestDelay();

  public abstract Integer getSuggestMinChars();

}
