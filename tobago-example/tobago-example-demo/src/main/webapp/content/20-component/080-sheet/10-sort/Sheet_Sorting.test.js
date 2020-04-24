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

import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Basics: Name", function (done) {
  let colNameFn = querySelectorFn("#page\\:mainForm\\:s1\\:columnName_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colNameFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "Earth",
      () => {
        leftPagingFn().value = "22";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}));
      });
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Proteus", "1.12", "1989",
      "Prospero", "-1962.95", "1999",
      "Prometheus", "0.61", "1980",
      "Praxidike", "625.3", "2000"));
  test.do(() => expectBodyTable(rowsFn(),
      "Proteus", "1.12", "1989",
      "Prospero", "-1962.95", "1999",
      "Prometheus", "0.61", "1980",
      "Praxidike", "625.3", "2000"));
  test.do(() => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Earth", "365.26", "",
      "Elara", "259.65", "1905",
      "Enceladus", "1.37", "1789",
      "Epimetheus", "0.69", "1980"));
  test.do(() => expectBodyTable(rowsFn(),
      "Earth", "365.26", "",
      "Elara", "259.65", "1905",
      "Enceladus", "1.37", "1789",
      "Epimetheus", "0.69", "1980"));
  test.start();
});

it("Basics: Period", function (done) {
  let colPeriodFn = querySelectorFn("#page\\:mainForm\\:s1\\:columnPeriod_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colPeriodFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colPeriodFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "Rosalind",
      () => {
        leftPagingFn().value = "29";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colPeriodFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colPeriodFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colPeriodFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => colPeriodFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Callisto", "16.69", "1610",
      "Titan", "15.95", "1655",
      "Oberon", "13.46", "1787",
      "Titania", "8.71", "1787"));
  test.do(() => expectBodyTable(rowsFn(),
      "Callisto", "16.69", "1610",
      "Titan", "15.95", "1655",
      "Oberon", "13.46", "1787",
      "Titania", "8.71", "1787"));
  test.do(() => colPeriodFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Rosalind", "0.56", "1986",
      "Pan", "0.58", "1990",
      "Atlas", "0.6", "1980",
      "Prometheus", "0.61", "1980"));
  test.do(() => expectBodyTable(rowsFn(),
      "Rosalind", "0.56", "1986",
      "Pan", "0.58", "1990",
      "Atlas", "0.6", "1980",
      "Prometheus", "0.61", "1980"));
  test.start();
});

it("Basics: Year", function (done) {
  let colYearFn = querySelectorFn("#page\\:mainForm\\:s1\\:columnDiscoverYear_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colYearFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colYearFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "Amalthea",
      () => {
        leftPagingFn().value = "22";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colYearFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colYearFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colYearFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => colYearFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => rowsFn() && rowsFn()[0].querySelectorAll(".tobago-out")[2].textContent === "1999");
  test.do(() => expect(rowsFn()[0].querySelectorAll(".tobago-out")[2].textContent).toBe("1999"));
  test.do(() => expect(rowsFn()[1].querySelectorAll(".tobago-out")[2].textContent).toBe("1999"));
  test.do(() => expect(rowsFn()[2].querySelectorAll(".tobago-out")[2].textContent).toBe("1997"));
  test.do(() => expect(rowsFn()[3].querySelectorAll(".tobago-out")[2].textContent).toBe("1997"));
  test.do(() => colYearFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => rowsFn() && rowsFn()[0].querySelectorAll(".tobago-out")[2].textContent === "1892");
  test.do(() => expect(rowsFn()[0].querySelectorAll(".tobago-out")[2].textContent).toBe("1892"));
  test.do(() => expect(rowsFn()[1].querySelectorAll(".tobago-out")[2].textContent).toBe("1898"));
  test.do(() => expect(rowsFn()[2].querySelectorAll(".tobago-out")[2].textContent).toBe("1904"));
  test.do(() => expect(rowsFn()[3].querySelectorAll(".tobago-out")[2].textContent).toBe("1905"));
  test.start();
});

/**
 * 1. goto line 8
 * 2. goto line 9
 */
it("Basics: left paging", function (done) {
  let colNameFn = querySelectorFn("#page\\:mainForm\\:s1\\:columnName_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colNameFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "1986U10",
      () => {
        leftPagingFn().value = "1";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => leftPagingFn().value = "8");
  test.do(() => leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Bianca", "0.43", "1986",
      "Caliban", "-579.39", "1997",
      "Callirrhoe", "758.8", "2000",
      "Callisto", "16.69", "1610"));
  test.do(() => expectBodyTable(rowsFn(),
      "Bianca", "0.43", "1986",
      "Caliban", "-579.39", "1997",
      "Callirrhoe", "758.8", "2000",
      "Callisto", "16.69", "1610"));
  test.do(() => leftPagingFn().value = "9");
  test.do(() => leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Caliban", "-579.39", "1997",
      "Callirrhoe", "758.8", "2000",
      "Callisto", "16.69", "1610",
      "Calypso", "1.89", "1980"));
  test.do(() => expectBodyTable(rowsFn(),
      "Caliban", "-579.39", "1997",
      "Callirrhoe", "758.8", "2000",
      "Callisto", "16.69", "1610",
      "Calypso", "1.89", "1980"));
  test.start();
});

