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

class TreeListbox {

  id: string;

  static init = function (element) {
    for (const treeListbox of DomUtils.selfOrElementsByClassName(element, "tobago-treeListbox")) {
      new TreeListbox(treeListbox);
    }
  };

  constructor(element: HTMLElement) {

    this.id = element.id;

    const selects = element.getElementsByTagName("select");
    for (let i = 0; i < selects.length; i++) {
      const listbox = selects.item(i) as HTMLSelectElement;
      // hide select tags for level > root
      if (listbox.previousElementSibling) {
        listbox.classList.add("d-none");
      }

      // add on change on all select tag, all options that are not selected hide there dedicated
      // select tag, and the selected option show its dedicated select tag.
      if (!listbox.disabled) {
        listbox.addEventListener("change", this.onChange.bind(this));
      }
    }
  }

  onChange(event: TextEvent) {
    let listbox = event.currentTarget as HTMLSelectElement;
    for (const child of listbox.children) {
      const option = child as HTMLOptionElement;
      if (option.tagName === "OPTION") {
        if (option.selected) {
          this.setSelected(option);
          let select = document.getElementById(option.id + DomUtils.SUB_COMPONENT_SEP + "parent") as HTMLSelectElement;
          if (!select) {
            select = listbox.parentElement.nextElementSibling.children[0] as HTMLSelectElement; // dummy
          }
          select.classList.remove("d-none");
          for (const sibling of listbox.parentElement.nextElementSibling.children) {
            if (sibling === select) {
              (sibling as HTMLElement).classList.remove("d-none");
            } else {
              (sibling as HTMLElement).classList.add("d-none");
            }
          }
        }
      }
    }

    // Deeper level (2nd and later) should only show the empty select tag.
    // The first child is the empty selection.

    let next = listbox.parentElement.nextElementSibling;
    if (next) {
      for (next = next.nextElementSibling; next; next = next.nextElementSibling) {
        for (const child of next.children) {
          const select = child as HTMLSelectElement;
          if (select.previousElementSibling) { // is not the first
            select.classList.add("d-none");
          } else { // is the first
            select.classList.remove("d-none");
          }
        }
      }
    }
  }

  setSelected(option: HTMLOptionElement) {
    const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "selected") as HTMLInputElement;
    if (hidden) {
      let value = <number[]>JSON.parse(hidden.value);
      value = []; // todo: multi-select
      value.push(parseInt(option.dataset["tobagoRowIndex"]));
      hidden.value = JSON.stringify(value);
    }
  }
}

Listener.register(TreeListbox.init, Phase.DOCUMENT_READY);
Listener.register(TreeListbox.init, Phase.AFTER_UPDATE);
