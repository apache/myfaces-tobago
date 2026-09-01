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

import {Offcanvas as BootstrapOffcanvas} from "bootstrap";
import {CollapsibleBase} from "./tobago-collapsible-base";
import {EventListenerStore} from "./tobago-event-listener-store";

const BootstrapOffcanvasEvent = {
  HIDE: "hide.bs.offcanvas",
  HIDDEN: "hidden.bs.offcanvas",
  HIDE_PREVENTED: "hidePrevented.bs.offcanvas",
  SHOW: "show.bs.offcanvas",
  SHOWN: "shown.bs.offcanvas"
};

export class Offcanvas extends CollapsibleBase {
  private listeners: EventListenerStore = new EventListenerStore();
  private offcanvas: BootstrapOffcanvas;

  constructor() {
    super();
  }

  connectedCallback(): void {
    const options = {};
    this.offcanvas = new BootstrapOffcanvas(this, options);
    if (!this.collapsed) {
      this.offcanvas.show();
    }

    this.listeners.add(this, BootstrapOffcanvasEvent.HIDDEN, () => {
      /**
       * Make sure collapsed=true is set when the offcanvas is closed by clicking on the background or pressing ESC.
       */
      if (this.connected) {
        this.collapsed = true;
      }
    });
  }

  disconnectedCallback(): void {
    this.offcanvas.hide();
    this.listeners.disconnect();
  }

  protected clientSideExpandAnimation(): void {
    this.offcanvas.show();
  }

  protected clientSideCollapseAnimation(): void {
    this.offcanvas.hide();
  }

  get connected(): boolean {
    return this.parentElement != null;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-offcanvas") == null) {
    window.customElements.define("tobago-offcanvas", Offcanvas);
  }
});
