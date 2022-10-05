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

import org.apache.myfaces.tobago.component.SupportsAccessKey;

import jakarta.el.ValueExpression;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.LabelTagDeclaration}
 */
public abstract class AbstractUILabel
    extends AbstractUILabelBase implements SupportsAccessKey {

  /*
   * Need to set the name to 'forComponent' which comes from UILabel.PropertyKeys.forComponent.
   * TODO a better way would be to improve the UILabel.PropertyKeys, so an exact string could be specified.
   * As an example, look at: jakarta.faces.component.UIMessages
   */
  @Override
  public void setValueExpression(String name, ValueExpression expression) {
    if ("for".equals(name)) {
      super.setValueExpression("forComponent", expression);
    } else {
      super.setValueExpression(name, expression);
    }
  }
}
