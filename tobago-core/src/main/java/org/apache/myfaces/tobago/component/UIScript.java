package org.apache.myfaces.tobago.component;

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

import org.apache.myfaces.tobago.internal.component.AbstractUIScript;

/**
 * @deprecated Since Tobago 3.1.0. The tag &lt;tc:script&gt; is using a handler
 * now: {@link org.apache.myfaces.tobago.facelets.ScriptHandler}.
 */
@Deprecated
public class UIScript
    extends AbstractUIScript {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Script";

  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.Script";

  enum PropertyKeys {
    file,
  }

  public String getFamily() {
    return COMPONENT_FAMILY;
  }

  /**
   * File name to include into the rendered page. The name must be full qualified, or relative.
   * If using a complete path from root, you'll need to add the contextPath from the web application.
   * This can be done with the EL #{request.contextPath}.
   */
  public java.lang.String getFile() {
    return (java.lang.String) getStateHelper().eval(PropertyKeys.file);
  }

  public void setFile(java.lang.String file) {
    getStateHelper().put(PropertyKeys.file, file);
  }
}