/**
 * 1. goto page 7
 * 2. goto page 16
 * 3. goto page 13
 */
it("Basics: center paging", function (done) {
  let colNameFn = querySelectorFn("#page\\:mainForm\\:s1\\:columnName_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");
  let centerPagingFn = querySelectorAllFn("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-center li .page-link");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colNameFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "1986U10",
      () => {
        leftPagingFn().value = "1";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => centerPagingFn().item(6).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Epimetheus", "0.69", "1980",
      "Erinome", "728.3", "2000",
      "Europa", "3.55", "1610",
      "Galatea", "0.43", "1989"));
  test.do(() => expectBodyTable(rowsFn(),
      "Epimetheus", "0.69", "1980",
      "Erinome", "728.3", "2000",
      "Europa", "3.55", "1610",
      "Galatea", "0.43", "1989"));
  test.do(() => centerPagingFn().item(10).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Phoebe", "-550.48", "1898",
      "Pluto", "90800.0", "1930",
      "Portia", "0.51", "1986",
      "Praxidike", "625.3", "2000"));
  test.do(() => expectBodyTable(rowsFn(),
      "Phoebe", "-550.48", "1898",
      "Pluto", "90800.0", "1930",
      "Portia", "0.51", "1986",
      "Praxidike", "625.3", "2000"));
  test.do(() => centerPagingFn().item(3).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Neptune", "60190.0", "1846",
      "Nereid", "360.13", "1949",
      "Oberon", "13.46", "1787",
      "Ophelia", "0.38", "1986"));
  test.do(() => expectBodyTable(rowsFn(),
      "Neptune", "60190.0", "1846",
      "Nereid", "360.13", "1949",
      "Oberon", "13.46", "1787",
      "Ophelia", "0.38", "1986"));
  test.start();
});

/**
 * 1. goto first page
 * 2. goto page 2 by pressing arrow-right
 * 3. goto last page
 * 4. goto page 21 by pressing arrow-left
 * 5. goto page 14
 */
