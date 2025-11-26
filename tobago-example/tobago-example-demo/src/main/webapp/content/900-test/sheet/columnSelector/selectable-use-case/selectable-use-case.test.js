/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("AJAX, selectable=single", function (done) {
  const selectAjax = elementByIdFn("page:mainForm:changeAjaxOrFsr::0");
  const selectSingle = elementByIdFn("page:mainForm:changeSelectable::0");
  const reset = elementByIdFn("page:mainForm:reset");
  const checkAjax = elementByIdFn("page:mainForm:checkAjax::field");
  const checkSelectable = elementByIdFn("page:mainForm:checkSelectable::field");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:solar::columnSelector']");
  const mercuryCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_1']");
  const mercuryRow = elementByIdFn("page:mainForm:solar:1:rowAjax");
  const venusCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_2']");
  const venusRow = elementByIdFn("page:mainForm:solar:2:rowAjax");
  const name = elementByIdFn("page:mainForm:name::field");

  const test = new JasmineTestTool(done);
  test.setup(() => checkAjax().checked, () => selectAjax().checked = true, "change", selectAjax);
  test.setup(() => checkSelectable().value === "single", () => selectSingle().checked = true, "change", selectSingle);
  test.setup(() => !isSelected(1) && !isSelected(2) && name().value === "Sun", null, "click", reset);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("hidden"));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", mercuryCheckbox, () => isSelected(1));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusRow, () => name().value === "Venus");
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Venus"));
  test.start();
});

it("AJAX, selectable=singleOrNone", function (done) {
  const selectAjax = elementByIdFn("page:mainForm:changeAjaxOrFsr::0");
  const selectSingleOrNone = elementByIdFn("page:mainForm:changeSelectable::1");
  const reset = elementByIdFn("page:mainForm:reset");
  const checkAjax = elementByIdFn("page:mainForm:checkAjax::field");
  const checkSelectable = elementByIdFn("page:mainForm:checkSelectable::field");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:solar::columnSelector']");
  const mercuryCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_1']");
  const mercuryRow = elementByIdFn("page:mainForm:solar:1:rowAjax");
  const venusCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_2']");
  const venusRow = elementByIdFn("page:mainForm:solar:2:rowAjax");
  const name = elementByIdFn("page:mainForm:name::field");

  const test = new JasmineTestTool(done);
  test.setup(() => checkAjax().checked, () => selectAjax().checked = true, "change", selectAjax);
  test.setup(() => checkSelectable().value === "singleOrNone", () => selectSingleOrNone().checked = true, "change", selectSingleOrNone);
  test.setup(() => !isSelected(1) && !isSelected(2) && name().value === "Sun", null, "click", reset);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("hidden"));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", mercuryCheckbox, () => isSelected(1));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => !isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusRow, () => name().value === "Venus");
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Venus"));
  test.start();
});

it("AJAX, selectable=multi", function (done) {
  const selectAjax = elementByIdFn("page:mainForm:changeAjaxOrFsr::0");
  const selectMulti = elementByIdFn("page:mainForm:changeSelectable::2");
  const reset = elementByIdFn("page:mainForm:reset");
  const checkAjax = elementByIdFn("page:mainForm:checkAjax::field");
  const checkSelectable = elementByIdFn("page:mainForm:checkSelectable::field");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:solar::columnSelector']");
  const mercuryCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_1']");
  const mercuryRow = elementByIdFn("page:mainForm:solar:1:rowAjax");
  const venusCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_2']");
  const venusRow = elementByIdFn("page:mainForm:solar:2:rowAjax");
  const name = elementByIdFn("page:mainForm:name::field");

  const test = new JasmineTestTool(done);
  test.setup(() => checkAjax().checked, () => selectAjax().checked = true, "change", selectAjax);
  test.setup(() => checkSelectable().value === "multi", () => selectMulti().checked = true, "change", selectMulti);
  test.setup(() => !isSelected(1) && !isSelected(2) && name().value === "Sun", null, "click", reset);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", mercuryCheckbox, () => isSelected(1));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => !isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusRow, () => name().value === "Venus");
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Venus"));
  test.start();
});

