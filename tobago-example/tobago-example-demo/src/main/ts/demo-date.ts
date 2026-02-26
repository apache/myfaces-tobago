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

/*
  simple example of a quick typing extra input date field.
*/
document.addEventListener("DOMContentLoaded", function (event: Event): void {
  document.querySelectorAll("tobago-date input[data-quick-pattern]").forEach(input => {
    console.debug("quick-pattern found for id=", input.id);
    const quickPattern = (input as HTMLInputElement).dataset.quickPattern as string;
    let regexp;
    switch (quickPattern) {
      case "ddmm":
        regexp = "[0-3][0-9][0-1][0-9]";
        break;
      case "mmdd":
        regexp = "[0-1][0-9][0-3][0-9]";
        break;
      default:
        console.error("Unsupported pattern", quickPattern);
        return;
    }
    const quick = document.createElement("input") as HTMLInputElement;
    quick.id = input.id + "::quick";
    quick.type = "text";
    quick.className = "form-control";
    quick.style.maxWidth = "5em";
    quick.placeholder = quickPattern;
    quick.pattern = regexp;
    quick.setAttribute("targetId", input.id);
    input.insertAdjacentElement("beforebegin", quick);

    quick.addEventListener("blur", (event => {
      const quick = event.currentTarget as HTMLInputElement;
      const value = quick.value;
      let day, month, year;

      if (value.length == 4) {
        day = Number.parseInt(value.substring(0, 2));
        month = Number.parseInt(value.substring(2, 4));
        year = new Date().getFullYear();
      }

      const string = `${year}-${month < 10 ? "0" + month : month}-${day < 10 ? "0" + day : day}`;
      console.info("date ->", string);
      const input = document.getElementById(quick.getAttribute("targetId")) as HTMLInputElement;
      input.value = string;

    }));

  });
});
