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

QUnit.test("Simple Panel", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $submit = jQueryFrameFn("#\\page\\:mainForm\\:simple\\:submitSimple");
  let $show = jQueryFrameFn("#\\page\\:mainForm\\:simple\\:showSimple");
  let $hide = jQueryFrameFn("#\\page\\:mainForm\\:simple\\:hideSimple");
  let $sectionCollapsed = jQueryFrameFn("#page\\:mainForm\\:simple\\:simpleSection\\:\\:collapse");
  let $in = jQueryFrameFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("some text");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 1);
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $hide().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($sectionCollapsed().val(), "true");
    assert.equal($in().length, 0);
  });
  TTT.action(function () {
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($sectionCollapsed().val(), "true");
    assert.equal($in().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $submit = jQueryFrameFn("#\\page\\:mainForm\\:server\\:submitServer");
  let $show = jQueryFrameFn("#\\page\\:mainForm\\:server\\:showServer");
  let $hide = jQueryFrameFn("#\\page\\:mainForm\\:server\\:hideServer");
  let $sectionCollapsed = jQueryFrameFn("#page\\:mainForm\\:server\\:fullRequestSection\\:\\:collapse");
  let $in = jQueryFrameFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("some text");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 1);
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $hide().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($sectionCollapsed().val(), "true");
    assert.equal($in().length, 0);
  });
  TTT.action(function () {
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($sectionCollapsed().val(), "true");
    assert.equal($in().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Client Side", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $submit = jQueryFrameFn("#\\page\\:mainForm\\:client\\:submitClient");
  let $show = jQueryFrameFn("#\\page\\:mainForm\\:client\\:showClient");
  let $hide = jQueryFrameFn("#\\page\\:mainForm\\:client\\:hideClient");
  let $sectionCollapsed = jQueryFrameFn("#page\\:mainForm\\:client\\:clientSection\\:\\:collapse");
  let $in = jQueryFrameFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show().click();
  });
  TTT.asserts(2, function () {
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("some text");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 1);
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $hide().click();
  });
  TTT.asserts(3, function () {
    assert.equal($messages().length, 1);
    assert.equal($sectionCollapsed().val(), "true");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 1);
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Ajax", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $submit = jQueryFrameFn("#\\page\\:mainForm\\:ajax\\:submitAjax");
  let $show = jQueryFrameFn("#\\page\\:mainForm\\:ajax\\:showAjax");
  let $hide = jQueryFrameFn("#\\page\\:mainForm\\:ajax\\:hideAjax");
  let $sectionCollapsed = jQueryFrameFn("#page\\:mainForm\\:ajax\\:ajaxSection\\:\\:collapse");
  let $in = jQueryFrameFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("some text");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 1);
    assert.equal($sectionCollapsed().val(), "false");
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $hide().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($sectionCollapsed().val(), "true");
    assert.equal($in().length, 0);
  });
  TTT.action(function () {
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($messages().length, 0);
    assert.equal($sectionCollapsed().val(), "true");
    assert.equal($in().length, 0);
  });
  TTT.startTest();
});
