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

QUnit.test("Simple Popup", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var openFn = jQueryFrameFn("#page\\:mainForm\\:simple\\:open1");
  var submitOnPageFn = jQueryFrameFn("#page\\:mainForm\\:simple\\:submitOnPage1");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:simple\\:controllerPopup\\:in1\\:\\:field");
  var submitOnPopupFn = jQueryFrameFn("#page\\:mainForm\\:simple\\:controllerPopup\\:submitOnPopup1");
  var closeFn = jQueryFrameFn("#page\\:mainForm\\:simple\\:controllerPopup\\:close1");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    openFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitOnPopupFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(inFn().length, 1);
    assert.equal(inFn().val(), "some text");
  });
  TTT.action(function () {
    inFn().val("");
    submitOnPopupFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(inFn().length, 1);
    assert.equal(inFn().val(), "");
  });
  TTT.action(function () {
    closeFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 0);
  });
  TTT.action(function () {
    submitOnPageFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var openFn = jQueryFrameFn("#page\\:mainForm\\:server\\:open2");
  var submitOnPageFn = jQueryFrameFn("#page\\:mainForm\\:server\\:submitOnPage2");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:in2\\:\\:field");
  var submitOnPopupFn = jQueryFrameFn("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:submitOnPopup2");
  var closeFn = jQueryFrameFn("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:close2");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    openFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 1);
  });
  TTT.action(function () {
    inFn().val("some text");
    submitOnPopupFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(inFn().length, 1);
    assert.equal(inFn().val(), "some text");
  });
  TTT.action(function () {
    inFn().val("");
    submitOnPopupFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(inFn().length, 1);
    assert.equal(inFn().val(), "");
  });
  TTT.action(function () {
    closeFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inFn().length, 0);
  });
  TTT.action(function () {
    submitOnPageFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Client Side", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var openFn = jQueryFrameFn("#page\\:mainForm\\:client\\:open3");
  var submitOnPageFn = jQueryFrameFn("#page\\:mainForm\\:client\\:submitOnPage3");
  var popupCollapsedFn = jQueryFrameFn("#page\\:mainForm\\:client\\:clientPopup\\:\\:collapse");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:client\\:clientPopup\\:in3\\:\\:field");
  var submitOnPopupFn = jQueryFrameFn("#page\\:mainForm\\:client\\:clientPopup\\:submitOnPopup3");
  var closeFn = jQueryFrameFn("#page\\:mainForm\\:client\\:clientPopup\\:close3");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    openFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(popupCollapsedFn().val(), "false");
  });
  TTT.action(function () {
    closeFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(popupCollapsedFn().val(), "true");
  });
  TTT.action(function () {
    openFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(popupCollapsedFn().val(), "false");
  });
  TTT.action(function () {
    inFn().val("some text");
    submitOnPopupFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(popupCollapsedFn().val(), "true");
  });
  TTT.action(function () {
    openFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(popupCollapsedFn().val(), "false");
  });
  TTT.action(function () {
    inFn().val("");
    submitOnPopupFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(popupCollapsedFn().val(), "true");
  });
  TTT.action(function () {
    submitOnPageFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});
