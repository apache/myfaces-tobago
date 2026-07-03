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

/**
 * Create a overlay barrier and animate it.
 */

import {Page} from "./tobago-page";
import {OverlayType} from "./tobago-overlay-type";
import {Css} from "./tobago-css";

export class Overlay extends HTMLElement {

  /**
   * Enhance or create faces.ajax.RequestOptions to use an overlay for the Ajax request.
   */
  static getEnhancedRequestOptions(options: faces.ajax.RequestOptions = {}): faces.ajax.RequestOptions {
    const renderIds = new Set<string>();
    if (options.render) {
      for (const id of options.render.split(" ")) {
        renderIds.add(id);
      }
    }

    const currentOnEvent = options.onevent;
    options.onevent = (data: faces.AjaxEvent) => {
      if (currentOnEvent) {
        currentOnEvent(data);
      }

      if (data.status === "begin") {
        for (const renderId of renderIds) {
          Overlay.render(renderId, OverlayType.ajax);
        }
      } else if (data.status === "success") {
        for (const renderId of renderIds) {
          Overlay.remove(renderId);
        }
      }
    };
    const currentOnError = options.onerror;
    options.onerror = (data: faces.AjaxError) => {
      if (currentOnError) {
        currentOnError(data);
      }

      for (const renderId of renderIds) {
        Overlay.render(renderId, OverlayType.error);
      }
    };
    return options;
  }

  static render(id: string, type: OverlayType, delay: number = Page
      .page(document.querySelector("tobago-page")).waitOverlayDelayAjax): void {
    const element = document.getElementById(id);

    const currentOverlay = element.querySelector(":scope > tobago-overlay");
    const currentOverlayType: OverlayType = currentOverlay?.getAttribute("type") as OverlayType;

    if (currentOverlay === null) {
      element.insertAdjacentHTML("beforeend", Overlay.htmlText(id, type,
          delay ? delay : Page.page(document.querySelector("tobago-page")).waitOverlayDelayAjax));
    } else if (currentOverlayType !== type) {
      currentOverlay?.remove();
      element.insertAdjacentHTML("beforeend", Overlay.htmlText(id, type, 0));
    }
  }

  static remove(id: string): void {
    const element = document.getElementById(id);
    const currentOverlay = element?.querySelector(":scope > tobago-overlay");
    currentOverlay?.remove();
  }

  static htmlText(id: string, type: OverlayType, delay: number): string {
    return `<tobago-overlay type='${type}' for='${id}' delay='${delay}' class='fade${type === OverlayType.error
        ? " text-danger" : ""}'/>`;
  }

  private timeout;
  private resizeObserver: ResizeObserver;

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.resizeObserver = new ResizeObserver(() => this.updatePosition());
    this.resizeObserver.observe(this.forElement);
    this.resizeObserver.observe(document.body); //indicates size change of other elements or browser window
    this.timeout = setTimeout(this.render.bind(this), this.delay);
  }

  disconnectedCallback() {
    clearTimeout(this.timeout);
    this.resizeObserver.disconnect();
    this.showScrollbar();
  }

  render(): void {
    let icon;
    switch (this.type) {
      case OverlayType.error:
        icon = "<i class='bi-exclamation-octagon fs-1'></i>";
        break;
      case OverlayType.dropZone:
        icon = "<i class='bi-upload fs-1'></i>";
        break;
      case OverlayType.submit:
        this.hideScrollbar();
      case OverlayType.ajax:
        icon = "<span class='spinner-border text-primary'></span>";
        break;
      default:
        icon = "";
    }

    this.insertAdjacentHTML("afterbegin", icon);
    this.updatePosition();
    this.classList.add(Css.SHOW);
  }

  private updatePosition(): void {
    this.style.top = "0px";
    this.style.left = "0px";

    const target = this.forElement.tagName === "TOBAGO-POPUP"
        ? this.forElement.querySelector(".modal-dialog .modal-content")
        : this.forElement;

    const forRect = target.getBoundingClientRect();
    const forStyle = getComputedStyle(target);
    const thisRect = this.getBoundingClientRect();

    this.style.top = (forRect.top - thisRect.top) + "px";
    this.style.left = (forRect.left - thisRect.left) + "px";
    this.style.width = forRect.width + "px";
    this.style.height = forRect.height + "px";
    this.style.borderTopLeftRadius = forStyle.borderTopLeftRadius;
    this.style.borderTopRightRadius = forStyle.borderTopRightRadius;
    this.style.borderBottomLeftRadius = forStyle.borderBottomLeftRadius;
    this.style.borderBottomRightRadius = forStyle.borderBottomRightRadius;
  }

  private hideScrollbar() {
    document.body.style.overflow = "hidden";
    document.body.style.paddingRight = `${this.scrollbarWidth}px`;
  }

  private showScrollbar() {
    document.body.style.overflow = null;
    document.body.style.paddingRight = null;
  }

  get scrollbarWidth(): number {
    return window.innerWidth - document.documentElement.clientWidth;
  }

  get forElement(): HTMLElement {
    return document.getElementById(this.for);
  }

  get for(): string {
    return this.getAttribute("for");
  }

  set for(forString: string) {
    this.setAttribute("for", forString);
  }

  /**
   * Examples:
   * wait, error, ajax, drop-zone
   */
  get type(): string {
    const attribute = this.getAttribute("type");
    if (attribute) {
      return attribute;
    } else {
      return "wait";
    }
  }

  set type(type: string) {
    this.setAttribute("type", type);
  }

  /**
   * The delay for the wait overlay. If not set the default delay is read from config.
   */
  get delay(): number {
    if (this.hasAttribute("delay")) {
      return parseInt(this.getAttribute("delay"));
    } else if (this.type === "ajax") {
      return Page.page(this).waitOverlayDelayAjax;
    } else {
      return Page.page(this).waitOverlayDelayFull;
    }
  }

  set delay(delay: number) {
    this.setAttribute("delay", String(delay));
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-overlay") == null) {
    window.customElements.define("tobago-overlay", Overlay);
  }
});
