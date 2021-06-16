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

import {Focus} from "./tobago-focus";

class SelectManyCheckbox extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    for (const input of this.inputs) {
      input.addEventListener("focus", Focus.setLastFocusId);

      if (input.readOnly) {
        input.addEventListener("click", preventClick);
      }

      function preventClick(event: MouseEvent): void {
        // in the "readonly" case, prevent the default, which is changing the "checked" state
        event.preventDefault();
      }
    }
  }

  get inputs(): NodeListOf<HTMLInputElement> {
    return this.querySelectorAll(`input[name='${this.id}']`);
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-many-checkbox") == null) {
    window.customElements.define("tobago-select-many-checkbox", SelectManyCheckbox);
  }
});
