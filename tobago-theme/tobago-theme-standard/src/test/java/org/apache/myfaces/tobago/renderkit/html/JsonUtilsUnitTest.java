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
import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.junit.Assert;
import org.junit.Test;

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
    map.setClick(new Command(null, null, null, null, new String[0], null, null, null, null, null));
    final String expected = "{'click':{}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void change() {
    final CommandMap map = new CommandMap();
    map.addCommand("change", new Command(null, null, null, null, new String[0], null, null, null, null, null));
    final String expected = "{'change':{}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void two() {
    final CommandMap map = new CommandMap();
    map.addCommand("click", new Command(null, null, "target", null, new String[0], null, null, null, null, null));
    map.addCommand("change", new Command(null, null, null, null, new String[0], null, null, null, null, null));
    final String expected = "{'click':{'target':'target'},'change':{}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(map));
  }

  @Test
  public void transition() {
    final CommandMap commandMap = new CommandMap();
    commandMap.setClick(new Command(null, false, null, null, new String[0], null, null, null, null, null));
    final String expected = "{'click':{'transition':false}}".replaceAll("'", "\"");
    Assert.assertEquals(expected, JsonUtils.encode(commandMap));
  }

  @Test
  public void more() {
    final CommandMap map = new CommandMap();
    final AbstractUICommand command = (AbstractUICommand)
        CreateComponentUtils.createComponent(facesContext, ComponentTypes.BUTTON, RendererTypes.BUTTON, "command");
    command.getAttributes().put(Attributes.POPUP_CLOSE, "immediate");
    command.setRenderedPartially(new String[] {"popup"});

    map.setClick(new Command(
        "ns:actionId", false, "_blank", "http://www.apache.org/", new String[]{"id1", "id2"}, "id_focus",
        "Really?", 1000, Popup.createPopup(command), true));
    final String expected = (
        "{"
            + "'click':{"
            + "'action':'ns:actionId',"
            + "'transition':false,"
            + "'target':'_blank',"
            + "'url':'http://www.apache.org/',"
            + "'partially':['id1','id2'],"
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

}
