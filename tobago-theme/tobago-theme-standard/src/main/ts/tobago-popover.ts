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

import {Popover, Tooltip} from "bootstrap";
import {ClientBehaviors} from "./tobago-client-behaviors";
import {EventListenerStore} from "./tobago-event-listener-store";

enum BootstrapPopoverEvent {
  HIDE = "hide.bs.popover",
  HIDDEN = "hidden.bs.popover",
  SHOW = "show.bs.popover",
  SHOWN = "shown.bs.popover"
}

type PopoverTrigger = NonNullable<Tooltip.Options["trigger"]>;

enum SanitizeMode {
  auto, never
}

class TobagoPopover extends HTMLElement {
  private listeners: EventListenerStore = new EventListenerStore();
  private instance: Popover;

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.initEventListener(BootstrapPopoverEvent.SHOW, ClientBehaviors.TOBAGO_POPOVER_SHOW, true);
    this.initEventListener(BootstrapPopoverEvent.SHOWN, ClientBehaviors.TOBAGO_POPOVER_SHOWN, false);
    this.initEventListener(BootstrapPopoverEvent.HIDE, ClientBehaviors.TOBAGO_POPOVER_HIDE, true);
    this.initEventListener(BootstrapPopoverEvent.HIDDEN, ClientBehaviors.TOBAGO_POPOVER_HIDDEN, false);

    this.instance = new Popover(this.baseElement, {
      container: this.popoverStore,
      customClass: this.customClass,
      title: this.label,
      content: this.value,
      html: !this.escape,
      sanitize: this.sanitize === SanitizeMode.auto,
      trigger: this.trigger
    });
  }

  disconnectedCallback(): void {
    this.instance?.dispose();
    this.listeners.disconnect();
  }

  private initEventListener(boostrapEvent: BootstrapPopoverEvent, tobagoEvent: ClientBehaviors,
                            cancelable: boolean): void {

    this.listeners.add(this.baseElement, boostrapEvent, (event) => {
      if (!this.baseElement.dispatchEvent(new CustomEvent(tobagoEvent, {cancelable: cancelable}))) {
        event.preventDefault();
      }
    });
  }

  get baseElement(): HTMLElement {
    if (this.parentElement?.tagName === "A" || this.parentElement?.tagName === "BUTTON") {
      return this.parentElement;
    } else {
      return this;
    }
  }

  get popoverStore(): HTMLDivElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector<HTMLDivElement>(".tobago-page-popoverStore");
  }

  get customClass(): string {
    const customClass = this.getAttribute("custom-class");
    return customClass ? customClass : "";
  }

  get label(): string {
    const label = this.getAttribute("label");
    return label ? label : "";
  }

  get value(): string {
    return this.getAttribute("value");
  }

  get escape(): boolean {
    return this.getAttribute("escape") !== "false";
  }

  get sanitize(): SanitizeMode {
    return SanitizeMode[this.getAttribute("sanitize")];
  }

  get trigger(): PopoverTrigger {
    return this.getAttribute("trigger") as PopoverTrigger;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-popover") == null) {
    window.customElements.define("tobago-popover", TobagoPopover);
  }
});
