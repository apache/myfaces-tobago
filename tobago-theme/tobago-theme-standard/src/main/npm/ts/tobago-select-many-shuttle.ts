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
import {DomUtils} from "./tobago-utils";

class SelectManyShuttle {

  static init = function (element: HTMLElement) {
    for (const shuttle of DomUtils.selfOrQuerySelectorAll(
        element, ".tobago-selectManyShuttle:not(.tobago-selectManyShuttle-disabled)")) {

      shuttle.querySelector(".tobago-selectManyShuttle-unselected").addEventListener(
          "dblclick", (event: Event) => {
            SelectManyShuttle.moveSelectedItems(event, true, false);
          });

      shuttle.querySelector(".tobago-selectManyShuttle-selected").addEventListener(
          "dblclick", (event: Event) => {
            SelectManyShuttle.moveSelectedItems(event, false, false);
          });

      shuttle.querySelector(".tobago-selectManyShuttle-addAll").addEventListener(
          "click", (event: Event) => {
            SelectManyShuttle.moveSelectedItems(event, true, true);
          });

      shuttle.querySelector(".tobago-selectManyShuttle-add").addEventListener(
          "click", (event: Event) => {
            SelectManyShuttle.moveSelectedItems(event, true, false);
          });

      shuttle.querySelector(".tobago-selectManyShuttle-removeAll").addEventListener(
          "click", (event: Event) => {
            SelectManyShuttle.moveSelectedItems(event, false, true);
          });

      shuttle.querySelector(".tobago-selectManyShuttle-remove").addEventListener(
          "click", (event: Event) => {
            SelectManyShuttle.moveSelectedItems(event, false, false);
          });
    }
  };

  static moveSelectedItems = function (event: Event, direction: boolean, all: boolean) {
    const currentTarget = event.currentTarget as HTMLElement;
    const shuttle = currentTarget.closest(".tobago-selectManyShuttle");
    const unselected = shuttle.querySelector(".tobago-selectManyShuttle-unselected");
    const selected = shuttle.querySelector(".tobago-selectManyShuttle-selected");
    var oldCount = selected.childElementCount;
    const source = direction ? unselected : selected;
    const target = direction ? selected : unselected;
    const options = source.querySelectorAll(all ? "option:not(:disabled)" : "option:checked");
    var hidden = shuttle.querySelector(".tobago-selectManyShuttle-hidden");
    var hiddenOptions = hidden.querySelectorAll("option");
    for (const option of options as NodeListOf<HTMLOptionElement>) {
      source.removeChild(option);
      target.appendChild(option);
      for (const hiddenOption of hiddenOptions) {
        if (hiddenOption.value === option.value) {
          hiddenOption.selected = direction;
        }
      }
    }

    if (oldCount !== selected.childElementCount) {
      hidden.dispatchEvent(new Event("change"));
    }
  };
}

Listener.register(SelectManyShuttle.init, Phase.DOCUMENT_READY);
Listener.register(SelectManyShuttle.init, Phase.AFTER_UPDATE);
