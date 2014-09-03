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


public interface HasRenderedPartially {
  /**
   * <p>
   * Indicate the partially rendered components in a case of a submit.
   * </p>
   * <p>
   * The search depends on the number of prefixed colons in the relativeId:
   * <dl>
   *   <dd>number of prefixed colons == 0</dd>
   *   <dt>fully relative</dt>
   *   <dd>number of prefixed colons == 1</dd>
   *   <dt>absolute (still normal findComponent syntax)</dt>
   *   <dd>number of prefixed colons == 2</dd>
   *   <dt>search in the current naming container (same as 0 colons)</dt>
   *   <dd>number of prefixed colons == 3</dd>
   *   <dt>search in the parent naming container of the current naming container</dt>
   *   <dd>number of prefixed colons > 3</dd>
   *   <dt>go to the next parent naming container for each additional colon</dt>
   * </dl>
   * </p>
   * <p>
   * If a literal is specified: to use more than one identifier the identifiers must be space delimited.
   * </p>
   * <p>
   * Using this in a UISheet component this list indicates components to update when calling
   * internal AJAX requests like sort or paging commands.
   * Don't forget to add the sheets id in that case, if needed.
   * </p>
   */
   @TagAttribute
   @UIComponentTagAttribute(type = "java.lang.String[]")
   void setRenderedPartially(String componentIds);
}
