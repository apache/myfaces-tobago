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

QUnit.test("submit: select A", function (assert) {
  var $selectA = jQueryFrameFn("#page\\:mainForm\\:selectA input");
  var $selectB = jQueryFrameFn("#page\\:mainForm\\:selectB input");
  var $selectC = jQueryFrameFn("#page\\:mainForm\\:selectC input");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:submit");
  var $output = jQueryFrameFn("#page\\:mainForm\\:submitOutput span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectA().prop("checked", true);
    $selectB().prop("checked", false);
    $selectC().prop("checked", false);
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "A ");
  });
  TTT.startTest();
});

QUnit.test("submit: select B and C", function (assert) {
  var $selectA = jQueryFrameFn("#page\\:mainForm\\:selectA input");
  var $selectB = jQueryFrameFn("#page\\:mainForm\\:selectB input");
  var $selectC = jQueryFrameFn("#page\\:mainForm\\:selectC input");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:submit");
  var $output = jQueryFrameFn("#page\\:mainForm\\:submitOutput span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectA().prop("checked", false);
    $selectB().prop("checked", true);
    $selectC().prop("checked", true);
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "B C ");
  });
  TTT.startTest();
});

QUnit.test("ajax: select D", function (assert) {
  var $selectD = jQueryFrameFn("#page\\:mainForm\\:selectD input");
  var $outputD = jQueryFrameFn("#page\\:mainForm\\:outputD span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectD().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($outputD().text(), "true");
  });
  TTT.startTest();
});

QUnit.test("ajax: deselect D", function (assert) {
  var $selectD = jQueryFrameFn("#page\\:mainForm\\:selectD input");
  var $outputD = jQueryFrameFn("#page\\:mainForm\\:outputD span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectD().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($outputD().text(), "false");
  });
  TTT.startTest();
});

QUnit.test("ajax: select E", function (assert) {
  var $selectE = jQueryFrameFn("#page\\:mainForm\\:selectE input");
  var $outputE = jQueryFrameFn("#page\\:mainForm\\:outputE span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectE().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($outputE().text(), "true");
  });
  TTT.startTest();
});

QUnit.test("ajax: deselect E", function (assert) {
  var $selectE = jQueryFrameFn("#page\\:mainForm\\:selectE input");
  var $outputE = jQueryFrameFn("#page\\:mainForm\\:outputE span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectE().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($outputE().text(), "false");
  });
  TTT.startTest();
});

QUnit.test("ajax: select F", function (assert) {
  var $selectF = jQueryFrameFn("#page\\:mainForm\\:selectF input");
  var $outputF = jQueryFrameFn("#page\\:mainForm\\:outputF span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectF().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($outputF().text(), "true");
  });
  TTT.startTest();
});

QUnit.test("ajax: deselect F", function (assert) {
  var $selectF = jQueryFrameFn("#page\\:mainForm\\:selectF input");
  var $outputF = jQueryFrameFn("#page\\:mainForm\\:outputF span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectF().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($outputF().text(), "false");
  });
  TTT.startTest();
});
