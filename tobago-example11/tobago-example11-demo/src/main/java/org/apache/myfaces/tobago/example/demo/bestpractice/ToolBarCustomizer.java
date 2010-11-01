package org.apache.myfaces.tobago.example.demo.bestpractice;

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

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ToolBarCustomizer {

  private List<Item> list;

  public ToolBarCustomizer() {
    resetList();
  }

  public String resetList() {
    list = new ArrayList<Item>(Arrays.asList(

        // TODO: load data from webapp/bestpractice/tool-bar-item*.xhtml

        new Item("New"),
        new Item("Edit"),
        new Item("Delete")
    ));

    return null;
  }

  public void itemUp(ActionEvent event) {
    Item item = (Item) ((UIParameter) event.getComponent().getChildren().get(0)).getValue();
    int oldIndex = list.indexOf(item);
    if (oldIndex > 0) {
      list.remove(item);
      list.add(oldIndex - 1, item);
    }
  }

  public void itemDown(ActionEvent event) {
    Item item = (Item) ((UIParameter) event.getComponent().getChildren().get(0)).getValue();
    int oldIndex = list.indexOf(item);
    if (oldIndex < list.size()) {
      list.remove(item);
      list.add(oldIndex + 1, item);
    }
  }

  public List<Item> getList() {
    return list;
  }

  public static class Item {

    private String label;
    private String name;

    private boolean visible = true;

    private Item(String label) {
      this.label = label;
      this.name = "tool-bar-item-" + label.toLowerCase(Locale.ENGLISH) + ".xhtml";
    }

    public String getLabel() {
      return label;
    }

    public String getName() {
      return name;
    }

    public boolean isVisible() {
      return visible;
    }

    public void setVisible(boolean visible) {
      this.visible = visible;
    }
  }
}
