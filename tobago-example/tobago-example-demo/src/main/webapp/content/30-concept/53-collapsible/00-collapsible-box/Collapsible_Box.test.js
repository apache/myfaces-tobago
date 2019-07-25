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

import {jQueryFrameFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("Simple Collapsible Box: show -> hide transition", function (assert) {
  var showFn = jQueryFrameFn("#page\\:mainForm\\:controller\\:show");
  var hideFn = jQueryFrameFn("#page\\:mainForm\\:controller\\:hide");
  var contentFn = jQueryFrameFn("#page\\:mainForm\\:controller\\:content");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(contentFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(contentFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Simple Collapsible Box: hide -> show transition", function (assert) {
  var showFn = jQueryFrameFn("#page\\:mainForm\\:controller\\:show");
  var hideFn = jQueryFrameFn("#page\\:mainForm\\:controller\\:hide");
  var contentFn = jQueryFrameFn("#page\\:mainForm\\:controller\\:content");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(contentFn().length, 0);
  });
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(contentFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: open both boxes", function (assert) {
  var show1Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:show1");
  var show2Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:show2");
  var content1Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:content1");
  var content2Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:content2");
  var content2Length = content2Fn().length;

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    show1Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn().length, 1);
    assert.equal(content2Fn().length, content2Length);
  });
  TTT.action(function () {
    show2Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn().length, 1);
    assert.equal(content2Fn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: open box 1, close box 2", function (assert) {
  var show1Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:show1");
  var hide2Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:hide2");
  var content1Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:content1");
  var content2Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:content2");
  var content2Length = content2Fn().length;

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    show1Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn().length, 1);
    assert.equal(content2Fn().length, content2Length);
  });
  TTT.action(function () {
    hide2Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn().length, 1);
    assert.equal(content2Fn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: close box 1, open box 2", function (assert) {
  var hide1Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:hide1");
  var show2Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:show2");
  var content1Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:content1");
  var content2Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:content2");
  var content2Length = content2Fn().length;

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hide1Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn().length, 0);
    assert.equal(content2Fn().length, content2Length);
  });
  TTT.action(function () {
    show2Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn().length, 0);
    assert.equal(content2Fn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: close both boxes", function (assert) {
  var hide1Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:hide1");
  var hide2Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:hide2");
  var content1Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:content1");
  var content2Fn = jQueryFrameFn("#page\\:mainForm\\:server\\:content2");
  var content2Length = content2Fn().length;

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hide1Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn().length, 0);
    assert.equal(content2Fn().length, content2Length);
  });
  TTT.action(function () {
    hide2Fn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(content1Fn().length, 0);
    assert.equal(content2Fn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Client Side: show -> hide transition", function (assert) {
  var showFn = jQueryFrameFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  var hideFn = jQueryFrameFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  var boxFn = jQueryFrameFn("#page\\:mainForm\\:client\\:noRequestBox");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().hasClass("tobago-collapsed"), false);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().hasClass("tobago-collapsed"), true);
  });
  TTT.startTest();
});

QUnit.test("Client Side: hide -> show transition", function (assert) {
  var showFn = jQueryFrameFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  var hideFn = jQueryFrameFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  var boxFn = jQueryFrameFn("#page\\:mainForm\\:client\\:noRequestBox");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().hasClass("tobago-collapsed"), true);
  });
  TTT.action(function () {
    showFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().hasClass("tobago-collapsed"), false);
  });
  TTT.startTest();
});

QUnit.test("Client Side: hide content and submit empty string", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var showFn = jQueryFrameFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  var hideFn = jQueryFrameFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  var boxFn = jQueryFrameFn("#page\\:mainForm\\:client\\:noRequestBox");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:client\\:inNoRequestBox\\:\\:field");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:client\\:submitNoRequestBox");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(boxFn().hasClass("tobago-collapsed"), true);
  });
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Ajax: show -> hide transition", function (assert) {
  var showFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  var hideFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Ajax: hide -> show transition", function (assert) {
  var showFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  var hideFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 0);
  });
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Ajax: hide content and submit empty string", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages .tobago-messages");
  var showFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  var hideFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:submitAjaxBox");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("");
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 0);
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
