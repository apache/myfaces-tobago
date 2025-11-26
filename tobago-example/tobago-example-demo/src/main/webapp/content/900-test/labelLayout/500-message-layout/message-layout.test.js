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
import {elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";

it("Input", function (done) {
  const alertsFn = querySelectorAllFn("tobago-messages .alert");
  const submitFn = elementByIdFn("page:mainForm:submit");
  const compareFn = elementByIdFn("page:mainForm:inCompare");
  const labelFn = querySelectorFn("#page\\:mainForm\\:in label");
  const inputFn = querySelectorFn("#page\\:mainForm\\:in\\:\\:field");
  const popoverFn = querySelectorFn("#page\\:mainForm\\:in tobago-popover");

  const test = new JasmineTestTool(done);
  test.setup(() => alertsFn().length > 0, null, "click", submitFn);
  test.do(() => expect(getFullWidth(compareFn())).toBeCloseTo(
      getFullWidth(labelFn()) + getFullWidth(inputFn()) + getFullWidth(popoverFn())
  ));
  test.start();
});

it("Input Group", function (done) {
  const alertsFn = querySelectorAllFn("tobago-messages .alert");
  const submitFn = elementByIdFn("page:mainForm:submit");
  const compareFn = elementByIdFn("page:mainForm:inputGroupCompare");
  const labelFn = querySelectorFn("#page\\:mainForm\\:inputGroup label");
  const inputFn = querySelectorFn("#page\\:mainForm\\:inputGroup .input-group");
  const popoverFn = querySelectorFn("#page\\:mainForm\\:inputGroup tobago-popover");

  const test = new JasmineTestTool(done);
  test.setup(() => alertsFn().length > 0, null, "click", submitFn);
  test.do(() => expect(getFullWidth(compareFn())).toBeCloseTo(
      getFullWidth(labelFn()) + getFullWidth(inputFn()) + getFullWidth(popoverFn())
  ));
  test.start();
});

it("Date", function (done) {
  const alertsFn = querySelectorAllFn("tobago-messages .alert");
  const submitFn = elementByIdFn("page:mainForm:submit");
  const compareFn = elementByIdFn("page:mainForm:dateCompare");
  const labelFn = querySelectorFn("#page\\:mainForm\\:date label");
  const inputFn = querySelectorFn("#page\\:mainForm\\:date\\:\\:field");
  const popoverFn = querySelectorFn("#page\\:mainForm\\:date tobago-popover");

  const test = new JasmineTestTool(done);
  test.setup(() => alertsFn().length > 0, null, "click", submitFn);
  test.do(() => expect(getFullWidth(compareFn())).toBeCloseTo(
      getFullWidth(labelFn()) + getFullWidth(inputFn()) + getFullWidth(popoverFn())
  ));
  test.start();
});

it("Shuttle", function (done) {
  const alertsFn = querySelectorAllFn("tobago-messages .alert");
  const submitFn = elementByIdFn("page:mainForm:submit");
  const compareFn = elementByIdFn("page:mainForm:shuttleCompare");
  const labelFn = querySelectorFn("#page\\:mainForm\\:shuttle label");
  const inputFn = querySelectorFn("#page\\:mainForm\\:shuttle .tobago-body");
  const popoverFn = querySelectorFn("#page\\:mainForm\\:shuttle tobago-popover");

  const test = new JasmineTestTool(done);
  test.setup(() => alertsFn().length > 0, null, "click", submitFn);
  test.do(() => expect(getFullWidth(compareFn())).toBeCloseTo(
      getFullWidth(labelFn()) + getFullWidth(inputFn()) + getFullWidth(popoverFn())
  ));
  test.start();
});

function getFullWidth(element) {
  const computedStyle = getComputedStyle(element);
  const marginLeft = parseFloat(computedStyle.marginLeft);
  const width = parseFloat(computedStyle.width);
  const marginRight = parseFloat(computedStyle.marginRight);
  return marginLeft + width + marginRight;
}
