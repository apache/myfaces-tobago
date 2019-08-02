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

export class DomUtils {

  /**
   * JSF's component separator constant
   */
  static readonly COMPONENT_SEP = ':';

  /**
   * Tobago's sub-coponent separator constant
   */
  static readonly SUB_COMPONENT_SEP = '::';

  /**
   * The Tobago root element
   */
  static page(): HTMLElement {
    const pages = document.getElementsByClassName("tobago-page");
    if (pages.length > 0) {
      if (pages.length >= 2) {
        console.warn("Found more than one tobago page!");
      }
      return pages.item(0) as HTMLElement;
    }
    console.warn("Found no tobago page!");
    return null;
  }

  /**
   * Find all elements (and also self) which have the class "className".
   * @param element Starting element in DOM to collect.
   * @param className Class of elements to find.
   */
  static selfOrElementsByClassName(element: HTMLElement, className: string): Array<HTMLElement> {
    const result: Array<HTMLElement> = new Array<HTMLElement>();
    if (element.classList.contains(className)) {
      result.push(element);
    }
    const list = element.getElementsByClassName(className);
    for (let i = 0; i < list.length; i++) {
      result.push(list.item(i) as HTMLElement);
    }
    return result;
  }

  /**
   * Find all elements (and also self) which have the attribute "attributeName".
   * @param element Starting element in DOM to collect.
   * @param selectors Name of the attribute of the elements to find.
   */
// todo: may return NodeListOf<HTMLElementTagNameMap[K]> or something like that.
  static selfOrQuerySelectorAll(element: HTMLElement, selectors: string): Array<HTMLElement> {
    const result: Array<HTMLElement> = new Array<HTMLElement>();
    if (element.matches(selectors)) {
      result.push(element);
    }
    for (const found of element.querySelectorAll(selectors)) {
      result.push(found as HTMLElement);
    }
    return result;
  }

  /**
   * Get the previous sibling element (without <style> elements).
   */
  static previousElementSibling(element: HTMLElement): HTMLElement {
    let sibling = element.previousElementSibling as HTMLElement;
    while (sibling != null) {
      if (sibling.tagName !== "STYLE") {
        return sibling;
      }
      sibling = sibling.previousElementSibling as HTMLElement;
    }
    return null;
  }

  /**
   * Get the next sibling element (without <style> elements).
   */
  static nextElementSibling(element: HTMLElement): HTMLElement {
    let sibling = element.nextElementSibling as HTMLElement;
    while (sibling !== null) {
      if (sibling.tagName !== "STYLE") {
        return sibling;
      }
      sibling = sibling.nextElementSibling as HTMLElement;
    }
    return null;
  }

  static outerWidthWithMargin(element: HTMLElement) {
    const style = window.getComputedStyle(element);
    return element.offsetWidth + parseInt(style.marginLeft) + parseInt(style.marginRight);
  }

  static outerHeightWithMargin(element: HTMLElement) {
    const style = window.getComputedStyle(element);
    return element.offsetHeight + parseInt(style.marginTop) + parseInt(style.marginBottom);
  }

  static offset(element: HTMLElement) {
    let top = 0;
    let left = 0;

    let currentElement = element;
    while (currentElement) {
      top += (currentElement.offsetTop - currentElement.scrollTop + currentElement.clientTop);
      left += (currentElement.offsetLeft - currentElement.scrollLeft + currentElement.clientLeft);
      currentElement = currentElement.offsetParent as HTMLElement;
    }

    return {top: top, left: left};
  }

  static isVisible(element: HTMLElement) {
    return element.offsetWidth > 0 || element.offsetHeight > 0 || element.getClientRects().length > 0;
  }

  /**
   *
   * @param id A JSF client id, type=string. Example: escapeClientId("page:input") -> "#page\\:input"
   * @return A string which can be used as a jQuery selector.
   */
  static escapeClientId(id: string): string {
    return '#' + id.replace(/([:\.])/g, '\\$1');
  }

}

export class Tobago4Utils {

  /**
   * Helps to select either elements from the whole DOM or only find in sub trees
   * (in the case of AJAX partial rendering)
   * @param elements a jQuery object to initialize (ajax) or null for initializing the whole document (full load).
   * @param selector a jQuery selector.
   */
  static selectWithJQuery(elements, selector) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
    return elements == null
        ? jQuery(selector)
        : elements.find(selector).add(elements.filter(selector));
  }

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
  static getNamingContainerId(id) {
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
  }

  /**
   * fix position, when the element it is outside of the current page
   * @param elements is an jQuery Array of elements to be fixed.
   */
  static keepElementInVisibleArea(elements) {
    elements.each(function () {
      var element = jQuery(this);
      var page = jQuery(".tobago-page-content:first");
      var left = element.offset().left;
      var top = element.offset().top;
      // fix menu position, when it is outside of the current page
      left = Math.max(0, Math.min(left, page.outerWidth() - element.outerWidth()));
      top = Math.max(0, Math.min(top, page.outerHeight() - element.outerHeight()));
      element.css('left', left);
      element.css('top', top);
    });
  }

  static addDataMarkup(element, markupString) {
    var dataTobagoMarkup = element.attr("data-tobago-markup");
    if (dataTobagoMarkup !== undefined) {
      var markups = jQuery.parseJSON(dataTobagoMarkup);
      markups.push(markupString);
      element.attr("data-tobago-markup", JSON.stringify(markups));
    } else {
      element.attr("data-tobago-markup", JSON.stringify(markupString));
    }
  }

  static removeDataMarkup(element, markupString) {
    var dataTobagoMarkup = element.attr("data-tobago-markup");
    if (dataTobagoMarkup !== undefined) {
      var markups = jQuery.parseJSON(dataTobagoMarkup);
      var index = jQuery.inArray(markupString, markups);
      if (index >= 0) {
        markups.splice(index, 1);
      } else if (markups === markupString) {
        markups = [];
      }

      if (markups.length > 0) {
        element.attr("data-tobago-markup", JSON.stringify(markups));
      } else {
        element.removeAttr("data-tobago-markup");
      }
    }
  }
}