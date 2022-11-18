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

package org.apache.myfaces.tobago.internal.util;

import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.component.UIParameter;

public class RenderUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void simple() {
    final UILink link = new UILink();
    link.setLink("local.xhtml");
    final String url = RenderUtils.generateUrl(getFacesContext(), link);
    Assertions.assertEquals("local.xhtml", url);
  }

  @Test
  public void mailto() {
    final UILink link = new UILink();
    link.setLink("mailto:MyFaces Discussion <users@myfaces.apache.org>");

    final UIParameter subject = new UIParameter();
    subject.setName("subject");
    subject.setValue("[Tobago] Preparation for the 5.0.0 release");
    link.getChildren().add(subject);

    final UIParameter body = new UIParameter();
    body.setName("body");
    body.setValue("Hi, folks,\n"
        + "\n"
        + "we plan to build version 5.0.0 of Tobago soon.");
    link.getChildren().add(body);

    final String url = RenderUtils.generateUrl(getFacesContext(), link);
    Assertions.assertEquals("mailto:MyFaces Discussion <users@myfaces.apache.org>"
        + "?subject=%5BTobago%5D%20Preparation%20for%20the%205.0.0%20release"
        + "&body=Hi%2C%20folks%2C%0A%0Awe%20plan%20to%20build%20version%205.0.0%20of%20Tobago%20soon.", url);
  }
}
