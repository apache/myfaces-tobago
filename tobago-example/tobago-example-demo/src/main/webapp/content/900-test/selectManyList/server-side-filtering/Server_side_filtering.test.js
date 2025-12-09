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

it("SelectManyList minChar=3: leaving component", function (done) {
  const minChar3 = elementByIdFn("page:mainForm:minChar3");
  const badges = querySelectorAllFn("div[id='page:mainForm:selectManyList::selectField'] > .tobago-badges > .btn-group");
  const filterInput = elementByIdFn("page:mainForm:selectManyList::filter");
  const tobagoSelectItems = querySelectorAllFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item");
  const rheaSelectItem = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item[data-tobago-value='Rhea']");
  const customFooter = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-custom-footer");
  const customFooterCell = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-custom-footer td");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = currentTimestamp());
  test.event("click", minChar3, () => currentTimestamp() > timestamp);
  test.do(() => expect(tobagoSelectItems().length).toBe(0));
  test.do(() => expect(customFooter().textContent.trim()).toBe("type 3 character for results"));
  test.do(() => filterInput().value = "hea");
  test.event("input", filterInput, () => tobagoSelectItems().length > 0);
  test.do(() => expect(tobagoSelectItems().length).toBe(3));
  test.do(() => expect(getComputedStyle(customFooterCell()).display).toBe("none"));
  test.event("click", rheaSelectItem, () => badges().length === 1);
  test.do(() => expect(badges()[0].dataset.tobagoValue).toBe("Rhea"));
  test.do(() => expect(tobagoSelectItems().length).toBe(3));
  test.event("blur", filterInput, () => tobagoSelectItems().length === 0);
  test.do(() => expect(tobagoSelectItems().length).toBe(0));
  test.start();
});

it("SelectManyList: avoid 'no valid selection' error", function (done) {
  const reset = elementByIdFn("page:mainForm:reset");
  const badges = querySelectorAllFn("div[id='page:mainForm:selectManyList::selectField'] > .tobago-badges > .btn-group");
  const firstBadge = querySelectorFn("div[id='page:mainForm:selectManyList::selectField'] > .tobago-badges > .btn-group > .badge");
  const filterInput = elementByIdFn("page:mainForm:selectManyList::filter");
  const tobagoSelectItems = querySelectorAllFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item");
  const lysitheaSelectItem = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item[data-tobago-value='Lysithea']");
  const hiddenQueryInput = elementByIdFn("page:mainForm:selectItemsFilteredMany::query");
  const popover = querySelectorFn("tobago-select-many-list[id='page:mainForm:selectManyList'] tobago-popover");

  const test = new JasmineTestTool(done);
  test.setup(() => badges().length === 0
          && tobagoSelectItems().length === 0
          && Number(hiddenQueryInput().dataset.tobagoMinChars) === 0,
      null, "click", reset);
  test.do(() => filterInput().value = "y");
  test.event("input", filterInput, () => tobagoSelectItems().length > 0);
  test.event("click", lysitheaSelectItem, () => badges().length > 0);
  test.do(() => expect(firstBadge().textContent).toBe("Lysithea"));
  test.event("click", reset, () => badges().length === 0);
  test.do(() => expect(popover()).toBeNull());
  test.start();
});

it("SelectManyList: footer must be visible for an empty list", function (done) {
  const reset = elementByIdFn("page:mainForm:reset");
  const badges = querySelectorAllFn("div[id='page:mainForm:selectManyList::selectField'] > .tobago-badges > .btn-group");
  const filterInput = elementByIdFn("page:mainForm:selectManyList::filter");
  const tobagoDropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList']");
  const tobagoSelectItems = querySelectorAllFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item");
  const customFooter = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-custom-footer");
  const hiddenQueryInput = elementByIdFn("page:mainForm:selectItemsFilteredMany::query");

  const test = new JasmineTestTool(done);
  test.setup(() => badges().length === 0
          && !tobagoDropdownMenu().classList.contains("show")
          && tobagoSelectItems().length === 0
          && Number(hiddenQueryInput().dataset.tobagoMinChars) === 0,
      null, "click", reset);
  test.do(() => expect(badges().length).toBe(0));
  test.do(() => expect(tobagoDropdownMenu().classList.contains("show")).toBeFalse());
  test.do(() => expect(tobagoSelectItems().length).toBe(0));
  test.event("input", filterInput, () => tobagoDropdownMenu().classList.contains("show"));
  test.do(() => expect(badges().length).toBe(0));
  test.do(() => expect(tobagoDropdownMenu().classList.contains("show")).toBeTrue());
  test.do(() => expect(tobagoSelectItems().length).toBe(0));
  test.do(() => expect(customFooter().textContent.trim()).toBe("filter for results"));
  test.start();
});

