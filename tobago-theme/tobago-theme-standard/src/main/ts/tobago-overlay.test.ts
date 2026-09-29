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

import {Overlay} from "./tobago-overlay";
import {OverlayType} from "./tobago-overlay-type";

beforeAll(() => {
  Object.defineProperty(globalThis, "faces", {
    configurable: true,
    value: {
      ajax: {
        request: jest.fn(),
        response: jest.fn(),
        addOnError: jest.fn(),
        addOnEvent: jest.fn()
      }
    }
  });

  document.dispatchEvent(new Event("tobago.init"));
  document.body.innerHTML = `<tobago-page><form><div class="tobago-page-menuStore"></div></form></tobago-page>`;
});

afterEach(() => {
  document.body.replaceChildren();
});

test("Ignore invalid IDs", () => {
  Overlay.render("invalidId", OverlayType.ajax);
  Overlay.remove("invalidId");

  expect(true).toBeTruthy();
});
