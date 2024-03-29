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

import jakarta.faces.component.behavior.AjaxBehavior;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectManyList;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class SelectManyListRendererUnitTest extends RendererTestBase {

  @Test
  public void ajax() throws IOException {
    final UISelectManyList c = (UISelectManyList) ComponentUtils.createComponent(
        facesContext, Tags.selectManyList.componentType(), RendererTypes.SelectManyList, "id");
    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Stratocaster");
    i1.setItemValue("Stratocaster");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Telecaster");
    i2.setItemValue("Telecaster");
    c.getChildren().add(i2);
    final AjaxBehavior behavior =
        (AjaxBehavior) facesContext.getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
    behavior.setExecute(Arrays.asList("textarea"));
    behavior.setRender(Arrays.asList("panel"));
    c.addClientBehavior("change", behavior);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyList/selectManyListAjax.html"), formattedResult());
  }
}
