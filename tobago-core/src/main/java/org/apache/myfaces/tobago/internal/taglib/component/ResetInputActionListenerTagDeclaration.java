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

import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.component.Attributes;

/**
 * A ResetInputActionListener is a declarative way to allow an action source to reset all EditableValueHolder
 * of a page or in a sub-form or part of the component tree.
 */
@Tag(name = "resetInputActionListener")
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.ResetInputActionListenerHandler")
public interface ResetInputActionListenerTagDeclaration {

  /**
   * A list of ids of components. For each id, the surrounding (virtual) UIForm will be searched, and for each of
   * them, all containing EditableValueHolder will be reset.
   */
  @TagAttribute(required = false, name = Attributes.EXECUTE, type = "java.lang.String")
  void setExecute(final javax.el.ValueExpression execute);

}
