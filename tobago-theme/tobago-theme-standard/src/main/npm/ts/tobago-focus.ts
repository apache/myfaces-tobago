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

import {Tobago4} from "./tobago-core";
import {DomUtils} from "./tobago-utils";
import {Listener, Phase} from "./tobago-listener";

export class Focus {

  private static getHidden(): HTMLInputElement {
    return <HTMLInputElement>document.getElementById(DomUtils.page().id + DomUtils.SUB_COMPONENT_SEP + "lastFocusId");
  }

  static setLastFocusId(id: string): void {
    this.getHidden().value = id;
  }

  static getLastFocusId(): string {
    return this.getHidden().value;
  }

  /**
   * Sets the focus to the requested element or to the first possible if
   * no element is explicitly requested.
   *
   * The priority order is:
   * - error (the first error element gets the focus)
   * - auto (the element with the tobago tag attribute focus="true" gets the focus)
   * - last (the element from the last request with same id gets the focus, not AJAX)
   * - first (the first input element (without tabindex=-1) gets the focus, not AJAX)
   */
  static init = function (element: HTMLElement) {

    const activeInputs = Focus.activeInputs(element);

    for (const focusable of activeInputs) {
      focusable.addEventListener("focus", function (event: FocusEvent) {
        const target = <HTMLElement>event.target;
        if (target.style.visibility !== "hidden" && target.style.display != "none") {
          // remember the last focused element, for later
          Focus.setLastFocusId(target.id);
        }
      });
    }

    // autofocus in popups doesn't work automatically... so we fix that here
    const modals = document.getElementsByClassName("modal");
    for (let i = 0; i < modals.length; i++) {
      modals.item(i).addEventListener('shown.bs.modal', Focus.initAutoFocus);
    }

    for (const hasDanger of DomUtils.selfOrElementsByClassName(element, ".has-danger")) {
      const activeInputsInsideDanger = Focus.activeInputs(hasDanger);
      if (activeInputsInsideDanger.length > 0) {
        activeInputsInsideDanger[0].focus();
        return;
      }
    }

    const autoFocus = element.querySelector("[autofocus]");
    if (autoFocus) {
      // nothing to do, because the browser make the work.
      return;
    }

    if (element.parentElement) {
      // seems to be AJAX, so end here
      return;
    }

    const lastFocusId = Focus.getLastFocusId();
    if (lastFocusId) {
      const element = document.getElementById(lastFocusId);
      if (element) {
        element.focus();
      }
      return;
    }

    if (activeInputs.length > 0) {
      activeInputs[0].focus();
      return;
    }
  };

  private static activeInputs(element: HTMLElement) {
    return DomUtils.selfOrQuerySelectorAll(element,
        "input:not([type='hidden']):not([disabled]):not([tabindex='-1'])," +
        "select:not([disabled]):not([tabindex='-1'])," +
        "textarea:not([disabled]):not([tabindex='-1'])")
        .filter((element) =>
            element.style.visibility !== "hidden"
            && element.style.display != "none");
  }

  private static initAutoFocus(event) {
    const modal = <HTMLElement>event.currentTarget;
    (<HTMLElement>modal.querySelector("[autofocus]")).focus();
  }
}

Listener.register(Focus.init, Phase.DOCUMENT_READY);
Listener.register(Focus.init, Phase.AFTER_UPDATE);
