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
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;

import javax.el.ValueExpression;

/**
 * Add an data attribute on the UIComponent
 * associated with the closest parent UIComponent custom action.
 * Data attributes will be passed through the renderers into the DOM of the user agent and
 * can be used by scripts.
 */
@Preliminary
@Tag(name = "dataAttribute", bodyContent = BodyContent.EMPTY)
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.DataAttributeHandler")
public interface DataAttributeTagDeclaration {

  /**
   * The name of the attribute in the parent component.
   */
  @TagAttribute(required = true, name = "name", type = "java.lang.String")
  void setName(final ValueExpression name);

  /**
   * The value of the attribute in the parent component.
   */
  @TagAttribute(required = true, name = "value", type = "java.lang.String")
  void setValue(final ValueExpression value);
}
