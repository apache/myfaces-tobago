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

QUnit.test("Simple Collapsible Box: show -> hide transition", function (assert) {
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:controller\\:show");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:controller\\:hide");
  let contentFn = testFrameQuerySelectorFn("#page\\:mainForm\\:controller\\:content");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(contentFn() !== null);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(contentFn(), null);
  });
  TTT.startTest();
});

QUnit.test("Simple Collapsible Box: hide -> show transition", function (assert) {
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:controller\\:show");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:controller\\:hide");
  let contentFn = testFrameQuerySelectorFn("#page\\:mainForm\\:controller\\:content");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(contentFn(), null);
  });
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(contentFn() !== null);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: open both boxes", function (assert) {
  let show1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:show1");
  let show2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:show2");
  let content1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:content1");
  let content2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:content2");
  let content2Length = content2Fn().length;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    show1Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.ok(content1Fn() !== null);
    assert.equal(content2Fn().length, content2Length);
  });
  TTT.action(function () {
    show2Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.ok(content1Fn() !== null);
    assert.ok(content2Fn() !== null);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: open box 1, close box 2", function (assert) {
  let show1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:show1");
  let hide2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:hide2");
  let content1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:content1");
  let content2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:content2");
  let content2Length = content2Fn().length;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    show1Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.ok(content1Fn() !== null);
    assert.equal(content2Fn().length, content2Length);
  });
  TTT.action(function () {
    hide2Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.ok(content1Fn() !== null);
    assert.equal(content2Fn(), null);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: close box 1, open box 2", function (assert) {
  let hide1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:hide1");
  let show2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:show2");
  let content1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:content1");
  let content2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:content2");
  let existContent2 = content2Fn() !== null;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hide1Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn(), null);
    assert.equal(content2Fn() !== null, existContent2);
  });
  TTT.action(function () {
    show2Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn(), null);
    assert.ok(content2Fn() !== null);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: close both boxes", function (assert) {
  let hide1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:hide1");
  let hide2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:hide2");
  let content1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:content1");
  let content2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:server\\:content2");
  let existContent2 = content2Fn() !== null;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hide1Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn(), null);
    assert.equal(content2Fn() !== null, existContent2);
  });
  TTT.action(function () {
    hide2Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn(), null);
    assert.equal(content2Fn(), null);
  });
  TTT.startTest();
});

QUnit.test("Client Side: show -> hide transition", function (assert) {
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  let boxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:noRequestBox");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().classList.contains("tobago-collapsed"), false);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().classList.contains("tobago-collapsed"), true);
  });
  TTT.startTest();
});

QUnit.test("Client Side: hide -> show transition", function (assert) {
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  let boxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:noRequestBox");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().classList.contains("tobago-collapsed"), true);
  });
  TTT.action(function () {
    showFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().classList.contains("tobago-collapsed"), false);
  });
  TTT.startTest();
});

QUnit.test("Client Side: hide content and submit empty string", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  let boxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:noRequestBox");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:inNoRequestBox\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:client\\:submitNoRequestBox");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().classList.contains("tobago-collapsed"), true);
  });
  TTT.action(function () {
    inFn().value = "";
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Ajax: show -> hide transition", function (assert) {
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn(), null);
  });
  TTT.startTest();
});

QUnit.test("Ajax: hide -> show transition", function (assert) {
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn(), null);
  });
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(inFn() !== null);
  });
  TTT.startTest();
});

QUnit.test("Ajax: hide content and submit empty string", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages .alert");
  let showFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajax\\:submitAjaxBox");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(inFn() !== null);
  });
  TTT.action(function () {
    inFn().value = "";
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn(), null);
  });
  TTT.action(function () {
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});
