(function (factory) {
    typeof define === 'function' && define.amd ? define(factory) :
    factory();
}((function () { 'use strict';

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
    var Phase;
    (function (Phase) {
        /** after the DOM was build */
        Phase[Phase["DOCUMENT_READY"] = 0] = "DOCUMENT_READY";
        /** after all images and CSS was loaded */
        Phase[Phase["WINDOW_LOAD"] = 1] = "WINDOW_LOAD";
        /** before sending a normal submit action */
        Phase[Phase["BEFORE_SUBMIT"] = 2] = "BEFORE_SUBMIT";
        /** after an AJAX call */
        Phase[Phase["AFTER_UPDATE"] = 3] = "AFTER_UPDATE";
        /** before ending a page */
        Phase[Phase["BEFORE_UNLOAD"] = 4] = "BEFORE_UNLOAD";
        /** before closing a window or tab */
        Phase[Phase["BEFORE_EXIT"] = 5] = "BEFORE_EXIT";
    })(Phase || (Phase = {}));
    var Order;
    (function (Order) {
        Order[Order["EARLIER"] = 0] = "EARLIER";
        Order[Order["EARLY"] = 1] = "EARLY";
        Order[Order["NORMAL"] = 2] = "NORMAL";
        Order[Order["LATE"] = 3] = "LATE";
        Order[Order["LATER"] = 4] = "LATER";
    })(Order || (Order = {}));
    class ListenerList {
        constructor() {
            this.map = new Map([
                [Order.EARLIER, []],
                [Order.EARLY, []],
                [Order.NORMAL, []],
                [Order.LATE, []],
                [Order.LATER, []]
            ]);
        }
        add(listener, order) {
            this.map.get(order).push(listener);
        }
        execute(element) {
            this.map.forEach((listeners, order) => {
                listeners.forEach((listener, index) => {
                    console.time("[tobago] execute " + order + " " + index);
                    listener(element);
                    console.timeEnd("[tobago] execute " + order + " " + index);
                });
            });
        }
    }
    class Listener {
        /**
         * Register a function to be executed on certain events.
         * @param listener Function to be executed.
         * @param phase The phase when code should be executed (e. g. Phase.DOCUMENT_READY).
         * @param order An optional order to sort function they depend on others (default: Tobago.Order.NORMAL).
         */
        static register(listener, phase, order = Order.NORMAL) {
            switch (phase) {
                case Phase.DOCUMENT_READY:
                    Listener.documentReady.add(listener, order);
                    break;
                case Phase.WINDOW_LOAD:
                    Listener.windowLoad.add(listener, order);
                    break;
                case Phase.BEFORE_SUBMIT:
                    Listener.beforeSubmit.add(listener, order);
                    break;
                case Phase.AFTER_UPDATE:
                    Listener.afterUpdate.add(listener, order);
                    break;
                case Phase.BEFORE_UNLOAD:
                    Listener.beforeUnload.add(listener, order);
                    break;
                case Phase.BEFORE_EXIT:
                    Listener.beforeExit.add(listener, order);
                    break;
                default:
                    console.error("Unknown phase: '" + phase + "'");
            }
        }
        static executeDocumentReady(element) {
            console.time("[tobago] execute documentReady");
            Listener.documentReady.execute(element);
            console.timeEnd("[tobago] execute documentReady");
        }
        static executeWindowLoad() {
            console.time("[tobago] execute windowLoad");
            Listener.windowLoad.execute();
            console.timeEnd("[tobago] execute windowLoad");
        }
        static executeBeforeSubmit() {
            console.time("[tobago] execute beforeSubmit");
            Listener.beforeSubmit.execute();
            console.timeEnd("[tobago] execute beforeSubmit");
        }
        static executeAfterUpdate(element) {
            console.time("[tobago] execute afterUpdate");
            Listener.afterUpdate.execute(element);
            console.timeEnd("[tobago] execute afterUpdate");
        }
        static executeBeforeUnload() {
            console.time("[tobago] execute beforeUnload");
            Listener.beforeUnload.execute();
            console.timeEnd("[tobago] execute beforeUnload");
        }
        static executeBeforeExit() {
            console.time("[tobago] execute beforeExit");
            Listener.beforeExit.execute();
            console.timeEnd("[tobago] execute beforeExit");
        }
    }
    // XXX check if "static" is nice
    Listener.documentReady = new ListenerList();
    Listener.windowLoad = new ListenerList();
    Listener.beforeSubmit = new ListenerList();
    Listener.afterUpdate = new ListenerList();
    Listener.beforeUnload = new ListenerList();
    Listener.beforeExit = new ListenerList();

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
    class DomUtils {
        /**
         * Find all elements (and also self) which have the class "className".
         * @param element Starting element in DOM to collect.
         * @param className Class of elements to find.
         */
        static selfOrElementsByClassName(element, className) {
            const result = new Array();
            if (element.classList.contains(className)) {
                result.push(element);
            }
            const list = element.getElementsByClassName(className);
            for (let i = 0; i < list.length; i++) {
                result.push(list.item(i));
            }
            return result;
        }
        /**
         * Find all elements (and also self) which have the attribute "attributeName".
         * @param element Starting element in DOM to collect.
         * @param selectors Name of the attribute of the elements to find.
         */
        // todo: may return NodeListOf<HTMLElementTagNameMap[K]> or something like that.
        static selfOrQuerySelectorAll(element, selectors) {
            const result = new Array();
            if (element.matches(selectors)) {
                result.push(element);
            }
            for (const found of element.querySelectorAll(selectors)) {
                result.push(found);
            }
            return result;
        }
        /**
         * Get the previous sibling element (without <style> elements).
         */
        static previousElementSibling(element) {
            let sibling = element.previousElementSibling;
            while (sibling != null) {
                if (sibling.tagName !== "STYLE") {
                    return sibling;
                }
                sibling = sibling.previousElementSibling;
            }
            return null;
        }
        /**
         * Get the next sibling element (without <style> elements).
         */
        static nextElementSibling(element) {
            let sibling = element.nextElementSibling;
            while (sibling !== null) {
                if (sibling.tagName !== "STYLE") {
                    return sibling;
                }
                sibling = sibling.nextElementSibling;
            }
            return null;
        }
        static outerWidthWithMargin(element) {
            const style = window.getComputedStyle(element);
            return element.offsetWidth + parseInt(style.marginLeft) + parseInt(style.marginRight);
        }
        static outerHeightWithMargin(element) {
            const style = window.getComputedStyle(element);
            return element.offsetHeight + parseInt(style.marginTop) + parseInt(style.marginBottom);
        }
        static offset(element) {
            let top = 0;
            let left = 0;
            let currentElement = element;
            while (currentElement) {
                top += (currentElement.offsetTop - currentElement.scrollTop + currentElement.clientTop);
                left += (currentElement.offsetLeft - currentElement.scrollLeft + currentElement.clientLeft);
                currentElement = currentElement.offsetParent;
            }
            return { top: top, left: left };
        }
        static isVisible(element) {
            return element.offsetWidth > 0 || element.offsetHeight > 0 || element.getClientRects().length > 0;
        }
        /**
         *
         * @param id A JSF client id, type=string. Example: escapeClientId("page:input") -> "#page\\:input"
         * @return A string which can be used as a jQuery selector.
         */
        static escapeClientId(id) {
            return "#" + id.replace(/([:\.])/g, "\\$1");
        }
        /**
         * "a:b" -> "a"
         * "a:b:c" -> "a:b"
         * "a" -> null
         * null -> null
         * "a:b::sub-component" -> "a"
         * "a::sub-component:b" -> "a::sub-component" // should currently not happen in Tobago
         *
         * @param clientId The clientId of a component.
         * @return The clientId of the naming container.
         */
        static getNamingContainerId(clientId) {
            if (clientId == null || clientId.lastIndexOf(DomUtils.COMPONENT_SEP) === -1) {
                return null;
            }
            let id = clientId;
            while (true) {
                const sub = id.lastIndexOf(DomUtils.SUB_COMPONENT_SEP);
                if (sub == -1) {
                    break;
                }
                if (sub + 1 == id.lastIndexOf(DomUtils.COMPONENT_SEP)) {
                    id = id.substring(0, sub);
                }
                else {
                    break;
                }
            }
            return id.substring(0, id.lastIndexOf(DomUtils.COMPONENT_SEP));
        }
        /**
         * @param element with transition
         * @return transition time in milliseconds
         */
        static getTransitionTime(element) {
            const style = getComputedStyle(element);
            let delay = parseFloat(style.transitionDelay);
            let duration = parseFloat(style.transitionDuration);
            return (delay + duration) * 1000;
        }
    }
    /**
     * JSF's component separator constant
     */
    DomUtils.COMPONENT_SEP = ":";
    /**
     * Tobago's sub-component separator constant
     */
    DomUtils.SUB_COMPONENT_SEP = "::";

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
    class Bar extends HTMLElement {
        constructor() {
            super();
            this.CssClass = {
                SHOW: "show",
                COLLAPSE: "collapse",
                COLLAPSING: "collapsing"
            };
            this.ariaExpanded = "aria-expanded";
            this.toggleButton.addEventListener("click", this.toggleCollapse.bind(this));
        }
        connectedCallback() {
            this.expanded = this.toggleButton.getAttribute(this.ariaExpanded) === "true";
        }
        toggleCollapse(event) {
            window.clearTimeout(this.timeout);
            if (this.expanded) {
                this.expanded = false;
                this.navbarContent.style.height = this.navbarContent.scrollHeight + "px";
                this.navbarContent.offsetHeight; //force reflow, to make sure height is set
                this.navbarContent.classList.add(this.CssClass.COLLAPSING);
                this.navbarContent.classList.remove(this.CssClass.COLLAPSE);
                this.navbarContent.classList.remove(this.CssClass.SHOW);
                this.navbarContent.style.height = null;
                this.timeout = window.setTimeout(() => {
                    this.navbarContent.classList.remove(this.CssClass.COLLAPSING);
                    this.navbarContent.classList.add(this.CssClass.COLLAPSE);
                    this.toggleButton.setAttribute(this.ariaExpanded, "false");
                }, DomUtils.getTransitionTime(this.navbarContent));
            }
            else {
                this.expanded = true;
                this.navbarContent.classList.remove(this.CssClass.COLLAPSE);
                this.navbarContent.classList.add(this.CssClass.COLLAPSING);
                this.navbarContent.style.height = this.navbarContent.scrollHeight + "px";
                this.timeout = window.setTimeout(() => {
                    this.navbarContent.classList.remove(this.CssClass.COLLAPSING);
                    this.navbarContent.classList.add(this.CssClass.COLLAPSE);
                    this.navbarContent.classList.add(this.CssClass.SHOW);
                    this.navbarContent.style.height = null;
                    this.toggleButton.setAttribute(this.ariaExpanded, "true");
                }, DomUtils.getTransitionTime(this.navbarContent));
            }
        }
        get toggleButton() {
            return this.querySelector(".navbar-toggler");
        }
        get navbarContent() {
            return this.querySelector(".navbar-collapse");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-bar") == null) {
            window.customElements.define("tobago-bar", Bar);
        }
    });

    /**!
     * @fileOverview Kickass library to create and place poppers near their reference elements.
     * @version 1.16.1
     * @license
     * Copyright (c) 2016 Federico Zivolo and contributors
     *
     * Permission is hereby granted, free of charge, to any person obtaining a copy
     * of this software and associated documentation files (the "Software"), to deal
     * in the Software without restriction, including without limitation the rights
     * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     * copies of the Software, and to permit persons to whom the Software is
     * furnished to do so, subject to the following conditions:
     *
     * The above copyright notice and this permission notice shall be included in all
     * copies or substantial portions of the Software.
     *
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
     * SOFTWARE.
     */
    var isBrowser = typeof window !== 'undefined' && typeof document !== 'undefined' && typeof navigator !== 'undefined';

    var timeoutDuration = function () {
      var longerTimeoutBrowsers = ['Edge', 'Trident', 'Firefox'];
      for (var i = 0; i < longerTimeoutBrowsers.length; i += 1) {
        if (isBrowser && navigator.userAgent.indexOf(longerTimeoutBrowsers[i]) >= 0) {
          return 1;
        }
      }
      return 0;
    }();

    function microtaskDebounce(fn) {
      var called = false;
      return function () {
        if (called) {
          return;
        }
        called = true;
        window.Promise.resolve().then(function () {
          called = false;
          fn();
        });
      };
    }

    function taskDebounce(fn) {
      var scheduled = false;
      return function () {
        if (!scheduled) {
          scheduled = true;
          setTimeout(function () {
            scheduled = false;
            fn();
          }, timeoutDuration);
        }
      };
    }

    var supportsMicroTasks = isBrowser && window.Promise;

    /**
    * Create a debounced version of a method, that's asynchronously deferred
    * but called in the minimum time possible.
    *
    * @method
    * @memberof Popper.Utils
    * @argument {Function} fn
    * @returns {Function}
    */
    var debounce = supportsMicroTasks ? microtaskDebounce : taskDebounce;

    /**
     * Check if the given variable is a function
     * @method
     * @memberof Popper.Utils
     * @argument {Any} functionToCheck - variable to check
     * @returns {Boolean} answer to: is a function?
     */
    function isFunction(functionToCheck) {
      var getType = {};
      return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
    }

    /**
     * Get CSS computed property of the given element
     * @method
     * @memberof Popper.Utils
     * @argument {Eement} element
     * @argument {String} property
     */
    function getStyleComputedProperty(element, property) {
      if (element.nodeType !== 1) {
        return [];
      }
      // NOTE: 1 DOM access here
      var window = element.ownerDocument.defaultView;
      var css = window.getComputedStyle(element, null);
      return property ? css[property] : css;
    }

    /**
     * Returns the parentNode or the host of the element
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element
     * @returns {Element} parent
     */
    function getParentNode(element) {
      if (element.nodeName === 'HTML') {
        return element;
      }
      return element.parentNode || element.host;
    }

    /**
     * Returns the scrolling parent of the given element
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element
     * @returns {Element} scroll parent
     */
    function getScrollParent(element) {
      // Return body, `getScroll` will take care to get the correct `scrollTop` from it
      if (!element) {
        return document.body;
      }

      switch (element.nodeName) {
        case 'HTML':
        case 'BODY':
          return element.ownerDocument.body;
        case '#document':
          return element.body;
      }

      // Firefox want us to check `-x` and `-y` variations as well

      var _getStyleComputedProp = getStyleComputedProperty(element),
          overflow = _getStyleComputedProp.overflow,
          overflowX = _getStyleComputedProp.overflowX,
          overflowY = _getStyleComputedProp.overflowY;

      if (/(auto|scroll|overlay)/.test(overflow + overflowY + overflowX)) {
        return element;
      }

      return getScrollParent(getParentNode(element));
    }

    /**
     * Returns the reference node of the reference object, or the reference object itself.
     * @method
     * @memberof Popper.Utils
     * @param {Element|Object} reference - the reference element (the popper will be relative to this)
     * @returns {Element} parent
     */
    function getReferenceNode(reference) {
      return reference && reference.referenceNode ? reference.referenceNode : reference;
    }

    var isIE11 = isBrowser && !!(window.MSInputMethodContext && document.documentMode);
    var isIE10 = isBrowser && /MSIE 10/.test(navigator.userAgent);

    /**
     * Determines if the browser is Internet Explorer
     * @method
     * @memberof Popper.Utils
     * @param {Number} version to check
     * @returns {Boolean} isIE
     */
    function isIE(version) {
      if (version === 11) {
        return isIE11;
      }
      if (version === 10) {
        return isIE10;
      }
      return isIE11 || isIE10;
    }

    /**
     * Returns the offset parent of the given element
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element
     * @returns {Element} offset parent
     */
    function getOffsetParent(element) {
      if (!element) {
        return document.documentElement;
      }

      var noOffsetParent = isIE(10) ? document.body : null;

      // NOTE: 1 DOM access here
      var offsetParent = element.offsetParent || null;
      // Skip hidden elements which don't have an offsetParent
      while (offsetParent === noOffsetParent && element.nextElementSibling) {
        offsetParent = (element = element.nextElementSibling).offsetParent;
      }

      var nodeName = offsetParent && offsetParent.nodeName;

      if (!nodeName || nodeName === 'BODY' || nodeName === 'HTML') {
        return element ? element.ownerDocument.documentElement : document.documentElement;
      }

      // .offsetParent will return the closest TH, TD or TABLE in case
      // no offsetParent is present, I hate this job...
      if (['TH', 'TD', 'TABLE'].indexOf(offsetParent.nodeName) !== -1 && getStyleComputedProperty(offsetParent, 'position') === 'static') {
        return getOffsetParent(offsetParent);
      }

      return offsetParent;
    }

    function isOffsetContainer(element) {
      var nodeName = element.nodeName;

      if (nodeName === 'BODY') {
        return false;
      }
      return nodeName === 'HTML' || getOffsetParent(element.firstElementChild) === element;
    }

    /**
     * Finds the root node (document, shadowDOM root) of the given element
     * @method
     * @memberof Popper.Utils
     * @argument {Element} node
     * @returns {Element} root node
     */
    function getRoot(node) {
      if (node.parentNode !== null) {
        return getRoot(node.parentNode);
      }

      return node;
    }

    /**
     * Finds the offset parent common to the two provided nodes
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element1
     * @argument {Element} element2
     * @returns {Element} common offset parent
     */
    function findCommonOffsetParent(element1, element2) {
      // This check is needed to avoid errors in case one of the elements isn't defined for any reason
      if (!element1 || !element1.nodeType || !element2 || !element2.nodeType) {
        return document.documentElement;
      }

      // Here we make sure to give as "start" the element that comes first in the DOM
      var order = element1.compareDocumentPosition(element2) & Node.DOCUMENT_POSITION_FOLLOWING;
      var start = order ? element1 : element2;
      var end = order ? element2 : element1;

      // Get common ancestor container
      var range = document.createRange();
      range.setStart(start, 0);
      range.setEnd(end, 0);
      var commonAncestorContainer = range.commonAncestorContainer;

      // Both nodes are inside #document

      if (element1 !== commonAncestorContainer && element2 !== commonAncestorContainer || start.contains(end)) {
        if (isOffsetContainer(commonAncestorContainer)) {
          return commonAncestorContainer;
        }

        return getOffsetParent(commonAncestorContainer);
      }

      // one of the nodes is inside shadowDOM, find which one
      var element1root = getRoot(element1);
      if (element1root.host) {
        return findCommonOffsetParent(element1root.host, element2);
      } else {
        return findCommonOffsetParent(element1, getRoot(element2).host);
      }
    }

    /**
     * Gets the scroll value of the given element in the given side (top and left)
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element
     * @argument {String} side `top` or `left`
     * @returns {number} amount of scrolled pixels
     */
    function getScroll(element) {
      var side = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 'top';

      var upperSide = side === 'top' ? 'scrollTop' : 'scrollLeft';
      var nodeName = element.nodeName;

      if (nodeName === 'BODY' || nodeName === 'HTML') {
        var html = element.ownerDocument.documentElement;
        var scrollingElement = element.ownerDocument.scrollingElement || html;
        return scrollingElement[upperSide];
      }

      return element[upperSide];
    }

    /*
     * Sum or subtract the element scroll values (left and top) from a given rect object
     * @method
     * @memberof Popper.Utils
     * @param {Object} rect - Rect object you want to change
     * @param {HTMLElement} element - The element from the function reads the scroll values
     * @param {Boolean} subtract - set to true if you want to subtract the scroll values
     * @return {Object} rect - The modifier rect object
     */
    function includeScroll(rect, element) {
      var subtract = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;

      var scrollTop = getScroll(element, 'top');
      var scrollLeft = getScroll(element, 'left');
      var modifier = subtract ? -1 : 1;
      rect.top += scrollTop * modifier;
      rect.bottom += scrollTop * modifier;
      rect.left += scrollLeft * modifier;
      rect.right += scrollLeft * modifier;
      return rect;
    }

    /*
     * Helper to detect borders of a given element
     * @method
     * @memberof Popper.Utils
     * @param {CSSStyleDeclaration} styles
     * Result of `getStyleComputedProperty` on the given element
     * @param {String} axis - `x` or `y`
     * @return {number} borders - The borders size of the given axis
     */

    function getBordersSize(styles, axis) {
      var sideA = axis === 'x' ? 'Left' : 'Top';
      var sideB = sideA === 'Left' ? 'Right' : 'Bottom';

      return parseFloat(styles['border' + sideA + 'Width']) + parseFloat(styles['border' + sideB + 'Width']);
    }

    function getSize(axis, body, html, computedStyle) {
      return Math.max(body['offset' + axis], body['scroll' + axis], html['client' + axis], html['offset' + axis], html['scroll' + axis], isIE(10) ? parseInt(html['offset' + axis]) + parseInt(computedStyle['margin' + (axis === 'Height' ? 'Top' : 'Left')]) + parseInt(computedStyle['margin' + (axis === 'Height' ? 'Bottom' : 'Right')]) : 0);
    }

    function getWindowSizes(document) {
      var body = document.body;
      var html = document.documentElement;
      var computedStyle = isIE(10) && getComputedStyle(html);

      return {
        height: getSize('Height', body, html, computedStyle),
        width: getSize('Width', body, html, computedStyle)
      };
    }

    var classCallCheck = function (instance, Constructor) {
      if (!(instance instanceof Constructor)) {
        throw new TypeError("Cannot call a class as a function");
      }
    };

    var createClass = function () {
      function defineProperties(target, props) {
        for (var i = 0; i < props.length; i++) {
          var descriptor = props[i];
          descriptor.enumerable = descriptor.enumerable || false;
          descriptor.configurable = true;
          if ("value" in descriptor) descriptor.writable = true;
          Object.defineProperty(target, descriptor.key, descriptor);
        }
      }

      return function (Constructor, protoProps, staticProps) {
        if (protoProps) defineProperties(Constructor.prototype, protoProps);
        if (staticProps) defineProperties(Constructor, staticProps);
        return Constructor;
      };
    }();





    var defineProperty = function (obj, key, value) {
      if (key in obj) {
        Object.defineProperty(obj, key, {
          value: value,
          enumerable: true,
          configurable: true,
          writable: true
        });
      } else {
        obj[key] = value;
      }

      return obj;
    };

    var _extends = Object.assign || function (target) {
      for (var i = 1; i < arguments.length; i++) {
        var source = arguments[i];

        for (var key in source) {
          if (Object.prototype.hasOwnProperty.call(source, key)) {
            target[key] = source[key];
          }
        }
      }

      return target;
    };

    /**
     * Given element offsets, generate an output similar to getBoundingClientRect
     * @method
     * @memberof Popper.Utils
     * @argument {Object} offsets
     * @returns {Object} ClientRect like output
     */
    function getClientRect(offsets) {
      return _extends({}, offsets, {
        right: offsets.left + offsets.width,
        bottom: offsets.top + offsets.height
      });
    }

    /**
     * Get bounding client rect of given element
     * @method
     * @memberof Popper.Utils
     * @param {HTMLElement} element
     * @return {Object} client rect
     */
    function getBoundingClientRect(element) {
      var rect = {};

      // IE10 10 FIX: Please, don't ask, the element isn't
      // considered in DOM in some circumstances...
      // This isn't reproducible in IE10 compatibility mode of IE11
      try {
        if (isIE(10)) {
          rect = element.getBoundingClientRect();
          var scrollTop = getScroll(element, 'top');
          var scrollLeft = getScroll(element, 'left');
          rect.top += scrollTop;
          rect.left += scrollLeft;
          rect.bottom += scrollTop;
          rect.right += scrollLeft;
        } else {
          rect = element.getBoundingClientRect();
        }
      } catch (e) {}

      var result = {
        left: rect.left,
        top: rect.top,
        width: rect.right - rect.left,
        height: rect.bottom - rect.top
      };

      // subtract scrollbar size from sizes
      var sizes = element.nodeName === 'HTML' ? getWindowSizes(element.ownerDocument) : {};
      var width = sizes.width || element.clientWidth || result.width;
      var height = sizes.height || element.clientHeight || result.height;

      var horizScrollbar = element.offsetWidth - width;
      var vertScrollbar = element.offsetHeight - height;

      // if an hypothetical scrollbar is detected, we must be sure it's not a `border`
      // we make this check conditional for performance reasons
      if (horizScrollbar || vertScrollbar) {
        var styles = getStyleComputedProperty(element);
        horizScrollbar -= getBordersSize(styles, 'x');
        vertScrollbar -= getBordersSize(styles, 'y');

        result.width -= horizScrollbar;
        result.height -= vertScrollbar;
      }

      return getClientRect(result);
    }

    function getOffsetRectRelativeToArbitraryNode(children, parent) {
      var fixedPosition = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;

      var isIE10 = isIE(10);
      var isHTML = parent.nodeName === 'HTML';
      var childrenRect = getBoundingClientRect(children);
      var parentRect = getBoundingClientRect(parent);
      var scrollParent = getScrollParent(children);

      var styles = getStyleComputedProperty(parent);
      var borderTopWidth = parseFloat(styles.borderTopWidth);
      var borderLeftWidth = parseFloat(styles.borderLeftWidth);

      // In cases where the parent is fixed, we must ignore negative scroll in offset calc
      if (fixedPosition && isHTML) {
        parentRect.top = Math.max(parentRect.top, 0);
        parentRect.left = Math.max(parentRect.left, 0);
      }
      var offsets = getClientRect({
        top: childrenRect.top - parentRect.top - borderTopWidth,
        left: childrenRect.left - parentRect.left - borderLeftWidth,
        width: childrenRect.width,
        height: childrenRect.height
      });
      offsets.marginTop = 0;
      offsets.marginLeft = 0;

      // Subtract margins of documentElement in case it's being used as parent
      // we do this only on HTML because it's the only element that behaves
      // differently when margins are applied to it. The margins are included in
      // the box of the documentElement, in the other cases not.
      if (!isIE10 && isHTML) {
        var marginTop = parseFloat(styles.marginTop);
        var marginLeft = parseFloat(styles.marginLeft);

        offsets.top -= borderTopWidth - marginTop;
        offsets.bottom -= borderTopWidth - marginTop;
        offsets.left -= borderLeftWidth - marginLeft;
        offsets.right -= borderLeftWidth - marginLeft;

        // Attach marginTop and marginLeft because in some circumstances we may need them
        offsets.marginTop = marginTop;
        offsets.marginLeft = marginLeft;
      }

      if (isIE10 && !fixedPosition ? parent.contains(scrollParent) : parent === scrollParent && scrollParent.nodeName !== 'BODY') {
        offsets = includeScroll(offsets, parent);
      }

      return offsets;
    }

    function getViewportOffsetRectRelativeToArtbitraryNode(element) {
      var excludeScroll = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;

      var html = element.ownerDocument.documentElement;
      var relativeOffset = getOffsetRectRelativeToArbitraryNode(element, html);
      var width = Math.max(html.clientWidth, window.innerWidth || 0);
      var height = Math.max(html.clientHeight, window.innerHeight || 0);

      var scrollTop = !excludeScroll ? getScroll(html) : 0;
      var scrollLeft = !excludeScroll ? getScroll(html, 'left') : 0;

      var offset = {
        top: scrollTop - relativeOffset.top + relativeOffset.marginTop,
        left: scrollLeft - relativeOffset.left + relativeOffset.marginLeft,
        width: width,
        height: height
      };

      return getClientRect(offset);
    }

    /**
     * Check if the given element is fixed or is inside a fixed parent
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element
     * @argument {Element} customContainer
     * @returns {Boolean} answer to "isFixed?"
     */
    function isFixed(element) {
      var nodeName = element.nodeName;
      if (nodeName === 'BODY' || nodeName === 'HTML') {
        return false;
      }
      if (getStyleComputedProperty(element, 'position') === 'fixed') {
        return true;
      }
      var parentNode = getParentNode(element);
      if (!parentNode) {
        return false;
      }
      return isFixed(parentNode);
    }

    /**
     * Finds the first parent of an element that has a transformed property defined
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element
     * @returns {Element} first transformed parent or documentElement
     */

    function getFixedPositionOffsetParent(element) {
      // This check is needed to avoid errors in case one of the elements isn't defined for any reason
      if (!element || !element.parentElement || isIE()) {
        return document.documentElement;
      }
      var el = element.parentElement;
      while (el && getStyleComputedProperty(el, 'transform') === 'none') {
        el = el.parentElement;
      }
      return el || document.documentElement;
    }

    /**
     * Computed the boundaries limits and return them
     * @method
     * @memberof Popper.Utils
     * @param {HTMLElement} popper
     * @param {HTMLElement} reference
     * @param {number} padding
     * @param {HTMLElement} boundariesElement - Element used to define the boundaries
     * @param {Boolean} fixedPosition - Is in fixed position mode
     * @returns {Object} Coordinates of the boundaries
     */
    function getBoundaries(popper, reference, padding, boundariesElement) {
      var fixedPosition = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : false;

      // NOTE: 1 DOM access here

      var boundaries = { top: 0, left: 0 };
      var offsetParent = fixedPosition ? getFixedPositionOffsetParent(popper) : findCommonOffsetParent(popper, getReferenceNode(reference));

      // Handle viewport case
      if (boundariesElement === 'viewport') {
        boundaries = getViewportOffsetRectRelativeToArtbitraryNode(offsetParent, fixedPosition);
      } else {
        // Handle other cases based on DOM element used as boundaries
        var boundariesNode = void 0;
        if (boundariesElement === 'scrollParent') {
          boundariesNode = getScrollParent(getParentNode(reference));
          if (boundariesNode.nodeName === 'BODY') {
            boundariesNode = popper.ownerDocument.documentElement;
          }
        } else if (boundariesElement === 'window') {
          boundariesNode = popper.ownerDocument.documentElement;
        } else {
          boundariesNode = boundariesElement;
        }

        var offsets = getOffsetRectRelativeToArbitraryNode(boundariesNode, offsetParent, fixedPosition);

        // In case of HTML, we need a different computation
        if (boundariesNode.nodeName === 'HTML' && !isFixed(offsetParent)) {
          var _getWindowSizes = getWindowSizes(popper.ownerDocument),
              height = _getWindowSizes.height,
              width = _getWindowSizes.width;

          boundaries.top += offsets.top - offsets.marginTop;
          boundaries.bottom = height + offsets.top;
          boundaries.left += offsets.left - offsets.marginLeft;
          boundaries.right = width + offsets.left;
        } else {
          // for all the other DOM elements, this one is good
          boundaries = offsets;
        }
      }

      // Add paddings
      padding = padding || 0;
      var isPaddingNumber = typeof padding === 'number';
      boundaries.left += isPaddingNumber ? padding : padding.left || 0;
      boundaries.top += isPaddingNumber ? padding : padding.top || 0;
      boundaries.right -= isPaddingNumber ? padding : padding.right || 0;
      boundaries.bottom -= isPaddingNumber ? padding : padding.bottom || 0;

      return boundaries;
    }

    function getArea(_ref) {
      var width = _ref.width,
          height = _ref.height;

      return width * height;
    }

    /**
     * Utility used to transform the `auto` placement to the placement with more
     * available space.
     * @method
     * @memberof Popper.Utils
     * @argument {Object} data - The data object generated by update method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The data object, properly modified
     */
    function computeAutoPlacement(placement, refRect, popper, reference, boundariesElement) {
      var padding = arguments.length > 5 && arguments[5] !== undefined ? arguments[5] : 0;

      if (placement.indexOf('auto') === -1) {
        return placement;
      }

      var boundaries = getBoundaries(popper, reference, padding, boundariesElement);

      var rects = {
        top: {
          width: boundaries.width,
          height: refRect.top - boundaries.top
        },
        right: {
          width: boundaries.right - refRect.right,
          height: boundaries.height
        },
        bottom: {
          width: boundaries.width,
          height: boundaries.bottom - refRect.bottom
        },
        left: {
          width: refRect.left - boundaries.left,
          height: boundaries.height
        }
      };

      var sortedAreas = Object.keys(rects).map(function (key) {
        return _extends({
          key: key
        }, rects[key], {
          area: getArea(rects[key])
        });
      }).sort(function (a, b) {
        return b.area - a.area;
      });

      var filteredAreas = sortedAreas.filter(function (_ref2) {
        var width = _ref2.width,
            height = _ref2.height;
        return width >= popper.clientWidth && height >= popper.clientHeight;
      });

      var computedPlacement = filteredAreas.length > 0 ? filteredAreas[0].key : sortedAreas[0].key;

      var variation = placement.split('-')[1];

      return computedPlacement + (variation ? '-' + variation : '');
    }

    /**
     * Get offsets to the reference element
     * @method
     * @memberof Popper.Utils
     * @param {Object} state
     * @param {Element} popper - the popper element
     * @param {Element} reference - the reference element (the popper will be relative to this)
     * @param {Element} fixedPosition - is in fixed position mode
     * @returns {Object} An object containing the offsets which will be applied to the popper
     */
    function getReferenceOffsets(state, popper, reference) {
      var fixedPosition = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : null;

      var commonOffsetParent = fixedPosition ? getFixedPositionOffsetParent(popper) : findCommonOffsetParent(popper, getReferenceNode(reference));
      return getOffsetRectRelativeToArbitraryNode(reference, commonOffsetParent, fixedPosition);
    }

    /**
     * Get the outer sizes of the given element (offset size + margins)
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element
     * @returns {Object} object containing width and height properties
     */
    function getOuterSizes(element) {
      var window = element.ownerDocument.defaultView;
      var styles = window.getComputedStyle(element);
      var x = parseFloat(styles.marginTop || 0) + parseFloat(styles.marginBottom || 0);
      var y = parseFloat(styles.marginLeft || 0) + parseFloat(styles.marginRight || 0);
      var result = {
        width: element.offsetWidth + y,
        height: element.offsetHeight + x
      };
      return result;
    }

    /**
     * Get the opposite placement of the given one
     * @method
     * @memberof Popper.Utils
     * @argument {String} placement
     * @returns {String} flipped placement
     */
    function getOppositePlacement(placement) {
      var hash = { left: 'right', right: 'left', bottom: 'top', top: 'bottom' };
      return placement.replace(/left|right|bottom|top/g, function (matched) {
        return hash[matched];
      });
    }

    /**
     * Get offsets to the popper
     * @method
     * @memberof Popper.Utils
     * @param {Object} position - CSS position the Popper will get applied
     * @param {HTMLElement} popper - the popper element
     * @param {Object} referenceOffsets - the reference offsets (the popper will be relative to this)
     * @param {String} placement - one of the valid placement options
     * @returns {Object} popperOffsets - An object containing the offsets which will be applied to the popper
     */
    function getPopperOffsets(popper, referenceOffsets, placement) {
      placement = placement.split('-')[0];

      // Get popper node sizes
      var popperRect = getOuterSizes(popper);

      // Add position, width and height to our offsets object
      var popperOffsets = {
        width: popperRect.width,
        height: popperRect.height
      };

      // depending by the popper placement we have to compute its offsets slightly differently
      var isHoriz = ['right', 'left'].indexOf(placement) !== -1;
      var mainSide = isHoriz ? 'top' : 'left';
      var secondarySide = isHoriz ? 'left' : 'top';
      var measurement = isHoriz ? 'height' : 'width';
      var secondaryMeasurement = !isHoriz ? 'height' : 'width';

      popperOffsets[mainSide] = referenceOffsets[mainSide] + referenceOffsets[measurement] / 2 - popperRect[measurement] / 2;
      if (placement === secondarySide) {
        popperOffsets[secondarySide] = referenceOffsets[secondarySide] - popperRect[secondaryMeasurement];
      } else {
        popperOffsets[secondarySide] = referenceOffsets[getOppositePlacement(secondarySide)];
      }

      return popperOffsets;
    }

    /**
     * Mimics the `find` method of Array
     * @method
     * @memberof Popper.Utils
     * @argument {Array} arr
     * @argument prop
     * @argument value
     * @returns index or -1
     */
    function find(arr, check) {
      // use native find if supported
      if (Array.prototype.find) {
        return arr.find(check);
      }

      // use `filter` to obtain the same behavior of `find`
      return arr.filter(check)[0];
    }

    /**
     * Return the index of the matching object
     * @method
     * @memberof Popper.Utils
     * @argument {Array} arr
     * @argument prop
     * @argument value
     * @returns index or -1
     */
    function findIndex(arr, prop, value) {
      // use native findIndex if supported
      if (Array.prototype.findIndex) {
        return arr.findIndex(function (cur) {
          return cur[prop] === value;
        });
      }

      // use `find` + `indexOf` if `findIndex` isn't supported
      var match = find(arr, function (obj) {
        return obj[prop] === value;
      });
      return arr.indexOf(match);
    }

    /**
     * Loop trough the list of modifiers and run them in order,
     * each of them will then edit the data object.
     * @method
     * @memberof Popper.Utils
     * @param {dataObject} data
     * @param {Array} modifiers
     * @param {String} ends - Optional modifier name used as stopper
     * @returns {dataObject}
     */
    function runModifiers(modifiers, data, ends) {
      var modifiersToRun = ends === undefined ? modifiers : modifiers.slice(0, findIndex(modifiers, 'name', ends));

      modifiersToRun.forEach(function (modifier) {
        if (modifier['function']) {
          // eslint-disable-line dot-notation
          console.warn('`modifier.function` is deprecated, use `modifier.fn`!');
        }
        var fn = modifier['function'] || modifier.fn; // eslint-disable-line dot-notation
        if (modifier.enabled && isFunction(fn)) {
          // Add properties to offsets to make them a complete clientRect object
          // we do this before each modifier to make sure the previous one doesn't
          // mess with these values
          data.offsets.popper = getClientRect(data.offsets.popper);
          data.offsets.reference = getClientRect(data.offsets.reference);

          data = fn(data, modifier);
        }
      });

      return data;
    }

    /**
     * Updates the position of the popper, computing the new offsets and applying
     * the new style.<br />
     * Prefer `scheduleUpdate` over `update` because of performance reasons.
     * @method
     * @memberof Popper
     */
    function update() {
      // if popper is destroyed, don't perform any further update
      if (this.state.isDestroyed) {
        return;
      }

      var data = {
        instance: this,
        styles: {},
        arrowStyles: {},
        attributes: {},
        flipped: false,
        offsets: {}
      };

      // compute reference element offsets
      data.offsets.reference = getReferenceOffsets(this.state, this.popper, this.reference, this.options.positionFixed);

      // compute auto placement, store placement inside the data object,
      // modifiers will be able to edit `placement` if needed
      // and refer to originalPlacement to know the original value
      data.placement = computeAutoPlacement(this.options.placement, data.offsets.reference, this.popper, this.reference, this.options.modifiers.flip.boundariesElement, this.options.modifiers.flip.padding);

      // store the computed placement inside `originalPlacement`
      data.originalPlacement = data.placement;

      data.positionFixed = this.options.positionFixed;

      // compute the popper offsets
      data.offsets.popper = getPopperOffsets(this.popper, data.offsets.reference, data.placement);

      data.offsets.popper.position = this.options.positionFixed ? 'fixed' : 'absolute';

      // run the modifiers
      data = runModifiers(this.modifiers, data);

      // the first `update` will call `onCreate` callback
      // the other ones will call `onUpdate` callback
      if (!this.state.isCreated) {
        this.state.isCreated = true;
        this.options.onCreate(data);
      } else {
        this.options.onUpdate(data);
      }
    }

    /**
     * Helper used to know if the given modifier is enabled.
     * @method
     * @memberof Popper.Utils
     * @returns {Boolean}
     */
    function isModifierEnabled(modifiers, modifierName) {
      return modifiers.some(function (_ref) {
        var name = _ref.name,
            enabled = _ref.enabled;
        return enabled && name === modifierName;
      });
    }

    /**
     * Get the prefixed supported property name
     * @method
     * @memberof Popper.Utils
     * @argument {String} property (camelCase)
     * @returns {String} prefixed property (camelCase or PascalCase, depending on the vendor prefix)
     */
    function getSupportedPropertyName(property) {
      var prefixes = [false, 'ms', 'Webkit', 'Moz', 'O'];
      var upperProp = property.charAt(0).toUpperCase() + property.slice(1);

      for (var i = 0; i < prefixes.length; i++) {
        var prefix = prefixes[i];
        var toCheck = prefix ? '' + prefix + upperProp : property;
        if (typeof document.body.style[toCheck] !== 'undefined') {
          return toCheck;
        }
      }
      return null;
    }

    /**
     * Destroys the popper.
     * @method
     * @memberof Popper
     */
    function destroy() {
      this.state.isDestroyed = true;

      // touch DOM only if `applyStyle` modifier is enabled
      if (isModifierEnabled(this.modifiers, 'applyStyle')) {
        this.popper.removeAttribute('x-placement');
        this.popper.style.position = '';
        this.popper.style.top = '';
        this.popper.style.left = '';
        this.popper.style.right = '';
        this.popper.style.bottom = '';
        this.popper.style.willChange = '';
        this.popper.style[getSupportedPropertyName('transform')] = '';
      }

      this.disableEventListeners();

      // remove the popper if user explicitly asked for the deletion on destroy
      // do not use `remove` because IE11 doesn't support it
      if (this.options.removeOnDestroy) {
        this.popper.parentNode.removeChild(this.popper);
      }
      return this;
    }

    /**
     * Get the window associated with the element
     * @argument {Element} element
     * @returns {Window}
     */
    function getWindow(element) {
      var ownerDocument = element.ownerDocument;
      return ownerDocument ? ownerDocument.defaultView : window;
    }

    function attachToScrollParents(scrollParent, event, callback, scrollParents) {
      var isBody = scrollParent.nodeName === 'BODY';
      var target = isBody ? scrollParent.ownerDocument.defaultView : scrollParent;
      target.addEventListener(event, callback, { passive: true });

      if (!isBody) {
        attachToScrollParents(getScrollParent(target.parentNode), event, callback, scrollParents);
      }
      scrollParents.push(target);
    }

    /**
     * Setup needed event listeners used to update the popper position
     * @method
     * @memberof Popper.Utils
     * @private
     */
    function setupEventListeners(reference, options, state, updateBound) {
      // Resize event listener on window
      state.updateBound = updateBound;
      getWindow(reference).addEventListener('resize', state.updateBound, { passive: true });

      // Scroll event listener on scroll parents
      var scrollElement = getScrollParent(reference);
      attachToScrollParents(scrollElement, 'scroll', state.updateBound, state.scrollParents);
      state.scrollElement = scrollElement;
      state.eventsEnabled = true;

      return state;
    }

    /**
     * It will add resize/scroll events and start recalculating
     * position of the popper element when they are triggered.
     * @method
     * @memberof Popper
     */
    function enableEventListeners() {
      if (!this.state.eventsEnabled) {
        this.state = setupEventListeners(this.reference, this.options, this.state, this.scheduleUpdate);
      }
    }

    /**
     * Remove event listeners used to update the popper position
     * @method
     * @memberof Popper.Utils
     * @private
     */
    function removeEventListeners(reference, state) {
      // Remove resize event listener on window
      getWindow(reference).removeEventListener('resize', state.updateBound);

      // Remove scroll event listener on scroll parents
      state.scrollParents.forEach(function (target) {
        target.removeEventListener('scroll', state.updateBound);
      });

      // Reset state
      state.updateBound = null;
      state.scrollParents = [];
      state.scrollElement = null;
      state.eventsEnabled = false;
      return state;
    }

    /**
     * It will remove resize/scroll events and won't recalculate popper position
     * when they are triggered. It also won't trigger `onUpdate` callback anymore,
     * unless you call `update` method manually.
     * @method
     * @memberof Popper
     */
    function disableEventListeners() {
      if (this.state.eventsEnabled) {
        cancelAnimationFrame(this.scheduleUpdate);
        this.state = removeEventListeners(this.reference, this.state);
      }
    }

    /**
     * Tells if a given input is a number
     * @method
     * @memberof Popper.Utils
     * @param {*} input to check
     * @return {Boolean}
     */
    function isNumeric(n) {
      return n !== '' && !isNaN(parseFloat(n)) && isFinite(n);
    }

    /**
     * Set the style to the given popper
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element - Element to apply the style to
     * @argument {Object} styles
     * Object with a list of properties and values which will be applied to the element
     */
    function setStyles(element, styles) {
      Object.keys(styles).forEach(function (prop) {
        var unit = '';
        // add unit if the value is numeric and is one of the following
        if (['width', 'height', 'top', 'right', 'bottom', 'left'].indexOf(prop) !== -1 && isNumeric(styles[prop])) {
          unit = 'px';
        }
        element.style[prop] = styles[prop] + unit;
      });
    }

    /**
     * Set the attributes to the given popper
     * @method
     * @memberof Popper.Utils
     * @argument {Element} element - Element to apply the attributes to
     * @argument {Object} styles
     * Object with a list of properties and values which will be applied to the element
     */
    function setAttributes(element, attributes) {
      Object.keys(attributes).forEach(function (prop) {
        var value = attributes[prop];
        if (value !== false) {
          element.setAttribute(prop, attributes[prop]);
        } else {
          element.removeAttribute(prop);
        }
      });
    }

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by `update` method
     * @argument {Object} data.styles - List of style properties - values to apply to popper element
     * @argument {Object} data.attributes - List of attribute properties - values to apply to popper element
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The same data object
     */
    function applyStyle(data) {
      // any property present in `data.styles` will be applied to the popper,
      // in this way we can make the 3rd party modifiers add custom styles to it
      // Be aware, modifiers could override the properties defined in the previous
      // lines of this modifier!
      setStyles(data.instance.popper, data.styles);

      // any property present in `data.attributes` will be applied to the popper,
      // they will be set as HTML attributes of the element
      setAttributes(data.instance.popper, data.attributes);

      // if arrowElement is defined and arrowStyles has some properties
      if (data.arrowElement && Object.keys(data.arrowStyles).length) {
        setStyles(data.arrowElement, data.arrowStyles);
      }

      return data;
    }

    /**
     * Set the x-placement attribute before everything else because it could be used
     * to add margins to the popper margins needs to be calculated to get the
     * correct popper offsets.
     * @method
     * @memberof Popper.modifiers
     * @param {HTMLElement} reference - The reference element used to position the popper
     * @param {HTMLElement} popper - The HTML element used as popper
     * @param {Object} options - Popper.js options
     */
    function applyStyleOnLoad(reference, popper, options, modifierOptions, state) {
      // compute reference element offsets
      var referenceOffsets = getReferenceOffsets(state, popper, reference, options.positionFixed);

      // compute auto placement, store placement inside the data object,
      // modifiers will be able to edit `placement` if needed
      // and refer to originalPlacement to know the original value
      var placement = computeAutoPlacement(options.placement, referenceOffsets, popper, reference, options.modifiers.flip.boundariesElement, options.modifiers.flip.padding);

      popper.setAttribute('x-placement', placement);

      // Apply `position` to popper before anything else because
      // without the position applied we can't guarantee correct computations
      setStyles(popper, { position: options.positionFixed ? 'fixed' : 'absolute' });

      return options;
    }

    /**
     * @function
     * @memberof Popper.Utils
     * @argument {Object} data - The data object generated by `update` method
     * @argument {Boolean} shouldRound - If the offsets should be rounded at all
     * @returns {Object} The popper's position offsets rounded
     *
     * The tale of pixel-perfect positioning. It's still not 100% perfect, but as
     * good as it can be within reason.
     * Discussion here: https://github.com/FezVrasta/popper.js/pull/715
     *
     * Low DPI screens cause a popper to be blurry if not using full pixels (Safari
     * as well on High DPI screens).
     *
     * Firefox prefers no rounding for positioning and does not have blurriness on
     * high DPI screens.
     *
     * Only horizontal placement and left/right values need to be considered.
     */
    function getRoundedOffsets(data, shouldRound) {
      var _data$offsets = data.offsets,
          popper = _data$offsets.popper,
          reference = _data$offsets.reference;
      var round = Math.round,
          floor = Math.floor;

      var noRound = function noRound(v) {
        return v;
      };

      var referenceWidth = round(reference.width);
      var popperWidth = round(popper.width);

      var isVertical = ['left', 'right'].indexOf(data.placement) !== -1;
      var isVariation = data.placement.indexOf('-') !== -1;
      var sameWidthParity = referenceWidth % 2 === popperWidth % 2;
      var bothOddWidth = referenceWidth % 2 === 1 && popperWidth % 2 === 1;

      var horizontalToInteger = !shouldRound ? noRound : isVertical || isVariation || sameWidthParity ? round : floor;
      var verticalToInteger = !shouldRound ? noRound : round;

      return {
        left: horizontalToInteger(bothOddWidth && !isVariation && shouldRound ? popper.left - 1 : popper.left),
        top: verticalToInteger(popper.top),
        bottom: verticalToInteger(popper.bottom),
        right: horizontalToInteger(popper.right)
      };
    }

    var isFirefox = isBrowser && /Firefox/i.test(navigator.userAgent);

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by `update` method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The data object, properly modified
     */
    function computeStyle(data, options) {
      var x = options.x,
          y = options.y;
      var popper = data.offsets.popper;

      // Remove this legacy support in Popper.js v2

      var legacyGpuAccelerationOption = find(data.instance.modifiers, function (modifier) {
        return modifier.name === 'applyStyle';
      }).gpuAcceleration;
      if (legacyGpuAccelerationOption !== undefined) {
        console.warn('WARNING: `gpuAcceleration` option moved to `computeStyle` modifier and will not be supported in future versions of Popper.js!');
      }
      var gpuAcceleration = legacyGpuAccelerationOption !== undefined ? legacyGpuAccelerationOption : options.gpuAcceleration;

      var offsetParent = getOffsetParent(data.instance.popper);
      var offsetParentRect = getBoundingClientRect(offsetParent);

      // Styles
      var styles = {
        position: popper.position
      };

      var offsets = getRoundedOffsets(data, window.devicePixelRatio < 2 || !isFirefox);

      var sideA = x === 'bottom' ? 'top' : 'bottom';
      var sideB = y === 'right' ? 'left' : 'right';

      // if gpuAcceleration is set to `true` and transform is supported,
      //  we use `translate3d` to apply the position to the popper we
      // automatically use the supported prefixed version if needed
      var prefixedProperty = getSupportedPropertyName('transform');

      // now, let's make a step back and look at this code closely (wtf?)
      // If the content of the popper grows once it's been positioned, it
      // may happen that the popper gets misplaced because of the new content
      // overflowing its reference element
      // To avoid this problem, we provide two options (x and y), which allow
      // the consumer to define the offset origin.
      // If we position a popper on top of a reference element, we can set
      // `x` to `top` to make the popper grow towards its top instead of
      // its bottom.
      var left = void 0,
          top = void 0;
      if (sideA === 'bottom') {
        // when offsetParent is <html> the positioning is relative to the bottom of the screen (excluding the scrollbar)
        // and not the bottom of the html element
        if (offsetParent.nodeName === 'HTML') {
          top = -offsetParent.clientHeight + offsets.bottom;
        } else {
          top = -offsetParentRect.height + offsets.bottom;
        }
      } else {
        top = offsets.top;
      }
      if (sideB === 'right') {
        if (offsetParent.nodeName === 'HTML') {
          left = -offsetParent.clientWidth + offsets.right;
        } else {
          left = -offsetParentRect.width + offsets.right;
        }
      } else {
        left = offsets.left;
      }
      if (gpuAcceleration && prefixedProperty) {
        styles[prefixedProperty] = 'translate3d(' + left + 'px, ' + top + 'px, 0)';
        styles[sideA] = 0;
        styles[sideB] = 0;
        styles.willChange = 'transform';
      } else {
        // othwerise, we use the standard `top`, `left`, `bottom` and `right` properties
        var invertTop = sideA === 'bottom' ? -1 : 1;
        var invertLeft = sideB === 'right' ? -1 : 1;
        styles[sideA] = top * invertTop;
        styles[sideB] = left * invertLeft;
        styles.willChange = sideA + ', ' + sideB;
      }

      // Attributes
      var attributes = {
        'x-placement': data.placement
      };

      // Update `data` attributes, styles and arrowStyles
      data.attributes = _extends({}, attributes, data.attributes);
      data.styles = _extends({}, styles, data.styles);
      data.arrowStyles = _extends({}, data.offsets.arrow, data.arrowStyles);

      return data;
    }

    /**
     * Helper used to know if the given modifier depends from another one.<br />
     * It checks if the needed modifier is listed and enabled.
     * @method
     * @memberof Popper.Utils
     * @param {Array} modifiers - list of modifiers
     * @param {String} requestingName - name of requesting modifier
     * @param {String} requestedName - name of requested modifier
     * @returns {Boolean}
     */
    function isModifierRequired(modifiers, requestingName, requestedName) {
      var requesting = find(modifiers, function (_ref) {
        var name = _ref.name;
        return name === requestingName;
      });

      var isRequired = !!requesting && modifiers.some(function (modifier) {
        return modifier.name === requestedName && modifier.enabled && modifier.order < requesting.order;
      });

      if (!isRequired) {
        var _requesting = '`' + requestingName + '`';
        var requested = '`' + requestedName + '`';
        console.warn(requested + ' modifier is required by ' + _requesting + ' modifier in order to work, be sure to include it before ' + _requesting + '!');
      }
      return isRequired;
    }

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by update method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The data object, properly modified
     */
    function arrow(data, options) {
      var _data$offsets$arrow;

      // arrow depends on keepTogether in order to work
      if (!isModifierRequired(data.instance.modifiers, 'arrow', 'keepTogether')) {
        return data;
      }

      var arrowElement = options.element;

      // if arrowElement is a string, suppose it's a CSS selector
      if (typeof arrowElement === 'string') {
        arrowElement = data.instance.popper.querySelector(arrowElement);

        // if arrowElement is not found, don't run the modifier
        if (!arrowElement) {
          return data;
        }
      } else {
        // if the arrowElement isn't a query selector we must check that the
        // provided DOM node is child of its popper node
        if (!data.instance.popper.contains(arrowElement)) {
          console.warn('WARNING: `arrow.element` must be child of its popper element!');
          return data;
        }
      }

      var placement = data.placement.split('-')[0];
      var _data$offsets = data.offsets,
          popper = _data$offsets.popper,
          reference = _data$offsets.reference;

      var isVertical = ['left', 'right'].indexOf(placement) !== -1;

      var len = isVertical ? 'height' : 'width';
      var sideCapitalized = isVertical ? 'Top' : 'Left';
      var side = sideCapitalized.toLowerCase();
      var altSide = isVertical ? 'left' : 'top';
      var opSide = isVertical ? 'bottom' : 'right';
      var arrowElementSize = getOuterSizes(arrowElement)[len];

      //
      // extends keepTogether behavior making sure the popper and its
      // reference have enough pixels in conjunction
      //

      // top/left side
      if (reference[opSide] - arrowElementSize < popper[side]) {
        data.offsets.popper[side] -= popper[side] - (reference[opSide] - arrowElementSize);
      }
      // bottom/right side
      if (reference[side] + arrowElementSize > popper[opSide]) {
        data.offsets.popper[side] += reference[side] + arrowElementSize - popper[opSide];
      }
      data.offsets.popper = getClientRect(data.offsets.popper);

      // compute center of the popper
      var center = reference[side] + reference[len] / 2 - arrowElementSize / 2;

      // Compute the sideValue using the updated popper offsets
      // take popper margin in account because we don't have this info available
      var css = getStyleComputedProperty(data.instance.popper);
      var popperMarginSide = parseFloat(css['margin' + sideCapitalized]);
      var popperBorderSide = parseFloat(css['border' + sideCapitalized + 'Width']);
      var sideValue = center - data.offsets.popper[side] - popperMarginSide - popperBorderSide;

      // prevent arrowElement from being placed not contiguously to its popper
      sideValue = Math.max(Math.min(popper[len] - arrowElementSize, sideValue), 0);

      data.arrowElement = arrowElement;
      data.offsets.arrow = (_data$offsets$arrow = {}, defineProperty(_data$offsets$arrow, side, Math.round(sideValue)), defineProperty(_data$offsets$arrow, altSide, ''), _data$offsets$arrow);

      return data;
    }

    /**
     * Get the opposite placement variation of the given one
     * @method
     * @memberof Popper.Utils
     * @argument {String} placement variation
     * @returns {String} flipped placement variation
     */
    function getOppositeVariation(variation) {
      if (variation === 'end') {
        return 'start';
      } else if (variation === 'start') {
        return 'end';
      }
      return variation;
    }

    /**
     * List of accepted placements to use as values of the `placement` option.<br />
     * Valid placements are:
     * - `auto`
     * - `top`
     * - `right`
     * - `bottom`
     * - `left`
     *
     * Each placement can have a variation from this list:
     * - `-start`
     * - `-end`
     *
     * Variations are interpreted easily if you think of them as the left to right
     * written languages. Horizontally (`top` and `bottom`), `start` is left and `end`
     * is right.<br />
     * Vertically (`left` and `right`), `start` is top and `end` is bottom.
     *
     * Some valid examples are:
     * - `top-end` (on top of reference, right aligned)
     * - `right-start` (on right of reference, top aligned)
     * - `bottom` (on bottom, centered)
     * - `auto-end` (on the side with more space available, alignment depends by placement)
     *
     * @static
     * @type {Array}
     * @enum {String}
     * @readonly
     * @method placements
     * @memberof Popper
     */
    var placements = ['auto-start', 'auto', 'auto-end', 'top-start', 'top', 'top-end', 'right-start', 'right', 'right-end', 'bottom-end', 'bottom', 'bottom-start', 'left-end', 'left', 'left-start'];

    // Get rid of `auto` `auto-start` and `auto-end`
    var validPlacements = placements.slice(3);

    /**
     * Given an initial placement, returns all the subsequent placements
     * clockwise (or counter-clockwise).
     *
     * @method
     * @memberof Popper.Utils
     * @argument {String} placement - A valid placement (it accepts variations)
     * @argument {Boolean} counter - Set to true to walk the placements counterclockwise
     * @returns {Array} placements including their variations
     */
    function clockwise(placement) {
      var counter = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;

      var index = validPlacements.indexOf(placement);
      var arr = validPlacements.slice(index + 1).concat(validPlacements.slice(0, index));
      return counter ? arr.reverse() : arr;
    }

    var BEHAVIORS = {
      FLIP: 'flip',
      CLOCKWISE: 'clockwise',
      COUNTERCLOCKWISE: 'counterclockwise'
    };

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by update method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The data object, properly modified
     */
    function flip(data, options) {
      // if `inner` modifier is enabled, we can't use the `flip` modifier
      if (isModifierEnabled(data.instance.modifiers, 'inner')) {
        return data;
      }

      if (data.flipped && data.placement === data.originalPlacement) {
        // seems like flip is trying to loop, probably there's not enough space on any of the flippable sides
        return data;
      }

      var boundaries = getBoundaries(data.instance.popper, data.instance.reference, options.padding, options.boundariesElement, data.positionFixed);

      var placement = data.placement.split('-')[0];
      var placementOpposite = getOppositePlacement(placement);
      var variation = data.placement.split('-')[1] || '';

      var flipOrder = [];

      switch (options.behavior) {
        case BEHAVIORS.FLIP:
          flipOrder = [placement, placementOpposite];
          break;
        case BEHAVIORS.CLOCKWISE:
          flipOrder = clockwise(placement);
          break;
        case BEHAVIORS.COUNTERCLOCKWISE:
          flipOrder = clockwise(placement, true);
          break;
        default:
          flipOrder = options.behavior;
      }

      flipOrder.forEach(function (step, index) {
        if (placement !== step || flipOrder.length === index + 1) {
          return data;
        }

        placement = data.placement.split('-')[0];
        placementOpposite = getOppositePlacement(placement);

        var popperOffsets = data.offsets.popper;
        var refOffsets = data.offsets.reference;

        // using floor because the reference offsets may contain decimals we are not going to consider here
        var floor = Math.floor;
        var overlapsRef = placement === 'left' && floor(popperOffsets.right) > floor(refOffsets.left) || placement === 'right' && floor(popperOffsets.left) < floor(refOffsets.right) || placement === 'top' && floor(popperOffsets.bottom) > floor(refOffsets.top) || placement === 'bottom' && floor(popperOffsets.top) < floor(refOffsets.bottom);

        var overflowsLeft = floor(popperOffsets.left) < floor(boundaries.left);
        var overflowsRight = floor(popperOffsets.right) > floor(boundaries.right);
        var overflowsTop = floor(popperOffsets.top) < floor(boundaries.top);
        var overflowsBottom = floor(popperOffsets.bottom) > floor(boundaries.bottom);

        var overflowsBoundaries = placement === 'left' && overflowsLeft || placement === 'right' && overflowsRight || placement === 'top' && overflowsTop || placement === 'bottom' && overflowsBottom;

        // flip the variation if required
        var isVertical = ['top', 'bottom'].indexOf(placement) !== -1;

        // flips variation if reference element overflows boundaries
        var flippedVariationByRef = !!options.flipVariations && (isVertical && variation === 'start' && overflowsLeft || isVertical && variation === 'end' && overflowsRight || !isVertical && variation === 'start' && overflowsTop || !isVertical && variation === 'end' && overflowsBottom);

        // flips variation if popper content overflows boundaries
        var flippedVariationByContent = !!options.flipVariationsByContent && (isVertical && variation === 'start' && overflowsRight || isVertical && variation === 'end' && overflowsLeft || !isVertical && variation === 'start' && overflowsBottom || !isVertical && variation === 'end' && overflowsTop);

        var flippedVariation = flippedVariationByRef || flippedVariationByContent;

        if (overlapsRef || overflowsBoundaries || flippedVariation) {
          // this boolean to detect any flip loop
          data.flipped = true;

          if (overlapsRef || overflowsBoundaries) {
            placement = flipOrder[index + 1];
          }

          if (flippedVariation) {
            variation = getOppositeVariation(variation);
          }

          data.placement = placement + (variation ? '-' + variation : '');

          // this object contains `position`, we want to preserve it along with
          // any additional property we may add in the future
          data.offsets.popper = _extends({}, data.offsets.popper, getPopperOffsets(data.instance.popper, data.offsets.reference, data.placement));

          data = runModifiers(data.instance.modifiers, data, 'flip');
        }
      });
      return data;
    }

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by update method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The data object, properly modified
     */
    function keepTogether(data) {
      var _data$offsets = data.offsets,
          popper = _data$offsets.popper,
          reference = _data$offsets.reference;

      var placement = data.placement.split('-')[0];
      var floor = Math.floor;
      var isVertical = ['top', 'bottom'].indexOf(placement) !== -1;
      var side = isVertical ? 'right' : 'bottom';
      var opSide = isVertical ? 'left' : 'top';
      var measurement = isVertical ? 'width' : 'height';

      if (popper[side] < floor(reference[opSide])) {
        data.offsets.popper[opSide] = floor(reference[opSide]) - popper[measurement];
      }
      if (popper[opSide] > floor(reference[side])) {
        data.offsets.popper[opSide] = floor(reference[side]);
      }

      return data;
    }

    /**
     * Converts a string containing value + unit into a px value number
     * @function
     * @memberof {modifiers~offset}
     * @private
     * @argument {String} str - Value + unit string
     * @argument {String} measurement - `height` or `width`
     * @argument {Object} popperOffsets
     * @argument {Object} referenceOffsets
     * @returns {Number|String}
     * Value in pixels, or original string if no values were extracted
     */
    function toValue(str, measurement, popperOffsets, referenceOffsets) {
      // separate value from unit
      var split = str.match(/((?:\-|\+)?\d*\.?\d*)(.*)/);
      var value = +split[1];
      var unit = split[2];

      // If it's not a number it's an operator, I guess
      if (!value) {
        return str;
      }

      if (unit.indexOf('%') === 0) {
        var element = void 0;
        switch (unit) {
          case '%p':
            element = popperOffsets;
            break;
          case '%':
          case '%r':
          default:
            element = referenceOffsets;
        }

        var rect = getClientRect(element);
        return rect[measurement] / 100 * value;
      } else if (unit === 'vh' || unit === 'vw') {
        // if is a vh or vw, we calculate the size based on the viewport
        var size = void 0;
        if (unit === 'vh') {
          size = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
        } else {
          size = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
        }
        return size / 100 * value;
      } else {
        // if is an explicit pixel unit, we get rid of the unit and keep the value
        // if is an implicit unit, it's px, and we return just the value
        return value;
      }
    }

    /**
     * Parse an `offset` string to extrapolate `x` and `y` numeric offsets.
     * @function
     * @memberof {modifiers~offset}
     * @private
     * @argument {String} offset
     * @argument {Object} popperOffsets
     * @argument {Object} referenceOffsets
     * @argument {String} basePlacement
     * @returns {Array} a two cells array with x and y offsets in numbers
     */
    function parseOffset(offset, popperOffsets, referenceOffsets, basePlacement) {
      var offsets = [0, 0];

      // Use height if placement is left or right and index is 0 otherwise use width
      // in this way the first offset will use an axis and the second one
      // will use the other one
      var useHeight = ['right', 'left'].indexOf(basePlacement) !== -1;

      // Split the offset string to obtain a list of values and operands
      // The regex addresses values with the plus or minus sign in front (+10, -20, etc)
      var fragments = offset.split(/(\+|\-)/).map(function (frag) {
        return frag.trim();
      });

      // Detect if the offset string contains a pair of values or a single one
      // they could be separated by comma or space
      var divider = fragments.indexOf(find(fragments, function (frag) {
        return frag.search(/,|\s/) !== -1;
      }));

      if (fragments[divider] && fragments[divider].indexOf(',') === -1) {
        console.warn('Offsets separated by white space(s) are deprecated, use a comma (,) instead.');
      }

      // If divider is found, we divide the list of values and operands to divide
      // them by ofset X and Y.
      var splitRegex = /\s*,\s*|\s+/;
      var ops = divider !== -1 ? [fragments.slice(0, divider).concat([fragments[divider].split(splitRegex)[0]]), [fragments[divider].split(splitRegex)[1]].concat(fragments.slice(divider + 1))] : [fragments];

      // Convert the values with units to absolute pixels to allow our computations
      ops = ops.map(function (op, index) {
        // Most of the units rely on the orientation of the popper
        var measurement = (index === 1 ? !useHeight : useHeight) ? 'height' : 'width';
        var mergeWithPrevious = false;
        return op
        // This aggregates any `+` or `-` sign that aren't considered operators
        // e.g.: 10 + +5 => [10, +, +5]
        .reduce(function (a, b) {
          if (a[a.length - 1] === '' && ['+', '-'].indexOf(b) !== -1) {
            a[a.length - 1] = b;
            mergeWithPrevious = true;
            return a;
          } else if (mergeWithPrevious) {
            a[a.length - 1] += b;
            mergeWithPrevious = false;
            return a;
          } else {
            return a.concat(b);
          }
        }, [])
        // Here we convert the string values into number values (in px)
        .map(function (str) {
          return toValue(str, measurement, popperOffsets, referenceOffsets);
        });
      });

      // Loop trough the offsets arrays and execute the operations
      ops.forEach(function (op, index) {
        op.forEach(function (frag, index2) {
          if (isNumeric(frag)) {
            offsets[index] += frag * (op[index2 - 1] === '-' ? -1 : 1);
          }
        });
      });
      return offsets;
    }

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by update method
     * @argument {Object} options - Modifiers configuration and options
     * @argument {Number|String} options.offset=0
     * The offset value as described in the modifier description
     * @returns {Object} The data object, properly modified
     */
    function offset(data, _ref) {
      var offset = _ref.offset;
      var placement = data.placement,
          _data$offsets = data.offsets,
          popper = _data$offsets.popper,
          reference = _data$offsets.reference;

      var basePlacement = placement.split('-')[0];

      var offsets = void 0;
      if (isNumeric(+offset)) {
        offsets = [+offset, 0];
      } else {
        offsets = parseOffset(offset, popper, reference, basePlacement);
      }

      if (basePlacement === 'left') {
        popper.top += offsets[0];
        popper.left -= offsets[1];
      } else if (basePlacement === 'right') {
        popper.top += offsets[0];
        popper.left += offsets[1];
      } else if (basePlacement === 'top') {
        popper.left += offsets[0];
        popper.top -= offsets[1];
      } else if (basePlacement === 'bottom') {
        popper.left += offsets[0];
        popper.top += offsets[1];
      }

      data.popper = popper;
      return data;
    }

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by `update` method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The data object, properly modified
     */
    function preventOverflow(data, options) {
      var boundariesElement = options.boundariesElement || getOffsetParent(data.instance.popper);

      // If offsetParent is the reference element, we really want to
      // go one step up and use the next offsetParent as reference to
      // avoid to make this modifier completely useless and look like broken
      if (data.instance.reference === boundariesElement) {
        boundariesElement = getOffsetParent(boundariesElement);
      }

      // NOTE: DOM access here
      // resets the popper's position so that the document size can be calculated excluding
      // the size of the popper element itself
      var transformProp = getSupportedPropertyName('transform');
      var popperStyles = data.instance.popper.style; // assignment to help minification
      var top = popperStyles.top,
          left = popperStyles.left,
          transform = popperStyles[transformProp];

      popperStyles.top = '';
      popperStyles.left = '';
      popperStyles[transformProp] = '';

      var boundaries = getBoundaries(data.instance.popper, data.instance.reference, options.padding, boundariesElement, data.positionFixed);

      // NOTE: DOM access here
      // restores the original style properties after the offsets have been computed
      popperStyles.top = top;
      popperStyles.left = left;
      popperStyles[transformProp] = transform;

      options.boundaries = boundaries;

      var order = options.priority;
      var popper = data.offsets.popper;

      var check = {
        primary: function primary(placement) {
          var value = popper[placement];
          if (popper[placement] < boundaries[placement] && !options.escapeWithReference) {
            value = Math.max(popper[placement], boundaries[placement]);
          }
          return defineProperty({}, placement, value);
        },
        secondary: function secondary(placement) {
          var mainSide = placement === 'right' ? 'left' : 'top';
          var value = popper[mainSide];
          if (popper[placement] > boundaries[placement] && !options.escapeWithReference) {
            value = Math.min(popper[mainSide], boundaries[placement] - (placement === 'right' ? popper.width : popper.height));
          }
          return defineProperty({}, mainSide, value);
        }
      };

      order.forEach(function (placement) {
        var side = ['left', 'top'].indexOf(placement) !== -1 ? 'primary' : 'secondary';
        popper = _extends({}, popper, check[side](placement));
      });

      data.offsets.popper = popper;

      return data;
    }

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by `update` method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The data object, properly modified
     */
    function shift(data) {
      var placement = data.placement;
      var basePlacement = placement.split('-')[0];
      var shiftvariation = placement.split('-')[1];

      // if shift shiftvariation is specified, run the modifier
      if (shiftvariation) {
        var _data$offsets = data.offsets,
            reference = _data$offsets.reference,
            popper = _data$offsets.popper;

        var isVertical = ['bottom', 'top'].indexOf(basePlacement) !== -1;
        var side = isVertical ? 'left' : 'top';
        var measurement = isVertical ? 'width' : 'height';

        var shiftOffsets = {
          start: defineProperty({}, side, reference[side]),
          end: defineProperty({}, side, reference[side] + reference[measurement] - popper[measurement])
        };

        data.offsets.popper = _extends({}, popper, shiftOffsets[shiftvariation]);
      }

      return data;
    }

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by update method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The data object, properly modified
     */
    function hide(data) {
      if (!isModifierRequired(data.instance.modifiers, 'hide', 'preventOverflow')) {
        return data;
      }

      var refRect = data.offsets.reference;
      var bound = find(data.instance.modifiers, function (modifier) {
        return modifier.name === 'preventOverflow';
      }).boundaries;

      if (refRect.bottom < bound.top || refRect.left > bound.right || refRect.top > bound.bottom || refRect.right < bound.left) {
        // Avoid unnecessary DOM access if visibility hasn't changed
        if (data.hide === true) {
          return data;
        }

        data.hide = true;
        data.attributes['x-out-of-boundaries'] = '';
      } else {
        // Avoid unnecessary DOM access if visibility hasn't changed
        if (data.hide === false) {
          return data;
        }

        data.hide = false;
        data.attributes['x-out-of-boundaries'] = false;
      }

      return data;
    }

    /**
     * @function
     * @memberof Modifiers
     * @argument {Object} data - The data object generated by `update` method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {Object} The data object, properly modified
     */
    function inner(data) {
      var placement = data.placement;
      var basePlacement = placement.split('-')[0];
      var _data$offsets = data.offsets,
          popper = _data$offsets.popper,
          reference = _data$offsets.reference;

      var isHoriz = ['left', 'right'].indexOf(basePlacement) !== -1;

      var subtractLength = ['top', 'left'].indexOf(basePlacement) === -1;

      popper[isHoriz ? 'left' : 'top'] = reference[basePlacement] - (subtractLength ? popper[isHoriz ? 'width' : 'height'] : 0);

      data.placement = getOppositePlacement(placement);
      data.offsets.popper = getClientRect(popper);

      return data;
    }

    /**
     * Modifier function, each modifier can have a function of this type assigned
     * to its `fn` property.<br />
     * These functions will be called on each update, this means that you must
     * make sure they are performant enough to avoid performance bottlenecks.
     *
     * @function ModifierFn
     * @argument {dataObject} data - The data object generated by `update` method
     * @argument {Object} options - Modifiers configuration and options
     * @returns {dataObject} The data object, properly modified
     */

    /**
     * Modifiers are plugins used to alter the behavior of your poppers.<br />
     * Popper.js uses a set of 9 modifiers to provide all the basic functionalities
     * needed by the library.
     *
     * Usually you don't want to override the `order`, `fn` and `onLoad` props.
     * All the other properties are configurations that could be tweaked.
     * @namespace modifiers
     */
    var modifiers = {
      /**
       * Modifier used to shift the popper on the start or end of its reference
       * element.<br />
       * It will read the variation of the `placement` property.<br />
       * It can be one either `-end` or `-start`.
       * @memberof modifiers
       * @inner
       */
      shift: {
        /** @prop {number} order=100 - Index used to define the order of execution */
        order: 100,
        /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
        enabled: true,
        /** @prop {ModifierFn} */
        fn: shift
      },

      /**
       * The `offset` modifier can shift your popper on both its axis.
       *
       * It accepts the following units:
       * - `px` or unit-less, interpreted as pixels
       * - `%` or `%r`, percentage relative to the length of the reference element
       * - `%p`, percentage relative to the length of the popper element
       * - `vw`, CSS viewport width unit
       * - `vh`, CSS viewport height unit
       *
       * For length is intended the main axis relative to the placement of the popper.<br />
       * This means that if the placement is `top` or `bottom`, the length will be the
       * `width`. In case of `left` or `right`, it will be the `height`.
       *
       * You can provide a single value (as `Number` or `String`), or a pair of values
       * as `String` divided by a comma or one (or more) white spaces.<br />
       * The latter is a deprecated method because it leads to confusion and will be
       * removed in v2.<br />
       * Additionally, it accepts additions and subtractions between different units.
       * Note that multiplications and divisions aren't supported.
       *
       * Valid examples are:
       * ```
       * 10
       * '10%'
       * '10, 10'
       * '10%, 10'
       * '10 + 10%'
       * '10 - 5vh + 3%'
       * '-10px + 5vh, 5px - 6%'
       * ```
       * > **NB**: If you desire to apply offsets to your poppers in a way that may make them overlap
       * > with their reference element, unfortunately, you will have to disable the `flip` modifier.
       * > You can read more on this at this [issue](https://github.com/FezVrasta/popper.js/issues/373).
       *
       * @memberof modifiers
       * @inner
       */
      offset: {
        /** @prop {number} order=200 - Index used to define the order of execution */
        order: 200,
        /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
        enabled: true,
        /** @prop {ModifierFn} */
        fn: offset,
        /** @prop {Number|String} offset=0
         * The offset value as described in the modifier description
         */
        offset: 0
      },

      /**
       * Modifier used to prevent the popper from being positioned outside the boundary.
       *
       * A scenario exists where the reference itself is not within the boundaries.<br />
       * We can say it has "escaped the boundaries" — or just "escaped".<br />
       * In this case we need to decide whether the popper should either:
       *
       * - detach from the reference and remain "trapped" in the boundaries, or
       * - if it should ignore the boundary and "escape with its reference"
       *
       * When `escapeWithReference` is set to`true` and reference is completely
       * outside its boundaries, the popper will overflow (or completely leave)
       * the boundaries in order to remain attached to the edge of the reference.
       *
       * @memberof modifiers
       * @inner
       */
      preventOverflow: {
        /** @prop {number} order=300 - Index used to define the order of execution */
        order: 300,
        /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
        enabled: true,
        /** @prop {ModifierFn} */
        fn: preventOverflow,
        /**
         * @prop {Array} [priority=['left','right','top','bottom']]
         * Popper will try to prevent overflow following these priorities by default,
         * then, it could overflow on the left and on top of the `boundariesElement`
         */
        priority: ['left', 'right', 'top', 'bottom'],
        /**
         * @prop {number} padding=5
         * Amount of pixel used to define a minimum distance between the boundaries
         * and the popper. This makes sure the popper always has a little padding
         * between the edges of its container
         */
        padding: 5,
        /**
         * @prop {String|HTMLElement} boundariesElement='scrollParent'
         * Boundaries used by the modifier. Can be `scrollParent`, `window`,
         * `viewport` or any DOM element.
         */
        boundariesElement: 'scrollParent'
      },

      /**
       * Modifier used to make sure the reference and its popper stay near each other
       * without leaving any gap between the two. Especially useful when the arrow is
       * enabled and you want to ensure that it points to its reference element.
       * It cares only about the first axis. You can still have poppers with margin
       * between the popper and its reference element.
       * @memberof modifiers
       * @inner
       */
      keepTogether: {
        /** @prop {number} order=400 - Index used to define the order of execution */
        order: 400,
        /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
        enabled: true,
        /** @prop {ModifierFn} */
        fn: keepTogether
      },

      /**
       * This modifier is used to move the `arrowElement` of the popper to make
       * sure it is positioned between the reference element and its popper element.
       * It will read the outer size of the `arrowElement` node to detect how many
       * pixels of conjunction are needed.
       *
       * It has no effect if no `arrowElement` is provided.
       * @memberof modifiers
       * @inner
       */
      arrow: {
        /** @prop {number} order=500 - Index used to define the order of execution */
        order: 500,
        /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
        enabled: true,
        /** @prop {ModifierFn} */
        fn: arrow,
        /** @prop {String|HTMLElement} element='[x-arrow]' - Selector or node used as arrow */
        element: '[x-arrow]'
      },

      /**
       * Modifier used to flip the popper's placement when it starts to overlap its
       * reference element.
       *
       * Requires the `preventOverflow` modifier before it in order to work.
       *
       * **NOTE:** this modifier will interrupt the current update cycle and will
       * restart it if it detects the need to flip the placement.
       * @memberof modifiers
       * @inner
       */
      flip: {
        /** @prop {number} order=600 - Index used to define the order of execution */
        order: 600,
        /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
        enabled: true,
        /** @prop {ModifierFn} */
        fn: flip,
        /**
         * @prop {String|Array} behavior='flip'
         * The behavior used to change the popper's placement. It can be one of
         * `flip`, `clockwise`, `counterclockwise` or an array with a list of valid
         * placements (with optional variations)
         */
        behavior: 'flip',
        /**
         * @prop {number} padding=5
         * The popper will flip if it hits the edges of the `boundariesElement`
         */
        padding: 5,
        /**
         * @prop {String|HTMLElement} boundariesElement='viewport'
         * The element which will define the boundaries of the popper position.
         * The popper will never be placed outside of the defined boundaries
         * (except if `keepTogether` is enabled)
         */
        boundariesElement: 'viewport',
        /**
         * @prop {Boolean} flipVariations=false
         * The popper will switch placement variation between `-start` and `-end` when
         * the reference element overlaps its boundaries.
         *
         * The original placement should have a set variation.
         */
        flipVariations: false,
        /**
         * @prop {Boolean} flipVariationsByContent=false
         * The popper will switch placement variation between `-start` and `-end` when
         * the popper element overlaps its reference boundaries.
         *
         * The original placement should have a set variation.
         */
        flipVariationsByContent: false
      },

      /**
       * Modifier used to make the popper flow toward the inner of the reference element.
       * By default, when this modifier is disabled, the popper will be placed outside
       * the reference element.
       * @memberof modifiers
       * @inner
       */
      inner: {
        /** @prop {number} order=700 - Index used to define the order of execution */
        order: 700,
        /** @prop {Boolean} enabled=false - Whether the modifier is enabled or not */
        enabled: false,
        /** @prop {ModifierFn} */
        fn: inner
      },

      /**
       * Modifier used to hide the popper when its reference element is outside of the
       * popper boundaries. It will set a `x-out-of-boundaries` attribute which can
       * be used to hide with a CSS selector the popper when its reference is
       * out of boundaries.
       *
       * Requires the `preventOverflow` modifier before it in order to work.
       * @memberof modifiers
       * @inner
       */
      hide: {
        /** @prop {number} order=800 - Index used to define the order of execution */
        order: 800,
        /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
        enabled: true,
        /** @prop {ModifierFn} */
        fn: hide
      },

      /**
       * Computes the style that will be applied to the popper element to gets
       * properly positioned.
       *
       * Note that this modifier will not touch the DOM, it just prepares the styles
       * so that `applyStyle` modifier can apply it. This separation is useful
       * in case you need to replace `applyStyle` with a custom implementation.
       *
       * This modifier has `850` as `order` value to maintain backward compatibility
       * with previous versions of Popper.js. Expect the modifiers ordering method
       * to change in future major versions of the library.
       *
       * @memberof modifiers
       * @inner
       */
      computeStyle: {
        /** @prop {number} order=850 - Index used to define the order of execution */
        order: 850,
        /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
        enabled: true,
        /** @prop {ModifierFn} */
        fn: computeStyle,
        /**
         * @prop {Boolean} gpuAcceleration=true
         * If true, it uses the CSS 3D transformation to position the popper.
         * Otherwise, it will use the `top` and `left` properties
         */
        gpuAcceleration: true,
        /**
         * @prop {string} [x='bottom']
         * Where to anchor the X axis (`bottom` or `top`). AKA X offset origin.
         * Change this if your popper should grow in a direction different from `bottom`
         */
        x: 'bottom',
        /**
         * @prop {string} [x='left']
         * Where to anchor the Y axis (`left` or `right`). AKA Y offset origin.
         * Change this if your popper should grow in a direction different from `right`
         */
        y: 'right'
      },

      /**
       * Applies the computed styles to the popper element.
       *
       * All the DOM manipulations are limited to this modifier. This is useful in case
       * you want to integrate Popper.js inside a framework or view library and you
       * want to delegate all the DOM manipulations to it.
       *
       * Note that if you disable this modifier, you must make sure the popper element
       * has its position set to `absolute` before Popper.js can do its work!
       *
       * Just disable this modifier and define your own to achieve the desired effect.
       *
       * @memberof modifiers
       * @inner
       */
      applyStyle: {
        /** @prop {number} order=900 - Index used to define the order of execution */
        order: 900,
        /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
        enabled: true,
        /** @prop {ModifierFn} */
        fn: applyStyle,
        /** @prop {Function} */
        onLoad: applyStyleOnLoad,
        /**
         * @deprecated since version 1.10.0, the property moved to `computeStyle` modifier
         * @prop {Boolean} gpuAcceleration=true
         * If true, it uses the CSS 3D transformation to position the popper.
         * Otherwise, it will use the `top` and `left` properties
         */
        gpuAcceleration: undefined
      }
    };

    /**
     * The `dataObject` is an object containing all the information used by Popper.js.
     * This object is passed to modifiers and to the `onCreate` and `onUpdate` callbacks.
     * @name dataObject
     * @property {Object} data.instance The Popper.js instance
     * @property {String} data.placement Placement applied to popper
     * @property {String} data.originalPlacement Placement originally defined on init
     * @property {Boolean} data.flipped True if popper has been flipped by flip modifier
     * @property {Boolean} data.hide True if the reference element is out of boundaries, useful to know when to hide the popper
     * @property {HTMLElement} data.arrowElement Node used as arrow by arrow modifier
     * @property {Object} data.styles Any CSS property defined here will be applied to the popper. It expects the JavaScript nomenclature (eg. `marginBottom`)
     * @property {Object} data.arrowStyles Any CSS property defined here will be applied to the popper arrow. It expects the JavaScript nomenclature (eg. `marginBottom`)
     * @property {Object} data.boundaries Offsets of the popper boundaries
     * @property {Object} data.offsets The measurements of popper, reference and arrow elements
     * @property {Object} data.offsets.popper `top`, `left`, `width`, `height` values
     * @property {Object} data.offsets.reference `top`, `left`, `width`, `height` values
     * @property {Object} data.offsets.arrow] `top` and `left` offsets, only one of them will be different from 0
     */

    /**
     * Default options provided to Popper.js constructor.<br />
     * These can be overridden using the `options` argument of Popper.js.<br />
     * To override an option, simply pass an object with the same
     * structure of the `options` object, as the 3rd argument. For example:
     * ```
     * new Popper(ref, pop, {
     *   modifiers: {
     *     preventOverflow: { enabled: false }
     *   }
     * })
     * ```
     * @type {Object}
     * @static
     * @memberof Popper
     */
    var Defaults = {
      /**
       * Popper's placement.
       * @prop {Popper.placements} placement='bottom'
       */
      placement: 'bottom',

      /**
       * Set this to true if you want popper to position it self in 'fixed' mode
       * @prop {Boolean} positionFixed=false
       */
      positionFixed: false,

      /**
       * Whether events (resize, scroll) are initially enabled.
       * @prop {Boolean} eventsEnabled=true
       */
      eventsEnabled: true,

      /**
       * Set to true if you want to automatically remove the popper when
       * you call the `destroy` method.
       * @prop {Boolean} removeOnDestroy=false
       */
      removeOnDestroy: false,

      /**
       * Callback called when the popper is created.<br />
       * By default, it is set to no-op.<br />
       * Access Popper.js instance with `data.instance`.
       * @prop {onCreate}
       */
      onCreate: function onCreate() {},

      /**
       * Callback called when the popper is updated. This callback is not called
       * on the initialization/creation of the popper, but only on subsequent
       * updates.<br />
       * By default, it is set to no-op.<br />
       * Access Popper.js instance with `data.instance`.
       * @prop {onUpdate}
       */
      onUpdate: function onUpdate() {},

      /**
       * List of modifiers used to modify the offsets before they are applied to the popper.
       * They provide most of the functionalities of Popper.js.
       * @prop {modifiers}
       */
      modifiers: modifiers
    };

    /**
     * @callback onCreate
     * @param {dataObject} data
     */

    /**
     * @callback onUpdate
     * @param {dataObject} data
     */

    // Utils
    // Methods
    var Popper = function () {
      /**
       * Creates a new Popper.js instance.
       * @class Popper
       * @param {Element|referenceObject} reference - The reference element used to position the popper
       * @param {Element} popper - The HTML / XML element used as the popper
       * @param {Object} options - Your custom options to override the ones defined in [Defaults](#defaults)
       * @return {Object} instance - The generated Popper.js instance
       */
      function Popper(reference, popper) {
        var _this = this;

        var options = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
        classCallCheck(this, Popper);

        this.scheduleUpdate = function () {
          return requestAnimationFrame(_this.update);
        };

        // make update() debounced, so that it only runs at most once-per-tick
        this.update = debounce(this.update.bind(this));

        // with {} we create a new object with the options inside it
        this.options = _extends({}, Popper.Defaults, options);

        // init state
        this.state = {
          isDestroyed: false,
          isCreated: false,
          scrollParents: []
        };

        // get reference and popper elements (allow jQuery wrappers)
        this.reference = reference && reference.jquery ? reference[0] : reference;
        this.popper = popper && popper.jquery ? popper[0] : popper;

        // Deep merge modifiers options
        this.options.modifiers = {};
        Object.keys(_extends({}, Popper.Defaults.modifiers, options.modifiers)).forEach(function (name) {
          _this.options.modifiers[name] = _extends({}, Popper.Defaults.modifiers[name] || {}, options.modifiers ? options.modifiers[name] : {});
        });

        // Refactoring modifiers' list (Object => Array)
        this.modifiers = Object.keys(this.options.modifiers).map(function (name) {
          return _extends({
            name: name
          }, _this.options.modifiers[name]);
        })
        // sort the modifiers by order
        .sort(function (a, b) {
          return a.order - b.order;
        });

        // modifiers have the ability to execute arbitrary code when Popper.js get inited
        // such code is executed in the same order of its modifier
        // they could add new properties to their options configuration
        // BE AWARE: don't add options to `options.modifiers.name` but to `modifierOptions`!
        this.modifiers.forEach(function (modifierOptions) {
          if (modifierOptions.enabled && isFunction(modifierOptions.onLoad)) {
            modifierOptions.onLoad(_this.reference, _this.popper, _this.options, modifierOptions, _this.state);
          }
        });

        // fire the first update to position the popper in the right place
        this.update();

        var eventsEnabled = this.options.eventsEnabled;
        if (eventsEnabled) {
          // setup event listeners, they will take care of update the position in specific situations
          this.enableEventListeners();
        }

        this.state.eventsEnabled = eventsEnabled;
      }

      // We can't use class properties because they don't get listed in the
      // class prototype and break stuff like Sinon stubs


      createClass(Popper, [{
        key: 'update',
        value: function update$$1() {
          return update.call(this);
        }
      }, {
        key: 'destroy',
        value: function destroy$$1() {
          return destroy.call(this);
        }
      }, {
        key: 'enableEventListeners',
        value: function enableEventListeners$$1() {
          return enableEventListeners.call(this);
        }
      }, {
        key: 'disableEventListeners',
        value: function disableEventListeners$$1() {
          return disableEventListeners.call(this);
        }

        /**
         * Schedules an update. It will run on the next UI update available.
         * @method scheduleUpdate
         * @memberof Popper
         */


        /**
         * Collection of utilities useful when writing custom modifiers.
         * Starting from version 1.7, this method is available only if you
         * include `popper-utils.js` before `popper.js`.
         *
         * **DEPRECATION**: This way to access PopperUtils is deprecated
         * and will be removed in v2! Use the PopperUtils module directly instead.
         * Due to the high instability of the methods contained in Utils, we can't
         * guarantee them to follow semver. Use them at your own risk!
         * @static
         * @private
         * @type {Object}
         * @deprecated since version 1.8
         * @member Utils
         * @memberof Popper
         */

      }]);
      return Popper;
    }();

    /**
     * The `referenceObject` is an object that provides an interface compatible with Popper.js
     * and lets you use it as replacement of a real DOM node.<br />
     * You can use this method to position a popper relatively to a set of coordinates
     * in case you don't have a DOM node to use as reference.
     *
     * ```
     * new Popper(referenceObject, popperNode);
     * ```
     *
     * NB: This feature isn't supported in Internet Explorer 10.
     * @name referenceObject
     * @property {Function} data.getBoundingClientRect
     * A function that returns a set of coordinates compatible with the native `getBoundingClientRect` method.
     * @property {number} data.clientWidth
     * An ES6 getter that will return the width of the virtual reference element.
     * @property {number} data.clientHeight
     * An ES6 getter that will return the height of the virtual reference element.
     */


    Popper.Utils = (typeof window !== 'undefined' ? window : global).PopperUtils;
    Popper.placements = placements;
    Popper.Defaults = Defaults;

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
    const Event$1 = {
        HIDE: "tobago.dropdown.hide",
        HIDDEN: "tobago.dropdown.hidden",
        SHOW: "tobago.dropdown.show",
        SHOWN: "tobago.dropdown.shown"
    };
    class Dropdown extends HTMLElement {
        constructor() {
            super();
            this.dropdownEntries = [];
            if (!this.classList.contains("tobago-dropdown-submenu")) { // ignore submenus
                const root = this.getRootNode();
                this.createDropdownEntries(this.dropdownMenu, null);
                this.toggleButton.addEventListener("click", this.toggleDropdown.bind(this));
                root.addEventListener("mouseup", this.mouseupOnDocument.bind(this));
                root.addEventListener("keydown", this.keydownOnDocument.bind(this));
            }
        }
        connectedCallback() {
        }
        toggleDropdown(event) {
            event.preventDefault();
            event.stopPropagation();
            if (this.dropdownVisible()) {
                this.closeDropdown();
            }
            else {
                this.openDropdown();
            }
        }
        mouseupOnDocument(event) {
            if (!this.toggleButtonSelected(event) && this.dropdownVisible()
                && !this.dropdownMenu.contains(event.target)) {
                this.closeDropdown();
            }
        }
        keydownOnDocument(event) {
            if (this.toggleButtonSelected(event) && !this.dropdownVisible()
                && (event.code === "ArrowUp" || event.code === "ArrowDown")) {
                event.preventDefault();
                event.stopPropagation();
                this.openDropdown();
                const interval = setInterval(() => {
                    if (this.dropdownVisible()) {
                        if (this.activeDropdownEntry) {
                            this.activeDropdownEntry.focus();
                        }
                        else {
                            this.dropdownEntries[0].focus();
                        }
                        clearInterval(interval);
                    }
                }, 0);
            }
            else if (this.dropdownVisible()
                && (event.code === "ArrowUp" || event.code === "ArrowDown"
                    || event.code === "ArrowLeft" || event.code === "ArrowRight"
                    || event.code === "Tab")) {
                event.preventDefault();
                event.stopPropagation();
                if (!this.activeDropdownEntry) {
                    this.dropdownEntries[0].focus();
                }
                else if (event.code === "ArrowUp" && this.activeDropdownEntry.previous) {
                    this.activeDropdownEntry.previous.focus();
                }
                else if (event.code === "ArrowDown" && this.activeDropdownEntry.next) {
                    this.activeDropdownEntry.next.focus();
                }
                else if (event.code === "ArrowRight" && this.activeDropdownEntry.children.length > 0) {
                    this.activeDropdownEntry.children[0].focus();
                }
                else if (event.code === "ArrowLeft" && this.activeDropdownEntry.parent) {
                    this.activeDropdownEntry.parent.focus();
                }
                else if (!event.shiftKey && event.code === "Tab") {
                    if (this.activeDropdownEntry.children.length > 0) {
                        this.activeDropdownEntry.children[0].focus();
                    }
                    else if (this.activeDropdownEntry.next) {
                        this.activeDropdownEntry.next.focus();
                    }
                    else {
                        let parent = this.activeDropdownEntry.parent;
                        while (parent) {
                            if (parent.next) {
                                this.activeDropdownEntry.clear();
                                parent.next.focus();
                                break;
                            }
                            else {
                                parent = parent.parent;
                            }
                        }
                    }
                }
                else if (event.shiftKey && event.code === "Tab") {
                    if (this.activeDropdownEntry.previous) {
                        this.activeDropdownEntry.previous.focus();
                    }
                    else if (this.activeDropdownEntry.parent) {
                        this.activeDropdownEntry.parent.focus();
                    }
                }
            }
            else if (this.dropdownVisible() && event.code === "Escape") {
                event.preventDefault();
                event.stopPropagation();
                this.closeDropdown();
            }
        }
        openDropdown() {
            this.dispatchEvent(new CustomEvent(Event$1.SHOW));
            if (!this.inStickyHeader()) {
                this.menuStore.appendChild(this.dropdownMenu);
                new Popper(this.toggleButton, this.dropdownMenu, {
                    placement: "bottom-start"
                });
            }
            for (const dropdownEntry of this.dropdownEntries) {
                dropdownEntry.clear();
            }
            this.dropdownMenu.classList.add("show");
            this.dispatchEvent(new CustomEvent(Event$1.SHOWN));
        }
        closeDropdown() {
            this.dispatchEvent(new CustomEvent(Event$1.HIDE));
            this.dropdownMenu.classList.remove("show");
            this.appendChild(this.dropdownMenu);
            this.dispatchEvent(new CustomEvent(Event$1.HIDDEN));
        }
        get toggleButton() {
            return this.querySelector(":scope > button[data-toggle='dropdown']");
        }
        toggleButtonSelected(event) {
            return this.toggleButton.contains(event.target);
        }
        inStickyHeader() {
            return Boolean(this.closest("tobago-header.sticky-top"));
        }
        get dropdownMenu() {
            const root = this.getRootNode();
            return root.querySelector(".dropdown-menu[name='" + this.id + "']");
        }
        dropdownVisible() {
            return this.dropdownMenu.classList.contains("show");
        }
        get menuStore() {
            const root = this.getRootNode();
            return root.querySelector(".tobago-page-menuStore");
        }
        get activeDropdownEntry() {
            for (const dropdownEntry of this.dropdownEntries) {
                if (dropdownEntry.active) {
                    return dropdownEntry;
                }
            }
            return null;
        }
        createDropdownEntries(dropdownMenu, parent) {
            let lastDropdownEntry = null;
            for (const dropdownItem of dropdownMenu.children) {
                if (dropdownItem.classList.contains("dropdown-item")) {
                    const entry = this.createDropdownEntry(dropdownItem, parent, lastDropdownEntry);
                    lastDropdownEntry = entry;
                    this.dropdownEntries.push(entry);
                    if (dropdownItem.classList.contains("tobago-dropdown-submenu")) {
                        this.createDropdownEntries(dropdownItem.querySelector(".dropdown-menu"), entry);
                    }
                }
                else {
                    const dropdownItems = dropdownItem.querySelectorAll(".dropdown-item");
                    for (const dropdownItem of dropdownItems) {
                        const entry = this.createDropdownEntry(dropdownItem, parent, lastDropdownEntry);
                        lastDropdownEntry = entry;
                        this.dropdownEntries.push(entry);
                    }
                }
            }
        }
        createDropdownEntry(dropdownItem, parent, previous) {
            const entry = new DropdownEntry(dropdownItem);
            if (parent) {
                entry.parent = parent;
                parent.children.push(entry);
            }
            if (previous) {
                previous.next = entry;
                entry.previous = previous;
            }
            return entry;
        }
    }
    class DropdownEntry {
        constructor(dropdownItem) {
            this._children = [];
            this._baseElement = dropdownItem;
            if (dropdownItem.classList.contains("tobago-dropdown-submenu")) {
                this.focusElement = dropdownItem.querySelector(".tobago-link");
            }
            else if (dropdownItem.tagName === "LABEL") {
                const root = dropdownItem.getRootNode();
                this.focusElement = root.getElementById(dropdownItem.getAttribute("for"));
            }
            else {
                this.focusElement = dropdownItem;
            }
            this._baseElement.addEventListener("mouseenter", this.activate.bind(this));
            this._baseElement.addEventListener("mouseleave", this.deactivate.bind(this));
        }
        activate(event) {
            this.active = true;
        }
        deactivate(event) {
            this.active = false;
        }
        get previous() {
            return this._previous;
        }
        set previous(value) {
            this._previous = value;
        }
        get next() {
            return this._next;
        }
        set next(value) {
            this._next = value;
        }
        get parent() {
            return this._parent;
        }
        set parent(value) {
            this._parent = value;
        }
        get children() {
            return this._children;
        }
        set children(value) {
            this._children = value;
        }
        get active() {
            return this._active;
        }
        set active(value) {
            this._active = value;
        }
        focus() {
            var _a, _b;
            (_a = this.previous) === null || _a === void 0 ? void 0 : _a.clear();
            (_b = this.next) === null || _b === void 0 ? void 0 : _b.clear();
            if (this.parent) {
                this.parent.active = false;
                this.parent._baseElement.classList.add("tobago-dropdown-open");
            }
            for (const child of this.children) {
                child.clear();
            }
            this._baseElement.classList.remove("tobago-dropdown-open");
            this._baseElement.classList.add("tobago-dropdown-selected");
            this.active = true;
            this.focusElement.focus();
        }
        clear() {
            this._baseElement.classList.remove("tobago-dropdown-open");
            this._baseElement.classList.remove("tobago-dropdown-selected");
            this.active = false;
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-dropdown") == null) {
            window.customElements.define("tobago-dropdown", Dropdown);
        }
    });

    function hasProperty(obj, prop) {
      return Object.prototype.hasOwnProperty.call(obj, prop);
    }

    function lastItemOf(arr) {
      return arr[arr.length - 1];
    }

    // push only the items not included in the array
    function pushUnique(arr, ...items) {
      items.forEach((item) => {
        if (arr.includes(item)) {
          return;
        }
        arr.push(item);
      });
      return arr;
    }

    function stringToArray(str, separator) {
      // convert empty string to an empty array
      return str ? str.split(separator) : [];
    }

    function isInRange(testVal, min, max) {
      const minOK = min === undefined || testVal >= min;
      const maxOK = max === undefined || testVal <= max;
      return minOK && maxOK;
    }

    function limitToRange(val, min, max) {
      if (val < min) {
        return min;
      }
      if (val > max) {
        return max;
      }
      return val;
    }

    function createTagRepeat(tagName, repeat, attributes = {}, index = 0, html = '') {
      const openTagSrc = Object.keys(attributes).reduce((src, attr) => {
        let val = attributes[attr];
        if (typeof val === 'function') {
          val = val(index);
        }
        return `${src} ${attr}="${val}"`;
      }, tagName);
      html += `<${openTagSrc}></${tagName}>`;

      const next = index + 1;
      return next < repeat
        ? createTagRepeat(tagName, repeat, attributes, next, html)
        : html;
    }

    // Remove the spacing surrounding tags for HTML parser not to create text nodes
    // before/after elements
    function optimizeTemplateHTML(html) {
      return html.replace(/>\s+/g, '>').replace(/\s+</, '<');
    }

    function stripTime(timeValue) {
      return new Date(timeValue).setHours(0, 0, 0, 0);
    }

    function today() {
      return new Date().setHours(0, 0, 0, 0);
    }

    // Get the time value of the start of given date or year, month and day
    function dateValue(...args) {
      switch (args.length) {
        case 0:
          return today();
        case 1:
          return stripTime(args[0]);
      }

      // use setFullYear() to keep 2-digit year from being mapped to 1900-1999
      const newDate = new Date(0);
      newDate.setFullYear(...args);
      return newDate.setHours(0, 0, 0, 0);
    }

    function addDays(date, amount) {
      const newDate = new Date(date);
      return newDate.setDate(newDate.getDate() + amount);
    }

    function addWeeks(date, amount) {
      return addDays(date, amount * 7);
    }

    function addMonths(date, amount) {
      // If the day of the date is not in the new month, the last day of the new
      // month will be returned. e.g. Jan 31 + 1 month → Feb 28 (not Mar 03)
      const newDate = new Date(date);
      const monthsToSet = newDate.getMonth() + amount;
      let expectedMonth = monthsToSet % 12;
      if (expectedMonth < 0) {
        expectedMonth += 12;
      }

      const time = newDate.setMonth(monthsToSet);
      return newDate.getMonth() !== expectedMonth ? newDate.setDate(0) : time;
    }

    function addYears(date, amount) {
      // If the date is Feb 29 and the new year is not a leap year, Feb 28 of the
      // new year will be returned.
      const newDate = new Date(date);
      const expectedMonth = newDate.getMonth();
      const time = newDate.setFullYear(newDate.getFullYear() + amount);
      return expectedMonth === 1 && newDate.getMonth() === 2 ? newDate.setDate(0) : time;
    }

    // Calculate the distance bettwen 2 days of the week
    function dayDiff(day, from) {
      return (day - from + 7) % 7;
    }

    // Get the date of the specified day of the week of given base date
    function dayOfTheWeekOf(baseDate, dayOfWeek, weekStart = 0) {
      const baseDay = new Date(baseDate).getDay();
      return addDays(baseDate, dayDiff(dayOfWeek, weekStart) - dayDiff(baseDay, weekStart));
    }

    // Get the ISO week of a date
    function getWeek(date) {
      // start of ISO week is Monday
      const thuOfTheWeek = dayOfTheWeekOf(date, 4, 1);
      // 1st week == the week where the 4th of January is in
      const firstThu = dayOfTheWeekOf(new Date(thuOfTheWeek).setMonth(0, 4), 4, 1);
      return Math.round((thuOfTheWeek - firstThu) / 604800000) + 1;
    }

    // Get the start year of the period of years that includes given date
    // years: length of the year period
    function startOfYearPeriod(date, years) {
      /* @see https://en.wikipedia.org/wiki/Year_zero#ISO_8601 */
      const year = new Date(date).getFullYear();
      return Math.floor(year / years) * years;
    }

    // pattern for format parts
    const reFormatTokens = /dd?|DD?|mm?|MM?|yy?(?:yy)?/;
    // pattern for non date parts
    const reNonDateParts = /[\s!-/:-@[-`{-~年月日]+/;
    // cache for persed formats
    let knownFormats = {};
    // parse funtions for date parts
    const parseFns = {
      y(date, year) {
        return new Date(date).setFullYear(parseInt(year, 10));
      },
      M: undefined,  // placeholder to maintain the key order
      m(date, month, locale) {
        const newDate = new Date(date);
        let monthIndex = parseInt(month, 10) - 1;

        if (isNaN(monthIndex)) {
          if (!month) {
            return NaN;
          }

          const monthName = month.toLowerCase();
          const compareNames = name => name.toLowerCase().startsWith(monthName);
          // compare with both short and full names because some locales have periods
          // in the short names (not equal to the first X letters of the full names)
          monthIndex = locale.monthsShort.findIndex(compareNames);
          if (monthIndex < 0) {
            monthIndex = locale.months.findIndex(compareNames);
          }
          return monthIndex < 0 ? NaN : newDate.setMonth(monthIndex);
        }

        newDate.setMonth(monthIndex);
        return newDate.getMonth() !== normalizeMonth(monthIndex)
          ? newDate.setDate(0)
          : newDate.getTime();
      },
      d(date, day) {
        return new Date(date).setDate(parseInt(day, 10));
      },
    };
    parseFns.M = parseFns.m;  // make "M" an alias of "m"
    // format functions for date parts
    const formatFns = {
      d(date) {
        return date.getDate();
      },
      dd(date) {
        return padZero(date.getDate(), 2);
      },
      D(date, locale) {
        return locale.daysShort[date.getDay()];
      },
      DD(date, locale) {
        return locale.days[date.getDay()];
      },
      m(date) {
        return date.getMonth() + 1;
      },
      mm(date) {
        return padZero(date.getMonth() + 1, 2);
      },
      M(date, locale) {
        return locale.monthsShort[date.getMonth()];
      },
      MM(date, locale) {
        return locale.months[date.getMonth()];
      },
      y(date) {
        return date.getFullYear();
      },
      yy(date) {
        return padZero(date.getFullYear(), 2).slice(-2);
      },
      yyyy(date) {
        return padZero(date.getFullYear(), 4);
      },
    };

    // get month index in normal range (0 - 11) from any number
    function normalizeMonth(monthIndex) {
      return monthIndex > -1 ? monthIndex % 12 : normalizeMonth(monthIndex + 12);
    }

    function padZero(num, length) {
      return num.toString().padStart(length, '0');
    }

    function parseFormatString(format) {
      if (typeof format !== 'string') {
        throw new Error("Invalid date format.");
      }
      if (format in knownFormats) {
        return knownFormats[format];
      }

      // sprit the format string into parts and seprators
      const separators = format.split(reFormatTokens);
      const parts = format.match(new RegExp(reFormatTokens, 'g'));
      if (separators.length === 0 || !parts) {
        throw new Error("Invalid date format.");
      }

      // collect format functions used in the format
      const partFormatters = parts.map(token => formatFns[token]);

      // collect parse functions used in the format
      // iterate over parseFns' keys in order to keep the order of the keys.
      const partParsers = Object.keys(parseFns).reduce((parsers, key) => {
        const token = parts.find(part => part[0] === key);
        if (!token) {
          return parsers;
        }
        parsers[key] = parseFns[key];
        return parsers;
      }, {});
      const partParserKeys = Object.keys(partParsers);

      return knownFormats[format] = {
        parser(dateStr, locale) {
          const dateParts = dateStr.split(reNonDateParts).reduce((dtParts, part, index) => {
            if (part.length > 0 && parts[index]) {
              const token = parts[index][0];
              if (parseFns[token] !== undefined) {
                dtParts[token] = part;
              }
            }
            return dtParts;
          }, {});

          // iterate over partParsers' keys so that the parsing is made in the oder
          // of year, month and day to prevent the day parser from correcting last
          // day of month wrongly
          return partParserKeys.reduce((origDate, key) => {
            const newDate = partParsers[key](origDate, dateParts[key], locale);
            // ingnore the part failed to parse
            return isNaN(newDate) ? origDate : newDate;
          }, today());
        },
        formatter(date, locale) {
          let dateStr = partFormatters.reduce((str, fn, index) => {
            return str += `${separators[index]}${fn(date, locale)}`;
          }, '');
          // separators' length is always parts' length + 1,
          return dateStr += lastItemOf(separators);
        },
      };
    }

    function parseDate(dateStr, format, locale) {
      if (dateStr instanceof Date || typeof dateStr === 'number') {
        const date = stripTime(dateStr);
        return isNaN(date) ? undefined : date;
      }
      if (!dateStr) {
        return undefined;
      }
      if (dateStr === 'today') {
        return today();
      }

      if (format && format.toValue) {
        const date = format.toValue(dateStr, format, locale);
        return isNaN(date) ? undefined : stripTime(date);
      }

      return parseFormatString(format).parser(dateStr, locale);
    }

    function formatDate(date, format, locale) {
      if (isNaN(date) || (!date && date !== 0)) {
        return '';
      }

      const dateObj = typeof date === 'number' ? new Date(date) : date;

      if (format.toDisplay) {
        return format.toDisplay(dateObj, format, locale);
      }

      return parseFormatString(format).formatter(dateObj, locale);
    }

    const listenerRegistry = new WeakMap();
    const {addEventListener, removeEventListener} = EventTarget.prototype;

    // Register event listeners to a key object
    // listeners: array of listener definitions;
    //   - each definition must be a flat array of event target and the arguments
    //     used to call addEventListener() on the target
    function registerListeners(keyObj, listeners) {
      let registered = listenerRegistry.get(keyObj);
      if (!registered) {
        registered = [];
        listenerRegistry.set(keyObj, registered);
      }
      listeners.forEach((listener) => {
        addEventListener.call(...listener);
        registered.push(listener);
      });
    }

    function unregisterListeners(keyObj) {
      let listeners = listenerRegistry.get(keyObj);
      if (!listeners) {
        return;
      }
      listeners.forEach((listener) => {
        removeEventListener.call(...listener);
      });
      listenerRegistry.delete(keyObj);
    }

    // Event.composedPath() polyfill for Edge
    // based on https://gist.github.com/kleinfreund/e9787d73776c0e3750dcfcdc89f100ec
    if (!Event.prototype.composedPath) {
      const getComposedPath = (node, path = []) => {
        path.push(node);

        let parent;
        if (node.parentNode) {
          parent = node.parentNode;
        } else if (node.host) { // ShadowRoot
          parent = node.host;
        } else if (node.defaultView) {  // Document
          parent = node.defaultView;
        }
        return parent ? getComposedPath(parent, path) : path;
      };

      Event.prototype.composedPath = function () {
        return getComposedPath(this.target);
      };
    }

    function findFromPath(path, criteria, currentTarget, index = 0) {
      const el = path[index];
      if (criteria(el)) {
        return el;
      } else if (el === currentTarget || !el.parentElement) {
        // stop when reaching currentTarget or <html>
        return;
      }
      return findFromPath(path, criteria, currentTarget, index + 1);
    }

    // Search for the actual target of a delegated event
    function findElementInEventPath(ev, selector) {
      const criteria = typeof selector === 'function' ? selector : el => el.matches(selector);
      return findFromPath(ev.composedPath(), criteria, ev.currentTarget);
    }

    // default locales
    const locales = {
      en: {
        days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
        daysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
        daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
        months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
        monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        today: "Today",
        clear: "Clear",
        titleFormat: "MM y"
      }
    };

    // config options updatable by setOptions() and their default values
    const defaultOptions = {
      autohide: false,
      beforeShowDay: null,
      beforeShowDecade: null,
      beforeShowMonth: null,
      beforeShowYear: null,
      calendarWeeks: false,
      clearBtn: false,
      dateDelimiter: ',',
      datesDisabled: [],
      daysOfWeekDisabled: [],
      daysOfWeekHighlighted: [],
      defaultViewDate: undefined, // placeholder, defaults to today() by the program
      disableTouchKeyboard: false,
      format: 'mm/dd/yyyy',
      language: 'en',
      maxDate: null,
      maxNumberOfDates: 1,
      maxView: 3,
      minDate: null,
      nextArrow: '»',
      orientation: 'auto',
      prevArrow: '«',
      showDaysOfWeek: true,
      showOnFocus: true,
      startView: 0,
      title: '',
      todayBtn: false,
      todayBtnMode: 0,
      todayHighlight: false,
      weekStart: 0,
    };

    const range = document.createRange();

    function parseHTML(html) {
      return range.createContextualFragment(html);
    }

    function hideElement(el) {
      if (el.style.display === 'none') {
        return;
      }
      // back up the existing display setting in data-style-display
      if (el.style.display) {
        el.dataset.styleDisplay = el.style.display;
      }
      el.style.display = 'none';
    }

    function showElement(el) {
      if (el.style.display !== 'none') {
        return;
      }
      if (el.dataset.styleDisplay) {
        // restore backed-up dispay property
        el.style.display = el.dataset.styleDisplay;
        delete el.dataset.styleDisplay;
      } else {
        el.style.display = '';
      }
    }

    function emptyChildNodes(el) {
      if (el.firstChild) {
        el.removeChild(el.firstChild);
        emptyChildNodes(el);
      }
    }

    function replaceChildNodes(el, newChildNodes) {
      emptyChildNodes(el);
      if (newChildNodes instanceof DocumentFragment) {
        el.appendChild(newChildNodes);
      } else if (typeof newChildNodes === 'string') {
        el.appendChild(parseHTML(newChildNodes));
      } else if (typeof newChildNodes.forEach === 'function') {
        newChildNodes.forEach((node) => {
          el.appendChild(node);
        });
      }
    }

    const {
      language: defaultLang,
      format: defaultFormat,
      weekStart: defaultWeekStart,
    } = defaultOptions;

    // Reducer function to filter out invalid day-of-week from the input
    function sanitizeDOW(dow, day) {
      return dow.length < 6 && day >= 0 && day < 7
        ? pushUnique(dow, day)
        : dow;
    }

    function calcEndOfWeek(startOfWeek) {
      return (startOfWeek + 6) % 7;
    }

    // validate input date. if invalid, fallback to the original value
    function validateDate(value, format, locale, origValue) {
      const date = parseDate(value, format, locale);
      return date !== undefined ? date : origValue;
    }

    // Validate viewId. if invalid, fallback to the original value
    function validateViewId(value, origValue) {
      const viewId = parseInt(value, 10);
      return viewId >= 0 && viewId < 4 ? viewId : origValue;
    }

    // Create Datepicker configuration to set
    function processOptions(options, datepicker) {
      const inOpts = Object.assign({}, options);
      const config = {};
      const locales = datepicker.constructor.locales;
      let {
        format,
        language,
        locale,
        maxDate,
        maxView,
        minDate,
        startView,
        weekStart,
      } = datepicker.config || {};

      if (inOpts.language) {
        let lang;
        if (inOpts.language !== language) {
          if (locales[inOpts.language]) {
            lang = inOpts.language;
          } else {
            // Check if langauge + region tag can fallback to the one without
            // region (e.g. fr-CA → fr)
            lang = inOpts.language.split('-')[0];
            if (locales[lang] === undefined) {
              lang = false;
            }
          }
        }
        delete inOpts.language;
        if (lang) {
          language = config.language = lang;

          // update locale as well when updating language
          const origLocale = locale || locales[defaultLang];
          // use default language's properties for the fallback
          locale = Object.assign({
            format: defaultFormat,
            weekStart: defaultWeekStart
          }, locales[defaultLang]);
          if (language !== defaultLang) {
            Object.assign(locale, locales[language]);
          }
          config.locale = locale;
          // if format and/or weekStart are the same as old locale's defaults,
          // update them to new locale's defaults
          if (format === origLocale.format) {
            format = config.format = locale.format;
          }
          if (weekStart === origLocale.weekStart) {
            weekStart = config.weekStart = locale.weekStart;
            config.weekEnd = calcEndOfWeek(locale.weekStart);
          }
        }
      }

      if (inOpts.format) {
        const hasToDisplay = typeof inOpts.format.toDisplay === 'function';
        const hasToValue = typeof inOpts.format.toValue === 'function';
        const validFormatString = reFormatTokens.test(inOpts.format);
        if ((hasToDisplay && hasToValue) || validFormatString) {
          format = config.format = inOpts.format;
        }
        delete inOpts.format;
      }

      //*** dates ***//
      // while min and maxDate for "no limit" in the options are better to be null
      // (especially when updating), the ones in the config have to be undefined
      // because null is treated as 0 (= unix epoch) when comparing with time value
      let minDt = minDate;
      let maxDt = maxDate;
      if (inOpts.minDate !== undefined) {
        minDt = inOpts.minDate === null
          ? dateValue(0, 0, 1)  // set 0000-01-01 to prevent negative values for year
          : validateDate(inOpts.minDate, format, locale, minDt);
        delete inOpts.minDate;
      }
      if (inOpts.maxDate !== undefined) {
        maxDt = inOpts.maxDate === null
          ? undefined
          : validateDate(inOpts.maxDate, format, locale, maxDt);
        delete inOpts.maxDate;
      }
      if (maxDt < minDt) {
        minDate = config.minDate = maxDt;
        maxDate = config.maxDate = minDt;
      } else {
        if (minDate !== minDt) {
          minDate = config.minDate = minDt;
        }
        if (maxDate !== maxDt) {
          maxDate = config.maxDate = maxDt;
        }
      }

      if (inOpts.datesDisabled) {
        config.datesDisabled = inOpts.datesDisabled.reduce((dates, dt) => {
          const date = parseDate(dt, format, locale);
          return date !== undefined ? pushUnique(dates, date) : dates;
        }, []);
        delete inOpts.datesDisabled;
      }
      if (inOpts.defaultViewDate !== undefined) {
        const viewDate = parseDate(inOpts.defaultViewDate, format, locale);
        if (viewDate !== undefined) {
          config.defaultViewDate = viewDate;
        }
        delete inOpts.defaultViewDate;
      }

      //*** days of week ***//
      if (inOpts.weekStart !== undefined) {
        const wkStart = Number(inOpts.weekStart) % 7;
        if (!isNaN(wkStart)) {
          weekStart = config.weekStart = wkStart;
          config.weekEnd = calcEndOfWeek(wkStart);
        }
        delete inOpts.weekStart;
      }
      if (inOpts.daysOfWeekDisabled) {
        config.daysOfWeekDisabled = inOpts.daysOfWeekDisabled.reduce(sanitizeDOW, []);
        delete inOpts.daysOfWeekDisabled;
      }
      if (inOpts.daysOfWeekHighlighted) {
        config.daysOfWeekHighlighted = inOpts.daysOfWeekHighlighted.reduce(sanitizeDOW, []);
        delete inOpts.daysOfWeekHighlighted;
      }

      //*** multi date ***//
      if (inOpts.maxNumberOfDates !== undefined) {
        const maxNumberOfDates = parseInt(inOpts.maxNumberOfDates, 10);
        if (maxNumberOfDates >= 0) {
          config.maxNumberOfDates = maxNumberOfDates;
          config.multidate = maxNumberOfDates !== 1;
        }
        delete inOpts.maxNumberOfDates;
      }
      if (inOpts.dateDelimiter) {
        config.dateDelimiter = String(inOpts.dateDelimiter);
        delete inOpts.dateDelimiter;
      }

      //*** view mode ***//
      let newMaxView = maxView;
      if (inOpts.maxView !== undefined) {
        newMaxView = validateViewId(inOpts.maxView, maxView);
        delete inOpts.maxView;
      }
      if (newMaxView !== maxView) {
        maxView = config.maxView = newMaxView;
      }

      let newStartView = startView;
      if (inOpts.startView !== undefined) {
        newStartView = validateViewId(inOpts.startView, newStartView);
        delete inOpts.startView;
      }
      // ensure start view < max
      newStartView = maxView < newStartView ? maxView : newStartView;
      if (newStartView !== startView) {
        config.startView = newStartView;
      }

      //*** template ***//
      if (inOpts.prevArrow) {
        const prevArrow = parseHTML(inOpts.prevArrow);
        if (prevArrow.childNodes.length > 0) {
          config.prevArrow = prevArrow.childNodes;
        }
        delete inOpts.prevArrow;
      }
      if (inOpts.nextArrow) {
        const nextArrow = parseHTML(inOpts.nextArrow);
        if (nextArrow.childNodes.length > 0) {
          config.nextArrow = nextArrow.childNodes;
        }
        delete inOpts.nextArrow;
      }

      //*** misc ***//
      if (inOpts.disableTouchKeyboard !== undefined) {
        config.disableTouchKeyboard = 'ontouchstart' in document && !!inOpts.disableTouchKeyboard;
        delete inOpts.disableTouchKeyboard;
      }
      if (inOpts.orientation) {
        const orientation = inOpts.orientation.toLowerCase().split(/\s+/g);
        config.orientation = {
          x: orientation.find(x => (x === 'left' || x === 'right')) || 'auto',
          y: orientation.find(y => (y === 'top' || y === 'bottom')) || 'auto',
        };
        delete inOpts.orientation;
      }
      if (inOpts.todayBtnMode !== undefined) {
        switch(inOpts.todayBtnMode) {
          case 0:
          case 1:
            config.todayBtnMode = inOpts.todayBtnMode;
        }
        delete inOpts.todayBtnMode;
      }

      //*** copy the rest ***//
      Object.keys(inOpts).forEach((key) => {
        if (inOpts[key] !== undefined && hasProperty(defaultOptions, key)) {
          config[key] = inOpts[key];
        }
      });

      return config;
    }

    const pickerTemplate = optimizeTemplateHTML(`<div class="datepicker">
  <div class="datepicker-picker">
    <div class="datepicker-header">
      <div class="datepicker-title"></div>
      <div class="datepicker-controls">
        <button class="%buttonClass% prev-btn"></button>
        <button class="%buttonClass% view-switch"></button>
        <button class="%buttonClass% next-btn"></button>
      </div>
    </div>
    <div class="datepicker-main"></div>
    <div class="datepicker-footer">
      <div class="datepicker-controls">
        <button class="%buttonClass% today-btn"></button>
        <button class="%buttonClass% clear-btn"></button>
      </div>
    </div>
  </div>
</div>`);

    const daysTemplate = optimizeTemplateHTML(`<div class="days">
  <div class="days-of-week">${createTagRepeat('span', 7, {class: 'dow'})}</div>
  <div class="datepicker-grid">${createTagRepeat('span', 42)}</div>
</div>`);

    const calendarWeeksTemplate = optimizeTemplateHTML(`<div class="calendar-weeks">
  <div class="days-of-week"><span class="dow"></span></div>
  <div class="weeks">${createTagRepeat('span', 6, {class: 'week'})}</div>
</div>`);

    // Base class of the view classes
    class View {
      constructor(picker, config) {
        Object.assign(this, config, {
          picker,
          element: parseHTML(`<div class="datepicker-view"></div>`).firstChild,
          selected: [],
        });
        this.init(this.picker.datepicker.config);
      }

      init(options) {
        this.setOptions(options);
        this.updateFocus();
        this.updateSelection();
      }

      // Execute beforeShow() callback and apply the result to the element
      // args:
      // - current - current value on the iteration on view rendering
      // - timeValue - time value of the date to pass to beforeShow()
      performBeforeHook(el, current, timeValue) {
        let result = this.beforeShow(new Date(timeValue));
        switch (typeof result) {
          case 'boolean':
            result = {enabled: result};
            break;
          case 'string':
            result = {classes: result};
        }

        if (result) {
          if (result.enabled === false) {
            el.classList.add('disabled');
            pushUnique(this.disabled, current);
          }
          if (result.classes) {
            const extraClasses = result.classes.split(/\s+/);
            el.classList.add(...extraClasses);
            if (extraClasses.includes('disabled')) {
              pushUnique(this.disabled, current);
            }
          }
          if (result.content) {
            replaceChildNodes(el, result.content);
          }
        }
      }
    }

    class DaysView extends View {
      constructor(picker) {
        super(picker, {
          id: 0,
          name: 'days',
          cellClass: 'day',
        });
      }

      init(options, onConstruction = true) {
        if (onConstruction) {
          const inner = parseHTML(daysTemplate).firstChild;
          this.dow = inner.firstChild;
          this.grid = inner.lastChild;
          this.element.appendChild(inner);
        }
        super.init(options);
      }

      setOptions(options) {
        let updateDOW;

        if (hasProperty(options, 'minDate')) {
          this.minDate = options.minDate;
        }
        if (hasProperty(options, 'maxDate')) {
          this.maxDate = options.maxDate;
        }
        if (options.datesDisabled) {
          this.datesDisabled = options.datesDisabled;
        }
        if (options.daysOfWeekDisabled) {
          this.daysOfWeekDisabled = options.daysOfWeekDisabled;
          updateDOW = true;
        }
        if (options.daysOfWeekHighlighted) {
          this.daysOfWeekHighlighted = options.daysOfWeekHighlighted;
        }
        if (options.todayHighlight !== undefined) {
          this.todayHighlight = options.todayHighlight;
        }
        if (options.weekStart !== undefined) {
          this.weekStart = options.weekStart;
          this.weekEnd = options.weekEnd;
          updateDOW = true;
        }
        if (options.locale) {
          const locale = this.locale = options.locale;
          this.dayNames = locale.daysMin;
          this.switchLabelFormat = locale.titleFormat;
          this.switchLabel = formatDate(this.picker.viewDate, locale.titleFormat, locale);
          updateDOW = true;
        }
        if (options.beforeShowDay !== undefined) {
          this.beforeShow = typeof options.beforeShowDay === 'function'
            ? options.beforeShowDay
            : undefined;
        }

        if (options.calendarWeeks !== undefined) {
          if (options.calendarWeeks && !this.calendarWeeks) {
            const weeksElem = parseHTML(calendarWeeksTemplate).firstChild;
            this.calendarWeeks = {
              element: weeksElem,
              dow: weeksElem.firstChild,
              weeks: weeksElem.lastChild,
            };
            this.element.insertBefore(weeksElem, this.element.firstChild);
          } else if (this.calendarWeeks && !options.calendarWeeks) {
            this.element.removeChild(this.calendarWeeks.element);
            this.calendarWeeks = null;
          }
        }
        if (options.showDaysOfWeek !== undefined) {
          if (options.showDaysOfWeek) {
            showElement(this.dow);
            if (this.calendarWeeks) {
              showElement(this.calendarWeeks.dow);
            }
          } else {
            hideElement(this.dow);
            if (this.calendarWeeks) {
              hideElement(this.calendarWeeks.dow);
            }
          }
        }

        // update days-of-week when locale, daysOfweekDisabled or weekStart is changed
        if (updateDOW) {
          Array.from(this.dow.children).forEach((el, index) => {
            const dow = (this.weekStart + index) % 7;
            el.textContent = this.dayNames[dow];
            el.className = this.daysOfWeekDisabled.includes(dow) ? 'dow disabled' : 'dow';
          });
        }
      }

      // Apply update on the focused date to view's settings
      updateFocus() {
        const viewDate = new Date(this.picker.viewDate);
        const viewYear = viewDate.getFullYear();
        const viewMonth = viewDate.getMonth();
        const firstOfMonth = dateValue(viewYear, viewMonth, 1);
        const start = dayOfTheWeekOf(firstOfMonth, this.weekStart, this.weekStart);

        this.first = firstOfMonth;
        this.last = dateValue(viewYear, viewMonth + 1, 0);
        this.start = start;

        this.switchLabel = formatDate(viewDate, this.switchLabelFormat, this.locale);
        this.focused = this.picker.viewDate;
      }

      // Apply update on the selected dates to view's settings
      updateSelection() {
        const {dates, range} = this.picker.datepicker;
        this.selected = dates;
        this.range = range;
      }

       // Update the entire view UI
      render() {
        // update today marker on ever render
        this.today = this.todayHighlight ? today() : undefined;
        // refresh disabled dates on every render in order to clear the ones added
        // by beforeShow hook at previous render
        this.disabled = [...this.datesDisabled];

        this.picker.setViewSwitchLabel(this.switchLabel);
        this.picker.setPrevBtnDisabled(this.first <= this.minDate);
        this.picker.setNextBtnDisabled(this.last >= this.maxDate);

        if (this.calendarWeeks) {
          // start of the UTC week (Monday) of the 1st of the month
          const startOfWeek = dayOfTheWeekOf(this.first, 1, 1);
          Array.from(this.calendarWeeks.weeks.children).forEach((el, index) => {
            el.textContent = getWeek(addWeeks(startOfWeek, index));
          });
        }
        Array.from(this.grid.children).forEach((el, index) => {
          const classList = el.classList;
          const current = addDays(this.start, index);
          const date = new Date(current);
          const day = date.getDay();

          el.className = `datepicker-cell ${this.cellClass}`;
          el.dataset.date = current;
          el.textContent = date.getDate();

          if (current < this.first) {
            classList.add('prev');
          } else if (current > this.last) {
            classList.add('next');
          }
          if (this.today === current) {
            classList.add('today');
          }
          if (current < this.minDate || current > this.maxDate || this.disabled.includes(current)) {
            classList.add('disabled');
          }
          if (this.daysOfWeekDisabled.includes(day)) {
            classList.add('disabled');
            pushUnique(this.disabled, current);
          }
          if (this.daysOfWeekHighlighted.includes(day)) {
            classList.add('highlighted');
          }
          if (this.range){
            const [rangeStart, rangeEnd] = this.range;
            if (current > rangeStart && current < rangeEnd) {
              classList.add('range');
            }
            if (current === rangeStart) {
              classList.add('range-start');
            }
            if (current === rangeEnd) {
              classList.add('range-end');
            }
          }
          if (this.selected.includes(current)) {
            classList.add('selected');
          }
          if (current === this.focused) {
            classList.add('focused');
          }

          if (this.beforeShow) {
            this.performBeforeHook(el, current, current);
          }
        });
      }

      // Update the view UI by applying the changes of selected and focused items
      refresh() {
        const [rangeStart, rangeEnd] = this.range || [];
        this.grid
          .querySelectorAll('.range, .range-start, .range-end, .selected, .focused')
          .forEach((el) => {
            el.classList.remove('range', 'range-start', 'range-end', 'selected', 'focused');
          });
        Array.from(this.grid.children).forEach((el) => {
          const current = Number(el.dataset.date);
          const classList = el.classList;
          if (current > rangeStart && current < rangeEnd) {
            classList.add('range');
          }
          if (current === rangeStart) {
            classList.add('range-start');
          }
          if (current === rangeEnd) {
            classList.add('range-end');
          }
          if (this.selected.includes(current)) {
            classList.add('selected');
          }
          if (current === this.focused) {
            classList.add('focused');
          }
        });
      }

      // Update the view UI by applying the change of focused item
      refreshFocus() {
        const index = Math.round((this.focused - this.start) / 86400000);
        this.grid.querySelectorAll('.focused').forEach((el) => {
          el.classList.remove('focused');
        });
        this.grid.children[index].classList.add('focused');
      }
    }

    class MonthsView extends View {
      constructor(picker) {
        super(picker, {
          id: 1,
          name: 'months',
          cellClass: 'month',
        });
      }

      init(options, onConstruction = true) {
        if (onConstruction) {
          this.grid = this.element;
          this.element.classList.add('months', 'datepicker-grid');
          this.grid.appendChild(parseHTML(createTagRepeat('span', 12, {'data-month': ix => ix})));
        }
        super.init(options);
      }

      setOptions(options) {
        if (options.locale) {
          this.monthNames = options.locale.monthsShort;
        }
        if (hasProperty(options, 'minDate')) {
          if (options.minDate === undefined) {
            this.minYear = this.minMonth = this.minDate = undefined;
          } else {
            const minDateObj = new Date(options.minDate);
            this.minYear = minDateObj.getFullYear();
            this.minMonth = minDateObj.getMonth();
            this.minDate = minDateObj.setDate(1);
          }
        }
        if (hasProperty(options, 'maxDate')) {
          if (options.maxDate === undefined) {
            this.maxYear = this.maxMonth = this.maxDate = undefined;
          } else {
            const maxDateObj = new Date(options.maxDate);
            this.maxYear = maxDateObj.getFullYear();
            this.maxMonth = maxDateObj.getMonth();
            this.maxDate = dateValue(this.maxYear, this.maxMonth + 1, 0);
          }
        }
        if (options.beforeShowMonth !== undefined) {
          this.beforeShow = typeof options.beforeShowMonth === 'function'
            ? options.beforeShowMonth
            : undefined;
        }
      }

      // Update view's settings to reflect the viewDate set on the picker
      updateFocus() {
        const viewDate = new Date(this.picker.viewDate);
        this.year = viewDate.getFullYear();
        this.switchLabel = this.year;
        this.focused = viewDate.getMonth();
      }

      // Update view's settings to reflect the selected dates
      updateSelection() {
        this.selected = this.picker.datepicker.dates.reduce((selected, timeValue) => {
          const date = new Date(timeValue);
          const year = date.getFullYear();
          const month = date.getMonth();
          if (selected[year] === undefined) {
            selected[year] = [month];
          } else {
            pushUnique(selected[year], month);
          }
          return selected;
        }, {});
      }

      // Update the entire view UI
      render() {
        // refresh disabled months on every render in order to clear the ones added
        // by beforeShow hook at previous render
        this.disabled = [];

        this.picker.setViewSwitchLabel(this.switchLabel);
        this.picker.setPrevBtnDisabled(this.year <= this.minYear);
        this.picker.setNextBtnDisabled(this.year >= this.maxYear);

        const selected = this.selected[this.year] || [];
        const yrOutOfRange = this.year < this.minYear || this.year > this.maxYear;
        const isMinYear = this.year === this.minYear;
        const isMaxYear = this.year === this.maxYear;
        Array.from(this.grid.children).forEach((el, index) => {
          const classList = el.classList;

          el.className = `datepicker-cell ${this.cellClass}`;
          // reset text on every render to clear the custom content set
          // by beforeShow hook at previous render
          el.textContent = this.monthNames[index];

          if (
            yrOutOfRange
            || isMinYear && index < this.minMonth
            || isMaxYear && index > this.maxMonth
          ) {
            classList.add('disabled');
          }
          if (selected.includes(index)) {
            classList.add('selected');
          }
          if (index === this.focused) {
            classList.add('focused');
          }

          if (this.beforeShow) {
            this.performBeforeHook(el, index, dateValue(this.year, index, 1));
          }
        });
      }

      // Update the view UI by applying the changes of selected and focused items
      refresh() {
        const selected = this.selected[this.year] || [];
        this.grid.querySelectorAll('.selected, .focused').forEach((el) => {
          el.classList.remove('selected', 'focused');
        });
        Array.from(this.grid.children).forEach((el, index) => {
          const classList = el.classList;
          if (selected.includes(index)) {
            classList.add('selected');
          }
          if (index === this.focused) {
            classList.add('focused');
          }
        });
      }

      // Update the view UI by applying the change of focused item
      refreshFocus() {
        this.grid.querySelectorAll('.focused').forEach((el) => {
          el.classList.remove('focused');
        });
        this.grid.children[this.focused].classList.add('focused');
      }
    }

    function toTitleCase(word) {
      return [...word].reduce((str, ch, ix) => str += ix ? ch : ch.toUpperCase(), '');
    }

    // Class representing the years and decades view elements
    class YearsView extends View {
      constructor(picker, config) {
        super(picker, config);
      }

      init(options, onConstruction = true) {
        if (onConstruction) {
          this.navStep = this.step * 10;
          this.beforeShowOption = `beforeShow${toTitleCase(this.cellClass)}`;
          this.grid = this.element;
          this.element.classList.add(this.name, 'datepicker-grid');
          this.grid.appendChild(parseHTML(createTagRepeat('span', 12)));
        }
        super.init(options);
      }

      setOptions(options) {
        if (hasProperty(options, 'minDate')) {
          if (options.minDate === undefined) {
            this.minYear = this.minDate = undefined;
          } else {
            this.minYear = startOfYearPeriod(options.minDate, this.step);
            this.minDate = dateValue(this.minYear, 0, 1);
          }
        }
        if (hasProperty(options, 'maxDate')) {
          if (options.maxDate === undefined) {
            this.maxYear = this.maxDate = undefined;
          } else {
            this.maxYear = startOfYearPeriod(options.maxDate, this.step);
            this.maxDate = dateValue(this.maxYear, 11, 31);
          }
        }
        if (options[this.beforeShowOption] !== undefined) {
          const beforeShow = options[this.beforeShowOption];
          this.beforeShow = typeof beforeShow === 'function' ? beforeShow : undefined;
        }
      }

      // Update view's settings to reflect the viewDate set on the picker
      updateFocus() {
        const viewDate = new Date(this.picker.viewDate);
        const first = startOfYearPeriod(viewDate, this.navStep);
        const last = first + 9 * this.step;

        this.first = first;
        this.last = last;
        this.start = first - this.step;
        this.switchLabel = `${first}-${last}`;
        this.focused = startOfYearPeriod(viewDate, this.step);
      }

      // Update view's settings to reflect the selected dates
      updateSelection() {
        this.selected = this.picker.datepicker.dates.reduce((years, timeValue) => {
          return pushUnique(years, startOfYearPeriod(timeValue, this.step));
        }, []);
      }

      // Update the entire view UI
      render() {
        // refresh disabled years on every render in order to clear the ones added
        // by beforeShow hook at previous render
        this.disabled = [];

        this.picker.setViewSwitchLabel(this.switchLabel);
        this.picker.setPrevBtnDisabled(this.first <= this.minYear);
        this.picker.setNextBtnDisabled(this.last >= this.maxYear);

        Array.from(this.grid.children).forEach((el, index) => {
          const classList = el.classList;
          const current = this.start + (index * this.step);

          el.className = `datepicker-cell ${this.cellClass}`;
          el.textContent = el.dataset.year = current;

          if (index === 0) {
            classList.add('prev');
          } else if (index === 11) {
            classList.add('next');
          }
          if (current < this.minYear || current > this.maxYear) {
            classList.add('disabled');
          }
          if (this.selected.includes(current)) {
            classList.add('selected');
          }
          if (current === this.focused) {
            classList.add('focused');
          }

          if (this.beforeShow) {
            this.performBeforeHook(el, current, dateValue(current, 0, 1));
          }
        });
      }

      // Update the view UI by applying the changes of selected and focused items
      refresh() {
        this.grid.querySelectorAll('.selected, .focused').forEach((el) => {
          el.classList.remove('selected', 'focused');
        });
        Array.from(this.grid.children).forEach((el) => {
          const current = Number(el.textContent);
          const classList = el.classList;
          if (this.selected.includes(current)) {
            classList.add('selected');
          }
          if (current === this.focused) {
            classList.add('focused');
          }
        });
      }

      // Update the view UI by applying the change of focused item
      refreshFocus() {
        const index = Math.round((this.focused - this.start) / this.step);
        this.grid.querySelectorAll('.focused').forEach((el) => {
          el.classList.remove('focused');
        });
        this.grid.children[index].classList.add('focused');
      }
    }

    function triggerDatepickerEvent(datepicker, type) {
      const detail = {
        date: datepicker.getDate(),
        viewDate: new Date(datepicker.picker.viewDate),
        viewId: datepicker.picker.currentView.id,
        datepicker,
      };
      datepicker.element.dispatchEvent(new CustomEvent(type, {detail}));
    }

    // direction: -1 (to previous), 1 (to next)
    function goToPrevOrNext(datepicker, direction) {
      const {minDate, maxDate} = datepicker.config;
      const {currentView, viewDate} = datepicker.picker;
      let newViewDate;
      switch (currentView.id) {
        case 0:
          newViewDate = addMonths(viewDate, direction);
          break;
        case 1:
          newViewDate = addYears(viewDate, direction);
          break;
        default:
          newViewDate = addYears(viewDate, direction * currentView.navStep);
      }
      newViewDate = limitToRange(newViewDate, minDate, maxDate);
      datepicker.picker.changeFocus(newViewDate).render();
    }

    function switchView(datepicker) {
      const viewId = datepicker.picker.currentView.id;
      if (viewId === datepicker.config.maxView) {
        return;
      }
      datepicker.picker.changeView(viewId + 1).render();
    }

    function goToSelectedMonthOrYear(datepicker, selection) {
      const picker = datepicker.picker;
      const viewDate = new Date(picker.viewDate);
      const viewId = picker.currentView.id;
      const newDate = viewId === 1
        ? addMonths(viewDate, selection - viewDate.getMonth())
        : addYears(viewDate, selection - viewDate.getFullYear());

      picker.changeFocus(newDate).changeView(viewId - 1).render();
    }

    function onClickTodayBtn(datepicker) {
      const picker = datepicker.picker;
      const currentDate = today();
      if (datepicker.config.todayBtnMode === 1) {
        if (datepicker.config.autohide) {
          datepicker.setDate(currentDate);
          return;
        }
        datepicker.setDate(currentDate, {render: false});
        picker.update();
      }
      if (picker.viewDate !== currentDate) {
        picker.changeFocus(currentDate);
      }
      picker.changeView(0).render();
    }

    function onClickClearBtn(datepicker) {
      datepicker.setDate({clear: true});
    }

    function onClickViewSwitch(datepicker) {
      switchView(datepicker);
    }

    function onClickPrevBtn(datepicker) {
      goToPrevOrNext(datepicker, -1);
    }

    function onClickNextBtn(datepicker) {
      goToPrevOrNext(datepicker, 1);
    }

    // For the picker's main block to delegete the events from `datepicker-cell`s
    function onClickView(datepicker, ev) {
      const target = findElementInEventPath(ev, '.datepicker-cell');
      if (!target || target.classList.contains('disabled')) {
        return;
      }

      switch (datepicker.picker.currentView.id) {
        case 0:
          datepicker.setDate(Number(target.dataset.date));
          break;
        case 1:
          goToSelectedMonthOrYear(datepicker, Number(target.dataset.month));
          break;
        default:
          goToSelectedMonthOrYear(datepicker, Number(target.dataset.year));
      }
    }

    function onClickPicker(datepicker, ev) {
      ev.preventDefault();
      ev.stopPropagation();

      // check if the picker is active in order to prevent the picker from being
      // re-shown after auto-hide when showOnFocus: true
      // it's caused by bubbled event from cells/buttons, but the bubbling cannot
      // be disabled because it's needed to keep the focus on the input element
      if (!datepicker.inline && datepicker.picker.active && !datepicker.config.disableTouchKeyboard) {
        datepicker.inputField.focus();
      }
    }

    function processPickerOptions(picker, options) {
      if (options.title !== undefined) {
        if (options.title) {
          picker.controls.title.textContent = options.title;
          showElement(picker.controls.title);
        } else {
          picker.controls.title.textContent = '';
          hideElement(picker.controls.title);
        }
      }
      if (options.prevArrow) {
        const prevBtn = picker.controls.prevBtn;
        emptyChildNodes(prevBtn);
        options.prevArrow.forEach((node) => {
          prevBtn.appendChild(node.cloneNode(true));
        });
      }
      if (options.nextArrow) {
        const nextBtn = picker.controls.nextBtn;
        emptyChildNodes(nextBtn);
        options.nextArrow.forEach((node) => {
          nextBtn.appendChild(node.cloneNode(true));
        });
      }
      if (options.locale) {
        picker.controls.todayBtn.textContent = options.locale.today;
        picker.controls.clearBtn.textContent = options.locale.clear;
      }
      if (options.todayBtn !== undefined) {
        if (options.todayBtn) {
          showElement(picker.controls.todayBtn);
        } else {
          hideElement(picker.controls.todayBtn);
        }
      }
      if (hasProperty(options, 'minDate') || hasProperty(options, 'maxDate')) {
        const {minDate, maxDate} = picker.datepicker.config;
        picker.controls.todayBtn.disabled = !isInRange(today(), minDate, maxDate);
      }
      if (options.clearBtn !== undefined) {
        if (options.clearBtn) {
          showElement(picker.controls.clearBtn);
        } else {
          hideElement(picker.controls.clearBtn);
        }
      }
    }

    // Compute view date to reset, which will be...
    // - the last item of the selected dates or defaultViewDate if no selection
    // - limitted to minDate or maxDate if it exceeds the range
    function computeResetViewDate(datepicker) {
      const {dates, config} = datepicker;
      const viewDate = dates.length > 0 ? lastItemOf(dates) : config.defaultViewDate;
      return limitToRange(viewDate, config.minDate, config.maxDate);
    }

    // Change current view's view date
    function setViewDate(picker, newDate) {
      const oldViewDate = new Date(picker.viewDate);
      const newViewDate = new Date(newDate);
      const {id, year, first, last} = picker.currentView;
      const viewYear = newViewDate.getFullYear();

      picker.viewDate = newDate;
      if (viewYear !== oldViewDate.getFullYear()) {
        triggerDatepickerEvent(picker.datepicker, 'changeYear');
      }
      if (newViewDate.getMonth() !== oldViewDate.getMonth()) {
        triggerDatepickerEvent(picker.datepicker, 'changeMonth');
      }

      // return whether the new date is in different period on time from the one
      // displayed in the current view
      // when true, the view needs to be re-rendered on the next UI refresh.
      switch (id) {
        case 0:
          return newDate < first || newDate > last;
        case 1:
          return viewYear !== year;
        default:
          return viewYear < first || viewYear > last;
      }
    }

    function getTextDirection(el) {
      return window.getComputedStyle(el).direction;
    }

    // Class representing the picker UI
    class Picker {
      constructor(datepicker) {
        this.datepicker = datepicker;

        const template = pickerTemplate.replace(/%buttonClass%/g, datepicker.config.buttonClass);
        const element = this.element = parseHTML(template).firstChild;
        const [header, main, footer] = element.firstChild.children;
        const title = header.firstElementChild;
        const [prevBtn, viewSwitch, nextBtn] = header.lastElementChild.children;
        const [todayBtn, clearBtn] = footer.firstChild.children;
        const controls = {
          title,
          prevBtn,
          viewSwitch,
          nextBtn,
          todayBtn,
          clearBtn,
        };
        this.main = main;
        this.controls = controls;

        const elementClass = datepicker.inline ? 'inline' : 'dropdown';
        element.classList.add(`datepicker-${elementClass}`);

        processPickerOptions(this, datepicker.config);
        this.viewDate = computeResetViewDate(datepicker);

        // set up event listeners
        registerListeners(datepicker, [
          [element, 'click', onClickPicker.bind(null, datepicker)],
          [main, 'click', onClickView.bind(null, datepicker)],
          [controls.viewSwitch, 'click', onClickViewSwitch.bind(null, datepicker)],
          [controls.prevBtn, 'click', onClickPrevBtn.bind(null, datepicker)],
          [controls.nextBtn, 'click', onClickNextBtn.bind(null, datepicker)],
          [controls.todayBtn, 'click', onClickTodayBtn.bind(null, datepicker)],
          [controls.clearBtn, 'click', onClickClearBtn.bind(null, datepicker)],
        ]);

        // set up views
        this.views = [
          new DaysView(this),
          new MonthsView(this),
          new YearsView(this, {id: 2, name: 'years', cellClass: 'year', step: 1}),
          new YearsView(this, {id: 3, name: 'decades', cellClass: 'decade', step: 10}),
        ];
        this.currentView = this.views[datepicker.config.startView];

        this.currentView.render();
        this.main.appendChild(this.currentView.element);
        datepicker.config.container.appendChild(this.element);
      }

      setOptions(options) {
        processPickerOptions(this, options);
        this.views.forEach((view) => {
          view.init(options, false);
        });
        this.currentView.render();
      }

      detach(){
        this.datepicker.config.container.removeChild(this.element);
      }

      show() {
        if (this.active) {
          return;
        }
        this.element.classList.add('active');
        this.active = true;

        const datepicker = this.datepicker;
        if (!datepicker.inline) {
          // ensure picker's direction matches input's
          const inputDirection = getTextDirection(datepicker.inputField);
          if (inputDirection !== getTextDirection(datepicker.config.container)) {
            this.element.dir = inputDirection;
          } else if (this.element.dir) {
            this.element.removeAttribute('dir');
          }

          this.place();
          if (datepicker.config.disableTouchKeyboard) {
            datepicker.inputField.blur();
          }
        }
        triggerDatepickerEvent(datepicker, 'show');
      }

      hide() {
        if (!this.active) {
          return;
        }
        this.datepicker.exitEditMode();
        this.element.classList.remove('active');
        this.active = false;
        triggerDatepickerEvent(this.datepicker, 'hide');
      }

      place() {
        const {classList, style} = this.element;
        const {config, inputField} = this.datepicker;
        const container = config.container;
        const {
          width: calendarWidth,
          height: calendarHeight,
        } = this.element.getBoundingClientRect();
        const {
          left: containerLeft,
          top: containerTop,
          width: containerWidth,
        } = container.getBoundingClientRect();
        const {
          left: inputLeft,
          top: inputTop,
          width: inputWidth,
          height: inputHeight
        } = inputField.getBoundingClientRect();
        let {x: orientX, y: orientY} = config.orientation;
        let scrollTop;
        let left;
        let top;

        if (container === document.body) {
          scrollTop = window.scrollY;
          left = inputLeft + window.scrollX;
          top = inputTop + scrollTop;
        } else {
          scrollTop = container.scrollTop;
          left = inputLeft - containerLeft;
          top = inputTop - containerTop + scrollTop;
        }

        if (orientX === 'auto') {
          if (left < 0) {
            // align to the left and move into visible area if input's left edge < window's
            orientX = 'left';
            left = 10;
          } else if (left + calendarWidth > containerWidth) {
            // align to the right if canlendar's right edge > container's
            orientX = 'right';
          } else {
            orientX = getTextDirection(inputField) === 'rtl' ? 'right' : 'left';
          }
        }
        if (orientX === 'right') {
          left -= calendarWidth - inputWidth;
        }

        if (orientY === 'auto') {
          orientY = top - calendarHeight < scrollTop ? 'bottom' : 'top';
        }
        if (orientY === 'top') {
          top -= calendarHeight;
        } else {
          top += inputHeight;
        }

        classList.remove(
          'datepicker-orient-top',
          'datepicker-orient-bottom',
          'datepicker-orient-right',
          'datepicker-orient-left'
        );
        classList.add(`datepicker-orient-${orientY}`, `datepicker-orient-${orientX}`);

        style.top = top ? `${top}px` : top;
        style.left = left ? `${left}px` : left;
      }

      setViewSwitchLabel(labelText) {
        this.controls.viewSwitch.textContent = labelText;
      }

      setPrevBtnDisabled(disabled) {
        this.controls.prevBtn.disabled = disabled;
      }

      setNextBtnDisabled(disabled) {
        this.controls.nextBtn.disabled = disabled;
      }

      changeView(viewId) {
        const oldView = this.currentView;
        const newView =  this.views[viewId];
        if (newView.id !== oldView.id) {
          this.currentView = newView;
          this._renderMethod = 'render';
          triggerDatepickerEvent(this.datepicker, 'changeView');
          this.main.replaceChild(newView.element, oldView.element);
        }
        return this;
      }

      // Change the focused date (view date)
      changeFocus(newViewDate) {
        this._renderMethod = setViewDate(this, newViewDate) ? 'render' : 'refreshFocus';
        this.views.forEach((view) => {
          view.updateFocus();
        });
        return this;
      }

      // Apply the change of the selected dates
      update() {
        const newViewDate = computeResetViewDate(this.datepicker);
        this._renderMethod = setViewDate(this, newViewDate) ? 'render' : 'refresh';
        this.views.forEach((view) => {
          view.updateFocus();
          view.updateSelection();
        });
        return this;
      }

      // Refresh the picker UI
      render() {
        const renderMethod = this._renderMethod || 'render';
        delete this._renderMethod;

        this.currentView[renderMethod]();
      }
    }

    // Find the closest date that doesn't meet the condition for unavailable date
    // Returns undefined if no available date is found
    // addFn: function to calculate the next date
    //   - args: time value, amount
    // increase: amount to pass to addFn
    // testFn: function to test the unavailablity of the date
    //   - args: time value; retun: true if unavailable
    function findNextAvailableOne(date, addFn, increase, testFn, min, max) {
      if (!isInRange(date, min, max)) {
        return;
      }
      if (testFn(date)) {
        const newDate = addFn(date, increase);
        return findNextAvailableOne(newDate, addFn, increase, testFn, min, max);
      }
      return date;
    }

    // direction: -1 (left/up), 1 (right/down)
    // vertical: true for up/down, false for left/right
    function moveByArrowKey(datepicker, ev, direction, vertical) {
      const currentView = datepicker.picker.currentView;
      const step = currentView.step || 1;
      let viewDate = datepicker.picker.viewDate;
      let addFn;
      let testFn;
      switch (currentView.id) {
        case 0:
          if (vertical) {
            viewDate = addDays(viewDate, direction * 7);
          } else if (ev.ctrlKey || ev.metaKey) {
            viewDate = addYears(viewDate, direction);
          } else {
            viewDate = addDays(viewDate, direction);
          }
          addFn = addDays;
          testFn = (date) => currentView.disabled.includes(date);
          break;
        case 1:
          viewDate = addMonths(viewDate, vertical ? direction * 4 : direction);
          addFn = addMonths;
          testFn = (date) => {
            const dt = new Date(date);
            const {year, disabled} = currentView;
            return dt.getFullYear() === year && disabled.includes(dt.getMonth());
          };
          break;
        default:
          viewDate = addYears(viewDate, direction * (vertical ? 4 : 1) * step);
          addFn = addYears;
          testFn = date => currentView.disabled.includes(startOfYearPeriod(date, step));
      }
      viewDate = findNextAvailableOne(
        viewDate,
        addFn,
        direction < 0 ? -step : step,
        testFn,
        currentView.minDate,
        currentView.maxDate
      );
      if (viewDate !== undefined) {
        datepicker.picker.changeFocus(viewDate).render();
      }
    }

    function onKeydown(datepicker, ev) {
      if (ev.key === 'Tab') {
        datepicker.refresh('input');
        datepicker.hide();
        return;
      }

      const viewId = datepicker.picker.currentView.id;
      if (!datepicker.picker.active) {
        switch (ev.key) {
          case 'ArrowDown':
          case 'Escape':
            datepicker.picker.show();
            break;
          case 'Enter':
            datepicker.update();
            break;
          default:
            return;
        }
      } else if (datepicker.editMode) {
        switch (ev.key) {
          case 'Escape':
            datepicker.exitEditMode();
            break;
          case 'Enter':
            datepicker.exitEditMode({update: true, autohide: datepicker.config.autohide});
            break;
          default:
            return;
        }
      } else {
        switch (ev.key) {
          case 'Escape':
            if (ev.shiftKey) {
              datepicker.enterEditMode();
            } else {
              datepicker.picker.hide();
            }
            break;
          case 'ArrowLeft':
            if (ev.ctrlKey || ev.metaKey) {
              goToPrevOrNext(datepicker, -1);
            } else {
              moveByArrowKey(datepicker, ev, -1, false);
            }
            break;
          case 'ArrowRight':
            if (ev.ctrlKey || ev.metaKey) {
              goToPrevOrNext(datepicker, 1);
            } else {
              moveByArrowKey(datepicker, ev, 1, false);
            }
            break;
          case 'ArrowUp':
            if (ev.ctrlKey || ev.metaKey) {
              switchView(datepicker);
            } else {
              moveByArrowKey(datepicker, ev, -1, true);
            }
            break;
          case 'ArrowDown':
            moveByArrowKey(datepicker, ev, 1, true);
            break;
          case 'Enter':
            if (viewId === 0) {
              datepicker.setDate(datepicker.picker.viewDate);
            } else {
              datepicker.picker.changeView(viewId - 1).render();
            }
            break;
          case 'Backspace':
          case 'Delete':
            datepicker.enterEditMode();
            return;
          default:
            if (ev.key.length === 1 && !ev.ctrlKey && !ev.metaKey) {
              datepicker.enterEditMode();
            }
            return;
        }
      }
      ev.preventDefault();
      ev.stopPropagation();
    }

    function onFocus(datepicker) {
      if (datepicker.config.showOnFocus) {
        datepicker.show();
      }
    }

    // for the prevention for entering edit mode while getting focus on click
    function onMousedown(datepicker, ev) {
      const el = ev.target;
      if (datepicker.picker.active) {
        el._clicking = setTimeout(() => {
          delete el._clicking;
        }, 2000);
      }
    }

    function onClickInput(datepicker, ev) {
      const el = ev.target;
      if (!el._clicking) {
        return;
      }
      clearTimeout(el._clicking);
      delete el._clicking;

      datepicker.enterEditMode();
    }

    function onPaste(datepicker, ev) {
      if (ev.clipboardData.types.includes('text/plain')) {
        datepicker.enterEditMode();
      }
    }

    // for the `document` to delegate the events from outside the picker/input field
    function onClickOutside(datepicker, ev) {
      const element = datepicker.element;
      const pickerElem = datepicker.picker.element;

      if (findElementInEventPath(ev, el => el === element || el === pickerElem)) {
        return;
      }
      datepicker.refresh('input');
      datepicker.hide();
    }

    function stringifyDates(dates, config) {
      return dates
        .map(dt => formatDate(dt, config.format, config.locale))
        .join(config.dateDelimiter);
    }

    // parse input dates and create an array of time values for selection
    // returns undefined if there are no valid dates in inputDates
    // when origDates (current selection) is passed, the function works to mix
    // the input dates into the current selection
    function processInputDates(inputDates, config, origDates = undefined) {
      if (inputDates.length === 0) {
        // empty input is considered valid unless origiDates is passed
        return origDates ? undefined : [];
      }

      let newDates = inputDates.reduce((dates, dt) => {
        const date = parseDate(dt, config.format, config.locale);
        if (
          date !== undefined
          && isInRange(date, config.minDate, config.maxDate)
          && !dates.includes(date)
          && !config.datesDisabled.includes(date)
          && !config.daysOfWeekDisabled.includes(new Date(date).getDay())
        ) {
          dates.push(date);
        }
        return dates;
      }, []);
      if (newDates.length === 0) {
        return;
      }
      if (origDates && config.multidate) {
        // get the synmetric difference between origDates and newDates
        newDates = newDates.reduce((dates, date) => {
          if (!origDates.includes(date)) {
            dates.push(date);
          }
          return dates;
        }, origDates.filter(date => !newDates.includes(date)));
      }
      // do length check always because user can input multiple dates regardless of the mode
      return config.maxNumberOfDates && newDates.length > config.maxNumberOfDates
        ? newDates.slice(config.maxNumberOfDates * -1)
        : newDates;
    }

    /**
     * Class representing a date picker
     */
    class Datepicker {
      /**
       * Create a date picker
       * @param  {Element} element - element to bind a date picker
       * @param  {Object} [options] - config options
       * @param  {DateRangePicker} [rangepicker] - DateRangePicker instance the
       * date picker belongs to. Use this only when creating date picker as a part
       * of date range picker
       */
      constructor(element, options = {}, rangepicker = undefined) {
        element.datepicker = this;
        this.element = element;

        // set up config
        const config = this.config = Object.assign({
          buttonClass: (options.buttonClass && String(options.buttonClass)) || 'button',
          container: document.body,
          defaultViewDate: today(),
          maxDate: undefined,
          minDate: undefined,
        }, processOptions(defaultOptions, this));
        this._options = options;
        Object.assign(config, processOptions(options, this));

        // configure by type
        const inline = this.inline = element.tagName !== 'INPUT';
        let inputField;
        let initialDates;

        if (inline) {
          config.container = element;
          initialDates = stringToArray(element.dataset.date, config.dateDelimiter);
          delete element.dataset.date;
        } else {
          const container = options.container ? document.querySelector(options.container) : null;
          if (container) {
            config.container = container;
          }
          inputField = this.inputField = element;
          inputField.classList.add('datepicker-input');
          initialDates = stringToArray(inputField.value, config.dateDelimiter);
        }
        // set initial value
        this.dates = processInputDates(initialDates, config) || [];

        if (rangepicker && rangepicker.constructor.name === 'DateRangePicker') {
          this.rangepicker = rangepicker;
          // add getter for range
          Object.defineProperty(this, 'range', {
            get() {
              return this.rangepicker.dates;
            },
          });
        }

        const picker = this.picker = new Picker(this);

        if (inline) {
          this.show();
        } else {
          // set up event listeners in other modes
          const onMousedownDocument = onClickOutside.bind(null, this);
          const listeners = [
            [inputField, 'keydown', onKeydown.bind(null, this)],
            [inputField, 'focus', onFocus.bind(null, this)],
            [inputField, 'mousedown', onMousedown.bind(null, this)],
            [inputField, 'click', onClickInput.bind(null, this)],
            [inputField, 'paste', onPaste.bind(null, this)],
            [document, 'mousedown', onMousedownDocument],
            [document, 'touchstart', onMousedownDocument],
            [window, 'resize', picker.place.bind(picker)]
          ];
          registerListeners(this, listeners);
        }
      }

      /**
       * Format Date object or time value in given format and language
       * @param  {Date|Number} date - date or time value to format
       * @param  {String|Object} format - format string or object that contains
       * toDisplay() custom formatter, whose signature is
       * - args:
       *   - date: {Date} - Date instance of the date passed to the method
       *   - format: {Object} - the format object passed to the method
       *   - locale: {Object} - locale for the language specified by `lang`
       * - return:
       *     {String} formatted date
       * @param  {String} [lang=en] - language code for the locale to use
       * @return {String} formatted date
       */
      static formatDate(date, format, lang) {
        return formatDate(date, format, lang && locales[lang] || locales.en);
      }

      /**
       * Pasre date string
       * @param  {String|Date|Number} dateStr - date string, Date object or time
       * value to parse
       * @param  {String|Object} format - format string or object that contains
       * toValue() custom parser, whose signature is
       * - args:
       *   - dateStr: {String|Date|Number} - the dateStr passed to the method
       *   - format: {Object} - the format object passed to the method
       *   - locale: {Object} - locale for the language specified by `lang`
       * - return:
       *     {Date|Number} parsed date or its time value
       * @param  {String} [lang=en] - language code for the locale to use
       * @return {Number} time value of parsed date
       */
      static parseDate(dateStr, format, lang) {
        return parseDate(dateStr, format, lang && locales[lang] || locales.en);
      }

      /**
       * @type {Object} - Installed locales in `[languageCode]: localeObject` format
       * en`:_English (US)_ is pre-installed.
       */
      static get locales() {
        return locales;
      }

      /**
       * @type {Boolean} - Whether the picker element is shown. `true` whne shown
       */
      get active() {
        return !!(this.picker && this.picker.active);
      }

      /**
       * Set new values to the config options
       * @param {Object} options - config options to update
       */
      setOptions(options) {
        const picker = this.picker;
        const newOptions = processOptions(options, this);
        Object.assign(this._options, options);
        Object.assign(this.config, newOptions);
        picker.setOptions(newOptions);

        const currentViewId = picker.currentView.id;
        if (newOptions.maxView < currentViewId) {
          picker.changeView(newOptions.maxView);
        } else if (
          newOptions.startView !== undefined
          && !picker.active
          && newOptions.startView !== currentViewId
        ) {
          picker.changeView(newOptions.startView);
        }

        this.refresh();
      }

      /**
       * Show the picker element
       */
      show() {
        if (this.inputField && this.inputField.disabled) {
          return;
        }
        this.picker.show();
      }

      /**
       * Hide the picker element
       * Not avilable on inline picker
       */
      hide() {
        if (this.inline) {
          return;
        }
        this.picker.hide();
        this.picker.update().changeView(this.config.startView).render();
      }

      /**
       * Destroy the Datepicker instance
       * @return {Detepicker} - the instance destroyed
       */
      destroy() {
        this.hide();
        unregisterListeners(this);
        this.picker.detach();
        if (!this.inline) {
          this.inputField.classList.remove('datepicker-input');
        }
        delete this.element.datepicker;
        return this;
      }

      /**
       * Get the selected date(s)
       *
       * The method returns a Date object of selected date by default, and returns
       * an array of selected dates in multidate mode. If format string is passed,
       * it returns date string(s) formatted in given format.
       *
       * @param  {String} [format] - Format string to stringify the date(s)
       * @return {Date|String|Date[]|String[]} - selected date(s), or if none is
       * selected, empty array in multidate mode and untitled in sigledate mode
       */
      getDate(format = undefined) {
        const callback = format
          ? date => formatDate(date, format, this.config.locale)
          : date => new Date(date);

        if (this.config.multidate) {
          return this.dates.map(callback);
        }
        if (this.dates.length > 0) {
          return callback(this.dates[0]);
        }
      }

      /**
       * Set selected date(s)
       *
       * In multidate mode, you can pass multiple dates as a series of arguments
       * or an array. (Since each date is parsed individually, the type of the
       * dates doesn't have to be the same.)
       * The given dates are used to toggle the select status of each date. The
       * number of selected dates is kept from exceeding the length set to
       * maxNumberOfDates.
       *
       * With clear: true option, the method can be used to clear the selection
       * and to replace the selection instead of toggling in multidate mode.
       * If the option is passed with no date arguments or an empty dates array,
       * it works as "clear" (clear the selection then set nothing), and if the
       * option is passed with new dates to select, it works as "replace" (clear
       * the selection then set the given dates)
       *
       * When render: false option is used, the method omits re-rendering the
       * picker element. In this case, you need to call refresh() method later in
       * order for the picker element to reflect the changes. The input field is
       * refreshed always regardless of this option.
       *
       * When invalid (unparsable, repeated, disabled or out-of-range) dates are
       * passed, the method ignores them and applies only valid ones. In the case
       * that all the given dates are invalid, which is distiguished from passing
       * no dates, the method considers it as an error and leaves the selection
       * untouched.
       *
       * @param {...(Date|Number|String)|Array} [dates] - Date strings, Date
       * objects, time values or mix of those for new selection
       * @param {Object} [options] - function options
       * - clear: {boolean} - Whether to clear the existing selection
       *     defualt: false
       * - render: {boolean} - Whether to re-render the picker element
       *     default: true
       * - autohide: {boolean} - Whether to hide the picker element after re-render
       *     Ignored when used with render: false
       *     default: config.autohide
       */
      setDate(...args) {
        const dates = [...args];
        const opts = {clear: false, render: true, autohide: this.config.autohide};
        const lastArg = lastItemOf(args);
        if (
          typeof lastArg === 'object'
          && !Array.isArray(lastArg)
          && !(lastArg instanceof Date)
        ) {
          Object.assign(opts, dates.pop());
        }

        const inputDates = Array.isArray(dates[0]) ? dates[0] : dates;
        const origDates = opts.clear ? undefined : this.dates;
        const newDates = processInputDates(inputDates, this.config, origDates);
        if (!newDates) {
          return;
        }
        if (newDates.toString() !== this.dates.toString()) {
          this.dates = newDates;
          if (opts.render) {
            this.picker.update();
            this.refresh();
          } else {
            this.refresh('input');
          }
          triggerDatepickerEvent(this, 'changeDate');
        } else {
          this.refresh('input');
        }
        if (opts.render && opts.autohide) {
          this.hide();
        }
      }

      /**
       * Update the selected date(s) with input field's value
       * Not avilable on inline picker
       *
       * The input field will be refreshed with properly formatted date string.
       *
       * @param  {Object} [options] - function options
       * - autohide: {boolean} - whether to hide the picker element after refresh
       *     default: false
       */
      update(options = undefined) {
        if (this.inline) {
          return;
        }

        const opts = Object.assign({autohide: false}, options);
        const inputDates = stringToArray(this.inputField.value, this.config.dateDelimiter);
        const newDates = processInputDates(inputDates, this.config);
        if (!newDates) {
          return;
        }
        if (newDates.toString() !== this.dates.toString()) {
          this.dates = newDates;
          this.picker.update();
          this.refresh();
          triggerDatepickerEvent(this, 'changeDate');
        } else {
          this.refresh('input');
        }
        if (opts.autohide) {
          this.hide();
        }
      }

      /**
       * Refresh the picker element and the associated input field
       * @param {String} [target] - target item when refreshing one item only
       * 'picker' or 'input'
       */
      refresh(target = undefined) {
        if (target !== 'input') {
          this.picker.render();
        }
        if (!this.inline && target !== 'picker') {
          this.inputField.value = stringifyDates(this.dates, this.config);
        }
      }

      /**
       * Enter edit mode
       * Not avilable on inline picker or when the picker element is hidden
       */
      enterEditMode() {
        if (this.inline || !this.picker.active || this.editMode) {
          return;
        }
        this.editMode = true;
        this.inputField.classList.add('in-edit');
      }

      /**
       * Exit from edit mode
       * Not avilable on inline picker
       * @param  {Object} [options] - function options
       * - update: {boolean} - whether to call update() after exiting
       *     If false, input field is revert to the existing selection
       *     default: false
       */
      exitEditMode(options = undefined) {
        if (this.inline || !this.editMode) {
          return;
        }
        const opts = Object.assign({update: false}, options);
        delete this.editMode;
        this.inputField.classList.remove('in-edit');
        if (opts.update) {
          this.update(opts);
        } else {
          this.inputField.value = stringifyDates(this.dates, this.config);
        }
      }
    }

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
    // XXX it might be nice, if this util was in tobago-date.ts, but in that case there are problems
    // XXX with Jest (UnitTesting)
    class DateUtils {
        /*
        Get the pattern from the "Java world",
        see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/SimpleDateFormat.html
        and convert it to 'vanillajs-datepicker', see https://mymth.github.io/vanillajs-datepicker/#/date-string+format
        Attention: Not every pattern char is supported.
        */
        static convertPattern(originalPattern) {
            let pattern;
            if (!originalPattern || originalPattern.length > 100) {
                console.warn("Pattern not supported: " + originalPattern);
                pattern = "";
            }
            else {
                pattern = originalPattern;
            }
            let analyzedPattern = "";
            let nextSegment = "";
            let escMode = false;
            for (let i = 0; i < pattern.length; i++) {
                const currentChar = pattern.charAt(i);
                if (currentChar == "'" && escMode == false) {
                    escMode = true;
                    analyzedPattern += DateUtils.convertPatternPart(nextSegment);
                    nextSegment = "";
                }
                else if (currentChar == "'" && pattern.charAt(i + 1) == "'") {
                    if (escMode) {
                        nextSegment += "\\";
                    }
                    nextSegment += "'";
                    i++;
                }
                else if (currentChar == "'" && escMode == true) {
                    escMode = false;
                    analyzedPattern += nextSegment;
                    nextSegment = "";
                }
                else {
                    if (escMode) {
                        nextSegment += "\\";
                    }
                    nextSegment += currentChar;
                }
            }
            if (nextSegment != "") {
                if (escMode) {
                    analyzedPattern += nextSegment;
                }
                else {
                    analyzedPattern += this.convertPatternPart(nextSegment);
                }
            }
            return analyzedPattern;
        }
        static convertPatternPart(originalPattern) {
            let pattern = originalPattern;
            if (pattern.search("G") > -1 || pattern.search("W") > -1 || pattern.search("F") > -1
                || pattern.search("K") > -1 || pattern.search("z") > -1 || pattern.search("X") > -1) {
                console.warn("Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported: " + pattern);
                pattern = "";
            }
            if (pattern.search("y") > -1) {
                pattern = pattern.replace(/y/g, "y");
            }
            if (pattern.search("M") > -1) {
                pattern = pattern.replace(/M/g, "m");
            }
            if (pattern.search("d") > -1) {
                pattern = pattern.replace(/dd+/g, "dd");
                pattern = pattern.replace(/\bd\b/g, "d");
            }
            return pattern;
        }
    }

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
    class Config {
        static set(key, value) {
            this.map.set(key, value);
        }
        static get(key) {
            const value = this.map.get(key);
            if (value) {
                return value;
            }
            else {
                console.warn("Config.get(" + key + ") = undefined");
                return 0;
            }
        }
    }
    Config.map = new Map();

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
    // XXX issue: if a ajax call is scheduled on the same element, the animation arrow will stacking and not desapearing.
    // XXX issue: "error" is not implemented correctly
    // see http://localhost:8080/demo-5-snapshot/content/30-concept/50-partial/Partial_Ajax.xhtml to use this feature
    // XXX todo: check full page transitions
    class Overlay {
        constructor(element, ajax = false, error = false, waitOverlayDelay) {
            /**
             * Is this overlay for an AJAX request, or an normal submit?
             * We need this information, because AJAX need to clone the animated image, but for a normal submit
             * we must not clone it, because the animation stops in some browsers.
             */
            this.ajax = true;
            /**
             * This boolean indicates, if the overlay is "error" or "wait".
             */
            this.error = false;
            /**
             * The delay for the wait overlay. If not set the default delay is read from Tobago.Config.
             */
            this.waitOverlayDelay = 0;
            this.element = element;
            this.ajax = ajax;
            this.error = error;
            this.waitOverlayDelay = waitOverlayDelay
                ? waitOverlayDelay
                : Config.get(this.ajax ? "Ajax.waitOverlayDelay" : "Tobago.waitOverlayDelay");
            // create the overlay
            this.overlay = document.createElement("div");
            this.overlay.classList.add("tobago-page-overlay");
            this.overlay.classList.add(this.error ? "tobago-page-overlay-markup-error" : "tobago-page-overlay-markup-wait");
            let left = "0";
            let top = "0";
            if (this.element.matches("body")) {
                this.overlay.style.position = "fixed";
                this.overlay.style.zIndex = "1500"; // greater than the bootstrap navbar
            }
            else {
                const rect = this.element.getBoundingClientRect();
                left = (rect.left + document.body.scrollLeft) + "px";
                top = (rect.top + document.body.scrollTop) + "px";
                this.overlay.style.width = this.element.offsetWidth + "px";
                this.overlay.style.height = this.element.offsetHeight + "px";
                // tbd: is this still needed?       this.overlay.style.position= "absolute"
                // XXX is set via class, but seams to be overridden in IE11?
            }
            document.getElementsByTagName("body")[0].append(this.overlay);
            let wait = document.createElement("div");
            wait.classList.add("tobago-page-overlayCenter");
            this.overlay.append(wait);
            let image = document.createElement("i");
            if (this.error) {
                image.classList.add("fa", "fa-flash", "fa-3x");
                wait.classList.add("alert-danger");
            }
            else {
                image.classList.add("fa", "fa-refresh", "fa-3x", "fa-spin");
                image.style.opacity = "0.4";
            }
            wait.append(image);
            wait.style.display = ""; //XXX ?
            this.overlay.style.backgroundColor = Page.page(this.element).style.backgroundColor;
            this.overlay.style.left = left;
            this.overlay.style.top = top;
            setTimeout(() => {
                this.overlay.classList.add("tobago-page-overlay-timeout");
            }, this.waitOverlayDelay);
            Overlay.overlayMap.set(element.id, this);
            console.debug("----> set overlay " + element.id);
        }
        static destroy(id) {
            console.debug("----> get overlay " + id);
            const overlay = Overlay.overlayMap.get(id);
            if (overlay) {
                overlay.overlay.remove();
                Overlay.overlayMap.delete(id);
            }
            else {
                console.warn("Overlay not found for id='" + id + "'");
            }
        }
    }
    Overlay.overlayMap = new Map();
    Config.set("Tobago.waitOverlayDelay", 1000);
    Config.set("Ajax.waitOverlayDelay", 1000);

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
    const Event$2 = {
        HIDE: "hide.bs.modal",
        HIDE_PREVENTED: "hidePrevented.bs.modal",
        HIDDEN: "hidden.bs.modal",
        SHOW: "show.bs.modal",
        SHOWN: "shown.bs.modal",
        FOCUSIN: "focusin.bs.modal",
        RESIZE: "resize.bs.modal",
        CLICK_DISMISS: "click.dismiss.bs.modal",
        KEYDOWN_DISMISS: "keydown.dismiss.bs.modal",
        MOUSEUP_DISMISS: "mouseup.dismiss.bs.modal",
        MOUSEDOWN_DISMISS: "mousedown.dismiss.bs.modal",
        CLICK_DATA_API: "click.bs.modal.data-api",
        TRANSITION_END: "bsTransitionEnd"
    };
    const ClassName = {
        SCROLLABLE: "modal-dialog-scrollable",
        SCROLLBAR_MEASURER: "modal-scrollbar-measure",
        BACKDROP: "modal-backdrop",
        OPEN: "modal-open",
        FADE: "fade",
        SHOW: "show",
        STATIC: "modal-static"
    };
    const Selector = {
        DIALOG: ".modal-dialog",
        MODAL_BODY: ".modal-body",
        DATA_TOGGLE: "[data-toggle='modal']",
        DATA_DISMISS: "[data-dismiss='modal']",
        FIXED_CONTENT: ".fixed-top, .fixed-bottom, .is-fixed, .sticky-top",
        STICKY_CONTENT: ".sticky-top"
    };
    class Popup extends HTMLElement {
        constructor() {
            super();
            this.emulateTransitionEndCalled = false;
            this._dialog = this.querySelector(Selector.DIALOG);
            this._backdrop = null;
            this._isShown = false;
            this._isBodyOverflowing = false;
            this._ignoreBackdropClick = false;
            // this._isTransitioning = false;
            this._scrollbarWidth = 0;
        }
        connectedCallback() {
            const hidden = Collapse.findHidden(this);
            if (hidden.value === "false") {
                // XXX hack: this is needed for popups open by AJAX.
                // XXX currently the DOM replacement done by Tobago doesn't remove the modal-backdrop
                for (const backdrop of document.querySelectorAll(".modal-backdrop")) {
                    backdrop.parentNode.removeChild(backdrop);
                }
                this.show(); // inits and opens the popup
            }
            else {
                this.hide(); // inits and hides the popup
            }
        }
        // Public
        // toggle(relatedTarget) {
        //   return this._isShown ? this.hide() : this.show(relatedTarget)
        // }
        show() {
            if (this._isShown /*|| this._isTransitioning*/) {
                return;
            }
            /*
                if (this.classList.contains(ClassName.FADE)) {
                  this._isTransitioning = true;
                }
            */
            const showEvent = new CustomEvent(Event$2.SHOW);
            this.dispatchEvent(showEvent);
            if (this._isShown || showEvent.defaultPrevented) {
                return;
            }
            this._isShown = true;
            this._checkScrollbar();
            this._setScrollbar();
            // this._adjustDialog();
            //
            // this._setEscapeEvent();
            // this._setResizeEvent();
            this._clickDismiss = (event) => { this.hide( /*event*/); };
            if (this.classList.contains(Selector.DATA_DISMISS)) {
                this.addEventListener(Event$2.CLICK_DISMISS, this._clickDismiss);
            }
            this._dialog.addEventListener(Event$2.MOUSEDOWN_DISMISS, () => {
                // $(this._element).one(Event.MOUSEUP_DISMISS, (event) => {  // XXX not implemented yet
                //   if ($(event.target).is(this._element)) { // XXX not implemented yet
                this._ignoreBackdropClick = true;
                // }
                // })
            });
            // this._showBackdrop(() => this._showElement(relatedTarget))
            this._showBackdrop(() => this._showElement());
        }
        hide( /*event*/) {
            // if (event) {
            //   event.preventDefault()
            // }
            if (!this._isShown /* || this._isTransitioning*/) {
                return;
            }
            const hideEvent = new CustomEvent(Event$2.HIDE);
            this.dispatchEvent(hideEvent);
            if (!this._isShown || hideEvent.defaultPrevented) {
                return;
            }
            this._isShown = false;
            // const transition = this.classList.contains(ClassName.FADE);
            // if (transition) {
            //   this._isTransitioning = true
            // }
            // this._setEscapeEvent();
            // this._setResizeEvent();
            // $(document).off(Event.FOCUSIN);
            this.classList.remove(ClassName.SHOW);
            this.removeEventListener(Event$2.CLICK_DISMISS, this._clickDismiss);
            // $(this._dialog).off(Event.MOUSEDOWN_DISMISS);
            // if (transition) {
            //   const transitionDuration = this.getTransitionDuration();
            //
            //   this.addEventListener(Popup.TRANSITION_END, (event:Event) => this._hideModal(event));
            //   this.emulateTransitionEnd(transitionDuration)
            // } else {
            this._hideModal();
            // }
        }
        // dispose() {
        //   [window, this._element, this._dialog]
        //       .forEach((htmlElement) => $(htmlElement).off(`.bs.modal`));
        /**
         * `document` has 2 events `Event.FOCUSIN` and `Event.CLICK_DATA_API`
         * Do not move `document` in `htmlElements` array
         * It will remove `Event.CLICK_DATA_API` event that should remain
         */
        // $(document).off(Event.FOCUSIN);
        //
        // $.removeData(this._element, 'bs.modal');
        //
        // this._config = null;
        // this._element = null;
        // this._dialog = null;
        // this._backdrop = null;
        // this._isShown = null;
        // this._isBodyOverflowing = null;
        // this._ignoreBackdropClick = null;
        // this._isTransitioning = null;
        // this._scrollbarWidth = null;
        // }
        // handleUpdate() {
        //   this._adjustDialog();
        // }
        // Private
        /*_getConfig(config) {
          config = {
            ...Default,
            ...config
          }
          Util.typeCheckConfig(NAME, config, DefaultType)
          return config
        }*/
        /*
          _triggerBackdropTransition() {
            if (this._config.backdrop === 'static') {
              const hideEventPrevented = $.Event(Event.HIDE_PREVENTED);
        
              $(this._element).trigger(hideEventPrevented);
              if (hideEventPrevented.defaultPrevented) {
                return;
              }
        
              this._element.classList.add(ClassName.STATIC);
        
              const modalTransitionDuration = DomUtils.getTransitionTime(this._element)
        
              $(this._element).one(Util.TRANSITION_END, () => {
                this._element.classList.remove(ClassName.STATIC)
              })
                  .emulateTransitionEnd(modalTransitionDuration);
              this._element.focus();
            } else {
              this.hide();
            }
          }
        */
        _showElement( /*relatedTarget*/) {
            // const transition = $(this._element).hasClass(ClassName.FADE)
            const modalBody = this._dialog ? this._dialog.querySelector(Selector.MODAL_BODY) : null;
            if (!this.parentNode ||
                this.parentNode.nodeType !== Node.ELEMENT_NODE) {
                // Don't move modal's DOM position
                document.body.appendChild(this);
            }
            this.style.display = "block";
            this.removeAttribute("aria-hidden");
            this.setAttribute("aria-modal", "true");
            if (this._dialog.classList.contains(ClassName.SCROLLABLE) && modalBody) {
                modalBody.scrollTop = 0;
            }
            else {
                this.scrollTop = 0;
            }
            // if (transition) {
            //   Util.reflow(this._element)
            // }
            this.classList.add(ClassName.SHOW);
            // const shownEvent = $.Event(Event.SHOWN, {
            //   relatedTarget
            // })
            // const transitionComplete = () => {
            // if (this._config.focus) {
            //   this._element.focus()
            // }
            // this._isTransitioning = false
            // $(this._element).trigger(shownEvent)
            // };
            // if (transition) {
            //   const transitionDuration = DomUtils.getTransitionTime(this._dialog)
            //
            //   $(this._dialog)
            //       .one(Util.TRANSITION_END, transitionComplete)
            //       .emulateTransitionEnd(transitionDuration)
            // } else {
            //   transitionComplete()
            // }
            this.dispatchEvent(new CustomEvent(Event$2.SHOWN));
            const autofocusElement = this.querySelector("[autofocus]");
            if (autofocusElement) {
                autofocusElement.focus();
            }
        }
        /*
          _enforceFocus() {
            $(document)
                .off(Event.FOCUSIN) // Guard against infinite focus loop
                .on(Event.FOCUSIN, (event) => {
                  if (document !== event.target &&
                      this._element !== event.target &&
                      $(this._element).has(event.target).length === 0) {
                    this._element.focus()
                  }
                })
          }
        
          _setEscapeEvent() {
            if (this._isShown && this._config.keyboard) {
              $(this._element).on(Event.KEYDOWN_DISMISS, (event) => {
                if (event.which === ESCAPE_KEYCODE) {
                  this._triggerBackdropTransition()
                }
              })
            } else if (!this._isShown) {
              $(this._element).off(Event.KEYDOWN_DISMISS)
            }
          }
        
          _setResizeEvent() {
            if (this._isShown) {
              $(window).on(Event.RESIZE, (event) => this.handleUpdate(event))
            } else {
              $(window).off(Event.RESIZE)
            }
          }
        */
        _hideModal() {
            this.style.display = "none";
            this.setAttribute("aria-hidden", "true");
            this.removeAttribute("aria-modal");
            // this._isTransitioning = false;
            this._showBackdrop(() => {
                document.body.classList.remove(ClassName.OPEN);
                // this._resetAdjustments();
                this._resetScrollbar();
                // $(this._element).trigger(Event.HIDDEN)
            });
        }
        _removeBackdrop() {
            if (this._backdrop) {
                this._backdrop.remove();
                this._backdrop = null;
            }
        }
        _showBackdrop(callback) {
            const animate = this.classList.contains(ClassName.FADE) ? ClassName.FADE : "";
            if (this._isShown /*&& this._config.backdrop*/) {
                this._backdrop = document.createElement("div");
                this._backdrop.classList.add(ClassName.BACKDROP);
                if (animate) {
                    this._backdrop.classList.add(animate);
                }
                document.body.append(this._backdrop);
                /*$(this._element).on(Event.CLICK_DISMISS, (event) => {
                  if (this._ignoreBackdropClick) {
                    this._ignoreBackdropClick = false
                    return
                  }
                  if (event.target !== event.currentTarget) {
                    return
                  }
          
                  this._triggerBackdropTransition()
                })*/
                /*if (animate) {
                  Util.reflow(this._backdrop)
                }*/
                this._backdrop.classList.add(ClassName.SHOW);
                if (!callback) {
                    return;
                }
                if (!animate) {
                    callback();
                    return;
                }
                const backdropTransitionDuration = DomUtils.getTransitionTime(this._backdrop);
                this.addOnetimeEventListener(this._backdrop, Event$2.TRANSITION_END, callback);
                this.emulateTransitionEnd(this._backdrop, backdropTransitionDuration);
            }
            else if (!this._isShown && this._backdrop) {
                this._backdrop.classList.remove(ClassName.SHOW);
                const callbackRemove = () => {
                    this._removeBackdrop();
                    if (callback) {
                        callback();
                    }
                };
                if (this.classList.contains(ClassName.FADE)) {
                    const backdropTransitionDuration = DomUtils.getTransitionTime(this._backdrop);
                    this.addOnetimeEventListener(this._backdrop, Event$2.TRANSITION_END, callbackRemove);
                    this.emulateTransitionEnd(this._backdrop, backdropTransitionDuration);
                }
                else {
                    callbackRemove();
                }
            }
            else if (callback) {
                callback();
            }
        }
        // ----------------------------------------------------------------------
        // the following methods are used to handle overflowing modals
        // todo (fat): these should probably be refactored out of modal.js
        // ----------------------------------------------------------------------
        /*_adjustDialog() {
          const isModalOverflowing =
              this._element.scrollHeight > document.documentElement.clientHeight
      
          if (!this._isBodyOverflowing && isModalOverflowing) {
            this._element.style.paddingLeft = `${this._scrollbarWidth}px`
          }
      
          if (this._isBodyOverflowing && !isModalOverflowing) {
            this._element.style.paddingRight = `${this._scrollbarWidth}px`
          }
        }*/
        /*_resetAdjustments() {
          this._element.style.paddingLeft = ''
          this._element.style.paddingRight = ''
        }*/
        _checkScrollbar() {
            const rect = document.body.getBoundingClientRect();
            this._isBodyOverflowing = rect.left + rect.right < window.innerWidth;
            this._scrollbarWidth = this._getScrollbarWidth();
        }
        _setScrollbar() {
            if (this._isBodyOverflowing) {
                // Note: DOMNode.style.paddingRight returns the actual value or '' if not set
                //   while $(DOMNode).css('padding-right') returns the calculated value or 0 if not set
                const fixedContent = [].slice.call(document.querySelectorAll(Selector.FIXED_CONTENT));
                const stickyContent = [].slice.call(document.querySelectorAll(Selector.STICKY_CONTENT));
                // Adjust fixed content padding
                /*$(fixedContent).each((index, element) => {
                  const actualPadding = element.style.paddingRight
                  const calculatedPadding = $(element).css('padding-right')
                  $(element)
                      .data('padding-right', actualPadding)
                      .css('padding-right', `${parseFloat(calculatedPadding) + this._scrollbarWidth}px`)
                })*/
                // Adjust sticky content margin
                /*$(stickyContent).each((index, element) => {
                  const actualMargin = element.style.marginRight
                  const calculatedMargin = $(element).css('margin-right')
                  $(element)
                      .data('margin-right', actualMargin)
                      .css('margin-right', `${parseFloat(calculatedMargin) - this._scrollbarWidth}px`)
                })*/
                // Adjust body padding
                const actualPadding = document.body.style.paddingRight;
                // const calculatedPadding = $(document.body).css('padding-right');
                /*$(document.body)
                    .data('padding-right', actualPadding)
                    .css('padding-right', `${parseFloat(calculatedPadding) + this._scrollbarWidth}px`)*/
            }
            document.body.classList.add(ClassName.OPEN);
        }
        _resetScrollbar() {
            // Restore fixed content padding
            const fixedContent = [].slice.call(document.querySelectorAll(Selector.FIXED_CONTENT));
            /*$(fixedContent).each((index, element) => {
              const padding = $(element).data('padding-right')
              $(element).removeData('padding-right')
              element.style.paddingRight = padding ? padding : ''
            })*/
            // Restore sticky content
            const elements = [].slice.call(document.querySelectorAll(`${Selector.STICKY_CONTENT}`));
            /*$(elements).each((index, element) => {
              const margin = $(element).data('margin-right')
              if (typeof margin !== 'undefined') {
                $(element).css('margin-right', margin).removeData('margin-right')
              }
            })*/
            // Restore body padding
            /*const padding = $(document.body).data('padding-right')
            $(document.body).removeData('padding-right')
            document.body.style.paddingRight = padding ? padding : '';*/
        }
        _getScrollbarWidth() {
            const scrollDiv = document.createElement("div");
            scrollDiv.className = ClassName.SCROLLBAR_MEASURER;
            document.body.appendChild(scrollDiv);
            const scrollbarWidth = scrollDiv.getBoundingClientRect().width - scrollDiv.clientWidth;
            document.body.removeChild(scrollDiv);
            return scrollbarWidth;
        }
        // Static
        /*static _jQueryInterface(config, relatedTarget) {
          return this.each(function () {
            let data = $(this).data(DATA_KEY)
            const _config = {
              ...Default,
              ...$(this).data(),
              ...typeof config === 'object' && config ? config : {}
            }
      
            if (!data) {
              data = new Modal(this, _config)
              $(this).data(DATA_KEY, data)
            }
      
            if (typeof config === 'string') {
              if (typeof data[config] === 'undefined') {
                throw new TypeError(`No method named "${config}"`)
              }
              data[config](relatedTarget)
            } else if (_config.show) {
              data.show(relatedTarget)
            }
          })
        }*/
        emulateTransitionEnd(element, duration) {
            this.emulateTransitionEndCalled = false;
            element.addEventListener(Event$2.TRANSITION_END, () => {
                this.emulateTransitionEndCalled = true;
            });
            setTimeout(() => {
                if (!this.emulateTransitionEndCalled) {
                    element.dispatchEvent(new CustomEvent(Event$2.TRANSITION_END));
                }
            }, duration);
        }
        addOnetimeEventListener(element, event, listener) {
            function listenerWrapper() {
                listener();
                element.removeEventListener(event, listenerWrapper);
            }
            element.addEventListener(event, listenerWrapper);
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-popup") == null) {
            window.customElements.define("tobago-popup", Popup);
        }
    });
    class Collapse {
        static findHidden(element) {
            const rootNode = element.getRootNode();
            return rootNode.getElementById(element.id + "::collapse");
        }
    }
    Collapse.execute = function (action, target) {
        const hidden = Collapse.findHidden(target);
        let newCollapsed;
        switch (action) {
            case "hide":
                newCollapsed = true;
                break;
            case "show":
                newCollapsed = false;
                break;
            default:
                console.error("unknown action: '" + action + "'");
        }
        if (newCollapsed) {
            if (target instanceof Popup) {
                target.hide();
            }
            else {
                target.classList.add("tobago-collapsed");
            }
        }
        else {
            if (target instanceof Popup) {
                target.show();
            }
            else {
                target.classList.remove("tobago-collapsed");
            }
        }
        hidden.value = newCollapsed;
    };

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
    class Behavior extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            switch (this.event) {
                case "load": // this is a special case, because the "load" is too late now.
                    this.callback();
                    break;
                case "resize":
                    document.body.addEventListener(this.event, this.callback.bind(this));
                    break;
                default:
                    const eventElement = this.eventElement;
                    if (eventElement) {
                        eventElement.addEventListener(this.event, this.callback.bind(this));
                    }
                    else {
                        console.warn("Can't find an element for the event.", this);
                    }
            }
        }
        callback(event) {
            if (this.collapseAction && this.collapseTarget) {
                const rootNode = this.getRootNode();
                Collapse.execute(this.collapseAction, rootNode.getElementById(this.collapseTarget));
            }
            if (this.execute || this.render) { // this means: AJAX case?
                if (this.render) {
                    // prepare overlay for all by AJAX reloaded elements
                    let partialIds = this.render.split(" ");
                    for (let i = 0; i < partialIds.length; i++) {
                        const partialElement = document.getElementById(partialIds[i]);
                        if (partialElement) {
                            new Overlay(partialElement, true);
                        }
                        else {
                            console.warn("No element found by id='%s' for overlay!", partialIds[i]);
                        }
                    }
                }
                jsf.ajax.request(this.actionElement, event, {
                    "javax.faces.behavior.event": this.event,
                    execute: this.execute,
                    render: this.render
                });
            }
            else {
                if (!this.omit) {
                    setTimeout(this.submit.bind(this), this.delay);
                }
            }
        }
        submit() {
            const id = this.fieldId != null ? this.fieldId : this.clientId;
            CommandHelper.submitAction(this, id, this.decoupled, this.target);
        }
        get event() {
            return this.getAttribute("event");
        }
        set event(event) {
            this.setAttribute("event", event);
        }
        get clientId() {
            return this.getAttribute("client-id");
        }
        set clientId(clientId) {
            this.setAttribute("client-id", clientId);
        }
        get fieldId() {
            return this.getAttribute("field-id");
        }
        set fieldId(fieldId) {
            this.setAttribute("field-id", fieldId);
        }
        get execute() {
            return this.getAttribute("execute");
        }
        set execute(execute) {
            this.setAttribute("execute", execute);
        }
        get render() {
            return this.getAttribute("render");
        }
        set render(render) {
            this.setAttribute("render", render);
        }
        get delay() {
            return parseInt(this.getAttribute("delay")) || 0;
        }
        set delay(delay) {
            this.setAttribute("delay", String(delay));
        }
        get omit() {
            return this.hasAttribute("omit");
        }
        set omit(omit) {
            if (omit) {
                this.setAttribute("omit", "");
            }
            else {
                this.removeAttribute("omit");
            }
        }
        get target() {
            return this.getAttribute("target");
        }
        set target(target) {
            this.setAttribute("target", target);
        }
        get confirmation() {
            return this.getAttribute("confirmation");
        }
        set confirmation(confirmation) {
            this.setAttribute("confirmation", confirmation);
        }
        get collapseAction() {
            return this.getAttribute("collapse-action");
        }
        set collapseAction(collapseAction) {
            this.setAttribute("collapse-action", collapseAction);
        }
        get collapseTarget() {
            return this.getAttribute("collapse-target");
        }
        set collapseTarget(collapseTarget) {
            this.setAttribute("collapse-target", collapseTarget);
        }
        get decoupled() {
            return this.hasAttribute("decoupled");
        }
        set decoupled(decoupled) {
            if (decoupled) {
                this.setAttribute("decoupled", "");
            }
            else {
                this.removeAttribute("decoupled");
            }
        }
        get actionElement() {
            const rootNode = this.getRootNode();
            const id = this.clientId;
            return rootNode.getElementById(id);
        }
        get eventElement() {
            const rootNode = this.getRootNode();
            const id = this.fieldId ? this.fieldId : this.clientId;
            return rootNode.getElementById(id);
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-behavior") == null) {
            window.customElements.define("tobago-behavior", Behavior);
        }
    });
    class CommandHelper {
    }
    CommandHelper.isSubmit = false;
    /**
     * Submitting the page with specified actionId.
     * @param source
     * @param actionId
     * @param decoupled
     * @param target
     */
    CommandHelper.submitAction = function (source, actionId, decoupled = false, target) {
        Transport.request(function () {
            if (!CommandHelper.isSubmit) {
                CommandHelper.isSubmit = true;
                const form = document.getElementsByTagName("form")[0];
                const oldTarget = form.getAttribute("target");
                const sourceHidden = document.getElementById("javax.faces.source");
                sourceHidden.disabled = false;
                sourceHidden.value = actionId;
                if (target) {
                    form.setAttribute("target", target);
                }
                const listenerOptions = {
                    source: source,
                    actionId: actionId /*,
                    options: commandHelper*/
                };
                const onSubmitResult = CommandHelper.onSubmit(listenerOptions);
                if (onSubmitResult) {
                    try {
                        form.submit();
                        // reset the source field after submit, to be prepared for possible next AJAX with decoupled=true
                        sourceHidden.disabled = true;
                        sourceHidden.value = "";
                    }
                    catch (e) {
                        Overlay.destroy(Page.page(form).id);
                        CommandHelper.isSubmit = false;
                        alert("Submit failed: " + e); // XXX localization, better error handling
                    }
                }
                if (target) {
                    if (oldTarget) {
                        form.setAttribute("target", oldTarget);
                    }
                    else {
                        form.removeAttribute("target");
                    }
                }
                if (target || decoupled || !onSubmitResult) {
                    CommandHelper.isSubmit = false;
                    Transport.pageSubmitted = false;
                }
            }
            if (!CommandHelper.isSubmit) {
                Transport.requestComplete(); // remove this from queue
            }
        }, true);
    };
    CommandHelper.onSubmit = function (listenerOptions) {
        Listener.executeBeforeSubmit();
        /*
        XXX check if we need the return false case
        XXX maybe we cancel the submit, but we continue the rest?
        XXX should the other phases also have this feature?

            var result = true; // Do not continue if any function returns false
            for (var order = 0; order < Listeners.beforeSubmit.length; order++) {
              var list = Listeners.beforeSubmit[order];
              for (var i = 0; i < list.length; i++) {
                result = list[i](listenerOptions);
                if (result === false) {
                  break;
                }
              }
            }
            if (result === false) {
              this.isSubmit = false;
              return false;
            }
        */
        CommandHelper.isSubmit = true;
        const element = document.documentElement; // XXX this might be the wrong element in case of shadow dom
        Page.page(element).onBeforeUnload();
        return true;
    };
    class Transport {
    }
    Transport.requests = [];
    Transport.currentActionId = null;
    Transport.pageSubmitted = false;
    /**
     * @return true if the request is queued.
     */
    Transport.request = function (req, submitPage, actionId) {
        let index = 0;
        if (submitPage) {
            Transport.pageSubmitted = true;
            index = Transport.requests.push(req);
            //console.debug('index = ' + index)
        }
        else if (!Transport.pageSubmitted) { // AJAX case
            console.debug("Current ActionId = " + Transport.currentActionId + " action= " + actionId);
            if (actionId && Transport.currentActionId === actionId) {
                console.info("Ignoring request");
                // If actionId equals currentActionId assume double request: do nothing
                return false;
            }
            index = Transport.requests.push(req);
            //console.debug('index = ' + index)
            Transport.currentActionId = actionId;
        }
        else {
            console.debug("else case");
            return false;
        }
        console.debug("index = " + index);
        if (index === 1) {
            console.info("Execute request!");
            Transport.startTime = new Date();
            Transport.requests[0]();
        }
        else {
            console.info("Request queued!");
        }
        return true;
    };
    // TBD XXX REMOVE is this called in non AJAX case?
    Transport.requestComplete = function () {
        Transport.requests.shift();
        Transport.currentActionId = null;
        console.debug("Request complete! Duration: " + (new Date().getTime() - Transport.startTime.getTime()) + "ms; "
            + "Queue size : " + Transport.requests.length);
        if (Transport.requests.length > 0) {
            console.debug("Execute request!");
            Transport.startTime = new Date();
            Transport.requests[0]();
        }
    };

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
    class ReloadManager {
        constructor() {
            this.timeouts = new Map();
        }
        schedule(id, reloadMillis) {
            if (reloadMillis > 0) {
                // may remove old schedule
                let oldTimeout = this.timeouts.get(id);
                if (oldTimeout) {
                    console.debug("clear reload timeout '" + oldTimeout + "' for #'" + id + "'");
                    window.clearTimeout(oldTimeout);
                    this.timeouts.delete(id);
                }
                // add new schedule
                let timeout = window.setTimeout(function () {
                    console.debug("reloading #'" + id + "'");
                    jsf.ajax.request(id, null, {
                        "javax.faces.behavior.event": "reload",
                        execute: id,
                        render: id
                    });
                }, reloadMillis);
                console.debug("adding reload timeout '" + timeout + "' for #'" + id + "'");
                this.timeouts.set(id, timeout);
            }
        }
    }
    ReloadManager.instance = new ReloadManager();
    ReloadManager.init = function (element) {
        for (const reload of DomUtils.selfOrQuerySelectorAll(element, "[data-tobago-reload]")) {
            ReloadManager.instance.schedule(reload.id, Number(reload.dataset.tobagoReload));
        }
    };
    Listener.register(ReloadManager.init, Phase.DOCUMENT_READY);
    Listener.register(ReloadManager.init, Phase.AFTER_UPDATE);

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
    class Page extends HTMLElement {
        constructor() {
            super();
        }
        /**
         * The Tobago root element
         */
        static page(element) {
            const rootNode = element.getRootNode();
            const pages = rootNode.querySelectorAll("tobago-page");
            if (pages.length > 0) {
                if (pages.length >= 2) {
                    console.warn("Found more than one tobago-page element!");
                }
                return pages.item(0);
            }
            console.warn("Found no tobago page!");
            return null;
        }
        connectedCallback() {
            this.registerAjaxListener();
            this.querySelector("form").addEventListener("submit", CommandHelper.onSubmit);
            window.addEventListener("unload", this.onUnload.bind(this));
            this.addEventListener("keypress", (event) => {
                let code = event.which; // XXX deprecated
                if (code === 0) {
                    code = event.keyCode;
                }
                if (code === 13) {
                    let target = event.target;
                    if (target.tagName === "A" || target.tagName === "BUTTON") {
                        return;
                    }
                    if (target.tagName === "TEXTAREA") {
                        if (!event.metaKey && !event.ctrlKey) {
                            return;
                        }
                    }
                    const name = target.getAttribute("name");
                    let id = name ? name : target.id;
                    while (id != null) {
                        const command = document.querySelector("[data-tobago-default='" + id + "']");
                        if (command) {
                            command.dispatchEvent(new MouseEvent("click"));
                            break;
                        }
                        id = DomUtils.getNamingContainerId(id);
                    }
                    return false;
                }
            });
            // todo remove this
            Listener.executeDocumentReady(document.documentElement);
        }
        onBeforeUnload() {
            if (this.transition) {
                new Overlay(this);
            }
            this.transition = this.oldTransition;
        }
        /**
         * Wrapper function to call application generated onunload function
         */
        onUnload() {
            console.info("on onload");
            if (CommandHelper.isSubmit) {
                if (this.transition) {
                    new Overlay(this);
                }
                this.transition = this.oldTransition;
            }
            else {
                Listener.executeBeforeExit();
            }
        }
        registerAjaxListener() {
            jsf.ajax.addOnEvent(this.jsfResponse.bind(this));
        }
        jsfResponse(event) {
            console.timeEnd("[tobago-jsf] jsf-ajax");
            console.time("[tobago-jsf] jsf-ajax");
            console.debug("[tobago-jsf] JSF event status: '%s'", event.status);
            if (event.status === "success") {
                event.responseXML.querySelectorAll("update").forEach(this.jsfResponseSuccess.bind(this));
            }
            else if (event.status === "complete") {
                event.responseXML.querySelectorAll("update").forEach(this.jsfResponseComplete.bind(this));
            }
        }
        jsfResponseSuccess(update) {
            const result = /<!\[CDATA\[(.*)]]>/gm.exec(update.innerHTML);
            const id = update.id;
            if (result !== null && result.length === 2 && result[1].startsWith("{\"reload\"")) {
                // not modified on server, needs be reloaded after some time
                console.debug("[tobago-jsf] Found reload-JSON in response!");
                ReloadManager.instance.schedule(id, JSON.parse(result[1]).reload.frequency);
            }
            else {
                console.info("[tobago-jsf] Update after jsf.ajax success: %s", id);
                if (JsfParameter.isJsfId(id)) {
                    console.debug("[tobago-jsf] updating #%s", id);
                    const rootNode = this.getRootNode();
                    let element = rootNode.getElementById(id);
                    if (element) {
                        Listener.executeAfterUpdate(element);
                    }
                    else {
                        console.warn("[tobago-jsf] element not found for #%s", id);
                    }
                }
                else if (JsfParameter.isJsfBody(id)) {
                    console.debug("[tobago-jsf] updating body");
                    // there should be only one element with this tag name
                    const rootNode = this.getRootNode();
                    Listener.executeAfterUpdate(rootNode.querySelector("tobago-page"));
                }
            }
        }
        jsfResponseComplete(update) {
            const id = update.id;
            if (JsfParameter.isJsfId(id)) {
                console.debug("[tobago-jsf] Update after jsf.ajax complete: #" + id);
                Overlay.destroy(id);
            }
        }
        get locale() {
            let locale = this.getAttribute("locale");
            if (!locale) {
                locale = document.documentElement.lang;
            }
            return locale;
        }
    }
    document.addEventListener("tobago.init", (event) => {
        if (window.customElements.get("tobago-page") == null) {
            window.customElements.define("tobago-page", Page);
        }
    });
    // todo remove this
    window.addEventListener("load", Listener.executeWindowLoad);
    class JsfParameter {
        static isJsfId(id) {
            switch (id) {
                case JsfParameter.VIEW_STATE:
                case JsfParameter.CLIENT_WINDOW:
                case JsfParameter.VIEW_ROOT:
                case JsfParameter.VIEW_HEAD:
                case JsfParameter.VIEW_BODY:
                case JsfParameter.RESOURCE:
                    return false;
                default:
                    return true;
            }
        }
        static isJsfBody(id) {
            switch (id) {
                case JsfParameter.VIEW_ROOT:
                case JsfParameter.VIEW_BODY:
                    return true;
                default:
                    return false;
            }
        }
    }
    JsfParameter.VIEW_STATE = "javax.faces.ViewState";
    JsfParameter.CLIENT_WINDOW = "javax.faces.ClientWindow";
    JsfParameter.VIEW_ROOT = "javax.faces.ViewRoot";
    JsfParameter.VIEW_HEAD = "javax.faces.ViewHead";
    JsfParameter.VIEW_BODY = "javax.faces.ViewBody";
    JsfParameter.RESOURCE = "javax.faces.Resource";

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
    class DatePicker extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            var _a;
            const input = this.inputElement;
            // todo: refactor "i18n" to "normal" attribute of tobago-date
            // todo: refactor: Make a class or interface for i18n
            const i18n = input.dataset.tobagoDateTimeI18n ? JSON.parse(input.dataset.tobagoDateTimeI18n) : undefined;
            // todo: refactor "pattern" to "normal" attribute of tobago-date
            const pattern = DateUtils.convertPattern(input.dataset.tobagoPattern);
            const locale = Page.page(this).locale;
            Datepicker.locales[locale] = {
                days: i18n.dayNames,
                daysShort: i18n.dayNamesShort,
                daysMin: i18n.dayNamesMin,
                months: i18n.monthNames,
                monthsShort: i18n.monthNamesShort,
                today: i18n.today,
                clear: i18n.clear,
                titleFormat: "MM y",
                format: pattern,
                weekstart: i18n.firstDay
            };
            new Datepicker(input, {
                buttonClass: "btn",
                orientation: "bottom top auto",
                autohide: true,
                language: locale
                // todo readonly
                // todo show week numbers
            });
            // XXX these two listeners are needed befor we have a solution for:
            // XXX https://github.com/mymth/vanillajs-datepicker/issues/13
            input.addEventListener("keyup", (event) => {
                if (event.ctrlKey || event.metaKey
                    || event.key.length > 1 && event.key !== "Backspace" && event.key !== "Delete") {
                    return;
                }
                // back up user's input when user types printable character or backspace/delete
                const target = event.target;
                target._oldValue = target.value;
            });
            input.addEventListener("blur", (event) => {
                const target = event.target;
                if (!document.hasFocus() || target._oldValue === undefined) {
                    // no-op when user goes to another window or the input field has no backed-up value
                    return;
                }
                if (target._oldValue !== target.value) {
                    target.datepicker.setDate(target._oldValue);
                }
                delete target._oldValue;
            });
            // simple solution for the picker: currently only open, not close is implemented
            (_a = this.querySelector(".tobago-date-picker")) === null || _a === void 0 ? void 0 : _a.addEventListener("click", (event) => {
                this.inputElement.focus();
            });
        }
        get inputElement() {
            return this.querySelector(".input");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-date") == null) {
            window.customElements.define("tobago-date", DatePicker);
        }
    });

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
    class File extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.input.form.enctype = "multipart/form-data";
        }
        get input() {
            return this.querySelector("input");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-file") == null) {
            window.customElements.define("tobago-file", File);
        }
    });

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
    class Focus extends HTMLElement {
        /**
         * The focusListener to set the lastFocusId must be implemented in the appropriate web elements.
         * @param event
         */
        static setLastFocusId(event) {
            const target = event.target;
            let computedStyle = getComputedStyle(target);
            if (target.getAttribute("type") !== "hidden"
                && target.getAttributeNames().indexOf("disabled") === -1
                && target.getAttribute("tabindex") !== "-1"
                && computedStyle.visibility !== "hidden"
                && computedStyle.display !== "none") {
                const root = target.getRootNode();
                const tobagoFocus = root.getElementById(Page.page(target).id + DomUtils.SUB_COMPONENT_SEP + "lastFocusId");
                tobagoFocus.querySelector("input").value = target.id;
            }
        }
        constructor() {
            super();
        }
        /**
         * Sets the focus to the requested element or to the first possible if
         * no element is explicitly requested.
         *
         * The priority order is:
         * - error (the first error element gets the focus)
         * - auto (the element with the tobago tag attribute focus="true" gets the focus)
         * - last (the element from the last request with same id gets the focus, not AJAX)
         * - first (the first input element (without tabindex=-1) gets the focus, not AJAX)
         */
        connectedCallback() {
            const errorElement = this.errorElement;
            if (errorElement) {
                errorElement.focus();
                return;
            }
            if (this.autofocusElements.length > 0) {
                // nothing to do, because the browser make the work.
                return;
            }
            const lastFocusedElement = this.lastFocusedElement;
            if (lastFocusedElement) {
                lastFocusedElement.focus();
                return;
            }
            const focusableElement = this.focusableElement;
            if (focusableElement) {
                focusableElement.focus();
                return;
            }
        }
        get errorElement() {
            const root = this.getRootNode();
            const elements = root.querySelectorAll(".tobago-messages-container .border-danger:not([disabled]):not([tabindex='-1'])");
            for (const element of elements) {
                const computedStyle = getComputedStyle(element);
                if (computedStyle.display !== "none" && computedStyle.visibility !== "hidden") {
                    return element;
                }
            }
        }
        get autofocusElements() {
            const root = this.getRootNode();
            return root.querySelectorAll("[autofocus]");
        }
        get lastFocusedElement() {
            const lastFocusId = this.hiddenInput.value;
            if (lastFocusId) {
                const root = this.getRootNode();
                return root.getElementById(lastFocusId);
            }
            else {
                return null;
            }
        }
        get hiddenInput() {
            return this.querySelector("input");
        }
        get focusableElement() {
            const root = this.getRootNode();
            const elements = root.querySelectorAll("input:not([type='hidden']):not([disabled]):not([tabindex='-1'])," +
                "select:not([disabled]):not([tabindex='-1'])," +
                "textarea:not([disabled]):not([tabindex='-1'])");
            for (const element of elements) {
                if (this.isVisible(element)) {
                    return element;
                }
            }
        }
        isVisible(element) {
            const computedStyle = getComputedStyle(element);
            if (computedStyle.display === "none" || computedStyle.visibility === "hidden") {
                return false;
            }
            else if (element.parentElement) {
                return this.isVisible(element.parentElement);
            }
            else {
                return true;
            }
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-focus") == null) {
            window.customElements.define("tobago-focus", Focus);
        }
    });

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
    class Footer extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.lastMaxFooterHeight = 0;
            if (this.isFixed) {
                window.addEventListener("resize", this.resize.bind(this));
                if (this.body) {
                    this.setMargins();
                }
            }
        }
        resize(event) {
            const maxFooterHeight = DomUtils.outerHeightWithMargin(this);
            if (maxFooterHeight !== this.lastMaxFooterHeight) {
                this.setMargins();
                this.lastMaxFooterHeight = maxFooterHeight;
            }
        }
        setMargins() {
            if (this.isFixed) {
                const maxFooterHeight = DomUtils.outerHeightWithMargin(this);
                if (maxFooterHeight > 0) {
                    this.body.style.marginBottom = maxFooterHeight + "px";
                }
            }
        }
        get body() {
            const root = this.getRootNode();
            return root.querySelector("body");
        }
        get isFixed() {
            return this.classList.contains("fixed-bottom");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-footer") == null) {
            window.customElements.define("tobago-footer", Footer);
        }
    });

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
    class In extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.input.addEventListener("focus", Focus.setLastFocusId);
        }
        get input() {
            return this.querySelector(DomUtils.escapeClientId(this.id + DomUtils.SUB_COMPONENT_SEP + "field"));
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-in") == null) {
            window.customElements.define("tobago-in", In);
        }
    });
    // XXX regexp example only - blueprint
    class RegExpTest {
        constructor(element) {
            this.element = element;
            this.regexp = new RegExp(this.element.dataset.regexp);
            console.debug("constructor: '%s'", element.id);
            this.element.addEventListener("change", this.checkValue.bind(this));
        }
        static init(element) {
            for (const input of DomUtils.selfOrElementsByClassName(element, "tobago-in")) { // todo only for data-regexp
                new RegExpTest(input);
            }
        }
        checkValue(event) {
            console.debug("changed: check if '%s' is okay!", this.regexp.toString());
            if (!this.regexp.test(this.element.value)) {
                this.element.classList.add("border-danger");
            }
            else {
                this.element.classList.remove("border-danger");
            }
        }
    }
    Listener.register(RegExpTest.init, Phase.DOCUMENT_READY);
    Listener.register(RegExpTest.init, Phase.AFTER_UPDATE);

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
    class Messages extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            for (let closeButton of this.closeButtons) {
                closeButton.addEventListener("click", this.closeAlert);
            }
        }
        closeAlert(event) {
            this.closest(".alert").remove();
        }
        get closeButtons() {
            return this.querySelectorAll(".alert button.btn-close");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-messages") == null) {
            window.customElements.define("tobago-messages", Messages);
        }
    });

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
    class Panel extends HTMLElement {
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-panel") == null) {
            window.customElements.define("tobago-panel", Panel);
        }
    });

    /* Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements.  See the NOTICE file distributed with
     * this work for additional information regarding copyright ownership.
     * The ASF licenses this file to you under the Apache License, Version 2.0
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
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // for old Edge (before Chromium)
    try {
        document.querySelector(":scope");
    }
    catch (exception) {
        const querySelectorWithScope = polyfill(Element.prototype.querySelector);
        Element.prototype.querySelector = function querySelector(selectors) {
            return querySelectorWithScope.apply(this, arguments);
        };
        const querySelectorAllWithScope = polyfill(Element.prototype.querySelectorAll);
        Element.prototype.querySelectorAll = function querySelectorAll(selectors) {
            return querySelectorAllWithScope.apply(this, arguments);
        };
        if (Element.prototype.matches) {
            const matchesWithScope = polyfill(Element.prototype.matches);
            Element.prototype.matches = function matches(selectors) {
                return matchesWithScope.apply(this, arguments);
            };
        }
        if (Element.prototype.closest) {
            const closestWithScope = polyfill(Element.prototype.closest);
            Element.prototype.closest = function closest(selectors) {
                return closestWithScope.apply(this, arguments);
            };
        }
        function polyfill(prototypeFunc) {
            const scope = /:scope(?![\w-])/gi;
            return function (selector) {
                if (selector.toLowerCase().indexOf(":scope") >= 0) {
                    const attr = "tobagoScopeAttribute";
                    arguments[0] = selector.replace(scope, "[" + attr + "]");
                    this.setAttribute(attr, "");
                    const element = prototypeFunc.apply(this, arguments);
                    this.removeAttribute(attr);
                    return element;
                }
                else {
                    return prototypeFunc.apply(this, arguments);
                }
            };
        }
    }
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
    class Popover extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.button.addEventListener("click", this.showPopover.bind(this));
            this.button.addEventListener("blur", this.hidePopover.bind(this));
        }
        showPopover() {
            this.menuStore.appendChild(this.popover);
            this.popper = new Popper(this.button, this.popover, {
                placement: "right",
                modifiers: {
                    arrow: {
                        element: ".arrow"
                    }
                },
                onCreate: this.updateBootstrapPopoverCss.bind(this),
                onUpdate: this.updateBootstrapPopoverCss.bind(this)
            });
            this.popover.classList.add("show");
        }
        hidePopover() {
            this.popover.classList.remove("show");
            this.appendChild(this.popover);
            if (this.popper !== undefined && this.popper !== null) {
                this.popper.destroy();
                this.popper = null;
            }
        }
        updateBootstrapPopoverCss() {
            const placement = this.popover.getAttribute("x-placement");
            if (placement === "right" && !this.popover.classList.contains("bs-popover-right")) {
                this.popover.classList.add("bs-popover-right");
                this.popover.classList.remove("bs-popover-left");
                this.updateAfterCssClassChange();
            }
            else if (placement === "left" && !this.popover.classList.contains("bs-popover-left")) {
                this.popover.classList.add("bs-popover-left");
                this.popover.classList.remove("bs-popover-right");
                this.updateAfterCssClassChange();
            }
        }
        updateAfterCssClassChange() {
            if (this.popper !== undefined && this.popper !== null) {
                this.popper.scheduleUpdate();
            }
        }
        get button() {
            return this.querySelector(":scope > .tobago-popover-button");
        }
        get popover() {
            const root = this.getRootNode();
            return root.querySelector(".tobago-popover-box[name='" + this.id + "']");
        }
        get menuStore() {
            const root = this.getRootNode();
            return root.querySelector(".tobago-page-menuStore");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-popover") == null) {
            window.customElements.define("tobago-popover", Popover);
        }
    });

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
    class Range extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            let range = this.range;
            let listener = this.showPopper.bind(this);
            range.addEventListener("input", listener);
            range.addEventListener("focus", listener);
        }
        get range() {
            return this.querySelector("input[type=range]");
        }
        get tooltip() {
            return this.querySelector(".popover");
        }
        get tooltipBody() {
            return this.querySelector(".popover-body");
        }
        showPopper() {
            let tooltip = this.tooltip;
            let range = this.range;
            // update value to display
            this.tooltipBody.textContent = range.value; // todo: use html from lit-html
            // init
            if (!this.popper) {
                this.popper = new Popper(range, tooltip, {
                    placement: "right"
                });
            }
            // show
            tooltip.classList.remove("d-none");
            // hide after some seconds
            if (this.timeout) {
                window.clearTimeout(this.timeout);
            }
            this.timeout = window.setTimeout(() => {
                tooltip.classList.add("d-none");
                console.log("timeout");
            }, 5000);
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-range") == null) {
            window.customElements.define("tobago-range", Range);
        }
    });

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
    class Scroll {
    }
    Scroll.initScrollPosition = function (element) {
        for (const panel of DomUtils.selfOrQuerySelectorAll(element, "[data-tobago-scroll-panel]")) {
            const hidden = panel.querySelector(":scope > [data-tobago-scroll-position]");
            const values = JSON.parse(hidden.value);
            if (values.length === 2) {
                panel.scrollLeft = values[0];
                panel.scrollTop = values[1];
            }
            else {
                console.warn("Wrong syntax for scroll: " + hidden.value);
            }
            panel.addEventListener("scroll", Scroll.scroll);
        }
    };
    Scroll.scroll = function (event) {
        const panel = event.currentTarget;
        const scrollLeft = panel.scrollLeft;
        const scrollTop = panel.scrollTop;
        const hidden = panel.querySelector(":scope > [data-tobago-scroll-position]");
        hidden.value = JSON.stringify([scrollLeft, scrollTop]);
    };
    Listener.register(Scroll.initScrollPosition, Phase.DOCUMENT_READY, Order.LATER);
    Listener.register(Scroll.initScrollPosition, Phase.AFTER_UPDATE, Order.LATER);

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
    class SelectBooleanCheckbox extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.field.addEventListener("focus", Focus.setLastFocusId);
            if (this.field.readOnly) {
                this.field.addEventListener("click", preventClick);
            }
            function preventClick(event) {
                // in the "readonly" case, prevent the default, which is changing the "checked" state
                event.preventDefault();
            }
        }
        get field() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "field");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-select-boolean-checkbox") == null) {
            window.customElements.define("tobago-select-boolean-checkbox", SelectBooleanCheckbox);
        }
    });

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
    class SelectBooleanToggle extends SelectBooleanCheckbox {
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-select-boolean-toggle") == null) {
            window.customElements.define("tobago-select-boolean-toggle", SelectBooleanToggle);
        }
    });

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
    class SelectManyCheckbox extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            for (const input of this.inputs) {
                input.addEventListener("focus", Focus.setLastFocusId);
                if (input.readOnly) {
                    input.addEventListener("click", preventClick);
                }
                function preventClick(event) {
                    // in the "readonly" case, prevent the default, which is changing the "checked" state
                    event.preventDefault();
                }
            }
        }
        get inputs() {
            return this.querySelectorAll("input[name='" + this.id + "']");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-select-many-checkbox") == null) {
            window.customElements.define("tobago-select-many-checkbox", SelectManyCheckbox);
        }
    });

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
    class SelectOneListbox extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.field.addEventListener("focus", Focus.setLastFocusId);
        }
        get field() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "field");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-select-one-listbox") == null) {
            window.customElements.define("tobago-select-one-listbox", SelectOneListbox);
        }
    });

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
    class SelectManyListbox extends SelectOneListbox {
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-select-many-listbox") == null) {
            window.customElements.define("tobago-select-many-listbox", SelectManyListbox);
        }
    });

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
    class SelectManyShuttle extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.unselectedSelect.addEventListener("focus", Focus.setLastFocusId);
            this.selectedSelect.addEventListener("focus", Focus.setLastFocusId);
            if (this.unselectedSelect.getAttribute("readonly") !== "readonly" && !this.unselectedSelect.disabled) {
                this.unselectedSelect.addEventListener("dblclick", this.addSelectedItems.bind(this));
            }
            if (this.selectedSelect.getAttribute("readonly") !== "readonly" && !this.selectedSelect.disabled) {
                this.selectedSelect.addEventListener("dblclick", this.removeSelectedItems.bind(this));
            }
            if (!this.addAllButton.disabled) {
                this.addAllButton.addEventListener("click", this.addAllItems.bind(this));
            }
            if (!this.addButton.disabled) {
                this.addButton.addEventListener("click", this.addSelectedItems.bind(this));
            }
            if (!this.removeButton.disabled) {
                this.removeButton.addEventListener("click", this.removeSelectedItems.bind(this));
            }
            if (!this.removeAllButton.disabled) {
                this.removeAllButton.addEventListener("click", this.removeAllItems.bind(this));
            }
        }
        addAllItems(event) {
            this.addItems(this.unselectedSelect.querySelectorAll("option:not(:disabled)"));
        }
        addSelectedItems(event) {
            this.addItems(this.unselectedSelect.querySelectorAll("option:checked"));
        }
        removeSelectedItems(event) {
            this.removeItems(this.selectedSelect.querySelectorAll("option:checked"));
        }
        removeAllItems(event) {
            this.removeItems(this.selectedSelect.querySelectorAll("option:not(:disabled)"));
        }
        addItems(options) {
            for (const option of options) {
                this.selectedSelect.add(option);
                this.changeHiddenOption(option, true);
            }
        }
        removeItems(options) {
            for (const option of options) {
                this.unselectedSelect.add(option);
                this.changeHiddenOption(option, false);
            }
        }
        changeHiddenOption(option, select) {
            const hiddenOption = this.hiddenSelect.querySelector("option[value='" + option.value + "']");
            hiddenOption.selected = select;
            this.dispatchEvent(new Event("change"));
        }
        get unselectedSelect() {
            return this.querySelector(".tobago-selectManyShuttle-unselected");
        }
        get selectedSelect() {
            return this.querySelector(".tobago-selectManyShuttle-selected");
        }
        get hiddenSelect() {
            return this.querySelector(".tobago-selectManyShuttle-hidden");
        }
        get addAllButton() {
            return this.querySelector(".tobago-selectManyShuttle-addAll");
        }
        get addButton() {
            return this.querySelector(".tobago-selectManyShuttle-add");
        }
        get removeButton() {
            return this.querySelector(".tobago-selectManyShuttle-remove");
        }
        get removeAllButton() {
            return this.querySelector(".tobago-selectManyShuttle-removeAll");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-select-many-shuttle") == null) {
            window.customElements.define("tobago-select-many-shuttle", SelectManyShuttle);
        }
    });

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
    class SelectOneChoice extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.field.addEventListener("focus", Focus.setLastFocusId);
        }
        get field() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "field");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-select-one-choice") == null) {
            window.customElements.define("tobago-select-one-choice", SelectOneChoice);
        }
    });

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
    class SelectOneRadio extends HTMLElement {
        constructor() {
            super();
            this.oldCheckedId = "";
        }
        connectedCallback() {
            this.saveSelection();
            for (const radio of this.radioGroup) {
                radio.addEventListener("focus", Focus.setLastFocusId);
                radio.addEventListener("click", this.clickSelection.bind(this));
            }
        }
        clickSelection(event) {
            const radio = event.currentTarget;
            if (radio.readOnly) {
                this.revertSelection();
            }
            else if (!radio.disabled && !radio.required && radio.id === this.oldCheckedId) {
                radio.checked = false;
                this.oldCheckedId = "";
            }
            this.saveSelection();
        }
        revertSelection() {
            for (const radio of this.radioGroup) {
                radio.checked = radio.id === this.oldCheckedId;
            }
        }
        saveSelection() {
            for (const radio of this.radioGroup) {
                if (radio.checked) {
                    this.oldCheckedId = radio.id;
                }
            }
        }
        get radioGroup() {
            return this.querySelectorAll("input[type='radio'][name='" + this.id + "']");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-select-one-radio") == null) {
            window.customElements.define("tobago-select-one-radio", SelectOneRadio);
        }
    });

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
    class Sheet extends HTMLElement {
        constructor() {
            super();
        }
        static getScrollBarSize() {
            const body = document.getElementsByTagName("body").item(0);
            const outer = document.createElement("div");
            outer.style.visibility = "hidden";
            outer.style.width = "100px";
            outer.style.overflow = "scroll";
            body.append(outer);
            const inner = document.createElement("div");
            inner.style.width = "100%";
            outer.append(inner);
            const widthWithScroll = inner.offsetWidth;
            body.removeChild(outer);
            return 100 - widthWithScroll;
        }
        static isInputElement(element) {
            return ["INPUT", "TEXTAREA", "SELECT", "A", "BUTTON"].indexOf(element.tagName) > -1;
        }
        static getRowTemplate(columns, rowIndex) {
            return `<tr row-index="${rowIndex}" class="tobago-sheet-row" dummy="dummy">
<td class="tobago-sheet-cell" colspan="${columns}"> </td>
</tr>`;
        }
        connectedCallback() {
            if (this.lazyUpdate) {
                // nothing to do here, will be done in method lazyResponse()
                return;
            }
            // synchronize column widths ----------------------------------------------------------------------------------- //
            // basic idea: there are two possible sources for the sizes:
            // 1. the columns attribute of <tc:sheet> like {"columns":[1.0,1.0,1.0]}, held by data attribute "tobago-layout"
            // 2. the hidden field which may contain a value like ",300,200,100,"
            //
            // The 1st source usually is the default set by the developer.
            // The 2nd source usually is the value set by the user manipulating the column widths.
            //
            // So, if the 2nd is set, we use it, if not set, we use the 1st source.
            let columnWidths = this.loadColumnWidths();
            console.info("columnWidths: %s", JSON.stringify(columnWidths));
            if (columnWidths && columnWidths.length === 0) { // active, but empty
                // otherwise use the layout definition
                const tokens = JSON.parse(this.dataset.tobagoLayout).columns;
                const columnRendered = this.isColumnRendered();
                const headerCols = this.getHeaderCols();
                const bodyTable = this.getBodyTable();
                const bodyCols = this.getBodyCols();
                console.assert(headerCols.length - 1 === bodyCols.length, "header and body column number doesn't match: %d != %d ", headerCols.length - 1, bodyCols.length);
                let sumRelative = 0; // tbd: is this needed?
                let widthRelative = bodyTable.offsetWidth;
                for (let i = 0; i < tokens.length; i++) {
                    if (columnRendered[i]) {
                        if (typeof tokens[i] === "number") {
                            sumRelative += tokens[i];
                        }
                        else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
                            const intValue = parseInt(tokens[i].measure);
                            if (tokens[i].measure.lastIndexOf("px") > 0) {
                                widthRelative -= intValue;
                            }
                            else if (tokens[i].measure.lastIndexOf("%") > 0) {
                                widthRelative -= bodyTable.offsetWidth * intValue / 100;
                            }
                        }
                        else {
                            console.debug("auto? = " + tokens[i]);
                        }
                    }
                }
                if (widthRelative < 0) {
                    widthRelative = 0;
                }
                let headerBodyColCount = 0;
                for (let i = 0; i < tokens.length; i++) {
                    let colWidth = 0;
                    if (columnRendered[i]) {
                        if (typeof tokens[i] === "number") {
                            colWidth = tokens[i] * widthRelative / sumRelative;
                        }
                        else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
                            const intValue = parseInt(tokens[i].measure);
                            if (tokens[i].measure.lastIndexOf("px") > 0) {
                                colWidth = intValue;
                            }
                            else if (tokens[i].measure.lastIndexOf("%") > 0) {
                                colWidth = bodyTable.offsetWidth * intValue / 100;
                            }
                        }
                        else {
                            console.debug("auto? = " + tokens[i]);
                        }
                        if (colWidth > 0) { // because tokens[i] == "auto"
                            headerCols.item(headerBodyColCount).setAttribute("width", String(colWidth));
                            bodyCols.item(headerBodyColCount).setAttribute("width", String(colWidth));
                        }
                        headerBodyColCount++;
                    }
                }
            }
            this.addHeaderFillerWidth();
            // resize column: mouse events -------------------------------------------------------------------------------- //
            for (const resizeElement of this.querySelectorAll(".tobago-sheet-headerResize")) {
                resizeElement.addEventListener("click", function () {
                    return false;
                });
                resizeElement.addEventListener("mousedown", this.mousedown.bind(this));
            }
            // scrolling -------------------------------------------------------------------------------------------------- //
            const sheetBody = this.getBody();
            // restore scroll position
            const value = JSON.parse(this.getHiddenScrollPosition().getAttribute("value"));
            sheetBody.scrollLeft = value[0];
            sheetBody.scrollTop = value[1];
            this.syncScrolling();
            // scroll events
            sheetBody.addEventListener("scroll", this.scroll.bind(this));
            // add selection listeners ------------------------------------------------------------------------------------ //
            const selectionMode = this.dataset.tobagoSelectionMode;
            if (selectionMode === "single" || selectionMode === "singleOrNone" || selectionMode === "multi") {
                for (const row of this.getRowElements()) {
                    row.addEventListener("mousedown", this.mousedownOnRow.bind(this));
                    row.addEventListener("click", this.clickOnRow.bind(this));
                }
            }
            for (const checkbox of this.querySelectorAll(".tobago-sheet-cell > input.tobago-sheet-columnSelector")) {
                checkbox.addEventListener("click", (event) => {
                    event.preventDefault();
                });
            }
            // lazy load by scrolling ----------------------------------------------------------------- //
            const lazy = this.lazy;
            if (lazy) {
                // prepare the sheet with some auto-created (empty) rows
                const rowCount = this.rowCount;
                const sheetBody = this.tableBodyDiv;
                const tableBody = this.tableBody;
                const columns = tableBody.rows[0].cells.length;
                let current = tableBody.rows[0]; // current row in this algorithm, begin with first
                // the algorithm goes straight through all rows, not selectors, because of performance
                for (let i = 0; i < rowCount; i++) {
                    if (current) {
                        const rowIndex = Number(current.getAttribute("row-index"));
                        if (i < rowIndex) {
                            const template = Sheet.getRowTemplate(columns, i);
                            current.insertAdjacentHTML("beforebegin", template);
                        }
                        else if (i === rowIndex) {
                            current = current.nextElementSibling;
                            // } else { TBD: I think this is not possible
                            //   const template = Sheet.getRowTemplate(columns, i);
                            //   current.insertAdjacentHTML("afterend", template);
                            //   current = current.nextElementSibling as HTMLTableRowElement;
                        }
                    }
                    else {
                        const template = Sheet.getRowTemplate(columns, i);
                        tableBody.insertAdjacentHTML("beforeend", template);
                    }
                }
                sheetBody.addEventListener("scroll", this.lazyCheck.bind(this));
                // initial
                this.lazyCheck();
            }
            // ---------------------------------------------------------------------------------------- //
            for (const checkbox of this.querySelectorAll(".tobago-sheet-header .tobago-sheet-columnSelector")) {
                checkbox.addEventListener("click", this.clickOnCheckbox.bind(this));
            }
            // init paging by pages ---------------------------------------------------------------------------------------- //
            for (const pagingText of this.querySelectorAll(".tobago-sheet-pagingText")) {
                pagingText.addEventListener("click", this.clickOnPaging.bind(this));
                const pagingInput = pagingText.querySelector("input.tobago-sheet-pagingInput");
                pagingInput.addEventListener("blur", this.blurPaging.bind(this));
                pagingInput.addEventListener("keydown", function (event) {
                    if (event.keyCode === 13) {
                        event.stopPropagation();
                        event.preventDefault();
                        event.currentTarget.dispatchEvent(new Event("blur"));
                    }
                });
            }
        }
        // attribute getter + setter ---------------------------------------------------------- //
        get lazyActive() {
            return this.hasAttribute("lazy-active");
        }
        set lazyActive(update) {
            if (update) {
                this.setAttribute("lazy-active", "");
            }
            else {
                this.removeAttribute("lazy-active");
            }
        }
        get lazy() {
            return this.hasAttribute("lazy");
        }
        set lazy(update) {
            if (update) {
                this.setAttribute("lazy", "");
            }
            else {
                this.removeAttribute("lazy");
            }
        }
        get lazyUpdate() {
            return this.hasAttribute("lazy-update");
        }
        get rows() {
            return parseInt(this.getAttribute("rows"));
        }
        get rowCount() {
            return parseInt(this.getAttribute("row-count"));
        }
        get tableBodyDiv() {
            return this.querySelector(".tobago-sheet-body");
        }
        get tableBody() {
            return this.querySelector(".tobago-sheet-bodyTable>tbody");
        }
        // -------------------------------------------------------------------------------------- //
        /*
          when an event occurs (initial load OR scroll event OR AJAX response)
      
          then -> Tobago.Sheet.lazyCheck()
                  1. check, if the lazy reload is currently active
                     a) yes -> do nothing and exit
                     b) no  -> step 2.
                  2. check, if there are data need to load (depends on scroll position and already loaded data)
                     a) yes -> set lazy reload to active and make an AJAX request with Tobago.Sheet.reloadLazy()
                     b) no  -> do nothing and exit
      
           AJAX response -> 1. update the rows in the sheet from the response
                            2. go to the first part of this description
        */
        /**
         * Checks if a lazy update is required, because there are unloaded rows in the visible area.
         */
        lazyCheck(event) {
            if (this.lazyActive) {
                // nothing to do, because there is an active AJAX running
                return;
            }
            if (this.lastCheckMillis && Date.now() - this.lastCheckMillis < 100) {
                // do nothing, because the last call was just a moment ago
                return;
            }
            this.lastCheckMillis = Date.now();
            const next = this.nextLazyLoad();
            // console.info("next %o", next); // @DEV_ONLY
            if (next) {
                this.lazyActive = true;
                const rootNode = this.getRootNode();
                const input = rootNode.getElementById(this.id + ":pageActionlazy");
                input.value = String(next);
                this.reloadWithAction(input);
            }
        }
        nextLazyLoad() {
            // find first tr in current visible area
            const rows = this.rows;
            const rowElements = this.tableBody.rows;
            let min = 0;
            let max = rowElements.length;
            // binary search
            let i;
            while (min < max) {
                i = Math.floor((max - min) / 2) + min;
                // console.log("min i max -> %d %d %d", min, i, max); // @DEV_ONLY
                if (this.isRowAboveVisibleArea(rowElements[i])) {
                    min = i + 1;
                }
                else {
                    max = i;
                }
            }
            for (i = min; i < min + rows && i < rowElements.length; i++) {
                if (this.isRowDummy(rowElements[i])) {
                    return i + 1;
                }
            }
            return null;
        }
        isRowAboveVisibleArea(tr) {
            const sheetBody = this.tableBodyDiv;
            const viewStart = sheetBody.scrollTop;
            const trEnd = tr.offsetTop + tr.clientHeight;
            return trEnd < viewStart;
        }
        isRowDummy(tr) {
            return tr.hasAttribute("dummy");
        }
        lazyResponse(event) {
            let updates;
            if (event.status === "complete") {
                updates = event.responseXML.querySelectorAll("update");
                for (let i = 0; i < updates.length; i++) {
                    const update = updates[i];
                    const id = update.getAttribute("id");
                    if (id.indexOf(":") > -1) { // is a JSF element id, but not a technical id from the framework
                        console.debug("[tobago-sheet][complete] Update after jsf.ajax complete: #" + id); // @DEV_ONLY
                        const sheet = document.getElementById(id);
                        sheet.id = id + "::lazy-temporary";
                        const page = Page.page(this);
                        page.insertAdjacentHTML("beforeend", `<div id="${id}"></div>`);
                        const sheetLoader = document.getElementById(id);
                    }
                }
            }
            else if (event.status === "success") {
                updates = event.responseXML.querySelectorAll("update");
                for (let i = 0; i < updates.length; i++) {
                    const update = updates[i];
                    const id = update.getAttribute("id");
                    if (id.indexOf(":") > -1) { // is a JSF element id, but not a technical id from the framework
                        console.debug("[tobago-sheet][success] Update after jsf.ajax complete: #" + id); // @DEV_ONLY
                        // sync the new rows into the sheet
                        const sheetLoader = document.getElementById(id);
                        const sheet = document.getElementById(id + "::lazy-temporary");
                        sheet.id = id;
                        const tbody = sheet.querySelector(".tobago-sheet-bodyTable>tbody");
                        const newRows = sheetLoader.querySelectorAll(".tobago-sheet-bodyTable>tbody>tr");
                        for (i = 0; i < newRows.length; i++) {
                            const newRow = newRows[i];
                            const rowIndex = Number(newRow.getAttribute("row-index"));
                            const row = tbody.querySelector("tr[row-index='" + rowIndex + "']");
                            // replace the old row with the new row
                            row.insertAdjacentElement("afterend", newRow);
                            tbody.removeChild(row);
                        }
                        sheetLoader.parentElement.removeChild(sheetLoader);
                        this.lazyActive = false;
                    }
                }
            }
        }
        lazyError(data) {
            console.error("Sheet lazy loading error:"
                + "\nError Description: " + data.description
                + "\nError Name: " + data.errorName
                + "\nError errorMessage: " + data.errorMessage
                + "\nResponse Code: " + data.responseCode
                + "\nResponse Text: " + data.responseText
                + "\nStatus: " + data.status
                + "\nType: " + data.type);
        }
        // tbd: how to do this in Tobago 5?
        reloadWithAction(source) {
            console.debug("reload sheet with action '" + source.id + "'"); // @DEV_ONLY
            const executeIds = this.id;
            const renderIds = this.id;
            const lazy = this.lazy;
            jsf.ajax.request(source.id, null, {
                "javax.faces.behavior.event": "reload",
                execute: executeIds,
                render: renderIds,
                onevent: lazy ? this.lazyResponse.bind(this) : undefined,
                onerror: lazy ? this.lazyError.bind(this) : undefined
            });
        }
        loadColumnWidths() {
            const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "widths");
            if (hidden) {
                return JSON.parse(hidden.getAttribute("value"));
            }
            else {
                return undefined;
            }
        }
        saveColumnWidths(widths) {
            const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "widths");
            if (hidden) {
                hidden.setAttribute("value", JSON.stringify(widths));
            }
            else {
                console.warn("ignored, should not be called, id='" + this.id + "'");
            }
        }
        isColumnRendered() {
            const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "rendered");
            return JSON.parse(hidden.getAttribute("value"));
        }
        addHeaderFillerWidth() {
            const last = document.getElementById(this.id).querySelector(".tobago-sheet-headerTable col:last-child");
            if (last) {
                last.setAttribute("width", String(Sheet.SCROLL_BAR_SIZE));
            }
        }
        mousedown(event) {
            Page.page(this).dataset.SheetMousedownData = this.id;
            // begin resizing
            console.debug("down");
            const resizeElement = event.currentTarget;
            const columnIndex = parseInt(resizeElement.dataset.tobagoColumnIndex);
            const headerColumn = this.getHeaderCols().item(columnIndex);
            const mousemoveListener = this.mousemove.bind(this);
            const mouseupListener = this.mouseup.bind(this);
            this.mousemoveData = {
                columnIndex: columnIndex,
                originalClientX: event.clientX,
                originalHeaderColumnWidth: parseInt(headerColumn.getAttribute("width")),
                mousemoveListener: mousemoveListener,
                mouseupListener: mouseupListener
            };
            document.addEventListener("mousemove", mousemoveListener);
            document.addEventListener("mouseup", mouseupListener);
        }
        mousemove(event) {
            console.debug("move");
            let delta = event.clientX - this.mousemoveData.originalClientX;
            delta = -Math.min(-delta, this.mousemoveData.originalHeaderColumnWidth - 10);
            let columnWidth = this.mousemoveData.originalHeaderColumnWidth + delta;
            this.getHeaderCols().item(this.mousemoveData.columnIndex).setAttribute("width", columnWidth);
            this.getBodyCols().item(this.mousemoveData.columnIndex).setAttribute("width", columnWidth);
            if (window.getSelection) {
                window.getSelection().removeAllRanges();
            }
            return false;
        }
        mouseup(event) {
            console.debug("up");
            // switch off the mouse move listener
            document.removeEventListener("mousemove", this.mousemoveData.mousemoveListener);
            document.removeEventListener("mouseup", this.mousemoveData.mouseupListener);
            // copy the width values from the header to the body, (and build a list of it)
            const tokens = JSON.parse(this.dataset.tobagoLayout).columns;
            const columnRendered = this.isColumnRendered();
            const columnWidths = this.loadColumnWidths();
            const bodyTable = this.getBodyTable();
            const headerCols = this.getHeaderCols();
            const bodyCols = this.getBodyCols();
            const widths = [];
            let headerBodyColCount = 0;
            for (let i = 0; i < columnRendered.length; i++) {
                if (columnRendered[i]) {
                    // last column is the filler column
                    const newWidth = parseInt(headerCols.item(headerBodyColCount).getAttribute("width"));
                    // for the hidden field
                    widths[i] = newWidth;
                    const oldWidth = parseInt(bodyCols.item(headerBodyColCount).getAttribute("width"));
                    if (oldWidth !== newWidth) {
                        bodyCols.item(headerBodyColCount).setAttribute("width", String(newWidth));
                    }
                    headerBodyColCount++;
                }
                else if (columnWidths !== undefined && columnWidths.length >= i) {
                    widths[i] = columnWidths[i];
                }
                else {
                    if (typeof tokens[i] === "number") {
                        widths[i] = 100;
                    }
                    else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
                        const intValue = parseInt(tokens[i].measure);
                        if (tokens[i].measure.lastIndexOf("px") > 0) {
                            widths[i] = intValue;
                        }
                        else if (tokens[i].measure.lastIndexOf("%") > 0) {
                            widths[i] = parseInt(bodyTable.style.width) / 100 * intValue;
                        }
                    }
                }
            }
            // store the width values in a hidden field
            this.saveColumnWidths(widths);
            return false;
        }
        scroll(event) {
            console.debug("scroll");
            const sheetBody = event.currentTarget;
            this.syncScrolling();
            // store the position in a hidden field
            const hidden = this.getHiddenScrollPosition();
            hidden.setAttribute("value", JSON.stringify([Math.round(sheetBody.scrollLeft), Math.round(sheetBody.scrollTop)]));
        }
        mousedownOnRow(event) {
            console.debug("mousedownOnRow");
            this.mousedownOnRowData = {
                x: event.clientX,
                y: event.clientY
            };
        }
        clickOnCheckbox(event) {
            const checkbox = event.currentTarget;
            if (checkbox.checked) {
                this.selectAll();
            }
            else {
                this.deselectAll();
            }
        }
        clickOnRow(event) {
            const row = event.currentTarget;
            if (row.classList.contains("tobago-sheet-columnSelector") || !Sheet.isInputElement(row)) {
                if (Math.abs(this.mousedownOnRowData.x - event.clientX)
                    + Math.abs(this.mousedownOnRowData.y - event.clientY) > 5) {
                    // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
                    // so we doesn't select the row.
                    return;
                }
                if (window.getSelection) {
                    window.getSelection().removeAllRanges();
                }
                const rows = this.getRowElements();
                const selector = this.getSelectorCheckbox(row);
                const selectionMode = this.dataset.tobagoSelectionMode;
                if ((!event.ctrlKey && !event.metaKey && !selector)
                    || selectionMode === "single" || selectionMode === "singleOrNone") {
                    this.deselectAll();
                    this.resetSelected();
                }
                const lastClickedRowIndex = parseInt(this.dataset.tobagoLastClickedRowIndex);
                if (event.shiftKey && selectionMode === "multi" && lastClickedRowIndex > -1) {
                    if (lastClickedRowIndex <= row.sectionRowIndex) {
                        this.selectRange(rows, lastClickedRowIndex, row.sectionRowIndex, true, false);
                    }
                    else {
                        this.selectRange(rows, row.sectionRowIndex, lastClickedRowIndex, true, false);
                    }
                }
                else if (selectionMode !== "singleOrNone" || !this.isRowSelected(row)) {
                    this.toggleSelection(row, selector);
                }
            }
        }
        clickOnPaging(event) {
            const element = event.currentTarget;
            const output = element.querySelector(".tobago-sheet-pagingOutput");
            output.style.display = "none";
            const input = element.querySelector(".tobago-sheet-pagingInput");
            input.style.display = "initial";
            input.focus();
            input.select();
        }
        blurPaging(event) {
            const input = event.currentTarget;
            const output = input.parentElement.querySelector(".tobago-sheet-pagingOutput");
            if (output.innerHTML !== input.value) {
                console.debug("Reloading sheet '" + this.id + "' old value='" + output.innerHTML + "' new value='" + input.value + "'");
                output.innerHTML = input.value;
                jsf.ajax.request(input.id, null, {
                    "javax.faces.behavior.event": "reload",
                    execute: this.id,
                    render: this.id
                });
            }
            else {
                console.info("no update needed");
                input.style.display = "none";
                output.style.display = "initial";
            }
        }
        syncScrolling() {
            // sync scrolling of body to header
            const header = this.getHeader();
            if (header) {
                header.scrollLeft = this.getBody().scrollLeft;
            }
        }
        getHeader() {
            return this.querySelector("tobago-sheet>header");
        }
        getHeaderTable() {
            return this.querySelector("tobago-sheet>header>table");
        }
        getHeaderCols() {
            return this.querySelectorAll("tobago-sheet>header>table>colgroup>col");
        }
        getBody() {
            return this.querySelector("tobago-sheet>.tobago-sheet-body");
        }
        getBodyTable() {
            return this.querySelector("tobago-sheet>.tobago-sheet-body>.tobago-sheet-bodyTable");
        }
        getBodyCols() {
            return this.querySelectorAll("tobago-sheet>.tobago-sheet-body>.tobago-sheet-bodyTable>colgroup>col");
        }
        getHiddenSelected() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "selected");
        }
        getHiddenScrollPosition() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "scrollPosition");
        }
        getHiddenExpanded() {
            return this.querySelector(DomUtils.escapeClientId(this.id + DomUtils.SUB_COMPONENT_SEP + "expanded"));
        }
        /**
         * Get the element, which indicates the selection
         */
        getSelectorCheckbox(row) {
            return row.querySelector("tr>td>input.tobago-sheet-columnSelector");
        }
        getRowElements() {
            return this.getBodyTable().querySelectorAll("tbody>tr");
        }
        getFirst() {
            return parseInt(this.dataset.tobagoFirst);
        }
        isRowSelected(row) {
            return this.isSelected(parseInt(row.dataset.tobagoRowIndex));
        }
        isSelected(rowIndex) {
            const value = JSON.parse(this.getHiddenSelected().value);
            return value.indexOf(rowIndex) > -1;
        }
        resetSelected() {
            this.getHiddenSelected().value = JSON.stringify([]);
        }
        toggleSelection(row, checkbox) {
            this.dataset.tobagoLastClickedRowIndex = String(row.sectionRowIndex);
            if (checkbox && !checkbox.disabled) {
                const selected = this.getHiddenSelected();
                const rowIndex = Number(row.getAttribute("row-index"));
                if (this.isSelected(rowIndex)) {
                    this.deselectRow(selected, rowIndex, row, checkbox);
                }
                else {
                    this.selectRow(selected, rowIndex, row, checkbox);
                }
            }
        }
        selectAll() {
            const rows = this.getRowElements();
            this.selectRange(rows, 0, rows.length - 1, true, false);
        }
        deselectAll() {
            const rows = this.getRowElements();
            this.selectRange(rows, 0, rows.length - 1, false, true);
        }
        toggleAll() {
            const rows = this.getRowElements();
            this.selectRange(rows, 0, rows.length - 1, true, true);
        }
        selectRange(rows, first, last, selectDeselected, deselectSelected) {
            const selected = this.getHiddenSelected();
            const value = new Set(JSON.parse(selected.value));
            for (let i = first; i <= last; i++) {
                const row = rows.item(i);
                const checkbox = this.getSelectorCheckbox(row);
                if (checkbox && !checkbox.disabled) {
                    const rowIndex = Number(row.getAttribute("row-index"));
                    const on = value.has(rowIndex);
                    if (selectDeselected && !on) {
                        this.selectRow(selected, rowIndex, row, checkbox);
                    }
                    else if (deselectSelected && on) {
                        this.deselectRow(selected, rowIndex, row, checkbox);
                    }
                }
            }
        }
        /**
         * @param selected input-element type=hidden: Hidden field with the selection state information
         * @param rowIndex int: zero based index of the row.
         * @param row tr-element: the row.
         * @param checkbox input-element: selector in the row.
         */
        selectRow(selected, rowIndex, row, checkbox) {
            const selectedSet = new Set(JSON.parse(selected.value));
            selected.value = JSON.stringify(Array.from(selectedSet.add(rowIndex)));
            row.classList.add("tobago-sheet-row-markup-selected");
            row.classList.add("table-info");
            checkbox.checked = true;
            setTimeout(function () {
                checkbox.checked = true;
            }, 0);
        }
        /**
         * @param selected input-element type=hidden: Hidden field with the selection state information
         * @param rowIndex int: zero based index of the row.
         * @param row tr-element: the row.
         * @param checkbox input-element: selector in the row.
         */
        deselectRow(selected, rowIndex, row, checkbox) {
            const selectedSet = new Set(JSON.parse(selected.value));
            selectedSet.delete(rowIndex);
            selected.value = JSON.stringify(Array.from(selectedSet));
            row.classList.remove("tobago-sheet-row-markup-selected");
            row.classList.remove("table-info");
            checkbox.checked = false;
            // XXX check if this is still needed... Async because of TOBAGO-1312
            setTimeout(function () {
                checkbox.checked = false;
            }, 0);
        }
    }
    Sheet.SCROLL_BAR_SIZE = Sheet.getScrollBarSize();
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-sheet") == null) {
            window.customElements.define("tobago-sheet", Sheet);
        }
    });

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
    class SplitLayout extends HTMLElement {
        constructor() {
            super();
            let first = true;
            let justAdded = false;
            for (let child of this.children) {
                if (justAdded) { // skip, because the just added had enlarges the list of children
                    justAdded = false;
                    continue;
                }
                if (getComputedStyle(child).display === "none") {
                    continue;
                }
                if (first) { // the first needs no splitter handle
                    first = false;
                    continue;
                }
                const splitter = document.createElement("div");
                splitter.classList.add(this.orientation === "horizontal" ? "tobago-splitLayout-horizontal" : "tobago-splitLayout-vertical");
                justAdded = true;
                splitter.addEventListener("mousedown", this.start.bind(this));
                child.parentElement.insertBefore(splitter, child);
            }
        }
        get orientation() {
            return this.getAttribute("orientation");
        }
        set orientation(orientation) {
            this.setAttribute("orientation", orientation);
        }
        start(event) {
            event.preventDefault();
            const splitter = event.target;
            const previous = DomUtils.previousElementSibling(splitter);
            this.offset = this.orientation === "horizontal"
                ? event.pageX - previous.offsetWidth : event.pageY - previous.offsetHeight;
            const mousedown = SplitLayoutMousedown.save(event, splitter);
            document.addEventListener("mousemove", this.move.bind(this));
            document.addEventListener("mouseup", this.stop.bind(this));
            const previousArea = mousedown.previous;
            if (this.orientation === "horizontal") {
                previousArea.style.width = String(previousArea.offsetWidth + "px");
            }
            else {
                previousArea.style.height = String(previousArea.offsetHeight + "px");
            }
            previousArea.style.flexGrow = "inherit";
            previousArea.style.flexBasis = "auto";
            console.debug("initial width/height = '%s'", (this.orientation === "horizontal" ? previousArea.style.width : previousArea.style.height));
        }
        move(event) {
            event.preventDefault();
            const data = SplitLayoutMousedown.load();
            const previousArea = data.previous;
            if (previousArea) {
                if (this.orientation === "horizontal") {
                    previousArea.style.width = String(event.pageX - this.offset) + "px";
                }
                else {
                    previousArea.style.height = String(event.pageY - this.offset) + "px";
                }
            }
        }
        stop(event) {
            document.removeEventListener("mousemove", this.move.bind(this)); // fixme remove the real added
            document.removeEventListener("mouseup", this.stop.bind(this)); // fixme remove the real added
            SplitLayoutMousedown.remove();
        }
    }
    class SplitLayoutMousedown {
        constructor(data) {
            if (data) {
                this.data = typeof data === "string" ? JSON.parse(data) : data;
            }
        }
        static save(event, splitter) {
            const horizontal = splitter.classList.contains("tobago-splitLayout-horizontal");
            const previous = DomUtils.previousElementSibling(splitter);
            const data = {
                splitLayoutId: splitter.parentElement.id,
                horizontal: horizontal,
                splitterIndex: this.indexOfSplitter(splitter, horizontal)
            };
            Page.page(splitter).dataset.SplitLayoutMousedownData = JSON.stringify(data);
            return new SplitLayoutMousedown(data);
        }
        static load() {
            const element = document.documentElement; // XXX this might be the wrong element in case of shadow dom
            return new SplitLayoutMousedown(Page.page(element).dataset.SplitLayoutMousedownData);
        }
        static remove() {
            const element = document.documentElement; // XXX this might be the wrong element in case of shadow dom
            Page.page(element).dataset.SplitLayoutMousedownData = null;
        }
        static indexOfSplitter(splitter, horizontal) {
            const list = splitter.parentElement.getElementsByClassName(horizontal ? "tobago-splitLayout-horizontal" : "tobago-splitLayout-vertical");
            for (let i = 0; i < list.length; i++) {
                if (list.item(i) === splitter) {
                    return i;
                }
            }
            return -1;
        }
        get splitter() {
            return this.data ? document.getElementById(this.data.splitLayoutId).getElementsByClassName(this.data.horizontal ? "tobago-splitLayout-horizontal" : "tobago-splitLayout-vertical")
                .item(this.data.splitterIndex) : null;
        }
        get previous() {
            return this.splitter ? DomUtils.previousElementSibling(this.splitter) : null;
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-split-layout") == null) {
            window.customElements.define("tobago-split-layout", SplitLayout);
        }
    });

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
    class Stars extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            const hiddenInput = this.querySelector("input[type=hidden]");
            const container = this.querySelector(".tobago-stars-container");
            const tooltip = container.querySelector(".tobago-stars-tooltip");
            const selected = container.querySelector(".tobago-stars-selected");
            const unselected = container.querySelector(".tobago-stars-unselected");
            const preselected = container.querySelector(".tobago-stars-preselected");
            const slider = container.querySelector(".tobago-stars-slider");
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
            }
            else if (placeholder) {
                selected.classList.add("tobago-placeholder");
                const placeholderValue = 100 * placeholder / max;
                selected.style["width"] = placeholderValue + "%";
                unselected.style["left"] = placeholderValue + "%";
                unselected.style["width"] = 100 - placeholderValue + "%";
            }
            if (!readonly && !disabled) {
                /* preselectMode is a Workaround for IE11: fires change event instead of input event */
                let preselectMode = false;
                slider.addEventListener("mousedown", function (event) {
                    preselectMode = true;
                });
                slider.addEventListener("mouseup", function (event) {
                    preselectMode = false;
                    selectStars();
                });
                slider.addEventListener("input", function (event) {
                    preselectStars();
                });
                slider.addEventListener("touchend", function (event) {
                    /* Workaround for mobile devices. TODO: fire AJAX request for 'touchend' */
                    // slider.trigger("change");
                    slider.dispatchEvent(new Event("change"));
                });
                slider.addEventListener("change", function (event) {
                    if (preselectMode) {
                        preselectStars();
                    }
                    else {
                        selectStars();
                    }
                });
                slider.addEventListener("touchstart", touchstart);
                slider.addEventListener("touchmove", touchstart);
            }
            // XXX current issue: on ios-Safari select 5 stars and than click on 1 star doesn't work on labeled component.
            function touchstart(event) {
                /* Workaround for Safari browser on iPhone */
                const target = event.currentTarget;
                const sliderValue = (parseInt(target.max) / target.offsetWidth)
                    * (event.touches[0].pageX - DomUtils.offset(slider).left);
                if (sliderValue > parseInt(target.max)) {
                    slider.value = target.max;
                }
                else if (sliderValue < parseInt(target.min)) {
                    slider.value = target.min;
                }
                else {
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
                }
                else {
                    tooltip.textContent = "";
                    tooltip.classList.add("trash");
                    if (placeholder) {
                        preselected.classList.add("show");
                        preselected.style["width"] = (100 * placeholder / max) + "%";
                    }
                    else {
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
                }
                else {
                    if (placeholder) {
                        selected.classList.add("tobago-placeholder");
                        const placeholderValue = 100 * placeholder / max;
                        selected.style["width"] = placeholderValue + "%";
                        unselected.style["left"] = placeholderValue + "%";
                        unselected.style["width"] = 100 - placeholderValue + "%";
                    }
                    else {
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
    document.addEventListener("DOMContentLoaded", function (event) {
        window.customElements.define("tobago-stars", Stars);
    });

    // Polyfill for element.matches, to support Internet Explorer. It's a relatively
    // simple polyfill, so we'll just include it rather than require the user to
    // include the polyfill themselves. Adapted from
    // https://developer.mozilla.org/en-US/docs/Web/API/Element/matches#Polyfill
    const matches = (element, selector) => {
      return element.matches
        ? element.matches(selector)
        : element.msMatchesSelector
        ? element.msMatchesSelector(selector)
        : element.webkitMatchesSelector
        ? element.webkitMatchesSelector(selector)
        : null
    };

    // Polyfill for element.closest, to support Internet Explorer. It's a relatively

    const closestPolyfill = (el, selector) => {
      let element = el;
      while (element && element.nodeType === 1) {
        if (matches(element, selector)) {
          return element
        }
        element = element.parentNode;
      }
      return null
    };

    const closest = (element, selector) => {
      return element.closest
        ? element.closest(selector)
        : closestPolyfill(element, selector)
    };

    // Returns true if the value has a "then" function. Adapted from
    // https://github.com/graphql/graphql-js/blob/499a75939f70c4863d44149371d6a99d57ff7c35/src/jsutils/isPromise.js
    const isPromise = value => Boolean(value && typeof value.then === 'function');

    class AutocompleteCore {
      value = ''
      searchCounter = 0
      results = []
      selectedIndex = -1

      constructor({
        search,
        autoSelect = false,
        setValue = () => {},
        setAttribute = () => {},
        onUpdate = () => {},
        onSubmit = () => {},
        onShow = () => {},
        onHide = () => {},
        onLoading = () => {},
        onLoaded = () => {},
      } = {}) {
        this.search = isPromise(search)
          ? search
          : value => Promise.resolve(search(value));
        this.autoSelect = autoSelect;
        this.setValue = setValue;
        this.setAttribute = setAttribute;
        this.onUpdate = onUpdate;
        this.onSubmit = onSubmit;
        this.onShow = onShow;
        this.onHide = onHide;
        this.onLoading = onLoading;
        this.onLoaded = onLoaded;
      }

      handleInput = event => {
        const { value } = event.target;
        this.updateResults(value);
        this.value = value;
      }

      handleKeyDown = event => {
        const { key } = event;

        switch (key) {
          case 'Up': // IE/Edge
          case 'Down': // IE/Edge
          case 'ArrowUp':
          case 'ArrowDown': {
            const selectedIndex =
              key === 'ArrowUp' || key === 'Up'
                ? this.selectedIndex - 1
                : this.selectedIndex + 1;
            event.preventDefault();
            this.handleArrows(selectedIndex);
            break
          }
          case 'Tab': {
            this.selectResult();
            break
          }
          case 'Enter': {
            const selectedResult = this.results[this.selectedIndex];
            this.selectResult();
            this.onSubmit(selectedResult);
            break
          }
          case 'Esc': // IE/Edge
          case 'Escape': {
            this.hideResults();
            this.setValue();
            break
          }
          default:
            return
        }
      }

      handleFocus = event => {
        const { value } = event.target;
        this.updateResults(value);
        this.value = value;
      }

      handleBlur = () => {
        this.hideResults();
      }

      // The mousedown event fires before the blur event. Calling preventDefault() when
      // the results list is clicked will prevent it from taking focus, firing the
      // blur event on the input element, and closing the results list before click fires.
      handleResultMouseDown = event => {
        event.preventDefault();
      }

      handleResultClick = event => {
        const { target } = event;
        const result = closest(target, '[data-result-index]');
        if (result) {
          this.selectedIndex = parseInt(result.dataset.resultIndex, 10);
          const selectedResult = this.results[this.selectedIndex];
          this.selectResult();
          this.onSubmit(selectedResult);
        }
      }

      handleArrows = selectedIndex => {
        // Loop selectedIndex back to first or last result if out of bounds
        const resultsCount = this.results.length;
        this.selectedIndex =
          ((selectedIndex % resultsCount) + resultsCount) % resultsCount;

        // Update results and aria attributes
        this.onUpdate(this.results, this.selectedIndex);
      }

      selectResult = () => {
        const selectedResult = this.results[this.selectedIndex];
        if (selectedResult) {
          this.setValue(selectedResult);
        }
        this.hideResults();
      }

      updateResults = value => {
        const currentSearch = ++this.searchCounter;
        this.onLoading();
        this.search(value).then(results => {
          if (currentSearch !== this.searchCounter) {
            return
          }
          this.results = results;
          this.onLoaded();

          if (this.results.length === 0) {
            this.hideResults();
            return
          }

          this.selectedIndex = this.autoSelect ? 0 : -1;
          this.onUpdate(this.results, this.selectedIndex);
          this.showResults();
        });
      }

      showResults = () => {
        this.setAttribute('aria-expanded', true);
        this.onShow();
      }

      hideResults = () => {
        this.selectedIndex = -1;
        this.results = [];
        this.setAttribute('aria-expanded', false);
        this.setAttribute('aria-activedescendant', '');
        this.onUpdate(this.results, this.selectedIndex);
        this.onHide();
      }

      // Make sure selected result isn't scrolled out of view
      checkSelectedResultVisible = resultsElement => {
        const selectedResultElement = resultsElement.querySelector(
          `[data-result-index="${this.selectedIndex}"]`
        );
        if (!selectedResultElement) {
          return
        }

        const resultsPosition = resultsElement.getBoundingClientRect();
        const selectedPosition = selectedResultElement.getBoundingClientRect();

        if (selectedPosition.top < resultsPosition.top) {
          // Element is above viewable area
          resultsElement.scrollTop -= resultsPosition.top - selectedPosition.top;
        } else if (selectedPosition.bottom > resultsPosition.bottom) {
          // Element is below viewable area
          resultsElement.scrollTop +=
            selectedPosition.bottom - resultsPosition.bottom;
        }
      }
    }

    // Generates a unique ID, with optional prefix. Adapted from
    // https://github.com/lodash/lodash/blob/61acdd0c295e4447c9c10da04e287b1ebffe452c/uniqueId.js
    let idCounter = 0;
    const uniqueId = (prefix = '') => `${prefix}${++idCounter}`;

    // Calculates whether element2 should be above or below element1. Always
    // places element2 below unless all of the following:
    // 1. There isn't enough visible viewport below to fit element2
    // 2. There is more room above element1 than there is below
    // 3. Placing elemen2 above 1 won't overflow window
    const getRelativePosition = (element1, element2) => {
      const position1 = element1.getBoundingClientRect();
      const position2 = element2.getBoundingClientRect();

      const positionAbove =
        /* 1 */ position1.bottom + position2.height > window.innerHeight &&
        /* 2 */ window.innerHeight - position1.bottom < position1.top &&
        /* 3 */ window.pageYOffset + position1.top - position2.height > 0;

      return positionAbove ? 'above' : 'below'
    };

    // Credit David Walsh (https://davidwalsh.name/javascript-debounce-function)

    // Returns a function, that, as long as it continues to be invoked, will not
    // be triggered. The function will be called after it stops being called for
    // N milliseconds. If `immediate` is passed, trigger the function on the
    // leading edge, instead of the trailing.
    const debounce$1 = (func, wait, immediate) => {
      let timeout;

      return function executedFunction() {
        const context = this;
        const args = arguments;

        const later = function() {
          timeout = null;
          if (!immediate) func.apply(context, args);
        };

        const callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);

        if (callNow) func.apply(context, args);
      }
    };

    // Creates a props object with overridden toString function. toString returns an attributes
    // string in the format: `key1="value1" key2="value2"` for easy use in an HTML string.
    class Props {
      constructor(index, selectedIndex, baseClass) {
        this.id = `${baseClass}-result-${index}`;
        this.class = `${baseClass}-result`;
        this['data-result-index'] = index;
        this.role = 'option';
        if (index === selectedIndex) {
          this['aria-selected'] = 'true';
        }
      }

      toString() {
        return Object.keys(this).reduce(
          (str, key) => `${str} ${key}="${this[key]}"`,
          ''
        )
      }
    }

    class Autocomplete {
      expanded = false
      loading = false
      position = {}
      resetPosition = true

      constructor(
        root,
        {
          search,
          onSubmit = () => {},
          onUpdate = () => {},
          baseClass = 'autocomplete',
          autoSelect,
          getResultValue = result => result,
          renderResult,
          debounceTime = 0,
        } = {}
      ) {
        this.root = typeof root === 'string' ? document.querySelector(root) : root;
        this.input = this.root.querySelector('input');
        this.resultList = this.root.querySelector('ul');
        this.baseClass = baseClass;
        this.getResultValue = getResultValue;
        this.onUpdate = onUpdate;
        if (typeof renderResult === 'function') {
          this.renderResult = renderResult;
        }

        const core = new AutocompleteCore({
          search,
          autoSelect,
          setValue: this.setValue,
          setAttribute: this.setAttribute,
          onUpdate: this.handleUpdate,
          onSubmit,
          onShow: this.handleShow,
          onHide: this.handleHide,
          onLoading: this.handleLoading,
          onLoaded: this.handleLoaded,
        });
        if (debounceTime > 0) {
          core.handleInput = debounce$1(core.handleInput, debounceTime);
        }
        this.core = core;

        this.initialize();
      }

      // Set up aria attributes and events
      initialize = () => {
        this.root.style.position = 'relative';

        this.input.setAttribute('role', 'combobox');
        this.input.setAttribute('autocomplete', 'off');
        this.input.setAttribute('autocapitalize', 'off');
        this.input.setAttribute('autocorrect', 'off');
        this.input.setAttribute('spellcheck', 'false');
        this.input.setAttribute('aria-autocomplete', 'list');
        this.input.setAttribute('aria-haspopup', 'listbox');
        this.input.setAttribute('aria-expanded', 'false');

        this.resultList.setAttribute('role', 'listbox');
        this.resultList.style.position = 'absolute';
        this.resultList.style.zIndex = '1';
        this.resultList.style.width = '100%';
        this.resultList.style.boxSizing = 'border-box';

        // Generate ID for results list if it doesn't have one
        if (!this.resultList.id) {
          this.resultList.id = uniqueId(`${this.baseClass}-result-list-`);
        }
        this.input.setAttribute('aria-owns', this.resultList.id);

        document.body.addEventListener('click', this.handleDocumentClick);
        this.input.addEventListener('input', this.core.handleInput);
        this.input.addEventListener('keydown', this.core.handleKeyDown);
        this.input.addEventListener('focus', this.core.handleFocus);
        this.input.addEventListener('blur', this.core.handleBlur);
        this.resultList.addEventListener(
          'mousedown',
          this.core.handleResultMouseDown
        );
        this.resultList.addEventListener('click', this.core.handleResultClick);
        this.updateStyle();
      }

      setAttribute = (attribute, value) => {
        this.input.setAttribute(attribute, value);
      }

      setValue = result => {
        this.input.value = result ? this.getResultValue(result) : '';
      }

      renderResult = (result, props) =>
        `<li ${props}>${this.getResultValue(result)}</li>`

      handleUpdate = (results, selectedIndex) => {
        this.resultList.innerHTML = '';
        results.forEach((result, index) => {
          const props = new Props(index, selectedIndex, this.baseClass);
          const resultHTML = this.renderResult(result, props);
          if (typeof resultHTML === 'string') {
            this.resultList.insertAdjacentHTML('beforeend', resultHTML);
          } else {
            this.resultList.insertAdjacentElement('beforeend', resultHTML);
          }
        });

        this.input.setAttribute(
          'aria-activedescendant',
          selectedIndex > -1 ? `${this.baseClass}-result-${selectedIndex}` : ''
        );

        if (this.resetPosition) {
          this.resetPosition = false;
          this.position = getRelativePosition(this.input, this.resultList);
          this.updateStyle();
        }
        this.core.checkSelectedResultVisible(this.resultList);
        this.onUpdate(results, selectedIndex);
      }

      handleShow = () => {
        this.expanded = true;
        this.updateStyle();
      }

      handleHide = () => {
        this.expanded = false;
        this.resetPosition = true;
        this.updateStyle();
      }

      handleLoading = () => {
        this.loading = true;
        this.updateStyle();
      }

      handleLoaded = () => {
        this.loading = false;
        this.updateStyle();
      }

      handleDocumentClick = event => {
        if (this.root.contains(event.target)) {
          return
        }
        this.core.hideResults();
      }

      updateStyle = () => {
        this.root.dataset.expanded = this.expanded;
        this.root.dataset.loading = this.loading;
        this.root.dataset.position = this.position;

        this.resultList.style.visibility = this.expanded ? 'visible' : 'hidden';
        this.resultList.style.pointerEvents = this.expanded ? 'auto' : 'none';
        if (this.position === 'below') {
          this.resultList.style.bottom = null;
          this.resultList.style.top = '100%';
        } else {
          this.resultList.style.top = null;
          this.resultList.style.bottom = '100%';
        }
      }
    }

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
    class Suggest extends HTMLElement {
        constructor() {
            super();
        }
        get hiddenInput() {
            return this.querySelector(":scope > input[type=hidden]");
        }
        get suggestInput() {
            const root = this.getRootNode();
            return root.getElementById(this.for);
        }
        get base() {
            return this.suggestInput.closest("tobago-in");
        }
        get for() {
            return this.getAttribute("for");
        }
        set for(forValue) {
            this.setAttribute("for", forValue);
        }
        get items() {
            return JSON.parse(this.getAttribute("items"));
        }
        connectedCallback() {
            console.log("* autocomplete init *********************************************************************");
            this.base.classList.add("autocomplete");
            this.suggestInput.classList.add("autocomplete-input");
            this.suggestInput.insertAdjacentHTML("afterend", `<ul class="autocomplete-result-list"></ul>`);
            const options = {
                search: input => {
                    console.debug("input = '" + input + "'");
                    if (input.length < 1) {
                        return [];
                    }
                    const inputLower = input.toLowerCase();
                    let strings = this.items.filter(country => {
                        return country.toLowerCase().startsWith(inputLower);
                    });
                    console.debug("out   = '" + strings + "'");
                    return strings;
                }
            };
            this.autocomplete = new Autocomplete(this.base, options);
            console.log(this.autocomplete);
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-suggest") == null) {
            window.customElements.define("tobago-suggest", Suggest);
        }
    });

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
    class TabGroup extends HTMLElement {
        constructor() {
            super();
            this.hiddenInput = this.querySelector(":scope > input[type=hidden]");
        }
        connectedCallback() {
        }
        get switchType() {
            return this.getAttribute("switch-type");
        }
        get tabs() {
            return this.querySelectorAll(":scope > .card-header > .card-header-tabs > tobago-tab");
        }
        getSelectedTab() {
            return this.querySelector(":scope > .card-header > .card-header-tabs > tobago-tab[index='" + this.selected + "']");
        }
        get selected() {
            return parseInt(this.hiddenInput.value);
        }
        set selected(index) {
            this.hiddenInput.value = String(index);
        }
    }
    class Tab extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            let navLink = this.navLink;
            if (!navLink.classList.contains("disabled")) {
                navLink.addEventListener("click", this.select.bind(this));
            }
        }
        get index() {
            return parseInt(this.getAttribute("index"));
        }
        get navLink() {
            return this.querySelector(".nav-link");
        }
        get tabGroup() {
            return this.closest("tobago-tab-group");
        }
        select(event) {
            const tabGroup = this.tabGroup;
            const old = tabGroup.getSelectedTab();
            tabGroup.selected = this.index;
            switch (tabGroup.switchType) {
                case "client":
                    old.navLink.classList.remove("active");
                    this.navLink.classList.add("active");
                    old.content.classList.remove("active");
                    this.content.classList.add("active");
                    break;
                case "reloadTab":
                    // will be done by <tobago-behavior>
                    break;
                case "reloadPage":
                    // will be done by <tobago-behavior>
                    break;
                case "none": // todo
                    console.error("Not implemented yet: %s", tabGroup.switchType);
                    break;
                default:
                    console.error("Unknown switchType='%s'", tabGroup.switchType);
                    break;
            }
        }
        get content() {
            return this.closest("tobago-tab-group")
                .querySelector(":scope > .card-body.tab-content > .tab-pane[index='" + this.index + "']");
        }
    }
    class TabContent extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
        }
        get index() {
            return parseInt(this.getAttribute("index"));
        }
    }
    document.addEventListener("DOMContentLoaded", function (event) {
        window.customElements.define("tobago-tab", Tab);
        window.customElements.define("tobago-tab-content", TabContent);
        window.customElements.define("tobago-tab-group", TabGroup);
    });

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
    class Textarea extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.textarea.addEventListener("focus", Focus.setLastFocusId);
        }
        get textarea() {
            return this.querySelector(DomUtils.escapeClientId(this.id + DomUtils.SUB_COMPONENT_SEP + "field"));
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-textarea") == null) {
            window.customElements.define("tobago-textarea", Textarea);
        }
    });

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
    var Selectable;
    (function (Selectable) {
        Selectable[Selectable["none"] = 0] = "none";
        Selectable[Selectable["multi"] = 1] = "multi";
        Selectable[Selectable["single"] = 2] = "single";
        Selectable[Selectable["singleOrNone"] = 3] = "singleOrNone";
        Selectable[Selectable["multiLeafOnly"] = 4] = "multiLeafOnly";
        Selectable[Selectable["singleLeafOnly"] = 5] = "singleLeafOnly";
        Selectable[Selectable["sibling"] = 6] = "sibling";
        Selectable[Selectable["siblingLeafOnly"] = 7] = "siblingLeafOnly";
        Selectable[Selectable["multiCascade"] = 8] = "multiCascade"; // Multi selection possible. When (de)selecting an item, the subtree will also be (un)selected.
    })(Selectable || (Selectable = {}));

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
    class Tree extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
        }
        clearSelectedNodes() {
            this.hiddenInputSelected.value = "[]"; //empty set
        }
        addSelectedNode(selectedNode) {
            const selectedNodes = new Set(JSON.parse(this.hiddenInputSelected.value));
            selectedNodes.add(selectedNode);
            this.hiddenInputSelected.value = JSON.stringify(Array.from(selectedNodes));
        }
        getSelectedNodes() {
            let queryString = "";
            for (const selectedNodeIndex of JSON.parse(this.hiddenInputSelected.value)) {
                if (queryString.length > 0) {
                    queryString += ", ";
                }
                queryString += "tobago-tree-node[index='" + selectedNodeIndex + "']";
            }
            if (queryString.length > 0) {
                return this.querySelectorAll(queryString);
            }
            else {
                return null;
            }
        }
        deleteSelectedNode(selectedNode) {
            const selectedNodes = new Set(JSON.parse(this.hiddenInputSelected.value));
            selectedNodes.delete(selectedNode);
            this.hiddenInputSelected.value = JSON.stringify(Array.from(selectedNodes));
        }
        get hiddenInputSelected() {
            return this.querySelector(":scope > .tobago-tree-selected");
        }
        clearExpandedNodes() {
            this.hiddenInputExpanded.value = "[]"; //empty set
        }
        addExpandedNode(expandedNode) {
            const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
            expandedNodes.add(expandedNode);
            this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
        }
        deleteExpandedNode(expandedNode) {
            const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
            expandedNodes.delete(expandedNode);
            this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
        }
        get hiddenInputExpanded() {
            return this.querySelector(":scope > .tobago-tree-expanded");
        }
        get selectable() {
            return Selectable[this.getAttribute("selectable")];
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-tree") == null) {
            window.customElements.define("tobago-tree", Tree);
        }
    });

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
    class TreeListbox extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.applySelected();
            for (const listbox of this.listboxes) {
                if (!listbox.disabled) {
                    listbox.addEventListener("change", this.select.bind(this));
                }
            }
        }
        select(event) {
            const listbox = event.currentTarget;
            this.unselectDescendants(listbox);
            this.setSelected();
            this.applySelected();
        }
        unselectDescendants(select) {
            let unselect = false;
            for (const listbox of this.listboxes) {
                if (unselect) {
                    const checkedOption = listbox.querySelector("option:checked");
                    if (checkedOption) {
                        checkedOption.selected = false;
                    }
                }
                else if (listbox.id === select.id) {
                    unselect = true;
                }
            }
        }
        setSelected() {
            const selected = [];
            for (const level of this.levelElements) {
                const checkedOption = level
                    .querySelector(".tobago-treeListbox-select:not(.d-none) option:checked");
                if (checkedOption) {
                    selected.push(checkedOption.index);
                }
            }
            this.hiddenInput.value = JSON.stringify(selected);
        }
        applySelected() {
            const selected = JSON.parse(this.hiddenInput.value);
            let nextActiveSelect = this.querySelector(".tobago-treeListbox-select");
            const levelElements = this.levelElements;
            for (let i = 0; i < levelElements.length; i++) {
                const level = levelElements[i];
                for (const select of this.getSelectElements(level)) {
                    if ((nextActiveSelect !== null && select.id === nextActiveSelect.id)
                        || (nextActiveSelect === null && select.disabled)) {
                        const check = i < selected.length ? selected[i] : null;
                        this.show(select, check);
                        nextActiveSelect = this.getNextActiveSelect(select, check);
                    }
                    else {
                        this.hide(select);
                    }
                }
            }
        }
        getSelectElements(level) {
            return level.querySelectorAll(".tobago-treeListbox-select");
        }
        getNextActiveSelect(select, check) {
            if (check !== null) {
                const option = select.querySelectorAll("option")[check];
                return this.querySelector(DomUtils.escapeClientId(option.id + DomUtils.SUB_COMPONENT_SEP + "parent"));
            }
            else {
                return null;
            }
        }
        show(select, check) {
            select.classList.remove("d-none");
            const checkedOption = select.querySelector("option:checked");
            if (checkedOption && checkedOption.index !== check) {
                checkedOption.selected = false;
            }
            if (check !== null && checkedOption.index !== check) {
                select.querySelectorAll("option")[check].selected = true;
            }
        }
        hide(select) {
            select.classList.add("d-none");
            const checkedOption = select.querySelector("option:checked");
            if (checkedOption) {
                checkedOption.selected = false;
            }
        }
        get listboxes() {
            return this.querySelectorAll(".tobago-treeListbox-select");
        }
        get levelElements() {
            return this.querySelectorAll(".tobago-treeListbox-level");
        }
        get hiddenInput() {
            return this.querySelector(DomUtils.escapeClientId(this.id + DomUtils.SUB_COMPONENT_SEP + "selected"));
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-tree-listbox") == null) {
            window.customElements.define("tobago-tree-listbox", TreeListbox);
        }
    });

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
    class TreeNode extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            if (this.expandable && this.toggles !== null) {
                this.toggles.forEach(element => element.addEventListener("click", this.toggleNode.bind(this)));
            }
        }
        toggleNode(event) {
            if (this.expanded) {
                for (const icon of this.icons) {
                    icon.classList.remove(icon.dataset.tobagoOpen);
                    icon.classList.add(icon.dataset.tobagoClosed);
                }
                for (const image of this.images) {
                    if (image.dataset.tobagoClosed) {
                        image.src = image.dataset.tobagoClosed;
                    }
                    else {
                        image.src = image.dataset.tobagoOpen;
                    }
                }
                this.deleteExpandedNode(this.index);
                this.classList.remove("tobago-treeNode-markup-expanded");
                this.hideNodes(this.treeChildNodes);
                if (this.tree) {
                    this.ajax(event, false);
                }
            }
            else {
                for (const icon of this.icons) {
                    icon.classList.remove(icon.dataset.tobagoClosed);
                    icon.classList.add(icon.dataset.tobagoOpen);
                }
                for (const image of this.images) {
                    if (image.dataset.tobagoOpen) {
                        image.src = image.dataset.tobagoOpen;
                    }
                    else {
                        image.src = image.dataset.tobagoClosed;
                    }
                }
                this.addExpandedNode(this.index);
                this.classList.add("tobago-treeNode-markup-expanded");
                this.showNodes(this.treeChildNodes);
                if (this.tree) {
                    this.ajax(event, this.treeChildNodes.length === 0);
                }
            }
        }
        ajax(event, renderTree) {
            jsf.ajax.request(this.id, event, {
                "javax.faces.behavior.event": "change",
                execute: this.tree.id,
                render: renderTree ? this.tree.id : null
            });
        }
        hideNodes(treeChildNodes) {
            for (const treeChildNode of treeChildNodes) {
                if (treeChildNode.sheet) {
                    treeChildNode.closest(".tobago-sheet-row").classList.add("d-none");
                }
                else {
                    treeChildNode.classList.add("d-none");
                }
                this.hideNodes(treeChildNode.treeChildNodes);
            }
        }
        showNodes(treeChildNodes) {
            for (const treeChildNode of treeChildNodes) {
                if (treeChildNode.sheet) {
                    treeChildNode.closest(".tobago-sheet-row").classList.remove("d-none");
                }
                else {
                    treeChildNode.classList.remove("d-none");
                }
                this.showNodes(treeChildNode.treeChildNodes);
            }
        }
        addExpandedNode(expandedNode) {
            const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
            expandedNodes.add(expandedNode);
            this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
        }
        deleteExpandedNode(expandedNode) {
            const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
            expandedNodes.delete(expandedNode);
            this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
        }
        get tree() {
            return this.closest("tobago-tree");
        }
        get sheet() {
            return this.closest("tobago-sheet");
        }
        get expandable() {
            return this.getAttribute("expandable") === "expandable";
        }
        get expanded() {
            for (const expandedNodeIndex of this.expandedNodes) {
                if (expandedNodeIndex === this.index) {
                    return true;
                }
            }
            return false;
        }
        get expandedNodes() {
            return new Set(JSON.parse(this.hiddenInputExpanded.value));
        }
        get hiddenInputExpanded() {
            if (this.tree) {
                return this.tree.hiddenInputExpanded;
            }
            else if (this.sheet) {
                return this.sheet.getHiddenExpanded();
            }
            else {
                console.error("Cannot detect 'tobago-tree' or 'tobago-sheet'.");
                return null;
            }
        }
        get treeChildNodes() {
            if (this.sheet) {
                return this.closest("tbody").querySelectorAll("tobago-tree-node[parent='" + this.id + "']");
            }
            else if (this.tree) {
                return this.parentElement.querySelectorAll("tobago-tree-node[parent='" + this.id + "']");
            }
            else {
                console.error("Cannot detect 'tobago-tree' or 'tobago-sheet'.");
                return null;
            }
        }
        get toggles() {
            return this.querySelectorAll(".tobago-treeNode-toggle");
        }
        get icons() {
            return this.querySelectorAll(".tobago-treeNode-toggle i");
        }
        get images() {
            return this.querySelectorAll(".tobago-treeNode-toggle img");
        }
        get index() {
            return Number(this.getAttribute("index"));
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-tree-node") == null) {
            window.customElements.define("tobago-tree-node", TreeNode);
        }
    });

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
    class TreeSelect extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.input.addEventListener("change", this.select.bind(this));
        }
        select(event) {
            switch (this.input.type) {
                case "radio":
                    this.tree.clearSelectedNodes();
                    this.tree.addSelectedNode(this.treeNode.index);
                    break;
                case "checkbox":
                    if (this.input.checked) {
                        this.tree.addSelectedNode(this.treeNode.index);
                    }
                    else {
                        this.tree.deleteSelectedNode(this.treeNode.index);
                    }
                    if (this.tree.selectable === Selectable.multiCascade) {
                        let treeNodeIds = [];
                        this.selectChildren(this.treeSelectChildren, this.input.checked, treeNodeIds);
                        /*if (treeNodeIds.length > 0) {
                          for (const id of treeNodeIds) {
                            let ts: TreeSelect = document.getElementById(id).querySelector("tobago-tree-select") as TreeSelect;
                            ts.input.dispatchEvent(new Event("change", {bubbles: false}));
                          }
                        }*/
                    }
                    break;
            }
        }
        selectChildren(treeSelectChildren, checked, treeNodeIds) {
            for (const treeSelect of treeSelectChildren) {
                if (treeSelect.input.checked !== checked) {
                    treeSelect.input.checked = checked;
                    treeNodeIds.push(treeSelect.treeNode.id);
                }
                if (checked) {
                    this.tree.addSelectedNode(treeSelect.treeNode.index);
                }
                else {
                    this.tree.deleteSelectedNode(treeSelect.treeNode.index);
                }
                this.selectChildren(treeSelect.treeSelectChildren, checked, treeNodeIds);
            }
        }
        get tree() {
            return this.treeNode.tree;
        }
        get treeNode() {
            return this.closest("tobago-tree-node");
        }
        get treeSelectChildren() {
            let treeNode = this.closest("tobago-tree-node");
            return treeNode.parentElement
                .querySelectorAll("tobago-tree-node[parent='" + treeNode.id + "'] tobago-tree-select");
        }
        get input() {
            return this.querySelector("input");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-tree-select") == null) {
            window.customElements.define("tobago-tree-select", TreeSelect);
        }
    });

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
    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", (event) => {
            document.dispatchEvent(new CustomEvent("tobago.init"));
        });
    }
    else {
        document.dispatchEvent(new CustomEvent("tobago.init"));
    }

})));
//# sourceMappingURL=tobago.js.map
