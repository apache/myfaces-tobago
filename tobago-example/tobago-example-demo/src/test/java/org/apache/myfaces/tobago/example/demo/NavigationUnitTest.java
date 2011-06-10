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

import org.apache.myfaces.tobago.internal.mock.faces.AbstractTobagoTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testFileNames() {
    List<String> list = new ArrayList<String>(Arrays.asList(
        "/content/00/test-1.xhtml",
// todo        "/content/00/x-bad.xhtml", // x- means excluded
        "/content/00/07/test-2.xhtml",
        "/content/00/07/x-bad.xhtml", // x- means excluded
        "/content/01/test.xhtml",
        "/content/00/00/test-4.xhtml",
        "/content/bad.jsp",
        "/content/00_00_bad.gif"
    ));
    final Navigation navigation = new Navigation(list);
    final Navigation.Node root = navigation.getTree();
    Assert.assertEquals(2, root.getChildCount());
    final Navigation.Node n00 = (Navigation.Node) root.getChildAt(0);
    Assert.assertEquals("/content/00", n00.getBranch());
    Assert.assertEquals("test-1", n00.getName());
    final Navigation.Node n01 = (Navigation.Node) root.getChildAt(1);
    Assert.assertEquals("/content/01", n01.getBranch());
    Assert.assertEquals("test", n01.getName());
    final Navigation.Node n0000 = (Navigation.Node) n00.getChildAt(0);
    Assert.assertEquals("/content/00/00", n0000.getBranch());
    Assert.assertEquals("test-4", n0000.getName());
    final Navigation.Node n0007 = (Navigation.Node) n00.getChildAt(1);
    Assert.assertEquals("/content/00/07", n0007.getBranch());
    Assert.assertEquals("test-2", n0007.getName());
  }
}
