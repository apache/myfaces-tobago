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

import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("not implemented yet", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("not implemented yet"));
  test.start();
});

/*
import {querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("single: select Music, select Mathematics", function (assert) {
  let musicFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  let mathematicsFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableSingleFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:1");
  let inputFn = querySelectorFn("tobago-tree-select input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), null);
  });
  TTT.action(function () {
    selectableSingleFn().checked = true;
    selectableSingleFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), null);
  });
  TTT.action(function () {
    musicFn().checked = true;
    musicFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music");
  });
  TTT.action(function () {
    mathematicsFn().checked = true;
    mathematicsFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Mathematics");
  });
  TTT.startTest();
});

QUnit.test("singleLeafOnly: select Classic, select Geography", function (assert) {
  let classicFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  let geographyFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:10\\:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableSingleLeafOnlyFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:2");
  let inputFn = querySelectorFn("tobago-tree-select input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), null);
  });
  TTT.action(function () {
    selectableSingleLeafOnlyFn().checked = true;
    selectableSingleLeafOnlyFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), null);
  });
  TTT.action(function () {
    classicFn().checked = true;
    classicFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Classic");
  });
  TTT.action(function () {
    geographyFn().checked = true;
    geographyFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Geography");
  });
  TTT.startTest();
});

QUnit.test("multi: select Music, select Geography, deselect Music", function (assert) {
  let musicFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  let geographyFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:10\\:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableMultiFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:3");
  let inputFn = querySelectorFn("tobago-tree-select input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), null);
  });
  TTT.action(function () {
    selectableMultiFn().checked = true;
    selectableMultiFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), null);
  });
  TTT.action(function () {
    musicFn().checked = true;
    musicFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music");
  });
  TTT.action(function () {
    geographyFn().checked = true;
    geographyFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music, Geography");
  });
  TTT.action(function () {
    musicFn().checked = false;
    musicFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Geography");
  });
  TTT.startTest();
});

QUnit.test("multiLeafOnly: select Classic, select Geography, deselect Classic", function (assert) {
  let classicFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  let geographyFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableMultiLeafOnlyFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:4");
  let inputFn = querySelectorFn("tobago-tree-select input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), null);
  });
  TTT.action(function () {
    selectableMultiLeafOnlyFn().checked = true;
    selectableMultiLeafOnlyFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), null);
  });
  TTT.action(function () {
    classicFn().checked = true;
    classicFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Classic");
  });
  TTT.action(function () {
    geographyFn().checked = true;
    geographyFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Classic, Geography");
  });
  TTT.action(function () {
    classicFn().checked = false;
    classicFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Geography");
  });
  TTT.startTest();
});

QUnit.test("multiCascade: select Music, select Mathematics, deselect Classic", function (assert) {
  let musicFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  let classicFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  let mathematicsFn = querySelectorFn("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput span");
  let selectableNoneFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:0");
  let selectableMultiCascadeFn = querySelectorFn("#page\\:mainForm\\:selectable\\:\\:5");
  let inputFn = querySelectorFn("tobago-tree-select input");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectableNoneFn().checked = true;
    selectableNoneFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFn(), null);
  });
  TTT.action(function () {
    selectableMultiCascadeFn().checked = true;
    selectableMultiCascadeFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notEqual(inputFn(), null);
  });
  TTT.action(function () {
    musicFn().checked = true;
    musicFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse(); // an ajax request is send for every leaf (Music, Classic, Pop, World)
  TTT.waitMs(2000); // wait for the last ajax
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music, Classic, Pop, World");
  });
  TTT.action(function () {
    mathematicsFn().checked = true;
    mathematicsFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music, Classic, Pop, World, Mathematics");
  });
  TTT.action(function () {
    classicFn().checked = false;
    classicFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Music, Pop, World, Mathematics");
  });
  TTT.startTest();
});
*/
