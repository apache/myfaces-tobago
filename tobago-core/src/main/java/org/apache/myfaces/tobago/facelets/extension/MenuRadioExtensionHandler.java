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

package org.apache.myfaces.tobago.facelets.extension;

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UISelectOneRadio;

import javax.faces.view.facelets.ComponentConfig;

/**
 * Facelets handler for the <code>&lt;tx:menuRadio></code> extension tag.
 * <pre>
 * &lt;tx:menuRadio>
 *   &lt;tc:selectItems/> &lt;!-- body -->
 * &lt;/tx:menuRadio></pre>
 * is the short form of
 * <pre>
 * &lt;tc:menuCommand>
 *   &lt;f:facet name="radio">
 *     &lt;tc:selectOneRadio>
 *       &lt;tc:selectItems/> &lt;!-- body -->
 *     &lt;/tc:selectOneRadio>
 *   &lt;/f:facet>
 * &lt;/tc:menuCommand></pre>
 */
public class MenuRadioExtensionHandler extends TobagoMenuExtensionHandler {

  public MenuRadioExtensionHandler(final ComponentConfig config) {
    super(config);
  }

  protected String getSubComponentType() {
    return UISelectOneRadio.COMPONENT_TYPE;
  }

  protected String getSubRendererType() {
    return "SelectOneRadio";
  }

  protected String getFacetName() {
    return Facets.RADIO;
  }
  
}
