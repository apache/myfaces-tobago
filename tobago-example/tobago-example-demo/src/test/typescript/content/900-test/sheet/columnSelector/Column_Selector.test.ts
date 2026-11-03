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

import {expect, type Page, test} from "@playwright/test";

test.describe("900-test/sheet/columnSelector/Column_Selector.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/columnSelector/Column_Selector.xhtml");
  });

  test("Default sheet: select row 2; select row4", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupDefaultSheet = page.locator("[id='page:mainForm:defaultSheet']");
    const row2 = page.locator("tr[row-index='2']");
    const row4 = page.locator("tr[row-index='4']");
    const firstPageButton = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("multi");
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();

    await setupDefaultSheet.click();
    await expect(firstPageButton).toBeDisabled();

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("multi");
    await expect(columnSelectorRendered).not.toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();

    await expect(row2).not.toHaveClass(/tobago-selected/);
    await expect(row4).not.toHaveClass(/tobago-selected/);

    await row2.click();
    await expect(row2).toHaveClass(/tobago-selected/);
    await expect(row4).not.toHaveClass(/tobago-selected/);

    await row4.click();
    await expect(row2).not.toHaveClass(/tobago-selected/);
    await expect(row4).toHaveClass(/tobago-selected/);
  });

  test("selectable=none + no column selector: select row 3 cannot happen", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupSelectableNone = page.locator("[id='page:mainForm:selectableNone']");
    const row3 = page.locator("tr[row-index='3']");

    await expectDefaultSheet(page);

    await setupSelectableNone.click();

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("none");
    await expect(columnSelectorRendered).not.toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();

    await row3.click();
    await expect(row3).not.toHaveClass(/tobago-selected/);
  });

  test("selectable=single: page 1; select row 3; page 4; select row 18; page 1", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupSelectableSingle = page.locator("[id='page:mainForm:selectableSingle']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const row3 = page.locator("tr[row-index='3']");
    const row18 = page.locator("tr[row-index='18']");
    const page4 = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='\\:4\\}']");
    const firstPageButton = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

    await expectDefaultSheet(page);
    await setupSelectableSingle.click();

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("single");
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();

    await expect(selectAllCheckbox).toHaveAttribute("type", "hidden");
    await expect(firstPageButton).toBeDisabled();

    await row3.click();
    await expect(row3).toContainClass("tobago-selected");

    expect(await isSelected(page, 3)).toBe(true);
    expect(await isSelected(page, 18)).toBe(false);

    await page4.click();
    await expect(row18).toBeVisible();
    await expect(row18).not.toContainClass("tobago-selected");
    await row18.click();
    await expect(row18).toContainClass("tobago-selected");
    expect(await isSelected(page, 3)).toBe(false);
    expect(await isSelected(page, 18)).toBe(true);

    await firstPageButton.click();
    await expect(row3).toBeVisible();
    await expect(row3).not.toContainClass("tobago-selected");
    expect(await isSelected(page, 3)).toBe(false);
    expect(await isSelected(page, 18)).toBe(true);
  });

  test("selectable=singleOrNone: page 1; select row 3; deselect row 3", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupSelectableSingleOrNone = page.locator("[id='page:mainForm:selectableSingleOrNone']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const row3 = page.locator("tr[row-index='3']");
    const firstPageButton = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

    await expectDefaultSheet(page);
    await setupSelectableSingleOrNone.click();

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("singleOrNone");
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();

    expect(await isSelected(page, 3)).toBe(false);
    await expect(firstPageButton).toBeDisabled();
    await expect(selectAllCheckbox).toHaveAttribute("type", "hidden");

    await row3.click();
    await expect(row3).toContainClass("tobago-selected");
    expect(await isSelected(page, 3)).toBe(true);

    await row3.click();
    await expect(row3).not.toContainClass("tobago-selected");
    expect(await isSelected(page, 3)).toBe(false);
  });

  test("selectable=multi: page 1; select row 4; page 4; select row 17; page 1", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupSelectableMulti = page.locator("[id='page:mainForm:selectableMulti']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const row4 = page.locator("tr[row-index='4']");
    const row17 = page.locator("tr[row-index='17']");
    const page4 = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='\\:4\\}']");
    const firstPageButton = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

    await expectDefaultSheet(page);
    await setupSelectableMulti.click();

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("multi");
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();
    expect(await isSelected(page, 4)).toBe(false);
    expect(await isSelected(page, 17)).toBe(false);

    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");
    await row4.click();
    await expect(row4).toContainClass("tobago-selected");
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(false);

    await page4.click();
    await expect(row17).toBeVisible();
    await expect(row17).not.toContainClass("tobago-selected");
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(false);

    await row17.click();
    await expect(row17).toContainClass("tobago-selected");
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(true);

    await firstPageButton.click();
    await expect(row4).toBeVisible();
    await expect(row4).toContainClass("tobago-selected");
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(true);
  });

  test("selectable=multi: select all; page 4; select row 17; page 1", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupSelectableMulti = page.locator("[id='page:mainForm:selectableMulti']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const row0 = page.locator("tr[row-index='0']");
    const row1 = page.locator("tr[row-index='1']");
    const row2 = page.locator("tr[row-index='2']");
    const row3 = page.locator("tr[row-index='3']");
    const row4 = page.locator("tr[row-index='4']");
    const row17 = page.locator("tr[row-index='17']");
    const page4 = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='\\:4\\}']");
    const firstPageButton = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

    await expectDefaultSheet(page);
    await setupSelectableMulti.click();

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("multi");
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();
    expect(await isSelected(page, 0)).toBe(false);
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(false);
    expect(await isSelected(page, 3)).toBe(false);
    expect(await isSelected(page, 4)).toBe(false);
    expect(await isSelected(page, 17)).toBe(false);

    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");

    await selectAllCheckbox.check();
    await expect(row0).toContainClass("tobago-selected");
    await expect(row1).toContainClass("tobago-selected");
    await expect(row2).toContainClass("tobago-selected");
    await expect(row3).toContainClass("tobago-selected");
    await expect(row4).toContainClass("tobago-selected");
    expect(await isSelected(page, 0)).toBe(true);
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(true);
    expect(await isSelected(page, 3)).toBe(true);
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(false);

    await page4.click();
    await expect(row17).toBeVisible();
    await expect(row17).not.toContainClass("tobago-selected");
    expect(await isSelected(page, 0)).toBe(true);
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(true);
    expect(await isSelected(page, 3)).toBe(true);
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(false);

    await row17.click();
    await expect(row17).toContainClass("tobago-selected");
    expect(await isSelected(page, 0)).toBe(true);
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(true);
    expect(await isSelected(page, 3)).toBe(true);
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(true);

    await firstPageButton.click();
    await expect(row0).toBeVisible();
    await expect(row0).toContainClass("tobago-selected");
    await expect(row1).toContainClass("tobago-selected");
    await expect(row2).toContainClass("tobago-selected");
    await expect(row3).toContainClass("tobago-selected");
    await expect(row4).toContainClass("tobago-selected");
    expect(await isSelected(page, 0)).toBe(true);
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(true);
    expect(await isSelected(page, 3)).toBe(true);
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(true);
  });

  test("selectable=multi: select all; deselect row 3; select row 3", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupSelectableMulti = page.locator("[id='page:mainForm:selectableMulti']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const row0 = page.locator("tr[row-index='0']");
    const row1 = page.locator("tr[row-index='1']");
    const row2 = page.locator("tr[row-index='2']");
    const row3 = page.locator("tr[row-index='3']");
    const row4 = page.locator("tr[row-index='4']");

    await expectDefaultSheet(page);
    await setupSelectableMulti.click();

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("multi");
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();
    expect(await isSelected(page, 0)).toBe(false);
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(false);
    expect(await isSelected(page, 3)).toBe(false);
    expect(await isSelected(page, 4)).toBe(false);

    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");

    await selectAllCheckbox.check();
    await expect(row0).toContainClass("tobago-selected");
    await expect(row1).toContainClass("tobago-selected");
    await expect(row2).toContainClass("tobago-selected");
    await expect(row3).toContainClass("tobago-selected");
    await expect(row4).toContainClass("tobago-selected");
    expect(await isSelected(page, 0)).toBe(true);
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(true);
    expect(await isSelected(page, 3)).toBe(true);
    expect(await isSelected(page, 4)).toBe(true);

    await row3.click();
    await expect(row3).not.toContainClass("tobago-selected");
    expect(await isSelected(page, 0)).toBe(true);
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(true);
    expect(await isSelected(page, 3)).toBe(false);
    expect(await isSelected(page, 4)).toBe(true);
    await expect(selectAllCheckbox).not.toBeChecked();

    await row3.click();
    await expect(row3).toContainClass("tobago-selected");
    expect(await isSelected(page, 0)).toBe(true);
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(true);
    expect(await isSelected(page, 3)).toBe(true);
    expect(await isSelected(page, 4)).toBe(true);
    await expect(selectAllCheckbox).toBeChecked();
  });

  test("disabled column selector", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorDisabled = page.locator("[id='page:mainForm:columnSelectorDisabled::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupDisabledColumnSelector = page.locator("[id='page:mainForm:disabledColumnSelector']");
    const hiddenSelectedField = page.locator("[id='page:mainForm:sheet::selected']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const rowSelector = page.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const row2 = page.locator("tr[row-index='2']");
    const row4 = page.locator("tr[row-index='4']");
    const firstPageButton = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

    await expectDefaultSheet(page);
    await setupDisabledColumnSelector.click();

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("multi");
    await expect(columnSelectorDisabled).toBeChecked();
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();
    await expect(hiddenSelectedField).toHaveValue("[2]");
    await expect(firstPageButton).toBeDisabled();

    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");
    await expect(selectAllCheckbox).toBeDisabled();
    await expect(rowSelector).toBeDisabled();
    await expect(row2).toContainClass("tobago-selected");
    await expect(row4).not.toContainClass("tobago-selected");

    await row4.click();
    await expect(hiddenSelectedField).toHaveValue("[2]");
    await expect(row2).toContainClass("tobago-selected");
    await expect(row4).not.toContainClass("tobago-selected");
  });

  test("column panel: page 1; select row 4; page 4; select row 17; page 1", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupColumnPanel = page.locator("[id='page:mainForm:columnPanel']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const row4 = page.locator("tr[row-index='4']");
    const row17 = page.locator("tr[row-index='17']");
    const columnPanel4 = page.locator(".tobago-column-panel[name='4']");
    const columnPanel17 = page.locator(".tobago-column-panel[name='17']");
    const page4 = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='\\:4\\}']");
    const firstPageButton = page.locator("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

    await expectDefaultSheet(page);
    await setupColumnPanel.click();

    await expect(rows).toHaveValue("5");
    await expect(lazy).not.toBeChecked();
    await expect(sheetSelectable).toHaveValue("multi");
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).toBeChecked();

    expect(await isSelected(page, 4)).toBe(false);
    expect(await isSelected(page, 17)).toBe(false);
    await expect(firstPageButton).toBeDisabled();

    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");

    await row4.click();
    await expect(row4).toContainClass("tobago-selected");
    await expect(columnPanel4).toContainClass("tobago-selected");
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(false);

    await page4.click();
    await expect(row17).toBeVisible();
    await expect(row17).not.toContainClass("tobago-selected");
    await expect(columnPanel17).not.toContainClass("tobago-selected");
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(false);

    await row17.click();
    await expect(row17).toBeVisible();
    await expect(row17).toContainClass("tobago-selected");
    await expect(columnPanel17).toContainClass("tobago-selected");
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(true);

    await firstPageButton.click();
    await expect(row4).toBeVisible();
    await expect(row4).toContainClass("tobago-selected");
    await expect(columnPanel4).toContainClass("tobago-selected");
    expect(await isSelected(page, 4)).toBe(true);
    expect(await isSelected(page, 17)).toBe(true);
  });

  test("lazy: select all; scroll down; deselect row 17; deselect row 3", async ({page}) => {
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const setupLazy = page.locator("[id='page:mainForm:setupLazy']");
    const hiddenSelectedField = page.locator("[id='page:mainForm:sheet::selected']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const row3 = page.locator("tr[row-index='3']");
    const row17 = page.locator("tr[row-index='17']");

    await expectDefaultSheet(page);
    await setupLazy.click();

    await expect(lazy).toBeChecked();
    await expect(sheetSelectable).toHaveValue("multi");
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).not.toBeChecked();
    await expect(hiddenSelectedField).toHaveValue("[]");
    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");

    await selectAllCheckbox.click();

    await expect(hiddenSelectedField).toHaveValue("[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87]");
    await expect(row3).toContainClass("tobago-selected");
    await expect(row17).toContainClass("tobago-selected");

    const row10 = row(page, 10);
    await focusRowIndex(page, 10);
    await expect(row10).toBeVisible();

    const row12 = row(page, 12);
    await focusRowIndex(page, 12);
    await expect(row12).toBeVisible();
    await row12.click();
    await row12.click();

    const row14 = row(page, 14);
    await focusRowIndex(page, 14);
    await expect(row14).toBeVisible();
    await row14.click();
    await row14.click();

    const row16 = row(page, 16);
    await focusRowIndex(page, 16);
    await expect(row16).toBeVisible();
    await row16.click();
    await row16.click();

    await expect(row14).toBeVisible();
    await row14.click();
    await row14.click();

    await focusRowIndex(page, 17);
    await expect(row17).toBeVisible();
    await row17.click();

    await expect(row3).toContainClass("tobago-selected");
    await expect(row17).not.toContainClass("tobago-selected");
    expect(await isSelected(page, 3)).toBe(true);
    expect(await isSelected(page, 17)).toBe(false);

    await focusRowIndex(page, 3);
    await expect(row3).toBeVisible();
    await row3.click();

    await expect(row3).not.toContainClass("tobago-selected");
    await expect(row17).not.toContainClass("tobago-selected");
    expect(await isSelected(page, 3)).toBe(false);
    expect(await isSelected(page, 17)).toBe(false);

  });

  test("lazy: scroll down, select row 17 column panel, select row 3 column panel", async ({page}) => {
    const rows = page.locator("[id='page:mainForm:rows::field']");
    const lazy = page.locator("[id='page:mainForm:lazy::field']");
    const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
    const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
    const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");
    const lazyColumnPanel = page.locator("[id='page:mainForm:lazyColumnPanel']");
    const hiddenSelectedField = page.locator("[id='page:mainForm:sheet::selected']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const row3 = page.locator("tr[row-index='3']");
    const row17 = page.locator("tr[row-index='17']");
    const columnPanel3 = page.locator(".tobago-column-panel[name='3']");
    const columnPanel17 = page.locator(".tobago-column-panel[name='17']");

    await expectDefaultSheet(page);
    await lazyColumnPanel.click();

    await expect(lazy).toBeChecked();
    await expect(sheetSelectable).toHaveValue("multi");
    await expect(columnSelectorRendered).toBeChecked();
    await expect(columnPanelRendered).toBeChecked();
    await expect(hiddenSelectedField).toHaveValue("[]");
    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");

    const row10 = row(page, 10);
    await focusRowIndex(page, 10);
    await expect(row10).toBeVisible();

    await expect(row3).not.toContainClass("tobago-selected");
    await expect(columnPanel3).not.toContainClass("tobago-selected");
    await expect(row17).not.toContainClass("tobago-selected");
    await expect(columnPanel17).not.toContainClass("tobago-selected");

    await row17.click();
    await expect(row3).not.toContainClass("tobago-selected");
    await expect(columnPanel3).not.toContainClass("tobago-selected");
    await expect(row17).toContainClass("tobago-selected");
    await expect(columnPanel17).toContainClass("tobago-selected");

    await columnPanel3.click();
    await expect(row3).toContainClass("tobago-selected");
    await expect(columnPanel3).toContainClass("tobago-selected");
    await expect(row17).toContainClass("tobago-selected");
    await expect(columnPanel17).toContainClass("tobago-selected");
  });
});

