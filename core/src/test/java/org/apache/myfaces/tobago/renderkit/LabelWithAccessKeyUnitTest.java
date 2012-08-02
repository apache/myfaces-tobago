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

import junit.framework.TestCase;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class LabelWithAccessKeyUnitTest extends TestCase {

  public void testSimple() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "Save");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(-1, label.getPos());
    assertEquals(null, label.getAccessKey());
  }


  public void testWithKeyFirstLetter() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "Save_");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertNull(label.getAccessKey());
  }



  public void testWithKeyLastLetter() {
    String result = "A_n_aly_ze";
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "A__n__a_ly__ze");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals(result, label.getText());
    assertEquals(5, label.getPos());
    assertEquals(new Character('l'), label.getAccessKey());
  }


  public void testAmpersand() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "_Save");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(0, label.getPos());
    assertEquals(new Character('S'), label.getAccessKey());
  }

  public void testAmpersandAtEnd() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "Save_");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(-1, label.getPos());
    assertEquals(null, label.getAccessKey());
  }

/*
  public void testAmpersandNo() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("labelWithAccessKey", "Save");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(-1, label.getPos());
    assertEquals(null, label.getAccessKey());
  }

  public void testMixed() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "Cancel");
    component.getAttributes().put("labelWithAccessKey", "Sa_ve");
    component.getAttributes().put("accessKey", "a");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(2, label.getPos());
    assertEquals(new Character('v'), label.getAccessKey());
  }
*/

}

