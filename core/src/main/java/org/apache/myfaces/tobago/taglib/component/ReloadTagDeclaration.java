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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

/*
 * Date: 28.05.2006
 * Time: 22:31:38
 */

/**
 * Update the parent component
 */

@Tag(name = "reload")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIReload")
public interface ReloadTagDeclaration extends TobagoTagDeclaration, HasIdBindingAndRendered {


  /**
   * Time in milliseconds after which the parent component is automaticaly reloaded.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "5000")
  void setFrequency(String frequency);

  /**
   * Is update required.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "true")
  void setUpdate(String update);

  /**
   * Flag indicating that
   * the update check should be performed
   * immediately (that is, during Apply Request Values phase) rather than
   * waiting until Render Response phase.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "false")
  void setImmediate(String immediate);
}
