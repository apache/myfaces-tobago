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
import javax.swing.tree.MutableTreeNode;

public class CategoryTree {

  public static DefaultMutableTreeNode createSample() {
    DefaultMutableTreeNode tree = new DefaultMutableTreeNode(new Node("Root Node", "root"));
    tree.insert(new DefaultMutableTreeNode(new Node("Sports", "sports")), 0);
    tree.insert(new DefaultMutableTreeNode(new Node("Movies", "movies")), 1);
    DefaultMutableTreeNode music = new DefaultMutableTreeNode(new Node("Music", "music"));
    tree.insert(music, 2);
    music.insert(new DefaultMutableTreeNode(new Node("Classic", "classic")), 0);
    music.insert(new DefaultMutableTreeNode(new Node("Pop", "pop")), 1);
    music.insert(new DefaultMutableTreeNode(new Node("World", "world")), 2);
    tree.insert(new DefaultMutableTreeNode(new Node("Games", "games")), 3);
    MutableTreeNode science = new DefaultMutableTreeNode(new Node("Science", "science"));
    science.insert(new DefaultMutableTreeNode(new Node("Geography", "geography")), 0);
    science.insert(new DefaultMutableTreeNode(new Node("Mathematics", "math")), 0);
    DefaultMutableTreeNode astro = new DefaultMutableTreeNode(new Node("Astronomy", "astro"));
    astro.insert(new DefaultMutableTreeNode(new Node("Education", "edu")), 0);
    astro.insert(new DefaultMutableTreeNode(new Node("Pictures", "pic")), 0);
    science.insert(astro, 2);
    tree.insert(science, 4);
    return tree;
  }

  public static class Node {

    private String name;

    private String id;

    public Node(String name, String id) {
      this.name = name;
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String toString() {
      return "Node name=" + name + " id=" + id;
    }
  }
}
