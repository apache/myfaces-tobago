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

package org.apache.myfaces.tobago.renderkit.html.util;

import org.junit.Assert;
import org.junit.Test;

public class HtmlRendererUtilsUnitTest {

  // the suffix number 1 means true or != null, 0 means false or == null

  @Test
  public void testWriteSubmitAction100() {
    Assert.assertEquals("Tobago.submitAction(this,'id',{});",
        HtmlRendererUtils.createSubmitAction("id", true, null, null));
  }

  @Test
  public void testWriteSubmitAction000() {
    Assert.assertEquals("Tobago.submitAction(this,'id',{transition:false});",
        HtmlRendererUtils.createSubmitAction("id", false, null, null));
  }

  @Test
  public void testWriteSubmitAction110() {
    Assert.assertEquals("Tobago.submitAction(this,'id',{target:'t'});",
        HtmlRendererUtils.createSubmitAction("id", true, "t", null));
  }

  @Test
  public void testWriteSubmitAction010() {
    Assert.assertEquals("Tobago.submitAction(this,'id',{transition:false,target:'t'});",
        HtmlRendererUtils.createSubmitAction("id", false, "t", null));
  }

  @Test
  public void testWriteSubmitAction101() {
    Assert.assertEquals("Tobago.submitAction(this,'id',{focus:'f'});",
        HtmlRendererUtils.createSubmitAction("id", true, null, "f"));
  }

  @Test
  public void testWriteSubmitAction001() {
    Assert.assertEquals("Tobago.submitAction(this,'id',{transition:false,focus:'f'});",
        HtmlRendererUtils.createSubmitAction("id", false, null, "f"));
  }

  @Test
  public void testWriteSubmitAction111() {
    Assert.assertEquals("Tobago.submitAction(this,'id',{target:'t',focus:'f'});",
        HtmlRendererUtils.createSubmitAction("id", true, "t", "f"));
  }

  @Test
  public void testWriteSubmitAction011() {
    Assert.assertEquals("Tobago.submitAction(this,'id',{transition:false,target:'t',focus:'f'});",
        HtmlRendererUtils.createSubmitAction("id", false, "t", "f"));
  }
}
