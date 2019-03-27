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

      const splitLayouts: Array<HTMLElement> = element.getSelfOrElementsByClassName("tobago-splitLayout");
      splitLayouts.forEach(function (splitLayout: HTMLElement): void {

        Array.from(splitLayout.getElementsByClassName("tobago-splitLayout-horizontal"))
            .forEach(function (splitter): void {
              splitter.addEventListener("mousedown", function (event: MouseEvent) {
                event.preventDefault();
                const splitter = <HTMLElement>event.target;
                const prev: HTMLElement = splitter.getPreviousElementSibling();
                const width: number = prev.offsetWidth;
                console.info("initial width = " + width);
                prev.style.width = width + "px";
                prev.style.flexGrow = "inherit";
                prev.style.flexBasis = "auto";
                // tbd: DOMStringMap, IE11?

                // new SplitLayoutMouseMoveData()

                document.tobagoPage().dataset["splitLayout.offset"] = String(event.pageX - width);
                document.tobagoPage().dataset["splitLayout.active"] = splitLayout.id;
                document.addEventListener("mousemove", SplitLayout.moveHorizontally);
                document.addEventListener("mouseup", SplitLayout.stop);
              });
            });

        Array.from(splitLayout.getElementsByClassName("tobago-splitLayout-vertical"))
            .forEach(function (splitter): void {
              splitter.addEventListener("mousedown", function (event: MouseEvent) {
                event.preventDefault();
                const splitter = <HTMLElement>event.target;
                const prev: HTMLElement = splitter.getPreviousElementSibling();
                const height: number = prev.offsetHeight;
                console.info("initial height = " + height);
                prev.style.height = height + "px";
                prev.style.flexGrow = "inherit";
                prev.style.flexBasis = "auto";
                // tbd: DOMStringMap, IE11?
                document.tobagoPage().dataset["splitLayout.offset"] = String(event.pageY - height);
                document.tobagoPage().dataset["splitLayout.active"] = splitLayout.id;
                document.addEventListener("mousemove", SplitLayout.moveVertically);
                document.addEventListener("mouseup", SplitLayout.stop);
              });
            });
      });
    };

    static moveHorizontally = function (event: MouseEvent): void {
      event.preventDefault();
      const offset: number = Number(document.tobagoPage().dataset["splitLayout.offset"]);
      const splitLayout: HTMLElement = document.getElementById(document.tobagoPage().dataset["splitLayout.active"]);
      console.info("" + event.pageX + " " + offset);
      const splitter: HTMLElement = <HTMLElement>splitLayout.getElementsByClassName("tobago-splitLayout-horizontal").item(0);
      const prev: HTMLElement = splitter.getPreviousElementSibling();
      prev.style.width = String(event.pageX - offset) + "px";
    };

    static moveVertically = function (event: MouseEvent): void {
      event.preventDefault();
      const offset: number = Number(document.tobagoPage().dataset["splitLayout.offset"]);
      const splitLayout: HTMLElement = document.getElementById(document.tobagoPage().dataset["splitLayout.active"]);
      console.info("" + event.pageY + " " + offset);
      const splitter: HTMLElement = <HTMLElement>splitLayout.getElementsByClassName("tobago-splitLayout-vertical").item(0);
      const prev: HTMLElement = splitter.getPreviousElementSibling();
      prev.style.height = String(event.pageY - offset) + "px";
    };

    static stop = function (event): void {
      document.removeEventListener("mousemove", SplitLayout.moveHorizontally);
      document.removeEventListener("mousemove", SplitLayout.moveVertically);
      document.removeEventListener("mouseup", SplitLayout.stop);
    };
  }

  Listener.register(SplitLayout.init, Phase.DOCUMENT_READY);
  Listener.register(SplitLayout.init, Phase.AFTER_UPDATE);
}
