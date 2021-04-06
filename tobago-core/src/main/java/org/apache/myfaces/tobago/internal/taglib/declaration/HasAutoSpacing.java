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

public interface HasAutoSpacing {
  /**
   * Automatically add spacing (margins/paddings) to the component for better positioning.
   * Default is 'true' except the component is inside a:
   * - header
   * - footer
   * - bar
   * - sheet
   * - tree
   * - link group
   * - button group
   * - before facet
   * - after facet
   * - label facet
   * - bar facet
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean")
  void setAutoSpacing(String autoSpacing);
}
