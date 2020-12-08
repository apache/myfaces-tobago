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

import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("submit: addAll, removeAll, addItem0to4, removeItem2to3", function (assert) {
  let unselectedOptions = querySelectorAllFn("#page\\:mainForm\\:submitExample\\:\\:unselected option");
  let selectedOptions = querySelectorAllFn("#page\\:mainForm\\:submitExample\\:\\:selected option");
  let addAllButton = querySelectorFn("#page\\:mainForm\\:submitExample\\:\\:addAll");
  let addButton = querySelectorFn("#page\\:mainForm\\:submitExample\\:\\:add");
  let removeButton = querySelectorFn("#page\\:mainForm\\:submitExample\\:\\:remove");
  let removeAllButton = querySelectorFn("#page\\:mainForm\\:submitExample\\:\\:removeAll");
  let submitButton = querySelectorFn("#page\\:mainForm\\:submitButton");
  let output = querySelectorFn("#page\\:mainForm\\:submitExampleOutput span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    addAllButton().click();
    submitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(unselectedOptions().length, 0);
    assert.equal(selectedOptions().length, 9);
    assert.equal(output().textContent, "[Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto]");
  });
  TTT.action(function () {
    removeAllButton().click();
    submitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(unselectedOptions().length, 9);
    assert.equal(selectedOptions().length, 0);
    assert.equal(output().textContent, "[]");
  });
  TTT.action(function () {
    unselectedOptions().item(0).selected = true;
    unselectedOptions().item(1).selected = true;
    unselectedOptions().item(2).selected = true;
    unselectedOptions().item(3).selected = true;
    unselectedOptions().item(4).selected = true;
    addButton().click();
    submitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(unselectedOptions().length, 4);
    assert.equal(selectedOptions().length, 5);
    assert.equal(output().textContent, "[Mercury, Venus, Earth, Mars, Jupiter]");
  });
  TTT.action(function () {
    selectedOptions().item(2).selected = true;
    selectedOptions().item(3).selected = true;
    removeButton().click();
    submitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(unselectedOptions().length, 6);
    assert.equal(selectedOptions().length, 3);
    assert.equal(output().textContent, "[Mercury, Venus, Jupiter]");
  });
  TTT.startTest();
});

QUnit.test("ajax: addAll, removeAll, addItem1to2, removeItem0", function (assert) {
  let unselectedOptions = querySelectorAllFn("#page\\:mainForm\\:ajaxExample\\:\\:unselected option");
  let selectedOptions = querySelectorAllFn("#page\\:mainForm\\:ajaxExample\\:\\:selected option");
  let addAllButton = querySelectorFn("#page\\:mainForm\\:ajaxExample\\:\\:addAll");
  let addButton = querySelectorFn("#page\\:mainForm\\:ajaxExample\\:\\:add");
  let removeButton = querySelectorFn("#page\\:mainForm\\:ajaxExample\\:\\:remove");
  let removeAllButton = querySelectorFn("#page\\:mainForm\\:ajaxExample\\:\\:removeAll");
  let output = querySelectorFn("#page\\:mainForm\\:outputStars span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    addAllButton().click();
  });
  // TTT.waitForResponse(); //TODO use waitForResponse()
  TTT.waitMs(5000);
  TTT.asserts(6, function () {
    assert.equal(unselectedOptions().length, 0);
    assert.equal(selectedOptions().length, 4);
    assert.ok(output().textContent.indexOf(selectedOptions().item(0).value) > 0);
    assert.ok(output().textContent.indexOf(selectedOptions().item(1).value) > 0);
    assert.ok(output().textContent.indexOf(selectedOptions().item(2).value) > 0);
    assert.ok(output().textContent.indexOf(selectedOptions().item(3).value) > 0);
  });
  TTT.action(function () {
    removeAllButton().click();
  });
  // TTT.waitForResponse(); //TODO use waitForResponse()
  TTT.waitMs(5000);
  TTT.asserts(3, function () {
    assert.equal(unselectedOptions().length, 4);
    assert.equal(selectedOptions().length, 0);
    assert.equal(output().textContent, "[]");
  });
  TTT.action(function () {
    unselectedOptions().item(1).selected = true;
    unselectedOptions().item(2).selected = true;
    addButton().click();
  });
  // TTT.waitForResponse(); //TODO use waitForResponse()
  TTT.waitMs(5000);
  TTT.asserts(4, function () {
    assert.equal(unselectedOptions().length, 2);
    assert.equal(selectedOptions().length, 2);
    assert.ok(output().textContent.indexOf(selectedOptions().item(0).value) > 0);
    assert.ok(output().textContent.indexOf(selectedOptions().item(1).value) > 0);
  });
  TTT.action(function () {
    selectedOptions().item(0).selected = true;
    selectedOptions().item(1).selected = false;
    removeButton().click();
  });
  // TTT.waitForResponse(); //TODO use waitForResponse()
  TTT.waitMs(5000);
  TTT.asserts(3, function () {
    assert.equal(unselectedOptions().length, 3);
    assert.equal(selectedOptions().length, 1);
    assert.ok(output().textContent.indexOf(selectedOptions().item(0).value) > 0);
  });
  TTT.startTest();
});
