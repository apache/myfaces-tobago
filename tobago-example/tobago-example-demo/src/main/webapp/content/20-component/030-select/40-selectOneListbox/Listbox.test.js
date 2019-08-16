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

QUnit.test("submit: select 'Nile'", function (assert) {
  let riversFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:riverList option");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:riverSubmit");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:riverOutput span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    riversFn().item(0).selected = true; // Nile
    riversFn().item(1).selected = false; // Amazon
    riversFn().item(2).selected = false; // Yangtze
    riversFn().item(3).selected = false; // Yellow River
    riversFn().item(4).selected = false; // Paraná River
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "6853 km");
  });
  TTT.startTest();
});

QUnit.test("submit: select 'Yangtze'", function (assert) {
  let riversFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:riverList option");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:riverSubmit");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:riverOutput span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    riversFn().item(0).selected = false; // Nile
    riversFn().item(1).selected = false; // Amazon
    riversFn().item(2).selected = true; // Yangtze
    riversFn().item(3).selected = false; // Yellow River
    riversFn().item(4).selected = false; // Paraná River
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "6300 km");

  });
  TTT.startTest();
});

QUnit.test("ajax: select Everest", function (assert) {
  let mountainListFn = testFrameQuerySelectorFn("#page\\:mainForm\\:mountainList\\:\\:field");
  let mountainsFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:mountainList option");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectedMountain span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    mountainsFn().item(1).selected = false;
    mountainsFn().item(2).selected = false;
    mountainsFn().item(3).selected = false;
    mountainsFn().item(4).selected = false;
    mountainsFn().item(0).selected = true; // Everest
    mountainListFn().dispatchEvent(new Event('change')); // Everest
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "8848 m");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Makalu", function (assert) {
  let mountainListFn = testFrameQuerySelectorFn("#page\\:mainForm\\:mountainList\\:\\:field");
  let mountainsFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:mountainList option");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectedMountain span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    mountainsFn().item(0).selected = false;
    mountainsFn().item(1).selected = false;
    mountainsFn().item(2).selected = false;
    mountainsFn().item(3).selected = false;
    mountainsFn().item(4).selected = true; // Everest
    mountainListFn().dispatchEvent(new Event('change')); // Everest
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "8481 m");
  });
  TTT.startTest();
});
