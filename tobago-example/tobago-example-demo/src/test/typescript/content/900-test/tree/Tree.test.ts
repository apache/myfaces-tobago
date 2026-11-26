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

import {expect, Locator, test} from "@playwright/test";

test.describe("900-test/tree/Tree.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/tree/Tree.xhtml");
  });

  test("treeSelect without label", async ({page}) => {
    const tree = page.locator("[id='page:mainForm:treeSelectNoLabel']");
    const links = tree.locator(".tobago-link.btn.btn-link");

    await expect(links).toHaveCount(12);
  });

  test("treeShowRoot: Open and close Mathematics, then open Astronomy", async ({page}) => {
    const tree = page.locator("tobago-tree[id='page:mainForm:treeShowRoot']");
    const mathematicsNode = tree.locator("tobago-tree-node").filter({hasText: "Mathematics"});
    const mathematicsToggle = mathematicsNode.locator(".tobago-toggle");
    const astronomyNode = tree.locator("tobago-tree-node").filter({hasText: "Astronomy"});
    const astronomyToggle = astronomyNode.locator(".tobago-toggle");

    await checkTreeExpandingState(tree, true, true, true, false, true, false, false, false);
    await mathematicsToggle.click();
    await checkTreeExpandingState(tree, true, true, true, false, true, true, false, false);
    await mathematicsToggle.click();
    await checkTreeExpandingState(tree, true, true, true, false, true, false, false, false);
    await astronomyToggle.click();
    await checkTreeExpandingState(tree, true, true, true, false, true, false, true, false);
  });

  test("treeShowRoot: Open and close World, then open Mathematics", async ({page}) => {
    const tree = page.locator("tobago-tree[id='page:mainForm:treeShowRoot']");
    const worldNode = tree.locator("tobago-tree-node").filter({hasText: "World"});
    const worldToggle = worldNode.locator(".tobago-toggle");
    const mathematicsNode = tree.locator("tobago-tree-node").filter({hasText: "Mathematics"});
    const mathematicsToggle = mathematicsNode.locator(".tobago-toggle");

    await checkTreeExpandingState(tree, true, true, true, false, true, false, false, false);
    await worldToggle.click();
    await checkTreeExpandingState(tree, true, true, true, true, true, false, false, false);
    await worldToggle.click();
    await checkTreeExpandingState(tree, true, true, true, false, true, false, false, false);
    await mathematicsToggle.click();
    await checkTreeExpandingState(tree, true, true, true, false, true, true, false, false);
  });

  test("treeShowRoot: Close and open Category (root)", async ({page}) => {
    const tree = page.locator("tobago-tree[id='page:mainForm:treeShowRoot']");
    const categoryNode = tree.locator("tobago-tree-node").filter({hasText: "Category"});
    const categoryToggle = categoryNode.locator(".tobago-toggle");

    await checkTreeExpandingState(tree, true, true, true, false, true, false, false, false);
    await categoryToggle.click();
    await checkTreeExpandingState(tree, true, false, false, false, false, false, false, false);
    await categoryToggle.click();
    await checkTreeExpandingState(tree, true, true, true, false, true, false, false, false);
  });

  async function checkTreeExpandingState(tree: Locator, showRoot: boolean,
                                         categoryExpanded: boolean,
                                         musicExpanded: boolean, worldExpanded: boolean,
                                         scienceExpanded: boolean, mathematicsExpanded: boolean,
                                         astronomyExpanded: boolean, picturesExpanded: boolean): Promise<void> {

    const categoryNode = tree.locator("tobago-tree-node").filter({hasText: "Category"});
    const categoryToggleIcon = categoryNode.locator(".tobago-toggle i");
    const sportsNode = tree.locator("tobago-tree-node").filter({hasText: "Sports"});
    const moviesNode = tree.locator("tobago-tree-node").filter({hasText: "Movies"});
    const musicNode = tree.locator("tobago-tree-node").filter({hasText: "Music"});
    const musicToggleIcon = musicNode.locator(".tobago-toggle i");
    const classicNode = tree.locator("tobago-tree-node").filter({hasText: "Classic"});
    const popNode = tree.locator("tobago-tree-node").filter({hasText: "Pop"});
    const worldNode = tree.locator("tobago-tree-node").filter({hasText: "World"});
    const worldToggleIcon = worldNode.locator(".tobago-toggle i");
    const caribNode = tree.locator("tobago-tree-node").filter({hasText: "Carib"});
    const africaNode = tree.locator("tobago-tree-node").filter({hasText: "Africa"});
    const gamesNode = tree.locator("tobago-tree-node").filter({hasText: "Games"});
    const scienceNode = tree.locator("tobago-tree-node").filter({hasText: "Science"});
    const scienceToggleIcon = scienceNode.locator(".tobago-toggle i");
    const mathematicsNode = tree.locator("tobago-tree-node").filter({hasText: "Mathematics"});
    const mathematicsToggleIcon = mathematicsNode.locator(".tobago-toggle i");
    const analysisNode = tree.locator("tobago-tree-node").filter({hasText: "Analysis"});
    const algebraNode = tree.locator("tobago-tree-node").filter({hasText: "Algebra"});
    const geographyNode = tree.locator("tobago-tree-node").filter({hasText: "Geography"});
    const astronomyNode = tree.locator("tobago-tree-node").filter({hasText: "Astronomy"});
    const astronomyToggleIcon = astronomyNode.locator(".tobago-toggle i");
    const educationNode = tree.locator("tobago-tree-node").filter({hasText: "Education"});
    const picturesNode = tree.locator("tobago-tree-node").filter({hasText: "Pictures"});
    const picturesToggleIcon = picturesNode.locator(".tobago-toggle i");
    const ngcNode = tree.locator("tobago-tree-node").filter({hasText: "NGC"});
    const messierNode = tree.locator("tobago-tree-node").filter({hasText: "Messier"});

    await expect(categoryNode).toBeVisible({visible: showRoot});
    if (showRoot) {
      await expect(categoryToggleIcon).toHaveClass(categoryExpanded ? "bi-dash-square" : "bi-plus-square");
    }
    await expect(sportsNode).toBeVisible({visible: categoryExpanded});
    await expect(moviesNode).toBeVisible({visible: categoryExpanded});
    await expect(musicNode).toBeVisible({visible: categoryExpanded});
    if (categoryExpanded) {
      await expect(musicToggleIcon).toHaveClass(musicExpanded ? "bi-dash-square" : "bi-plus-square");
    }
    await expect(classicNode).toBeVisible({visible: categoryExpanded && musicExpanded});
    await expect(popNode).toBeVisible({visible: categoryExpanded && musicExpanded});
    await expect(worldNode).toBeVisible({visible: categoryExpanded && musicExpanded});
    if (categoryExpanded && musicExpanded) {
      await expect(worldToggleIcon).toHaveClass(worldExpanded ? "bi-dash-square" : "bi-plus-square");
    }
    await expect(caribNode).toBeVisible({visible: categoryExpanded && musicExpanded && worldExpanded});
    await expect(africaNode).toBeVisible({visible: categoryExpanded && musicExpanded && worldExpanded});
    await expect(gamesNode).toBeVisible({visible: categoryExpanded});
    await expect(scienceNode).toBeVisible({visible: categoryExpanded});
    if (categoryExpanded) {
      await expect(scienceToggleIcon).toHaveClass(scienceExpanded ? "bi-dash-square" : "bi-plus-square");
    }
    await expect(mathematicsNode).toBeVisible({visible: categoryExpanded && scienceExpanded});
    if (categoryExpanded && scienceExpanded) {
      await expect(mathematicsToggleIcon).toHaveClass(mathematicsExpanded ? "bi-dash-square" : "bi-plus-square");
    }
    await expect(analysisNode).toBeVisible({visible: categoryExpanded && scienceExpanded && mathematicsExpanded});
    await expect(algebraNode).toBeVisible({visible: categoryExpanded && scienceExpanded && mathematicsExpanded});
    await expect(geographyNode).toBeVisible({visible: categoryExpanded && scienceExpanded});
    await expect(astronomyNode).toBeVisible({visible: categoryExpanded && scienceExpanded});
    if (categoryExpanded && scienceExpanded) {
      await expect(astronomyToggleIcon).toHaveClass(astronomyExpanded ? "bi-dash-square" : "bi-plus-square");
    }
    await expect(educationNode).toBeVisible({visible: categoryExpanded && scienceExpanded && astronomyExpanded});
    await expect(picturesNode).toBeVisible({visible: categoryExpanded && scienceExpanded && astronomyExpanded});
    if (categoryExpanded && scienceExpanded && astronomyExpanded) {
      await expect(picturesToggleIcon).toHaveClass(picturesExpanded ? "bi-dash-square" : "bi-plus-square");
    }
    await expect(ngcNode).toBeVisible({visible: categoryExpanded && scienceExpanded && astronomyExpanded && picturesExpanded});
    await expect(messierNode).toBeVisible({visible: categoryExpanded && scienceExpanded && astronomyExpanded && picturesExpanded});
  }

  test("treeIndentWithIcon: tab through", async ({page}) => {
    const tree = page.locator("tobago-tree[id='page:mainForm:treeIndentWithIcon']");
    const musicToggle = tree.locator("tobago-tree-node").filter({hasText: "Music"}).locator(".tobago-toggle").first();
    const worldToggle = tree.locator("tobago-tree-node").filter({hasText: "World"}).locator(".tobago-toggle").first();
    const scienceToggle = tree.locator("tobago-tree-node").filter({hasText: "Science"}).locator(".tobago-toggle").first();
    const mathematicsToggle = tree.locator("tobago-tree-node").filter({hasText: "Mathematics"}).locator(".tobago-toggle").first();
    const astronomyToggle = tree.locator("tobago-tree-node").filter({hasText: "Astronomy"}).locator(".tobago-toggle").first();

    await musicToggle.focus();
    await expect(musicToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(worldToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(scienceToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(mathematicsToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(astronomyToggle).toBeFocused();

    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await expect(mathematicsToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(scienceToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(worldToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(musicToggle).toBeFocused();
    await page.keyboard.up("Shift");
  });

  test("treeIconWithoutIndent: tab through", async ({page}) => {
    const tree = page.locator("tobago-tree[id='page:mainForm:treeIconWithoutIndent']");
    const musicToggle = tree.locator("tobago-tree-node").filter({hasText: "Music"}).locator(".tobago-toggle").first();
    const worldToggle = tree.locator("tobago-tree-node").filter({hasText: "World"}).locator(".tobago-toggle").first();
    const scienceToggle = tree.locator("tobago-tree-node").filter({hasText: "Science"}).locator(".tobago-toggle").first();
    const mathematicsToggle = tree.locator("tobago-tree-node").filter({hasText: "Mathematics"}).locator(".tobago-toggle").first();
    const astronomyToggle = tree.locator("tobago-tree-node").filter({hasText: "Astronomy"}).locator(".tobago-toggle").first();

    await musicToggle.focus();
    await expect(musicToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(worldToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(scienceToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(mathematicsToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(astronomyToggle).toBeFocused();

    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await expect(mathematicsToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(scienceToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(worldToggle).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(musicToggle).toBeFocused();
    await page.keyboard.up("Shift");
  });
});
