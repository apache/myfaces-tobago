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

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectManyShuttle = {};

Tobago.SelectManyShuttle.init = function(elements) {

  var shuttles = Tobago.Utils.selectWithJQuery(elements, ".tobago-selectManyShuttle:not(.tobago-selectManyShuttle-disabled)");

  shuttles.find(".tobago-selectManyShuttle-unselected").dblclick(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), true, false);
  });

  shuttles.find(".tobago-selectManyShuttle-selected").dblclick(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), false, false);
  });

  shuttles.find(".tobago-selectManyShuttle-addAll").click(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), true, true);
  });

  shuttles.find(".tobago-selectManyShuttle-add").click(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), true, false);
  });

  shuttles.find(".tobago-selectManyShuttle-removeAll").click(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), false, true);
  });

  shuttles.find(".tobago-selectManyShuttle-remove").click(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), false, false);
  });
};

Tobago.SelectManyShuttle.moveSelectedItems = function($shuttle, direction, all) {
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
  $shifted.each(function() {
    var $option = jQuery(this);
    $hiddenOptions.filter("[value='" + $option.val() + "']").prop("selected", direction);
  });

  if (count !== $selected.children().length) {
    var e = jQuery.Event("change");
    // trigger an change event for command facets
    $hidden.trigger( e );
  }
};

Tobago.registerListener(Tobago.SelectManyShuttle.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectManyShuttle.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectOneRadio = {};

Tobago.SelectOneRadio.init = function(elements) {
  var selectOneRadios = Tobago.Utils.selectWithJQuery(elements, ".tobago-selectOneRadio");
  selectOneRadios.each(function() {
    var ul = jQuery(this);
    var id = ul.closest("[id]").attr("id");
    var radios = jQuery('input[name="' + id.replace(/([:\.])/g, '\\$1') + '"]');
    radios.each(function () {
      var selectOneRadio = jQuery(this);
      selectOneRadio.data("tobago-old-value", selectOneRadio.prop("checked"));
    });
    radios.click(function() {
      var selectOneRadio = jQuery(this);
      var readonly = selectOneRadio.prop("readonly");
      var required = selectOneRadio.prop("required");
      if (!required && !readonly) {
        if (selectOneRadio.data("tobago-old-value") == selectOneRadio.prop("checked")) {
          selectOneRadio.prop("checked", false);
        }
        selectOneRadio.data("tobago-old-value", selectOneRadio.prop("checked"));
      }
      if (readonly) {
        radios.each(function () {
          var radio = jQuery(this);
          radio.prop("checked", radio.data("tobago-old-value"));
        });
      } else {
        radios.each(function () {
          if (this.id != selectOneRadio.get(0).id) {
            var radio = jQuery(this);
            radio.prop("checked", false);
            radio.data("tobago-old-value", radio.prop("checked"));
          }
        });
      }
    });
  });
};

Tobago.registerListener(Tobago.SelectOneRadio.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectOneRadio.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectOneListbox = {};

Tobago.SelectOneListbox.init = function (elements) {
  var selects = Tobago.Utils.selectWithJQuery(elements, ".tobago-selectOneListbox");
  var notRequired = selects.not(".tobago-selectOneListbox-markup-required");
  notRequired
      .change(function () {
        var element = jQuery(this);
        if (element.data("tobago-old-value") == undefined) {
          element.data("tobago-old-value", -1);
        }
      }).click(function () {
        var element = jQuery(this);
        if (element.data("tobago-old-value") == undefined
            || element.data("tobago-old-value") == element.prop("selectedIndex")) {
          element.prop("selectedIndex", -1);
        }
        element.data("tobago-old-value", element.prop("selectedIndex"));
      });
};

Tobago.registerListener(Tobago.SelectOneListbox.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectOneListbox.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectBooleanCheckbox = {};

Tobago.SelectBooleanCheckbox.init = function(elements) {
  var checkboxes = Tobago.Utils.selectWithJQuery(elements, ".tobago-selectBooleanCheckbox input[readonly]");
  checkboxes.each(function() {
    // Save the initial state to restore it, when the user tries to manipulate it.
    var initial = jQuery(this).is(":checked");
    jQuery(this).click(function() {
      jQuery(this).prop("checked", initial);
    });
  });
};

Tobago.registerListener(Tobago.SelectBooleanCheckbox.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectBooleanCheckbox.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectBooleanToggle = {};

Tobago.SelectBooleanToggle.init = function(elements) {
  var toggles = Tobago.Utils.selectWithJQuery(elements, ".tobago-selectBooleanToggle input[readonly]");
  toggles.each(function() {
    // Save the initial state to restore it, when the user tries to manipulate it.
    var initial = jQuery(this).is(":checked");
    jQuery(this).click(function() {
      jQuery(this).prop("checked", initial);
    });
  });
};

Tobago.registerListener(Tobago.SelectBooleanToggle.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectBooleanToggle.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectManyCheckbox = {};

Tobago.SelectManyCheckbox.init = function(elements) {
  var checkboxes = Tobago.Utils.selectWithJQuery(elements, ".tobago-selectManyCheckbox input[readonly]");
  checkboxes.each(function() {
    // Save the initial state to restore it, when the user tries to manipulate it.
    var initial = jQuery(this).is(":checked");
    jQuery(this).click(function() {
      jQuery(this).prop("checked", initial);
    });
  });
};

Tobago.registerListener(Tobago.SelectManyCheckbox.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectManyCheckbox.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