async function isSelected(page: Page, number: number): Promise<boolean> {
  const hiddenSelectedField = page.locator("[id='page:mainForm:sheet::selected']");
  const value = await hiddenSelectedField.inputValue();
  const selectedSet = new Set(JSON.parse(value));
  return selectedSet.has(number);
}

function row(page: Page, rowIndex: number) {
  return page.locator(`tr[row-index='${rowIndex}']`);
}

async function focusRowIndex(page: Page, rowIndex: number) {
  await page.locator("[id='page:mainForm:sheet'] .tobago-body").evaluate((sheetBody, rowIndex) => {
    const row = sheetBody.querySelector("tr[row-index='" + rowIndex + "']") as HTMLTableRowElement;
    sheetBody.scrollTop = row.offsetTop;
  }, rowIndex);
}

async function expectDefaultSheet(page: Page) {
  const rows = page.locator("[id='page:mainForm:rows::field']");
  const lazy = page.locator("[id='page:mainForm:lazy::field']");
  const sheetSelectable = page.locator("[id='page:mainForm:sheetSelectable::field']");
  const columnSelectorRendered = page.locator("[id='page:mainForm:columnSelectorRendered::field']");
  const columnPanelRendered = page.locator("[id='page:mainForm:columnPanelRendered::field']");

  await expect(rows).toHaveValue("5");
  await expect(lazy).not.toBeChecked();
  await expect(sheetSelectable).toHaveValue("multi");
  await expect(columnSelectorRendered).toBeChecked();
  await expect(columnPanelRendered).not.toBeChecked();
}

