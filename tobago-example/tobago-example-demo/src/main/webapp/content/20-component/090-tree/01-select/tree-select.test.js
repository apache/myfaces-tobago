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

QUnit.test("single: select Music, select Mathematics", function (assert) {
  var $music = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  var $mathematics = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableSingle = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:1");
  var $input = jQueryFrameFn(".tobago-treeSelect input");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectableNone().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($input().length, 0);
  });
  TTT.action(function () {
    $selectableSingle().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual($input().length, 0);
  });
  TTT.action(function () {
    $music().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Music");
  });
  TTT.action(function () {
    $mathematics().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("singleLeafOnly: select Classic, select Mathematics", function (assert) {
  var $classic = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  var $mathematics = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableSingleLeafOnly = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:2");
  var $input = jQueryFrameFn(".tobago-treeSelect input");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectableNone().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($input().length, 0);
  });
  TTT.action(function () {
    $selectableSingleLeafOnly().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual($input().length, 0);
  });
  TTT.action(function () {
    $classic().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Classic");
  });
  TTT.action(function () {
    $mathematics().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("multi: select Music, select Mathematics, deselect Music", function (assert) {
  var $music = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  var $mathematics = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableMulti = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:3");
  var $input = jQueryFrameFn(".tobago-treeSelect input");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectableNone().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($input().length, 0);
  });
  TTT.action(function () {
    $selectableMulti().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual($input().length, 0);
  });
  TTT.action(function () {
    $music().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Music");
  });
  TTT.action(function () {
    $mathematics().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Music, Mathematics");
  });
  TTT.action(function () {
    $music().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("multiLeafOnly: select Classic, select Mathematics, deselect Classic", function (assert) {
  var $classic = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  var $mathematics = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableMultiLeafOnly = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:4");
  var $input = jQueryFrameFn(".tobago-treeSelect input");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectableNone().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($input().length, 0);
  });
  TTT.action(function () {
    $selectableMultiLeafOnly().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual($input().length, 0);
  });
  TTT.action(function () {
    $classic().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Classic");
  });
  TTT.action(function () {
    $mathematics().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Classic, Mathematics");
  });
  TTT.action(function () {
    $classic().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("multiCascade: select Music, select Mathematics, deselect Classic", function (assert) {
  var $music = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  var $classic = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  var $mathematics = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableMultiCascade = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:5");
  var $input = jQueryFrameFn(".tobago-treeSelect input");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $selectableNone().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($input().length, 0);
  });
  TTT.action(function () {
    $selectableMultiCascade().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual($input().length, 0);
  });
  TTT.action(function () {
    $music().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse(); // an ajax request is send for every leaf (Music, Classic, Pop, World)
  TTT.waitMs(2000); // wait for the last ajax
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Music, Classic, Pop, World");
  });
  TTT.action(function () {
    $mathematics().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Music, Classic, Pop, World, Mathematics");
  });
  TTT.action(function () {
    $classic().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Music, Pop, World, Mathematics");
  });
  TTT.startTest();
});
