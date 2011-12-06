package org.apache.myfaces.tobago.example.data;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class MixedCommandTree {

  public static NamedNode createSample() {

    NamedNode tree = new NamedNode("Commands");

    NamedNode actions = new NamedNode("Actions");
    tree.add(actions);
    actions.add(new NamedNode("Action 1", "Action 1", null, null));
    actions.add(new NamedNode("Action 2", "Action 2", null, null));
    actions.add(new NamedNode("Action 3", "Action 3", null, null));

    NamedNode links = new NamedNode("Links");
    tree.add(links);
    links.add(new NamedNode("MyFaces", null, null, "http://myfaces.apache.org/"));
    links.add(new NamedNode("Apache", null, null, "http://www.apache.org/"));

    NamedNode scripts = new NamedNode("Scripts");
    tree.add(scripts);
    scripts.add(new NamedNode("1", null, "alert(1);", null));
    scripts.add(new NamedNode("2", null, "alert(2);", null));

    return tree;
  }
}
