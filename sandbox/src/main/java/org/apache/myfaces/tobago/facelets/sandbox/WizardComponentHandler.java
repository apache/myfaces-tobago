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

package org.apache.myfaces.tobago.facelets.sandbox;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.ELAdaptor;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;
import org.apache.myfaces.tobago.facelets.TobagoComponentHandler;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

public class WizardComponentHandler extends TobagoComponentHandler {

  private TagAttribute outcomeAttribute;

  public WizardComponentHandler(ComponentConfig componentConfig) {
    super(componentConfig);
    outcomeAttribute = getAttribute("outcome");
  }

  protected void onComponentCreated(FaceletContext faceletContext, UIComponent wizard, UIComponent parent) {

    if (outcomeAttribute != null) {
      if (outcomeAttribute.isLiteral()) {
        wizard.getAttributes().put("outcome", outcomeAttribute.getValue(faceletContext));
      } else {
        ValueExpression expression = outcomeAttribute.getValueExpression(faceletContext, String.class);
        ELAdaptor.setExpression(wizard, "outcome", expression);
      }
    }

    super.onComponentCreated(faceletContext, wizard, parent);
  }

}
