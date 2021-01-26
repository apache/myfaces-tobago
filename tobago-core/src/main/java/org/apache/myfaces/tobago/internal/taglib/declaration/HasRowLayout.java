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

public interface HasRowLayout {
  /**
   * <p>
   * This value defines the layout constraints for row layout.
   * It is a space separated list of layout tokens '&lt;n&gt;fr', '&lt;measure&gt;' or the keyword 'auto'.
   * Where &lt;n&gt; is a positive integer and &lt;measure&gt; is a valid CSS length.
   * Example: '2fr 1fr 100px 3rem auto'.
   * </p>
   * <p>
   * Deprecated: The old syntax for "2fr" is "2*". The old name for "auto" is "fixed".
   * </p>
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "1fr")
  void setRows(String rows);
}
