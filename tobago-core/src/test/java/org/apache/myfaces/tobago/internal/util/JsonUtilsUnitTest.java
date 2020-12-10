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
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MeasureList;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

// using ' instead of " to make it better readable.

public class JsonUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void empty() {
    final CommandMap map = new CommandMap();
    Assertions.assertEquals("{}", JsonUtils.encode(map));

    Assertions.assertNull(JsonUtils.encode((CommandMap) null));
  }

  @Test
  public void click() {
    final CommandMap map = new CommandMap();
    map.setClick(new Command(null, null, null, null, "", null, null, null, null, null));
    final String expected = "{'click':{}}".replaceAll("'", "\"");
    Assertions.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void change() {
    final CommandMap map = new CommandMap();
    map.addCommand(ClientBehaviors.change, new Command(null, null, null, null, null, null, null, null, null, null));
    final String expected = "{'change':{}}".replaceAll("'", "\"");
    Assertions.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void two() {
    final CommandMap map = new CommandMap();
    map.addCommand(ClientBehaviors.click, new Command(null, null, null, "target", null, null, null, null, null, null));
    map.addCommand(ClientBehaviors.change, new Command(null, null, null, null, null, null, null, null, null, null));
    final String expected = "{'click':{'target':'target'},'change':{}}".replaceAll("'", "\"");
    Assertions.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void transition() {
    final CommandMap commandMap = new CommandMap();
    commandMap.setClick(new Command(null, null, false, null, null, null, null, null, null, null));
    final String expected = "{'click':{'transition':false}}".replaceAll("'", "\"");
    Assertions.assertEquals(expected, JsonUtils.encode(commandMap));
  }

  @Test
  public void more() {
    final CommandMap map = new CommandMap();
    final AbstractUICommand command = (AbstractUICommand)
        ComponentUtils.createComponent(facesContext, Tags.button.componentType(), RendererTypes.Button, "command");
    ComponentUtils.setAttribute(command, Attributes.popupClose, "immediate");

    map.setClick(new Command(
        "ns:actionId",
        null,
        false,
        "_blank",
        StringUtils.join(Arrays.asList("id1", "id2"), ' '),
        StringUtils.join(Arrays.asList("id1", "id2"), ' '),
        "Really?", 1000, new Collapse(Collapse.Action.show, "myId"), true));
    final String expected = (
        "{"
            + "'click':{"
            + "'clientId':'ns:actionId',"
            + "'transition':false,"
            + "'target':'_blank',"
            + "'execute':'id1 id2',"
            + "'render':'id1 id2',"
            + "'collapse':{"
            + "'transition':'show',"
            + "'forId':'myId'"
            + "},"
            + "'confirmation':'Really?',"
            + "'delay':1000,"
            + "'omit':true"
            + "}"
            + "}").replaceAll("'", "\"");
    Assertions.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void months() {
    final DateTimeI18n dateTimeI18n = DateTimeI18n.valueOf(Locale.GERMANY);
    final String marchShort = dateTimeI18n.getMonthsShort()[2]; // different with JDK 1.8.0_51 and 1.8.0_60
    final String[] daysShort = dateTimeI18n.getDaysShort(); // different with JDK 1.8.0    and 1.9.0
    final String[] daysMin = dateTimeI18n.getDaysMin(); // different with JDK 1.8.0 and 1.9.0
    final String expected
        = ("{'months':['Januar','Februar','März','April','Mai','Juni',"
        + "'Juli','August','September','Oktober','November','Dezember'],"
        + "'monthsShort':['Jan','Feb','" + marchShort + "','Apr','Mai','Jun','Jul','Aug','Sep','Okt','Nov','Dez'],"
        + "'days':['Sonntag','Montag','Dienstag','Mittwoch','Donnerstag','Freitag','Samstag'],"
        + "'daysShort':['" + daysShort[0] + "','" + daysShort[1] + "','" + daysShort[2] + "','"
        + daysShort[3] + "','" + daysShort[4] + "','" + daysShort[5] + "','" + daysShort[6] + "'],"
        + "'daysMin':['" + daysMin[0] + "','" + daysMin[1] + "','" + daysMin[2] + "','"
        + daysMin[3] + "','" + daysMin[4] + "','" + daysMin[5] + "','" + daysMin[6] + "'],"
        + "'firstDay':1,'minDays':4,'today':'Today','cancel':'Cancel','clear':'Clear','week':'Week'}")
        .replaceAll("'", "\"");

    Assertions.assertEquals(expected, JsonUtils.encode(dateTimeI18n));
  }

  @Test
  public void decodeIntegerArray() {

    Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), JsonUtils.decodeIntegerArray("[1,2,3,4]"));

    Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), JsonUtils.decodeIntegerArray(" [ 1 , 2 , 3 , 4 ] "));

    Assertions.assertEquals(Collections.singletonList(1), JsonUtils.decodeIntegerArray("[1]"));

    Assertions.assertEquals(Collections.emptyList(), JsonUtils.decodeIntegerArray("[]"));

    Assertions.assertEquals(Arrays.asList(1000000000, 2, 3, 4), JsonUtils.decodeIntegerArray("[1000000000,2,3,4]"));

    Assertions.assertEquals(Arrays.asList(2, 3, 4), JsonUtils.decodeIntegerArray("[null,2,3,4]"));

    Assertions.assertEquals(Collections.emptyList(), JsonUtils.decodeIntegerArray("1,2,3,4"));

    Assertions.assertNull(JsonUtils.decodeIntegerArray(null));
  }

  @Test
  public void encodeStringArray() {
    Assertions.assertEquals("[\"A-rập Xê-út (Tiếng A-rập)\"]",
        JsonUtils.encode(new String[]{"A-rập Xê-út (Tiếng A-rập)"}));

    Assertions.assertEquals("[\"foo\\\"bar\"]", JsonUtils.encode(new String[]{"foo\"bar"}));

    Assertions.assertNull(JsonUtils.encode((String[]) null));
  }

  @Test
  public void encodeMarkup() {
    final Markup a = Markup.valueOf("a");
    final Markup ab = Markup.valueOf("a,b");

    final String expectedA = "['a']".replaceAll("'", "\"");
    final String expectedAb = "['a','b']".replaceAll("'", "\"");

    Assertions.assertEquals(expectedA, JsonUtils.encode(a));
    Assertions.assertEquals(expectedAb, JsonUtils.encode(ab));
    Assertions.assertNull(JsonUtils.encode(Markup.NULL));
    Assertions.assertNull(JsonUtils.encode((Markup) null));

  }

  @Test
  public void testCommandMap() {
    CommandMap map = new CommandMap();
    map.addCommand(
        ClientBehaviors.blur,
        new Command(
            "doit", null, false, "field", "execute", "render", "Do \"you\" want?", 100,
            new Collapse(Collapse.Action.hide, "box"), false));

    final String expected
        = ("{'blur':"
        + "{'clientId':'doit',"
        + "'transition':false,"
        + "'target':'field',"
        + "'execute':'execute',"
        + "'render':'render',"
        + "'collapse':{'transition':'hide','forId':'box'},"
        + "'confirmation':'Do \\'you\\' want?',"
        + "'delay':100}}").replaceAll("'", "\"");
    Assertions.assertEquals(expected, JsonUtils.encode(map), "command map");
  }

  @Test
  public void encodeMeasureList() {
    final MeasureList measureList = new MeasureList();
    measureList.add(Measure.AUTO);
    measureList.add(Measure.FRACTION1);
    measureList.add(Measure.valueOf(100));

    Assertions.assertEquals(
        "{\"name\":[\"auto\",1.0,{\"measure\":\"100px\"}]}",
        JsonUtils.encode(measureList, "name"));
  }

  @Test
  public void encodeBooleanArray() {
    final Boolean[] array = new Boolean[]{true, false, true};
    Assertions.assertEquals("[true,false,true]", JsonUtils.encode(array));

    Assertions.assertNull(JsonUtils.encode((Boolean[]) null));

    Assertions.assertEquals("[]", JsonUtils.encode(new Boolean[0]));
  }

  @Test
  public void encodeIntegerArray() {
    final Integer[] array = new Integer[]{-1_000_000_000, 0, 42};
    Assertions.assertEquals("[-1000000000,0,42]", JsonUtils.encode(array));

    Assertions.assertNull(JsonUtils.encode((Integer[]) null));

    Assertions.assertEquals("[]", JsonUtils.encode(new Integer[0]));
  }

  @Test
  public void encodeIntegerList() {
    final List<Integer> list = Arrays.asList(-1_000_000_000, 0, 42);
    Assertions.assertEquals("[-1000000000,0,42]", JsonUtils.encode(list));

    Assertions.assertNull(JsonUtils.encode((List<Integer>) null));

    Assertions.assertEquals("[]", JsonUtils.encode(new ArrayList<>()));
  }

  @Test
  public void encodeEmptyArray() {
    Assertions.assertEquals("[]", JsonUtils.encodeEmptyArray());
  }

}
