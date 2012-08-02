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

package org.apache.myfaces.tobago.renderkit.html;

import junit.framework.TestCase;

/*
 * Date: 2007-05-01
 */

public class StyleClassesUnitTest extends TestCase {

  public void testRemoveOneMatch() {
    StyleClasses c = new StyleClasses();
    c.addFullQualifiedClass("bla");
    c.addFullQualifiedClass("tobago-test-inline");
    c.addFullQualifiedClass("blupp");
    c.removeTobagoClasses("test");
    assertEquals("bla blupp", c.toString());
  }

  public void testRemoveNoMatch() {
    StyleClasses c = new StyleClasses();
    c.addFullQualifiedClass("bla");
    c.addFullQualifiedClass("tobago-test-inline");
    c.addFullQualifiedClass("blupp");
    c.removeTobagoClasses("no");
    assertEquals("bla tobago-test-inline blupp", c.toString());
  }

  public void testRemoveEmpty() {
    StyleClasses c = new StyleClasses();
    c.removeTobagoClasses("no");
    assertEquals(null, c.toString());
  }

  public void testAddMarkupClass() {
    StyleClasses c = new StyleClasses();
    c.addMarkupClass("myComponent", "big");
    assertEquals("tobago-myComponent-markup-big", c.toString());
    c.removeMarkupClass("myComponent", "big");
    assertEquals(null, c.toString());
  }

  public void testAddMarkupClassSub() {
    StyleClasses c = new StyleClasses();
    c.addMarkupClass("myComponent", "mySub", "big");
    assertEquals("tobago-myComponent-mySub-markup-big", c.toString());
    c.removeMarkupClass("myComponent", "mySub", "big");
    assertEquals(null, c.toString());
  }

  public void testAddAspectClass() {
    StyleClasses c = new StyleClasses();
    c.addAspectClass("myComponent", StyleClasses.Aspect.DISABLED);
    assertEquals("tobago-myComponent-disabled", c.toString());
    c.removeAspectClass("myComponent", StyleClasses.Aspect.DISABLED);
    assertEquals(null, c.toString());
  }

  public void testAddAspectClassSub() {
    StyleClasses c = new StyleClasses();
    c.addAspectClass("myComponent", "mySub", StyleClasses.Aspect.DISABLED);
    assertEquals("tobago-myComponent-mySub-disabled", c.toString());
    c.removeAspectClass("myComponent", "mySub", StyleClasses.Aspect.DISABLED);
    assertEquals(null, c.toString());
  }

}
