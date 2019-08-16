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

QUnit.test("On click with ajax", function (assert) {
  let oneClickAjaxFn = testFrameQuerySelectorFn("#page\\:mainForm\\:changeExample\\:\\:0");
  let venusFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:2\\:sample0");
  let jupiterFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:5\\:sample0");
  let saturnFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:6\\:sample0");
  let namefieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:name\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    oneClickAjaxFn().checked = true;
    oneClickAjaxFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(venusFn());
    assert.ok(jupiterFn());
    assert.ok(saturnFn());
  });
  TTT.action(function () {
    venusFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(namefieldFn().value, "Venus");
  });
  TTT.action(function () {
    jupiterFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(namefieldFn().value, "Jupiter");
  });
  TTT.action(function () {
    saturnFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(namefieldFn().value, "Saturn");
  });
  TTT.startTest();
});

QUnit.test("On click with full request", function (assert) {
  let oneClickFullRequestFn = testFrameQuerySelectorFn("#page\\:mainForm\\:changeExample\\:\\:1");
  let venusFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:2\\:sample1");
  let jupiterFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:5\\:sample1");
  let saturnFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:6\\:sample1");
  let namefieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:name\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    oneClickFullRequestFn().checked = true;
    oneClickFullRequestFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(venusFn());
    assert.ok(jupiterFn());
    assert.ok(saturnFn());
  });
  TTT.action(function () {
    venusFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(namefieldFn().value, "Venus");
  });
  TTT.action(function () {
    jupiterFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(namefieldFn().value, "Jupiter");
  });
  TTT.action(function () {
    saturnFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(namefieldFn().value, "Saturn");
  });
  TTT.startTest();
});

QUnit.test("On double click with full request", function (assert) {
  let doubleClickFullRequestFn = testFrameQuerySelectorFn("#page\\:mainForm\\:changeExample\\:\\:2");
  let venusFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:2\\:sample2");
  let jupiterFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:5\\:sample2");
  let saturnFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:6\\:sample2");
  let namefieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:name\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    doubleClickFullRequestFn().checked = true;
    doubleClickFullRequestFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(venusFn());
    assert.ok(jupiterFn());
    assert.ok(saturnFn());
  });
  TTT.action(function () {
    venusFn().dispatchEvent(new Event('dblclick'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(namefieldFn().value, "Venus");
  });
  TTT.action(function () {
    jupiterFn().dispatchEvent(new Event('dblclick'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(namefieldFn().value, "Jupiter");
  });
  TTT.action(function () {
    saturnFn().dispatchEvent(new Event('dblclick'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(namefieldFn().value, "Saturn");
  });
  TTT.startTest();
});

QUnit.test("Open popup on click with ajax", function (assert) {
  let radioButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:changeExample\\:\\:3");
  let venusFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:2\\:sample3");
  let jupiterFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:5\\:sample3");
  let saturnFn = testFrameQuerySelectorFn("#page\\:mainForm\\:s1\\:6\\:sample3");
  let popupFn = testFrameQuerySelectorFn("#page\\:mainForm\\:popup");
  let nameFn = testFrameQuerySelectorFn("#page\\:mainForm\\:popup\\:popupName\\:\\:field");
  let cancelFn = testFrameQuerySelectorFn("#page\\:mainForm\\:popup\\:cancel");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    radioButtonFn().checked = true;
    radioButtonFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(venusFn());
    assert.ok(jupiterFn());
    assert.ok(saturnFn());
  });
  TTT.action(function () {
    venusFn().click();
  });
  TTT.waitForResponse();
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.ok(popupFn().classList.contains("show"));
    assert.equal(nameFn().value, "Venus");
  });
  TTT.action(function () {
    cancelFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(1, function () {
    assert.notOk(popupFn().classList.contains("show"));
  });
  TTT.action(function () {
    jupiterFn().click();
  });
  TTT.waitForResponse();
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.ok(popupFn().classList.contains("show"));
    assert.equal(nameFn().value, "Jupiter");
  });
  TTT.action(function () {
    cancelFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(1, function () {
    assert.notOk(popupFn().classList.contains("show"));
  });
  TTT.action(function () {
    saturnFn().click();
  });
  TTT.waitForResponse();
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.ok(popupFn().classList.contains("show"));
    assert.equal(nameFn().value, "Saturn");
  });
  TTT.action(function () {
    cancelFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(1, function () {
    assert.notOk(popupFn().classList.contains("show"));
  });
  TTT.startTest();
});
