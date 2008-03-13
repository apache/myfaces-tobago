package org.apache.myfaces.tobago.taglib.sandbox;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIWizard;
import org.apache.myfaces.tobago.taglib.component.TobagoBodyTag;
import org.apache.myfaces.tobago.internal.taglib.TagUtils;

import javax.faces.component.UIComponent;

public class WizardControllerTag extends TobagoBodyTag implements WizardTagDeclaration {

  private static final Log LOG = LogFactory.getLog(WizardTag.class);

  private String controller;

  @Override
  public String getComponentType() {
    return UIWizard.COMPONENT_TYPE;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    // xxx
    //ComponentUtil.setStringProperty(component, ATTR_CONTROLLER, controller);
    TagUtils.setStringProperty(component, "controller", controller);
  }

  @Override
  public void release() {
    super.release();
    controller = null;
  }

  public void setController(String controller) {
    this.controller = controller;
  }
}
