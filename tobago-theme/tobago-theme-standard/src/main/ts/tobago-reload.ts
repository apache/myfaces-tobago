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

class TobagoReload extends HTMLElement {

  /**
   * Map to store the scheduled timeouts by id, to prevent duplicate scheduling of the same elements.
   */
  private static timeoutMap: Map<string, number> = new Map<string, number>();

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.schedule(this.id, this.component.id, this.frequency);
  }

  public schedule(reloadId: string, componentId: string, reloadMillis: number): void {
    if (reloadMillis > 0) {

      // may remove old schedule
      const oldTimeout = TobagoReload.timeoutMap.get(componentId);
      if (oldTimeout) {
        console.debug("clear reload timeout '%s' for #'%s'", oldTimeout, componentId);
        window.clearTimeout(oldTimeout);
        TobagoReload.timeoutMap.delete(componentId);
      }

      // add new schedule
      const timeout = window.setTimeout(function (): void {
        console.debug("reloading #'%s'", componentId);
        jsf.ajax.request(
            reloadId,
            null,
            {
              "jakarta.faces.behavior.event": "reload",
              execute: `${reloadId} ${componentId}`,
              render: `${reloadId} ${componentId}`
            });
      }, reloadMillis);
      console.debug("adding reload timeout '%s' for #'%s'", timeout, componentId);
      TobagoReload.timeoutMap.set(componentId, timeout);
    }
  }

  get component(): HTMLElement {
    return this.parentElement;
  }

  /** frequency is the number of millis for the timeout */
  get frequency(): number {
    const frequency = this.getAttribute("frequency");
    if (frequency) {
      return Number.parseFloat(frequency);
    } else {
      return 0;
    }
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-reload") == null) {
    window.customElements.define("tobago-reload", TobagoReload);
  }
});
