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

import {Key} from "./tobago-key";
import {Overlay} from "./tobago-overlay";
import {OverlayType} from "./tobago-overlay-type";
import {Page} from "./tobago-page";

export class TobagoPaginator extends HTMLElement {

  constructor() {
    super();
  }

  get text(): HTMLElement {
    return this.querySelector("span.page-link");
  }

  get output(): HTMLElement {
    return this.querySelector(".page-link span");
  }

  get input(): HTMLInputElement {
    return this.querySelector("input");
  }

  get sheet(): HTMLElement {
    const sheet: HTMLElement = this.closest("tobago-sheet");
    if (sheet != null) {
      return sheet;
    } else {
      window.alert("todo: implement find, wenn this is not a child");
      return document.querySelector("tobago-sheet");
    }
  }

  get buttons(): NodeListOf<Element> {
    return this.querySelectorAll("li.page-item:not(.disabled)>button");
  }

  get action(): HTMLInputElement {
    return this.querySelector("input[type=hidden]");
  }

  connectedCallback(): void {
    const text = this.text;
    if (text) {
      text.addEventListener("click", this.clickOnPaging.bind(this));
    }

    const input = this.input;
    if (input) {
      input.addEventListener("blur", this.blurPaging.bind(this));
      input.addEventListener("keydown", function (event: KeyboardEvent): void {
        if (event.key === Key.ENTER) {
          event.stopPropagation();
          event.preventDefault();
          event.currentTarget.dispatchEvent(new Event("blur"));
        }
      }.bind(this));
    }

    const buttons = this.buttons;
    if (buttons) {
      buttons.forEach(item => item.addEventListener("click", this.clickOnButton.bind(this)));
    }
  }

  clickOnPaging(event: MouseEvent): void {
    const output: HTMLElement = this.output;
    output.style.display = "none";

    const input: HTMLInputElement = this.input;
    input.style.display = "initial";
    input.focus();
    input.select();
  }

  blurPaging(event: FocusEvent): void {
    const input = event.currentTarget as HTMLInputElement;
    const output: HTMLElement = this.output;
    const number = Number.parseInt(input.value); // sanitizing
    if (number > 0 && number.toString() !== output.innerHTML) {
      console.debug("Reloading sheet '%s' old value='%s' new value='%s'", this.id, output.innerHTML, number);
      output.innerHTML = number.toString();
      const action = this.tagName == "TOBAGO-PAGINATOR-ROW" ? "toRow" : "toPage";
      input.value = `{"action":"${action}","target":${number}}`;
      this.overlayAndAjaxReload();
    } else {
      console.info("no update needed");
    }
    input.style.display = "none";
    output.style.display = "initial";
  }

  clickOnButton(event: MouseEvent): void {
    const button = event.currentTarget as HTMLButtonElement;
    button.disabled = true;
    const input = this.input;
    input.value = button.dataset.tobagoAction;
    this.overlayAndAjaxReload();
  }

  private overlayAndAjaxReload() {
    const sheet = this.sheet;

    sheet.insertAdjacentHTML("beforeend",
        Overlay.htmlText(sheet.id, OverlayType.ajax, Page.page(this).waitOverlayDelayAjax));

    faces.ajax.request(
        this.id,
        null,
        {
          params: {
            "javax.faces.behavior.event": "reload"
          },
          execute: sheet.id,
          render: sheet.id
        });
  }
}
