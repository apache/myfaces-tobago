package org.apache.myfaces.tobago.taglib.decl;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
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

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 18.04.2006
 * Time: 11:39:26
 * To change this template use File | Settings | File Templates.
 */
public interface HasValueChangeListener {
  /**
   * MethodBinding representing a value change listener method
   * that will be notified when a new value has been set for this input component.
   * The expression must evaluate to a public method that takes a ValueChangeEvent
   * parameter, with a return type of void.
   * @param valueChangeListener
   */
  @TagAttribute
  @UIComponentTagAttribute(expression = DynamicExpression.METHOD_BINDING)
  void setValueChangeListener(String valueChangeListener);
}
