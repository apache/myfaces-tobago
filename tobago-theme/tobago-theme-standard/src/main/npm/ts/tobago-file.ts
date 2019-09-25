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

export class File extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.input.form.enctype = "multipart/form-data";

    this.input.addEventListener("change", this.select.bind(this));
  }

  get input(): HTMLInputElement {
    return this.querySelector(".custom-file-input");
  }

  get label(): HTMLInputElement {
    return this.querySelector(".custom-file-label");
  }

  select(event: MouseEvent): void {
    if (this.input.value === "") {
      this.label.classList.add("tobago-file-placeholder");
      this.label.textContent = this.input.placeholder;
    } else {
      this.label.classList.remove("tobago-file-placeholder");

      let text: string;
      if (this.input.multiple) {
        const format: string = this.input.dataset["tobagoFileMultiFormat"];
        text = format.replace("{}", String(this.input.files.length));
      } else {
        text = this.input.value;
        // remove path, if any. Some old browsers set the path, others like webkit uses the prefix "C:\path\".
        const pos: number = Math.max(text.lastIndexOf("/"), text.lastIndexOf("\\"));
        if (pos >= 0) {
          text = text.substr(pos + 1);
        }
      }
      this.label.textContent = text;
    }
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-file", File);
});
