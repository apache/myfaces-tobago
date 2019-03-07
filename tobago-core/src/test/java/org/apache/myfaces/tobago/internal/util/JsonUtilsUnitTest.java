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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.internal.context.DateTimeI18n;
import org.apache.myfaces.tobago.internal.renderkit.Collapse;
import org.apache.myfaces.tobago.internal.renderkit.Command;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.util.ComponentUtils;
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
    map.setClick(new Command(null, null, null, "", null, null, null, null, null, null));
    final String expected = "{'click':{}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void change() {
    final CommandMap map = new CommandMap();
    map.addCommand(ClientBehaviors.change, new Command(null, null, null, null, null, null, null, null, null, null));
    final String expected = "{'change':{}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void two() {
    final CommandMap map = new CommandMap();
    map.addCommand(ClientBehaviors.click, new Command(null, null, "target", null, null, null, null, null, null, null));
    map.addCommand(ClientBehaviors.change, new Command(null, null, null, null, null, null, null, null, null, null));
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
        ComponentUtils.createComponent(facesContext, Tags.button.componentType(), RendererTypes.Button, "command");
    ComponentUtils.setAttribute(command, Attributes.popupClose, "immediate");

    map.setClick(new Command(
        "ns:actionId",
        false,
        "_blank",
        StringUtils.join(Arrays.asList("id1", "id2"), ' '),
        StringUtils.join(Arrays.asList("id1", "id2"), ' '),
        "id_focus",
        "Really?", 1000, new Collapse(Collapse.Action.show, "myId"), true));
    final String expected = (
        "{"
            + "'click':{"
            + "'action':'ns:actionId',"
            + "'transition':false,"
            + "'target':'_blank',"
            + "'execute':'id1 id2',"
            + "'render':'id1 id2',"
            + "'collapse':{"
            + "'transition':'show',"
            + "'forId':'myId'"
            + "},"
            + "'focus':'id_focus',"
            + "'confirmation':'Really?',"
            + "'delay':1000,"
            + "'omit':true"
            + "}"
            + "}").replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void monthNames() {
    final DateTimeI18n dateTimeI18n = DateTimeI18n.valueOf(Locale.GERMANY);
    final String marchShort = dateTimeI18n.getMonthNamesShort()[2]; // different with JDK 1.8.0_51 and 1.8.0_60
    final String[] dayNamesShort = dateTimeI18n.getDayNamesShort(); // different with JDK 1.8.0 and 1.9.0
    final String[] dayNamesMin = dateTimeI18n.getDayNamesMin(); // different with JDK 1.8.0 and 1.9.0
    final String expected
        = ("{'monthNames':['Januar','Februar','März','April','Mai','Juni',"
        + "'Juli','August','September','Oktober','November','Dezember'],"
        + "'monthNamesShort':['Jan','Feb','" + marchShort + "','Apr','Mai','Jun','Jul','Aug','Sep','Okt','Nov','Dez'],"
        + "'dayNames':['Sonntag','Montag','Dienstag','Mittwoch','Donnerstag','Freitag','Samstag'],"
        + "'dayNamesShort':['" + dayNamesShort[0] + "','" + dayNamesShort[1] + "','" + dayNamesShort[2] + "','"
        + dayNamesShort[3] + "','" + dayNamesShort[4] + "','" + dayNamesShort[5] + "','" + dayNamesShort[6] + "'],"
        + "'dayNamesMin':['" + dayNamesMin[0] + "','" + dayNamesMin[1] + "','" + dayNamesMin[2] + "','"
        + dayNamesMin[3] + "','" + dayNamesMin[4] + "','" + dayNamesMin[5] + "','" + dayNamesMin[6] + "'],"
        + "'firstDay':1}").replaceAll("'", "\"");

    Assert.assertEquals(expected, JsonUtils.encode(dateTimeI18n));
  }

  @Test
  public void decodeIntegerArray() {

    Assert.assertEquals(Arrays.asList(1, 2, 3, 4), JsonUtils.decodeIntegerArray("[1,2,3,4]"));

    Assert.assertEquals(Arrays.asList(1, 2, 3, 4), JsonUtils.decodeIntegerArray(" [ 1 , 2 , 3 , 4 ] "));

    Assert.assertEquals(Arrays.asList(1), JsonUtils.decodeIntegerArray("[1]"));

    Assert.assertEquals(Arrays.asList(), JsonUtils.decodeIntegerArray("[]"));

    Assert.assertEquals(Arrays.asList(1000000000, 2, 3, 4), JsonUtils.decodeIntegerArray("[1000000000,2,3,4]"));

    Assert.assertEquals(Arrays.asList(2, 3, 4), JsonUtils.decodeIntegerArray("[null,2,3,4]"));

    Assert.assertEquals(Arrays.asList(), JsonUtils.decodeIntegerArray("1,2,3,4"));
  }

  @Test
  public void encodeStringArray() {
    Assert.assertEquals("[\"A-rập Xê-út (Tiếng A-rập)\"]",
        JsonUtils.encode(new String[]{"A-rập Xê-út (Tiếng A-rập)"}, false));
    Assert.assertEquals("[\"foo\"bar\"]", JsonUtils.encode(new String[]{"foo\"bar"}, false));
    Assert.assertEquals("[\"foo\\\"bar\"]", JsonUtils.encode(new String[]{"foo\"bar"}, true));
  }

  @Test
  public void encodeMarkup() {
    final Markup a = Markup.valueOf("a");
    final Markup ab = Markup.valueOf("a,b");

    final String expectedA = "['a']".replaceAll("'", "\"");
    final String expectedAb = "['a','b']".replaceAll("'", "\"");

    Assert.assertEquals(expectedA, JsonUtils.encode(a));
    Assert.assertEquals(expectedAb, JsonUtils.encode(ab));
    Assert.assertNull(JsonUtils.encode(Markup.NULL));
    Assert.assertNull(JsonUtils.encode((Markup) null));

  }

  @Test
  public void testCommandMap() {
    CommandMap map = new CommandMap();
    map.addCommand(
        ClientBehaviors.blur,
        new Command(
            "doit", false, "field", "execute", "render", "focus", "Do \"you\" want?", 100,
            new Collapse(Collapse.Action.hide, "box"), false));

    final String expected
        = ("{'blur':"
        + "{'action':'doit',"
        + "'transition':false,"
        + "'target':'field',"
        + "'execute':'execute',"
        + "'render':'render',"
        + "'collapse':{'transition':'hide','forId':'box'},"
        + "'focus':'focus',"
        + "'confirmation':'Do \\'you\\' want?',"
        + "'delay':100}}").replaceAll("'", "\"");
    Assert.assertEquals("command map", expected, JsonUtils.encode(map));
  }

}
