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

export class Spinner {
  private referenceElement: HTMLElement;
  private spinner: HTMLElement;

  /**
   * Constructs an instance of the class with required DOM elements.
   *
   * @param {HTMLElement} referenceElement for positioning
   * @param {HTMLElement} spinner DIV element which contains the spinner
   */
  constructor(referenceElement: HTMLElement, spinner: HTMLElement) {
    this.referenceElement = referenceElement;
    this.spinner = spinner;
  }

  public show(): void {
    this.spinner.style.top = "0";
    this.spinner.style.right = "0";
    this.spinner.style.width = this.size + "px";
    this.spinner.style.height = this.size + "px";

    this.spinner.classList.add(Css.TOBAGO_SHOW); //put spinner on position={0, 0} to get the real clientRect values

    const spinnerRect = this.spinner.getBoundingClientRect();
    const refRect = this.referenceElement.getBoundingClientRect();
    const refStyle = getComputedStyle(this.referenceElement);
    const refBorderWidth = parseFloat(refStyle.borderWidth);
    const refPaddingTop = parseFloat(refStyle.paddingTop);
    const refPaddingRight = parseFloat(refStyle.paddingRight);

    this.spinner.style.top = (refRect.top - spinnerRect.top + refBorderWidth + refPaddingTop) + "px";
    this.spinner.style.right = (spinnerRect.right - refRect.right + refBorderWidth + refPaddingRight) + "px";
  }

  public hide(): void {
    this.spinner.classList.remove(Css.TOBAGO_SHOW);
  }

  public get size(): number {
    const cStyle = getComputedStyle(this.referenceElement);
    return this.referenceElement.clientHeight - parseFloat(cStyle.paddingTop) - parseFloat(cStyle.paddingBottom);
  }
}
