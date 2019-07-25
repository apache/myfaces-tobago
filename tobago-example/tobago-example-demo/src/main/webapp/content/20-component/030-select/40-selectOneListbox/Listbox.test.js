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

import {jQueryFrameFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("submit: select 'Nile'", function (assert) {
  var riversFn = jQueryFrameFn("#page\\:mainForm\\:riverList option");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:riverSubmit");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:riverOutput span");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    riversFn().eq(0).prop("selected", true); // Nile
    riversFn().eq(1).prop("selected", false); // Amazon
    riversFn().eq(2).prop("selected", false); // Yangtze
    riversFn().eq(3).prop("selected", false); // Yellow River
    riversFn().eq(4).prop("selected", false); // Paraná River
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "6853 km");
  });
  TTT.startTest();
});

QUnit.test("submit: select 'Yangtze'", function (assert) {
  var riversFn = jQueryFrameFn("#page\\:mainForm\\:riverList option");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:riverSubmit");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:riverOutput span");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    riversFn().eq(0).prop("selected", false); // Nile
    riversFn().eq(1).prop("selected", false); // Amazon
    riversFn().eq(2).prop("selected", true); // Yangtze
    riversFn().eq(3).prop("selected", false); // Yellow River
    riversFn().eq(4).prop("selected", false); // Paraná River
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "6300 km");

  });
  TTT.startTest();
});

QUnit.test("ajax: select Everest", function (assert) {
  var mountainsFn = jQueryFrameFn("#page\\:mainForm\\:mountainList option");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:selectedMountain span");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    mountainsFn().eq(1).prop("selected", false);
    mountainsFn().eq(2).prop("selected", false);
    mountainsFn().eq(3).prop("selected", false);
    mountainsFn().eq(4).prop("selected", false);
    mountainsFn().eq(0).prop("selected", true).trigger("change"); // Everest
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "8848 m");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Makalu", function (assert) {
  var mountainsFn = jQueryFrameFn("#page\\:mainForm\\:mountainList option");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:selectedMountain span");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    mountainsFn().eq(0).prop("selected", false);
    mountainsFn().eq(1).prop("selected", false);
    mountainsFn().eq(2).prop("selected", false);
    mountainsFn().eq(3).prop("selected", false);
    mountainsFn().eq(4).prop("selected", true).trigger("change"); // Everest
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "8481 m");
  });
  TTT.startTest();
});
