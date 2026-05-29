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

import {AjaxQueueStatic} from "./tobago-ajax-queue-static";

export interface QueueItem {
  element: Element;
  event: Event;
  executeValues: string[];
  renderIds: Set<string>;
  func: Function;
  inProgress: boolean;
}

/**
 * The following rules should apply to the Ajax request queue:
 * - Only one request is active at the same time.
 * - A new request is ignored if the exact same request (based on element/event.type/executeValues/renderIds) is
 *   currently in progress.
 * - A new request replaces a queued request (based on element/event.type) and is moved to the end of the queue.
 * - A queued request will be removed if the element is not connected to the DOM.
 */
class AjaxQueue {
  private queue: QueueItem[] = [];

  request(elementOrId: Element | string, event?: Event, options: faces.ajax.RequestOptions = {}): void {
    const element = elementOrId instanceof Element ? elementOrId : document.getElementById(elementOrId);

    const currentOnEvent = options.onevent;
    options.onevent = (data: faces.AjaxEvent) => {
      currentOnEvent(data);
      this.ajaxEventListener(data.source, data.type, data.status);
    };
    const currentOnError = options.onerror;
    options.onerror = (data: faces.AjaxError) => {
      currentOnError(data);
      this.ajaxEventListener(data.source, data.type, data.status);
    };

    AjaxQueueStatic.request(this.queue, element, event, options, () => faces.ajax.request(element, event, options));
  }

  private ajaxEventListener(source: Element,
                            type: "event" | "error",
                            status: faces.AjaxEventStatus | faces.AjaxErrorStatus | "clientError" | "timeout"): void {
    AjaxQueueStatic.ajaxEventListener(this.queue, source, type, status);
  }
}

export const ajaxQueue = new AjaxQueue();

/**
 * To use the Ajax queue from Tobago, replace "faces.ajax.request(...)" with "tobago.ajax.request(...)".
 * "tobago.ajax.request(...)" calls "faces.ajax.request(...)", so "faces.ajax.addOnEvent()" and "faces.ajax.addOnError"
 * still work.
 */
(window as any).tobago = {
  ajax: {
    request: (elementOrId: Element | string, event?: Event, options?: faces.ajax.RequestOptions) =>
        ajaxQueue.request(elementOrId, event, options)
  }
};
