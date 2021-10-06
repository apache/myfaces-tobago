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
import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";

it("Check width for tc:date", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "date");
  test.start();
});

it("Check width for tc:file", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "file");
  test.start();
});

it("Check width for tc:in", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "in");
  test.start();
});

it("Check width for input group", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "inGroup");
  test.start();
});

it("Check width for tc:out", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "out");
  test.start();
});

it("Check width for tc:selectBooleanCheckbox", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "selectBooleanCheckbox");
  test.start();
});

it("Check width for tc:selectManyCheckbox", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "selectManyCheckbox");
  test.start();
});

it("Check width for tc:selectManyListbox", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "selectManyListbox");
  test.start();
});

it("Check width for tc:selectManyShuttle", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "selectManyShuttle");
  test.start();
});

it("Check width for tc:selectOneChoice", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "selectOneChoice");
  test.start();
});

it("Check width for tc:selectOneListbox", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "selectOneListbox");
  test.start();
});

it("Check width for tc:selectOneRadio", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "selectOneRadio");
  test.start();
});

it("Check width for tc:textarea", function (done) {
  const test = new JasmineTestTool(done);
  addSteps(test, "textarea");
  test.start();
});

function addSteps(test, idPart) {
  const compLabelFn = querySelectorFn("#page\\:mainForm\\:" + idPart + " label");
  const compTopLabelFn = querySelectorFn("#page\\:mainForm\\:" + idPart + "Top label");
  const referenceLabel = elementByIdFn("page:mainForm:referenceLabel");

  test.do(() => expect(getComputedStyle(compLabelFn()).width).toBe("155px"));
  test.do(() => expect(getComputedStyle(compTopLabelFn()).width)
      .toBeLessThanOrEqual(getComputedStyle(referenceLabel()).width));
}
