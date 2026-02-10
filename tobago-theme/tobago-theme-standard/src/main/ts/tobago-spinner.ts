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
  private base: HTMLElement;
  private referenceElement: HTMLElement;
  private inputField: HTMLElement;
  private spinner: HTMLElement;

  /**
   * Constructs an instance of the class with required DOM elements.
   *
   * @param {HTMLElement} baseElement root element for the component
   * @param {HTMLElement} referenceElement for positioning
   * @param {HTMLElement} inputField to measure the height
   * @param {HTMLElement} spinner DIV element which contains the spinner
   */
  constructor(baseElement: HTMLElement, referenceElement: HTMLElement, inputField: HTMLElement, spinner: HTMLElement) {
    this.base = baseElement;
    this.referenceElement = referenceElement;
    this.inputField = inputField;
    this.spinner = spinner;
  }

  public show(): void {
    this.spinner.style.right = (this.right + this.marginRight) + "px";
    this.spinner.style.width = this.size + "px";
    this.spinner.style.height = this.size + "px";
    this.spinner.classList.add(Css.TOBAGO_SHOW);
  }

  public hide(): void {
    this.spinner.classList.remove(Css.TOBAGO_SHOW);
  }

  public get size(): number {
    const cStyle = getComputedStyle(this.inputField);
    return this.inputField.clientHeight - parseFloat(cStyle.paddingTop) - parseFloat(cStyle.paddingBottom);
  }

  private get marginRight(): number {
    const cStyle = getComputedStyle(this.referenceElement);
    return parseFloat(cStyle.borderWidth) + parseFloat(cStyle.paddingRight);
  }

  private get right(): number {
    const baseBox = this.base.getBoundingClientRect();
    const refBox = this.referenceElement.getBoundingClientRect();
    return baseBox.right - refBox.right;
  }
}
