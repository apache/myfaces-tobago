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

package org.apache.myfaces.tobago.internal.taglib.declaration;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

public interface IsPlain {

  /**
   * <p><b>Use with caution: Only for special cases!</b></p>
   *
   * <p>This attribute is useful for tc:out if labelLayout=skip is set.
   * Use true, if you want to only render the text (no surrounding tag).
   * Use false, if you enable the possibility to apply styles to the output.</p>
   *
   * <p>For tc:form there will no div tag be rendered. So, the content
   * can be used freely from layout managers.</p>
   *
   * <p>But, no AJAX is possible for components with "plain" set,
   * because there is no client element with an "id" in the DOM.</p>
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setPlain(String plain);
}