it("Full server request, selectable=single", function (done) {
  const selectFsr = elementByIdFn("page:mainForm:changeAjaxOrFsr::1");
  const selectSingle = elementByIdFn("page:mainForm:changeSelectable::0");
  const reset = elementByIdFn("page:mainForm:reset");
  const checkAjax = elementByIdFn("page:mainForm:checkAjax::field");
  const checkSelectable = elementByIdFn("page:mainForm:checkSelectable::field");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:solar::columnSelector']");
  const mercuryCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_1']");
  const mercuryRow = elementByIdFn("page:mainForm:solar:1:rowAjax");
  const venusCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_2']");
  const venusRow = elementByIdFn("page:mainForm:solar:2:rowAjax");
  const name = elementByIdFn("page:mainForm:name::field");

  const test = new JasmineTestTool(done);
  test.setup(() => checkAjax().checked, () => selectFsr().checked = true, "change", selectFsr);
  test.setup(() => checkSelectable().value === "single", () => selectSingle().checked = true, "change", selectSingle);
  test.setup(() => !isSelected(1) && !isSelected(2) && name().value === "Sun", null, "click", reset);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("hidden"));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", mercuryCheckbox, () => isSelected(1));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusRow, () => name().value === "Venus");
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Venus"));
  test.start();
});

it("Full server request, selectable=singleOrNone", function (done) {
  const selectFsr = elementByIdFn("page:mainForm:changeAjaxOrFsr::1");
  const selectSingleOrNone = elementByIdFn("page:mainForm:changeSelectable::1");
  const reset = elementByIdFn("page:mainForm:reset");
  const checkAjax = elementByIdFn("page:mainForm:checkAjax::field");
  const checkSelectable = elementByIdFn("page:mainForm:checkSelectable::field");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:solar::columnSelector']");
  const mercuryCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_1']");
  const mercuryRow = elementByIdFn("page:mainForm:solar:1:rowAjax");
  const venusCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_2']");
  const venusRow = elementByIdFn("page:mainForm:solar:2:rowAjax");
  const name = elementByIdFn("page:mainForm:name::field");

  const test = new JasmineTestTool(done);
  test.setup(() => checkAjax().checked, () => selectFsr().checked = true, "change", selectFsr);
  test.setup(() => checkSelectable().value === "singleOrNone", () => selectSingleOrNone().checked = true, "change", selectSingleOrNone);
  test.setup(() => !isSelected(1) && !isSelected(2) && name().value === "Sun", null, "click", reset);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("hidden"));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", mercuryCheckbox, () => isSelected(1));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => !isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusRow, () => name().value === "Venus");
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Venus"));
  test.start();
});

it("Full server request, selectable=multi", function (done) {
  const selectFsr = elementByIdFn("page:mainForm:changeAjaxOrFsr::1");
  const selectMulti = elementByIdFn("page:mainForm:changeSelectable::2");
  const reset = elementByIdFn("page:mainForm:reset");
  const checkAjax = elementByIdFn("page:mainForm:checkAjax::field");
  const checkSelectable = elementByIdFn("page:mainForm:checkSelectable::field");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:solar::columnSelector']");
  const mercuryCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_1']");
  const mercuryRow = elementByIdFn("page:mainForm:solar:1:rowAjax");
  const venusCheckbox = querySelectorFn("input[name='page:mainForm:solar_data_row_selector_2']");
  const venusRow = elementByIdFn("page:mainForm:solar:2:rowAjax");
  const name = elementByIdFn("page:mainForm:name::field");

  const test = new JasmineTestTool(done);
  test.setup(() => checkAjax().checked, () => selectFsr().checked = true, "change", selectFsr);
  test.setup(() => checkSelectable().value === "multi", () => selectMulti().checked = true, "change", selectMulti);
  test.setup(() => !isSelected(1) && !isSelected(2) && name().value === "Sun", null, "click", reset);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));
  test.do(() => expect(mercuryCheckbox().checked).toBe(false));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(false));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", mercuryCheckbox, () => isSelected(1));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusCheckbox, () => !isSelected(2));
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Sun"));

  test.event("click", venusRow, () => name().value === "Venus");
  test.do(() => expect(mercuryCheckbox().checked).toBe(true));
  test.do(() => expect(venusCheckbox().checked).toBe(false));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(mercuryRow().classList).toContain("tobago-selected"));
  test.do(() => expect(venusRow().classList).not.toContain("tobago-selected"));
  test.do(() => expect(name().value).toBe("Venus"));
  test.start();
});

function isSelected(number) {
  const hiddenSelectedField = elementByIdFn("page:mainForm:solar::selected");
  const selectedSet = new Set(JSON.parse(hiddenSelectedField().value));
  return selectedSet.has(number);
}
