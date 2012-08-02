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

package org.apache.myfaces.tobago.model;

import junit.framework.TestCase;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public class TreeModelUnitTest extends TestCase {

  private DefaultMutableTreeNode tree;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (tree == null) {
      tree = new DefaultMutableTreeNode("Category");
      tree.add(new DefaultMutableTreeNode("Sports"));
      tree.add(new DefaultMutableTreeNode("Movies"));
      DefaultMutableTreeNode sience = new DefaultMutableTreeNode("Science");
      tree.add(sience);
      sience.add(new DefaultMutableTreeNode("Geography"));
      sience.add(new DefaultMutableTreeNode("Mathematics"));
      DefaultMutableTreeNode astronomy = new DefaultMutableTreeNode("Astronomy");
      astronomy.add(new DefaultMutableTreeNode("Education"));
      astronomy.add(new DefaultMutableTreeNode("Pictures"));
      sience.add(astronomy);
      tree.add(new DefaultMutableTreeNode("Music"));
      tree.add(new DefaultMutableTreeNode("Games"));
    }
  }

  public void testAddressing() {
    TreeModel model = new TreeModel(tree);
    assertEquals("Root", "Category", model.getNode("_0").getUserObject());
    assertEquals("Sports", "Sports", model.getNode("_0_0").getUserObject());
    assertEquals("Astronomy", "Astronomy", model.getNode("_0_2_2").getUserObject());
    assertEquals("Games", "Games", model.getNode("_0_4").getUserObject());
  }

  public void testPathIndexList() {
    TreeModel model = new TreeModel(tree);
    List<String> pathIndexList = model.getPathIndexList();
    assertEquals("Count", 11, pathIndexList.size());
    assertEquals("Root", "_0", pathIndexList.get(0));
    assertEquals("Sports", "_0_0", pathIndexList.get(1));
    assertEquals("Astronomy", "_0_2_2", pathIndexList.get(6));
    assertEquals("Games", "_0_4", pathIndexList.get(10));
  }

  /*
  cat
    sport
    /sport
    movies
    /movies
    science
      geo
      /geo
      math
      /math
      astro
        edu
        /edu
        pict
        /pict
      /astro
    music
    /music
    games
    /games
  /cat
    */
  public void testDoublePathIndexList() {
    TreeModel model = new TreeModel(tree);
    List<TreeModel.Tag> list = model.getDoublePathIndexList();
    assertEquals("Count", 22, list.size());
    assertEquals("Root", "_0", list.get(0).getName());
    assertEquals("Root", "_0", list.get(21).getName());
    assertEquals("Sports", "_0_0", list.get(1).getName());
    assertEquals("Sports", "_0_0", list.get(2).getName());
    assertEquals("Astronomy", "_0_2_2", list.get(10).getName());
    assertEquals("Astronomy", "_0_2_2", list.get(15).getName());
    assertEquals("Games", "_0_4", list.get(19).getName());
    assertEquals("Games", "_0_4", list.get(20).getName());

    assertTrue("Astronomy", list.get(10).isStart());
    assertFalse("Astronomy", list.get(15).isStart());
  }

  public void testParentPathIndex() {
    TreeModel model = new TreeModel(tree);
    assertEquals("Root", null, model.getParentPathIndex("_0"));
    assertEquals("Root", "_0", model.getParentPathIndex("_0_1"));
    assertEquals("Root", "_0", model.getParentPathIndex("_0_33"));
    assertEquals("Root", "_0_3_33", model.getParentPathIndex("_0_3_33_33"));
  }

}
