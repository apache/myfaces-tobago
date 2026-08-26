/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {EventListenerStore} from "./tobago-event-listener-store";
import {BehaviorMode} from "./tobago-behavior-mode";
import {Css} from "./tobago-css";
import {CollapseOperation} from "./tobago-collapse-operation";

export interface CollapsibleEventDetail {
  behaviorMode: BehaviorMode;
}

export abstract class CollapsibleBase extends HTMLElement {
  protected listeners: EventListenerStore = new EventListenerStore();

  connectedCallback(): void {
    this.listeners.add(this, this.showEventName, (event: CustomEvent<CollapsibleEventDetail>) => {
      if (event.target === this) {
        this.clientBehaviorShow(event);
      }
    });
    this.listeners.add(this, this.hideEventName, (event: CustomEvent<CollapsibleEventDetail>) => {
      if (event.target === this) {
        this.clientBehaviorHide(event);
      }
    });
  }

  disconnectedCallback(): void {
    this.listeners.disconnect();
  }

  //this method must not be named 'show' (TOBAGO-2148)
  protected clientBehaviorShow(event: CustomEvent<CollapsibleEventDetail>): void {
    this.classList.remove(Css.TOBAGO_COLLAPSED);
    this.collapsed = false;
    this.fireEvent("shown", event.detail.behaviorMode);
  }

  //this method must not be named 'hide' (TOBAGO-2148)
  protected clientBehaviorHide(event: CustomEvent<CollapsibleEventDetail>): void {
    this.classList.add(Css.TOBAGO_COLLAPSED);
    this.collapsed = true;
    this.fireEvent("hidden", event.detail.behaviorMode);
  }

  executeCollapseOperation(collapseOperation: CollapseOperation, mode: BehaviorMode): void {
    if (CollapseOperation.show === collapseOperation
        || (CollapseOperation.toggle === collapseOperation && this.collapsed)) {
      this.fireEvent("show", mode);
    } else if (CollapseOperation.hide === collapseOperation
        || (CollapseOperation.toggle === collapseOperation && !this.collapsed)) {
      this.fireEvent("hide", mode);
    }
  }

  fireEvent(eventName: string, behaviorMode: BehaviorMode) {
    const fullEventName = this.getFullEventName(eventName);

    this.dispatchEvent(new CustomEvent<CollapsibleEventDetail>(fullEventName, {
      bubbles: true,
      detail: {
        behaviorMode: behaviorMode
      }
    }));
  }

  private getFullEventName(eventName: string): string {
    return "tobago." + this.tagName.substring(7).toLowerCase() + "." + eventName;
  }

  get collapsed(): boolean {
    return this.collapseField.value === "true";
  }

  set collapsed(value: boolean) {
    this.collapseField.value = String(value);
  }

  get collapseField(): HTMLInputElement {
    return this.querySelector("input[id$='::collapse']");
  }

  get showEventName(): string {
    return this.getFullEventName("show");
  }

  get hideEventName(): string {
    return this.getFullEventName("hide");
  }
}
