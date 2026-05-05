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

test.describe("900-test/popup/Popup.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/popup/Popup.xhtml");
  });

  test("Open 'Client Popup' and press 'Cancel'.", async ({page}) => {
    let popupFn = page.locator("[id='page:mainForm:form2:clientPopup']");
    let collapseFn = page.locator("[id='page:mainForm:form2:clientPopup::collapse']");
    let openButtonFn = page.locator("[id='page:mainForm:form2:open']");
    let cancelButtonFn = page.locator("[id='page:mainForm:form2:clientPopup:cancel2']");

    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");

    let promise = modalEventPromise(page, popupFn);
    await openButtonFn.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popupFn).toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "false");

    promise = modalEventPromise(page, popupFn);
    await cancelButtonFn.click();
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
  });

  test("Open 'Client Popup', press 'Submit' while field is empty. Press 'Cancel'.", async ({page}) => {
    let popupFn = page.locator("[id='page:mainForm:form2:clientPopup']");
    let collapseFn = page.locator("[id='page:mainForm:form2:clientPopup::collapse']");
    let openButtonFn = page.locator("[id='page:mainForm:form2:open']");
    let outputFn = page.locator("[id='page:mainForm:form2:out'] input");
    let messagesFn = page.locator("[id='page:mainForm:form2:clientPopup:messages'] div");
    let inputFieldFn = page.locator("[id='page:mainForm:form2:clientPopup:in2::field']");
    let submitButtonFn = page.locator("[id='page:mainForm:form2:clientPopup:submit2']");
    let cancelButtonFn = page.locator("[id='page:mainForm:form2:clientPopup:cancel2']");

    await expect(outputFn).toHaveValue("");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(messagesFn).toHaveCount(0);

    let promise = modalEventPromise(page, popupFn);
    await openButtonFn.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popupFn).toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "false");

    await inputFieldFn.fill("");
    await submitButtonFn.click();
    await expect(messagesFn).toHaveCount(1);

    promise = modalEventPromise(page, popupFn);
    await cancelButtonFn.click();
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(outputFn).toHaveValue("");
  });

  test("Open 'Client Popup', press 'Submit' while field has content. Press 'Cancel'.", async ({page}) => {
    let popupFn = page.locator("[id='page:mainForm:form2:clientPopup']");
    let collapseFn = page.locator("[id='page:mainForm:form2:clientPopup::collapse']");
    let openButtonFn = page.locator("[id='page:mainForm:form2:open']");
    let outputFn = page.locator("[id='page:mainForm:form2:out'] input");
    let messagesFn = page.locator("[id='page:mainForm:form2:clientPopup:messages'] div");
    let inputFieldFn = page.locator("[id='page:mainForm:form2:clientPopup:in2::field']");
    let submitButtonFn = page.locator("[id='page:mainForm:form2:clientPopup:submit2']");
    let cancelButtonFn = page.locator("[id='page:mainForm:form2:clientPopup:cancel2']");

    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(outputFn).not.toHaveValue("Tobago");

    let promise = modalEventPromise(page, popupFn);
    await openButtonFn.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popupFn).toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "false");

    await inputFieldFn.fill("Tobago");
    await submitButtonFn.click();
    await expect(outputFn).toHaveValue("Tobago");

    promise = modalEventPromise(page, popupFn);
    await cancelButtonFn.click();
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(messagesFn).toHaveCount(0);
  });

  test("Open 'Client Popup', press 'Submit & Close' while field is empty.", async ({page}) => {
    let popupFn = page.locator("[id='page:mainForm:form2:clientPopup']");
    let collapseFn = page.locator("[id='page:mainForm:form2:clientPopup::collapse']");
    let openButtonFn = page.locator("[id='page:mainForm:form2:open']");
    let outputFn = page.locator("[id='page:mainForm:form2:out'] input");
    let messagesFn = page.locator("[id='page:mainForm:form2:clientPopup:messages'] div");
    let inputFieldFn = page.locator("[id='page:mainForm:form2:clientPopup:in2::field']");
    let submitCloseButtonFn = page.locator("[id='page:mainForm:form2:clientPopup:submitClose2']");

    await expect(outputFn).toHaveValue("");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(messagesFn).toHaveCount(0);

    let promise = modalEventPromise(page, popupFn);
    await openButtonFn.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popupFn).toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "false");

    await inputFieldFn.fill("");
    promise = modalEventPromise(page, popupFn);
    await submitCloseButtonFn.click();
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(messagesFn).toHaveCount(1);
    await expect(outputFn).toHaveValue("");
  });

  test("Open 'Client Popup', press 'Submit & Close' while field has content.", async ({page}) => {
    let popupFn = page.locator("[id='page:mainForm:form2:clientPopup']");
    let collapseFn = page.locator("[id='page:mainForm:form2:clientPopup::collapse']");
    let openButtonFn = page.locator("[id='page:mainForm:form2:open']");
    let outputFn = page.locator("[id='page:mainForm:form2:out'] input");
    let messagesFn = page.locator("[id='page:mainForm:form2:clientPopup:messages'] div");
    let inputFieldFn = page.locator("[id='page:mainForm:form2:clientPopup:in2::field']");
    let submitCloseButtonFn = page.locator("[id='page:mainForm:form2:clientPopup:submitClose2']");

    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(outputFn).not.toHaveValue("Little Tobago");

    let promise = modalEventPromise(page, popupFn);
    await openButtonFn.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popupFn).toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "false");

    await inputFieldFn.fill("Little Tobago");
    promise = modalEventPromise(page, popupFn);
    await submitCloseButtonFn.click();
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(messagesFn).toHaveCount(0);
    await expect(outputFn).toHaveValue("Little Tobago");
  });

  test("Open 'Large Popup'.", async ({page}) => {
    let dropdownButtonFn = page.locator("[id='page:mainForm:dropdownButton::command']");
    let dropdownMenuFn = page.locator(".tobago-dropdown-menu[name=page\\:mainForm\\:dropdownButton]");
    let openButtonFn = page.locator("[id='page:mainForm:largePopupLink']");
    let popupFn = page.locator("[id='page:mainForm:largePopup']");
    let collapseFn = page.locator("[id='page:mainForm:largePopup::collapse']");
    let closeButtonFn = page.locator("[id='page:mainForm:largePopup:closeLargePopup']");

    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(dropdownMenuFn).not.toContainClass("show");
    await dropdownButtonFn.scrollIntoViewIfNeeded();
    await dropdownButtonFn.click();
    await expect(dropdownMenuFn).toContainClass("show");

    let promise = modalEventPromise(page, popupFn);
    await openButtonFn.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popupFn).toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "false");
    await expect(dropdownMenuFn).not.toContainClass("show");

    promise = modalEventPromise(page, popupFn);
    await closeButtonFn.click();
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
  });

  test("Open 'Small Popup'.", async ({page}) => {
    let dropdownButtonFn = page.locator("[id='page:mainForm:dropdownButton::command']");
    let dropdownMenuFn = page.locator(".tobago-dropdown-menu[name=page\\:mainForm\\:dropdownButton]");
    let openButtonFn = page.locator("[id='page:mainForm:smallPopupLink']");
    let popupFn = page.locator("[id='page:mainForm:smallPopup']");
    let collapseFn = page.locator("[id='page:mainForm:smallPopup::collapse']");
    let closeButtonFn = page.locator("[id='page:mainForm:smallPopup:closeSmallPopup']");

    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
    await expect(dropdownMenuFn).not.toContainClass("show");
    await dropdownButtonFn.scrollIntoViewIfNeeded();
    await dropdownButtonFn.click();
    await expect(dropdownMenuFn).toContainClass("show");

    let promise = modalEventPromise(page, popupFn);
    await openButtonFn.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popupFn).toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "false");
    await expect(dropdownMenuFn).not.toContainClass("show");

    promise = modalEventPromise(page, popupFn);
    await closeButtonFn.click();
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
  });

  test("open 'refresh content popup' and press 'close'", async ({page}) => {
    const wrapperFn = page.locator("[id='page:mainForm:wrapperForIntegrationTest']");
    const popupFn = page.locator("[id='page:mainForm:refreshPopup']");
    const collapseFn = page.locator("[id='page:mainForm:refreshPopup::collapse']");
    const openButtonFn = page.locator("[id='page:mainForm:openRefreshPopup']");
    const closeButtonFn = page.locator("[id='page:mainForm:refreshPopup:closeRefreshPopup']");

    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");

    let promise = modalEventPromise(page, wrapperFn);
    await openButtonFn.click();
    await expect(await promise).toBe("shown.bs.modal");
    await expect(popupFn).toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "false");

    promise = modalEventPromise(page, wrapperFn);
    await closeButtonFn.click();
    await expect(await promise).toBe("hidden.bs.modal");
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
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
