/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.UILabeledInputLayout;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jun 20, 2005
 * Time: 6:44:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Tag(name="labeledInputLayout", bodyContent=BodyContent.EMPTY)
public class LabeledInputLayoutTag extends TobagoTag {

  private String layout;

  private String layoutOrder;




  public String getComponentType() {
    return UILabeledInputLayout.COMPONENT_TYPE;
  }

  public String getLayout() {
    return layout;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(
        component, ATTR_COLUMNS, layout, getIterationHelper());
  }

  /**
   * LayoutConstraints for width layout.
   * Semicolon separated list of tree layout tokens ('*', '&lt;x>*', '&lt;x>px' or '&lt;x>%').
   * Where '*' is equvalent to '1*'.
   */
  @TagAttribute()
  @UIComponentTagAttribute()
  public void setLayout(String layout) {
    this.layout = layout;
  }

  public String getLayoutOrder() {
    return layoutOrder;
  }

  /**
   * layout order description.
   * default in standard theme is "LCP" -> label left, component middle and picker at right
   * anny combinations of this three letters are valid.
   */
//  @TagAttribute()
//  @UIComponentTagAttribute()
  public void setLayoutOrder(String layoutOrder) {
    this.layoutOrder = layoutOrder;
  }
}
