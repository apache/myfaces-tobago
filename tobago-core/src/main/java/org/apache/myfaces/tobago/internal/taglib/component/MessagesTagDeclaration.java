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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUIMessages;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasFor;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;

/*
 * Date: 02.04.2006
 * Time: 15:53:45
 */

/**
 * Renders error/validation messages.
 */
@Tag(name = "messages", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIMessages",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIMessages",
    rendererType = RendererTypes.MESSAGES,
    allowedChildComponenents = "NONE")

public interface MessagesTagDeclaration
    extends HasIdBindingAndRendered, HasFor, IsGridLayoutComponent, HasMarkup, HasCurrentMarkup {

  /**
   * Flag indicating that only messages that are not associated to any
   * particular UIComponent should be displayed. That are messages without clientId.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "false")
  void setGlobalOnly(String globalOnly);

  /**
   * Flag indicating whether the detail should be included
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setShowDetail(String showDetail);

  /**
   * Flag indicating whether the summary should be included
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setShowSummary(String showSummary);

   /**
   * Sets the mininum severity to be shown. E. g. "warn" shows only "warn", "error" and "fatal".
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.application.FacesMessage$Severity", defaultValue = "info",
      defaultCode = "javax.faces.application.FacesMessage.SEVERITY_INFO")
  void setMinSeverity(String minSeverity);

   /**
   * Sets the maximum severity to be shown. E. g. "warn" shows only "warn" and "info".
    * When setting this attribute you usually shoud take care, that you have a second message tag to show the
    * higher severity levels.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.application.FacesMessage$Severity", defaultValue = "fatal",
      defaultCode = "javax.faces.application.FacesMessage.SEVERITY_FATAL")
  void setMaxSeverity(String maxSeverity);

   /**
   * Sets the maximum number of messages to show.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "2147483647")
  void setMaxNumber(String maxNumber);

   /**
   * Sets the order of the messages.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.component.UIMessages$OrderBy",
      allowedValues = {AbstractUIMessages.OrderBy.OCCURRENCE_STRING, AbstractUIMessages.OrderBy.SEVERITY_STRING},
      defaultValue = AbstractUIMessages.OrderBy.OCCURRENCE_STRING, 
      defaultCode = "org.apache.myfaces.tobago.internal.component.AbstractUIMessages.OrderBy.OCCURRENCE")
  void setOrderBy(String orderBy);

  /**
   * Has the user to confirm this message?
   * This attributes handles the case, if the application wants to warn the user about a problem,
   * and the user has to confirm the message before he/she can continue.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setConfirmation(String confirmation);

}
