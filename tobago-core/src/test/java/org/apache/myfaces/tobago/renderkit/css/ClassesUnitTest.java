package org.apache.myfaces.tobago.renderkit.css;

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

import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.CreateComponentUtils;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.mock.faces.AbstractTobagoTestBase;
import org.junit.Assert;
import org.junit.Test;

public class ClassesUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testSimple() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    Assert.assertEquals("tobago-in-simple", Classes.simple(in, "simple").getStringValue());
  }

  @Test
  public void testFull() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    Assert.assertEquals("tobago-in", Classes.full(in).getStringValue());
  }

  @Test
  public void testDisabled() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setDisabled(true);
    Assert.assertEquals("tobago-in tobago-in-disabled", Classes.full(in).getStringValue());
  }

  @Test
  public void testReadonly() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setReadonly(true);
    Assert.assertEquals("tobago-in tobago-in-readonly", Classes.full(in).getStringValue());
  }

  @Test
  public void testDisabledReadonly() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setDisabled(true);
    in.setReadonly(true);
    Assert.assertEquals("tobago-in tobago-in-disabled tobago-in-readonly", Classes.full(in).getStringValue());
  }

  @Test
  public void testError() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setValid(false);
    Assert.assertEquals("tobago-in tobago-in-error", Classes.full(in).getStringValue());
  }

  @Test
  public void testMarkup() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setMarkup(Markup.valueOf("important"));
    Assert.assertEquals("tobago-in tobago-in-markup-important", Classes.full(in).getStringValue());
  }

  @Test
  public void testSub() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    Assert.assertEquals("tobago-in tobago-in-sub", Classes.full(in, "sub").getStringValue());
  }

  @Test
  public void testSubs() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    Assert.assertEquals("tobago-in tobago-in-sub1 tobago-in-sub2",
        Classes.full(in, new String[]{"sub1", "sub2"}).getStringValue());
  }

  @Test
  public void testMixed() {
    final UIInput in = (UIInput) CreateComponentUtils.createComponent(
        getFacesContext(), ComponentTypes.IN, RendererTypes.IN, "in");
    in.setDisabled(true);
    in.setReadonly(true);
    in.setValid(false);
    in.setMarkup(Markup.valueOf("important,deleted"));
    Assert.assertEquals("tobago-in tobago-in-disabled tobago-in-readonly tobago-in-error "
        + "tobago-in-markup-important tobago-in-markup-deleted tobago-in-sub",
        Classes.full(in, "sub").getStringValue());
  }

}
