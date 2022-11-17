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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UISelectBooleanToggle;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SelectBooleanToggleRendererUnitTest extends RendererTestBase {

  @Test
  public void label() throws IOException {
    final UISelectBooleanToggle c = (UISelectBooleanToggle) ComponentUtils.createComponent(
        facesContext, Tags.selectBooleanToggle.componentType(), RendererTypes.SelectBooleanToggle, "id");
    c.setLabel("label");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectBooleanToggle/selectBooleanToggleLabel.html"),
        formattedResult());
  }

  @Test
  public void itemLabel() throws IOException {
    final UISelectBooleanToggle c = (UISelectBooleanToggle) ComponentUtils.createComponent(
        facesContext, Tags.selectBooleanToggle.componentType(), RendererTypes.SelectBooleanToggle, "id");
    c.setItemLabel("label");

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectBooleanToggle/selectBooleanToggleItemLabel.html"),
        formattedResult());
  }
}
