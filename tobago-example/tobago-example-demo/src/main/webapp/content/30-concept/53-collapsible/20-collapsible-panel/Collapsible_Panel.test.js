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

import {testFrameQuerySelectorAllFn, testFrameQuerySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("Simple Panel", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:showSimple");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:hideSimple");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:submitSimple");
  let panelCollapsedFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "some text";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    hideFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().value, "true");
    assert.equal(inFn(), null);
  });
  TTT.action(function () {
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().value, "true");
    assert.equal(inFn(), null);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:showServer");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:hideServer");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:submitServer");
  let panelCollapsedFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "some text";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    hideFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().value, "true");
    assert.equal(inFn(), null);
  });
  TTT.action(function () {
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().value, "true");
    assert.equal(inFn(), null);
  });
  TTT.startTest();
});

QUnit.test("Client Side", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:showClient");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:hideClient");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let panelCollapsedFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "some text";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    hideFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().value, "true");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.startTest();
});

QUnit.test("Ajax", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:showAjax");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:hideAjax");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:submitAjax");
  let panelCollapsedFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "some text";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().value, "false");
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    hideFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().value, "true");
    assert.equal(inFn(), null);
  });
  TTT.action(function () {
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().value, "true");
    assert.equal(inFn(), null);
  });
  TTT.startTest();
});
