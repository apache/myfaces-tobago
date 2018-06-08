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
  let musicFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  let mathematicsFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableSingleFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:1");
  let inputFn = jQueryFrameFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectableNoneFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn().length, 0);
  });
  TTT.action(function () {
    selectableSingleFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn().length, 0);
  });
  TTT.action(function () {
    musicFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Music");
  });
  TTT.action(function () {
    mathematicsFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("singleLeafOnly: select Classic, select Mathematics", function (assert) {
  let classicFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  let mathematicsFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableSingleLeafOnlyFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:2");
  let inputFn = jQueryFrameFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectableNoneFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn().length, 0);
  });
  TTT.action(function () {
    selectableSingleLeafOnlyFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn().length, 0);
  });
  TTT.action(function () {
    classicFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Classic");
  });
  TTT.action(function () {
    mathematicsFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("multi: select Music, select Mathematics, deselect Music", function (assert) {
  let musicFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  let mathematicsFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableMultiFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:3");
  let inputFn = jQueryFrameFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectableNoneFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn().length, 0);
  });
  TTT.action(function () {
    selectableMultiFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn().length, 0);
  });
  TTT.action(function () {
    musicFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Music");
  });
  TTT.action(function () {
    mathematicsFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Music, Mathematics");
  });
  TTT.action(function () {
    musicFn().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("multiLeafOnly: select Classic, select Mathematics, deselect Classic", function (assert) {
  let classicFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  let mathematicsFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableMultiLeafOnlyFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:4");
  let inputFn = jQueryFrameFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectableNoneFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn().length, 0);
  });
  TTT.action(function () {
    selectableMultiLeafOnlyFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn().length, 0);
  });
  TTT.action(function () {
    classicFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Classic");
  });
  TTT.action(function () {
    mathematicsFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Classic, Mathematics");
  });
  TTT.action(function () {
    classicFn().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("multiCascade: select Music, select Mathematics, deselect Classic", function (assert) {
  let musicFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  let classicFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  let mathematicsFn = jQueryFrameFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableMultiCascadeFn = jQueryFrameFn("#page\\:mainForm\\:selectable\\:\\:5");
  let inputFn = jQueryFrameFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectableNoneFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn().length, 0);
  });
  TTT.action(function () {
    selectableMultiCascadeFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn().length, 0);
  });
  TTT.action(function () {
    musicFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse(); // an ajax request is send for every leaf (Music, Classic, Pop, World)
  TTT.waitMs(2000); // wait for the last ajax
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Music, Classic, Pop, World");
  });
  TTT.action(function () {
    mathematicsFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Music, Classic, Pop, World, Mathematics");
  });
  TTT.action(function () {
    classicFn().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Music, Pop, World, Mathematics");
  });
  TTT.startTest();
});
