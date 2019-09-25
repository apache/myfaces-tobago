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

import {Listener, Order, Phase} from "./tobago-listener";
import {DomUtils} from "./tobago-utils";

class Scroll {

  static initScrollPosition = function (element: HTMLElement): void {
    for (const panel of DomUtils.selfOrQuerySelectorAll(element, "[data-tobago-scroll-panel]")) {

      const hidden = panel.querySelector(":scope > [data-tobago-scroll-position]") as HTMLInputElement;
      const values: number[] = JSON.parse(hidden.value);
      if (values.length === 2) {
        panel.scrollLeft = values[0];
        panel.scrollTop = values[1];
      } else {
        console.warn("Wrong syntax for scroll: " + hidden.value);
      }

      panel.addEventListener("scroll", Scroll.scroll);
    }
  };

  static scroll = function (event: Event): void {
    const panel = event.currentTarget as HTMLDivElement;
    const scrollLeft = panel.scrollLeft;
    const scrollTop = panel.scrollTop;
    const hidden = panel.querySelector(":scope > [data-tobago-scroll-position]") as HTMLInputElement;
    hidden.value = JSON.stringify([scrollLeft, scrollTop]);
  };
}

Listener.register(Scroll.initScrollPosition, Phase.DOCUMENT_READY, Order.LATER);
Listener.register(Scroll.initScrollPosition, Phase.AFTER_UPDATE, Order.LATER);
