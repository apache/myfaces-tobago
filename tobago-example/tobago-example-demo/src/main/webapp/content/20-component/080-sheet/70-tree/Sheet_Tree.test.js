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

QUnit.test("Collapse tree", function (assert) {
  let row0nameFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:nameOut");
  let row0centralBodyFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:centralBodyOut");
  let row0distanceFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:distanceOut");
  let row0periodFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:periodOut");
  let row0discovererFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:discovererOut");
  let row0yearFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:yearOut");
  let row1nameFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:nameOut");
  let row1centralBodyFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:centralBodyOut");
  let row1distanceFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:distanceOut");
  let row1periodFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:periodOut");
  let row1discovererFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:discovererOut");
  let row1yearFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:yearOut");
  let rootTreeButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:nameCol .tobago-treeNode-toggle");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(14, function () {
    assert.equal(row0nameFn().textContent, "Sun");
    assert.equal(row0centralBodyFn().textContent, "-");
    assert.equal(row0distanceFn().textContent, "0");
    assert.equal(row0periodFn().textContent, "0.0");
    assert.equal(row0discovererFn().textContent, "-");
    assert.equal(row0yearFn().textContent, "");
    assert.equal(row1nameFn().textContent, "Mercury");
    assert.equal(row1centralBodyFn().textContent, "Sun");
    assert.equal(row1distanceFn().textContent, "57910");
    assert.equal(row1periodFn().textContent, "87.97");
    assert.equal(row1discovererFn().textContent, "-");
    assert.equal(row1yearFn().textContent, "");

    let sheetRow = row1yearFn().parentElement.parentElement;
    assert.ok(sheetRow.classList.contains("tobago-sheet-row"));
    assert.notEqual(getComputedStyle(sheetRow).display, "none");
  });
  TTT.action(function () {
    rootTreeButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitMs(1000);
  TTT.asserts(8, function () {
    assert.equal(row0nameFn().textContent, "Sun");
    assert.equal(row0centralBodyFn().textContent, "-");
    assert.equal(row0distanceFn().textContent, "0");
    assert.equal(row0periodFn().textContent, "0.0");
    assert.equal(row0discovererFn().textContent, "-");
    assert.equal(row0yearFn().textContent, "");

    let sheetRow = row1yearFn().parentElement.parentElement;
    assert.ok(sheetRow.classList.contains("tobago-sheet-row"));
    assert.equal(getComputedStyle(sheetRow).display, "none");
  });
  TTT.startTest();
});
