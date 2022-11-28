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

import {Overlay} from "./tobago-overlay";
import {OverlayType} from "./tobago-overlay-type";

export class Page extends HTMLElement {

  submitActive = false;

  /**
   * The Tobago root element
   */
  static page(element: HTMLElement): Page {
    const rootNode = element.getRootNode() as ShadowRoot | Document;
    const pages = rootNode.querySelectorAll("tobago-page");
    if (pages.length > 0) {
      if (pages.length >= 2) {
        console.warn("Found more than one tobago-page element!");
      }
      return pages.item(0) as Page;
    }
    console.warn("Found no tobago page!");
    return null;
  }

  /**
   * "a:b" -> "a"
   * "a:b:c" -> "a:b"
   * "a" -> null
   * null -> null
   * "a:b::sub-component" -> "a"
   * "a::sub-component:b" -> "a::sub-component" // should currently not happen in Tobago
   *
   * @param clientId The clientId of a component.
   * @return The clientId of the naming container.
   */
  static getNamingContainerId(clientId: string): string {
    if (clientId == null || clientId.lastIndexOf(":") === -1) {
      return null;
    }

    let id = clientId;
    while (true) {
      const sub = id.lastIndexOf("::");
      if (sub == -1) {
        break;
      }
      if (sub + 1 == id.lastIndexOf(":")) {
        id = id.substring(0, sub);
      } else {
        break;
      }
    }
    return id.substring(0, id.lastIndexOf(":"));
  }

  constructor() {
    super();
  }

  connectedCallback(): void {

    this.registerAjaxListener();

    this.form.addEventListener("submit", this.beforeSubmit.bind(this));

    window.addEventListener("unload", this.beforeUnload.bind(this));

    this.addEventListener("keypress", (event: KeyboardEvent): boolean => {
      let code = event.which; // XXX deprecated
      if (code === 0) {
        code = event.keyCode;
      }
      if (code === 13) {
        const target = event.target as HTMLElement;
        if (target.tagName === "A" || target.tagName === "BUTTON") {
          return;
        }
        if (target.tagName === "TEXTAREA") {
          if (!event.metaKey && !event.ctrlKey) {
            return;
          }
        }
        const name = target.getAttribute("name");
        let id = name ? name : target.id;
        while (id != null) {
          const command = document.querySelector(`[data-tobago-default='${id}']`);
          if (command) {
            command.dispatchEvent(new MouseEvent("click"));
            break;
          }
          id = Page.getNamingContainerId(id);
        }
        return false;
      }
    });
  }

  beforeSubmit(event: Event, decoupled = false): void {
    this.submitActive = true;
    if (!decoupled) {
      this.body.insertAdjacentHTML("beforeend",
          Overlay.htmlText(this.id, OverlayType.wait, this.waitOverlayDelayFull));
    }
    console.debug(this.body.querySelector("tobago-overlay"));
  }

  /**
   * Wrapper function to call application generated onunload function
   */
  beforeUnload(): void {
    console.debug("unload");
    // todo: here me may check, if user will loose its edit state on the page
  }

  registerAjaxListener(): void {
    jsf.ajax.addOnEvent(this.jsfResponse.bind(this));
  }

  jsfResponse(event: EventData): void {
    console.timeEnd("[tobago-jsf] jsf-ajax");
    console.time("[tobago-jsf] jsf-ajax");
    console.debug("[tobago-jsf] JSF event status: '%s'", event.status);
    if (event.status === "success") {
      event.responseXML.querySelectorAll("update").forEach(this.jsfResponseSuccess.bind(this));
    } else if (event.status === "complete") {
      event.responseXML.querySelectorAll("update").forEach(this.jsfResponseComplete.bind(this));
    }
  }

  jsfResponseSuccess(update: Element): void {
    const id = update.id;
    let rootNode = this.getRootNode() as ShadowRoot | Document;
    // XXX in case of "this" is tobago-page (e.g. ajax exception handling) rootNode is not set correctly???
    if (!rootNode.getElementById) {
      rootNode = document;
    }
    console.debug("[tobago-jsf] Update after jsf.ajax success: %s", id);
  }

  jsfResponseComplete(update: Element): void {
    const id = update.id;
    if (JsfParameter.isJsfId(id)) {
      console.debug("[tobago-jsf] Update after jsf.ajax complete: #", id);
      const overlay = this.querySelector(`tobago-overlay[for='${id}']`);
      if (overlay) {
        overlay.remove();
      } else {
        console.warn("Didn't found overlay for id", id);
      }
    }
  }

  get form(): HTMLFormElement {
    return this.querySelector("form");
  }

  get body(): HTMLBodyElement {
    return this.closest("body");
  }

  get locale(): string {
    let locale = this.getAttribute("locale");
    if (!locale) {
      locale = document.documentElement.lang;
    }
    return locale;
  }

  get focusOnError(): boolean {
    return this.getAttribute("focus-on-error") === "true";
  }

  set focusOnError(focusOnError: boolean) {
    this.setAttribute("focus-on-error", String(focusOnError));
  }

  get waitOverlayDelayFull(): number {
    return parseInt(this.getAttribute("wait-overlay-delay-full")) || 1000;
  }

  set waitOverlayDelayFull(waitOverlayDelayFull: number) {
    this.setAttribute("wait-overlay-delay-full", String(waitOverlayDelayFull));
  }

  get waitOverlayDelayAjax(): number {
    return parseInt(this.getAttribute("wait-overlay-delay-ajax")) || 1000;
  }

  set waitOverlayDelayAjax(waitOverlayDelayAjax: number) {
    this.setAttribute("wait-overlay-delay-ajax", waitOverlayDelayAjax.toString());
  }

}

document.addEventListener("tobago.init", (event: Event): void => {
  if (window.customElements.get("tobago-page") == null) {
    window.customElements.define("tobago-page", Page);
  }
});

class JsfParameter {

  static VIEW_STATE = "jakarta.faces.ViewState";
  static CLIENT_WINDOW = "jakarta.faces.ClientWindow";
  static VIEW_ROOT = "jakarta.faces.ViewRoot";
  static VIEW_HEAD = "jakarta.faces.ViewHead";
  static VIEW_BODY = "jakarta.faces.ViewBody";
  static RESOURCE = "jakarta.faces.Resource";

  static isJsfId(id: string): boolean {
    switch (id) {
      case JsfParameter.VIEW_STATE:
      case JsfParameter.CLIENT_WINDOW:
      case JsfParameter.VIEW_ROOT:
      case JsfParameter.VIEW_HEAD:
      case JsfParameter.VIEW_BODY:
      case JsfParameter.RESOURCE:
        return false;
      default:
        return true;
    }
  }

  static isJsfBody(id): boolean {
    switch (id) {
      case JsfParameter.VIEW_ROOT:
      case JsfParameter.VIEW_BODY:
        return true;
      default:
        return false;
    }
  }
}
