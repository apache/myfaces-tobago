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

package org.apache.myfaces.tobago.internal.renderkit;

import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandUnitTest {

  @Test
  public void test() {

    final Command a = new Command(
        "a client id", "a field id", null, null, "a execute", null, "a conf", null,
        new Collapse(Collapse.Operation.show, "a collapse"), false, false, "dummy", false);

    final Command b = new Command(
        "b client id", "b field id", null, null, "b execute", null, "b conf", null,
        new Collapse(Collapse.Operation.show, "b collapse"), false, false, "dummy", true);

    a.merge(b);

    Assertions.assertEquals(
        ("{'click':"
            + "{'clientId':'a client id',"
            + "'fieldId':'a field id',"
            + "'execute':'a execute b execute'"
            + ",'collapse':{'transition':'show','forId':'a collapse'}"
            + ",'confirmation':'a conf'}}")
            .replaceAll("'", "\""),
        JsonUtils.encode(new CommandMap(a)));

    final Command c = new Command(
        null, null, null, null, null, null, null, null,
        null, false, false, "dummy", false);

    c.merge(b);

    Assertions.assertEquals(
        ("{'click':"
            + "{'clientId':'b client id',"
            + "'fieldId':'b field id',"
            + "'execute':'b execute',"
            + "'collapse':{'transition':'show','forId':'b collapse'},"
            + "'confirmation':'b conf'}}")
            .replaceAll("'", "\""),
        JsonUtils.encode(new CommandMap(c)));

  }
}
