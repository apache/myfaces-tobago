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

public interface HasMarkup {
  
  /**
   * Indicate markup of this component.
   * The allowed markups can be defined or overridden in the theme.
   * The value 'none' should not be used any longer. Just leave the attribute empty, or use a NULL pointer. 
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.context.Markup")
  void setMarkup(String markup);

  /**
   * Internal markup. Is same as markup plus additional values like "required" or "error".
   * TODO: this property may be transient!
   */
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.context.Markup",
      defaultCode = "getMarkup()")
  void setCurrentMarkup(String markup);
}
