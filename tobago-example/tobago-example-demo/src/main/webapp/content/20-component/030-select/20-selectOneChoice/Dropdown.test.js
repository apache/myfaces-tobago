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
import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("submit: Alice", function (assert) {
  let aliceFn = querySelectorFn("#page\\:mainForm\\:selectPerson\\:\\:field option[value^='Alice']");
  let bobFn = querySelectorFn("#page\\:mainForm\\:selectPerson\\:\\:field option[value^='Bob']");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputPerson span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    aliceFn().selected = true;
    bobFn().selected = false;
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Alice Anderson");
  });
  TTT.startTest();
});

QUnit.test("submit: Bob", function (assert) {
  let aliceFn = querySelectorFn("#page\\:mainForm\\:selectPerson\\:\\:field option[value^='Alice']");
  let bobFn = querySelectorFn("#page\\:mainForm\\:selectPerson\\:\\:field option[value^='Bob']");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputPerson span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    aliceFn().selected = false;
    bobFn().selected = true;
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Bob Brunch");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Mars", function (assert) {
  let planetFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field");
  let marsOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='1']");
  let jupiterOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='2']");
  let moonsFn = querySelectorAllFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    jupiterOptionFn().selected = false;
    marsOptionFn().selected = true;
    planetFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(moonsFn().item(0).text, "Phobos");
    assert.equal(moonsFn().item(1).text, "Deimos");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Jupiter", function (assert) {
  let planetFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field");
  let marsOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='1']");
  let jupiterOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='2']");
  let moonsFn = querySelectorAllFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    marsOptionFn().selected = false;
    jupiterOptionFn().selected = true;
    planetFn().dispatchEvent(new Event("change", {bubbles: true}));
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
*/
