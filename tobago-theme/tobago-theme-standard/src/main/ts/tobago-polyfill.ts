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
} catch (exception) {
  const querySelectorWithScope = polyfill(Element.prototype.querySelector);
  Element.prototype.querySelector = function querySelector(selectors: string): Element {
    return querySelectorWithScope.apply(this, arguments);
  };

  const querySelectorAllWithScope = polyfill(Element.prototype.querySelectorAll);
  Element.prototype.querySelectorAll = function querySelectorAll(selectors: string): NodeListOf<Element> {
    return querySelectorAllWithScope.apply(this, arguments);
  };

  if (Element.prototype.matches) {
    const matchesWithScope = polyfill(Element.prototype.matches);
    Element.prototype.matches = function matches(selectors: string): boolean {
      return matchesWithScope.apply(this, arguments);
    };
  }

  if (Element.prototype.closest) {
    const closestWithScope = polyfill(Element.prototype.closest);
    Element.prototype.closest = function closest(selectors: string): Element {
      return closestWithScope.apply(this, arguments);
    };
  }

  function polyfill(prototypeFunc: any): any {
    const scope = /:scope(?![\w-])/gi;

    return function (selector: any): any {
      if (selector.toLowerCase().indexOf(":scope") >= 0) {
        const attr = "tobagoScopeAttribute";
        arguments[0] = selector.replace(scope, `[${attr}]`);
        this.setAttribute(attr, "");
        const element = prototypeFunc.apply(this, arguments);
        this.removeAttribute(attr);
        return element;
      } else {
        return prototypeFunc.apply(this, arguments);
      }
    };
  }
}

// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
