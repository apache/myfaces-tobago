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

QUnit.test("submit: Alice", function (assert) {
  var aliceFn = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Alice')");
  var bobFn = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Bob')");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:outputPerson span");

  var TTT = new TobagoTestTool(assert);
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
  var aliceFn = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Alice')");
  var bobFn = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Bob')");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:outputPerson span");

  var TTT = new TobagoTestTool(assert);
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
  var marsFn = jQueryFrameFn("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Mars')");
  var jupiterFn = jQueryFrameFn("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Jupiter')");
  var moonsFn = jQueryFrameFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    jupiterFn().prop("selected", false);
    marsFn().prop("selected", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(moonsFn().eq(0).text().trim(), "Phobos");
    assert.equal(moonsFn().eq(1).text().trim(), "Deimos");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Jupiter", function (assert) {
  var marsFn = jQueryFrameFn("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Mars')");
  var jupiterFn = jQueryFrameFn("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Jupiter')");
  var moonsFn = jQueryFrameFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    marsFn().prop("selected", false);
    jupiterFn().prop("selected", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(moonsFn().eq(0).text().trim(), "Europa");
    assert.equal(moonsFn().eq(1).text().trim(), "Ganymed");
    assert.equal(moonsFn().eq(2).text().trim(), "Io");
    assert.equal(moonsFn().eq(3).text().trim(), "Kallisto");
  });
  TTT.startTest();
});
