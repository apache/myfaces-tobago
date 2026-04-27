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

import {QueueItem} from "./tobago-ajax-queue";

/**
 * Static logic for AjaxQueue class for better testing.
 */
export class AjaxQueueStatic {
  static request(queue: QueueItem[], element: Element, event: Event, options: faces.ajax.RequestOptions,
                 facesAjaxRequest: Function): void {
    this.removeFromQueue(queue, element, event);

    for (const queueItem of queue) {
      if (queueItem.element === element && queueItem.event?.type === event?.type && queueItem.inProgress
          && JSON.stringify(queueItem.executeValues) === JSON
              .stringify(this.getExecuteValues(element, options?.execute))
          && this.isSubset(queueItem.renderIds, this.getIdSet(options?.render))) {
        return;
      }
    }

    queue.push({
      element: element,
      event: event,
      executeValues: this.getExecuteValues(element, options?.execute),
      renderIds: this.getIdSet(options?.render),
      func: facesAjaxRequest,
      inProgress: false
    });
    this.processQueue(queue);
  }

  /**
   * Remove all inactive elements from the queue. Elements which are in progress are ignored.
   */
  static removeFromQueue(queue: QueueItem[], element: Element, event: Event): void {
    for (let i = 0; i < queue.length; i++) {
      if (queue[i].element === element && queue[i].event?.type === event?.type) {
        if (!queue[i].inProgress) {
          queue.splice(i, 1);
          i--;
        }
      }
    }
  }

  /**
   * Get all values from the "execute" ids. Keep in mind that the "<f:ajax execute>" parameter always contains a
   * "@this", so the source element must also be included.
   * @param element any dom element no matter being it HTML or JSF, from which the event is emitted
   * @param execute execute ids
   */
  static getExecuteValues(element: Element, execute?: string): string[] {
    const payload: string[] = [];

    const ids = new Set<string>();
    ids.add(element.id);

    if (execute) {
      const executeIds = execute.split(" ");
      for (const executeId of executeIds) {
        ids.add(executeId);
      }
    }

    for (const id of ids) {
      const htmlElement = document.getElementById(id);
      const inputs = htmlElement.querySelectorAll("input");
      const options = htmlElement.querySelectorAll("option");
      const textareas = htmlElement.querySelectorAll("textarea");

      for (const input of inputs) {
        if (input.type === "checkbox" || input.type === "radio") {
          payload.push(String(input.checked));
        } else {
          payload.push(input.value);
        }
      }
      for (const option of options) {
        payload.push(String(option.selected));
      }
      for (const textarea of textareas) {
        payload.push(textarea.value);
      }
    }

    return payload;
  }

  static getIdSet(idString?: string): Set<string> {
    const idSet = new Set<string>();
    if (idString) {
      for (const id of idString.split(" ")) {
        idSet.add(id);
      }
    }
    return idSet;
  }

  static isSubset(mainSet: Set<string>, subSet: Set<string>): boolean {
    for (const value of subSet) {
      if (!mainSet.has(value)) {
        return false;
      }
    }
    return true;
  }

  static processQueue(queue: QueueItem[]): void {
    if (queue.length > 0 && !queue[0].inProgress) {
      queue[0].inProgress = true;
      queue[0].func();
    }
  }

  static ajaxEventListener(queue: QueueItem[], source: Element,
                           type: "event" | "error",
                           status: faces.AjaxEventStatus | faces.AjaxErrorStatus | "clientError" | "timeout"): void {
    if (queue.length > 0 && queue[0].inProgress
        && source === queue[0].element
        && ((type === "event" && status === "success") || type === "error")) {
      queue.shift();
      this.processQueue(queue);
    }
  }
}
