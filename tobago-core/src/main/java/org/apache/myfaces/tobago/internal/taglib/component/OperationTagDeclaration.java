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

import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;

/**
 * An operation describes an Tobago command, which will usually executed on client side.
 *
 * ********************* &lt;br/&gt;
 * WARNING &lt;br/&gt;
 * ********************* &lt;br/&gt;
 * This feature is preliminary and may be subject of change in later versions!  &lt;br/&gt;
 */
@Preliminary
@Tag(name = "operation")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIOperation",
    componentFamily = "org.apache.myfaces.tobago.Operation",
    rendererType = RendererTypes.OPERATION,
    allowedChildComponenents = "NONE")
public interface OperationTagDeclaration {

  /**
   * Name of the operation to be executed.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  void setName(String name);

  /**
   * The id of the component the operation is related to.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  void setFor(String forAttribute);

}
