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

class SelectOneRadio {

  static init = function (elements) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
    var selectOneRadios = Tobago4Utils.selectWithJQuery(elements, ".tobago-selectOneRadio");
    selectOneRadios.each(function () {
      var ul = jQuery(this);
      var id = ul.closest("[id]").attr("id");
      var radios = jQuery('input[name="' + id.replace(/([:\.])/g, '\\$1') + '"]');
      radios.each(function () {
        var selectOneRadio = jQuery(this);
        selectOneRadio.data("tobago-old-value", selectOneRadio.prop("checked"));
      });
      radios.click(function () {
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
}

Listener.register(SelectOneRadio.init, Phase.DOCUMENT_READY);
Listener.register(SelectOneRadio.init, Phase.AFTER_UPDATE);
