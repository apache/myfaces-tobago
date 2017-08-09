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

Tobago.Popup = {};

/**
 * Init popup for bootstrap
 */
Tobago.Popup.init = function (elements) {

  var popups = Tobago.Utils.selectWithJQuery(elements, ".modal");
  popups.each(function () {
    var $popup = jQuery(this);
    var $hidden = Tobago.Collapse.findHidden($popup);
    if ($hidden.val() == "false") {
      // XXX hack: this is needed for popups open by AJAX.
      // XXX currently the DOM replacement done by Tobago doesn't remove the modal-backdrop
      jQuery(".modal-backdrop").remove();

      jQuery(this).modal("show"); // inits and opens the popup
    } else {
      jQuery(this).modal("hide"); // inits and hides the popup
    }
  });
};

Tobago.Popup.close = function (button) {
  jQuery(button).parents('.modal:first').modal("hide");

};

Tobago.registerListener(Tobago.Popup.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Popup.init, Tobago.Phase.AFTER_UPDATE);

Tobago.Collapse = {};

Tobago.Collapse.findHidden = function ($element) {
  return jQuery(Tobago.Utils.escapeClientId($element.attr("id") + "::collapse"));
};

Tobago.Collapse.execute = function (collapse) {
  var transition = collapse.transition;
  var $for = jQuery(Tobago.Utils.escapeClientId(collapse.forId));
  var $hidden = Tobago.Collapse.findHidden($for);
  var isPopup = $for.hasClass("tobago-popup");
  var newCollapsed;
  switch (transition) {
    case "hide":
      newCollapsed = true;
      break;
    case "show":
      newCollapsed = false;
      break;
    default:
      console.error("unknown transition: '" + transition + "'");
  }
  if (newCollapsed) {
    if (isPopup) {
      $for.modal("hide");
    } else {
      $for.addClass("tobago-collapsed");
    }
  } else {
    if (isPopup) {
      $for.modal("show");
    } else {
      $for.removeClass("tobago-collapsed");
    }
  }
  $hidden.val(newCollapsed);
};
