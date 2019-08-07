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

class SelectOneListbox {

  static init = function (elements) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
    var selects = Tobago4Utils.selectWithJQuery(elements, ".tobago-selectOneListbox");
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
}

Listener.register(SelectOneListbox.init, Phase.DOCUMENT_READY);
Listener.register(SelectOneListbox.init, Phase.AFTER_UPDATE);
