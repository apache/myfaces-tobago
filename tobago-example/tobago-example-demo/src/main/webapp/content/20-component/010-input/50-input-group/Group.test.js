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

QUnit.test("ajax: chat send button", function (assert) {
  var chatlogFn = jQueryFrameFn("#page\\:mainForm\\:tachatlog\\:\\:field");
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:inewmessage\\:\\:field");
  var sendButtonFn = jQueryFrameFn("#page\\:mainForm\\:sendButton");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inputFn().val("delete chat");
    sendButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(chatlogFn().text(), "");
  });
  TTT.action(function () {
    inputFn().val("Hi Peter, how are you?");
    sendButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(chatlogFn().text(), "User Two: Hi Peter, how are you?");
  });
  TTT.startTest();
});

QUnit.test("ajax: dropdown button", function (assert) {
  var buttonFn = jQueryFrameFn("#page\\:mainForm\\:lsendtoc\\:\\:command");
  var buttonLabelFn = jQueryFrameFn("#page\\:mainForm\\:lsendtoc\\:\\:command span");
  var sendToPeterFn = jQueryFrameFn("#page\\:mainForm\\:sendToPeter");
  var sendToBobFn = jQueryFrameFn("#page\\:mainForm\\:sendToBob");
  var sendToAllFn = jQueryFrameFn("#page\\:mainForm\\:sendToAll");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    buttonFn().click();
    sendToPeterFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(buttonLabelFn().text(), "SendTo: Peter");
  });
  TTT.action(function () {
    buttonFn().click();
    sendToBobFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(buttonLabelFn().text(), "SendTo: Bob");
  });
  TTT.action(function () {
    buttonFn().click();
    sendToAllFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(buttonLabelFn().text(), "SendTo: All");
  });
  TTT.startTest();
});

QUnit.test("ajax: currency change event", function (assert) {
  var currencyInputFn = jQueryFrameFn("#page\\:mainForm\\:value\\:\\:field");
  var currencyOptionFn = jQueryFrameFn("#page\\:mainForm\\:currency option");
  var outputFieldFn = jQueryFrameFn("#page\\:mainForm\\:valueInEuro .tobago-out");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    currencyInputFn().val("1000");
    currencyOptionFn().eq(1).prop("selected", false);
    currencyOptionFn().eq(2).prop("selected", false);
    currencyOptionFn().eq(3).prop("selected", false);
    currencyOptionFn().eq(0).prop("selected", true).trigger("change"); // Yen
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFieldFn().text(), "8,85");
  });
  TTT.action(function () {
    currencyInputFn().val("2000");
    currencyOptionFn().eq(0).prop("selected", false);
    currencyOptionFn().eq(2).prop("selected", false);
    currencyOptionFn().eq(3).prop("selected", false);
    currencyOptionFn().eq(1).prop("selected", true).trigger("change"); // Trinidad-Tobago Dollar
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFieldFn().text(), "267,50");
  });
  TTT.action(function () {
    currencyInputFn().val("3000");
    currencyOptionFn().eq(0).prop("selected", false);
    currencyOptionFn().eq(1).prop("selected", false);
    currencyOptionFn().eq(3).prop("selected", false);
    currencyOptionFn().eq(2).prop("selected", true).trigger("change"); // US Dollar
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFieldFn().text(), "2.688,29");
  });
  TTT.startTest();
});
