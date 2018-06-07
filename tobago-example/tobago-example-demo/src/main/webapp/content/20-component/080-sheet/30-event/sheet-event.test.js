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

QUnit.test("On click with ajax", function (assert) {
  let $oneClickAjax = jQueryFrameFn("#page\\:mainForm\\:changeExample\\:\\:0");
  let $venus = jQueryFrameFn("#page\\:mainForm\\:s1\\:2\\:sample0");
  let $jupiter = jQueryFrameFn("#page\\:mainForm\\:s1\\:5\\:sample0");
  let $saturn = jQueryFrameFn("#page\\:mainForm\\:s1\\:6\\:sample0");
  let $namefield = jQueryFrameFn("#page\\:mainForm\\:name\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $oneClickAjax().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($venus().length, 1);
    assert.equal($jupiter().length, 1);
    assert.equal($saturn().length, 1);
  });
  TTT.action(function () {
    $venus().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($namefield().val(), "Venus");
  });
  TTT.action(function () {
    $jupiter().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($namefield().val(), "Jupiter");
  });
  TTT.action(function () {
    $saturn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($namefield().val(), "Saturn");
  });
  TTT.startTest();
});

QUnit.test("On click with full request", function (assert) {
  let $oneClickFullRequest = jQueryFrameFn("#page\\:mainForm\\:changeExample\\:\\:1");
  let $venus = jQueryFrameFn("#page\\:mainForm\\:s1\\:2\\:sample1");
  let $jupiter = jQueryFrameFn("#page\\:mainForm\\:s1\\:5\\:sample1");
  let $saturn = jQueryFrameFn("#page\\:mainForm\\:s1\\:6\\:sample1");
  let $namefield = jQueryFrameFn("#page\\:mainForm\\:name\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $oneClickFullRequest().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($venus().length, 1);
    assert.equal($jupiter().length, 1);
    assert.equal($saturn().length, 1);
  });
  TTT.action(function () {
    $venus().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($namefield().val(), "Venus");
  });
  TTT.action(function () {
    $jupiter().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($namefield().val(), "Jupiter");
  });
  TTT.action(function () {
    $saturn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($namefield().val(), "Saturn");
  });
  TTT.startTest();
});

QUnit.test("On double click with full request", function (assert) {
  let $doubleClickFullRequest = jQueryFrameFn("#page\\:mainForm\\:changeExample\\:\\:2");
  let $venus = jQueryFrameFn("#page\\:mainForm\\:s1\\:2\\:sample2");
  let $jupiter = jQueryFrameFn("#page\\:mainForm\\:s1\\:5\\:sample2");
  let $saturn = jQueryFrameFn("#page\\:mainForm\\:s1\\:6\\:sample2");
  let $namefield = jQueryFrameFn("#page\\:mainForm\\:name\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $doubleClickFullRequest().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($venus().length, 1);
    assert.equal($jupiter().length, 1);
    assert.equal($saturn().length, 1);
  });
  TTT.action(function () {
    $venus().dblclick();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($namefield().val(), "Venus");
  });
  TTT.action(function () {
    $jupiter().dblclick();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($namefield().val(), "Jupiter");
  });
  TTT.action(function () {
    $saturn().dblclick();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($namefield().val(), "Saturn");
  });
  TTT.startTest();
});

QUnit.test("Open popup on click with ajax", function (assert) {
  let $radioButton = jQueryFrameFn("#page\\:mainForm\\:changeExample\\:\\:3");
  let $venus = jQueryFrameFn("#page\\:mainForm\\:s1\\:2\\:sample3");
  let $jupiter = jQueryFrameFn("#page\\:mainForm\\:s1\\:5\\:sample3");
  let $saturn = jQueryFrameFn("#page\\:mainForm\\:s1\\:6\\:sample3");
  let $popup = jQueryFrameFn("#page\\:mainForm\\:popup");
  let $name = jQueryFrameFn("#page\\:mainForm\\:popup\\:popupName\\:\\:field");
  let $cancel = jQueryFrameFn("#page\\:mainForm\\:popup\\:cancel");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $radioButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($venus().length, 1);
    assert.equal($jupiter().length, 1);
    assert.equal($saturn().length, 1);
  });
  TTT.action(function () {
    $venus().click();
  });
  TTT.waitForResponse();
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.ok($popup().hasClass("show"));
    assert.equal($name().val(), "Venus");
  });
  TTT.action(function () {
    $cancel().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(1, function () {
    assert.notOk($popup().hasClass("show"));
  });
  TTT.action(function () {
    $jupiter().click();
  });
  TTT.waitForResponse();
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.ok($popup().hasClass("show"));
    assert.equal($name().val(), "Jupiter");
  });
  TTT.action(function () {
    $cancel().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(1, function () {
    assert.notOk($popup().hasClass("show"));
  });
  TTT.action(function () {
    $saturn().click();
  });
  TTT.waitForResponse();
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.ok($popup().hasClass("show"));
    assert.equal($name().val(), "Saturn");
  });
  TTT.action(function () {
    $cancel().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(1, function () {
    assert.notOk($popup().hasClass("show"));
  });
  TTT.startTest();
});
