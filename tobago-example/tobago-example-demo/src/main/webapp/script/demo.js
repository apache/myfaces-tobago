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

import {Listener, Phase} from "../tobago/standard/tobago-bootstrap/5.0.0-SNAPSHOT/js/tobago-listener";

class Demo {

  static init(element) {
    Demo.initAlert(element);
    // Demo.initInspect(element); //TODO fix inspection
    Demo.initTestLinks(element);
    Demo.initTestFrame(element);
    Demo.initGoogleSearch(element);
    Demo.initMailTo(element);
  }

  static initAlert(element) {
    for (const e of element.querySelectorAll("[data-alert-text]")) {
      e.addEventListener("click", function (event) {
        alert(event.currentTarget.dataset["alertText"]);
      });
    }
  }

  static initInspect(element) {

    for (const code of element.querySelectorAll("code")) {
      for (const br of code.querySelectorAll("br")) {
        br.parentNode.insertBefore("\n", br);
        br.parentNode.removeChild(br);
      }
    }

    for (const e of element.querySelectorAll(".tobago-flexLayout")) {

      // do highlighting with hovering only in the content-area
      if (e.closest("#page\\:content")) {
        e.addEventListener("hover", function (event) {

          // clear old selections:
          for (const selected of document.querySelectorAll(".demo-selected")) {
            selected.classList.remove("demo-selected");
          }

          const element = event.currentTarget;
          element.classList.add("demo-selected");
          const clientId = element.closest("[id]").id;
          const id = clientId.substr(clientId.lastIndexOf(":") + 1);
          const source = document.getElementById("demo-view-source");

          for (const span of source.querySelectorAll("span.token.attr-value")) {
            if (span.textContent === 'id=' + '"' + id + '"') {
              span.parentElement.classList.add("demo-selected");
            }
          }
        });
      }
    }
  }

  static initTestLinks(element) {
    const runLink = document.getElementById("page:header:runtest");
    if (runLink && parent.document.getElementById("qunit")) {
      runLink.classList.add("d-none");
    }
  }

  static initTestFrame(element) {
    const testFrame = document.getElementById("page:testframe");
    if (testFrame) {
      testFrame.addEventListener("onload", function () {
        element.height = element.contentWindow.body.scrollHeight;
      });
    }
  }

  static initGoogleSearch(element) {
    const input = document.getElementById("page:search:searchField");
    if (input) {
      const search = "+site%3Atobago-vm.apache.org+demo-4";
      input.addEventListener("change", function (event) {
        const input = event.currentTarget;
        const button = document.getElementById("page:search:searchCommand");
        button.setAttribute("href", "https://www.google.com/search?q=" + encodeURI(input.value) + search);
      });
      input.addEventListener("keypress", function (event) {
        if (event.which === 13) {
          console.log("ENTER");
          window.location.href = "https://www.google.com/search?q=" + encodeURI(input.value()) + search;
        }
      });
    }
  };

  static initMailTo(element) {
    for (let link of document.querySelectorAll("[href^=mailto]")) {
      // this is, to fix URL encoded spaces
      const string = link.getAttribute("href");
      const begin = string.indexOf("subject=");
      const href = string.substring(0, begin) + string.substring(begin).replace(/\+/g, "%20");
      link.setAttribute("href", href);
    }
  };

}

Listener.register(Demo.init, Phase.DOCUMENT_READY);
Listener.register(Demo.init, Phase.AFTER_UPDATE);

// call highlighting again. (is called for all, not only for the elements, because it's easier to implement.)
if (window.location.pathname !== "/test.xhtml") { // not in the test framework
  Listener.register(Prism.highlightAll, Phase.AFTER_UPDATE);
}
