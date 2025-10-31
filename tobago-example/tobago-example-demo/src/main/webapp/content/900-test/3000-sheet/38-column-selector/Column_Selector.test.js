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

it("Default sheet: select row 2; select row4", function (done) {
  const rows = elementByIdFn("page:mainForm:rows::field");
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupDefaultSheet = elementByIdFn("page:mainForm:defaultSheet");
  const row2 = querySelectorFn("tr[row-index='2']");
  const row4 = querySelectorFn("tr[row-index='4']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  test.setup(() => rows().value === "5" && !lazy().checked && sheetSelectable().value === "multi"
          && !columnSelectorRendered().checked
          && !columnPanelRendered().checked
          && !isSelected(2)
          && !isSelected(4),
      null, "click", setupDefaultSheet);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(isSelected(4)).toBe(false));
  test.do(() => expect(row2().classList).not.toContain("tobago-selected"));
  test.do(() => expect(row4().classList).not.toContain("tobago-selected"));

  test.event("click", row2, () => isSelected(2));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(isSelected(4)).toBe(false));
  test.do(() => expect(row2().classList).toContain("tobago-selected"));
  test.do(() => expect(row4().classList).not.toContain("tobago-selected"));

  test.event("click", row4, () => isSelected(4));
  test.do(() => expect(isSelected(2)).toBe(false));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(row2().classList).not.toContain("tobago-selected"));
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.start();
});

it("selectable=none + no column selector: select row 3 cannot happen", function (done) {
  const rows = elementByIdFn("page:mainForm:rows::field");
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupSelectableNone = elementByIdFn("page:mainForm:selectableNone");
  const row3 = querySelectorFn("tr[row-index='3']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  test.setup(() => rows().value === "5" && !lazy().checked && sheetSelectable().value === "none"
          && !columnSelectorRendered().checked
          && !columnPanelRendered().checked,
      null, "click", setupSelectableNone);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => row3().dispatchEvent(new Event("click", {bubbles: true})));
  test.waitMs(1000);
  test.do(() => expect(isSelected(3)).toBe(false));
  test.do(() => expect(row3().classList).not.toContain("tobago-selected"));

  test.start();
});

it("selectable=single: page 1; select row 3; page 4; select row 18; page 1", function (done) {
  const rows = elementByIdFn("page:mainForm:rows::field");
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupSelectableSingle = elementByIdFn("page:mainForm:selectableSingle");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const row3 = querySelectorFn("tr[row-index='3']");
  const row18 = querySelectorFn("tr[row-index='18']");
  const page4 = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='\\:4\\}']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  test.setup(() => rows().value === "5" && !lazy().checked && sheetSelectable().value === "single"
          && columnSelectorRendered().checked
          && !columnPanelRendered().checked
          && !isSelected(3)
          && !isSelected(18),
      null, "click", setupSelectableSingle);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("hidden"));
  test.event("click", row3, () => row3().classList.contains("tobago-selected"));
  test.do(() => expect(row3().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(3)).toBe(true));
  test.do(() => expect(isSelected(18)).toBe(false));

  test.event("click", page4, () => row18() !== null);
  test.do(() => expect(row18()).not.toBeNull());

  test.event("click", row18, () => row18().classList.contains("tobago-selected"));
  test.do(() => expect(row18().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(3)).toBe(false));
  test.do(() => expect(isSelected(18)).toBe(true));

  test.event("click", firstPageButton, () => row3() !== null);
  test.do(() => expect(row3()).not.toBeNull());
  test.do(() => expect(row3().classList).not.toContain("tobago-selected"));
  test.do(() => expect(isSelected(3)).toBe(false));
  test.do(() => expect(isSelected(18)).toBe(true));
  test.start();
});

it("selectable=singleOrNone: page 1; select row 3; deselect row 3", function (done) {
  const rows = elementByIdFn("page:mainForm:rows::field");
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupSelectableSingleOrNone = elementByIdFn("page:mainForm:selectableSingleOrNone");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const row3 = querySelectorFn("tr[row-index='3']");
  const row18 = querySelectorFn("tr[row-index='18']");
  const page4 = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='\\:4\\}']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  test.setup(() => rows().value === "5" && !lazy().checked && sheetSelectable().value === "singleOrNone"
          && columnSelectorRendered().checked
          && !columnPanelRendered().checked
          && !isSelected(3),
      null, "click", setupSelectableSingleOrNone);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("hidden"));
  test.event("click", row3, () => row3().classList.contains("tobago-selected"));
  test.do(() => expect(row3().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(3)).toBe(true));

  test.event("click", row3, () => !row3().classList.contains("tobago-selected"));
  test.do(() => expect(row3().classList).not.toContain("tobago-selected"));
  test.do(() => expect(isSelected(3)).toBe(false));
  test.start();
});

