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

import com.sun.facelets.tag.jsf.ComponentConfig;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;

/**
 * Facelets handler for the <code>&lt;tx:menuCheckbox></code> extension tag.
 * <pre>
 * &lt;tx:menuCheckbox/></pre>
 * is the short form of
 * <pre>
 * &lt;tc:menuCommand>
 *   &lt;f:facet name="checkbox">
 *     &lt;tc:selectBooleanCheckbox/>
 *   &lt;/f:facet>
 * &lt;/tc:menuCommand></pre>
 */
public class MenuCheckboxExtensionHandler extends TobagoMenuExtensionHandler {

  public MenuCheckboxExtensionHandler(ComponentConfig config) {
    super(config);
  }

  protected String getSubComponentType() {
    return UISelectBooleanCheckbox.COMPONENT_TYPE;
  }

  protected String getSubRendererType() {
    return "SelectBooleanCheckbox";
  }

  protected String getFacetName() {
    return Facets.CHECKBOX;
  }
}
