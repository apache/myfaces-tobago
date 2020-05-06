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
    const locale: string = Page.page().locale;
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

    new Datepicker(input, {
      buttonClass: "btn",
      orientation: "bottom top auto",
      autohide: true,
      language: locale
      // todo readonly
      // todo show week numbers
    });

    // XXX these two listeners are needed befor we have a solution for:
    // XXX https://github.com/mymth/vanillajs-datepicker/issues/13
    input.addEventListener("keyup", (event) => {
      if (event.ctrlKey || event.metaKey
          || event.key.length > 1 && event.key !== "Backspace" && event.key !== "Delete") {
        return;
      }
      // back up user's input when user types printable character or backspace/delete
      const target = event.target as any;
      target._oldValue = target.value;
    });
    input.addEventListener("blur", (event) => {
      const target = event.target as any;
      if (!document.hasFocus() || target._oldValue === undefined) {
        // no-op when user goes to another window or the input field has no backed-up value
        return;
      }
      if (target._oldValue !== target.value) {
        target.datepicker.setDate(target._oldValue);
      }
      delete target._oldValue;
    });
  }

  get inputElement(): HTMLInputElement {
    return this.querySelector(".input") as HTMLInputElement;
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-date", DatePicker);
});
