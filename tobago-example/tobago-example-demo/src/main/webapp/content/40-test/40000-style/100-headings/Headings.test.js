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

QUnit.test("Test h1", function (assert) {
  let alinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:link1");
  let buttonlinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:actionLink1");
  testFont(assert, alinkFn, buttonlinkFn);
});

QUnit.test("Test h2", function (assert) {
  let alinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:link2");
  let buttonlinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:actionLink2");
  testFont(assert, alinkFn, buttonlinkFn);
});

QUnit.test("Test h3", function (assert) {
  let alinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:link3");
  let buttonlinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:actionLink3");
  testFont(assert, alinkFn, buttonlinkFn);
});

QUnit.test("Test h4", function (assert) {
  let alinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:link4");
  let buttonlinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:actionLink4");
  testFont(assert, alinkFn, buttonlinkFn);
});

QUnit.test("Test h5", function (assert) {
  let alinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:link5");
  let buttonlinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:actionLink5");
  testFont(assert, alinkFn, buttonlinkFn);
});

QUnit.test("Test h6", function (assert) {
  let alinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:link6");
  let buttonlinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:actionLink6");
  testFont(assert, alinkFn, buttonlinkFn);
});

QUnit.test("Test no heading", function (assert) {
  let alinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:link0");
  let buttonlinkFn = testFrameQuerySelectorFn("#page\\:mainForm\\:actionLink0");
  testFont(assert, alinkFn, buttonlinkFn);
});

function testFont(assert, alinkFn, buttonlinkFn) {
  assert.expect(5);

  const alinkComputedStyle = getComputedStyle(alinkFn());
  const buttonlinkFnComputedStyle = getComputedStyle(buttonlinkFn());
  assert.equal(alinkComputedStyle.color, buttonlinkFnComputedStyle.color);
  assert.equal(alinkComputedStyle.fontFamily, buttonlinkFnComputedStyle.fontFamily);
  assert.equal(alinkComputedStyle.fontSize, buttonlinkFnComputedStyle.fontSize);
  assert.equal(alinkComputedStyle.fontWeight, buttonlinkFnComputedStyle.fontWeight);
  assert.equal(alinkComputedStyle.textDecoration, buttonlinkFnComputedStyle.textDecoration);
}

QUnit.test("Ajax reload for section 2", function (assert) {
  let reloadButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:reloadSection2");
  let section2HeaderFn = testFrameQuerySelectorFn("#page\\:mainForm\\:levelTwoSection h3");
  let timestampFn = testFrameQuerySelectorFn("#page\\:mainForm\\:timestamp span");
  let firstTimestamp = timestampFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.ok(section2HeaderFn() !== null);
  });
  TTT.action(function () {
    reloadButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.ok(section2HeaderFn() !== null);
    assert.ok(firstTimestamp < timestampFn().textContent, "value of new timestamp must be higher");
  });
  TTT.startTest();
});
