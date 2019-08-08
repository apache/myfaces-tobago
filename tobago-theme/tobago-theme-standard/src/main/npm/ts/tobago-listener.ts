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

  map: Map<Order, Array<(HTMLElement) => void>> = new Map([
    [Order.EARLIER, []],
    [Order.EARLY, []],
    [Order.NORMAL, []],
    [Order.LATE, []],
    [Order.LATER, []]
  ]);

  add(listener: (HTMLElement) => void, order: Order) {
    this.map.get(order).push(listener);
  }

  execute(element?: HTMLElement) {

    this.map.forEach((listeners: Array<(HTMLElement) => void>, order: Order) => {
      listeners.forEach(
          (listener, index) => {
            console.time("[tobago] execute " + order + " " + index);
            listener(element);
            console.timeEnd("[tobago] execute " + order + " " + index);
          }
      );
    });
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
   * @param phase The phase when code should be executed (e. g. Phase.DOCUMENT_READY).
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
        console.error("Unknown phase: '" + phase + "'");
    }
  }

  static executeDocumentReady(element: HTMLElement) {
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

  static executeAfterUpdate(element: HTMLElement) {
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
