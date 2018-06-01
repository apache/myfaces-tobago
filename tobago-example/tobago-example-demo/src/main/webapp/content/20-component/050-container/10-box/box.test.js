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

QUnit.test("Accordion: Box 1: 'hide' to 'show' to 'hide'", function (assert) {
  var $box = jQueryFrameFn("#page\\:mainForm\\:accordionBox1");
  var $showBox = jQueryFrameFn("#page\\:mainForm\\:showBox1");
  var $hideBox = jQueryFrameFn("#page\\:mainForm\\:hideBox1");
  var $boxBody = jQueryFrameFn("#page\\:mainForm\\:accordionBox1 .card-body");

  var TTT = new TobagoTestTools(assert);
  TTT.asserts(3, function () {
    assert.equal($showBox().length, 1);
    assert.equal($hideBox().length, 0);
    assert.equal($boxBody().text().trim().length, 0);
  });
  TTT.action(function () {
    $showBox().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($showBox().length, 0);
    assert.equal($hideBox().length, 1);
    assert.notEqual($boxBody().text().trim().length, 0);
  });
  TTT.action(function () {
    $hideBox().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($showBox().length, 1);
    assert.equal($hideBox().length, 0);
    assert.equal($boxBody().text().trim().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Accordion: Box 2: 'hide' to 'show' to 'hide'", function (assert) {
  var $box = jQueryFrameFn("#page\\:mainForm\\:accordionBox2");
  var $showBox = jQueryFrameFn("#page\\:mainForm\\:showBox2");
  var $hideBox = jQueryFrameFn("#page\\:mainForm\\:hideBox2");
  var $boxBody = jQueryFrameFn("#page\\:mainForm\\:accordionBox2 .card-body");

  var TTT = new TobagoTestTools(assert);
  TTT.asserts(3, function () {
    assert.equal($showBox().length, 1);
    assert.equal($hideBox().length, 0);
    assert.equal($boxBody().text().trim().length, 0);
  });
  TTT.action(function () {
    $showBox().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($showBox().length, 0);
    assert.equal($hideBox().length, 1);
    assert.notEqual($boxBody().text().trim().length, 0);
  });
  TTT.action(function () {
    $hideBox().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($showBox().length, 1);
    assert.equal($hideBox().length, 0);
    assert.equal($boxBody().text().trim().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Accordion: Box 3: 'hide' to 'show' to 'hide'", function (assert) {
  var $box = jQueryFrameFn("#page\\:mainForm\\:accordionBox3");
  var $showBox = jQueryFrameFn("#page\\:mainForm\\:showBox3");
  var $hideBox = jQueryFrameFn("#page\\:mainForm\\:hideBox3");

  var TTT = new TobagoTestTools(assert);
  TTT.asserts(3, function () {
    assert.ok($box().hasClass("tobago-collapsed"));
    assert.equal($showBox().length, 1);
    assert.equal($hideBox().length, 0);
  });
  TTT.action(function () {
    $showBox().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.notOk($box().hasClass("tobago-collapsed"));
    assert.equal($showBox().length, 0);
    assert.equal($hideBox().length, 1);
  });
  TTT.action(function () {
    $hideBox().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok($box().hasClass("tobago-collapsed"));
    assert.equal($showBox().length, 1);
    assert.equal($hideBox().length, 0);
  });
  TTT.startTest();
});
