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

import {testFrameQuerySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("Accordion: Box 1: 'hide' to 'show' to 'hide'", function (assert) {
  let boxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:accordionBox1");
  let showBoxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:showBox1");
  let hideBoxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:hideBox1");
  let boxBodyFn = testFrameQuerySelectorFn("#page\\:mainForm\\:accordionBox1 .card-body");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(3, function () {
    assert.ok(showBoxFn() !== null);
    assert.ok(hideBoxFn() === null);
    assert.equal(boxBodyFn().textContent.trim(), "");
  });
  TTT.action(function () {
    showBoxFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(showBoxFn() === null);
    assert.ok(hideBoxFn() !== null);
    assert.notEqual(boxBodyFn().textContent.trim(), null);
  });
  TTT.action(function () {
    hideBoxFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(showBoxFn() !== null);
    assert.ok(hideBoxFn() === null);
    assert.equal(boxBodyFn().textContent.trim(), "");
  });
  TTT.startTest();
});

QUnit.test("Accordion: Box 2: 'hide' to 'show' to 'hide'", function (assert) {
  let boxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:accordionBox2");
  let showBoxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:showBox2");
  let hideBoxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:hideBox2");
  let boxBodyFn = testFrameQuerySelectorFn("#page\\:mainForm\\:accordionBox2 .card-body");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(3, function () {
    assert.ok(showBoxFn() !== null);
    assert.ok(hideBoxFn() === null);
    assert.equal(boxBodyFn().textContent.trim(), "");
  });
  TTT.action(function () {
    showBoxFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(showBoxFn() === null);
    assert.ok(hideBoxFn() !== null);
    assert.notEqual(boxBodyFn().textContent.trim(), null);
  });
  TTT.action(function () {
    hideBoxFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(showBoxFn() !== null);
    assert.ok(hideBoxFn() === null);
    assert.equal(boxBodyFn().textContent.trim(), "");
  });
  TTT.startTest();
});

QUnit.test("Accordion: Box 3: 'hide' to 'show' to 'hide'", function (assert) {
  let boxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:accordionBox3");
  let showBoxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:showBox3");
  let hideBoxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:hideBox3");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(3, function () {
    assert.ok(boxFn().classList.contains("tobago-collapsed"));
    assert.ok(showBoxFn() !== null);
    assert.ok(hideBoxFn() === null);
  });
  TTT.action(function () {
    showBoxFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.notOk(boxFn().classList.contains("tobago-collapsed"));
    assert.ok(showBoxFn() === null);
    assert.ok(hideBoxFn() !== null);
  });
  TTT.action(function () {
    hideBoxFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(boxFn().classList.contains("tobago-collapsed"));
    assert.ok(showBoxFn() !== null);
    assert.ok(hideBoxFn() === null);
  });
  TTT.startTest();
});
