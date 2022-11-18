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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

public class ComponentUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testSplitList() {
    Assertions.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab cd"));
    Assertions.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab  cd"));
    Assertions.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab,  cd"));
    Assertions.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab , cd"));
    Assertions.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab,,cd"));
  }

  @Test
  public void testFindDescendant() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIComponent p = ComponentUtils.createComponent(
        facesContext, Tags.panel.componentType(), RendererTypes.Panel, "p");
    final UIComponent i = ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "i");
    p.getChildren().add(i);

    final UIIn in = ComponentUtils.findDescendant(p, UIIn.class);
    Assertions.assertEquals(i, in);
  }

  @Test
  public void testGetMaximumSeverity() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();

    final UIIn input = new UIIn();
    final String inputId = "InputID";
    input.setId(inputId);

    input.setValid(true);
    Assertions.assertEquals(null, ComponentUtils.getMaximumSeverity(input));
    input.setValid(false);
    Assertions.assertEquals(FacesMessage.SEVERITY_ERROR, ComponentUtils.getMaximumSeverity(input));

    facesContext.addMessage(inputId, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info Message", null));
    input.setValid(true);
    Assertions.assertEquals(FacesMessage.SEVERITY_INFO, ComponentUtils.getMaximumSeverity(input));
    input.setValid(false);
    Assertions.assertEquals(FacesMessage.SEVERITY_ERROR, ComponentUtils.getMaximumSeverity(input));

    facesContext.addMessage(inputId, new FacesMessage(FacesMessage.SEVERITY_FATAL, "FATAL", null));
    input.setValid(true);
    Assertions.assertEquals(FacesMessage.SEVERITY_FATAL, ComponentUtils.getMaximumSeverity(input));
    input.setValid(false);
    Assertions.assertEquals(FacesMessage.SEVERITY_FATAL, ComponentUtils.getMaximumSeverity(input));

    final UIOut output = new UIOut();
    final String outputId = "OutputID";
    output.setId(outputId);

    Assertions.assertEquals(null, ComponentUtils.getMaximumSeverity(output));

    facesContext.addMessage(outputId, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info Message", null));
    Assertions.assertEquals(FacesMessage.SEVERITY_INFO, ComponentUtils.getMaximumSeverity(output));

    facesContext.addMessage(outputId, new FacesMessage(FacesMessage.SEVERITY_FATAL, "FATAL", null));
    Assertions.assertEquals(FacesMessage.SEVERITY_FATAL, ComponentUtils.getMaximumSeverity(output));
  }
}
