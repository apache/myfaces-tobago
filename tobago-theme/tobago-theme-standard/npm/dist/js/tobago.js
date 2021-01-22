(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(require('vanillajs-datepicker/js/Datepicker.js')) :
    typeof define === 'function' && define.amd ? define(['vanillajs-datepicker/js/Datepicker.js'], factory) :
    (global = typeof globalThis !== 'undefined' ? globalThis : global || self, factory(global.Datepicker));
}(this, (function (Datepicker) { 'use strict';

    function _interopDefaultLegacy (e) { return e && typeof e === 'object' && 'default' in e ? e : { 'default': e }; }

    var Datepicker__default = /*#__PURE__*/_interopDefaultLegacy(Datepicker);

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
    // XXX remove me, for cleanup
    class DomUtils {
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
         * @param element with transition
         * @return transition time in milliseconds
         */
        static getTransitionTime(element) {
            const style = window.getComputedStyle(element);
            let delay = Number.parseFloat(style.transitionDelay);
            let duration = Number.parseFloat(style.transitionDuration);
            return (delay + duration) * 1000;
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

    var top = 'top';
    var bottom = 'bottom';
    var right = 'right';
    var left = 'left';
    var auto = 'auto';
    var basePlacements = [top, bottom, right, left];
    var start = 'start';
    var end = 'end';
    var clippingParents = 'clippingParents';
    var viewport = 'viewport';
    var popper = 'popper';
    var reference = 'reference';
    var variationPlacements = /*#__PURE__*/basePlacements.reduce(function (acc, placement) {
      return acc.concat([placement + "-" + start, placement + "-" + end]);
    }, []);
    var placements = /*#__PURE__*/[].concat(basePlacements, [auto]).reduce(function (acc, placement) {
      return acc.concat([placement, placement + "-" + start, placement + "-" + end]);
    }, []); // modifiers that need to read the DOM

    var beforeRead = 'beforeRead';
    var read = 'read';
    var afterRead = 'afterRead'; // pure-logic modifiers

    var beforeMain = 'beforeMain';
    var main = 'main';
    var afterMain = 'afterMain'; // modifier with the purpose to write to the DOM (or write into a framework state)

    var beforeWrite = 'beforeWrite';
    var write = 'write';
    var afterWrite = 'afterWrite';
    var modifierPhases = [beforeRead, read, afterRead, beforeMain, main, afterMain, beforeWrite, write, afterWrite];

    function getNodeName(element) {
      return element ? (element.nodeName || '').toLowerCase() : null;
    }

    /*:: import type { Window } from '../types'; */

    /*:: declare function getWindow(node: Node | Window): Window; */
    function getWindow(node) {
      if (node.toString() !== '[object Window]') {
        var ownerDocument = node.ownerDocument;
        return ownerDocument ? ownerDocument.defaultView || window : window;
      }

      return node;
    }

    /*:: declare function isElement(node: mixed): boolean %checks(node instanceof
      Element); */

    function isElement(node) {
      var OwnElement = getWindow(node).Element;
      return node instanceof OwnElement || node instanceof Element;
    }
    /*:: declare function isHTMLElement(node: mixed): boolean %checks(node instanceof
      HTMLElement); */


    function isHTMLElement(node) {
      var OwnElement = getWindow(node).HTMLElement;
      return node instanceof OwnElement || node instanceof HTMLElement;
    }
    /*:: declare function isShadowRoot(node: mixed): boolean %checks(node instanceof
      ShadowRoot); */


    function isShadowRoot(node) {
      var OwnElement = getWindow(node).ShadowRoot;
      return node instanceof OwnElement || node instanceof ShadowRoot;
    }

    // and applies them to the HTMLElements such as popper and arrow

    function applyStyles(_ref) {
      var state = _ref.state;
      Object.keys(state.elements).forEach(function (name) {
        var style = state.styles[name] || {};
        var attributes = state.attributes[name] || {};
        var element = state.elements[name]; // arrow is optional + virtual elements

        if (!isHTMLElement(element) || !getNodeName(element)) {
          return;
        } // Flow doesn't support to extend this property, but it's the most
        // effective way to apply styles to an HTMLElement
        // $FlowFixMe[cannot-write]


        Object.assign(element.style, style);
        Object.keys(attributes).forEach(function (name) {
          var value = attributes[name];

          if (value === false) {
            element.removeAttribute(name);
          } else {
            element.setAttribute(name, value === true ? '' : value);
          }
        });
      });
    }

    function effect(_ref2) {
      var state = _ref2.state;
      var initialStyles = {
        popper: {
          position: state.options.strategy,
          left: '0',
          top: '0',
          margin: '0'
        },
        arrow: {
          position: 'absolute'
        },
        reference: {}
      };
      Object.assign(state.elements.popper.style, initialStyles.popper);

      if (state.elements.arrow) {
        Object.assign(state.elements.arrow.style, initialStyles.arrow);
      }

      return function () {
        Object.keys(state.elements).forEach(function (name) {
          var element = state.elements[name];
          var attributes = state.attributes[name] || {};
          var styleProperties = Object.keys(state.styles.hasOwnProperty(name) ? state.styles[name] : initialStyles[name]); // Set all values to an empty string to unset them

          var style = styleProperties.reduce(function (style, property) {
            style[property] = '';
            return style;
          }, {}); // arrow is optional + virtual elements

          if (!isHTMLElement(element) || !getNodeName(element)) {
            return;
          }

          Object.assign(element.style, style);
          Object.keys(attributes).forEach(function (attribute) {
            element.removeAttribute(attribute);
          });
        });
      };
    } // eslint-disable-next-line import/no-unused-modules


    var applyStyles$1 = {
      name: 'applyStyles',
      enabled: true,
      phase: 'write',
      fn: applyStyles,
      effect: effect,
      requires: ['computeStyles']
    };

    function getBasePlacement(placement) {
      return placement.split('-')[0];
    }

    // Returns the layout rect of an element relative to its offsetParent. Layout
    // means it doesn't take into account transforms.
    function getLayoutRect(element) {
      return {
        x: element.offsetLeft,
        y: element.offsetTop,
        width: element.offsetWidth,
        height: element.offsetHeight
      };
    }

    function contains(parent, child) {
      var rootNode = child.getRootNode && child.getRootNode(); // First, attempt with faster native method

      if (parent.contains(child)) {
        return true;
      } // then fallback to custom implementation with Shadow DOM support
      else if (rootNode && isShadowRoot(rootNode)) {
          var next = child;

          do {
            if (next && parent.isSameNode(next)) {
              return true;
            } // $FlowFixMe[prop-missing]: need a better way to handle this...


            next = next.parentNode || next.host;
          } while (next);
        } // Give up, the result is false


      return false;
    }

    function getComputedStyle$1(element) {
      return getWindow(element).getComputedStyle(element);
    }

    function isTableElement(element) {
      return ['table', 'td', 'th'].indexOf(getNodeName(element)) >= 0;
    }

    function getDocumentElement(element) {
      // $FlowFixMe[incompatible-return]: assume body is always available
      return ((isElement(element) ? element.ownerDocument : // $FlowFixMe[prop-missing]
      element.document) || window.document).documentElement;
    }

    function getParentNode(element) {
      if (getNodeName(element) === 'html') {
        return element;
      }

      return (// this is a quicker (but less type safe) way to save quite some bytes from the bundle
        // $FlowFixMe[incompatible-return]
        // $FlowFixMe[prop-missing]
        element.assignedSlot || // step into the shadow DOM of the parent of a slotted node
        element.parentNode || // DOM Element detected
        // $FlowFixMe[incompatible-return]: need a better way to handle this...
        element.host || // ShadowRoot detected
        // $FlowFixMe[incompatible-call]: HTMLElement is a Node
        getDocumentElement(element) // fallback

      );
    }

    function getTrueOffsetParent(element) {
      if (!isHTMLElement(element) || // https://github.com/popperjs/popper-core/issues/837
      getComputedStyle$1(element).position === 'fixed') {
        return null;
      }

      var offsetParent = element.offsetParent;

      if (offsetParent) {
        var html = getDocumentElement(offsetParent);

        if (getNodeName(offsetParent) === 'body' && getComputedStyle$1(offsetParent).position === 'static' && getComputedStyle$1(html).position !== 'static') {
          return html;
        }
      }

      return offsetParent;
    } // `.offsetParent` reports `null` for fixed elements, while absolute elements
    // return the containing block


    function getContainingBlock(element) {
      var currentNode = getParentNode(element);

      while (isHTMLElement(currentNode) && ['html', 'body'].indexOf(getNodeName(currentNode)) < 0) {
        var css = getComputedStyle$1(currentNode); // This is non-exhaustive but covers the most common CSS properties that
        // create a containing block.

        if (css.transform !== 'none' || css.perspective !== 'none' || css.willChange && css.willChange !== 'auto') {
          return currentNode;
        } else {
          currentNode = currentNode.parentNode;
        }
      }

      return null;
    } // Gets the closest ancestor positioned element. Handles some edge cases,
    // such as table ancestors and cross browser bugs.


    function getOffsetParent(element) {
      var window = getWindow(element);
      var offsetParent = getTrueOffsetParent(element);

      while (offsetParent && isTableElement(offsetParent) && getComputedStyle$1(offsetParent).position === 'static') {
        offsetParent = getTrueOffsetParent(offsetParent);
      }

      if (offsetParent && getNodeName(offsetParent) === 'body' && getComputedStyle$1(offsetParent).position === 'static') {
        return window;
      }

      return offsetParent || getContainingBlock(element) || window;
    }

    function getMainAxisFromPlacement(placement) {
      return ['top', 'bottom'].indexOf(placement) >= 0 ? 'x' : 'y';
    }

    function within(min, value, max) {
      return Math.max(min, Math.min(value, max));
    }

    function getFreshSideObject() {
      return {
        top: 0,
        right: 0,
        bottom: 0,
        left: 0
      };
    }

    function mergePaddingObject(paddingObject) {
      return Object.assign(Object.assign({}, getFreshSideObject()), paddingObject);
    }

    function expandToHashMap(value, keys) {
      return keys.reduce(function (hashMap, key) {
        hashMap[key] = value;
        return hashMap;
      }, {});
    }

    function arrow(_ref) {
      var _state$modifiersData$;

      var state = _ref.state,
          name = _ref.name;
      var arrowElement = state.elements.arrow;
      var popperOffsets = state.modifiersData.popperOffsets;
      var basePlacement = getBasePlacement(state.placement);
      var axis = getMainAxisFromPlacement(basePlacement);
      var isVertical = [left, right].indexOf(basePlacement) >= 0;
      var len = isVertical ? 'height' : 'width';

      if (!arrowElement || !popperOffsets) {
        return;
      }

      var paddingObject = state.modifiersData[name + "#persistent"].padding;
      var arrowRect = getLayoutRect(arrowElement);
      var minProp = axis === 'y' ? top : left;
      var maxProp = axis === 'y' ? bottom : right;
      var endDiff = state.rects.reference[len] + state.rects.reference[axis] - popperOffsets[axis] - state.rects.popper[len];
      var startDiff = popperOffsets[axis] - state.rects.reference[axis];
      var arrowOffsetParent = getOffsetParent(arrowElement);
      var clientSize = arrowOffsetParent ? axis === 'y' ? arrowOffsetParent.clientHeight || 0 : arrowOffsetParent.clientWidth || 0 : 0;
      var centerToReference = endDiff / 2 - startDiff / 2; // Make sure the arrow doesn't overflow the popper if the center point is
      // outside of the popper bounds

      var min = paddingObject[minProp];
      var max = clientSize - arrowRect[len] - paddingObject[maxProp];
      var center = clientSize / 2 - arrowRect[len] / 2 + centerToReference;
      var offset = within(min, center, max); // Prevents breaking syntax highlighting...

      var axisProp = axis;
      state.modifiersData[name] = (_state$modifiersData$ = {}, _state$modifiersData$[axisProp] = offset, _state$modifiersData$.centerOffset = offset - center, _state$modifiersData$);
    }

    function effect$1(_ref2) {
      var state = _ref2.state,
          options = _ref2.options,
          name = _ref2.name;
      var _options$element = options.element,
          arrowElement = _options$element === void 0 ? '[data-popper-arrow]' : _options$element,
          _options$padding = options.padding,
          padding = _options$padding === void 0 ? 0 : _options$padding;

      if (arrowElement == null) {
        return;
      } // CSS selector


      if (typeof arrowElement === 'string') {
        arrowElement = state.elements.popper.querySelector(arrowElement);

        if (!arrowElement) {
          return;
        }
      }

      if (!contains(state.elements.popper, arrowElement)) {

        return;
      }

      state.elements.arrow = arrowElement;
      state.modifiersData[name + "#persistent"] = {
        padding: mergePaddingObject(typeof padding !== 'number' ? padding : expandToHashMap(padding, basePlacements))
      };
    } // eslint-disable-next-line import/no-unused-modules


    var arrow$1 = {
      name: 'arrow',
      enabled: true,
      phase: 'main',
      fn: arrow,
      effect: effect$1,
      requires: ['popperOffsets'],
      requiresIfExists: ['preventOverflow']
    };

    var unsetSides = {
      top: 'auto',
      right: 'auto',
      bottom: 'auto',
      left: 'auto'
    }; // Round the offsets to the nearest suitable subpixel based on the DPR.
    // Zooming can change the DPR, but it seems to report a value that will
    // cleanly divide the values into the appropriate subpixels.

    function roundOffsetsByDPR(_ref) {
      var x = _ref.x,
          y = _ref.y;
      var win = window;
      var dpr = win.devicePixelRatio || 1;
      return {
        x: Math.round(x * dpr) / dpr || 0,
        y: Math.round(y * dpr) / dpr || 0
      };
    }

    function mapToStyles(_ref2) {
      var _Object$assign2;

      var popper = _ref2.popper,
          popperRect = _ref2.popperRect,
          placement = _ref2.placement,
          offsets = _ref2.offsets,
          position = _ref2.position,
          gpuAcceleration = _ref2.gpuAcceleration,
          adaptive = _ref2.adaptive,
          roundOffsets = _ref2.roundOffsets;

      var _ref3 = roundOffsets ? roundOffsetsByDPR(offsets) : offsets,
          _ref3$x = _ref3.x,
          x = _ref3$x === void 0 ? 0 : _ref3$x,
          _ref3$y = _ref3.y,
          y = _ref3$y === void 0 ? 0 : _ref3$y;

      var hasX = offsets.hasOwnProperty('x');
      var hasY = offsets.hasOwnProperty('y');
      var sideX = left;
      var sideY = top;
      var win = window;

      if (adaptive) {
        var offsetParent = getOffsetParent(popper);

        if (offsetParent === getWindow(popper)) {
          offsetParent = getDocumentElement(popper);
        } // $FlowFixMe[incompatible-cast]: force type refinement, we compare offsetParent with window above, but Flow doesn't detect it

        /*:: offsetParent = (offsetParent: Element); */


        if (placement === top) {
          sideY = bottom;
          y -= offsetParent.clientHeight - popperRect.height;
          y *= gpuAcceleration ? 1 : -1;
        }

        if (placement === left) {
          sideX = right;
          x -= offsetParent.clientWidth - popperRect.width;
          x *= gpuAcceleration ? 1 : -1;
        }
      }

      var commonStyles = Object.assign({
        position: position
      }, adaptive && unsetSides);

      if (gpuAcceleration) {
        var _Object$assign;

        return Object.assign(Object.assign({}, commonStyles), {}, (_Object$assign = {}, _Object$assign[sideY] = hasY ? '0' : '', _Object$assign[sideX] = hasX ? '0' : '', _Object$assign.transform = (win.devicePixelRatio || 1) < 2 ? "translate(" + x + "px, " + y + "px)" : "translate3d(" + x + "px, " + y + "px, 0)", _Object$assign));
      }

      return Object.assign(Object.assign({}, commonStyles), {}, (_Object$assign2 = {}, _Object$assign2[sideY] = hasY ? y + "px" : '', _Object$assign2[sideX] = hasX ? x + "px" : '', _Object$assign2.transform = '', _Object$assign2));
    }

    function computeStyles(_ref4) {
      var state = _ref4.state,
          options = _ref4.options;
      var _options$gpuAccelerat = options.gpuAcceleration,
          gpuAcceleration = _options$gpuAccelerat === void 0 ? true : _options$gpuAccelerat,
          _options$adaptive = options.adaptive,
          adaptive = _options$adaptive === void 0 ? true : _options$adaptive,
          _options$roundOffsets = options.roundOffsets,
          roundOffsets = _options$roundOffsets === void 0 ? true : _options$roundOffsets;

      var commonStyles = {
        placement: getBasePlacement(state.placement),
        popper: state.elements.popper,
        popperRect: state.rects.popper,
        gpuAcceleration: gpuAcceleration
      };

      if (state.modifiersData.popperOffsets != null) {
        state.styles.popper = Object.assign(Object.assign({}, state.styles.popper), mapToStyles(Object.assign(Object.assign({}, commonStyles), {}, {
          offsets: state.modifiersData.popperOffsets,
          position: state.options.strategy,
          adaptive: adaptive,
          roundOffsets: roundOffsets
        })));
      }

      if (state.modifiersData.arrow != null) {
        state.styles.arrow = Object.assign(Object.assign({}, state.styles.arrow), mapToStyles(Object.assign(Object.assign({}, commonStyles), {}, {
          offsets: state.modifiersData.arrow,
          position: 'absolute',
          adaptive: false,
          roundOffsets: roundOffsets
        })));
      }

      state.attributes.popper = Object.assign(Object.assign({}, state.attributes.popper), {}, {
        'data-popper-placement': state.placement
      });
    } // eslint-disable-next-line import/no-unused-modules


    var computeStyles$1 = {
      name: 'computeStyles',
      enabled: true,
      phase: 'beforeWrite',
      fn: computeStyles,
      data: {}
    };

    var passive = {
      passive: true
    };

    function effect$2(_ref) {
      var state = _ref.state,
          instance = _ref.instance,
          options = _ref.options;
      var _options$scroll = options.scroll,
          scroll = _options$scroll === void 0 ? true : _options$scroll,
          _options$resize = options.resize,
          resize = _options$resize === void 0 ? true : _options$resize;
      var window = getWindow(state.elements.popper);
      var scrollParents = [].concat(state.scrollParents.reference, state.scrollParents.popper);

      if (scroll) {
        scrollParents.forEach(function (scrollParent) {
          scrollParent.addEventListener('scroll', instance.update, passive);
        });
      }

      if (resize) {
        window.addEventListener('resize', instance.update, passive);
      }

      return function () {
        if (scroll) {
          scrollParents.forEach(function (scrollParent) {
            scrollParent.removeEventListener('scroll', instance.update, passive);
          });
        }

        if (resize) {
          window.removeEventListener('resize', instance.update, passive);
        }
      };
    } // eslint-disable-next-line import/no-unused-modules


    var eventListeners = {
      name: 'eventListeners',
      enabled: true,
      phase: 'write',
      fn: function fn() {},
      effect: effect$2,
      data: {}
    };

    var hash = {
      left: 'right',
      right: 'left',
      bottom: 'top',
      top: 'bottom'
    };
    function getOppositePlacement(placement) {
      return placement.replace(/left|right|bottom|top/g, function (matched) {
        return hash[matched];
      });
    }

    var hash$1 = {
      start: 'end',
      end: 'start'
    };
    function getOppositeVariationPlacement(placement) {
      return placement.replace(/start|end/g, function (matched) {
        return hash$1[matched];
      });
    }

    function getBoundingClientRect(element) {
      var rect = element.getBoundingClientRect();
      return {
        width: rect.width,
        height: rect.height,
        top: rect.top,
        right: rect.right,
        bottom: rect.bottom,
        left: rect.left,
        x: rect.left,
        y: rect.top
      };
    }

    function getWindowScroll(node) {
      var win = getWindow(node);
      var scrollLeft = win.pageXOffset;
      var scrollTop = win.pageYOffset;
      return {
        scrollLeft: scrollLeft,
        scrollTop: scrollTop
      };
    }

    function getWindowScrollBarX(element) {
      // If <html> has a CSS width greater than the viewport, then this will be
      // incorrect for RTL.
      // Popper 1 is broken in this case and never had a bug report so let's assume
      // it's not an issue. I don't think anyone ever specifies width on <html>
      // anyway.
      // Browsers where the left scrollbar doesn't cause an issue report `0` for
      // this (e.g. Edge 2019, IE11, Safari)
      return getBoundingClientRect(getDocumentElement(element)).left + getWindowScroll(element).scrollLeft;
    }

    function getViewportRect(element) {
      var win = getWindow(element);
      var html = getDocumentElement(element);
      var visualViewport = win.visualViewport;
      var width = html.clientWidth;
      var height = html.clientHeight;
      var x = 0;
      var y = 0; // NB: This isn't supported on iOS <= 12. If the keyboard is open, the popper
      // can be obscured underneath it.
      // Also, `html.clientHeight` adds the bottom bar height in Safari iOS, even
      // if it isn't open, so if this isn't available, the popper will be detected
      // to overflow the bottom of the screen too early.

      if (visualViewport) {
        width = visualViewport.width;
        height = visualViewport.height; // Uses Layout Viewport (like Chrome; Safari does not currently)
        // In Chrome, it returns a value very close to 0 (+/-) but contains rounding
        // errors due to floating point numbers, so we need to check precision.
        // Safari returns a number <= 0, usually < -1 when pinch-zoomed
        // Feature detection fails in mobile emulation mode in Chrome.
        // Math.abs(win.innerWidth / visualViewport.scale - visualViewport.width) <
        // 0.001
        // Fallback here: "Not Safari" userAgent

        if (!/^((?!chrome|android).)*safari/i.test(navigator.userAgent)) {
          x = visualViewport.offsetLeft;
          y = visualViewport.offsetTop;
        }
      }

      return {
        width: width,
        height: height,
        x: x + getWindowScrollBarX(element),
        y: y
      };
    }

    // of the `<html>` and `<body>` rect bounds if horizontally scrollable

    function getDocumentRect(element) {
      var html = getDocumentElement(element);
      var winScroll = getWindowScroll(element);
      var body = element.ownerDocument.body;
      var width = Math.max(html.scrollWidth, html.clientWidth, body ? body.scrollWidth : 0, body ? body.clientWidth : 0);
      var height = Math.max(html.scrollHeight, html.clientHeight, body ? body.scrollHeight : 0, body ? body.clientHeight : 0);
      var x = -winScroll.scrollLeft + getWindowScrollBarX(element);
      var y = -winScroll.scrollTop;

      if (getComputedStyle$1(body || html).direction === 'rtl') {
        x += Math.max(html.clientWidth, body ? body.clientWidth : 0) - width;
      }

      return {
        width: width,
        height: height,
        x: x,
        y: y
      };
    }

    function isScrollParent(element) {
      // Firefox wants us to check `-x` and `-y` variations as well
      var _getComputedStyle = getComputedStyle$1(element),
          overflow = _getComputedStyle.overflow,
          overflowX = _getComputedStyle.overflowX,
          overflowY = _getComputedStyle.overflowY;

      return /auto|scroll|overlay|hidden/.test(overflow + overflowY + overflowX);
    }

    function getScrollParent(node) {
      if (['html', 'body', '#document'].indexOf(getNodeName(node)) >= 0) {
        // $FlowFixMe[incompatible-return]: assume body is always available
        return node.ownerDocument.body;
      }

      if (isHTMLElement(node) && isScrollParent(node)) {
        return node;
      }

      return getScrollParent(getParentNode(node));
    }

    /*
    given a DOM element, return the list of all scroll parents, up the list of ancesors
    until we get to the top window object. This list is what we attach scroll listeners
    to, because if any of these parent elements scroll, we'll need to re-calculate the
    reference element's position.
    */

    function listScrollParents(element, list) {
      if (list === void 0) {
        list = [];
      }

      var scrollParent = getScrollParent(element);
      var isBody = getNodeName(scrollParent) === 'body';
      var win = getWindow(scrollParent);
      var target = isBody ? [win].concat(win.visualViewport || [], isScrollParent(scrollParent) ? scrollParent : []) : scrollParent;
      var updatedList = list.concat(target);
      return isBody ? updatedList : // $FlowFixMe[incompatible-call]: isBody tells us target will be an HTMLElement here
      updatedList.concat(listScrollParents(getParentNode(target)));
    }

    function rectToClientRect(rect) {
      return Object.assign(Object.assign({}, rect), {}, {
        left: rect.x,
        top: rect.y,
        right: rect.x + rect.width,
        bottom: rect.y + rect.height
      });
    }

    function getInnerBoundingClientRect(element) {
      var rect = getBoundingClientRect(element);
      rect.top = rect.top + element.clientTop;
      rect.left = rect.left + element.clientLeft;
      rect.bottom = rect.top + element.clientHeight;
      rect.right = rect.left + element.clientWidth;
      rect.width = element.clientWidth;
      rect.height = element.clientHeight;
      rect.x = rect.left;
      rect.y = rect.top;
      return rect;
    }

    function getClientRectFromMixedType(element, clippingParent) {
      return clippingParent === viewport ? rectToClientRect(getViewportRect(element)) : isHTMLElement(clippingParent) ? getInnerBoundingClientRect(clippingParent) : rectToClientRect(getDocumentRect(getDocumentElement(element)));
    } // A "clipping parent" is an overflowable container with the characteristic of
    // clipping (or hiding) overflowing elements with a position different from
    // `initial`


    function getClippingParents(element) {
      var clippingParents = listScrollParents(getParentNode(element));
      var canEscapeClipping = ['absolute', 'fixed'].indexOf(getComputedStyle$1(element).position) >= 0;
      var clipperElement = canEscapeClipping && isHTMLElement(element) ? getOffsetParent(element) : element;

      if (!isElement(clipperElement)) {
        return [];
      } // $FlowFixMe[incompatible-return]: https://github.com/facebook/flow/issues/1414


      return clippingParents.filter(function (clippingParent) {
        return isElement(clippingParent) && contains(clippingParent, clipperElement) && getNodeName(clippingParent) !== 'body';
      });
    } // Gets the maximum area that the element is visible in due to any number of
    // clipping parents


    function getClippingRect(element, boundary, rootBoundary) {
      var mainClippingParents = boundary === 'clippingParents' ? getClippingParents(element) : [].concat(boundary);
      var clippingParents = [].concat(mainClippingParents, [rootBoundary]);
      var firstClippingParent = clippingParents[0];
      var clippingRect = clippingParents.reduce(function (accRect, clippingParent) {
        var rect = getClientRectFromMixedType(element, clippingParent);
        accRect.top = Math.max(rect.top, accRect.top);
        accRect.right = Math.min(rect.right, accRect.right);
        accRect.bottom = Math.min(rect.bottom, accRect.bottom);
        accRect.left = Math.max(rect.left, accRect.left);
        return accRect;
      }, getClientRectFromMixedType(element, firstClippingParent));
      clippingRect.width = clippingRect.right - clippingRect.left;
      clippingRect.height = clippingRect.bottom - clippingRect.top;
      clippingRect.x = clippingRect.left;
      clippingRect.y = clippingRect.top;
      return clippingRect;
    }

    function getVariation(placement) {
      return placement.split('-')[1];
    }

    function computeOffsets(_ref) {
      var reference = _ref.reference,
          element = _ref.element,
          placement = _ref.placement;
      var basePlacement = placement ? getBasePlacement(placement) : null;
      var variation = placement ? getVariation(placement) : null;
      var commonX = reference.x + reference.width / 2 - element.width / 2;
      var commonY = reference.y + reference.height / 2 - element.height / 2;
      var offsets;

      switch (basePlacement) {
        case top:
          offsets = {
            x: commonX,
            y: reference.y - element.height
          };
          break;

        case bottom:
          offsets = {
            x: commonX,
            y: reference.y + reference.height
          };
          break;

        case right:
          offsets = {
            x: reference.x + reference.width,
            y: commonY
          };
          break;

        case left:
          offsets = {
            x: reference.x - element.width,
            y: commonY
          };
          break;

        default:
          offsets = {
            x: reference.x,
            y: reference.y
          };
      }

      var mainAxis = basePlacement ? getMainAxisFromPlacement(basePlacement) : null;

      if (mainAxis != null) {
        var len = mainAxis === 'y' ? 'height' : 'width';

        switch (variation) {
          case start:
            offsets[mainAxis] = offsets[mainAxis] - (reference[len] / 2 - element[len] / 2);
            break;

          case end:
            offsets[mainAxis] = offsets[mainAxis] + (reference[len] / 2 - element[len] / 2);
            break;
        }
      }

      return offsets;
    }

    function detectOverflow(state, options) {
      if (options === void 0) {
        options = {};
      }

      var _options = options,
          _options$placement = _options.placement,
          placement = _options$placement === void 0 ? state.placement : _options$placement,
          _options$boundary = _options.boundary,
          boundary = _options$boundary === void 0 ? clippingParents : _options$boundary,
          _options$rootBoundary = _options.rootBoundary,
          rootBoundary = _options$rootBoundary === void 0 ? viewport : _options$rootBoundary,
          _options$elementConte = _options.elementContext,
          elementContext = _options$elementConte === void 0 ? popper : _options$elementConte,
          _options$altBoundary = _options.altBoundary,
          altBoundary = _options$altBoundary === void 0 ? false : _options$altBoundary,
          _options$padding = _options.padding,
          padding = _options$padding === void 0 ? 0 : _options$padding;
      var paddingObject = mergePaddingObject(typeof padding !== 'number' ? padding : expandToHashMap(padding, basePlacements));
      var altContext = elementContext === popper ? reference : popper;
      var referenceElement = state.elements.reference;
      var popperRect = state.rects.popper;
      var element = state.elements[altBoundary ? altContext : elementContext];
      var clippingClientRect = getClippingRect(isElement(element) ? element : element.contextElement || getDocumentElement(state.elements.popper), boundary, rootBoundary);
      var referenceClientRect = getBoundingClientRect(referenceElement);
      var popperOffsets = computeOffsets({
        reference: referenceClientRect,
        element: popperRect,
        strategy: 'absolute',
        placement: placement
      });
      var popperClientRect = rectToClientRect(Object.assign(Object.assign({}, popperRect), popperOffsets));
      var elementClientRect = elementContext === popper ? popperClientRect : referenceClientRect; // positive = overflowing the clipping rect
      // 0 or negative = within the clipping rect

      var overflowOffsets = {
        top: clippingClientRect.top - elementClientRect.top + paddingObject.top,
        bottom: elementClientRect.bottom - clippingClientRect.bottom + paddingObject.bottom,
        left: clippingClientRect.left - elementClientRect.left + paddingObject.left,
        right: elementClientRect.right - clippingClientRect.right + paddingObject.right
      };
      var offsetData = state.modifiersData.offset; // Offsets can be applied only to the popper element

      if (elementContext === popper && offsetData) {
        var offset = offsetData[placement];
        Object.keys(overflowOffsets).forEach(function (key) {
          var multiply = [right, bottom].indexOf(key) >= 0 ? 1 : -1;
          var axis = [top, bottom].indexOf(key) >= 0 ? 'y' : 'x';
          overflowOffsets[key] += offset[axis] * multiply;
        });
      }

      return overflowOffsets;
    }

    /*:: type OverflowsMap = { [ComputedPlacement]: number }; */

    /*;; type OverflowsMap = { [key in ComputedPlacement]: number }; */
    function computeAutoPlacement(state, options) {
      if (options === void 0) {
        options = {};
      }

      var _options = options,
          placement = _options.placement,
          boundary = _options.boundary,
          rootBoundary = _options.rootBoundary,
          padding = _options.padding,
          flipVariations = _options.flipVariations,
          _options$allowedAutoP = _options.allowedAutoPlacements,
          allowedAutoPlacements = _options$allowedAutoP === void 0 ? placements : _options$allowedAutoP;
      var variation = getVariation(placement);
      var placements$1 = variation ? flipVariations ? variationPlacements : variationPlacements.filter(function (placement) {
        return getVariation(placement) === variation;
      }) : basePlacements;
      var allowedPlacements = placements$1.filter(function (placement) {
        return allowedAutoPlacements.indexOf(placement) >= 0;
      });

      if (allowedPlacements.length === 0) {
        allowedPlacements = placements$1;
      } // $FlowFixMe[incompatible-type]: Flow seems to have problems with two array unions...


      var overflows = allowedPlacements.reduce(function (acc, placement) {
        acc[placement] = detectOverflow(state, {
          placement: placement,
          boundary: boundary,
          rootBoundary: rootBoundary,
          padding: padding
        })[getBasePlacement(placement)];
        return acc;
      }, {});
      return Object.keys(overflows).sort(function (a, b) {
        return overflows[a] - overflows[b];
      });
    }

    function getExpandedFallbackPlacements(placement) {
      if (getBasePlacement(placement) === auto) {
        return [];
      }

      var oppositePlacement = getOppositePlacement(placement);
      return [getOppositeVariationPlacement(placement), oppositePlacement, getOppositeVariationPlacement(oppositePlacement)];
    }

    function flip(_ref) {
      var state = _ref.state,
          options = _ref.options,
          name = _ref.name;

      if (state.modifiersData[name]._skip) {
        return;
      }

      var _options$mainAxis = options.mainAxis,
          checkMainAxis = _options$mainAxis === void 0 ? true : _options$mainAxis,
          _options$altAxis = options.altAxis,
          checkAltAxis = _options$altAxis === void 0 ? true : _options$altAxis,
          specifiedFallbackPlacements = options.fallbackPlacements,
          padding = options.padding,
          boundary = options.boundary,
          rootBoundary = options.rootBoundary,
          altBoundary = options.altBoundary,
          _options$flipVariatio = options.flipVariations,
          flipVariations = _options$flipVariatio === void 0 ? true : _options$flipVariatio,
          allowedAutoPlacements = options.allowedAutoPlacements;
      var preferredPlacement = state.options.placement;
      var basePlacement = getBasePlacement(preferredPlacement);
      var isBasePlacement = basePlacement === preferredPlacement;
      var fallbackPlacements = specifiedFallbackPlacements || (isBasePlacement || !flipVariations ? [getOppositePlacement(preferredPlacement)] : getExpandedFallbackPlacements(preferredPlacement));
      var placements = [preferredPlacement].concat(fallbackPlacements).reduce(function (acc, placement) {
        return acc.concat(getBasePlacement(placement) === auto ? computeAutoPlacement(state, {
          placement: placement,
          boundary: boundary,
          rootBoundary: rootBoundary,
          padding: padding,
          flipVariations: flipVariations,
          allowedAutoPlacements: allowedAutoPlacements
        }) : placement);
      }, []);
      var referenceRect = state.rects.reference;
      var popperRect = state.rects.popper;
      var checksMap = new Map();
      var makeFallbackChecks = true;
      var firstFittingPlacement = placements[0];

      for (var i = 0; i < placements.length; i++) {
        var placement = placements[i];

        var _basePlacement = getBasePlacement(placement);

        var isStartVariation = getVariation(placement) === start;
        var isVertical = [top, bottom].indexOf(_basePlacement) >= 0;
        var len = isVertical ? 'width' : 'height';
        var overflow = detectOverflow(state, {
          placement: placement,
          boundary: boundary,
          rootBoundary: rootBoundary,
          altBoundary: altBoundary,
          padding: padding
        });
        var mainVariationSide = isVertical ? isStartVariation ? right : left : isStartVariation ? bottom : top;

        if (referenceRect[len] > popperRect[len]) {
          mainVariationSide = getOppositePlacement(mainVariationSide);
        }

        var altVariationSide = getOppositePlacement(mainVariationSide);
        var checks = [];

        if (checkMainAxis) {
          checks.push(overflow[_basePlacement] <= 0);
        }

        if (checkAltAxis) {
          checks.push(overflow[mainVariationSide] <= 0, overflow[altVariationSide] <= 0);
        }

        if (checks.every(function (check) {
          return check;
        })) {
          firstFittingPlacement = placement;
          makeFallbackChecks = false;
          break;
        }

        checksMap.set(placement, checks);
      }

      if (makeFallbackChecks) {
        // `2` may be desired in some cases – research later
        var numberOfChecks = flipVariations ? 3 : 1;

        var _loop = function _loop(_i) {
          var fittingPlacement = placements.find(function (placement) {
            var checks = checksMap.get(placement);

            if (checks) {
              return checks.slice(0, _i).every(function (check) {
                return check;
              });
            }
          });

          if (fittingPlacement) {
            firstFittingPlacement = fittingPlacement;
            return "break";
          }
        };

        for (var _i = numberOfChecks; _i > 0; _i--) {
          var _ret = _loop(_i);

          if (_ret === "break") break;
        }
      }

      if (state.placement !== firstFittingPlacement) {
        state.modifiersData[name]._skip = true;
        state.placement = firstFittingPlacement;
        state.reset = true;
      }
    } // eslint-disable-next-line import/no-unused-modules


    var flip$1 = {
      name: 'flip',
      enabled: true,
      phase: 'main',
      fn: flip,
      requiresIfExists: ['offset'],
      data: {
        _skip: false
      }
    };

    function getSideOffsets(overflow, rect, preventedOffsets) {
      if (preventedOffsets === void 0) {
        preventedOffsets = {
          x: 0,
          y: 0
        };
      }

      return {
        top: overflow.top - rect.height - preventedOffsets.y,
        right: overflow.right - rect.width + preventedOffsets.x,
        bottom: overflow.bottom - rect.height + preventedOffsets.y,
        left: overflow.left - rect.width - preventedOffsets.x
      };
    }

    function isAnySideFullyClipped(overflow) {
      return [top, right, bottom, left].some(function (side) {
        return overflow[side] >= 0;
      });
    }

    function hide(_ref) {
      var state = _ref.state,
          name = _ref.name;
      var referenceRect = state.rects.reference;
      var popperRect = state.rects.popper;
      var preventedOffsets = state.modifiersData.preventOverflow;
      var referenceOverflow = detectOverflow(state, {
        elementContext: 'reference'
      });
      var popperAltOverflow = detectOverflow(state, {
        altBoundary: true
      });
      var referenceClippingOffsets = getSideOffsets(referenceOverflow, referenceRect);
      var popperEscapeOffsets = getSideOffsets(popperAltOverflow, popperRect, preventedOffsets);
      var isReferenceHidden = isAnySideFullyClipped(referenceClippingOffsets);
      var hasPopperEscaped = isAnySideFullyClipped(popperEscapeOffsets);
      state.modifiersData[name] = {
        referenceClippingOffsets: referenceClippingOffsets,
        popperEscapeOffsets: popperEscapeOffsets,
        isReferenceHidden: isReferenceHidden,
        hasPopperEscaped: hasPopperEscaped
      };
      state.attributes.popper = Object.assign(Object.assign({}, state.attributes.popper), {}, {
        'data-popper-reference-hidden': isReferenceHidden,
        'data-popper-escaped': hasPopperEscaped
      });
    } // eslint-disable-next-line import/no-unused-modules


    var hide$1 = {
      name: 'hide',
      enabled: true,
      phase: 'main',
      requiresIfExists: ['preventOverflow'],
      fn: hide
    };

    function distanceAndSkiddingToXY(placement, rects, offset) {
      var basePlacement = getBasePlacement(placement);
      var invertDistance = [left, top].indexOf(basePlacement) >= 0 ? -1 : 1;

      var _ref = typeof offset === 'function' ? offset(Object.assign(Object.assign({}, rects), {}, {
        placement: placement
      })) : offset,
          skidding = _ref[0],
          distance = _ref[1];

      skidding = skidding || 0;
      distance = (distance || 0) * invertDistance;
      return [left, right].indexOf(basePlacement) >= 0 ? {
        x: distance,
        y: skidding
      } : {
        x: skidding,
        y: distance
      };
    }

    function offset(_ref2) {
      var state = _ref2.state,
          options = _ref2.options,
          name = _ref2.name;
      var _options$offset = options.offset,
          offset = _options$offset === void 0 ? [0, 0] : _options$offset;
      var data = placements.reduce(function (acc, placement) {
        acc[placement] = distanceAndSkiddingToXY(placement, state.rects, offset);
        return acc;
      }, {});
      var _data$state$placement = data[state.placement],
          x = _data$state$placement.x,
          y = _data$state$placement.y;

      if (state.modifiersData.popperOffsets != null) {
        state.modifiersData.popperOffsets.x += x;
        state.modifiersData.popperOffsets.y += y;
      }

      state.modifiersData[name] = data;
    } // eslint-disable-next-line import/no-unused-modules


    var offset$1 = {
      name: 'offset',
      enabled: true,
      phase: 'main',
      requires: ['popperOffsets'],
      fn: offset
    };

    function popperOffsets(_ref) {
      var state = _ref.state,
          name = _ref.name;
      // Offsets are the actual position the popper needs to have to be
      // properly positioned near its reference element
      // This is the most basic placement, and will be adjusted by
      // the modifiers in the next step
      state.modifiersData[name] = computeOffsets({
        reference: state.rects.reference,
        element: state.rects.popper,
        strategy: 'absolute',
        placement: state.placement
      });
    } // eslint-disable-next-line import/no-unused-modules


    var popperOffsets$1 = {
      name: 'popperOffsets',
      enabled: true,
      phase: 'read',
      fn: popperOffsets,
      data: {}
    };

    function getAltAxis(axis) {
      return axis === 'x' ? 'y' : 'x';
    }

    function preventOverflow(_ref) {
      var state = _ref.state,
          options = _ref.options,
          name = _ref.name;
      var _options$mainAxis = options.mainAxis,
          checkMainAxis = _options$mainAxis === void 0 ? true : _options$mainAxis,
          _options$altAxis = options.altAxis,
          checkAltAxis = _options$altAxis === void 0 ? false : _options$altAxis,
          boundary = options.boundary,
          rootBoundary = options.rootBoundary,
          altBoundary = options.altBoundary,
          padding = options.padding,
          _options$tether = options.tether,
          tether = _options$tether === void 0 ? true : _options$tether,
          _options$tetherOffset = options.tetherOffset,
          tetherOffset = _options$tetherOffset === void 0 ? 0 : _options$tetherOffset;
      var overflow = detectOverflow(state, {
        boundary: boundary,
        rootBoundary: rootBoundary,
        padding: padding,
        altBoundary: altBoundary
      });
      var basePlacement = getBasePlacement(state.placement);
      var variation = getVariation(state.placement);
      var isBasePlacement = !variation;
      var mainAxis = getMainAxisFromPlacement(basePlacement);
      var altAxis = getAltAxis(mainAxis);
      var popperOffsets = state.modifiersData.popperOffsets;
      var referenceRect = state.rects.reference;
      var popperRect = state.rects.popper;
      var tetherOffsetValue = typeof tetherOffset === 'function' ? tetherOffset(Object.assign(Object.assign({}, state.rects), {}, {
        placement: state.placement
      })) : tetherOffset;
      var data = {
        x: 0,
        y: 0
      };

      if (!popperOffsets) {
        return;
      }

      if (checkMainAxis) {
        var mainSide = mainAxis === 'y' ? top : left;
        var altSide = mainAxis === 'y' ? bottom : right;
        var len = mainAxis === 'y' ? 'height' : 'width';
        var offset = popperOffsets[mainAxis];
        var min = popperOffsets[mainAxis] + overflow[mainSide];
        var max = popperOffsets[mainAxis] - overflow[altSide];
        var additive = tether ? -popperRect[len] / 2 : 0;
        var minLen = variation === start ? referenceRect[len] : popperRect[len];
        var maxLen = variation === start ? -popperRect[len] : -referenceRect[len]; // We need to include the arrow in the calculation so the arrow doesn't go
        // outside the reference bounds

        var arrowElement = state.elements.arrow;
        var arrowRect = tether && arrowElement ? getLayoutRect(arrowElement) : {
          width: 0,
          height: 0
        };
        var arrowPaddingObject = state.modifiersData['arrow#persistent'] ? state.modifiersData['arrow#persistent'].padding : getFreshSideObject();
        var arrowPaddingMin = arrowPaddingObject[mainSide];
        var arrowPaddingMax = arrowPaddingObject[altSide]; // If the reference length is smaller than the arrow length, we don't want
        // to include its full size in the calculation. If the reference is small
        // and near the edge of a boundary, the popper can overflow even if the
        // reference is not overflowing as well (e.g. virtual elements with no
        // width or height)

        var arrowLen = within(0, referenceRect[len], arrowRect[len]);
        var minOffset = isBasePlacement ? referenceRect[len] / 2 - additive - arrowLen - arrowPaddingMin - tetherOffsetValue : minLen - arrowLen - arrowPaddingMin - tetherOffsetValue;
        var maxOffset = isBasePlacement ? -referenceRect[len] / 2 + additive + arrowLen + arrowPaddingMax + tetherOffsetValue : maxLen + arrowLen + arrowPaddingMax + tetherOffsetValue;
        var arrowOffsetParent = state.elements.arrow && getOffsetParent(state.elements.arrow);
        var clientOffset = arrowOffsetParent ? mainAxis === 'y' ? arrowOffsetParent.clientTop || 0 : arrowOffsetParent.clientLeft || 0 : 0;
        var offsetModifierValue = state.modifiersData.offset ? state.modifiersData.offset[state.placement][mainAxis] : 0;
        var tetherMin = popperOffsets[mainAxis] + minOffset - offsetModifierValue - clientOffset;
        var tetherMax = popperOffsets[mainAxis] + maxOffset - offsetModifierValue;
        var preventedOffset = within(tether ? Math.min(min, tetherMin) : min, offset, tether ? Math.max(max, tetherMax) : max);
        popperOffsets[mainAxis] = preventedOffset;
        data[mainAxis] = preventedOffset - offset;
      }

      if (checkAltAxis) {
        var _mainSide = mainAxis === 'x' ? top : left;

        var _altSide = mainAxis === 'x' ? bottom : right;

        var _offset = popperOffsets[altAxis];

        var _min = _offset + overflow[_mainSide];

        var _max = _offset - overflow[_altSide];

        var _preventedOffset = within(_min, _offset, _max);

        popperOffsets[altAxis] = _preventedOffset;
        data[altAxis] = _preventedOffset - _offset;
      }

      state.modifiersData[name] = data;
    } // eslint-disable-next-line import/no-unused-modules


    var preventOverflow$1 = {
      name: 'preventOverflow',
      enabled: true,
      phase: 'main',
      fn: preventOverflow,
      requiresIfExists: ['offset']
    };

    function getHTMLElementScroll(element) {
      return {
        scrollLeft: element.scrollLeft,
        scrollTop: element.scrollTop
      };
    }

    function getNodeScroll(node) {
      if (node === getWindow(node) || !isHTMLElement(node)) {
        return getWindowScroll(node);
      } else {
        return getHTMLElementScroll(node);
      }
    }

    // Composite means it takes into account transforms as well as layout.

    function getCompositeRect(elementOrVirtualElement, offsetParent, isFixed) {
      if (isFixed === void 0) {
        isFixed = false;
      }

      var documentElement = getDocumentElement(offsetParent);
      var rect = getBoundingClientRect(elementOrVirtualElement);
      var isOffsetParentAnElement = isHTMLElement(offsetParent);
      var scroll = {
        scrollLeft: 0,
        scrollTop: 0
      };
      var offsets = {
        x: 0,
        y: 0
      };

      if (isOffsetParentAnElement || !isOffsetParentAnElement && !isFixed) {
        if (getNodeName(offsetParent) !== 'body' || // https://github.com/popperjs/popper-core/issues/1078
        isScrollParent(documentElement)) {
          scroll = getNodeScroll(offsetParent);
        }

        if (isHTMLElement(offsetParent)) {
          offsets = getBoundingClientRect(offsetParent);
          offsets.x += offsetParent.clientLeft;
          offsets.y += offsetParent.clientTop;
        } else if (documentElement) {
          offsets.x = getWindowScrollBarX(documentElement);
        }
      }

      return {
        x: rect.left + scroll.scrollLeft - offsets.x,
        y: rect.top + scroll.scrollTop - offsets.y,
        width: rect.width,
        height: rect.height
      };
    }

    function order(modifiers) {
      var map = new Map();
      var visited = new Set();
      var result = [];
      modifiers.forEach(function (modifier) {
        map.set(modifier.name, modifier);
      }); // On visiting object, check for its dependencies and visit them recursively

      function sort(modifier) {
        visited.add(modifier.name);
        var requires = [].concat(modifier.requires || [], modifier.requiresIfExists || []);
        requires.forEach(function (dep) {
          if (!visited.has(dep)) {
            var depModifier = map.get(dep);

            if (depModifier) {
              sort(depModifier);
            }
          }
        });
        result.push(modifier);
      }

      modifiers.forEach(function (modifier) {
        if (!visited.has(modifier.name)) {
          // check for visited object
          sort(modifier);
        }
      });
      return result;
    }

    function orderModifiers(modifiers) {
      // order based on dependencies
      var orderedModifiers = order(modifiers); // order based on phase

      return modifierPhases.reduce(function (acc, phase) {
        return acc.concat(orderedModifiers.filter(function (modifier) {
          return modifier.phase === phase;
        }));
      }, []);
    }

    function debounce(fn) {
      var pending;
      return function () {
        if (!pending) {
          pending = new Promise(function (resolve) {
            Promise.resolve().then(function () {
              pending = undefined;
              resolve(fn());
            });
          });
        }

        return pending;
      };
    }

    function mergeByName(modifiers) {
      var merged = modifiers.reduce(function (merged, current) {
        var existing = merged[current.name];
        merged[current.name] = existing ? Object.assign(Object.assign(Object.assign({}, existing), current), {}, {
          options: Object.assign(Object.assign({}, existing.options), current.options),
          data: Object.assign(Object.assign({}, existing.data), current.data)
        }) : current;
        return merged;
      }, {}); // IE11 does not support Object.values

      return Object.keys(merged).map(function (key) {
        return merged[key];
      });
    }

    var DEFAULT_OPTIONS = {
      placement: 'bottom',
      modifiers: [],
      strategy: 'absolute'
    };

    function areValidElements() {
      for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return !args.some(function (element) {
        return !(element && typeof element.getBoundingClientRect === 'function');
      });
    }

    function popperGenerator(generatorOptions) {
      if (generatorOptions === void 0) {
        generatorOptions = {};
      }

      var _generatorOptions = generatorOptions,
          _generatorOptions$def = _generatorOptions.defaultModifiers,
          defaultModifiers = _generatorOptions$def === void 0 ? [] : _generatorOptions$def,
          _generatorOptions$def2 = _generatorOptions.defaultOptions,
          defaultOptions = _generatorOptions$def2 === void 0 ? DEFAULT_OPTIONS : _generatorOptions$def2;
      return function createPopper(reference, popper, options) {
        if (options === void 0) {
          options = defaultOptions;
        }

        var state = {
          placement: 'bottom',
          orderedModifiers: [],
          options: Object.assign(Object.assign({}, DEFAULT_OPTIONS), defaultOptions),
          modifiersData: {},
          elements: {
            reference: reference,
            popper: popper
          },
          attributes: {},
          styles: {}
        };
        var effectCleanupFns = [];
        var isDestroyed = false;
        var instance = {
          state: state,
          setOptions: function setOptions(options) {
            cleanupModifierEffects();
            state.options = Object.assign(Object.assign(Object.assign({}, defaultOptions), state.options), options);
            state.scrollParents = {
              reference: isElement(reference) ? listScrollParents(reference) : reference.contextElement ? listScrollParents(reference.contextElement) : [],
              popper: listScrollParents(popper)
            }; // Orders the modifiers based on their dependencies and `phase`
            // properties

            var orderedModifiers = orderModifiers(mergeByName([].concat(defaultModifiers, state.options.modifiers))); // Strip out disabled modifiers

            state.orderedModifiers = orderedModifiers.filter(function (m) {
              return m.enabled;
            }); // Validate the provided modifiers so that the consumer will get warned

            runModifierEffects();
            return instance.update();
          },
          // Sync update – it will always be executed, even if not necessary. This
          // is useful for low frequency updates where sync behavior simplifies the
          // logic.
          // For high frequency updates (e.g. `resize` and `scroll` events), always
          // prefer the async Popper#update method
          forceUpdate: function forceUpdate() {
            if (isDestroyed) {
              return;
            }

            var _state$elements = state.elements,
                reference = _state$elements.reference,
                popper = _state$elements.popper; // Don't proceed if `reference` or `popper` are not valid elements
            // anymore

            if (!areValidElements(reference, popper)) {

              return;
            } // Store the reference and popper rects to be read by modifiers


            state.rects = {
              reference: getCompositeRect(reference, getOffsetParent(popper), state.options.strategy === 'fixed'),
              popper: getLayoutRect(popper)
            }; // Modifiers have the ability to reset the current update cycle. The
            // most common use case for this is the `flip` modifier changing the
            // placement, which then needs to re-run all the modifiers, because the
            // logic was previously ran for the previous placement and is therefore
            // stale/incorrect

            state.reset = false;
            state.placement = state.options.placement; // On each update cycle, the `modifiersData` property for each modifier
            // is filled with the initial data specified by the modifier. This means
            // it doesn't persist and is fresh on each update.
            // To ensure persistent data, use `${name}#persistent`

            state.orderedModifiers.forEach(function (modifier) {
              return state.modifiersData[modifier.name] = Object.assign({}, modifier.data);
            });

            for (var index = 0; index < state.orderedModifiers.length; index++) {

              if (state.reset === true) {
                state.reset = false;
                index = -1;
                continue;
              }

              var _state$orderedModifie = state.orderedModifiers[index],
                  fn = _state$orderedModifie.fn,
                  _state$orderedModifie2 = _state$orderedModifie.options,
                  _options = _state$orderedModifie2 === void 0 ? {} : _state$orderedModifie2,
                  name = _state$orderedModifie.name;

              if (typeof fn === 'function') {
                state = fn({
                  state: state,
                  options: _options,
                  name: name,
                  instance: instance
                }) || state;
              }
            }
          },
          // Async and optimistically optimized update – it will not be executed if
          // not necessary (debounced to run at most once-per-tick)
          update: debounce(function () {
            return new Promise(function (resolve) {
              instance.forceUpdate();
              resolve(state);
            });
          }),
          destroy: function destroy() {
            cleanupModifierEffects();
            isDestroyed = true;
          }
        };

        if (!areValidElements(reference, popper)) {

          return instance;
        }

        instance.setOptions(options).then(function (state) {
          if (!isDestroyed && options.onFirstUpdate) {
            options.onFirstUpdate(state);
          }
        }); // Modifiers have the ability to execute arbitrary code before the first
        // update cycle runs. They will be executed in the same order as the update
        // cycle. This is useful when a modifier adds some persistent data that
        // other modifiers need to use, but the modifier is run after the dependent
        // one.

        function runModifierEffects() {
          state.orderedModifiers.forEach(function (_ref3) {
            var name = _ref3.name,
                _ref3$options = _ref3.options,
                options = _ref3$options === void 0 ? {} : _ref3$options,
                effect = _ref3.effect;

            if (typeof effect === 'function') {
              var cleanupFn = effect({
                state: state,
                name: name,
                instance: instance,
                options: options
              });

              var noopFn = function noopFn() {};

              effectCleanupFns.push(cleanupFn || noopFn);
            }
          });
        }

        function cleanupModifierEffects() {
          effectCleanupFns.forEach(function (fn) {
            return fn();
          });
          effectCleanupFns = [];
        }

        return instance;
      };
    }
    var createPopper = /*#__PURE__*/popperGenerator(); // eslint-disable-next-line import/no-unused-modules

    var defaultModifiers = [eventListeners, popperOffsets$1, computeStyles$1, applyStyles$1];
    var createPopper$1 = /*#__PURE__*/popperGenerator({
      defaultModifiers: defaultModifiers
    }); // eslint-disable-next-line import/no-unused-modules

    var defaultModifiers$1 = [eventListeners, popperOffsets$1, computeStyles$1, applyStyles$1, offset$1, flip$1, preventOverflow$1, arrow$1, hide$1];
    var createPopper$2 = /*#__PURE__*/popperGenerator({
      defaultModifiers: defaultModifiers$1
    }); // eslint-disable-next-line import/no-unused-modules

    var Popper = /*#__PURE__*/Object.freeze({
        __proto__: null,
        popperGenerator: popperGenerator,
        detectOverflow: detectOverflow,
        createPopperBase: createPopper,
        createPopper: createPopper$2,
        createPopperLite: createPopper$1,
        top: top,
        bottom: bottom,
        right: right,
        left: left,
        auto: auto,
        basePlacements: basePlacements,
        start: start,
        end: end,
        clippingParents: clippingParents,
        viewport: viewport,
        popper: popper,
        reference: reference,
        variationPlacements: variationPlacements,
        placements: placements,
        beforeRead: beforeRead,
        read: read,
        afterRead: afterRead,
        beforeMain: beforeMain,
        main: main,
        afterMain: afterMain,
        beforeWrite: beforeWrite,
        write: write,
        afterWrite: afterWrite,
        modifierPhases: modifierPhases,
        applyStyles: applyStyles$1,
        arrow: arrow$1,
        computeStyles: computeStyles$1,
        eventListeners: eventListeners,
        flip: flip$1,
        hide: hide$1,
        offset: offset$1,
        popperOffsets: popperOffsets$1,
        preventOverflow: preventOverflow$1
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
    // import {createPopper} from "@popperjs/core/dist/esm/popper";
    const Event$1 = {
        HIDE: "tobago.dropdown.hide",
        HIDDEN: "tobago.dropdown.hidden",
        SHOW: "tobago.dropdown.show",
        SHOWN: "tobago.dropdown.shown"
    };
    /**
     * The dropdown implementation of Bootstrap does not support submenus. Therefore we need an own dropdown implementation.
     */
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
                createPopper$2(this.toggleButton, this.dropdownMenu, {
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
        static convertPatternJava2Js(originalPattern) {
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
            this.overlay.classList.add(this.error ? "tobago-page-overlay-markup-error" : null);
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

    /*!
      * Bootstrap v5.0.0-beta1 (https://getbootstrap.com/)
      * Copyright 2011-2020 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
      * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
      */

    function _defineProperties(target, props) {
      for (var i = 0; i < props.length; i++) {
        var descriptor = props[i];
        descriptor.enumerable = descriptor.enumerable || false;
        descriptor.configurable = true;
        if ("value" in descriptor) descriptor.writable = true;
        Object.defineProperty(target, descriptor.key, descriptor);
      }
    }

    function _createClass(Constructor, protoProps, staticProps) {
      if (protoProps) _defineProperties(Constructor.prototype, protoProps);
      if (staticProps) _defineProperties(Constructor, staticProps);
      return Constructor;
    }

    function _extends() {
      _extends = Object.assign || function (target) {
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

      return _extends.apply(this, arguments);
    }

    function _inheritsLoose(subClass, superClass) {
      subClass.prototype = Object.create(superClass.prototype);
      subClass.prototype.constructor = subClass;
      subClass.__proto__ = superClass;
    }

    /**
     * --------------------------------------------------------------------------
     * Bootstrap (v5.0.0-beta1): util/index.js
     * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
     * --------------------------------------------------------------------------
     */
    var MAX_UID = 1000000;
    var MILLISECONDS_MULTIPLIER = 1000;
    var TRANSITION_END = 'transitionend'; // Shoutout AngusCroll (https://goo.gl/pxwQGp)

    var toType = function toType(obj) {
      if (obj === null || obj === undefined) {
        return "" + obj;
      }

      return {}.toString.call(obj).match(/\s([a-z]+)/i)[1].toLowerCase();
    };
    /**
     * --------------------------------------------------------------------------
     * Public Util Api
     * --------------------------------------------------------------------------
     */


    var getUID = function getUID(prefix) {
      do {
        prefix += Math.floor(Math.random() * MAX_UID);
      } while (document.getElementById(prefix));

      return prefix;
    };

    var getSelector = function getSelector(element) {
      var selector = element.getAttribute('data-bs-target');

      if (!selector || selector === '#') {
        var hrefAttr = element.getAttribute('href');
        selector = hrefAttr && hrefAttr !== '#' ? hrefAttr.trim() : null;
      }

      return selector;
    };

    var getSelectorFromElement = function getSelectorFromElement(element) {
      var selector = getSelector(element);

      if (selector) {
        return document.querySelector(selector) ? selector : null;
      }

      return null;
    };

    var getElementFromSelector = function getElementFromSelector(element) {
      var selector = getSelector(element);
      return selector ? document.querySelector(selector) : null;
    };

    var getTransitionDurationFromElement = function getTransitionDurationFromElement(element) {
      if (!element) {
        return 0;
      } // Get transition-duration of the element


      var _window$getComputedSt = window.getComputedStyle(element),
          transitionDuration = _window$getComputedSt.transitionDuration,
          transitionDelay = _window$getComputedSt.transitionDelay;

      var floatTransitionDuration = Number.parseFloat(transitionDuration);
      var floatTransitionDelay = Number.parseFloat(transitionDelay); // Return 0 if element or transition duration is not found

      if (!floatTransitionDuration && !floatTransitionDelay) {
        return 0;
      } // If multiple durations are defined, take the first


      transitionDuration = transitionDuration.split(',')[0];
      transitionDelay = transitionDelay.split(',')[0];
      return (Number.parseFloat(transitionDuration) + Number.parseFloat(transitionDelay)) * MILLISECONDS_MULTIPLIER;
    };

    var triggerTransitionEnd = function triggerTransitionEnd(element) {
      element.dispatchEvent(new Event(TRANSITION_END));
    };

    var isElement$1 = function isElement(obj) {
      return (obj[0] || obj).nodeType;
    };

    var emulateTransitionEnd = function emulateTransitionEnd(element, duration) {
      var called = false;
      var durationPadding = 5;
      var emulatedDuration = duration + durationPadding;

      function listener() {
        called = true;
        element.removeEventListener(TRANSITION_END, listener);
      }

      element.addEventListener(TRANSITION_END, listener);
      setTimeout(function () {
        if (!called) {
          triggerTransitionEnd(element);
        }
      }, emulatedDuration);
    };

    var typeCheckConfig = function typeCheckConfig(componentName, config, configTypes) {
      Object.keys(configTypes).forEach(function (property) {
        var expectedTypes = configTypes[property];
        var value = config[property];
        var valueType = value && isElement$1(value) ? 'element' : toType(value);

        if (!new RegExp(expectedTypes).test(valueType)) {
          throw new Error(componentName.toUpperCase() + ": " + ("Option \"" + property + "\" provided type \"" + valueType + "\" ") + ("but expected type \"" + expectedTypes + "\"."));
        }
      });
    };

    var isVisible = function isVisible(element) {
      if (!element) {
        return false;
      }

      if (element.style && element.parentNode && element.parentNode.style) {
        var elementStyle = getComputedStyle(element);
        var parentNodeStyle = getComputedStyle(element.parentNode);
        return elementStyle.display !== 'none' && parentNodeStyle.display !== 'none' && elementStyle.visibility !== 'hidden';
      }

      return false;
    };

    var findShadowRoot = function findShadowRoot(element) {
      if (!document.documentElement.attachShadow) {
        return null;
      } // Can find the shadow root otherwise it'll return the document


      if (typeof element.getRootNode === 'function') {
        var root = element.getRootNode();
        return root instanceof ShadowRoot ? root : null;
      }

      if (element instanceof ShadowRoot) {
        return element;
      } // when we don't find a shadow root


      if (!element.parentNode) {
        return null;
      }

      return findShadowRoot(element.parentNode);
    };

    var noop = function noop() {
      return function () {};
    };

    var reflow = function reflow(element) {
      return element.offsetHeight;
    };

    var getjQuery = function getjQuery() {
      var _window = window,
          jQuery = _window.jQuery;

      if (jQuery && !document.body.hasAttribute('data-bs-no-jquery')) {
        return jQuery;
      }

      return null;
    };

    var onDOMContentLoaded = function onDOMContentLoaded(callback) {
      if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', callback);
      } else {
        callback();
      }
    };

    var isRTL = document.documentElement.dir === 'rtl';

    /**
     * --------------------------------------------------------------------------
     * Bootstrap (v5.0.0-beta1): dom/data.js
     * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
     * --------------------------------------------------------------------------
     */

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */
    var mapData = function () {
      var storeData = {};
      var id = 1;
      return {
        set: function set(element, key, data) {
          if (typeof element.bsKey === 'undefined') {
            element.bsKey = {
              key: key,
              id: id
            };
            id++;
          }

          storeData[element.bsKey.id] = data;
        },
        get: function get(element, key) {
          if (!element || typeof element.bsKey === 'undefined') {
            return null;
          }

          var keyProperties = element.bsKey;

          if (keyProperties.key === key) {
            return storeData[keyProperties.id];
          }

          return null;
        },
        delete: function _delete(element, key) {
          if (typeof element.bsKey === 'undefined') {
            return;
          }

          var keyProperties = element.bsKey;

          if (keyProperties.key === key) {
            delete storeData[keyProperties.id];
            delete element.bsKey;
          }
        }
      };
    }();

    var Data = {
      setData: function setData(instance, key, data) {
        mapData.set(instance, key, data);
      },
      getData: function getData(instance, key) {
        return mapData.get(instance, key);
      },
      removeData: function removeData(instance, key) {
        mapData.delete(instance, key);
      }
    };

    /**
     * --------------------------------------------------------------------------
     * Bootstrap (v5.0.0-beta1): dom/event-handler.js
     * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
     * --------------------------------------------------------------------------
     */
    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var namespaceRegex = /[^.]*(?=\..*)\.|.*/;
    var stripNameRegex = /\..*/;
    var stripUidRegex = /::\d+$/;
    var eventRegistry = {}; // Events storage

    var uidEvent = 1;
    var customEvents = {
      mouseenter: 'mouseover',
      mouseleave: 'mouseout'
    };
    var nativeEvents = new Set(['click', 'dblclick', 'mouseup', 'mousedown', 'contextmenu', 'mousewheel', 'DOMMouseScroll', 'mouseover', 'mouseout', 'mousemove', 'selectstart', 'selectend', 'keydown', 'keypress', 'keyup', 'orientationchange', 'touchstart', 'touchmove', 'touchend', 'touchcancel', 'pointerdown', 'pointermove', 'pointerup', 'pointerleave', 'pointercancel', 'gesturestart', 'gesturechange', 'gestureend', 'focus', 'blur', 'change', 'reset', 'select', 'submit', 'focusin', 'focusout', 'load', 'unload', 'beforeunload', 'resize', 'move', 'DOMContentLoaded', 'readystatechange', 'error', 'abort', 'scroll']);
    /**
     * ------------------------------------------------------------------------
     * Private methods
     * ------------------------------------------------------------------------
     */

    function getUidEvent(element, uid) {
      return uid && uid + "::" + uidEvent++ || element.uidEvent || uidEvent++;
    }

    function getEvent(element) {
      var uid = getUidEvent(element);
      element.uidEvent = uid;
      eventRegistry[uid] = eventRegistry[uid] || {};
      return eventRegistry[uid];
    }

    function bootstrapHandler(element, fn) {
      return function handler(event) {
        event.delegateTarget = element;

        if (handler.oneOff) {
          EventHandler.off(element, event.type, fn);
        }

        return fn.apply(element, [event]);
      };
    }

    function bootstrapDelegationHandler(element, selector, fn) {
      return function handler(event) {
        var domElements = element.querySelectorAll(selector);

        for (var target = event.target; target && target !== this; target = target.parentNode) {
          for (var i = domElements.length; i--;) {
            if (domElements[i] === target) {
              event.delegateTarget = target;

              if (handler.oneOff) {
                EventHandler.off(element, event.type, fn);
              }

              return fn.apply(target, [event]);
            }
          }
        } // To please ESLint


        return null;
      };
    }

    function findHandler(events, handler, delegationSelector) {
      if (delegationSelector === void 0) {
        delegationSelector = null;
      }

      var uidEventList = Object.keys(events);

      for (var i = 0, len = uidEventList.length; i < len; i++) {
        var event = events[uidEventList[i]];

        if (event.originalHandler === handler && event.delegationSelector === delegationSelector) {
          return event;
        }
      }

      return null;
    }

    function normalizeParams(originalTypeEvent, handler, delegationFn) {
      var delegation = typeof handler === 'string';
      var originalHandler = delegation ? delegationFn : handler; // allow to get the native events from namespaced events ('click.bs.button' --> 'click')

      var typeEvent = originalTypeEvent.replace(stripNameRegex, '');
      var custom = customEvents[typeEvent];

      if (custom) {
        typeEvent = custom;
      }

      var isNative = nativeEvents.has(typeEvent);

      if (!isNative) {
        typeEvent = originalTypeEvent;
      }

      return [delegation, originalHandler, typeEvent];
    }

    function addHandler(element, originalTypeEvent, handler, delegationFn, oneOff) {
      if (typeof originalTypeEvent !== 'string' || !element) {
        return;
      }

      if (!handler) {
        handler = delegationFn;
        delegationFn = null;
      }

      var _normalizeParams = normalizeParams(originalTypeEvent, handler, delegationFn),
          delegation = _normalizeParams[0],
          originalHandler = _normalizeParams[1],
          typeEvent = _normalizeParams[2];

      var events = getEvent(element);
      var handlers = events[typeEvent] || (events[typeEvent] = {});
      var previousFn = findHandler(handlers, originalHandler, delegation ? handler : null);

      if (previousFn) {
        previousFn.oneOff = previousFn.oneOff && oneOff;
        return;
      }

      var uid = getUidEvent(originalHandler, originalTypeEvent.replace(namespaceRegex, ''));
      var fn = delegation ? bootstrapDelegationHandler(element, handler, delegationFn) : bootstrapHandler(element, handler);
      fn.delegationSelector = delegation ? handler : null;
      fn.originalHandler = originalHandler;
      fn.oneOff = oneOff;
      fn.uidEvent = uid;
      handlers[uid] = fn;
      element.addEventListener(typeEvent, fn, delegation);
    }

    function removeHandler(element, events, typeEvent, handler, delegationSelector) {
      var fn = findHandler(events[typeEvent], handler, delegationSelector);

      if (!fn) {
        return;
      }

      element.removeEventListener(typeEvent, fn, Boolean(delegationSelector));
      delete events[typeEvent][fn.uidEvent];
    }

    function removeNamespacedHandlers(element, events, typeEvent, namespace) {
      var storeElementEvent = events[typeEvent] || {};
      Object.keys(storeElementEvent).forEach(function (handlerKey) {
        if (handlerKey.includes(namespace)) {
          var event = storeElementEvent[handlerKey];
          removeHandler(element, events, typeEvent, event.originalHandler, event.delegationSelector);
        }
      });
    }

    var EventHandler = {
      on: function on(element, event, handler, delegationFn) {
        addHandler(element, event, handler, delegationFn, false);
      },
      one: function one(element, event, handler, delegationFn) {
        addHandler(element, event, handler, delegationFn, true);
      },
      off: function off(element, originalTypeEvent, handler, delegationFn) {
        if (typeof originalTypeEvent !== 'string' || !element) {
          return;
        }

        var _normalizeParams2 = normalizeParams(originalTypeEvent, handler, delegationFn),
            delegation = _normalizeParams2[0],
            originalHandler = _normalizeParams2[1],
            typeEvent = _normalizeParams2[2];

        var inNamespace = typeEvent !== originalTypeEvent;
        var events = getEvent(element);
        var isNamespace = originalTypeEvent.startsWith('.');

        if (typeof originalHandler !== 'undefined') {
          // Simplest case: handler is passed, remove that listener ONLY.
          if (!events || !events[typeEvent]) {
            return;
          }

          removeHandler(element, events, typeEvent, originalHandler, delegation ? handler : null);
          return;
        }

        if (isNamespace) {
          Object.keys(events).forEach(function (elementEvent) {
            removeNamespacedHandlers(element, events, elementEvent, originalTypeEvent.slice(1));
          });
        }

        var storeElementEvent = events[typeEvent] || {};
        Object.keys(storeElementEvent).forEach(function (keyHandlers) {
          var handlerKey = keyHandlers.replace(stripUidRegex, '');

          if (!inNamespace || originalTypeEvent.includes(handlerKey)) {
            var event = storeElementEvent[keyHandlers];
            removeHandler(element, events, typeEvent, event.originalHandler, event.delegationSelector);
          }
        });
      },
      trigger: function trigger(element, event, args) {
        if (typeof event !== 'string' || !element) {
          return null;
        }

        var $ = getjQuery();
        var typeEvent = event.replace(stripNameRegex, '');
        var inNamespace = event !== typeEvent;
        var isNative = nativeEvents.has(typeEvent);
        var jQueryEvent;
        var bubbles = true;
        var nativeDispatch = true;
        var defaultPrevented = false;
        var evt = null;

        if (inNamespace && $) {
          jQueryEvent = $.Event(event, args);
          $(element).trigger(jQueryEvent);
          bubbles = !jQueryEvent.isPropagationStopped();
          nativeDispatch = !jQueryEvent.isImmediatePropagationStopped();
          defaultPrevented = jQueryEvent.isDefaultPrevented();
        }

        if (isNative) {
          evt = document.createEvent('HTMLEvents');
          evt.initEvent(typeEvent, bubbles, true);
        } else {
          evt = new CustomEvent(event, {
            bubbles: bubbles,
            cancelable: true
          });
        } // merge custom information in our event


        if (typeof args !== 'undefined') {
          Object.keys(args).forEach(function (key) {
            Object.defineProperty(evt, key, {
              get: function get() {
                return args[key];
              }
            });
          });
        }

        if (defaultPrevented) {
          evt.preventDefault();
        }

        if (nativeDispatch) {
          element.dispatchEvent(evt);
        }

        if (evt.defaultPrevented && typeof jQueryEvent !== 'undefined') {
          jQueryEvent.preventDefault();
        }

        return evt;
      }
    };

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var VERSION = '5.0.0-beta1';

    var BaseComponent = /*#__PURE__*/function () {
      function BaseComponent(element) {
        if (!element) {
          return;
        }

        this._element = element;
        Data.setData(element, this.constructor.DATA_KEY, this);
      }

      var _proto = BaseComponent.prototype;

      _proto.dispose = function dispose() {
        Data.removeData(this._element, this.constructor.DATA_KEY);
        this._element = null;
      }
      /** Static */
      ;

      BaseComponent.getInstance = function getInstance(element) {
        return Data.getData(element, this.DATA_KEY);
      };

      _createClass(BaseComponent, null, [{
        key: "VERSION",
        get: function get() {
          return VERSION;
        }
      }]);

      return BaseComponent;
    }();

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME = 'alert';
    var DATA_KEY = 'bs.alert';
    var EVENT_KEY = "." + DATA_KEY;
    var DATA_API_KEY = '.data-api';
    var SELECTOR_DISMISS = '[data-bs-dismiss="alert"]';
    var EVENT_CLOSE = "close" + EVENT_KEY;
    var EVENT_CLOSED = "closed" + EVENT_KEY;
    var EVENT_CLICK_DATA_API = "click" + EVENT_KEY + DATA_API_KEY;
    var CLASSNAME_ALERT = 'alert';
    var CLASSNAME_FADE = 'fade';
    var CLASSNAME_SHOW = 'show';
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Alert = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(Alert, _BaseComponent);

      function Alert() {
        return _BaseComponent.apply(this, arguments) || this;
      }

      var _proto = Alert.prototype;

      // Public
      _proto.close = function close(element) {
        var rootElement = element ? this._getRootElement(element) : this._element;

        var customEvent = this._triggerCloseEvent(rootElement);

        if (customEvent === null || customEvent.defaultPrevented) {
          return;
        }

        this._removeElement(rootElement);
      } // Private
      ;

      _proto._getRootElement = function _getRootElement(element) {
        return getElementFromSelector(element) || element.closest("." + CLASSNAME_ALERT);
      };

      _proto._triggerCloseEvent = function _triggerCloseEvent(element) {
        return EventHandler.trigger(element, EVENT_CLOSE);
      };

      _proto._removeElement = function _removeElement(element) {
        var _this = this;

        element.classList.remove(CLASSNAME_SHOW);

        if (!element.classList.contains(CLASSNAME_FADE)) {
          this._destroyElement(element);

          return;
        }

        var transitionDuration = getTransitionDurationFromElement(element);
        EventHandler.one(element, TRANSITION_END, function () {
          return _this._destroyElement(element);
        });
        emulateTransitionEnd(element, transitionDuration);
      };

      _proto._destroyElement = function _destroyElement(element) {
        if (element.parentNode) {
          element.parentNode.removeChild(element);
        }

        EventHandler.trigger(element, EVENT_CLOSED);
      } // Static
      ;

      Alert.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          var data = Data.getData(this, DATA_KEY);

          if (!data) {
            data = new Alert(this);
          }

          if (config === 'close') {
            data[config](this);
          }
        });
      };

      Alert.handleDismiss = function handleDismiss(alertInstance) {
        return function (event) {
          if (event) {
            event.preventDefault();
          }

          alertInstance.close(this);
        };
      };

      _createClass(Alert, null, [{
        key: "DATA_KEY",
        // Getters
        get: function get() {
          return DATA_KEY;
        }
      }]);

      return Alert;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * Data Api implementation
     * ------------------------------------------------------------------------
     */


    EventHandler.on(document, EVENT_CLICK_DATA_API, SELECTOR_DISMISS, Alert.handleDismiss(new Alert()));
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Alert to jQuery only if jQuery is present
     */

    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME];
        $.fn[NAME] = Alert.jQueryInterface;
        $.fn[NAME].Constructor = Alert;

        $.fn[NAME].noConflict = function () {
          $.fn[NAME] = JQUERY_NO_CONFLICT;
          return Alert.jQueryInterface;
        };
      }
    });

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$1 = 'button';
    var DATA_KEY$1 = 'bs.button';
    var EVENT_KEY$1 = "." + DATA_KEY$1;
    var DATA_API_KEY$1 = '.data-api';
    var CLASS_NAME_ACTIVE = 'active';
    var SELECTOR_DATA_TOGGLE = '[data-bs-toggle="button"]';
    var EVENT_CLICK_DATA_API$1 = "click" + EVENT_KEY$1 + DATA_API_KEY$1;
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Button = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(Button, _BaseComponent);

      function Button() {
        return _BaseComponent.apply(this, arguments) || this;
      }

      var _proto = Button.prototype;

      // Public
      _proto.toggle = function toggle() {
        // Toggle class and sync the `aria-pressed` attribute with the return value of the `.toggle()` method
        this._element.setAttribute('aria-pressed', this._element.classList.toggle(CLASS_NAME_ACTIVE));
      } // Static
      ;

      Button.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          var data = Data.getData(this, DATA_KEY$1);

          if (!data) {
            data = new Button(this);
          }

          if (config === 'toggle') {
            data[config]();
          }
        });
      };

      _createClass(Button, null, [{
        key: "DATA_KEY",
        // Getters
        get: function get() {
          return DATA_KEY$1;
        }
      }]);

      return Button;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * Data Api implementation
     * ------------------------------------------------------------------------
     */


    EventHandler.on(document, EVENT_CLICK_DATA_API$1, SELECTOR_DATA_TOGGLE, function (event) {
      event.preventDefault();
      var button = event.target.closest(SELECTOR_DATA_TOGGLE);
      var data = Data.getData(button, DATA_KEY$1);

      if (!data) {
        data = new Button(button);
      }

      data.toggle();
    });
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Button to jQuery only if jQuery is present
     */

    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$1];
        $.fn[NAME$1] = Button.jQueryInterface;
        $.fn[NAME$1].Constructor = Button;

        $.fn[NAME$1].noConflict = function () {
          $.fn[NAME$1] = JQUERY_NO_CONFLICT;
          return Button.jQueryInterface;
        };
      }
    });

    /**
     * --------------------------------------------------------------------------
     * Bootstrap (v5.0.0-beta1): dom/manipulator.js
     * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
     * --------------------------------------------------------------------------
     */
    function normalizeData(val) {
      if (val === 'true') {
        return true;
      }

      if (val === 'false') {
        return false;
      }

      if (val === Number(val).toString()) {
        return Number(val);
      }

      if (val === '' || val === 'null') {
        return null;
      }

      return val;
    }

    function normalizeDataKey(key) {
      return key.replace(/[A-Z]/g, function (chr) {
        return "-" + chr.toLowerCase();
      });
    }

    var Manipulator = {
      setDataAttribute: function setDataAttribute(element, key, value) {
        element.setAttribute("data-bs-" + normalizeDataKey(key), value);
      },
      removeDataAttribute: function removeDataAttribute(element, key) {
        element.removeAttribute("data-bs-" + normalizeDataKey(key));
      },
      getDataAttributes: function getDataAttributes(element) {
        if (!element) {
          return {};
        }

        var attributes = {};
        Object.keys(element.dataset).filter(function (key) {
          return key.startsWith('bs');
        }).forEach(function (key) {
          var pureKey = key.replace(/^bs/, '');
          pureKey = pureKey.charAt(0).toLowerCase() + pureKey.slice(1, pureKey.length);
          attributes[pureKey] = normalizeData(element.dataset[key]);
        });
        return attributes;
      },
      getDataAttribute: function getDataAttribute(element, key) {
        return normalizeData(element.getAttribute("data-bs-" + normalizeDataKey(key)));
      },
      offset: function offset(element) {
        var rect = element.getBoundingClientRect();
        return {
          top: rect.top + document.body.scrollTop,
          left: rect.left + document.body.scrollLeft
        };
      },
      position: function position(element) {
        return {
          top: element.offsetTop,
          left: element.offsetLeft
        };
      }
    };

    /**
     * --------------------------------------------------------------------------
     * Bootstrap (v5.0.0-beta1): dom/selector-engine.js
     * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
     * --------------------------------------------------------------------------
     */

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */
    var NODE_TEXT = 3;
    var SelectorEngine = {
      matches: function matches(element, selector) {
        return element.matches(selector);
      },
      find: function find(selector, element) {
        var _ref;

        if (element === void 0) {
          element = document.documentElement;
        }

        return (_ref = []).concat.apply(_ref, Element.prototype.querySelectorAll.call(element, selector));
      },
      findOne: function findOne(selector, element) {
        if (element === void 0) {
          element = document.documentElement;
        }

        return Element.prototype.querySelector.call(element, selector);
      },
      children: function children(element, selector) {
        var _ref2;

        var children = (_ref2 = []).concat.apply(_ref2, element.children);

        return children.filter(function (child) {
          return child.matches(selector);
        });
      },
      parents: function parents(element, selector) {
        var parents = [];
        var ancestor = element.parentNode;

        while (ancestor && ancestor.nodeType === Node.ELEMENT_NODE && ancestor.nodeType !== NODE_TEXT) {
          if (this.matches(ancestor, selector)) {
            parents.push(ancestor);
          }

          ancestor = ancestor.parentNode;
        }

        return parents;
      },
      prev: function prev(element, selector) {
        var previous = element.previousElementSibling;

        while (previous) {
          if (previous.matches(selector)) {
            return [previous];
          }

          previous = previous.previousElementSibling;
        }

        return [];
      },
      next: function next(element, selector) {
        var next = element.nextElementSibling;

        while (next) {
          if (this.matches(next, selector)) {
            return [next];
          }

          next = next.nextElementSibling;
        }

        return [];
      }
    };

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$2 = 'carousel';
    var DATA_KEY$2 = 'bs.carousel';
    var EVENT_KEY$2 = "." + DATA_KEY$2;
    var DATA_API_KEY$2 = '.data-api';
    var ARROW_LEFT_KEY = 'ArrowLeft';
    var ARROW_RIGHT_KEY = 'ArrowRight';
    var TOUCHEVENT_COMPAT_WAIT = 500; // Time for mouse compat events to fire after touch

    var SWIPE_THRESHOLD = 40;
    var Default = {
      interval: 5000,
      keyboard: true,
      slide: false,
      pause: 'hover',
      wrap: true,
      touch: true
    };
    var DefaultType = {
      interval: '(number|boolean)',
      keyboard: 'boolean',
      slide: '(boolean|string)',
      pause: '(string|boolean)',
      wrap: 'boolean',
      touch: 'boolean'
    };
    var DIRECTION_NEXT = 'next';
    var DIRECTION_PREV = 'prev';
    var DIRECTION_LEFT = 'left';
    var DIRECTION_RIGHT = 'right';
    var EVENT_SLIDE = "slide" + EVENT_KEY$2;
    var EVENT_SLID = "slid" + EVENT_KEY$2;
    var EVENT_KEYDOWN = "keydown" + EVENT_KEY$2;
    var EVENT_MOUSEENTER = "mouseenter" + EVENT_KEY$2;
    var EVENT_MOUSELEAVE = "mouseleave" + EVENT_KEY$2;
    var EVENT_TOUCHSTART = "touchstart" + EVENT_KEY$2;
    var EVENT_TOUCHMOVE = "touchmove" + EVENT_KEY$2;
    var EVENT_TOUCHEND = "touchend" + EVENT_KEY$2;
    var EVENT_POINTERDOWN = "pointerdown" + EVENT_KEY$2;
    var EVENT_POINTERUP = "pointerup" + EVENT_KEY$2;
    var EVENT_DRAG_START = "dragstart" + EVENT_KEY$2;
    var EVENT_LOAD_DATA_API = "load" + EVENT_KEY$2 + DATA_API_KEY$2;
    var EVENT_CLICK_DATA_API$2 = "click" + EVENT_KEY$2 + DATA_API_KEY$2;
    var CLASS_NAME_CAROUSEL = 'carousel';
    var CLASS_NAME_ACTIVE$1 = 'active';
    var CLASS_NAME_SLIDE = 'slide';
    var CLASS_NAME_END = 'carousel-item-end';
    var CLASS_NAME_START = 'carousel-item-start';
    var CLASS_NAME_NEXT = 'carousel-item-next';
    var CLASS_NAME_PREV = 'carousel-item-prev';
    var CLASS_NAME_POINTER_EVENT = 'pointer-event';
    var SELECTOR_ACTIVE = '.active';
    var SELECTOR_ACTIVE_ITEM = '.active.carousel-item';
    var SELECTOR_ITEM = '.carousel-item';
    var SELECTOR_ITEM_IMG = '.carousel-item img';
    var SELECTOR_NEXT_PREV = '.carousel-item-next, .carousel-item-prev';
    var SELECTOR_INDICATORS = '.carousel-indicators';
    var SELECTOR_DATA_SLIDE = '[data-bs-slide], [data-bs-slide-to]';
    var SELECTOR_DATA_RIDE = '[data-bs-ride="carousel"]';
    var PointerType = {
      TOUCH: 'touch',
      PEN: 'pen'
    };
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Carousel = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(Carousel, _BaseComponent);

      function Carousel(element, config) {
        var _this;

        _this = _BaseComponent.call(this, element) || this;
        _this._items = null;
        _this._interval = null;
        _this._activeElement = null;
        _this._isPaused = false;
        _this._isSliding = false;
        _this.touchTimeout = null;
        _this.touchStartX = 0;
        _this.touchDeltaX = 0;
        _this._config = _this._getConfig(config);
        _this._indicatorsElement = SelectorEngine.findOne(SELECTOR_INDICATORS, _this._element);
        _this._touchSupported = 'ontouchstart' in document.documentElement || navigator.maxTouchPoints > 0;
        _this._pointerEvent = Boolean(window.PointerEvent);

        _this._addEventListeners();

        return _this;
      } // Getters


      var _proto = Carousel.prototype;

      // Public
      _proto.next = function next() {
        if (!this._isSliding) {
          this._slide(DIRECTION_NEXT);
        }
      };

      _proto.nextWhenVisible = function nextWhenVisible() {
        // Don't call next when the page isn't visible
        // or the carousel or its parent isn't visible
        if (!document.hidden && isVisible(this._element)) {
          this.next();
        }
      };

      _proto.prev = function prev() {
        if (!this._isSliding) {
          this._slide(DIRECTION_PREV);
        }
      };

      _proto.pause = function pause(event) {
        if (!event) {
          this._isPaused = true;
        }

        if (SelectorEngine.findOne(SELECTOR_NEXT_PREV, this._element)) {
          triggerTransitionEnd(this._element);
          this.cycle(true);
        }

        clearInterval(this._interval);
        this._interval = null;
      };

      _proto.cycle = function cycle(event) {
        if (!event) {
          this._isPaused = false;
        }

        if (this._interval) {
          clearInterval(this._interval);
          this._interval = null;
        }

        if (this._config && this._config.interval && !this._isPaused) {
          this._updateInterval();

          this._interval = setInterval((document.visibilityState ? this.nextWhenVisible : this.next).bind(this), this._config.interval);
        }
      };

      _proto.to = function to(index) {
        var _this2 = this;

        this._activeElement = SelectorEngine.findOne(SELECTOR_ACTIVE_ITEM, this._element);

        var activeIndex = this._getItemIndex(this._activeElement);

        if (index > this._items.length - 1 || index < 0) {
          return;
        }

        if (this._isSliding) {
          EventHandler.one(this._element, EVENT_SLID, function () {
            return _this2.to(index);
          });
          return;
        }

        if (activeIndex === index) {
          this.pause();
          this.cycle();
          return;
        }

        var direction = index > activeIndex ? DIRECTION_NEXT : DIRECTION_PREV;

        this._slide(direction, this._items[index]);
      };

      _proto.dispose = function dispose() {
        _BaseComponent.prototype.dispose.call(this);

        EventHandler.off(this._element, EVENT_KEY$2);
        this._items = null;
        this._config = null;
        this._interval = null;
        this._isPaused = null;
        this._isSliding = null;
        this._activeElement = null;
        this._indicatorsElement = null;
      } // Private
      ;

      _proto._getConfig = function _getConfig(config) {
        config = _extends({}, Default, config);
        typeCheckConfig(NAME$2, config, DefaultType);
        return config;
      };

      _proto._handleSwipe = function _handleSwipe() {
        var absDeltax = Math.abs(this.touchDeltaX);

        if (absDeltax <= SWIPE_THRESHOLD) {
          return;
        }

        var direction = absDeltax / this.touchDeltaX;
        this.touchDeltaX = 0; // swipe left

        if (direction > 0) {
          this.prev();
        } // swipe right


        if (direction < 0) {
          this.next();
        }
      };

      _proto._addEventListeners = function _addEventListeners() {
        var _this3 = this;

        if (this._config.keyboard) {
          EventHandler.on(this._element, EVENT_KEYDOWN, function (event) {
            return _this3._keydown(event);
          });
        }

        if (this._config.pause === 'hover') {
          EventHandler.on(this._element, EVENT_MOUSEENTER, function (event) {
            return _this3.pause(event);
          });
          EventHandler.on(this._element, EVENT_MOUSELEAVE, function (event) {
            return _this3.cycle(event);
          });
        }

        if (this._config.touch && this._touchSupported) {
          this._addTouchEventListeners();
        }
      };

      _proto._addTouchEventListeners = function _addTouchEventListeners() {
        var _this4 = this;

        var start = function start(event) {
          if (_this4._pointerEvent && PointerType[event.pointerType.toUpperCase()]) {
            _this4.touchStartX = event.clientX;
          } else if (!_this4._pointerEvent) {
            _this4.touchStartX = event.touches[0].clientX;
          }
        };

        var move = function move(event) {
          // ensure swiping with one touch and not pinching
          if (event.touches && event.touches.length > 1) {
            _this4.touchDeltaX = 0;
          } else {
            _this4.touchDeltaX = event.touches[0].clientX - _this4.touchStartX;
          }
        };

        var end = function end(event) {
          if (_this4._pointerEvent && PointerType[event.pointerType.toUpperCase()]) {
            _this4.touchDeltaX = event.clientX - _this4.touchStartX;
          }

          _this4._handleSwipe();

          if (_this4._config.pause === 'hover') {
            // If it's a touch-enabled device, mouseenter/leave are fired as
            // part of the mouse compatibility events on first tap - the carousel
            // would stop cycling until user tapped out of it;
            // here, we listen for touchend, explicitly pause the carousel
            // (as if it's the second time we tap on it, mouseenter compat event
            // is NOT fired) and after a timeout (to allow for mouse compatibility
            // events to fire) we explicitly restart cycling
            _this4.pause();

            if (_this4.touchTimeout) {
              clearTimeout(_this4.touchTimeout);
            }

            _this4.touchTimeout = setTimeout(function (event) {
              return _this4.cycle(event);
            }, TOUCHEVENT_COMPAT_WAIT + _this4._config.interval);
          }
        };

        SelectorEngine.find(SELECTOR_ITEM_IMG, this._element).forEach(function (itemImg) {
          EventHandler.on(itemImg, EVENT_DRAG_START, function (e) {
            return e.preventDefault();
          });
        });

        if (this._pointerEvent) {
          EventHandler.on(this._element, EVENT_POINTERDOWN, function (event) {
            return start(event);
          });
          EventHandler.on(this._element, EVENT_POINTERUP, function (event) {
            return end(event);
          });

          this._element.classList.add(CLASS_NAME_POINTER_EVENT);
        } else {
          EventHandler.on(this._element, EVENT_TOUCHSTART, function (event) {
            return start(event);
          });
          EventHandler.on(this._element, EVENT_TOUCHMOVE, function (event) {
            return move(event);
          });
          EventHandler.on(this._element, EVENT_TOUCHEND, function (event) {
            return end(event);
          });
        }
      };

      _proto._keydown = function _keydown(event) {
        if (/input|textarea/i.test(event.target.tagName)) {
          return;
        }

        switch (event.key) {
          case ARROW_LEFT_KEY:
            event.preventDefault();
            this.prev();
            break;

          case ARROW_RIGHT_KEY:
            event.preventDefault();
            this.next();
            break;
        }
      };

      _proto._getItemIndex = function _getItemIndex(element) {
        this._items = element && element.parentNode ? SelectorEngine.find(SELECTOR_ITEM, element.parentNode) : [];
        return this._items.indexOf(element);
      };

      _proto._getItemByDirection = function _getItemByDirection(direction, activeElement) {
        var isNextDirection = direction === DIRECTION_NEXT;
        var isPrevDirection = direction === DIRECTION_PREV;

        var activeIndex = this._getItemIndex(activeElement);

        var lastItemIndex = this._items.length - 1;
        var isGoingToWrap = isPrevDirection && activeIndex === 0 || isNextDirection && activeIndex === lastItemIndex;

        if (isGoingToWrap && !this._config.wrap) {
          return activeElement;
        }

        var delta = direction === DIRECTION_PREV ? -1 : 1;
        var itemIndex = (activeIndex + delta) % this._items.length;
        return itemIndex === -1 ? this._items[this._items.length - 1] : this._items[itemIndex];
      };

      _proto._triggerSlideEvent = function _triggerSlideEvent(relatedTarget, eventDirectionName) {
        var targetIndex = this._getItemIndex(relatedTarget);

        var fromIndex = this._getItemIndex(SelectorEngine.findOne(SELECTOR_ACTIVE_ITEM, this._element));

        return EventHandler.trigger(this._element, EVENT_SLIDE, {
          relatedTarget: relatedTarget,
          direction: eventDirectionName,
          from: fromIndex,
          to: targetIndex
        });
      };

      _proto._setActiveIndicatorElement = function _setActiveIndicatorElement(element) {
        if (this._indicatorsElement) {
          var indicators = SelectorEngine.find(SELECTOR_ACTIVE, this._indicatorsElement);

          for (var i = 0; i < indicators.length; i++) {
            indicators[i].classList.remove(CLASS_NAME_ACTIVE$1);
          }

          var nextIndicator = this._indicatorsElement.children[this._getItemIndex(element)];

          if (nextIndicator) {
            nextIndicator.classList.add(CLASS_NAME_ACTIVE$1);
          }
        }
      };

      _proto._updateInterval = function _updateInterval() {
        var element = this._activeElement || SelectorEngine.findOne(SELECTOR_ACTIVE_ITEM, this._element);

        if (!element) {
          return;
        }

        var elementInterval = Number.parseInt(element.getAttribute('data-bs-interval'), 10);

        if (elementInterval) {
          this._config.defaultInterval = this._config.defaultInterval || this._config.interval;
          this._config.interval = elementInterval;
        } else {
          this._config.interval = this._config.defaultInterval || this._config.interval;
        }
      };

      _proto._slide = function _slide(direction, element) {
        var _this5 = this;

        var activeElement = SelectorEngine.findOne(SELECTOR_ACTIVE_ITEM, this._element);

        var activeElementIndex = this._getItemIndex(activeElement);

        var nextElement = element || activeElement && this._getItemByDirection(direction, activeElement);

        var nextElementIndex = this._getItemIndex(nextElement);

        var isCycling = Boolean(this._interval);
        var directionalClassName;
        var orderClassName;
        var eventDirectionName;

        if (direction === DIRECTION_NEXT) {
          directionalClassName = CLASS_NAME_START;
          orderClassName = CLASS_NAME_NEXT;
          eventDirectionName = DIRECTION_LEFT;
        } else {
          directionalClassName = CLASS_NAME_END;
          orderClassName = CLASS_NAME_PREV;
          eventDirectionName = DIRECTION_RIGHT;
        }

        if (nextElement && nextElement.classList.contains(CLASS_NAME_ACTIVE$1)) {
          this._isSliding = false;
          return;
        }

        var slideEvent = this._triggerSlideEvent(nextElement, eventDirectionName);

        if (slideEvent.defaultPrevented) {
          return;
        }

        if (!activeElement || !nextElement) {
          // Some weirdness is happening, so we bail
          return;
        }

        this._isSliding = true;

        if (isCycling) {
          this.pause();
        }

        this._setActiveIndicatorElement(nextElement);

        this._activeElement = nextElement;

        if (this._element.classList.contains(CLASS_NAME_SLIDE)) {
          nextElement.classList.add(orderClassName);
          reflow(nextElement);
          activeElement.classList.add(directionalClassName);
          nextElement.classList.add(directionalClassName);
          var transitionDuration = getTransitionDurationFromElement(activeElement);
          EventHandler.one(activeElement, TRANSITION_END, function () {
            nextElement.classList.remove(directionalClassName, orderClassName);
            nextElement.classList.add(CLASS_NAME_ACTIVE$1);
            activeElement.classList.remove(CLASS_NAME_ACTIVE$1, orderClassName, directionalClassName);
            _this5._isSliding = false;
            setTimeout(function () {
              EventHandler.trigger(_this5._element, EVENT_SLID, {
                relatedTarget: nextElement,
                direction: eventDirectionName,
                from: activeElementIndex,
                to: nextElementIndex
              });
            }, 0);
          });
          emulateTransitionEnd(activeElement, transitionDuration);
        } else {
          activeElement.classList.remove(CLASS_NAME_ACTIVE$1);
          nextElement.classList.add(CLASS_NAME_ACTIVE$1);
          this._isSliding = false;
          EventHandler.trigger(this._element, EVENT_SLID, {
            relatedTarget: nextElement,
            direction: eventDirectionName,
            from: activeElementIndex,
            to: nextElementIndex
          });
        }

        if (isCycling) {
          this.cycle();
        }
      } // Static
      ;

      Carousel.carouselInterface = function carouselInterface(element, config) {
        var data = Data.getData(element, DATA_KEY$2);

        var _config = _extends({}, Default, Manipulator.getDataAttributes(element));

        if (typeof config === 'object') {
          _config = _extends({}, _config, config);
        }

        var action = typeof config === 'string' ? config : _config.slide;

        if (!data) {
          data = new Carousel(element, _config);
        }

        if (typeof config === 'number') {
          data.to(config);
        } else if (typeof action === 'string') {
          if (typeof data[action] === 'undefined') {
            throw new TypeError("No method named \"" + action + "\"");
          }

          data[action]();
        } else if (_config.interval && _config.ride) {
          data.pause();
          data.cycle();
        }
      };

      Carousel.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          Carousel.carouselInterface(this, config);
        });
      };

      Carousel.dataApiClickHandler = function dataApiClickHandler(event) {
        var target = getElementFromSelector(this);

        if (!target || !target.classList.contains(CLASS_NAME_CAROUSEL)) {
          return;
        }

        var config = _extends({}, Manipulator.getDataAttributes(target), Manipulator.getDataAttributes(this));

        var slideIndex = this.getAttribute('data-bs-slide-to');

        if (slideIndex) {
          config.interval = false;
        }

        Carousel.carouselInterface(target, config);

        if (slideIndex) {
          Data.getData(target, DATA_KEY$2).to(slideIndex);
        }

        event.preventDefault();
      };

      _createClass(Carousel, null, [{
        key: "Default",
        get: function get() {
          return Default;
        }
      }, {
        key: "DATA_KEY",
        get: function get() {
          return DATA_KEY$2;
        }
      }]);

      return Carousel;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * Data Api implementation
     * ------------------------------------------------------------------------
     */


    EventHandler.on(document, EVENT_CLICK_DATA_API$2, SELECTOR_DATA_SLIDE, Carousel.dataApiClickHandler);
    EventHandler.on(window, EVENT_LOAD_DATA_API, function () {
      var carousels = SelectorEngine.find(SELECTOR_DATA_RIDE);

      for (var i = 0, len = carousels.length; i < len; i++) {
        Carousel.carouselInterface(carousels[i], Data.getData(carousels[i], DATA_KEY$2));
      }
    });
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Carousel to jQuery only if jQuery is present
     */

    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$2];
        $.fn[NAME$2] = Carousel.jQueryInterface;
        $.fn[NAME$2].Constructor = Carousel;

        $.fn[NAME$2].noConflict = function () {
          $.fn[NAME$2] = JQUERY_NO_CONFLICT;
          return Carousel.jQueryInterface;
        };
      }
    });

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$3 = 'collapse';
    var DATA_KEY$3 = 'bs.collapse';
    var EVENT_KEY$3 = "." + DATA_KEY$3;
    var DATA_API_KEY$3 = '.data-api';
    var Default$1 = {
      toggle: true,
      parent: ''
    };
    var DefaultType$1 = {
      toggle: 'boolean',
      parent: '(string|element)'
    };
    var EVENT_SHOW = "show" + EVENT_KEY$3;
    var EVENT_SHOWN = "shown" + EVENT_KEY$3;
    var EVENT_HIDE = "hide" + EVENT_KEY$3;
    var EVENT_HIDDEN = "hidden" + EVENT_KEY$3;
    var EVENT_CLICK_DATA_API$3 = "click" + EVENT_KEY$3 + DATA_API_KEY$3;
    var CLASS_NAME_SHOW = 'show';
    var CLASS_NAME_COLLAPSE = 'collapse';
    var CLASS_NAME_COLLAPSING = 'collapsing';
    var CLASS_NAME_COLLAPSED = 'collapsed';
    var WIDTH = 'width';
    var HEIGHT = 'height';
    var SELECTOR_ACTIVES = '.show, .collapsing';
    var SELECTOR_DATA_TOGGLE$1 = '[data-bs-toggle="collapse"]';
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Collapse = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(Collapse, _BaseComponent);

      function Collapse(element, config) {
        var _this;

        _this = _BaseComponent.call(this, element) || this;
        _this._isTransitioning = false;
        _this._config = _this._getConfig(config);
        _this._triggerArray = SelectorEngine.find(SELECTOR_DATA_TOGGLE$1 + "[href=\"#" + element.id + "\"]," + (SELECTOR_DATA_TOGGLE$1 + "[data-bs-target=\"#" + element.id + "\"]"));
        var toggleList = SelectorEngine.find(SELECTOR_DATA_TOGGLE$1);

        for (var i = 0, len = toggleList.length; i < len; i++) {
          var elem = toggleList[i];
          var selector = getSelectorFromElement(elem);
          var filterElement = SelectorEngine.find(selector).filter(function (foundElem) {
            return foundElem === element;
          });

          if (selector !== null && filterElement.length) {
            _this._selector = selector;

            _this._triggerArray.push(elem);
          }
        }

        _this._parent = _this._config.parent ? _this._getParent() : null;

        if (!_this._config.parent) {
          _this._addAriaAndCollapsedClass(_this._element, _this._triggerArray);
        }

        if (_this._config.toggle) {
          _this.toggle();
        }

        return _this;
      } // Getters


      var _proto = Collapse.prototype;

      // Public
      _proto.toggle = function toggle() {
        if (this._element.classList.contains(CLASS_NAME_SHOW)) {
          this.hide();
        } else {
          this.show();
        }
      };

      _proto.show = function show() {
        var _this2 = this;

        if (this._isTransitioning || this._element.classList.contains(CLASS_NAME_SHOW)) {
          return;
        }

        var actives;
        var activesData;

        if (this._parent) {
          actives = SelectorEngine.find(SELECTOR_ACTIVES, this._parent).filter(function (elem) {
            if (typeof _this2._config.parent === 'string') {
              return elem.getAttribute('data-bs-parent') === _this2._config.parent;
            }

            return elem.classList.contains(CLASS_NAME_COLLAPSE);
          });

          if (actives.length === 0) {
            actives = null;
          }
        }

        var container = SelectorEngine.findOne(this._selector);

        if (actives) {
          var tempActiveData = actives.find(function (elem) {
            return container !== elem;
          });
          activesData = tempActiveData ? Data.getData(tempActiveData, DATA_KEY$3) : null;

          if (activesData && activesData._isTransitioning) {
            return;
          }
        }

        var startEvent = EventHandler.trigger(this._element, EVENT_SHOW);

        if (startEvent.defaultPrevented) {
          return;
        }

        if (actives) {
          actives.forEach(function (elemActive) {
            if (container !== elemActive) {
              Collapse.collapseInterface(elemActive, 'hide');
            }

            if (!activesData) {
              Data.setData(elemActive, DATA_KEY$3, null);
            }
          });
        }

        var dimension = this._getDimension();

        this._element.classList.remove(CLASS_NAME_COLLAPSE);

        this._element.classList.add(CLASS_NAME_COLLAPSING);

        this._element.style[dimension] = 0;

        if (this._triggerArray.length) {
          this._triggerArray.forEach(function (element) {
            element.classList.remove(CLASS_NAME_COLLAPSED);
            element.setAttribute('aria-expanded', true);
          });
        }

        this.setTransitioning(true);

        var complete = function complete() {
          _this2._element.classList.remove(CLASS_NAME_COLLAPSING);

          _this2._element.classList.add(CLASS_NAME_COLLAPSE, CLASS_NAME_SHOW);

          _this2._element.style[dimension] = '';

          _this2.setTransitioning(false);

          EventHandler.trigger(_this2._element, EVENT_SHOWN);
        };

        var capitalizedDimension = dimension[0].toUpperCase() + dimension.slice(1);
        var scrollSize = "scroll" + capitalizedDimension;
        var transitionDuration = getTransitionDurationFromElement(this._element);
        EventHandler.one(this._element, TRANSITION_END, complete);
        emulateTransitionEnd(this._element, transitionDuration);
        this._element.style[dimension] = this._element[scrollSize] + "px";
      };

      _proto.hide = function hide() {
        var _this3 = this;

        if (this._isTransitioning || !this._element.classList.contains(CLASS_NAME_SHOW)) {
          return;
        }

        var startEvent = EventHandler.trigger(this._element, EVENT_HIDE);

        if (startEvent.defaultPrevented) {
          return;
        }

        var dimension = this._getDimension();

        this._element.style[dimension] = this._element.getBoundingClientRect()[dimension] + "px";
        reflow(this._element);

        this._element.classList.add(CLASS_NAME_COLLAPSING);

        this._element.classList.remove(CLASS_NAME_COLLAPSE, CLASS_NAME_SHOW);

        var triggerArrayLength = this._triggerArray.length;

        if (triggerArrayLength > 0) {
          for (var i = 0; i < triggerArrayLength; i++) {
            var trigger = this._triggerArray[i];
            var elem = getElementFromSelector(trigger);

            if (elem && !elem.classList.contains(CLASS_NAME_SHOW)) {
              trigger.classList.add(CLASS_NAME_COLLAPSED);
              trigger.setAttribute('aria-expanded', false);
            }
          }
        }

        this.setTransitioning(true);

        var complete = function complete() {
          _this3.setTransitioning(false);

          _this3._element.classList.remove(CLASS_NAME_COLLAPSING);

          _this3._element.classList.add(CLASS_NAME_COLLAPSE);

          EventHandler.trigger(_this3._element, EVENT_HIDDEN);
        };

        this._element.style[dimension] = '';
        var transitionDuration = getTransitionDurationFromElement(this._element);
        EventHandler.one(this._element, TRANSITION_END, complete);
        emulateTransitionEnd(this._element, transitionDuration);
      };

      _proto.setTransitioning = function setTransitioning(isTransitioning) {
        this._isTransitioning = isTransitioning;
      };

      _proto.dispose = function dispose() {
        _BaseComponent.prototype.dispose.call(this);

        this._config = null;
        this._parent = null;
        this._triggerArray = null;
        this._isTransitioning = null;
      } // Private
      ;

      _proto._getConfig = function _getConfig(config) {
        config = _extends({}, Default$1, config);
        config.toggle = Boolean(config.toggle); // Coerce string values

        typeCheckConfig(NAME$3, config, DefaultType$1);
        return config;
      };

      _proto._getDimension = function _getDimension() {
        return this._element.classList.contains(WIDTH) ? WIDTH : HEIGHT;
      };

      _proto._getParent = function _getParent() {
        var _this4 = this;

        var parent = this._config.parent;

        if (isElement$1(parent)) {
          // it's a jQuery object
          if (typeof parent.jquery !== 'undefined' || typeof parent[0] !== 'undefined') {
            parent = parent[0];
          }
        } else {
          parent = SelectorEngine.findOne(parent);
        }

        var selector = SELECTOR_DATA_TOGGLE$1 + "[data-bs-parent=\"" + parent + "\"]";
        SelectorEngine.find(selector, parent).forEach(function (element) {
          var selected = getElementFromSelector(element);

          _this4._addAriaAndCollapsedClass(selected, [element]);
        });
        return parent;
      };

      _proto._addAriaAndCollapsedClass = function _addAriaAndCollapsedClass(element, triggerArray) {
        if (!element || !triggerArray.length) {
          return;
        }

        var isOpen = element.classList.contains(CLASS_NAME_SHOW);
        triggerArray.forEach(function (elem) {
          if (isOpen) {
            elem.classList.remove(CLASS_NAME_COLLAPSED);
          } else {
            elem.classList.add(CLASS_NAME_COLLAPSED);
          }

          elem.setAttribute('aria-expanded', isOpen);
        });
      } // Static
      ;

      Collapse.collapseInterface = function collapseInterface(element, config) {
        var data = Data.getData(element, DATA_KEY$3);

        var _config = _extends({}, Default$1, Manipulator.getDataAttributes(element), typeof config === 'object' && config ? config : {});

        if (!data && _config.toggle && typeof config === 'string' && /show|hide/.test(config)) {
          _config.toggle = false;
        }

        if (!data) {
          data = new Collapse(element, _config);
        }

        if (typeof config === 'string') {
          if (typeof data[config] === 'undefined') {
            throw new TypeError("No method named \"" + config + "\"");
          }

          data[config]();
        }
      };

      Collapse.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          Collapse.collapseInterface(this, config);
        });
      };

      _createClass(Collapse, null, [{
        key: "Default",
        get: function get() {
          return Default$1;
        }
      }, {
        key: "DATA_KEY",
        get: function get() {
          return DATA_KEY$3;
        }
      }]);

      return Collapse;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * Data Api implementation
     * ------------------------------------------------------------------------
     */


    EventHandler.on(document, EVENT_CLICK_DATA_API$3, SELECTOR_DATA_TOGGLE$1, function (event) {
      // preventDefault only for <a> elements (which change the URL) not inside the collapsible element
      if (event.target.tagName === 'A') {
        event.preventDefault();
      }

      var triggerData = Manipulator.getDataAttributes(this);
      var selector = getSelectorFromElement(this);
      var selectorElements = SelectorEngine.find(selector);
      selectorElements.forEach(function (element) {
        var data = Data.getData(element, DATA_KEY$3);
        var config;

        if (data) {
          // update parent attribute
          if (data._parent === null && typeof triggerData.parent === 'string') {
            data._config.parent = triggerData.parent;
            data._parent = data._getParent();
          }

          config = 'toggle';
        } else {
          config = triggerData;
        }

        Collapse.collapseInterface(element, config);
      });
    });
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Collapse to jQuery only if jQuery is present
     */

    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$3];
        $.fn[NAME$3] = Collapse.jQueryInterface;
        $.fn[NAME$3].Constructor = Collapse;

        $.fn[NAME$3].noConflict = function () {
          $.fn[NAME$3] = JQUERY_NO_CONFLICT;
          return Collapse.jQueryInterface;
        };
      }
    });

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$4 = 'dropdown';
    var DATA_KEY$4 = 'bs.dropdown';
    var EVENT_KEY$4 = "." + DATA_KEY$4;
    var DATA_API_KEY$4 = '.data-api';
    var ESCAPE_KEY = 'Escape';
    var SPACE_KEY = 'Space';
    var TAB_KEY = 'Tab';
    var ARROW_UP_KEY = 'ArrowUp';
    var ARROW_DOWN_KEY = 'ArrowDown';
    var RIGHT_MOUSE_BUTTON = 2; // MouseEvent.button value for the secondary button, usually the right button

    var REGEXP_KEYDOWN = new RegExp(ARROW_UP_KEY + "|" + ARROW_DOWN_KEY + "|" + ESCAPE_KEY);
    var EVENT_HIDE$1 = "hide" + EVENT_KEY$4;
    var EVENT_HIDDEN$1 = "hidden" + EVENT_KEY$4;
    var EVENT_SHOW$1 = "show" + EVENT_KEY$4;
    var EVENT_SHOWN$1 = "shown" + EVENT_KEY$4;
    var EVENT_CLICK = "click" + EVENT_KEY$4;
    var EVENT_CLICK_DATA_API$4 = "click" + EVENT_KEY$4 + DATA_API_KEY$4;
    var EVENT_KEYDOWN_DATA_API = "keydown" + EVENT_KEY$4 + DATA_API_KEY$4;
    var EVENT_KEYUP_DATA_API = "keyup" + EVENT_KEY$4 + DATA_API_KEY$4;
    var CLASS_NAME_DISABLED = 'disabled';
    var CLASS_NAME_SHOW$1 = 'show';
    var CLASS_NAME_DROPUP = 'dropup';
    var CLASS_NAME_DROPEND = 'dropend';
    var CLASS_NAME_DROPSTART = 'dropstart';
    var CLASS_NAME_NAVBAR = 'navbar';
    var SELECTOR_DATA_TOGGLE$2 = '[data-bs-toggle="dropdown"]';
    var SELECTOR_FORM_CHILD = '.dropdown form';
    var SELECTOR_MENU = '.dropdown-menu';
    var SELECTOR_NAVBAR_NAV = '.navbar-nav';
    var SELECTOR_VISIBLE_ITEMS = '.dropdown-menu .dropdown-item:not(.disabled):not(:disabled)';
    var PLACEMENT_TOP = isRTL ? 'top-end' : 'top-start';
    var PLACEMENT_TOPEND = isRTL ? 'top-start' : 'top-end';
    var PLACEMENT_BOTTOM = isRTL ? 'bottom-end' : 'bottom-start';
    var PLACEMENT_BOTTOMEND = isRTL ? 'bottom-start' : 'bottom-end';
    var PLACEMENT_RIGHT = isRTL ? 'left-start' : 'right-start';
    var PLACEMENT_LEFT = isRTL ? 'right-start' : 'left-start';
    var Default$2 = {
      offset: 0,
      flip: true,
      boundary: 'clippingParents',
      reference: 'toggle',
      display: 'dynamic',
      popperConfig: null
    };
    var DefaultType$2 = {
      offset: '(number|string|function)',
      flip: 'boolean',
      boundary: '(string|element)',
      reference: '(string|element)',
      display: 'string',
      popperConfig: '(null|object)'
    };
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Dropdown$1 = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(Dropdown, _BaseComponent);

      function Dropdown(element, config) {
        var _this;

        _this = _BaseComponent.call(this, element) || this;
        _this._popper = null;
        _this._config = _this._getConfig(config);
        _this._menu = _this._getMenuElement();
        _this._inNavbar = _this._detectNavbar();

        _this._addEventListeners();

        return _this;
      } // Getters


      var _proto = Dropdown.prototype;

      // Public
      _proto.toggle = function toggle() {
        if (this._element.disabled || this._element.classList.contains(CLASS_NAME_DISABLED)) {
          return;
        }

        var isActive = this._element.classList.contains(CLASS_NAME_SHOW$1);

        Dropdown.clearMenus();

        if (isActive) {
          return;
        }

        this.show();
      };

      _proto.show = function show() {
        if (this._element.disabled || this._element.classList.contains(CLASS_NAME_DISABLED) || this._menu.classList.contains(CLASS_NAME_SHOW$1)) {
          return;
        }

        var parent = Dropdown.getParentFromElement(this._element);
        var relatedTarget = {
          relatedTarget: this._element
        };
        var showEvent = EventHandler.trigger(this._element, EVENT_SHOW$1, relatedTarget);

        if (showEvent.defaultPrevented) {
          return;
        } // Totally disable Popper for Dropdowns in Navbar


        if (!this._inNavbar) {
          if (typeof Popper === 'undefined') {
            throw new TypeError('Bootstrap\'s dropdowns require Popper (https://popper.js.org)');
          }

          var referenceElement = this._element;

          if (this._config.reference === 'parent') {
            referenceElement = parent;
          } else if (isElement$1(this._config.reference)) {
            referenceElement = this._config.reference; // Check if it's jQuery element

            if (typeof this._config.reference.jquery !== 'undefined') {
              referenceElement = this._config.reference[0];
            }
          }

          this._popper = createPopper$2(referenceElement, this._menu, this._getPopperConfig());
        } // If this is a touch-enabled device we add extra
        // empty mouseover listeners to the body's immediate children;
        // only needed because of broken event delegation on iOS
        // https://www.quirksmode.org/blog/archives/2014/02/mouse_event_bub.html


        if ('ontouchstart' in document.documentElement && !parent.closest(SELECTOR_NAVBAR_NAV)) {
          var _ref;

          (_ref = []).concat.apply(_ref, document.body.children).forEach(function (elem) {
            return EventHandler.on(elem, 'mouseover', null, noop());
          });
        }

        this._element.focus();

        this._element.setAttribute('aria-expanded', true);

        this._menu.classList.toggle(CLASS_NAME_SHOW$1);

        this._element.classList.toggle(CLASS_NAME_SHOW$1);

        EventHandler.trigger(parent, EVENT_SHOWN$1, relatedTarget);
      };

      _proto.hide = function hide() {
        if (this._element.disabled || this._element.classList.contains(CLASS_NAME_DISABLED) || !this._menu.classList.contains(CLASS_NAME_SHOW$1)) {
          return;
        }

        var parent = Dropdown.getParentFromElement(this._element);
        var relatedTarget = {
          relatedTarget: this._element
        };
        var hideEvent = EventHandler.trigger(parent, EVENT_HIDE$1, relatedTarget);

        if (hideEvent.defaultPrevented) {
          return;
        }

        if (this._popper) {
          this._popper.destroy();
        }

        this._menu.classList.toggle(CLASS_NAME_SHOW$1);

        this._element.classList.toggle(CLASS_NAME_SHOW$1);

        EventHandler.trigger(parent, EVENT_HIDDEN$1, relatedTarget);
      };

      _proto.dispose = function dispose() {
        _BaseComponent.prototype.dispose.call(this);

        EventHandler.off(this._element, EVENT_KEY$4);
        this._menu = null;

        if (this._popper) {
          this._popper.destroy();

          this._popper = null;
        }
      };

      _proto.update = function update() {
        this._inNavbar = this._detectNavbar();

        if (this._popper) {
          this._popper.update();
        }
      } // Private
      ;

      _proto._addEventListeners = function _addEventListeners() {
        var _this2 = this;

        EventHandler.on(this._element, EVENT_CLICK, function (event) {
          event.preventDefault();
          event.stopPropagation();

          _this2.toggle();
        });
      };

      _proto._getConfig = function _getConfig(config) {
        config = _extends({}, this.constructor.Default, Manipulator.getDataAttributes(this._element), config);
        typeCheckConfig(NAME$4, config, this.constructor.DefaultType);
        return config;
      };

      _proto._getMenuElement = function _getMenuElement() {
        return SelectorEngine.next(this._element, SELECTOR_MENU)[0];
      };

      _proto._getPlacement = function _getPlacement() {
        var parentDropdown = this._element.parentNode;

        if (parentDropdown.classList.contains(CLASS_NAME_DROPEND)) {
          return PLACEMENT_RIGHT;
        }

        if (parentDropdown.classList.contains(CLASS_NAME_DROPSTART)) {
          return PLACEMENT_LEFT;
        } // We need to trim the value because custom properties can also include spaces


        var isEnd = getComputedStyle(this._menu).getPropertyValue('--bs-position').trim() === 'end';

        if (parentDropdown.classList.contains(CLASS_NAME_DROPUP)) {
          return isEnd ? PLACEMENT_TOPEND : PLACEMENT_TOP;
        }

        return isEnd ? PLACEMENT_BOTTOMEND : PLACEMENT_BOTTOM;
      };

      _proto._detectNavbar = function _detectNavbar() {
        return this._element.closest("." + CLASS_NAME_NAVBAR) !== null;
      };

      _proto._getPopperConfig = function _getPopperConfig() {
        var popperConfig = {
          placement: this._getPlacement(),
          modifiers: [{
            name: 'preventOverflow',
            options: {
              altBoundary: this._config.flip,
              rootBoundary: this._config.boundary
            }
          }]
        }; // Disable Popper if we have a static display

        if (this._config.display === 'static') {
          popperConfig.modifiers = [{
            name: 'applyStyles',
            enabled: false
          }];
        }

        return _extends({}, popperConfig, this._config.popperConfig);
      } // Static
      ;

      Dropdown.dropdownInterface = function dropdownInterface(element, config) {
        var data = Data.getData(element, DATA_KEY$4);

        var _config = typeof config === 'object' ? config : null;

        if (!data) {
          data = new Dropdown(element, _config);
        }

        if (typeof config === 'string') {
          if (typeof data[config] === 'undefined') {
            throw new TypeError("No method named \"" + config + "\"");
          }

          data[config]();
        }
      };

      Dropdown.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          Dropdown.dropdownInterface(this, config);
        });
      };

      Dropdown.clearMenus = function clearMenus(event) {
        if (event && (event.button === RIGHT_MOUSE_BUTTON || event.type === 'keyup' && event.key !== TAB_KEY)) {
          return;
        }

        var toggles = SelectorEngine.find(SELECTOR_DATA_TOGGLE$2);

        for (var i = 0, len = toggles.length; i < len; i++) {
          var parent = Dropdown.getParentFromElement(toggles[i]);
          var context = Data.getData(toggles[i], DATA_KEY$4);
          var relatedTarget = {
            relatedTarget: toggles[i]
          };

          if (event && event.type === 'click') {
            relatedTarget.clickEvent = event;
          }

          if (!context) {
            continue;
          }

          var dropdownMenu = context._menu;

          if (!toggles[i].classList.contains(CLASS_NAME_SHOW$1)) {
            continue;
          }

          if (event && (event.type === 'click' && /input|textarea/i.test(event.target.tagName) || event.type === 'keyup' && event.key === TAB_KEY) && dropdownMenu.contains(event.target)) {
            continue;
          }

          var hideEvent = EventHandler.trigger(parent, EVENT_HIDE$1, relatedTarget);

          if (hideEvent.defaultPrevented) {
            continue;
          } // If this is a touch-enabled device we remove the extra
          // empty mouseover listeners we added for iOS support


          if ('ontouchstart' in document.documentElement) {
            var _ref2;

            (_ref2 = []).concat.apply(_ref2, document.body.children).forEach(function (elem) {
              return EventHandler.off(elem, 'mouseover', null, noop());
            });
          }

          toggles[i].setAttribute('aria-expanded', 'false');

          if (context._popper) {
            context._popper.destroy();
          }

          dropdownMenu.classList.remove(CLASS_NAME_SHOW$1);
          toggles[i].classList.remove(CLASS_NAME_SHOW$1);
          EventHandler.trigger(parent, EVENT_HIDDEN$1, relatedTarget);
        }
      };

      Dropdown.getParentFromElement = function getParentFromElement(element) {
        return getElementFromSelector(element) || element.parentNode;
      };

      Dropdown.dataApiKeydownHandler = function dataApiKeydownHandler(event) {
        // If not input/textarea:
        //  - And not a key in REGEXP_KEYDOWN => not a dropdown command
        // If input/textarea:
        //  - If space key => not a dropdown command
        //  - If key is other than escape
        //    - If key is not up or down => not a dropdown command
        //    - If trigger inside the menu => not a dropdown command
        if (/input|textarea/i.test(event.target.tagName) ? event.key === SPACE_KEY || event.key !== ESCAPE_KEY && (event.key !== ARROW_DOWN_KEY && event.key !== ARROW_UP_KEY || event.target.closest(SELECTOR_MENU)) : !REGEXP_KEYDOWN.test(event.key)) {
          return;
        }

        event.preventDefault();
        event.stopPropagation();

        if (this.disabled || this.classList.contains(CLASS_NAME_DISABLED)) {
          return;
        }

        var parent = Dropdown.getParentFromElement(this);
        var isActive = this.classList.contains(CLASS_NAME_SHOW$1);

        if (event.key === ESCAPE_KEY) {
          var button = this.matches(SELECTOR_DATA_TOGGLE$2) ? this : SelectorEngine.prev(this, SELECTOR_DATA_TOGGLE$2)[0];
          button.focus();
          Dropdown.clearMenus();
          return;
        }

        if (!isActive || event.key === SPACE_KEY) {
          Dropdown.clearMenus();
          return;
        }

        var items = SelectorEngine.find(SELECTOR_VISIBLE_ITEMS, parent).filter(isVisible);

        if (!items.length) {
          return;
        }

        var index = items.indexOf(event.target); // Up

        if (event.key === ARROW_UP_KEY && index > 0) {
          index--;
        } // Down


        if (event.key === ARROW_DOWN_KEY && index < items.length - 1) {
          index++;
        } // index is -1 if the first keydown is an ArrowUp


        index = index === -1 ? 0 : index;
        items[index].focus();
      };

      _createClass(Dropdown, null, [{
        key: "Default",
        get: function get() {
          return Default$2;
        }
      }, {
        key: "DefaultType",
        get: function get() {
          return DefaultType$2;
        }
      }, {
        key: "DATA_KEY",
        get: function get() {
          return DATA_KEY$4;
        }
      }]);

      return Dropdown;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * Data Api implementation
     * ------------------------------------------------------------------------
     */


    EventHandler.on(document, EVENT_KEYDOWN_DATA_API, SELECTOR_DATA_TOGGLE$2, Dropdown$1.dataApiKeydownHandler);
    EventHandler.on(document, EVENT_KEYDOWN_DATA_API, SELECTOR_MENU, Dropdown$1.dataApiKeydownHandler);
    EventHandler.on(document, EVENT_CLICK_DATA_API$4, Dropdown$1.clearMenus);
    EventHandler.on(document, EVENT_KEYUP_DATA_API, Dropdown$1.clearMenus);
    EventHandler.on(document, EVENT_CLICK_DATA_API$4, SELECTOR_DATA_TOGGLE$2, function (event) {
      event.preventDefault();
      event.stopPropagation();
      Dropdown$1.dropdownInterface(this, 'toggle');
    });
    EventHandler.on(document, EVENT_CLICK_DATA_API$4, SELECTOR_FORM_CHILD, function (e) {
      return e.stopPropagation();
    });
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Dropdown to jQuery only if jQuery is present
     */

    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$4];
        $.fn[NAME$4] = Dropdown$1.jQueryInterface;
        $.fn[NAME$4].Constructor = Dropdown$1;

        $.fn[NAME$4].noConflict = function () {
          $.fn[NAME$4] = JQUERY_NO_CONFLICT;
          return Dropdown$1.jQueryInterface;
        };
      }
    });

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$5 = 'modal';
    var DATA_KEY$5 = 'bs.modal';
    var EVENT_KEY$5 = "." + DATA_KEY$5;
    var DATA_API_KEY$5 = '.data-api';
    var ESCAPE_KEY$1 = 'Escape';
    var Default$3 = {
      backdrop: true,
      keyboard: true,
      focus: true
    };
    var DefaultType$3 = {
      backdrop: '(boolean|string)',
      keyboard: 'boolean',
      focus: 'boolean'
    };
    var EVENT_HIDE$2 = "hide" + EVENT_KEY$5;
    var EVENT_HIDE_PREVENTED = "hidePrevented" + EVENT_KEY$5;
    var EVENT_HIDDEN$2 = "hidden" + EVENT_KEY$5;
    var EVENT_SHOW$2 = "show" + EVENT_KEY$5;
    var EVENT_SHOWN$2 = "shown" + EVENT_KEY$5;
    var EVENT_FOCUSIN = "focusin" + EVENT_KEY$5;
    var EVENT_RESIZE = "resize" + EVENT_KEY$5;
    var EVENT_CLICK_DISMISS = "click.dismiss" + EVENT_KEY$5;
    var EVENT_KEYDOWN_DISMISS = "keydown.dismiss" + EVENT_KEY$5;
    var EVENT_MOUSEUP_DISMISS = "mouseup.dismiss" + EVENT_KEY$5;
    var EVENT_MOUSEDOWN_DISMISS = "mousedown.dismiss" + EVENT_KEY$5;
    var EVENT_CLICK_DATA_API$5 = "click" + EVENT_KEY$5 + DATA_API_KEY$5;
    var CLASS_NAME_SCROLLBAR_MEASURER = 'modal-scrollbar-measure';
    var CLASS_NAME_BACKDROP = 'modal-backdrop';
    var CLASS_NAME_OPEN = 'modal-open';
    var CLASS_NAME_FADE = 'fade';
    var CLASS_NAME_SHOW$2 = 'show';
    var CLASS_NAME_STATIC = 'modal-static';
    var SELECTOR_DIALOG = '.modal-dialog';
    var SELECTOR_MODAL_BODY = '.modal-body';
    var SELECTOR_DATA_TOGGLE$3 = '[data-bs-toggle="modal"]';
    var SELECTOR_DATA_DISMISS = '[data-bs-dismiss="modal"]';
    var SELECTOR_FIXED_CONTENT = '.fixed-top, .fixed-bottom, .is-fixed, .sticky-top';
    var SELECTOR_STICKY_CONTENT = '.sticky-top';
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Modal = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(Modal, _BaseComponent);

      function Modal(element, config) {
        var _this;

        _this = _BaseComponent.call(this, element) || this;
        _this._config = _this._getConfig(config);
        _this._dialog = SelectorEngine.findOne(SELECTOR_DIALOG, element);
        _this._backdrop = null;
        _this._isShown = false;
        _this._isBodyOverflowing = false;
        _this._ignoreBackdropClick = false;
        _this._isTransitioning = false;
        _this._scrollbarWidth = 0;
        return _this;
      } // Getters


      var _proto = Modal.prototype;

      // Public
      _proto.toggle = function toggle(relatedTarget) {
        return this._isShown ? this.hide() : this.show(relatedTarget);
      };

      _proto.show = function show(relatedTarget) {
        var _this2 = this;

        if (this._isShown || this._isTransitioning) {
          return;
        }

        if (this._element.classList.contains(CLASS_NAME_FADE)) {
          this._isTransitioning = true;
        }

        var showEvent = EventHandler.trigger(this._element, EVENT_SHOW$2, {
          relatedTarget: relatedTarget
        });

        if (this._isShown || showEvent.defaultPrevented) {
          return;
        }

        this._isShown = true;

        this._checkScrollbar();

        this._setScrollbar();

        this._adjustDialog();

        this._setEscapeEvent();

        this._setResizeEvent();

        EventHandler.on(this._element, EVENT_CLICK_DISMISS, SELECTOR_DATA_DISMISS, function (event) {
          return _this2.hide(event);
        });
        EventHandler.on(this._dialog, EVENT_MOUSEDOWN_DISMISS, function () {
          EventHandler.one(_this2._element, EVENT_MOUSEUP_DISMISS, function (event) {
            if (event.target === _this2._element) {
              _this2._ignoreBackdropClick = true;
            }
          });
        });

        this._showBackdrop(function () {
          return _this2._showElement(relatedTarget);
        });
      };

      _proto.hide = function hide(event) {
        var _this3 = this;

        if (event) {
          event.preventDefault();
        }

        if (!this._isShown || this._isTransitioning) {
          return;
        }

        var hideEvent = EventHandler.trigger(this._element, EVENT_HIDE$2);

        if (hideEvent.defaultPrevented) {
          return;
        }

        this._isShown = false;

        var transition = this._element.classList.contains(CLASS_NAME_FADE);

        if (transition) {
          this._isTransitioning = true;
        }

        this._setEscapeEvent();

        this._setResizeEvent();

        EventHandler.off(document, EVENT_FOCUSIN);

        this._element.classList.remove(CLASS_NAME_SHOW$2);

        EventHandler.off(this._element, EVENT_CLICK_DISMISS);
        EventHandler.off(this._dialog, EVENT_MOUSEDOWN_DISMISS);

        if (transition) {
          var transitionDuration = getTransitionDurationFromElement(this._element);
          EventHandler.one(this._element, TRANSITION_END, function (event) {
            return _this3._hideModal(event);
          });
          emulateTransitionEnd(this._element, transitionDuration);
        } else {
          this._hideModal();
        }
      };

      _proto.dispose = function dispose() {
        [window, this._element, this._dialog].forEach(function (htmlElement) {
          return EventHandler.off(htmlElement, EVENT_KEY$5);
        });

        _BaseComponent.prototype.dispose.call(this);
        /**
         * `document` has 2 events `EVENT_FOCUSIN` and `EVENT_CLICK_DATA_API`
         * Do not move `document` in `htmlElements` array
         * It will remove `EVENT_CLICK_DATA_API` event that should remain
         */


        EventHandler.off(document, EVENT_FOCUSIN);
        this._config = null;
        this._dialog = null;
        this._backdrop = null;
        this._isShown = null;
        this._isBodyOverflowing = null;
        this._ignoreBackdropClick = null;
        this._isTransitioning = null;
        this._scrollbarWidth = null;
      };

      _proto.handleUpdate = function handleUpdate() {
        this._adjustDialog();
      } // Private
      ;

      _proto._getConfig = function _getConfig(config) {
        config = _extends({}, Default$3, config);
        typeCheckConfig(NAME$5, config, DefaultType$3);
        return config;
      };

      _proto._showElement = function _showElement(relatedTarget) {
        var _this4 = this;

        var transition = this._element.classList.contains(CLASS_NAME_FADE);

        var modalBody = SelectorEngine.findOne(SELECTOR_MODAL_BODY, this._dialog);

        if (!this._element.parentNode || this._element.parentNode.nodeType !== Node.ELEMENT_NODE) {
          // Don't move modal's DOM position
          document.body.appendChild(this._element);
        }

        this._element.style.display = 'block';

        this._element.removeAttribute('aria-hidden');

        this._element.setAttribute('aria-modal', true);

        this._element.setAttribute('role', 'dialog');

        this._element.scrollTop = 0;

        if (modalBody) {
          modalBody.scrollTop = 0;
        }

        if (transition) {
          reflow(this._element);
        }

        this._element.classList.add(CLASS_NAME_SHOW$2);

        if (this._config.focus) {
          this._enforceFocus();
        }

        var transitionComplete = function transitionComplete() {
          if (_this4._config.focus) {
            _this4._element.focus();
          }

          _this4._isTransitioning = false;
          EventHandler.trigger(_this4._element, EVENT_SHOWN$2, {
            relatedTarget: relatedTarget
          });
        };

        if (transition) {
          var transitionDuration = getTransitionDurationFromElement(this._dialog);
          EventHandler.one(this._dialog, TRANSITION_END, transitionComplete);
          emulateTransitionEnd(this._dialog, transitionDuration);
        } else {
          transitionComplete();
        }
      };

      _proto._enforceFocus = function _enforceFocus() {
        var _this5 = this;

        EventHandler.off(document, EVENT_FOCUSIN); // guard against infinite focus loop

        EventHandler.on(document, EVENT_FOCUSIN, function (event) {
          if (document !== event.target && _this5._element !== event.target && !_this5._element.contains(event.target)) {
            _this5._element.focus();
          }
        });
      };

      _proto._setEscapeEvent = function _setEscapeEvent() {
        var _this6 = this;

        if (this._isShown) {
          EventHandler.on(this._element, EVENT_KEYDOWN_DISMISS, function (event) {
            if (_this6._config.keyboard && event.key === ESCAPE_KEY$1) {
              event.preventDefault();

              _this6.hide();
            } else if (!_this6._config.keyboard && event.key === ESCAPE_KEY$1) {
              _this6._triggerBackdropTransition();
            }
          });
        } else {
          EventHandler.off(this._element, EVENT_KEYDOWN_DISMISS);
        }
      };

      _proto._setResizeEvent = function _setResizeEvent() {
        var _this7 = this;

        if (this._isShown) {
          EventHandler.on(window, EVENT_RESIZE, function () {
            return _this7._adjustDialog();
          });
        } else {
          EventHandler.off(window, EVENT_RESIZE);
        }
      };

      _proto._hideModal = function _hideModal() {
        var _this8 = this;

        this._element.style.display = 'none';

        this._element.setAttribute('aria-hidden', true);

        this._element.removeAttribute('aria-modal');

        this._element.removeAttribute('role');

        this._isTransitioning = false;

        this._showBackdrop(function () {
          document.body.classList.remove(CLASS_NAME_OPEN);

          _this8._resetAdjustments();

          _this8._resetScrollbar();

          EventHandler.trigger(_this8._element, EVENT_HIDDEN$2);
        });
      };

      _proto._removeBackdrop = function _removeBackdrop() {
        this._backdrop.parentNode.removeChild(this._backdrop);

        this._backdrop = null;
      };

      _proto._showBackdrop = function _showBackdrop(callback) {
        var _this9 = this;

        var animate = this._element.classList.contains(CLASS_NAME_FADE) ? CLASS_NAME_FADE : '';

        if (this._isShown && this._config.backdrop) {
          this._backdrop = document.createElement('div');
          this._backdrop.className = CLASS_NAME_BACKDROP;

          if (animate) {
            this._backdrop.classList.add(animate);
          }

          document.body.appendChild(this._backdrop);
          EventHandler.on(this._element, EVENT_CLICK_DISMISS, function (event) {
            if (_this9._ignoreBackdropClick) {
              _this9._ignoreBackdropClick = false;
              return;
            }

            if (event.target !== event.currentTarget) {
              return;
            }

            if (_this9._config.backdrop === 'static') {
              _this9._triggerBackdropTransition();
            } else {
              _this9.hide();
            }
          });

          if (animate) {
            reflow(this._backdrop);
          }

          this._backdrop.classList.add(CLASS_NAME_SHOW$2);

          if (!animate) {
            callback();
            return;
          }

          var backdropTransitionDuration = getTransitionDurationFromElement(this._backdrop);
          EventHandler.one(this._backdrop, TRANSITION_END, callback);
          emulateTransitionEnd(this._backdrop, backdropTransitionDuration);
        } else if (!this._isShown && this._backdrop) {
          this._backdrop.classList.remove(CLASS_NAME_SHOW$2);

          var callbackRemove = function callbackRemove() {
            _this9._removeBackdrop();

            callback();
          };

          if (this._element.classList.contains(CLASS_NAME_FADE)) {
            var _backdropTransitionDuration = getTransitionDurationFromElement(this._backdrop);

            EventHandler.one(this._backdrop, TRANSITION_END, callbackRemove);
            emulateTransitionEnd(this._backdrop, _backdropTransitionDuration);
          } else {
            callbackRemove();
          }
        } else {
          callback();
        }
      };

      _proto._triggerBackdropTransition = function _triggerBackdropTransition() {
        var _this10 = this;

        var hideEvent = EventHandler.trigger(this._element, EVENT_HIDE_PREVENTED);

        if (hideEvent.defaultPrevented) {
          return;
        }

        var isModalOverflowing = this._element.scrollHeight > document.documentElement.clientHeight;

        if (!isModalOverflowing) {
          this._element.style.overflowY = 'hidden';
        }

        this._element.classList.add(CLASS_NAME_STATIC);

        var modalTransitionDuration = getTransitionDurationFromElement(this._dialog);
        EventHandler.off(this._element, TRANSITION_END);
        EventHandler.one(this._element, TRANSITION_END, function () {
          _this10._element.classList.remove(CLASS_NAME_STATIC);

          if (!isModalOverflowing) {
            EventHandler.one(_this10._element, TRANSITION_END, function () {
              _this10._element.style.overflowY = '';
            });
            emulateTransitionEnd(_this10._element, modalTransitionDuration);
          }
        });
        emulateTransitionEnd(this._element, modalTransitionDuration);

        this._element.focus();
      } // ----------------------------------------------------------------------
      // the following methods are used to handle overflowing modals
      // ----------------------------------------------------------------------
      ;

      _proto._adjustDialog = function _adjustDialog() {
        var isModalOverflowing = this._element.scrollHeight > document.documentElement.clientHeight;

        if (!this._isBodyOverflowing && isModalOverflowing && !isRTL || this._isBodyOverflowing && !isModalOverflowing && isRTL) {
          this._element.style.paddingLeft = this._scrollbarWidth + "px";
        }

        if (this._isBodyOverflowing && !isModalOverflowing && !isRTL || !this._isBodyOverflowing && isModalOverflowing && isRTL) {
          this._element.style.paddingRight = this._scrollbarWidth + "px";
        }
      };

      _proto._resetAdjustments = function _resetAdjustments() {
        this._element.style.paddingLeft = '';
        this._element.style.paddingRight = '';
      };

      _proto._checkScrollbar = function _checkScrollbar() {
        var rect = document.body.getBoundingClientRect();
        this._isBodyOverflowing = Math.round(rect.left + rect.right) < window.innerWidth;
        this._scrollbarWidth = this._getScrollbarWidth();
      };

      _proto._setScrollbar = function _setScrollbar() {
        var _this11 = this;

        if (this._isBodyOverflowing) {
          // Note: DOMNode.style.paddingRight returns the actual value or '' if not set
          //   while $(DOMNode).css('padding-right') returns the calculated value or 0 if not set
          // Adjust fixed content padding
          SelectorEngine.find(SELECTOR_FIXED_CONTENT).forEach(function (element) {
            var actualPadding = element.style.paddingRight;
            var calculatedPadding = window.getComputedStyle(element)['padding-right'];
            Manipulator.setDataAttribute(element, 'padding-right', actualPadding);
            element.style.paddingRight = Number.parseFloat(calculatedPadding) + _this11._scrollbarWidth + "px";
          }); // Adjust sticky content margin

          SelectorEngine.find(SELECTOR_STICKY_CONTENT).forEach(function (element) {
            var actualMargin = element.style.marginRight;
            var calculatedMargin = window.getComputedStyle(element)['margin-right'];
            Manipulator.setDataAttribute(element, 'margin-right', actualMargin);
            element.style.marginRight = Number.parseFloat(calculatedMargin) - _this11._scrollbarWidth + "px";
          }); // Adjust body padding

          var actualPadding = document.body.style.paddingRight;
          var calculatedPadding = window.getComputedStyle(document.body)['padding-right'];
          Manipulator.setDataAttribute(document.body, 'padding-right', actualPadding);
          document.body.style.paddingRight = Number.parseFloat(calculatedPadding) + this._scrollbarWidth + "px";
        }

        document.body.classList.add(CLASS_NAME_OPEN);
      };

      _proto._resetScrollbar = function _resetScrollbar() {
        // Restore fixed content padding
        SelectorEngine.find(SELECTOR_FIXED_CONTENT).forEach(function (element) {
          var padding = Manipulator.getDataAttribute(element, 'padding-right');

          if (typeof padding !== 'undefined') {
            Manipulator.removeDataAttribute(element, 'padding-right');
            element.style.paddingRight = padding;
          }
        }); // Restore sticky content and navbar-toggler margin

        SelectorEngine.find("" + SELECTOR_STICKY_CONTENT).forEach(function (element) {
          var margin = Manipulator.getDataAttribute(element, 'margin-right');

          if (typeof margin !== 'undefined') {
            Manipulator.removeDataAttribute(element, 'margin-right');
            element.style.marginRight = margin;
          }
        }); // Restore body padding

        var padding = Manipulator.getDataAttribute(document.body, 'padding-right');

        if (typeof padding === 'undefined') {
          document.body.style.paddingRight = '';
        } else {
          Manipulator.removeDataAttribute(document.body, 'padding-right');
          document.body.style.paddingRight = padding;
        }
      };

      _proto._getScrollbarWidth = function _getScrollbarWidth() {
        // thx d.walsh
        var scrollDiv = document.createElement('div');
        scrollDiv.className = CLASS_NAME_SCROLLBAR_MEASURER;
        document.body.appendChild(scrollDiv);
        var scrollbarWidth = scrollDiv.getBoundingClientRect().width - scrollDiv.clientWidth;
        document.body.removeChild(scrollDiv);
        return scrollbarWidth;
      } // Static
      ;

      Modal.jQueryInterface = function jQueryInterface(config, relatedTarget) {
        return this.each(function () {
          var data = Data.getData(this, DATA_KEY$5);

          var _config = _extends({}, Default$3, Manipulator.getDataAttributes(this), typeof config === 'object' && config ? config : {});

          if (!data) {
            data = new Modal(this, _config);
          }

          if (typeof config === 'string') {
            if (typeof data[config] === 'undefined') {
              throw new TypeError("No method named \"" + config + "\"");
            }

            data[config](relatedTarget);
          }
        });
      };

      _createClass(Modal, null, [{
        key: "Default",
        get: function get() {
          return Default$3;
        }
      }, {
        key: "DATA_KEY",
        get: function get() {
          return DATA_KEY$5;
        }
      }]);

      return Modal;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * Data Api implementation
     * ------------------------------------------------------------------------
     */


    EventHandler.on(document, EVENT_CLICK_DATA_API$5, SELECTOR_DATA_TOGGLE$3, function (event) {
      var _this12 = this;

      var target = getElementFromSelector(this);

      if (this.tagName === 'A' || this.tagName === 'AREA') {
        event.preventDefault();
      }

      EventHandler.one(target, EVENT_SHOW$2, function (showEvent) {
        if (showEvent.defaultPrevented) {
          // only register focus restorer if modal will actually get shown
          return;
        }

        EventHandler.one(target, EVENT_HIDDEN$2, function () {
          if (isVisible(_this12)) {
            _this12.focus();
          }
        });
      });
      var data = Data.getData(target, DATA_KEY$5);

      if (!data) {
        var config = _extends({}, Manipulator.getDataAttributes(target), Manipulator.getDataAttributes(this));

        data = new Modal(target, config);
      }

      data.show(this);
    });
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Modal to jQuery only if jQuery is present
     */

    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$5];
        $.fn[NAME$5] = Modal.jQueryInterface;
        $.fn[NAME$5].Constructor = Modal;

        $.fn[NAME$5].noConflict = function () {
          $.fn[NAME$5] = JQUERY_NO_CONFLICT;
          return Modal.jQueryInterface;
        };
      }
    });

    /**
     * --------------------------------------------------------------------------
     * Bootstrap (v5.0.0-beta1): util/sanitizer.js
     * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
     * --------------------------------------------------------------------------
     */
    var uriAttrs = new Set(['background', 'cite', 'href', 'itemtype', 'longdesc', 'poster', 'src', 'xlink:href']);
    var ARIA_ATTRIBUTE_PATTERN = /^aria-[\w-]*$/i;
    /**
     * A pattern that recognizes a commonly useful subset of URLs that are safe.
     *
     * Shoutout to Angular 7 https://github.com/angular/angular/blob/7.2.4/packages/core/src/sanitization/url_sanitizer.ts
     */

    var SAFE_URL_PATTERN = /^(?:(?:https?|mailto|ftp|tel|file):|[^#&/:?]*(?:[#/?]|$))/gi;
    /**
     * A pattern that matches safe data URLs. Only matches image, video and audio types.
     *
     * Shoutout to Angular 7 https://github.com/angular/angular/blob/7.2.4/packages/core/src/sanitization/url_sanitizer.ts
     */

    var DATA_URL_PATTERN = /^data:(?:image\/(?:bmp|gif|jpeg|jpg|png|tiff|webp)|video\/(?:mpeg|mp4|ogg|webm)|audio\/(?:mp3|oga|ogg|opus));base64,[\d+/a-z]+=*$/i;

    var allowedAttribute = function allowedAttribute(attr, allowedAttributeList) {
      var attrName = attr.nodeName.toLowerCase();

      if (allowedAttributeList.includes(attrName)) {
        if (uriAttrs.has(attrName)) {
          return Boolean(attr.nodeValue.match(SAFE_URL_PATTERN) || attr.nodeValue.match(DATA_URL_PATTERN));
        }

        return true;
      }

      var regExp = allowedAttributeList.filter(function (attrRegex) {
        return attrRegex instanceof RegExp;
      }); // Check if a regular expression validates the attribute.

      for (var i = 0, len = regExp.length; i < len; i++) {
        if (attrName.match(regExp[i])) {
          return true;
        }
      }

      return false;
    };

    var DefaultAllowlist = {
      // Global attributes allowed on any supplied element below.
      '*': ['class', 'dir', 'id', 'lang', 'role', ARIA_ATTRIBUTE_PATTERN],
      a: ['target', 'href', 'title', 'rel'],
      area: [],
      b: [],
      br: [],
      col: [],
      code: [],
      div: [],
      em: [],
      hr: [],
      h1: [],
      h2: [],
      h3: [],
      h4: [],
      h5: [],
      h6: [],
      i: [],
      img: ['src', 'srcset', 'alt', 'title', 'width', 'height'],
      li: [],
      ol: [],
      p: [],
      pre: [],
      s: [],
      small: [],
      span: [],
      sub: [],
      sup: [],
      strong: [],
      u: [],
      ul: []
    };
    function sanitizeHtml(unsafeHtml, allowList, sanitizeFn) {
      var _ref;

      if (!unsafeHtml.length) {
        return unsafeHtml;
      }

      if (sanitizeFn && typeof sanitizeFn === 'function') {
        return sanitizeFn(unsafeHtml);
      }

      var domParser = new window.DOMParser();
      var createdDocument = domParser.parseFromString(unsafeHtml, 'text/html');
      var allowlistKeys = Object.keys(allowList);

      var elements = (_ref = []).concat.apply(_ref, createdDocument.body.querySelectorAll('*'));

      var _loop = function _loop(i, len) {
        var _ref2;

        var el = elements[i];
        var elName = el.nodeName.toLowerCase();

        if (!allowlistKeys.includes(elName)) {
          el.parentNode.removeChild(el);
          return "continue";
        }

        var attributeList = (_ref2 = []).concat.apply(_ref2, el.attributes);

        var allowedAttributes = [].concat(allowList['*'] || [], allowList[elName] || []);
        attributeList.forEach(function (attr) {
          if (!allowedAttribute(attr, allowedAttributes)) {
            el.removeAttribute(attr.nodeName);
          }
        });
      };

      for (var i = 0, len = elements.length; i < len; i++) {
        var _ret = _loop(i);

        if (_ret === "continue") continue;
      }

      return createdDocument.body.innerHTML;
    }

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$6 = 'tooltip';
    var DATA_KEY$6 = 'bs.tooltip';
    var EVENT_KEY$6 = "." + DATA_KEY$6;
    var CLASS_PREFIX = 'bs-tooltip';
    var BSCLS_PREFIX_REGEX = new RegExp("(^|\\s)" + CLASS_PREFIX + "\\S+", 'g');
    var DISALLOWED_ATTRIBUTES = new Set(['sanitize', 'allowList', 'sanitizeFn']);
    var DefaultType$4 = {
      animation: 'boolean',
      template: 'string',
      title: '(string|element|function)',
      trigger: 'string',
      delay: '(number|object)',
      html: 'boolean',
      selector: '(string|boolean)',
      placement: '(string|function)',
      container: '(string|element|boolean)',
      fallbackPlacements: '(null|array)',
      boundary: '(string|element)',
      customClass: '(string|function)',
      sanitize: 'boolean',
      sanitizeFn: '(null|function)',
      allowList: 'object',
      popperConfig: '(null|object)'
    };
    var AttachmentMap = {
      AUTO: 'auto',
      TOP: 'top',
      RIGHT: isRTL ? 'left' : 'right',
      BOTTOM: 'bottom',
      LEFT: isRTL ? 'right' : 'left'
    };
    var Default$4 = {
      animation: true,
      template: '<div class="tooltip" role="tooltip">' + '<div class="tooltip-arrow"></div>' + '<div class="tooltip-inner"></div>' + '</div>',
      trigger: 'hover focus',
      title: '',
      delay: 0,
      html: false,
      selector: false,
      placement: 'top',
      container: false,
      fallbackPlacements: null,
      boundary: 'clippingParents',
      customClass: '',
      sanitize: true,
      sanitizeFn: null,
      allowList: DefaultAllowlist,
      popperConfig: null
    };
    var Event$1$1 = {
      HIDE: "hide" + EVENT_KEY$6,
      HIDDEN: "hidden" + EVENT_KEY$6,
      SHOW: "show" + EVENT_KEY$6,
      SHOWN: "shown" + EVENT_KEY$6,
      INSERTED: "inserted" + EVENT_KEY$6,
      CLICK: "click" + EVENT_KEY$6,
      FOCUSIN: "focusin" + EVENT_KEY$6,
      FOCUSOUT: "focusout" + EVENT_KEY$6,
      MOUSEENTER: "mouseenter" + EVENT_KEY$6,
      MOUSELEAVE: "mouseleave" + EVENT_KEY$6
    };
    var CLASS_NAME_FADE$1 = 'fade';
    var CLASS_NAME_MODAL = 'modal';
    var CLASS_NAME_SHOW$3 = 'show';
    var HOVER_STATE_SHOW = 'show';
    var HOVER_STATE_OUT = 'out';
    var SELECTOR_TOOLTIP_INNER = '.tooltip-inner';
    var TRIGGER_HOVER = 'hover';
    var TRIGGER_FOCUS = 'focus';
    var TRIGGER_CLICK = 'click';
    var TRIGGER_MANUAL = 'manual';
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Tooltip = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(Tooltip, _BaseComponent);

      function Tooltip(element, config) {
        var _this;

        if (typeof Popper === 'undefined') {
          throw new TypeError('Bootstrap\'s tooltips require Popper (https://popper.js.org)');
        }

        _this = _BaseComponent.call(this, element) || this; // private

        _this._isEnabled = true;
        _this._timeout = 0;
        _this._hoverState = '';
        _this._activeTrigger = {};
        _this._popper = null; // Protected

        _this.config = _this._getConfig(config);
        _this.tip = null;

        _this._setListeners();

        return _this;
      } // Getters


      var _proto = Tooltip.prototype;

      // Public
      _proto.enable = function enable() {
        this._isEnabled = true;
      };

      _proto.disable = function disable() {
        this._isEnabled = false;
      };

      _proto.toggleEnabled = function toggleEnabled() {
        this._isEnabled = !this._isEnabled;
      };

      _proto.toggle = function toggle(event) {
        if (!this._isEnabled) {
          return;
        }

        if (event) {
          var dataKey = this.constructor.DATA_KEY;
          var context = Data.getData(event.delegateTarget, dataKey);

          if (!context) {
            context = new this.constructor(event.delegateTarget, this._getDelegateConfig());
            Data.setData(event.delegateTarget, dataKey, context);
          }

          context._activeTrigger.click = !context._activeTrigger.click;

          if (context._isWithActiveTrigger()) {
            context._enter(null, context);
          } else {
            context._leave(null, context);
          }
        } else {
          if (this.getTipElement().classList.contains(CLASS_NAME_SHOW$3)) {
            this._leave(null, this);

            return;
          }

          this._enter(null, this);
        }
      };

      _proto.dispose = function dispose() {
        clearTimeout(this._timeout);
        EventHandler.off(this._element, this.constructor.EVENT_KEY);
        EventHandler.off(this._element.closest("." + CLASS_NAME_MODAL), 'hide.bs.modal', this._hideModalHandler);

        if (this.tip) {
          this.tip.parentNode.removeChild(this.tip);
        }

        this._isEnabled = null;
        this._timeout = null;
        this._hoverState = null;
        this._activeTrigger = null;

        if (this._popper) {
          this._popper.destroy();
        }

        this._popper = null;
        this.config = null;
        this.tip = null;

        _BaseComponent.prototype.dispose.call(this);
      };

      _proto.show = function show() {
        var _this2 = this;

        if (this._element.style.display === 'none') {
          throw new Error('Please use show on visible elements');
        }

        if (this.isWithContent() && this._isEnabled) {
          var showEvent = EventHandler.trigger(this._element, this.constructor.Event.SHOW);
          var shadowRoot = findShadowRoot(this._element);
          var isInTheDom = shadowRoot === null ? this._element.ownerDocument.documentElement.contains(this._element) : shadowRoot.contains(this._element);

          if (showEvent.defaultPrevented || !isInTheDom) {
            return;
          }

          var tip = this.getTipElement();
          var tipId = getUID(this.constructor.NAME);
          tip.setAttribute('id', tipId);

          this._element.setAttribute('aria-describedby', tipId);

          this.setContent();

          if (this.config.animation) {
            tip.classList.add(CLASS_NAME_FADE$1);
          }

          var placement = typeof this.config.placement === 'function' ? this.config.placement.call(this, tip, this._element) : this.config.placement;

          var attachment = this._getAttachment(placement);

          this._addAttachmentClass(attachment);

          var container = this._getContainer();

          Data.setData(tip, this.constructor.DATA_KEY, this);

          if (!this._element.ownerDocument.documentElement.contains(this.tip)) {
            container.appendChild(tip);
          }

          EventHandler.trigger(this._element, this.constructor.Event.INSERTED);
          this._popper = createPopper$2(this._element, tip, this._getPopperConfig(attachment));
          tip.classList.add(CLASS_NAME_SHOW$3);
          var customClass = typeof this.config.customClass === 'function' ? this.config.customClass() : this.config.customClass;

          if (customClass) {
            var _tip$classList;

            (_tip$classList = tip.classList).add.apply(_tip$classList, customClass.split(' '));
          } // If this is a touch-enabled device we add extra
          // empty mouseover listeners to the body's immediate children;
          // only needed because of broken event delegation on iOS
          // https://www.quirksmode.org/blog/archives/2014/02/mouse_event_bub.html


          if ('ontouchstart' in document.documentElement) {
            var _ref;

            (_ref = []).concat.apply(_ref, document.body.children).forEach(function (element) {
              EventHandler.on(element, 'mouseover', noop());
            });
          }

          var complete = function complete() {
            var prevHoverState = _this2._hoverState;
            _this2._hoverState = null;
            EventHandler.trigger(_this2._element, _this2.constructor.Event.SHOWN);

            if (prevHoverState === HOVER_STATE_OUT) {
              _this2._leave(null, _this2);
            }
          };

          if (this.tip.classList.contains(CLASS_NAME_FADE$1)) {
            var transitionDuration = getTransitionDurationFromElement(this.tip);
            EventHandler.one(this.tip, TRANSITION_END, complete);
            emulateTransitionEnd(this.tip, transitionDuration);
          } else {
            complete();
          }
        }
      };

      _proto.hide = function hide() {
        var _this3 = this;

        if (!this._popper) {
          return;
        }

        var tip = this.getTipElement();

        var complete = function complete() {
          if (_this3._hoverState !== HOVER_STATE_SHOW && tip.parentNode) {
            tip.parentNode.removeChild(tip);
          }

          _this3._cleanTipClass();

          _this3._element.removeAttribute('aria-describedby');

          EventHandler.trigger(_this3._element, _this3.constructor.Event.HIDDEN);

          if (_this3._popper) {
            _this3._popper.destroy();

            _this3._popper = null;
          }
        };

        var hideEvent = EventHandler.trigger(this._element, this.constructor.Event.HIDE);

        if (hideEvent.defaultPrevented) {
          return;
        }

        tip.classList.remove(CLASS_NAME_SHOW$3); // If this is a touch-enabled device we remove the extra
        // empty mouseover listeners we added for iOS support

        if ('ontouchstart' in document.documentElement) {
          var _ref2;

          (_ref2 = []).concat.apply(_ref2, document.body.children).forEach(function (element) {
            return EventHandler.off(element, 'mouseover', noop);
          });
        }

        this._activeTrigger[TRIGGER_CLICK] = false;
        this._activeTrigger[TRIGGER_FOCUS] = false;
        this._activeTrigger[TRIGGER_HOVER] = false;

        if (this.tip.classList.contains(CLASS_NAME_FADE$1)) {
          var transitionDuration = getTransitionDurationFromElement(tip);
          EventHandler.one(tip, TRANSITION_END, complete);
          emulateTransitionEnd(tip, transitionDuration);
        } else {
          complete();
        }

        this._hoverState = '';
      };

      _proto.update = function update() {
        if (this._popper !== null) {
          this._popper.update();
        }
      } // Protected
      ;

      _proto.isWithContent = function isWithContent() {
        return Boolean(this.getTitle());
      };

      _proto.getTipElement = function getTipElement() {
        if (this.tip) {
          return this.tip;
        }

        var element = document.createElement('div');
        element.innerHTML = this.config.template;
        this.tip = element.children[0];
        return this.tip;
      };

      _proto.setContent = function setContent() {
        var tip = this.getTipElement();
        this.setElementContent(SelectorEngine.findOne(SELECTOR_TOOLTIP_INNER, tip), this.getTitle());
        tip.classList.remove(CLASS_NAME_FADE$1, CLASS_NAME_SHOW$3);
      };

      _proto.setElementContent = function setElementContent(element, content) {
        if (element === null) {
          return;
        }

        if (typeof content === 'object' && isElement$1(content)) {
          if (content.jquery) {
            content = content[0];
          } // content is a DOM node or a jQuery


          if (this.config.html) {
            if (content.parentNode !== element) {
              element.innerHTML = '';
              element.appendChild(content);
            }
          } else {
            element.textContent = content.textContent;
          }

          return;
        }

        if (this.config.html) {
          if (this.config.sanitize) {
            content = sanitizeHtml(content, this.config.allowList, this.config.sanitizeFn);
          }

          element.innerHTML = content;
        } else {
          element.textContent = content;
        }
      };

      _proto.getTitle = function getTitle() {
        var title = this._element.getAttribute('data-bs-original-title');

        if (!title) {
          title = typeof this.config.title === 'function' ? this.config.title.call(this._element) : this.config.title;
        }

        return title;
      };

      _proto.updateAttachment = function updateAttachment(attachment) {
        if (attachment === 'right') {
          return 'end';
        }

        if (attachment === 'left') {
          return 'start';
        }

        return attachment;
      } // Private
      ;

      _proto._getPopperConfig = function _getPopperConfig(attachment) {
        var _this4 = this;

        var flipModifier = {
          name: 'flip',
          options: {
            altBoundary: true
          }
        };

        if (this.config.fallbackPlacements) {
          flipModifier.options.fallbackPlacements = this.config.fallbackPlacements;
        }

        var defaultBsConfig = {
          placement: attachment,
          modifiers: [flipModifier, {
            name: 'preventOverflow',
            options: {
              rootBoundary: this.config.boundary
            }
          }, {
            name: 'arrow',
            options: {
              element: "." + this.constructor.NAME + "-arrow"
            }
          }, {
            name: 'onChange',
            enabled: true,
            phase: 'afterWrite',
            fn: function fn(data) {
              return _this4._handlePopperPlacementChange(data);
            }
          }],
          onFirstUpdate: function onFirstUpdate(data) {
            if (data.options.placement !== data.placement) {
              _this4._handlePopperPlacementChange(data);
            }
          }
        };
        return _extends({}, defaultBsConfig, this.config.popperConfig);
      };

      _proto._addAttachmentClass = function _addAttachmentClass(attachment) {
        this.getTipElement().classList.add(CLASS_PREFIX + "-" + this.updateAttachment(attachment));
      };

      _proto._getContainer = function _getContainer() {
        if (this.config.container === false) {
          return document.body;
        }

        if (isElement$1(this.config.container)) {
          return this.config.container;
        }

        return SelectorEngine.findOne(this.config.container);
      };

      _proto._getAttachment = function _getAttachment(placement) {
        return AttachmentMap[placement.toUpperCase()];
      };

      _proto._setListeners = function _setListeners() {
        var _this5 = this;

        var triggers = this.config.trigger.split(' ');
        triggers.forEach(function (trigger) {
          if (trigger === 'click') {
            EventHandler.on(_this5._element, _this5.constructor.Event.CLICK, _this5.config.selector, function (event) {
              return _this5.toggle(event);
            });
          } else if (trigger !== TRIGGER_MANUAL) {
            var eventIn = trigger === TRIGGER_HOVER ? _this5.constructor.Event.MOUSEENTER : _this5.constructor.Event.FOCUSIN;
            var eventOut = trigger === TRIGGER_HOVER ? _this5.constructor.Event.MOUSELEAVE : _this5.constructor.Event.FOCUSOUT;
            EventHandler.on(_this5._element, eventIn, _this5.config.selector, function (event) {
              return _this5._enter(event);
            });
            EventHandler.on(_this5._element, eventOut, _this5.config.selector, function (event) {
              return _this5._leave(event);
            });
          }
        });

        this._hideModalHandler = function () {
          if (_this5._element) {
            _this5.hide();
          }
        };

        EventHandler.on(this._element.closest("." + CLASS_NAME_MODAL), 'hide.bs.modal', this._hideModalHandler);

        if (this.config.selector) {
          this.config = _extends({}, this.config, {
            trigger: 'manual',
            selector: ''
          });
        } else {
          this._fixTitle();
        }
      };

      _proto._fixTitle = function _fixTitle() {
        var title = this._element.getAttribute('title');

        var originalTitleType = typeof this._element.getAttribute('data-bs-original-title');

        if (title || originalTitleType !== 'string') {
          this._element.setAttribute('data-bs-original-title', title || '');

          if (title && !this._element.getAttribute('aria-label') && !this._element.textContent) {
            this._element.setAttribute('aria-label', title);
          }

          this._element.setAttribute('title', '');
        }
      };

      _proto._enter = function _enter(event, context) {
        var dataKey = this.constructor.DATA_KEY;
        context = context || Data.getData(event.delegateTarget, dataKey);

        if (!context) {
          context = new this.constructor(event.delegateTarget, this._getDelegateConfig());
          Data.setData(event.delegateTarget, dataKey, context);
        }

        if (event) {
          context._activeTrigger[event.type === 'focusin' ? TRIGGER_FOCUS : TRIGGER_HOVER] = true;
        }

        if (context.getTipElement().classList.contains(CLASS_NAME_SHOW$3) || context._hoverState === HOVER_STATE_SHOW) {
          context._hoverState = HOVER_STATE_SHOW;
          return;
        }

        clearTimeout(context._timeout);
        context._hoverState = HOVER_STATE_SHOW;

        if (!context.config.delay || !context.config.delay.show) {
          context.show();
          return;
        }

        context._timeout = setTimeout(function () {
          if (context._hoverState === HOVER_STATE_SHOW) {
            context.show();
          }
        }, context.config.delay.show);
      };

      _proto._leave = function _leave(event, context) {
        var dataKey = this.constructor.DATA_KEY;
        context = context || Data.getData(event.delegateTarget, dataKey);

        if (!context) {
          context = new this.constructor(event.delegateTarget, this._getDelegateConfig());
          Data.setData(event.delegateTarget, dataKey, context);
        }

        if (event) {
          context._activeTrigger[event.type === 'focusout' ? TRIGGER_FOCUS : TRIGGER_HOVER] = false;
        }

        if (context._isWithActiveTrigger()) {
          return;
        }

        clearTimeout(context._timeout);
        context._hoverState = HOVER_STATE_OUT;

        if (!context.config.delay || !context.config.delay.hide) {
          context.hide();
          return;
        }

        context._timeout = setTimeout(function () {
          if (context._hoverState === HOVER_STATE_OUT) {
            context.hide();
          }
        }, context.config.delay.hide);
      };

      _proto._isWithActiveTrigger = function _isWithActiveTrigger() {
        for (var trigger in this._activeTrigger) {
          if (this._activeTrigger[trigger]) {
            return true;
          }
        }

        return false;
      };

      _proto._getConfig = function _getConfig(config) {
        var dataAttributes = Manipulator.getDataAttributes(this._element);
        Object.keys(dataAttributes).forEach(function (dataAttr) {
          if (DISALLOWED_ATTRIBUTES.has(dataAttr)) {
            delete dataAttributes[dataAttr];
          }
        });

        if (config && typeof config.container === 'object' && config.container.jquery) {
          config.container = config.container[0];
        }

        config = _extends({}, this.constructor.Default, dataAttributes, typeof config === 'object' && config ? config : {});

        if (typeof config.delay === 'number') {
          config.delay = {
            show: config.delay,
            hide: config.delay
          };
        }

        if (typeof config.title === 'number') {
          config.title = config.title.toString();
        }

        if (typeof config.content === 'number') {
          config.content = config.content.toString();
        }

        typeCheckConfig(NAME$6, config, this.constructor.DefaultType);

        if (config.sanitize) {
          config.template = sanitizeHtml(config.template, config.allowList, config.sanitizeFn);
        }

        return config;
      };

      _proto._getDelegateConfig = function _getDelegateConfig() {
        var config = {};

        if (this.config) {
          for (var key in this.config) {
            if (this.constructor.Default[key] !== this.config[key]) {
              config[key] = this.config[key];
            }
          }
        }

        return config;
      };

      _proto._cleanTipClass = function _cleanTipClass() {
        var tip = this.getTipElement();
        var tabClass = tip.getAttribute('class').match(BSCLS_PREFIX_REGEX);

        if (tabClass !== null && tabClass.length > 0) {
          tabClass.map(function (token) {
            return token.trim();
          }).forEach(function (tClass) {
            return tip.classList.remove(tClass);
          });
        }
      };

      _proto._handlePopperPlacementChange = function _handlePopperPlacementChange(popperData) {
        var state = popperData.state;

        if (!state) {
          return;
        }

        this.tip = state.elements.popper;

        this._cleanTipClass();

        this._addAttachmentClass(this._getAttachment(state.placement));
      } // Static
      ;

      Tooltip.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          var data = Data.getData(this, DATA_KEY$6);

          var _config = typeof config === 'object' && config;

          if (!data && /dispose|hide/.test(config)) {
            return;
          }

          if (!data) {
            data = new Tooltip(this, _config);
          }

          if (typeof config === 'string') {
            if (typeof data[config] === 'undefined') {
              throw new TypeError("No method named \"" + config + "\"");
            }

            data[config]();
          }
        });
      };

      _createClass(Tooltip, null, [{
        key: "Default",
        get: function get() {
          return Default$4;
        }
      }, {
        key: "NAME",
        get: function get() {
          return NAME$6;
        }
      }, {
        key: "DATA_KEY",
        get: function get() {
          return DATA_KEY$6;
        }
      }, {
        key: "Event",
        get: function get() {
          return Event$1$1;
        }
      }, {
        key: "EVENT_KEY",
        get: function get() {
          return EVENT_KEY$6;
        }
      }, {
        key: "DefaultType",
        get: function get() {
          return DefaultType$4;
        }
      }]);

      return Tooltip;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Tooltip to jQuery only if jQuery is present
     */


    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$6];
        $.fn[NAME$6] = Tooltip.jQueryInterface;
        $.fn[NAME$6].Constructor = Tooltip;

        $.fn[NAME$6].noConflict = function () {
          $.fn[NAME$6] = JQUERY_NO_CONFLICT;
          return Tooltip.jQueryInterface;
        };
      }
    });

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$7 = 'popover';
    var DATA_KEY$7 = 'bs.popover';
    var EVENT_KEY$7 = "." + DATA_KEY$7;
    var CLASS_PREFIX$1 = 'bs-popover';
    var BSCLS_PREFIX_REGEX$1 = new RegExp("(^|\\s)" + CLASS_PREFIX$1 + "\\S+", 'g');

    var Default$5 = _extends({}, Tooltip.Default, {
      placement: 'right',
      trigger: 'click',
      content: '',
      template: '<div class="popover" role="tooltip">' + '<div class="popover-arrow"></div>' + '<h3 class="popover-header"></h3>' + '<div class="popover-body"></div>' + '</div>'
    });

    var DefaultType$5 = _extends({}, Tooltip.DefaultType, {
      content: '(string|element|function)'
    });

    var Event$2 = {
      HIDE: "hide" + EVENT_KEY$7,
      HIDDEN: "hidden" + EVENT_KEY$7,
      SHOW: "show" + EVENT_KEY$7,
      SHOWN: "shown" + EVENT_KEY$7,
      INSERTED: "inserted" + EVENT_KEY$7,
      CLICK: "click" + EVENT_KEY$7,
      FOCUSIN: "focusin" + EVENT_KEY$7,
      FOCUSOUT: "focusout" + EVENT_KEY$7,
      MOUSEENTER: "mouseenter" + EVENT_KEY$7,
      MOUSELEAVE: "mouseleave" + EVENT_KEY$7
    };
    var CLASS_NAME_FADE$2 = 'fade';
    var CLASS_NAME_SHOW$4 = 'show';
    var SELECTOR_TITLE = '.popover-header';
    var SELECTOR_CONTENT = '.popover-body';
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Popover = /*#__PURE__*/function (_Tooltip) {
      _inheritsLoose(Popover, _Tooltip);

      function Popover() {
        return _Tooltip.apply(this, arguments) || this;
      }

      var _proto = Popover.prototype;

      // Overrides
      _proto.isWithContent = function isWithContent() {
        return this.getTitle() || this._getContent();
      };

      _proto.setContent = function setContent() {
        var tip = this.getTipElement(); // we use append for html objects to maintain js events

        this.setElementContent(SelectorEngine.findOne(SELECTOR_TITLE, tip), this.getTitle());

        var content = this._getContent();

        if (typeof content === 'function') {
          content = content.call(this._element);
        }

        this.setElementContent(SelectorEngine.findOne(SELECTOR_CONTENT, tip), content);
        tip.classList.remove(CLASS_NAME_FADE$2, CLASS_NAME_SHOW$4);
      } // Private
      ;

      _proto._addAttachmentClass = function _addAttachmentClass(attachment) {
        this.getTipElement().classList.add(CLASS_PREFIX$1 + "-" + this.updateAttachment(attachment));
      };

      _proto._getContent = function _getContent() {
        return this._element.getAttribute('data-bs-content') || this.config.content;
      };

      _proto._cleanTipClass = function _cleanTipClass() {
        var tip = this.getTipElement();
        var tabClass = tip.getAttribute('class').match(BSCLS_PREFIX_REGEX$1);

        if (tabClass !== null && tabClass.length > 0) {
          tabClass.map(function (token) {
            return token.trim();
          }).forEach(function (tClass) {
            return tip.classList.remove(tClass);
          });
        }
      } // Static
      ;

      Popover.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          var data = Data.getData(this, DATA_KEY$7);

          var _config = typeof config === 'object' ? config : null;

          if (!data && /dispose|hide/.test(config)) {
            return;
          }

          if (!data) {
            data = new Popover(this, _config);
            Data.setData(this, DATA_KEY$7, data);
          }

          if (typeof config === 'string') {
            if (typeof data[config] === 'undefined') {
              throw new TypeError("No method named \"" + config + "\"");
            }

            data[config]();
          }
        });
      };

      _createClass(Popover, null, [{
        key: "Default",
        // Getters
        get: function get() {
          return Default$5;
        }
      }, {
        key: "NAME",
        get: function get() {
          return NAME$7;
        }
      }, {
        key: "DATA_KEY",
        get: function get() {
          return DATA_KEY$7;
        }
      }, {
        key: "Event",
        get: function get() {
          return Event$2;
        }
      }, {
        key: "EVENT_KEY",
        get: function get() {
          return EVENT_KEY$7;
        }
      }, {
        key: "DefaultType",
        get: function get() {
          return DefaultType$5;
        }
      }]);

      return Popover;
    }(Tooltip);
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Popover to jQuery only if jQuery is present
     */


    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$7];
        $.fn[NAME$7] = Popover.jQueryInterface;
        $.fn[NAME$7].Constructor = Popover;

        $.fn[NAME$7].noConflict = function () {
          $.fn[NAME$7] = JQUERY_NO_CONFLICT;
          return Popover.jQueryInterface;
        };
      }
    });

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$8 = 'scrollspy';
    var DATA_KEY$8 = 'bs.scrollspy';
    var EVENT_KEY$8 = "." + DATA_KEY$8;
    var DATA_API_KEY$6 = '.data-api';
    var Default$6 = {
      offset: 10,
      method: 'auto',
      target: ''
    };
    var DefaultType$6 = {
      offset: 'number',
      method: 'string',
      target: '(string|element)'
    };
    var EVENT_ACTIVATE = "activate" + EVENT_KEY$8;
    var EVENT_SCROLL = "scroll" + EVENT_KEY$8;
    var EVENT_LOAD_DATA_API$1 = "load" + EVENT_KEY$8 + DATA_API_KEY$6;
    var CLASS_NAME_DROPDOWN_ITEM = 'dropdown-item';
    var CLASS_NAME_ACTIVE$2 = 'active';
    var SELECTOR_DATA_SPY = '[data-bs-spy="scroll"]';
    var SELECTOR_NAV_LIST_GROUP = '.nav, .list-group';
    var SELECTOR_NAV_LINKS = '.nav-link';
    var SELECTOR_NAV_ITEMS = '.nav-item';
    var SELECTOR_LIST_ITEMS = '.list-group-item';
    var SELECTOR_DROPDOWN = '.dropdown';
    var SELECTOR_DROPDOWN_TOGGLE = '.dropdown-toggle';
    var METHOD_OFFSET = 'offset';
    var METHOD_POSITION = 'position';
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var ScrollSpy = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(ScrollSpy, _BaseComponent);

      function ScrollSpy(element, config) {
        var _this;

        _this = _BaseComponent.call(this, element) || this;
        _this._scrollElement = element.tagName === 'BODY' ? window : element;
        _this._config = _this._getConfig(config);
        _this._selector = _this._config.target + " " + SELECTOR_NAV_LINKS + ", " + _this._config.target + " " + SELECTOR_LIST_ITEMS + ", " + _this._config.target + " ." + CLASS_NAME_DROPDOWN_ITEM;
        _this._offsets = [];
        _this._targets = [];
        _this._activeTarget = null;
        _this._scrollHeight = 0;
        EventHandler.on(_this._scrollElement, EVENT_SCROLL, function (event) {
          return _this._process(event);
        });

        _this.refresh();

        _this._process();

        return _this;
      } // Getters


      var _proto = ScrollSpy.prototype;

      // Public
      _proto.refresh = function refresh() {
        var _this2 = this;

        var autoMethod = this._scrollElement === this._scrollElement.window ? METHOD_OFFSET : METHOD_POSITION;
        var offsetMethod = this._config.method === 'auto' ? autoMethod : this._config.method;
        var offsetBase = offsetMethod === METHOD_POSITION ? this._getScrollTop() : 0;
        this._offsets = [];
        this._targets = [];
        this._scrollHeight = this._getScrollHeight();
        var targets = SelectorEngine.find(this._selector);
        targets.map(function (element) {
          var targetSelector = getSelectorFromElement(element);
          var target = targetSelector ? SelectorEngine.findOne(targetSelector) : null;

          if (target) {
            var targetBCR = target.getBoundingClientRect();

            if (targetBCR.width || targetBCR.height) {
              return [Manipulator[offsetMethod](target).top + offsetBase, targetSelector];
            }
          }

          return null;
        }).filter(function (item) {
          return item;
        }).sort(function (a, b) {
          return a[0] - b[0];
        }).forEach(function (item) {
          _this2._offsets.push(item[0]);

          _this2._targets.push(item[1]);
        });
      };

      _proto.dispose = function dispose() {
        _BaseComponent.prototype.dispose.call(this);

        EventHandler.off(this._scrollElement, EVENT_KEY$8);
        this._scrollElement = null;
        this._config = null;
        this._selector = null;
        this._offsets = null;
        this._targets = null;
        this._activeTarget = null;
        this._scrollHeight = null;
      } // Private
      ;

      _proto._getConfig = function _getConfig(config) {
        config = _extends({}, Default$6, typeof config === 'object' && config ? config : {});

        if (typeof config.target !== 'string' && isElement$1(config.target)) {
          var id = config.target.id;

          if (!id) {
            id = getUID(NAME$8);
            config.target.id = id;
          }

          config.target = "#" + id;
        }

        typeCheckConfig(NAME$8, config, DefaultType$6);
        return config;
      };

      _proto._getScrollTop = function _getScrollTop() {
        return this._scrollElement === window ? this._scrollElement.pageYOffset : this._scrollElement.scrollTop;
      };

      _proto._getScrollHeight = function _getScrollHeight() {
        return this._scrollElement.scrollHeight || Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
      };

      _proto._getOffsetHeight = function _getOffsetHeight() {
        return this._scrollElement === window ? window.innerHeight : this._scrollElement.getBoundingClientRect().height;
      };

      _proto._process = function _process() {
        var scrollTop = this._getScrollTop() + this._config.offset;

        var scrollHeight = this._getScrollHeight();

        var maxScroll = this._config.offset + scrollHeight - this._getOffsetHeight();

        if (this._scrollHeight !== scrollHeight) {
          this.refresh();
        }

        if (scrollTop >= maxScroll) {
          var target = this._targets[this._targets.length - 1];

          if (this._activeTarget !== target) {
            this._activate(target);
          }

          return;
        }

        if (this._activeTarget && scrollTop < this._offsets[0] && this._offsets[0] > 0) {
          this._activeTarget = null;

          this._clear();

          return;
        }

        for (var i = this._offsets.length; i--;) {
          var isActiveTarget = this._activeTarget !== this._targets[i] && scrollTop >= this._offsets[i] && (typeof this._offsets[i + 1] === 'undefined' || scrollTop < this._offsets[i + 1]);

          if (isActiveTarget) {
            this._activate(this._targets[i]);
          }
        }
      };

      _proto._activate = function _activate(target) {
        this._activeTarget = target;

        this._clear();

        var queries = this._selector.split(',').map(function (selector) {
          return selector + "[data-bs-target=\"" + target + "\"]," + selector + "[href=\"" + target + "\"]";
        });

        var link = SelectorEngine.findOne(queries.join(','));

        if (link.classList.contains(CLASS_NAME_DROPDOWN_ITEM)) {
          SelectorEngine.findOne(SELECTOR_DROPDOWN_TOGGLE, link.closest(SELECTOR_DROPDOWN)).classList.add(CLASS_NAME_ACTIVE$2);
          link.classList.add(CLASS_NAME_ACTIVE$2);
        } else {
          // Set triggered link as active
          link.classList.add(CLASS_NAME_ACTIVE$2);
          SelectorEngine.parents(link, SELECTOR_NAV_LIST_GROUP).forEach(function (listGroup) {
            // Set triggered links parents as active
            // With both <ul> and <nav> markup a parent is the previous sibling of any nav ancestor
            SelectorEngine.prev(listGroup, SELECTOR_NAV_LINKS + ", " + SELECTOR_LIST_ITEMS).forEach(function (item) {
              return item.classList.add(CLASS_NAME_ACTIVE$2);
            }); // Handle special case when .nav-link is inside .nav-item

            SelectorEngine.prev(listGroup, SELECTOR_NAV_ITEMS).forEach(function (navItem) {
              SelectorEngine.children(navItem, SELECTOR_NAV_LINKS).forEach(function (item) {
                return item.classList.add(CLASS_NAME_ACTIVE$2);
              });
            });
          });
        }

        EventHandler.trigger(this._scrollElement, EVENT_ACTIVATE, {
          relatedTarget: target
        });
      };

      _proto._clear = function _clear() {
        SelectorEngine.find(this._selector).filter(function (node) {
          return node.classList.contains(CLASS_NAME_ACTIVE$2);
        }).forEach(function (node) {
          return node.classList.remove(CLASS_NAME_ACTIVE$2);
        });
      } // Static
      ;

      ScrollSpy.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          var data = Data.getData(this, DATA_KEY$8);

          var _config = typeof config === 'object' && config;

          if (!data) {
            data = new ScrollSpy(this, _config);
          }

          if (typeof config === 'string') {
            if (typeof data[config] === 'undefined') {
              throw new TypeError("No method named \"" + config + "\"");
            }

            data[config]();
          }
        });
      };

      _createClass(ScrollSpy, null, [{
        key: "Default",
        get: function get() {
          return Default$6;
        }
      }, {
        key: "DATA_KEY",
        get: function get() {
          return DATA_KEY$8;
        }
      }]);

      return ScrollSpy;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * Data Api implementation
     * ------------------------------------------------------------------------
     */


    EventHandler.on(window, EVENT_LOAD_DATA_API$1, function () {
      SelectorEngine.find(SELECTOR_DATA_SPY).forEach(function (spy) {
        return new ScrollSpy(spy, Manipulator.getDataAttributes(spy));
      });
    });
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .ScrollSpy to jQuery only if jQuery is present
     */

    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$8];
        $.fn[NAME$8] = ScrollSpy.jQueryInterface;
        $.fn[NAME$8].Constructor = ScrollSpy;

        $.fn[NAME$8].noConflict = function () {
          $.fn[NAME$8] = JQUERY_NO_CONFLICT;
          return ScrollSpy.jQueryInterface;
        };
      }
    });

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$9 = 'tab';
    var DATA_KEY$9 = 'bs.tab';
    var EVENT_KEY$9 = "." + DATA_KEY$9;
    var DATA_API_KEY$7 = '.data-api';
    var EVENT_HIDE$3 = "hide" + EVENT_KEY$9;
    var EVENT_HIDDEN$3 = "hidden" + EVENT_KEY$9;
    var EVENT_SHOW$3 = "show" + EVENT_KEY$9;
    var EVENT_SHOWN$3 = "shown" + EVENT_KEY$9;
    var EVENT_CLICK_DATA_API$6 = "click" + EVENT_KEY$9 + DATA_API_KEY$7;
    var CLASS_NAME_DROPDOWN_MENU = 'dropdown-menu';
    var CLASS_NAME_ACTIVE$3 = 'active';
    var CLASS_NAME_DISABLED$1 = 'disabled';
    var CLASS_NAME_FADE$3 = 'fade';
    var CLASS_NAME_SHOW$5 = 'show';
    var SELECTOR_DROPDOWN$1 = '.dropdown';
    var SELECTOR_NAV_LIST_GROUP$1 = '.nav, .list-group';
    var SELECTOR_ACTIVE$1 = '.active';
    var SELECTOR_ACTIVE_UL = ':scope > li > .active';
    var SELECTOR_DATA_TOGGLE$4 = '[data-bs-toggle="tab"], [data-bs-toggle="pill"], [data-bs-toggle="list"]';
    var SELECTOR_DROPDOWN_TOGGLE$1 = '.dropdown-toggle';
    var SELECTOR_DROPDOWN_ACTIVE_CHILD = ':scope > .dropdown-menu .active';
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Tab = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(Tab, _BaseComponent);

      function Tab() {
        return _BaseComponent.apply(this, arguments) || this;
      }

      var _proto = Tab.prototype;

      // Public
      _proto.show = function show() {
        var _this = this;

        if (this._element.parentNode && this._element.parentNode.nodeType === Node.ELEMENT_NODE && this._element.classList.contains(CLASS_NAME_ACTIVE$3) || this._element.classList.contains(CLASS_NAME_DISABLED$1)) {
          return;
        }

        var previous;
        var target = getElementFromSelector(this._element);

        var listElement = this._element.closest(SELECTOR_NAV_LIST_GROUP$1);

        if (listElement) {
          var itemSelector = listElement.nodeName === 'UL' || listElement.nodeName === 'OL' ? SELECTOR_ACTIVE_UL : SELECTOR_ACTIVE$1;
          previous = SelectorEngine.find(itemSelector, listElement);
          previous = previous[previous.length - 1];
        }

        var hideEvent = null;

        if (previous) {
          hideEvent = EventHandler.trigger(previous, EVENT_HIDE$3, {
            relatedTarget: this._element
          });
        }

        var showEvent = EventHandler.trigger(this._element, EVENT_SHOW$3, {
          relatedTarget: previous
        });

        if (showEvent.defaultPrevented || hideEvent !== null && hideEvent.defaultPrevented) {
          return;
        }

        this._activate(this._element, listElement);

        var complete = function complete() {
          EventHandler.trigger(previous, EVENT_HIDDEN$3, {
            relatedTarget: _this._element
          });
          EventHandler.trigger(_this._element, EVENT_SHOWN$3, {
            relatedTarget: previous
          });
        };

        if (target) {
          this._activate(target, target.parentNode, complete);
        } else {
          complete();
        }
      } // Private
      ;

      _proto._activate = function _activate(element, container, callback) {
        var _this2 = this;

        var activeElements = container && (container.nodeName === 'UL' || container.nodeName === 'OL') ? SelectorEngine.find(SELECTOR_ACTIVE_UL, container) : SelectorEngine.children(container, SELECTOR_ACTIVE$1);
        var active = activeElements[0];
        var isTransitioning = callback && active && active.classList.contains(CLASS_NAME_FADE$3);

        var complete = function complete() {
          return _this2._transitionComplete(element, active, callback);
        };

        if (active && isTransitioning) {
          var transitionDuration = getTransitionDurationFromElement(active);
          active.classList.remove(CLASS_NAME_SHOW$5);
          EventHandler.one(active, TRANSITION_END, complete);
          emulateTransitionEnd(active, transitionDuration);
        } else {
          complete();
        }
      };

      _proto._transitionComplete = function _transitionComplete(element, active, callback) {
        if (active) {
          active.classList.remove(CLASS_NAME_ACTIVE$3);
          var dropdownChild = SelectorEngine.findOne(SELECTOR_DROPDOWN_ACTIVE_CHILD, active.parentNode);

          if (dropdownChild) {
            dropdownChild.classList.remove(CLASS_NAME_ACTIVE$3);
          }

          if (active.getAttribute('role') === 'tab') {
            active.setAttribute('aria-selected', false);
          }
        }

        element.classList.add(CLASS_NAME_ACTIVE$3);

        if (element.getAttribute('role') === 'tab') {
          element.setAttribute('aria-selected', true);
        }

        reflow(element);

        if (element.classList.contains(CLASS_NAME_FADE$3)) {
          element.classList.add(CLASS_NAME_SHOW$5);
        }

        if (element.parentNode && element.parentNode.classList.contains(CLASS_NAME_DROPDOWN_MENU)) {
          var dropdownElement = element.closest(SELECTOR_DROPDOWN$1);

          if (dropdownElement) {
            SelectorEngine.find(SELECTOR_DROPDOWN_TOGGLE$1).forEach(function (dropdown) {
              return dropdown.classList.add(CLASS_NAME_ACTIVE$3);
            });
          }

          element.setAttribute('aria-expanded', true);
        }

        if (callback) {
          callback();
        }
      } // Static
      ;

      Tab.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          var data = Data.getData(this, DATA_KEY$9) || new Tab(this);

          if (typeof config === 'string') {
            if (typeof data[config] === 'undefined') {
              throw new TypeError("No method named \"" + config + "\"");
            }

            data[config]();
          }
        });
      };

      _createClass(Tab, null, [{
        key: "DATA_KEY",
        // Getters
        get: function get() {
          return DATA_KEY$9;
        }
      }]);

      return Tab;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * Data Api implementation
     * ------------------------------------------------------------------------
     */


    EventHandler.on(document, EVENT_CLICK_DATA_API$6, SELECTOR_DATA_TOGGLE$4, function (event) {
      event.preventDefault();
      var data = Data.getData(this, DATA_KEY$9) || new Tab(this);
      data.show();
    });
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Tab to jQuery only if jQuery is present
     */

    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$9];
        $.fn[NAME$9] = Tab.jQueryInterface;
        $.fn[NAME$9].Constructor = Tab;

        $.fn[NAME$9].noConflict = function () {
          $.fn[NAME$9] = JQUERY_NO_CONFLICT;
          return Tab.jQueryInterface;
        };
      }
    });

    /**
     * ------------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------------
     */

    var NAME$a = 'toast';
    var DATA_KEY$a = 'bs.toast';
    var EVENT_KEY$a = "." + DATA_KEY$a;
    var EVENT_CLICK_DISMISS$1 = "click.dismiss" + EVENT_KEY$a;
    var EVENT_HIDE$4 = "hide" + EVENT_KEY$a;
    var EVENT_HIDDEN$4 = "hidden" + EVENT_KEY$a;
    var EVENT_SHOW$4 = "show" + EVENT_KEY$a;
    var EVENT_SHOWN$4 = "shown" + EVENT_KEY$a;
    var CLASS_NAME_FADE$4 = 'fade';
    var CLASS_NAME_HIDE = 'hide';
    var CLASS_NAME_SHOW$6 = 'show';
    var CLASS_NAME_SHOWING = 'showing';
    var DefaultType$7 = {
      animation: 'boolean',
      autohide: 'boolean',
      delay: 'number'
    };
    var Default$7 = {
      animation: true,
      autohide: true,
      delay: 5000
    };
    var SELECTOR_DATA_DISMISS$1 = '[data-bs-dismiss="toast"]';
    /**
     * ------------------------------------------------------------------------
     * Class Definition
     * ------------------------------------------------------------------------
     */

    var Toast = /*#__PURE__*/function (_BaseComponent) {
      _inheritsLoose(Toast, _BaseComponent);

      function Toast(element, config) {
        var _this;

        _this = _BaseComponent.call(this, element) || this;
        _this._config = _this._getConfig(config);
        _this._timeout = null;

        _this._setListeners();

        return _this;
      } // Getters


      var _proto = Toast.prototype;

      // Public
      _proto.show = function show() {
        var _this2 = this;

        var showEvent = EventHandler.trigger(this._element, EVENT_SHOW$4);

        if (showEvent.defaultPrevented) {
          return;
        }

        this._clearTimeout();

        if (this._config.animation) {
          this._element.classList.add(CLASS_NAME_FADE$4);
        }

        var complete = function complete() {
          _this2._element.classList.remove(CLASS_NAME_SHOWING);

          _this2._element.classList.add(CLASS_NAME_SHOW$6);

          EventHandler.trigger(_this2._element, EVENT_SHOWN$4);

          if (_this2._config.autohide) {
            _this2._timeout = setTimeout(function () {
              _this2.hide();
            }, _this2._config.delay);
          }
        };

        this._element.classList.remove(CLASS_NAME_HIDE);

        reflow(this._element);

        this._element.classList.add(CLASS_NAME_SHOWING);

        if (this._config.animation) {
          var transitionDuration = getTransitionDurationFromElement(this._element);
          EventHandler.one(this._element, TRANSITION_END, complete);
          emulateTransitionEnd(this._element, transitionDuration);
        } else {
          complete();
        }
      };

      _proto.hide = function hide() {
        var _this3 = this;

        if (!this._element.classList.contains(CLASS_NAME_SHOW$6)) {
          return;
        }

        var hideEvent = EventHandler.trigger(this._element, EVENT_HIDE$4);

        if (hideEvent.defaultPrevented) {
          return;
        }

        var complete = function complete() {
          _this3._element.classList.add(CLASS_NAME_HIDE);

          EventHandler.trigger(_this3._element, EVENT_HIDDEN$4);
        };

        this._element.classList.remove(CLASS_NAME_SHOW$6);

        if (this._config.animation) {
          var transitionDuration = getTransitionDurationFromElement(this._element);
          EventHandler.one(this._element, TRANSITION_END, complete);
          emulateTransitionEnd(this._element, transitionDuration);
        } else {
          complete();
        }
      };

      _proto.dispose = function dispose() {
        this._clearTimeout();

        if (this._element.classList.contains(CLASS_NAME_SHOW$6)) {
          this._element.classList.remove(CLASS_NAME_SHOW$6);
        }

        EventHandler.off(this._element, EVENT_CLICK_DISMISS$1);

        _BaseComponent.prototype.dispose.call(this);

        this._config = null;
      } // Private
      ;

      _proto._getConfig = function _getConfig(config) {
        config = _extends({}, Default$7, Manipulator.getDataAttributes(this._element), typeof config === 'object' && config ? config : {});
        typeCheckConfig(NAME$a, config, this.constructor.DefaultType);
        return config;
      };

      _proto._setListeners = function _setListeners() {
        var _this4 = this;

        EventHandler.on(this._element, EVENT_CLICK_DISMISS$1, SELECTOR_DATA_DISMISS$1, function () {
          return _this4.hide();
        });
      };

      _proto._clearTimeout = function _clearTimeout() {
        clearTimeout(this._timeout);
        this._timeout = null;
      } // Static
      ;

      Toast.jQueryInterface = function jQueryInterface(config) {
        return this.each(function () {
          var data = Data.getData(this, DATA_KEY$a);

          var _config = typeof config === 'object' && config;

          if (!data) {
            data = new Toast(this, _config);
          }

          if (typeof config === 'string') {
            if (typeof data[config] === 'undefined') {
              throw new TypeError("No method named \"" + config + "\"");
            }

            data[config](this);
          }
        });
      };

      _createClass(Toast, null, [{
        key: "DefaultType",
        get: function get() {
          return DefaultType$7;
        }
      }, {
        key: "Default",
        get: function get() {
          return Default$7;
        }
      }, {
        key: "DATA_KEY",
        get: function get() {
          return DATA_KEY$a;
        }
      }]);

      return Toast;
    }(BaseComponent);
    /**
     * ------------------------------------------------------------------------
     * jQuery
     * ------------------------------------------------------------------------
     * add .Toast to jQuery only if jQuery is present
     */


    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[NAME$a];
        $.fn[NAME$a] = Toast.jQueryInterface;
        $.fn[NAME$a].Constructor = Toast;

        $.fn[NAME$a].noConflict = function () {
          $.fn[NAME$a] = JQUERY_NO_CONFLICT;
          return Toast.jQueryInterface;
        };
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
    // import {Modal} from "bootstrap/dist/js/bootstrap.bundle";
    class Popup extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            let options = {};
            this.modal = new Modal(this, options);
        }
        show() {
            console.log("show");
            this.modal.show();
        }
        hide() {
            console.log("hide");
            this.modal.hide();
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-popup") == null) {
            window.customElements.define("tobago-popup", Popup);
        }
    });
    class Collapse$1 {
        static findHidden(element) {
            const rootNode = element.getRootNode();
            return rootNode.getElementById(element.id + "::collapse");
        }
    }
    Collapse$1.execute = function (action, target) {
        const hidden = Collapse$1.findHidden(target);
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
                Collapse$1.execute(this.collapseAction, rootNode.getElementById(this.collapseTarget));
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
    // TODO: might be implemented with a web component
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
            if (clientId == null || clientId.lastIndexOf(":") === -1) {
                return null;
            }
            let id = clientId;
            while (true) {
                const sub = id.lastIndexOf("::");
                if (sub == -1) {
                    break;
                }
                if (sub + 1 == id.lastIndexOf(":")) {
                    id = id.substring(0, sub);
                }
                else {
                    break;
                }
            }
            return id.substring(0, id.lastIndexOf(":"));
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
                        id = Page.getNamingContainerId(id);
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
            const field = this.field;
            const locale = Page.page(this).locale;
            const i18n = this.i18n;
            i18n.titleFormat = "MM y"; // todo i18n
            i18n.format = this.pattern;
            Datepicker__default['default'].locales[locale] = i18n;
            const options = {
                buttonClass: "btn",
                orientation: "bottom top auto",
                autohide: true,
                language: locale,
                todayBtn: this.todayButton,
                todayBtnMode: 1
                // todo readonly
                // todo show week numbers
            };
            const datepicker = new Datepicker__default['default'](field, options);
            // XXX these listeners are needed as long as we have a solution for:
            // XXX https://github.com/mymth/vanillajs-datepicker/issues/13
            // XXX the 2nd point is missing the "normal" change event on the input element
            field.addEventListener("keyup", (event) => {
                // console.info("event -----> ", event.type);
                if (event.metaKey || event.key.length > 1 && event.key !== "Backspace" && event.key !== "Delete") {
                    return;
                }
                // back up user's input when user types printable character or backspace/delete
                const target = event.target;
                target._oldValue = target.value;
            });
            field.addEventListener("focus", (event) => {
                // console.info("event -----> ", event.type);
                this.lastValue = field.value;
            });
            field.addEventListener("blur", (event) => {
                // console.info("event -----> ", event.type);
                const target = event.target;
                // no-op when user goes to another window or the input field has no backed-up value
                if (document.hasFocus() && target._oldValue !== undefined) {
                    if (target._oldValue !== target.value) {
                        target.datepicker.setDate(target._oldValue || { clear: true });
                    }
                    delete target._oldValue;
                }
                if (this.lastValue !== field.value) {
                    field.dispatchEvent(new Event("change"));
                }
            });
            datepicker.element.addEventListener("changeDate", (event) => {
                // console.info("event -----> ", event.type);
                field.dispatchEvent(new Event("change"));
            });
            // simple solution for the picker: currently only open, not close is implemented
            (_a = this.querySelector(".tobago-date-picker")) === null || _a === void 0 ? void 0 : _a.addEventListener("click", (event) => {
                this.field.focus();
            });
        }
        get todayButton() {
            return this.hasAttribute("today-button");
        }
        set todayButton(todayButton) {
            if (todayButton) {
                this.setAttribute("today-button", "");
            }
            else {
                this.removeAttribute("today-button");
            }
        }
        get pattern() {
            const pattern = this.getAttribute("pattern");
            return DateUtils.convertPatternJava2Js(pattern); // todo: to the conversation in Java, not here
        }
        get i18n() {
            const i18n = this.getAttribute("i18n");
            return i18n ? JSON.parse(i18n) : undefined;
        }
        get field() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + "::field");
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
                const tobagoFocus = root.getElementById(Page.page(target).id + "::lastFocusId");
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
            if (this.isFixed) {
                // call now
                this.adjustMargin();
                // and after resize
                window.addEventListener("resize", this.adjustMargin.bind(this));
            }
        }
        adjustMargin(event) {
            const style = window.getComputedStyle(this);
            const maxFooterHeight = this.offsetHeight + Number.parseInt(style.marginTop) + Number.parseInt(style.marginBottom);
            if (maxFooterHeight !== this.lastMaxFooterHeight) {
                this.lastMaxFooterHeight = maxFooterHeight;
                this.closest("body").style.marginBottom = maxFooterHeight + "px";
            }
        }
        isFixed() {
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
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + "::field");
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
            for (const input of RegExpTest.selfOrElementsByClassName(element, "tobago-in")) { // todo only for data-regexp
                new RegExpTest(input);
            }
        }
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
    // import {Popover} from "bootstrap/dist/js/bootstrap.bundle";
    class TobagoPopover extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.popover = new Popover(this.trigger, {
                container: this.menuStore
            });
        }
        get trigger() {
            return this;
        }
        get menuStore() {
            const root = this.getRootNode();
            return root.querySelector(".tobago-page-menuStore");
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-popover") == null) {
            window.customElements.define("tobago-popover", TobagoPopover);
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
    class TobagoRange extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.popover = new Popover(this.range, {
                container: this.menuStore,
                content: this.content.bind(this),
                trigger: "input",
                placement: "auto",
                delay: {
                    show: 0,
                    hide: 500
                }
            });
            const range = this.range;
            const listener = this.updatePopover.bind(this);
            range.addEventListener("input", listener);
            range.addEventListener("focus", listener);
        }
        get range() {
            return this.querySelector("input[type=range]");
        }
        get menuStore() {
            const root = this.getRootNode();
            return root.querySelector(".tobago-page-menuStore");
        }
        get tooltipBody() {
            return this.querySelector(".popover-body");
        }
        content() {
            return this.range.value;
        }
        updatePopover() {
            // XXX why update doesn't show the new content?
            //  this.popover.update();
            this.popover.show();
        }
    }
    document.addEventListener("tobago.init", function (event) {
        if (window.customElements.get("tobago-range") == null) {
            window.customElements.define("tobago-range", TobagoRange);
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
            return rootNode.getElementById(this.id + "::field");
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
            return rootNode.getElementById(this.id + "::field");
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
            return rootNode.getElementById(this.id + "::field");
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
            const hidden = document.getElementById(this.id + "::widths");
            if (hidden) {
                return JSON.parse(hidden.getAttribute("value"));
            }
            else {
                return undefined;
            }
        }
        saveColumnWidths(widths) {
            const hidden = document.getElementById(this.id + "::widths");
            if (hidden) {
                hidden.setAttribute("value", JSON.stringify(widths));
            }
            else {
                console.warn("ignored, should not be called, id='" + this.id + "'");
            }
        }
        isColumnRendered() {
            const hidden = document.getElementById(this.id + "::rendered");
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
            return rootNode.getElementById(this.id + "::selected");
        }
        getHiddenScrollPosition() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + "::scrollPosition");
        }
        getHiddenExpanded() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + "::expanded");
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
        /**
         * Get the previous sibling element (without <style> elements).
         */
        // todo: calls of this method can probably be simplified
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
        get orientation() {
            return this.getAttribute("orientation");
        }
        set orientation(orientation) {
            this.setAttribute("orientation", orientation);
        }
        start(event) {
            event.preventDefault();
            const splitter = event.target;
            const previous = SplitLayout.previousElementSibling(splitter);
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
            const previous = SplitLayout.previousElementSibling(splitter);
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
            return this.splitter ? SplitLayout.previousElementSibling(this.splitter) : null;
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
        static leftOffset(element) {
            let left = 0;
            let currentElement = element;
            while (currentElement) {
                left += (currentElement.offsetLeft - currentElement.scrollLeft + currentElement.clientLeft);
                currentElement = currentElement.offsetParent;
            }
            return left;
        }
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
                    * (event.touches[0].pageX - Stars.leftOffset(slider));
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
    class Tab$1 extends HTMLElement {
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
        window.customElements.define("tobago-tab", Tab$1);
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
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + "::field");
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
                const rootNode = this.getRootNode();
                return rootNode.getElementById(option.id + "::parent");
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
            const rootNode = this.getRootNode();
            return rootNode.getElementById(this.id + "::selected");
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