it("selectable=multi: page 1; select row 4; page 4; select row 17; page 1", function (done) {
  const rows = elementByIdFn("page:mainForm:rows::field");
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupSelectableMulti = elementByIdFn("page:mainForm:selectableMulti");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const row4 = querySelectorFn("tr[row-index='4']");
  const row17 = querySelectorFn("tr[row-index='17']");
  const page4 = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='\\:4\\}']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  test.setup(() => rows().value === "5" && !lazy().checked && sheetSelectable().value === "multi"
          && columnSelectorRendered().checked
          && !columnPanelRendered().checked
          && !isSelected(4)
          && !isSelected(17),
      null, "click", setupSelectableMulti);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));
  test.event("click", row4, () => row4().classList.contains("tobago-selected"));
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(false));

  test.event("click", page4, () => row17() !== null);
  test.do(() => expect(row17()).not.toBeNull());

  test.event("click", row17, () => row17().classList.contains("tobago-selected"));
  test.do(() => expect(row17().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(true));

  test.event("click", firstPageButton, () => row4() !== null);
  test.do(() => expect(row4()).not.toBeNull());
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(true));
  test.start();
});

it("selectable=multi: select all; page 4; select row 17; page 1", function (done) {
  const rows = elementByIdFn("page:mainForm:rows::field");
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupSelectableMulti = elementByIdFn("page:mainForm:selectableMulti");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const row0 = querySelectorFn("tr[row-index='0']");
  const row1 = querySelectorFn("tr[row-index='1']");
  const row2 = querySelectorFn("tr[row-index='2']");
  const row3 = querySelectorFn("tr[row-index='3']");
  const row4 = querySelectorFn("tr[row-index='4']");
  const row17 = querySelectorFn("tr[row-index='17']");
  const page4 = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='\\:4\\}']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  test.setup(() => rows().value === "5" && !lazy().checked && sheetSelectable().value === "multi"
          && columnSelectorRendered().checked
          && !columnPanelRendered().checked
          && !isSelected(0)
          && !isSelected(1)
          && !isSelected(2)
          && !isSelected(3)
          && !isSelected(4)
          && !isSelected(17),
      null, "click", setupSelectableMulti);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));

  test.event("click", selectAllCheckbox, () => isSelected(0) && isSelected(1) && isSelected(2) && isSelected(3) && isSelected(4));
  test.do(() => expect(row0().classList).toContain("tobago-selected"));
  test.do(() => expect(row1().classList).toContain("tobago-selected"));
  test.do(() => expect(row2().classList).toContain("tobago-selected"));
  test.do(() => expect(row3().classList).toContain("tobago-selected"));
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(0)).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(isSelected(3)).toBe(true));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(false));

  test.event("click", page4, () => row17() !== null);
  test.do(() => expect(row17()).not.toBeNull());
  test.do(() => expect(row17().classList).not.toContain("tobago-selected"));

  test.event("click", row17, () => row17().classList.contains("tobago-selected"));
  test.do(() => expect(row17().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(0)).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(isSelected(3)).toBe(true));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(true));

  test.event("click", firstPageButton, () => firstPageButton().disabled);
  test.do(() => expect(row0().classList).toContain("tobago-selected"));
  test.do(() => expect(row1().classList).toContain("tobago-selected"));
  test.do(() => expect(row2().classList).toContain("tobago-selected"));
  test.do(() => expect(row3().classList).toContain("tobago-selected"));
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(0)).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(isSelected(3)).toBe(true));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(true));
  test.start();
});

