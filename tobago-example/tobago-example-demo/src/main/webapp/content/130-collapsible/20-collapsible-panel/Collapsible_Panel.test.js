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

import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Simple Panel: show -> hide transition", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:simple\\:showSimple");
  let hideFn = querySelectorFn("#page\\:mainForm\\:simple\\:hideSimple");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.event("click", hideFn, () => panelCollapsedFn().value === "true" && inFn() === null);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Simple Panel: hide -> show transition", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:simple\\:showSimple");
  let hideFn = querySelectorFn("#page\\:mainForm\\:simple\\:hideSimple");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "true" && inFn() === null,
      null, "click", hideFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.event("click", showFn, () => panelCollapsedFn().value === "false" && inFn() !== null);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Simple Panel: collapsed = false; submit valid input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:simple\\:showSimple");
  let submitFn = querySelectorFn("#page\\:mainForm\\:simple\\:submitSimple");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.setup(
      () => messagesFn().length > 0,
      () => inFn().value = "",
      "click", submitFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "Alice");
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Simple Panel: collapsed = false; submit empty input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:simple\\:showSimple");
  let submitFn = querySelectorFn("#page\\:mainForm\\:simple\\:submitSimple");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.setup(
      () => messagesFn().length === 0,
      () => inFn().value = "Bob",
      "click", submitFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => inFn().value = "");
  test.event("click", submitFn, () => messagesFn().length > 0);
  test.do(() => expect(messagesFn().length).toBeGreaterThan(0));
  test.start();
});

it("Simple Panel: valid input; show -> hide transition; submit", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:simple\\:showSimple");
  let hideFn = querySelectorFn("#page\\:mainForm\\:simple\\:hideSimple");
  let submitFn = querySelectorFn("#page\\:mainForm\\:simple\\:submitSimple");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");
  let clientSubmitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let clientInFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "Charlie");
  test.event("click", hideFn, () => panelCollapsedFn().value === "true" && inFn() === null);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.setup(
      () => messagesFn().length > 0,
      () => clientInFn().value = "",
      "click", clientSubmitFn);
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Simple Panel: empty input; show -> hide transition; submit", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:simple\\:showSimple");
  let hideFn = querySelectorFn("#page\\:mainForm\\:simple\\:hideSimple");
  let submitFn = querySelectorFn("#page\\:mainForm\\:simple\\:submitSimple");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");
  let clientSubmitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let clientInFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "");
  test.event("click", hideFn, () => panelCollapsedFn().value === "true" && inFn() === null);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.setup(
      () => messagesFn().length > 0,
      () => clientInFn().value = "",
      "click", clientSubmitFn);
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Full Server Request: show -> hide transition", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:server\\:showServer");
  let hideFn = querySelectorFn("#page\\:mainForm\\:server\\:hideServer");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.event("click", hideFn, () => panelCollapsedFn().value === "true" && inFn() === null);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Full Server Request: hide -> show transition", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:server\\:showServer");
  let hideFn = querySelectorFn("#page\\:mainForm\\:server\\:hideServer");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "true" && inFn() === null,
      null, "click", hideFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.event("click", showFn, () => panelCollapsedFn().value === "false" && inFn() !== null);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Full Server Request: collapsed = false; submit valid input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:server\\:showServer");
  let submitFn = querySelectorFn("#page\\:mainForm\\:server\\:submitServer");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.setup(
      () => messagesFn().length > 0,
      () => inFn().value = "",
      "click", submitFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "Alice");
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Full Server Request: collapsed = false; submit empty input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:server\\:showServer");
  let submitFn = querySelectorFn("#page\\:mainForm\\:server\\:submitServer");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.setup(
      () => messagesFn().length === 0,
      () => inFn().value = "Bob",
      "click", submitFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => inFn().value = "");
  test.event("click", submitFn, () => messagesFn().length > 0);
  test.do(() => expect(messagesFn().length).toBeGreaterThan(0));
  test.start();
});

