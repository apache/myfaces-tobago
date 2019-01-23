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

namespace Tobago {

  const init = function (element: HTMLElement) {
    const files: JQuery<NodeListOf<Element>> = jQuery(element.querySelectorAll(".tobago-file-real"));
    files.on("change", function () {
      const file: JQuery<NodeListOf<Element>> = jQuery(this);
      const pretty: JQuery<NodeListOf<Element>> = file.parent().find(".tobago-file-pretty");
      let text: string;
      if (file.prop("multiple")) {
        const format: string = file.data("tobago-file-multi-format");
        text = format.replace("{}", file.prop("files").length);
      } else {
        text = <string>file.val();
        // remove path, if any. Some old browsers set the path, others like webkit uses the prefix "C:\path\".
        const pos: number = Math.max(text.lastIndexOf('/'), text.lastIndexOf('\\'));
        if (pos >= 0) {
          text = text.substr(pos + 1);
        }
      }
      pretty.val(text);
    });
    // click on the button (when using focus with keyboard)
    files.each(function () {
      const real = jQuery(this);
      real.parent().find("button").on("click", function () {
        real.trigger("click");
      });
    });
    if (files.length > 0) {
      document.getElementsByTagName("form")[0].setAttribute('enctype', 'multipart/form-data');
    }
  };

  Listener.register(init, Phase.DOCUMENT_READY);
  Listener.register(init, Phase.AFTER_UPDATE);
}
