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

  test("tc:button - click", async ({page}) => {
    await runStep(page, "button", "click", false,
        page.locator("[id='page:mainForm:buttonevent']"), page.locator("[id='page:mainForm:buttonajax']"),
        async (component: Locator) => await component.click());
  });
  test("tc:button - dblclick", async ({page}) => {
    await runStep(page, "button", "dblclick", false,
        page.locator("[id='page:mainForm:buttonevent']"), page.locator("[id='page:mainForm:buttonajax']"),
        async (component: Locator) => await component.dblclick());
  });
  test("tc:button - focus", async ({page}) => {
    await runStep(page, "button", "focus", false,
        page.locator("[id='page:mainForm:buttonevent']"), page.locator("[id='page:mainForm:buttonajax']"),
        async (component: Locator) => await component.focus());
  });
  test("tc:button - blur", async ({page}) => {
    await runStep(page, "button", "blur", false,
        page.locator("[id='page:mainForm:buttonevent']"), page.locator("[id='page:mainForm:buttonajax']"),
        async (component: Locator) => {
          const testBoxHeader = component.page().locator("[id='page:mainForm:compTestBox'] .tobago-box-header").first();
          await component.focus();
          await expect(component).toBeFocused();
          await testBoxHeader.click();
          await expect(component).not.toBeFocused();
        });
  });

  test("tc:in - change", async ({page}) => {
    await runStep(page, "in", "change", true,
        page.locator("[id='page:mainForm:inevent::field']"), page.locator("[id='page:mainForm:inajax::field']"),
        async (component: Locator) => {
          await component.fill("Alice");
          await component.blur();
        });
  });
  test("tc:in - click", async ({page}) => {
    await runStep(page, "in", "click", true,
        page.locator("[id='page:mainForm:inevent::field']"), page.locator("[id='page:mainForm:inajax::field']"),
        async (component: Locator) => {
          await component.fill("Bob");
          await component.click();
        });
  });
  test("tc:in - dblclick", async ({page}) => {
    await runStep(page, "in", "dblclick", true,
        page.locator("[id='page:mainForm:inevent::field']"), page.locator("[id='page:mainForm:inajax::field']"),
        async (component: Locator) => {
          await component.fill("Charlie");
          await component.dblclick();
        });
  });
  test("tc:in - focus", async ({page}) => {
    await runStep(page, "in", "focus", true,
        page.locator("[id='page:mainForm:inevent::field']"), page.locator("[id='page:mainForm:inajax::field']"),
        async (component: Locator) => {
          await component.evaluate((element) => (element as HTMLInputElement).value = "David");
          await component.focus();
        });
  });
  test("tc:in - blur", async ({page}) => {
    await runStep(page, "in", "blur", true,
        page.locator("[id='page:mainForm:inevent::field']"), page.locator("[id='page:mainForm:inajax::field']"),
        async (component: Locator) => {
          await component.fill("Eve");
          await component.blur();
        });
  });

  test("tc:row - click", async ({page}) => {
    await runStep(page, "row", "click", false,
        page.locator("[id='page:mainForm:sheetevent:0:selectPlanet']"), page.locator("[id='page:mainForm:sheetajax:0:selectPlanet']"),
        async (component: Locator) => await component.click());
  });
  test("tc:row - dblclick", async ({page}) => {
    await runStep(page, "row", "dblclick", false,
        page.locator("[id='page:mainForm:sheetevent:0:selectPlanet']"), page.locator("[id='page:mainForm:sheetajax:0:selectPlanet']"),
        async (component: Locator) => await component.dblclick());
  });

  test("tc:selectBooleanCheckbox - change", async ({page}) => {
    await runStep(page, "selectBooleanCheckbox", "change", true,
        page.locator("[id='page:mainForm:selectBooleanCheckboxevent::field']"), page.locator("[id='page:mainForm:selectBooleanCheckboxajax::field']"),
        async (component: Locator) => await component.click());
  });
  test("tc:selectBooleanCheckbox - click", async ({page}) => {
    await runStep(page, "selectBooleanCheckbox", "click", true,
        page.locator("[id='page:mainForm:selectBooleanCheckboxevent::field']"), page.locator("[id='page:mainForm:selectBooleanCheckboxajax::field']"),
        async (component: Locator) => await component.click());
  });
  test("tc:selectBooleanCheckbox - dblclick", async ({page}) => {
    await runStep(page, "selectBooleanCheckbox", "dblclick", true,
        page.locator("[id='page:mainForm:selectBooleanCheckboxevent::field']"), page.locator("[id='page:mainForm:selectBooleanCheckboxajax::field']"),
        async (component: Locator) => {
          await component.click();
          await component.dblclick();
        });
  });
  test("tc:selectBooleanCheckbox - focus", async ({page}) => {
    await runStep(page, "selectBooleanCheckbox", "focus", true,
        page.locator("[id='page:mainForm:selectBooleanCheckboxevent::field']"), page.locator("[id='page:mainForm:selectBooleanCheckboxajax::field']"),
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
  });
  test("tc:selectBooleanCheckbox - blur", async ({page}) => {
    await runStep(page, "selectBooleanCheckbox", "blur", true,
        page.locator("[id='page:mainForm:selectBooleanCheckboxevent::field']"), page.locator("[id='page:mainForm:selectBooleanCheckboxajax::field']"),
        async (component: Locator) => {
          await component.click();
          await component.focus(); // call .focus() explicit, because .click() is insufficient for Safari
          await expect(component).toBeFocused();
          const testBoxHeader = component.page().locator("[id='page:mainForm:compTestBox'] .tobago-box-header").first();
          await testBoxHeader.click();
        });
  });

  test("tc:selectOneList - change", async ({page}) => {
    await runStep(page, "selectOneList", "change", true,
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='alpha']"), page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='alpha']"),
        async (component: Locator) => await component.click());
  });
  test("tc:selectOneList - click", async ({page}) => {
    await runStep(page, "selectOneList", "click", true,
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='beta']"), page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='beta']"),
        async (component: Locator) => await component.click());
  });
  test("tc:selectOneList - dblclick", async ({page}) => {
    await runStep(page, "selectOneList", "dblclick", true,
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='gamma']"), page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='gamma']"),
        async (component: Locator) => {
          await component.click();
          await component.dblclick();
        });
  });
  test("tc:selectOneList - focus", async ({page}) => {
    await runStep(page, "selectOneList", "focus", true,
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='delta']"), page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='delta']"),
        async (component: Locator) => await component.click());
  });
  test("tc:selectOneList - blur", async ({page}) => {
    await runStep(page, "selectOneList", "blur", true,
        page.locator("[id='page:mainForm:selectOneListevent'] tr[data-tobago-value='alpha']"), page.locator("[id='page:mainForm:selectOneListajax'] tr[data-tobago-value='alpha']"),
        async (component: Locator) => {
          await component.click();
          const testBoxHeader = component.page().locator("[id='page:mainForm:compTestBox'] .tobago-box-header").first();
          await testBoxHeader.click();
        });
  });

  test("tc:selectOneListbox - change", async ({page}) => {
    await runStep(page, "selectOneListbox", "change", true,
        page.locator("[id='page:mainForm:selectOneListboxevent::field']"), page.locator("[id='page:mainForm:selectOneListboxajax::field']"),
        async (component: Locator) => await component.selectOption("Alpha"));
  });
  test("tc:selectOneListbox - click", async ({page}) => {
    await runStep(page, "selectOneListbox", "click", true,
        page.locator("[id='page:mainForm:selectOneListboxevent::field']"), page.locator("[id='page:mainForm:selectOneListboxajax::field']"),
        async (component: Locator) => await component.click());
  });
  test("tc:selectOneListbox - dblclick", async ({page}) => {
    // hasValueChangeListener=false because dblclick didn't trigger the valueChangeListener
    await runStep(page, "selectOneListbox", "dblclick", false,
        page.locator("[id='page:mainForm:selectOneListboxevent::field']"), page.locator("[id='page:mainForm:selectOneListboxajax::field']"),
        async (component: Locator) => await component.dblclick());
  });
  test("tc:selectOneListbox - focus", async ({page}) => {
    // hasValueChangeListener=false because focus didn't trigger the valueChangeListener for Edge and Chrome/Chromium
    await runStep(page, "selectOneListbox", "focus", false,
        page.locator("[id='page:mainForm:selectOneListboxevent::field']"), page.locator("[id='page:mainForm:selectOneListboxajax::field']"),
        async (component: Locator) => await component.focus());
  });
  test("tc:selectOneListbox - blur", async ({page}) => {
    await runStep(page, "selectOneListbox", "blur", true,
        page.locator("[id='page:mainForm:selectOneListboxevent::field']"), page.locator("[id='page:mainForm:selectOneListboxajax::field']"),
        async (component: Locator) => {
          await component.selectOption("Delta");
          await component.focus();
          const testBoxHeader = component.page().locator("[id='page:mainForm:compTestBox'] .tobago-box-header").first();
          await testBoxHeader.click();
        });
  });

  test("tc:textarea - change", async ({page}) => {
    await runStep(page, "textarea", "change", true,
        page.locator("[id='page:mainForm:textareaevent::field']"), page.locator("[id='page:mainForm:textareaajax::field']"),
        async (component: Locator) => {
          await component.fill("Alice");
          await component.blur();
        });
  });
  test("tc:textarea - click", async ({page}) => {
    await runStep(page, "textarea", "click", true,
        page.locator("[id='page:mainForm:textareaevent::field']"), page.locator("[id='page:mainForm:textareaajax::field']"),
        async (component: Locator) => {
          await component.fill("Bob");
          await component.click();
        });
  });
  test("tc:textarea - dblclick", async ({page}) => {
    await runStep(page, "textarea", "dblclick", true,
        page.locator("[id='page:mainForm:textareaevent::field']"), page.locator("[id='page:mainForm:textareaajax::field']"),
        async (component: Locator) => {
          await component.fill("Charlie");
          await component.dblclick();
        });
  });
  test("tc:textarea - focus", async ({page}) => {
    await runStep(page, "textarea", "focus", true,
        page.locator("[id='page:mainForm:textareaevent::field']"), page.locator("[id='page:mainForm:textareaajax::field']"),
        async (component: Locator) => {
          await component.evaluate((element) => (element as HTMLInputElement).value = "David");
          await component.focus();
        });
  });
  test("tc:textarea - blur", async ({page}) => {
    await runStep(page, "textarea", "blur", true,
        page.locator("[id='page:mainForm:textareaevent::field']"), page.locator("[id='page:mainForm:textareaajax::field']"),
        async (component: Locator) => {
          await component.fill("Eve");
          await component.blur();
        });
  });
  test("tc:textarea - input", async ({page}) => {
    await runStep(page, "textarea", "input", true,
        page.locator("[id='page:mainForm:textareaevent::field']"), page.locator("[id='page:mainForm:textareaajax::field']"),
        async (component: Locator) => {
          await component.evaluate((element) => (element as HTMLInputElement).value = "Fiona");
          await component.fill("Frank");
        });
  });

  async function runStep(page: Page, componentName: string, eventName: string, hasValueChangeListener: boolean,
                         eventComponent: Locator, ajaxComponent: Locator,
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
    await expect(page.locator("[id='page:mainForm:inAction::field']")).toBeFocused(); //wait for Tobago re-focus mechanism
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
