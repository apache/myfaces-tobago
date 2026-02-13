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

test.describe("010-input/50-input-group/Group.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/010-input/50-input-group/Group.xhtml");
  });

  test("ajax: chat send button", async ({page}) => {
    const chatlogFn = page.locator("[id='page:mainForm:tachatlog::field']");
    const inputFn = page.locator("[id='page:mainForm:inewmessage::field']");
    const sendButtonFn = page.locator("[id='page:mainForm:sendButton']");

    await inputFn.fill("delete chat");
    await sendButtonFn.click();
    await expect(chatlogFn).toHaveText("");
    await inputFn.fill("Hi Peter, how are you?");
    await sendButtonFn.click();
    await expect(chatlogFn).toHaveText("User Two: Hi Peter, how are you?");
  });

  test("ajax: dropdown button", async ({page}) => {
    const buttonFn = page.locator("[id='page:mainForm:lsendtoc::command']");
    const buttonLabelFn = page.locator("[id='page:mainForm:lsendtoc::command'] span");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:lsendtoc']");
    const sendToPeterFn = page.locator("[id='page:mainForm:sendToPeter']");
    const sendToBobFn = page.locator("[id='page:mainForm:sendToBob']");
    const sendToAllFn = page.locator("[id='page:mainForm:sendToAll']");

    await buttonFn.scrollIntoViewIfNeeded();
    await buttonFn.click();
    await expect(sendToAllFn).toBeVisible();
    await sendToAllFn.click();
    await expect(buttonLabelFn).toHaveText("SendTo: All");

    await buttonFn.scrollIntoViewIfNeeded();
    await buttonFn.click();
    await expect(dropdownMenu.locator("..")).toContainClass("tobago-page-menuStore");
    await sendToPeterFn.click();
    await expect(dropdownMenu.locator("..")).not.toContainClass("tobago-page-menuStore");
    await expect(buttonLabelFn).toHaveText("SendTo: Peter");

    await buttonFn.scrollIntoViewIfNeeded();
    await buttonFn.click();
    await expect(dropdownMenu.locator("..")).toContainClass("tobago-page-menuStore");
    await sendToBobFn.click();
    await expect(dropdownMenu.locator("..")).not.toContainClass("tobago-page-menuStore");
    await expect(buttonLabelFn).toHaveText("SendTo: Bob");

    await buttonFn.scrollIntoViewIfNeeded();
    await buttonFn.click();
    await expect(dropdownMenu.locator("..")).toContainClass("tobago-page-menuStore");
    await sendToAllFn.click();
    await expect(dropdownMenu.locator("..")).not.toContainClass("tobago-page-menuStore");
    await expect(buttonLabelFn).toHaveText("SendTo: All");

  });

  test("ajax: currency change event", async ({page}) => {
    const inputFn = page.locator("[id='page:mainForm:value::field']");
    const selectFn = page.locator("[id='page:mainForm:currency::field']");
    const optionsFn = page.locator("[id='page:mainForm:currency'] option");
    const outputFn = page.locator("[id='page:mainForm:valueInEuro'] .form-control-plaintext");

    await inputFn.scrollIntoViewIfNeeded();
    await inputFn.focus();
    await expect(inputFn).toBeFocused();
    await inputFn.fill("0");
    await selectFn.click();
    await selectFn.selectOption({index: 3}); //Euro
    await expect(outputFn).toHaveText(/0[.,]00/);

    await inputFn.focus();
    await expect(inputFn).toBeFocused();
    await inputFn.fill("1000");
    await selectFn.selectOption({index: 0}); //Yen
    await expect(outputFn).toHaveText(/8[.,]85/);

    await inputFn.focus();
    await expect(inputFn).toBeFocused();
    await inputFn.fill("2000");
    await selectFn.selectOption({index: 1}); //Trinidad-Tobago Dollar
    await expect(outputFn).toHaveText(/267[.,]50/);

    await inputFn.focus();
    await expect(inputFn).toBeFocused();
    await inputFn.fill("3000");
    await selectFn.selectOption({index: 2}); //US Dollar
    await expect(outputFn).toHaveText(/2[.,]688[.,]29/);
  });

  /**
   * need this function, because chrome displays "1.000,00" and firefox displays "1,000.00"
   */
  function convertInt(string) {
    return parseInt(string.replaceAll(",", "").replaceAll(".", ""));
  }
});
