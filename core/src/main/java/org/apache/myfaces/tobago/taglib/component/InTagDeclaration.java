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

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasDeprecatedWidth;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasSuggestMethod;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsPassword;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

/*
 * Created: Aug 5, 2005 4:53:25 PM
 * $Id$
 */
/**
 * Renders a text input field.
 */
@Tag(name = "in")
@BodyContentDescription(anyTagOf = "facestag")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIInput",
    rendererType = "In")
public interface InTagDeclaration
    extends TextInputTagDeclaration, HasIdBindingAndRendered,
    HasConverter, IsReadonly, IsDisabled, HasDeprecatedWidth,
    IsInline, IsRequired, HasTip, HasLabelAndAccessKey,
    IsPassword, HasSuggestMethod {

  /**
   * Indicate markup of this component.
   * Possible value is 'none'. But this can be overridden in the theme.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none")
  void setMarkup(String markup);
}
