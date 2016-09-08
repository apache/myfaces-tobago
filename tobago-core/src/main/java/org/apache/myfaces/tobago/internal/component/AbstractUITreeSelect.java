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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Visual;

import javax.el.ValueExpression;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractUITreeSelect extends UISelectBoolean implements Visual, ClientBehaviorHolder {

  // todo generate
  private static final Collection<String> EVENT_NAMES = Arrays.asList("change");

  // todo generate
  @Override
  public String getDefaultEventName() {
    return "change";
  }

  // todo generate
  @Override
  public Collection<String> getEventNames() {
    return EVENT_NAMES;
  }

  @Override
  public void updateModel(final FacesContext context) {
    super.updateModel(context);
  }

  /**
   * The value is stored in the state of the parent UITree, if the value attribute has not a value expression.
   * The value is stored normally, if there is a value expression.
   * @return Is the value stored in the state of the UITree parent object?
   */
  public boolean isValueStoredInState() {
    final ValueExpression valueExpression = getValueExpression(Attributes.value.getName());
    return valueExpression == null;
  }
}
