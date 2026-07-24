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

test.describe("300-requests/150-behavior/Behavior.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/300-requests/150-behavior/Behavior.xhtml");
  });

  test("Ajax Input", async ({page}) => {
    const ajaxInputFn = page.locator("[id='page:mainForm:inputAjax::field']");
    const ajaxOutputFn = page.locator("[id='page:mainForm:outputAjax'] .form-control-plaintext");

    await ajaxInputFn.fill("Alice");
    await ajaxInputFn.blur();
    await expect(ajaxOutputFn).toHaveText("Alice");
  });

  test("Event Input", async ({page}) => {
    const eventInputFn = page.locator("[id='page:mainForm:inputEvent::field']");
    const eventOutputFn = page.locator("[id='page:mainForm:outputEvent'] .form-control-plaintext");

    await eventInputFn.fill("Charlie");
    await eventInputFn.blur();
    await expect(eventOutputFn).toHaveText("Charlie");
  });

  test("change the event name", async ({page}) => {
    const dblButtonAjaxFn = page.locator("[id='page:mainForm:dblButtonAjax']");
    const dblButtonEventFn = page.locator("[id='page:mainForm:dblButtonEvent']");
    const dblCounterFn = page.locator("[id='page:mainForm:dblCounter'] .form-control-plaintext");
    const counter = Number(await dblCounterFn.textContent());

    await dblButtonAjaxFn.dblclick();
    await expect(dblCounterFn).toHaveText((counter + 1).toString());
    await dblButtonEventFn.dblclick();
    await expect(dblCounterFn).toHaveText((counter + 2).toString());
    await dblButtonEventFn.dblclick();
    await expect(dblCounterFn).toHaveText((counter + 3).toString());
    await dblButtonAjaxFn.dblclick();
    await expect(dblCounterFn).toHaveText((counter + 4).toString());
  });

  test("f:ajax and tc:event", async ({page}) => {
    const submitFn = page.locator("[id='page:mainForm:btnAjaxEvent']");
    const outFn = page.locator("[id='page:mainForm:out'] .form-control-plaintext");

    await submitFn.dblclick();
    await expect(outFn).toHaveText("Event");
    await submitFn.click();
    await expect(outFn).toHaveText("Ajax");
  });

  test("Custom event", async ({page}) => {
    const button = page.locator("[id='page:mainForm:customEventButton']");
    const output = page.locator("[id='page:mainForm:customEventOutput']");

    await expect(output).toHaveText("");
    await button.click();
    await expect(output).toHaveText(/^my-event fired at /);
  });
});
