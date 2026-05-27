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
import {Navbar, Root} from "./base/bootstrap-variables";

test.describe("link/Link.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/link/Link.xhtml");
  });

  test("tc:link: plain link - a.tobago-link vs button.tobago-link", async ({page}) => {
    const aLink = page.locator("a[id='page:mainForm:aLink']");
    await testPlainLink(aLink);

    const buttonLink = page.locator("button[id='page:mainForm:buttonLink']");
    await testPlainLink(buttonLink);
  });

  async function testPlainLink(component: Locator): Promise<void> {
    const tagName: string = await component.evaluate((element) => element.tagName);

    const boxShadow = "none";
    const color = Root.linkColor;
    const fontSize = "16px";
    const height = tagName === "BUTTON" ? "24px" : "auto"; //button-element cannot be "display: inline"
    const margin = "0px 0px 16px";
    const outline = false;
    const padding = "0px";
    const textDecoration = Root.linkDecoration;
    await testCSS(component, boxShadow, color, fontSize, height, margin, outline, padding, textDecoration);

    await component.hover();
    const hoverColor = Root.linkHoverColor;
    await testCSS(component, boxShadow, hoverColor, fontSize, height, margin, outline, padding, textDecoration);

    const sectionTitle = component.page().locator("tobago-section[id='page:mainForm:aVsButton'] > .tobago-header");
    await sectionTitle.click();
    await component.focus();
    const focusOutline = true;
    await testCSS(component, boxShadow, color, fontSize, height, margin, focusOutline, padding, textDecoration);
  }

  test("tc:link: nav-link - a.tobago-link vs button.tobago-link", async ({page}) => {
    const linksALink = page.locator("a[id='page:mainForm:linksALink']");
    await testNavLink(linksALink);

    const linksButtonLink = page.locator("button[id='page:mainForm:linksButtonLink']");
    await testNavLink(linksButtonLink);
  });

  async function testNavLink(component: Locator): Promise<void> {
    const boxShadow = "none";
    const color = Root.linkColor;
    const fontSize = "16px";
    const height = "40px";
    const margin = "0px";
    const outline = false;
    const padding = "8px 16px";
    const textDecoration = "none";
    await testCSS(component, boxShadow, color, fontSize, height, margin, outline, padding, textDecoration);

    await component.hover();
    const hoverColor = Root.linkHoverColor;
    await testCSS(component, boxShadow, hoverColor, fontSize, height, margin, outline, padding, textDecoration);

    const sectionTitle = component.page().locator("tobago-section[id='page:mainForm:aVsButton'] > .tobago-header");
    await sectionTitle.click();
    await component.focus();
    const focusBoxShadow = "rgba(13, 110, 253, 0.25) 0px 0px 0px 4px";
    const focusOutline = false;
    await testCSS(component, focusBoxShadow, hoverColor, fontSize, height, margin, focusOutline, padding, textDecoration);
  }

  test("tc:link: tc:bar plain link - a.tobago-link vs button.tobago-link", async ({page}) => {
    const barALink = page.locator("a[id='page:mainForm:barALink']");
    await testBarPlainLink(barALink);

    const barButtonLink = page.locator("button[id='page:mainForm:barButtonLink']");
    await testBarPlainLink(barButtonLink);
  });

  async function testBarPlainLink(component: Locator): Promise<void> {
    const boxShadow = "none";
    const color = Root.linkColor;
    const fontSize = "16px";
    const height = "24px";
    const margin = "0px";
    const outline = false;
    const padding = "0px";
    const textDecoration = Root.linkDecoration;
    await testCSS(component, boxShadow, color, fontSize, height, margin, outline, padding, textDecoration);

    await component.hover();
    const hoverColor = Root.linkHoverColor;
    await testCSS(component, boxShadow, hoverColor, fontSize, height, margin, outline, padding, textDecoration);

    const sectionTitle = component.page().locator("tobago-section[id='page:mainForm:aVsButton'] > .tobago-header");
    await sectionTitle.click();
    await component.focus();
    const focusOutline = true;
    await testCSS(component, boxShadow, color, fontSize, height, margin, focusOutline, padding, textDecoration);
  }

  test("tc:link: tc:bar nav-link - a.tobago-link vs button.tobago-link", async ({page}) => {
    const barLinksALink = page.locator("a[id='page:mainForm:barLinksALink']");
    await testBarNavLink(barLinksALink);

    const barLinksButtonLink = page.locator("button[id='page:mainForm:barLinksButtonLink']");
    await testBarNavLink(barLinksButtonLink);
  });

  async function testBarNavLink(component: Locator): Promise<void> {
    const boxShadow = "none";
    const color = Navbar.color;
    const fontSize = "16px";
    const height = "40px";
    const margin = "0px";
    const outline = false;
    const padding = Navbar.navLinkPaddingX; //padding-y is: Navbar.paddingY
    const textDecoration = "none";
    await testCSS(component, boxShadow, color, fontSize, height, margin, outline, padding, textDecoration);

    await component.hover();
    const hoverColor = Navbar.hoverColor;
    await testCSS(component, boxShadow, hoverColor, fontSize, height, margin, outline, padding, textDecoration);

    const sectionTitle = component.page().locator("tobago-section[id='page:mainForm:aVsButton'] > .tobago-header");
    await sectionTitle.click();
    await component.focus();
    const focusBoxShadow = "rgba(13, 110, 253, 0.25) 0px 0px 0px 4px";
    const focusOutline = false;
    await testCSS(component, focusBoxShadow, hoverColor, fontSize, height, margin, focusOutline, padding,
        textDecoration);
  }

  async function testCSS(component: Locator, boxShadow: string, color: string, fontSize: string, height: string,
                         margin: string, outline: boolean, padding: string, textDecoration: string): Promise<void> {
    await expect(component).toHaveCSS("box-shadow", boxShadow);
    await expect(component).toHaveCSS("color", color);
    await expect(component).toHaveCSS("font-size", fontSize);
    await expect(component).toHaveCSS("height", height);
    await expect(component).toHaveCSS("margin", margin);
    if (outline) {
      await expect(component).not.toHaveCSS("outline-style", "none");
      await expect(component).not.toHaveCSS("outline-width", "0px");
    } else {
      await expect(component).toHaveCSS("outline-style", "none");
    }
    await expect(component).toHaveCSS("padding", padding);
    await expect(component).toHaveCSS("text-decoration", textDecoration);
  }
});
