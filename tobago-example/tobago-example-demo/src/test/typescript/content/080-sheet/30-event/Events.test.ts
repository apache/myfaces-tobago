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

test.describe("080-sheet/30-event/Events.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/080-sheet/30-event/Events.xhtml");
  });

  test("On click with ajax", async ({page}) => {
    const onClickAjax = page.locator("[id='page:mainForm:changeExample::0']");
    const detailsBox = page.locator("[id='page:mainForm:detail']");
    const detailsBoxNameField = page.locator("[id='page:mainForm:name::field']");
    const sun = page.locator("[id='page:mainForm:s1:0:sample0']");
    const venus = page.locator("[id='page:mainForm:s1:2:sample0']");
    const jupiter = page.locator("[id='page:mainForm:s1:5:sample0']");
    const saturn = page.locator("[id='page:mainForm:s1:6:sample0']");

    await onClickAjax.check();
    await expect(detailsBox).toBeVisible();
    await sun.click();
    await expect(detailsBoxNameField).toHaveValue("Sun");
    await venus.click();
    await expect(detailsBoxNameField).toHaveValue("Venus");
    await jupiter.click();
    await expect(detailsBoxNameField).toHaveValue("Jupiter");
    await saturn.click();
    await expect(detailsBoxNameField).toHaveValue("Saturn");
  });

  test("On click with full request", async ({page}) => {
    const onClickFullRequest = page.locator("[id='page:mainForm:changeExample::1']");
    const detailsBox = page.locator("[id='page:mainForm:detail']");
    const detailsBoxNameField = page.locator("[id='page:mainForm:name::field']");
    const sun = page.locator("[id='page:mainForm:s1:0:sample1']");
    const venus = page.locator("[id='page:mainForm:s1:2:sample1']");
    const jupiter = page.locator("[id='page:mainForm:s1:5:sample1']");
    const saturn = page.locator("[id='page:mainForm:s1:6:sample1']");

    await onClickFullRequest.check();
    await expect(detailsBox).toBeVisible();
    await sun.click();
    await expect(detailsBoxNameField).toHaveValue("Sun");
    await venus.click();
    await expect(detailsBoxNameField).toHaveValue("Venus");
    await jupiter.click();
    await expect(detailsBoxNameField).toHaveValue("Jupiter");
    await saturn.click();
    await expect(detailsBoxNameField).toHaveValue("Saturn");
  });

  test("On double click with full request", async ({page}) => {
    const onDblclickFullRequest = page.locator("[id='page:mainForm:changeExample::2']");
    const detailsBox = page.locator("[id='page:mainForm:detail']");
    const detailsBoxNameField = page.locator("[id='page:mainForm:name::field']");
    const sun = page.locator("[id='page:mainForm:s1:0:sample2']");
    const venus = page.locator("[id='page:mainForm:s1:2:sample2']");
    const jupiter = page.locator("[id='page:mainForm:s1:5:sample2']");
    const saturn = page.locator("[id='page:mainForm:s1:6:sample2']");

    await onDblclickFullRequest.check();
    await expect(detailsBox).toBeVisible();
    await sun.dblclick();
    await expect(detailsBoxNameField).toHaveValue("Sun");
    await venus.dblclick();
    await expect(detailsBoxNameField).toHaveValue("Venus");
    await jupiter.dblclick();
    await expect(detailsBoxNameField).toHaveValue("Jupiter");
    await saturn.dblclick();
    await expect(detailsBoxNameField).toHaveValue("Saturn");
  });

  test("Open popup on click with ajax", async ({page}) => {
    const onClickAjaxPopup = page.locator("[id='page:mainForm:changeExample::3']");
    const detailsBox = page.locator("[id='page:mainForm:detail']");
    const venus = page.locator("[id='page:mainForm:s1:2:sample3']");
    const jupiter = page.locator("[id='page:mainForm:s1:5:sample3']");
    const saturn = page.locator("[id='page:mainForm:s1:6:sample3']");
    const body = page.locator("body");
    const backdrop = page.locator(".modal-backdrop.fade.show");
    const reloadable = page.locator("[id='page:mainForm:reloadable']");
    const popup = page.locator("[id='page:mainForm:popup']");
    const popupNameField = page.locator("[id='page:mainForm:popup:popupName::field']");
    const cancel = page.locator("[id='page:mainForm:popup:cancel']");

    await onClickAjaxPopup.check();
    await expect(detailsBox).toHaveCount(0);
    await expect(backdrop).toHaveCount(0);
    await expect(popup).not.toContainClass("show");

    await venus.click();
    await expect(body).toContainClass("modal-open");
    await expect(backdrop).toHaveCount(1);
    await expect(backdrop).toBeVisible();
    await expect(popup).toContainClass("show");
    await expect(popupNameField).toHaveValue("Venus");

    await cancel.click();
    await expect(body).not.toContainClass("modal-open");
    await expect(backdrop).toHaveCount(0);
    await expect(popup).not.toContainClass("show");

    await jupiter.click();
    await expect(body).toContainClass("modal-open");
    await expect(backdrop).toHaveCount(1);
    await expect(backdrop).toBeVisible();
    await expect(popup).toContainClass("show");
    await expect(popupNameField).toHaveValue("Jupiter");

    await cancel.click();
    await expect(body).not.toContainClass("modal-open");
    await expect(backdrop).toHaveCount(0);
    await expect(popup).not.toContainClass("show");

    await saturn.click();
    await expect(body).toContainClass("modal-open");
    await expect(backdrop).toHaveCount(1);
    await expect(backdrop).toBeVisible();
    await expect(popup).toContainClass("show");
    await expect(popupNameField).toHaveValue("Saturn");

    await cancel.click();
    await expect(body).not.toContainClass("modal-open");
    await expect(backdrop).toHaveCount(0);
    await expect(popup).not.toContainClass("show");
  });

  test("On double click with ajax", async ({page}) => {
    const onDblclickAjax = page.locator("[id='page:mainForm:changeExample::4']");
    const detailsBox = page.locator("[id='page:mainForm:detail']");
    const detailsBoxNameField = page.locator("[id='page:mainForm:name::field']");
    const sun = page.locator("[id='page:mainForm:s1:0:sample4']");
    const venus = page.locator("[id='page:mainForm:s1:2:sample4']");
    const jupiter = page.locator("[id='page:mainForm:s1:5:sample4']");
    const saturn = page.locator("[id='page:mainForm:s1:6:sample4']");

    await onDblclickAjax.check();
    await expect(detailsBox).toBeVisible();
    await sun.dblclick();
    await expect(detailsBoxNameField).toHaveValue("Sun");
    await venus.dblclick();
    await expect(detailsBoxNameField).toHaveValue("Venus");
    await jupiter.dblclick();
    await expect(detailsBoxNameField).toHaveValue("Jupiter");
    await saturn.dblclick();
    await expect(detailsBoxNameField).toHaveValue("Saturn");
  });
});
