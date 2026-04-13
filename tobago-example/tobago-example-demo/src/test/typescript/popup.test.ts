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

import {expect, test} from "@playwright/test";

test.describe("130-collapsible/10-collapsible-popup/Collapsible_Popup.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/130-collapsible/10-collapsible-popup/Collapsible_Popup.xhtml");
  });

  test("Simple popup", async ({page, browserName}) => {
    const messages = page.locator("[id='page:messages'] .alert");
    const open = page.locator("[id='page:mainForm:simple:open1']");
    const submitOnPage = page.locator("[id='page:mainForm:simple:submitOnPage1']");
    const popup = page.locator("[id='page:mainForm:simple:simplePopup']");
    const input = page.locator("[id='page:mainForm:simple:simplePopup:in1::field']");
    const out = page.locator("[id='page:mainForm:simple:simplePopup:out1'] .form-control-plaintext");
    const submitOnPopup = page.locator("[id='page:mainForm:simple:simplePopup:submitOnPopup1']");
    const close = page.locator("[id='page:mainForm:simple:simplePopup:close1']");

    const openClientPopup = page.locator("button[id='page:mainForm:client:open3']");
    const clientPopupInput = page.locator("input[id='page:mainForm:client:clientPopup:in3::field']");
    const clientPopupSubmit = page.locator("button[id='page:mainForm:client:clientPopup:submitOnPopup3']");

    const date = new Date().toString();

    if (await popup.evaluate(e => e.classList.contains("show"))) {
      await close.click();
      await expect(popup).not.toContainClass("show");
    }

    await open.click();
    await expect(popup).toContainClass("show");
    await expect(input).toBeVisible();

    await input.fill(date);
    await submitOnPopup.click();
    await expect(messages).toHaveCount(0);
    await expect(input).toBeVisible();
    await expect(input).toHaveValue(date);
    await expect(out).toHaveText(date);

    await input.fill("");
    await submitOnPopup.click();
    await expect(messages).toHaveCount(1);
    await expect(input).toBeVisible();
    await expect(input).toHaveValue("");
    await expect(out).toHaveText(date);

    await close.click();
    await expect(popup).not.toContainClass("show");
    await expect(input).not.toBeVisible();

    //add an error message for the next step
    await openClientPopup.click();
    await clientPopupInput.fill("");
    await clientPopupSubmit.click();
    await expect(messages).toHaveCount(1);

    await submitOnPage.click();
    await expect(messages).toHaveCount(0);
  });

  test("Full server request", async ({page, browserName}) => {
    const messages = page.locator("[id='page:messages'] .alert");
    const open = page.locator("[id='page:mainForm:server:open2']");
    const submitOnPage = page.locator("[id='page:mainForm:server:submitOnPage2']");
    const popup = page.locator("[id='page:mainForm:server:serverPopup']");
    const input = page.locator("[id='page:mainForm:server:serverPopup:in2::field']");
    const out = page.locator("[id='page:mainForm:server:serverPopup:out2'] .form-control-plaintext");
    const submitOnPopup = page.locator("[id='page:mainForm:server:serverPopup:submitOnPopup2']");
    const close = page.locator("[id='page:mainForm:server:serverPopup:close2']");

    const openClientPopup = page.locator("button[id='page:mainForm:client:open3']");
    const clientPopupInput = page.locator("input[id='page:mainForm:client:clientPopup:in3::field']");
    const clientPopupSubmit = page.locator("button[id='page:mainForm:client:clientPopup:submitOnPopup3']");

    const date = new Date().toString();

    if (await popup.evaluate(e => e.classList.contains("show"))) {
      await close.click();
      await expect(popup).not.toContainClass("show");
    }
    await open.click();
    await expect(popup).toContainClass("show");
    await expect(input).toBeVisible();

    await input.fill(date);
    await submitOnPopup.click();
    await expect(messages).toHaveCount(0);
    await expect(input).toBeVisible();
    await expect(input).toHaveValue(date);
    await expect(out).toHaveText(date);

    await input.fill("");
    await submitOnPopup.click();
    await expect(messages).toHaveCount(1);
    await expect(input).toBeVisible();
    await expect(input).toHaveValue("");
    await expect(out).toHaveText(date);

    await close.click();
    await expect(popup).not.toContainClass("show");
    await expect(input).not.toBeVisible();

    //add an error message for the next step
    await openClientPopup.click();
    await clientPopupInput.fill("");
    await clientPopupSubmit.click();
    await expect(messages).toHaveCount(1);

    await submitOnPage.click();
    await expect(messages).toHaveCount(0);
  });

  test("Client side", async ({page, browserName}) => {
    const messages = page.locator("[id='page:messages'] .alert");
    const open = page.locator("[id='page:mainForm:client:open3']");
    const submitOnPage = page.locator("[id='page:mainForm:client:submitOnPage3']");
    const popup = page.locator("[id='page:mainForm:client:clientPopup']");
    const collapse = page.locator("input[id='page:mainForm:client:clientPopup::collapse']");
    const input = page.locator("[id='page:mainForm:client:clientPopup:in3::field']");
    const out = page.locator("[id='page:mainForm:client:clientPopup:out3'] .form-control-plaintext");
    const submitOnPopup = page.locator("[id='page:mainForm:client:clientPopup:submitOnPopup3']");
    const close = page.locator("[id='page:mainForm:client:clientPopup:close3']");

    const simpleSubmitOnPage = page.locator("[id='page:mainForm:simple:submitOnPage1']");

    const date = new Date().toString();

    if (await popup.evaluate(e => e.classList.contains("show"))) {
      await close.click();
      await expect(popup).not.toContainClass("show");
      await expect(collapse).toHaveValue("true");
    }

    await open.click();
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveValue("false");
    await expect(input).toBeVisible();

    await close.click();
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveValue("true");
    await expect(input).not.toBeVisible();

    await open.click();
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveValue("false");
    await expect(input).toBeVisible();

    await input.fill(date);
    await submitOnPopup.click();
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveValue("true");
    await expect(input).not.toBeVisible();
    await expect(messages).toHaveCount(0);
    await expect(input).toHaveValue(date);
    await expect(out).toHaveText(date);

    await open.click();
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveValue("false");
    await expect(input).toBeVisible();

    await input.fill("");
    await submitOnPopup.click();
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveValue("true");
    await expect(input).not.toBeVisible();
    await expect(messages).toHaveCount(1);
    await expect(input).toHaveValue("");
    await expect(out).toHaveText(date);

    //remove error message for the next step
    await simpleSubmitOnPage.click();
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveValue("true");
    await expect(input).not.toBeVisible();
    await expect(messages).toHaveCount(0);

    await submitOnPage.click();
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveValue("true");
    await expect(input).not.toBeVisible();
    await expect(messages).toHaveCount(1);
  });
});