it("Full Server Request: valid input; show -> hide transition; submit", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:server\\:showServer");
  let hideFn = querySelectorFn("#page\\:mainForm\\:server\\:hideServer");
  let submitFn = querySelectorFn("#page\\:mainForm\\:server\\:submitServer");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");
  let clientSubmitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let clientInFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "Charlie");
  test.event("click", hideFn, () => panelCollapsedFn().value === "true" && inFn() === null);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.setup(
      () => messagesFn().length > 0,
      () => clientInFn().value = "",
      "click", clientSubmitFn);
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Full Server Request: empty input; show -> hide transition; submit", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:server\\:showServer");
  let hideFn = querySelectorFn("#page\\:mainForm\\:server\\:hideServer");
  let submitFn = querySelectorFn("#page\\:mainForm\\:server\\:submitServer");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");
  let clientSubmitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let clientInFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "");
  test.event("click", hideFn, () => panelCollapsedFn().value === "true" && inFn() === null);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.setup(
      () => messagesFn().length > 0,
      () => clientInFn().value = "",
      "click", clientSubmitFn);
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Client Sided: show -> hide transition", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:client\\:showClient");
  let hideFn = querySelectorFn("#page\\:mainForm\\:client\\:hideClient");
  let submitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => messagesFn().length === 0,
      () => inFn().value = "Alice",
      "click", submitFn);
  test.setup(
      () => panelCollapsedFn().value === "false",
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.event("click", hideFn, () => panelCollapsedFn().value === "true");
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Client Sided: hide -> show transition", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:client\\:showClient");
  let hideFn = querySelectorFn("#page\\:mainForm\\:client\\:hideClient");
  let submitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => messagesFn().length === 0,
      () => inFn().value = "Bob",
      "click", submitFn);
  test.setup(
      () => panelCollapsedFn().value === "true",
      null, "click", hideFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.event("click", showFn, () => panelCollapsedFn().value === "false");
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Client Sided: collapsed = false; submit valid input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:client\\:showClient");
  let submitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => messagesFn().length > 0,
      () => inFn().value = "",
      "click", submitFn);
  test.setup(
      () => panelCollapsedFn().value === "false",
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "Charlie");
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Client Sided: collapsed = false; submit empty input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:client\\:showClient");
  let submitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => messagesFn().length === 0,
      () => inFn().value = "Bob",
      "click", submitFn);
  test.setup(
      () => panelCollapsedFn().value === "false",
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => inFn().value = "");
  test.event("click", submitFn, () => messagesFn().length > 0);
  test.do(() => expect(messagesFn().length).toBeGreaterThan(0));
  test.start();
});

it("Client Sided: collapsed = true; submit valid input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let hideFn = querySelectorFn("#page\\:mainForm\\:client\\:hideClient");
  let submitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => messagesFn().length > 0,
      () => inFn().value = "",
      "click", submitFn);
  test.setup(
      () => panelCollapsedFn().value === "true",
      null, "click", hideFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "Dave");
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Client Sided: collapsed = true; submit empty input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let hideFn = querySelectorFn("#page\\:mainForm\\:client\\:hideClient");
  let submitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => messagesFn().length === 0,
      () => inFn().value = "Eve",
      "click", submitFn);
  test.setup(
      () => panelCollapsedFn().value === "true",
      null, "click", hideFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => inFn().value = "");
  test.event("click", submitFn, () => messagesFn().length > 0);
  test.do(() => expect(messagesFn().length).toBeGreaterThan(0));
  test.start();
});

it("Ajax: show -> hide transition", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjax");
  let hideFn = querySelectorFn("#page\\:mainForm\\:ajax\\:hideAjax");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");
  let clientSubmitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let clientInFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.setup(
      () => messagesFn().length === 0,
      () => clientInFn().value = "Alice",
      "click", clientSubmitFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.event("click", hideFn, () => panelCollapsedFn().value === "true" && inFn() === null);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Ajax: hide -> show transition", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjax");
  let hideFn = querySelectorFn("#page\\:mainForm\\:ajax\\:hideAjax");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");
  let clientSubmitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let clientInFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "true" && inFn() === null,
      null, "click", hideFn);
  test.setup(
      () => messagesFn().length === 0,
      () => clientInFn().value = "Bob",
      "click", clientSubmitFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.event("click", showFn, () => panelCollapsedFn().value === "false" && inFn() !== null);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Ajax: collapsed = false; submit valid input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjax");
  let submitFn = querySelectorFn("#page\\:mainForm\\:ajax\\:submitAjax");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.setup(
      () => messagesFn().length > 0,
      () => inFn().value = "",
      "click", submitFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "Alice");
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Ajax: collapsed = false; submit empty input", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjax");
  let submitFn = querySelectorFn("#page\\:mainForm\\:ajax\\:submitAjax");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.setup(
      () => messagesFn().length === 0,
      () => inFn().value = "Bob",
      "click", submitFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => inFn().value = "");
  test.event("click", submitFn, () => messagesFn().length > 0);
  test.do(() => expect(messagesFn().length).toBeGreaterThan(0));
  test.start();
});

it("Ajax: valid input; show -> hide transition; submit", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjax");
  let hideFn = querySelectorFn("#page\\:mainForm\\:ajax\\:hideAjax");
  let submitFn = querySelectorFn("#page\\:mainForm\\:ajax\\:submitAjax");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");
  let clientSubmitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let clientInFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "Charlie");
  test.event("click", hideFn, () => panelCollapsedFn().value === "true" && inFn() === null);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.setup(
      () => messagesFn().length > 0,
      () => clientInFn().value = "",
      "click", clientSubmitFn);
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Ajax: empty input; show -> hide transition; submit", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjax");
  let hideFn = querySelectorFn("#page\\:mainForm\\:ajax\\:hideAjax");
  let submitFn = querySelectorFn("#page\\:mainForm\\:ajax\\:submitAjax");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");
  let clientSubmitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let clientInFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => panelCollapsedFn().value === "false" && inFn() !== null,
      null, "click", showFn);
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => inFn().value = "");
  test.event("click", hideFn, () => panelCollapsedFn().value === "true" && inFn() === null);
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn()).toBeNull());
  test.setup(
      () => messagesFn().length > 0,
      () => clientInFn().value = "",
      "click", clientSubmitFn);
  test.event("click", submitFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});