it("SelectManyList: ajax listener", function (done) {
  const reset = elementByIdFn("page:mainForm:reset");
  const selectField = querySelectorFn("#page\\:mainForm\\:selectManyList\\:\\:selectField");
  const badges = querySelectorAllFn("div[id='page:mainForm:selectManyList::selectField'] > .tobago-badges > .btn-group");
  const filterInput = elementByIdFn("page:mainForm:selectManyList::filter");
  const tobagoSelectItems = querySelectorAllFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item");
  const thebeSelectItem = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item[data-tobago-value='Thebe']");
  const hiddenQueryInput = elementByIdFn("page:mainForm:selectItemsFilteredMany::query");

  const test = new JasmineTestTool(done);
  test.setup(() => changeCount() === 0
          && clickCount() === 0
          && dblclickCount() === 0
          && focusCount() === 0
          && blurCount() === 0
          && badges().length === 0
          && tobagoSelectItems().length === 0
          && Number(hiddenQueryInput().dataset.tobagoMinChars) === 0,
      null, "click", reset);
  test.event("click", selectField, () => clickCount() >= 1);
  test.do(() => expect(changeCount()).toBe(0));
  test.do(() => expect(clickCount()).toBe(1));
  test.do(() => expect(dblclickCount()).toBe(0));
  test.do(() => expect(focusCount()).toBe(1));
  test.do(() => expect(blurCount()).toBe(0));
  test.event("dblclick", selectField, () => dblclickCount() >= 1);
  test.do(() => expect(changeCount()).toBe(0));
  test.do(() => expect(clickCount()).toBe(1));
  test.do(() => expect(dblclickCount()).toBe(1));
  test.do(() => expect(focusCount()).toBe(1));
  test.do(() => expect(blurCount()).toBe(0));
  test.event("focus", reset, () => blurCount() >= 1);
  test.do(() => expect(changeCount()).toBe(0));
  test.do(() => expect(clickCount()).toBe(1));
  test.do(() => expect(dblclickCount()).toBe(1));
  test.do(() => expect(focusCount()).toBe(1));
  test.do(() => expect(blurCount()).toBe(1));
  test.event("click", filterInput, () => clickCount() >= 2);
  test.do(() => expect(changeCount()).toBe(0));
  test.do(() => expect(clickCount()).toBe(2));
  test.do(() => expect(dblclickCount()).toBe(1));
  test.do(() => expect(focusCount()).toBe(2));
  test.do(() => expect(blurCount()).toBe(1));
  test.do(() => filterInput().value = "b");
  test.event("input", filterInput, () => tobagoSelectItems().length > 0);
  test.event("click", thebeSelectItem, () => badges().length > 0);
  test.do(() => expect(changeCount()).toBe(1));
  test.do(() => expect(clickCount()).toBe(3));
  test.do(() => expect(dblclickCount()).toBe(1));
  test.do(() => expect(focusCount()).toBe(2));
  test.do(() => expect(blurCount()).toBe(1));
  test.event("dblclick", selectField, () => dblclickCount() >= 2);
  test.do(() => expect(changeCount()).toBe(1));
  test.do(() => expect(clickCount()).toBe(3));
  test.do(() => expect(dblclickCount()).toBe(2));
  test.do(() => expect(focusCount()).toBe(2));
  test.do(() => expect(blurCount()).toBe(1));
  test.event("focus", reset, () => blurCount() >= 2);
  test.do(() => expect(changeCount()).toBe(1));
  test.do(() => expect(clickCount()).toBe(3));
  test.do(() => expect(dblclickCount()).toBe(2));
  test.do(() => expect(focusCount()).toBe(2));
  test.do(() => expect(blurCount()).toBe(2));
  test.start();
});

function currentTimestamp() {
  const timestampOut = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  return Number(timestampOut().textContent);
}

function changeCount() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:changeCounter .form-control-plaintext")().textContent);
}

function clickCount() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:clickCounter .form-control-plaintext")().textContent);
}

function dblclickCount() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:dblclickCounter .form-control-plaintext")().textContent);
}

function focusCount() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:focusCounter .form-control-plaintext")().textContent);
}

function blurCount() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:blurCounter .form-control-plaintext")().textContent);
}
