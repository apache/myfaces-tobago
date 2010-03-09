package org.apache.myfaces.tobago.taglib.decl;

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

/**
 * This interface is useful for migration. The width and height attributes can be set
 * in Tobago 1.0. With this interface it can be set for compatibility.
 *
 * @see IsGridLayoutComponent
 */
@Deprecated
public interface IsGridLayoutComponentWithDeprecatedDimension extends IsGridLayoutComponentBase {

  /**
   * This value will usually be set by the layout manager.
   *
   * @param width The width for this component.
   * @deprecated
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((LayoutComponentRenderer)getRenderer(getFacesContext())).getWidth(getFacesContext(), this)")
  void setWidth(String width);

  /**
   * This value will usually be set by the layout manager.
   *
   * @param height The height for this component.
   * @deprecated
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((LayoutComponentRenderer)getRenderer(getFacesContext())).getHeight(getFacesContext(), this)")
  void setHeight(String height);

}
