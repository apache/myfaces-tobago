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

import {Behavior} from "./tobago-behavior";
import {BehaviorMode} from "./tobago-behavior-mode";

const addOnEvent = jest.fn();
beforeAll(() => {
  Object.defineProperty(globalThis, "faces", {
    configurable: true,
    value: {
      ajax: {
        addOnEvent
      }
    }
  });
  document.dispatchEvent(new Event("tobago.init"));
});

beforeEach(() => {
  document.body.innerHTML = `<tobago-page><form id="page:mainForm"></form></tobago-page>`;
});

afterEach(() => {
  jest.restoreAllMocks();
  document.body.replaceChildren();
});

afterAll(() => {
  Reflect.deleteProperty(globalThis, "faces");
});

test("BehaviorMode.client", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>Section</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">2026-12-31</span>
    </tobago-out>
    <tobago-panel id="page:mainForm:panel">
      <input type="hidden" name="page:mainForm:panel::collapse" id="page:mainForm:panel::collapse" value="false">
      <tobago-in id="page:mainForm:input" class="tobago-auto-spacing">
        <input type="text" name="page:mainForm:input" id="page:mainForm:input::field" value="Content"
               class="form-control"></tobago-in>
    </tobago-panel>
    <button type="button" id="page:mainForm:hide" name="page:mainForm:hide"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:hide" omit="omit" collapse-operation="hide"
                       collapse-target="page:mainForm:panel"></tobago-behavior>
      <span>Hide</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:hide']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.client);
  expect(behavior.clientSideAnimation).toBe(true);
});

test("BehaviorMode.ajax renderer='timestamp'", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>Section</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">2026-12-31</span>
    </tobago-out>
    <tobago-panel id="page:mainForm:panel">
      <input type="hidden" name="page:mainForm:panel::collapse" id="page:mainForm:panel::collapse" value="false">
      <tobago-in id="page:mainForm:input" class="tobago-auto-spacing">
        <input type="text" name="page:mainForm:input" id="page:mainForm:input::field" value="Content"
               class="form-control"></tobago-in>
    </tobago-panel>
    <button type="button" id="page:mainForm:hide" name="page:mainForm:hide"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:hide" execute="page:mainForm:hide"
                       render="page:mainForm:timestamp" omit="omit" collapse-operation="hide"
                       collapse-target="page:mainForm:panel"></tobago-behavior>
      <span>Hide</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:hide']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.ajax);
  expect(behavior.clientSideAnimation).toBe(true);
  expect(behavior.collapseOperationExecute).toBe("page:mainForm:hide");
});

test("BehaviorMode.ajax renderer='panel'", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>Section</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">2026-12-31</span>
    </tobago-out>
    <tobago-panel id="page:mainForm:panel">
      <input type="hidden" name="page:mainForm:panel::collapse" id="page:mainForm:panel::collapse" value="false">
      <tobago-in id="page:mainForm:input" class="tobago-auto-spacing">
        <input type="text" name="page:mainForm:input" id="page:mainForm:input::field" value="Content"
               class="form-control"></tobago-in>
    </tobago-panel>
    <button type="button" id="page:mainForm:hide" name="page:mainForm:hide"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:hide" execute="page:mainForm:hide"
                       render="page:mainForm:panel" omit="omit" collapse-operation="hide"
                       collapse-target="page:mainForm:panel"></tobago-behavior>
      <span>Hide</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:hide']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.ajax);
  expect(behavior.clientSideAnimation).toBe(true);
  expect(behavior.collapseOperationExecute).toBe("page:mainForm:hide page:mainForm:panel");
});

test("BehaviorMode.ajax renderer='panel parent'", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>Section</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">2026-12-31</span>
    </tobago-out>
    <tobago-panel id="page:mainForm:panel">
      <input type="hidden" name="page:mainForm:panel::collapse" id="page:mainForm:panel::collapse" value="false">
      <tobago-in id="page:mainForm:input" class="tobago-auto-spacing">
        <input type="text" name="page:mainForm:input" id="page:mainForm:input::field" value="Content"
               class="form-control"></tobago-in>
    </tobago-panel>
    <button type="button" id="page:mainForm:hide" name="page:mainForm:hide"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:hide" execute="page:mainForm:hide"
                       render="page:mainForm:section" omit="omit" collapse-operation="hide"
                       collapse-target="page:mainForm:panel"></tobago-behavior>
      <span>Hide</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:hide']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.ajax);
  expect(behavior.clientSideAnimation).toBe(true);
  expect(behavior.collapseOperationExecute).toBe("page:mainForm:hide page:mainForm:panel");
});

