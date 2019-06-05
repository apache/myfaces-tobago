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
import {DomUtils} from "./tobago-utils";

class File {

  static init(element: HTMLElement) {
    for (const e of DomUtils.selfOrQuerySelectorAll(element, ".tobago-file-real")) {
      const real = <HTMLInputElement>e;
      real.addEventListener("change", function () {
        const pretty = <HTMLInputElement>real.parentElement.querySelector(".tobago-file-pretty");
        let text: string;
        if (real.multiple) {
          const format: string = real.dataset["tobagoFileMultiFormat"];
          text = format.replace("{}", real.files.length.toString());
        } else {
          text = <string>real.value;
          // remove path, if any. Some old browsers set the path, others like webkit uses the prefix "C:\path\".
          const pos: number = Math.max(text.lastIndexOf('/'), text.lastIndexOf('\\'));
          if (pos >= 0) {
            text = text.substr(pos + 1);
          }
        }
        pretty.value = text;
      });
      // click on the button (when using focus with keyboard)
      real.parentElement.querySelector("button").addEventListener("click", function () {
        real.click();
      });
      real.form.enctype = "multipart/form-data";
    }
  };
}

Listener.register(File.init, Phase.DOCUMENT_READY);
Listener.register(File.init, Phase.AFTER_UPDATE);
