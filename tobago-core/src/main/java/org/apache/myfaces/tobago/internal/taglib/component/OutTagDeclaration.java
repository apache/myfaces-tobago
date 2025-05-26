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

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Markup;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAutoSpacing;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasDecorationPosition;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasEscape;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasSanitize;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsPlain;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UIOutput;
import java.text.MessageFormat;

/**
 * Renders a text
 */
@Tag(name = "out")
@BodyContentDescription(anyTagOf = "f:converter|f:convertNumber|f:convertDateTime|...")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIOut",
    uiComponentFacesClass = "jakarta.faces.component.UIOutput",
    componentFamily = UIOutput.COMPONENT_FAMILY,
    rendererType = RendererTypes.OUT,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponents = "NONE",
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_PRIMARY,
            description = "Set text color to primary color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SECONDARY,
            description = "Set text color to secondary color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SUCCESS,
            description = "Set text color to success color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_DANGER,
            description = "Set text color to danger color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_WARNING,
            description = "Set text color to warning color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_INFO,
            description = "Set text color to info color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_LIGHT,
            description = "Set text color to light color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_DARK,
            description = "Set text color to dark color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_BOLD,
            description = "Set font to bold."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_THIN,
            description = "Set font to thin."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_ITALIC,
            description = "Set font to italic."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_NUMBER,
            description = "Align text to the right."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_DELETED,
            description = "Strike text through."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_LARGE,
            description = "Set text size to large."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SMALL,
            description = "Set text size to small."
        )
    })

public interface OutTagDeclaration
    extends HasIdBindingAndRendered, HasConverter, HasTip, HasValue, IsVisual, HasEscape,
    HasSanitize, HasLabel, HasLabelLayout, IsPlain, HasAutoSpacing, HasDecorationPosition {

  /**
   * Flag indicating that new-line characters will be converted to HTML &lt;br&gt;.
   * This is useful for pre-formatted plain text.
   * This has only an effect, with escape="true".
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setKeepLineBreaks(String keepLineBreaks);

  /**
   * Indicates that the renderer should create an element in the output language
   * (e.g. an span or div tag around the output text).
   * Use true, if you enable the possibility to apply styles to the output.
   * Use false, if you want to keep the code small (especially inside of sheets).
   *
   * @deprecated Use attribute 'plain' instead ({@link IsPlain#setPlain(String)}).
   */
  @Deprecated(since = "4.0.0", forRemoval = true)
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setCreateSpan(String createSpan);

  /**
   * This attribute is useful if labelLayout=skip is set.
   * Use true, if you want to only render the text (no surrounding tag).
   * Use false, if you enable the possibility to apply styles to the output.
   *
   * @deprecated Use attribute 'plain' instead.
   */
  @Deprecated(since = "4.3.0", forRemoval = true)
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setCompact(String compact);

  /**
   * Activates formatting of the value with the method {@link MessageFormat#format(String, Object...)}
   * A parameters the values of the children of type {@link jakarta.faces.component.UIParameter} are used.
   *
   * @since 5.5.0
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setMessageFormat(String messageFormat);
}
