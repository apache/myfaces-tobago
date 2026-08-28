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

declare global {
  interface Tobago {
    ajax: {
      /**
       * Tobago Ajax request queue for {@link faces.ajax.request}.
       *
       * Faces Ajax requests have to be queued on the client side. The following rules apply:
       * - Only one request is active at the same time.
       * - A new request is ignored if the exact same request (based on element/event.type/executeValues/renderIds) is
       *   currently in progress.
       * - A new request replaces a queued request (based on element/event.type) and is moved to the end of the queue.
       * - A queued request will be removed if the element is not connected to the DOM.
       *
       * {@link faces.ajax.addOnEvent} and {@link faces.ajax.addOnError} are not affected by the queue and still work as before.
       *
       * @param elementOrId The source element or its ID.
       * @param event The triggering event.
       * @param options JSF Ajax request options.
       */
      request(
          elementOrId: Element | string,
          event?: Event | null,
          options?: faces.ajax.RequestOptions
      ): void;
    };
    /**
     * Handle the collapse state for collapsible components, such as tc:box, tc:panel, tc:popup, tc:offcanvas, tc:section.
     */
    collapsible: {
      /**
       * Show (expand) a collapsible component.
       *
       * @param {Element|string} elementOrId The root element of a collapsible component (e.g., tobago-popup for tc:popup) or its ID.
       * @throws {TypeError} Throws an error if the element is not found or is not valid.
       */
      show(elementOrId: Element | string): void;
      /**
       * Hide (collapse) a collapsible component.
       *
       * @param {Element|string} elementOrId The root element of a collapsible component (e.g., tobago-popup for tc:popup) or its ID.
       * @throws {TypeError} Throws an error if the element is not found or is not valid.
       */
      hide(elementOrId: Element | string): void;
    };
  }

  const tobago: Tobago;
}

export {};
