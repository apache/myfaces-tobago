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

it("Standard: remove 'Earth'", function (done) {
  const removeEarthButtonFn = querySelectorFn("#page\\:mainForm\\:basic\\:selectedStandard\\:\\:field .btn-group[data-tobago-value='Earth'] .tobago-button");
  const badgeVenusFn = querySelectorFn("#page\\:mainForm\\:basic\\:selectedStandard\\:\\:field .btn-group[data-tobago-value='Venus']");
  const badgeEarthFn = querySelectorFn("#page\\:mainForm\\:basic\\:selectedStandard\\:\\:field .btn-group[data-tobago-value='Earth']");
  const badgeJupiterFn = querySelectorFn("#page\\:mainForm\\:basic\\:selectedStandard\\:\\:field .btn-group[data-tobago-value='Jupiter']");
  const selectedRowsFn = querySelectorAllFn("#page\\:mainForm\\:basic\\:selectedStandard tr.table-primary");
  const disabledBadgeVenusFn = querySelectorFn("#page\\:mainForm\\:basic\\:selectedDisabled\\:\\:field .btn-group[data-tobago-value='Venus']");
  const disabledBadgeEarthFn = querySelectorFn("#page\\:mainForm\\:basic\\:selectedDisabled\\:\\:field .btn-group[data-tobago-value='Earth']");
  const disabledBadgeJupiterFn = querySelectorFn("#page\\:mainForm\\:basic\\:selectedDisabled\\:\\:field .btn-group[data-tobago-value='Jupiter']");
  const submitFn = elementByIdFn("page:mainForm:basic:submit");
  const resetFn = elementByIdFn("page:mainForm:basic:reset");

  const test = new JasmineTestTool(done);
  test.setup(() => selectedRowsFn().length === 3
          && selectedRowsFn()[0].getAttribute("data-tobago-value") === "Venus"
          && selectedRowsFn()[1].getAttribute("data-tobago-value") === "Earth"
          && selectedRowsFn()[2].getAttribute("data-tobago-value") === "Jupiter",
      null, "click", resetFn);

  test.do(() => expect(badgeVenusFn()).not.toBeNull());
  test.do(() => expect(badgeEarthFn()).not.toBeNull());
  test.do(() => expect(badgeJupiterFn()).not.toBeNull());
  test.do(() => expect(selectedRowsFn()[0].getAttribute("data-tobago-value")).toEqual("Venus"));
  test.do(() => expect(selectedRowsFn()[1].getAttribute("data-tobago-value")).toEqual("Earth"));
  test.do(() => expect(selectedRowsFn()[2].getAttribute("data-tobago-value")).toEqual("Jupiter"));
  test.do(() => expect(disabledBadgeVenusFn()).not.toBeNull());
  test.do(() => expect(disabledBadgeEarthFn()).not.toBeNull());
  test.do(() => expect(disabledBadgeJupiterFn()).not.toBeNull());

  test.event("click", removeEarthButtonFn, () => selectedRowsFn().length === 2);
  test.do(() => expect(badgeVenusFn()).not.toBeNull());
  test.do(() => expect(badgeEarthFn()).toBeNull());
  test.do(() => expect(badgeJupiterFn()).not.toBeNull());
  test.do(() => expect(selectedRowsFn()[0].getAttribute("data-tobago-value")).toEqual("Venus"));
  test.do(() => expect(selectedRowsFn()[1].getAttribute("data-tobago-value")).toEqual("Jupiter"));
  test.do(() => expect(disabledBadgeVenusFn()).not.toBeNull());
  test.do(() => expect(disabledBadgeEarthFn()).not.toBeNull());
  test.do(() => expect(disabledBadgeJupiterFn()).not.toBeNull());

  test.event("click", submitFn, () => disabledBadgeEarthFn() === null);
  test.do(() => expect(badgeVenusFn()).not.toBeNull());
  test.do(() => expect(badgeEarthFn()).toBeNull());
  test.do(() => expect(badgeJupiterFn()).not.toBeNull());
  test.do(() => expect(selectedRowsFn()[0].getAttribute("data-tobago-value")).toEqual("Venus"));
  test.do(() => expect(selectedRowsFn()[1].getAttribute("data-tobago-value")).toEqual("Jupiter"));
  test.do(() => expect(disabledBadgeVenusFn()).not.toBeNull());
  test.do(() => expect(disabledBadgeEarthFn()).toBeNull());
  test.do(() => expect(disabledBadgeJupiterFn()).not.toBeNull());

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
