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

import {jQueryFrameFn, testFrameQuerySelectorAllFn, testFrameQuerySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("submit: Alice", function (assert) {
  let aliceFn = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Alice')");
  let bobFn = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Bob')");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:outputPerson span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    aliceFn().prop("selected", true);
    bobFn().prop("selected", false);
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Alice Anderson");

  });
  TTT.startTest();
});

QUnit.test("submit: Bob", function (assert) {
  let aliceFn = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Alice')");
  let bobFn = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Bob')");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:outputPerson span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    aliceFn().prop("selected", false);
    bobFn().prop("selected", true);
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Bob Brunch");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Mars", function (assert) {
  let planetFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field");
  let marsOptionFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='1']");
  let jupiterOptionFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='2']");
  let moonsFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    jupiterOptionFn().selected = false;
    marsOptionFn().selected = true;
    planetFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(moonsFn().item(0).text, "Phobos");
    assert.equal(moonsFn().item(1).text, "Deimos");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Jupiter", function (assert) {
  let planetFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field");
  let marsOptionFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='1']");
  let jupiterOptionFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='2']");
  let moonsFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    marsOptionFn().selected = false;
    jupiterOptionFn().selected = true;
    planetFn().dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(moonsFn().item(0).text, "Europa");
    assert.equal(moonsFn().item(1).text, "Ganymed");
    assert.equal(moonsFn().item(2).text, "Io");
    assert.equal(moonsFn().item(3).text, "Kallisto");
  });
  TTT.startTest();
});
