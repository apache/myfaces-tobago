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

test.describe("900-test/z-misc/messageLayout/messageLayout.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/messageLayout/messageLayout.xhtml");
  });

  test("Fatal shadow", async ({page, browserName}) => {
    const fatalBoxShadow = "rgba(220, 53, 69, 0.25) 0px 0px 0px 4px";
    const messages = page.locator("[id='page:messages'] .alert");
    const submitButton = page.locator("[id='page:mainForm:submit']");

    await submitButton.click();
    await expect(messages).not.toHaveCount(0);

    const inputField = page.locator("[id='page:mainForm:fatalIn::field']");
    await inputField.focus();
    await expect(inputField).toHaveCSS("box-shadow", fatalBoxShadow);
    const dateField = page.locator("[id='page:mainForm:fatalDate::field']");
    await dateField.focus();
    await expect(dateField).toHaveCSS("box-shadow", fatalBoxShadow);
    const textarea = page.locator("[id='page:mainForm:fatalTextarea::field']");
    await textarea.focus();
    await expect(textarea).toHaveCSS("box-shadow", fatalBoxShadow);
    const selectBooleanCheckbox = page.locator("[id='page:mainForm:fatalCheckbox::field']");
    await selectBooleanCheckbox.focus();
    await expect(selectBooleanCheckbox).toHaveCSS("box-shadow", fatalBoxShadow);
    const selectBooleanToggle = page.locator("[id='page:mainForm:fatalToggle::field']");
    await selectBooleanToggle.focus();
    await expect(selectBooleanToggle).toHaveCSS("box-shadow", fatalBoxShadow);
    const selectManyCheckbox = page.locator("[id='page:mainForm:fatalCheckboxes::0']");
    await selectManyCheckbox.focus();
    await expect(selectManyCheckbox).toHaveCSS("box-shadow", fatalBoxShadow);
    const selectOneRadio = page.locator("[id='page:mainForm:fatalRadio::0']");
    await selectOneRadio.focus();
    await expect(selectOneRadio).toHaveCSS("box-shadow", fatalBoxShadow);
    const selectOneChoice = page.locator("[id='page:mainForm:fatalDropdown::field']");
    await selectOneChoice.focus();
    await expect(selectOneChoice).toHaveCSS("box-shadow", fatalBoxShadow);
    const selectOneListbox = page.locator("[id='page:mainForm:fatalOneListbox::field']");
    await selectOneListbox.focus();
    await expect(selectOneListbox).toHaveCSS("box-shadow", fatalBoxShadow);
    const selectManyListbox = page.locator("[id='page:mainForm:fatalManyListbox::field']");
    await selectManyListbox.focus();
    await expect(selectManyListbox).toHaveCSS("box-shadow", fatalBoxShadow);
    const selectManyShuttle = page.locator("[id='page:mainForm:fatalShuttle::selected']");
    await selectManyShuttle.focus();
    await expect(selectManyShuttle).toHaveCSS("box-shadow", fatalBoxShadow);
  });

  test("Error shadow", async ({page, browserName}) => {
    const errorBoxShadow = "rgba(220, 53, 69, 0.25) 0px 0px 0px 4px";
    const messages = page.locator("[id='page:messages'] .alert");
    const submitButton = page.locator("[id='page:mainForm:submit']");

    await submitButton.click();
    await expect(messages).not.toHaveCount(0);

    const inputField = page.locator("[id='page:mainForm:errorIn::field']");
    await inputField.focus();
    await expect(inputField).toHaveCSS("box-shadow", errorBoxShadow);
    const dateField = page.locator("[id='page:mainForm:errorDate::field']");
    await dateField.focus();
    await expect(dateField).toHaveCSS("box-shadow", errorBoxShadow);
    const textarea = page.locator("[id='page:mainForm:errorTextarea::field']");
    await textarea.focus();
    await expect(textarea).toHaveCSS("box-shadow", errorBoxShadow);
    const selectBooleanCheckbox = page.locator("[id='page:mainForm:errorCheckbox::field']");
    await selectBooleanCheckbox.focus();
    await expect(selectBooleanCheckbox).toHaveCSS("box-shadow", errorBoxShadow);
    const selectBooleanToggle = page.locator("[id='page:mainForm:errorToggle::field']");
    await selectBooleanToggle.focus();
    await expect(selectBooleanToggle).toHaveCSS("box-shadow", errorBoxShadow);
    const selectManyCheckbox = page.locator("[id='page:mainForm:errorCheckboxes::0']");
    await selectManyCheckbox.focus();
    await expect(selectManyCheckbox).toHaveCSS("box-shadow", errorBoxShadow);
    const selectOneRadio = page.locator("[id='page:mainForm:errorRadio::0']");
    await selectOneRadio.focus();
    await expect(selectOneRadio).toHaveCSS("box-shadow", errorBoxShadow);
    const selectOneChoice = page.locator("[id='page:mainForm:errorDropdown::field']");
    await selectOneChoice.focus();
    await expect(selectOneChoice).toHaveCSS("box-shadow", errorBoxShadow);
    const selectOneListbox = page.locator("[id='page:mainForm:errorOneListbox::field']");
    await selectOneListbox.focus();
    await expect(selectOneListbox).toHaveCSS("box-shadow", errorBoxShadow);
    const selectManyListbox = page.locator("[id='page:mainForm:errorManyListbox::field']");
    await selectManyListbox.focus();
    await expect(selectManyListbox).toHaveCSS("box-shadow", errorBoxShadow);
    const selectManyShuttle = page.locator("[id='page:mainForm:errorShuttle::selected']");
    await selectManyShuttle.focus();
    await expect(selectManyShuttle).toHaveCSS("box-shadow", errorBoxShadow);
  });

  test("Warning shadow", async ({page, browserName}) => {
    const warnBoxShadow = "rgba(255, 193, 7, 0.25) 0px 0px 0px 4px";
    const messages = page.locator("[id='page:messages'] .alert");
    const submitButton = page.locator("[id='page:mainForm:submit']");

    await submitButton.click();
    await expect(messages).not.toHaveCount(0);

    const inputField = page.locator("[id='page:mainForm:warnIn::field']");
    await inputField.focus();
    await expect(inputField).toHaveCSS("box-shadow", warnBoxShadow);
    const dateField = page.locator("[id='page:mainForm:warnDate::field']");
    await dateField.focus();
    await expect(dateField).toHaveCSS("box-shadow", warnBoxShadow);
    const textarea = page.locator("[id='page:mainForm:warnTextarea::field']");
    await textarea.focus();
    await expect(textarea).toHaveCSS("box-shadow", warnBoxShadow);
    const selectBooleanCheckbox = page.locator("[id='page:mainForm:warnCheckbox::field']");
    await selectBooleanCheckbox.focus();
    await expect(selectBooleanCheckbox).toHaveCSS("box-shadow", warnBoxShadow);
    const selectBooleanToggle = page.locator("[id='page:mainForm:warnToggle::field']");
    await selectBooleanToggle.focus();
    await expect(selectBooleanToggle).toHaveCSS("box-shadow", warnBoxShadow);
    const selectManyCheckbox = page.locator("[id='page:mainForm:warnCheckboxes::0']");
    await selectManyCheckbox.focus();
    await expect(selectManyCheckbox).toHaveCSS("box-shadow", warnBoxShadow);
    const selectOneRadio = page.locator("[id='page:mainForm:warnRadio::0']");
    await selectOneRadio.focus();
    await expect(selectOneRadio).toHaveCSS("box-shadow", warnBoxShadow);
    const selectOneChoice = page.locator("[id='page:mainForm:warnDropdown::field']");
    await selectOneChoice.focus();
    await expect(selectOneChoice).toHaveCSS("box-shadow", warnBoxShadow);
    const selectOneListbox = page.locator("[id='page:mainForm:warnOneListbox::field']");
    await selectOneListbox.focus();
    await expect(selectOneListbox).toHaveCSS("box-shadow", warnBoxShadow);
    const selectManyListbox = page.locator("[id='page:mainForm:warnManyListbox::field']");
    await selectManyListbox.focus();
    await expect(selectManyListbox).toHaveCSS("box-shadow", warnBoxShadow);
    const selectManyShuttle = page.locator("[id='page:mainForm:warnShuttle::selected']");
    await selectManyShuttle.focus();
    await expect(selectManyShuttle).toHaveCSS("box-shadow", warnBoxShadow);
  });

  test("Information shadow", async ({page, browserName}) => {
    const infoBoxShadow = "rgba(13, 202, 240, 0.25) 0px 0px 0px 4px";
    const messages = page.locator("[id='page:messages'] .alert");
    const submitButton = page.locator("[id='page:mainForm:submit']");

    await submitButton.click();
    await expect(messages).not.toHaveCount(0);

    const inputField = page.locator("[id='page:mainForm:infoIn::field']");
    await inputField.focus();
    await expect(inputField).toHaveCSS("box-shadow", infoBoxShadow);
    const dateField = page.locator("[id='page:mainForm:infoDate::field']");
    await dateField.focus();
    await expect(dateField).toHaveCSS("box-shadow", infoBoxShadow);
    const textarea = page.locator("[id='page:mainForm:infoTextarea::field']");
    await textarea.focus();
    await expect(textarea).toHaveCSS("box-shadow", infoBoxShadow);
    const selectBooleanCheckbox = page.locator("[id='page:mainForm:infoCheckbox::field']");
    await selectBooleanCheckbox.focus();
    await expect(selectBooleanCheckbox).toHaveCSS("box-shadow", infoBoxShadow);
    const selectBooleanToggle = page.locator("[id='page:mainForm:infoToggle::field']");
    await selectBooleanToggle.focus();
    await expect(selectBooleanToggle).toHaveCSS("box-shadow", infoBoxShadow);
    const selectManyCheckbox = page.locator("[id='page:mainForm:infoCheckboxes::0']");
    await selectManyCheckbox.focus();
    await expect(selectManyCheckbox).toHaveCSS("box-shadow", infoBoxShadow);
    const selectOneRadio = page.locator("[id='page:mainForm:infoRadio::0']");
    await selectOneRadio.focus();
    await expect(selectOneRadio).toHaveCSS("box-shadow", infoBoxShadow);
    const selectOneChoice = page.locator("[id='page:mainForm:infoDropdown::field']");
    await selectOneChoice.focus();
    await expect(selectOneChoice).toHaveCSS("box-shadow", infoBoxShadow);
    const selectOneListbox = page.locator("[id='page:mainForm:infoOneListbox::field']");
    await selectOneListbox.focus();
    await expect(selectOneListbox).toHaveCSS("box-shadow", infoBoxShadow);
    const selectManyListbox = page.locator("[id='page:mainForm:infoManyListbox::field']");
    await selectManyListbox.focus();
    await expect(selectManyListbox).toHaveCSS("box-shadow", infoBoxShadow);
    const selectManyShuttle = page.locator("[id='page:mainForm:infoShuttle::selected']");
    await selectManyShuttle.focus();
    await expect(selectManyShuttle).toHaveCSS("box-shadow", infoBoxShadow);
  });
});
