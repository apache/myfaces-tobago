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

import {expect, Locator, Page, test} from "@playwright/test";

test.describe("900-test/popup/static-backdrop/StaticBackdrop.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/popup/static-backdrop/StaticBackdrop.xhtml");
  });

  test("Open 'modal=false'-Popup, close it, press 'Submit'", async ({page}) => {
    const openButton = page.locator("[id='page:mainForm:showModalFalse']");
    const submit = page.locator("[id='page:mainForm:submit']");
    const wrapper = page.locator("[id='page:mainForm:popupWrapper']");
    const popup = page.locator("[id='page:mainForm:modalFalse']");
    const collapse = page.locator("[id='page:mainForm:modalFalse::collapse']");

    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");

    let promise = modalEventPromise(page, wrapper);
    await openButton.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "false");

    promise = modalEventPromise(page, wrapper);
    await page.mouse.click(0, 0); //click on backdrop
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");

    await submit.click();
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");
  });

  test("Open 'modal=false'-Popup, close it via ESC, press 'Submit'", async ({page}) => {
    const openButton = page.locator("[id='page:mainForm:showModalFalse']");
    const submit = page.locator("[id='page:mainForm:submit']");
    const wrapper = page.locator("[id='page:mainForm:popupWrapper']");
    const popup = page.locator("[id='page:mainForm:modalFalse']");
    const collapse = page.locator("[id='page:mainForm:modalFalse::collapse']");

    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");

    let promise = modalEventPromise(page, wrapper);
    await openButton.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "false");

    promise = modalEventPromise(page, wrapper);
    await page.keyboard.press("Escape");
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");

    await submit.click();
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");
  });

  test("Open 'modal=true'-Popup, close it, press 'Submit'", async ({page}) => {
    const body = page.locator("body");
    const openButton = page.locator("[id='page:mainForm:showModalTrue']");
    const submit = page.locator("[id='page:mainForm:submit']");
    const wrapper = page.locator("[id='page:mainForm:popupWrapper']");
    const popup = page.locator("[id='page:mainForm:modalTrue']");
    const collapse = page.locator("[id='page:mainForm:modalTrue::collapse']");
    const closeButton = page.locator("[id='page:mainForm:modalTrue:hideModalTrue']");

    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");

    let promise = modalEventPromise(page, wrapper);
    await openButton.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "false");

    await closeButton.click();
    await expect(body).not.toContainClass("modal-open");
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");

    await submit.click();
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");
  });

  test("Open 'modal=true'-Popup, try to close it via ESC", async ({page}) => {
    const body = page.locator("body");
    const openButton = page.locator("[id='page:mainForm:showModalTrue']");
    const wrapper = page.locator("[id='page:mainForm:popupWrapper']");
    const popup = page.locator("[id='page:mainForm:modalTrue']");
    const collapse = page.locator("[id='page:mainForm:modalTrue::collapse']");
    const closeButton = page.locator("[id='page:mainForm:modalTrue:hideModalTrue']");

    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");

    let promise = modalEventPromise(page, wrapper);
    await openButton.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "false");

    await page.keyboard.press("Escape");
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "false");

    //finally close via close button
    await closeButton.click();
    await expect(body).not.toContainClass("modal-open");
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");
  });

  test("Open Popup 3, close it, press 'Submit'", async ({page}) => {
    const openButton = page.locator("[id='page:mainForm:showPopup3']");
    const submit = page.locator("[id='page:mainForm:submit']");
    const wrapper = page.locator("[id='page:mainForm:popupWrapper']");
    const popup = page.locator("[id='page:mainForm:popup3']");
    const collapse = page.locator("[id='page:mainForm:popup3::collapse']");
    const popup3CollapsedOutput = page.locator("[id='page:mainForm:popup3Collapsed'] .form-control-plaintext");

    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");
    await expect(popup3CollapsedOutput).toHaveText("true");

    let promise = modalEventPromise(page, wrapper);
    await openButton.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "false");
    await expect(popup3CollapsedOutput).toHaveText("false");

    promise = modalEventPromise(page, wrapper);
    await page.mouse.click(0, 0); //click on backdrop
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");
    await expect(popup3CollapsedOutput).toHaveText("false");

    await submit.click();
    await expect(popup).not.toContainClass("show");
    await expect(collapse).toHaveAttribute("value", "true");
    await expect(popup3CollapsedOutput).toHaveText("true");
  });

  async function modalEventPromise(page: Page, locator: Locator) {
    const elementId = await locator.getAttribute("id");
    return page.evaluate((id) => {
      return new Promise(resolve => {
        const element = document.querySelector("[id='" + id + "']");
        element.addEventListener("shown.bs.modal", (event: CustomEvent) => {
          resolve("shown.bs.modal");
        }, {once: true});
        element.addEventListener("hidden.bs.modal", (event: CustomEvent) => {
          resolve("hidden.bs.modal");
        }, {once: true});
      });
    }, elementId) as Promise<any>;
  }
});