it("Basics: right paging", function (done) {
  let colNameFn = querySelectorFn("#page\\:mainForm\\:s1\\:columnName_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");
  let rightPagingFn = querySelectorAllFn("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-right .page-link");
  let jumpToPageFn = querySelectorFn("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-right .page-link input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colNameFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "Earth",
      () => {
        leftPagingFn().value = "22";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => rightPagingFn().item(0).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "1986U10", "0.64", "1999",
      "Adrastea", "0.3", "1979",
      "Amalthea", "0.5", "1892",
      "Ananke", "-629.77", "1951"));
  test.do(() => expectBodyTable(rowsFn(),
      "1986U10", "0.64", "1999",
      "Adrastea", "0.3", "1979",
      "Amalthea", "0.5", "1892",
      "Ananke", "-629.77", "1951"));
  test.do(() => rightPagingFn().item(3).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Ariel", "2.52", "1851",
      "Atlas", "0.6", "1980",
      "Belinda", "0.62", "1986",
      "Bianca", "0.43", "1986"));
  test.do(() => expectBodyTable(rowsFn(),
      "Ariel", "2.52", "1851",
      "Atlas", "0.6", "1980",
      "Belinda", "0.62", "1986",
      "Bianca", "0.43", "1986"));
  test.do(() => rightPagingFn().item(4).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Triton", "-5.88", "1846",
      "Umbriel", "4.14", "1851",
      "Uranus", "30685.0", "1781",
      "Venus", "224.7", ""));
  test.do(() => expectBodyTable(rowsFn(),
      "Triton", "-5.88", "1846",
      "Umbriel", "4.14", "1851",
      "Uranus", "30685.0", "1781",
      "Venus", "224.7", ""));
  test.do(() => rightPagingFn().item(1).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Thebe", "0.67", "1979",
      "Themisto", "130.02", "2000",
      "Titan", "15.95", "1655",
      "Titania", "8.71", "1787"));
  test.do(() => expectBodyTable(rowsFn(),
      "Thebe", "0.67", "1979",
      "Themisto", "130.02", "2000",
      "Titan", "15.95", "1655",
      "Titania", "8.71", "1787"));
  test.do(() => jumpToPageFn().value = "14");
  test.do(() => jumpToPageFn().dispatchEvent(new Event("blur", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Neptune", "60190.0", "1846",
      "Nereid", "360.13", "1949",
      "Oberon", "13.46", "1787",
      "Ophelia", "0.38", "1986"));
  test.do(() => expectBodyTable(rowsFn(),
      "Neptune", "60190.0", "1846",
      "Nereid", "360.13", "1949",
      "Oberon", "13.46", "1787",
      "Ophelia", "0.38", "1986"));
  test.start();
});

it("Custom Sorting: Name", function (done) {
  let colNameFn = querySelectorFn("#page\\:mainForm\\:s2\\:customColumnName_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colNameFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "Earth",
      () => {
        leftPagingFn().value = "22";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Proteus", "1.12", "1989",
      "Prospero", "-1962.95", "1999",
      "Prometheus", "0.61", "1980",
      "Praxidike", "625.3", "2000"));
  test.do(() => expectBodyTable(rowsFn(),
      "Proteus", "1.12", "1989",
      "Prospero", "-1962.95", "1999",
      "Prometheus", "0.61", "1980",
      "Praxidike", "625.3", "2000"));
  test.do(() => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Earth", "365.26", "",
      "Elara", "259.65", "1905",
      "Enceladus", "1.37", "1789",
      "Epimetheus", "0.69", "1980"));
  test.do(() => expectBodyTable(rowsFn(),
      "Earth", "365.26", "",
      "Elara", "259.65", "1905",
      "Enceladus", "1.37", "1789",
      "Epimetheus", "0.69", "1980"));
  test.start();
});

it("Custom Sorting: Period", function (done) {
  let colPeriodFn = querySelectorFn("#page\\:mainForm\\:s2\\:customColumnPeriod_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colPeriodFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colPeriodFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "Mimas",
      () => {
        leftPagingFn().value = "29";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colPeriodFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colPeriodFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colPeriodFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => colPeriodFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Elara", "259.65", "1905",
      "Lysithea", "259.22", "1938",
      "Himalia", "250.57", "1904",
      "Leda", "238.72", "1974"));
  test.do(() => expectBodyTable(rowsFn(),
      "Elara", "259.65", "1905",
      "Lysithea", "259.22", "1938",
      "Himalia", "250.57", "1904",
      "Leda", "238.72", "1974"));
  test.do(() => colPeriodFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Mimas", "0.94", "1789",
      "Proteus", "1.12", "1989",
      "Deimos", "1.26", "1877",
      "Enceladus", "1.37", "1789"));
  test.do(() => expectBodyTable(rowsFn(),
      "Mimas", "0.94", "1789",
      "Proteus", "1.12", "1989",
      "Deimos", "1.26", "1877",
      "Enceladus", "1.37", "1789"));
  test.start();
});

