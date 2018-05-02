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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $open = jQueryFrameFn("#page\\:mainForm\\:simple\\:open1");
  var $submitOnPage = jQueryFrameFn("#page\\:mainForm\\:simple\\:submitOnPage1");
  var $in = jQueryFrameFn("#page\\:mainForm\\:simple\\:controllerPopup\\:in1\\:\\:field");
  var $submitOnPopup = jQueryFrameFn("#page\\:mainForm\\:simple\\:controllerPopup\\:submitOnPopup1");
  var $close = jQueryFrameFn("#page\\:mainForm\\:simple\\:controllerPopup\\:close1");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $open().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("some text");
    $submitOnPopup().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($in().length, 1);
    assert.equal($in().val(), "some text");
  });
  TTT.action(function () {
    $in().val("");
    $submitOnPopup().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 1);
    assert.equal($in().length, 1);
    assert.equal($in().val(), "");
  });
  TTT.action(function () {
    $close().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 0);
  });
  TTT.action(function () {
    $submitOnPage().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request", function (assert) {
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $open = jQueryFrameFn("#page\\:mainForm\\:server\\:open2");
  var $submitOnPage = jQueryFrameFn("#page\\:mainForm\\:server\\:submitOnPage2");
  var $in = jQueryFrameFn("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:in2\\:\\:field");
  var $submitOnPopup = jQueryFrameFn("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:submitOnPopup2");
  var $close = jQueryFrameFn("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:close2");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $open().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("some text");
    $submitOnPopup().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($in().length, 1);
    assert.equal($in().val(), "some text");
  });
  TTT.action(function () {
    $in().val("");
    $submitOnPopup().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 1);
    assert.equal($in().length, 1);
    assert.equal($in().val(), "");
  });
  TTT.action(function () {
    $close().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 0);
  });
  TTT.action(function () {
    $submitOnPage().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Client Side", function (assert) {
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $open = jQueryFrameFn("#page\\:mainForm\\:client\\:open3");
  var $submitOnPage = jQueryFrameFn("#page\\:mainForm\\:client\\:submitOnPage3");
  var $popupCollapsed = jQueryFrameFn("#page\\:mainForm\\:client\\:clientPopup\\:\\:collapse");
  var $in = jQueryFrameFn("#page\\:mainForm\\:client\\:clientPopup\\:in3\\:\\:field");
  var $submitOnPopup = jQueryFrameFn("#page\\:mainForm\\:client\\:clientPopup\\:submitOnPopup3");
  var $close = jQueryFrameFn("#page\\:mainForm\\:client\\:clientPopup\\:close3");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $open().click();
  });
  TTT.asserts(1, function () {
    assert.equal($popupCollapsed().val(), "false");
  });
  TTT.action(function () {
    $close().click();
  });
  TTT.asserts(1, function () {
    assert.equal($popupCollapsed().val(), "true");
  });
  TTT.action(function () {
    $open().click();
  });
  TTT.asserts(1, function () {
    assert.equal($popupCollapsed().val(), "false");
  });
  TTT.action(function () {
    $in().val("some text");
    $submitOnPopup().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($messages().length, 0);
    assert.equal($popupCollapsed().val(), "true");
  });
  TTT.action(function () {
    $open().click();
  });
  TTT.asserts(1, function () {
    assert.equal($popupCollapsed().val(), "false");
  });
  TTT.action(function () {
    $in().val("");
    $submitOnPopup().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($messages().length, 1);
    assert.equal($popupCollapsed().val(), "true");
  });
  TTT.action(function () {
    $submitOnPage().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.startTest();
});
