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

package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;

/*
 * Date: 02.04.2006
 * Time: 16:20:59
 */

/**
 * Add a child UISelectItem component to the UIComponent
 * associated with the closed parent UIComponent custom
 * action.
 */
@Tag(name = "selectItem", bodyContent = BodyContent.EMPTY)
@UIComponentTag(uiComponent = "org.apache.myfaces.tobago.component.UISelectItem")
public interface SelectItemTagDeclaration extends TobagoTagDeclaration, HasBinding, HasId, HasMarkup {
  /**
   * Flag indicating whether the option created
   * by this component is disabled.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Boolean"}, defaultValue = "false")
  void setItemDescription(String itemDescription);

  /**
   * Flag indicating whether the option created
   * by this component is disabled.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Boolean"}, defaultValue = "false")
  void setItemDisabled(String itemDisabled);

  /**
   * Label to be displayed to the user for this option.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setItemLabel(String itemLabel);

  /**
   * Value to be returned to the server if this option is selected by the user.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setItemValue(String itemValue);

  /**
   * Value binding expression pointing at a SelectItem instance containing
   * the information for this option.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.model.SelectItem")
  void setValue(String value);

  /**
   * Image to be displayed to the user for this option.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setItemImage(String itemImage);
}
