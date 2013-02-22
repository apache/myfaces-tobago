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

import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIDate;
import org.apache.myfaces.tobago.component.UIDatePicker;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

public class DateExtensionHandler extends TobagoLabelExtensionHandler {

  private TagAttribute pickerIdAttribute;
  private TagAttribute formIdAttribute;

  public DateExtensionHandler(ComponentConfig config) {
    super(config);
    pickerIdAttribute = getAttribute("pickerId");
    formIdAttribute = getAttribute("formId");
  }

  protected String getSubComponentType() {
    return UIDate.COMPONENT_TYPE;
  }

  protected String getSubRendererType() {
    return RendererTypes.DATE;
  }

  public void onComponentPopulated(FaceletContext faceletContext, UIComponent panel, UIComponent parent) {
    super.onComponentPopulated(faceletContext, panel, parent);
    if (panel.getChildCount() == 2) {
      Application application = faceletContext.getFacesContext().getApplication();
      UIViewRoot root = ComponentUtils.findViewRoot(faceletContext, parent);

      UIForm form = (UIForm) application.createComponent(UIForm.COMPONENT_TYPE);
      form.setRendererType(RendererTypes.FORM);
      form.setId(formIdAttribute != null ? formIdAttribute.getValue(faceletContext) : root.createUniqueId());
      panel.getChildren().add(form);

      UIDatePicker picker = (UIDatePicker) application.createComponent(UIDatePicker.COMPONENT_TYPE);
      picker.setRendererType(RendererTypes.DATE_PICKER);
      picker.setFor("@auto");
      picker.setId(pickerIdAttribute != null ? pickerIdAttribute.getValue(faceletContext) : root.createUniqueId());
      if (picker.getAttributes().get(OnComponentCreated.MARKER) == null) {
        picker.getAttributes().put(OnComponentCreated.MARKER, Boolean.TRUE);
        picker.onComponentCreated(faceletContext.getFacesContext(), panel);
      }
      form.getChildren().add(picker);
    }
  }

  protected String getColumns(String first) {
    return first + ";*;auto";
  }
}
