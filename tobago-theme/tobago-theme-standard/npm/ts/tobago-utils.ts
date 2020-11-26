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
export class DomUtils {

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
   * @param element with transition
   * @return transition time in milliseconds
   */
  static getTransitionTime(element: HTMLElement): number {
    const style = window.getComputedStyle(element);
    let delay: number = Number.parseFloat(style.transitionDelay);
    let duration: number = Number.parseFloat(style.transitionDuration);
    return (delay + duration) * 1000;
  }
}
