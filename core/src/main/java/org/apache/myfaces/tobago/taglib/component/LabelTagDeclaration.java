package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.taglib.decl.HasFor;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabelWithAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.IsGridLayoutComponent;
import org.apache.myfaces.tobago.taglib.decl.IsInline;

/**
 * Renders a label component.
 */
@Tag(name = "label")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UILabel",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUILabel",
    rendererType = RendererTypes.LABEL,
    allowedChildComponenents = "NONE")
public interface LabelTagDeclaration extends HasIdBindingAndRendered,
    HasLabelWithAccessKey, HasFor, IsInline, HasTip, HasMarkup, IsGridLayoutComponent {

  /**
   * Text value to display as label. Overwritten by 'labelWithAccessKey'
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setValue(String value);

}
