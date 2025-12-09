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

import {expect, test} from "@playwright/test";
import {Table} from "./base/bootstrap-variables";

test.describe("sheet/columnNode/ColumnNode.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/sheet/columnNode/ColumnNode.xhtml");
  });

  test("Open the 'World' node to see 'Carib' and 'Africa'", async ({page}) => {
    const reset = page.locator(".tobago-button[id='page:mainForm:reset']");
    await reset.click();

    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    await expect(sheet).toBeVisible();
    const treeNodes = sheet.locator("tobago-tree-node");
    await expect(treeNodes).toHaveCount(12);

    const outWorld = sheet
        .locator("tobago-tree-node tobago-out span.form-control-plaintext")
        .filter({hasText: "World"});
    const outCarib = sheet
        .locator("tobago-tree-node tobago-out span.form-control-plaintext")
        .filter({hasText: "Carib"});
    const outAfrica = sheet
        .locator("tobago-tree-node tobago-out span.form-control-plaintext")
        .filter({hasText: "Africa"});
    await expect(outWorld).toBeVisible();
    await expect(outCarib).not.toBeVisible();
    await expect(outAfrica).not.toBeVisible();

    const node6Toggle = sheet.locator("tobago-tree-node[index='6'] .tobago-toggle");
    await node6Toggle.click();

    await expect(treeNodes).toHaveCount(14);
    await expect(outWorld).toBeVisible();
    await expect(outCarib).toBeVisible();
    await expect(outAfrica).toBeVisible();
  });

  test("Expand 'Mathematics', collapse 'Science'; row must not be selected", async ({page}) => {
    const reset = page.locator(".tobago-button[id='page:mainForm:reset']");
    await reset.click();

    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    const hiddenSelected = sheet.locator("input[id='page:mainForm:sheet::selected']");
    await expect(hiddenSelected).toHaveValue("[]");

    const node8Out = sheet.locator("tobago-tree-node[index='8']  .form-control-plaintext");
    const node9Out = sheet.locator("tobago-tree-node[index='9']  .form-control-plaintext");
    const node10Out = sheet.locator("tobago-tree-node[index='10'] .form-control-plaintext");
    const node11Out = sheet.locator("tobago-tree-node[index='11'] .form-control-plaintext");
    const node12Out = sheet.locator("tobago-tree-node[index='12'] .form-control-plaintext");
    const node13Out = sheet.locator("tobago-tree-node[index='13'] .form-control-plaintext");

    await expect(node8Out).toBeVisible();
    await expect(node8Out).toHaveText("Science");
    await expect(node9Out).toBeVisible();
    await expect(node9Out).toHaveText("Mathematics");
    await expect(node10Out).toBeVisible();
    await expect(node10Out).toHaveText("Geography");
    await expect(node11Out).toBeVisible();
    await expect(node11Out).toHaveText("Astronomy");
    await expect(node12Out).not.toBeVisible();
    await expect(node13Out).not.toBeVisible();

    const node9Toggle = sheet.locator("tobago-tree-node[index='9'] .tobago-toggle");
    await node9Toggle.click();

    const row9cell = sheet.locator("tr[row-index='9'] td:first-child");
    await expect(row9cell).toHaveCSS("background-color", Table.bg);
    await expect(hiddenSelected).toHaveValue("[]");

    await expect(node8Out).toBeVisible();
    await expect(node8Out).toHaveText("Science");
    await expect(node9Out).toBeVisible();
    await expect(node9Out).toHaveText("Mathematics");
    await expect(node10Out).toBeVisible();
    await expect(node10Out).toHaveText("Analysis");
    await expect(node11Out).toBeVisible();
    await expect(node11Out).toHaveText("Algebra");
    await expect(node12Out).toBeVisible();
    await expect(node12Out).toHaveText("Geography");
    await expect(node13Out).toBeVisible();
    await expect(node13Out).toHaveText("Astronomy");

    const node8Toggle = sheet.locator("tobago-tree-node[index='8'] .tobago-toggle");
    await node8Toggle.click();

    const row8cell = sheet.locator("tr[row-index='8'] td:first-child");
    await expect(row8cell).toHaveCSS("background-color", Table.bg);
    await expect(hiddenSelected).toHaveValue("[]");

    await expect(node8Out).toBeVisible();
    await expect(node8Out).toHaveText("Science");
    await expect(node9Out).not.toBeVisible();
    await expect(node10Out).not.toBeVisible();
    await expect(node11Out).not.toBeVisible();
    await expect(node12Out).not.toBeVisible();
    await expect(node13Out).not.toBeVisible();
  });
});

test.describe("sheet/columnPanel/ColumnPanel.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("content/900-test/sheet/columnPanel/ColumnPanel.xhtml");
  });

  test("Markup 'striped'", async ({page}) => {
    const colorWhite = "rgb(255, 255, 255)";
    const boxShadowLightGray = "rgba(0, 0, 0, 0.05) 0px 0px 0px 9999px inset";
    const boxShadowTransparent = "rgba(0, 0, 0, 0) 0px 0px 0px 9999px inset";

    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    await expect(sheet).toBeVisible();

    const tableStriped = sheet.locator(".table-striped");
    await expect(tableStriped).toBeVisible();

    for (let i = 0; i < 17; i++) {
      const rowTd = tableStriped.locator("tr[row-index='" + i + "'] td:first-child");
      const columPanelTd = tableStriped.locator("tr[name='" + i + "'] td");

      await expect(rowTd).toHaveCSS("background-color", colorWhite);
      await expect(rowTd).toHaveCSS("box-shadow", i % 2 ? boxShadowTransparent : boxShadowLightGray);
      await expect(columPanelTd).toHaveCSS("background-color", colorWhite);
      await expect(columPanelTd).toHaveCSS("box-shadow", i % 2 ? boxShadowTransparent : boxShadowLightGray);
    }
  });
});
