(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? factory(require('@popperjs/core')) :
  typeof define === 'function' && define.amd ? define(['@popperjs/core'], factory) :
  (global = typeof globalThis !== 'undefined' ? globalThis : global || self, factory(global.Popper));
}(this, (function (Popper) { 'use strict';

  function _interopNamespace(e) {
    if (e && e.__esModule) return e;
    var n = Object.create(null);
    if (e) {
      Object.keys(e).forEach(function (k) {
        if (k !== 'default') {
          var d = Object.getOwnPropertyDescriptor(e, k);
          Object.defineProperty(n, k, d.get ? d : {
            enumerable: true,
            get: function () {
              return e[k];
            }
          });
        }
      });
    }
    n['default'] = e;
    return Object.freeze(n);
  }

  var Popper__namespace = /*#__PURE__*/_interopNamespace(Popper);

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
          const delay = Number.parseFloat(style.transitionDelay);
          const duration = Number.parseFloat(style.transitionDuration);
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
              this.navbarContent.style.height = `${this.navbarContent.scrollHeight}px`;
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
              this.navbarContent.style.height = `${this.navbarContent.scrollHeight}px`;
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
  const TobagoDropdownEvent = {
      HIDE: "tobago.dropdown.hide",
      HIDDEN: "tobago.dropdown.hidden",
      SHOW: "tobago.dropdown.show",
      SHOWN: "tobago.dropdown.shown"
  };
  /**
   * The dropdown implementation of Bootstrap does not move the menu to the tobago-page-menuStore. This behavior is
   * implemented in this class.
   */
  class Dropdown extends HTMLElement {
      constructor() {
          super();
          if (!this.classList.contains("tobago-dropdown-submenu")) { // ignore submenus
              this.addEventListener("shown.bs.dropdown", this.openDropdown.bind(this));
              this.addEventListener("hidden.bs.dropdown", this.closeDropdown.bind(this));
          }
      }
      connectedCallback() {
      }
      openDropdown() {
          this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.SHOW));
          if (!this.inStickyHeader()) {
              this.menuStore.appendChild(this.dropdownMenu);
          }
          this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.SHOWN));
      }
      closeDropdown() {
          this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.HIDE));
          if (!this.inStickyHeader()) {
              this.appendChild(this.dropdownMenu);
          }
          this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.HIDDEN));
      }
      inStickyHeader() {
          return Boolean(this.closest("tobago-header.sticky-top"));
      }
      get dropdownMenu() {
          const root = this.getRootNode();
          return root.querySelector(".dropdown-menu[name='" + this.id + "']");
      }
      get menuStore() {
          const root = this.getRootNode();
          return root.querySelector(".tobago-page-menuStore");
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
        if (monthIndex < 0) {
          return NaN;
        }
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

    // collect parse function keys used in the format
    // iterate over parseFns' keys in order to keep the order of the keys.
    const partParserKeys = Object.keys(parseFns).reduce((keys, key) => {
      const token = parts.find(part => part[0] !== 'D' && part[0].toLowerCase() === key);
      if (token) {
        keys.push(key);
      }
      return keys;
    }, []);

    return knownFormats[format] = {
      parser(dateStr, locale) {
        const dateParts = dateStr.split(reNonDateParts).reduce((dtParts, part, index) => {
          if (part.length > 0 && parts[index]) {
            const token = parts[index][0];
            if (token === 'M') {
              dtParts.m = part;
            } else if (token !== 'D') {
              dtParts[token] = part;
            }
          }
          return dtParts;
        }, {});

        // iterate over partParserkeys so that the parsing is made in the oder
        // of year, month and day to prevent the day parser from correcting last
        // day of month wrongly
        return partParserKeys.reduce((origDate, key) => {
          const newDate = parseFns[key](origDate, dateParts[key], locale);
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
    pickLevel: 0,
    prevArrow: '«',
    showDaysOfWeek: true,
    showOnClick: true,
    showOnFocus: true,
    startView: 0,
    title: '',
    todayBtn: false,
    todayBtnMode: 0,
    todayHighlight: false,
    updateOnBlur: true,
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
  function validateViewId(value, origValue, max = 3) {
    const viewId = parseInt(value, 10);
    return viewId >= 0 && viewId <= max ? viewId : origValue;
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
      pickLevel,
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

    //*** pick level & view ***//
    let newPickLevel = pickLevel;
    if (inOpts.pickLevel !== undefined) {
      newPickLevel = validateViewId(inOpts.pickLevel, 2);
      delete inOpts.pickLevel;
    }
    if (newPickLevel !== pickLevel) {
      pickLevel = config.pickLevel = newPickLevel;
    }

    let newMaxView = maxView;
    if (inOpts.maxView !== undefined) {
      newMaxView = validateViewId(inOpts.maxView, maxView);
      delete inOpts.maxView;
    }
    // ensure max view >= pick level
    newMaxView = pickLevel > newMaxView ? pickLevel : newMaxView;
    if (newMaxView !== maxView) {
      maxView = config.maxView = newMaxView;
    }

    let newStartView = startView;
    if (inOpts.startView !== undefined) {
      newStartView = validateViewId(inOpts.startView, newStartView);
      delete inOpts.startView;
    }
    // ensure pick level <= start view <= max view
    if (newStartView < pickLevel) {
      newStartView = pickLevel;
    } else if (newStartView > maxView) {
      newStartView = maxView;
    }
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
        <button type="button" class="%buttonClass% prev-btn"></button>
        <button type="button" class="%buttonClass% view-switch"></button>
        <button type="button" class="%buttonClass% next-btn"></button>
      </div>
    </div>
    <div class="datepicker-main"></div>
    <div class="datepicker-footer">
      <div class="datepicker-controls">
        <button type="button" class="%buttonClass% today-btn"></button>
        <button type="button" class="%buttonClass% clear-btn"></button>
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
      if (options.pickLevel !== undefined) {
        this.isMinView = this.id === options.pickLevel;
      }
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
      this.focused = this.picker.viewDate;
    }

    // Apply update on the selected dates to view's settings
    updateSelection() {
      const {dates, rangepicker} = this.picker.datepicker;
      this.selected = dates;
      if (rangepicker) {
        this.range = rangepicker.dates;
      }
    }

     // Update the entire view UI
    render() {
      // update today marker on ever render
      this.today = this.todayHighlight ? today() : undefined;
      // refresh disabled dates on every render in order to clear the ones added
      // by beforeShow hook at previous render
      this.disabled = [...this.datesDisabled];

      const switchLabel = formatDate(this.focused, this.switchLabelFormat, this.locale);
      this.picker.setViewSwitchLabel(switchLabel);
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
        if (this.range) {
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

  function computeMonthRange(range, thisYear) {
    if (!range || !range[0] || !range[1]) {
      return;
    }

    const [[startY, startM], [endY, endM]] = range;
    if (startY > thisYear || endY < thisYear) {
      return;
    }
    return [
      startY === thisYear ? startM : -1,
      endY === thisYear ? endM : 12,
    ];
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
      this.focused = viewDate.getMonth();
    }

    // Update view's settings to reflect the selected dates
    updateSelection() {
      const {dates, rangepicker} = this.picker.datepicker;
      this.selected = dates.reduce((selected, timeValue) => {
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
      if (rangepicker && rangepicker.dates) {
        this.range = rangepicker.dates.map(timeValue => {
          const date = new Date(timeValue);
          return isNaN(date) ? undefined : [date.getFullYear(), date.getMonth()];
        });
      }
    }

    // Update the entire view UI
    render() {
      // refresh disabled months on every render in order to clear the ones added
      // by beforeShow hook at previous render
      this.disabled = [];

      this.picker.setViewSwitchLabel(this.year);
      this.picker.setPrevBtnDisabled(this.year <= this.minYear);
      this.picker.setNextBtnDisabled(this.year >= this.maxYear);

      const selected = this.selected[this.year] || [];
      const yrOutOfRange = this.year < this.minYear || this.year > this.maxYear;
      const isMinYear = this.year === this.minYear;
      const isMaxYear = this.year === this.maxYear;
      const range = computeMonthRange(this.range, this.year);

      Array.from(this.grid.children).forEach((el, index) => {
        const classList = el.classList;
        const date = dateValue(this.year, index, 1);

        el.className = `datepicker-cell ${this.cellClass}`;
        if (this.isMinView) {
          el.dataset.date = date;
        }
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
        if (range) {
          const [rangeStart, rangeEnd] = range;
          if (index > rangeStart && index < rangeEnd) {
            classList.add('range');
          }
          if (index === rangeStart) {
            classList.add('range-start');
          }
          if (index === rangeEnd) {
            classList.add('range-end');
          }
        }
        if (selected.includes(index)) {
          classList.add('selected');
        }
        if (index === this.focused) {
          classList.add('focused');
        }

        if (this.beforeShow) {
          this.performBeforeHook(el, index, date);
        }
      });
    }

    // Update the view UI by applying the changes of selected and focused items
    refresh() {
      const selected = this.selected[this.year] || [];
      const [rangeStart, rangeEnd] = computeMonthRange(this.range, this.year) || [];
      this.grid
        .querySelectorAll('.range, .range-start, .range-end, .selected, .focused')
        .forEach((el) => {
          el.classList.remove('range', 'range-start', 'range-end', 'selected', 'focused');
        });
      Array.from(this.grid.children).forEach((el, index) => {
        const classList = el.classList;
        if (index > rangeStart && index < rangeEnd) {
          classList.add('range');
        }
        if (index === rangeStart) {
          classList.add('range-start');
        }
        if (index === rangeEnd) {
          classList.add('range-end');
        }
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
      this.focused = startOfYearPeriod(viewDate, this.step);
    }

    // Update view's settings to reflect the selected dates
    updateSelection() {
      const {dates, rangepicker} = this.picker.datepicker;
      this.selected = dates.reduce((years, timeValue) => {
        return pushUnique(years, startOfYearPeriod(timeValue, this.step));
      }, []);
      if (rangepicker && rangepicker.dates) {
        this.range = rangepicker.dates.map(timeValue => {
          if (timeValue !== undefined) {
            return startOfYearPeriod(timeValue, this.step);
          }
        });
      }
    }

    // Update the entire view UI
    render() {
      // refresh disabled years on every render in order to clear the ones added
      // by beforeShow hook at previous render
      this.disabled = [];

      this.picker.setViewSwitchLabel(`${this.first}-${this.last}`);
      this.picker.setPrevBtnDisabled(this.first <= this.minYear);
      this.picker.setNextBtnDisabled(this.last >= this.maxYear);

      Array.from(this.grid.children).forEach((el, index) => {
        const classList = el.classList;
        const current = this.start + (index * this.step);
        const date = dateValue(current, 0, 1);

        el.className = `datepicker-cell ${this.cellClass}`;
        if (this.isMinView) {
          el.dataset.date = date;
        }
        el.textContent = el.dataset.year = current;

        if (index === 0) {
          classList.add('prev');
        } else if (index === 11) {
          classList.add('next');
        }
        if (current < this.minYear || current > this.maxYear) {
          classList.add('disabled');
        }
        if (this.range) {
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
          this.performBeforeHook(el, current, date);
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
        const current = Number(el.textContent);
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

  function unfocus(datepicker) {
    if (datepicker.config.updateOnBlur) {
      datepicker.update({autohide: true});
    } else {
      datepicker.refresh('input');
      datepicker.hide();
    }
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

    const {id, isMinView} = datepicker.picker.currentView;
    if (isMinView) {
      datepicker.setDate(Number(target.dataset.date));
    } else if (id === 1) {
      goToSelectedMonthOrYear(datepicker, Number(target.dataset.month));
    } else {
      goToSelectedMonthOrYear(datepicker, Number(target.dataset.year));
    }
  }

  function onClickPicker(datepicker) {
    if (!datepicker.inline && !datepicker.config.disableTouchKeyboard) {
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
        [element, 'click', onClickPicker.bind(null, datepicker), {capture: true}],
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

    detach() {
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
    render(quickRender = true) {
      const renderMethod = (quickRender && this._renderMethod) || 'render';
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
    const picker = datepicker.picker;
    const currentView = picker.currentView;
    const step = currentView.step || 1;
    let viewDate = picker.viewDate;
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
      picker.changeFocus(viewDate).render();
    }
  }

  function onKeydown(datepicker, ev) {
    if (ev.key === 'Tab') {
      unfocus(datepicker);
      return;
    }

    const picker = datepicker.picker;
    const {id, isMinView} = picker.currentView;
    if (!picker.active) {
      switch (ev.key) {
        case 'ArrowDown':
        case 'Escape':
          picker.show();
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
          picker.hide();
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
          picker.hide();
          break;
        case 'ArrowLeft':
          if (ev.ctrlKey || ev.metaKey) {
            goToPrevOrNext(datepicker, -1);
          } else if (ev.shiftKey) {
            datepicker.enterEditMode();
            return;
          } else {
            moveByArrowKey(datepicker, ev, -1, false);
          }
          break;
        case 'ArrowRight':
          if (ev.ctrlKey || ev.metaKey) {
            goToPrevOrNext(datepicker, 1);
          } else if (ev.shiftKey) {
            datepicker.enterEditMode();
            return;
          } else {
            moveByArrowKey(datepicker, ev, 1, false);
          }
          break;
        case 'ArrowUp':
          if (ev.ctrlKey || ev.metaKey) {
            switchView(datepicker);
          } else if (ev.shiftKey) {
            datepicker.enterEditMode();
            return;
          } else {
            moveByArrowKey(datepicker, ev, -1, true);
          }
          break;
        case 'ArrowDown':
          if (ev.shiftKey && !ev.ctrlKey && !ev.metaKey) {
            datepicker.enterEditMode();
            return;
          }
          moveByArrowKey(datepicker, ev, 1, true);
          break;
        case 'Enter':
          if (isMinView) {
            datepicker.setDate(picker.viewDate);
          } else {
            picker.changeView(id - 1).render();
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
    if (datepicker.picker.active || datepicker.config.showOnClick) {
      el._active = el === document.activeElement;
      el._clicking = setTimeout(() => {
        delete el._active;
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

    if (el._active) {
      datepicker.enterEditMode();
    }
    delete el._active;

    if (datepicker.config.showOnClick) {
      datepicker.show();
    }
  }

  function onPaste(datepicker, ev) {
    if (ev.clipboardData.types.includes('text/plain')) {
      datepicker.enterEditMode();
    }
  }

  // for the `document` to delegate the events from outside the picker/input field
  function onClickOutside(datepicker, ev) {
    const element = datepicker.element;
    if (element !== document.activeElement) {
      return;
    }
    const pickerElem = datepicker.picker.element;
    if (findElementInEventPath(ev, el => el === element || el === pickerElem)) {
      return;
    }
    unfocus(datepicker);
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
  function processInputDates(datepicker, inputDates, clear = false) {
    const {config, dates: origDates, rangepicker} = datepicker;
      if (inputDates.length === 0) {
      // empty input is considered valid unless origiDates is passed
      return clear ? [] : undefined;
    }

    const rangeEnd = rangepicker && datepicker === rangepicker.datepickers[1];
    let newDates = inputDates.reduce((dates, dt) => {
      let date = parseDate(dt, config.format, config.locale);
      if (date === undefined) {
        return dates;
      }
      if (config.pickLevel > 0) {
        // adjust to 1st of the month/Jan 1st of the year
        // or to the last day of the monh/Dec 31st of the year if the datepicker
        // is the range-end picker of a rangepicker
        const dt = new Date(date);
        if (config.pickLevel === 1) {
          date = rangeEnd
            ? dt.setMonth(dt.getMonth() + 1, 0)
            : dt.setDate(1);
        } else {
          date = rangeEnd
            ? dt.setFullYear(dt.getFullYear() + 1, 0, 0)
            : dt.setMonth(0, 1);
        }
      }
      if (
        isInRange(date, config.minDate, config.maxDate)
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
    if (config.multidate && !clear) {
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

  // refresh the UI elements
  // modes: 1: input only, 2, picker only, 3 both
  function refreshUI(datepicker, mode = 3, quickRender = true) {
    const {config, picker, inputField} = datepicker;
    if (mode & 2) {
      const newView = picker.active ? config.pickLevel : config.startView;
      picker.update().changeView(newView).render(quickRender);
    }
    if (mode & 1 && inputField) {
      inputField.value = stringifyDates(datepicker.dates, config);
    }
  }

  function setDate(datepicker, inputDates, options) {
    let {clear, render, autohide} = options;
    if (render === undefined) {
      render = true;
    }
    if (!render) {
      autohide = false;
    } else if (autohide === undefined) {
      autohide = datepicker.config.autohide;
    }

    const newDates = processInputDates(datepicker, inputDates, clear);
    if (!newDates) {
      return;
    }
    if (newDates.toString() !== datepicker.dates.toString()) {
      datepicker.dates = newDates;
      refreshUI(datepicker, render ? 3 : 1);
      triggerDatepickerEvent(datepicker, 'changeDate');
    } else {
      refreshUI(datepicker, 1);
    }
    if (autohide) {
      datepicker.hide();
    }
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
      if (rangepicker) {
        // check validiry
        const index = rangepicker.inputs.indexOf(inputField);
        const datepickers = rangepicker.datepickers;
        if (index < 0 || index > 1 || !Array.isArray(datepickers)) {
          throw Error('Invalid rangepicker object.');
        }
        // attach itaelf to the rangepicker here so that processInputDates() can
        // determine if this is the range-end picker of the rangepicker while
        // setting inital values when pickLevel > 0
        datepickers[index] = this;
        // add getter for rangepicker
        Object.defineProperty(this, 'rangepicker', {
          get() {
            return rangepicker;
          },
        });
      }

      // set initial value
      this.dates = processInputDates(this, initialDates) || [];
      if (inputField) {
        inputField.value = stringifyDates(this.dates, config);
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
     * Parse date string
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
     * @type {HTMLDivElement} - DOM object of picker element
     */
    get pickerElement() {
      return this.picker ? this.picker.element : undefined;
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

      refreshUI(this, 3);
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
     * Not available on inline picker
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
     * that all the given dates are invalid, which is distinguished from passing
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
      const opts = {};
      const lastArg = lastItemOf(args);
      if (
        typeof lastArg === 'object'
        && !Array.isArray(lastArg)
        && !(lastArg instanceof Date)
        && lastArg
      ) {
        Object.assign(opts, dates.pop());
      }

      const inputDates = Array.isArray(dates[0]) ? dates[0] : dates;
      setDate(this, inputDates, opts);
    }

    /**
     * Update the selected date(s) with input field's value
     * Not available on inline picker
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

      const opts = {clear: true, autohide: !!(options && options.autohide)};
      const inputDates = stringToArray(this.inputField.value, this.config.dateDelimiter);
      setDate(this, inputDates, opts);
    }

    /**
     * Refresh the picker element and the associated input field
     * @param {String} [target] - target item when refreshing one item only
     * 'picker' or 'input'
     * @param {Boolean} [forceRender] - whether to re-render the picker element
     * regardless of its state instead of optimized refresh
     */
    refresh(target = undefined, forceRender = false) {
      if (target && typeof target !== 'string') {
        forceRender = target;
        target = undefined;
      }

      let mode;
      if (target === 'picker') {
        mode = 2;
      } else if (target === 'input') {
        mode = 1;
      } else {
        mode = 3;
      }
      refreshUI(this, mode, !forceRender);
    }

    /**
     * Enter edit mode
     * Not available on inline picker or when the picker element is hidden
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
     * Not available on inline picker
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
          const wait = document.createElement("div");
          wait.classList.add("tobago-page-overlayCenter");
          this.overlay.append(wait);
          const image = document.createElement("i");
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
    * Bootstrap v5.0.0-beta2 (https://getbootstrap.com/)
    * Copyright 2011-2021 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
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

    _setPrototypeOf(subClass, superClass);
  }

  function _setPrototypeOf(o, p) {
    _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) {
      o.__proto__ = p;
      return o;
    };

    return _setPrototypeOf(o, p);
  }

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta2): util/index.js
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
      var hrefAttr = element.getAttribute('href'); // The only valid content that could double as a selector are IDs or classes,
      // so everything starting with `#` or `.`. If a "real" URL is used as the selector,
      // `document.querySelector` will rightfully complain it is invalid.
      // See https://github.com/twbs/bootstrap/issues/32273

      if (!hrefAttr || !hrefAttr.includes('#') && !hrefAttr.startsWith('.')) {
        return null;
      } // Just in case some CMS puts out a full URL with the anchor appended


      if (hrefAttr.includes('#') && !hrefAttr.startsWith('#')) {
        hrefAttr = '#' + hrefAttr.split('#')[1];
      }

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

  var isElement = function isElement(obj) {
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
      var valueType = value && isElement(value) ? 'element' : toType(value);

      if (!new RegExp(expectedTypes).test(valueType)) {
        throw new TypeError(componentName.toUpperCase() + ": " + ("Option \"" + property + "\" provided type \"" + valueType + "\" ") + ("but expected type \"" + expectedTypes + "\"."));
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

  var defineJQueryPlugin = function defineJQueryPlugin(name, plugin) {
    onDOMContentLoaded(function () {
      var $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        var JQUERY_NO_CONFLICT = $.fn[name];
        $.fn[name] = plugin.jQueryInterface;
        $.fn[name].Constructor = plugin;

        $.fn[name].noConflict = function () {
          $.fn[name] = JQUERY_NO_CONFLICT;
          return plugin.jQueryInterface;
        };
      }
    });
  };

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta2): dom/data.js
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
   * Bootstrap (v5.0.0-beta2): dom/event-handler.js
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
              // eslint-disable-next-line unicorn/consistent-destructuring
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

  var VERSION = '5.0.0-beta2';

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
  var CLASS_NAME_ALERT = 'alert';
  var CLASS_NAME_FADE = 'fade';
  var CLASS_NAME_SHOW = 'show';
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
      return getElementFromSelector(element) || element.closest("." + CLASS_NAME_ALERT);
    };

    _proto._triggerCloseEvent = function _triggerCloseEvent(element) {
      return EventHandler.trigger(element, EVENT_CLOSE);
    };

    _proto._removeElement = function _removeElement(element) {
      var _this = this;

      element.classList.remove(CLASS_NAME_SHOW);

      if (!element.classList.contains(CLASS_NAME_FADE)) {
        this._destroyElement(element);

        return;
      }

      var transitionDuration = getTransitionDurationFromElement(element);
      EventHandler.one(element, 'transitionend', function () {
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
      get: // Getters
      function get() {
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

  defineJQueryPlugin(NAME, Alert);

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
      get: // Getters
      function get() {
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

  defineJQueryPlugin(NAME$1, Button);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta2): dom/manipulator.js
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
   * Bootstrap (v5.0.0-beta2): dom/selector-engine.js
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

      return (_ref2 = []).concat.apply(_ref2, element.children).filter(function (child) {
        return child.matches(selector);
      });
    },
    parents: function parents(element, selector) {
      var parents = [];
      var ancestor = element.parentNode;

      while (ancestor && ancestor.nodeType === Node.ELEMENT_NODE && ancestor.nodeType !== NODE_TEXT) {
        if (ancestor.matches(selector)) {
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
        if (next.matches(selector)) {
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
  var SELECTOR_INDICATOR = '[data-bs-target]';
  var SELECTOR_DATA_SLIDE = '[data-bs-slide], [data-bs-slide-to]';
  var SELECTOR_DATA_RIDE = '[data-bs-ride="carousel"]';
  var POINTER_TYPE_TOUCH = 'touch';
  var POINTER_TYPE_PEN = 'pen';
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
        if (isRTL) {
          this.next();
        } else {
          this.prev();
        }
      } // swipe right


      if (direction < 0) {
        if (isRTL) {
          this.prev();
        } else {
          this.next();
        }
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
        if (_this4._pointerEvent && (event.pointerType === POINTER_TYPE_PEN || event.pointerType === POINTER_TYPE_TOUCH)) {
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
        if (_this4._pointerEvent && (event.pointerType === POINTER_TYPE_PEN || event.pointerType === POINTER_TYPE_TOUCH)) {
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

      if (event.key === ARROW_LEFT_KEY) {
        event.preventDefault();

        if (isRTL) {
          this.next();
        } else {
          this.prev();
        }
      } else if (event.key === ARROW_RIGHT_KEY) {
        event.preventDefault();

        if (isRTL) {
          this.prev();
        } else {
          this.next();
        }
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
        var activeIndicator = SelectorEngine.findOne(SELECTOR_ACTIVE, this._indicatorsElement);
        activeIndicator.classList.remove(CLASS_NAME_ACTIVE$1);
        activeIndicator.removeAttribute('aria-current');
        var indicators = SelectorEngine.find(SELECTOR_INDICATOR, this._indicatorsElement);

        for (var i = 0; i < indicators.length; i++) {
          if (Number.parseInt(indicators[i].getAttribute('data-bs-slide-to'), 10) === this._getItemIndex(element)) {
            indicators[i].classList.add(CLASS_NAME_ACTIVE$1);
            indicators[i].setAttribute('aria-current', 'true');
            break;
          }
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
      var directionalClassName = direction === DIRECTION_NEXT ? CLASS_NAME_START : CLASS_NAME_END;
      var orderClassName = direction === DIRECTION_NEXT ? CLASS_NAME_NEXT : CLASS_NAME_PREV;
      var eventDirectionName = direction === DIRECTION_NEXT ? DIRECTION_LEFT : DIRECTION_RIGHT;

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
        EventHandler.one(activeElement, 'transitionend', function () {
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

  defineJQueryPlugin(NAME$2, Carousel);

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
  var CLASS_NAME_SHOW$1 = 'show';
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
      if (this._element.classList.contains(CLASS_NAME_SHOW$1)) {
        this.hide();
      } else {
        this.show();
      }
    };

    _proto.show = function show() {
      var _this2 = this;

      if (this._isTransitioning || this._element.classList.contains(CLASS_NAME_SHOW$1)) {
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

        _this2._element.classList.add(CLASS_NAME_COLLAPSE, CLASS_NAME_SHOW$1);

        _this2._element.style[dimension] = '';

        _this2.setTransitioning(false);

        EventHandler.trigger(_this2._element, EVENT_SHOWN);
      };

      var capitalizedDimension = dimension[0].toUpperCase() + dimension.slice(1);
      var scrollSize = "scroll" + capitalizedDimension;
      var transitionDuration = getTransitionDurationFromElement(this._element);
      EventHandler.one(this._element, 'transitionend', complete);
      emulateTransitionEnd(this._element, transitionDuration);
      this._element.style[dimension] = this._element[scrollSize] + "px";
    };

    _proto.hide = function hide() {
      var _this3 = this;

      if (this._isTransitioning || !this._element.classList.contains(CLASS_NAME_SHOW$1)) {
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

      this._element.classList.remove(CLASS_NAME_COLLAPSE, CLASS_NAME_SHOW$1);

      var triggerArrayLength = this._triggerArray.length;

      if (triggerArrayLength > 0) {
        for (var i = 0; i < triggerArrayLength; i++) {
          var trigger = this._triggerArray[i];
          var elem = getElementFromSelector(trigger);

          if (elem && !elem.classList.contains(CLASS_NAME_SHOW$1)) {
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
      EventHandler.one(this._element, 'transitionend', complete);
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

      if (isElement(parent)) {
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

      var isOpen = element.classList.contains(CLASS_NAME_SHOW$1);
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
    if (event.target.tagName === 'A' || event.delegateTarget && event.delegateTarget.tagName === 'A') {
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

  defineJQueryPlugin(NAME$3, Collapse);

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
  var CLASS_NAME_SHOW$2 = 'show';
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
    offset: [0, 2],
    flip: true,
    boundary: 'clippingParents',
    reference: 'toggle',
    display: 'dynamic',
    popperConfig: null
  };
  var DefaultType$2 = {
    offset: '(array|string|function)',
    flip: 'boolean',
    boundary: '(string|element)',
    reference: '(string|element|object)',
    display: 'string',
    popperConfig: '(null|object|function)'
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

      var isActive = this._element.classList.contains(CLASS_NAME_SHOW$2);

      Dropdown.clearMenus();

      if (isActive) {
        return;
      }

      this.show();
    };

    _proto.show = function show() {
      if (this._element.disabled || this._element.classList.contains(CLASS_NAME_DISABLED) || this._menu.classList.contains(CLASS_NAME_SHOW$2)) {
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


      if (this._inNavbar) {
        Manipulator.setDataAttribute(this._menu, 'popper', 'none');
      } else {
        if (typeof Popper__namespace === 'undefined') {
          throw new TypeError('Bootstrap\'s dropdowns require Popper (https://popper.js.org)');
        }

        var referenceElement = this._element;

        if (this._config.reference === 'parent') {
          referenceElement = parent;
        } else if (isElement(this._config.reference)) {
          referenceElement = this._config.reference; // Check if it's jQuery element

          if (typeof this._config.reference.jquery !== 'undefined') {
            referenceElement = this._config.reference[0];
          }
        } else if (typeof this._config.reference === 'object') {
          referenceElement = this._config.reference;
        }

        var popperConfig = this._getPopperConfig();

        var isDisplayStatic = popperConfig.modifiers.find(function (modifier) {
          return modifier.name === 'applyStyles' && modifier.enabled === false;
        });
        this._popper = Popper.createPopper(referenceElement, this._menu, popperConfig);

        if (isDisplayStatic) {
          Manipulator.setDataAttribute(this._menu, 'popper', 'static');
        }
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

      this._menu.classList.toggle(CLASS_NAME_SHOW$2);

      this._element.classList.toggle(CLASS_NAME_SHOW$2);

      EventHandler.trigger(this._element, EVENT_SHOWN$1, relatedTarget);
    };

    _proto.hide = function hide() {
      if (this._element.disabled || this._element.classList.contains(CLASS_NAME_DISABLED) || !this._menu.classList.contains(CLASS_NAME_SHOW$2)) {
        return;
      }

      var relatedTarget = {
        relatedTarget: this._element
      };
      var hideEvent = EventHandler.trigger(this._element, EVENT_HIDE$1, relatedTarget);

      if (hideEvent.defaultPrevented) {
        return;
      }

      if (this._popper) {
        this._popper.destroy();
      }

      this._menu.classList.toggle(CLASS_NAME_SHOW$2);

      this._element.classList.toggle(CLASS_NAME_SHOW$2);

      Manipulator.removeDataAttribute(this._menu, 'popper');
      EventHandler.trigger(this._element, EVENT_HIDDEN$1, relatedTarget);
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

      if (typeof config.reference === 'object' && !isElement(config.reference) && typeof config.reference.getBoundingClientRect !== 'function') {
        // Popper virtual elements require a getBoundingClientRect method
        throw new TypeError(NAME$4.toUpperCase() + ": Option \"reference\" provided type \"object\" without a required \"getBoundingClientRect\" method.");
      }

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

    _proto._getOffset = function _getOffset() {
      var _this3 = this;

      var offset = this._config.offset;

      if (typeof offset === 'string') {
        return offset.split(',').map(function (val) {
          return Number.parseInt(val, 10);
        });
      }

      if (typeof offset === 'function') {
        return function (popperData) {
          return offset(popperData, _this3._element);
        };
      }

      return offset;
    };

    _proto._getPopperConfig = function _getPopperConfig() {
      var defaultBsPopperConfig = {
        placement: this._getPlacement(),
        modifiers: [{
          name: 'preventOverflow',
          options: {
            altBoundary: this._config.flip,
            boundary: this._config.boundary
          }
        }, {
          name: 'offset',
          options: {
            offset: this._getOffset()
          }
        }]
      }; // Disable Popper if we have a static display

      if (this._config.display === 'static') {
        defaultBsPopperConfig.modifiers = [{
          name: 'applyStyles',
          enabled: false
        }];
      }

      return _extends({}, defaultBsPopperConfig, typeof this._config.popperConfig === 'function' ? this._config.popperConfig(defaultBsPopperConfig) : this._config.popperConfig);
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

        if (!toggles[i].classList.contains(CLASS_NAME_SHOW$2)) {
          continue;
        }

        if (event && (event.type === 'click' && /input|textarea/i.test(event.target.tagName) || event.type === 'keyup' && event.key === TAB_KEY) && dropdownMenu.contains(event.target)) {
          continue;
        }

        var hideEvent = EventHandler.trigger(toggles[i], EVENT_HIDE$1, relatedTarget);

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

        dropdownMenu.classList.remove(CLASS_NAME_SHOW$2);
        toggles[i].classList.remove(CLASS_NAME_SHOW$2);
        Manipulator.removeDataAttribute(dropdownMenu, 'popper');
        EventHandler.trigger(toggles[i], EVENT_HIDDEN$1, relatedTarget);
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
      var isActive = this.classList.contains(CLASS_NAME_SHOW$2);

      if (event.key === ESCAPE_KEY) {
        var button = this.matches(SELECTOR_DATA_TOGGLE$2) ? this : SelectorEngine.prev(this, SELECTOR_DATA_TOGGLE$2)[0];
        button.focus();
        Dropdown.clearMenus();
        return;
      }

      if (!isActive && (event.key === ARROW_UP_KEY || event.key === ARROW_DOWN_KEY)) {
        var _button = this.matches(SELECTOR_DATA_TOGGLE$2) ? this : SelectorEngine.prev(this, SELECTOR_DATA_TOGGLE$2)[0];

        _button.click();

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

  defineJQueryPlugin(NAME$4, Dropdown$1);

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
  var CLASS_NAME_FADE$1 = 'fade';
  var CLASS_NAME_SHOW$3 = 'show';
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

      if (this._element.classList.contains(CLASS_NAME_FADE$1)) {
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

      var transition = this._element.classList.contains(CLASS_NAME_FADE$1);

      if (transition) {
        this._isTransitioning = true;
      }

      this._setEscapeEvent();

      this._setResizeEvent();

      EventHandler.off(document, EVENT_FOCUSIN);

      this._element.classList.remove(CLASS_NAME_SHOW$3);

      EventHandler.off(this._element, EVENT_CLICK_DISMISS);
      EventHandler.off(this._dialog, EVENT_MOUSEDOWN_DISMISS);

      if (transition) {
        var transitionDuration = getTransitionDurationFromElement(this._element);
        EventHandler.one(this._element, 'transitionend', function (event) {
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

      var transition = this._element.classList.contains(CLASS_NAME_FADE$1);

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

      this._element.classList.add(CLASS_NAME_SHOW$3);

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
        EventHandler.one(this._dialog, 'transitionend', transitionComplete);
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

      var animate = this._element.classList.contains(CLASS_NAME_FADE$1) ? CLASS_NAME_FADE$1 : '';

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

        this._backdrop.classList.add(CLASS_NAME_SHOW$3);

        if (!animate) {
          callback();
          return;
        }

        var backdropTransitionDuration = getTransitionDurationFromElement(this._backdrop);
        EventHandler.one(this._backdrop, 'transitionend', callback);
        emulateTransitionEnd(this._backdrop, backdropTransitionDuration);
      } else if (!this._isShown && this._backdrop) {
        this._backdrop.classList.remove(CLASS_NAME_SHOW$3);

        var callbackRemove = function callbackRemove() {
          _this9._removeBackdrop();

          callback();
        };

        if (this._element.classList.contains(CLASS_NAME_FADE$1)) {
          var _backdropTransitionDuration = getTransitionDurationFromElement(this._backdrop);

          EventHandler.one(this._backdrop, 'transitionend', callbackRemove);
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
      EventHandler.off(this._element, 'transitionend');
      EventHandler.one(this._element, 'transitionend', function () {
        _this10._element.classList.remove(CLASS_NAME_STATIC);

        if (!isModalOverflowing) {
          EventHandler.one(_this10._element, 'transitionend', function () {
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
        this._setElementAttributes(SELECTOR_FIXED_CONTENT, 'paddingRight', function (calculatedValue) {
          return calculatedValue + _this11._scrollbarWidth;
        });

        this._setElementAttributes(SELECTOR_STICKY_CONTENT, 'marginRight', function (calculatedValue) {
          return calculatedValue - _this11._scrollbarWidth;
        });

        this._setElementAttributes('body', 'paddingRight', function (calculatedValue) {
          return calculatedValue + _this11._scrollbarWidth;
        });
      }

      document.body.classList.add(CLASS_NAME_OPEN);
    };

    _proto._setElementAttributes = function _setElementAttributes(selector, styleProp, callback) {
      SelectorEngine.find(selector).forEach(function (element) {
        var actualValue = element.style[styleProp];
        var calculatedValue = window.getComputedStyle(element)[styleProp];
        Manipulator.setDataAttribute(element, styleProp, actualValue);
        element.style[styleProp] = callback(Number.parseFloat(calculatedValue)) + 'px';
      });
    };

    _proto._resetScrollbar = function _resetScrollbar() {
      this._resetElementAttributes(SELECTOR_FIXED_CONTENT, 'paddingRight');

      this._resetElementAttributes(SELECTOR_STICKY_CONTENT, 'marginRight');

      this._resetElementAttributes('body', 'paddingRight');
    };

    _proto._resetElementAttributes = function _resetElementAttributes(selector, styleProp) {
      SelectorEngine.find(selector).forEach(function (element) {
        var value = Manipulator.getDataAttribute(element, styleProp);

        if (typeof value === 'undefined' && element === document.body) {
          element.style[styleProp] = '';
        } else {
          Manipulator.removeDataAttribute(element, styleProp);
          element.style[styleProp] = value;
        }
      });
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

    data.toggle(this);
  });
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .Modal to jQuery only if jQuery is present
   */

  defineJQueryPlugin(NAME$5, Modal);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta2): util/sanitizer.js
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
        return Boolean(SAFE_URL_PATTERN.test(attr.nodeValue) || DATA_URL_PATTERN.test(attr.nodeValue));
      }

      return true;
    }

    var regExp = allowedAttributeList.filter(function (attrRegex) {
      return attrRegex instanceof RegExp;
    }); // Check if a regular expression validates the attribute.

    for (var i = 0, len = regExp.length; i < len; i++) {
      if (regExp[i].test(attrName)) {
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
    offset: '(array|string|function)',
    container: '(string|element|boolean)',
    fallbackPlacements: 'array',
    boundary: '(string|element)',
    customClass: '(string|function)',
    sanitize: 'boolean',
    sanitizeFn: '(null|function)',
    allowList: 'object',
    popperConfig: '(null|object|function)'
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
    offset: [0, 0],
    container: false,
    fallbackPlacements: ['top', 'right', 'bottom', 'left'],
    boundary: 'clippingParents',
    customClass: '',
    sanitize: true,
    sanitizeFn: null,
    allowList: DefaultAllowlist,
    popperConfig: null
  };
  var Event$1 = {
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
  var CLASS_NAME_FADE$2 = 'fade';
  var CLASS_NAME_MODAL = 'modal';
  var CLASS_NAME_SHOW$4 = 'show';
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

      if (typeof Popper__namespace === 'undefined') {
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
        var context = this._initializeOnDelegatedTarget(event);

        context._activeTrigger.click = !context._activeTrigger.click;

        if (context._isWithActiveTrigger()) {
          context._enter(null, context);
        } else {
          context._leave(null, context);
        }
      } else {
        if (this.getTipElement().classList.contains(CLASS_NAME_SHOW$4)) {
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

      if (this.tip && this.tip.parentNode) {
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

      if (!(this.isWithContent() && this._isEnabled)) {
        return;
      }

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
        tip.classList.add(CLASS_NAME_FADE$2);
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
      this._popper = Popper.createPopper(this._element, tip, this._getPopperConfig(attachment));
      tip.classList.add(CLASS_NAME_SHOW$4);
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

      if (this.tip.classList.contains(CLASS_NAME_FADE$2)) {
        var transitionDuration = getTransitionDurationFromElement(this.tip);
        EventHandler.one(this.tip, 'transitionend', complete);
        emulateTransitionEnd(this.tip, transitionDuration);
      } else {
        complete();
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

      tip.classList.remove(CLASS_NAME_SHOW$4); // If this is a touch-enabled device we remove the extra
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

      if (this.tip.classList.contains(CLASS_NAME_FADE$2)) {
        var transitionDuration = getTransitionDurationFromElement(tip);
        EventHandler.one(tip, 'transitionend', complete);
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
      tip.classList.remove(CLASS_NAME_FADE$2, CLASS_NAME_SHOW$4);
    };

    _proto.setElementContent = function setElementContent(element, content) {
      if (element === null) {
        return;
      }

      if (typeof content === 'object' && isElement(content)) {
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

    _proto._initializeOnDelegatedTarget = function _initializeOnDelegatedTarget(event, context) {
      var dataKey = this.constructor.DATA_KEY;
      context = context || Data.getData(event.delegateTarget, dataKey);

      if (!context) {
        context = new this.constructor(event.delegateTarget, this._getDelegateConfig());
        Data.setData(event.delegateTarget, dataKey, context);
      }

      return context;
    };

    _proto._getOffset = function _getOffset() {
      var _this4 = this;

      var offset = this.config.offset;

      if (typeof offset === 'string') {
        return offset.split(',').map(function (val) {
          return Number.parseInt(val, 10);
        });
      }

      if (typeof offset === 'function') {
        return function (popperData) {
          return offset(popperData, _this4._element);
        };
      }

      return offset;
    };

    _proto._getPopperConfig = function _getPopperConfig(attachment) {
      var _this5 = this;

      var defaultBsPopperConfig = {
        placement: attachment,
        modifiers: [{
          name: 'flip',
          options: {
            altBoundary: true,
            fallbackPlacements: this.config.fallbackPlacements
          }
        }, {
          name: 'offset',
          options: {
            offset: this._getOffset()
          }
        }, {
          name: 'preventOverflow',
          options: {
            boundary: this.config.boundary
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
            return _this5._handlePopperPlacementChange(data);
          }
        }],
        onFirstUpdate: function onFirstUpdate(data) {
          if (data.options.placement !== data.placement) {
            _this5._handlePopperPlacementChange(data);
          }
        }
      };
      return _extends({}, defaultBsPopperConfig, typeof this.config.popperConfig === 'function' ? this.config.popperConfig(defaultBsPopperConfig) : this.config.popperConfig);
    };

    _proto._addAttachmentClass = function _addAttachmentClass(attachment) {
      this.getTipElement().classList.add(CLASS_PREFIX + "-" + this.updateAttachment(attachment));
    };

    _proto._getContainer = function _getContainer() {
      if (this.config.container === false) {
        return document.body;
      }

      if (isElement(this.config.container)) {
        return this.config.container;
      }

      return SelectorEngine.findOne(this.config.container);
    };

    _proto._getAttachment = function _getAttachment(placement) {
      return AttachmentMap[placement.toUpperCase()];
    };

    _proto._setListeners = function _setListeners() {
      var _this6 = this;

      var triggers = this.config.trigger.split(' ');
      triggers.forEach(function (trigger) {
        if (trigger === 'click') {
          EventHandler.on(_this6._element, _this6.constructor.Event.CLICK, _this6.config.selector, function (event) {
            return _this6.toggle(event);
          });
        } else if (trigger !== TRIGGER_MANUAL) {
          var eventIn = trigger === TRIGGER_HOVER ? _this6.constructor.Event.MOUSEENTER : _this6.constructor.Event.FOCUSIN;
          var eventOut = trigger === TRIGGER_HOVER ? _this6.constructor.Event.MOUSELEAVE : _this6.constructor.Event.FOCUSOUT;
          EventHandler.on(_this6._element, eventIn, _this6.config.selector, function (event) {
            return _this6._enter(event);
          });
          EventHandler.on(_this6._element, eventOut, _this6.config.selector, function (event) {
            return _this6._leave(event);
          });
        }
      });

      this._hideModalHandler = function () {
        if (_this6._element) {
          _this6.hide();
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
      context = this._initializeOnDelegatedTarget(event, context);

      if (event) {
        context._activeTrigger[event.type === 'focusin' ? TRIGGER_FOCUS : TRIGGER_HOVER] = true;
      }

      if (context.getTipElement().classList.contains(CLASS_NAME_SHOW$4) || context._hoverState === HOVER_STATE_SHOW) {
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
      context = this._initializeOnDelegatedTarget(event, context);

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
        return Event$1;
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


  defineJQueryPlugin(NAME$6, Tooltip);

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
    offset: [0, 8],
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
  var CLASS_NAME_FADE$3 = 'fade';
  var CLASS_NAME_SHOW$5 = 'show';
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
      tip.classList.remove(CLASS_NAME_FADE$3, CLASS_NAME_SHOW$5);
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
      get: // Getters
      function get() {
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


  defineJQueryPlugin(NAME$7, Popover);

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
      EventHandler.on(_this._scrollElement, EVENT_SCROLL, function () {
        return _this._process();
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

      if (typeof config.target !== 'string' && isElement(config.target)) {
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

  defineJQueryPlugin(NAME$8, ScrollSpy);

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
  var CLASS_NAME_FADE$4 = 'fade';
  var CLASS_NAME_SHOW$6 = 'show';
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

      var hideEvent = previous ? EventHandler.trigger(previous, EVENT_HIDE$3, {
        relatedTarget: this._element
      }) : null;
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
      var isTransitioning = callback && active && active.classList.contains(CLASS_NAME_FADE$4);

      var complete = function complete() {
        return _this2._transitionComplete(element, active, callback);
      };

      if (active && isTransitioning) {
        var transitionDuration = getTransitionDurationFromElement(active);
        active.classList.remove(CLASS_NAME_SHOW$6);
        EventHandler.one(active, 'transitionend', complete);
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

      if (element.classList.contains(CLASS_NAME_FADE$4)) {
        element.classList.add(CLASS_NAME_SHOW$6);
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
      get: // Getters
      function get() {
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

  defineJQueryPlugin(NAME$9, Tab);

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
  var CLASS_NAME_FADE$5 = 'fade';
  var CLASS_NAME_HIDE = 'hide';
  var CLASS_NAME_SHOW$7 = 'show';
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
        this._element.classList.add(CLASS_NAME_FADE$5);
      }

      var complete = function complete() {
        _this2._element.classList.remove(CLASS_NAME_SHOWING);

        _this2._element.classList.add(CLASS_NAME_SHOW$7);

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
        EventHandler.one(this._element, 'transitionend', complete);
        emulateTransitionEnd(this._element, transitionDuration);
      } else {
        complete();
      }
    };

    _proto.hide = function hide() {
      var _this3 = this;

      if (!this._element.classList.contains(CLASS_NAME_SHOW$7)) {
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

      this._element.classList.remove(CLASS_NAME_SHOW$7);

      if (this._config.animation) {
        var transitionDuration = getTransitionDurationFromElement(this._element);
        EventHandler.one(this._element, 'transitionend', complete);
        emulateTransitionEnd(this._element, transitionDuration);
      } else {
        complete();
      }
    };

    _proto.dispose = function dispose() {
      this._clearTimeout();

      if (this._element.classList.contains(CLASS_NAME_SHOW$7)) {
        this._element.classList.remove(CLASS_NAME_SHOW$7);
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


  defineJQueryPlugin(NAME$a, Toast);

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
          const options = {};
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
                      // if the clientId doesn't exists in DOM, it's probably the id of tobago-behavior custom element
                      this.parentElement.addEventListener(this.event, this.callback.bind(this));
                      // todo: not sure if this warning can be removed;
                      console.warn("Can't find an element for the event. Use parentElement instead.", this);
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
                  const partialIds = this.render.split(" ");
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
              const oldTimeout = this.timeouts.get(id);
              if (oldTimeout) {
                  console.debug("clear reload timeout '" + oldTimeout + "' for #'" + id + "'");
                  window.clearTimeout(oldTimeout);
                  this.timeouts.delete(id);
              }
              // add new schedule
              const timeout = window.setTimeout(function () {
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
                  const target = event.target;
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
                  const element = rootNode.getElementById(id);
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
          Datepicker.locales[locale] = i18n;
          const options = {
              buttonClass: "btn",
              orientation: "auto",
              autohide: true,
              language: locale,
              todayBtn: this.todayButton,
              todayBtnMode: 1
              // todo readonly
              // todo show week numbers
          };
          const datepicker = new Datepicker(field, options);
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
          return this.getAttribute("pattern");
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
          const computedStyle = getComputedStyle(target);
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

  function _classCallCheck(instance, Constructor) {
    if (!(instance instanceof Constructor)) {
      throw new TypeError("Cannot call a class as a function");
    }
  }

  function _defineProperties$1(target, props) {
    for (var i = 0; i < props.length; i++) {
      var descriptor = props[i];
      descriptor.enumerable = descriptor.enumerable || false;
      descriptor.configurable = true;
      if ("value" in descriptor) descriptor.writable = true;
      Object.defineProperty(target, descriptor.key, descriptor);
    }
  }

  function _createClass$1(Constructor, protoProps, staticProps) {
    if (protoProps) _defineProperties$1(Constructor.prototype, protoProps);
    if (staticProps) _defineProperties$1(Constructor, staticProps);
    return Constructor;
  }

  function _defineProperty(obj, key, value) {
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
  }

  // Polyfill for element.matches, to support Internet Explorer. It's a relatively
  // simple polyfill, so we'll just include it rather than require the user to
  // include the polyfill themselves. Adapted from
  // https://developer.mozilla.org/en-US/docs/Web/API/Element/matches#Polyfill
  var matches = function matches(element, selector) {
    return element.matches ? element.matches(selector) : element.msMatchesSelector ? element.msMatchesSelector(selector) : element.webkitMatchesSelector ? element.webkitMatchesSelector(selector) : null;
  };

  // Polyfill for element.closest, to support Internet Explorer. It's a relatively

  var closestPolyfill = function closestPolyfill(el, selector) {
    var element = el;

    while (element && element.nodeType === 1) {
      if (matches(element, selector)) {
        return element;
      }

      element = element.parentNode;
    }

    return null;
  };

  var closest = function closest(element, selector) {
    return element.closest ? element.closest(selector) : closestPolyfill(element, selector);
  };

  // Returns true if the value has a "then" function. Adapted from
  // https://github.com/graphql/graphql-js/blob/499a75939f70c4863d44149371d6a99d57ff7c35/src/jsutils/isPromise.js
  var isPromise = function isPromise(value) {
    return Boolean(value && typeof value.then === 'function');
  };

  var AutocompleteCore = function AutocompleteCore() {
    var _this = this;

    var _ref = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {},
        search = _ref.search,
        _ref$autoSelect = _ref.autoSelect,
        autoSelect = _ref$autoSelect === void 0 ? false : _ref$autoSelect,
        _ref$setValue = _ref.setValue,
        setValue = _ref$setValue === void 0 ? function () {} : _ref$setValue,
        _ref$setAttribute = _ref.setAttribute,
        setAttribute = _ref$setAttribute === void 0 ? function () {} : _ref$setAttribute,
        _ref$onUpdate = _ref.onUpdate,
        onUpdate = _ref$onUpdate === void 0 ? function () {} : _ref$onUpdate,
        _ref$onSubmit = _ref.onSubmit,
        onSubmit = _ref$onSubmit === void 0 ? function () {} : _ref$onSubmit,
        _ref$onShow = _ref.onShow,
        onShow = _ref$onShow === void 0 ? function () {} : _ref$onShow,
        _ref$onHide = _ref.onHide,
        onHide = _ref$onHide === void 0 ? function () {} : _ref$onHide,
        _ref$onLoading = _ref.onLoading,
        onLoading = _ref$onLoading === void 0 ? function () {} : _ref$onLoading,
        _ref$onLoaded = _ref.onLoaded,
        onLoaded = _ref$onLoaded === void 0 ? function () {} : _ref$onLoaded;

    _classCallCheck(this, AutocompleteCore);

    _defineProperty(this, "value", '');

    _defineProperty(this, "searchCounter", 0);

    _defineProperty(this, "results", []);

    _defineProperty(this, "selectedIndex", -1);

    _defineProperty(this, "handleInput", function (event) {
      var value = event.target.value;

      _this.updateResults(value);

      _this.value = value;
    });

    _defineProperty(this, "handleKeyDown", function (event) {
      var key = event.key;

      switch (key) {
        case 'Up': // IE/Edge

        case 'Down': // IE/Edge

        case 'ArrowUp':
        case 'ArrowDown':
          {
            var selectedIndex = key === 'ArrowUp' || key === 'Up' ? _this.selectedIndex - 1 : _this.selectedIndex + 1;
            event.preventDefault();

            _this.handleArrows(selectedIndex);

            break;
          }

        case 'Tab':
          {
            _this.selectResult();

            break;
          }

        case 'Enter':
          {
            var selectedResult = _this.results[_this.selectedIndex];

            _this.selectResult();

            _this.onSubmit(selectedResult);

            break;
          }

        case 'Esc': // IE/Edge

        case 'Escape':
          {
            _this.hideResults();

            _this.setValue();

            break;
          }

        default:
          return;
      }
    });

    _defineProperty(this, "handleFocus", function (event) {
      var value = event.target.value;

      _this.updateResults(value);

      _this.value = value;
    });

    _defineProperty(this, "handleBlur", function () {
      _this.hideResults();
    });

    _defineProperty(this, "handleResultMouseDown", function (event) {
      event.preventDefault();
    });

    _defineProperty(this, "handleResultClick", function (event) {
      var target = event.target;
      var result = closest(target, '[data-result-index]');

      if (result) {
        _this.selectedIndex = parseInt(result.dataset.resultIndex, 10);
        var selectedResult = _this.results[_this.selectedIndex];

        _this.selectResult();

        _this.onSubmit(selectedResult);
      }
    });

    _defineProperty(this, "handleArrows", function (selectedIndex) {
      // Loop selectedIndex back to first or last result if out of bounds
      var resultsCount = _this.results.length;
      _this.selectedIndex = (selectedIndex % resultsCount + resultsCount) % resultsCount; // Update results and aria attributes

      _this.onUpdate(_this.results, _this.selectedIndex);
    });

    _defineProperty(this, "selectResult", function () {
      var selectedResult = _this.results[_this.selectedIndex];

      if (selectedResult) {
        _this.setValue(selectedResult);
      }

      _this.hideResults();
    });

    _defineProperty(this, "updateResults", function (value) {
      var currentSearch = ++_this.searchCounter;

      _this.onLoading();

      _this.search(value).then(function (results) {
        if (currentSearch !== _this.searchCounter) {
          return;
        }

        _this.results = results;

        _this.onLoaded();

        if (_this.results.length === 0) {
          _this.hideResults();

          return;
        }

        _this.selectedIndex = _this.autoSelect ? 0 : -1;

        _this.onUpdate(_this.results, _this.selectedIndex);

        _this.showResults();
      });
    });

    _defineProperty(this, "showResults", function () {
      _this.setAttribute('aria-expanded', true);

      _this.onShow();
    });

    _defineProperty(this, "hideResults", function () {
      _this.selectedIndex = -1;
      _this.results = [];

      _this.setAttribute('aria-expanded', false);

      _this.setAttribute('aria-activedescendant', '');

      _this.onUpdate(_this.results, _this.selectedIndex);

      _this.onHide();
    });

    _defineProperty(this, "checkSelectedResultVisible", function (resultsElement) {
      var selectedResultElement = resultsElement.querySelector("[data-result-index=\"".concat(_this.selectedIndex, "\"]"));

      if (!selectedResultElement) {
        return;
      }

      var resultsPosition = resultsElement.getBoundingClientRect();
      var selectedPosition = selectedResultElement.getBoundingClientRect();

      if (selectedPosition.top < resultsPosition.top) {
        // Element is above viewable area
        resultsElement.scrollTop -= resultsPosition.top - selectedPosition.top;
      } else if (selectedPosition.bottom > resultsPosition.bottom) {
        // Element is below viewable area
        resultsElement.scrollTop += selectedPosition.bottom - resultsPosition.bottom;
      }
    });

    this.search = isPromise(search) ? search : function (value) {
      return Promise.resolve(search(value));
    };
    this.autoSelect = autoSelect;
    this.setValue = setValue;
    this.setAttribute = setAttribute;
    this.onUpdate = onUpdate;
    this.onSubmit = onSubmit;
    this.onShow = onShow;
    this.onHide = onHide;
    this.onLoading = onLoading;
    this.onLoaded = onLoaded;
  };

  // Generates a unique ID, with optional prefix. Adapted from
  // https://github.com/lodash/lodash/blob/61acdd0c295e4447c9c10da04e287b1ebffe452c/uniqueId.js
  var idCounter = 0;

  var uniqueId = function uniqueId() {
    var prefix = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
    return "".concat(prefix).concat(++idCounter);
  };

  // Calculates whether element2 should be above or below element1. Always
  // places element2 below unless all of the following:
  // 1. There isn't enough visible viewport below to fit element2
  // 2. There is more room above element1 than there is below
  // 3. Placing elemen2 above 1 won't overflow window
  var getRelativePosition = function getRelativePosition(element1, element2) {
    var position1 = element1.getBoundingClientRect();
    var position2 = element2.getBoundingClientRect();
    var positionAbove =
    /* 1 */
    position1.bottom + position2.height > window.innerHeight &&
    /* 2 */
    window.innerHeight - position1.bottom < position1.top &&
    /* 3 */
    window.pageYOffset + position1.top - position2.height > 0;
    return positionAbove ? 'above' : 'below';
  };

  // Credit David Walsh (https://davidwalsh.name/javascript-debounce-function)
  // Returns a function, that, as long as it continues to be invoked, will not
  // be triggered. The function will be called after it stops being called for
  // N milliseconds. If `immediate` is passed, trigger the function on the
  // leading edge, instead of the trailing.
  var debounce = function debounce(func, wait, immediate) {
    var timeout;
    return function executedFunction() {
      var context = this;
      var args = arguments;

      var later = function later() {
        timeout = null;
        if (!immediate) func.apply(context, args);
      };

      var callNow = immediate && !timeout;
      clearTimeout(timeout);
      timeout = setTimeout(later, wait);
      if (callNow) func.apply(context, args);
    };
  };

  // string in the format: `key1="value1" key2="value2"` for easy use in an HTML string.

  var Props =
  /*#__PURE__*/
  function () {
    function Props(index, selectedIndex, baseClass) {
      _classCallCheck(this, Props);

      this.id = "".concat(baseClass, "-result-").concat(index);
      this["class"] = "".concat(baseClass, "-result");
      this['data-result-index'] = index;
      this.role = 'option';

      if (index === selectedIndex) {
        this['aria-selected'] = 'true';
      }
    }

    _createClass$1(Props, [{
      key: "toString",
      value: function toString() {
        var _this = this;

        return Object.keys(this).reduce(function (str, key) {
          return "".concat(str, " ").concat(key, "=\"").concat(_this[key], "\"");
        }, '');
      }
    }]);

    return Props;
  }();

  var Autocomplete = function Autocomplete(root) {
    var _this2 = this;

    var _ref = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {},
        search = _ref.search,
        _ref$onSubmit = _ref.onSubmit,
        onSubmit = _ref$onSubmit === void 0 ? function () {} : _ref$onSubmit,
        _ref$onUpdate = _ref.onUpdate,
        onUpdate = _ref$onUpdate === void 0 ? function () {} : _ref$onUpdate,
        _ref$baseClass = _ref.baseClass,
        baseClass = _ref$baseClass === void 0 ? 'autocomplete' : _ref$baseClass,
        autoSelect = _ref.autoSelect,
        _ref$getResultValue = _ref.getResultValue,
        getResultValue = _ref$getResultValue === void 0 ? function (result) {
      return result;
    } : _ref$getResultValue,
        renderResult = _ref.renderResult,
        _ref$debounceTime = _ref.debounceTime,
        debounceTime = _ref$debounceTime === void 0 ? 0 : _ref$debounceTime;

    _classCallCheck(this, Autocomplete);

    _defineProperty(this, "expanded", false);

    _defineProperty(this, "loading", false);

    _defineProperty(this, "position", {});

    _defineProperty(this, "resetPosition", true);

    _defineProperty(this, "initialize", function () {
      _this2.root.style.position = 'relative';

      _this2.input.setAttribute('role', 'combobox');

      _this2.input.setAttribute('autocomplete', 'off');

      _this2.input.setAttribute('autocapitalize', 'off');

      _this2.input.setAttribute('autocorrect', 'off');

      _this2.input.setAttribute('spellcheck', 'false');

      _this2.input.setAttribute('aria-autocomplete', 'list');

      _this2.input.setAttribute('aria-haspopup', 'listbox');

      _this2.input.setAttribute('aria-expanded', 'false');

      _this2.resultList.setAttribute('role', 'listbox');

      _this2.resultList.style.position = 'absolute';
      _this2.resultList.style.zIndex = '1';
      _this2.resultList.style.width = '100%';
      _this2.resultList.style.boxSizing = 'border-box'; // Generate ID for results list if it doesn't have one

      if (!_this2.resultList.id) {
        _this2.resultList.id = uniqueId("".concat(_this2.baseClass, "-result-list-"));
      }

      _this2.input.setAttribute('aria-owns', _this2.resultList.id);

      document.body.addEventListener('click', _this2.handleDocumentClick);

      _this2.input.addEventListener('input', _this2.core.handleInput);

      _this2.input.addEventListener('keydown', _this2.core.handleKeyDown);

      _this2.input.addEventListener('focus', _this2.core.handleFocus);

      _this2.input.addEventListener('blur', _this2.core.handleBlur);

      _this2.resultList.addEventListener('mousedown', _this2.core.handleResultMouseDown);

      _this2.resultList.addEventListener('click', _this2.core.handleResultClick);

      _this2.updateStyle();
    });

    _defineProperty(this, "setAttribute", function (attribute, value) {
      _this2.input.setAttribute(attribute, value);
    });

    _defineProperty(this, "setValue", function (result) {
      _this2.input.value = result ? _this2.getResultValue(result) : '';
    });

    _defineProperty(this, "renderResult", function (result, props) {
      return "<li ".concat(props, ">").concat(_this2.getResultValue(result), "</li>");
    });

    _defineProperty(this, "handleUpdate", function (results, selectedIndex) {
      _this2.resultList.innerHTML = '';
      results.forEach(function (result, index) {
        var props = new Props(index, selectedIndex, _this2.baseClass);

        var resultHTML = _this2.renderResult(result, props);

        if (typeof resultHTML === 'string') {
          _this2.resultList.insertAdjacentHTML('beforeend', resultHTML);
        } else {
          _this2.resultList.insertAdjacentElement('beforeend', resultHTML);
        }
      });

      _this2.input.setAttribute('aria-activedescendant', selectedIndex > -1 ? "".concat(_this2.baseClass, "-result-").concat(selectedIndex) : '');

      if (_this2.resetPosition) {
        _this2.resetPosition = false;
        _this2.position = getRelativePosition(_this2.input, _this2.resultList);

        _this2.updateStyle();
      }

      _this2.core.checkSelectedResultVisible(_this2.resultList);

      _this2.onUpdate(results, selectedIndex);
    });

    _defineProperty(this, "handleShow", function () {
      _this2.expanded = true;

      _this2.updateStyle();
    });

    _defineProperty(this, "handleHide", function () {
      _this2.expanded = false;
      _this2.resetPosition = true;

      _this2.updateStyle();
    });

    _defineProperty(this, "handleLoading", function () {
      _this2.loading = true;

      _this2.updateStyle();
    });

    _defineProperty(this, "handleLoaded", function () {
      _this2.loading = false;

      _this2.updateStyle();
    });

    _defineProperty(this, "handleDocumentClick", function (event) {
      if (_this2.root.contains(event.target)) {
        return;
      }

      _this2.core.hideResults();
    });

    _defineProperty(this, "updateStyle", function () {
      _this2.root.dataset.expanded = _this2.expanded;
      _this2.root.dataset.loading = _this2.loading;
      _this2.root.dataset.position = _this2.position;
      _this2.resultList.style.visibility = _this2.expanded ? 'visible' : 'hidden';
      _this2.resultList.style.pointerEvents = _this2.expanded ? 'auto' : 'none';

      if (_this2.position === 'below') {
        _this2.resultList.style.bottom = null;
        _this2.resultList.style.top = '100%';
      } else {
        _this2.resultList.style.top = null;
        _this2.resultList.style.bottom = '100%';
      }
    });

    this.root = typeof root === 'string' ? document.querySelector(root) : root;
    this.input = this.root.querySelector('input');
    this.resultList = this.root.querySelector('ul');
    this.baseClass = baseClass;
    this.getResultValue = getResultValue;
    this.onUpdate = onUpdate;

    if (typeof renderResult === 'function') {
      this.renderResult = renderResult;
    }

    var core = new AutocompleteCore({
      search: search,
      autoSelect: autoSelect,
      setValue: this.setValue,
      setAttribute: this.setAttribute,
      onUpdate: this.handleUpdate,
      onSubmit: onSubmit,
      onShow: this.handleShow,
      onHide: this.handleHide,
      onLoading: this.handleLoading,
      onLoaded: this.handleLoaded
    });

    if (debounceTime > 0) {
      core.handleInput = debounce(core.handleInput, debounceTime);
    }

    this.core = core;
    this.initialize();
  } // Set up aria attributes and events
  ;

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
  class Suggest {
      constructor(tobagoIn) {
          this.tobagoIn = tobagoIn;
      }
      init() {
          if (!this.suggest) {
              console.warn("[tobago-suggest] could not find tobago-suggest");
              return;
          }
          this.registerAjaxListener();
          this.base.classList.add("autocomplete");
          this.base.insertAdjacentHTML("afterbegin", "<div class=\"autocomplete-pseudo-container\"></div>");
          this.suggestInput.classList.add("autocomplete-input");
          this.suggestInput.insertAdjacentHTML("afterend", "<ul class=\"autocomplete-result-list\"></ul>");
          const options = {
              search: input => {
                  console.debug("[tobago-suggest] input = '" + input + "'");
                  const minChars = this.minChars ? this.minChars : 1;
                  if (input.length < minChars) {
                      return [];
                  }
                  this.hiddenInput.value = input.toLowerCase();
                  this.positioningSpinner();
                  return new Promise(resolve => {
                      if (input.length < 1) {
                          return resolve([]);
                      }
                      if (this.update) {
                          this.resolve = resolve;
                          const suggestId = this.suggest.id;
                          jsf.ajax.request(suggestId, null, {
                              "javax.faces.behavior.event": "suggest",
                              execute: suggestId,
                              render: suggestId
                          });
                      }
                      else {
                          return resolve(this.filterItems());
                      }
                  });
              },
              onUpdate: (results, selectedIndex) => {
                  this.positioningResultList();
                  this.setResultListMaxHeight();
              },
              debounceTime: this.delay
          };
          new Autocomplete(this.base, options);
          if (!this.localMenu) {
              this.menuStore.append(this.resultList);
          }
      }
      registerAjaxListener() {
          jsf.ajax.addOnEvent(this.resolvePromise.bind(this));
      }
      resolvePromise(event) {
          if (event.source === this.suggest && event.status === "success") {
              return this.resolve(this.filterItems());
          }
      }
      filterItems() {
          return this.items.filter(item => {
              return item.toLowerCase().indexOf(this.hiddenInput.value) > -1;
          });
      }
      positioningSpinner() {
          const baseRect = this.base.getBoundingClientRect();
          const suggestInputRect = this.suggestInput.getBoundingClientRect();
          const suggestInputStyle = getComputedStyle(this.suggestInput);
          this.pseudoContainer.style.left = suggestInputRect.x - baseRect.x + suggestInputRect.width
              - parseFloat(getComputedStyle(this.pseudoContainer, ":after").width)
              - parseFloat(suggestInputStyle.marginRight)
              - parseFloat(suggestInputStyle.borderRight)
              - parseFloat(suggestInputStyle.paddingRight) + "px";
          this.pseudoContainer.style.top = suggestInputRect.y - baseRect.y + (suggestInputRect.height / 2) + "px";
      }
      positioningResultList() {
          const space = 2;
          if (this.localMenu) {
              const parentRect = this.suggestInput.parentElement.getBoundingClientRect();
              const suggestInputRect = this.suggestInput.getBoundingClientRect();
              this.resultList.style.marginLeft = (suggestInputRect.x - parentRect.x) + "px";
              this.resultList.style.maxWidth = suggestInputRect.width + "px";
              this.resultList.style.marginTop = space + "px";
              this.resultList.style.marginBottom = space + "px";
          }
          else {
              const suggestInputRect = this.suggestInput.getBoundingClientRect();
              this.resultList.style.minWidth = suggestInputRect.width + "px";
              this.resultList.style.left = suggestInputRect.left + "px";
              if (this.resultListPosition === "below") {
                  this.resultList.style.marginTop =
                      window.scrollY + suggestInputRect.top + suggestInputRect.height + space + "px";
                  this.resultList.style.marginBottom = null;
              }
              else if (this.resultListPosition === "above") {
                  this.resultList.style.marginTop = null;
                  this.resultList.style.marginBottom = -(window.scrollY + suggestInputRect.top - space) + "px";
              }
          }
      }
      setResultListMaxHeight() {
          const resultListEntry = this.resultList.querySelector(".autocomplete-result");
          if (this.maxItems && resultListEntry) {
              const resultListStyle = getComputedStyle(this.resultList);
              this.resultList.style.maxHeight = (parseFloat(resultListStyle.borderTop)
                  + parseFloat(resultListStyle.paddingTop)
                  + (this.maxItems * parseFloat(getComputedStyle(resultListEntry).height))
                  + parseFloat(resultListStyle.paddingBottom)
                  + parseFloat(resultListStyle.borderBottom)) + "px";
          }
      }
      get base() {
          return this.tobagoIn;
      }
      get pseudoContainer() {
          return this.base.querySelector(":scope > .autocomplete-pseudo-container");
      }
      get suggestInput() {
          const root = this.base.getRootNode();
          return root.getElementById(this.suggest.getAttribute("for"));
      }
      get suggest() {
          return this.base.querySelector("tobago-suggest");
      }
      get hiddenInput() {
          return this.suggest.querySelector(":scope > input[type=hidden]");
      }
      get items() {
          return JSON.parse(this.suggest.getAttribute("items"));
      }
      get resultList() {
          const root = this.base.getRootNode();
          const resultListId = this.suggestInput.getAttribute("aria-owns");
          return root.getElementById(resultListId);
      }
      get resultListPosition() {
          return this.base.dataset.position;
      }
      get menuStore() {
          const root = this.base.getRootNode();
          return root.querySelector(".tobago-page-menuStore");
      }
      get update() {
          return this.suggest.getAttribute("update") !== null;
      }
      get delay() {
          return parseInt(this.suggest.getAttribute("delay"));
      }
      get maxItems() {
          return parseInt(this.suggest.getAttribute("max-items"));
      }
      get minChars() {
          return parseInt(this.suggest.getAttribute("min-chars"));
      }
      get localMenu() {
          return this.suggest.getAttribute("local-menu") !== null;
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
  class In extends HTMLElement {
      constructor() {
          super();
      }
      connectedCallback() {
          this.input.addEventListener("focus", Focus.setLastFocusId);
          if (this.querySelector("tobago-suggest")) {
              const suggest = new Suggest(this);
              suggest.init();
          }
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
          for (const closeButton of this.closeButtons) {
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
          this.saveSelection();
          this.field.addEventListener("click", this.clickSelection.bind(this));
          this.field.addEventListener("focus", Focus.setLastFocusId);
      }
      clickSelection(event) {
          const select = event.currentTarget;
          if (!select.required && this.field.selectedIndex === this.oldselectedIndex) {
              this.field.selectedIndex = -1;
          }
          this.saveSelection();
      }
      saveSelection() {
          this.oldselectedIndex = this.field.selectedIndex;
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
          const columnWidths = this.loadColumnWidths();
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
              let r = 0;
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
                      else if (tokens[i] === "auto") {
                          const value = headerCols.item(r).offsetWidth;
                          widthRelative -= value;
                          tokens[i] = { measure: value + "px" }; // converting "auto" to a specific value
                      }
                      else {
                          console.debug("(layout columns a) auto? token[i]='%s' i=%i", tokens[i], i);
                      }
                  }
              }
              if (widthRelative < 0) {
                  widthRelative = 0;
              }
              r = 0;
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
                          console.debug("(layout columns b) auto? token[i]='%s' i=%i", tokens[i], i);
                      }
                      if (colWidth > 0) { // because tokens[i] == "auto"
                          headerCols.item(r).setAttribute("width", String(colWidth));
                          bodyCols.item(r).setAttribute("width", String(colWidth));
                      }
                      r++;
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
                      document.getElementById(id);
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
          const columnWidth = this.mousemoveData.originalHeaderColumnWidth + delta;
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
          for (const child of this.children) {
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
          SplitLayout.previousElementSibling(splitter);
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
          const navLink = this.navLink;
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
                      const treeNodeIds = [];
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
          const treeNode = this.closest("tobago-tree-node");
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
