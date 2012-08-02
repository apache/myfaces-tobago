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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.util.BundleMapWrapper;

import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Map;

/**
 * Load a resource bundle localized for the Locale of the current view
 * from the tobago resource path, and expose it (as a Map) in the request
 * attributes of the current request.
 */
@Tag(name = "loadBundle", bodyContent = BodyContent.EMPTY)
public class LoadBundleTag extends TagSupport {

  private static final long serialVersionUID = 4949984721486410191L;

  private String basename;
  private String var;

  public int doStartTag() throws JspException {

    String bundleBaseName;
    FacesContext context = FacesContext.getCurrentInstance();
    if (UIComponentTag.isValueReference(basename)) {
      bundleBaseName = (String) context.getApplication().createValueBinding(basename).getValue(context);
    } else {
      bundleBaseName = basename;
    }
    Map toStore = new BundleMapWrapper(bundleBaseName);
    // TODO find a better way
    context.getExternalContext().getSessionMap().put(var, toStore);
//        .getRequestMap().put(var, toStore);

    return EVAL_BODY_INCLUDE;
  }

  public void release() {
    basename = null;
    var = null;
  }

  public String getBasename() {
    return basename;
  }

  /**
   * Base name of the resource bundle to be loaded.
   */
  @TagAttribute(required = true)
  public void setBasename(String basename) {
    this.basename = basename;
  }

  public String getVar() {
    return var;
  }

  /**
   * Name of a session-scope attribute under which the bundle data
   * will be exposed.
   */
  @TagAttribute(required = true)
  public void setVar(String var) {
    this.var = var;
  }

}

