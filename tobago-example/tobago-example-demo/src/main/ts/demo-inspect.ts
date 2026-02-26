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

// todo: this code is not tested

class DemoInspect {

  static initInspect(element: HTMLElement): void {

    for (const code of element.querySelectorAll("code")) {
      for (const br of code.querySelectorAll("br")) {
        br.parentNode.insertBefore(document.createTextNode("\n"), br);
        br.parentNode.removeChild(br);
      }
    }

    for (const e of element.querySelectorAll("tobago-in")) {

      // do highlighting with hovering only in the content-area
      if (e.closest("#page\\:content")) {
        e.addEventListener("hover", function (event: Event): void {

          // clear old selections:
          for (const selected of document.querySelectorAll(".demo-selected")) {
            selected.classList.remove("demo-selected");
          }

          const element = event.currentTarget as HTMLElement;
          element.classList.add("demo-selected");
          const clientId = element.closest("[id]").id;
          const id = clientId.substr(clientId.lastIndexOf(":") + 1);
          const source = document.getElementById("demo-view-source");

          for (const span of source.querySelectorAll("span.token.attr-value")) {
            if (span.textContent === `id="${id}"`) {
              span.parentElement.classList.add("demo-selected");
            }
          }
        });
      }
    }
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  const element = document.documentElement; // XXX fixme
  // XXX init areas after Faces AJAX update not implemented yet!
  DemoInspect.initInspect(element); //TODO fix inspection
});
