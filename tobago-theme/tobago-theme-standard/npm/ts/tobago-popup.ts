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

export class Popup extends HTMLElement {

  modal:any;

  constructor() {
    super();
  }

  connectedCallback(): void {
   let options = {};

    // @ts-ignore
    this.modal = new bootstrap.Modal(this, options);
  }

  show():void {
    console.log("show");
    this.modal.show();
  }

  hide():void {
    console.log("hide");
    this.modal.hide();
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

  static execute = function (action: string, target: HTMLElement): void {
    const hidden = Collapse.findHidden(target);
    let newCollapsed;
    switch (action) {
      case "hide":
        newCollapsed = true;
        break;
      case "show":
        newCollapsed = false;
        break;
      default:
        console.error("unknown action: '" + action + "'");
    }
    if (newCollapsed) {
      if (target instanceof Popup) {
        target.hide();
      } else {
        target.classList.add("tobago-collapsed");
      }
    } else {
      if (target instanceof Popup) {
        target.show();
      } else {
        target.classList.remove("tobago-collapsed");
      }
    }
    hidden.value = newCollapsed;
  };
}
