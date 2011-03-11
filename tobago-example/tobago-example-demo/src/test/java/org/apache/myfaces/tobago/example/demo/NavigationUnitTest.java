package org.apache.myfaces.tobago.example.demo;

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

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class NavigationUnitTest {

  @Test
  public void testFileNames() {
    Navigation navigation = new Navigation();
    List<String> list = Arrays.asList(
        "00-test-1.xhtml",
        "00|07-test-2.xhtml",
        "01-test.xhtml",
        "00|00-test-4.xhtml",
        "bad.xhtml",
        "00_00_bad.xhtml"
    );

    final Navigation.Node root = navigation.buildNodes(list);
    Assert.assertEquals(2, root.getChildCount());
    final Navigation.Node n00 = (Navigation.Node) root.getChildAt(0);
    Assert.assertEquals("00", n00.getBranch());
    Assert.assertEquals("test-1", n00.getName());
    final Navigation.Node n01 = (Navigation.Node) root.getChildAt(1);
    Assert.assertEquals("01", n01.getBranch());
    Assert.assertEquals("test", n01.getName());
    final Navigation.Node n0000 = (Navigation.Node) n00.getChildAt(0);
    Assert.assertEquals("00|00", n0000.getBranch());
    Assert.assertEquals("test-4", n0000.getName());
    final Navigation.Node n0007 = (Navigation.Node) n00.getChildAt(1);
    Assert.assertEquals("00|07", n0007.getBranch());
    Assert.assertEquals("test-2", n0007.getName());
  }
}
