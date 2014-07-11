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

package org.apache.myfaces.tobago.context;

import org.junit.Assert;
import org.junit.Test;

public class ResourceUtilsUnitTest {

  @Test
  public void testCreateString() {
    Assert.assertEquals("folder/treeNode-icon-open.png",
        ResourceUtils.createString("folder", "treeNode", "icon", "open", ResourceUtils.PNG));
  }

  @Test
  public void testCreateString2() {
    Assert.assertEquals("folder/treeNode-icon.png",
        ResourceUtils.createString("folder", "treeNode", "icon", ResourceUtils.PNG));
  }

  @Test
  public void testAddPostfixToFilename() {
    Assert.assertEquals("folder/treeNode-iconDisabled.png",
        ResourceUtils.addPostfixToFilename("folder/treeNode-icon.png", "Disabled"));
  }
}