it("Custom Sorting: Year", function (done) {
  let colYearFn = querySelectorFn("#page\\:mainForm\\:s2\\:customColumnYear_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colYearFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colYearFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelectorAll(".tobago-out")[2].textContent === "1789",
      () => {
        leftPagingFn().value = "22";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colYearFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colYearFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colYearFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => colYearFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => rowsFn() && rowsFn()[0].querySelectorAll(".tobago-out")[2].textContent === "1989");
  test.do(() => expect(rowsFn()[0].querySelectorAll(".tobago-out")[2].textContent).toBe("1989"));
  test.do(() => expect(rowsFn()[1].querySelectorAll(".tobago-out")[2].textContent).toBe("1989"));
  test.do(() => expect(rowsFn()[2].querySelectorAll(".tobago-out")[2].textContent).toBe("1989"));
  test.do(() => expect(rowsFn()[3].querySelectorAll(".tobago-out")[2].textContent).toBe("1986"));
  test.do(() => colYearFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => rowsFn() && rowsFn()[0].querySelectorAll(".tobago-out")[2].textContent === "1789");
  test.do(() => expect(rowsFn()[0].querySelectorAll(".tobago-out")[2].textContent).toBe("1789"));
  test.do(() => expect(rowsFn()[1].querySelectorAll(".tobago-out")[2].textContent).toBe("1846"));
  test.do(() => expect(rowsFn()[2].querySelectorAll(".tobago-out")[2].textContent).toBe("1846"));
  test.do(() => expect(rowsFn()[3].querySelectorAll(".tobago-out")[2].textContent).toBe("1848"));
  test.start();
});

/**
 * 1. goto line 8
 * 2. goto line 9
 */
it("Custom Sorting: left paging", function (done) {
  let colNameFn = querySelectorFn("#page\\:mainForm\\:s2\\:customColumnName_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colNameFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "1986U10",
      () => {
        leftPagingFn().value = "1";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => leftPagingFn().value = "8");
  test.do(() => leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Bianca", "0.43", "1986",
      "Caliban", "-579.39", "1997",
      "Callirrhoe", "758.8", "2000",
      "Callisto", "16.69", "1610"));
  test.do(() => expectBodyTable(rowsFn(),
      "Bianca", "0.43", "1986",
      "Caliban", "-579.39", "1997",
      "Callirrhoe", "758.8", "2000",
      "Callisto", "16.69", "1610"));
  test.do(() => leftPagingFn().value = "9");
  test.do(() => leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Caliban", "-579.39", "1997",
      "Callirrhoe", "758.8", "2000",
      "Callisto", "16.69", "1610",
      "Calypso", "1.89", "1980"));
  test.do(() => expectBodyTable(rowsFn(),
      "Caliban", "-579.39", "1997",
      "Callirrhoe", "758.8", "2000",
      "Callisto", "16.69", "1610",
      "Calypso", "1.89", "1980"));
  test.start();
});

/**
 * 1. goto page 7
 * 2. goto page 16
 * 3. goto page 13
 */
it("Custom Sorting: center paging", function (done) {
  let colNameFn = querySelectorFn("#page\\:mainForm\\:s2\\:customColumnName_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");
  let centerPagingFn = querySelectorAllFn("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-center li .page-link");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colNameFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "1986U10",
      () => {
        leftPagingFn().value = "1";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => centerPagingFn().item(6).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Epimetheus", "0.69", "1980",
      "Erinome", "728.3", "2000",
      "Europa", "3.55", "1610",
      "Galatea", "0.43", "1989"));
  test.do(() => expectBodyTable(rowsFn(),
      "Epimetheus", "0.69", "1980",
      "Erinome", "728.3", "2000",
      "Europa", "3.55", "1610",
      "Galatea", "0.43", "1989"));
  test.do(() => centerPagingFn().item(10).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Phoebe", "-550.48", "1898",
      "Pluto", "90800.0", "1930",
      "Portia", "0.51", "1986",
      "Praxidike", "625.3", "2000"));
  test.do(() => expectBodyTable(rowsFn(),
      "Phoebe", "-550.48", "1898",
      "Pluto", "90800.0", "1930",
      "Portia", "0.51", "1986",
      "Praxidike", "625.3", "2000"));
  test.do(() => centerPagingFn().item(3).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Neptune", "60190.0", "1846",
      "Nereid", "360.13", "1949",
      "Oberon", "13.46", "1787",
      "Ophelia", "0.38", "1986"));
  test.do(() => expectBodyTable(rowsFn(),
      "Neptune", "60190.0", "1846",
      "Nereid", "360.13", "1949",
      "Oberon", "13.46", "1787",
      "Ophelia", "0.38", "1986"));
  test.start();
});

