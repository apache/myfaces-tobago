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

  const init = function (elements: HTMLElement | HTMLDocument): void {

    const $elements: any = jQuery(elements);
    initSplitLayout(<JQuery<NodeListOf<Element>>>$elements); // XXX cast and any is ugly.

    // fixing fixed header/footer: content should not scroll behind the footer

    const body: JQuery<NodeListOf<Element>> = Tobago4.Utils.selectWithJQuery(elements, "body");
    const headers: JQuery<NodeListOf<Element>> = Tobago4.Utils.selectWithJQuery(elements, ".fixed-top");
    const footers: JQuery<NodeListOf<Element>> = Tobago4.Utils.selectWithJQuery(elements, ".fixed-bottom");

    setMargins(body, headers, footers);

    let lastMaxHeaderHeight = 0;
    let lastMaxFooterHeight = 0;
    jQuery(window).on("resize", function (): void {
      const maxHeaderHeight: number = getMaxHeaderHeight(headers);
      const maxFooterHeight: number = getMaxFooterHeight(footers);

      if (maxHeaderHeight !== lastMaxHeaderHeight
          || maxFooterHeight !== lastMaxFooterHeight) {
        setMargins(body, headers, footers);

        lastMaxHeaderHeight = maxHeaderHeight;
        lastMaxFooterHeight = maxFooterHeight;
      }
    });

    function setMargins(
        body: JQuery<NodeListOf<Element>>,
        headers: JQuery<NodeListOf<Element>>,
        footers: JQuery<NodeListOf<Element>>): void {
      const maxHeaderHeight: number = getMaxHeaderHeight(headers);
      const maxFooterHeight: number = getMaxFooterHeight(footers);

      if (maxHeaderHeight > 0) {
        body.css("margin-top", maxHeaderHeight + "px");
      }
      if (maxFooterHeight > 0) {
        body.css("margin-bottom", maxFooterHeight + "px");
      }
    }
  };

  const getMaxHeaderHeight = function (headers: JQuery<NodeListOf<Element>>): number {
    let maxHeaderHeight: number = 0;
    headers.each(function (): void {
      const height: number = jQuery(this).outerHeight(true);
      if (height > maxHeaderHeight) {
        maxHeaderHeight = height;
      }
    });
    return maxHeaderHeight;
  };

  const getMaxFooterHeight = function (footers: JQuery<NodeListOf<Element>>): number {
    let maxFooterHeight: number = 0;
    footers.each(function (): void {
      const height: number = jQuery(this).outerHeight(true);
      if (height > maxFooterHeight) {
        maxFooterHeight = height;
      }
    });
    return maxFooterHeight;
  };

  const initSplitLayout = function (elements: JQuery<NodeListOf<Element>>): void {
    const hSplitter: JQuery<NodeListOf<Element>> = Tobago4.Utils.selectWithJQuery(elements, ".tobago-splitLayout-horizontal");
    hSplitter.each(function (): void {
      const splitter: JQuery<NodeListOf<Element>> = jQuery(this);
      splitter.on("mousedown", {splitter: splitter}, function (event) {
        const prev: JQuery<NodeListOf<Element>> = splitter.prevAll(":not(style):last");
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
            moveHorizontally);
        jQuery(document).on("mouseup", stop);
      });
    });
    const vSplitter: JQuery<NodeListOf<Element>>
        = Tobago4.Utils.selectWithJQuery(elements, ".tobago-splitLayout-vertical");
    vSplitter.each(function (): void {
      const splitter = jQuery(this);
      splitter.on("mousedown", {splitter: splitter}, function (event) {
        const prev: JQuery<NodeListOf<Element>> = splitter.prevAll(":not(style):last");
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
            moveVertically);
        jQuery(document).on("mouseup", stop);
      });
    });
  };

  const moveHorizontally = function (event): void {
    console.info("" + event.pageX + " " + event.data.offset);
    const prev: JQuery<NodeListOf<Element>> = event.data.splitter.prev();
    prev.width(event.pageX - event.data.offset + "px");
  };

  const moveVertically = function (event): void {
    console.info("" + event.pageY + " " + event.data.offset);
    const prev: JQuery<NodeListOf<Element>> = event.data.splitter.prev();
    prev.height(event.pageY - event.data.offset + "px");
  };

  const stop = function (event): void {
    jQuery(document).off("mousemove", moveHorizontally);
    jQuery(document).off("mousemove", moveVertically);
    jQuery(document).off("mouseup", stop);
  };

  Listener.register(init, Phase.DOCUMENT_READY);
  Listener.register(init, Phase.AFTER_UPDATE);
}
