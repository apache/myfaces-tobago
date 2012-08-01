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

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class StyleClassesUnitTest {

  @Test
  public void testRemoveOneMatch() {
    StyleClasses c = new StyleClasses();
    c.addFullQualifiedClass("bla");
    c.addFullQualifiedClass("tobago-test-inline");
    c.addFullQualifiedClass("blupp");
    c.removeTobagoClasses("test");
    Assert.assertEquals("bla blupp", c.toString());
  }

  @Test
  public void testRemoveNoMatch() {
    StyleClasses c = new StyleClasses();
    c.addFullQualifiedClass("bla");
    c.addFullQualifiedClass("tobago-test-inline");
    c.addFullQualifiedClass("blupp");
    c.removeTobagoClasses("no");
    Assert.assertEquals("bla tobago-test-inline blupp", c.toString());
  }

  @Test
  public void testRemoveEmpty() {
    StyleClasses c = new StyleClasses();
    c.removeTobagoClasses("no");
    Assert.assertEquals(null, c.toString());
  }

  @Test
  public void testAddMarkupClass() {
    StyleClasses c = new StyleClasses();
    c.addMarkupClass("myComponent", "big");
    Assert.assertEquals("tobago-myComponent-markup-big", c.toString());
    c.removeMarkupClass("myComponent", "big");
    Assert.assertEquals(null, c.toString());
  }

  @Test
  public void testAddMarkupClassSub() {
    StyleClasses c = new StyleClasses();
    c.addMarkupClass("myComponent", "mySub", "big");
    Assert.assertEquals("tobago-myComponent-mySub-markup-big", c.toString());
    c.removeMarkupClass("myComponent", "mySub", "big");
    Assert.assertEquals(null, c.toString());
  }

  @Test
  public void testAddAspectClass() {
    StyleClasses c = new StyleClasses();
    c.addAspectClass("myComponent", StyleClasses.Aspect.DISABLED);
    Assert.assertEquals("tobago-myComponent-disabled", c.toString());
    c.removeAspectClass("myComponent", StyleClasses.Aspect.DISABLED);
    Assert.assertEquals(null, c.toString());
  }

  @Test
  public void testAddAspectClassSub() {
    StyleClasses c = new StyleClasses();
    c.addAspectClass("myComponent", "mySub", StyleClasses.Aspect.DISABLED);
    Assert.assertEquals("tobago-myComponent-mySub-disabled", c.toString());
    c.removeAspectClass("myComponent", "mySub", StyleClasses.Aspect.DISABLED);
    Assert.assertEquals(null, c.toString());
  }

}
