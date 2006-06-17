package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIDatePicker;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;

import javax.faces.component.UIComponent;


/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 30.05.2006
 * Time: 19:11:07
 * To change this template use File | Settings | File Templates.
 */
public class DatePickerTag extends CommandTag implements DatePickerTagDeclaration {

  private String forComponent;

  @Override
  public String getComponentType() {
    return UIDatePicker.COMPONENT_TYPE;
  }

  public String getFor() {
    return forComponent;
  }

  @Override
  public void release() {
    super.release();
    forComponent = null;
  }

  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_FOR, forComponent);
  }

}
