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
import {elementByIdFn, querySelectorAllFn} from "/script/tobago-test.js";

const fatalBoxShadow = "rgba(220, 53, 69, 0.25) 0px 0px 0px 4px";
const errorBoxShadow = "rgba(220, 53, 69, 0.25) 0px 0px 0px 4px";
const warnBoxShadow = "rgba(255, 193, 7, 0.25) 0px 0px 0px 4px";
const infoBoxShadow = "rgba(13, 202, 240, 0.25) 0px 0px 0px 4px";

describe("Fatal shadow", function () {
  it("tc:in", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalIn::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:date", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalDate::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:textarea", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalTextarea::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:selectBooleanCheckbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalCheckbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:selectBooleanToggle", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalToggle::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:selectManyCheckbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalCheckboxes::0");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:selectOneRadio", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalRadio::0");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:selectOneChoice", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalDropdown::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:selectOneListbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalOneListbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:selectManyListbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalManyListbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });

  it("tc:selectManyShuttle", function (done) {
    const componentFn = elementByIdFn("page:mainForm:fatalShuttle::selected");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, fatalBoxShadow);
    test.start();
  });
});

describe("Error shadow", function () {
  it("tc:in", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorIn::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:date", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorDate::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:textarea", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorTextarea::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:selectBooleanCheckbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorCheckbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:selectBooleanToggle", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorToggle::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:selectManyCheckbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorCheckboxes::0");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:selectOneRadio", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorRadio::0");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:selectOneChoice", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorDropdown::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:selectOneListbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorOneListbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:selectManyListbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorManyListbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });

  it("tc:selectManyShuttle", function (done) {
    const componentFn = elementByIdFn("page:mainForm:errorShuttle::selected");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, errorBoxShadow);
    test.start();
  });
});

describe("Warning shadow", function () {
  it("tc:in", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnIn::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:date", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnDate::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:textarea", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnTextarea::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:selectBooleanCheckbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnCheckbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:selectBooleanToggle", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnToggle::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:selectManyCheckbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnCheckboxes::0");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:selectOneRadio", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnRadio::0");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:selectOneChoice", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnDropdown::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:selectOneListbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnOneListbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:selectManyListbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnManyListbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });

  it("tc:selectManyShuttle", function (done) {
    const componentFn = elementByIdFn("page:mainForm:warnShuttle::selected");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, warnBoxShadow);
    test.start();
  });
});

describe("Information shadow", function () {
  it("tc:in", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoIn::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:date", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoDate::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:textarea", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoTextarea::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:selectBooleanCheckbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoCheckbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:selectBooleanToggle", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoToggle::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:selectManyCheckbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoCheckboxes::0");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:selectOneRadio", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoRadio::0");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:selectOneChoice", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoDropdown::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:selectOneListbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoOneListbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:selectManyListbox", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoManyListbox::field");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });

  it("tc:selectManyShuttle", function (done) {
    const componentFn = elementByIdFn("page:mainForm:infoShuttle::selected");

    const test = new JasmineTestTool(done);
    addSteps(test, componentFn, infoBoxShadow);
    test.start();
  });
});

function addSteps(test, componentFn, shadow) {
  const messagesFn = querySelectorAllFn("#page\\:messages .alert");
  const submitButtonFn = elementByIdFn("page:mainForm:submit");

  test.setup(() => messagesFn().length > 0, null, "click", submitButtonFn);
  test.do(() => componentFn().focus());
  test.wait(() => getComputedStyle(componentFn()).boxShadow === shadow);
  test.do(() => expect(getComputedStyle(componentFn()).boxShadow).toBe(shadow));
}
