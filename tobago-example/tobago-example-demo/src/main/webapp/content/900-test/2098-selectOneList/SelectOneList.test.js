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

it("Standard: select 'Venus', select 'Earth'", function (done) {
  const selectFieldFn = elementByIdFn("page:mainForm:selectedStandard::selectField");
  const optionsFn = querySelectorAllFn(".tobago-options[name='page:mainForm:selectedStandard']");
  const mercuryRowFn = querySelectorFn(".tobago-options[name='page:mainForm:selectedStandard'] tr[data-tobago-value='Mercury']");
  const venusRowFn = querySelectorFn(".tobago-options[name='page:mainForm:selectedStandard'] tr[data-tobago-value='Venus']");
  const earthRowFn = querySelectorFn(".tobago-options[name='page:mainForm:selectedStandard'] tr[data-tobago-value='Earth']");
  const outputFn = querySelectorFn("#page\\:mainForm\\:output .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "Mercury", null, "click", mercuryRowFn);

  test.do(() => expect(optionsFn().length).toEqual(1));
  test.do(() => expect(optionsFn()[0].classList).not.toContain("show"));

  test.event("click", selectFieldFn, () => optionsFn()[0].classList.contains("show"));
  test.do(() => expect(optionsFn().length).toEqual(1));
  test.do(() => expect(optionsFn()[0].classList).toContain("show"));

  test.event("click", venusRowFn, () => outputFn().textContent === "Venus");
  test.do(() => expect(optionsFn().length).toEqual(1));
  test.do(() => expect(optionsFn()[0].classList).not.toContain("show"));
  test.do(() => expect(outputFn().textContent).toEqual("Venus"));

  test.event("click", selectFieldFn, () => optionsFn()[0].classList.contains("show"));
  test.do(() => expect(optionsFn().length).toEqual(1));
  test.do(() => expect(optionsFn()[0].classList).toContain("show"));

  test.event("click", earthRowFn, () => outputFn().textContent === "Earth");
  test.do(() => expect(optionsFn().length).toEqual(1));
  test.do(() => expect(optionsFn()[0].classList).not.toContain("show"));
  test.do(() => expect(outputFn().textContent).toEqual("Earth"));

  test.start();
});
