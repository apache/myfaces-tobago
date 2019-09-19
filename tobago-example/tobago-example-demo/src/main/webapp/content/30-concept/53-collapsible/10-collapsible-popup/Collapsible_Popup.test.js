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

QUnit.test("Simple Popup", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let openFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:open1");
  let submitOnPageFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:submitOnPage1");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:controllerPopup\\:in1\\:\\:field");
  let submitOnPopupFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:controllerPopup\\:submitOnPopup1");
  let closeFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simple\\:controllerPopup\\:close1");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    openFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "some text";
    submitOnPopupFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.ok(inFn() !== null);
    assert.equal(inFn().value, "some text");
  });
  TTT.action(function () {
    inFn().value = "";
    submitOnPopupFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.ok(inFn() !== null);
    assert.equal(inFn().value, "");
  });
  TTT.action(function () {
    closeFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn(), null);
  });
  TTT.action(function () {
    submitOnPageFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let openFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:open2");
  let submitOnPageFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:submitOnPage2");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:in2\\:\\:field");
  let submitOnPopupFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:submitOnPopup2");
  let closeFn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:close2");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    openFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "some text";
    submitOnPopupFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.ok(inFn() !== null);
    assert.equal(inFn().value, "some text");
  });
  TTT.action(function () {
    inFn().value = "";
    submitOnPopupFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.ok(inFn() !== null);
    assert.equal(inFn().value, "");
  });
  TTT.action(function () {
    closeFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn(), null);
  });
  TTT.action(function () {
    submitOnPageFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Client Side", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let openFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:open3");
  let submitOnPageFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:submitOnPage3");
  let popupCollapsedFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:clientPopup\\:\\:collapse");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:clientPopup\\:in3\\:\\:field");
  let submitOnPopupFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:clientPopup\\:submitOnPopup3");
  let closeFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:clientPopup\\:close3");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    openFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.equal(popupCollapsedFn().value, "false");
  });
  TTT.action(function () {
    closeFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.equal(popupCollapsedFn().value, "true");
  });
  TTT.action(function () {
    openFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.equal(popupCollapsedFn().value, "false");
  });
  TTT.action(function () {
    inFn().value = "some text";
    submitOnPopupFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(popupCollapsedFn().value, "true");
  });
  TTT.action(function () {
    openFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.equal(popupCollapsedFn().value, "false");
  });
  TTT.action(function () {
    inFn().value = "";
    submitOnPopupFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(popupCollapsedFn().value, "true");
  });
  TTT.action(function () {
    submitOnPageFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});
