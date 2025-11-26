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

import {elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("submit button", function (done) {
  const timestampFn = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const selected = elementByIdFn("page:mainForm:sheet::selected")
  const sun = elementByIdFn("page:mainForm:sheet:1:outOrbit")
  const submitButton = elementByIdFn("page:mainForm:sheet:0:submitButton")

  let timestamp;

  const test = new JasmineTestTool(done);
  test.setup(() => selected().value === "[1]", null, "click", sun);
  test.do(() => timestamp = timestampFn().textContent);
  test.event("click", submitButton, () => timestamp !== timestampFn().textContent);
  test.do(() => expect(selected().value).toEqual("[1]"));
  test.start();
});

it("ajax button", function (done) {
  const timestampFn = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const selected = elementByIdFn("page:mainForm:sheet::selected")
  const sun = elementByIdFn("page:mainForm:sheet:1:outOrbit")
  const ajaxButton = elementByIdFn("page:mainForm:sheet:0:ajaxButton")

  let timestamp;

  const test = new JasmineTestTool(done);
  test.setup(() => selected().value === "[1]", null, "click", sun);
  test.do(() => timestamp = timestampFn().textContent);
  test.event("click", ajaxButton, () => timestamp !== timestampFn().textContent);
  test.do(() => expect(selected().value).toEqual("[1]"));
  test.start();
});

it("submit button in panel", function (done) {
  const timestampFn = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const selected = elementByIdFn("page:mainForm:sheet::selected")
  const sun = elementByIdFn("page:mainForm:sheet:1:outOrbit")
  const submitButton = elementByIdFn("page:mainForm:sheet:0:submitPanelButton")

  let timestamp;

  const test = new JasmineTestTool(done);
  test.setup(() => selected().value === "[1]", null, "click", sun);
  test.do(() => timestamp = timestampFn().textContent);
  test.event("click", submitButton, () => timestamp !== timestampFn().textContent);
  test.do(() => expect(selected().value).toEqual("[1]"));
  test.start();
});

it("ajax button in panel", function (done) {
  const timestampFn = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const selected = elementByIdFn("page:mainForm:sheet::selected")
  const sun = elementByIdFn("page:mainForm:sheet:1:outOrbit")
  const ajaxButton = elementByIdFn("page:mainForm:sheet:0:ajaxPanelButton")

  let timestamp;

  const test = new JasmineTestTool(done);
  test.setup(() => selected().value === "[1]", null, "click", sun);
  test.do(() => timestamp = timestampFn().textContent);
  test.event("click", ajaxButton, () => timestamp !== timestampFn().textContent);
  test.do(() => expect(selected().value).toEqual("[1]"));
  test.start();
});

it("select 'gamma' from list", function (done) {
  const timestampFn = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const selected = elementByIdFn("page:mainForm:sheet::selected")
  const sun = elementByIdFn("page:mainForm:sheet:1:outOrbit")
  const listField = elementByIdFn("page:mainForm:sheet:0:list::field")
  const listOptions = querySelectorAllFn("#page\\:mainForm\\:sheet\\:0\\:list option");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.setup(() => selected().value === "[1]", null, "click", sun);
  test.do(() => timestamp = timestampFn().textContent);
  test.do(() => listOptions().item(0).selected = false);
  test.do(() => listOptions().item(1).selected = false);
  test.do(() => listOptions().item(2).selected = true); //Gamma
  test.do(() => listOptions().item(3).selected = false);
  test.event("change", listField, () => timestamp !== timestampFn().textContent);
  test.do(() => expect(selected().value).toEqual("[1]"));
  test.start();
});
