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
import * as fs from "node:fs";
import * as path from "node:path";

const webappDir = path.join(__dirname, "../../../main/webapp");
const contentDir = path.join(webappDir, "content");

function getUrls(webappDir: string, contentDir: string): string[] {
  const urls: string[] = [];
  const entries = fs.readdirSync(contentDir, {withFileTypes: true, recursive: true});
  entries.forEach((entry) => {
    if (entry.isFile()) {
      const entryName = entry.name.toLowerCase();
      if (entryName.endsWith(".xhtml") && !entryName.startsWith("x-")
          && entryName !== "root_dummy.xhtml") {
        urls.push(path.sep + path.relative(webappDir, entry.parentPath) + path.sep + entry.name);
      }
    }
  });
  return urls;
}

for (const url of getUrls(webappDir, contentDir)) {
  test.describe(`General tests for ${url}`, () => {

    test.beforeEach(async ({page}, testInfo) => {
      await page.goto(url);
    });

    test("Duplicate ID", async ({page}) => {
      const duplicateIds = await page.evaluate(() => {
        const ids = Array.from(document.querySelectorAll("[id]")).map(el => el.id);
        const duplicates = ids.filter((id, index) => ids.indexOf(id) !== index);
        return Array.from(new Set(duplicates));
      });

      expect(duplicateIds, `Found duplicate IDs on ${url}: ${duplicateIds.join(", ")}`).toHaveLength(0);
    });

    test("test '???", async ({page}) => {
      const textContent = await page.evaluate(() => {
        return document.querySelector("html").textContent;
      });

      expect(textContent.indexOf("???"), "There must no '???' on " + url).toBeLessThanOrEqual(-1);
    });

    test("If there is a tobago-header it must be a tobago-footer", async ({page}) => {
      const headerExists = await page.evaluate(() => {
        return document.querySelector("tobago-page > form > tobago-header") !== null;
      });

      const footerExists = await page.evaluate(() => {
        return document.querySelector("tobago-page > form > tobago-footer") !== null;
      });

      expect(headerExists).toBe(footerExists);
    });
  });
}
