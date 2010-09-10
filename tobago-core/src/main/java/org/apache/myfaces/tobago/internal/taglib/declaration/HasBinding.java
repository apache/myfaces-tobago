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

import javax.servlet.jsp.JspException;

public interface HasBinding {
  /**
   * The value binding expression linking this component to a property in a backing bean.
   * <p/>
   * Warning: For the tobago extension library <b>tx</b> the binding differs from JSP and Facelets:
   * <ul>
   * <li>JSP: The component is the inner main control (e. g. UIInput).</li>
   * <li>Facelets: The component is the outer UIPanel.</li>
   * </ul>
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.component.UIComponent")
  void setBinding(String binding) throws JspException;
}
