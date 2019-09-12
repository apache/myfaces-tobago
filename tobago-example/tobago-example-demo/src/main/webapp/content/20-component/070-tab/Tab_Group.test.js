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
  let tabGroupFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabGroupClient");
  let tab1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab1Client");
  let tab3Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab3Client");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(5, function () {
    assert.equal(tabGroupFn().hiddenInput.value, 0);
    assert.ok(tab1Fn().querySelector(".nav-link").classList.contains("active"));
    assert.notOk(tab3Fn().querySelector(".nav-link").classList.contains("active"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    tab3Fn().querySelector(".nav-link").click();
  });
  TTT.asserts(5, function () {
    assert.equal(tabGroupFn().hiddenInput.value, 3);
    assert.notOk(tab1Fn().querySelector(".nav-link").classList.contains("active"));
    assert.ok(tab3Fn().querySelector(".nav-link").classList.contains("active"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.startTest();
});

QUnit.test("Ajax: Select Tab 3", function (assert) {
  let tabGroupFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabGroupAjax");
  let tab1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab1Ajax");
  let tab3Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab3Ajax");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(5, function () {
    assert.equal(tabGroupFn().hiddenInput.value, 0);
    assert.ok(tab1Fn().querySelector(".nav-link").classList.contains("active"));
    assert.notOk(tab3Fn().querySelector(".nav-link").classList.contains("active"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    tab3Fn().querySelector(".nav-link").click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(tabGroupFn().hiddenInput.value, 3);
    assert.notOk(tab1Fn().querySelector(".nav-link").classList.contains("active"));
    assert.ok(tab3Fn().querySelector(".nav-link").classList.contains("active"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.startTest();
});

QUnit.test("FullReload: Select Tab 3", function (assert) {
  let tabGroupFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabGroupFullReload");
  let tab1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab1FullReload");
  let tab3Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tab3FullReload");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(5, function () {
    assert.equal(tabGroupFn().hiddenInput.value, 0);
    assert.ok(tab1Fn().querySelector(".nav-link").classList.contains("active"));
    assert.notOk(tab3Fn().querySelector(".nav-link").classList.contains("active"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    tab3Fn().querySelector(".nav-link").click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(tabGroupFn().hiddenInput.value, 3);
    assert.notOk(tab1Fn().querySelector(".nav-link").classList.contains("active"));
    assert.ok(tab3Fn().querySelector(".nav-link").classList.contains("active"));
    assert.ok(tab1Fn().classList.contains("tobago-tab-markup-one"));
    assert.ok(tab3Fn().classList.contains("tobago-tab-markup-three"));
  });
  TTT.startTest();
});
