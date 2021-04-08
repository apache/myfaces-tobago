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

import {Listener, Phase} from "./tobago-listener";
import {Focus} from "./tobago-focus";
import {Suggest} from "./tobago-suggest";

export class In extends HTMLElement {
  constructor() {
    super();
  }

  connectedCallback(): void {
    this.input.addEventListener("focus", Focus.setLastFocusId);
    if (this.querySelector("tobago-suggest")) {
      const suggest = new Suggest(this);
      suggest.init();
    }
  }

  get input(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(this.id + "::field") as HTMLInputElement;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-in") == null) {
    window.customElements.define("tobago-in", In);
  }
});

// XXX regexp example only - blueprint
class RegExpTest {

  private readonly element: HTMLInputElement;
  private readonly regexp: RegExp;

  // todo: use "custom-elements" instead of this init listener
  static init(element: HTMLElement): void {
    for (const input of RegExpTest.selfOrElementsByClassName(element, "tobago-in")) { // todo only for data-regexp
      new RegExpTest(input as HTMLInputElement);
    }
  }

  /**
   * Find all elements (and also self) which have the class "className".
   * @param element Starting element in DOM to collect.
   * @param className Class of elements to find.
   */
  static selfOrElementsByClassName(element: HTMLElement, className: string): Array<HTMLElement> {
    const result: Array<HTMLElement> = new Array<HTMLElement>();
    if (!element) {
      element = document.documentElement;
    }
    if (element.classList.contains(className)) {
      result.push(element);
    }
    const list = element.getElementsByClassName(className);
    for (let i = 0; i < list.length; i++) {
      result.push(list.item(i) as HTMLElement);
    }
    return result;
  }

  constructor(element: HTMLInputElement) {

    this.element = element;
    this.regexp = new RegExp(this.element.dataset.regexp);

    console.debug("constructor: '%s'", element.id);

    this.element.addEventListener("change", this.checkValue.bind(this));
  }

  checkValue(event: TextEvent): void {
    console.debug("changed: check if '%s' is okay!", this.regexp.toString());
    if (!this.regexp.test(this.element.value)) {
      this.element.classList.add("border-danger");
    } else {
      this.element.classList.remove("border-danger");
    }
  }
}

Listener.register(RegExpTest.init, Phase.DOCUMENT_READY);
Listener.register(RegExpTest.init, Phase.AFTER_UPDATE);
