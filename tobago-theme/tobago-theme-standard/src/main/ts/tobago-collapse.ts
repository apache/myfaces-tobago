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

import {CollapseOperation} from "./tobago-collapse-operation";
import {BehaviorMode} from "./tobago-behavior-mode";
import {Css} from "./tobago-css";
import {Popup} from "./tobago-popup";
import {Offcanvas} from "./tobago-offcanvas";

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
      Collapse.fireEvent(target, "hide");
      if (target instanceof Popup || target instanceof Offcanvas) {
        target.clientBehaviorHide(behaviorMode);
      } else {
        target.classList.add(Css.TOBAGO_COLLAPSED);
      }
      Collapse.fireEvent(target, "hidden");
    } else {
      Collapse.fireEvent(target, "show");
      if (target instanceof Popup || target instanceof Offcanvas) {
        target.clientBehaviorShow(behaviorMode);
      } else {
        target.classList.remove(Css.TOBAGO_COLLAPSED);
      }
      Collapse.fireEvent(target, "shown");
    }
    hidden.value = newCollapsed;
  };

  static fireEvent(target: HTMLElement, eventName: string) {
    const fullEventName = "tobago." + target.tagName.substring(7).toLowerCase() + "." + eventName;
    target.dispatchEvent(new CustomEvent(fullEventName, {bubbles: true}));
  }
}
