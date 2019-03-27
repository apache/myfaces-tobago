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

namespace Tobago {

  class SplitLayout {

    static init = function (element: HTMLElement): void {

      const splitLayouts: Array<HTMLElement> = element.tobagoSelfOrElementsByClassName("tobago-splitLayout");
      splitLayouts.forEach(function (splitLayout: HTMLElement): void {

        Array.from(splitLayout.getElementsByClassName("tobago-splitLayout-horizontal"))
            .forEach(function (splitter): void {
              splitter.addEventListener("mousedown", SplitLayout.start);
            });

        Array.from(splitLayout.getElementsByClassName("tobago-splitLayout-vertical"))
            .forEach(function (splitter): void {
              splitter.addEventListener("mousedown", SplitLayout.start);
            });
      });
    };

    static start = function (event: MouseEvent) {
      event.preventDefault();
      const mousedown = SplitLayoutMousedown.save(event, <HTMLElement>event.target);
      document.addEventListener("mousemove", SplitLayout.move);
      document.addEventListener("mouseup", SplitLayout.stop);
      const previousArea = mousedown.previous;
      if (mousedown.horizontal) {
        previousArea.style.width = String(previousArea.offsetWidth + "px");
      } else {
        previousArea.style.height = String(previousArea.offsetHeight + "px");
      }
      previousArea.style.flexGrow = "inherit";
      previousArea.style.flexBasis = "auto";
      console.info("initial = " + (mousedown.horizontal ? previousArea.style.width : previousArea.style.height));
    };

    static move = function (event: MouseEvent): void {
      event.preventDefault();
      const data = SplitLayoutMousedown.load();
      const offset: number = data.offset;
      const previousArea: HTMLElement = data.previous;
      if (data.horizontal) {
        previousArea.style.width = String(event.pageX - offset) + "px";
      } else {
        previousArea.style.height = String(event.pageY - offset) + "px";
      }
    };

    static stop = function (event: MouseEvent): void {
      document.removeEventListener("mousemove", SplitLayout.move);
      document.removeEventListener("mouseup", SplitLayout.stop);
      SplitLayoutMousedown.remove();
    };
  }

  /**
   * Data class to store "offset" and "splitter"-element of the mouse down event in the tobago-page.
   */
  interface SplitLayoutMousedownData {
    readonly offset: number;
    readonly splitLayoutId: string;
    readonly horizontal: boolean;
  }

  class SplitLayoutMousedown {

    private data: SplitLayoutMousedownData;

    private constructor(data: SplitLayoutMousedownData | string) {
      this.data = typeof data === "string" ? JSON.parse(data) : data;
    }

    get offset(): number {
      return this.data.offset;
    }

    get splitter(): HTMLElement {
      return document.getElementById(this.data.splitLayoutId).getElementsByClassName(
          this.data.horizontal ? "tobago-splitLayout-horizontal" : "tobago-splitLayout-vertical")
          .item(0) as HTMLElement;
    }

    get previous(): HTMLElement {
      return this.splitter.tobagoPreviousElementSibling();
    }

    get horizontal(): boolean {
      return this.data.horizontal;
    }

    static save(event: MouseEvent, splitter: HTMLElement): SplitLayoutMousedown {
      const horizontal = splitter.classList.contains("tobago-splitLayout-horizontal");
      const previous = splitter.tobagoPreviousElementSibling();
      const data: SplitLayoutMousedownData = {
        splitLayoutId: splitter.parentElement.id,
        horizontal: horizontal,
        offset: horizontal ? event.pageX - previous.offsetWidth : event.pageY - previous.offsetHeight
      };
      document.tobagoPage().dataset["SplitLayoutMousedownData"] = JSON.stringify(data);
      return new SplitLayoutMousedown(data);
    }

    static load() {
      return new SplitLayoutMousedown(document.tobagoPage().dataset["SplitLayoutMousedownData"]);
    }

    static remove() {
      return document.tobagoPage().dataset["SplitLayoutMousedownData"] = null;
    }

  }

  Listener.register(SplitLayout.init, Phase.DOCUMENT_READY);
  Listener.register(SplitLayout.init, Phase.AFTER_UPDATE);
}
