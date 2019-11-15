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

import {DatePickerLightElement} from "@vaadin/vaadin-date-picker/vaadin-date-picker-light";
// @ts-ignore
import moment = require("moment");
import {Page} from "./tobago-page";

interface VaadinDate {
  day: number;
  month: number;
  year: number;
}

interface VaadinDatePickerI18n {
  week: string;
  calendar?: string;
  clear: string;
  today: string;
  cancel: string;
  firstDayOfWeek: number;
  monthNames: string[];
  weekdays: string[];
  weekdaysShort: string[];
  formatDate: (date: VaadinDate) => string;
  formatTitle: (monthName: string, fullYear: string) => string;
  parseDate: (dateString: string) => VaadinDate;
}

class DatePicker extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    let vaadinDatePicker = document.createElement("vaadin-date-picker-light") as DatePickerLightElement;
    vaadinDatePicker.setAttribute("attr-for-value", "value");
    let input = this.inputElement;
    const i18n = input.dataset.tobagoDateTimeI18n ? JSON.parse(input.dataset.tobagoDateTimeI18n) : undefined;
    vaadinDatePicker.i18n = this.createVaadinI18n(i18n);
    vaadinDatePicker.readonly = input.hasAttribute("readonly"); // todo make attribute
    vaadinDatePicker.showWeekNumbers = true; // tbd

    while (this.childNodes.length) {
      vaadinDatePicker.appendChild(this.firstChild);
    }
    this.appendChild(vaadinDatePicker);
  }

  /*
Get the pattern from the "Java world" (http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
and convert it to 'moment.js'.
Attention: Not every pattern char is supported.
*/
  analyzePattern(): string {
    const originalPattern = this.inputElement.dataset.tobagoPattern;

    let pattern;
    if (!originalPattern || originalPattern.length > 100) {
      console.warn("Pattern not supported: " + originalPattern);
      pattern = "";
    } else {
      pattern = originalPattern;
    }

    let analyzedPattern = "";
    let nextSegment = "";
    let escMode = false;
    for (let i = 0; i < pattern.length; i++) {
      const currentChar = pattern.charAt(i);
      if (currentChar == "'" && escMode == false) {
        escMode = true;
        analyzedPattern += this.analyzePatternPart(nextSegment);
        nextSegment = "";
      } else if (currentChar == "'" && pattern.charAt(i + 1) == "'") {
        if (escMode) {
          nextSegment += "\\";
        }
        nextSegment += "'";
        i++;
      } else if (currentChar == "'" && escMode == true) {
        escMode = false;
        analyzedPattern += nextSegment;
        nextSegment = "";
      } else {
        if (escMode) {
          nextSegment += "\\";
        }
        nextSegment += currentChar;
      }
    }
    if (nextSegment != "") {
      if (escMode) {
        analyzedPattern += nextSegment;
      } else {
        analyzedPattern += this.analyzePatternPart(nextSegment);
      }
    }

    return analyzedPattern;
  }

  analyzePatternPart(originalPattern: string): string {

    let pattern = originalPattern;

    if (pattern.search("G") > -1 || pattern.search("W") > -1 || pattern.search("F") > -1
        || pattern.search("K") > -1 || pattern.search("z") > -1 || pattern.search("X") > -1) {
      console.warn("Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported: " + pattern);
      pattern = "";
    }

    if (pattern.search("y") > -1) {
      pattern = pattern.replace(/y/g, "Y");
    }
    if (pattern.search("Y") > -1) {
      pattern = pattern.replace(/\bY\b/g, "YYYY");
      pattern = pattern.replace(/\bYYY\b/g, "YY");
      pattern = pattern.replace(/YYYYYY+/g, "YYYYY");
    }

    if (pattern.search("MMMMM") > -1) {
      pattern = pattern.replace(/MMMMM+/g, "MMMM");
    }

    if (pattern.search("w") > -1) {
      pattern = pattern.replace(/\bw\b/g, "W");
      pattern = pattern.replace(/www+/g, "WW");
    }

    if (pattern.search("D") > -1) {
      pattern = pattern.replace(/DDD+/g, "DDDD");
      pattern = pattern.replace(/\bD{1,2}\b/g, "DDD");
    }

    if (pattern.search("d") > -1) {
      pattern = pattern.replace(/dd+/g, "DD");
      pattern = pattern.replace(/\bd\b/g, "D");
    }

    if (pattern.search("E") > -1) {
      pattern = pattern.replace(/\bE{1,3}\b/g, "dd");
      pattern = pattern.replace(/EEEE+/g, "dddd");
    }

    if (pattern.search("u") > -1) {
      pattern = pattern.replace(/u+/g, "E");
    }
    if (pattern.search("a") > -1) {
      pattern = pattern.replace(/a+/g, "A");
    }
    if (pattern.search("HHH") > -1) {
      pattern = pattern.replace(/HHH+/g, "HH");
    }
    if (pattern.search("kkk") > -1) {
      pattern = pattern.replace(/kkk+/g, "kk");
    }
    if (pattern.search("hhh") > -1) {
      pattern = pattern.replace(/hhh+/g, "hh");
    }
    if (pattern.search("mmm") > -1) {
      pattern = pattern.replace(/mmm+/g, "mm");
    }
    if (pattern.search("sss") > -1) {
      pattern = pattern.replace(/sss+/g, "ss");
    }
    if (pattern.search("SSSS") > -1) {
      pattern = pattern.replace(/SSSS+/g, "SSS");
    }
    if (pattern.search("Z") > -1) {
      pattern = pattern.replace(/\bZ\b/g, "ZZ");
      pattern = pattern.replace(/ZZZ+/g, "ZZ");
    }

    return pattern;
  }

  createVaadinI18n(i18n: any): VaadinDatePickerI18n {

    const pattern = this.analyzePattern();
    const locale = Page.page().locale;

    moment.updateLocale(locale, {
      months: i18n.monthNames,
      monthsShort: i18n.monthNamesShort,
      weekdays: i18n.dayNames,
      weekdaysShort: i18n.dayNamesShort,
      weekdaysMin: i18n.dayNamesMin,
      week: {
        dow: i18n.firstDay,
        doy: 7 + i18n.firstDay - i18n.minDays // XXX seems not to be supported by VaadinDatePicker: may file an issue!
      }
    });

    const localeData = moment.localeData(locale);
    return {

      cancel: i18n.cancel,
      clear: i18n.clear,
      firstDayOfWeek: localeData.firstDayOfWeek(),
      monthNames: localeData.months(),
      today: i18n.today,
      week: i18n.week,
      weekdays: localeData.weekdays(),
      weekdaysShort: localeData.weekdaysShort(),
      formatDate: (date: VaadinDate) => {
        return moment({
          date: date.day,
          month: date.month,
          year: date.year,
        })
            .locale(locale!)
            .format(pattern);
      },
      formatTitle: (monthName: string, fullYear: string) => `${monthName} ${fullYear}`,
      parseDate: (dateString: string) => {
        const date = moment(dateString, pattern, locale);
        return {
          day: date.date(),
          month: date.month(),
          year: date.year(),
        };
      },
    };
  }

  get inputElement(): HTMLInputElement {
    return this.querySelector(".input") as HTMLInputElement;
  }
}

// XXX switched on
document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-date", DatePicker);
});
