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

import org.apache.myfaces.tobago.context.Markup;

import javax.swing.tree.DefaultMutableTreeNode;

public class CategoryTree {

  public static DefaultMutableTreeNode createSample() {
    DefaultMutableTreeNode tree = createNode("Root Node", "root");
    tree.insert(createNode("Sports", "sports"), 0);
    tree.insert(createNode("Movies", "movies"), 1);
    DefaultMutableTreeNode music = createNode("Music", "music");
    tree.insert(music, 2);
    music.insert(createNode("Classic", "classic"), 0);
    music.insert(createNode("Pop", "pop"), 1);
    music.insert(createNode("World", "world"), 2);
    tree.insert(createNode("Games", "games"), 3);
    DefaultMutableTreeNode science = createNode("Science", "science");
    science.insert(createNode("Geography", "geography"), 0);
    science.insert(createNode("Mathematics", "math"), 0);
    DefaultMutableTreeNode astro = createNode("Astronomy", "astro");
    astro.insert(createNode("Education", "edu"), 0);
    astro.insert(createNode("Pictures", "pic"), 0);
    science.insert(astro, 2);
    tree.insert(science, 4);
    return tree;
  }

  public static DefaultMutableTreeNode createNode(String name, String id) {
    return new DefaultMutableTreeNode(new Node(name, id));
  }

  public static DefaultMutableTreeNode createSample2() {
    DefaultMutableTreeNode tree = new DefaultMutableTreeNode(new Node("1 Category"));
    tree.add(new DefaultMutableTreeNode(new Node("1.1 Sports")));
    tree.add(new DefaultMutableTreeNode(new Node("1.2 Movies")));
    DefaultMutableTreeNode temp = new DefaultMutableTreeNode(new Node("1.3 Science"));
    tree.add(temp);
    DefaultMutableTreeNode music = new DefaultMutableTreeNode(new Node("1.4 Music"));
    tree.add(music);
    tree.add(new DefaultMutableTreeNode(new Node("1.5 Games")));
    temp.add(new DefaultMutableTreeNode(new Node("1.3.1 Geography (strong markup)", Markup.STRONG)));
    temp.add(new DefaultMutableTreeNode(new Node("1.3.2 Mathematics (strong markup)", Markup.STRONG)));
    DefaultMutableTreeNode temp2 = new DefaultMutableTreeNode(new Node("1.3.3 Pictures"));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.1 Education")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.2 Family")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.3 Comercial")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.4 Summer (disabled)", true)));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.5 Winter (disabled)", true)));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.6 Red")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.7 Black")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.8 White")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.9 Good")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.10 Evil")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.11 Flower")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.12 Animal")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.13 Personal")));
    temp.add(temp2);
    DefaultMutableTreeNode bulk = new DefaultMutableTreeNode(new Node("1.6 Bulk"));
    for (int i = 0; i < 5; i++) {
      bulk.add(new DefaultMutableTreeNode(new Node("1.6." + (i + 1) + " Some Node")));
    }
    tree.add(bulk);
    ((Node) tree.getUserObject()).setExpanded(true);
    ((Node) temp.getUserObject()).setExpanded(true);
    ((Node) tree.getUserObject()).setSelected(true);
    ((Node) temp.getUserObject()).setSelected(true);

    return tree;
  }

}
