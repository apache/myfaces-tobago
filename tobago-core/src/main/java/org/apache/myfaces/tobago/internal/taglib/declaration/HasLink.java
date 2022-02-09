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

public interface HasLink {
  /**
   * Link to an arbitrary URL, either an internal link or an external link.
   *
   * <dl>
   *   <dt>internal absolute link</dt>
   *   <dd>Starts with a slash '/' character. The context path will be added.
   *       A session id will be added, if needed.</dd>
   *   <dt>external link</dt>
   *   <dd>Starts with protocol followed by a colon ':' character.
   *       The link will not be modified.</dd>
   *   <dt>internal relative link</dt>
   *   <dd>Any other strings. A session id will be added, if needed.</dd>
   * </dl>
   *
   * @param link The external or internal link.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setLink(String link);
}
