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

import {Css} from "./tobago-css";
import {EventListenerStore} from "./util/EventListenerStore";

class Stars extends HTMLElement {
  private listeners: EventListenerStore = new EventListenerStore();

  constructor() {
    super();
  }

  private static leftOffset(element: HTMLElement): number {
    let left = 0;

    let currentElement = element;
    while (currentElement) {
      left += (currentElement.offsetLeft - currentElement.scrollLeft + currentElement.clientLeft);
      currentElement = currentElement.offsetParent as HTMLElement;
    }

    return left;
  }

  connectedCallback(): void {
    if (this.min === 0) {
      this.slider.style["left"] = `${-100 / this.max}%`;
      this.slider.style["width"] = `${100 + (100 / this.max)}%`;
    }

    const currentValue = parseInt(this.hiddenInput.value);
    if (currentValue > 0) {
      const percentValue = 100 * currentValue / this.max;
      this.selected.style["width"] = `${percentValue}%`;
      this.unselected.style["left"] = `${percentValue}%`;
      this.unselected.style["width"] = `${100 - percentValue}%`;
    } else if (this.placeholder) {
      this.selected.classList.add(Css.TOBAGO_PLACEHOLDER);
      const placeholderValue = 100 * this.placeholder / this.max;
      this.selected.style["width"] = `${placeholderValue}%`;
      this.unselected.style["left"] = `${placeholderValue}%`;
      this.unselected.style["width"] = `${100 - placeholderValue}%`;
    }

    if (!this.readonly && !this.disabled) {
      /* preselectMode is a Workaround for IE11: fires change event instead of input event */
      let preselectMode = false;
      this.listeners.add(this.slider, "mousedown", (event: Event): void => {
        preselectMode = true;
      });
      this.listeners.add(this.slider, "mouseup", function (event: Event): void {
        preselectMode = false;
        this.selectStars();
      });

      this.listeners.add(this.slider, "input", function (event: Event): void {
        this.preselectStars();
      });
      this.listeners.add(this.slider, "touchend", function (event: Event): void {
        /* Workaround for mobile devices. TODO: fire AJAX request for 'touchend' */
        // slider.trigger("change");
        this.slider.dispatchEvent(new Event("change"));
      });
      this.listeners.add(this.slider, "change", function (event: Event): void {
        if (preselectMode) {
          this.preselectStars();
        } else {
          this.selectStars();
        }
      });

      this.listeners.add(this.slider, "touchstart", this.touchstart);
      this.listeners.add(this.slider, "touchmove", this.touchstart);
    }
  }

  disconnectedCallback(): void {
    this.listeners.disconnect();
  }

  // XXX current issue: on ios-Safari select 5 stars and than click on 1 star doesn't work on labeled component.
  private touchstart(event: TouchEvent): void {
    /* Workaround for Safari browser on iPhone */
    const target = event.currentTarget as HTMLInputElement;
    const sliderValue = (parseInt(target.max) / target.offsetWidth)
        * (event.touches[0].pageX - Stars.leftOffset(this.slider));
    if (sliderValue > parseInt(target.max)) {
      this.slider.value = target.max;
    } else if (sliderValue < parseInt(target.min)) {
      this.slider.value = target.min;
    } else {
      this.slider.value = String(sliderValue);
    }
    this.preselectStars();
  }

  private preselectStars(): void {
    this.tooltip.classList.add(Css.SHOW);

    if (parseInt(this.slider.value) > 0) {
      this.tooltip.classList.remove(Css.TRASH);
      this.tooltip.textContent = (5 * (parseInt(this.slider.value)) / this.max).toFixed(2);

      this.preselected.classList.add(Css.SHOW);
      this.preselected.style["width"] = `${100 * parseInt(this.slider.value) / this.max}%`;
    } else {
      this.tooltip.textContent = "";
      this.tooltip.classList.add(Css.TRASH);

      if (this.placeholder) {
        this.preselected.classList.add(Css.SHOW);
        this.preselected.style["width"] = `${100 * this.placeholder / this.max}%`;
      } else {
        this.preselected.classList.remove(Css.SHOW);
      }
    }
  }

  private selectStars(): void {
    this.tooltip.classList.remove(Css.SHOW);
    this.preselected.classList.remove(Css.SHOW);

    if (parseInt(this.slider.value) > 0) {
      this.selected.classList.remove(Css.TOBAGO_PLACEHOLDER);

      const percentValue = 100 * parseInt(this.slider.value) / this.max;
      this.selected.style["width"] = `${percentValue}%`;
      this.unselected.style["left"] = `${percentValue}%`;
      this.unselected.style["width"] = `${100 - percentValue}%`;

      this.hiddenInput.value = this.slider.value;
    } else {
      if (this.placeholder) {
        this.selected.classList.add(Css.TOBAGO_PLACEHOLDER);
        const placeholderValue = 100 * this.placeholder / this.max;
        this.selected.style["width"] = `${placeholderValue}%`;
        this.unselected.style["left"] = `${placeholderValue}%`;
        this.unselected.style["width"] = `${100 - placeholderValue}%`;
      } else {
        this.selected.classList.remove(Css.TOBAGO_PLACEHOLDER);
        this.selected.style["width"] = "";
        this.unselected.style["left"] = "";
        this.unselected.style["width"] = "";
      }

      this.hiddenInput.value = this.required ? "" : this.slider.value;
    }
  }

  get hiddenInput(): HTMLInputElement {
    return this.querySelector("input[type=hidden]");
  }

  get container(): HTMLElement {
    return this.querySelector(".tobago-stars-container");
  }

  get tooltip(): HTMLElement {
    return this.container.querySelector(".tobago-stars-tooltip");
  }

  get selected(): HTMLElement {
    return this.container.querySelector(".tobago-stars-selected");
  }

  get unselected(): HTMLElement {
    return this.container.querySelector(".tobago-stars-unselected");
  }

  get preselected(): HTMLElement {
    return this.container.querySelector(".tobago-stars-preselected");
  }

  get slider(): HTMLInputElement {
    return this.container.querySelector(".tobago-stars-slider");
  }

  get readonly(): boolean {
    return this.slider.readOnly;
  }

  get disabled(): boolean {
    return this.slider.disabled;
  }

  get required(): boolean {
    return this.slider.required;
  }

  get min(): number {
    return parseInt(this.slider.max);
  }

  get max(): number {
    return parseInt(this.slider.max);
  }

  get placeholder(): number {
    return parseInt(this.slider.placeholder);
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-stars", Stars);
});
