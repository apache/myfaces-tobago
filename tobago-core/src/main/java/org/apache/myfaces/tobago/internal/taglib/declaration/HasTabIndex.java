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

public interface HasTabIndex {

  /**
   * Controls the navigation of the focus through the
   * input controls on a page with the Tab-Key.
   * The navigation starts from the element with
   * the lowest tabIndex value to the element with the highest value.
   * Elements that have identical tabIndex values should be navigated
   * in the order they appear in the character stream
   * Elements that are disabled or with a negative tabIndex
   * do not participate in the tabbing order.
   *
   * @param tabIndex
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer")
  void setTabIndex(String tabIndex);
}
