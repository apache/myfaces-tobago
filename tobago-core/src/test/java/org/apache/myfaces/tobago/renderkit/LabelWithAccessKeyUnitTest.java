/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.09.2004 14:11:53.
 * $Id:LabelWithAccessKeyUnitTest.java 1300 2005-08-10 16:40:23 +0200 (Mi, 10 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import junit.framework.TestCase;

public class LabelWithAccessKeyUnitTest extends TestCase {

  public void testSimple() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "Save");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(-1, label.getPos());
    assertEquals(null, label.getAccessKey());
  }

  // code-sniplet-start id="test1"
  public void testWithKeyFirstLetter() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "Save");
    component.getAttributes().put("accessKey", "s");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(0, label.getPos());
    assertEquals(new Character('s'), label.getAccessKey());
  }
  // code-sniplet-end id="test1"

  public void testWithKeyLastLetter() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "Save");
    component.getAttributes().put("accessKey", "E");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(3, label.getPos());
    assertEquals(new Character('E'), label.getAccessKey());
  }

  public void testAmpersand() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("labelWithAccessKey", "_Save");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(0, label.getPos());
    assertEquals(new Character('S'), label.getAccessKey());
  }

  public void testAmpersandAtEnd() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("labelWithAccessKey", "Save_");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(-1, label.getPos());
    assertEquals(null, label.getAccessKey());
  }

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

}

