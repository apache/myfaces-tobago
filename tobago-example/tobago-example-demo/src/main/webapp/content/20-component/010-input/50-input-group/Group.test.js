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

QUnit.test("ajax: chat send button", function (assert) {
  let chatlogFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tachatlog\\:\\:field");
  let inputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:inewmessage\\:\\:field");
  let sendButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sendButton");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inputFn().value = "delete chat";
    sendButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(chatlogFn().textContent, "");
  });
  TTT.action(function () {
    inputFn().value = "Hi Peter, how are you?";
    sendButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(chatlogFn().textContent, "User Two: Hi Peter, how are you?");
  });
  TTT.startTest();
});

QUnit.test("ajax: dropdown button", function (assert) {
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:lsendtoc\\:\\:command");
  let buttonLabelFn = testFrameQuerySelectorFn("#page\\:mainForm\\:lsendtoc\\:\\:command span");
  let sendToPeterFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sendToPeter");
  let sendToBobFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sendToBob");
  let sendToAllFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sendToAll");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
    sendToPeterFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(buttonLabelFn().textContent, "SendTo: Peter");
  });
  TTT.action(function () {
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
    sendToBobFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(buttonLabelFn().textContent, "SendTo: Bob");
  });
  TTT.action(function () {
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
    sendToAllFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(buttonLabelFn().textContent, "SendTo: All");
  });
  TTT.startTest();
});

QUnit.test("ajax: currency change event", function (assert) {
  let currencyInputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:value\\:\\:field");
  let currencyFn = testFrameQuerySelectorFn("#page\\:mainForm\\:currency");
  let currencyOptionFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:currency option");
  let outputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:valueInEuro .tobago-out");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    currencyInputFn().value = "1000";
    currencyOptionFn().item(1).selected = false;
    currencyOptionFn().item(2).selected = false;
    currencyOptionFn().item(3).selected = false;
    currencyOptionFn().item(0).selected = true; // Yen
    currencyFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFieldFn().textContent, "8,85");
  });
  TTT.action(function () {
    currencyInputFn().value = "2000";
    currencyOptionFn().item(0).selected = false;
    currencyOptionFn().item(2).selected = false;
    currencyOptionFn().item(3).selected = false;
    currencyOptionFn().item(1).selected = true; // Trinidad-Tobago Dollar
    currencyFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFieldFn().textContent, "267,50");
  });
  TTT.action(function () {
    currencyInputFn().value = "3000";
    currencyOptionFn().item(0).selected = false;
    currencyOptionFn().item(1).selected = false;
    currencyOptionFn().item(3).selected = false;
    currencyOptionFn().item(2).selected = true; // US Dollar
    currencyFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFieldFn().textContent, "2.688,29");
  });
  TTT.startTest();
});
