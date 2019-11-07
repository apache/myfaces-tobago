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

class SelectOneListbox extends HTMLElement {

  private oldSelectedIndex: number = -1;

  constructor() {
    super();
  }

  connectedCallback(): void {
    if (!this.select.required) {
      this.select.addEventListener("click", this.clickSelection.bind(this));
      this.select.addEventListener("keyup", this.keySelection.bind(this));
    }
  }

  private clickSelection(event: MouseEvent): void {
    const select = event.currentTarget as HTMLSelectElement;
    if (select.selectedIndex >= 0 && select.selectedIndex === this.oldSelectedIndex) {
      this.revertSelection(select);
    } else {
      this.saveSelection(select);
    }
  }

  private keySelection(event: KeyboardEvent): void {
    const select = event.currentTarget as HTMLSelectElement;
    if (event.code === "Space") {
      if (select.selectedIndex >= 0) {
        this.oldSelectedIndex = select.selectedIndex;
        select.selectedIndex = -1;
      } else {
        select.selectedIndex = this.oldSelectedIndex;
      }
    } else {
      this.saveSelection(select);
    }
  }

  private revertSelection(select: HTMLSelectElement): void {
    if (select.selectedIndex > -1) {
      this.oldSelectedIndex = select.selectedIndex;
      select.selectedIndex = -1;
    } else {
      select.selectedIndex = this.oldSelectedIndex;
      this.oldSelectedIndex = -1;
    }
  }

  private saveSelection(select: HTMLSelectElement): void {
    this.oldSelectedIndex = select.selectedIndex;
  }

  get select(): HTMLSelectElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "field") as HTMLSelectElement;
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-select-one-listbox", SelectOneListbox);
});
