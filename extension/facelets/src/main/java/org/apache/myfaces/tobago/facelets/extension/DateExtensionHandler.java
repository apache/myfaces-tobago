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

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentSupport;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.UIDateInput;
import org.apache.myfaces.tobago.component.UIDatePicker;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

/*
 * Date: Aug 8, 2007
 * Time: 5:47:26 PM
 */
public class DateExtensionHandler extends TobagoLabelExtensionHandler {

  public DateExtensionHandler(ComponentConfig config) {
    super(config);
  }

  protected String getSubComponentType() {
    return UIDateInput.COMPONENT_TYPE;
  }

  protected String getSubRendererType() {
    return TobagoConstants.RENDERER_TYPE_DATE;
  }

  protected void onComponentPopulated(FaceletContext faceletContext, UIComponent panel, UIComponent parent) {
    if (panel.getChildCount() == 2) {
      Application application = faceletContext.getFacesContext().getApplication();
      UIDatePicker picker = (UIDatePicker) application.createComponent(UIDatePicker.COMPONENT_TYPE);
      picker.setRendererType(TobagoConstants.RENDERER_TYPE_DATE_PICKER);
      picker.setFor("@auto");
      UIViewRoot root = ComponentSupport.getViewRoot(faceletContext, parent);
      picker.setId(root.createUniqueId());
      if (picker.getAttributes().get(TobagoConstants.TOBAGO_COMPONENT_CREATED) == null) {
        picker.getAttributes().put(TobagoConstants.TOBAGO_COMPONENT_CREATED, Boolean.TRUE);
        picker.onComponentCreated();
      }
      panel.getChildren().add(picker);
    }
  }

  protected String getColumns(String first) {
    return first + ";*;fixed";
  }
}
