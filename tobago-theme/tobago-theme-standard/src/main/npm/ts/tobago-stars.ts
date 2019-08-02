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

import {Listener, Phase} from "./tobago-listener";
import {DomUtils, Tobago4Utils} from "./tobago-utils";

class Stars {

  static init(element: HTMLElement) {
    for (const star of DomUtils.selfOrQuerySelectorAll(element, ".tobago-stars")) {
      const hiddenInput = star.querySelector("input[type=hidden]") as HTMLInputElement;
      const container = star.querySelector(".tobago-stars-container") as HTMLElement;
      const tooltip = container.querySelector(".tobago-stars-tooltip") as HTMLElement;
      const selected = container.querySelector(".tobago-stars-selected") as HTMLElement;
      const unselected = container.querySelector(".tobago-stars-unselected") as HTMLElement;
      const preselected = container.querySelector(".tobago-stars-preselected") as HTMLElement;
      const slider = container.querySelector(".tobago-stars-slider") as HTMLInputElement;

      const readonly = slider.readOnly;
      const disabled = slider.disabled;
      const required = slider.required;

      const max = parseInt(slider.max);
      const placeholder = parseInt(slider.placeholder);

      if (parseInt(slider.min) === 0) {
        slider.style["left"] = "-" + (100 / max) + "%";
        slider.style["width"] = 100 + (100 / max) + "%";
      }

      const currentValue = parseInt(hiddenInput.value);
      if (currentValue > 0) {
        const percentValue = 100 * currentValue / max;
        selected.style["width"] = percentValue + "%";
        unselected.style["left"] = percentValue + "%";
        unselected.style["width"] = 100 - percentValue + "%";
      } else if (placeholder) {
        selected.classList.add("tobago-placeholder");
        const placeholderValue = 100 * placeholder / max;
        selected.style["width"] = placeholderValue + "%";
        unselected.style["left"] = placeholderValue + "%";
        unselected.style["width"] = 100 - placeholderValue + "%";
      }

      if (!readonly && !disabled) {
        /* preselectMode is a Workaround for IE11: fires change event instead of input event */
        let preselectMode = false;
        slider.addEventListener('mousedown', function (event) {
          preselectMode = true;
        });
        slider.addEventListener('mouseup', function (event) {
          preselectMode = false;
          selectStars();
        });

        slider.addEventListener('input', function (event) {
          preselectStars();
        });
        slider.addEventListener('touchend', function (event) {
          /* Workaround for mobile devices. TODO: fire AJAX request for 'touchend' */
          // slider.trigger("change");
          slider.dispatchEvent(new Event("change"));
        });
        slider.addEventListener('change', function (event) {
          if (preselectMode) {
            preselectStars();
          } else {
            selectStars();
          }
        });

        slider.addEventListener('touchstart', touchstart);
        slider.addEventListener('touchmove', touchstart);
      }

      // XXX current issue: on ios-Safari select 5 stars and than click on 1 star doesn't work on labeled component.
      function touchstart(event: TouchEvent) {
        /* Workaround for Safari browser on iPhone */
        const target = event.currentTarget as HTMLInputElement;
        const sliderValue = (parseInt(target.max) / target.offsetWidth) * (event.touches[0].pageX - slider.offsetLeft);
        if (sliderValue > parseInt(target.max)) {
          slider.value = target.max;
        } else if (sliderValue < parseInt(target.min)) {
          slider.value = target.min;
        } else {
          slider.value = String(sliderValue);
        }
        preselectStars();
      }

      function preselectStars() {
        tooltip.classList.add("show");

        if (parseInt(slider.value) > 0) {
          tooltip.classList.remove("trash");
          tooltip.textContent = (5 * (parseInt(slider.value)) / max).toFixed(2);

          preselected.classList.add("show");
          preselected.style["width"] = (100 * parseInt(slider.value) / max) + "%";
        } else {
          tooltip.textContent = "";
          tooltip.classList.add("trash");

          if (placeholder) {
            preselected.classList.add("show");
            preselected.style["width"] = (100 * placeholder / max) + "%";
          } else {
            preselected.classList.remove("show");
          }
        }
      }

      function selectStars() {
        tooltip.classList.remove("show");
        preselected.classList.remove("show");

        if (parseInt(slider.value) > 0) {
          selected.classList.remove("tobago-placeholder");

          const percentValue = 100 * parseInt(slider.value) / max;
          selected.style["width"] = percentValue + "%";
          unselected.style["left"] = percentValue + "%";
          unselected.style["width"] = 100 - percentValue + "%";

          hiddenInput.value = slider.value;
        } else {
          if (placeholder) {
            selected.classList.add("tobago-placeholder");
            const placeholderValue = 100 * placeholder / max;
            selected.style["width"] = placeholderValue + "%";
            unselected.style["left"] = placeholderValue + "%";
            unselected.style["width"] = 100 - placeholderValue + "%";
          } else {
            selected.classList.remove("tobago-placeholder");
            selected.style["width"] = "";
            unselected.style["left"] = "";
            unselected.style["width"] = "";
          }

          hiddenInput.value = required ? "" : slider.value;
        }
      }
    }
  }
}

Listener.register(Stars.init, Phase.DOCUMENT_READY);
Listener.register(Stars.init, Phase.AFTER_UPDATE);
