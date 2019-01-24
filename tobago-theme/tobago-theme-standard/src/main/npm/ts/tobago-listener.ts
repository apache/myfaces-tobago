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

namespace Tobago {

  export enum Phase {
    /** after the DOM was build */
    DOCUMENT_READY,
    /** after all images and CSS was loaded */
    WINDOW_LOAD,
    /** before sending a normal submit action */
    BEFORE_SUBMIT,
    /** after an AJAX call */
    AFTER_UPDATE,
    /** before ending a page */
    BEFORE_UNLOAD,
    /** before closing a window or tab */
    BEFORE_EXIT,
  }

  export enum Order {
    EARLIER = 0,
    EARLY = 1,
    NORMAL = 2,
    LATE = 3,
    LATER = 4
  }

  class ListenerList {

    // XXX might be replaced with "Map" when ES6 is available

    earlier: Array<(HTMLElement) => void> = [];
    early: Array<(HTMLElement) => void> = [];
    normal: Array<(HTMLElement) => void> = [];
    late: Array<(HTMLElement) => void> = [];
    later: Array<(HTMLElement) => void> = [];

    add(listener: (HTMLElement) => void, order: Order) {
      switch (order) {
        case Order.EARLIER:
          this.earlier.push(listener);
          break;
        case Order.EARLY:
          this.early.push(listener);
          break;
        case Order.NORMAL:
          this.normal.push(listener);
          break;
        case Order.LATE:
          this.late.push(listener);
          break;
        case Order.LATER:
          this.later.push(listener);
          break;
        default:
          console.error("Unknown order: '" + order + "'");
      }
    }

    execute(element?: HTMLElement | HTMLDocument) {
      this.earlier.forEach(
          (value, index) => {
            console.time("[tobago] execute earlier " + index); // @DEV_ONLY
            value(element);
            console.timeEnd("[tobago] execute earlier " + index); // @DEV_ONLY
          }
      );
      this.early.forEach(
          (value, index) => {
            console.time("[tobago] execute early " + index); // @DEV_ONLY
            value(element);
            console.timeEnd("[tobago] execute early " + index); // @DEV_ONLY
          }
      );
      this.normal.forEach(
          (value, index) => {
            console.time("[tobago] execute normal " + index); // @DEV_ONLY
            value(element);
            console.timeEnd("[tobago] execute normal " + index); // @DEV_ONLY
          }
      );
      this.late.forEach(
          (value, index) => {
            console.time("[tobago] execute late " + index); // @DEV_ONLY
            value(element);
            console.timeEnd("[tobago] execute late " + index); // @DEV_ONLY
          }
      );
      this.later.forEach(
          (value, index) => {
            console.time("[tobago] execute later " + index); // @DEV_ONLY
            value(element);
            console.timeEnd("[tobago] execute later " + index); // @DEV_ONLY
          }
      );
    }
  }

  export class Listener {

    // XXX check if "static" is nice

    static documentReady: ListenerList = new ListenerList();
    static windowLoad: ListenerList = new ListenerList();
    static beforeSubmit: ListenerList = new ListenerList();
    static afterUpdate: ListenerList = new ListenerList();
    static beforeUnload: ListenerList = new ListenerList();
    static beforeExit: ListenerList = new ListenerList();

    /**
     * Register a function to be executed on certain events.
     * @param listener Function to be executed.
     * @param phase The phase when code should be executed (e. g. Tobago.Phase.DOCUMENT_READY).
     * @param order An optional order to sort function they depend on others (default: Tobago.Order.NORMAL).
     */
    static register(
        listener: (HTMLElement) => void,
        phase: Phase,
        order: Order = Order.NORMAL) {

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
          console.error("Unknown phase: '" + phase + "'"); // @DEV_ONLY
      }
    }

    static executeDocumentReady(element: HTMLElement) {
      console.time("[tobago] execute documentReady"); // @DEV_ONLY
      Listener.documentReady.execute(element);
      console.timeEnd("[tobago] execute documentReady"); // @DEV_ONLY
    }

    static executeWindowLoad() {
      console.time("[tobago] execute windowLoad"); // @DEV_ONLY
      Listener.windowLoad.execute();
      console.timeEnd("[tobago] execute windowLoad"); // @DEV_ONLY
    }

    static executeBeforeSubmit() {
      console.time("[tobago] execute beforeSubmit"); // @DEV_ONLY
      Listener.beforeSubmit.execute();
      console.timeEnd("[tobago] execute beforeSubmit"); // @DEV_ONLY
    }

    static executeAfterUpdate(element: HTMLElement) {
      console.time("[tobago] execute afterUpdate"); // @DEV_ONLY
      Listener.afterUpdate.execute(element);
      console.timeEnd("[tobago] execute afterUpdate"); // @DEV_ONLY
    }

    static executeBeforeUnload() {
      console.time("[tobago] execute beforeUnload"); // @DEV_ONLY
      Listener.beforeUnload.execute();
      console.timeEnd("[tobago] execute beforeUnload"); // @DEV_ONLY
    }

    static executeBeforeExit() {
      console.time("[tobago] execute beforeExit"); // @DEV_ONLY
      Listener.beforeExit.execute();
      console.timeEnd("[tobago] execute beforeExit"); // @DEV_ONLY
    }
  }

  /**
   * Backward compatible listener registration. In the case of an AJAX call (phase = Phase.AFTER_UPDATE)
   * this listener will be called with a jQuery-object, the new one will get an HTMLElement.
   * @deprecated since 5.0.0
   */
  export function registerListener(listener, phase, order) {

    Tobago.Listener.register(function(element: HTMLElement) {
      listener(jQuery(element));
    }, phase, order);
  }

}
