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
import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.el.ValueExpression;

/**
 * <p>
 * Load a resource bundle localized for the locale of the current view
 * from the tobago resource path, and expose it (as a Map) in the session
 * attributes (session scope is needed to support ajax requests).
 * </p>
 * <p>
 * The main difference to the JSF tag f:localBundle is the support of Tobago themes and
 * the XML formal for properties files.
 * </p>
 * <p>
 * Since JSF 1.2 it is possible to use a {@link org.apache.myfaces.tobago.context.TobagoBundle}
 * and configure it in the faces-config.xml.
 * </p>
 */
@Tag(name = "loadBundle")
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.LoadBundleHandler")
public interface LoadBundleTagDeclaration {

  /**
   * Base name of the resource bundle to be loaded.
   */
  @TagAttribute(required = true, name = "basename", type = "java.lang.String")
  void setBasename(final ValueExpression basename);

  /**
   * Name of a session-scope attribute under which the bundle data
   * will be exposed.
   */
  @TagAttribute(required = true, name = "var")
  @UIComponentTagAttribute(expression = DynamicExpression.PROHIBITED)
  void setVar(final String var);

}
