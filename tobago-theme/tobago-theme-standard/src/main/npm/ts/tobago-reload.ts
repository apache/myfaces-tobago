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

  // todo: remove "export" when everything is in namespace Tobago
  export class ReloadManager {

    static instance: ReloadManager = new ReloadManager();

    /**
     * Map to store the scheduled timeouts by id, to prevent duplicate scheduling of the same elements.
     */
    private timeouts: Map<string, number>;

    private constructor() {
      this.timeouts = new Map<string, number>();
    }

    static init = function (element: HTMLElement): void {
      for (const reload of element.tobagoSelfOrQuerySelectorAll("[data-tobago-reload]")) {
        ReloadManager.instance.schedule(reload.id, Number(reload.dataset["tobagoReload"]));
      }
    };

    public schedule = function (id: string, reloadMillis: number) {
      if (reloadMillis > 0) {

        // may remove old schedule
        let oldTimeout = this.timeouts.get(id);
        if (oldTimeout) {
          console.debug("clear reload timeout '" + oldTimeout + "' for #'" + id + "'");
          window.clearTimeout(oldTimeout);
          this.timeouts.delete(id);
        }

        // add new schedule
        let timeout = window.setTimeout(function () {
          console.debug("reloading #'" + id + "'");
          jsf.ajax.request(
              id,
              null,
              {
                "javax.faces.behavior.event": "reload",
                execute: id,
                render: id
              });
        }, reloadMillis);
        console.debug("adding reload timeout '" + timeout + "' for #'" + id + "'");
        this.timeouts.set(id, timeout);
      }
    };

  }

  Listener.register(ReloadManager.init, Phase.DOCUMENT_READY);
  Listener.register(ReloadManager.init, Phase.AFTER_UPDATE);
}
