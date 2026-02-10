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

import {Collapse} from "./tobago-collapse";
import {File} from "./tobago-file";
import {Page} from "./tobago-page";
import {CollapseOperation} from "./tobago-collapse-operation";
import {BehaviorMode} from "./tobago-behavior-mode";
import {Overlay} from "./tobago-overlay";
import {OverlayType} from "./tobago-overlay-type";

class Behavior extends HTMLElement {
  private eventListener: EventListenerOrEventListenerObject;

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
        this.eventListener = this.callback.bind(this);

        if (eventElement) {
          eventElement.addEventListener(this.event, this.eventListener);
        } else {
          // if the clientId doesn't exist in DOM, it's probably the id of tobago-behavior custom element
          this.parentElement.addEventListener(this.event, this.eventListener);
          console.debug("Can't find an element for the event. Use parentElement instead.", this);
        }
      }
    }
  }

  disconnectedCallback(): void {
    const eventElement = this.eventElement;
    if (eventElement) {
      eventElement.removeEventListener(this.event, this.eventListener);
    } else {
      this.parentElement?.removeEventListener(this.event, this.eventListener);
    }
  }

  callback(event?: Event): void {
    if (this.stopPropagation) {
      event.stopPropagation();
    }

    if (this.confirmation) {
      if (!window.confirm(this.confirmation)) {
        event?.preventDefault();
        return;
      }
    }

    switch (this.mode) {
      case BehaviorMode.ajax:
        if (!this.immediate) {
          const valid = this.validate(this.execute);
          if (!valid) {
            console.warn("Validation failed! No request will be sent.");
            return;
          }
        }
        break;
      case BehaviorMode.full:
        // todo: not implemented/activated yet, because this implementation doesn't support sub-forms
        // if (!this.validate(Page.page(this).id)) {
        //   console.warn("Validation failed! No request will be sent.");
        //   return;
        // }
        break;
      default:
        // nothing to do
    }

    if (event) {
      event.currentTarget.dispatchEvent(new CustomEvent(this.customEventName, {bubbles: true}));
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
          for (const partialId of partialIds) {
            const partialElement = document.getElementById(partialId);
            if (partialElement) {
              let id: string;
              if (partialElement.tagName === "TOBAGO-POPUP") {
                // popup needs no overlay, it has no area to show
                id = partialElement.querySelector(".modal-dialog").id;
              } else {
                id = partialElement.id;
              }
              partialElement.insertAdjacentHTML("beforeend",
                  Overlay.htmlText(id, OverlayType.ajax, Page.page(this).waitOverlayDelayAjax));
            } else {
              console.warn("No element found by id='%s' for overlay!", partialId);
            }
          }
        }
        faces.ajax.request(
            this.actionElement,
            event,
            {
              params: {
                "jakarta.faces.behavior.event": this.event
              },
              execute: this.execute,
              render: this.render,
              resetValues: this.resetValues,
              delay: this.delay,
              onevent: (event: EventData) => {
                // console.debug(`Status: ${event.status} - Type: ${event.type}`);
              },
              myfaces: {
                upload: {
                  progress: (upload: XMLHttpRequestUpload, event: ProgressEvent) => {
                    // console.debug(`progress: ${event.loaded} of ${event.total}`);
                    if (this.actionElement?.tagName === "TOBAGO-FILE") {
                      (this.actionElement as File).updateProgress(event.loaded, event.total);
                    }
                  },
                  preinit: (upload: XMLHttpRequestUpload) => {
                    // console.debug(`preinit`);
                  },
                  loadstart: (upload: XMLHttpRequestUpload, event: ProgressEvent) => {
                    // console.debug(`loadstart: ${event.loaded} of ${event.total}`);
                    if (this.actionElement?.tagName === "TOBAGO-FILE") {
                      (this.actionElement as File).startProgress(event.loaded, event.total);
                    }
                  },
                  load: (upload: XMLHttpRequestUpload, event: ProgressEvent) => {
                    // console.debug(`load: ${event.loaded} of ${event.total}`);
                  },
                  loadend: (upload: XMLHttpRequestUpload, event: ProgressEvent) => {
                    // console.debug(`loadend: ${event.loaded} of ${event.total}`);
                    if (this.actionElement?.tagName === "TOBAGO-FILE") {
                      (this.actionElement as File).finishProgress();
                    }
                  },
                  error: (upload: XMLHttpRequestUpload, event: ProgressEvent) => {
                    // console.debug(`error: ${event.loaded} of ${event.total}`);
                  },
                  abort: (upload: XMLHttpRequestUpload, event: ProgressEvent) => {
                    // console.debug(`abort: ${event.loaded} of ${event.total}`);
                  },
                  timeout: (upload: XMLHttpRequestUpload, event: ProgressEvent) => {
                    // console.debug(`timeout: ${event.loaded} of ${event.total}`);
                  }
                }
              }
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

  validate(ids: string): boolean {
    let allValid = true;
    if (ids) {
      // console.debug("Is all valid? ids=", ids);
      for (const id of ids.split(/\s+/)) {
        const element = document.getElementById(id);
        if (element) {
          // todo: all elements (needs to be tested very well)
          // const formElements = element.querySelectorAll<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>(
          //     "input, textarea, select"
          const formElements = element.querySelectorAll<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>(
              "input[type=file]"
          );
          for (const formElement of formElements) {
            if (!formElement.checkValidity()) {
              // console.debug("invalid", formElement.id);
              allValid = false;
            } else {
              // console.debug("valid", formElement.id);
            }
          }
        }
      }
      // console.debug("Is all valid?", allValid);
      return allValid;
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

  get resetValues(): boolean {
    return this.hasAttribute("reset-values");
  }

  set resetValues(resetValues: boolean) {
    if (resetValues) {
      this.setAttribute("reset-values", "");
    } else {
      this.removeAttribute("reset-values");
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

  get immediate(): boolean {
    return this.hasAttribute("immediate");
  }

  set immediate(immediate: boolean) {
    if (immediate) {
      this.setAttribute("immediate", "");
    } else {
      this.removeAttribute("immediate");
    }
  }

  get stopPropagation(): boolean {
    return this.hasAttribute("stop-propagation");
  }

  get customEventName(): string {
    return this.getAttribute("custom-event-name");
  }

  get actionElement(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    const id = this.clientId;
    if (typeof rootNode.getElementById === "function") {
      return rootNode.getElementById(id);
    } else {
      return null;
    }
  }

  get eventElement(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    if (rootNode instanceof Document || rootNode instanceof ShadowRoot) {
      //disconnectedCallback: getElementById may not available, because rootNode is an instance ofHTMLElement

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

    return null;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-behavior") == null) {
    window.customElements.define("tobago-behavior", Behavior);
  }
});
