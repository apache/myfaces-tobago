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
          if (!element) {
              element = document.documentElement;
          }
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
  class Dropdown$1 extends HTMLElement {
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
          window.customElements.define("tobago-dropdown", Dropdown$1);
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
    if (datepicker.config.showOnFocus && !datepicker._showing) {
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

      // set initial dates
      this.dates = [];
      // process initial value
      const inputDateValues = processInputDates(this, initialDates);
      if (inputDateValues && inputDateValues.length > 0) {
        this.dates = inputDateValues;
      }
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
      if (this.inputField) {
        if (this.inputField.disabled) {
          return;
        }
        if (this.inputField !== document.activeElement) {
          this._showing = true;
          this.inputField.focus();
          delete this._showing;
        }
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

  function getWindow(node) {
    if (node == null) {
      return window;
    }

    if (node.toString() !== '[object Window]') {
      var ownerDocument = node.ownerDocument;
      return ownerDocument ? ownerDocument.defaultView || window : window;
    }

    return node;
  }

  function isElement$1(node) {
    var OwnElement = getWindow(node).Element;
    return node instanceof OwnElement || node instanceof Element;
  }

  function isHTMLElement(node) {
    var OwnElement = getWindow(node).HTMLElement;
    return node instanceof OwnElement || node instanceof HTMLElement;
  }

  function isShadowRoot(node) {
    // IE 11 has no ShadowRoot
    if (typeof ShadowRoot === 'undefined') {
      return false;
    }

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

  function effect$2(_ref2) {
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
    state.styles = initialStyles;

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
    effect: effect$2,
    requires: ['computeStyles']
  };

  function getBasePlacement(placement) {
    return placement.split('-')[0];
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

  // means it doesn't take into account transforms.

  function getLayoutRect(element) {
    var clientRect = getBoundingClientRect(element); // Use the clientRect sizes if it's not been transformed.
    // Fixes https://github.com/popperjs/popper-core/issues/1223

    var width = element.offsetWidth;
    var height = element.offsetHeight;

    if (Math.abs(clientRect.width - width) <= 1) {
      width = clientRect.width;
    }

    if (Math.abs(clientRect.height - height) <= 1) {
      height = clientRect.height;
    }

    return {
      x: element.offsetLeft,
      y: element.offsetTop,
      width: width,
      height: height
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
    return ((isElement$1(element) ? element.ownerDocument : // $FlowFixMe[prop-missing]
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
      element.parentNode || ( // DOM Element detected
      isShadowRoot(element) ? element.host : null) || // ShadowRoot detected
      // $FlowFixMe[incompatible-call]: HTMLElement is a Node
      getDocumentElement(element) // fallback

    );
  }

  function getTrueOffsetParent(element) {
    if (!isHTMLElement(element) || // https://github.com/popperjs/popper-core/issues/837
    getComputedStyle$1(element).position === 'fixed') {
      return null;
    }

    return element.offsetParent;
  } // `.offsetParent` reports `null` for fixed elements, while absolute elements
  // return the containing block


  function getContainingBlock(element) {
    var isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') !== -1;
    var isIE = navigator.userAgent.indexOf('Trident') !== -1;

    if (isIE && isHTMLElement(element)) {
      // In IE 9, 10 and 11 fixed elements containing block is always established by the viewport
      var elementCss = getComputedStyle$1(element);

      if (elementCss.position === 'fixed') {
        return null;
      }
    }

    var currentNode = getParentNode(element);

    while (isHTMLElement(currentNode) && ['html', 'body'].indexOf(getNodeName(currentNode)) < 0) {
      var css = getComputedStyle$1(currentNode); // This is non-exhaustive but covers the most common CSS properties that
      // create a containing block.
      // https://developer.mozilla.org/en-US/docs/Web/CSS/Containing_block#identifying_the_containing_block

      if (css.transform !== 'none' || css.perspective !== 'none' || css.contain === 'paint' || ['transform', 'perspective'].indexOf(css.willChange) !== -1 || isFirefox && css.willChange === 'filter' || isFirefox && css.filter && css.filter !== 'none') {
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

    if (offsetParent && (getNodeName(offsetParent) === 'html' || getNodeName(offsetParent) === 'body' && getComputedStyle$1(offsetParent).position === 'static')) {
      return window;
    }

    return offsetParent || getContainingBlock(element) || window;
  }

  function getMainAxisFromPlacement(placement) {
    return ['top', 'bottom'].indexOf(placement) >= 0 ? 'x' : 'y';
  }

  var max = Math.max;
  var min = Math.min;
  var round = Math.round;

  function within(min$1, value, max$1) {
    return max(min$1, min(value, max$1));
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
    return Object.assign({}, getFreshSideObject(), paddingObject);
  }

  function expandToHashMap(value, keys) {
    return keys.reduce(function (hashMap, key) {
      hashMap[key] = value;
      return hashMap;
    }, {});
  }

  var toPaddingObject = function toPaddingObject(padding, state) {
    padding = typeof padding === 'function' ? padding(Object.assign({}, state.rects, {
      placement: state.placement
    })) : padding;
    return mergePaddingObject(typeof padding !== 'number' ? padding : expandToHashMap(padding, basePlacements));
  };

  function arrow(_ref) {
    var _state$modifiersData$;

    var state = _ref.state,
        name = _ref.name,
        options = _ref.options;
    var arrowElement = state.elements.arrow;
    var popperOffsets = state.modifiersData.popperOffsets;
    var basePlacement = getBasePlacement(state.placement);
    var axis = getMainAxisFromPlacement(basePlacement);
    var isVertical = [left, right].indexOf(basePlacement) >= 0;
    var len = isVertical ? 'height' : 'width';

    if (!arrowElement || !popperOffsets) {
      return;
    }

    var paddingObject = toPaddingObject(options.padding, state);
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
        options = _ref2.options;
    var _options$element = options.element,
        arrowElement = _options$element === void 0 ? '[data-popper-arrow]' : _options$element;

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
      x: round(round(x * dpr) / dpr) || 0,
      y: round(round(y * dpr) / dpr) || 0
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

    var _ref3 = roundOffsets === true ? roundOffsetsByDPR(offsets) : typeof roundOffsets === 'function' ? roundOffsets(offsets) : offsets,
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
      var heightProp = 'clientHeight';
      var widthProp = 'clientWidth';

      if (offsetParent === getWindow(popper)) {
        offsetParent = getDocumentElement(popper);

        if (getComputedStyle$1(offsetParent).position !== 'static') {
          heightProp = 'scrollHeight';
          widthProp = 'scrollWidth';
        }
      } // $FlowFixMe[incompatible-cast]: force type refinement, we compare offsetParent with window above, but Flow doesn't detect it


      offsetParent = offsetParent;

      if (placement === top) {
        sideY = bottom; // $FlowFixMe[prop-missing]

        y -= offsetParent[heightProp] - popperRect.height;
        y *= gpuAcceleration ? 1 : -1;
      }

      if (placement === left) {
        sideX = right; // $FlowFixMe[prop-missing]

        x -= offsetParent[widthProp] - popperRect.width;
        x *= gpuAcceleration ? 1 : -1;
      }
    }

    var commonStyles = Object.assign({
      position: position
    }, adaptive && unsetSides);

    if (gpuAcceleration) {
      var _Object$assign;

      return Object.assign({}, commonStyles, (_Object$assign = {}, _Object$assign[sideY] = hasY ? '0' : '', _Object$assign[sideX] = hasX ? '0' : '', _Object$assign.transform = (win.devicePixelRatio || 1) < 2 ? "translate(" + x + "px, " + y + "px)" : "translate3d(" + x + "px, " + y + "px, 0)", _Object$assign));
    }

    return Object.assign({}, commonStyles, (_Object$assign2 = {}, _Object$assign2[sideY] = hasY ? y + "px" : '', _Object$assign2[sideX] = hasX ? x + "px" : '', _Object$assign2.transform = '', _Object$assign2));
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
      state.styles.popper = Object.assign({}, state.styles.popper, mapToStyles(Object.assign({}, commonStyles, {
        offsets: state.modifiersData.popperOffsets,
        position: state.options.strategy,
        adaptive: adaptive,
        roundOffsets: roundOffsets
      })));
    }

    if (state.modifiersData.arrow != null) {
      state.styles.arrow = Object.assign({}, state.styles.arrow, mapToStyles(Object.assign({}, commonStyles, {
        offsets: state.modifiersData.arrow,
        position: 'absolute',
        adaptive: false,
        roundOffsets: roundOffsets
      })));
    }

    state.attributes.popper = Object.assign({}, state.attributes.popper, {
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

  function effect(_ref) {
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
    effect: effect,
    data: {}
  };

  var hash$1 = {
    left: 'right',
    right: 'left',
    bottom: 'top',
    top: 'bottom'
  };
  function getOppositePlacement(placement) {
    return placement.replace(/left|right|bottom|top/g, function (matched) {
      return hash$1[matched];
    });
  }

  var hash = {
    start: 'end',
    end: 'start'
  };
  function getOppositeVariationPlacement(placement) {
    return placement.replace(/start|end/g, function (matched) {
      return hash[matched];
    });
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
    var _element$ownerDocumen;

    var html = getDocumentElement(element);
    var winScroll = getWindowScroll(element);
    var body = (_element$ownerDocumen = element.ownerDocument) == null ? void 0 : _element$ownerDocumen.body;
    var width = max(html.scrollWidth, html.clientWidth, body ? body.scrollWidth : 0, body ? body.clientWidth : 0);
    var height = max(html.scrollHeight, html.clientHeight, body ? body.scrollHeight : 0, body ? body.clientHeight : 0);
    var x = -winScroll.scrollLeft + getWindowScrollBarX(element);
    var y = -winScroll.scrollTop;

    if (getComputedStyle$1(body || html).direction === 'rtl') {
      x += max(html.clientWidth, body ? body.clientWidth : 0) - width;
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
    var _element$ownerDocumen;

    if (list === void 0) {
      list = [];
    }

    var scrollParent = getScrollParent(element);
    var isBody = scrollParent === ((_element$ownerDocumen = element.ownerDocument) == null ? void 0 : _element$ownerDocumen.body);
    var win = getWindow(scrollParent);
    var target = isBody ? [win].concat(win.visualViewport || [], isScrollParent(scrollParent) ? scrollParent : []) : scrollParent;
    var updatedList = list.concat(target);
    return isBody ? updatedList : // $FlowFixMe[incompatible-call]: isBody tells us target will be an HTMLElement here
    updatedList.concat(listScrollParents(getParentNode(target)));
  }

  function rectToClientRect(rect) {
    return Object.assign({}, rect, {
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

    if (!isElement$1(clipperElement)) {
      return [];
    } // $FlowFixMe[incompatible-return]: https://github.com/facebook/flow/issues/1414


    return clippingParents.filter(function (clippingParent) {
      return isElement$1(clippingParent) && contains(clippingParent, clipperElement) && getNodeName(clippingParent) !== 'body';
    });
  } // Gets the maximum area that the element is visible in due to any number of
  // clipping parents


  function getClippingRect(element, boundary, rootBoundary) {
    var mainClippingParents = boundary === 'clippingParents' ? getClippingParents(element) : [].concat(boundary);
    var clippingParents = [].concat(mainClippingParents, [rootBoundary]);
    var firstClippingParent = clippingParents[0];
    var clippingRect = clippingParents.reduce(function (accRect, clippingParent) {
      var rect = getClientRectFromMixedType(element, clippingParent);
      accRect.top = max(rect.top, accRect.top);
      accRect.right = min(rect.right, accRect.right);
      accRect.bottom = min(rect.bottom, accRect.bottom);
      accRect.left = max(rect.left, accRect.left);
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
    var clippingClientRect = getClippingRect(isElement$1(element) ? element : element.contextElement || getDocumentElement(state.elements.popper), boundary, rootBoundary);
    var referenceClientRect = getBoundingClientRect(referenceElement);
    var popperOffsets = computeOffsets({
      reference: referenceClientRect,
      element: popperRect,
      strategy: 'absolute',
      placement: placement
    });
    var popperClientRect = rectToClientRect(Object.assign({}, popperRect, popperOffsets));
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

  function hide$1(_ref) {
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
    state.attributes.popper = Object.assign({}, state.attributes.popper, {
      'data-popper-reference-hidden': isReferenceHidden,
      'data-popper-escaped': hasPopperEscaped
    });
  } // eslint-disable-next-line import/no-unused-modules


  var hide$2 = {
    name: 'hide',
    enabled: true,
    phase: 'main',
    requiresIfExists: ['preventOverflow'],
    fn: hide$1
  };

  function distanceAndSkiddingToXY(placement, rects, offset) {
    var basePlacement = getBasePlacement(placement);
    var invertDistance = [left, top].indexOf(basePlacement) >= 0 ? -1 : 1;

    var _ref = typeof offset === 'function' ? offset(Object.assign({}, rects, {
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
    var tetherOffsetValue = typeof tetherOffset === 'function' ? tetherOffset(Object.assign({}, state.rects, {
      placement: state.placement
    })) : tetherOffset;
    var data = {
      x: 0,
      y: 0
    };

    if (!popperOffsets) {
      return;
    }

    if (checkMainAxis || checkAltAxis) {
      var mainSide = mainAxis === 'y' ? top : left;
      var altSide = mainAxis === 'y' ? bottom : right;
      var len = mainAxis === 'y' ? 'height' : 'width';
      var offset = popperOffsets[mainAxis];
      var min$1 = popperOffsets[mainAxis] + overflow[mainSide];
      var max$1 = popperOffsets[mainAxis] - overflow[altSide];
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

      if (checkMainAxis) {
        var preventedOffset = within(tether ? min(min$1, tetherMin) : min$1, offset, tether ? max(max$1, tetherMax) : max$1);
        popperOffsets[mainAxis] = preventedOffset;
        data[mainAxis] = preventedOffset - offset;
      }

      if (checkAltAxis) {
        var _mainSide = mainAxis === 'x' ? top : left;

        var _altSide = mainAxis === 'x' ? bottom : right;

        var _offset = popperOffsets[altAxis];

        var _min = _offset + overflow[_mainSide];

        var _max = _offset - overflow[_altSide];

        var _preventedOffset = within(tether ? min(_min, tetherMin) : _min, _offset, tether ? max(_max, tetherMax) : _max);

        popperOffsets[altAxis] = _preventedOffset;
        data[altAxis] = _preventedOffset - _offset;
      }
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

  function debounce$1(fn) {
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
      merged[current.name] = existing ? Object.assign({}, existing, current, {
        options: Object.assign({}, existing.options, current.options),
        data: Object.assign({}, existing.data, current.data)
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
        options: Object.assign({}, DEFAULT_OPTIONS, defaultOptions),
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
          state.options = Object.assign({}, defaultOptions, state.options, options);
          state.scrollParents = {
            reference: isElement$1(reference) ? listScrollParents(reference) : reference.contextElement ? listScrollParents(reference.contextElement) : [],
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
        update: debounce$1(function () {
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
  var createPopper$2 = /*#__PURE__*/popperGenerator(); // eslint-disable-next-line import/no-unused-modules

  var defaultModifiers$1 = [eventListeners, popperOffsets$1, computeStyles$1, applyStyles$1];
  var createPopper$1 = /*#__PURE__*/popperGenerator({
    defaultModifiers: defaultModifiers$1
  }); // eslint-disable-next-line import/no-unused-modules

  var defaultModifiers = [eventListeners, popperOffsets$1, computeStyles$1, applyStyles$1, offset$1, flip$1, preventOverflow$1, arrow$1, hide$2];
  var createPopper = /*#__PURE__*/popperGenerator({
    defaultModifiers: defaultModifiers
  }); // eslint-disable-next-line import/no-unused-modules

  var Popper = /*#__PURE__*/Object.freeze({
    __proto__: null,
    popperGenerator: popperGenerator,
    detectOverflow: detectOverflow,
    createPopperBase: createPopper$2,
    createPopper: createPopper,
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
    hide: hide$2,
    offset: offset$1,
    popperOffsets: popperOffsets$1,
    preventOverflow: preventOverflow$1
  });

  /*!
    * Bootstrap v5.0.0-beta3 (https://getbootstrap.com/)
    * Copyright 2011-2021 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
    * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
    */

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): util/index.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  const MAX_UID = 1000000;
  const MILLISECONDS_MULTIPLIER = 1000;
  const TRANSITION_END = 'transitionend'; // Shoutout AngusCroll (https://goo.gl/pxwQGp)

  const toType = obj => {
    if (obj === null || obj === undefined) {
      return `${obj}`;
    }

    return {}.toString.call(obj).match(/\s([a-z]+)/i)[1].toLowerCase();
  };
  /**
   * --------------------------------------------------------------------------
   * Public Util Api
   * --------------------------------------------------------------------------
   */


  const getUID = prefix => {
    do {
      prefix += Math.floor(Math.random() * MAX_UID);
    } while (document.getElementById(prefix));

    return prefix;
  };

  const getSelector = element => {
    let selector = element.getAttribute('data-bs-target');

    if (!selector || selector === '#') {
      let hrefAttr = element.getAttribute('href'); // The only valid content that could double as a selector are IDs or classes,
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

  const getSelectorFromElement = element => {
    const selector = getSelector(element);

    if (selector) {
      return document.querySelector(selector) ? selector : null;
    }

    return null;
  };

  const getElementFromSelector = element => {
    const selector = getSelector(element);
    return selector ? document.querySelector(selector) : null;
  };

  const getTransitionDurationFromElement = element => {
    if (!element) {
      return 0;
    } // Get transition-duration of the element


    let {
      transitionDuration,
      transitionDelay
    } = window.getComputedStyle(element);
    const floatTransitionDuration = Number.parseFloat(transitionDuration);
    const floatTransitionDelay = Number.parseFloat(transitionDelay); // Return 0 if element or transition duration is not found

    if (!floatTransitionDuration && !floatTransitionDelay) {
      return 0;
    } // If multiple durations are defined, take the first


    transitionDuration = transitionDuration.split(',')[0];
    transitionDelay = transitionDelay.split(',')[0];
    return (Number.parseFloat(transitionDuration) + Number.parseFloat(transitionDelay)) * MILLISECONDS_MULTIPLIER;
  };

  const triggerTransitionEnd = element => {
    element.dispatchEvent(new Event(TRANSITION_END));
  };

  const isElement = obj => (obj[0] || obj).nodeType;

  const emulateTransitionEnd = (element, duration) => {
    let called = false;
    const durationPadding = 5;
    const emulatedDuration = duration + durationPadding;

    function listener() {
      called = true;
      element.removeEventListener(TRANSITION_END, listener);
    }

    element.addEventListener(TRANSITION_END, listener);
    setTimeout(() => {
      if (!called) {
        triggerTransitionEnd(element);
      }
    }, emulatedDuration);
  };

  const typeCheckConfig = (componentName, config, configTypes) => {
    Object.keys(configTypes).forEach(property => {
      const expectedTypes = configTypes[property];
      const value = config[property];
      const valueType = value && isElement(value) ? 'element' : toType(value);

      if (!new RegExp(expectedTypes).test(valueType)) {
        throw new TypeError(`${componentName.toUpperCase()}: ` + `Option "${property}" provided type "${valueType}" ` + `but expected type "${expectedTypes}".`);
      }
    });
  };

  const isVisible = element => {
    if (!element) {
      return false;
    }

    if (element.style && element.parentNode && element.parentNode.style) {
      const elementStyle = getComputedStyle(element);
      const parentNodeStyle = getComputedStyle(element.parentNode);
      return elementStyle.display !== 'none' && parentNodeStyle.display !== 'none' && elementStyle.visibility !== 'hidden';
    }

    return false;
  };

  const isDisabled = element => {
    if (!element || element.nodeType !== Node.ELEMENT_NODE) {
      return true;
    }

    if (element.classList.contains('disabled')) {
      return true;
    }

    if (typeof element.disabled !== 'undefined') {
      return element.disabled;
    }

    return element.hasAttribute('disabled') && element.getAttribute('disabled') !== 'false';
  };

  const findShadowRoot = element => {
    if (!document.documentElement.attachShadow) {
      return null;
    } // Can find the shadow root otherwise it'll return the document


    if (typeof element.getRootNode === 'function') {
      const root = element.getRootNode();
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

  const noop = () => function () {};

  const reflow = element => element.offsetHeight;

  const getjQuery = () => {
    const {
      jQuery
    } = window;

    if (jQuery && !document.body.hasAttribute('data-bs-no-jquery')) {
      return jQuery;
    }

    return null;
  };

  const onDOMContentLoaded = callback => {
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', callback);
    } else {
      callback();
    }
  };

  const isRTL = () => document.documentElement.dir === 'rtl';

  const defineJQueryPlugin = (name, plugin) => {
    onDOMContentLoaded(() => {
      const $ = getjQuery();
      /* istanbul ignore if */

      if ($) {
        const JQUERY_NO_CONFLICT = $.fn[name];
        $.fn[name] = plugin.jQueryInterface;
        $.fn[name].Constructor = plugin;

        $.fn[name].noConflict = () => {
          $.fn[name] = JQUERY_NO_CONFLICT;
          return plugin.jQueryInterface;
        };
      }
    });
  };

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): dom/data.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */

  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */
  const elementMap = new Map();
  var Data = {
    set(element, key, instance) {
      if (!elementMap.has(element)) {
        elementMap.set(element, new Map());
      }

      const instanceMap = elementMap.get(element); // make it clear we only want one instance per element
      // can be removed later when multiple key/instances are fine to be used

      if (!instanceMap.has(key) && instanceMap.size !== 0) {
        // eslint-disable-next-line no-console
        console.error(`Bootstrap doesn't allow more than one instance per element. Bound instance: ${Array.from(instanceMap.keys())[0]}.`);
        return;
      }

      instanceMap.set(key, instance);
    },

    get(element, key) {
      if (elementMap.has(element)) {
        return elementMap.get(element).get(key) || null;
      }

      return null;
    },

    remove(element, key) {
      if (!elementMap.has(element)) {
        return;
      }

      const instanceMap = elementMap.get(element);
      instanceMap.delete(key); // free up element references if there are no instances left for an element

      if (instanceMap.size === 0) {
        elementMap.delete(element);
      }
    }

  };

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): dom/event-handler.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const namespaceRegex = /[^.]*(?=\..*)\.|.*/;
  const stripNameRegex = /\..*/;
  const stripUidRegex = /::\d+$/;
  const eventRegistry = {}; // Events storage

  let uidEvent = 1;
  const customEvents = {
    mouseenter: 'mouseover',
    mouseleave: 'mouseout'
  };
  const nativeEvents = new Set(['click', 'dblclick', 'mouseup', 'mousedown', 'contextmenu', 'mousewheel', 'DOMMouseScroll', 'mouseover', 'mouseout', 'mousemove', 'selectstart', 'selectend', 'keydown', 'keypress', 'keyup', 'orientationchange', 'touchstart', 'touchmove', 'touchend', 'touchcancel', 'pointerdown', 'pointermove', 'pointerup', 'pointerleave', 'pointercancel', 'gesturestart', 'gesturechange', 'gestureend', 'focus', 'blur', 'change', 'reset', 'select', 'submit', 'focusin', 'focusout', 'load', 'unload', 'beforeunload', 'resize', 'move', 'DOMContentLoaded', 'readystatechange', 'error', 'abort', 'scroll']);
  /**
   * ------------------------------------------------------------------------
   * Private methods
   * ------------------------------------------------------------------------
   */

  function getUidEvent(element, uid) {
    return uid && `${uid}::${uidEvent++}` || element.uidEvent || uidEvent++;
  }

  function getEvent(element) {
    const uid = getUidEvent(element);
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
      const domElements = element.querySelectorAll(selector);

      for (let {
        target
      } = event; target && target !== this; target = target.parentNode) {
        for (let i = domElements.length; i--;) {
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

  function findHandler(events, handler, delegationSelector = null) {
    const uidEventList = Object.keys(events);

    for (let i = 0, len = uidEventList.length; i < len; i++) {
      const event = events[uidEventList[i]];

      if (event.originalHandler === handler && event.delegationSelector === delegationSelector) {
        return event;
      }
    }

    return null;
  }

  function normalizeParams(originalTypeEvent, handler, delegationFn) {
    const delegation = typeof handler === 'string';
    const originalHandler = delegation ? delegationFn : handler; // allow to get the native events from namespaced events ('click.bs.button' --> 'click')

    let typeEvent = originalTypeEvent.replace(stripNameRegex, '');
    const custom = customEvents[typeEvent];

    if (custom) {
      typeEvent = custom;
    }

    const isNative = nativeEvents.has(typeEvent);

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

    const [delegation, originalHandler, typeEvent] = normalizeParams(originalTypeEvent, handler, delegationFn);
    const events = getEvent(element);
    const handlers = events[typeEvent] || (events[typeEvent] = {});
    const previousFn = findHandler(handlers, originalHandler, delegation ? handler : null);

    if (previousFn) {
      previousFn.oneOff = previousFn.oneOff && oneOff;
      return;
    }

    const uid = getUidEvent(originalHandler, originalTypeEvent.replace(namespaceRegex, ''));
    const fn = delegation ? bootstrapDelegationHandler(element, handler, delegationFn) : bootstrapHandler(element, handler);
    fn.delegationSelector = delegation ? handler : null;
    fn.originalHandler = originalHandler;
    fn.oneOff = oneOff;
    fn.uidEvent = uid;
    handlers[uid] = fn;
    element.addEventListener(typeEvent, fn, delegation);
  }

  function removeHandler(element, events, typeEvent, handler, delegationSelector) {
    const fn = findHandler(events[typeEvent], handler, delegationSelector);

    if (!fn) {
      return;
    }

    element.removeEventListener(typeEvent, fn, Boolean(delegationSelector));
    delete events[typeEvent][fn.uidEvent];
  }

  function removeNamespacedHandlers(element, events, typeEvent, namespace) {
    const storeElementEvent = events[typeEvent] || {};
    Object.keys(storeElementEvent).forEach(handlerKey => {
      if (handlerKey.includes(namespace)) {
        const event = storeElementEvent[handlerKey];
        removeHandler(element, events, typeEvent, event.originalHandler, event.delegationSelector);
      }
    });
  }

  const EventHandler = {
    on(element, event, handler, delegationFn) {
      addHandler(element, event, handler, delegationFn, false);
    },

    one(element, event, handler, delegationFn) {
      addHandler(element, event, handler, delegationFn, true);
    },

    off(element, originalTypeEvent, handler, delegationFn) {
      if (typeof originalTypeEvent !== 'string' || !element) {
        return;
      }

      const [delegation, originalHandler, typeEvent] = normalizeParams(originalTypeEvent, handler, delegationFn);
      const inNamespace = typeEvent !== originalTypeEvent;
      const events = getEvent(element);
      const isNamespace = originalTypeEvent.startsWith('.');

      if (typeof originalHandler !== 'undefined') {
        // Simplest case: handler is passed, remove that listener ONLY.
        if (!events || !events[typeEvent]) {
          return;
        }

        removeHandler(element, events, typeEvent, originalHandler, delegation ? handler : null);
        return;
      }

      if (isNamespace) {
        Object.keys(events).forEach(elementEvent => {
          removeNamespacedHandlers(element, events, elementEvent, originalTypeEvent.slice(1));
        });
      }

      const storeElementEvent = events[typeEvent] || {};
      Object.keys(storeElementEvent).forEach(keyHandlers => {
        const handlerKey = keyHandlers.replace(stripUidRegex, '');

        if (!inNamespace || originalTypeEvent.includes(handlerKey)) {
          const event = storeElementEvent[keyHandlers];
          removeHandler(element, events, typeEvent, event.originalHandler, event.delegationSelector);
        }
      });
    },

    trigger(element, event, args) {
      if (typeof event !== 'string' || !element) {
        return null;
      }

      const $ = getjQuery();
      const typeEvent = event.replace(stripNameRegex, '');
      const inNamespace = event !== typeEvent;
      const isNative = nativeEvents.has(typeEvent);
      let jQueryEvent;
      let bubbles = true;
      let nativeDispatch = true;
      let defaultPrevented = false;
      let evt = null;

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
          bubbles,
          cancelable: true
        });
      } // merge custom information in our event


      if (typeof args !== 'undefined') {
        Object.keys(args).forEach(key => {
          Object.defineProperty(evt, key, {
            get() {
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
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): base-component.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const VERSION = '5.0.0-beta3';

  class BaseComponent {
    constructor(element) {
      element = typeof element === 'string' ? document.querySelector(element) : element;

      if (!element) {
        return;
      }

      this._element = element;
      Data.set(this._element, this.constructor.DATA_KEY, this);
    }

    dispose() {
      Data.remove(this._element, this.constructor.DATA_KEY);
      this._element = null;
    }
    /** Static */


    static getInstance(element) {
      return Data.get(element, this.DATA_KEY);
    }

    static get VERSION() {
      return VERSION;
    }

  }

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): alert.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$b = 'alert';
  const DATA_KEY$b = 'bs.alert';
  const EVENT_KEY$b = `.${DATA_KEY$b}`;
  const DATA_API_KEY$8 = '.data-api';
  const SELECTOR_DISMISS = '[data-bs-dismiss="alert"]';
  const EVENT_CLOSE = `close${EVENT_KEY$b}`;
  const EVENT_CLOSED = `closed${EVENT_KEY$b}`;
  const EVENT_CLICK_DATA_API$7 = `click${EVENT_KEY$b}${DATA_API_KEY$8}`;
  const CLASS_NAME_ALERT = 'alert';
  const CLASS_NAME_FADE$5 = 'fade';
  const CLASS_NAME_SHOW$8 = 'show';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Alert extends BaseComponent {
    // Getters
    static get DATA_KEY() {
      return DATA_KEY$b;
    } // Public


    close(element) {
      const rootElement = element ? this._getRootElement(element) : this._element;

      const customEvent = this._triggerCloseEvent(rootElement);

      if (customEvent === null || customEvent.defaultPrevented) {
        return;
      }

      this._removeElement(rootElement);
    } // Private


    _getRootElement(element) {
      return getElementFromSelector(element) || element.closest(`.${CLASS_NAME_ALERT}`);
    }

    _triggerCloseEvent(element) {
      return EventHandler.trigger(element, EVENT_CLOSE);
    }

    _removeElement(element) {
      element.classList.remove(CLASS_NAME_SHOW$8);

      if (!element.classList.contains(CLASS_NAME_FADE$5)) {
        this._destroyElement(element);

        return;
      }

      const transitionDuration = getTransitionDurationFromElement(element);
      EventHandler.one(element, 'transitionend', () => this._destroyElement(element));
      emulateTransitionEnd(element, transitionDuration);
    }

    _destroyElement(element) {
      if (element.parentNode) {
        element.parentNode.removeChild(element);
      }

      EventHandler.trigger(element, EVENT_CLOSED);
    } // Static


    static jQueryInterface(config) {
      return this.each(function () {
        let data = Data.get(this, DATA_KEY$b);

        if (!data) {
          data = new Alert(this);
        }

        if (config === 'close') {
          data[config](this);
        }
      });
    }

    static handleDismiss(alertInstance) {
      return function (event) {
        if (event) {
          event.preventDefault();
        }

        alertInstance.close(this);
      };
    }

  }
  /**
   * ------------------------------------------------------------------------
   * Data Api implementation
   * ------------------------------------------------------------------------
   */


  EventHandler.on(document, EVENT_CLICK_DATA_API$7, SELECTOR_DISMISS, Alert.handleDismiss(new Alert()));
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .Alert to jQuery only if jQuery is present
   */

  defineJQueryPlugin(NAME$b, Alert);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): button.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$a = 'button';
  const DATA_KEY$a = 'bs.button';
  const EVENT_KEY$a = `.${DATA_KEY$a}`;
  const DATA_API_KEY$7 = '.data-api';
  const CLASS_NAME_ACTIVE$3 = 'active';
  const SELECTOR_DATA_TOGGLE$5 = '[data-bs-toggle="button"]';
  const EVENT_CLICK_DATA_API$6 = `click${EVENT_KEY$a}${DATA_API_KEY$7}`;
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Button extends BaseComponent {
    // Getters
    static get DATA_KEY() {
      return DATA_KEY$a;
    } // Public


    toggle() {
      // Toggle class and sync the `aria-pressed` attribute with the return value of the `.toggle()` method
      this._element.setAttribute('aria-pressed', this._element.classList.toggle(CLASS_NAME_ACTIVE$3));
    } // Static


    static jQueryInterface(config) {
      return this.each(function () {
        let data = Data.get(this, DATA_KEY$a);

        if (!data) {
          data = new Button(this);
        }

        if (config === 'toggle') {
          data[config]();
        }
      });
    }

  }
  /**
   * ------------------------------------------------------------------------
   * Data Api implementation
   * ------------------------------------------------------------------------
   */


  EventHandler.on(document, EVENT_CLICK_DATA_API$6, SELECTOR_DATA_TOGGLE$5, event => {
    event.preventDefault();
    const button = event.target.closest(SELECTOR_DATA_TOGGLE$5);
    let data = Data.get(button, DATA_KEY$a);

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

  defineJQueryPlugin(NAME$a, Button);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): dom/manipulator.js
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
    return key.replace(/[A-Z]/g, chr => `-${chr.toLowerCase()}`);
  }

  const Manipulator = {
    setDataAttribute(element, key, value) {
      element.setAttribute(`data-bs-${normalizeDataKey(key)}`, value);
    },

    removeDataAttribute(element, key) {
      element.removeAttribute(`data-bs-${normalizeDataKey(key)}`);
    },

    getDataAttributes(element) {
      if (!element) {
        return {};
      }

      const attributes = {};
      Object.keys(element.dataset).filter(key => key.startsWith('bs')).forEach(key => {
        let pureKey = key.replace(/^bs/, '');
        pureKey = pureKey.charAt(0).toLowerCase() + pureKey.slice(1, pureKey.length);
        attributes[pureKey] = normalizeData(element.dataset[key]);
      });
      return attributes;
    },

    getDataAttribute(element, key) {
      return normalizeData(element.getAttribute(`data-bs-${normalizeDataKey(key)}`));
    },

    offset(element) {
      const rect = element.getBoundingClientRect();
      return {
        top: rect.top + document.body.scrollTop,
        left: rect.left + document.body.scrollLeft
      };
    },

    position(element) {
      return {
        top: element.offsetTop,
        left: element.offsetLeft
      };
    }

  };

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): dom/selector-engine.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */

  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */
  const NODE_TEXT = 3;
  const SelectorEngine = {
    find(selector, element = document.documentElement) {
      return [].concat(...Element.prototype.querySelectorAll.call(element, selector));
    },

    findOne(selector, element = document.documentElement) {
      return Element.prototype.querySelector.call(element, selector);
    },

    children(element, selector) {
      return [].concat(...element.children).filter(child => child.matches(selector));
    },

    parents(element, selector) {
      const parents = [];
      let ancestor = element.parentNode;

      while (ancestor && ancestor.nodeType === Node.ELEMENT_NODE && ancestor.nodeType !== NODE_TEXT) {
        if (ancestor.matches(selector)) {
          parents.push(ancestor);
        }

        ancestor = ancestor.parentNode;
      }

      return parents;
    },

    prev(element, selector) {
      let previous = element.previousElementSibling;

      while (previous) {
        if (previous.matches(selector)) {
          return [previous];
        }

        previous = previous.previousElementSibling;
      }

      return [];
    },

    next(element, selector) {
      let next = element.nextElementSibling;

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
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): carousel.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$9 = 'carousel';
  const DATA_KEY$9 = 'bs.carousel';
  const EVENT_KEY$9 = `.${DATA_KEY$9}`;
  const DATA_API_KEY$6 = '.data-api';
  const ARROW_LEFT_KEY = 'ArrowLeft';
  const ARROW_RIGHT_KEY = 'ArrowRight';
  const TOUCHEVENT_COMPAT_WAIT = 500; // Time for mouse compat events to fire after touch

  const SWIPE_THRESHOLD = 40;
  const Default$8 = {
    interval: 5000,
    keyboard: true,
    slide: false,
    pause: 'hover',
    wrap: true,
    touch: true
  };
  const DefaultType$8 = {
    interval: '(number|boolean)',
    keyboard: 'boolean',
    slide: '(boolean|string)',
    pause: '(string|boolean)',
    wrap: 'boolean',
    touch: 'boolean'
  };
  const ORDER_NEXT = 'next';
  const ORDER_PREV = 'prev';
  const DIRECTION_LEFT = 'left';
  const DIRECTION_RIGHT = 'right';
  const EVENT_SLIDE = `slide${EVENT_KEY$9}`;
  const EVENT_SLID = `slid${EVENT_KEY$9}`;
  const EVENT_KEYDOWN = `keydown${EVENT_KEY$9}`;
  const EVENT_MOUSEENTER = `mouseenter${EVENT_KEY$9}`;
  const EVENT_MOUSELEAVE = `mouseleave${EVENT_KEY$9}`;
  const EVENT_TOUCHSTART = `touchstart${EVENT_KEY$9}`;
  const EVENT_TOUCHMOVE = `touchmove${EVENT_KEY$9}`;
  const EVENT_TOUCHEND = `touchend${EVENT_KEY$9}`;
  const EVENT_POINTERDOWN = `pointerdown${EVENT_KEY$9}`;
  const EVENT_POINTERUP = `pointerup${EVENT_KEY$9}`;
  const EVENT_DRAG_START = `dragstart${EVENT_KEY$9}`;
  const EVENT_LOAD_DATA_API$2 = `load${EVENT_KEY$9}${DATA_API_KEY$6}`;
  const EVENT_CLICK_DATA_API$5 = `click${EVENT_KEY$9}${DATA_API_KEY$6}`;
  const CLASS_NAME_CAROUSEL = 'carousel';
  const CLASS_NAME_ACTIVE$2 = 'active';
  const CLASS_NAME_SLIDE = 'slide';
  const CLASS_NAME_END = 'carousel-item-end';
  const CLASS_NAME_START = 'carousel-item-start';
  const CLASS_NAME_NEXT = 'carousel-item-next';
  const CLASS_NAME_PREV = 'carousel-item-prev';
  const CLASS_NAME_POINTER_EVENT = 'pointer-event';
  const SELECTOR_ACTIVE$1 = '.active';
  const SELECTOR_ACTIVE_ITEM = '.active.carousel-item';
  const SELECTOR_ITEM = '.carousel-item';
  const SELECTOR_ITEM_IMG = '.carousel-item img';
  const SELECTOR_NEXT_PREV = '.carousel-item-next, .carousel-item-prev';
  const SELECTOR_INDICATORS = '.carousel-indicators';
  const SELECTOR_INDICATOR = '[data-bs-target]';
  const SELECTOR_DATA_SLIDE = '[data-bs-slide], [data-bs-slide-to]';
  const SELECTOR_DATA_RIDE = '[data-bs-ride="carousel"]';
  const POINTER_TYPE_TOUCH = 'touch';
  const POINTER_TYPE_PEN = 'pen';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Carousel extends BaseComponent {
    constructor(element, config) {
      super(element);
      this._items = null;
      this._interval = null;
      this._activeElement = null;
      this._isPaused = false;
      this._isSliding = false;
      this.touchTimeout = null;
      this.touchStartX = 0;
      this.touchDeltaX = 0;
      this._config = this._getConfig(config);
      this._indicatorsElement = SelectorEngine.findOne(SELECTOR_INDICATORS, this._element);
      this._touchSupported = 'ontouchstart' in document.documentElement || navigator.maxTouchPoints > 0;
      this._pointerEvent = Boolean(window.PointerEvent);

      this._addEventListeners();
    } // Getters


    static get Default() {
      return Default$8;
    }

    static get DATA_KEY() {
      return DATA_KEY$9;
    } // Public


    next() {
      if (!this._isSliding) {
        this._slide(ORDER_NEXT);
      }
    }

    nextWhenVisible() {
      // Don't call next when the page isn't visible
      // or the carousel or its parent isn't visible
      if (!document.hidden && isVisible(this._element)) {
        this.next();
      }
    }

    prev() {
      if (!this._isSliding) {
        this._slide(ORDER_PREV);
      }
    }

    pause(event) {
      if (!event) {
        this._isPaused = true;
      }

      if (SelectorEngine.findOne(SELECTOR_NEXT_PREV, this._element)) {
        triggerTransitionEnd(this._element);
        this.cycle(true);
      }

      clearInterval(this._interval);
      this._interval = null;
    }

    cycle(event) {
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
    }

    to(index) {
      this._activeElement = SelectorEngine.findOne(SELECTOR_ACTIVE_ITEM, this._element);

      const activeIndex = this._getItemIndex(this._activeElement);

      if (index > this._items.length - 1 || index < 0) {
        return;
      }

      if (this._isSliding) {
        EventHandler.one(this._element, EVENT_SLID, () => this.to(index));
        return;
      }

      if (activeIndex === index) {
        this.pause();
        this.cycle();
        return;
      }

      const order = index > activeIndex ? ORDER_NEXT : ORDER_PREV;

      this._slide(order, this._items[index]);
    }

    dispose() {
      EventHandler.off(this._element, EVENT_KEY$9);
      this._items = null;
      this._config = null;
      this._interval = null;
      this._isPaused = null;
      this._isSliding = null;
      this._activeElement = null;
      this._indicatorsElement = null;
      super.dispose();
    } // Private


    _getConfig(config) {
      config = { ...Default$8,
        ...config
      };
      typeCheckConfig(NAME$9, config, DefaultType$8);
      return config;
    }

    _handleSwipe() {
      const absDeltax = Math.abs(this.touchDeltaX);

      if (absDeltax <= SWIPE_THRESHOLD) {
        return;
      }

      const direction = absDeltax / this.touchDeltaX;
      this.touchDeltaX = 0;

      if (!direction) {
        return;
      }

      this._slide(direction > 0 ? DIRECTION_RIGHT : DIRECTION_LEFT);
    }

    _addEventListeners() {
      if (this._config.keyboard) {
        EventHandler.on(this._element, EVENT_KEYDOWN, event => this._keydown(event));
      }

      if (this._config.pause === 'hover') {
        EventHandler.on(this._element, EVENT_MOUSEENTER, event => this.pause(event));
        EventHandler.on(this._element, EVENT_MOUSELEAVE, event => this.cycle(event));
      }

      if (this._config.touch && this._touchSupported) {
        this._addTouchEventListeners();
      }
    }

    _addTouchEventListeners() {
      const start = event => {
        if (this._pointerEvent && (event.pointerType === POINTER_TYPE_PEN || event.pointerType === POINTER_TYPE_TOUCH)) {
          this.touchStartX = event.clientX;
        } else if (!this._pointerEvent) {
          this.touchStartX = event.touches[0].clientX;
        }
      };

      const move = event => {
        // ensure swiping with one touch and not pinching
        this.touchDeltaX = event.touches && event.touches.length > 1 ? 0 : event.touches[0].clientX - this.touchStartX;
      };

      const end = event => {
        if (this._pointerEvent && (event.pointerType === POINTER_TYPE_PEN || event.pointerType === POINTER_TYPE_TOUCH)) {
          this.touchDeltaX = event.clientX - this.touchStartX;
        }

        this._handleSwipe();

        if (this._config.pause === 'hover') {
          // If it's a touch-enabled device, mouseenter/leave are fired as
          // part of the mouse compatibility events on first tap - the carousel
          // would stop cycling until user tapped out of it;
          // here, we listen for touchend, explicitly pause the carousel
          // (as if it's the second time we tap on it, mouseenter compat event
          // is NOT fired) and after a timeout (to allow for mouse compatibility
          // events to fire) we explicitly restart cycling
          this.pause();

          if (this.touchTimeout) {
            clearTimeout(this.touchTimeout);
          }

          this.touchTimeout = setTimeout(event => this.cycle(event), TOUCHEVENT_COMPAT_WAIT + this._config.interval);
        }
      };

      SelectorEngine.find(SELECTOR_ITEM_IMG, this._element).forEach(itemImg => {
        EventHandler.on(itemImg, EVENT_DRAG_START, e => e.preventDefault());
      });

      if (this._pointerEvent) {
        EventHandler.on(this._element, EVENT_POINTERDOWN, event => start(event));
        EventHandler.on(this._element, EVENT_POINTERUP, event => end(event));

        this._element.classList.add(CLASS_NAME_POINTER_EVENT);
      } else {
        EventHandler.on(this._element, EVENT_TOUCHSTART, event => start(event));
        EventHandler.on(this._element, EVENT_TOUCHMOVE, event => move(event));
        EventHandler.on(this._element, EVENT_TOUCHEND, event => end(event));
      }
    }

    _keydown(event) {
      if (/input|textarea/i.test(event.target.tagName)) {
        return;
      }

      if (event.key === ARROW_LEFT_KEY) {
        event.preventDefault();

        this._slide(DIRECTION_LEFT);
      } else if (event.key === ARROW_RIGHT_KEY) {
        event.preventDefault();

        this._slide(DIRECTION_RIGHT);
      }
    }

    _getItemIndex(element) {
      this._items = element && element.parentNode ? SelectorEngine.find(SELECTOR_ITEM, element.parentNode) : [];
      return this._items.indexOf(element);
    }

    _getItemByOrder(order, activeElement) {
      const isNext = order === ORDER_NEXT;
      const isPrev = order === ORDER_PREV;

      const activeIndex = this._getItemIndex(activeElement);

      const lastItemIndex = this._items.length - 1;
      const isGoingToWrap = isPrev && activeIndex === 0 || isNext && activeIndex === lastItemIndex;

      if (isGoingToWrap && !this._config.wrap) {
        return activeElement;
      }

      const delta = isPrev ? -1 : 1;
      const itemIndex = (activeIndex + delta) % this._items.length;
      return itemIndex === -1 ? this._items[this._items.length - 1] : this._items[itemIndex];
    }

    _triggerSlideEvent(relatedTarget, eventDirectionName) {
      const targetIndex = this._getItemIndex(relatedTarget);

      const fromIndex = this._getItemIndex(SelectorEngine.findOne(SELECTOR_ACTIVE_ITEM, this._element));

      return EventHandler.trigger(this._element, EVENT_SLIDE, {
        relatedTarget,
        direction: eventDirectionName,
        from: fromIndex,
        to: targetIndex
      });
    }

    _setActiveIndicatorElement(element) {
      if (this._indicatorsElement) {
        const activeIndicator = SelectorEngine.findOne(SELECTOR_ACTIVE$1, this._indicatorsElement);
        activeIndicator.classList.remove(CLASS_NAME_ACTIVE$2);
        activeIndicator.removeAttribute('aria-current');
        const indicators = SelectorEngine.find(SELECTOR_INDICATOR, this._indicatorsElement);

        for (let i = 0; i < indicators.length; i++) {
          if (Number.parseInt(indicators[i].getAttribute('data-bs-slide-to'), 10) === this._getItemIndex(element)) {
            indicators[i].classList.add(CLASS_NAME_ACTIVE$2);
            indicators[i].setAttribute('aria-current', 'true');
            break;
          }
        }
      }
    }

    _updateInterval() {
      const element = this._activeElement || SelectorEngine.findOne(SELECTOR_ACTIVE_ITEM, this._element);

      if (!element) {
        return;
      }

      const elementInterval = Number.parseInt(element.getAttribute('data-bs-interval'), 10);

      if (elementInterval) {
        this._config.defaultInterval = this._config.defaultInterval || this._config.interval;
        this._config.interval = elementInterval;
      } else {
        this._config.interval = this._config.defaultInterval || this._config.interval;
      }
    }

    _slide(directionOrOrder, element) {
      const order = this._directionToOrder(directionOrOrder);

      const activeElement = SelectorEngine.findOne(SELECTOR_ACTIVE_ITEM, this._element);

      const activeElementIndex = this._getItemIndex(activeElement);

      const nextElement = element || this._getItemByOrder(order, activeElement);

      const nextElementIndex = this._getItemIndex(nextElement);

      const isCycling = Boolean(this._interval);
      const isNext = order === ORDER_NEXT;
      const directionalClassName = isNext ? CLASS_NAME_START : CLASS_NAME_END;
      const orderClassName = isNext ? CLASS_NAME_NEXT : CLASS_NAME_PREV;

      const eventDirectionName = this._orderToDirection(order);

      if (nextElement && nextElement.classList.contains(CLASS_NAME_ACTIVE$2)) {
        this._isSliding = false;
        return;
      }

      const slideEvent = this._triggerSlideEvent(nextElement, eventDirectionName);

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
        const transitionDuration = getTransitionDurationFromElement(activeElement);
        EventHandler.one(activeElement, 'transitionend', () => {
          nextElement.classList.remove(directionalClassName, orderClassName);
          nextElement.classList.add(CLASS_NAME_ACTIVE$2);
          activeElement.classList.remove(CLASS_NAME_ACTIVE$2, orderClassName, directionalClassName);
          this._isSliding = false;
          setTimeout(() => {
            EventHandler.trigger(this._element, EVENT_SLID, {
              relatedTarget: nextElement,
              direction: eventDirectionName,
              from: activeElementIndex,
              to: nextElementIndex
            });
          }, 0);
        });
        emulateTransitionEnd(activeElement, transitionDuration);
      } else {
        activeElement.classList.remove(CLASS_NAME_ACTIVE$2);
        nextElement.classList.add(CLASS_NAME_ACTIVE$2);
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
    }

    _directionToOrder(direction) {
      if (![DIRECTION_RIGHT, DIRECTION_LEFT].includes(direction)) {
        return direction;
      }

      if (isRTL()) {
        return direction === DIRECTION_RIGHT ? ORDER_PREV : ORDER_NEXT;
      }

      return direction === DIRECTION_RIGHT ? ORDER_NEXT : ORDER_PREV;
    }

    _orderToDirection(order) {
      if (![ORDER_NEXT, ORDER_PREV].includes(order)) {
        return order;
      }

      if (isRTL()) {
        return order === ORDER_NEXT ? DIRECTION_LEFT : DIRECTION_RIGHT;
      }

      return order === ORDER_NEXT ? DIRECTION_RIGHT : DIRECTION_LEFT;
    } // Static


    static carouselInterface(element, config) {
      let data = Data.get(element, DATA_KEY$9);
      let _config = { ...Default$8,
        ...Manipulator.getDataAttributes(element)
      };

      if (typeof config === 'object') {
        _config = { ..._config,
          ...config
        };
      }

      const action = typeof config === 'string' ? config : _config.slide;

      if (!data) {
        data = new Carousel(element, _config);
      }

      if (typeof config === 'number') {
        data.to(config);
      } else if (typeof action === 'string') {
        if (typeof data[action] === 'undefined') {
          throw new TypeError(`No method named "${action}"`);
        }

        data[action]();
      } else if (_config.interval && _config.ride) {
        data.pause();
        data.cycle();
      }
    }

    static jQueryInterface(config) {
      return this.each(function () {
        Carousel.carouselInterface(this, config);
      });
    }

    static dataApiClickHandler(event) {
      const target = getElementFromSelector(this);

      if (!target || !target.classList.contains(CLASS_NAME_CAROUSEL)) {
        return;
      }

      const config = { ...Manipulator.getDataAttributes(target),
        ...Manipulator.getDataAttributes(this)
      };
      const slideIndex = this.getAttribute('data-bs-slide-to');

      if (slideIndex) {
        config.interval = false;
      }

      Carousel.carouselInterface(target, config);

      if (slideIndex) {
        Data.get(target, DATA_KEY$9).to(slideIndex);
      }

      event.preventDefault();
    }

  }
  /**
   * ------------------------------------------------------------------------
   * Data Api implementation
   * ------------------------------------------------------------------------
   */


  EventHandler.on(document, EVENT_CLICK_DATA_API$5, SELECTOR_DATA_SLIDE, Carousel.dataApiClickHandler);
  EventHandler.on(window, EVENT_LOAD_DATA_API$2, () => {
    const carousels = SelectorEngine.find(SELECTOR_DATA_RIDE);

    for (let i = 0, len = carousels.length; i < len; i++) {
      Carousel.carouselInterface(carousels[i], Data.get(carousels[i], DATA_KEY$9));
    }
  });
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .Carousel to jQuery only if jQuery is present
   */

  defineJQueryPlugin(NAME$9, Carousel);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): collapse.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$8 = 'collapse';
  const DATA_KEY$8 = 'bs.collapse';
  const EVENT_KEY$8 = `.${DATA_KEY$8}`;
  const DATA_API_KEY$5 = '.data-api';
  const Default$7 = {
    toggle: true,
    parent: ''
  };
  const DefaultType$7 = {
    toggle: 'boolean',
    parent: '(string|element)'
  };
  const EVENT_SHOW$5 = `show${EVENT_KEY$8}`;
  const EVENT_SHOWN$5 = `shown${EVENT_KEY$8}`;
  const EVENT_HIDE$5 = `hide${EVENT_KEY$8}`;
  const EVENT_HIDDEN$5 = `hidden${EVENT_KEY$8}`;
  const EVENT_CLICK_DATA_API$4 = `click${EVENT_KEY$8}${DATA_API_KEY$5}`;
  const CLASS_NAME_SHOW$7 = 'show';
  const CLASS_NAME_COLLAPSE = 'collapse';
  const CLASS_NAME_COLLAPSING = 'collapsing';
  const CLASS_NAME_COLLAPSED = 'collapsed';
  const WIDTH = 'width';
  const HEIGHT = 'height';
  const SELECTOR_ACTIVES = '.show, .collapsing';
  const SELECTOR_DATA_TOGGLE$4 = '[data-bs-toggle="collapse"]';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Collapse$1 extends BaseComponent {
    constructor(element, config) {
      super(element);
      this._isTransitioning = false;
      this._config = this._getConfig(config);
      this._triggerArray = SelectorEngine.find(`${SELECTOR_DATA_TOGGLE$4}[href="#${this._element.id}"],` + `${SELECTOR_DATA_TOGGLE$4}[data-bs-target="#${this._element.id}"]`);
      const toggleList = SelectorEngine.find(SELECTOR_DATA_TOGGLE$4);

      for (let i = 0, len = toggleList.length; i < len; i++) {
        const elem = toggleList[i];
        const selector = getSelectorFromElement(elem);
        const filterElement = SelectorEngine.find(selector).filter(foundElem => foundElem === this._element);

        if (selector !== null && filterElement.length) {
          this._selector = selector;

          this._triggerArray.push(elem);
        }
      }

      this._parent = this._config.parent ? this._getParent() : null;

      if (!this._config.parent) {
        this._addAriaAndCollapsedClass(this._element, this._triggerArray);
      }

      if (this._config.toggle) {
        this.toggle();
      }
    } // Getters


    static get Default() {
      return Default$7;
    }

    static get DATA_KEY() {
      return DATA_KEY$8;
    } // Public


    toggle() {
      if (this._element.classList.contains(CLASS_NAME_SHOW$7)) {
        this.hide();
      } else {
        this.show();
      }
    }

    show() {
      if (this._isTransitioning || this._element.classList.contains(CLASS_NAME_SHOW$7)) {
        return;
      }

      let actives;
      let activesData;

      if (this._parent) {
        actives = SelectorEngine.find(SELECTOR_ACTIVES, this._parent).filter(elem => {
          if (typeof this._config.parent === 'string') {
            return elem.getAttribute('data-bs-parent') === this._config.parent;
          }

          return elem.classList.contains(CLASS_NAME_COLLAPSE);
        });

        if (actives.length === 0) {
          actives = null;
        }
      }

      const container = SelectorEngine.findOne(this._selector);

      if (actives) {
        const tempActiveData = actives.find(elem => container !== elem);
        activesData = tempActiveData ? Data.get(tempActiveData, DATA_KEY$8) : null;

        if (activesData && activesData._isTransitioning) {
          return;
        }
      }

      const startEvent = EventHandler.trigger(this._element, EVENT_SHOW$5);

      if (startEvent.defaultPrevented) {
        return;
      }

      if (actives) {
        actives.forEach(elemActive => {
          if (container !== elemActive) {
            Collapse$1.collapseInterface(elemActive, 'hide');
          }

          if (!activesData) {
            Data.set(elemActive, DATA_KEY$8, null);
          }
        });
      }

      const dimension = this._getDimension();

      this._element.classList.remove(CLASS_NAME_COLLAPSE);

      this._element.classList.add(CLASS_NAME_COLLAPSING);

      this._element.style[dimension] = 0;

      if (this._triggerArray.length) {
        this._triggerArray.forEach(element => {
          element.classList.remove(CLASS_NAME_COLLAPSED);
          element.setAttribute('aria-expanded', true);
        });
      }

      this.setTransitioning(true);

      const complete = () => {
        this._element.classList.remove(CLASS_NAME_COLLAPSING);

        this._element.classList.add(CLASS_NAME_COLLAPSE, CLASS_NAME_SHOW$7);

        this._element.style[dimension] = '';
        this.setTransitioning(false);
        EventHandler.trigger(this._element, EVENT_SHOWN$5);
      };

      const capitalizedDimension = dimension[0].toUpperCase() + dimension.slice(1);
      const scrollSize = `scroll${capitalizedDimension}`;
      const transitionDuration = getTransitionDurationFromElement(this._element);
      EventHandler.one(this._element, 'transitionend', complete);
      emulateTransitionEnd(this._element, transitionDuration);
      this._element.style[dimension] = `${this._element[scrollSize]}px`;
    }

    hide() {
      if (this._isTransitioning || !this._element.classList.contains(CLASS_NAME_SHOW$7)) {
        return;
      }

      const startEvent = EventHandler.trigger(this._element, EVENT_HIDE$5);

      if (startEvent.defaultPrevented) {
        return;
      }

      const dimension = this._getDimension();

      this._element.style[dimension] = `${this._element.getBoundingClientRect()[dimension]}px`;
      reflow(this._element);

      this._element.classList.add(CLASS_NAME_COLLAPSING);

      this._element.classList.remove(CLASS_NAME_COLLAPSE, CLASS_NAME_SHOW$7);

      const triggerArrayLength = this._triggerArray.length;

      if (triggerArrayLength > 0) {
        for (let i = 0; i < triggerArrayLength; i++) {
          const trigger = this._triggerArray[i];
          const elem = getElementFromSelector(trigger);

          if (elem && !elem.classList.contains(CLASS_NAME_SHOW$7)) {
            trigger.classList.add(CLASS_NAME_COLLAPSED);
            trigger.setAttribute('aria-expanded', false);
          }
        }
      }

      this.setTransitioning(true);

      const complete = () => {
        this.setTransitioning(false);

        this._element.classList.remove(CLASS_NAME_COLLAPSING);

        this._element.classList.add(CLASS_NAME_COLLAPSE);

        EventHandler.trigger(this._element, EVENT_HIDDEN$5);
      };

      this._element.style[dimension] = '';
      const transitionDuration = getTransitionDurationFromElement(this._element);
      EventHandler.one(this._element, 'transitionend', complete);
      emulateTransitionEnd(this._element, transitionDuration);
    }

    setTransitioning(isTransitioning) {
      this._isTransitioning = isTransitioning;
    }

    dispose() {
      super.dispose();
      this._config = null;
      this._parent = null;
      this._triggerArray = null;
      this._isTransitioning = null;
    } // Private


    _getConfig(config) {
      config = { ...Default$7,
        ...config
      };
      config.toggle = Boolean(config.toggle); // Coerce string values

      typeCheckConfig(NAME$8, config, DefaultType$7);
      return config;
    }

    _getDimension() {
      return this._element.classList.contains(WIDTH) ? WIDTH : HEIGHT;
    }

    _getParent() {
      let {
        parent
      } = this._config;

      if (isElement(parent)) {
        // it's a jQuery object
        if (typeof parent.jquery !== 'undefined' || typeof parent[0] !== 'undefined') {
          parent = parent[0];
        }
      } else {
        parent = SelectorEngine.findOne(parent);
      }

      const selector = `${SELECTOR_DATA_TOGGLE$4}[data-bs-parent="${parent}"]`;
      SelectorEngine.find(selector, parent).forEach(element => {
        const selected = getElementFromSelector(element);

        this._addAriaAndCollapsedClass(selected, [element]);
      });
      return parent;
    }

    _addAriaAndCollapsedClass(element, triggerArray) {
      if (!element || !triggerArray.length) {
        return;
      }

      const isOpen = element.classList.contains(CLASS_NAME_SHOW$7);
      triggerArray.forEach(elem => {
        if (isOpen) {
          elem.classList.remove(CLASS_NAME_COLLAPSED);
        } else {
          elem.classList.add(CLASS_NAME_COLLAPSED);
        }

        elem.setAttribute('aria-expanded', isOpen);
      });
    } // Static


    static collapseInterface(element, config) {
      let data = Data.get(element, DATA_KEY$8);
      const _config = { ...Default$7,
        ...Manipulator.getDataAttributes(element),
        ...(typeof config === 'object' && config ? config : {})
      };

      if (!data && _config.toggle && typeof config === 'string' && /show|hide/.test(config)) {
        _config.toggle = false;
      }

      if (!data) {
        data = new Collapse$1(element, _config);
      }

      if (typeof config === 'string') {
        if (typeof data[config] === 'undefined') {
          throw new TypeError(`No method named "${config}"`);
        }

        data[config]();
      }
    }

    static jQueryInterface(config) {
      return this.each(function () {
        Collapse$1.collapseInterface(this, config);
      });
    }

  }
  /**
   * ------------------------------------------------------------------------
   * Data Api implementation
   * ------------------------------------------------------------------------
   */


  EventHandler.on(document, EVENT_CLICK_DATA_API$4, SELECTOR_DATA_TOGGLE$4, function (event) {
    // preventDefault only for <a> elements (which change the URL) not inside the collapsible element
    if (event.target.tagName === 'A' || event.delegateTarget && event.delegateTarget.tagName === 'A') {
      event.preventDefault();
    }

    const triggerData = Manipulator.getDataAttributes(this);
    const selector = getSelectorFromElement(this);
    const selectorElements = SelectorEngine.find(selector);
    selectorElements.forEach(element => {
      const data = Data.get(element, DATA_KEY$8);
      let config;

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

      Collapse$1.collapseInterface(element, config);
    });
  });
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .Collapse to jQuery only if jQuery is present
   */

  defineJQueryPlugin(NAME$8, Collapse$1);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): dropdown.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$7 = 'dropdown';
  const DATA_KEY$7 = 'bs.dropdown';
  const EVENT_KEY$7 = `.${DATA_KEY$7}`;
  const DATA_API_KEY$4 = '.data-api';
  const ESCAPE_KEY$2 = 'Escape';
  const SPACE_KEY = 'Space';
  const TAB_KEY = 'Tab';
  const ARROW_UP_KEY = 'ArrowUp';
  const ARROW_DOWN_KEY = 'ArrowDown';
  const RIGHT_MOUSE_BUTTON = 2; // MouseEvent.button value for the secondary button, usually the right button

  const REGEXP_KEYDOWN = new RegExp(`${ARROW_UP_KEY}|${ARROW_DOWN_KEY}|${ESCAPE_KEY$2}`);
  const EVENT_HIDE$4 = `hide${EVENT_KEY$7}`;
  const EVENT_HIDDEN$4 = `hidden${EVENT_KEY$7}`;
  const EVENT_SHOW$4 = `show${EVENT_KEY$7}`;
  const EVENT_SHOWN$4 = `shown${EVENT_KEY$7}`;
  const EVENT_CLICK = `click${EVENT_KEY$7}`;
  const EVENT_CLICK_DATA_API$3 = `click${EVENT_KEY$7}${DATA_API_KEY$4}`;
  const EVENT_KEYDOWN_DATA_API = `keydown${EVENT_KEY$7}${DATA_API_KEY$4}`;
  const EVENT_KEYUP_DATA_API = `keyup${EVENT_KEY$7}${DATA_API_KEY$4}`;
  const CLASS_NAME_DISABLED = 'disabled';
  const CLASS_NAME_SHOW$6 = 'show';
  const CLASS_NAME_DROPUP = 'dropup';
  const CLASS_NAME_DROPEND = 'dropend';
  const CLASS_NAME_DROPSTART = 'dropstart';
  const CLASS_NAME_NAVBAR = 'navbar';
  const SELECTOR_DATA_TOGGLE$3 = '[data-bs-toggle="dropdown"]';
  const SELECTOR_MENU = '.dropdown-menu';
  const SELECTOR_NAVBAR_NAV = '.navbar-nav';
  const SELECTOR_VISIBLE_ITEMS = '.dropdown-menu .dropdown-item:not(.disabled):not(:disabled)';
  const PLACEMENT_TOP = isRTL() ? 'top-end' : 'top-start';
  const PLACEMENT_TOPEND = isRTL() ? 'top-start' : 'top-end';
  const PLACEMENT_BOTTOM = isRTL() ? 'bottom-end' : 'bottom-start';
  const PLACEMENT_BOTTOMEND = isRTL() ? 'bottom-start' : 'bottom-end';
  const PLACEMENT_RIGHT = isRTL() ? 'left-start' : 'right-start';
  const PLACEMENT_LEFT = isRTL() ? 'right-start' : 'left-start';
  const Default$6 = {
    offset: [0, 2],
    boundary: 'clippingParents',
    reference: 'toggle',
    display: 'dynamic',
    popperConfig: null
  };
  const DefaultType$6 = {
    offset: '(array|string|function)',
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

  class Dropdown extends BaseComponent {
    constructor(element, config) {
      super(element);
      this._popper = null;
      this._config = this._getConfig(config);
      this._menu = this._getMenuElement();
      this._inNavbar = this._detectNavbar();

      this._addEventListeners();
    } // Getters


    static get Default() {
      return Default$6;
    }

    static get DefaultType() {
      return DefaultType$6;
    }

    static get DATA_KEY() {
      return DATA_KEY$7;
    } // Public


    toggle() {
      if (this._element.disabled || this._element.classList.contains(CLASS_NAME_DISABLED)) {
        return;
      }

      const isActive = this._element.classList.contains(CLASS_NAME_SHOW$6);

      Dropdown.clearMenus();

      if (isActive) {
        return;
      }

      this.show();
    }

    show() {
      if (this._element.disabled || this._element.classList.contains(CLASS_NAME_DISABLED) || this._menu.classList.contains(CLASS_NAME_SHOW$6)) {
        return;
      }

      const parent = Dropdown.getParentFromElement(this._element);
      const relatedTarget = {
        relatedTarget: this._element
      };
      const showEvent = EventHandler.trigger(this._element, EVENT_SHOW$4, relatedTarget);

      if (showEvent.defaultPrevented) {
        return;
      } // Totally disable Popper for Dropdowns in Navbar


      if (this._inNavbar) {
        Manipulator.setDataAttribute(this._menu, 'popper', 'none');
      } else {
        if (typeof Popper === 'undefined') {
          throw new TypeError('Bootstrap\'s dropdowns require Popper (https://popper.js.org)');
        }

        let referenceElement = this._element;

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

        const popperConfig = this._getPopperConfig();

        const isDisplayStatic = popperConfig.modifiers.find(modifier => modifier.name === 'applyStyles' && modifier.enabled === false);
        this._popper = createPopper(referenceElement, this._menu, popperConfig);

        if (isDisplayStatic) {
          Manipulator.setDataAttribute(this._menu, 'popper', 'static');
        }
      } // If this is a touch-enabled device we add extra
      // empty mouseover listeners to the body's immediate children;
      // only needed because of broken event delegation on iOS
      // https://www.quirksmode.org/blog/archives/2014/02/mouse_event_bub.html


      if ('ontouchstart' in document.documentElement && !parent.closest(SELECTOR_NAVBAR_NAV)) {
        [].concat(...document.body.children).forEach(elem => EventHandler.on(elem, 'mouseover', null, noop()));
      }

      this._element.focus();

      this._element.setAttribute('aria-expanded', true);

      this._menu.classList.toggle(CLASS_NAME_SHOW$6);

      this._element.classList.toggle(CLASS_NAME_SHOW$6);

      EventHandler.trigger(this._element, EVENT_SHOWN$4, relatedTarget);
    }

    hide() {
      if (this._element.disabled || this._element.classList.contains(CLASS_NAME_DISABLED) || !this._menu.classList.contains(CLASS_NAME_SHOW$6)) {
        return;
      }

      const relatedTarget = {
        relatedTarget: this._element
      };
      const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE$4, relatedTarget);

      if (hideEvent.defaultPrevented) {
        return;
      }

      if (this._popper) {
        this._popper.destroy();
      }

      this._menu.classList.toggle(CLASS_NAME_SHOW$6);

      this._element.classList.toggle(CLASS_NAME_SHOW$6);

      Manipulator.removeDataAttribute(this._menu, 'popper');
      EventHandler.trigger(this._element, EVENT_HIDDEN$4, relatedTarget);
    }

    dispose() {
      EventHandler.off(this._element, EVENT_KEY$7);
      this._menu = null;

      if (this._popper) {
        this._popper.destroy();

        this._popper = null;
      }

      super.dispose();
    }

    update() {
      this._inNavbar = this._detectNavbar();

      if (this._popper) {
        this._popper.update();
      }
    } // Private


    _addEventListeners() {
      EventHandler.on(this._element, EVENT_CLICK, event => {
        event.preventDefault();
        this.toggle();
      });
    }

    _getConfig(config) {
      config = { ...this.constructor.Default,
        ...Manipulator.getDataAttributes(this._element),
        ...config
      };
      typeCheckConfig(NAME$7, config, this.constructor.DefaultType);

      if (typeof config.reference === 'object' && !isElement(config.reference) && typeof config.reference.getBoundingClientRect !== 'function') {
        // Popper virtual elements require a getBoundingClientRect method
        throw new TypeError(`${NAME$7.toUpperCase()}: Option "reference" provided type "object" without a required "getBoundingClientRect" method.`);
      }

      return config;
    }

    _getMenuElement() {
      return SelectorEngine.next(this._element, SELECTOR_MENU)[0];
    }

    _getPlacement() {
      const parentDropdown = this._element.parentNode;

      if (parentDropdown.classList.contains(CLASS_NAME_DROPEND)) {
        return PLACEMENT_RIGHT;
      }

      if (parentDropdown.classList.contains(CLASS_NAME_DROPSTART)) {
        return PLACEMENT_LEFT;
      } // We need to trim the value because custom properties can also include spaces


      const isEnd = getComputedStyle(this._menu).getPropertyValue('--bs-position').trim() === 'end';

      if (parentDropdown.classList.contains(CLASS_NAME_DROPUP)) {
        return isEnd ? PLACEMENT_TOPEND : PLACEMENT_TOP;
      }

      return isEnd ? PLACEMENT_BOTTOMEND : PLACEMENT_BOTTOM;
    }

    _detectNavbar() {
      return this._element.closest(`.${CLASS_NAME_NAVBAR}`) !== null;
    }

    _getOffset() {
      const {
        offset
      } = this._config;

      if (typeof offset === 'string') {
        return offset.split(',').map(val => Number.parseInt(val, 10));
      }

      if (typeof offset === 'function') {
        return popperData => offset(popperData, this._element);
      }

      return offset;
    }

    _getPopperConfig() {
      const defaultBsPopperConfig = {
        placement: this._getPlacement(),
        modifiers: [{
          name: 'preventOverflow',
          options: {
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

      return { ...defaultBsPopperConfig,
        ...(typeof this._config.popperConfig === 'function' ? this._config.popperConfig(defaultBsPopperConfig) : this._config.popperConfig)
      };
    } // Static


    static dropdownInterface(element, config) {
      let data = Data.get(element, DATA_KEY$7);

      const _config = typeof config === 'object' ? config : null;

      if (!data) {
        data = new Dropdown(element, _config);
      }

      if (typeof config === 'string') {
        if (typeof data[config] === 'undefined') {
          throw new TypeError(`No method named "${config}"`);
        }

        data[config]();
      }
    }

    static jQueryInterface(config) {
      return this.each(function () {
        Dropdown.dropdownInterface(this, config);
      });
    }

    static clearMenus(event) {
      if (event) {
        if (event.button === RIGHT_MOUSE_BUTTON || event.type === 'keyup' && event.key !== TAB_KEY) {
          return;
        }

        if (/input|select|textarea|form/i.test(event.target.tagName)) {
          return;
        }
      }

      const toggles = SelectorEngine.find(SELECTOR_DATA_TOGGLE$3);

      for (let i = 0, len = toggles.length; i < len; i++) {
        const context = Data.get(toggles[i], DATA_KEY$7);
        const relatedTarget = {
          relatedTarget: toggles[i]
        };

        if (event && event.type === 'click') {
          relatedTarget.clickEvent = event;
        }

        if (!context) {
          continue;
        }

        const dropdownMenu = context._menu;

        if (!toggles[i].classList.contains(CLASS_NAME_SHOW$6)) {
          continue;
        }

        if (event) {
          // Don't close the menu if the clicked element or one of its parents is the dropdown button
          if ([context._element].some(element => event.composedPath().includes(element))) {
            continue;
          } // Tab navigation through the dropdown menu shouldn't close the menu


          if (event.type === 'keyup' && event.key === TAB_KEY && dropdownMenu.contains(event.target)) {
            continue;
          }
        }

        const hideEvent = EventHandler.trigger(toggles[i], EVENT_HIDE$4, relatedTarget);

        if (hideEvent.defaultPrevented) {
          continue;
        } // If this is a touch-enabled device we remove the extra
        // empty mouseover listeners we added for iOS support


        if ('ontouchstart' in document.documentElement) {
          [].concat(...document.body.children).forEach(elem => EventHandler.off(elem, 'mouseover', null, noop()));
        }

        toggles[i].setAttribute('aria-expanded', 'false');

        if (context._popper) {
          context._popper.destroy();
        }

        dropdownMenu.classList.remove(CLASS_NAME_SHOW$6);
        toggles[i].classList.remove(CLASS_NAME_SHOW$6);
        Manipulator.removeDataAttribute(dropdownMenu, 'popper');
        EventHandler.trigger(toggles[i], EVENT_HIDDEN$4, relatedTarget);
      }
    }

    static getParentFromElement(element) {
      return getElementFromSelector(element) || element.parentNode;
    }

    static dataApiKeydownHandler(event) {
      // If not input/textarea:
      //  - And not a key in REGEXP_KEYDOWN => not a dropdown command
      // If input/textarea:
      //  - If space key => not a dropdown command
      //  - If key is other than escape
      //    - If key is not up or down => not a dropdown command
      //    - If trigger inside the menu => not a dropdown command
      if (/input|textarea/i.test(event.target.tagName) ? event.key === SPACE_KEY || event.key !== ESCAPE_KEY$2 && (event.key !== ARROW_DOWN_KEY && event.key !== ARROW_UP_KEY || event.target.closest(SELECTOR_MENU)) : !REGEXP_KEYDOWN.test(event.key)) {
        return;
      }

      event.preventDefault();
      event.stopPropagation();

      if (this.disabled || this.classList.contains(CLASS_NAME_DISABLED)) {
        return;
      }

      const parent = Dropdown.getParentFromElement(this);
      const isActive = this.classList.contains(CLASS_NAME_SHOW$6);

      if (event.key === ESCAPE_KEY$2) {
        const button = this.matches(SELECTOR_DATA_TOGGLE$3) ? this : SelectorEngine.prev(this, SELECTOR_DATA_TOGGLE$3)[0];
        button.focus();
        Dropdown.clearMenus();
        return;
      }

      if (!isActive && (event.key === ARROW_UP_KEY || event.key === ARROW_DOWN_KEY)) {
        const button = this.matches(SELECTOR_DATA_TOGGLE$3) ? this : SelectorEngine.prev(this, SELECTOR_DATA_TOGGLE$3)[0];
        button.click();
        return;
      }

      if (!isActive || event.key === SPACE_KEY) {
        Dropdown.clearMenus();
        return;
      }

      const items = SelectorEngine.find(SELECTOR_VISIBLE_ITEMS, parent).filter(isVisible);

      if (!items.length) {
        return;
      }

      let index = items.indexOf(event.target); // Up

      if (event.key === ARROW_UP_KEY && index > 0) {
        index--;
      } // Down


      if (event.key === ARROW_DOWN_KEY && index < items.length - 1) {
        index++;
      } // index is -1 if the first keydown is an ArrowUp


      index = index === -1 ? 0 : index;
      items[index].focus();
    }

  }
  /**
   * ------------------------------------------------------------------------
   * Data Api implementation
   * ------------------------------------------------------------------------
   */


  EventHandler.on(document, EVENT_KEYDOWN_DATA_API, SELECTOR_DATA_TOGGLE$3, Dropdown.dataApiKeydownHandler);
  EventHandler.on(document, EVENT_KEYDOWN_DATA_API, SELECTOR_MENU, Dropdown.dataApiKeydownHandler);
  EventHandler.on(document, EVENT_CLICK_DATA_API$3, Dropdown.clearMenus);
  EventHandler.on(document, EVENT_KEYUP_DATA_API, Dropdown.clearMenus);
  EventHandler.on(document, EVENT_CLICK_DATA_API$3, SELECTOR_DATA_TOGGLE$3, function (event) {
    event.preventDefault();
    Dropdown.dropdownInterface(this);
  });
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .Dropdown to jQuery only if jQuery is present
   */

  defineJQueryPlugin(NAME$7, Dropdown);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): modal.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$6 = 'modal';
  const DATA_KEY$6 = 'bs.modal';
  const EVENT_KEY$6 = `.${DATA_KEY$6}`;
  const DATA_API_KEY$3 = '.data-api';
  const ESCAPE_KEY$1 = 'Escape';
  const Default$5 = {
    backdrop: true,
    keyboard: true,
    focus: true
  };
  const DefaultType$5 = {
    backdrop: '(boolean|string)',
    keyboard: 'boolean',
    focus: 'boolean'
  };
  const EVENT_HIDE$3 = `hide${EVENT_KEY$6}`;
  const EVENT_HIDE_PREVENTED = `hidePrevented${EVENT_KEY$6}`;
  const EVENT_HIDDEN$3 = `hidden${EVENT_KEY$6}`;
  const EVENT_SHOW$3 = `show${EVENT_KEY$6}`;
  const EVENT_SHOWN$3 = `shown${EVENT_KEY$6}`;
  const EVENT_FOCUSIN$1 = `focusin${EVENT_KEY$6}`;
  const EVENT_RESIZE = `resize${EVENT_KEY$6}`;
  const EVENT_CLICK_DISMISS$2 = `click.dismiss${EVENT_KEY$6}`;
  const EVENT_KEYDOWN_DISMISS = `keydown.dismiss${EVENT_KEY$6}`;
  const EVENT_MOUSEUP_DISMISS = `mouseup.dismiss${EVENT_KEY$6}`;
  const EVENT_MOUSEDOWN_DISMISS = `mousedown.dismiss${EVENT_KEY$6}`;
  const EVENT_CLICK_DATA_API$2 = `click${EVENT_KEY$6}${DATA_API_KEY$3}`;
  const CLASS_NAME_SCROLLBAR_MEASURER = 'modal-scrollbar-measure';
  const CLASS_NAME_BACKDROP = 'modal-backdrop';
  const CLASS_NAME_OPEN = 'modal-open';
  const CLASS_NAME_FADE$4 = 'fade';
  const CLASS_NAME_SHOW$5 = 'show';
  const CLASS_NAME_STATIC = 'modal-static';
  const SELECTOR_DIALOG = '.modal-dialog';
  const SELECTOR_MODAL_BODY = '.modal-body';
  const SELECTOR_DATA_TOGGLE$2 = '[data-bs-toggle="modal"]';
  const SELECTOR_DATA_DISMISS$2 = '[data-bs-dismiss="modal"]';
  const SELECTOR_FIXED_CONTENT$1 = '.fixed-top, .fixed-bottom, .is-fixed, .sticky-top';
  const SELECTOR_STICKY_CONTENT$1 = '.sticky-top';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Modal extends BaseComponent {
    constructor(element, config) {
      super(element);
      this._config = this._getConfig(config);
      this._dialog = SelectorEngine.findOne(SELECTOR_DIALOG, this._element);
      this._backdrop = null;
      this._isShown = false;
      this._isBodyOverflowing = false;
      this._ignoreBackdropClick = false;
      this._isTransitioning = false;
      this._scrollbarWidth = 0;
    } // Getters


    static get Default() {
      return Default$5;
    }

    static get DATA_KEY() {
      return DATA_KEY$6;
    } // Public


    toggle(relatedTarget) {
      return this._isShown ? this.hide() : this.show(relatedTarget);
    }

    show(relatedTarget) {
      if (this._isShown || this._isTransitioning) {
        return;
      }

      if (this._isAnimated()) {
        this._isTransitioning = true;
      }

      const showEvent = EventHandler.trigger(this._element, EVENT_SHOW$3, {
        relatedTarget
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

      EventHandler.on(this._element, EVENT_CLICK_DISMISS$2, SELECTOR_DATA_DISMISS$2, event => this.hide(event));
      EventHandler.on(this._dialog, EVENT_MOUSEDOWN_DISMISS, () => {
        EventHandler.one(this._element, EVENT_MOUSEUP_DISMISS, event => {
          if (event.target === this._element) {
            this._ignoreBackdropClick = true;
          }
        });
      });

      this._showBackdrop(() => this._showElement(relatedTarget));
    }

    hide(event) {
      if (event) {
        event.preventDefault();
      }

      if (!this._isShown || this._isTransitioning) {
        return;
      }

      const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE$3);

      if (hideEvent.defaultPrevented) {
        return;
      }

      this._isShown = false;

      const isAnimated = this._isAnimated();

      if (isAnimated) {
        this._isTransitioning = true;
      }

      this._setEscapeEvent();

      this._setResizeEvent();

      EventHandler.off(document, EVENT_FOCUSIN$1);

      this._element.classList.remove(CLASS_NAME_SHOW$5);

      EventHandler.off(this._element, EVENT_CLICK_DISMISS$2);
      EventHandler.off(this._dialog, EVENT_MOUSEDOWN_DISMISS);

      if (isAnimated) {
        const transitionDuration = getTransitionDurationFromElement(this._element);
        EventHandler.one(this._element, 'transitionend', event => this._hideModal(event));
        emulateTransitionEnd(this._element, transitionDuration);
      } else {
        this._hideModal();
      }
    }

    dispose() {
      [window, this._element, this._dialog].forEach(htmlElement => EventHandler.off(htmlElement, EVENT_KEY$6));
      super.dispose();
      /**
       * `document` has 2 events `EVENT_FOCUSIN` and `EVENT_CLICK_DATA_API`
       * Do not move `document` in `htmlElements` array
       * It will remove `EVENT_CLICK_DATA_API` event that should remain
       */

      EventHandler.off(document, EVENT_FOCUSIN$1);
      this._config = null;
      this._dialog = null;
      this._backdrop = null;
      this._isShown = null;
      this._isBodyOverflowing = null;
      this._ignoreBackdropClick = null;
      this._isTransitioning = null;
      this._scrollbarWidth = null;
    }

    handleUpdate() {
      this._adjustDialog();
    } // Private


    _getConfig(config) {
      config = { ...Default$5,
        ...config
      };
      typeCheckConfig(NAME$6, config, DefaultType$5);
      return config;
    }

    _showElement(relatedTarget) {
      const isAnimated = this._isAnimated();

      const modalBody = SelectorEngine.findOne(SELECTOR_MODAL_BODY, this._dialog);

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

      if (isAnimated) {
        reflow(this._element);
      }

      this._element.classList.add(CLASS_NAME_SHOW$5);

      if (this._config.focus) {
        this._enforceFocus();
      }

      const transitionComplete = () => {
        if (this._config.focus) {
          this._element.focus();
        }

        this._isTransitioning = false;
        EventHandler.trigger(this._element, EVENT_SHOWN$3, {
          relatedTarget
        });
      };

      if (isAnimated) {
        const transitionDuration = getTransitionDurationFromElement(this._dialog);
        EventHandler.one(this._dialog, 'transitionend', transitionComplete);
        emulateTransitionEnd(this._dialog, transitionDuration);
      } else {
        transitionComplete();
      }
    }

    _enforceFocus() {
      EventHandler.off(document, EVENT_FOCUSIN$1); // guard against infinite focus loop

      EventHandler.on(document, EVENT_FOCUSIN$1, event => {
        if (document !== event.target && this._element !== event.target && !this._element.contains(event.target)) {
          this._element.focus();
        }
      });
    }

    _setEscapeEvent() {
      if (this._isShown) {
        EventHandler.on(this._element, EVENT_KEYDOWN_DISMISS, event => {
          if (this._config.keyboard && event.key === ESCAPE_KEY$1) {
            event.preventDefault();
            this.hide();
          } else if (!this._config.keyboard && event.key === ESCAPE_KEY$1) {
            this._triggerBackdropTransition();
          }
        });
      } else {
        EventHandler.off(this._element, EVENT_KEYDOWN_DISMISS);
      }
    }

    _setResizeEvent() {
      if (this._isShown) {
        EventHandler.on(window, EVENT_RESIZE, () => this._adjustDialog());
      } else {
        EventHandler.off(window, EVENT_RESIZE);
      }
    }

    _hideModal() {
      this._element.style.display = 'none';

      this._element.setAttribute('aria-hidden', true);

      this._element.removeAttribute('aria-modal');

      this._element.removeAttribute('role');

      this._isTransitioning = false;

      this._showBackdrop(() => {
        document.body.classList.remove(CLASS_NAME_OPEN);

        this._resetAdjustments();

        this._resetScrollbar();

        EventHandler.trigger(this._element, EVENT_HIDDEN$3);
      });
    }

    _removeBackdrop() {
      this._backdrop.parentNode.removeChild(this._backdrop);

      this._backdrop = null;
    }

    _showBackdrop(callback) {
      const isAnimated = this._isAnimated();

      if (this._isShown && this._config.backdrop) {
        this._backdrop = document.createElement('div');
        this._backdrop.className = CLASS_NAME_BACKDROP;

        if (isAnimated) {
          this._backdrop.classList.add(CLASS_NAME_FADE$4);
        }

        document.body.appendChild(this._backdrop);
        EventHandler.on(this._element, EVENT_CLICK_DISMISS$2, event => {
          if (this._ignoreBackdropClick) {
            this._ignoreBackdropClick = false;
            return;
          }

          if (event.target !== event.currentTarget) {
            return;
          }

          if (this._config.backdrop === 'static') {
            this._triggerBackdropTransition();
          } else {
            this.hide();
          }
        });

        if (isAnimated) {
          reflow(this._backdrop);
        }

        this._backdrop.classList.add(CLASS_NAME_SHOW$5);

        if (!isAnimated) {
          callback();
          return;
        }

        const backdropTransitionDuration = getTransitionDurationFromElement(this._backdrop);
        EventHandler.one(this._backdrop, 'transitionend', callback);
        emulateTransitionEnd(this._backdrop, backdropTransitionDuration);
      } else if (!this._isShown && this._backdrop) {
        this._backdrop.classList.remove(CLASS_NAME_SHOW$5);

        const callbackRemove = () => {
          this._removeBackdrop();

          callback();
        };

        if (isAnimated) {
          const backdropTransitionDuration = getTransitionDurationFromElement(this._backdrop);
          EventHandler.one(this._backdrop, 'transitionend', callbackRemove);
          emulateTransitionEnd(this._backdrop, backdropTransitionDuration);
        } else {
          callbackRemove();
        }
      } else {
        callback();
      }
    }

    _isAnimated() {
      return this._element.classList.contains(CLASS_NAME_FADE$4);
    }

    _triggerBackdropTransition() {
      const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE_PREVENTED);

      if (hideEvent.defaultPrevented) {
        return;
      }

      const isModalOverflowing = this._element.scrollHeight > document.documentElement.clientHeight;

      if (!isModalOverflowing) {
        this._element.style.overflowY = 'hidden';
      }

      this._element.classList.add(CLASS_NAME_STATIC);

      const modalTransitionDuration = getTransitionDurationFromElement(this._dialog);
      EventHandler.off(this._element, 'transitionend');
      EventHandler.one(this._element, 'transitionend', () => {
        this._element.classList.remove(CLASS_NAME_STATIC);

        if (!isModalOverflowing) {
          EventHandler.one(this._element, 'transitionend', () => {
            this._element.style.overflowY = '';
          });
          emulateTransitionEnd(this._element, modalTransitionDuration);
        }
      });
      emulateTransitionEnd(this._element, modalTransitionDuration);

      this._element.focus();
    } // ----------------------------------------------------------------------
    // the following methods are used to handle overflowing modals
    // ----------------------------------------------------------------------


    _adjustDialog() {
      const isModalOverflowing = this._element.scrollHeight > document.documentElement.clientHeight;

      if (!this._isBodyOverflowing && isModalOverflowing && !isRTL() || this._isBodyOverflowing && !isModalOverflowing && isRTL()) {
        this._element.style.paddingLeft = `${this._scrollbarWidth}px`;
      }

      if (this._isBodyOverflowing && !isModalOverflowing && !isRTL() || !this._isBodyOverflowing && isModalOverflowing && isRTL()) {
        this._element.style.paddingRight = `${this._scrollbarWidth}px`;
      }
    }

    _resetAdjustments() {
      this._element.style.paddingLeft = '';
      this._element.style.paddingRight = '';
    }

    _checkScrollbar() {
      const rect = document.body.getBoundingClientRect();
      this._isBodyOverflowing = Math.round(rect.left + rect.right) < window.innerWidth;
      this._scrollbarWidth = this._getScrollbarWidth();
    }

    _setScrollbar() {
      if (this._isBodyOverflowing) {
        this._setElementAttributes(SELECTOR_FIXED_CONTENT$1, 'paddingRight', calculatedValue => calculatedValue + this._scrollbarWidth);

        this._setElementAttributes(SELECTOR_STICKY_CONTENT$1, 'marginRight', calculatedValue => calculatedValue - this._scrollbarWidth);

        this._setElementAttributes('body', 'paddingRight', calculatedValue => calculatedValue + this._scrollbarWidth);
      }

      document.body.classList.add(CLASS_NAME_OPEN);
    }

    _setElementAttributes(selector, styleProp, callback) {
      SelectorEngine.find(selector).forEach(element => {
        if (element !== document.body && window.innerWidth > element.clientWidth + this._scrollbarWidth) {
          return;
        }

        const actualValue = element.style[styleProp];
        const calculatedValue = window.getComputedStyle(element)[styleProp];
        Manipulator.setDataAttribute(element, styleProp, actualValue);
        element.style[styleProp] = callback(Number.parseFloat(calculatedValue)) + 'px';
      });
    }

    _resetScrollbar() {
      this._resetElementAttributes(SELECTOR_FIXED_CONTENT$1, 'paddingRight');

      this._resetElementAttributes(SELECTOR_STICKY_CONTENT$1, 'marginRight');

      this._resetElementAttributes('body', 'paddingRight');
    }

    _resetElementAttributes(selector, styleProp) {
      SelectorEngine.find(selector).forEach(element => {
        const value = Manipulator.getDataAttribute(element, styleProp);

        if (typeof value === 'undefined' && element === document.body) {
          element.style[styleProp] = '';
        } else {
          Manipulator.removeDataAttribute(element, styleProp);
          element.style[styleProp] = value;
        }
      });
    }

    _getScrollbarWidth() {
      // thx d.walsh
      const scrollDiv = document.createElement('div');
      scrollDiv.className = CLASS_NAME_SCROLLBAR_MEASURER;
      document.body.appendChild(scrollDiv);
      const scrollbarWidth = scrollDiv.getBoundingClientRect().width - scrollDiv.clientWidth;
      document.body.removeChild(scrollDiv);
      return scrollbarWidth;
    } // Static


    static jQueryInterface(config, relatedTarget) {
      return this.each(function () {
        let data = Data.get(this, DATA_KEY$6);
        const _config = { ...Default$5,
          ...Manipulator.getDataAttributes(this),
          ...(typeof config === 'object' && config ? config : {})
        };

        if (!data) {
          data = new Modal(this, _config);
        }

        if (typeof config === 'string') {
          if (typeof data[config] === 'undefined') {
            throw new TypeError(`No method named "${config}"`);
          }

          data[config](relatedTarget);
        }
      });
    }

  }
  /**
   * ------------------------------------------------------------------------
   * Data Api implementation
   * ------------------------------------------------------------------------
   */


  EventHandler.on(document, EVENT_CLICK_DATA_API$2, SELECTOR_DATA_TOGGLE$2, function (event) {
    const target = getElementFromSelector(this);

    if (this.tagName === 'A' || this.tagName === 'AREA') {
      event.preventDefault();
    }

    EventHandler.one(target, EVENT_SHOW$3, showEvent => {
      if (showEvent.defaultPrevented) {
        // only register focus restorer if modal will actually get shown
        return;
      }

      EventHandler.one(target, EVENT_HIDDEN$3, () => {
        if (isVisible(this)) {
          this.focus();
        }
      });
    });
    let data = Data.get(target, DATA_KEY$6);

    if (!data) {
      const config = { ...Manipulator.getDataAttributes(target),
        ...Manipulator.getDataAttributes(this)
      };
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

  defineJQueryPlugin(NAME$6, Modal);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): util/scrollBar.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  const SELECTOR_FIXED_CONTENT = '.fixed-top, .fixed-bottom, .is-fixed';
  const SELECTOR_STICKY_CONTENT = '.sticky-top';

  const getWidth = () => {
    // https://developer.mozilla.org/en-US/docs/Web/API/Window/innerWidth#usage_notes
    const documentWidth = document.documentElement.clientWidth;
    return Math.abs(window.innerWidth - documentWidth);
  };

  const hide = (width = getWidth()) => {
    document.body.style.overflow = 'hidden';

    _setElementAttributes(SELECTOR_FIXED_CONTENT, 'paddingRight', calculatedValue => calculatedValue + width);

    _setElementAttributes(SELECTOR_STICKY_CONTENT, 'marginRight', calculatedValue => calculatedValue - width);

    _setElementAttributes('body', 'paddingRight', calculatedValue => calculatedValue + width);
  };

  const _setElementAttributes = (selector, styleProp, callback) => {
    const scrollbarWidth = getWidth();
    SelectorEngine.find(selector).forEach(element => {
      if (element !== document.body && window.innerWidth > element.clientWidth + scrollbarWidth) {
        return;
      }

      const actualValue = element.style[styleProp];
      const calculatedValue = window.getComputedStyle(element)[styleProp];
      Manipulator.setDataAttribute(element, styleProp, actualValue);
      element.style[styleProp] = callback(Number.parseFloat(calculatedValue)) + 'px';
    });
  };

  const reset = () => {
    document.body.style.overflow = 'auto';

    _resetElementAttributes(SELECTOR_FIXED_CONTENT, 'paddingRight');

    _resetElementAttributes(SELECTOR_STICKY_CONTENT, 'marginRight');

    _resetElementAttributes('body', 'paddingRight');
  };

  const _resetElementAttributes = (selector, styleProp) => {
    SelectorEngine.find(selector).forEach(element => {
      const value = Manipulator.getDataAttribute(element, styleProp);

      if (typeof value === 'undefined' && element === document.body) {
        element.style.removeProperty(styleProp);
      } else {
        Manipulator.removeDataAttribute(element, styleProp);
        element.style[styleProp] = value;
      }
    });
  };

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): offcanvas.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$5 = 'offcanvas';
  const DATA_KEY$5 = 'bs.offcanvas';
  const EVENT_KEY$5 = `.${DATA_KEY$5}`;
  const DATA_API_KEY$2 = '.data-api';
  const EVENT_LOAD_DATA_API$1 = `load${EVENT_KEY$5}${DATA_API_KEY$2}`;
  const ESCAPE_KEY = 'Escape';
  const Default$4 = {
    backdrop: true,
    keyboard: true,
    scroll: false
  };
  const DefaultType$4 = {
    backdrop: 'boolean',
    keyboard: 'boolean',
    scroll: 'boolean'
  };
  const CLASS_NAME_BACKDROP_BODY = 'offcanvas-backdrop';
  const CLASS_NAME_SHOW$4 = 'show';
  const CLASS_NAME_TOGGLING = 'offcanvas-toggling';
  const OPEN_SELECTOR = '.offcanvas.show';
  const ACTIVE_SELECTOR = `${OPEN_SELECTOR}, .${CLASS_NAME_TOGGLING}`;
  const EVENT_SHOW$2 = `show${EVENT_KEY$5}`;
  const EVENT_SHOWN$2 = `shown${EVENT_KEY$5}`;
  const EVENT_HIDE$2 = `hide${EVENT_KEY$5}`;
  const EVENT_HIDDEN$2 = `hidden${EVENT_KEY$5}`;
  const EVENT_FOCUSIN = `focusin${EVENT_KEY$5}`;
  const EVENT_CLICK_DATA_API$1 = `click${EVENT_KEY$5}${DATA_API_KEY$2}`;
  const EVENT_CLICK_DISMISS$1 = `click.dismiss${EVENT_KEY$5}`;
  const SELECTOR_DATA_DISMISS$1 = '[data-bs-dismiss="offcanvas"]';
  const SELECTOR_DATA_TOGGLE$1 = '[data-bs-toggle="offcanvas"]';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Offcanvas extends BaseComponent {
    constructor(element, config) {
      super(element);
      this._config = this._getConfig(config);
      this._isShown = false;

      this._addEventListeners();
    } // Getters


    static get Default() {
      return Default$4;
    }

    static get DATA_KEY() {
      return DATA_KEY$5;
    } // Public


    toggle(relatedTarget) {
      return this._isShown ? this.hide() : this.show(relatedTarget);
    }

    show(relatedTarget) {
      if (this._isShown) {
        return;
      }

      const showEvent = EventHandler.trigger(this._element, EVENT_SHOW$2, {
        relatedTarget
      });

      if (showEvent.defaultPrevented) {
        return;
      }

      this._isShown = true;
      this._element.style.visibility = 'visible';

      if (this._config.backdrop) {
        document.body.classList.add(CLASS_NAME_BACKDROP_BODY);
      }

      if (!this._config.scroll) {
        hide();
      }

      this._element.classList.add(CLASS_NAME_TOGGLING);

      this._element.removeAttribute('aria-hidden');

      this._element.setAttribute('aria-modal', true);

      this._element.setAttribute('role', 'dialog');

      this._element.classList.add(CLASS_NAME_SHOW$4);

      const completeCallBack = () => {
        this._element.classList.remove(CLASS_NAME_TOGGLING);

        EventHandler.trigger(this._element, EVENT_SHOWN$2, {
          relatedTarget
        });

        this._enforceFocusOnElement(this._element);
      };

      setTimeout(completeCallBack, getTransitionDurationFromElement(this._element));
    }

    hide() {
      if (!this._isShown) {
        return;
      }

      const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE$2);

      if (hideEvent.defaultPrevented) {
        return;
      }

      this._element.classList.add(CLASS_NAME_TOGGLING);

      EventHandler.off(document, EVENT_FOCUSIN);

      this._element.blur();

      this._isShown = false;

      this._element.classList.remove(CLASS_NAME_SHOW$4);

      const completeCallback = () => {
        this._element.setAttribute('aria-hidden', true);

        this._element.removeAttribute('aria-modal');

        this._element.removeAttribute('role');

        this._element.style.visibility = 'hidden';

        if (this._config.backdrop) {
          document.body.classList.remove(CLASS_NAME_BACKDROP_BODY);
        }

        if (!this._config.scroll) {
          reset();
        }

        EventHandler.trigger(this._element, EVENT_HIDDEN$2);

        this._element.classList.remove(CLASS_NAME_TOGGLING);
      };

      setTimeout(completeCallback, getTransitionDurationFromElement(this._element));
    } // Private


    _getConfig(config) {
      config = { ...Default$4,
        ...Manipulator.getDataAttributes(this._element),
        ...(typeof config === 'object' ? config : {})
      };
      typeCheckConfig(NAME$5, config, DefaultType$4);
      return config;
    }

    _enforceFocusOnElement(element) {
      EventHandler.off(document, EVENT_FOCUSIN); // guard against infinite focus loop

      EventHandler.on(document, EVENT_FOCUSIN, event => {
        if (document !== event.target && element !== event.target && !element.contains(event.target)) {
          element.focus();
        }
      });
      element.focus();
    }

    _addEventListeners() {
      EventHandler.on(this._element, EVENT_CLICK_DISMISS$1, SELECTOR_DATA_DISMISS$1, () => this.hide());
      EventHandler.on(document, 'keydown', event => {
        if (this._config.keyboard && event.key === ESCAPE_KEY) {
          this.hide();
        }
      });
      EventHandler.on(document, EVENT_CLICK_DATA_API$1, event => {
        const target = SelectorEngine.findOne(getSelectorFromElement(event.target));

        if (!this._element.contains(event.target) && target !== this._element) {
          this.hide();
        }
      });
    } // Static


    static jQueryInterface(config) {
      return this.each(function () {
        const data = Data.get(this, DATA_KEY$5) || new Offcanvas(this, typeof config === 'object' ? config : {});

        if (typeof config !== 'string') {
          return;
        }

        if (data[config] === undefined || config.startsWith('_') || config === 'constructor') {
          throw new TypeError(`No method named "${config}"`);
        }

        data[config](this);
      });
    }

  }
  /**
   * ------------------------------------------------------------------------
   * Data Api implementation
   * ------------------------------------------------------------------------
   */


  EventHandler.on(document, EVENT_CLICK_DATA_API$1, SELECTOR_DATA_TOGGLE$1, function (event) {
    const target = getElementFromSelector(this);

    if (['A', 'AREA'].includes(this.tagName)) {
      event.preventDefault();
    }

    if (isDisabled(this)) {
      return;
    }

    EventHandler.one(target, EVENT_HIDDEN$2, () => {
      // focus on trigger when it is closed
      if (isVisible(this)) {
        this.focus();
      }
    }); // avoid conflict when clicking a toggler of an offcanvas, while another is open

    const allReadyOpen = SelectorEngine.findOne(ACTIVE_SELECTOR);

    if (allReadyOpen && allReadyOpen !== target) {
      return;
    }

    const data = Data.get(target, DATA_KEY$5) || new Offcanvas(target);
    data.toggle(this);
  });
  EventHandler.on(window, EVENT_LOAD_DATA_API$1, () => {
    SelectorEngine.find(OPEN_SELECTOR).forEach(el => (Data.get(el, DATA_KEY$5) || new Offcanvas(el)).show());
  });
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   */

  defineJQueryPlugin(NAME$5, Offcanvas);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): util/sanitizer.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  const uriAttrs = new Set(['background', 'cite', 'href', 'itemtype', 'longdesc', 'poster', 'src', 'xlink:href']);
  const ARIA_ATTRIBUTE_PATTERN = /^aria-[\w-]*$/i;
  /**
   * A pattern that recognizes a commonly useful subset of URLs that are safe.
   *
   * Shoutout to Angular 7 https://github.com/angular/angular/blob/7.2.4/packages/core/src/sanitization/url_sanitizer.ts
   */

  const SAFE_URL_PATTERN = /^(?:(?:https?|mailto|ftp|tel|file):|[^#&/:?]*(?:[#/?]|$))/i;
  /**
   * A pattern that matches safe data URLs. Only matches image, video and audio types.
   *
   * Shoutout to Angular 7 https://github.com/angular/angular/blob/7.2.4/packages/core/src/sanitization/url_sanitizer.ts
   */

  const DATA_URL_PATTERN = /^data:(?:image\/(?:bmp|gif|jpeg|jpg|png|tiff|webp)|video\/(?:mpeg|mp4|ogg|webm)|audio\/(?:mp3|oga|ogg|opus));base64,[\d+/a-z]+=*$/i;

  const allowedAttribute = (attr, allowedAttributeList) => {
    const attrName = attr.nodeName.toLowerCase();

    if (allowedAttributeList.includes(attrName)) {
      if (uriAttrs.has(attrName)) {
        return Boolean(SAFE_URL_PATTERN.test(attr.nodeValue) || DATA_URL_PATTERN.test(attr.nodeValue));
      }

      return true;
    }

    const regExp = allowedAttributeList.filter(attrRegex => attrRegex instanceof RegExp); // Check if a regular expression validates the attribute.

    for (let i = 0, len = regExp.length; i < len; i++) {
      if (regExp[i].test(attrName)) {
        return true;
      }
    }

    return false;
  };

  const DefaultAllowlist = {
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
    if (!unsafeHtml.length) {
      return unsafeHtml;
    }

    if (sanitizeFn && typeof sanitizeFn === 'function') {
      return sanitizeFn(unsafeHtml);
    }

    const domParser = new window.DOMParser();
    const createdDocument = domParser.parseFromString(unsafeHtml, 'text/html');
    const allowlistKeys = Object.keys(allowList);
    const elements = [].concat(...createdDocument.body.querySelectorAll('*'));

    for (let i = 0, len = elements.length; i < len; i++) {
      const el = elements[i];
      const elName = el.nodeName.toLowerCase();

      if (!allowlistKeys.includes(elName)) {
        el.parentNode.removeChild(el);
        continue;
      }

      const attributeList = [].concat(...el.attributes);
      const allowedAttributes = [].concat(allowList['*'] || [], allowList[elName] || []);
      attributeList.forEach(attr => {
        if (!allowedAttribute(attr, allowedAttributes)) {
          el.removeAttribute(attr.nodeName);
        }
      });
    }

    return createdDocument.body.innerHTML;
  }

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): tooltip.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$4 = 'tooltip';
  const DATA_KEY$4 = 'bs.tooltip';
  const EVENT_KEY$4 = `.${DATA_KEY$4}`;
  const CLASS_PREFIX$1 = 'bs-tooltip';
  const BSCLS_PREFIX_REGEX$1 = new RegExp(`(^|\\s)${CLASS_PREFIX$1}\\S+`, 'g');
  const DISALLOWED_ATTRIBUTES = new Set(['sanitize', 'allowList', 'sanitizeFn']);
  const DefaultType$3 = {
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
  const AttachmentMap = {
    AUTO: 'auto',
    TOP: 'top',
    RIGHT: isRTL() ? 'left' : 'right',
    BOTTOM: 'bottom',
    LEFT: isRTL() ? 'right' : 'left'
  };
  const Default$3 = {
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
  const Event$2 = {
    HIDE: `hide${EVENT_KEY$4}`,
    HIDDEN: `hidden${EVENT_KEY$4}`,
    SHOW: `show${EVENT_KEY$4}`,
    SHOWN: `shown${EVENT_KEY$4}`,
    INSERTED: `inserted${EVENT_KEY$4}`,
    CLICK: `click${EVENT_KEY$4}`,
    FOCUSIN: `focusin${EVENT_KEY$4}`,
    FOCUSOUT: `focusout${EVENT_KEY$4}`,
    MOUSEENTER: `mouseenter${EVENT_KEY$4}`,
    MOUSELEAVE: `mouseleave${EVENT_KEY$4}`
  };
  const CLASS_NAME_FADE$3 = 'fade';
  const CLASS_NAME_MODAL = 'modal';
  const CLASS_NAME_SHOW$3 = 'show';
  const HOVER_STATE_SHOW = 'show';
  const HOVER_STATE_OUT = 'out';
  const SELECTOR_TOOLTIP_INNER = '.tooltip-inner';
  const TRIGGER_HOVER = 'hover';
  const TRIGGER_FOCUS = 'focus';
  const TRIGGER_CLICK = 'click';
  const TRIGGER_MANUAL = 'manual';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Tooltip extends BaseComponent {
    constructor(element, config) {
      if (typeof Popper === 'undefined') {
        throw new TypeError('Bootstrap\'s tooltips require Popper (https://popper.js.org)');
      }

      super(element); // private

      this._isEnabled = true;
      this._timeout = 0;
      this._hoverState = '';
      this._activeTrigger = {};
      this._popper = null; // Protected

      this.config = this._getConfig(config);
      this.tip = null;

      this._setListeners();
    } // Getters


    static get Default() {
      return Default$3;
    }

    static get NAME() {
      return NAME$4;
    }

    static get DATA_KEY() {
      return DATA_KEY$4;
    }

    static get Event() {
      return Event$2;
    }

    static get EVENT_KEY() {
      return EVENT_KEY$4;
    }

    static get DefaultType() {
      return DefaultType$3;
    } // Public


    enable() {
      this._isEnabled = true;
    }

    disable() {
      this._isEnabled = false;
    }

    toggleEnabled() {
      this._isEnabled = !this._isEnabled;
    }

    toggle(event) {
      if (!this._isEnabled) {
        return;
      }

      if (event) {
        const context = this._initializeOnDelegatedTarget(event);

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
    }

    dispose() {
      clearTimeout(this._timeout);
      EventHandler.off(this._element, this.constructor.EVENT_KEY);
      EventHandler.off(this._element.closest(`.${CLASS_NAME_MODAL}`), 'hide.bs.modal', this._hideModalHandler);

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
      super.dispose();
    }

    show() {
      if (this._element.style.display === 'none') {
        throw new Error('Please use show on visible elements');
      }

      if (!(this.isWithContent() && this._isEnabled)) {
        return;
      }

      const showEvent = EventHandler.trigger(this._element, this.constructor.Event.SHOW);
      const shadowRoot = findShadowRoot(this._element);
      const isInTheDom = shadowRoot === null ? this._element.ownerDocument.documentElement.contains(this._element) : shadowRoot.contains(this._element);

      if (showEvent.defaultPrevented || !isInTheDom) {
        return;
      }

      const tip = this.getTipElement();
      const tipId = getUID(this.constructor.NAME);
      tip.setAttribute('id', tipId);

      this._element.setAttribute('aria-describedby', tipId);

      this.setContent();

      if (this.config.animation) {
        tip.classList.add(CLASS_NAME_FADE$3);
      }

      const placement = typeof this.config.placement === 'function' ? this.config.placement.call(this, tip, this._element) : this.config.placement;

      const attachment = this._getAttachment(placement);

      this._addAttachmentClass(attachment);

      const container = this._getContainer();

      Data.set(tip, this.constructor.DATA_KEY, this);

      if (!this._element.ownerDocument.documentElement.contains(this.tip)) {
        container.appendChild(tip);
        EventHandler.trigger(this._element, this.constructor.Event.INSERTED);
      }

      if (this._popper) {
        this._popper.update();
      } else {
        this._popper = createPopper(this._element, tip, this._getPopperConfig(attachment));
      }

      tip.classList.add(CLASS_NAME_SHOW$3);
      const customClass = typeof this.config.customClass === 'function' ? this.config.customClass() : this.config.customClass;

      if (customClass) {
        tip.classList.add(...customClass.split(' '));
      } // If this is a touch-enabled device we add extra
      // empty mouseover listeners to the body's immediate children;
      // only needed because of broken event delegation on iOS
      // https://www.quirksmode.org/blog/archives/2014/02/mouse_event_bub.html


      if ('ontouchstart' in document.documentElement) {
        [].concat(...document.body.children).forEach(element => {
          EventHandler.on(element, 'mouseover', noop());
        });
      }

      const complete = () => {
        const prevHoverState = this._hoverState;
        this._hoverState = null;
        EventHandler.trigger(this._element, this.constructor.Event.SHOWN);

        if (prevHoverState === HOVER_STATE_OUT) {
          this._leave(null, this);
        }
      };

      if (this.tip.classList.contains(CLASS_NAME_FADE$3)) {
        const transitionDuration = getTransitionDurationFromElement(this.tip);
        EventHandler.one(this.tip, 'transitionend', complete);
        emulateTransitionEnd(this.tip, transitionDuration);
      } else {
        complete();
      }
    }

    hide() {
      if (!this._popper) {
        return;
      }

      const tip = this.getTipElement();

      const complete = () => {
        if (this._isWithActiveTrigger()) {
          return;
        }

        if (this._hoverState !== HOVER_STATE_SHOW && tip.parentNode) {
          tip.parentNode.removeChild(tip);
        }

        this._cleanTipClass();

        this._element.removeAttribute('aria-describedby');

        EventHandler.trigger(this._element, this.constructor.Event.HIDDEN);

        if (this._popper) {
          this._popper.destroy();

          this._popper = null;
        }
      };

      const hideEvent = EventHandler.trigger(this._element, this.constructor.Event.HIDE);

      if (hideEvent.defaultPrevented) {
        return;
      }

      tip.classList.remove(CLASS_NAME_SHOW$3); // If this is a touch-enabled device we remove the extra
      // empty mouseover listeners we added for iOS support

      if ('ontouchstart' in document.documentElement) {
        [].concat(...document.body.children).forEach(element => EventHandler.off(element, 'mouseover', noop));
      }

      this._activeTrigger[TRIGGER_CLICK] = false;
      this._activeTrigger[TRIGGER_FOCUS] = false;
      this._activeTrigger[TRIGGER_HOVER] = false;

      if (this.tip.classList.contains(CLASS_NAME_FADE$3)) {
        const transitionDuration = getTransitionDurationFromElement(tip);
        EventHandler.one(tip, 'transitionend', complete);
        emulateTransitionEnd(tip, transitionDuration);
      } else {
        complete();
      }

      this._hoverState = '';
    }

    update() {
      if (this._popper !== null) {
        this._popper.update();
      }
    } // Protected


    isWithContent() {
      return Boolean(this.getTitle());
    }

    getTipElement() {
      if (this.tip) {
        return this.tip;
      }

      const element = document.createElement('div');
      element.innerHTML = this.config.template;
      this.tip = element.children[0];
      return this.tip;
    }

    setContent() {
      const tip = this.getTipElement();
      this.setElementContent(SelectorEngine.findOne(SELECTOR_TOOLTIP_INNER, tip), this.getTitle());
      tip.classList.remove(CLASS_NAME_FADE$3, CLASS_NAME_SHOW$3);
    }

    setElementContent(element, content) {
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
    }

    getTitle() {
      let title = this._element.getAttribute('data-bs-original-title');

      if (!title) {
        title = typeof this.config.title === 'function' ? this.config.title.call(this._element) : this.config.title;
      }

      return title;
    }

    updateAttachment(attachment) {
      if (attachment === 'right') {
        return 'end';
      }

      if (attachment === 'left') {
        return 'start';
      }

      return attachment;
    } // Private


    _initializeOnDelegatedTarget(event, context) {
      const dataKey = this.constructor.DATA_KEY;
      context = context || Data.get(event.delegateTarget, dataKey);

      if (!context) {
        context = new this.constructor(event.delegateTarget, this._getDelegateConfig());
        Data.set(event.delegateTarget, dataKey, context);
      }

      return context;
    }

    _getOffset() {
      const {
        offset
      } = this.config;

      if (typeof offset === 'string') {
        return offset.split(',').map(val => Number.parseInt(val, 10));
      }

      if (typeof offset === 'function') {
        return popperData => offset(popperData, this._element);
      }

      return offset;
    }

    _getPopperConfig(attachment) {
      const defaultBsPopperConfig = {
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
            element: `.${this.constructor.NAME}-arrow`
          }
        }, {
          name: 'onChange',
          enabled: true,
          phase: 'afterWrite',
          fn: data => this._handlePopperPlacementChange(data)
        }],
        onFirstUpdate: data => {
          if (data.options.placement !== data.placement) {
            this._handlePopperPlacementChange(data);
          }
        }
      };
      return { ...defaultBsPopperConfig,
        ...(typeof this.config.popperConfig === 'function' ? this.config.popperConfig(defaultBsPopperConfig) : this.config.popperConfig)
      };
    }

    _addAttachmentClass(attachment) {
      this.getTipElement().classList.add(`${CLASS_PREFIX$1}-${this.updateAttachment(attachment)}`);
    }

    _getContainer() {
      if (this.config.container === false) {
        return document.body;
      }

      if (isElement(this.config.container)) {
        return this.config.container;
      }

      return SelectorEngine.findOne(this.config.container);
    }

    _getAttachment(placement) {
      return AttachmentMap[placement.toUpperCase()];
    }

    _setListeners() {
      const triggers = this.config.trigger.split(' ');
      triggers.forEach(trigger => {
        if (trigger === 'click') {
          EventHandler.on(this._element, this.constructor.Event.CLICK, this.config.selector, event => this.toggle(event));
        } else if (trigger !== TRIGGER_MANUAL) {
          const eventIn = trigger === TRIGGER_HOVER ? this.constructor.Event.MOUSEENTER : this.constructor.Event.FOCUSIN;
          const eventOut = trigger === TRIGGER_HOVER ? this.constructor.Event.MOUSELEAVE : this.constructor.Event.FOCUSOUT;
          EventHandler.on(this._element, eventIn, this.config.selector, event => this._enter(event));
          EventHandler.on(this._element, eventOut, this.config.selector, event => this._leave(event));
        }
      });

      this._hideModalHandler = () => {
        if (this._element) {
          this.hide();
        }
      };

      EventHandler.on(this._element.closest(`.${CLASS_NAME_MODAL}`), 'hide.bs.modal', this._hideModalHandler);

      if (this.config.selector) {
        this.config = { ...this.config,
          trigger: 'manual',
          selector: ''
        };
      } else {
        this._fixTitle();
      }
    }

    _fixTitle() {
      const title = this._element.getAttribute('title');

      const originalTitleType = typeof this._element.getAttribute('data-bs-original-title');

      if (title || originalTitleType !== 'string') {
        this._element.setAttribute('data-bs-original-title', title || '');

        if (title && !this._element.getAttribute('aria-label') && !this._element.textContent) {
          this._element.setAttribute('aria-label', title);
        }

        this._element.setAttribute('title', '');
      }
    }

    _enter(event, context) {
      context = this._initializeOnDelegatedTarget(event, context);

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

      context._timeout = setTimeout(() => {
        if (context._hoverState === HOVER_STATE_SHOW) {
          context.show();
        }
      }, context.config.delay.show);
    }

    _leave(event, context) {
      context = this._initializeOnDelegatedTarget(event, context);

      if (event) {
        context._activeTrigger[event.type === 'focusout' ? TRIGGER_FOCUS : TRIGGER_HOVER] = context._element.contains(event.relatedTarget);
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

      context._timeout = setTimeout(() => {
        if (context._hoverState === HOVER_STATE_OUT) {
          context.hide();
        }
      }, context.config.delay.hide);
    }

    _isWithActiveTrigger() {
      for (const trigger in this._activeTrigger) {
        if (this._activeTrigger[trigger]) {
          return true;
        }
      }

      return false;
    }

    _getConfig(config) {
      const dataAttributes = Manipulator.getDataAttributes(this._element);
      Object.keys(dataAttributes).forEach(dataAttr => {
        if (DISALLOWED_ATTRIBUTES.has(dataAttr)) {
          delete dataAttributes[dataAttr];
        }
      });

      if (config && typeof config.container === 'object' && config.container.jquery) {
        config.container = config.container[0];
      }

      config = { ...this.constructor.Default,
        ...dataAttributes,
        ...(typeof config === 'object' && config ? config : {})
      };

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

      typeCheckConfig(NAME$4, config, this.constructor.DefaultType);

      if (config.sanitize) {
        config.template = sanitizeHtml(config.template, config.allowList, config.sanitizeFn);
      }

      return config;
    }

    _getDelegateConfig() {
      const config = {};

      if (this.config) {
        for (const key in this.config) {
          if (this.constructor.Default[key] !== this.config[key]) {
            config[key] = this.config[key];
          }
        }
      }

      return config;
    }

    _cleanTipClass() {
      const tip = this.getTipElement();
      const tabClass = tip.getAttribute('class').match(BSCLS_PREFIX_REGEX$1);

      if (tabClass !== null && tabClass.length > 0) {
        tabClass.map(token => token.trim()).forEach(tClass => tip.classList.remove(tClass));
      }
    }

    _handlePopperPlacementChange(popperData) {
      const {
        state
      } = popperData;

      if (!state) {
        return;
      }

      this.tip = state.elements.popper;

      this._cleanTipClass();

      this._addAttachmentClass(this._getAttachment(state.placement));
    } // Static


    static jQueryInterface(config) {
      return this.each(function () {
        let data = Data.get(this, DATA_KEY$4);

        const _config = typeof config === 'object' && config;

        if (!data && /dispose|hide/.test(config)) {
          return;
        }

        if (!data) {
          data = new Tooltip(this, _config);
        }

        if (typeof config === 'string') {
          if (typeof data[config] === 'undefined') {
            throw new TypeError(`No method named "${config}"`);
          }

          data[config]();
        }
      });
    }

  }
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .Tooltip to jQuery only if jQuery is present
   */


  defineJQueryPlugin(NAME$4, Tooltip);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): popover.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$3 = 'popover';
  const DATA_KEY$3 = 'bs.popover';
  const EVENT_KEY$3 = `.${DATA_KEY$3}`;
  const CLASS_PREFIX = 'bs-popover';
  const BSCLS_PREFIX_REGEX = new RegExp(`(^|\\s)${CLASS_PREFIX}\\S+`, 'g');
  const Default$2 = { ...Tooltip.Default,
    placement: 'right',
    offset: [0, 8],
    trigger: 'click',
    content: '',
    template: '<div class="popover" role="tooltip">' + '<div class="popover-arrow"></div>' + '<h3 class="popover-header"></h3>' + '<div class="popover-body"></div>' + '</div>'
  };
  const DefaultType$2 = { ...Tooltip.DefaultType,
    content: '(string|element|function)'
  };
  const Event$1 = {
    HIDE: `hide${EVENT_KEY$3}`,
    HIDDEN: `hidden${EVENT_KEY$3}`,
    SHOW: `show${EVENT_KEY$3}`,
    SHOWN: `shown${EVENT_KEY$3}`,
    INSERTED: `inserted${EVENT_KEY$3}`,
    CLICK: `click${EVENT_KEY$3}`,
    FOCUSIN: `focusin${EVENT_KEY$3}`,
    FOCUSOUT: `focusout${EVENT_KEY$3}`,
    MOUSEENTER: `mouseenter${EVENT_KEY$3}`,
    MOUSELEAVE: `mouseleave${EVENT_KEY$3}`
  };
  const CLASS_NAME_FADE$2 = 'fade';
  const CLASS_NAME_SHOW$2 = 'show';
  const SELECTOR_TITLE = '.popover-header';
  const SELECTOR_CONTENT = '.popover-body';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Popover extends Tooltip {
    // Getters
    static get Default() {
      return Default$2;
    }

    static get NAME() {
      return NAME$3;
    }

    static get DATA_KEY() {
      return DATA_KEY$3;
    }

    static get Event() {
      return Event$1;
    }

    static get EVENT_KEY() {
      return EVENT_KEY$3;
    }

    static get DefaultType() {
      return DefaultType$2;
    } // Overrides


    isWithContent() {
      return this.getTitle() || this._getContent();
    }

    setContent() {
      const tip = this.getTipElement(); // we use append for html objects to maintain js events

      this.setElementContent(SelectorEngine.findOne(SELECTOR_TITLE, tip), this.getTitle());

      let content = this._getContent();

      if (typeof content === 'function') {
        content = content.call(this._element);
      }

      this.setElementContent(SelectorEngine.findOne(SELECTOR_CONTENT, tip), content);
      tip.classList.remove(CLASS_NAME_FADE$2, CLASS_NAME_SHOW$2);
    } // Private


    _addAttachmentClass(attachment) {
      this.getTipElement().classList.add(`${CLASS_PREFIX}-${this.updateAttachment(attachment)}`);
    }

    _getContent() {
      return this._element.getAttribute('data-bs-content') || this.config.content;
    }

    _cleanTipClass() {
      const tip = this.getTipElement();
      const tabClass = tip.getAttribute('class').match(BSCLS_PREFIX_REGEX);

      if (tabClass !== null && tabClass.length > 0) {
        tabClass.map(token => token.trim()).forEach(tClass => tip.classList.remove(tClass));
      }
    } // Static


    static jQueryInterface(config) {
      return this.each(function () {
        let data = Data.get(this, DATA_KEY$3);

        const _config = typeof config === 'object' ? config : null;

        if (!data && /dispose|hide/.test(config)) {
          return;
        }

        if (!data) {
          data = new Popover(this, _config);
          Data.set(this, DATA_KEY$3, data);
        }

        if (typeof config === 'string') {
          if (typeof data[config] === 'undefined') {
            throw new TypeError(`No method named "${config}"`);
          }

          data[config]();
        }
      });
    }

  }
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .Popover to jQuery only if jQuery is present
   */


  defineJQueryPlugin(NAME$3, Popover);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): scrollspy.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$2 = 'scrollspy';
  const DATA_KEY$2 = 'bs.scrollspy';
  const EVENT_KEY$2 = `.${DATA_KEY$2}`;
  const DATA_API_KEY$1 = '.data-api';
  const Default$1 = {
    offset: 10,
    method: 'auto',
    target: ''
  };
  const DefaultType$1 = {
    offset: 'number',
    method: 'string',
    target: '(string|element)'
  };
  const EVENT_ACTIVATE = `activate${EVENT_KEY$2}`;
  const EVENT_SCROLL = `scroll${EVENT_KEY$2}`;
  const EVENT_LOAD_DATA_API = `load${EVENT_KEY$2}${DATA_API_KEY$1}`;
  const CLASS_NAME_DROPDOWN_ITEM = 'dropdown-item';
  const CLASS_NAME_ACTIVE$1 = 'active';
  const SELECTOR_DATA_SPY = '[data-bs-spy="scroll"]';
  const SELECTOR_NAV_LIST_GROUP$1 = '.nav, .list-group';
  const SELECTOR_NAV_LINKS = '.nav-link';
  const SELECTOR_NAV_ITEMS = '.nav-item';
  const SELECTOR_LIST_ITEMS = '.list-group-item';
  const SELECTOR_DROPDOWN$1 = '.dropdown';
  const SELECTOR_DROPDOWN_TOGGLE$1 = '.dropdown-toggle';
  const METHOD_OFFSET = 'offset';
  const METHOD_POSITION = 'position';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class ScrollSpy extends BaseComponent {
    constructor(element, config) {
      super(element);
      this._scrollElement = this._element.tagName === 'BODY' ? window : this._element;
      this._config = this._getConfig(config);
      this._selector = `${this._config.target} ${SELECTOR_NAV_LINKS}, ${this._config.target} ${SELECTOR_LIST_ITEMS}, ${this._config.target} .${CLASS_NAME_DROPDOWN_ITEM}`;
      this._offsets = [];
      this._targets = [];
      this._activeTarget = null;
      this._scrollHeight = 0;
      EventHandler.on(this._scrollElement, EVENT_SCROLL, () => this._process());
      this.refresh();

      this._process();
    } // Getters


    static get Default() {
      return Default$1;
    }

    static get DATA_KEY() {
      return DATA_KEY$2;
    } // Public


    refresh() {
      const autoMethod = this._scrollElement === this._scrollElement.window ? METHOD_OFFSET : METHOD_POSITION;
      const offsetMethod = this._config.method === 'auto' ? autoMethod : this._config.method;
      const offsetBase = offsetMethod === METHOD_POSITION ? this._getScrollTop() : 0;
      this._offsets = [];
      this._targets = [];
      this._scrollHeight = this._getScrollHeight();
      const targets = SelectorEngine.find(this._selector);
      targets.map(element => {
        const targetSelector = getSelectorFromElement(element);
        const target = targetSelector ? SelectorEngine.findOne(targetSelector) : null;

        if (target) {
          const targetBCR = target.getBoundingClientRect();

          if (targetBCR.width || targetBCR.height) {
            return [Manipulator[offsetMethod](target).top + offsetBase, targetSelector];
          }
        }

        return null;
      }).filter(item => item).sort((a, b) => a[0] - b[0]).forEach(item => {
        this._offsets.push(item[0]);

        this._targets.push(item[1]);
      });
    }

    dispose() {
      super.dispose();
      EventHandler.off(this._scrollElement, EVENT_KEY$2);
      this._scrollElement = null;
      this._config = null;
      this._selector = null;
      this._offsets = null;
      this._targets = null;
      this._activeTarget = null;
      this._scrollHeight = null;
    } // Private


    _getConfig(config) {
      config = { ...Default$1,
        ...(typeof config === 'object' && config ? config : {})
      };

      if (typeof config.target !== 'string' && isElement(config.target)) {
        let {
          id
        } = config.target;

        if (!id) {
          id = getUID(NAME$2);
          config.target.id = id;
        }

        config.target = `#${id}`;
      }

      typeCheckConfig(NAME$2, config, DefaultType$1);
      return config;
    }

    _getScrollTop() {
      return this._scrollElement === window ? this._scrollElement.pageYOffset : this._scrollElement.scrollTop;
    }

    _getScrollHeight() {
      return this._scrollElement.scrollHeight || Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
    }

    _getOffsetHeight() {
      return this._scrollElement === window ? window.innerHeight : this._scrollElement.getBoundingClientRect().height;
    }

    _process() {
      const scrollTop = this._getScrollTop() + this._config.offset;

      const scrollHeight = this._getScrollHeight();

      const maxScroll = this._config.offset + scrollHeight - this._getOffsetHeight();

      if (this._scrollHeight !== scrollHeight) {
        this.refresh();
      }

      if (scrollTop >= maxScroll) {
        const target = this._targets[this._targets.length - 1];

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

      for (let i = this._offsets.length; i--;) {
        const isActiveTarget = this._activeTarget !== this._targets[i] && scrollTop >= this._offsets[i] && (typeof this._offsets[i + 1] === 'undefined' || scrollTop < this._offsets[i + 1]);

        if (isActiveTarget) {
          this._activate(this._targets[i]);
        }
      }
    }

    _activate(target) {
      this._activeTarget = target;

      this._clear();

      const queries = this._selector.split(',').map(selector => `${selector}[data-bs-target="${target}"],${selector}[href="${target}"]`);

      const link = SelectorEngine.findOne(queries.join(','));

      if (link.classList.contains(CLASS_NAME_DROPDOWN_ITEM)) {
        SelectorEngine.findOne(SELECTOR_DROPDOWN_TOGGLE$1, link.closest(SELECTOR_DROPDOWN$1)).classList.add(CLASS_NAME_ACTIVE$1);
        link.classList.add(CLASS_NAME_ACTIVE$1);
      } else {
        // Set triggered link as active
        link.classList.add(CLASS_NAME_ACTIVE$1);
        SelectorEngine.parents(link, SELECTOR_NAV_LIST_GROUP$1).forEach(listGroup => {
          // Set triggered links parents as active
          // With both <ul> and <nav> markup a parent is the previous sibling of any nav ancestor
          SelectorEngine.prev(listGroup, `${SELECTOR_NAV_LINKS}, ${SELECTOR_LIST_ITEMS}`).forEach(item => item.classList.add(CLASS_NAME_ACTIVE$1)); // Handle special case when .nav-link is inside .nav-item

          SelectorEngine.prev(listGroup, SELECTOR_NAV_ITEMS).forEach(navItem => {
            SelectorEngine.children(navItem, SELECTOR_NAV_LINKS).forEach(item => item.classList.add(CLASS_NAME_ACTIVE$1));
          });
        });
      }

      EventHandler.trigger(this._scrollElement, EVENT_ACTIVATE, {
        relatedTarget: target
      });
    }

    _clear() {
      SelectorEngine.find(this._selector).filter(node => node.classList.contains(CLASS_NAME_ACTIVE$1)).forEach(node => node.classList.remove(CLASS_NAME_ACTIVE$1));
    } // Static


    static jQueryInterface(config) {
      return this.each(function () {
        let data = Data.get(this, DATA_KEY$2);

        const _config = typeof config === 'object' && config;

        if (!data) {
          data = new ScrollSpy(this, _config);
        }

        if (typeof config === 'string') {
          if (typeof data[config] === 'undefined') {
            throw new TypeError(`No method named "${config}"`);
          }

          data[config]();
        }
      });
    }

  }
  /**
   * ------------------------------------------------------------------------
   * Data Api implementation
   * ------------------------------------------------------------------------
   */


  EventHandler.on(window, EVENT_LOAD_DATA_API, () => {
    SelectorEngine.find(SELECTOR_DATA_SPY).forEach(spy => new ScrollSpy(spy, Manipulator.getDataAttributes(spy)));
  });
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .ScrollSpy to jQuery only if jQuery is present
   */

  defineJQueryPlugin(NAME$2, ScrollSpy);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): tab.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME$1 = 'tab';
  const DATA_KEY$1 = 'bs.tab';
  const EVENT_KEY$1 = `.${DATA_KEY$1}`;
  const DATA_API_KEY = '.data-api';
  const EVENT_HIDE$1 = `hide${EVENT_KEY$1}`;
  const EVENT_HIDDEN$1 = `hidden${EVENT_KEY$1}`;
  const EVENT_SHOW$1 = `show${EVENT_KEY$1}`;
  const EVENT_SHOWN$1 = `shown${EVENT_KEY$1}`;
  const EVENT_CLICK_DATA_API = `click${EVENT_KEY$1}${DATA_API_KEY}`;
  const CLASS_NAME_DROPDOWN_MENU = 'dropdown-menu';
  const CLASS_NAME_ACTIVE = 'active';
  const CLASS_NAME_FADE$1 = 'fade';
  const CLASS_NAME_SHOW$1 = 'show';
  const SELECTOR_DROPDOWN = '.dropdown';
  const SELECTOR_NAV_LIST_GROUP = '.nav, .list-group';
  const SELECTOR_ACTIVE = '.active';
  const SELECTOR_ACTIVE_UL = ':scope > li > .active';
  const SELECTOR_DATA_TOGGLE = '[data-bs-toggle="tab"], [data-bs-toggle="pill"], [data-bs-toggle="list"]';
  const SELECTOR_DROPDOWN_TOGGLE = '.dropdown-toggle';
  const SELECTOR_DROPDOWN_ACTIVE_CHILD = ':scope > .dropdown-menu .active';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Tab$1 extends BaseComponent {
    // Getters
    static get DATA_KEY() {
      return DATA_KEY$1;
    } // Public


    show() {
      if (this._element.parentNode && this._element.parentNode.nodeType === Node.ELEMENT_NODE && this._element.classList.contains(CLASS_NAME_ACTIVE) || isDisabled(this._element)) {
        return;
      }

      let previous;
      const target = getElementFromSelector(this._element);

      const listElement = this._element.closest(SELECTOR_NAV_LIST_GROUP);

      if (listElement) {
        const itemSelector = listElement.nodeName === 'UL' || listElement.nodeName === 'OL' ? SELECTOR_ACTIVE_UL : SELECTOR_ACTIVE;
        previous = SelectorEngine.find(itemSelector, listElement);
        previous = previous[previous.length - 1];
      }

      const hideEvent = previous ? EventHandler.trigger(previous, EVENT_HIDE$1, {
        relatedTarget: this._element
      }) : null;
      const showEvent = EventHandler.trigger(this._element, EVENT_SHOW$1, {
        relatedTarget: previous
      });

      if (showEvent.defaultPrevented || hideEvent !== null && hideEvent.defaultPrevented) {
        return;
      }

      this._activate(this._element, listElement);

      const complete = () => {
        EventHandler.trigger(previous, EVENT_HIDDEN$1, {
          relatedTarget: this._element
        });
        EventHandler.trigger(this._element, EVENT_SHOWN$1, {
          relatedTarget: previous
        });
      };

      if (target) {
        this._activate(target, target.parentNode, complete);
      } else {
        complete();
      }
    } // Private


    _activate(element, container, callback) {
      const activeElements = container && (container.nodeName === 'UL' || container.nodeName === 'OL') ? SelectorEngine.find(SELECTOR_ACTIVE_UL, container) : SelectorEngine.children(container, SELECTOR_ACTIVE);
      const active = activeElements[0];
      const isTransitioning = callback && active && active.classList.contains(CLASS_NAME_FADE$1);

      const complete = () => this._transitionComplete(element, active, callback);

      if (active && isTransitioning) {
        const transitionDuration = getTransitionDurationFromElement(active);
        active.classList.remove(CLASS_NAME_SHOW$1);
        EventHandler.one(active, 'transitionend', complete);
        emulateTransitionEnd(active, transitionDuration);
      } else {
        complete();
      }
    }

    _transitionComplete(element, active, callback) {
      if (active) {
        active.classList.remove(CLASS_NAME_ACTIVE);
        const dropdownChild = SelectorEngine.findOne(SELECTOR_DROPDOWN_ACTIVE_CHILD, active.parentNode);

        if (dropdownChild) {
          dropdownChild.classList.remove(CLASS_NAME_ACTIVE);
        }

        if (active.getAttribute('role') === 'tab') {
          active.setAttribute('aria-selected', false);
        }
      }

      element.classList.add(CLASS_NAME_ACTIVE);

      if (element.getAttribute('role') === 'tab') {
        element.setAttribute('aria-selected', true);
      }

      reflow(element);

      if (element.classList.contains(CLASS_NAME_FADE$1)) {
        element.classList.add(CLASS_NAME_SHOW$1);
      }

      if (element.parentNode && element.parentNode.classList.contains(CLASS_NAME_DROPDOWN_MENU)) {
        const dropdownElement = element.closest(SELECTOR_DROPDOWN);

        if (dropdownElement) {
          SelectorEngine.find(SELECTOR_DROPDOWN_TOGGLE).forEach(dropdown => dropdown.classList.add(CLASS_NAME_ACTIVE));
        }

        element.setAttribute('aria-expanded', true);
      }

      if (callback) {
        callback();
      }
    } // Static


    static jQueryInterface(config) {
      return this.each(function () {
        const data = Data.get(this, DATA_KEY$1) || new Tab$1(this);

        if (typeof config === 'string') {
          if (typeof data[config] === 'undefined') {
            throw new TypeError(`No method named "${config}"`);
          }

          data[config]();
        }
      });
    }

  }
  /**
   * ------------------------------------------------------------------------
   * Data Api implementation
   * ------------------------------------------------------------------------
   */


  EventHandler.on(document, EVENT_CLICK_DATA_API, SELECTOR_DATA_TOGGLE, function (event) {
    event.preventDefault();
    const data = Data.get(this, DATA_KEY$1) || new Tab$1(this);
    data.show();
  });
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .Tab to jQuery only if jQuery is present
   */

  defineJQueryPlugin(NAME$1, Tab$1);

  /**
   * --------------------------------------------------------------------------
   * Bootstrap (v5.0.0-beta3): toast.js
   * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
   * --------------------------------------------------------------------------
   */
  /**
   * ------------------------------------------------------------------------
   * Constants
   * ------------------------------------------------------------------------
   */

  const NAME = 'toast';
  const DATA_KEY = 'bs.toast';
  const EVENT_KEY = `.${DATA_KEY}`;
  const EVENT_CLICK_DISMISS = `click.dismiss${EVENT_KEY}`;
  const EVENT_HIDE = `hide${EVENT_KEY}`;
  const EVENT_HIDDEN = `hidden${EVENT_KEY}`;
  const EVENT_SHOW = `show${EVENT_KEY}`;
  const EVENT_SHOWN = `shown${EVENT_KEY}`;
  const CLASS_NAME_FADE = 'fade';
  const CLASS_NAME_HIDE = 'hide';
  const CLASS_NAME_SHOW = 'show';
  const CLASS_NAME_SHOWING = 'showing';
  const DefaultType = {
    animation: 'boolean',
    autohide: 'boolean',
    delay: 'number'
  };
  const Default = {
    animation: true,
    autohide: true,
    delay: 5000
  };
  const SELECTOR_DATA_DISMISS = '[data-bs-dismiss="toast"]';
  /**
   * ------------------------------------------------------------------------
   * Class Definition
   * ------------------------------------------------------------------------
   */

  class Toast extends BaseComponent {
    constructor(element, config) {
      super(element);
      this._config = this._getConfig(config);
      this._timeout = null;

      this._setListeners();
    } // Getters


    static get DefaultType() {
      return DefaultType;
    }

    static get Default() {
      return Default;
    }

    static get DATA_KEY() {
      return DATA_KEY;
    } // Public


    show() {
      const showEvent = EventHandler.trigger(this._element, EVENT_SHOW);

      if (showEvent.defaultPrevented) {
        return;
      }

      this._clearTimeout();

      if (this._config.animation) {
        this._element.classList.add(CLASS_NAME_FADE);
      }

      const complete = () => {
        this._element.classList.remove(CLASS_NAME_SHOWING);

        this._element.classList.add(CLASS_NAME_SHOW);

        EventHandler.trigger(this._element, EVENT_SHOWN);

        if (this._config.autohide) {
          this._timeout = setTimeout(() => {
            this.hide();
          }, this._config.delay);
        }
      };

      this._element.classList.remove(CLASS_NAME_HIDE);

      reflow(this._element);

      this._element.classList.add(CLASS_NAME_SHOWING);

      if (this._config.animation) {
        const transitionDuration = getTransitionDurationFromElement(this._element);
        EventHandler.one(this._element, 'transitionend', complete);
        emulateTransitionEnd(this._element, transitionDuration);
      } else {
        complete();
      }
    }

    hide() {
      if (!this._element.classList.contains(CLASS_NAME_SHOW)) {
        return;
      }

      const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE);

      if (hideEvent.defaultPrevented) {
        return;
      }

      const complete = () => {
        this._element.classList.add(CLASS_NAME_HIDE);

        EventHandler.trigger(this._element, EVENT_HIDDEN);
      };

      this._element.classList.remove(CLASS_NAME_SHOW);

      if (this._config.animation) {
        const transitionDuration = getTransitionDurationFromElement(this._element);
        EventHandler.one(this._element, 'transitionend', complete);
        emulateTransitionEnd(this._element, transitionDuration);
      } else {
        complete();
      }
    }

    dispose() {
      this._clearTimeout();

      if (this._element.classList.contains(CLASS_NAME_SHOW)) {
        this._element.classList.remove(CLASS_NAME_SHOW);
      }

      EventHandler.off(this._element, EVENT_CLICK_DISMISS);
      super.dispose();
      this._config = null;
    } // Private


    _getConfig(config) {
      config = { ...Default,
        ...Manipulator.getDataAttributes(this._element),
        ...(typeof config === 'object' && config ? config : {})
      };
      typeCheckConfig(NAME, config, this.constructor.DefaultType);
      return config;
    }

    _setListeners() {
      EventHandler.on(this._element, EVENT_CLICK_DISMISS, SELECTOR_DATA_DISMISS, () => this.hide());
    }

    _clearTimeout() {
      clearTimeout(this._timeout);
      this._timeout = null;
    } // Static


    static jQueryInterface(config) {
      return this.each(function () {
        let data = Data.get(this, DATA_KEY);

        const _config = typeof config === 'object' && config;

        if (!data) {
          data = new Toast(this, _config);
        }

        if (typeof config === 'string') {
          if (typeof data[config] === 'undefined') {
            throw new TypeError(`No method named "${config}"`);
          }

          data[config](this);
        }
      });
    }

  }
  /**
   * ------------------------------------------------------------------------
   * jQuery
   * ------------------------------------------------------------------------
   * add .Toast to jQuery only if jQuery is present
   */


  defineJQueryPlugin(NAME, Toast);

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
              Collapse.execute(this.collapseAction, rootNode.getElementById(this.collapseTarget));
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
              let rootNode = this.getRootNode();
              // XXX in case of "this" is tobago-page (e.g. ajax exception handling) rootNode is not set correctly???
              if (!rootNode.getElementById) {
                  rootNode = document;
              }
              console.info("[tobago-jsf] Update after jsf.ajax success: %s", id);
              if (JsfParameter.isJsfId(id)) {
                  console.debug("[tobago-jsf] updating #%s", id);
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

    _createClass(Props, [{
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
      // todo: use "custom-elements" instead of this init listener
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
          if (!element) {
              element = document.documentElement;
          }
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
  class Tab extends HTMLElement {
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
