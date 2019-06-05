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
import {DomUtils, Tobago4Utils} from "./tobago-utils";

export class Popup {

/**
 * Init popup for bootstrap
 */
static init = function (elements) {
  elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
  var popups = Tobago4Utils.selectWithJQuery(elements, ".modal");
  popups.each(function () {
    var $popup = jQuery(this);
    var $hidden = Collapse.findHidden($popup);
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

static close = function (button) {
  jQuery(button).parents('.modal:first').modal("hide");

};
}

Listener.register(Popup.init, Phase.DOCUMENT_READY);
Listener.register(Popup.init, Phase.AFTER_UPDATE);

export class Collapse {

static findHidden = function ($element) {
  return jQuery(DomUtils.escapeClientId($element.attr("id") + "::collapse"));
};

static execute = function (collapse) {
  var transition = collapse.transition;
  var $for = jQuery(DomUtils.escapeClientId(collapse.forId));
  var $hidden = Collapse.findHidden($for);
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
}
