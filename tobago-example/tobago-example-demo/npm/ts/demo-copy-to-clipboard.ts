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

class DemoCopyToClipboard extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.addEventListener("click", (event: MouseEvent) => {
      const sourceElement = document.getElementById(this.source);

      if (window.getSelection) {
        const selection = window.getSelection();
        const range = document.createRange();
        range.selectNodeContents(sourceElement);
        selection.removeAllRanges();
        selection.addRange(range);
      } else {
        console.warn("Text select not possible: Unsupported browser.");
      }
      try {
        const result = document.execCommand("copy");
        console.debug("result: " + result);
      } catch (error) {
        console.error("Copying text not possible");
      }
    });
  }

  get source(): string {
    return this.getAttribute("source");
  }

  set source(name: string) {
    this.setAttribute("source", name);
  }

}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  if (window.customElements.get("demo-copy-to-clipboard") == null) {
    window.customElements.define("demo-copy-to-clipboard", DemoCopyToClipboard);
  }
});
