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
import {EventListenerStore} from "./util/EventListenerStore";
import {FocusableElement, tabbable} from "tabbable";
import {Key} from "./tobago-key";

const BootstrapPopupEvent = {
  HIDE: "hide.bs.modal",
  HIDDEN: "hidden.bs.modal",
  HIDE_PREVENTED: "hidePrevented.bs.modal",
  SHOW: "show.bs.modal",
  SHOWN: "shown.bs.modal"
};

export class Popup extends HTMLElement {
  private listeners: EventListenerStore = new EventListenerStore();
  modal: Modal;

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.modal = new Modal(this, {focus: false});
    this.listeners.add(this, "keydown", this.keydownEvent.bind(this));
    this.listeners.add(this, BootstrapPopupEvent.SHOWN, () => this.focus());
    this.listeners.add(this, BootstrapPopupEvent.HIDDEN, () => {
      /**
       * Make sure that collapsed=true is set when the popup is closed by clicking on the background or pressing ESC.
       */
      if (this.connected) {
        this.collapsed = true;
      }
    });

    if (!this.collapsed) {
      this.clientBehaviorShow();
    }
  }

  disconnectedCallback(): void {
    this.clientBehaviorHide();
    // dispose seems to make trouble here: Scrolling is out or order after this call.
    // this.modal.dispose();
    this.listeners.disconnect();
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

  private keydownEvent(event: KeyboardEvent): void {
    switch (event.key) {
      case Key.ESCAPE:
        event.stopPropagation();
        this.modal.hide();
        break;
      case Key.TAB: {
        const focusableElements: FocusableElement[] = tabbable(this);
        const firstFocusableElement = focusableElements[0];
        const lastFocusableElement = focusableElements[focusableElements.length - 1];
        if (event.shiftKey) {
          if (document.activeElement === this || document.activeElement === firstFocusableElement) {
            event.preventDefault();
            lastFocusableElement.focus();
          }
        } else {
          if (document.activeElement === this || document.activeElement === lastFocusableElement) {
            event.preventDefault();
            firstFocusableElement.focus();
          }
        }
      }
        break;
      default:
        break;
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
