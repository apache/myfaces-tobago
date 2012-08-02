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
 * Date: 10.02.2006
 * Time: 22:20:01
 */

/**
 * Renders a layout cell.
 * A panel with ability to span over more than one layout cells.
 */
@Tag(name = "cell")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UICell",
    rendererType = "Panel")
public interface CellTagDeclaration extends TobagoBodyTagDeclaration,
    HasIdBindingAndRendered {

  /**
   * Count of layout column's to span over.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Integer"},
      defaultValue = "1")
  void setSpanX(String spanX);

  /**
   * Count of layout row's to span over.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Integer"},
      defaultValue = "1")
  void setSpanY(String spanY);

  /**
   * possible values are:
   * 'false' : no scrollbars should rendered
   * 'true'  : scrollbars should always rendered
   * 'auto'  : scrollbars should rendered when needed
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "false",
      allowedValues = {"false", "true", "auto"})
  void setScrollbars(String scrollbars);
}
