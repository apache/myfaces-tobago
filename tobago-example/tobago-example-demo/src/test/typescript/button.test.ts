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

test.describe("900-test/button/dropdown/Dropdown.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/button/dropdown/Dropdown.xhtml");
  });

  test("TAB, shift-TAB without open", async ({page, browserName}) => {
    const beforeComp = page.locator("[id='page:mainForm:beforeComp::field']");
    const dropdownForm = page.locator("[id='page:mainForm:dropdownForm::command']");
    const dropdownClassic = page.locator("[id='page:mainForm:dropdownClassic::command']");
    const dropdownSubmenus = page.locator("[id='page:mainForm:dropdownSubmenus::command']");
    const afterComp = page.locator("[id='page:mainForm:afterComp::field']");

    await beforeComp.click();
    await expect(beforeComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(dropdownForm).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(dropdownClassic).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(dropdownSubmenus).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(afterComp).toBeFocused();

    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(dropdownSubmenus).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(dropdownClassic).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(dropdownForm).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(beforeComp).toBeFocused();
  });

  test("Enter-Escape; Enter-Enter; Enter-Tab-Escape; Enter-shiftTab-Escape; ArrowDown-Escape; ArrowUp-Escape",
      async ({page, browserName}) => {
        const beforeComp = page.locator("[id='page:mainForm:beforeComp::field']");
        await beforeComp.click();
        await expect(beforeComp).toBeFocused();

        const dropdownForm = page.locator("[id='page:mainForm:dropdownForm::command']");
        const dropdownFormDate = page.locator("[id='page:mainForm:dropdownFormDate::field']");
        const dropdownFormApplyButton = page.locator("[id='page:mainForm:applyButton']");
        await page.keyboard.press("Tab");
        await expect(dropdownForm).toBeFocused();
        await expect(dropdownFormDate).not.toBeVisible();
        await expect(dropdownFormApplyButton).not.toBeVisible();
        await test(dropdownForm, dropdownFormDate, dropdownFormApplyButton);

        const dropdownClassic = page.locator("[id='page:mainForm:dropdownClassic::command']");
        const dropdownClassicLink1 = page.locator("[id='page:mainForm:dropdownClassicLink1']");
        const dropdownClassicLink3 = page.locator("[id='page:mainForm:dropdownClassicLink3']");
        await page.keyboard.press("Tab");
        await expect(dropdownClassic).toBeFocused();
        await expect(dropdownClassicLink1).not.toBeVisible();
        await expect(dropdownClassicLink3).not.toBeVisible();
        await test(dropdownClassic, dropdownClassicLink1, dropdownClassicLink3);

        const dropdownSubmenus = page.locator("[id='page:mainForm:dropdownSubmenus::command']");
        const dropdownSubmenusPlay = page.locator("[id='page:mainForm:dropdownSubmenusPlay']");
        const dropdownSubmenusTrack = page.locator("[id='page:mainForm:dropdownSubmenusTrack::command']");
        await page.keyboard.press("Tab");
        await expect(dropdownSubmenus).toBeFocused();
        await expect(dropdownSubmenusPlay).not.toBeVisible();
        await expect(dropdownSubmenusTrack).not.toBeVisible();
        await test(dropdownSubmenus, dropdownSubmenusPlay, dropdownSubmenusTrack);

        async function test(toggleButton: Locator, firstElement: Locator, lastElement: Locator): Promise<void> {
          //Enter-Escape
          await page.keyboard.press("Enter");
          await expect(firstElement).toBeVisible();
          await expect(toggleButton).toBeFocused();
          await page.keyboard.press("Escape");
          await expect(firstElement).not.toBeVisible();
          await expect(lastElement).not.toBeVisible();
          await expect(toggleButton).toBeFocused();

          //Enter-Enter
          await page.keyboard.press("Enter");
          await expect(firstElement).toBeVisible();
          await expect(toggleButton).toBeFocused();
          await page.keyboard.press("Enter");
          await expect(firstElement).not.toBeVisible();
          await expect(lastElement).not.toBeVisible();
          await expect(toggleButton).toBeFocused();

          //Enter-Tab-Escape
          await page.keyboard.press("Enter");
          await expect(firstElement).toBeVisible();
          await expect(toggleButton).toBeFocused();
          await page.keyboard.press("Tab");
          await expect(firstElement).toBeFocused();
          await page.keyboard.press("Escape");
          await expect(firstElement).not.toBeVisible();
          await expect(lastElement).not.toBeVisible();
          await expect(toggleButton).toBeFocused();

          //Enter-shiftTab-Escape
          await page.keyboard.press("Enter");
          await expect(firstElement).toBeVisible();
          await expect(toggleButton).toBeFocused();
          await page.keyboard.down("Shift");
          await page.keyboard.press("Tab");
          await page.keyboard.up("Shift");
          await expect(lastElement).toBeFocused();
          await page.keyboard.press("Escape");
          await expect(firstElement).not.toBeVisible();
          await expect(lastElement).not.toBeVisible();
          await expect(toggleButton).toBeFocused();

          //ArrowDown-Escape
          await page.keyboard.press("ArrowDown");
          await expect(firstElement).toBeVisible();
          await expect(firstElement).toBeFocused();
          await page.keyboard.press("Escape");
          await expect(firstElement).not.toBeVisible();
          await expect(lastElement).not.toBeVisible();
          await expect(toggleButton).toBeFocused();

          //ArrowUp-Escape
          await page.keyboard.press("ArrowUp");
          await expect(lastElement).toBeVisible();
          await expect(lastElement).toBeFocused();
          await page.keyboard.press("Escape");
          await expect(firstElement).not.toBeVisible();
          await expect(lastElement).not.toBeVisible();
          await expect(toggleButton).toBeFocused();
        }
      });

  test("click-Escape; click-click; click-Tab-Escape; click-shiftTab-Escape", async ({page, browserName}) => {
    const dropdownForm = page.locator("[id='page:mainForm:dropdownForm::command']");
    const dropdownFormDate = page.locator("[id='page:mainForm:dropdownFormDate::field']");
    const dropdownFormApplyButton = page.locator("[id='page:mainForm:applyButton']");
    await test(dropdownForm, dropdownFormDate, dropdownFormApplyButton);

    const dropdownClassic = page.locator("[id='page:mainForm:dropdownClassic::command']");
    const dropdownClassicLink1 = page.locator("[id='page:mainForm:dropdownClassicLink1']");
    const dropdownClassicLink3 = page.locator("[id='page:mainForm:dropdownClassicLink3']");
    await test(dropdownClassic, dropdownClassicLink1, dropdownClassicLink3);

    const dropdownSubmenus = page.locator("[id='page:mainForm:dropdownSubmenus::command']");
    const dropdownSubmenusPlay = page.locator("[id='page:mainForm:dropdownSubmenusPlay']");
    const dropdownSubmenusTrack = page.locator("[id='page:mainForm:dropdownSubmenusTrack::command']");
    await test(dropdownSubmenus, dropdownSubmenusPlay, dropdownSubmenusTrack);

    async function test(toggleButton: Locator, firstElement: Locator, lastElement: Locator): Promise<void> {
      //click-Escape
      await toggleButton.click();
      await expect(toggleButton).toBeFocused();
      await expect(firstElement).toBeVisible();
      await page.keyboard.press("Escape");
      await expect(firstElement).not.toBeVisible();
      await expect(lastElement).not.toBeVisible();
      await expect(toggleButton).toBeFocused();

      //click-click
      await toggleButton.click();
      await expect(toggleButton).toBeFocused();
      await expect(firstElement).toBeVisible();
      await toggleButton.click();
      await expect(firstElement).not.toBeVisible();
      await expect(lastElement).not.toBeVisible();
      await expect(toggleButton).toBeFocused();

      //click-Tab-Escape
      await toggleButton.click();
      await expect(firstElement).toBeVisible();
      await expect(toggleButton).toBeFocused();
      await page.keyboard.press("Tab");
      await expect(firstElement).toBeFocused();
      await page.keyboard.press("Escape");
      await expect(firstElement).not.toBeVisible();
      await expect(lastElement).not.toBeVisible();
      await expect(toggleButton).toBeFocused();

      //click-shiftTab-Escape
      await toggleButton.click();
      await expect(firstElement).toBeVisible();
      await expect(toggleButton).toBeFocused();
      await page.keyboard.down("Shift");
      await page.keyboard.press("Tab");
      await page.keyboard.up("Shift");
      await expect(lastElement).toBeFocused();
      await page.keyboard.press("Escape");
      await expect(firstElement).not.toBeVisible();
      await expect(lastElement).not.toBeVisible();
      await expect(toggleButton).toBeFocused();
    }
  });

  test("click-ArrowDown-Escape; click-ArrowUp-Escape", async ({page, browserName}) => {
    const dropdownForm = page.locator("[id='page:mainForm:dropdownForm::command']");
    const dropdownFormDate = page.locator("[id='page:mainForm:dropdownFormDate::field']");
    const dropdownFormApplyButton = page.locator("[id='page:mainForm:applyButton']");
    await test(dropdownForm, dropdownFormDate, dropdownFormApplyButton, true);

    const dropdownClassic = page.locator("[id='page:mainForm:dropdownClassic::command']");
    const dropdownClassicLink1 = page.locator("[id='page:mainForm:dropdownClassicLink1']");
    const dropdownClassicLink3 = page.locator("[id='page:mainForm:dropdownClassicLink3']");
    await test(dropdownClassic, dropdownClassicLink1, dropdownClassicLink3, false);

    const dropdownSubmenus = page.locator("[id='page:mainForm:dropdownSubmenus::command']");
    const dropdownSubmenusPlay = page.locator("[id='page:mainForm:dropdownSubmenusPlay']");
    const dropdownSubmenusTrack = page.locator("[id='page:mainForm:dropdownSubmenusTrack::command']");
    await test(dropdownSubmenus, dropdownSubmenusPlay, dropdownSubmenusTrack, false);

    async function test(toggleButton: Locator, firstElement: Locator, lastElement: Locator, panelFacet: boolean): Promise<void> {
      //click-ArrowDown-Escape
      await toggleButton.click();
      await expect(toggleButton).toBeFocused();
      await expect(firstElement).toBeVisible();
      await page.keyboard.press("ArrowDown");
      if (panelFacet) {
        await expect(firstElement).not.toBeFocused();
        await expect(toggleButton).toBeFocused();
      } else {
        await expect(firstElement).toBeFocused();
      }
      await page.keyboard.press("Escape");
      await expect(firstElement).not.toBeVisible();
      await expect(lastElement).not.toBeVisible();
      await expect(toggleButton).toBeFocused();

      //click-ArrowUp-Escape
      await toggleButton.click();
      await expect(toggleButton).toBeFocused();
      await expect(firstElement).toBeVisible();
      await page.keyboard.press("ArrowUp");
      if (panelFacet) {
        await expect(lastElement).not.toBeFocused();
        await expect(toggleButton).toBeFocused();
      } else {
        await expect(lastElement).toBeFocused();
      }
      await page.keyboard.press("Escape");
      await expect(firstElement).not.toBeVisible();
      await expect(lastElement).not.toBeVisible();
      await expect(toggleButton).toBeFocused();
    }
  });

  test("Space-Space; Enter-Space; click-Space; Space-Tab", async ({page, browserName}) => {
    const beforeComp = page.locator("[id='page:mainForm:beforeComp::field']");
    await beforeComp.click();
    await expect(beforeComp).toBeFocused();

    const dropdownForm = page.locator("[id='page:mainForm:dropdownForm::command']");
    const dropdownFormDate = page.locator("[id='page:mainForm:dropdownFormDate::field']");
    const dropdownFormApplyButton = page.locator("[id='page:mainForm:applyButton']");
    await page.keyboard.press("Tab");
    await expect(dropdownForm).toBeFocused();
    await expect(dropdownFormDate).not.toBeVisible();
    await expect(dropdownFormApplyButton).not.toBeVisible();
    await test(dropdownForm, dropdownFormDate, dropdownFormApplyButton);

    const dropdownClassic = page.locator("[id='page:mainForm:dropdownClassic::command']");
    const dropdownClassicLink1 = page.locator("[id='page:mainForm:dropdownClassicLink1']");
    const dropdownClassicLink3 = page.locator("[id='page:mainForm:dropdownClassicLink3']");
    await page.keyboard.press("Tab");
    await expect(dropdownClassic).toBeFocused();
    await expect(dropdownClassicLink1).not.toBeVisible();
    await expect(dropdownClassicLink3).not.toBeVisible();
    await test(dropdownClassic, dropdownClassicLink1, dropdownClassicLink3);

    const dropdownSubmenus = page.locator("[id='page:mainForm:dropdownSubmenus::command']");
    const dropdownSubmenusPlay = page.locator("[id='page:mainForm:dropdownSubmenusPlay']");
    const dropdownSubmenusTrack = page.locator("[id='page:mainForm:dropdownSubmenusTrack::command']");
    await page.keyboard.press("Tab");
    await expect(dropdownSubmenus).toBeFocused();
    await expect(dropdownSubmenusPlay).not.toBeVisible();
    await expect(dropdownSubmenusTrack).not.toBeVisible();
    await test(dropdownSubmenus, dropdownSubmenusPlay, dropdownSubmenusTrack);

    async function test(toggleButton: Locator, firstElement: Locator, lastElement: Locator): Promise<void> {
      //Space-Space
      await page.keyboard.press("Space");
      await expect(firstElement).toBeVisible();
      await expect(toggleButton).toBeFocused();
      await page.keyboard.press("Space");
      await expect(firstElement).not.toBeVisible();
      await expect(lastElement).not.toBeVisible();
      await expect(toggleButton).toBeFocused();

      //Enter-Space
      await page.keyboard.press("Enter");
      await expect(firstElement).toBeVisible();
      await expect(toggleButton).toBeFocused();
      await page.keyboard.press("Space");
      await expect(firstElement).not.toBeVisible();
      await expect(lastElement).not.toBeVisible();
      await expect(toggleButton).toBeFocused();

      //Space-Tab-Escape
      await page.keyboard.press("Space");
      await expect(firstElement).toBeVisible();
      await expect(toggleButton).toBeFocused();
      await page.keyboard.press("Tab");
      await expect(firstElement).toBeFocused();
      await page.keyboard.press("Escape");
      await expect(firstElement).not.toBeVisible();
      await expect(lastElement).not.toBeVisible();
      await expect(toggleButton).toBeFocused();
    }
  });

  test("SubMenus: focus; ArrowDown-ArrowDown; ArrowRight; hover entry '3'; ArrowLeft; ArrowUp",
      async ({page, browserName}) => {
        const rootButton = page.locator("[id='page:mainForm:dropdownSubmenus::command']");
        const entryPlay = page.locator("[id='page:mainForm:dropdownSubmenusPlay']");
        const entrySubmenu = page.locator("[id='page:mainForm:dropdownSubmenusSubmenu::command']");
        const entry1 = page.locator("[id='page:mainForm:entry1::command']");
        const entry11 = page.locator("[id='page:mainForm:entry11']");
        const entry2 = page.locator("[id='page:mainForm:entry2']");
        const entry3 = page.locator("[id='page:mainForm:entry3::command']");
        const entry31 = page.locator("[id='page:mainForm:entry31']");

        await rootButton.focus();
        await expect(rootButton).toBeFocused();
        await expect(rootButton).toHaveAttribute("aria-expanded", "false");
        await expect(entrySubmenu).not.toBeVisible();
        await page.keyboard.press("ArrowDown");
        await expect(rootButton).toHaveAttribute("aria-expanded", "true");
        await expect(entrySubmenu).toHaveAttribute("aria-expanded", "false");
        await expect(entryPlay).toBeFocused();
        await expect(entrySubmenu).toBeVisible();
        await expect(entry1).not.toBeVisible();
        await expect(entry2).not.toBeVisible();
        await expect(entry3).not.toBeVisible();
        await page.keyboard.press("ArrowDown");
        await expect(rootButton).toHaveAttribute("aria-expanded", "true");
        await expect(entrySubmenu).toHaveAttribute("aria-expanded", "true");
        await expect(entry1).toHaveAttribute("aria-expanded", "false");
        await expect(entry3).toHaveAttribute("aria-expanded", "false");
        await expect(entrySubmenu).toBeFocused();
        await expect(entry1).toBeVisible();
        await expect(entry2).toBeVisible();
        await expect(entry3).toBeVisible();
        await expect(entry11).not.toBeVisible();
        await expect(entry31).not.toBeVisible();
        await page.keyboard.press("ArrowRight");
        await expect(rootButton).toHaveAttribute("aria-expanded", "true");
        await expect(entrySubmenu).toHaveAttribute("aria-expanded", "true");
        await expect(entry1).toHaveAttribute("aria-expanded", "true");
        await expect(entry3).toHaveAttribute("aria-expanded", "false");
        await expect(entry1).toBeFocused();
        await expect(entry11).toBeVisible();
        await expect(entry31).not.toBeVisible();
        await entry3.hover();
        await expect(rootButton).toHaveAttribute("aria-expanded", "true");
        await expect(entrySubmenu).toHaveAttribute("aria-expanded", "true");
        await expect(entry1).toHaveAttribute("aria-expanded", "false");
        await expect(entry3).toHaveAttribute("aria-expanded", "true");
        await expect(entry1).toBeFocused();
        await expect(entry11).not.toBeVisible();
        await expect(entry31).toBeVisible();
        await page.keyboard.press("ArrowLeft");
        await expect(rootButton).toHaveAttribute("aria-expanded", "true");
        await expect(entrySubmenu).toHaveAttribute("aria-expanded", "true");
        await expect(entry1).toHaveAttribute("aria-expanded", "false");
        await expect(entry3).toHaveAttribute("aria-expanded", "false");
        await expect(entrySubmenu).toBeFocused();
        await expect(entry1).toBeVisible();
        await expect(entry2).toBeVisible();
        await expect(entry3).toBeVisible();
        await expect(entry11).not.toBeVisible();
        await expect(entry31).not.toBeVisible();
        await page.keyboard.press("ArrowUp");
        await expect(rootButton).toHaveAttribute("aria-expanded", "true");
        await expect(entrySubmenu).toHaveAttribute("aria-expanded", "false");
        await expect(entryPlay).toBeFocused();
        await expect(entrySubmenu).toBeVisible();
        await expect(entry1).not.toBeVisible();
        await expect(entry2).not.toBeVisible();
        await expect(entry3).not.toBeVisible();
      });
});
