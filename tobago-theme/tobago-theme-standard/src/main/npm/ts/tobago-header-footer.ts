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

  class HeaderFooter {

    static init = function (element: HTMLElement): void {

      // fixing fixed header/footer: content should not scroll behind the footer

      const body: JQuery<NodeListOf<Element>> = Tobago4.Utils.selectWithJQuery(jQuery(element), "body");
      const headers: JQuery<NodeListOf<Element>> = Tobago4.Utils.selectWithJQuery(jQuery(element), ".fixed-top");
      const footers: JQuery<NodeListOf<Element>> = Tobago4.Utils.selectWithJQuery(jQuery(element), ".fixed-bottom");

      setMargins(body, headers, footers);

      let lastMaxHeaderHeight = 0;
      let lastMaxFooterHeight = 0;
      jQuery(window).on("resize", function (): void {
        const maxHeaderHeight: number = HeaderFooter.getMaxHeaderHeight(headers);
        const maxFooterHeight: number = HeaderFooter.getMaxFooterHeight(footers);

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
        const maxHeaderHeight: number = HeaderFooter.getMaxHeaderHeight(headers);
        const maxFooterHeight: number = HeaderFooter.getMaxFooterHeight(footers);

        if (maxHeaderHeight > 0) {
          body.css("margin-top", maxHeaderHeight + "px");
        }
        if (maxFooterHeight > 0) {
          body.css("margin-bottom", maxFooterHeight + "px");
        }
      }
    };

    static getMaxHeaderHeight = function (headers: JQuery<NodeListOf<Element>>): number {
      let maxHeaderHeight: number = 0;
      headers.each(function (): void {
        const height: number = jQuery(this).outerHeight(true);
        if (height > maxHeaderHeight) {
          maxHeaderHeight = height;
        }
      });
      return maxHeaderHeight;
    };

    static getMaxFooterHeight = function (footers: JQuery<NodeListOf<Element>>): number {
      let maxFooterHeight: number = 0;
      footers.each(function (): void {
        const height: number = jQuery(this).outerHeight(true);
        if (height > maxFooterHeight) {
          maxFooterHeight = height;
        }
      });
      return maxFooterHeight;
    };

  }

  Listener.register(HeaderFooter.init, Phase.DOCUMENT_READY);
  Listener.register(HeaderFooter.init, Phase.AFTER_UPDATE);
}
