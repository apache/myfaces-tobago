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

type EventFunction = (component: Locator) => Promise<any>;

test.describe("900-test/event/Event.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/event/Event.xhtml");
  });

  test("tc:button", async ({page}) => {
    const eventComponent = page.locator("[id='page:mainForm:buttonevent']");
    const ajaxComponent = page.locator("[id='page:mainForm:buttonajax']");

    await runStep(page, "button", "click", eventComponent, ajaxComponent, false,
        async (component: Locator) => await component.click());
    await runStep(page, "button", "dblclick", eventComponent, ajaxComponent, false,
        async (component: Locator) => await component.dblclick());
    await runStep(page, "button", "focus", eventComponent, ajaxComponent, false,
        async (component: Locator) => await component.focus());
    await runStep(page, "button", "blur", eventComponent, ajaxComponent, false,
        async (component: Locator) => {
          const testBoxHeader = component.page().locator("[id='page:mainForm:compTestBox'] .tobago-box-header").first();
          await component.focus();
          await expect(component).toBeFocused();
          await testBoxHeader.click();
          await expect(component).not.toBeFocused();
        });
  });

  test("tc:in", async ({page}) => {
    const eventComponent = page.locator("[id='page:mainForm:inevent::field']");
    const ajaxComponent = page.locator("[id='page:mainForm:inajax::field']");

    await runStep(page, "in", "change", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.fill("Alice");
          await component.blur();
        });
    await runStep(page, "in", "click", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.fill("Bob");
          await component.click();
        });
    await runStep(page, "in", "dblclick", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.fill("Charlie");
          await component.dblclick();
        });
    await runStep(page, "in", "focus", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.evaluate((element) => (element as HTMLInputElement).value = "David");
          await component.focus();
        });
    await runStep(page, "in", "blur", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.fill("Eve");
          await component.blur();
        });
  });

  test("tc:row", async ({page}) => {
    const eventComponent = page.locator("[id='page:mainForm:sheetevent:0:selectPlanet']");
    const ajaxComponent = page.locator("[id='page:mainForm:sheetajax:0:selectPlanet']");

    await runStep(page, "row", "click", eventComponent, ajaxComponent, false,
        async (component: Locator) => await component.click());
    await runStep(page, "row", "dblclick", eventComponent, ajaxComponent, false,
        async (component: Locator) => await component.dblclick());
  });

  test("tc:selectBooleanCheckbox", async ({page}) => {
    const eventComponent = page.locator("[id='page:mainForm:selectBooleanCheckboxevent::field']");
    const ajaxComponent = page.locator("[id='page:mainForm:selectBooleanCheckboxajax::field']");

    await runStep(page, "selectBooleanCheckbox", "change", eventComponent, ajaxComponent, true,
        async (component: Locator) => await component.click());
    await runStep(page, "selectBooleanCheckbox", "click", eventComponent, ajaxComponent, true,
        async (component: Locator) => await component.click());
    await runStep(page, "selectBooleanCheckbox", "dblclick", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.click();
          await component.dblclick();
        });
    await runStep(page, "selectBooleanCheckbox", "focus", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          const oldCheckboxValue: boolean = await component.isChecked();
          const newCheckboxValue: boolean = !oldCheckboxValue;
          await component.evaluate((element, newValue: boolean) => {
            (element as HTMLInputElement).checked = newValue;
          }, newCheckboxValue);
          if (newCheckboxValue) {
            await expect(component).toBeChecked();
          } else {
            await expect(component).not.toBeChecked();
          }
          await component.focus();
        });
    await runStep(page, "selectBooleanCheckbox", "blur", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.click();
          await component.focus(); // call .focus() explicit, because .click() is insufficient for Safari
          await expect(component).toBeFocused();
          const testBoxHeader = component.page().locator("[id='page:mainForm:compTestBox'] .tobago-box-header").first();
          await testBoxHeader.click();
        });
  });

  test("tc:selectOneList", async ({page}) => {
    await selectCompEvent(page, "selectOneList", "change");

    await runStep(page, "selectOneList", "change",
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='alpha']"),
        page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='alpha']"),
        true,
        async (component: Locator) => await component.click());
    await runStep(page, "selectOneList", "click",
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='beta']"),
        page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='beta']"),
        true,
        async (component: Locator) => await component.click());
    await runStep(page, "selectOneList", "dblclick",
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='gamma']"),
        page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='gamma']"),
        true,
        async (component: Locator) => {
          await component.click();
          await component.dblclick();
        });
    await runStep(page, "selectOneList", "focus",
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='delta']"),
        page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='delta']"),
        true,
        async (component: Locator) => await component.click());
    await runStep(page, "selectOneList", "blur",
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='alpha']"),
        page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='alpha']"),
        true,
        async (component: Locator) => {
          await component.click();
          const testBoxHeader = component.page().locator("[id='page:mainForm:compTestBox'] .tobago-box-header").first();
          await testBoxHeader.click();
        });
  });

  test("tc:selectOneListbox", async ({page}) => {
    const eventComponent = page.locator("[id='page:mainForm:selectOneListboxevent::field']");
    const ajaxComponent = page.locator("[id='page:mainForm:selectOneListboxajax::field']");

    await runStep(page, "selectOneListbox", "change", eventComponent, ajaxComponent, true,
        async (component: Locator) => await component.selectOption("Alpha"));
    await runStep(page, "selectOneListbox", "click", eventComponent, ajaxComponent, true,
        async (component: Locator) => await component.click());

    // hasValueChangeListener=false because dblclick didn't trigger the valueChangeListener
    await runStep(page, "selectOneListbox", "dblclick", eventComponent, ajaxComponent, false,
        async (component: Locator) => await component.dblclick());

    // hasValueChangeListener=false because focus didn't trigger the valueChangeListener for Edge and Chrome/Chromium
    await runStep(page, "selectOneListbox", "focus", eventComponent, ajaxComponent, false,
        async (component: Locator) => await component.focus());
    await runStep(page, "selectOneListbox", "blur", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.selectOption("Delta");
          await component.focus();
          const testBoxHeader = component.page().locator("[id='page:mainForm:compTestBox'] .tobago-box-header").first();
          await testBoxHeader.click();
        });
  });

  test("tc:textarea", async ({page}) => {
    const eventComponent = page.locator("[id='page:mainForm:textareaevent::field']");
    const ajaxComponent = page.locator("[id='page:mainForm:textareaajax::field']");

    await runStep(page, "textarea", "change", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.fill("Alice");
          await component.blur();
        });
    await runStep(page, "textarea", "click", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.fill("Bob");
          await component.click();
        });
    await runStep(page, "textarea", "dblclick", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.fill("Charlie");
          await component.dblclick();
        });
    await runStep(page, "textarea", "focus", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.evaluate((element) => (element as HTMLInputElement).value = "David");
          await component.focus();
        });
    await runStep(page, "textarea", "blur", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.fill("Eve");
          await component.blur();
        });
    await runStep(page, "textarea", "input", eventComponent, ajaxComponent, true,
        async (component: Locator) => {
          await component.evaluate((element) => (element as HTMLInputElement).value = "Fiona");
          await component.fill("Frank");
        });
  });

  async function runStep(page: Page, componentName: string, eventName: string,
                         eventComponent: Locator, ajaxComponent: Locator, hasValueChangeListener: boolean,
                         eventFunction: EventFunction): Promise<void> {
    await selectCompEvent(page, componentName, eventName);
    const timestamp = page.locator("[id='page:mainForm:inTimestamp::field']");
    let timestampValue = await timestamp.inputValue();
    await eventFunction(eventComponent);
    await expect(timestamp).not.toHaveValue(timestampValue);
    await expectCount(page, 1, 1, 0, hasValueChangeListener ? 1 : 0);
    timestampValue = await timestamp.inputValue();
    await eventFunction(ajaxComponent);
    await expect(timestamp).not.toHaveValue(timestampValue);
    await expectCount(page, 1, 1, 1, hasValueChangeListener ? 2 : 0);
  }

  async function selectCompEvent(page: Page, componentName: string, eventName: string): Promise<void> {
    const timestamp = page.locator("[id='page:mainForm:inTimestamp::field']");
    const timestampValue = await timestamp.inputValue();
    const rowIndex = await getRowIndex(page, componentName);
    const selectorButton = page.locator(`[id='page:mainForm:componentTable:${rowIndex}:${eventName}Behavior']`);
    await selectorButton.click();
    await expect(timestamp).not.toHaveValue(timestampValue);
  }

  async function getRowIndex(page: Page, componentName: string): Promise<string> {
    const tagNames = page.locator("[id='page:mainForm:componentTable'] tr td:first-child .form-control-plaintext");
    const rowIndex = await tagNames.evaluateAll((tagNames, componentName) => {
      for (const tagName of tagNames) {
        if (tagName.textContent === componentName) {
          const tr = tagName.closest("tr") as HTMLTableRowElement;
          return tr.getAttribute("row-index");
        }
      }
      return "";
    }, componentName);

    return rowIndex as string;
  }

  async function expectCount(page: Page, actionCount: number, actionListenerCount: number, ajaxListenerCount: number,
                             valueChangeListenerCount: number): Promise<void> {
    const actionCounter = page.locator("[id='page:mainForm:inAction::field']");
    const actionListenerCounter = page.locator("[id='page:mainForm:inActionListener::field']");
    const ajaxListenerCounter = page.locator("[id='page:mainForm:inAjaxListener::field']");
    const valueChangeListenerCounter = page.locator("[id='page:mainForm:inValueChangeListener::field']");

    await expect(actionCounter).toHaveValue(String(actionCount));
    await expect(actionListenerCounter).toHaveValue(String(actionListenerCount));
    await expect(ajaxListenerCounter).toHaveValue(String(ajaxListenerCount));
    await expect(valueChangeListenerCounter).toHaveValue(String(valueChangeListenerCount));
  }
});
