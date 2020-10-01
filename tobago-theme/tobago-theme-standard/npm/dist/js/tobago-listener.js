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
export var Phase;
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
export var Order;
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
export class Listener {
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
//# sourceMappingURL=tobago-listener.js.map