/**
 * 1. goto first page
 * 2. goto page 2 by pressing arrow-right
 * 3. goto last page
 * 4. goto page 21 by pressing arrow-left
 * 5. goto page 14
 */
it("Custom Sorting: right paging", function (done) {
  let colNameFn = querySelectorFn("#page\\:mainForm\\:s2\\:customColumnName_sorter");
  let rowsFn = querySelectorAllFn("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  let leftPagingFn = querySelectorFn("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");
  let rightPagingFn = querySelectorAllFn("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-right .page-link");
  let rightPagingInputFn = querySelectorFn("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-right .page-link input");

  let test = new JasmineTestTool(done);
  test.setup(
      () => colNameFn().classList.contains("tobago-sheet-header-markup-ascending"),
      () => colNameFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.setup(
      () => rowsFn()[0].querySelector(".tobago-out").textContent === "Earth",
      () => {
        leftPagingFn().value = "22";
        leftPagingFn().dispatchEvent(new Event("blur", {bubbles: true}))
      });
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-sortable")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-ascending")).toBe(true));
  test.do(() => expect(colNameFn().classList.contains("tobago-sheet-header-markup-descending")).not.toBe(true));
  test.do(() => rightPagingFn().item(0).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "1986U10", "0.64", "1999",
      "Adrastea", "0.3", "1979",
      "Amalthea", "0.5", "1892",
      "Ananke", "-629.77", "1951"));
  test.do(() => expectBodyTable(rowsFn(),
      "1986U10", "0.64", "1999",
      "Adrastea", "0.3", "1979",
      "Amalthea", "0.5", "1892",
      "Ananke", "-629.77", "1951"));
  test.do(() => rightPagingFn().item(3).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Ariel", "2.52", "1851",
      "Atlas", "0.6", "1980",
      "Belinda", "0.62", "1986",
      "Bianca", "0.43", "1986"));
  test.do(() => expectBodyTable(rowsFn(),
      "Ariel", "2.52", "1851",
      "Atlas", "0.6", "1980",
      "Belinda", "0.62", "1986",
      "Bianca", "0.43", "1986"));
  test.do(() => rightPagingFn().item(4).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Triton", "-5.88", "1846",
      "Umbriel", "4.14", "1851",
      "Uranus", "30685.0", "1781",
      "Venus", "224.7", ""));
  test.do(() => expectBodyTable(rowsFn(),
      "Triton", "-5.88", "1846",
      "Umbriel", "4.14", "1851",
      "Uranus", "30685.0", "1781",
      "Venus", "224.7", ""));
  test.do(() => rightPagingFn().item(1).dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Thebe", "0.67", "1979",
      "Themisto", "130.02", "2000",
      "Titan", "15.95", "1655",
      "Titania", "8.71", "1787"));
  test.do(() => expectBodyTable(rowsFn(),
      "Thebe", "0.67", "1979",
      "Themisto", "130.02", "2000",
      "Titan", "15.95", "1655",
      "Titania", "8.71", "1787"));
  test.do(() => rightPagingInputFn().value = "14");
  test.do(() => rightPagingInputFn().dispatchEvent(new Event("blur", {bubbles: true})));
  test.wait(() => waitForBodyTable(rowsFn(),
      "Neptune", "60190.0", "1846",
      "Nereid", "360.13", "1949",
      "Oberon", "13.46", "1787",
      "Ophelia", "0.38", "1986"));
  test.do(() => expectBodyTable(rowsFn(),
      "Neptune", "60190.0", "1846",
      "Nereid", "360.13", "1949",
      "Oberon", "13.46", "1787",
      "Ophelia", "0.38", "1986"));
  test.start();
});

