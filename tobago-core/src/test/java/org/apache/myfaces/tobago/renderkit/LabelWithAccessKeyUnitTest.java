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

package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.Assert;
import org.junit.Test;

public class LabelWithAccessKeyUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testSimple() {
    final UILabel component = new UILabel();
    component.setValue("Save");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testWithKeyFirstLetter() {
    final UILabel component = new UILabel();
    component.setValue("Save_");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertNull(label.getAccessKey());
  }

  @Test
  public void testWithKeyLastLetter() {
    final UILabel component = new UILabel();
    component.setValue("A__n__a_ly__ze");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("A_n_aly_ze", label.getLabel());
    Assert.assertEquals(5, label.getPos());
    Assert.assertEquals(Character.valueOf('l'), label.getAccessKey());
  }

  @Test
  public void testAmpersand() {
    final UILabel component = new UILabel();
    component.setValue("_Save");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(0, label.getPos());
    Assert.assertEquals(Character.valueOf('s'), label.getAccessKey());
  }

  @Test
  public void testAmpersandAtEnd() {
    final UILabel component = new UILabel();
    component.setValue("Save_");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testUmlauts() {
    // Umlauts are not supported as accessKey
    final UILabel component = new UILabel();
    component.setValue("Löschen");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Löschen", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testUmlauts2() {
    // Umlauts are not supported as accessKey
    final UILabel component = new UILabel();
    component.setValue("L_öschen");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Löschen", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testKey() {
    final UILabel component = new UILabel();
    component.setValue("Save");
    component.setAccessKey('a');
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(1, label.getPos());
    Assert.assertEquals((Character) 'a', label.getAccessKey());
  }

  @Test
  public void testKeyWithWongCase() {
    final UILabel component = new UILabel();
    component.setValue("Save");
    component.setAccessKey('A');
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(1, label.getPos());
    Assert.assertEquals((Character) 'a', label.getAccessKey());
  }

  @Test
  public void testNumberKey() {
    final UILabel component = new UILabel();
    component.setValue("Save");
    component.setAccessKey('5');
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals((Character) '5', label.getAccessKey());
  }

  @Test
  public void testForbiddenKey() {
    final UILabel component = new UILabel();
    component.setValue("Save");
    component.setAccessKey('#');
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testForbiddenKey2() {
    final UILabel component = new UILabel();
    component.setValue("Save");
    component.setAccessKey('á');
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testForbiddenKey3() {
    final UILabel component = new UILabel();
    component.setValue("Save");
    component.setAccessKey('ä');
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("Save", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testUnderscores1() {
    final UILabel component = new UILabel();
    component.setValue("_");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testUnderscores2() {
    final UILabel component = new UILabel();
    component.setValue("__");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("_", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testUnderscores3() {
    final UILabel component = new UILabel();
    component.setValue("___");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("_", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testUnderscores4() {
    final UILabel component = new UILabel();
    component.setValue("____");
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals("__", label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }

  @Test
  public void testNull() {
    final UILabel component = new UILabel();
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    Assert.assertEquals(null, label.getLabel());
    Assert.assertEquals(-1, label.getPos());
    Assert.assertEquals(null, label.getAccessKey());
  }
}
