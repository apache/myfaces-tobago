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

import {Css} from "./tobago-css";

export abstract class CollapsibleBase extends HTMLElement {

  /**
   * Toggle the expand/collapse state of the component.
   * @param clientSideAnimation enabled animated collapsing in the browser (optional - default is true).
   */
  public toggle(clientSideAnimation: boolean = true): void {
    if (this.collapsed) {
      this.expand(clientSideAnimation);
    } else {
      this.collapse(clientSideAnimation);
    }
  }

  /**
   * Expand (show) the component.
   * @param clientSideAnimation enabled animated collapsing in the browser (optional - default is true).
   */
  public expand(clientSideAnimation: boolean = true): void { //this method must not be named 'show' (TOBAGO-2148)
    if (this.fireEvent("show", true)) {
      this.collapsed = false;
      if (clientSideAnimation) {
        this.clientSideExpandAnimation();
      }
      this.fireEvent("shown", false);
    }
  }

  protected clientSideExpandAnimation(): void {
    this.classList.remove(Css.TOBAGO_COLLAPSED);
  }

  /**
   * Collapse (hide) the component.
   * @param clientSideAnimation enabled animated collapsing in the browser (optional - default is true).
   */
  public collapse(clientSideAnimation: boolean = true): void { //this method must not be named 'hide' (TOBAGO-2148)
    if (this.fireEvent("hide", true)) {
      this.collapsed = true;
      if (clientSideAnimation) {
        this.clientSideCollapseAnimation();
      }
      this.fireEvent("hidden", false);
    }
  }

  protected clientSideCollapseAnimation(): void {
    this.classList.add(Css.TOBAGO_COLLAPSED);
  }

  private fireEvent(eventName: string, cancelable: boolean): boolean {
    const fullEventName = "tobago." + this.tagName.substring(7).toLowerCase() + "." + eventName;
    return this.dispatchEvent(new CustomEvent(fullEventName, {bubbles: true, cancelable: cancelable}));
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
}
