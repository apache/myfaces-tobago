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

import {Css} from "./tobago-css";
import {Alert, Toast} from "bootstrap";

enum Type {
  alert, toast
}

class Messages extends HTMLElement {

  constructor() {
    super();
  }

  get type(): Type {
    return this.classList.contains(Css.TOAST_CONTAINER) ? Type.toast : Type.alert;
  }

  get alerts(): NodeListOf<HTMLDivElement> {
    return this.querySelectorAll(".alert");
  }

  get toasts(): NodeListOf<HTMLDivElement> {
    return this.querySelectorAll(".toast");
  }

  connectedCallback(): void {
    if (this.type === Type.alert) {
      for (const alert of this.alerts) {
        new Alert(alert);
      }
    } else {
      for (const toastElement of this.toasts) {
        const disposeDelay = Number(toastElement.dataset.tobagoDisposeDelay);
        const delay = disposeDelay >= 0 ? disposeDelay : 0;
        const options: Toast.Options = {animation: true, autohide: disposeDelay >= 0, delay: delay};
        const toast = new Toast(toastElement, options);
        toast.show();

        toastElement.addEventListener("hidden.bs.toast", function (event: Event): void {
          toastElement.remove();
        });
      }
    }
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-messages") == null) {
    window.customElements.define("tobago-messages", Messages);
  }
});
