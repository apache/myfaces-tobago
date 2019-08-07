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

class SelectManyShuttle {

  static init = function (elements) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
    var shuttles = Tobago4Utils.selectWithJQuery(elements, ".tobago-selectManyShuttle:not(.tobago-selectManyShuttle-disabled)");

    shuttles.find(".tobago-selectManyShuttle-unselected").dblclick(function () {
      SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), true, false);
    });

    shuttles.find(".tobago-selectManyShuttle-selected").dblclick(function () {
      SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), false, false);
    });

    shuttles.find(".tobago-selectManyShuttle-addAll").click(function () {
      SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), true, true);
    });

    shuttles.find(".tobago-selectManyShuttle-add").click(function () {
      SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), true, false);
    });

    shuttles.find(".tobago-selectManyShuttle-removeAll").click(function () {
      SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), false, true);
    });

    shuttles.find(".tobago-selectManyShuttle-remove").click(function () {
      SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), false, false);
    });
  };

  static moveSelectedItems = function ($shuttle, direction, all) {
    var $unselected = $shuttle.find(".tobago-selectManyShuttle-unselected");
    var $selected = $shuttle.find(".tobago-selectManyShuttle-selected");
    var count = $selected.children().length;
    var $source = direction ? $unselected : $selected;
    var $target = direction ? $selected : $unselected;
    var $shifted = $source.find(all ? "option:not(:disabled)" : "option:selected").remove().appendTo($target);

    // synchronize the hidden select
    var $hidden = $shuttle.find(".tobago-selectManyShuttle-hidden");
    var $hiddenOptions = $hidden.find("option");
    // todo: may be optimized: put values in a hash map?
    $shifted.each(function () {
      var $option = jQuery(this);
      $hiddenOptions.filter("[value='" + $option.val() + "']").prop("selected", direction);
    });

    if (count !== $selected.children().length) {
      var e = jQuery.Event("change");
      // trigger an change event for command facets
      $hidden.trigger(e);
    }
  };
}

Listener.register(SelectManyShuttle.init, Phase.DOCUMENT_READY);
Listener.register(SelectManyShuttle.init, Phase.AFTER_UPDATE);
