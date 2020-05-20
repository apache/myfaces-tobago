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

import {DomUtils} from "./tobago-utils";

export class SelectBooleanCheckbox extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    if (this.input.readOnly) {
      this.input.addEventListener("click", preventClick);
    }

    function preventClick(event: MouseEvent): void {
      // in the "readonly" case, prevent the default, which is changing the "checked" state
      event.preventDefault();
    }
  }

  get input(): HTMLInputElement {
    return this.querySelector(DomUtils.escapeClientId(this.id + DomUtils.SUB_COMPONENT_SEP + "field"));
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-select-boolean-checkbox", SelectBooleanCheckbox);
});
