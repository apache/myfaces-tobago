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
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.util.BundleMapWrapper;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Map;

/**
 * Load a resource bundle localized for the locale of the current view
 * from the tobago resource path, and expose it (as a Map) in the session
 * attributes (session scope is needed to support ajax requests).
 * <p />
 * The main difference to the JSF tag f:localBundle is the support of Tobago themes and
 * the XML formal for properties files.
 * <p />
 * Since JSF 1.2 it is possible to use a {@link org.apache.myfaces.tobago.context.TobagoBundle}
 * and configure it in the faces-config.xml.
 */
@Tag(name = "loadBundle", bodyContent = BodyContent.EMPTY)
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.LoadBundleTag")
public abstract class LoadBundleTag extends TagSupport {

  private static final long serialVersionUID = 4949984721486410191L;
  /**
   * Base name of the resource bundle to be loaded.
   */
  @TagAttribute(required = true, name = "basename")
  public abstract String getBasenameValue();

  /**
   * Name of a session-scope attribute under which the bundle data
   * will be exposed.
   */
  @TagAttribute(required = true, name = "var")
  public abstract String getVarValue();

  public int doStartTag() throws JspException {

    FacesContext context = FacesContext.getCurrentInstance();

    Map toStore = new BundleMapWrapper(getBasenameValue());
    // (session scope is needed to support ajax requests)
    context.getExternalContext().getSessionMap().put(getVarValue(), toStore);

    return EVAL_BODY_INCLUDE;
  }
}
