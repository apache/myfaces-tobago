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

import {Listener, Phase} from "./tobago-listener";
import {Tobago4Utils} from "./tobago-utils";

class TreeListbox {
  static init = function (elements) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
    var treeListbox = Tobago4Utils.selectWithJQuery(elements, ".tobago-treeListbox");
    // hide select tags for level > root
    treeListbox.children().find("select:not(:first)").hide();

    var listboxSelects = treeListbox.find("select");

    listboxSelects.children("option").each(TreeListbox.initNextLevel);
    listboxSelects.each(TreeListbox.initListeners);
  };

// find all option tags and add the dedicated select tag in its data section.
  static initNextLevel = function () {
    var option = jQuery(this);
    var select = option.closest(".tobago-treeListbox-level").next()
        .find("[data-tobago-tree-parent='" + option.attr("id") + "']");
    if (select.length == 1) {
      option.data("tobago-select", select);
    } else {
      var empty = option.closest(".tobago-treeListbox-level").next().children(":first");
      option.data("tobago-select", empty);
    }
  };

// add on change on all select tag, all options that are not selected hide there dedicated
// select tag, and the selected option show its dedicated select tag.
  static initListeners = function () {

    jQuery(this).change(TreeListbox.onChange);

    jQuery(this).focus(function () {
      jQuery(this).change();
    });
  };

  static onChange = function () {
    var listbox = jQuery(this);
    listbox.children("option:not(:selected)").each(function () {
      jQuery(this).data("tobago-select").hide();
    });
    listbox.children("option:selected").each(function () {
      jQuery(this).data("tobago-select").show();
    });
    TreeListbox.setSelected(listbox);

    // Deeper level (2nd and later) should only show the empty select tag.
    // The first child is the empty selection.
    listbox.parent().nextAll(":not(:first)").each(function () {
      jQuery(this).children(":not(:first)").hide();
      jQuery(this).children(":first").show();
    });

  };

  static setSelected = function (listbox) {
    var hidden = listbox.closest(".tobago-treeListbox").children("[data-tobago-selection-mode]");
    if (hidden.length == 1) {
      var selectedValue = ";";
      listbox.children("option:selected").each(function () {
        selectedValue += jQuery(this).attr("id") + ";";
      });
      hidden.val(selectedValue);
    }
  };
}

Listener.register(TreeListbox.init, Phase.DOCUMENT_READY);
Listener.register(TreeListbox.init, Phase.AFTER_UPDATE);
