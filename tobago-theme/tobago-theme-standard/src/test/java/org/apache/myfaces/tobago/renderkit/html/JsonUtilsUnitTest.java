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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.internal.context.DateTimeI18n;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Locale;

// using ' instead of " to make it better readable.

public class JsonUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void empty() {
    final CommandMap map = new CommandMap();
    Assert.assertEquals("{}", JsonUtils.encode(map));
  }

  @Test
  public void click() {
    final CommandMap map = new CommandMap();
    map.setClick(new Command(null, null, null, null, "", null, null, null, null, null));
    final String expected = "{'click':{}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void change() {
    final CommandMap map = new CommandMap();
    map.addCommand("change", new Command(null, null, null, null, null, null, null, null, null, null));
    final String expected = "{'change':{}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void two() {
    final CommandMap map = new CommandMap();
    map.addCommand("click", new Command(null, null, "target", null, null, null, null, null, null, null));
    map.addCommand("change", new Command(null, null, null, null, null, null, null, null, null, null));
    final String expected = "{'click':{'target':'target'},'change':{}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void transition() {
    final CommandMap commandMap = new CommandMap();
    commandMap.setClick(new Command(null, false, null, null, null, null, null, null, null, null));
    final String expected = "{'click':{'transition':false}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(commandMap));
  }

  @Test
  public void more() {
    final CommandMap map = new CommandMap();
    final AbstractUICommand command = (AbstractUICommand)
        CreateComponentUtils.createComponent(facesContext, UIButton.COMPONENT_TYPE, RendererTypes.Button, "command");
    ComponentUtils.setAttribute(command, Attributes.popupClose, "immediate");
    command.setRenderedPartially(new String[]{"popup"});

    map.setClick(new Command(
        "ns:actionId",
        false,
        "_blank", "http://www.apache.org/",
        StringUtils.join(Arrays.asList("id1", "id2"), ' '),
        "id_focus",
        "Really?", 1000, Popup.createPopup(command), true));
    final String expected = (
        "{"
            + "'click':{"
            + "'action':'ns:actionId',"
            + "'transition':false,"
            + "'target':'_blank',"
            + "'url':'http://www.apache.org/',"
            + "'partially':'id1 id2',"
            + "'focus':'id_focus',"
            + "'confirmation':'Really?',"
            + "'delay':1000,"
            + "'popup':{"
            + "'command':'close',"
            + "'immediate':true"
            + "},"
            + "'omit':true"
            + "}"
            + "}").replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void monthNames() {
    final DateTimeI18n dateTimeI18n = DateTimeI18n.valueOf(Locale.GERMANY);
    final String marchShort = dateTimeI18n.getMonthNamesShort()[2]; // different with JDK 1.8.0_51 and 1.8.0_60
    final String expected
        = ("{'monthNames':['Januar','Februar','März','April','Mai','Juni',"
        + "'Juli','August','September','Oktober','November','Dezember'],"
        + "'monthNamesShort':['Jan','Feb','" + marchShort + "','Apr','Mai','Jun','Jul','Aug','Sep','Okt','Nov','Dez'],"
        + "'dayNames':['Sonntag','Montag','Dienstag','Mittwoch','Donnerstag','Freitag','Samstag'],"
        + "'dayNamesShort':['So','Mo','Di','Mi','Do','Fr','Sa'],"
        + "'dayNamesMin':['So','Mo','Di','Mi','Do','Fr','Sa'],"
        + "'firstDay':1}").replaceAll("'", "\"");

    Assert.assertEquals(expected, JsonUtils.encode(dateTimeI18n));
  }

  @Test
  public void decodeIntegerArray() {

    Assert.assertEquals(Arrays.asList(1,2,3,4), JsonUtils.decodeIntegerArray("[1,2,3,4]"));

    Assert.assertEquals(Arrays.asList(1,2,3,4), JsonUtils.decodeIntegerArray(" [ 1 , 2 , 3 , 4 ] "));

    Assert.assertEquals(Arrays.asList(1), JsonUtils.decodeIntegerArray("[1]"));

    Assert.assertEquals(Arrays.asList(), JsonUtils.decodeIntegerArray("[]"));

    Assert.assertEquals(Arrays.asList(1000000000,2,3,4), JsonUtils.decodeIntegerArray("[1000000000,2,3,4]"));

    Assert.assertEquals(Arrays.asList(2,3,4), JsonUtils.decodeIntegerArray("[null,2,3,4]"));

    Assert.assertEquals(null, JsonUtils.decodeIntegerArray("1,2,3,4"));
  }

}
