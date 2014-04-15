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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;

import javax.el.ValueExpression;

/**
 * Add an attribute on the UIComponent
 * associated with the closest parent UIComponent custom action.
 */
@Tag(name = "attribute", bodyContent = BodyContent.EMPTY)
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.AttributeHandler")
public interface AttributeTagDeclaration {

  /**
   * The name of the attribute in the parent component.
   */
  @TagAttribute(required = true, name = "name", type = "java.lang.String")
  public void setName(final ValueExpression name);

  /**
   * The value of the attribute in the parent component.
   */
  @TagAttribute(required = true, name = "value", type = "java.lang.String")
  public  void setValue(final ValueExpression value);
  /**
   * Warning: The mode is only available when using Facelets.
   * Allowed values are "action", "actionListener", "actionFromValue", "isNotSet", "isSet", "valueIfSet".
   * <br/>
   * "action" (method binding) evaluate the expression to find the method binding which is referenced with the template.
   * <br/>
   * "actionListener" same as "action" but for the method signature of ActionListeners.
   * <br/>
   * "isSet" (boolean) checks, if the expression is set from the composition caller.
   * <br/>
   * "isNotSet" (boolean) negation of "isSet"
   * <br/>
   * "actionFromValue" Evaluates the ValueBinding to get an outcome set directly (no action method)
   * <br/>
   * "valueIfSet" set the attribute only if the value is set.
   */
  @TagAttribute(name = "mode")
  public void setMode(final ValueExpression mode);

}