it("selectable=multi: select all; deselect row 3; select row 3", function (done) {
  const rows = elementByIdFn("page:mainForm:rows::field");
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupSelectableMulti = elementByIdFn("page:mainForm:selectableMulti");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const row0 = querySelectorFn("tr[row-index='0']");
  const row1 = querySelectorFn("tr[row-index='1']");
  const row2 = querySelectorFn("tr[row-index='2']");
  const row3 = querySelectorFn("tr[row-index='3']");
  const row4 = querySelectorFn("tr[row-index='4']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  test.setup(() => rows().value === "5" && !lazy().checked && sheetSelectable().value === "multi"
          && columnSelectorRendered().checked
          && !columnPanelRendered().checked
          && !isSelected(0)
          && !isSelected(1)
          && !isSelected(2)
          && !isSelected(3)
          && !isSelected(4),
      null, "click", setupSelectableMulti);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));
  test.do(() => expect(selectAllCheckbox().checked).toBe(false));

  test.event("click", selectAllCheckbox, () => isSelected(0) && isSelected(1) && isSelected(2) && isSelected(3) && isSelected(4));
  test.do(() => expect(selectAllCheckbox().checked).toBe(true));
  test.do(() => expect(row0().classList).toContain("tobago-selected"));
  test.do(() => expect(row1().classList).toContain("tobago-selected"));
  test.do(() => expect(row2().classList).toContain("tobago-selected"));
  test.do(() => expect(row3().classList).toContain("tobago-selected"));
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(0)).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(isSelected(3)).toBe(true));
  test.do(() => expect(isSelected(4)).toBe(true));

  test.event("click", row3, () => !isSelected(3));
  test.do(() => expect(selectAllCheckbox().checked).toBe(false));
  test.do(() => expect(row0().classList).toContain("tobago-selected"));
  test.do(() => expect(row1().classList).toContain("tobago-selected"));
  test.do(() => expect(row2().classList).toContain("tobago-selected"));
  test.do(() => expect(row3().classList).not.toContain("tobago-selected"));
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(0)).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(isSelected(3)).toBe(false));
  test.do(() => expect(isSelected(4)).toBe(true));

  test.event("click", row3, () => isSelected(3));
  test.do(() => expect(selectAllCheckbox().checked).toBe(true));
  test.do(() => expect(row0().classList).toContain("tobago-selected"));
  test.do(() => expect(row1().classList).toContain("tobago-selected"));
  test.do(() => expect(row2().classList).toContain("tobago-selected"));
  test.do(() => expect(row3().classList).toContain("tobago-selected"));
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(0)).toBe(true));
  test.do(() => expect(isSelected(1)).toBe(true));
  test.do(() => expect(isSelected(2)).toBe(true));
  test.do(() => expect(isSelected(3)).toBe(true));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.start();
});

it("disabled column selector", function (done) {
  const rows = elementByIdFn("page:mainForm:rows::field");
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorDisabled = elementByIdFn("page:mainForm:columnSelectorDisabled::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupDisabledColumnSelector = elementByIdFn("page:mainForm:disabledColumnSelector");
  const hiddenSelectedField = elementByIdFn("page:mainForm:sheet::selected");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const rowSelector = querySelectorFn("input[name='page:mainForm:sheet_data_row_selector_0']");
  const row2 = querySelectorFn("tr[row-index='2']");
  const row4 = querySelectorFn("tr[row-index='4']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  test.setup(() => rows().value === "5" && !lazy().checked && sheetSelectable().value === "multi"
          && columnSelectorDisabled().checked
          && columnSelectorRendered().checked
          && !columnPanelRendered().checked
          && hiddenSelectedField().value === "[2]",
      null, "click", setupDisabledColumnSelector);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));
  test.do(() => expect(selectAllCheckbox().disabled).toBe(true));
  test.do(() => expect(rowSelector().disabled).toBe(true));
  test.do(() => expect(row2().classList).toContain("tobago-selected"));
  test.do(() => expect(row4().classList).not.toContain("tobago-selected"));

  test.do(() => row4().dispatchEvent(new Event("click", {bubbles: true})));
  test.waitMs(1000);
  test.do(() => expect(hiddenSelectedField().value).toBe("[2]"));
  test.do(() => expect(row2().classList).toContain("tobago-selected"));
  test.do(() => expect(row4().classList).not.toContain("tobago-selected"));
  test.start();
});

it("column panel: page 1; select row 4; page 4; select row 17; page 1", function (done) {
  const rows = elementByIdFn("page:mainForm:rows::field");
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupColumnPanel = elementByIdFn("page:mainForm:columnPanel");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const row4 = querySelectorFn("tr[row-index='4']");
  const row17 = querySelectorFn("tr[row-index='17']");
  const columnPanel4 = querySelectorFn(".tobago-column-panel[name='4']");
  const columnPanel17 = querySelectorFn(".tobago-column-panel[name='17']");
  const page4 = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='\\:4\\}']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  test.setup(() => rows().value === "5" && !lazy().checked && sheetSelectable().value === "multi"
          && columnSelectorRendered().checked
          && columnPanelRendered().checked
          && !isSelected(4)
          && !isSelected(17),
      null, "click", setupColumnPanel);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));

  test.event("click", row4, () => row4().classList.contains("tobago-selected"));
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.do(() => expect(columnPanel4().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(false));

  test.event("click", page4, () => row17() !== null);
  test.do(() => expect(row17()).not.toBeNull());

  test.event("click", columnPanel17, () => row17().classList.contains("tobago-selected"));
  test.do(() => expect(row17().classList).toContain("tobago-selected"));
  test.do(() => expect(columnPanel17().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(true));

  test.event("click", firstPageButton, () => row4() !== null);
  test.do(() => expect(row4()).not.toBeNull());
  test.do(() => expect(row4().classList).toContain("tobago-selected"));
  test.do(() => expect(columnPanel4().classList).toContain("tobago-selected"));
  test.do(() => expect(isSelected(4)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(true));
  test.start();
});

it("lazy: select all; scroll down; deselect row 17; deselect row 3", function (done) {
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const setupLazy = elementByIdFn("page:mainForm:setupLazy");
  const hiddenSelectedField = elementByIdFn("page:mainForm:sheet::selected");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const row3 = querySelectorFn("tr[row-index='3']");
  const row17 = querySelectorFn("tr[row-index='17']");

  const test = new JasmineTestTool(done);
  test.setup(() => lazy().checked && sheetSelectable().value === "multi"
          && columnSelectorRendered().checked
          && !columnPanelRendered().checked
          && hiddenSelectedField().value === "[]",
      null, "click", setupLazy);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));

  test.event("click", selectAllCheckbox, () => hiddenSelectedField().value === "[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87]");
  test.do(() => expect(hiddenSelectedField().value).toBe("[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87]"));
  test.do(() => expect(row3().classList).toContain("tobago-selected"));
  test.do(() => expect(row17().classList).toContain("tobago-selected"));

  test.do(() => focusRowIndex(10));
  test.wait(() => isLoaded(10));

  test.event("click", row17, () => !isSelected(17));
  test.do(() => expect(row3().classList).toContain("tobago-selected"));
  test.do(() => expect(row17().classList).not.toContain("tobago-selected"));
  test.do(() => expect(isSelected(3)).toBe(true));
  test.do(() => expect(isSelected(17)).toBe(false));

  test.event("click", row3, () => !isSelected(3));
  test.do(() => expect(row3().classList).not.toContain("tobago-selected"));
  test.do(() => expect(row17().classList).not.toContain("tobago-selected"));
  test.do(() => expect(isSelected(3)).toBe(false));
  test.do(() => expect(isSelected(17)).toBe(false));
  test.start();
});

