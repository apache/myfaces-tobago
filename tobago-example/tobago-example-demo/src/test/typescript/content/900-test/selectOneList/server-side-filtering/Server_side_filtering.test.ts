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

import {expect, test} from "@playwright/test";

test.describe("900-test/selectOneList/server-side-filtering/Server_side_filtering.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectOneList/server-side-filtering/Server_side_filtering.xhtml");
  });

  test("SelectOneList: margin-right of span before filter input", async ({page}) => {
    const span = page.locator("[id='page:mainForm:selectOneList::selectField'] span");
    const filterInput = page.locator("[id='page:mainForm:selectOneList::filter']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList']");
    const earthSelectItem = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-select-item[data-tobago-value='Earth']");
    const customFooter = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-custom-footer");
    const output = page.locator("[id='page:mainForm:outputSelectOneList'] .form-control-plaintext");

    await expect(span).not.toBeVisible();
    await expect(span).toHaveCSS("margin-right", "0px");
    await expect(dropdownMenu).not.toContainClass("show");
    await expect(customFooter).toHaveText("filter for results");
    await filterInput.fill("a");
    await expect(dropdownMenu).toContainClass("show");
    await expect(customFooter).toHaveText("there are more results");
    await earthSelectItem.click();
    await expect(span).toHaveText("Earth");
    await expect(output).toHaveText("Earth");
    await expect(span).not.toHaveCSS("margin-right", "0px");
    await expect(dropdownMenu).not.toContainClass("show");
  });

  test("SelectOneList minChar=3: leaving component", async ({page}) => {
    const minChar3 = page.locator("[id='page:mainForm:minChar3']");
    const span = page.locator("[id='page:mainForm:selectOneList::selectField'] span");
    const filterInput = page.locator("[id='page:mainForm:selectOneList::filter']");
    const tobagoSelectItems = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-select-item");
    const rheaSelectItem = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-select-item[data-tobago-value='Rhea']");
    const customFooter = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-custom-footer");
    const customFooterCell = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-custom-footer td");
    const output = page.locator("[id='page:mainForm:outputSelectOneList'] .form-control-plaintext");

    await minChar3.click();
    await expect(tobagoSelectItems).toHaveCount(0);
    await expect(customFooter).toHaveText("type 3 character for results");
    await filterInput.fill("hea");
    await expect(tobagoSelectItems).toHaveCount(3);
    await expect(customFooterCell).toHaveCSS("display", "none");
    await rheaSelectItem.click();
    await expect(span).toHaveText("Rhea");
    await expect(output).toHaveText("Rhea");
    await expect(tobagoSelectItems).toHaveCount(3);
    await minChar3.focus(); //blur event on filterInput
    await expect(tobagoSelectItems).toHaveCount(0);
  });

  test("SelectOneList: avoid 'no valid selection' error", async ({page}) => {
    const reset = page.locator("[id='page:mainForm:reset']");
    const span = page.locator("[id='page:mainForm:selectOneList::selectField'] span");
    const filterInput = page.locator("[id='page:mainForm:selectOneList::filter']");
    const phobosSelectItem = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-select-item[data-tobago-value='Phobos']");
    const popover = page.locator("tobago-select-one-list[id='page:mainForm:selectOneList'] tobago-popover");
    const output = page.locator("[id='page:mainForm:outputSelectOneList'] .form-control-plaintext");

    await expect(span).not.toBeVisible();
    await filterInput.fill("p");
    await phobosSelectItem.click();
    await expect(span).toHaveText("Phobos");
    await expect(output).toHaveText("Phobos");
    await reset.click();
    await expect(span).toHaveText("");
    await expect(output).toHaveText("");
    await expect(popover).not.toBeVisible();
  });

  test("SelectOneList: footer must be visible for an empty list", async ({page}) => {
    const selectField = page.locator("[id='page:mainForm:selectOneList::selectField']");
    const span = page.locator("[id='page:mainForm:selectOneList::selectField'] span");
    const tobagoDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList']");
    const tobagoSelectItems = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-select-item");
    const customFooter = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-custom-footer");

    await expect(span).not.toBeVisible();
    await expect(tobagoDropdownMenu).not.toContainClass("show");
    await expect(tobagoSelectItems).toHaveCount(0);
    await selectField.click();
    await expect(span).not.toBeVisible();
    await expect(tobagoDropdownMenu).toContainClass("show");
    await expect(tobagoSelectItems).toHaveCount(0);
    await expect(customFooter).toHaveText("filter for results");
  });

  test("SelectOneList: ajax listener", async ({page}) => {
    const reset = page.locator("[id='page:mainForm:reset']");
    const changeCounter = page.locator("[id='page:mainForm:changeCounter'] .form-control-plaintext");
    const clickCounter = page.locator("[id='page:mainForm:clickCounter'] .form-control-plaintext");
    const dblclickCounter = page.locator("[id='page:mainForm:dblclickCounter'] .form-control-plaintext");
    const focusCounter = page.locator("[id='page:mainForm:focusCounter'] .form-control-plaintext");
    const blurCounter = page.locator("[id='page:mainForm:blurCounter'] .form-control-plaintext");
    const selectField = page.locator("[id='page:mainForm:selectOneList::selectField']");
    const span = page.locator("[id='page:mainForm:selectOneList::selectField'] span");
    const filterInput = page.locator("[id='page:mainForm:selectOneList::filter']");
    const venusSelectItem = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList'] .tobago-select-item[data-tobago-value='Venus']");

    await selectField.click();
    await expect(changeCounter).toHaveText("0");
    await expect(clickCounter).toHaveText("1");
    await expect(dblclickCounter).toHaveText("0");
    await expect(focusCounter).toHaveText("1");
    await expect(blurCounter).toHaveText("0");
    await selectField.dblclick();
    await expect(changeCounter).toHaveText("0");
    await expect(clickCounter).toHaveText("3");
    await expect(dblclickCounter).toHaveText("1");
    await expect(focusCounter).toHaveText("1");
    await expect(blurCounter).toHaveText("0");
    await reset.focus();
    await expect(changeCounter).toHaveText("0");
    await expect(clickCounter).toHaveText("3");
    await expect(dblclickCounter).toHaveText("1");
    await expect(focusCounter).toHaveText("1");
    await expect(blurCounter).toHaveText("1");
    await filterInput.click();
    await expect(changeCounter).toHaveText("0");
    await expect(clickCounter).toHaveText("4");
    await expect(dblclickCounter).toHaveText("1");
    await expect(focusCounter).toHaveText("2");
    await expect(blurCounter).toHaveText("1");
    await filterInput.fill("v");
    await expect(venusSelectItem).toBeVisible();
    await venusSelectItem.click();
    await expect(span).toHaveText("Venus");
    await expect(changeCounter).toHaveText("1");
    await expect(clickCounter).toHaveText("5");
    await expect(dblclickCounter).toHaveText("1");
    await expect(focusCounter).toHaveText("2");
    await expect(blurCounter).toHaveText("1");
    await selectField.dblclick();
    await expect(changeCounter).toHaveText("1");
    await expect(clickCounter).toHaveText("7");
    await expect(dblclickCounter).toHaveText("2");
    await expect(focusCounter).toHaveText("2");
    await expect(blurCounter).toHaveText("1");
    await reset.focus();
    await expect(changeCounter).toHaveText("1");
    await expect(clickCounter).toHaveText("7");
    await expect(dblclickCounter).toHaveText("2");
    await expect(focusCounter).toHaveText("2");
    await expect(blurCounter).toHaveText("2");
  });
});
