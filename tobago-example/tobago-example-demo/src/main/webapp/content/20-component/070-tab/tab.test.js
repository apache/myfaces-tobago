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

QUnit.test("Client: Select Tab 3", function (assert) {
  let hiddenInputFn = jQueryFrameFn("#page\\:mainForm\\:tabGroupClient\\:\\:activeIndex");
  let tab1Fn = jQueryFrameFn("#page\\:mainForm\\:tab1Client");
  let tab3Fn = jQueryFrameFn("#page\\:mainForm\\:tab3Client");
  let tab3linkFn = jQueryFrameFn("#page\\:mainForm\\:tab3Client .nav-link");

  let TTT = new TobagoTestTools(assert);
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().val(), 0);
    assert.ok(tab1Fn().hasClass("tobago-tab-markup-selected"));
    assert.notOk(tab3Fn().hasClass("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().hasClass("tobago-tab-markup-one"));
    assert.ok(tab3Fn().hasClass("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    tab3linkFn().click();
  });
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().val(), 3);
    assert.notOk(tab1Fn().hasClass("tobago-tab-markup-selected"));
    assert.ok(tab3Fn().hasClass("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().hasClass("tobago-tab-markup-one"));
    assert.ok(tab3Fn().hasClass("tobago-tab-markup-three"));
  });
  TTT.startTest();
});

QUnit.test("Ajax: Select Tab 3", function (assert) {
  let hiddenInputFn = jQueryFrameFn("#page\\:mainForm\\:tabGroupAjax\\:\\:activeIndex");
  let tab1Fn = jQueryFrameFn("#page\\:mainForm\\:tab1Ajax");
  let tab3Fn = jQueryFrameFn("#page\\:mainForm\\:tab3Ajax");
  let tab3linkFn = jQueryFrameFn("#page\\:mainForm\\:tab3Ajax .nav-link");

  let TTT = new TobagoTestTools(assert);
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().val(), 0);
    assert.ok(tab1Fn().hasClass("tobago-tab-markup-selected"));
    assert.notOk(tab3Fn().hasClass("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().hasClass("tobago-tab-markup-one"));
    assert.ok(tab3Fn().hasClass("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    tab3linkFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().val(), 3);
    assert.notOk(tab1Fn().hasClass("tobago-tab-markup-selected"));
    assert.ok(tab3Fn().hasClass("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().hasClass("tobago-tab-markup-one"));
    assert.ok(tab3Fn().hasClass("tobago-tab-markup-three"));
  });
  TTT.startTest();
});

QUnit.test("FullReload: Select Tab 3", function (assert) {
  let hiddenInputFn = jQueryFrameFn("#page\\:mainForm\\:tabGroupFullReload\\:\\:activeIndex");
  let tab1Fn = jQueryFrameFn("#page\\:mainForm\\:tab1FullReload");
  let tab3Fn = jQueryFrameFn("#page\\:mainForm\\:tab3FullReload");
  let tab3linkFn = jQueryFrameFn("#page\\:mainForm\\:tab3FullReload .nav-link");

  let TTT = new TobagoTestTools(assert);
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().val(), 0);
    assert.ok(tab1Fn().hasClass("tobago-tab-markup-selected"));
    assert.notOk(tab3Fn().hasClass("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().hasClass("tobago-tab-markup-one"));
    assert.ok(tab3Fn().hasClass("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    tab3linkFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(hiddenInputFn().val(), 3);
    assert.notOk(tab1Fn().hasClass("tobago-tab-markup-selected"));
    assert.ok(tab3Fn().hasClass("tobago-tab-markup-selected"));
    assert.ok(tab1Fn().hasClass("tobago-tab-markup-one"));
    assert.ok(tab3Fn().hasClass("tobago-tab-markup-three"));
  });
  TTT.startTest();
});
