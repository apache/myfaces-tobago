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

it("Standard: select 'Mars', select 'Saturn'", function (done) {
  const selectedFieldFn = querySelectorFn(".tobago-select-field[name='page:mainForm:basic:selectedStandard'] span");
  const marsRowFn = querySelectorFn(".tobago-options[name='page:mainForm:basic:selectedStandard'] tr[data-tobago-value='Mars']");
  const saturnRowFn = querySelectorFn(".tobago-options[name='page:mainForm:basic:selectedStandard'] tr[data-tobago-value='Saturn']");
  const neptuneRowFn = querySelectorFn(".tobago-options[name='page:mainForm:basic:selectedStandard'] tr[data-tobago-value='Neptune']");
  const disabledSelectedFieldFn = querySelectorFn(".tobago-select-field[name='page:mainForm:basic:selectedDisabled'] span");
  const submitFn = elementByIdFn("page:mainForm:basic:submit");

  const test = new JasmineTestTool(done);
  test.setup(() => selectedFieldFn().textContent === "Neptune", neptuneRowFn().click(), "click", submitFn);

  test.do(() => expect(selectedFieldFn().textContent).toEqual("Neptune"));
  test.do(() => expect(disabledSelectedFieldFn().textContent).toEqual("Neptune"));

  test.do(() => marsRowFn().click());
  test.event("click", submitFn, () => disabledSelectedFieldFn().textContent === "Mars");
  test.do(() => expect(selectedFieldFn().textContent).toEqual("Mars"));
  test.do(() => expect(disabledSelectedFieldFn().textContent).toEqual("Mars"));

  test.do(() => saturnRowFn().click());
  test.event("click", submitFn, () => disabledSelectedFieldFn().textContent === "Saturn");
  test.do(() => expect(selectedFieldFn().textContent).toEqual("Saturn"));
  test.do(() => expect(disabledSelectedFieldFn().textContent).toEqual("Saturn"));

  test.start();
});

it("Filter (contains)", function (done) {
  const searchFn = elementByIdFn("page:mainForm:selectedFilter::filter");
  const hiddenRowsFn = querySelectorAllFn("#page\\:mainForm\\:selectedFilter tr.d-none:not(.tobago-no-entries)");
  const mercuryRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr[data-tobago-value='Mercury']");
  const venusRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr[data-tobago-value='Venus']");
  const earthRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr[data-tobago-value='Earth']");
  const marsRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr[data-tobago-value='Mars']");
  const jupiterRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr[data-tobago-value='Jupiter']");
  const saturnRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr[data-tobago-value='Saturn']");
  const uranusRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr[data-tobago-value='Uranus']");
  const neptuneRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr[data-tobago-value='Neptune']");
  const plutoRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr[data-tobago-value='Pluto']");
  const noEntriesRowFn = querySelectorFn("#page\\:mainForm\\:selectedFilter tr.tobago-no-entries");

  const test = new JasmineTestTool(done);
  test.setup(() => hiddenRowsFn().length === 0, () => searchFn().value = "", "input", searchFn);

  test.do(() => expect(mercuryRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(venusRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(earthRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(marsRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(jupiterRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(saturnRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(uranusRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(neptuneRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(plutoRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(noEntriesRowFn().classList.contains("d-none")).toBeTrue());

  test.do(() => searchFn().value = "M");
  test.event("input", searchFn, () => venusRowFn().classList.contains("d-none"));

  test.do(() => expect(mercuryRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(venusRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(earthRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(marsRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(jupiterRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(saturnRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(uranusRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(neptuneRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(plutoRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(noEntriesRowFn().classList.contains("d-none")).toBeTrue());

  test.do(() => searchFn().value = "Ma");
  test.event("input", searchFn, () => mercuryRowFn().classList.contains("d-none"));

  test.do(() => expect(mercuryRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(venusRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(earthRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(marsRowFn().classList.contains("d-none")).toBeFalse());
  test.do(() => expect(jupiterRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(saturnRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(uranusRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(neptuneRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(plutoRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(noEntriesRowFn().classList.contains("d-none")).toBeTrue());

  test.do(() => searchFn().value = "Max");
  test.event("input", searchFn, () => marsRowFn().classList.contains("d-none"));

  test.do(() => expect(mercuryRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(venusRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(earthRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(marsRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(jupiterRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(saturnRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(uranusRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(neptuneRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(plutoRowFn().classList.contains("d-none")).toBeTrue());
  test.do(() => expect(noEntriesRowFn().classList.contains("d-none")).toBeFalse());

  test.start();
});
