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

import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";
import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";

it("Client: Select Tab 3", function (done) {
  let hiddenInputFn = elementByIdFn("page:mainForm:tabGroupClient::index");
  let tab1Fn = querySelectorFn("#page\\:mainForm\\:tab1Client .nav-link");
  let tab3Fn = querySelectorFn("#page\\:mainForm\\:tab3Client .nav-link");

  const test = new JasmineTestTool(done);
  test.setup(() => hiddenInputFn().value === "0",
      null, "click", tab1Fn);
  test.do(() => expect(hiddenInputFn().value).toBe("0"));
  test.do(() => expect(tab1Fn().classList).toContain("active"));
  test.do(() => expect(tab3Fn().classList).not.toContain("active"));
  test.event("click", tab3Fn, () => tab3Fn().classList.contains("active"))
  test.do(() => expect(hiddenInputFn().value).toBe("3"));
  test.do(() => expect(tab1Fn().classList).not.toContain("active"));
  test.do(() => expect(tab3Fn().classList).toContain("active"));
  test.start();
});

it("Ajax: Select Tab 3", function (done) {
  const tabGroup = elementByIdFn("page:mainForm:tabGroupAjax");
  const hiddenInputFn = elementByIdFn("page:mainForm:tabGroupAjax::index");
  const tab1Fn = querySelectorFn("#page\\:mainForm\\:tab1Ajax .nav-link");
  const tab3Fn = querySelectorFn("#page\\:mainForm\\:tab3Ajax .nav-link");

  const test = new JasmineTestTool(done);
  test.setup(() => hiddenInputFn().value === "0",
      null, "click", tab1Fn);
  test.do(() => expect(hiddenInputFn().value).toBe("0"));
  test.do(() => expect(tab1Fn().classList).toContain("active"));
  test.do(() => expect(tab3Fn().classList).not.toContain("active"));
  test.event("click", tab3Fn, () => tab3Fn().classList.contains("active"))
  test.do(() => expect(hiddenInputFn().value).toBe("3"));
  test.do(() => expect(tab1Fn().classList).not.toContain("active"));
  test.do(() => expect(tab3Fn().classList).toContain("active"));

  test.do(() => expect(tabGroup().classList).not.toContain("position-relative"));
  test.waitMs(3000);
  test.do(() => expect(tabGroup().classList).not.toContain("position-relative"));

  test.start();
});

it("FullReload: Select Tab 3", function (done) {
  let hiddenInputFn = elementByIdFn("page:mainForm:tabGroupFullReload::index");
  let tab1Fn = querySelectorFn("#page\\:mainForm\\:tab1FullReload .nav-link");
  let tab3Fn = querySelectorFn("#page\\:mainForm\\:tab3FullReload .nav-link");

  const test = new JasmineTestTool(done);
  test.setup(() => hiddenInputFn().value === "0",
      null, "click", tab1Fn);
  test.do(() => expect(hiddenInputFn().value).toBe("0"));
  test.do(() => expect(tab1Fn().classList).toContain("active"));
  test.do(() => expect(tab3Fn().classList).not.toContain("active"));
  test.event("click", tab3Fn, () => tab3Fn().classList.contains("active"))
  test.do(() => expect(hiddenInputFn().value).toBe("3"));
  test.do(() => expect(tab1Fn().classList).not.toContain("active"));
  test.do(() => expect(tab3Fn().classList).toContain("active"));
  test.start();
});
