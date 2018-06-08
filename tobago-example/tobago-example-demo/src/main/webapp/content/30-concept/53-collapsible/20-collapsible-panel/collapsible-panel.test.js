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

QUnit.test("Simple Panel", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let showFn = jQueryFrameFn("#\\page\\:mainForm\\:simple\\:showSimple");
  let hideFn = jQueryFrameFn("#\\page\\:mainForm\\:simple\\:hideSimple");
  let submitFn = jQueryFrameFn("#\\page\\:mainForm\\:simple\\:submitSimple");
  let panelCollapsedFn = jQueryFrameFn("#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.action(function () {
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let showFn = jQueryFrameFn("#\\page\\:mainForm\\:server\\:showServer");
  let hideFn = jQueryFrameFn("#\\page\\:mainForm\\:server\\:hideServer");
  let submitFn = jQueryFrameFn("#\\page\\:mainForm\\:server\\:submitServer");
  let panelCollapsedFn = jQueryFrameFn("#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.action(function () {
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Client Side", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let showFn = jQueryFrameFn("#\\page\\:mainForm\\:client\\:showClient");
  let hideFn = jQueryFrameFn("#\\page\\:mainForm\\:client\\:hideClient");
  let submitFn = jQueryFrameFn("#\\page\\:mainForm\\:client\\:submitClient");
  let panelCollapsedFn = jQueryFrameFn("#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().val(), "true");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Ajax", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let showFn = jQueryFrameFn("#\\page\\:mainForm\\:ajax\\:showAjax");
  let hideFn = jQueryFrameFn("#\\page\\:mainForm\\:ajax\\:hideAjax");
  let submitFn = jQueryFrameFn("#\\page\\:mainForm\\:ajax\\:submitAjax");
  let panelCollapsedFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    showFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(panelCollapsedFn().val(), "false");
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(panelCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.action(function () {
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(panelCollapsedFn().val(), "true");
    assert.equal(inFn().length, 0);
  });
  TTT.startTest();
});
