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

import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("inputfield with label", function (done) {
  const labelFn = querySelectorFn("#page\\:mainForm\\:dNormal > label");
  const dateFieldFn = querySelectorFn("#page\\:mainForm\\:dNormal\\:\\:field");
  const sputnik = "1969-07-20";
  const other = "1999-12-31";

  const test = new JasmineTestTool(done);
  test.do(() => expect(labelFn().textContent).toBe("Date"));
  test.do(() => expect(dateFieldFn().value).toBe(sputnik));
  test.do(() => dateFieldFn().value = other);
  test.do(() => expect(dateFieldFn().value).toBe(other));
  test.start();
});

it("submit", function (done) {
  const dateFieldFn = elementByIdFn("page:mainForm:formSubmit:input::field");
  const outputFn = querySelectorFn("#page\\:mainForm\\:formSubmit\\:output .form-control-plaintext");
  const submitFn = elementByIdFn("page:mainForm:formSubmit:button");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "2016-05-22", () => dateFieldFn().value = "2016-05-22", "click", submitFn);
  test.do(() => expect(outputFn().textContent).toBe("2016-05-22"));
  test.do(() => dateFieldFn().value = "1952-07-29");
  test.event("click", submitFn, () => outputFn().textContent === "1952-07-29");
  test.do(() => expect(outputFn().textContent).toBe("1952-07-29"));
  test.start();
});

it("ajax", function (done) {
  const dateFieldFn = elementByIdFn("page:mainForm:ajaxinput::field");
  const outputFn = querySelectorFn("#page\\:mainForm\\:outputfield .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "", () => dateFieldFn().value = "", "change", dateFieldFn);
  test.do(() => expect(outputFn().textContent).toBe(""));
  test.do(() => dateFieldFn().value = "1857-03-04");
  test.event("change", dateFieldFn, () => outputFn().textContent === "1857-03-04");
  test.do(() => expect(outputFn().textContent).toBe("1857-03-04"));
  test.start();
});
