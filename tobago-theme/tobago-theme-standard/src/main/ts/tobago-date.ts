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

class InputSupport {

  static readonly YEAR_MONTH = `${new Date().toISOString().substr(0, 7)}`;

  readonly type: string;
  readonly support: boolean;

  constructor(type: string) {
    this.type = type;
    this.support = InputSupport.checkSupport(type);
  }

  private static checkSupport(type: string): boolean {
    const input = document.createElement("input");
    input.setAttribute("type", type);
    const INVALID_TEXT = "this is not a date";
    input.setAttribute("value", INVALID_TEXT);
    return input.value !== INVALID_TEXT;
  }

  example(step: number): string {
    switch (this.type) {
      case "date":
        return InputSupport.YEAR_MONTH + "-20";
      case "time":
        switch (step) {
          case 1:
            return "20:15:00";
          case 0.001:
            return "20:15:00.000";
          default:
            return "20:15";
        }
      case "datetime-local":
        switch (step) {
          case 1:
            return InputSupport.YEAR_MONTH + "-20T20:15:00";
          case 0.001:
            return InputSupport.YEAR_MONTH + "-20T20:15:00.000";
          default:
            return InputSupport.YEAR_MONTH + "-20T20:15";
        }
      case "month":
        return InputSupport.YEAR_MONTH;
      case "week":
        return InputSupport.YEAR_MONTH.substr(0, 4) + "-W52";
      default:
        return "";
    }
  }
}

class TobagoDate extends HTMLElement {

  private static readonly SUPPORTS = {
    "date": new InputSupport("date"),
    "time": new InputSupport("time"),
    "datetime-local": new InputSupport("datetime-local"),
    "month": new InputSupport("month"),
    "week": new InputSupport("week")
  };

  constructor() {
    super();
  }

  connectedCallback(): void {
    const support: InputSupport = TobagoDate.SUPPORTS[this.type];
    console.debug("check input type support", this.type, support);
    if (!support?.support) {
      this.type = "text";
      this.field.placeholder = support.example(this.step) + " " + (this.pattern ? this.pattern : "");
    }
    const nowButton = this.nowButton;
    if (nowButton) {
      nowButton.addEventListener("click", this.initNowButton.bind(this));
    }
  }

  initNowButton(): void {
    this.field.valueAsDate = new Date();
  }

  workaround(): void {
    window.alert("workaround!");
  }

  get type(): string {
    return this.field?.getAttribute("type");
  }

  set type(type: string) {
    this.field?.setAttribute("type", type);
  }

  get min(): string {
    return this.field?.getAttribute("min");
  }

  get max(): string {
    return this.field?.getAttribute("max");
  }

  get step(): number {
    return Number.parseFloat(this.field?.getAttribute("step"));
  }

  get pattern(): string {
    return this.getAttribute("pattern");
  }

  get field(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(this.id + "::field") as HTMLInputElement;
  }

  get nowButton(): HTMLButtonElement {
    return this.querySelector(".tobago-now");
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-date") == null) {
    window.customElements.define("tobago-date", TobagoDate);
  }
});
