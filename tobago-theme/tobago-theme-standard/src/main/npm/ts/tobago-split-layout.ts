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

      const hSplitter: Array<Element> = querySelectorAllOrSelfByClass(element, "tobago-splitLayout-horizontal");
      hSplitter.forEach(function (element): void {
        // const hSplitter: JQuery<NodeListOf<Element>>
        //     = Tobago4.Utils.selectWithJQuery(jQuery(element), ".tobago-splitLayout-horizontal");
        // hSplitter.each(function (): void {
        const splitter: JQuery<Element> = jQuery(element);
        splitter.on("mousedown", {splitter: splitter}, function (event) {
          const prev: JQuery<Element> = splitter.prevAll(":not(style):last");
          const width: number = prev.outerWidth();
          console.info("initial width = " + width);
          prev.css("width", width + "px");
          prev.css({
                "flex-grow": "inherit",
                "flex-basis": "auto"
              }
          );
          jQuery(document).on(
              "mousemove",
              {
                offset: event.pageX - width,
                splitter: splitter
              },
              SplitLayout.moveHorizontally);
          jQuery(document).on("mouseup", SplitLayout.stop);
        });
      });
      const vSplitter: Array<Element> = querySelectorAllOrSelfByClass(element, "tobago-splitLayout-vertical");
      vSplitter.forEach(function (value): void {
        // const vSplitter: JQuery<NodeListOf<Element>>
        //     = Tobago4.Utils.selectWithJQuery(jQuery(element), ".tobago-splitLayout-vertical");
        // vSplitter.each(function (): void {
        const splitter = jQuery(value);
        splitter.on("mousedown", {splitter: splitter}, function (event) {
          const prev: JQuery<Element> = splitter.prevAll(":not(style):last");
          const height: number = prev.outerHeight();
          console.info("initial height = " + height);
          prev.css("height", height + "px");
          prev.css({"flex-grow": "inherit", "flex-basis": "auto"});
          jQuery(document).on(
              "mousemove",
              {
                offset: event.pageY - height,
                splitter: splitter
              },
              SplitLayout.moveVertically);
          jQuery(document).on("mouseup", SplitLayout.stop);
        });
      });
    };

    static moveHorizontally = function (event): void {
      console.info("" + event.pageX + " " + event.data.offset);
      const prev: JQuery<NodeListOf<Element>> = event.data.splitter.prev();
      prev.width(event.pageX - event.data.offset + "px");
    };

    static moveVertically = function (event): void {
      console.info("" + event.pageY + " " + event.data.offset);
      const prev: JQuery<NodeListOf<Element>> = event.data.splitter.prev();
      prev.height(event.pageY - event.data.offset + "px");
    };

    static stop = function (event): void {
      jQuery(document).off("mousemove", SplitLayout.moveHorizontally);
      jQuery(document).off("mousemove", SplitLayout.moveVertically);
      jQuery(document).off("mouseup", SplitLayout.stop);
    };

  }

  Listener.register(SplitLayout.init, Phase.DOCUMENT_READY);
  Listener.register(SplitLayout.init, Phase.AFTER_UPDATE);
}
