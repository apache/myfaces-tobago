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

import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("compare a.link and button.link", function (done) {
  const aLinkText = querySelectorFn("#page\\:mainForm\\:aLink span");
  const buttonLinkText = querySelectorFn("#page\\:mainForm\\:buttonLink span");

  const test = new JasmineTestTool(done);
  test.do(() => expect(aLinkText().offsetLeft).toBe(buttonLinkText().offsetLeft));
  test.start();
});

it("Dropdown menu must have three entries", function (done) {
  const dropdown = elementByIdFn("page:mainForm:dropdownRepeat");

  const test = new JasmineTestTool(done);
  test.do(() => expect(dropdown()).not.toBeNull());
  test.do(() => expect(dropdown().querySelectorAll(".dropdown-item").length).toBe(3));
  test.do(() => expect(dropdown().querySelectorAll(".dropdown-item")[0].textContent).toBe("Nile"));
  test.do(() => expect(dropdown().querySelectorAll(".dropdown-item")[1].textContent).toBe("Amazon"));
  test.do(() => expect(dropdown().querySelectorAll(".dropdown-item")[2].textContent).toBe("Yangtze"));
  test.start();
});
