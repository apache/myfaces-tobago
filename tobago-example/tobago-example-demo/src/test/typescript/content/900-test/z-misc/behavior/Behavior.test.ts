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

test.describe("900-test/z-misc/behavior/Behavior.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/behavior/Behavior.xhtml");
  });

  test("Simple Event", async ({page}) => {
    const buttonFn = page.locator("[id='page:mainForm:simpleEvent']");
    const oldCounterValues = await getCounterValues(page);

    await buttonFn.click();
    await expect((await getCounterValues(page)).buttonAction).toEqual(oldCounterValues.buttonAction);
    await expect((await getCounterValues(page)).buttonAction).toEqual(oldCounterValues.buttonAction);
    await expect((await getCounterValues(page)).buttonActionListener).toEqual(oldCounterValues.buttonActionListener);
    await expect((await getCounterValues(page)).action1).toEqual(oldCounterValues.action1 + 1);
    await expect((await getCounterValues(page)).actionListener1).toEqual(oldCounterValues.actionListener1 + 1);
    await expect((await getCounterValues(page)).ajaxListener1).toEqual(oldCounterValues.ajaxListener1);
    await expect((await getCounterValues(page)).action2).toEqual(oldCounterValues.action2);
    await expect((await getCounterValues(page)).actionListener2).toEqual(oldCounterValues.actionListener2);
    await expect((await getCounterValues(page)).ajaxListener2).toEqual(oldCounterValues.ajaxListener2);
    await expect((await getCounterValues(page)).action3).toEqual(oldCounterValues.action3);
    await expect((await getCounterValues(page)).actionListener3).toEqual(oldCounterValues.actionListener3);
    await expect((await getCounterValues(page)).ajaxListener3).toEqual(oldCounterValues.ajaxListener3);
  });

  test("Simple Ajax", async ({page}) => {
    const buttonFn = page.locator("[id='page:mainForm:simpleAjax']");
    const oldCounterValues = await getCounterValues(page);

    await buttonFn.click();
    await expect((await getCounterValues(page)).buttonAction).toEqual(oldCounterValues.buttonAction);
    await expect((await getCounterValues(page)).buttonActionListener).toEqual(oldCounterValues.buttonActionListener);
    await expect((await getCounterValues(page)).action1).toEqual(oldCounterValues.action1);
    await expect((await getCounterValues(page)).actionListener1).toEqual(oldCounterValues.actionListener1);
    await expect((await getCounterValues(page)).ajaxListener1).toEqual(oldCounterValues.ajaxListener1 + 1);
    await expect((await getCounterValues(page)).action2).toEqual(oldCounterValues.action2);
    await expect((await getCounterValues(page)).actionListener2).toEqual(oldCounterValues.actionListener2);
    await expect((await getCounterValues(page)).ajaxListener2).toEqual(oldCounterValues.ajaxListener2);
    await expect((await getCounterValues(page)).action3).toEqual(oldCounterValues.action3);
    await expect((await getCounterValues(page)).actionListener3).toEqual(oldCounterValues.actionListener3);
    await expect((await getCounterValues(page)).ajaxListener3).toEqual(oldCounterValues.ajaxListener3);
  });

  test("Simple EventAjax", async ({page}) => {
    const buttonFn = page.locator("[id='page:mainForm:simpleEventAjax']");
    const oldCounterValues = await getCounterValues(page);

    await buttonFn.click();
    await expect((await getCounterValues(page)).buttonAction).toEqual(oldCounterValues.buttonAction);
    await expect((await getCounterValues(page)).buttonActionListener).toEqual(oldCounterValues.buttonActionListener);
    await expect((await getCounterValues(page)).action1).toEqual(oldCounterValues.action1);
    await expect((await getCounterValues(page)).actionListener1).toEqual(oldCounterValues.actionListener1);
    await expect((await getCounterValues(page)).ajaxListener1).toEqual(oldCounterValues.ajaxListener1 + 1);
    await expect((await getCounterValues(page)).action2).toEqual(oldCounterValues.action2);
    await expect((await getCounterValues(page)).actionListener2).toEqual(oldCounterValues.actionListener2);
    await expect((await getCounterValues(page)).ajaxListener2).toEqual(oldCounterValues.ajaxListener2);
    await expect((await getCounterValues(page)).action3).toEqual(oldCounterValues.action3);
    await expect((await getCounterValues(page)).actionListener3).toEqual(oldCounterValues.actionListener3);
    await expect((await getCounterValues(page)).ajaxListener3).toEqual(oldCounterValues.ajaxListener3);
  });

  test("Dropdown: Simple Event", async ({page}) => {
    const dropdownButtonFn = page.locator("[id='page:mainForm:dropdown::command']");
    const entryFn = page.locator("[id='page:mainForm:simpleEventEntry']");
    const oldCounterValues = await getCounterValues(page);

    await expect(entryFn).not.toBeVisible();
    await dropdownButtonFn.scrollIntoViewIfNeeded();
    await dropdownButtonFn.click();
    await expect(dropdownButtonFn).toHaveAttribute("aria-expanded", "true");
    await expect(entryFn).toBeVisible();

    await entryFn.click();
    await expect((await getCounterValues(page)).buttonAction).toEqual(oldCounterValues.buttonAction);
    await expect((await getCounterValues(page)).buttonActionListener).toEqual(oldCounterValues.buttonActionListener);
    await expect((await getCounterValues(page)).action1).toEqual(oldCounterValues.action1 + 1);
    await expect((await getCounterValues(page)).actionListener1).toEqual(oldCounterValues.actionListener1 + 1);
    await expect((await getCounterValues(page)).ajaxListener1).toEqual(oldCounterValues.ajaxListener1);
    await expect((await getCounterValues(page)).action2).toEqual(oldCounterValues.action2);
    await expect((await getCounterValues(page)).actionListener2).toEqual(oldCounterValues.actionListener2);
    await expect((await getCounterValues(page)).ajaxListener2).toEqual(oldCounterValues.ajaxListener2);
    await expect((await getCounterValues(page)).action3).toEqual(oldCounterValues.action3);
    await expect((await getCounterValues(page)).actionListener3).toEqual(oldCounterValues.actionListener3);
    await expect((await getCounterValues(page)).ajaxListener3).toEqual(oldCounterValues.ajaxListener3);
    await expect(entryFn).not.toBeVisible();
  });

  test("Dropdown: Simple Ajax", async ({page}) => {
    const dropdownButtonFn = page.locator("[id='page:mainForm:dropdown::command']");
    const entryFn = page.locator("[id='page:mainForm:simpleAjaxEntry']");
    const oldCounterValues = await getCounterValues(page);

    await expect(entryFn).not.toBeVisible();
    await dropdownButtonFn.scrollIntoViewIfNeeded();
    await dropdownButtonFn.click();
    await expect(dropdownButtonFn).toHaveAttribute("aria-expanded", "true");
    await expect(entryFn).toBeVisible();

    await entryFn.click();
    await expect((await getCounterValues(page)).buttonAction).toEqual(oldCounterValues.buttonAction);
    await expect((await getCounterValues(page)).buttonActionListener).toEqual(oldCounterValues.buttonActionListener);
    await expect((await getCounterValues(page)).action1).toEqual(oldCounterValues.action1);
    await expect((await getCounterValues(page)).actionListener1).toEqual(oldCounterValues.actionListener1);
    await expect((await getCounterValues(page)).ajaxListener1).toEqual(oldCounterValues.ajaxListener1 + 1);
    await expect((await getCounterValues(page)).action2).toEqual(oldCounterValues.action2);
    await expect((await getCounterValues(page)).actionListener2).toEqual(oldCounterValues.actionListener2);
    await expect((await getCounterValues(page)).ajaxListener2).toEqual(oldCounterValues.ajaxListener2);
    await expect((await getCounterValues(page)).action3).toEqual(oldCounterValues.action3);
    await expect((await getCounterValues(page)).actionListener3).toEqual(oldCounterValues.actionListener3);
    await expect((await getCounterValues(page)).ajaxListener3).toEqual(oldCounterValues.ajaxListener3);
    await expect(entryFn).not.toBeVisible();
  });

  test("Dropdown: Simple EventAjax", async ({page}) => {
    const dropdownButtonFn = page.locator("[id='page:mainForm:dropdown::command']");
    const entryFn = page.locator("[id='page:mainForm:simpleEventAjaxEntry']");
    const oldCounterValues = await getCounterValues(page);

    await expect(entryFn).not.toBeVisible();
    await dropdownButtonFn.scrollIntoViewIfNeeded();
    await dropdownButtonFn.click();
    await expect(dropdownButtonFn).toHaveAttribute("aria-expanded", "true");
    await expect(entryFn).toBeVisible();

    await entryFn.click();
    await expect((await getCounterValues(page)).buttonAction).toEqual(oldCounterValues.buttonAction);
    await expect((await getCounterValues(page)).buttonActionListener).toEqual(oldCounterValues.buttonActionListener);
    await expect((await getCounterValues(page)).action1).toEqual(oldCounterValues.action1);
    await expect((await getCounterValues(page)).actionListener1).toEqual(oldCounterValues.actionListener1);
    await expect((await getCounterValues(page)).ajaxListener1).toEqual(oldCounterValues.ajaxListener1 + 1);
    await expect((await getCounterValues(page)).action2).toEqual(oldCounterValues.action2);
    await expect((await getCounterValues(page)).actionListener2).toEqual(oldCounterValues.actionListener2);
    await expect((await getCounterValues(page)).ajaxListener2).toEqual(oldCounterValues.ajaxListener2);
    await expect((await getCounterValues(page)).action3).toEqual(oldCounterValues.action3);
    await expect((await getCounterValues(page)).actionListener3).toEqual(oldCounterValues.actionListener3);
    await expect((await getCounterValues(page)).ajaxListener3).toEqual(oldCounterValues.ajaxListener3);
    await expect(entryFn).not.toBeVisible();
  });

  test("Advanced Button: Option 1", async ({page}) => {
    const optionId = 0; //Event 1 + no Ajax enabled
    const buttonFn = page.locator("[id='page:mainForm:advancedButton']");
    const expectedCounterValues = await getCounterValues(page);
    expectedCounterValues.action1++;
    expectedCounterValues.actionListener1++;

    await testAdvancedButton(page, optionId, buttonFn, "dblclick", expectedCounterValues);
  });

  test("Advanced Button: Option 2", async ({page}) => {
    const optionId = 1; //Event 2 + Ajax 3 enabled
    const buttonFn = page.locator("[id='page:mainForm:advancedButton']");
    const expectedCounterValues = await getCounterValues(page);
    expectedCounterValues.buttonAction++;
    expectedCounterValues.buttonActionListener++;
    expectedCounterValues.ajaxListener3++;

    await testAdvancedButton(page, optionId, buttonFn, "dblclick", expectedCounterValues);
  });

  test("Advanced Button: Option 3", async ({page}) => {
    const optionId = 2; //Event 3 + all Ajax enabled
    const buttonFn = page.locator("[id='page:mainForm:advancedButton']");
    const expectedCounterValues = await getCounterValues(page);
    expectedCounterValues.buttonAction++;
    expectedCounterValues.buttonActionListener++;
    expectedCounterValues.ajaxListener1++;
    expectedCounterValues.ajaxListener2++;
    expectedCounterValues.ajaxListener3++;

    await testAdvancedButton(page, optionId, buttonFn, "click", expectedCounterValues);
  });

  test("Row: Option 1", async ({page}) => {
    const optionId = 0; //Event 1 + no Ajax enabled
    const rowFn = page.locator("[id='page:mainForm:sheet:0:row']");
    const expectedCounterValues = await getCounterValues(page);
    expectedCounterValues.action1++;
    expectedCounterValues.actionListener1++;

    await testAdvancedButton(page, optionId, rowFn, "dblclick", expectedCounterValues);
  });

  test("Row: Option 2", async ({page}) => {
    const optionId = 1; //Event 2 + Ajax 3 enabled
    const rowFn = page.locator("[id='page:mainForm:sheet:0:row']");
    const expectedCounterValues = await getCounterValues(page);
    expectedCounterValues.action2++;
    expectedCounterValues.actionListener2++;
    expectedCounterValues.ajaxListener3++;

    await testAdvancedButton(page, optionId, rowFn, "dblclick", expectedCounterValues);
  });

  test("Row: Option 3", async ({page}) => {
    const optionId = 2; //Event 3 + all Ajax enabled
    const rowFn = page.locator("[id='page:mainForm:sheet:0:row']");
    const expectedCounterValues = await getCounterValues(page);
    expectedCounterValues.ajaxListener1++;
    expectedCounterValues.ajaxListener2++;
    expectedCounterValues.action3++;
    expectedCounterValues.actionListener3++;
    expectedCounterValues.ajaxListener3++;

    await testAdvancedButton(page, optionId, rowFn, "click", expectedCounterValues);
  });

  test("Input: Click Event", async ({page}) => {
    const inputFn = page.locator("[id='page:mainForm:inputClick::field']");
    const hideFn = page.locator("[id='page:mainForm:hideOperationTextBox']");
    const operationOutFn = page.locator("[id='page:mainForm:operationOut']");

    await hideFn.click();
    await expect(operationOutFn).not.toBeVisible();
    await inputFn.click();
    await expect(operationOutFn).toBeVisible();
  });

  async function testAdvancedButton(page: Page, optionId: number, componentFn: Locator, eventType: string, expectedCounterValues: CounterValues) {
    const hideFn = page.locator("[id='page:mainForm:hideOperationTextBox']");
    const operationOutFn = page.locator("[id='page:mainForm:operationOut']");
    const optionFn = page.locator("[id='page:mainForm:advancedSelector::" + optionId + "']");

    await hideFn.click();
    await expect(operationOutFn).not.toBeVisible();
    await optionFn.click();
    await expect(optionFn).toBeChecked();

    if (eventType === "dblclick") {
      await componentFn.dblclick();
    } else {
      await componentFn.click();
    }
    await expect(operationOutFn).toBeVisible();
    await expect((await getCounterValues(page)).buttonAction).toBe(expectedCounterValues.buttonAction);
    await expect((await getCounterValues(page)).buttonActionListener).toBe(expectedCounterValues.buttonActionListener);
    await expect((await getCounterValues(page)).action1).toBe(expectedCounterValues.action1);
    await expect((await getCounterValues(page)).actionListener1).toBe(expectedCounterValues.actionListener1);
    await expect((await getCounterValues(page)).ajaxListener1).toBe(expectedCounterValues.ajaxListener1);
    await expect((await getCounterValues(page)).action2).toBe(expectedCounterValues.action2);
    await expect((await getCounterValues(page)).actionListener2).toBe(expectedCounterValues.actionListener2);
    await expect((await getCounterValues(page)).ajaxListener2).toBe(expectedCounterValues.ajaxListener2);
    await expect((await getCounterValues(page)).action3).toBe(expectedCounterValues.action3);
    await expect((await getCounterValues(page)).actionListener3).toBe(expectedCounterValues.actionListener3);
    await expect((await getCounterValues(page)).ajaxListener3).toBe(expectedCounterValues.ajaxListener3);
  }

  async function getCounterValues(page: Page) {
    const buttonActionCounter = page.locator("tobago-out[id='page:mainForm:buttonActionCounter'] span.form-control-plaintext");
    const buttonActionListenerCounter = page.locator("tobago-out[id='page:mainForm:buttonActionListenerCounter'] span.form-control-plaintext");
    const actionCount1 = page.locator("tobago-out[id='page:mainForm:actionCounter1'] span.form-control-plaintext");
    const actionListenerCount1 = page.locator("tobago-out[id='page:mainForm:actionListenerCounter1'] span.form-control-plaintext");
    const ajaxListenerCount1 = page.locator("tobago-out[id='page:mainForm:ajaxListenerCounter1'] span.form-control-plaintext");
    const actionCount2 = page.locator("tobago-out[id='page:mainForm:actionCounter2'] span.form-control-plaintext");
    const actionListenerCount2 = page.locator("tobago-out[id='page:mainForm:actionListenerCounter2'] span.form-control-plaintext");
    const ajaxListenerCount2 = page.locator("tobago-out[id='page:mainForm:ajaxListenerCounter2'] span.form-control-plaintext");
    const actionCount3 = page.locator("tobago-out[id='page:mainForm:actionCounter3'] span.form-control-plaintext");
    const actionListenerCount3 = page.locator("tobago-out[id='page:mainForm:actionListenerCounter3'] span.form-control-plaintext");
    const ajaxListenerCount3 = page.locator("tobago-out[id='page:mainForm:ajaxListenerCounter3'] span.form-control-plaintext");

    return new CounterValues(await parse(buttonActionCounter), await parse(buttonActionListenerCounter),
        await parse(actionCount1), await parse(actionListenerCount1), await parse(ajaxListenerCount1),
        await parse(actionCount2), await parse(actionListenerCount2), await parse(ajaxListenerCount2),
        await parse(actionCount3), await parse(actionListenerCount3), await parse(ajaxListenerCount3));
  }

  async function parse(element: Locator) {
    const textContent = await element.textContent();
    return parseInt(textContent.trim());
  }

  class CounterValues {
    buttonAction: number;
    buttonActionListener: number;
    action1: number;
    actionListener1: number;
    ajaxListener1;
    action2: number;
    actionListener2: number;
    ajaxListener2: number;
    action3: number;
    actionListener3: number;
    ajaxListener3: number;

    constructor(buttonAction: number, buttonActionListener: number,
                action1: number, actionListener1: number, ajaxListener1: number,
                action2: number, actionListener2: number, ajaxListener2: number,
                action3: number, actionListener3: number, ajaxListener3: number) {
      this.buttonAction = buttonAction;
      this.buttonActionListener = buttonActionListener;
      this.action1 = action1;
      this.actionListener1 = actionListener1;
      this.ajaxListener1 = ajaxListener1;
      this.action2 = action2;
      this.actionListener2 = actionListener2;
      this.ajaxListener2 = ajaxListener2;
      this.action3 = action3;
      this.actionListener3 = actionListener3;
      this.ajaxListener3 = ajaxListener3;
    }
  }
});
