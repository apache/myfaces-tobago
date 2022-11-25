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

import {Page} from "./tobago-page";
import {Config} from "./tobago-config";

export class Focus extends HTMLElement {

  constructor() {
    super();
  }

  /**
   * The focusListener to set the lastFocusId must be implemented in the appropriate web elements.
   * @param event
   */
  static setLastFocusId(event: FocusEvent): void {
    const target = event.target as HTMLElement;
    const computedStyle = getComputedStyle(target);

    if (target.getAttribute("type") !== "hidden"
        && target.getAttributeNames().indexOf("disabled") === -1
        && target.getAttribute("tabindex") !== "-1"
        && computedStyle.visibility !== "hidden"
        && computedStyle.display !== "none") {
      const root = target.getRootNode() as ShadowRoot | Document;
      const tobagoFocus = root.getElementById(Page.page(target).id + "::lastFocusId");
      tobagoFocus.querySelector("input").value = target.id;
    }
  }

  /**
   * Sets the focus to the requested element or to the first possible if
   * no element is explicitly requested.
   *
   * The priority order is:
   * - error (the first error element gets the focus)
   * - auto (the element with the tobago tag attribute focus="true" gets the focus)
   * - last (the element from the last request with same id gets the focus, not AJAX)
   * - first (the first input element (without tabindex=-1) gets the focus, not AJAX)
   */
  connectedCallback(): void {
    if (Page.page(this).focusOnError) {
      const errorElement = this.errorElement;
      if (errorElement) {
        errorElement.focus();
        return;
      }
    }

    if (this.autofocusElements.length > 0) {
      // nothing to do, because the browser make the work.
      return;
    }

    const lastFocusedElement = this.lastFocusedElement;
    if (lastFocusedElement) {
      lastFocusedElement.focus();
      return;
    }

    const focusableElement = this.focusableElement;
    if (focusableElement) {
      focusableElement.focus();
      return;
    }
  }

  private get errorElement(): HTMLElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    const elements: NodeListOf<HTMLElement> = root.querySelectorAll(
        ".tobago-messages-container .border-danger:not([disabled]):not([tabindex='-1'])");

    for (const element of elements) {
      const computedStyle = getComputedStyle(element);
      if (computedStyle.display !== "none" && computedStyle.visibility !== "hidden") {
        return element;
      }
    }
  }

  private get autofocusElements(): NodeListOf<HTMLElement> {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelectorAll("[autofocus]");
  }

  private get lastFocusedElement(): HTMLElement {
    const lastFocusId: string = this.hiddenInput.value;
    if (lastFocusId) {
      const root = this.getRootNode() as ShadowRoot | Document;
      return root.getElementById(lastFocusId);
    } else {
      return null;
    }
  }

  private get hiddenInput(): HTMLInputElement {
    return this.querySelector("input");
  }

  private get focusableElement(): HTMLElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    const elements: NodeListOf<HTMLElement> = root.querySelectorAll(
      "input:not([type='hidden']):not([disabled]):not([tabindex='-1']),"
      + "select:not([disabled]):not([tabindex='-1']),textarea:not([disabled]):not([tabindex='-1'])");

    for (const element of elements) {
      if (this.isVisible(element)) {
        return element;
      }
    }
  }

  private isVisible(element: HTMLElement): boolean {
    const computedStyle = getComputedStyle(element);
    if (computedStyle.display === "none" || computedStyle.visibility === "hidden") {
      return false;
    } else if (element.parentElement) {
      return this.isVisible(element.parentElement);
    } else {
      return true;
    }
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-focus") == null) {
    window.customElements.define("tobago-focus", Focus);
  }
});
