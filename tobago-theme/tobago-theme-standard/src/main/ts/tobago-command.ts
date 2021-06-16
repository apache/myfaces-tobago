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
import {Collapse} from "./tobago-popup";
import {Page} from "./tobago-page";
import {CollapseOperation} from "./tobago-collapsible-operation";
import {BehaviorMode} from "./tobago-behavior-mode";

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
      default:
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

  callback(event?: Event): void {

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
            const partialElement = document.getElementById(partialIds[i]);
            if (partialElement) {
              new Overlay(partialElement, true);
            } else {
              console.warn("No element found by id='%s' for overlay!", partialIds[i]);
            }
          }
        }
        jsf.ajax.request(
            this.actionElement,
            event,
            {
              "javax.faces.behavior.event": this.event,
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

  submit(): void {
    const id = this.fieldId != null ? this.fieldId : this.clientId;
    CommandHelper.submitAction(this, id, this.decoupled, this.target);
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
    return rootNode.getElementById(id);
  }

  /* XXX todo:
    get element(): HTMLElement {
      let e = this.parentElement;
      // XXX special case, using the row, but <tobago-behavior> can't be a child of <tr>
      while (e.matches("td.tobago-sheet-cell-markup-filler")
      // XXX fix position of <tobago-behavior> inside of input-group
      || e.matches(".input-group")
      || e.matches(".tobago-input-group-outer")) {
        e = e.parentElement;
      }
      return e;
    }
  */

}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-behavior") == null) {
    window.customElements.define("tobago-behavior", Behavior);
  }
});

export class CommandHelper {
  static isSubmit: boolean = false;

  /**
   * Submitting the page with specified actionId.
   * @param source
   * @param actionId
   * @param decoupled
   * @param target
   */
  public static submitAction = function (
      source: HTMLElement, actionId: string, decoupled: boolean = false, target?: string): void {

    Transport.request(function (): void {
      if (!CommandHelper.isSubmit) {
        CommandHelper.isSubmit = true;
        const form = document.getElementsByTagName("form")[0] as HTMLFormElement;
        const oldTarget = form.getAttribute("target");
        const sourceHidden = document.getElementById("javax.faces.source") as HTMLInputElement;
        sourceHidden.disabled = false;
        sourceHidden.value = actionId;
        if (target) {
          form.setAttribute("target", target);
        }
        const listenerOptions = {
          source: source,
          actionId: actionId/*,
          options: commandHelper*/
        };
        const onSubmitResult = CommandHelper.onSubmit(listenerOptions);
        if (onSubmitResult) {
          try {
            form.submit();
            // reset the source field after submit, to be prepared for possible next AJAX with decoupled=true
            sourceHidden.disabled = true;
            sourceHidden.value = "";
          } catch (e) {
            Overlay.destroy(Page.page(form).id);
            CommandHelper.isSubmit = false;
            alert("Submit failed: " + e); // XXX localization, better error handling
          }
        }
        if (target) {
          if (oldTarget) {
            form.setAttribute("target", oldTarget);
          } else {
            form.removeAttribute("target");
          }
        }
        if (target || decoupled || !onSubmitResult) {
          CommandHelper.isSubmit = false;
          Transport.pageSubmitted = false;
        }
      }
      if (!CommandHelper.isSubmit) {
        Transport.requestComplete(); // remove this from queue
      }
    }, true);
  };

  static onSubmit = function (listenerOptions: any): boolean {

    CommandHelper.isSubmit = true;

    const element: HTMLElement = document.documentElement; // XXX this might be the wrong element in case of shadow dom
    Page.page(element).onBeforeUnload();

    return true;
  };

}

class Transport {
  static requests = [];
  static currentActionId = null;
  static pageSubmitted = false;
  static startTime: Date;

  /**
   * @return true if the request is queued.
   */
  static request = function (req: () => void, submitPage: boolean, actionId?: string): boolean {
    let index = 0;
    if (submitPage) {
      Transport.pageSubmitted = true;
      index = Transport.requests.push(req);
      //console.debug('index = ' + index)
    } else if (!Transport.pageSubmitted) { // AJAX case
      console.debug("Current ActionId='%s' action='%s'", Transport.currentActionId, actionId);
      if (actionId && Transport.currentActionId === actionId) {
        console.info("Ignoring request");
        // If actionId equals currentActionId assume double request: do nothing
        return false;
      }
      index = Transport.requests.push(req);
      //console.debug('index = ' + index)
      Transport.currentActionId = actionId;
    } else {
      console.debug("else case");
      return false;
    }
    console.debug("index='%s'", index);
    if (index === 1) {
      console.info("Execute request!");
      Transport.startTime = new Date();
      Transport.requests[0]();
    } else {
      console.info("Request queued!");
    }
    return true;
  };

  // TBD XXX REMOVE is this called in non AJAX case?

  static requestComplete = function (): void {
    Transport.requests.shift();
    Transport.currentActionId = null;
    console.debug("Request complete! Duration: %s ms; "
        + "Queue size : %s", new Date().getTime() - Transport.startTime.getTime(),  Transport.requests.length);
    if (Transport.requests.length > 0) {
      console.debug("Execute request!");
      Transport.startTime = new Date();
      Transport.requests[0]();
    }
  };
}