it("lazy: scroll down, select row 17 column panel, select row 3 column panel", function (done) {
  const lazy = elementByIdFn("page:mainForm:lazy::field");
  const sheetSelectable = elementByIdFn("page:mainForm:sheetSelectable::field");
  const columnSelectorRendered = elementByIdFn("page:mainForm:columnSelectorRendered::field");
  const columnPanelRendered = elementByIdFn("page:mainForm:columnPanelRendered::field");
  const lazyColumnPanel = elementByIdFn("page:mainForm:lazyColumnPanel");
  const hiddenSelectedField = elementByIdFn("page:mainForm:sheet::selected");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const row3 = querySelectorFn("tr[row-index='3']");
  const row17 = querySelectorFn("tr[row-index='17']");
  const columnPanel3 = querySelectorFn(".tobago-column-panel[name='3']");
  const columnPanel17 = querySelectorFn(".tobago-column-panel[name='17']");

  const test = new JasmineTestTool(done);
  test.setup(() => lazy().checked && sheetSelectable().value === "multi"
          && columnSelectorRendered().checked
          && columnPanelRendered().checked
          && hiddenSelectedField().value === "[]",
      null, "click", lazyColumnPanel);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));

  test.waitMs(1000);
  test.do(() => focusRowIndex(10));
  test.wait(() => isLoaded(10));

  test.do(() => expect(row3().classList).not.toContain("tobago-selected"));
  test.do(() => expect(row17().classList).not.toContain("tobago-selected"));
  test.do(() => expect(columnPanel3().classList).not.toContain("tobago-selected"));
  test.do(() => expect(columnPanel17().classList).not.toContain("tobago-selected"));

  test.event("click", columnPanel17, () => isSelected(17));
  test.do(() => expect(row3().classList).not.toContain("tobago-selected"));
  test.do(() => expect(row17().classList).toContain("tobago-selected"));
  test.do(() => expect(columnPanel3().classList).not.toContain("tobago-selected"));
  test.do(() => expect(columnPanel17().classList).toContain("tobago-selected"));

  test.event("click", columnPanel3, () => isSelected(3));
  test.do(() => expect(row3().classList).toContain("tobago-selected"));
  test.do(() => expect(row17().classList).toContain("tobago-selected"));
  test.do(() => expect(columnPanel3().classList).toContain("tobago-selected"));
  test.do(() => expect(columnPanel17().classList).toContain("tobago-selected"));
  test.start();
});

function isSelected(number) {
  const hiddenSelectedField = elementByIdFn("page:mainForm:sheet::selected");
  const selectedSet = new Set(JSON.parse(hiddenSelectedField().value));
  return selectedSet.has(number);
}

function row(rowIndex) {
  return querySelectorFn("tr[row-index='" + rowIndex + "']")();
}

function focusRowIndex(rowIndex) {
  querySelectorFn("#page\\:mainForm\\:sheet .tobago-body")().scrollTop = row(rowIndex).offsetTop;
}

function isLoaded(startRow, endRow) {
  if (endRow === undefined) {
    endRow = startRow;
  }
  for (let i = startRow; i <= endRow; i++) {
    if (row(i).getAttribute("dummy") != null) {
      return false;
    }
  }
  return true;
}
