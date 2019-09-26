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

class SelectOneRadio {

  static init = function (element: HTMLElement): void {
    for (const radio of DomUtils.selfOrQuerySelectorAll(element, ".tobago-selectOneRadio")) {
      const id = radio.closest("[id]").id;
      const quotedId = id.replace(/([:.])/g, "\\$1");
      const allConnected = document.querySelectorAll("input[name=" + quotedId + "]") as NodeListOf<HTMLInputElement>;
      for (const connectedRadio of allConnected) {
        connectedRadio.dataset.tobagoOldValue = String(connectedRadio.checked);
        connectedRadio.addEventListener("click", (event: Event) => {
          const target = event.currentTarget as HTMLInputElement;
          const readOnly = target.readOnly;
          const required = target.required;
          if (!required && !readOnly) {
            if (target.dataset.tobagoOldValue === String(target.checked)) {
              target.checked = false;
            }
            target.dataset.tobagoOldValue = String(target.checked);
          }
          if (readOnly) {
            for (const connectedRadio of allConnected) {
              connectedRadio.checked = connectedRadio.dataset.tobagoOldValue === "true";
            }
          } else {
            for (const connectedRadio of allConnected) {
              if (target.id != connectedRadio.id) {
                connectedRadio.checked = false;
                connectedRadio.dataset.tobagoOldValue = String(connectedRadio.checked);
              }
            }
          }
        });
      }
    }
  };
}

Listener.register(SelectOneRadio.init, Phase.DOCUMENT_READY);
Listener.register(SelectOneRadio.init, Phase.AFTER_UPDATE);
