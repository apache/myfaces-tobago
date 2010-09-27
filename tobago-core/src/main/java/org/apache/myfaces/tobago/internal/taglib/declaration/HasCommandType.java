package org.apache.myfaces.tobago.internal.taglib.declaration;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;

@Deprecated
public interface HasCommandType {
  /**
   * <p>
   * Type of command component to create. Valid values are 'navigate', 'reset',
   * 'script' or 'submit'.
   * If not specified, or not a valid value,
   * the default value is 'submit' is used.
   * </p>
   * <p>
   * Deprecation info: 
   * <ul>
   * <li>Instead of 'navigate' please use the link attribute.</li>
   * <li>Instead of 'script' please use the onclick attribute.</li>
   * <li>'submit' is the default, so you can omit it.</li>
   * <li>'reset' should not be used, but can emulated by the application (e.g. cancel button).</li>
   * </ul>
   * </p>
   * @deprecated
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = AbstractUICommandBase.COMMAND_TYPE_SUBMIT)
  @Deprecated
  void setType(String type);
}
