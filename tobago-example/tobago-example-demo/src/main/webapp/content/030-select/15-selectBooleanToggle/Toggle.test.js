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
import {querySelectorFn} from "/script/tobago-test.js";

it("submit: select A", function (done) {
  let selectAFn = querySelectorFn("#page\\:mainForm\\:selectA input");
  let selectBFn = querySelectorFn("#page\\:mainForm\\:selectB input");
  let selectCFn = querySelectorFn("#page\\:mainForm\\:selectC input");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:submitOutput .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "",
      () => {
        selectAFn().checked = false;
        selectBFn().checked = false;
        selectCFn().checked = false;
      }, "click", submitFn);
  test.do(() => selectAFn().checked = true);
  test.do(() => selectBFn().checked = false);
  test.do(() => selectCFn().checked = false);
  test.event("click", submitFn, () => outputFn().textContent.trim(), "A");
  test.do(() => expect(outputFn().textContent.trim()).toBe("A"));
  test.start();
});

it("submit: select B and C", function (done) {
  let selectAFn = querySelectorFn("#page\\:mainForm\\:selectA input");
  let selectBFn = querySelectorFn("#page\\:mainForm\\:selectB input");
  let selectCFn = querySelectorFn("#page\\:mainForm\\:selectC input");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:submitOutput .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "",
      () => {
        selectAFn().checked = false;
        selectBFn().checked = false;
        selectCFn().checked = false;
      }, "click", submitFn);
  test.do(() => selectAFn().checked = false);
  test.do(() => selectBFn().checked = true);
  test.do(() => selectCFn().checked = true);
  test.event("click", submitFn, () => outputFn().textContent.trim(), "B C");
  test.do(() => expect(outputFn().textContent.trim()).toBe("B C"));
  test.start();
});

it("ajax: select D", function (done) {
  let selectFn = querySelectorFn("#page\\:mainForm\\:selectD input");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputD .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "false",
      () => selectFn().checked = false,
      "change", selectFn);
  test.do(() => selectFn().checked = true);
  test.event("change", selectFn, () => outputFn().textContent === "true")
  test.do(() => expect(outputFn().textContent).toBe("true"));
  test.start();
});

it("ajax: deselect D", function (done) {
  let selectFn = querySelectorFn("#page\\:mainForm\\:selectD input");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputD .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "true",
      () => selectFn().checked = true,
      "change", selectFn);
  test.do(() => selectFn().checked = false);
  test.event("change", selectFn, () => outputFn().textContent === "false")
  test.do(() => expect(outputFn().textContent).toBe("false"));
  test.start();
});

it("ajax: select E", function (done) {
  let selectFn = querySelectorFn("#page\\:mainForm\\:selectE input");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputE .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "false",
      () => selectFn().checked = false,
      "change", selectFn);
  test.do(() => selectFn().checked = true);
  test.event("change", selectFn, () => outputFn().textContent === "true")
  test.do(() => expect(outputFn().textContent).toBe("true"));
  test.start();
});

it("ajax: deselect E", function (done) {
  let selectFn = querySelectorFn("#page\\:mainForm\\:selectE input");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputE .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "true",
      () => selectFn().checked = true,
      "change", selectFn);
  test.do(() => selectFn().checked = false);
  test.event("change", selectFn, () => outputFn().textContent === "false")
  test.do(() => expect(outputFn().textContent).toBe("false"));
  test.start();
});

it("ajax: select F", function (done) {
  let selectFn = querySelectorFn("#page\\:mainForm\\:selectF input");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputF .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "false",
      () => selectFn().checked = false,
      "change", selectFn);
  test.do(() => selectFn().checked = true);
  test.event("change", selectFn, () => outputFn().textContent === "true")
  test.do(() => expect(outputFn().textContent).toBe("true"));
  test.start();
});

it("ajax: deselect F", function (done) {
  let selectFn = querySelectorFn("#page\\:mainForm\\:selectF input");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputF .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "true",
      () => selectFn().checked = true,
      "change", selectFn);
  test.do(() => selectFn().checked = false);
  test.event("change", selectFn, () => outputFn().textContent === "false")
  test.do(() => expect(outputFn().textContent).toBe("false"));
  test.start();
});
