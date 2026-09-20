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

test.describe("900-test/popup/jQuery/jQuery.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/popup/jQuery/jQuery.xhtml");
  });

  test("Open 'Client Popup' and press 'Close'.", async ({page}) => {
    let popup = page.locator("[id='page:mainForm:clientPopup']");
    let hiddenCollapseField = page.locator("[id='page:mainForm:clientPopup::collapse']");
    let openButton = page.locator("[id='page:mainForm:openClientPopup']");
    let closeButton = page.locator("[id='page:mainForm:clientPopup:closeClientPopup']");

    await expect(popup).not.toContainClass("show");
    await expect(hiddenCollapseField).toHaveAttribute("value", "true");

    let promise = modalEventPromise(page, popup);
    await openButton.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popup).toContainClass("show");
    await expect(hiddenCollapseField).toHaveAttribute("value", "false");

    promise = modalEventPromise(page, popup);
    await closeButton.click();
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popup).not.toContainClass("show");
    await expect(hiddenCollapseField).toHaveAttribute("value", "true");
  });

  test("Open 'Ajax Popup' and press 'Close'.", async ({page}) => {
    let popup = page.locator("[id='page:mainForm:ajaxPopup']");
    let hiddenCollapseField = page.locator("[id='page:mainForm:ajaxPopup::collapse']");
    let openButton = page.locator("[id='page:mainForm:openAjaxPopup']");
    let closeButton = page.locator("[id='page:mainForm:ajaxPopup:closeAjaxPopup']");

    await expect(popup).not.toContainClass("show");
    await expect(hiddenCollapseField).toHaveAttribute("value", "true");

    await openButton.click();
    await expect(popup).toContainClass("show");
    await expect(hiddenCollapseField).toHaveAttribute("value", "false");

    await closeButton.click();
    await expect(popup).not.toContainClass("show");
    await expect(hiddenCollapseField).toHaveAttribute("value", "true");
  });

  test("Open 'Server Popup' and press 'Close'.", async ({page}) => {
    let popup = page.locator("[id='page:mainForm:serverPopup']");
    let hiddenCollapseField = page.locator("[id='page:mainForm:serverPopup::collapse']");
    let openButton = page.locator("[id='page:mainForm:openServerPopup']");
    let closeButton = page.locator("[id='page:mainForm:serverPopup:closeServerPopup']");

    await expect(popup).not.toContainClass("show");
    await expect(hiddenCollapseField).toHaveAttribute("value", "true");

    await openButton.click();
    await expect(popup).toContainClass("show");
    await expect(hiddenCollapseField).toHaveAttribute("value", "false");

    await closeButton.click();
    await expect(popup).not.toContainClass("show");
    await expect(hiddenCollapseField).toHaveAttribute("value", "true");
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
