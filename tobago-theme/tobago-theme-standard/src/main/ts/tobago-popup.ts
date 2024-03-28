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

import {Modal} from "bootstrap";
import {BehaviorMode} from "./tobago-behavior-mode";
import {Collapse} from "./tobago-collapse";

const BootstrapPopupEvent = {
  HIDE: "hide.bs.modal",
  HIDDEN: "hidden.bs.modal",
  HIDE_PREVENTED: "hidePrevented.bs.modal",
  SHOW: "show.bs.modal",
  SHOWN: "shown.bs.modal"
};

export class Popup extends HTMLElement {

  modal: Modal;

  constructor() {
    super();
  }

  connectedCallback(): void {
    const options = {};
    this.modal = new Modal(this, options);
    if (!this.collapsed) {
      this.clientBehaviorShow();
    }

    this.addEventListener(BootstrapPopupEvent.HIDDEN, () => {
      /**
       * Notify server if popup is closed by clicking on the backdrop or press ESC.
       */
      if (this.connected) {
        this.collapsed = true;

        jsf.ajax.request(
            this,
            null,
            {
              "tobago.collapsible.processState": "true"
            });
      }
    });
  }

  disconnectedCallback(): void {
    this.clientBehaviorHide();
    // dispose seems to make trouble here: Scrolling is out or order after this call.
    // this.modal.dispose();
  }

  clientBehaviorShow(behaviorMode?: BehaviorMode): void { //this method must not named 'show' (TOBAGO-2148)
    console.debug("show - behaviorMode:", behaviorMode);
    if (behaviorMode == null || behaviorMode == BehaviorMode.client) {
      this.modal.show();
    } else {
      // otherwise the update from server will show the popup
    }
  }

  clientBehaviorHide(behaviorMode?: BehaviorMode): void { //this method must not named 'hide' (TOBAGO-2148)
    console.debug("hide - behaviorMode:", behaviorMode);
    if (behaviorMode == null || behaviorMode == BehaviorMode.client) {
      this.modal.hide();
    } else {
      // otherwise the update from server will hide the popup
    }
  }

  get collapsed(): boolean {
    return JSON.parse(Collapse.findHidden(this).value);
  }

  set collapsed(collapsed: boolean) {
    Collapse.findHidden(this).value = String(collapsed);
  }

  get connected(): boolean {
    return this.parentElement != null;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-popup") == null) {
    window.customElements.define("tobago-popup", Popup);
  }
});
