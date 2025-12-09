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
import {Color} from "./base/browser-styles";
import {Card, Nav, Root} from "./base/bootstrap-variables";

test.describe("tabGroup/style/Style.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/tabGroup/style/Style.xhtml");
  });

  test("tc:tabGroup: Tabgroup_Style", async ({page}) => {
    const tabGroup = page.locator("tobago-tab-group[id='page:mainForm:tabGroup2']");
    await expect(tabGroup).toHaveCSS("background-color", Root.white);

    const tabHeader = tabGroup.locator(".card-header");
    await expect(tabHeader).toHaveCSS("background-color", Card.capBg);

    const tabList = tabHeader.locator(".nav.nav-tabs.card-header-tabs");
    await expect(tabList).toHaveCSS("background-color", Color.transparent);

    await testTab(tabList.locator("tobago-tab[id='page:mainForm:tabNormal']"));
    await testTabDisabled(tabList.locator("tobago-tab[id='page:mainForm:tabDisabled']"));
    await testTab(tabList.locator("tobago-tab[id='page:mainForm:tabIcon']"));
    await testTabWithBarFacet(tabList.locator("tobago-tab[id='page:mainForm:tabBarFacetClose']"));
    await testTabWithBarFacetDisabled(tabList.locator("tobago-tab[id='page:mainForm:tabBarFacetCloseDisabled']"));
    await testTabWithBarFacet(tabList.locator("tobago-tab[id='page:mainForm:tabBarFacetLink']"));
    await testTabWithBarFacet(tabList.locator("tobago-tab[id='page:mainForm:tabBarFacetImage']"));
    await testTabWithBarFacet(tabList.locator("tobago-tab[id='page:mainForm:tabBarFacetText']"));
    await testTabWithBarFacet(tabList.locator("tobago-tab[id='page:mainForm:tabBarFacetCombine']"));

    const tabPaneActive = tabGroup.locator(".tab-pane.active");
    await expect(tabPaneActive).toHaveCSS("background-color", Color.transparent);
  });

  async function testTab(tab: Locator): Promise<void> {
    await expect(tab).toHaveCSS("height", "41px");
    await expect(tab).toHaveCSS("background-color", Color.transparent);
    await expect(tab).toHaveCSS("border-width", "0px");

    const navLink = tab.locator(".nav-link");
    await expect(navLink).toHaveCSS("color", Nav.linkColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", Nav.tabsBorderWidth);
    await expect(navLink).toHaveCSS("border-radius", "6px 6px 0px 0px");
    await expect(navLink).toHaveCSS("border-color", Color.transparent);

    await navLink.hover();
    await expect(navLink).toHaveCSS("color", Nav.linkHoverColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", Nav.tabsBorderWidth);
    await expect(navLink).toHaveCSS("border-left-color", Root.secondaryBg);
    await expect(navLink).toHaveCSS("border-top-color", Root.secondaryBg);
    await expect(navLink).toHaveCSS("border-right-color", Root.secondaryBg);
    await expect(navLink).toHaveCSS("border-bottom-color", Root.borderColor);

    await navLink.click();
    await expect(navLink).toHaveCSS("color", Nav.tabsLinkActiveColor);
    await expect(navLink).toHaveCSS("background-color", Card.bg);
    await expect(navLink).toHaveCSS("border-width", Nav.tabsBorderWidth);
    await expect(navLink).toHaveCSS("border-left-color", Root.borderColor);
    await expect(navLink).toHaveCSS("border-top-color", Root.borderColor);
    await expect(navLink).toHaveCSS("border-right-color", Root.borderColor);
    await expect(navLink).toHaveCSS("border-bottom-color", Root.white);
  }

  async function testTabWithBarFacet(tab: Locator): Promise<void> {
    await expect(tab).toHaveCSS("height", "41px");
    await expect(tab).toHaveCSS("background-color", Color.transparent);
    await expect(tab).toHaveCSS("border-width", "0px");

    const navLink = tab.locator(".nav-link");
    await expect(navLink).toHaveCSS("color", Nav.linkColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", "1px 0px 1px 1px");
    await expect(navLink).toHaveCSS("border-radius", "6px 0px 0px");
    await expect(navLink).toHaveCSS("border-left-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-top-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-bottom-color", Color.transparent);
    const barFacet = tab.locator(":scope > div");
    await expect(barFacet).toHaveCSS("background-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-width", "1px 1px 1px 0px");
    await expect(barFacet).toHaveCSS("border-radius", "0px 6px 0px 0px");
    await expect(barFacet).toHaveCSS("border-top-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-right-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-bottom-color", Color.transparent);

    await navLink.hover();
    await expect(navLink).toHaveCSS("color", Nav.linkHoverColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", "1px 0px 1px 1px");
    await expect(navLink).toHaveCSS("border-left-color", Root.secondaryBg);
    await expect(navLink).toHaveCSS("border-top-color", Root.secondaryBg);
    await expect(navLink).toHaveCSS("border-bottom-color", Root.borderColor);
    await expect(barFacet).toHaveCSS("background-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-width", "1px 1px 1px 0px");
    await expect(barFacet).toHaveCSS("border-top-color", Root.secondaryBg);
    await expect(barFacet).toHaveCSS("border-right-color", Root.secondaryBg);
    await expect(barFacet).toHaveCSS("border-bottom-color", Root.borderColor);

    await navLink.click();
    await expect(navLink).toHaveCSS("color", Nav.tabsLinkActiveColor);
    await expect(navLink).toHaveCSS("background-color", Card.bg);
    await expect(navLink).toHaveCSS("border-width", "1px 0px 1px 1px");
    await expect(navLink).toHaveCSS("border-left-color", Root.borderColor);
    await expect(navLink).toHaveCSS("border-top-color", Root.borderColor);
    await expect(navLink).toHaveCSS("border-bottom-color", Root.white);
    await expect(barFacet).toHaveCSS("background-color", Card.bg);
    await expect(barFacet).toHaveCSS("border-width", "1px 1px 1px 0px");
    await expect(barFacet).toHaveCSS("border-top-color", Root.borderColor);
    await expect(barFacet).toHaveCSS("border-right-color", Root.borderColor);
    await expect(barFacet).toHaveCSS("border-bottom-color", Root.white);
  }

  async function testTabDisabled(tab: Locator): Promise<void> {
    await expect(tab).toHaveCSS("height", "41px");
    await expect(tab).toHaveCSS("background-color", Color.transparent);
    await expect(tab).toHaveCSS("border-width", "0px");

    const navLink = tab.locator(".nav-link");
    await expect(navLink).toHaveCSS("color", Nav.linkDisabledColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", Nav.tabsBorderWidth);
    await expect(navLink).toHaveCSS("border-color", Color.transparent);

    await navLink.hover({force: true});
    await expect(navLink).toHaveCSS("color", Nav.linkDisabledColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", Nav.tabsBorderWidth);
    await expect(navLink).toHaveCSS("border-color", Color.transparent);

    await navLink.click({force: true});
    await expect(navLink).toHaveCSS("color", Nav.linkDisabledColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", Nav.tabsBorderWidth);
    await expect(navLink).toHaveCSS("border-color", Color.transparent);
  }

  async function testTabWithBarFacetDisabled(tab: Locator): Promise<void> {
    await expect(tab).toHaveCSS("height", "41px");
    await expect(tab).toHaveCSS("background-color", Color.transparent);
    await expect(tab).toHaveCSS("border-width", "0px");

    const navLink = tab.locator(".nav-link");
    await expect(navLink).toHaveCSS("color", Nav.linkDisabledColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", "1px 0px 1px 1px");
    await expect(navLink).toHaveCSS("border-radius", "6px 0px 0px");
    await expect(navLink).toHaveCSS("border-left-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-top-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-bottom-color", Color.transparent);
    const barFacet = tab.locator(":scope > div");
    await expect(barFacet).toHaveCSS("background-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-width", "1px 1px 1px 0px");
    await expect(barFacet).toHaveCSS("border-radius", "0px 6px 0px 0px");
    await expect(barFacet).toHaveCSS("border-top-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-right-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-bottom-color", Color.transparent);

    await navLink.hover({force: true});
    await expect(navLink).toHaveCSS("color", Nav.linkDisabledColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", "1px 0px 1px 1px");
    await expect(navLink).toHaveCSS("border-left-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-top-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-bottom-color", Color.transparent);
    await expect(barFacet).toHaveCSS("background-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-width", "1px 1px 1px 0px");
    await expect(barFacet).toHaveCSS("border-top-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-right-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-bottom-color", Color.transparent);

    await navLink.click({force: true});
    await expect(navLink).toHaveCSS("color", Nav.linkDisabledColor);
    await expect(navLink).toHaveCSS("background-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-width", "1px 0px 1px 1px");
    await expect(navLink).toHaveCSS("border-left-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-top-color", Color.transparent);
    await expect(navLink).toHaveCSS("border-bottom-color", Color.transparent);
    await expect(barFacet).toHaveCSS("background-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-width", "1px 1px 1px 0px");
    await expect(barFacet).toHaveCSS("border-top-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-right-color", Color.transparent);
    await expect(barFacet).toHaveCSS("border-bottom-color", Color.transparent);
  }
});

test.describe("tabGroup/file_immediate/File_immediate.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/tabGroup/file_immediate/File_immediate.xhtml");
  });

  test("Select tab 'Two', press OK, select tab 'One', press OK", async ({page}) => {
    const tabGroupIndex = page.locator("input[id='page:mainForm:tabgroup::index']");
    const tab1 = page.locator("tobago-tab[id='page:mainForm:nt1'] .nav-link");
    const tab2 = page.locator("tobago-tab[id='page:mainForm:nt2'] .nav-link");
    const file = page.locator("tobago-file[id='page:mainForm:nt1:file']");
    const output = page.locator("tobago-out[id='page:mainForm:nt2:output']");
    const messagesError = page.locator("tobago-messages[id='page:messages'] .alert-danger");
    const okButton = page.locator("button[id='page:mainForm:ok']");

    await tab2.click();
    await expect(tabGroupIndex).toHaveValue("1");
    await expect(file).not.toBeVisible();
    await expect(output).toBeVisible();

    await okButton.click();
    await expect(tabGroupIndex).toHaveValue("1");
    await expect(messagesError).not.toBeVisible();

    await tab1.click();
    await expect(tabGroupIndex).toHaveValue("0");
    await expect(file).toBeVisible();
    await expect(output).not.toBeVisible();

    await okButton.click();
    await expect(tabGroupIndex).toHaveValue("0");
    await expect(messagesError).toBeVisible();
  });
});
