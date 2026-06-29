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

import {expect, Locator, test} from "@playwright/test";

test.describe("080-sheet/10-sort/Sorting.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/080-sheet/10-sort/Sorting.xhtml");
  });

  test("Basics: Name", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s1']");
    const colNameFn = page.locator("[id='page:mainForm:s1:columnName_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s1'] .tobago-body tbody tr");

    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).toContainClass("tobago-ascending");
    await expect(colNameFn).not.toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        "1986U10", "0.64", "1999",
        "Adrastea", "0.3", "1979",
        "Amalthea", "0.5", "1892",
        "Ananke", "-629.77", "1951");
    await gotoRow(sheet, 22);
    await testRowContent(rowsFn,
        "Earth", "365.26", "",
        "Elara", "259.65", "1905",
        "Enceladus", "1.37", "1789",
        "Epimetheus", "0.69", "1980");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await expect(colNameFn).toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        "Proteus", "1.12", "1989",
        "Prospero", "-1962.95", "1999",
        "Prometheus", "0.61", "1980",
        "Praxidike", "625.3", "2000");
  });

  test("Basics: Period", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s1']");
    const colPeriodFn = page.locator("[id='page:mainForm:s1:columnPeriod_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s1'] .tobago-body tbody tr");

    await expect(colPeriodFn).not.toContainClass("tobago-ascending");
    await colPeriodFn.click();
    await expect(colPeriodFn).toContainClass("tobago-sortable");
    await expect(colPeriodFn).toContainClass("tobago-ascending");
    await expect(colPeriodFn).not.toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        "Setebos", "-2196.35", "1999",
        "Prospero", "-1962.95", "1999",
        "Sycorax", "-1283.48", "1997",
        "Sinope", "-758.9", "1914");
    await gotoRow(sheet, 29);
    await testRowContent(rowsFn,
        "Rosalind", "0.56", "1986",
        "Pan", "0.58", "1990",
        "Atlas", "0.6", "1980",
        "Prometheus", "0.61", "1980");
    await colPeriodFn.click();
    await expect(colPeriodFn).toContainClass("tobago-sortable");
    await expect(colPeriodFn).not.toContainClass("tobago-ascending");
    await expect(colPeriodFn).toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        "Callisto", "16.69", "1610",
        "Titan", "15.95", "1655",
        "Oberon", "13.46", "1787",
        "Titania", "8.71", "1787");
  });

  test("Basics: Year", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s1']");
    const colYearFn = page.locator("[id='page:mainForm:s1:columnDiscoverYear_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s1'] .tobago-body tbody tr");

    await expect(colYearFn).not.toContainClass("tobago-ascending");
    await colYearFn.click();
    await expect(colYearFn).toContainClass("tobago-sortable");
    await expect(colYearFn).toContainClass("tobago-ascending");
    await expect(colYearFn).not.toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        null, null, "1610",
        null, null, "1610",
        null, null, "1610",
        null, null, "1610");
    await gotoRow(sheet, 22);
    await testRowContent(rowsFn,
        null, null, "1892",
        null, null, "1898",
        null, null, "1904",
        null, null, "1905");
    await colYearFn.click();
    await expect(colYearFn).toContainClass("tobago-sortable");
    await expect(colYearFn).not.toContainClass("tobago-ascending");
    await expect(colYearFn).toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        null, null, "1999",
        null, null, "1999",
        null, null, "1997",
        null, null, "1997");
  });

  test("Basics: tobago-paginator-row; row 8, row 9", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s1']");
    const colNameFn = page.locator("[id='page:mainForm:s1:columnName_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s1'] .tobago-body tbody tr");

    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).toContainClass("tobago-ascending");
    await expect(colNameFn).not.toContainClass("tobago-descending");
    await gotoRow(sheet, 8);
    await testRowContent(rowsFn,
        "Bianca", "0.43", "1986",
        "Caliban", "-579.39", "1997",
        "Callirrhoe", "758.8", "2000",
        "Callisto", "16.69", "1610");
    await gotoRow(sheet, 9);
    await testRowContent(rowsFn,
        "Caliban", "-579.39", "1997",
        "Callirrhoe", "758.8", "2000",
        "Callisto", "16.69", "1610",
        "Calypso", "1.89", "1980");
  });

  test("Basics: tobago-paginator-list; page 7, 16, 14", async ({page}) => {
    const colNameFn = page.locator("[id='page:mainForm:s1:columnName_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s1'] .tobago-body tbody tr");
    const paginatorList = page.locator("[id='page:mainForm:s1:_paginator_list']");

    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).toContainClass("tobago-ascending");
    await expect(colNameFn).not.toContainClass("tobago-descending");
    await paginatorList.getByRole("button", {name: "7"}).click();
    await testRowContent(rowsFn,
        "Epimetheus", "0.69", "1980",
        "Erinome", "728.3", "2000",
        "Europa", "3.55", "1610",
        "Galatea", "0.43", "1989");
    await paginatorList.locator("button").last().click(); // "..."-button goes to page 16
    await testRowContent(rowsFn,
        "Phoebe", "-550.48", "1898",
        "Pluto", "90800.0", "1930",
        "Portia", "0.51", "1986",
        "Praxidike", "625.3", "2000");
    await paginatorList.getByRole("button", {name: "14"}).click();
    await testRowContent(rowsFn,
        "Neptune", "60190.0", "1846",
        "Nereid", "360.13", "1949",
        "Oberon", "13.46", "1787",
        "Ophelia", "0.38", "1986");
  });

  test("Basics: tobago-paginator-page; last page, previous page, first page, next page, page 14", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s1']");
    const colNameFn = page.locator("[id='page:mainForm:s1:columnName_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s1'] .tobago-body tbody tr");
    const paginatorPage = sheet.locator("tobago-paginator-page");
    const rightPagingFirstFn = paginatorPage.locator(".page-item").nth(0);
    const rightPagingPrevFn = paginatorPage.locator(".page-item").nth(1);
    const rightPagingNextFn = paginatorPage.locator(".page-item").nth(3);
    const rightPagingLastFn = paginatorPage.locator(".page-item").nth(4);

    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).toContainClass("tobago-ascending");
    await expect(colNameFn).not.toContainClass("tobago-descending");
    await rightPagingLastFn.click();
    await testRowContent(rowsFn,
        "Triton", "-5.88", "1846",
        "Umbriel", "4.14", "1851",
        "Uranus", "30685.0", "1781",
        "Venus", "224.7", "");
    await rightPagingPrevFn.click();
    await testRowContent(rowsFn,
        "Thebe", "0.67", "1979",
        "Themisto", "130.02", "2000",
        "Titan", "15.95", "1655",
        "Titania", "8.71", "1787");
    await rightPagingFirstFn.click();
    await testRowContent(rowsFn,
        "1986U10", "0.64", "1999",
        "Adrastea", "0.3", "1979",
        "Amalthea", "0.5", "1892",
        "Ananke", "-629.77", "1951");
    await rightPagingNextFn.click();
    await testRowContent(rowsFn,
        "Ariel", "2.52", "1851",
        "Atlas", "0.6", "1980",
        "Belinda", "0.62", "1986",
        "Bianca", "0.43", "1986");
    await gotoPage(sheet, 14);
    await testRowContent(rowsFn,
        "Neptune", "60190.0", "1846",
        "Nereid", "360.13", "1949",
        "Oberon", "13.46", "1787",
        "Ophelia", "0.38", "1986");
  });

  test("Custom Sorting: Name", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s2']");
    const colNameFn = page.locator("[id='page:mainForm:s2:customColumnName_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s2'] .tobago-body tbody tr");

    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).toContainClass("tobago-ascending");
    await expect(colNameFn).not.toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        "1986U10", "0.64", "1999",
        "Adrastea", "0.3", "1979",
        "Amalthea", "0.5", "1892",
        "Ananke", "-629.77", "1951");
    await gotoRow(sheet, 22);
    await testRowContent(rowsFn,
        "Earth", "365.26", "",
        "Elara", "259.65", "1905",
        "Enceladus", "1.37", "1789",
        "Epimetheus", "0.69", "1980");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await expect(colNameFn).toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        "Proteus", "1.12", "1989",
        "Prospero", "-1962.95", "1999",
        "Prometheus", "0.61", "1980",
        "Praxidike", "625.3", "2000");
  });

  test("Custom Sorting: Period", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s2']");
    const colPeriodFn = page.locator("[id='page:mainForm:s2:customColumnPeriod_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s2'] .tobago-body tbody tr");

    await expect(colPeriodFn).not.toContainClass("tobago-ascending");
    await colPeriodFn.click();
    await expect(colPeriodFn).toContainClass("tobago-sortable");
    await expect(colPeriodFn).toContainClass("tobago-ascending");
    await expect(colPeriodFn).not.toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        "Sun", "0.0", null,
        "Metis", "0.29", "1979",
        "Naiad", "0.29", "1989",
        "Adrastea", "0.3", "1979");
    await gotoRow(sheet, 29);
    await testRowContent(rowsFn,
        "Mimas", "0.94", "1789",
        "Proteus", "1.12", "1989",
        "Deimos", "1.26", "1877",
        "Enceladus", "1.37", "1789");
    await colPeriodFn.click();
    await expect(colPeriodFn).toContainClass("tobago-sortable");
    await expect(colPeriodFn).not.toContainClass("tobago-ascending");
    await expect(colPeriodFn).toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        "Elara", "259.65", "1905",
        "Lysithea", "259.22", "1938",
        "Himalia", "250.57", "1904",
        "Leda", "238.72", "1974");
  });

  test("Custom Sorting: Year", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s2']");
    const colYearFn = page.locator("[id='page:mainForm:s2:customColumnYear_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s2'] .tobago-body tbody tr");

    await expect(colYearFn).not.toContainClass("tobago-ascending");
    await colYearFn.click();
    await expect(colYearFn).toContainClass("tobago-sortable");
    await expect(colYearFn).toContainClass("tobago-ascending");
    await expect(colYearFn).not.toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        null, null, "",
        null, null, "",
        null, null, "",
        null, null, "");
    await gotoRow(sheet, 22);
    await testRowContent(rowsFn,
        null, null, "1789",
        null, null, "1846",
        null, null, "1846",
        null, null, "1848");
    await colYearFn.click();
    await expect(colYearFn).toContainClass("tobago-sortable");
    await expect(colYearFn).not.toContainClass("tobago-ascending");
    await expect(colYearFn).toContainClass("tobago-descending");
    await testRowContent(rowsFn,
        null, null, "1989",
        null, null, "1989",
        null, null, "1989",
        null, null, "1986");
  });

  test("Custom Sorting: tobago-paginator-row; row 8, row 9", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s2']");
    const colNameFn = page.locator("[id='page:mainForm:s2:customColumnName_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s2'] .tobago-body tbody tr");

    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).toContainClass("tobago-ascending");
    await expect(colNameFn).not.toContainClass("tobago-descending");
    await gotoRow(sheet, 8);
    await testRowContent(rowsFn,
        "Bianca", "0.43", "1986",
        "Caliban", "-579.39", "1997",
        "Callirrhoe", "758.8", "2000",
        "Callisto", "16.69", "1610");
    await gotoRow(sheet, 9);
    await testRowContent(rowsFn,
        "Caliban", "-579.39", "1997",
        "Callirrhoe", "758.8", "2000",
        "Callisto", "16.69", "1610",
        "Calypso", "1.89", "1980");
  });

  test("Custom Sorting: tobago-paginator-list; page 7, 16, 14", async ({page}) => {
    const colNameFn = page.locator("[id='page:mainForm:s2:customColumnName_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s2'] .tobago-body tbody tr");
    const paginatorList = page.locator("[id='page:mainForm:s2:_paginator_list']");

    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).toContainClass("tobago-ascending");
    await expect(colNameFn).not.toContainClass("tobago-descending");
    await paginatorList.getByRole("button", {name: "7"}).click();
    await testRowContent(rowsFn,
        "Epimetheus", "0.69", "1980",
        "Erinome", "728.3", "2000",
        "Europa", "3.55", "1610",
        "Galatea", "0.43", "1989");
    await paginatorList.locator("button").last().click(); // "..."-button goes to page 16
    await testRowContent(rowsFn,
        "Phoebe", "-550.48", "1898",
        "Pluto", "90800.0", "1930",
        "Portia", "0.51", "1986",
        "Praxidike", "625.3", "2000");
    await paginatorList.getByRole("button", {name: "14"}).click();
    await testRowContent(rowsFn,
        "Neptune", "60190.0", "1846",
        "Nereid", "360.13", "1949",
        "Oberon", "13.46", "1787",
        "Ophelia", "0.38", "1986");
  });

  test("Custom Sorting: tobago-paginator-page; last page, previous page, first page, next page, page 14", async ({page}) => {
    const sheet = page.locator("[id='page:mainForm:s2']");
    const colNameFn = page.locator("[id='page:mainForm:s2:customColumnName_sorter']");
    const rowsFn = page.locator("[id='page:mainForm:s2'] .tobago-body tbody tr");
    const paginatorPage = sheet.locator("tobago-paginator-page");
    const rightPagingFirstFn = paginatorPage.locator(".page-item").nth(0);
    const rightPagingPrevFn = paginatorPage.locator(".page-item").nth(1);
    const rightPagingNextFn = paginatorPage.locator(".page-item").nth(3);
    const rightPagingLastFn = paginatorPage.locator(".page-item").nth(4);

    await expect(colNameFn).not.toContainClass("tobago-ascending");
    await colNameFn.click();
    await expect(colNameFn).toContainClass("tobago-sortable");
    await expect(colNameFn).toContainClass("tobago-ascending");
    await expect(colNameFn).not.toContainClass("tobago-descending");
    await rightPagingLastFn.click();
    await testRowContent(rowsFn,
        "Triton", "-5.88", "1846",
        "Umbriel", "4.14", "1851",
        "Uranus", "30685.0", "1781",
        "Venus", "224.7", "");
    await rightPagingPrevFn.click();
    await testRowContent(rowsFn,
        "Thebe", "0.67", "1979",
        "Themisto", "130.02", "2000",
        "Titan", "15.95", "1655",
        "Titania", "8.71", "1787");
    await rightPagingFirstFn.click();
    await testRowContent(rowsFn,
        "1986U10", "0.64", "1999",
        "Adrastea", "0.3", "1979",
        "Amalthea", "0.5", "1892",
        "Ananke", "-629.77", "1951");
    await rightPagingNextFn.click();
    await testRowContent(rowsFn,
        "Ariel", "2.52", "1851",
        "Atlas", "0.6", "1980",
        "Belinda", "0.62", "1986",
        "Bianca", "0.43", "1986");
    await gotoPage(sheet, 14);
    await testRowContent(rowsFn,
        "Neptune", "60190.0", "1846",
        "Nereid", "360.13", "1949",
        "Oberon", "13.46", "1787",
        "Ophelia", "0.38", "1986");
  });

  async function gotoRow(sheet: Locator, rowNumber: number): Promise<void> {
    const rowPaginator = sheet.locator("tobago-paginator-row");
    const inputField = rowPaginator.locator("input");
    await expect(inputField).not.toBeVisible();
    await rowPaginator.click();
    await expect(inputField).toBeVisible();
    await inputField.fill(rowNumber.toString());
    await sheet.page().keyboard.press("Enter");
    await expect(inputField).not.toBeVisible();
  }

  async function gotoPage(sheet: Locator, pageNumber: number): Promise<void> {
    const paginatorPage = sheet.locator("tobago-paginator-page");
    const centerClickField = paginatorPage.locator(".page-item").nth(2);
    const inputField = centerClickField.locator("input");

    await expect(inputField).not.toBeVisible();
    await centerClickField.click();
    await expect(inputField).toBeVisible();
    await inputField.fill(pageNumber.toString());
    await sheet.page().keyboard.press("Enter");
    await expect(inputField).not.toBeVisible();
  }

  async function testRowContent(rows: Locator, ...expected: (string | null)[]): Promise<void> {
    await expect(rows).toHaveCount(4);
    await testCellContent(rows.nth(0).locator("td").nth(0), expected[0]);
    await testCellContent(rows.nth(0).locator("td").nth(1), expected[1]);
    await testCellContent(rows.nth(0).locator("td").nth(2), expected[2]);
    await testCellContent(rows.nth(1).locator("td").nth(0), expected[3]);
    await testCellContent(rows.nth(1).locator("td").nth(1), expected[4]);
    await testCellContent(rows.nth(1).locator("td").nth(2), expected[5]);
    await testCellContent(rows.nth(2).locator("td").nth(0), expected[6]);
    await testCellContent(rows.nth(2).locator("td").nth(1), expected[7]);
    await testCellContent(rows.nth(2).locator("td").nth(2), expected[8]);
    await testCellContent(rows.nth(3).locator("td").nth(0), expected[9]);
    await testCellContent(rows.nth(3).locator("td").nth(1), expected[10]);
    await testCellContent(rows.nth(3).locator("td").nth(2), expected[11]);
  }

  async function testCellContent(cell: Locator, expected: string | null): Promise<void> {
    if (expected) {
      await expect(cell).toHaveText(expected);
    }
  }
});