function waitForBodyTable(rowsFn, row0col0, row0col1, row0col2, row1col0, row1col1, row1col2,
                          row2col0, row2col1, row2col2, row3col0, row3col1, row3col2) {
  let $row0Col0 = rowsFn.item(0).querySelectorAll(".tobago-sheet-cell").item(0).textContent.trim();
  let $row0Col1 = rowsFn.item(0).querySelectorAll(".tobago-sheet-cell").item(1).textContent.trim();
  let $row0Col2 = rowsFn.item(0).querySelectorAll(".tobago-sheet-cell").item(2).textContent.trim();
  let $row1Col0 = rowsFn.item(1).querySelectorAll(".tobago-sheet-cell").item(0).textContent.trim();
  let $row1Col1 = rowsFn.item(1).querySelectorAll(".tobago-sheet-cell").item(1).textContent.trim();
  let $row1Col2 = rowsFn.item(1).querySelectorAll(".tobago-sheet-cell").item(2).textContent.trim();
  let $row2Col0 = rowsFn.item(2).querySelectorAll(".tobago-sheet-cell").item(0).textContent.trim();
  let $row2Col1 = rowsFn.item(2).querySelectorAll(".tobago-sheet-cell").item(1).textContent.trim();
  let $row2Col2 = rowsFn.item(2).querySelectorAll(".tobago-sheet-cell").item(2).textContent.trim();
  let $row3Col0 = rowsFn.item(3).querySelectorAll(".tobago-sheet-cell").item(0).textContent.trim();
  let $row3Col1 = rowsFn.item(3).querySelectorAll(".tobago-sheet-cell").item(1).textContent.trim();
  let $row3Col2 = rowsFn.item(3).querySelectorAll(".tobago-sheet-cell").item(2).textContent.trim();

  return ($row0Col0 === row0col0 && $row0Col1 === row0col1 && $row0Col2 === row0col2
      && $row1Col0 === row1col0 && $row1Col1 === row1col1 && $row1Col2 === row1col2
      && $row2Col0 === row2col0 && $row2Col1 === row2col1 && $row2Col2 === row2col2
      && $row3Col0 === row3col0 && $row3Col1 === row3col1 && $row3Col2 === row3col2);
}

function expectBodyTable(rowsFn, row0col0, row0col1, row0col2, row1col0, row1col1, row1col2,
                         row2col0, row2col1, row2col2, row3col0, row3col1, row3col2) {
  let $row0Col0 = rowsFn.item(0).querySelectorAll(".tobago-sheet-cell").item(0).textContent.trim();
  let $row0Col1 = rowsFn.item(0).querySelectorAll(".tobago-sheet-cell").item(1).textContent.trim();
  let $row0Col2 = rowsFn.item(0).querySelectorAll(".tobago-sheet-cell").item(2).textContent.trim();
  let $row1Col0 = rowsFn.item(1).querySelectorAll(".tobago-sheet-cell").item(0).textContent.trim();
  let $row1Col1 = rowsFn.item(1).querySelectorAll(".tobago-sheet-cell").item(1).textContent.trim();
  let $row1Col2 = rowsFn.item(1).querySelectorAll(".tobago-sheet-cell").item(2).textContent.trim();
  let $row2Col0 = rowsFn.item(2).querySelectorAll(".tobago-sheet-cell").item(0).textContent.trim();
  let $row2Col1 = rowsFn.item(2).querySelectorAll(".tobago-sheet-cell").item(1).textContent.trim();
  let $row2Col2 = rowsFn.item(2).querySelectorAll(".tobago-sheet-cell").item(2).textContent.trim();
  let $row3Col0 = rowsFn.item(3).querySelectorAll(".tobago-sheet-cell").item(0).textContent.trim();
  let $row3Col1 = rowsFn.item(3).querySelectorAll(".tobago-sheet-cell").item(1).textContent.trim();
  let $row3Col2 = rowsFn.item(3).querySelectorAll(".tobago-sheet-cell").item(2).textContent.trim();

  expect($row0Col0).toBe(row0col0, "row0col0");
  expect($row0Col1).toBe(row0col1, "row0col1");
  expect($row0Col2).toBe(row0col2, "row0col2");
  expect($row1Col0).toBe(row1col0, "row1col0");
  expect($row1Col1).toBe(row1col1, "row1col1");
  expect($row1Col2).toBe(row1col2, "row1col2");
  expect($row2Col0).toBe(row2col0, "row2col0");
  expect($row2Col1).toBe(row2col1, "row2col1");
  expect($row2Col2).toBe(row2col2, "row2col2")
  expect($row3Col0).toBe(row3col0, "row3col0");
  expect($row3Col1).toBe(row3col1, "row3col1");
  expect($row3Col2).toBe(row3col2, "row3col2");
}
