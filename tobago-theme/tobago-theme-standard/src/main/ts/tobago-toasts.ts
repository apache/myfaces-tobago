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

import {Toast} from "bootstrap";
import {Placement} from "./tobago-placement";
import {Css} from "./tobago-css";

enum StateEnum {
  created = "created",
  showed = "showed",
  closed = "closed"
}

interface StateData {
  state: StateEnum;
  hideTime: string;
}

const BootstrapToastEvent = {
  HIDE: "hide.bs.toast",
  HIDDEN: "hidden.bs.toast",
  SHOW: "show.bs.toast",
  SHOWN: "shown.bs.toast"
};

class Toasts extends HTMLElement {

  constructor() {
    super();
  }

  get toastStore(): HTMLDivElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector<HTMLDivElement>(".tobago-page-toastStore");
  }

  get localToasts(): NodeListOf<HTMLDivElement> {
    return this.querySelectorAll(".toast");
  }

  get storeToasts(): NodeListOf<HTMLDivElement> {
    return this.toastStore.querySelectorAll(".toast[name^='" + this.id + "::']");
  }

  get statesInput(): HTMLInputElement {
    return this.querySelector("input[name='" + this.id + "::states']");
  }

  get states(): Map<string, StateData> {
    return new Map(Object.entries(JSON.parse(this.statesInput.value)));
  }

  set states(states: Map<string, StateData>) {
    this.statesInput.value = JSON.stringify(Object.fromEntries(states));
  }

  connectedCallback(): void {
    for (const storeToast of this.storeToasts) {
      const localToast = this.querySelector<HTMLDivElement>(".toast[id='" + storeToast.getAttribute("name") + "']");
      if (!localToast) {
        this.removeToast(storeToast);
      }
    }

    for (const localToast of this.localToasts) {
      const storeToast = this.toastStore.querySelector<HTMLDivElement>(".toast[name='" + localToast.id + "']");
      if (storeToast) {
        this.updateToast(storeToast, localToast);
      } else {
        this.showToast(localToast);
      }
    }
  }

  private removeToast(storeToast: HTMLDivElement): void {
    const toast = new Toast(storeToast, {autohide: false, delay: 0});
    this.addHiddenEventListeners(storeToast);
    toast.hide();
  }

  private updateToast(storeToast: HTMLDivElement, localToast: HTMLDivElement): void {
    if (localToast.querySelector("." + Css.TOAST_HEADER)) {
      storeToast.replaceChildren(
          localToast.querySelector("." + Css.TOAST_HEADER),
          localToast.querySelector("." + Css.TOAST_BODY));
    } else {
      storeToast.replaceChildren(localToast.querySelector("." + Css.TOAST_BODY));
    }
  }

  private showToast(localToast: HTMLDivElement): void {
    const placement: Placement = Placement[localToast.dataset.tobagoPlacement];
    const disposeDelay = Number(localToast.dataset.tobagoDisposeDelay);

    localToast.removeAttribute("id");
    this.getToastContainer(placement).insertAdjacentElement("beforeend", localToast);

    this.addShowEventListeners(localToast);
    this.addHideEventListeners(localToast);
    this.addHiddenEventListeners(localToast);

    const toast = new Toast(localToast, {autohide: disposeDelay > 0, delay: Math.max(disposeDelay, 0)});
    const id = localToast.getAttribute("name");
    const stateData = this.states.get(id);

    if (stateData.state === StateEnum.created) {
      toast.show();
    } else if (stateData.state === StateEnum.showed) {
      localToast.classList.add(Css.SHOW);
      localToast.classList.add(Css.FADE); //set fade for hide animation; set fade after show!

      if (disposeDelay >= 0) {
        setTimeout(() => {
          toast.hide();
        }, disposeDelay);
      }
    }
  }

  private addShowEventListeners(toast: HTMLDivElement) {
    const id = toast.getAttribute("name");
    toast.addEventListener("show.bs.toast", () => {
      const states = this.states;
      states.get(id).state = StateEnum.showed;
      this.states = states;
      faces.ajax.request(this.id, null, {
        params: {
          "jakarta.faces.behavior.event": "toastShown"
        },
        execute: this.id
      });
    });

  }

  private addHideEventListeners(toast: HTMLDivElement) {
    const id = toast.getAttribute("name");
    toast.addEventListener("hide.bs.toast", () => {
      const states = this.states;
      states.get(id).state = StateEnum.closed;
      this.states = states;
      faces.ajax.request(this.id, null, {
        params: {
          "jakarta.faces.behavior.event": "toastHide"
        },
        execute: this.id
      });
    });
  }

  private addHiddenEventListeners(toast: HTMLDivElement) {
    toast.addEventListener("hidden.bs.toast", () => {
      toast.remove();
    });
  }

  private getToastContainer(placement: Placement): HTMLDivElement {
    const cssClasses = this.getToastContainerCssClass(placement);
    const selector = cssClasses.join(".");
    const element = this.toastStore.querySelector<HTMLDivElement>("." + selector);
    if (element) {
      return element;
    } else {
      const div = document.createElement("div");
      for (const cssClass of cssClasses) {
        div.classList.add(cssClass);
      }
      return this.toastStore.insertAdjacentElement("beforeend", div) as HTMLDivElement;
    }
  }

  private getToastContainerCssClass(placement: Placement): Css[] {
    const css: Css[] = [];
    css.push(Css.TOAST_CONTAINER);
    css.push(Css.POSITION_FIXED);
    css.push(Css.P_3);

    switch (placement) {
      case Placement.topLeft:
      case Placement.topCenter:
      case Placement.topRight:
        css.push(Css.TOP_0);
        break;
      case Placement.middleLeft:
      case Placement.middleCenter:
      case Placement.middleRight:
        css.push(Css.TOP_50);
        break;
      case Placement.bottomLeft:
      case Placement.bottomCenter:
      case Placement.bottomRight:
        css.push(Css.BOTTOM_0);
        break;
    }

    switch (placement) {
      case Placement.topLeft:
      case Placement.middleLeft:
      case Placement.bottomLeft:
        css.push(Css.START_0);
        break;
      case Placement.topCenter:
      case Placement.middleCenter:
      case Placement.bottomCenter:
        css.push(Css.START_50);
        break;
      case Placement.topRight:
      case Placement.middleRight:
      case Placement.bottomRight:
        css.push(Css.END_0);
        break;
    }

    switch (placement) {
      case Placement.topCenter:
      case Placement.bottomCenter:
        css.push(Css.TRANSLATE_MIDDLE_X);
        break;
      case Placement.middleLeft:
      case Placement.middleRight:
        css.push(Css.TRANSLATE_MIDDLE_Y);
        break;
      case Placement.middleCenter:
        css.push(Css.TRANSLATE_MIDDLE);
        break;
    }

    return css;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-toasts") == null) {
    window.customElements.define("tobago-toasts", Toasts);
  }
});
