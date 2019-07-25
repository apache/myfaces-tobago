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

QUnit.test("Simple Panel", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var submitFn = jQueryFrameFn("#\\page\\:mainForm\\:simple\\:submitSimple");
  var showFn = jQueryFrameFn("#\\page\\:mainForm\\:simple\\:showSimple");
  var hideFn = jQueryFrameFn("#\\page\\:mainForm\\:simple\\:hideSimple");
  var sectionCollapsedFn = jQueryFrameFn("#page\\:mainForm\\:simple\\:simpleSection\\:\\:collapse");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(sectionCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.action(function () {
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(sectionCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var submitFn = jQueryFrameFn("#\\page\\:mainForm\\:server\\:submitServer");
  var showFn = jQueryFrameFn("#\\page\\:mainForm\\:server\\:showServer");
  var hideFn = jQueryFrameFn("#\\page\\:mainForm\\:server\\:hideServer");
  var sectionCollapsedFn = jQueryFrameFn("#page\\:mainForm\\:server\\:fullRequestSection\\:\\:collapse");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(sectionCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.action(function () {
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(sectionCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Client Side", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var submitFn = jQueryFrameFn("#\\page\\:mainForm\\:client\\:submitClient");
  var showFn = jQueryFrameFn("#\\page\\:mainForm\\:client\\:showClient");
  var hideFn = jQueryFrameFn("#\\page\\:mainForm\\:client\\:hideClient");
  var sectionCollapsedFn = jQueryFrameFn("#page\\:mainForm\\:client\\:clientSection\\:\\:collapse");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.asserts(2, function () {
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(sectionCollapsedFn().val(), "true");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Ajax", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var submitFn = jQueryFrameFn("#\\page\\:mainForm\\:ajax\\:submitAjax");
  var showFn = jQueryFrameFn("#\\page\\:mainForm\\:ajax\\:showAjax");
  var hideFn = jQueryFrameFn("#\\page\\:mainForm\\:ajax\\:hideAjax");
  var sectionCollapsedFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:ajaxSection\\:\\:collapse");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(sectionCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(sectionCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.action(function () {
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(sectionCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.startTest();
});
