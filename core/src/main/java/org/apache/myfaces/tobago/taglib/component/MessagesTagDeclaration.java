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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.AbstractUIMessages;
import org.apache.myfaces.tobago.taglib.decl.HasFor;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

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
    uiComponentBaseClass = "org.apache.myfaces.tobago.component.AbstractUIMessages",
    rendererType = "Messages",
    allowedChildComponenents = "NONE")

public interface MessagesTagDeclaration extends HasIdBindingAndRendered, HasFor {

  /**
   * Flag indicating that only messages that are not associated to any
   * particular UIComponent should be displayed. That are messages without clientId.
   * The default is "false".
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setGlobalOnly(String globalOnly);

  /**
   * Flag indicating whether the detail should be included
   * The default is "false".
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "false")
  void setShowDetail(String showDetail);

  /**
   * Flag indicating whether the summary should be included
   * The default is "true".
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "true")
  void setShowSummary(String showSummary);

   /**
   * Sets the mininum severity to be shown. E. g. "warn" shows only "warn", "error" and "fatal".
   * The default is "info".
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.application.FacesMessage$Severity", defaultValue = "info",
      defaultCode = "javax.faces.application.FacesMessage.SEVERITY_INFO")
  void setMinSeverity(String minSeverity);

   /**
   * Sets the maximum severity to be shown. E. g. "warn" shows only "warn" and "info".
    * When setting this attribute you usually shoud take care, that you have a second message tag to show the
    * higher severity levels.
   * The default is "fatal".
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.application.FacesMessage$Severity", defaultValue = "fatal",
      defaultCode = "javax.faces.application.FacesMessage.SEVERITY_FATAL")
  void setMaxSeverity(String maxSeverity);

   /**
   * Sets the maximum number of messages to show.
   * The default is 2147483647 (more or less unlimited).
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "2147483647")
  void setMaxNumber(String maxNumber);

   /**
   * Sets the order of the messages.
   * The default "occurence".
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.component.UIMessages$OrderBy",
      allowedValues = {AbstractUIMessages.OrderBy.OCCURENCE_STRING, AbstractUIMessages.OrderBy.SEVERITY_STRING},
      defaultValue = AbstractUIMessages.OrderBy.OCCURENCE_STRING, 
      defaultCode = "org.apache.myfaces.tobago.component.AbstractUIMessages.OrderBy.OCCURENCE" )
  void setOrderBy(String orderBy);
}
