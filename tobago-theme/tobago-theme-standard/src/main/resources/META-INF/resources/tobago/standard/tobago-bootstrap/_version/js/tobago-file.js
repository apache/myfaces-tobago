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

Tobago.File = {};

Tobago.File.init = function(elements) {
  var files = Tobago.Utils.selectWithJQuery(elements, ".tobago-file-real");
  files.change(function () {
    var file = jQuery(this);
    var pretty = file.parent().find(".tobago-file-pretty");
    var text;
    if (file.prop("multiple")) {
      var format = file.data("tobago-file-multi-format");
      text = format.replace("{}", file.prop("files").length);
    } else {
      text = file.val();
      // remove path, if any. Some old browsers set the path, others like webkit uses the prefix "C:\facepath\".
      var pos = Math.max(text.lastIndexOf('/'), text.lastIndexOf('\\'));
      if (pos >= 0) {
        text = text.substr(pos + 1);
      }
    }
    pretty.val(text);
  });
  // click on the button (when using focus with keyboard)
  files.each(function() {
    var real = jQuery(this);
    real.parent().find("button").click(function() {
      real.click();
    });
  });
  if (files.length > 0) {
    Tobago.findForm().attr('enctype', 'multipart/form-data');
  }
};

Tobago.registerListener(Tobago.File.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.File.init, Tobago.Phase.AFTER_UPDATE);
