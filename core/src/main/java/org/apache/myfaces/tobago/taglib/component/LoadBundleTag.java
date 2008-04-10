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
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.util.BundleMapWrapper;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Map;

/**
 * Load a resource bundle localized for the Locale of the current view
 * from the tobago resource path, and expose it (as a Map) in the request
 * attributes of the current request.
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
    // TODO find a better way
    context.getExternalContext().getSessionMap().put(getVarValue(), toStore);
//        .getRequestMap().put(var, toStore);

    return EVAL_BODY_INCLUDE;
  }

}

