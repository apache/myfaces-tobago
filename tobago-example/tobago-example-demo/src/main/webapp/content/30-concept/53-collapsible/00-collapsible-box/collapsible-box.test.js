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

QUnit.test("Simple Collapsible Box: show -> hide transition", function (assert) {
  var $show = jQueryFrameFn("#page\\:mainForm\\:controller\\:show");
  var $hide = jQueryFrameFn("#page\\:mainForm\\:controller\\:hide");
  var $content = jQueryFrameFn("#page\\:mainForm\\:controller\\:content");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($content().length, 1);
  });
  TTT.action(function () {
    $hide().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($content().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Simple Collapsible Box: hide -> show transition", function (assert) {
  var $show = jQueryFrameFn("#page\\:mainForm\\:controller\\:show");
  var $hide = jQueryFrameFn("#page\\:mainForm\\:controller\\:hide");
  var $content = jQueryFrameFn("#page\\:mainForm\\:controller\\:content");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $hide().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($content().length, 0);
  });
  TTT.action(function () {
    $show().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($content().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: open both boxes", function (assert) {
  var $show1 = jQueryFrameFn("#page\\:mainForm\\:server\\:show1");
  var $show2 = jQueryFrameFn("#page\\:mainForm\\:server\\:show2");
  var $content1 = jQueryFrameFn("#page\\:mainForm\\:server\\:content1");
  var $content2 = jQueryFrameFn("#page\\:mainForm\\:server\\:content2");
  var content2Length = $content2().length;

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show1().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($content1().length, 1);
    assert.equal($content2().length, content2Length);
  });
  TTT.action(function () {
    $show2().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($content1().length, 1);
    assert.equal($content2().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: open box 1, close box 2", function (assert) {
  var $show1 = jQueryFrameFn("#page\\:mainForm\\:server\\:show1");
  var $hide2 = jQueryFrameFn("#page\\:mainForm\\:server\\:hide2");
  var $content1 = jQueryFrameFn("#page\\:mainForm\\:server\\:content1");
  var $content2 = jQueryFrameFn("#page\\:mainForm\\:server\\:content2");
  var content2Length = $content2().length;

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show1().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($content1().length, 1);
    assert.equal($content2().length, content2Length);
  });
  TTT.action(function () {
    $hide2().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($content1().length, 1);
    assert.equal($content2().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: close box 1, open box 2", function (assert) {
  var $hide1 = jQueryFrameFn("#page\\:mainForm\\:server\\:hide1");
  var $show2 = jQueryFrameFn("#page\\:mainForm\\:server\\:show2");
  var $content1 = jQueryFrameFn("#page\\:mainForm\\:server\\:content1");
  var $content2 = jQueryFrameFn("#page\\:mainForm\\:server\\:content2");
  var content2Length = $content2().length;

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $hide1().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($content1().length, 0);
    assert.equal($content2().length, content2Length);
  });
  TTT.action(function () {
    $show2().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($content1().length, 0);
    assert.equal($content2().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Full Server Request: close both boxes", function (assert) {
  var $hide1 = jQueryFrameFn("#page\\:mainForm\\:server\\:hide1");
  var $hide2 = jQueryFrameFn("#page\\:mainForm\\:server\\:hide2");
  var $content1 = jQueryFrameFn("#page\\:mainForm\\:server\\:content1");
  var $content2 = jQueryFrameFn("#page\\:mainForm\\:server\\:content2");
  var content2Length = $content2().length;

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $hide1().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($content1().length, 0);
    assert.equal($content2().length, content2Length);
  });
  TTT.action(function () {
    $hide2().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($content1().length, 0);
    assert.equal($content2().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Client Side: show -> hide transition", function (assert) {
  var $show = jQueryFrameFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  var $hide = jQueryFrameFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  var $box = jQueryFrameFn("#page\\:mainForm\\:client\\:noRequestBox");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show().click();
  });
  TTT.asserts(1, function () {
    assert.equal($box().hasClass("tobago-collapsed"), false);
  });
  TTT.action(function () {
    $hide().click();
  });
  TTT.asserts(1, function () {
    assert.equal($box().hasClass("tobago-collapsed"), true);
  });
  TTT.startTest();
});

QUnit.test("Client Side: hide -> show transition", function (assert) {
  var $show = jQueryFrameFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  var $hide = jQueryFrameFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  var $box = jQueryFrameFn("#page\\:mainForm\\:client\\:noRequestBox");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $hide().click();
  });
  TTT.asserts(1, function () {
    assert.equal($box().hasClass("tobago-collapsed"), true);
  });
  TTT.action(function () {
    $show().click();
  });
  TTT.asserts(1, function () {
    assert.equal($box().hasClass("tobago-collapsed"), false);
  });
  TTT.startTest();
});

QUnit.test("Client Side: hide content and submit empty string", function (assert) {
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $show = jQueryFrameFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  var $hide = jQueryFrameFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  var $box = jQueryFrameFn("#page\\:mainForm\\:client\\:noRequestBox");
  var $in = jQueryFrameFn("#page\\:mainForm\\:client\\:inNoRequestBox\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:client\\:submitNoRequestBox");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $hide().click();
  });
  TTT.asserts(1, function () {
    assert.equal($box().hasClass("tobago-collapsed"), true);
  });
  TTT.action(function () {
    $in().val("");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Ajax: show -> hide transition", function (assert) {
  var $show = jQueryFrameFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  var $hide = jQueryFrameFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  var $in = jQueryFrameFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $hide().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Ajax: hide -> show transition", function (assert) {
  var $show = jQueryFrameFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  var $hide = jQueryFrameFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  var $in = jQueryFrameFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $hide().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 0);
  });
  TTT.action(function () {
    $show().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Ajax: hide content and submit empty string", function (assert) {
  var $messages = jQueryFrameFn("#page\\:messages .tobago-messages");
  var $show = jQueryFrameFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  var $hide = jQueryFrameFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  var $in = jQueryFrameFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:ajax\\:submitAjaxBox");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $show().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 1);
  });
  TTT.action(function () {
    $in().val("");
    $hide().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($in().length, 0);
  });
  TTT.action(function () {
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 0);
  });
  TTT.startTest();
});
