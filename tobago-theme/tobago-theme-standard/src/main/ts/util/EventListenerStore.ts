/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

export class EventListenerStore {
  private listeners: Map<Document | HTMLElement | Window, {
    type: string;
    listener: EventListenerOrEventListenerObject;
  }> = new Map();

  add(element: Document | HTMLElement | Window, type: string, listener: EventListenerOrEventListenerObject): void {
    element.addEventListener(type, listener);
    this.listeners.set(element, {type, listener});
  }

  disconnect(): void {
    this.listeners.forEach(({type, listener}, element) => {
      element.removeEventListener(type, listener);
    });
    this.listeners = new Map();
  }

  /**
   * Clean up listeners of disconnected elements.
   */
  cleanup(): void {
    for (const element of this.listeners.keys()) {
      if ((element instanceof Document || element instanceof HTMLElement) && !element.isConnected) {
        this.listeners.delete(element);
      }
    }
  }
}
