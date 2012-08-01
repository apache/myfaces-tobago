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

package org.apache.myfaces.tobago.renderkit.css;

import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.mock.faces.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.faces.component.UIComponent;

public class ClassesUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testSimple() {
    final UIIn in = (UIIn) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    Assert.assertEquals("tobago-in-simple", Classes.create(in, "simple").getStringValue());
  }

  @Test
  public void testFull() {
    final UIIn in = (UIIn) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    Assert.assertEquals("tobago-in", Classes.create(in).getStringValue());
  }

  @Test
  public void testDisabled() {
    final UIIn in = (UIIn) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setDisabled(true);
    updateCurrentMarkup(in);
    Assert.assertEquals("tobago-in tobago-in-markup-disabled", Classes.create(in).getStringValue());
  }

  @Test
  public void testReadonly() {
    final UIIn in = (UIIn) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setReadonly(true);
    updateCurrentMarkup(in);
    Assert.assertEquals("tobago-in tobago-in-markup-readonly", Classes.create(in).getStringValue());
  }

  @Test
  public void testDisabledReadonly() {
    final UIIn in = (UIIn) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setDisabled(true);
    in.setReadonly(true);
    updateCurrentMarkup(in);
    Assert.assertEquals(
        "tobago-in tobago-in-markup-disabled tobago-in-markup-readonly", Classes.create(in).getStringValue());
  }

  @Test
  public void testError() {
    final UIIn in = (UIIn) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setValid(false);
    updateCurrentMarkup(in);
    Assert.assertEquals("tobago-in tobago-in-markup-error", Classes.create(in).getStringValue());
  }

  @Test
  public void testMarkup() {
    final UIIn in = (UIIn) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setMarkup(Markup.valueOf("important"));
    updateCurrentMarkup(in);
    Assert.assertEquals("tobago-in tobago-in-markup-important", Classes.create(in).getStringValue());
  }

  @Test
  public void testSub() {
    final UIIn in = (UIIn) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    Assert.assertEquals("tobago-in-sub", Classes.create(in, "sub").getStringValue());
  }

  @Test
  public void testMixed() {
    final UIIn in = (UIIn) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setDisabled(true);
    in.setReadonly(true);
    in.setValid(false);
    in.setMarkup(Markup.valueOf("important,deleted"));
    updateCurrentMarkup(in);
    Assert.assertEquals("tobago-in-sub tobago-in-sub-markup-important tobago-in-sub-markup-deleted "
        + "tobago-in-sub-markup-disabled tobago-in-sub-markup-readonly tobago-in-sub-markup-error",
        Classes.create(in, "sub").getStringValue());
  }

  private void updateCurrentMarkup(UIComponent component) {
    SupportsMarkup m = (SupportsMarkup) component;
    m.setCurrentMarkup(ComponentUtils.updateMarkup(component, m.getMarkup()));
  }

}
