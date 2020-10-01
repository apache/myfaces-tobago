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
//# sourceMappingURL=tobago-utils.js.map