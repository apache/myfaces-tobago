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

import {querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("On click with ajax", function (done) {
  let oneClickAjaxFn = querySelectorFn("#page\\:mainForm\\:changeExample\\:\\:0");
  let venusFn = querySelectorFn("#page\\:mainForm\\:s1\\:2\\:sample0");
  let jupiterFn = querySelectorFn("#page\\:mainForm\\:s1\\:5\\:sample0");
  let saturnFn = querySelectorFn("#page\\:mainForm\\:s1\\:6\\:sample0");
  let namefieldFn = querySelectorFn("#page\\:mainForm\\:name\\:\\:field");

  let test = new JasmineTestTool(done);
  test.do(() => oneClickAjaxFn().checked = true);
  test.do(() => oneClickAjaxFn().dispatchEvent(new Event("change", {bubbles: true})));
  test.wait(() => venusFn());
  test.do(() => expect(venusFn() != null).toBe(true));
  test.do(() => expect(jupiterFn() != null).toBe(true));
  test.do(() => expect(saturnFn() != null).toBe(true));
  test.do(() => venusFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => namefieldFn() && namefieldFn().value === "Venus");
  test.do(() => expect(namefieldFn().value).toBe("Venus"));
  test.do(() => jupiterFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => namefieldFn() && namefieldFn().value === "Jupiter");
  test.do(() => expect(namefieldFn().value).toBe("Jupiter"));
  test.do(() => saturnFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => namefieldFn() && namefieldFn().value === "Saturn");
  test.do(() => expect(namefieldFn().value).toBe("Saturn"));
  test.start();
});

it("On click with full request", function (done) {
  let oneClickFullRequestFn = querySelectorFn("#page\\:mainForm\\:changeExample\\:\\:1");
  let venusFn = querySelectorFn("#page\\:mainForm\\:s1\\:2\\:sample1");
  let jupiterFn = querySelectorFn("#page\\:mainForm\\:s1\\:5\\:sample1");
  let saturnFn = querySelectorFn("#page\\:mainForm\\:s1\\:6\\:sample1");
  let namefieldFn = querySelectorFn("#page\\:mainForm\\:name\\:\\:field");

  let test = new JasmineTestTool(done);
  test.do(() => oneClickFullRequestFn().checked = true);
  test.do(() => oneClickFullRequestFn().dispatchEvent(new Event("change", {bubbles: true})));
  test.wait(() => venusFn());
  test.do(() => expect(venusFn() != null).toBe(true));
  test.do(() => expect(jupiterFn() != null).toBe(true));
  test.do(() => expect(saturnFn() != null).toBe(true));
  test.do(() => venusFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => namefieldFn() && namefieldFn().value === "Venus");
  test.do(() => expect(namefieldFn().value).toBe("Venus"));
  test.do(() => jupiterFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => namefieldFn() && namefieldFn().value === "Jupiter");
  test.do(() => expect(namefieldFn().value).toBe("Jupiter"));
  test.do(() => saturnFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => namefieldFn() && namefieldFn().value === "Saturn");
  test.do(() => expect(namefieldFn().value).toBe("Saturn"));
  test.start();
});

it("On double click with full request", function (done) {
  let doubleClickFullRequestFn = querySelectorFn("#page\\:mainForm\\:changeExample\\:\\:2");
  let venusFn = querySelectorFn("#page\\:mainForm\\:s1\\:2\\:sample2");
  let jupiterFn = querySelectorFn("#page\\:mainForm\\:s1\\:5\\:sample2");
  let saturnFn = querySelectorFn("#page\\:mainForm\\:s1\\:6\\:sample2");
  let namefieldFn = querySelectorFn("#page\\:mainForm\\:name\\:\\:field");

  let test = new JasmineTestTool(done);
  test.do(() => doubleClickFullRequestFn().checked = true);
  test.do(() => doubleClickFullRequestFn().dispatchEvent(new Event("change", {bubbles: true})));
  test.wait(() => venusFn());
  test.do(() => expect(venusFn() != null).toBe(true));
  test.do(() => expect(jupiterFn() != null).toBe(true));
  test.do(() => expect(saturnFn() != null).toBe(true));
  test.do(() => venusFn().dispatchEvent(new Event("dblclick", {bubbles: true})));
  test.wait(() => namefieldFn() && namefieldFn().value === "Venus");
  test.do(() => expect(namefieldFn().value).toBe("Venus"));
  test.do(() => jupiterFn().dispatchEvent(new Event("dblclick", {bubbles: true})));
  test.wait(() => namefieldFn() && namefieldFn().value === "Jupiter");
  test.do(() => expect(namefieldFn().value).toBe("Jupiter"));
  test.do(() => saturnFn().dispatchEvent(new Event("dblclick", {bubbles: true})));
  test.wait(() => namefieldFn() && namefieldFn().value === "Saturn");
  test.do(() => expect(namefieldFn().value).toBe("Saturn"));
  test.start();
});

it("Open popup on click with ajax", function (done) {
  let radioButtonFn = querySelectorFn("#page\\:mainForm\\:changeExample\\:\\:3");
  let venusFn = querySelectorFn("#page\\:mainForm\\:s1\\:2\\:sample3");
  let jupiterFn = querySelectorFn("#page\\:mainForm\\:s1\\:5\\:sample3");
  let saturnFn = querySelectorFn("#page\\:mainForm\\:s1\\:6\\:sample3");
  let popupFn = querySelectorFn("#page\\:mainForm\\:popup");
  let nameFn = querySelectorFn("#page\\:mainForm\\:popup\\:popupName\\:\\:field");
  let cancelFn = querySelectorFn("#page\\:mainForm\\:popup\\:cancel");

  let test = new JasmineTestTool(done);
  test.do(() => radioButtonFn().checked = true);
  test.do(() => radioButtonFn().dispatchEvent(new Event("change", {bubbles: true})));
  test.wait(() => venusFn());
  test.do(() => expect(venusFn() != null).toBe(true));
  test.do(() => expect(jupiterFn() != null).toBe(true));
  test.do(() => expect(saturnFn() != null).toBe(true));
  test.do(() => venusFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => popupFn() && popupFn().classList.contains("show") === true);
  test.do(() => expect(popupFn().classList.contains("show")).toBe(true));
  test.do(() => expect(nameFn().value).toBe("Venus"));
  test.do(() => cancelFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => popupFn() && popupFn().classList.contains("show") !== true);
  test.do(() => expect(popupFn().classList.contains("show")).not.toBe(true));
  test.do(() => jupiterFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => popupFn() && popupFn().classList.contains("show") === true);
  test.do(() => expect(popupFn().classList.contains("show")).toBe(true));
  test.do(() => expect(nameFn().value).toBe("Jupiter"));
  test.do(() => cancelFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => popupFn() && popupFn().classList.contains("show") !== true);
  test.do(() => expect(popupFn().classList.contains("show")).not.toBe(true));
  test.do(() => saturnFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => popupFn() && popupFn().classList.contains("show") === true);
  test.do(() => expect(popupFn().classList.contains("show")).toBe(true));
  test.do(() => expect(nameFn().value).toBe("Saturn"));
  test.do(() => cancelFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => popupFn() && popupFn().classList.contains("show") !== true);
  test.do(() => expect(popupFn().classList.contains("show")).not.toBe(true));
  test.start();
});
