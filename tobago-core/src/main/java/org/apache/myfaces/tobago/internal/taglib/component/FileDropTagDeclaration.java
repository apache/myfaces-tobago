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

import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAction;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasActionListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasOnchange;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsMultiple;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;

import javax.faces.component.UIInput;

/**
 * Renders a file drop component. This component always makes a partial (ajax) upload!
 * <p/>
 * For content constraints please use <a href="validateFileItem.html">tc:validateFileItem</a>.
 */
@Tag(name = "fileDrop")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIFileDrop",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIFileDrop",
    uiComponentFacesClass = "javax.faces.component.UIInput",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = RendererTypes.FILE_DROP,
    allowedChildComponenents = "NONE")
public interface FileDropTagDeclaration
    extends HasValidator, HasValidatorMessage, HasRequiredMessage, HasConverterMessage, HasOnchange,
    HasValueChangeListener, HasIdBindingAndRendered, IsDisabled, HasMarkup, HasCurrentMarkup, IsFocus, IsMultiple,
    HasLabel, HasImage, HasTip, IsReadonly, IsRequired, HasTabIndex, IsGridLayoutComponent,
    HasAction, HasActionListener {

  /**
   * Value binding expression pointing to a
   * <code>javax.servlet.http.Part</code> property to store the
   * uploaded file.
   */
  @TagAttribute()
  @UIComponentTagAttribute(
      type = {"javax.servlet.http.Part", "org.apache.commons.fileupload.FileItem[]"},
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED)
  void setValue(String value);

  /**
   * <p>
   * Indicate the partially rendered components in a case of a file drop.
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
   *  As default the dropZoneId is used.
   * </p>
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.String[]")
  void setRenderedPartially(String componentIds);

  /**
   * <p>
   * Indicate the highlighted file drop area.
   * </p>
   * <p>
   * You should use:
   *   <ul>
   *     <li>a clientId (absolute or relative see setRenderedPartially)</li>
   *     <li>@this to use only the layouted space of this tc:fileDrop</li>
   *     <li>@parent to use the layouted space of the next parent UIComponent</li>
   *     <li>@panel to use the layouted space of the next parent tc:panel</li>
   *   </ul>
   * </p>
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.String")
  void setDropZoneId(String dropZoneId);

  /**
   * <p>
   * Indicate visible component style.
   * </p>
   * <p>
   * Possible values:
   *   <ul>
   *     <li>DROP_ZONE</li>
   *     <li>FILE</li>
   *     <li>BUTTON</li>
   *     <li>LINK</li>
   *     <li>NONE</li>
   *   </ul>
   * </p>
   * <p>
   *   Default is DROP_ZONE.
   * </p>
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.String")
  void setVisibleType(String visibleType);
}
