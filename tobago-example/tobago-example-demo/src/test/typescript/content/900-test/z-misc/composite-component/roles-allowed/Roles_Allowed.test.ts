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

test.describe("900-test/z-misc/composite-component/roles-allowed/Roles_Allowed.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/composite-component/roles-allowed/Roles_Allowed.xhtml");
  });

  test("RolesAllowed as 'guest'", async ({page}) => {
    const userFn = page.locator("[id='page:mainForm:username::field']");
    const passwordFn = page.locator("[id='page:mainForm:password::field']");
    const loginFn = page.locator("[id='page:mainForm:login']");
    const logoutFn = page.locator("[id='page:mainForm:logout']");
    const logoutBatch = page.locator("[id='page:mainForm:logoutBatch']");
    const outputLabelFn = page.locator("[id='page:mainForm:output'] label");
    const outputValueFn = page.locator("[id='page:mainForm:output'] .form-control-plaintext");
    const buttonFn = page.locator("[id='page:mainForm:button']");
    const ccOutputLabelFn = page.locator("[id='page:mainForm:ccRolesTest:out'] label");
    const ccOutputValueFn = page.locator("[id='page:mainForm:ccRolesTest:out'] .form-control-plaintext");
    const ccButtonFn = page.locator("[id='page:mainForm:ccRolesTest:submit']");
    const ccButton2Fn = page.locator("[id='page:mainForm:ccRolesTest:submit2']");

    let timestampNoCompositeValue: number;
    let timestampRolesCompositeValue: number;

    await expect(loginFn).toBeVisible();
    await expect(outputLabelFn).toHaveText("Output label");
    await expect(buttonFn).toBeDisabled();
    await expect(ccOutputLabelFn).toHaveText("Label");
    await expect(ccButtonFn).toBeDisabled();
    await expect(ccButton2Fn).toBeDisabled();

    await userFn.fill("guest");
    await passwordFn.fill("guest");
    await loginFn.click();
    await expect(logoutBatch).toHaveCount(1);
    await page.goto("/content/900-test/z-misc/composite-component/roles-allowed/Roles_Allowed.xhtml");

    await expect(logoutFn).toBeVisible();
    await expect(outputLabelFn).toHaveText("Output label");
    await expect(buttonFn).toBeEnabled();
    await expect(ccOutputLabelFn).toHaveText("Label");
    await expect(ccButtonFn).toBeEnabled();
    await expect(ccButton2Fn).toBeEnabled();

    timestampNoCompositeValue = Number(await outputValueFn.textContent());
    timestampRolesCompositeValue = Number(await ccOutputValueFn.textContent());
    await buttonFn.click();
    await expect.poll(async () => Number(await outputValueFn.textContent())).toBeGreaterThan(timestampNoCompositeValue);
    await expect.poll(async () => Number(await ccOutputValueFn.textContent())).toBeGreaterThan(timestampRolesCompositeValue);

    timestampNoCompositeValue = Number(await outputValueFn.textContent());
    timestampRolesCompositeValue = Number(await ccOutputValueFn.textContent());
    await ccButtonFn.click();
    await expect.poll(async () => Number(await outputValueFn.textContent())).toBeGreaterThan(timestampNoCompositeValue);
    await expect.poll(async () => Number(await ccOutputValueFn.textContent())).toBeGreaterThan(timestampRolesCompositeValue);

    timestampNoCompositeValue = Number(await outputValueFn.textContent());
    timestampRolesCompositeValue = Number(await ccOutputValueFn.textContent());
    await ccButton2Fn.click();
    await expect.poll(async () => Number(await outputValueFn.textContent())).toBeGreaterThan(timestampNoCompositeValue);
    await expect.poll(async () => Number(await ccOutputValueFn.textContent())).toBeGreaterThan(timestampRolesCompositeValue);

    await logoutFn.click();
    await expect(loginFn).toBeVisible();
  });
});
