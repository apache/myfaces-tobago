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

// XXX which? nothing works...
// import * as bootstrap from "bootstrap/dist/js/bootstrap";
// import Popover from "bootstrap/dist/js/bootstrap.bundle";
// import {createPopper} from "@popperjs/core/dist/esm/popper";
import {Popover} from "bootstrap";
import {MenuStore} from "./tobago-menu-store";

// import {Popover} from "bootstrap/dist/js/bootstrap.bundle";

class TobagoPopover extends HTMLElement {

  private popover: Popover;

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.popover = new Popover(this.trigger, {
      container: MenuStore.get()
    });
  }

  get trigger(): HTMLElement {
    return this;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-popover") == null) {
    window.customElements.define("tobago-popover", TobagoPopover);
  }
});
