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

class DemoAlert extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback() {
    this.addEventListener("click", this.alert.bind(this));
  }

  alert() {
    window.alert(this.value);
  }

  get value() {
    return this.getAttribute("value");
  }
}

document.addEventListener("DOMContentLoaded", function (event) {
  if (window.customElements.get("demo-alert") == null) {
    window.customElements.define("demo-alert", DemoAlert);
  }
});

class DemoHighlight extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback() {
    if (this.language) {
      this.innerHTML = `<pre><code class="language-${this.language}">${this.innerHTML.trim()}</demo-highlight>`;
    } else {
      this.innerHTML = `<pre><code>${this.innerHTML.trim()}</demo-highlight>`;
    }
    Prism.highlightElement(this.querySelector("code"));
  }

  get language() {
    return this.getAttribute('language');
  }
}

document.addEventListener("DOMContentLoaded", function (event) {
  if (window.customElements.get("demo-highlight") == null) {
    window.customElements.define("demo-highlight", DemoHighlight);
  }
});

class DemoSearch extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback() {
    const input = this.input;
    const a = this.a;
    if (input && a) {
      a.setAttribute("href", this.google());
      input.addEventListener("change", this.change.bind(this));
      input.addEventListener("keypress", this.keypress.bind(this));
    }
  }

  google() {
    return "https://www.google.com/search?q=site%3Atobago-vm.apache.org+demo-4-release+";
  }

  change(event) {
    this.a.href = this.google() + encodeURI(this.input.value);
  }

  keypress(event) {
    if (event.which === 13) {
      this.a.href = this.google() + encodeURI(this.input.value);
    }
  }

  get input() {
    return this.querySelector("input");
  }

  get a() {
    return this.querySelector("a");
  }
}

document.addEventListener("DOMContentLoaded", function (event) {
  if (window.customElements.get("demo-search") == null) {
    window.customElements.define("demo-search", DemoSearch);
  }
});

class Demo {

  static initInspect(element) {

    for (const code of element.querySelectorAll("code")) {
      for (const br of code.querySelectorAll("br")) {
        br.parentNode.insertBefore("\n", br);
        br.parentNode.removeChild(br);
      }
    }

    for (const e of element.querySelectorAll("tobago-in")) {

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
}

document.addEventListener("DOMContentLoaded", function (event) {
  let element = document; // XXX fixme
  // Demo.initInspect(element); //TODO fix inspection
  Demo.initTestLinks(element);
  Demo.initTestFrame(element);
});

// XXX init areas after JSF AJAX update not implemented yet!

// call highlighting again. (is called for all, not only for the elements, because it's easier to implement.)
// if (window.location.pathname !== "/test.xhtml") { // not in the test framework
//   Listener.register(Prism.highlightAll, Phase.AFTER_UPDATE);
// }
