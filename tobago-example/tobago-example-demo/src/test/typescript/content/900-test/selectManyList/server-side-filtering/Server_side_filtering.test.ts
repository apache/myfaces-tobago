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

test.describe("900-test/selectManyList/server-side-filtering/Server_side_filtering.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectManyList/server-side-filtering/Server_side_filtering.xhtml");
  });

  test("SelectManyList minChar=3: leaving component", async ({page}) => {
    const minChar3 = page.locator("[id='page:mainForm:minChar3']");
    const badges = page.locator("div[id='page:mainForm:selectManyList::selectField'] > .tobago-badges > .btn-group");
    const filterInput = page.locator("[id='page:mainForm:selectManyList::filter']");
    const tobagoSelectItems = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item");
    const rheaSelectItem = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item[data-tobago-value='Rhea']");
    const customFooter = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-custom-footer");
    const customFooterCell = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-custom-footer td");
    const output = page.locator("[id='page:mainForm:outputSelectManyList'] .form-control-plaintext");

    await minChar3.click();
    await expect(tobagoSelectItems).toHaveCount(0);
    await expect(customFooter).toHaveText("type 3 character for results");
    await filterInput.fill("hea");
    await expect(tobagoSelectItems).toHaveCount(3);
    await expect(customFooterCell).toHaveCSS("display", "none");
    await rheaSelectItem.click();
    await expect(badges).toHaveCount(1);
    await expect(badges.nth(0)).toHaveAttribute("data-tobago-value", "Rhea");
    await expect(output).toHaveText("Rhea;");
    await expect(tobagoSelectItems).toHaveCount(3);
    await minChar3.focus(); //blur event on filterInput
    await expect(tobagoSelectItems).toHaveCount(0);
  });

  test("SelectManyList: avoid 'no valid selection' error", async ({page}) => {
    const reset = page.locator("[id='page:mainForm:reset']");
    const badges = page.locator("div[id='page:mainForm:selectManyList::selectField'] > .tobago-badges > .btn-group");
    const filterInput = page.locator("[id='page:mainForm:selectManyList::filter']");
    const lysitheaSelectItem = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item[data-tobago-value='Lysithea']");
    const popover = page.locator("tobago-select-many-list[id='page:mainForm:selectManyList'] tobago-popover");
    const output = page.locator("[id='page:mainForm:outputSelectManyList'] .form-control-plaintext");

    await filterInput.fill("y");
    await lysitheaSelectItem.click();
    await expect(badges).not.toHaveCount(0);
    await expect(badges.nth(0)).toHaveText("Lysithea");
    await expect(output).toHaveText("Lysithea;");
    await reset.click();
    await expect(badges).toHaveCount(0);
    await expect(output).toHaveText("");
    await expect(popover).not.toBeVisible();
  });

  test("SelectManyList: footer must be visible for an empty list", async ({page}) => {
    const selectField = page.locator("[id='page:mainForm:selectManyList::selectField']");
    const badges = page.locator("div[id='page:mainForm:selectManyList::selectField'] > .tobago-badges > .btn-group");
    const tobagoDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList']");
    const tobagoSelectItems = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item");
    const customFooter = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-custom-footer");

    await expect(badges).toHaveCount(0);
    await expect(tobagoDropdownMenu).not.toContainClass("show");
    await expect(tobagoSelectItems).toHaveCount(0);
    await selectField.scrollIntoViewIfNeeded();
    await selectField.click();
    await expect(badges).toHaveCount(0);
    await expect(tobagoDropdownMenu).toContainClass("show");
    await expect(tobagoSelectItems).toHaveCount(0);
    await expect(customFooter).toHaveText("filter for results");
  });

  test("SelectManyList: ajax listener", async ({page}) => {
    const reset = page.locator("[id='page:mainForm:reset']");
    const changeCounter = page.locator("[id='page:mainForm:changeCounter'] .form-control-plaintext");
    const clickCounter = page.locator("[id='page:mainForm:clickCounter'] .form-control-plaintext");
    const dblclickCounter = page.locator("[id='page:mainForm:dblclickCounter'] .form-control-plaintext");
    const focusCounter = page.locator("[id='page:mainForm:focusCounter'] .form-control-plaintext");
    const blurCounter = page.locator("[id='page:mainForm:blurCounter'] .form-control-plaintext");
    const selectField = page.locator("[id='page:mainForm:selectManyList::selectField']");
    const badges = page.locator("div[id='page:mainForm:selectManyList::selectField'] > .tobago-badges > .btn-group");
    const filterInput = page.locator("[id='page:mainForm:selectManyList::filter']");
    const thebeSelectItem = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item[data-tobago-value='Thebe']");
    const output = page.locator("[id='page:mainForm:outputSelectManyList'] .form-control-plaintext");

    await selectField.click();
    await expect(changeCounter).toHaveText("0");
    await expect(clickCounter).toHaveText("1");
    await expect(dblclickCounter).toHaveText("0");
    await expect(focusCounter).toHaveText("1");
    await expect(blurCounter).toHaveText("0");
    await selectField.dblclick();
    await expect(changeCounter).toHaveText("0");
    await expect(clickCounter).toHaveText("2"); //increase by one instead of two, because Ajax queue blocks a second identical click event request
    await expect(dblclickCounter).toHaveText("1");
    await expect(focusCounter).toHaveText("1");
    await expect(blurCounter).toHaveText("0");
    await reset.focus();
    await expect(changeCounter).toHaveText("0");
    await expect(clickCounter).toHaveText("2");
    await expect(dblclickCounter).toHaveText("1");
    await expect(focusCounter).toHaveText("1");
    await expect(blurCounter).toHaveText("1");
    await filterInput.click();
    await expect(changeCounter).toHaveText("0");
    await expect(clickCounter).toHaveText("3");
    await expect(dblclickCounter).toHaveText("1");
    await expect(focusCounter).toHaveText("2");
    await expect(blurCounter).toHaveText("1");
    await filterInput.fill("b");
    await expect(thebeSelectItem).toBeVisible();
    await thebeSelectItem.click();
    await expect(output).toHaveText("Thebe;");
    await expect(badges).not.toHaveCount(0);
    await expect(changeCounter).toHaveText("1");
    await expect(clickCounter).toHaveText("4");
    await expect(dblclickCounter).toHaveText("1");
    await expect(focusCounter).toHaveText("2");
    await expect(blurCounter).toHaveText("1");
    await selectField.dblclick();
    await expect(changeCounter).toHaveText("1");
    await expect(clickCounter).toHaveText("5");  //increase by one instead of two, because Ajax queue blocks a second identical click event request
    await expect(dblclickCounter).toHaveText("2");
    await expect(focusCounter).toHaveText("2");
    await expect(blurCounter).toHaveText("1");
    await reset.focus();
    await expect(changeCounter).toHaveText("1");
    await expect(clickCounter).toHaveText("5");
    await expect(dblclickCounter).toHaveText("2");
    await expect(focusCounter).toHaveText("2");
    await expect(blurCounter).toHaveText("2");
  });
});
