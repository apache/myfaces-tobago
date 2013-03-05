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
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

public class SelectBooleanCheckboxExtensionHandler extends TobagoLabelExtensionHandler {

  private TagAttribute itemLabelAttribute;

  public SelectBooleanCheckboxExtensionHandler(ComponentConfig config) {
    super(config);
    itemLabelAttribute = getAttribute(Attributes.ITEM_LABEL);
  }

  protected void enrichInput(FaceletContext faceletContext, UIComponent input) {
    super.enrichInput(faceletContext, input);
    UISelectBooleanCheckbox checkbox = (UISelectBooleanCheckbox) input;
    if (itemLabelAttribute != null) {
      if (itemLabelAttribute.isLiteral()) {
        checkbox.setItemLabel(itemLabelAttribute.getValue(faceletContext));
      } else {
        ValueExpression expression = itemLabelAttribute.getValueExpression(faceletContext, String.class);
        checkbox.setValueExpression(Attributes.ITEM_LABEL, expression);
      }
    } else {
      checkbox.setItemLabel(""); // for compatibility (TOBAGO-1093)
    }
  }

  protected String getSubComponentType() {
    return UISelectBooleanCheckbox.COMPONENT_TYPE;
  }

  protected String getSubRendererType() {
    return RendererTypes.SELECT_BOOLEAN_CHECKBOX;
  }
}
