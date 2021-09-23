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

it("Accordion: Box 1: 'hide' to 'show' to 'hide'", function (done) {
  let boxFn = querySelectorFn("#page\\:mainForm\\:accordionBox1");
  let showBoxFn = querySelectorFn("#page\\:mainForm\\:showBox1");
  let hideBoxFn = querySelectorFn("#page\\:mainForm\\:hideBox1");
  let boxBodyFn = querySelectorFn("#page\\:mainForm\\:accordionBox1 .card-body");

  let test = new JasmineTestTool(done);
  test.do(() => expect(showBoxFn() !== null).toBe(true));
  test.do(() => expect(hideBoxFn() === null).toBe(true));
  test.do(() => expect(boxBodyFn().textContent.trim()).toBe(""));
  test.do(() => showBoxFn().click());
  test.wait(() => showBoxFn() === null && hideBoxFn() !== null && boxBodyFn().textContent.trim() !== null);
  test.do(() => expect(showBoxFn() === null).toBe(true));
  test.do(() => expect(hideBoxFn() !== null).toBe(true));
  test.do(() => expect(boxBodyFn().textContent.trim()).not.toBe(null));
  test.do(() => hideBoxFn().click());
  test.wait(() => showBoxFn() !== null && hideBoxFn() === null && boxBodyFn().textContent.trim() === "");
  test.do(() => expect(showBoxFn() !== null).toBe(true));
  test.do(() => expect(hideBoxFn() === null).toBe(true));
  test.do(() => expect(boxBodyFn().textContent.trim()).toBe(""));
  test.start();
});

it("Accordion: Box 2: 'hide' to 'show' to 'hide'", function (done) {
  let boxFn = querySelectorFn("#page\\:mainForm\\:accordionBox2");
  let showBoxFn = querySelectorFn("#page\\:mainForm\\:showBox2");
  let hideBoxFn = querySelectorFn("#page\\:mainForm\\:hideBox2");
  let boxBodyFn = querySelectorFn("#page\\:mainForm\\:accordionBox2 .card-body");

  let test = new JasmineTestTool(done);
  test.do(() => expect(showBoxFn() !== null).toBe(true));
  test.do(() => expect(hideBoxFn() === null).toBe(true));
  test.do(() => expect(boxBodyFn().textContent.trim()).toBe(""));
  test.do(() => showBoxFn().click());
  test.wait(() => showBoxFn() === null && hideBoxFn() !== null && boxBodyFn().textContent.trim() !== null);
  test.do(() => expect(showBoxFn() === null).toBe(true));
  test.do(() => expect(hideBoxFn() !== null).toBe(true));
  test.do(() => expect(boxBodyFn().textContent.trim()).not.toBe(null));
  test.do(() => hideBoxFn().click());
  test.wait(() => showBoxFn() !== null && hideBoxFn() === null && boxBodyFn().textContent.trim() === "");
  test.do(() => expect(showBoxFn() !== null).toBe(true));
  test.do(() => expect(hideBoxFn() === null).toBe(true));
  test.do(() => expect(boxBodyFn().textContent.trim()).toBe(""));
  test.start();
});

it("Accordion: Box 3: 'hide' to 'show' to 'hide'", function (done) {
  let boxFn = querySelectorFn("#page\\:mainForm\\:accordionBox3");
  let showBoxFn = querySelectorFn("#page\\:mainForm\\:showBox3");
  let hideBoxFn = querySelectorFn("#page\\:mainForm\\:hideBox3");

  let test = new JasmineTestTool(done);
  test.do(() => expect(boxFn().classList.contains("tobago-collapsed")).toBe(true));
  test.do(() => expect(showBoxFn() !== null).toBe(true));
  test.do(() => expect(hideBoxFn() === null).toBe(true));
  test.do(() => showBoxFn().click());
  test.wait(() => boxFn() && boxFn().classList.contains("tobago-collapsed") !== true);
  test.do(() => expect(boxFn().classList.contains("tobago-collapsed")).not.toBe(true));
  test.do(() => expect(showBoxFn() === null).toBe(true));
  test.do(() => expect(hideBoxFn() !== null).toBe(true));
  test.do(() => hideBoxFn().click());
  test.wait(() => boxFn() && boxFn().classList.contains("tobago-collapsed") === true);
  test.do(() => expect(showBoxFn() !== null).toBe(true));
  test.do(() => expect(hideBoxFn() === null).toBe(true));
  test.start();
});