test("BehaviorMode.ajax renderer='panel child'", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>Section</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">2026-12-31</span>
    </tobago-out>
    <tobago-panel id="page:mainForm:panel">
      <input type="hidden" name="page:mainForm:panel::collapse" id="page:mainForm:panel::collapse" value="false">
      <tobago-in id="page:mainForm:input" class="tobago-auto-spacing">
        <input type="text" name="page:mainForm:input" id="page:mainForm:input::field" value="Content"
               class="form-control"></tobago-in>
    </tobago-panel>
    <button type="button" id="page:mainForm:hide" name="page:mainForm:hide"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:hide" execute="page:mainForm:hide"
                       render="page:mainForm:input" omit="omit" collapse-operation="hide"
                       collapse-target="page:mainForm:panel"></tobago-behavior>
      <span>Hide</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:hide']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.ajax);
  expect(behavior.clientSideAnimation).toBe(true);
  expect(behavior.collapseOperationExecute).toBe("page:mainForm:hide");
});

test("BehaviorMode.ajax renderer='timestamp'", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>TOBAGO-2543</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">1789559651034</span>
    </tobago-out>
    <tobago-popup id="page:mainForm:popup" class="modal fade" tabindex="-1" role="dialog">
      <div id="page:mainForm:popup::dialog" class="modal-dialog" role="document">
        <div class="modal-content">
          <input type="hidden" name="page:mainForm:popup::collapse" id="page:mainForm:popup::collapse"
                 value="true">
          <tobago-in id="page:mainForm:popup:input" class="tobago-auto-spacing">
            <input type="text" name="page:mainForm:popup:input"
                   id="page:mainForm:popup:input::field" value="Content" class="form-control">
          </tobago-in>
        </div>
      </div>
    </tobago-popup>
    <button type="button" id="page:mainForm:show" name="page:mainForm:show"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:show" execute="page:mainForm:show"
                       render="page:mainForm:timestamp" omit="omit" collapse-operation="show"
                       collapse-target="page:mainForm:popup"></tobago-behavior>
      <span>Show</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:show']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.ajax);
  expect(behavior.clientSideAnimation).toBe(true);
  expect(behavior.collapseOperationExecute).toBe("page:mainForm:show");
});

test("BehaviorMode.ajax renderer='popup'", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>TOBAGO-2543</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">1789559651034</span>
    </tobago-out>
    <tobago-popup id="page:mainForm:popup" class="modal fade" tabindex="-1" role="dialog">
      <div id="page:mainForm:popup::dialog" class="modal-dialog" role="document">
        <div class="modal-content">
          <input type="hidden" name="page:mainForm:popup::collapse" id="page:mainForm:popup::collapse"
                 value="true">
          <tobago-in id="page:mainForm:popup:input" class="tobago-auto-spacing">
            <input type="text" name="page:mainForm:popup:input"
                   id="page:mainForm:popup:input::field" value="Content" class="form-control">
          </tobago-in>
        </div>
      </div>
    </tobago-popup>
    <button type="button" id="page:mainForm:show" name="page:mainForm:show"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:show" execute="page:mainForm:show"
                       render="page:mainForm:popup" omit="omit" collapse-operation="show"
                       collapse-target="page:mainForm:popup"></tobago-behavior>
      <span>Show</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:show']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.ajax);
  expect(behavior.clientSideAnimation).toBe(false);
  expect(behavior.collapseOperationExecute).toBe("page:mainForm:show page:mainForm:popup");
});

