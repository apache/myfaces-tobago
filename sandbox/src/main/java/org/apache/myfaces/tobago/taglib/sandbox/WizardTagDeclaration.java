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

package org.apache.myfaces.tobago.taglib.sandbox;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

/**
 * Renders a fexible Wizard.
 */
@Tag(name = "wizard")
@BodyContentDescription(anyTagOf = "facestag")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIWizard",
    rendererType = "Wizard")
public interface WizardTagDeclaration extends HasIdBindingAndRendered {

  @TagAttribute(required = true)
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.model.Wizard",
      expression = DynamicExpression.VALUE_BINDING)
  void setController(String controller);

  /**
   * Name of a request-scope attribute under which the model data will be exposed.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute(expression = DynamicExpression.NONE)
  void setVar(String var);

  /**
   * Outcome to navigate to this page.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute(expression = DynamicExpression.NONE)
  void setOutcome(String outcome);

  /**
   * Title of this page
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setTitle(String title);

  /**
   * Is a jump forward to folling pages allowed?
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean")
  void setAllowJumpForward(String allowJumpForward);
}

