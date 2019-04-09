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

    private readonly element: HTMLDivElement;

    constructor(element: HTMLDivElement) {
      this.element = element;

      for (const splitter of this.element.getElementsByClassName("tobago-splitLayout-horizontal")) {
        splitter.addEventListener("mousedown", this.start.bind(this));
      }

      for (const splitter of this.element.getElementsByClassName("tobago-splitLayout-vertical")) {
        splitter.addEventListener("mousedown", this.start.bind(this));
      }
    }

    static init = function (element: HTMLElement): void {
      for (const splitLayout of element.tobagoSelfOrElementsByClassName("tobago-splitLayout")) {
        new SplitLayout(<HTMLDivElement>splitLayout);
      }
    };

    start(event: MouseEvent) {
      event.preventDefault();
      const mousedown = SplitLayoutMousedown.save(event, <HTMLElement>event.target);
      document.addEventListener("mousemove", this.move.bind(this));
      document.addEventListener("mouseup", this.stop.bind(this));
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

    move(event: MouseEvent): void {
      event.preventDefault();
      const data = SplitLayoutMousedown.load();
      const offset: number = data.offset;
      const previousArea = data.previous;
      if (data.horizontal) {
        previousArea.style.width = String(event.pageX - offset) + "px";
      } else {
        previousArea.style.height = String(event.pageY - offset) + "px";
      }
    };

    stop(event: MouseEvent): void {
      document.removeEventListener("mousemove", this.move.bind(this));
      document.removeEventListener("mouseup", this.stop.bind(this));
      SplitLayoutMousedown.remove();
    };
  }

  /**
   * Data class to store "offset" and "splitter"-element of the mouse down event in the tobago-page.
   */
  interface SplitLayoutMousedownData {
    readonly offset: number;
    readonly splitLayoutId: string;
    readonly splitterIndex: number;
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
          .item(this.data.splitterIndex) as HTMLElement;
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
        splitterIndex: this.indexOfSplitter(splitter, horizontal),
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

    private static indexOfSplitter(splitter: HTMLElement, horizontal: boolean): number {
      const list = splitter.parentElement.getElementsByClassName(
          horizontal ? "tobago-splitLayout-horizontal" : "tobago-splitLayout-vertical");
      for (let k = 0; k < list.length; k++) {
        if (list.item(k) === splitter) {
          return k;
        }
      }
      return -1;
    }

  }

  Listener.register(SplitLayout.init, Phase.DOCUMENT_READY);
  Listener.register(SplitLayout.init, Phase.AFTER_UPDATE);
}
