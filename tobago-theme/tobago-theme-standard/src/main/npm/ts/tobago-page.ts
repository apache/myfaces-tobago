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
import {Jsf} from "./tobago-jsf";

export class Page extends HTMLElement {

  /**
   * The Tobago root element
   */
  static page(): HTMLElement {
    const pages = document.getElementsByTagName("tobago-page");
    if (pages.length > 0) {
      if (pages.length >= 2) {
        console.warn("Found more than one tobago page!");
      }
      return pages.item(0) as HTMLElement;
    }
    console.warn("Found no tobago page!");
    return null;
  }

  constructor() {
    super();
  }

  connectedCallback(): void {

    Jsf.registerAjaxListener();

    this.addEventListener("keypress", function (event: KeyboardEvent): boolean {
      let code = event.which; // XXX deprecated
      if (code === 0) {
        code = event.keyCode;
      }
      if (code === 13) {
        let target = event.target as HTMLElement;
        if (target.tagName === "A" || target.tagName === "BUTTON") {
          return;
        }
        if (target.tagName === "TEXTAREA") {
          if (!event.metaKey && !event.ctrlKey) {
            return;
          }
        }
        const name = target.getAttribute("name");
        let id = name ? name : target.id;
        while (id != null) {
          const command = document.querySelector("[data-tobago-default='" + id + "']");
          if (command) {
            command.dispatchEvent(new MouseEvent("click"));
            break;
          }
          id = DomUtils.getNamingContainerId(id);
        }
        return false;
      }
    });
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-page", Page);
});
