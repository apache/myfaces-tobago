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

Tobago.Utils = {};

/**
 *
 * @param id A JSF client id, type=string. Example: escapeClientId("page:input") -> "#page\\:input"
 * @return A string which can be used as a jQuery selector.
 */
Tobago.Utils.escapeClientId = function(id) {
  return '#' + id.replace(/:/g, '\\:');
};

/**
 * @deprecated since Tobago 2.0.5 because of misspelling
 */
Tobago.Utils.selectWidthJQuery = function(elements, selector) {
  return Tobago.Utils.selectWithJQuery(elements, selector);
};

/**
 * Helps to select either elements from the whole DOM or only find in sub trees
 * (in the case of AJAX partial rendering)
 * @param elements a jQuery object to initialize (ajax) or null for initializing the whole document (full load).
 * @param selector a jQuery selector.
 */
Tobago.Utils.selectWithJQuery = function(elements, selector) {

  if (elements == null) {
    return jQuery(selector);
  }

  if (Tobago.browser.isMsie678) {
    if (selector.match(/^\[[-_a-zA-Z0-9]+\]$/)) { // single attribute set
      return Tobago.Utils.ieSelectWithJQueryAttr(elements, selector);
    }
    if (selector == Tobago.Command.INPUTS_FOR_DEFAULT) { // specific list of input elements
      return Tobago.Utils.ieSelectWithJQueryInputs(elements);
    }
    //if (selector.match(/^\.[-_a-zA-Z0-9]+$/)) { // single class
    //  return Tobago.Utils.ieSelectWithJQueryClass(elements, selector);
    //}
  }

  return elements.find(selector).add(elements.filter(selector));
};

/** internal function for IE <= 8 performance */
Tobago.Utils.ieSelectWithJQueryAttr = function (elements, selector) {
  var founds = [];
  for (var i = 0; i < elements.length; i++) {
    Tobago.Utils.ieFilterAttributes(elements.get(i), selector.substr(1, selector.length - 2), founds);
  }
  return jQuery(founds);
};

/** internal function for IE <= 8 performance */
Tobago.Utils.ieFilterAttributes = function (element, filter, result) {
  if (element[filter] !== undefined) {
    result.push(element);
  }
  for (var i = 0; i < element.childNodes.length; i++) {
    Tobago.Utils.ieFilterAttributes(element.childNodes[i], filter, result);
  }
};

/** internal function for IE <= 8 performance */
Tobago.Utils.ieSelectWithJQueryInputs = function(elements) {
  var founds = [];
  for (var i = 0; i < elements.length; i++) {
    var element = elements.get(i);
    Tobago.Utils.ieFilterTags(element, ["INPUT", "SELECT", "TEXTAREA", "A", "BUTTON"], founds);
  }
  return jQuery(founds);
};

/** internal function for IE <= 8 performance */
Tobago.Utils.ieFilterTags = function (element, tagNames, result) {
  for (var i = 0; i < tagNames.length; i++) {
    if (element.tagName == tagNames[i]) {
      result.push(element);
      break;
    }
  }
  for (i = 0; i < element.childNodes.length; i++) {
    Tobago.Utils.ieFilterTags(element.childNodes[i], tagNames, result);
  }
};

Tobago.Utils.findSubComponent = function(element, subId) {
  return jQuery(Tobago.Utils.getSubComponentId(element.attr('id'), subId));
};

Tobago.Utils.getSubComponentId = function(id, subId) {
  if (id != null) {
    return "#" + id.replace(/:/g, "\\:") + "\\:\\:" + subId;
  } else {
    return null;
  }
};

/** @deprecated */
Tobago.Utils.findSuperComponent = function(element) {
  return jQuery(Tobago.Utils.getSuperComponentId(element.attr('id')));
};

Tobago.Utils.getSuperComponentId = function(id) {
  return "#" + id.substring(0, id.lastIndexOf("::")).replace(/:/g, "\\:");
};

/**
 * "a:b" -> "a"
 * "a:b:c" -> "a:b"
 * "a" -> null
 * null -> null
 * "a:b::sub-component" -> "a"
 * "a::sub-component:b" -> "a::sub-component" // should currently not happen in Tobago
 *
 * @param id The clientId of a component.
 * @return The clientId of the naming container.
 */
Tobago.Utils.getNamingContainerId = function (id) {
  if (id == null) {
    return null;
  }
  if (id.lastIndexOf(":") == -1) {
    return null;
  }
  while (true) {
    var sub = id.lastIndexOf("::");
    if (sub == -1) {
      break;
    }
    if (sub + 1 == id.lastIndexOf(":")) {
      id = id.substring(0, sub);
    } else {
      break;
    }
  }
  return id.substring(0, id.lastIndexOf(":"));
};

/**
 * fix position, when the element it is outside of the current page
 * @param elements is an jQuery Array of elements to be fixed.
 */
Tobago.Utils.keepElementInVisibleArea = function(elements) {
  elements.each(function() {
    var element = jQuery(this);
    var page = jQuery(".tobago-page-content:first");
    var left = element.position().left;
    var top = element.position().top;
    // fix menu position, when it is outside of the current page
    left = Math.max(0, Math.min(left, page.outerWidth() - element.outerWidth()));
    top = Math.max(0, Math.min(top, page.outerHeight() - element.outerHeight()));
    element.css('left', left);
    element.css('top', top);
  });
};

/** @deprecated */
Tobago.Utils.acceleratorKeyEvent = function() {
  return Tobago.browser.isWebkit || Tobago.browser.isGecko ? 'keydown' : 'keypress';
};
