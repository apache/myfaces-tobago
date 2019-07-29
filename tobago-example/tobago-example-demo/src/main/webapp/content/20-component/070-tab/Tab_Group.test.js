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

QUnit.test("Client: Select Tab 3", function (assert) {
  var hiddenInputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabGroupClient\\:\\:activeIndex");
  var tab1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab1Client");
  var tab3Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab3Client");
  var tab3linkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab3Client .nav-link");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().value, 0);
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-selected"));
    assert.notOk(tab3Fn().classList.contains("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    tab3linkFn().click();
  });
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().value, 3);
    assert.notOk(tab1Fn().classList.contains("tobago-tab-markup-selected"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.startTest();
});

QUnit.test("Ajax: Select Tab 3", function (assert) {
  var hiddenInputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabGroupAjax\\:\\:activeIndex");
  var tab1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab1Ajax");
  var tab3Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab3Ajax");
  var tab3linkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab3Ajax .nav-link");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().value, 0);
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-selected"));
    assert.notOk(tab3Fn().classList.contains("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    tab3linkFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().value, 3);
    assert.notOk(tab1Fn().classList.contains("tobago-tab-markup-selected"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.startTest();
});

QUnit.test("FullReload: Select Tab 3", function (assert) {
  var hiddenInputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabGroupFullReload\\:\\:activeIndex");
  var tab1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab1FullReload");
  var tab3Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab3FullReload");
  var tab3linkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab3FullReload .nav-link");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().value, 0);
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-selected"));
    assert.notOk(tab3Fn().classList.contains("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    tab3linkFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().value, 3);
    assert.notOk(tab1Fn().classList.contains("tobago-tab-markup-selected"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.startTest();
});
