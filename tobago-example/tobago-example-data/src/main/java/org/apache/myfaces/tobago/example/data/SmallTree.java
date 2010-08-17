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

import javax.swing.tree.DefaultMutableTreeNode;

public class SmallTree {

  public static DefaultMutableTreeNode createSample() {
    DefaultMutableTreeNode tree = new DefaultMutableTreeNode(new Node("Root Node", "root"));
    DefaultMutableTreeNode music = new DefaultMutableTreeNode(new Node("Music", "music"));
    tree.add(music);
    music.add(new DefaultMutableTreeNode(new Node("Pop", "pop")));
    tree.add(new DefaultMutableTreeNode(new Node("Sports", "sports")));
    return tree;
  }
}
