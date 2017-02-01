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

package org.apache.myfaces.tobago.example.data;

public class CommandNodeFactory {

  public static CommandNode createSample() {
    final CommandNode root = new CommandNode("Commands");

    final CommandNode actions = new CommandNode("Actions");
    actions.add(new CommandNode("Action 1", "ActionOne", null));
    actions.add(new CommandNode("Action 2", "ActionTwo", null));
    root.add(actions);

    final CommandNode links = new CommandNode("Links");
    links.add(new CommandNode("Apache", null, "https://www.apache.org/"));
    links.add(new CommandNode("MyFaces", null, "https://myfaces.apache.org/"));
    links.add(new CommandNode("Tobago", null, "https://myfaces.apache.org/tobago/"));
    root.add(links);

    return root;
  }
}
