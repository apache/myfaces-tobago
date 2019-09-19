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

import {testFrameQuerySelectorAllFn, testFrameQuerySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("Basics: 'M'", function (assert) {
  let inputString = "M";
  let expectedLength = 10;

  testMarsBasics(assert, inputString, expectedLength);
});

QUnit.test("Basics: 'Ma'", function (assert) {
  let inputString = "Ma";
  let expectedLength = 4;

  testMarsBasics(assert, inputString, expectedLength);
});

QUnit.test("Basics: 'Mar'", function (assert) {
  let inputString = "Mar";
  let expectedLength = 1;

  testMarsBasics(assert, inputString, expectedLength);
});

QUnit.test("Basics: 'Mars'", function (assert) {
  let inputString = "Mars";
  let expectedLength = 1;

  testMarsBasics(assert, inputString, expectedLength);
});

function testMarsBasics(assert, inputString, expectedLength) {
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:inBasic\\:\\:field");
  let suggestionsFn = getSuggestions("#page\\:mainForm\\:inBasic");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = inputString;
    inFn().dispatchEvent(new Event("input", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(expectedLength + 1, function () {
    assert.equal(suggestionsFn().length, expectedLength);
    for (let i = 0; i < expectedLength; i++) {
      assert.ok(suggestionsFn().item(i).querySelector("strong").textContent.toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }
  });
  TTT.startTest();
}

QUnit.test("Basics: Add 'eus' and click first entry.", function (assert) {
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:inBasic\\:\\:field");
  let suggestionsFn = getSuggestions("#page\\:mainForm\\:inBasic");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "eus";
    inFn().dispatchEvent(new Event("input", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(inFn().value, "eus");
    assert.equal(suggestionsFn().length, 3);
    assert.equal(suggestionsFn().item(0).querySelector("strong").textContent, "eus");
    assert.equal(suggestionsFn().item(1).querySelector("strong").textContent, "eus");
    assert.equal(suggestionsFn().item(2).querySelector("strong").textContent, "eus");
  });
  TTT.action(function () {
    suggestionsFn().item(0).dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.equal(inFn().value, "Prometheus");

  });
  TTT.startTest();
});

QUnit.test("Advanced: 'C'", function (assert) {
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:inAdvanced\\:\\:field");
  let suggestionsFn = getSuggestions("#page\\:mainForm\\:inAdvanced");
  let suggestionDelay = 2000;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "C";
    inFn().dispatchEvent(new Event("input", {bubbles: true}));
  });
  TTT.waitMs(suggestionDelay);
  TTT.asserts(1, function () {
    assert.equal(suggestionsFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Advanced: 'Ca'", function (assert) {
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:inAdvanced\\:\\:field");
  let suggestionsFn = getSuggestions("#page\\:mainForm\\:inAdvanced");
  let suggestionDelay = 2000;
  let startTime = Date.now();

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "Ca";
    inFn().dispatchEvent(new Event("input", {bubbles: true}));
  });
  TTT.waitMs(200); // default suggestion delay
  TTT.asserts(1, function () {
    // Nothing happen, because the delay is greater than the default delay.
    assert.equal(suggestionsFn().length, 0);
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(suggestionsFn().length, 2);
    assert.equal(suggestionsFn().item(0).querySelector("strong").textContent, "Ca");
    assert.equal(suggestionsFn().item(1).querySelector("strong").textContent, "Ca");
    assert.ok(Date.now() - startTime >= suggestionDelay, "Delay for suggest popup must be greater/equal "
        + suggestionDelay);
  });
  TTT.startTest();
});

QUnit.test("Client side: 'Ju'", function (assert) {
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:inClient\\:\\:field");
  let suggestionsFn = getSuggestions("#page\\:mainForm\\:inClient");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "Ju";
    inFn().dispatchEvent(new Event("input", {bubbles: true}));
  });
  TTT.asserts(3, function () {
    assert.equal(suggestionsFn().length, 2);
    assert.equal(suggestionsFn().item(0).querySelector("strong").textContent, "Ju");
    assert.equal(suggestionsFn().item(1).querySelector("strong").textContent, "Ju");
  });
  TTT.startTest();
});

function escapeClientId(clientId) {
  return '#' + clientId.replace(/([:\.])/g, '\\$1');
}

function getSuggestions(id) {
  return testFrameQuerySelectorAllFn(escapeClientId(
      testFrameQuerySelectorFn(id + " tobago-suggest")().id + "::popup") + " .tt-suggestion");
}
