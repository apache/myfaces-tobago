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

package org.apache.myfaces.tobago.internal.internal.renderkit;

import org.apache.myfaces.tobago.internal.renderkit.Collapse;
import org.apache.myfaces.tobago.internal.renderkit.Command;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;

public class CommandUnitTest {

  @Test
  public void test() {

    final Command a = new Command();
    a.setAction("a action");
    a.setCollapse(new Collapse(Collapse.Action.show, "a collapse"));
    a.setConfirmation("a conf");
    a.setExecute("a execute");

    final Command b = new Command();
    b.setAction("b action");
    b.setCollapse(new Collapse(Collapse.Action.show, "b collapse"));
    b.setConfirmation("b conf");
    b.setExecute("b execute");

    a.merge(b);

    Assert.assertEquals(
        ("{'click':"
            + "{'action':'a action',"
            + "'execute':'a execute b execute'"
            + ",'collapse':{'transition':'show','forId':'a collapse'}"
            + ",'confirmation':'a conf'}}")
            .replaceAll("'", "\""),
        JsonUtils.encode(new CommandMap(a)));

    final Command c = new Command();

    c.merge(b);

    Assert.assertEquals(
        ("{'click':"
            + "{'action':'b action',"
            + "'execute':'b execute',"
            + "'collapse':{'transition':'show','forId':'b collapse'},"
            + "'confirmation':'b conf'}}")
            .replaceAll("'", "\""),
        JsonUtils.encode(new CommandMap(c)));

  }
}
