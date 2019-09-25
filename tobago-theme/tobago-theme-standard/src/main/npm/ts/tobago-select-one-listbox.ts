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

class SelectOneListbox {

  static init = function (element: HTMLElement): void {
    for (const listbox of DomUtils.selfOrQuerySelectorAll(element, ".tobago-selectOneListbox:not(:required)")) {
      listbox.addEventListener("change", (event: Event) => {
        const target = event.currentTarget as HTMLSelectElement;
        if (!target.dataset["tobagoOldValue"]) {
          target.dataset["tobagoOldValue"] = "-1";
        }
      });
      listbox.addEventListener("click", (event: Event) => {
        const target = event.currentTarget as HTMLSelectElement;
        if (!target.dataset["tobagoOldValue"] || parseInt(target.dataset["tobagoOldValue"]) === target.selectedIndex) {
          target.selectedIndex = -1;
        }
        target.dataset["tobagoOldValue"] = String(target.selectedIndex);
      });
    }
  };
}

Listener.register(SelectOneListbox.init, Phase.DOCUMENT_READY);
Listener.register(SelectOneListbox.init, Phase.AFTER_UPDATE);
