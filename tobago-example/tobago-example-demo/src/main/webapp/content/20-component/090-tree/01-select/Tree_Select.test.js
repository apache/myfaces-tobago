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

QUnit.test("single: select Music, select Mathematics", function (assert) {
  let musicFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  let mathematicsFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableSingleFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:1");
  let inputFn = testFrameQuerySelectorFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), undefined);
  });
  TTT.action(function () {
    selectableSingleFn().checked = true;
    selectableSingleFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), undefined);
  });
  TTT.action(function () {
    musicFn().checked = true;
    musicFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music");
  });
  TTT.action(function () {
    mathematicsFn().checked = true;
    mathematicsFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("singleLeafOnly: select Classic, select Mathematics", function (assert) {
  let classicFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  let mathematicsFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableSingleLeafOnlyFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:2");
  let inputFn = testFrameQuerySelectorFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), undefined);
  });
  TTT.action(function () {
    selectableSingleLeafOnlyFn().checked = true;
    selectableSingleLeafOnlyFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), undefined);
  });
  TTT.action(function () {
    classicFn().checked = true;
    classicFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Classic");
  });
  TTT.action(function () {
    mathematicsFn().checked = true;
    mathematicsFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("multi: select Music, select Mathematics, deselect Music", function (assert) {
  let musicFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  let mathematicsFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableMultiFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:3");
  let inputFn = testFrameQuerySelectorFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), undefined);
  });
  TTT.action(function () {
    selectableMultiFn().checked = true;
    selectableMultiFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), undefined);
  });
  TTT.action(function () {
    musicFn().checked = true;
    musicFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music");
  });
  TTT.action(function () {
    mathematicsFn().checked = true;
    mathematicsFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music, Mathematics");
  });
  TTT.action(function () {
    musicFn().checked = false;
    musicFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("multiLeafOnly: select Classic, select Mathematics, deselect Classic", function (assert) {
  let classicFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  let mathematicsFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableMultiLeafOnlyFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:4");
  let inputFn = testFrameQuerySelectorFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), undefined);
  });
  TTT.action(function () {
    selectableMultiLeafOnlyFn().checked = true;
    selectableMultiLeafOnlyFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), undefined);
  });
  TTT.action(function () {
    classicFn().checked = true;
    classicFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Classic");
  });
  TTT.action(function () {
    mathematicsFn().checked = true;
    mathematicsFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Classic, Mathematics");
  });
  TTT.action(function () {
    classicFn().checked = false;
    classicFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("multiCascade: select Music, select Mathematics, deselect Classic", function (assert) {
  let musicFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  let classicFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  let mathematicsFn = testFrameQuerySelectorFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableMultiCascadeFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectable\\:\\:5");
  let inputFn = testFrameQuerySelectorFn(".tobago-treeSelect input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), undefined);
  });
  TTT.action(function () {
    selectableMultiCascadeFn().checked = true;
    selectableMultiCascadeFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), undefined);
  });
  TTT.action(function () {
    musicFn().checked = true;
    musicFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse(); // an ajax request is send for every leaf (Music, Classic, Pop, World)
  TTT.waitMs(2000); // wait for the last ajax
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music, Classic, Pop, World");
  });
  TTT.action(function () {
    mathematicsFn().checked = true;
    mathematicsFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music, Classic, Pop, World, Mathematics");
  });
  TTT.action(function () {
    classicFn().checked = false;
    classicFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music, Pop, World, Mathematics");
  });
  TTT.startTest();
});
