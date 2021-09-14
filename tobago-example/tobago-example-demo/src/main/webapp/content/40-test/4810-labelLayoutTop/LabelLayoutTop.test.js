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

it("fix tc:file first", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("not implemented yet"));
  test.start();
});

/*
import {querySelectorFn} from "/script/tobago-test.js";

QUnit.test("Check width for tc:date", function (assert) {
  assert.expect(2);
  testWidth(assert, "date");
});

QUnit.test("Check width for tc:file", function (assert) {
  assert.expect(2);
  testWidth(assert, "file");
});

QUnit.test("Check width for tc:in", function (assert) {
  assert.expect(2);
  testWidth(assert, "in");
});

QUnit.test("Check width for input group", function (assert) {
  assert.expect(2);
  testWidth(assert, "inGroup");
});

QUnit.test("Check width for tc:out", function (assert) {
  assert.expect(2);
  testWidth(assert, "out");
});

QUnit.test("Check width for tc:selectBooleanCheckbox", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectBooleanCheckbox");
});

QUnit.test("Check width for tc:selectManyCheckbox", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectManyCheckbox");
});

QUnit.test("Check width for tc:selectManyListbox", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectManyListbox");
});

QUnit.test("Check width for tc:selectManyShuttle", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectManyShuttle");
});

QUnit.test("Check width for tc:selectOneChoice", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectOneChoice");
});

QUnit.test("Check width for tc:selectOneListbox", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectOneListbox");
});

QUnit.test("Check width for tc:selectOneRadio", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectOneRadio");
});

QUnit.test("Check width for tc:textarea", function (assert) {
  assert.expect(2);
  testWidth(assert, "textarea");
});

function testWidth(assert, idPart) {
  let compLabelFn = querySelectorFn("#page\\:mainForm\\:" + idPart + " label");
  let compTopFn = querySelectorFn("#page\\:mainForm\\:" + idPart + "Top");

  assert.equal(getComputedStyle(compLabelFn()).width, "155px");
  assert.equal(getComputedStyle(compTopFn().querySelector("label")).width, getComputedStyle(compTopFn()).width);
}
*/