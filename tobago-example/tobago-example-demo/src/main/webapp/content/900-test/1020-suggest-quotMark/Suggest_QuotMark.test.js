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

import {elementByIdFn, querySelectorAllFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Basics: 'M'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:input::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 3,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "M");
  test.event("input", inputFn, () => resultListFn().length === 3);
  test.do(() => expect(resultListFn().length).toBe(3));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mercury"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Mars"));
  test.do(() => expect(resultListFn()[2].textContent).toBe("Quotation\"Mark"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Basics: 'Ma'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:input::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 2,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "Ma");
  test.event("input", inputFn, () => resultListFn().length === 2);
  test.do(() => expect(resultListFn().length).toBe(2));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mars"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Quotation\"Mark"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Basics: 'Mar'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:input::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 2,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "Mar");
  test.event("input", inputFn, () => resultListFn().length === 2);
  test.do(() => expect(resultListFn().length).toBe(2));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mars"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Quotation\"Mark"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Basics: 'Mars'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:input::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 1,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "Mars");
  test.event("input", inputFn, () => resultListFn().length === 1);
  test.do(() => expect(resultListFn().length).toBe(1));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mars"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});