test.describe("900-test/popup/dropdown/Dropdown.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/popup/dropdown/Dropdown.xhtml");
  });

  test("Tabbing the popup", async ({page, browserName}) => {
    const popupOpen = page.locator("[id='page:mainForm:popup-open']");
    const popup = page.locator("[id='page:mainForm:popup']");
    const popupModalDialog = popup.locator(".modal-dialog");
    const popupX = page.locator("[id='page:mainForm:popup:popup-x']");
    const suggestInputField = page.locator("[id='page:mainForm:popup:inSuggest::field']");
    const suggestDropdownMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:popup:suggest']");
    const mars = suggestDropdownMenu.locator("li[data-result-index='0']");
    const amalthea = suggestDropdownMenu.locator("li[data-result-index='1']");
    const himalia = suggestDropdownMenu.locator("li[data-result-index='2']");
    const mimas = suggestDropdownMenu.locator("li[data-result-index='3']");
    const selectOneList = page.locator("[id='page:mainForm:popup:selectOneList']");
    const selectOneListMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:popup:selectOneList']");
    const selectOneListMercuryRow = selectOneListMenu.locator("tr[data-tobago-value='Mercury']");
    const selectOneListPlutoRow = selectOneListMenu.locator("tr[data-tobago-value='Pluto']");
    const selectManyList = page.locator("[id='page:mainForm:popup:selectManyList']");
    const selectManyListMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:popup:selectManyList']");
    const selectManyListMercuryRow = selectManyListMenu.locator("tr[data-tobago-value='Mercury']");
    const selectManyListPlutoRow = selectManyListMenu.locator("tr[data-tobago-value='Pluto']");
    const musicPlayerButton = page.locator("[id='page:mainForm:popup:musicPlayerButton::command']");
    const play = page.locator("[id='page:mainForm:popup:play']");
    const track = page.locator("[id='page:mainForm:popup:track::command']");
    const popupClose = page.locator("[id='page:mainForm:popup:popup-close']");

    await expect(popup).not.toContainClass("show");
    await popupOpen.click();
    await expect(popupModalDialog).not.toHaveCSS("transform", "none"); //animation
    await expect(popupModalDialog).toHaveCSS("transform", "none"); //animation done
    await expect(popup).toContainClass("show");
    await page.keyboard.press("Tab");
    await expect(popupX).toBeFocused();

    //suggest
    await page.keyboard.press("Tab");
    await expect(suggestInputField).toBeFocused();
    await page.keyboard.press("m");
    await page.keyboard.press("a");
    await expect(mars).toBeVisible();
    await expect(amalthea).toBeVisible();
    await expect(himalia).toBeVisible();
    await expect(mimas).toBeVisible();
    await page.keyboard.press("Tab");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.press("Escape");
    await expect(mars).not.toBeVisible();
    await expect(amalthea).not.toBeVisible();
    await expect(himalia).not.toBeVisible();
    await expect(mimas).not.toBeVisible();
    await expect(popup).toContainClass("show");

    //selectOneList
    await expect(selectOneList).not.toContainClass("tobago-focus");
    await page.keyboard.press("Tab");
    await expect(selectOneList).toContainClass("tobago-focus");
    await page.keyboard.press("Enter");
    await expect(selectOneListMercuryRow).toBeVisible();
    await expect(selectOneListMercuryRow).not.toContainClass("tobago-preselect");
    await page.keyboard.press("Tab");
    await expect(selectOneListMercuryRow).toContainClass("tobago-preselect");
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(selectOneListMercuryRow).not.toContainClass("tobago-preselect");
    await expect(selectOneListPlutoRow).toBeVisible();
    await expect(selectOneListPlutoRow).toContainClass("tobago-preselect");
    await page.keyboard.press("Escape");
    await expect(selectOneListMercuryRow).not.toBeVisible();
    await expect(selectOneListPlutoRow).not.toBeVisible();

    //selectManyList
    await expect(selectManyList).not.toContainClass("tobago-focus");
    await page.keyboard.press("Tab");
    await expect(selectManyList).toContainClass("tobago-focus");
    await page.keyboard.press("Enter");
    await expect(selectManyListMercuryRow).toBeVisible();
    await expect(selectManyListMercuryRow).not.toContainClass("tobago-preselect");
    await page.keyboard.press("Tab");
    await expect(selectManyListMercuryRow).toContainClass("tobago-preselect");
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(selectManyListMercuryRow).not.toContainClass("tobago-preselect");
    await expect(selectManyListPlutoRow).toBeVisible();
    await expect(selectManyListPlutoRow).toContainClass("tobago-preselect");
    await page.keyboard.press("Escape");
    await expect(selectManyListMercuryRow).not.toBeVisible();
    await expect(selectManyListPlutoRow).not.toBeVisible();

    //MusicPlayer dropdown menu
    await expect(musicPlayerButton).not.toBeFocused();
    await page.keyboard.press("Tab");
    await expect(musicPlayerButton).toBeFocused();
    await page.keyboard.press("Enter");
    await expect(play).toBeVisible();
    await expect(play).not.toBeFocused();
    await page.keyboard.press("Tab");
    await expect(play).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(track).toBeVisible();
    await expect(track).toBeFocused();
    await page.keyboard.press("Escape");
    await expect(play).not.toBeVisible();
    await expect(track).not.toBeVisible();

    await page.keyboard.press("Tab");
    await expect(popupClose).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(popupX).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(popupClose).toBeFocused();

    await expect(popup).toContainClass("show");
    await page.keyboard.press("Escape");
    await expect(popup).not.toContainClass("show");
    await popupOpen.click();
    await expect(popupModalDialog).not.toHaveCSS("transform", "none"); //animation
    await expect(popupModalDialog).toHaveCSS("transform", "none"); //animation done
    await expect(popup).toContainClass("show");
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(popupClose).toBeFocused();
  });
});
