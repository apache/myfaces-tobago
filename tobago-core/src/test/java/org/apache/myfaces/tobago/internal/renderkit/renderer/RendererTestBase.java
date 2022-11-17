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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RendererTestBase extends AbstractTobagoTestBase {

  protected String formattedResult() throws IOException {
    return formatCrlf2LfAndTrim(format1To2Indent(getLastWritten()));
  }

  protected String loadHtml(final String fileName) throws IOException {
    final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    try (InputStream is = classLoader.getResourceAsStream(fileName)) {
      if (is == null) {
        throw new FileNotFoundException(fileName);
      }
      try (InputStreamReader isr = new InputStreamReader(is);
           BufferedReader reader = new BufferedReader(isr)) {
        final String xml = reader.lines().collect(Collectors.joining(System.lineSeparator()))
            .replaceAll("<!--[^>]*-->", "")
            .replaceAll("^\n\n", "");
        return formatCrlf2LfAndTrim(xml);
      }
    }
  }

  protected String format1To2Indent(final String xml) {
    return xml.replaceAll("^\n", "")
        .replaceAll("\n <", "\n\t<")
        .replaceAll("\n  <", "\n\t\t<")
        .replaceAll("\n   <", "\n\t\t\t<")
        .replaceAll("\n    <", "\n\t\t\t\t<")
        .replaceAll("\n     <", "\n\t\t\t\t\t<")
        .replaceAll("\n      <", "\n\t\t\t\t\t\t<")
        .replaceAll("\n       <", "\n\t\t\t\t\t\t\t<")
        .replaceAll("\t", "  ");
  }

  protected String formatCrlf2LfAndTrim(final String xml) {
    return xml.replaceAll("\r\n", "\n").trim();
  }

  protected DefaultMutableTreeNode getTreeSample() {
    final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Category");
    addNode(root, 0, "Sports");
    addNode(root, 0, "Movies");
    addNode(root, 0, "Music");
    addNode(root, 1, "Classic");
    addNode(root, 1, "Pop");
    addNode(root, 1, "World");
    addNode(root, 2, "Carib");
    addNode(root, 2, "Africa");
    addNode(root, 0, "Games");
    addNode(root, 0, "Science");
    addNode(root, 1, "Mathematics");
    addNode(root, 2, "Analysis");
    addNode(root, 2, "Algebra");
    addNode(root, 1, "Geography");
    addNode(root, 1, "Astronomy");
    addNode(root, 2, "Education");
    addNode(root, 2, "Pictures");
    addNode(root, 3, "NGC");
    addNode(root, 3, "Messier");

    return root;
  }

  private void addNode(DefaultMutableTreeNode root, int level, Object userObject) {
    DefaultMutableTreeNode node = root;
    for (int i = 0; i < level; i++) {
      node = (DefaultMutableTreeNode) node.getLastChild();
    }
    node.add(new DefaultMutableTreeNode(userObject));
  }

  protected List<Item> getSheetSample(final int count) {
    final List<Item> list = new ArrayList<Item>();
    for (int i = 0; i < count; i++) {
      list.add(new Item("Name " + i));

    }
    return list;
  }

  public static class Item {

    private String name;

    public Item(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

}
