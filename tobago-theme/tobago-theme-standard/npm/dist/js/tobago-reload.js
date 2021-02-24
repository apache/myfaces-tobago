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
import { Listener, Phase } from "./tobago-listener";
import { DomUtils } from "./tobago-utils";
// TODO: might be implemented with a web component
export class ReloadManager {
    constructor() {
        this.timeouts = new Map();
    }
    schedule(id, reloadMillis) {
        if (reloadMillis > 0) {
            // may remove old schedule
            const oldTimeout = this.timeouts.get(id);
            if (oldTimeout) {
                console.debug("clear reload timeout '" + oldTimeout + "' for #'" + id + "'");
                window.clearTimeout(oldTimeout);
                this.timeouts.delete(id);
            }
            // add new schedule
            const timeout = window.setTimeout(function () {
                console.debug("reloading #'" + id + "'");
                jsf.ajax.request(id, null, {
                    "javax.faces.behavior.event": "reload",
                    execute: id,
                    render: id
                });
            }, reloadMillis);
            console.debug("adding reload timeout '" + timeout + "' for #'" + id + "'");
            this.timeouts.set(id, timeout);
        }
    }
}
ReloadManager.instance = new ReloadManager();
ReloadManager.init = function (element) {
    for (const reload of DomUtils.selfOrQuerySelectorAll(element, "[data-tobago-reload]")) {
        ReloadManager.instance.schedule(reload.id, Number(reload.dataset.tobagoReload));
    }
};
Listener.register(ReloadManager.init, Phase.DOCUMENT_READY);
Listener.register(ReloadManager.init, Phase.AFTER_UPDATE);
//# sourceMappingURL=tobago-reload.js.map