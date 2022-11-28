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

import {Collapse} from "./tobago-popup";
import {Page} from "./tobago-page";
import {CollapseOperation} from "./tobago-collapsible-operation";
import {BehaviorMode} from "./tobago-behavior-mode";
import {Overlay} from "./tobago-overlay";
import {OverlayType} from "./tobago-overlay-type";

class Behavior extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    switch (this.event) {
      case "load": // this is a special case, because the "load" is too late now.
        this.callback();
        break;
      case "resize":
        document.body.addEventListener(this.event, this.callback.bind(this));
        break;
      default: {
        const eventElement = this.eventElement;
        if (eventElement) {
          eventElement.addEventListener(this.event, this.callback.bind(this));
        } else {
          // if the clientId doesn't exists in DOM, it's probably the id of tobago-behavior custom element
          this.parentElement.addEventListener(this.event, this.callback.bind(this));
          // todo: not sure if this warning can be removed;
          console.warn("Can't find an element for the event. Use parentElement instead.", this);
        }
      }
    }
  }

  callback(event?: Event): void {

    if (this.confirmation) {
      if (!window.confirm(this.confirmation)) {
        event?.preventDefault();
        return;
      }
    }

    if (this.collapseOperation && this.collapseTarget) {
      const rootNode = this.getRootNode() as ShadowRoot | Document;
      Collapse.execute(this.collapseOperation, rootNode.getElementById(this.collapseTarget), this.mode);
    }

    switch (this.mode) {
      case BehaviorMode.ajax:
        if (this.render) {
          // prepare overlay for all by AJAX reloaded elements
          const partialIds = this.render.split(" ");
          for (let i = 0; i < partialIds.length; i++) {
            const partialId = partialIds[i];
            const partialElement = document.getElementById(partialId);
            if (partialElement) {
              let id: string;
              if (partialElement.tagName === "TOBAGO-POPUP") {
                // popup needs no overlay, is has no area to show
                id = partialElement.querySelector(".modal-dialog").id;
              } else {
                id = partialElement.id;
              }
              partialElement.insertAdjacentHTML("beforeend",
                  Overlay.htmlText(id, OverlayType.wait, Page.page(this).waitOverlayDelayAjax));
            } else {
              console.warn("No element found by id='%s' for overlay!", partialId);
            }
          }
        }
        jsf.ajax.request(
            this.actionElement,
            event,
            {
              "jakarta.faces.behavior.event": this.event,
              execute: this.execute,
              render: this.render
            });
        break;
      case BehaviorMode.full:
        setTimeout(this.submit.bind(this), this.delay);
        break;
      default:
        // nothing to do

    }
  }

  /**
   * Submitting the page (= the form).
   */
  submit(): void {
    console.info("Execute submit!");
    const page = Page.page(this);
    if (!page.submitActive) {
      page.submitActive = true;
      const actionId = this.fieldId != null ? this.fieldId : this.clientId;
      const form = page.form;
      const oldTarget = form.getAttribute("target");
      const sourceHidden = document.getElementById("jakarta.faces.source") as HTMLInputElement;
      sourceHidden.disabled = false;
      sourceHidden.value = actionId;
      if (this.target != null) {
        form.setAttribute("target", this.target);
      }

      page.beforeSubmit(null, this.decoupled || this.target != null);

      try {
        form.submit();
        // reset the source field after submit, to be prepared for possible next AJAX with decoupled=true
        sourceHidden.disabled = true;
        sourceHidden.value = "";
      } catch (e) {
        console.error("Submit failed!", e);
        const overlay = this.closest("body").querySelector(`tobago-overlay[for='${page.id}']`);
        overlay.remove();
        page.submitActive = false;
        alert("Submit failed!"); // XXX localization, better error handling
      }
      if (this.target) {
        if (oldTarget) {
          form.setAttribute("target", oldTarget);
        } else {
          form.removeAttribute("target");
        }
      }
      if (this.target || this.decoupled) {
        page.submitActive = false;
      }
    }
  }

  get mode(): BehaviorMode {
    if (this.render || this.execute) {
      return BehaviorMode.ajax;
    } else if (!this.omit) {
      return BehaviorMode.full;
    } else {
      return BehaviorMode.client;
    }
  }

  get event(): string {
    return this.getAttribute("event");
  }

  set event(event: string) {
    this.setAttribute("event", event);
  }

  get clientId(): string {
    return this.getAttribute("client-id");
  }

  set clientId(clientId: string) {
    this.setAttribute("client-id", clientId);
  }

  get fieldId(): string {
    return this.getAttribute("field-id");
  }

  set fieldId(fieldId: string) {
    this.setAttribute("field-id", fieldId);
  }

  get execute(): string {
    return this.getAttribute("execute");
  }

  set execute(execute: string) {
    this.setAttribute("execute", execute);
  }

  get render(): string {
    return this.getAttribute("render");
  }

  set render(render: string) {
    this.setAttribute("render", render);
  }

  get delay(): number {
    return parseInt(this.getAttribute("delay")) || 0;
  }

  set delay(delay: number) {
    this.setAttribute("delay", String(delay));
  }

  get omit(): boolean {
    return this.hasAttribute("omit");
  }

  set omit(omit: boolean) {
    if (omit) {
      this.setAttribute("omit", "");
    } else {
      this.removeAttribute("omit");
    }
  }

  get target(): string {
    return this.getAttribute("target");
  }

  set target(target: string) {
    this.setAttribute("target", target);
  }

  get confirmation(): string {
    return this.getAttribute("confirmation");
  }

  set confirmation(confirmation: string) {
    this.setAttribute("confirmation", confirmation);
  }

  get collapseOperation(): CollapseOperation {
    return CollapseOperation[this.getAttribute("collapse-operation")];
  }

  set collapseOperation(collapseOperation: CollapseOperation) {
    this.setAttribute("collapse-operation", CollapseOperation[collapseOperation]);
  }

  get collapseTarget(): string {
    return this.getAttribute("collapse-target");
  }

  set collapseTarget(collapseTarget: string) {
    this.setAttribute("collapse-target", collapseTarget);
  }

  get decoupled(): boolean {
    return this.hasAttribute("decoupled");
  }

  set decoupled(decoupled: boolean) {
    if (decoupled) {
      this.setAttribute("decoupled", "");
    } else {
      this.removeAttribute("decoupled");
    }
  }

  get actionElement(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    const id = this.clientId;
    return rootNode.getElementById(id);
  }

  get eventElement(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    const id = this.fieldId ? this.fieldId : this.clientId;
    let result = rootNode.getElementById(id);
    if (result == null) {
      if (this.parentElement.tagName === "TD") {
        // if <tc:event> is inside <tc:row> the <tobago-behaviour> is rendered inside a <td>, because it's not
        // allowed directly inside a <tr>.
        result = this.parentElement.parentElement;
        // XXX this might not be a good solution, better fix this in the SheetRenderer
      }
    }
    return result;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-behavior") == null) {
    window.customElements.define("tobago-behavior", Behavior);
  }
});
