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

import Datepicker from "vanillajs-datepicker/js/Datepicker.js";
import {DateUtils} from "./tobago-date-utils";
import {Page} from "./tobago-page";

class DatePicker extends HTMLElement {

  lastValue: string;

  constructor() {
    super();
  }

  connectedCallback(): void {
    const input = this.inputElement;

    // todo: refactor "i18n" to "normal" attribute of tobago-date
    // todo: refactor: Make a class or interface for i18n
    const i18n = input.dataset.tobagoDateTimeI18n ? JSON.parse(input.dataset.tobagoDateTimeI18n) : undefined;
    // todo: refactor "pattern" to "normal" attribute of tobago-date
    const pattern = DateUtils.convertPattern(input.dataset.tobagoPattern);
    const locale: string = Page.page(this).locale;
    Datepicker.locales[locale] = {
      days: i18n.dayNames,
      daysShort: i18n.dayNamesShort,
      daysMin: i18n.dayNamesMin,
      months: i18n.monthNames,
      monthsShort: i18n.monthNamesShort,
      today: i18n.today,
      clear: i18n.clear,
      titleFormat: "MM y", // todo i18n
      format: pattern,
      weekstart: i18n.firstDay
    };

    const datepicker = new Datepicker(input, {
      buttonClass: "btn",
      orientation: "bottom top auto",
      autohide: true,
      language: locale
      // todo readonly
      // todo show week numbers
    });

    // XXX these listeners are needed as long as we have a solution for:
    // XXX https://github.com/mymth/vanillajs-datepicker/issues/13
    // XXX the 2nd point is missing the "normal" change event on the input element
    input.addEventListener("keyup", (event) => {
      // console.info("event -----> ", event.type);
      if (event.metaKey || event.key.length > 1 && event.key !== "Backspace" && event.key !== "Delete") {
        return;
      }
      // back up user's input when user types printable character or backspace/delete
      const target = event.target as any;
      target._oldValue = target.value;
    });

    input.addEventListener("focus", (event) => {
      // console.info("event -----> ", event.type);
      this.lastValue = input.value;
    });

    input.addEventListener("blur", (event) => {
      // console.info("event -----> ", event.type);
      const target = event.target as any;

      // no-op when user goes to another window or the input field has no backed-up value
      if (document.hasFocus() && target._oldValue !== undefined) {
        if (target._oldValue !== target.value) {
          target.datepicker.setDate(target._oldValue || {clear: true});
        }
        delete target._oldValue;
      }

      if (this.lastValue !== input.value) {
        input.dispatchEvent(new Event("change"));
      }
    });

    datepicker.element.addEventListener("changeDate", (event) => {
      // console.info("event -----> ", event.type);
      input.dispatchEvent(new Event("change"));
    });

    // simple solution for the picker: currently only open, not close is implemented
    this.querySelector(".tobago-date-picker")?.addEventListener("click",
        (event: MouseEvent) => {
          this.inputElement.focus();
        }
    );
  }

  get inputElement(): HTMLInputElement {
    return this.querySelector(".input") as HTMLInputElement;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-date") == null) {
    window.customElements.define("tobago-date", DatePicker);
  }
});
