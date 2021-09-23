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
import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";

it("submit: addAll, removeAll, addItem0to4, removeItem2to3", function (done) {
  let unselectedOptions = querySelectorAllFn("#page\\:mainForm\\:submitExample\\:\\:unselected option");
  let selectedOptions = querySelectorAllFn("#page\\:mainForm\\:submitExample\\:\\:selected option");
  let addAllButton = querySelectorFn("#page\\:mainForm\\:submitExample\\:\\:addAll");
  let addButton = querySelectorFn("#page\\:mainForm\\:submitExample\\:\\:add");
  let removeButton = querySelectorFn("#page\\:mainForm\\:submitExample\\:\\:remove");
  let removeAllButton = querySelectorFn("#page\\:mainForm\\:submitExample\\:\\:removeAll");
  let submitButton = querySelectorFn("#page\\:mainForm\\:submitButton");
  let output = querySelectorFn("#page\\:mainForm\\:submitExampleOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => selectedOptions().length === 0, null, "click", removeAllButton);
  test.setup(() => output().textContent === "[]", null, "click", submitButton);

  test.event("click", addAllButton, () => selectedOptions().length === 9);
  test.do(() => expect(unselectedOptions().length).toBe(0));
  test.do(() => expect(selectedOptions().length).toBe(9));
  test.event("click", submitButton,
      () => output().textContent === "[Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto]");
  test.do(() => expect(output().textContent)
      .toBe("[Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto]"));

  test.event("click", removeAllButton, () => selectedOptions().length === 0);
  test.do(() => expect(unselectedOptions().length).toBe(9));
  test.do(() => expect(selectedOptions().length).toBe(0));
  test.event("click", submitButton, () => output().textContent === "[]");
  test.do(() => expect(output().textContent).toBe("[]"));

  test.do(() => unselectedOptions().item(0).selected = true);
  test.do(() => unselectedOptions().item(1).selected = true);
  test.do(() => unselectedOptions().item(2).selected = true);
  test.do(() => unselectedOptions().item(3).selected = true);
  test.do(() => unselectedOptions().item(4).selected = true);
  test.event("click", addButton, () => selectedOptions().length === 5);
  test.do(() => expect(unselectedOptions().length).toBe(4));
  test.do(() => expect(selectedOptions().length).toBe(5));
  test.event("click", submitButton, () => output().textContent === "[Mercury, Venus, Earth, Mars, Jupiter]");
  test.do(() => expect(output().textContent).toBe("[Mercury, Venus, Earth, Mars, Jupiter]"));

  test.do(() => selectedOptions().item(2).selected = true);
  test.do(() => selectedOptions().item(3).selected = true);
  test.event("click", removeButton, () => selectedOptions().length === 3);
  test.do(() => expect(unselectedOptions().length).toBe(6));
  test.do(() => expect(selectedOptions().length).toBe(3));
  test.event("click", submitButton, () => output().textContent === "[Mercury, Venus, Jupiter]");
  test.do(() => expect(output().textContent).toBe("[Mercury, Venus, Jupiter]"));

  const pageOverlays = querySelectorAllFn("tobago-overlay");
  test.do(() => expect(pageOverlays().length).toBe(0));

  test.start();
});

it("ajax: addAll, removeAll, addItem1to2, removeItem0", function (done) {
  let unselectedOptions = querySelectorAllFn("#page\\:mainForm\\:ajaxExample\\:\\:unselected option");
  let selectedOptions = querySelectorAllFn("#page\\:mainForm\\:ajaxExample\\:\\:selected option");
  let addAllButton = querySelectorFn("#page\\:mainForm\\:ajaxExample\\:\\:addAll");
  let addButton = querySelectorFn("#page\\:mainForm\\:ajaxExample\\:\\:add");
  let removeButton = querySelectorFn("#page\\:mainForm\\:ajaxExample\\:\\:remove");
  let removeAllButton = querySelectorFn("#page\\:mainForm\\:ajaxExample\\:\\:removeAll");
  let output = querySelectorFn("#page\\:mainForm\\:outputStars .form-control-plaintext");
  let submitButton = querySelectorFn("#page\\:mainForm\\:submitButton");

  const test = new JasmineTestTool(done);
  test.setup(() => output().textContent === "[]", null, "click", removeAllButton);
  test.setup(() => unselectedOptions().item(0).text === "Proxima Centauri"
      && unselectedOptions().item(1).text === "Alpha Centauri"
      && unselectedOptions().item(2).text === "Wolf 359"
      && unselectedOptions().item(3).text === "Sirius",
      null, "click", submitButton);

  test.event("click", addAllButton,
      () => output().textContent === "[Proxima Centauri, Alpha Centauri, Wolf 359, Sirius]");
  test.do(() => expect(unselectedOptions().length).toBe(0));
  test.do(() => expect(selectedOptions().length).toBe(4));
  test.do(() => expect(output().textContent).toBe("[Proxima Centauri, Alpha Centauri, Wolf 359, Sirius]"));

  test.event("click", removeAllButton, () => output().textContent === "[]");
  test.do(() => expect(unselectedOptions().length).toBe(4));
  test.do(() => expect(selectedOptions().length).toBe(0));
  test.do(() => expect(output().textContent).toBe("[]"));

  test.do(() => unselectedOptions().item(1).selected = true);
  test.do(() => unselectedOptions().item(2).selected = true);
  test.event("click", addButton, () => output().textContent === "[Alpha Centauri, Wolf 359]");
  test.do(() => expect(unselectedOptions().length).toBe(2));
  test.do(() => expect(selectedOptions().length).toBe(2));
  test.do(() => expect(output().textContent).toBe("[Alpha Centauri, Wolf 359]"));

  test.do(() => selectedOptions().item(0).selected = true);
  test.do(() => selectedOptions().item(1).selected = false);
  test.event("click", removeButton, () => output().textContent === "[Wolf 359]");
  test.do(() => expect(unselectedOptions().length).toBe(3));
  test.do(() => expect(selectedOptions().length).toBe(1));
  test.do(() => expect(output().textContent).toBe("[Wolf 359]"));

  const pageOverlays = querySelectorAllFn("tobago-overlay");
  test.do(() => expect(pageOverlays().length).toBe(0, "there must be no tobago-overlay"));
  test.start();
});
