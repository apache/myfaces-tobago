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
      titleFormat: "MM y", // todo i18m
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
  }

  get inputElement(): HTMLInputElement {
    return this.querySelector(".input") as HTMLInputElement;
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-date", DatePicker);
});

export class DatePickerUtils {

  /*
  Get the pattern from the "Java world",
  see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/SimpleDateFormat.html
  and convert it to 'vanillajs-datepicker', see https://mymth.github.io/vanillajs-datepicker/#/date-string+format
  Attention: Not every pattern char is supported.
  */
  public static convertPattern(originalPattern: string): string {
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
        analyzedPattern += DateUtils.convertPatternPart(nextSegment);
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
        analyzedPattern += this.convertPatternPart(nextSegment);
      }
    }

    return analyzedPattern;
  }

  static convertPatternPart(originalPattern: string): string {

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
}
