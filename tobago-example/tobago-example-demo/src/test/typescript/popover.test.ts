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

import {expect, Locator, test} from "@playwright/test";

test.describe("900-test/popover/Popover.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/popover/Popover.xhtml");
  });

  test("Open and close popover on buttons", async ({page}) => {
    const showCounter = page.locator("tobago-out[id='page:mainForm:showCounter'] .form-control-plaintext");
    const shownCounter = page.locator("tobago-out[id='page:mainForm:shownCounter'] .form-control-plaintext");
    const hideCounter = page.locator("tobago-out[id='page:mainForm:hideCounter'] .form-control-plaintext");
    const hiddenCounter = page.locator("tobago-out[id='page:mainForm:hiddenCounter'] .form-control-plaintext");

    await expect(showCounter).toHaveText("0");
    await expect(shownCounter).toHaveText("0");
    await expect(hideCounter).toHaveText("0");
    await expect(hiddenCounter).toHaveText("0");

    const focusButton = page.locator("[id='page:mainForm:focusPopover']");
    await expect(focusButton).not.toHaveAttribute("aria-describedby");
    await focusButton.focus();
    await expect(showCounter).toHaveText("1");
    await expect(focusButton).toHaveAttribute("aria-describedby");
    const focusPopover = page.locator("[id='" + await focusButton.getAttribute("aria-describedby") + "']");
    await expect(focusPopover).toBeVisible();
    await expect(shownCounter).toHaveText("1");
    await page.mouse.click(0, 0);
    await expect(hideCounter).toHaveText("1");
    await expect(hiddenCounter).toHaveText("1");
    await expect(focusButton).not.toHaveAttribute("aria-describedby");

    const clickButton = page.locator("[id='page:mainForm:clickPopover']");
    await expect(clickButton).not.toHaveAttribute("aria-describedby");
    await clickButton.click();
    await expect(showCounter).toHaveText("2");
    await expect(clickButton).toHaveAttribute("aria-describedby");
    const clickPopover = page.locator("[id='" + await clickButton.getAttribute("aria-describedby") + "']");
    await expect(clickPopover).toBeVisible();
    await expect(shownCounter).toHaveText("2");
    await clickButton.click();
    await expect(hideCounter).toHaveText("2");
    await expect(hiddenCounter).toHaveText("2");
    await expect(clickButton).not.toHaveAttribute("aria-describedby");

    const hoverButton = page.locator("[id='page:mainForm:hoverPopover']");
    await expect(hoverButton).not.toHaveAttribute("aria-describedby");
    await hoverButton.hover();
    await expect(showCounter).toHaveText("3");
    await expect(hoverButton).toHaveAttribute("aria-describedby");
    const hoverPopover = page.locator("[id='" + await hoverButton.getAttribute("aria-describedby") + "']");
    await expect(hoverPopover).toBeVisible();
    await expect(shownCounter).toHaveText("3");
    await page.mouse.move(0, 0);
    await expect(hideCounter).toHaveText("3");
    await expect(hiddenCounter).toHaveText("3");
    await expect(hoverButton).not.toHaveAttribute("aria-describedby");
  });

  test("Prevent show event", async ({page}) => {
    const preventShow = page.locator("input[type='radio'][id='page:mainForm:selectEventPrevention::1']");
    await preventShow.click();

    const showCounter = page.locator("tobago-out[id='page:mainForm:showCounter'] .form-control-plaintext");
    const shownCounter = page.locator("tobago-out[id='page:mainForm:shownCounter'] .form-control-plaintext");
    const hideCounter = page.locator("tobago-out[id='page:mainForm:hideCounter'] .form-control-plaintext");
    const hiddenCounter = page.locator("tobago-out[id='page:mainForm:hiddenCounter'] .form-control-plaintext");

    await expect(showCounter).toHaveText("0");
    await expect(shownCounter).toHaveText("0");
    await expect(hideCounter).toHaveText("0");
    await expect(hiddenCounter).toHaveText("0");

    const focusButton = page.locator("[id='page:mainForm:focusPopover']");
    await expect(focusButton).not.toHaveAttribute("aria-describedby");
    await focusButton.focus();
    await expect(showCounter).toHaveText("1");
    await expect(focusButton).not.toHaveAttribute("aria-describedby");
    await expect(shownCounter).toHaveText("0");
    await page.mouse.click(0, 0);
    await expect(hideCounter).toHaveText("0");
    await expect(hiddenCounter).toHaveText("0");
    await expect(focusButton).not.toHaveAttribute("aria-describedby");

    const clickButton = page.locator("[id='page:mainForm:clickPopover']");
    await expect(clickButton).not.toHaveAttribute("aria-describedby");
    await clickButton.click();
    await expect(showCounter).toHaveText("2");
    await expect(clickButton).not.toHaveAttribute("aria-describedby");
    await expect(shownCounter).toHaveText("0");
    await clickButton.click();
    await expect(hideCounter).toHaveText("0");
    await expect(hiddenCounter).toHaveText("0");
    await expect(clickButton).not.toHaveAttribute("aria-describedby");

    const hoverButton = page.locator("[id='page:mainForm:hoverPopover']");
    await expect(hoverButton).not.toHaveAttribute("aria-describedby");
    await hoverButton.hover();
    await expect(showCounter).toHaveText("3");
    await expect(hoverButton).not.toHaveAttribute("aria-describedby");
    await expect(shownCounter).toHaveText("0");
    await page.mouse.move(0, 0);
    await expect(hideCounter).toHaveText("0");
    await expect(hiddenCounter).toHaveText("0");
    await expect(hoverButton).not.toHaveAttribute("aria-describedby");
  });

  test("Prevent hide event: focus button", async ({page}) => {
    const preventHide = page.locator("input[type='radio'][id='page:mainForm:selectEventPrevention::2']");
    await preventHide.click();

    const showCounter = page.locator("tobago-out[id='page:mainForm:showCounter'] .form-control-plaintext");
    const shownCounter = page.locator("tobago-out[id='page:mainForm:shownCounter'] .form-control-plaintext");
    const hideCounter = page.locator("tobago-out[id='page:mainForm:hideCounter'] .form-control-plaintext");
    const hiddenCounter = page.locator("tobago-out[id='page:mainForm:hiddenCounter'] .form-control-plaintext");

    await expect(showCounter).toHaveText("0");
    await expect(shownCounter).toHaveText("0");
    await expect(hideCounter).toHaveText("0");
    await expect(hiddenCounter).toHaveText("0");

    const focusButton = page.locator("[id='page:mainForm:focusPopover']");
    await expect(focusButton).not.toHaveAttribute("aria-describedby");
    await focusButton.focus();
    await expect(showCounter).toHaveText("1");
    await expect(focusButton).toHaveAttribute("aria-describedby");
    const focusPopover = page.locator("[id='" + await focusButton.getAttribute("aria-describedby") + "']");
    await expect(focusPopover).toBeVisible();
    await expect(shownCounter).toHaveText("1");
    await page.mouse.click(0, 0);
    await expect(hideCounter).toHaveText("1");
    await expect(hiddenCounter).toHaveText("0");
    await expect(focusPopover).toBeVisible();
    await expect(focusButton).toHaveAttribute("aria-describedby");
    await expect(focusPopover).toBeVisible();
  });

  test("Prevent hide event: click button", async ({page}) => {
    const preventHide = page.locator("input[type='radio'][id='page:mainForm:selectEventPrevention::2']");
    await preventHide.click();

    const showCounter = page.locator("tobago-out[id='page:mainForm:showCounter'] .form-control-plaintext");
    const shownCounter = page.locator("tobago-out[id='page:mainForm:shownCounter'] .form-control-plaintext");
    const hideCounter = page.locator("tobago-out[id='page:mainForm:hideCounter'] .form-control-plaintext");
    const hiddenCounter = page.locator("tobago-out[id='page:mainForm:hiddenCounter'] .form-control-plaintext");

    await expect(showCounter).toHaveText("0");
    await expect(shownCounter).toHaveText("0");
    await expect(hideCounter).toHaveText("0");
    await expect(hiddenCounter).toHaveText("0");

    const clickButton = page.locator("[id='page:mainForm:clickPopover']");
    await expect(clickButton).not.toHaveAttribute("aria-describedby");
    await clickButton.click();
    await expect(showCounter).toHaveText("1");
    await expect(clickButton).toHaveAttribute("aria-describedby");
    const clickPopover = page.locator("[id='" + await clickButton.getAttribute("aria-describedby") + "']");
    await expect(clickPopover).toBeVisible();
    await expect(shownCounter).toHaveText("1");
    await clickButton.click();
    await expect(hideCounter).toHaveText("1");
    await expect(hiddenCounter).toHaveText("0");
    await expect(clickButton).toHaveAttribute("aria-describedby");
    await expect(clickPopover).toBeVisible();
  });

  test("Prevent hide event: hover button", async ({page}) => {
    const preventHide = page.locator("input[type='radio'][id='page:mainForm:selectEventPrevention::2']");
    await preventHide.click();

    const showCounter = page.locator("tobago-out[id='page:mainForm:showCounter'] .form-control-plaintext");
    const shownCounter = page.locator("tobago-out[id='page:mainForm:shownCounter'] .form-control-plaintext");
    const hideCounter = page.locator("tobago-out[id='page:mainForm:hideCounter'] .form-control-plaintext");
    const hiddenCounter = page.locator("tobago-out[id='page:mainForm:hiddenCounter'] .form-control-plaintext");

    await expect(showCounter).toHaveText("0");
    await expect(shownCounter).toHaveText("0");
    await expect(hideCounter).toHaveText("0");
    await expect(hiddenCounter).toHaveText("0");

    const hoverButton = page.locator("[id='page:mainForm:hoverPopover']");
    await expect(hoverButton).not.toHaveAttribute("aria-describedby");
    await hoverButton.hover();
    await expect(showCounter).toHaveText("1");
    await expect(hoverButton).toHaveAttribute("aria-describedby");
    const hoverPopover = page.locator("[id='" + await hoverButton.getAttribute("aria-describedby") + "']");
    await expect(hoverPopover).toBeVisible();
    await expect(shownCounter).toHaveText("1");
    await page.mouse.move(0, 0);
    await expect(hideCounter).toHaveText("1");
    await expect(hiddenCounter).toHaveText("0");
    await expect(hoverButton).toHaveAttribute("aria-describedby");
    await expect(hoverPopover).toBeVisible();
  });

  test("helpTrigger=hover", async ({page}) => {
    const datePopover = page.locator("[id='page:mainForm:date'] tobago-popover");
    const filePopover = page.locator("[id='page:mainForm:file'] tobago-popover");
    const inPopover = page.locator("[id='page:mainForm:in'] tobago-popover");
    const rangePopover = page.locator("[id='page:mainForm:range'] tobago-popover");
    const selectBooleanCheckboxPopover = page.locator("[id='page:mainForm:selectBooleanCheckbox'] tobago-popover");
    const selectBooleanTogglePopover = page.locator("[id='page:mainForm:selectBooleanToggle'] tobago-popover");
    const selectManyCheckboxPopover = page.locator("[id='page:mainForm:selectManyCheckbox'] tobago-popover");
    const selectManyListPopover = page.locator("[id='page:mainForm:selectManyList'] tobago-popover");
    const selectManyListboxPopover = page.locator("[id='page:mainForm:selectManyListbox'] tobago-popover");
    const selectManyShuttlePopover = page.locator("[id='page:mainForm:selectManyShuttle'] tobago-popover");
    const selectOneChoicePopover = page.locator("[id='page:mainForm:selectOneChoice'] tobago-popover");
    const selectOneListPopover = page.locator("[id='page:mainForm:selectOneList'] tobago-popover");
    const selectOneListboxPopover = page.locator("[id='page:mainForm:selectOneListbox'] tobago-popover");
    const selectOneRadioPopover = page.locator("[id='page:mainForm:selectOneRadio'] tobago-popover");
    const starsPopover = page.locator("[id='page:mainForm:stars'] tobago-popover");
    const textareaPopover = page.locator("[id='page:mainForm:textarea'] tobago-popover");

    await test(datePopover);
    await test(filePopover);
    await test(inPopover);
    await test(rangePopover);
    await test(selectBooleanCheckboxPopover);
    await test(selectBooleanTogglePopover);
    await test(selectManyCheckboxPopover);
    await test(selectManyListPopover);
    await test(selectManyListboxPopover);
    await test(selectManyShuttlePopover);
    await test(selectOneChoicePopover);
    await test(selectOneListPopover);
    await test(selectOneListboxPopover);
    await test(selectOneRadioPopover);
    await test(starsPopover);
    await test(textareaPopover);

    async function test(hoverButton: Locator): Promise<void> {
      await expect(hoverButton).not.toHaveAttribute("aria-describedby");
      await hoverButton.hover();
      await expect(hoverButton).toHaveAttribute("aria-describedby");
      const hoverPopover = page.locator("[id='" + await hoverButton.getAttribute("aria-describedby") + "']");
      await expect(hoverPopover).toBeVisible();
      await page.mouse.move(0, 0);
      await expect(hoverButton).not.toHaveAttribute("aria-describedby");
    }
  });
});