test("BehaviorMode.ajax renderer='popup parent'", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>TOBAGO-2543</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">1789559651034</span>
    </tobago-out>
    <tobago-popup id="page:mainForm:popup" class="modal fade" tabindex="-1" role="dialog">
      <div id="page:mainForm:popup::dialog" class="modal-dialog" role="document">
        <div class="modal-content">
          <input type="hidden" name="page:mainForm:popup::collapse" id="page:mainForm:popup::collapse"
                 value="true">
          <tobago-in id="page:mainForm:popup:input" class="tobago-auto-spacing">
            <input type="text" name="page:mainForm:popup:input"
                   id="page:mainForm:popup:input::field" value="Content" class="form-control">
          </tobago-in>
        </div>
      </div>
    </tobago-popup>
    <button type="button" id="page:mainForm:show" name="page:mainForm:show"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:show" execute="page:mainForm:show"
                       render="page:mainForm:section" omit="omit" collapse-operation="show"
                       collapse-target="page:mainForm:popup"></tobago-behavior>
      <span>Show</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:show']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.ajax);
  expect(behavior.clientSideAnimation).toBe(false);
  expect(behavior.collapseOperationExecute).toBe("page:mainForm:show page:mainForm:popup");
});

test("BehaviorMode.ajax renderer='popup child'", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>TOBAGO-2543</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">1789559651034</span>
    </tobago-out>
    <tobago-popup id="page:mainForm:popup" class="modal fade" tabindex="-1" role="dialog">
      <div id="page:mainForm:popup::dialog" class="modal-dialog" role="document">
        <div class="modal-content">
          <input type="hidden" name="page:mainForm:popup::collapse" id="page:mainForm:popup::collapse"
                 value="true">
          <tobago-in id="page:mainForm:popup:input" class="tobago-auto-spacing">
            <input type="text" name="page:mainForm:popup:input"
                   id="page:mainForm:popup:input::field" value="Content" class="form-control">
          </tobago-in>
        </div>
      </div>
    </tobago-popup>
    <button type="button" id="page:mainForm:show" name="page:mainForm:show"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:show" execute="page:mainForm:show"
                       render="page:mainForm:popup:input" omit="omit" collapse-operation="show"
                       collapse-target="page:mainForm:popup"></tobago-behavior>
      <span>Show</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:show']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.ajax);
  expect(behavior.clientSideAnimation).toBe(true);
  expect(behavior.collapseOperationExecute).toBe("page:mainForm:show");
});

test("BehaviorMode.full", () => {
  document.querySelector("form").insertAdjacentHTML("beforeend", `
<tobago-section id="page:mainForm:section" class="tobago-auto-spacing" data-tobago-level="2">
  <div class="tobago-header">
    <h2>
      <span>Section</span></h2></div>
  <div class="tobago-section-content">
    <tobago-out id="page:mainForm:timestamp" class="tobago-label-container tobago-auto-spacing"><label
            for="page:mainForm:timestamp" class="col-form-label">Timestamp</label><span class="form-control-plaintext">2026-12-31</span>
    </tobago-out>
    <tobago-panel id="page:mainForm:panel">
      <input type="hidden" name="page:mainForm:panel::collapse" id="page:mainForm:panel::collapse" value="false">
      <tobago-in id="page:mainForm:input" class="tobago-auto-spacing">
        <input type="text" name="page:mainForm:input" id="page:mainForm:input::field" value="Content"
               class="form-control"></tobago-in>
    </tobago-panel>
    <button type="button" id="page:mainForm:show" name="page:mainForm:show"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:show" collapse-operation="show"
                       collapse-target="page:mainForm:panel"></tobago-behavior>
      <span>Show</span></button>
    <button type="button" id="page:mainForm:hide" name="page:mainForm:hide"
            class="tobago-button btn btn-secondary tobago-auto-spacing">
      <tobago-behavior event="click" client-id="page:mainForm:hide" collapse-operation="hide"
                       collapse-target="page:mainForm:panel"></tobago-behavior>
      <span>Hide</span></button>
  </div>
</tobago-section>
`);

  const behavior: Behavior = document.querySelector("tobago-behavior[client-id='page:mainForm:hide']") as Behavior;
  expect(behavior).toBeInstanceOf(Behavior);
  expect(behavior.mode).toBe(BehaviorMode.full);
  expect(behavior.clientSideAnimation).toBe(false);
});
