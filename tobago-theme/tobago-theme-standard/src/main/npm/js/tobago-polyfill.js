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

// todo: can be removed

if (window.NodeList && !NodeList.prototype.forEach) {
  NodeList.prototype.forEach = Array.prototype.forEach;
}

// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// from https://developer.mozilla.org/en-US/docs/Web/API/Element/closest
// todo: check, if this is needed for Tobago 5
// for ie
if (!Element.prototype.matches) {
  Element.prototype.matches
      = Element.prototype.msMatchesSelector || Element.prototype.webkitMatchesSelector;
}

// for ie
if (!Element.prototype.closest) {
  Element.prototype.closest = function (s) {
    let el = this;
    do {
      if (el.matches(s)) return el;
      el = el.parentElement || el.parentNode;
    } while (el !== null && el.nodeType === 1);
    return null;
  };
}

// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// for edge/ie
try {
  document.querySelector(":scope");
} catch (exception) {
  var querySelectorWithScope = polyfill(Element.prototype.querySelector);
  Element.prototype.querySelector = function querySelector(selectors) {
    return querySelectorWithScope.apply(this, arguments);
  };

  var querySelectorAllWithScope = polyfill(Element.prototype.querySelectorAll);
  Element.prototype.querySelectorAll = function querySelectorAll(selectors) {
    return querySelectorAllWithScope.apply(this, arguments);
  };

  if (Element.prototype.matches) {
    var matchesWithScope = polyfill(Element.prototype.matches);
    Element.prototype.matches = function matches(selectors) {
      return matchesWithScope.apply(this, arguments);
    };
  }

  if (Element.prototype.closest) {
    var closestWithScope = polyfill(Element.prototype.closest);
    Element.prototype.closest = function closest(selectors) {
      return closestWithScope.apply(this, arguments);
    };
  }

  function polyfill(prototypeFunc) {
    var scope = /:scope(?![\w-])/gi;

    return function (selector) {
      if (selector.toLowerCase().indexOf(":scope") >= 0) {
        var attr = 'tobagoScopeAttribute';
        arguments[0] = selector.replace(scope, '[' + attr + ']');
        this.setAttribute(attr, '');
        var element = prototypeFunc.apply(this, arguments);
        this.removeAttribute(attr);
        return element;
      } else {
        return prototypeFunc.apply(this, arguments);
      }
    };
  }
}

// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
