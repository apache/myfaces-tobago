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

// import * as bootstrap from "bootstrap/dist/js/bootstrap.esm";
// import "bootstrap/dist/js/bootstrap.esm";
// import "bootstrap/dist/js/bootstrap";
// import {Modal} from "bootstrap/dist/js/bootstrap.bundle";
import {Modal} from "bootstrap";
import {BehaviorMode} from "./tobago-behavior-mode";
import {CollapseOperation} from "./tobago-collapsible-operation";

export class Popup extends HTMLElement {

  modal: Modal;

  constructor() {
    super();
  }

  connectedCallback(): void {
    const options = {};
    this.modal = new Modal(this, options);
    if (!this.collapsed) {
      this.show();
    }
  }

  disconnectedCallback(): void {
    this.hide();
    // dispose seems to make trouble here: Scrolling is out or order after this call.
    // this.modal.dispose();
  }

  show(behaviorMode?: BehaviorMode): void {
    console.log("show");
    console.log("behaviorMode", behaviorMode, BehaviorMode.client);
    if (behaviorMode == null || behaviorMode == BehaviorMode.client) {
      this.modal.show();
    } else {
      // otherwise the update from server will show the popup
    }
  }

  hide(behaviorMode?: BehaviorMode): void {
    console.log("hide");
    console.log("behaviorMode", behaviorMode, BehaviorMode.client);
    if (behaviorMode == null || behaviorMode == BehaviorMode.client) {
      this.modal.hide();
    } else {
      // otherwise the update from server will hide the popup
    }
  }

  get collapsed(): boolean {
    return JSON.parse(Collapse.findHidden(this).value);
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-popup") == null) {
    window.customElements.define("tobago-popup", Popup);
  }
});

export class Collapse {

  static findHidden(element: HTMLElement): HTMLInputElement {
    const rootNode = element.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(element.id + "::collapse") as HTMLInputElement;
  }

  static execute = function (operation: CollapseOperation, target: HTMLElement, behaviorMode: BehaviorMode): void {
    const hidden = Collapse.findHidden(target);
    let newCollapsed;
    switch (operation) {
      case CollapseOperation.hide:
        newCollapsed = true;
        break;
      case CollapseOperation.show:
        newCollapsed = false;
        break;
      default:
        console.error("unknown operation: '%s'", operation);
    }
    if (newCollapsed) {
      if (target instanceof Popup) {
        target.hide(behaviorMode);
      } else {
        target.classList.add("tobago-collapsed");
      }
    } else {
      if (target instanceof Popup) {
        target.show(behaviorMode);
      } else {
        target.classList.remove("tobago-collapsed");
      }
    }
    hidden.value = newCollapsed;
  };
}
