/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 07.09.2004 14:11:53.
 * $Id$
 */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
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

  public void testWithKeyFirstLetter() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("label", "Save");
    component.getAttributes().put("accessKey", "s");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(0, label.getPos());
    assertEquals(new Character('s'), label.getAccessKey());
  }

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
    component.getAttributes().put("labelWithAccessKey", "&Save");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(0, label.getPos());
    assertEquals(new Character('S'), label.getAccessKey());
  }

  public void testAmpersandAtEnd() {
    UIComponent component = new UIOutput();
    component.getAttributes().put("labelWithAccessKey", "Save&");
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
    component.getAttributes().put("labelWithAccessKey", "Sa&ve");
    component.getAttributes().put("accessKey", "a");
    LabelWithAccessKey label = new LabelWithAccessKey(component);
    assertEquals("Save", label.getText());
    assertEquals(2, label.getPos());
    assertEquals(new Character('v'), label.getAccessKey());
  }

}

