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
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;

import javax.faces.view.facelets.ComponentConfig;

/**
 * Facelets handler for the <code>&lt;tx:menuCheckbox&gt;</code> extension tag.
 * <pre>
 * &lt;tx:menuCheckbox/&gt;</pre>
 * is the short form of
 * <pre>
 * &lt;tc:menuCommand&gt;
 *   &lt;f:facet name="checkbox"&gt;
 *     &lt;tc:selectBooleanCheckbox/&gt;
 *   &lt;/f:facet&gt;
 * &lt;/tc:menuCommand&gt;</pre>
 *
 * @deprecated since Tobago 3.0. The tx-library is deprecated, please use the tc-library.
 */
@Deprecated
public class MenuCheckboxExtensionHandler extends TobagoMenuExtensionHandler {

  public MenuCheckboxExtensionHandler(final ComponentConfig config) {
    super(config);
  }

  @Override
  protected String getSubComponentType() {
    return UISelectBooleanCheckbox.COMPONENT_TYPE;
  }

  @Override
  protected String getSubRendererType() {
    return "SelectBooleanCheckbox";
  }

  @Override
  protected String getFacetName() {
    return Facets.CHECKBOX;
  }
}
