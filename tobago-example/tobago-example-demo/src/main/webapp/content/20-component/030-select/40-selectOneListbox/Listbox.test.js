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

it("submit: select 'Nile'", function (done) {
  let riversFn = querySelectorAllFn("#page\\:mainForm\\:riverList option");
  let submitFn = querySelectorFn("#page\\:mainForm\\:riverSubmit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:riverOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent !== "6853 km",
      () => {
        riversFn().item(0).selected = false; // Nile
        riversFn().item(1).selected = false; // Amazon
        riversFn().item(2).selected = false; // Yangtze
        riversFn().item(3).selected = false; // Yellow River
        riversFn().item(4).selected = false; // Paraná River
      },
      "click", submitFn);
  test.do(() => riversFn().item(0).selected = true); // Nile
  test.do(() => riversFn().item(1).selected = false); // Amazon
  test.do(() => riversFn().item(2).selected = false); // Yangtze
  test.do(() => riversFn().item(3).selected = false); // Yellow River
  test.do(() => riversFn().item(4).selected = false); // Paraná River
  test.event("click", submitFn, () => outputFn().textContent === "6853 km");
  test.do(() => expect(outputFn().textContent).toBe("6853 km"));
  test.start();
});

it("submit: select 'Yangtze'", function (done) {
  let riversFn = querySelectorAllFn("#page\\:mainForm\\:riverList option");
  let submitFn = querySelectorFn("#page\\:mainForm\\:riverSubmit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:riverOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent !== "6300 km",
      () => {
        riversFn().item(0).selected = false; // Nile
        riversFn().item(1).selected = false; // Amazon
        riversFn().item(2).selected = false; // Yangtze
        riversFn().item(3).selected = false; // Yellow River
        riversFn().item(4).selected = false; // Paraná River
      },
      "click", submitFn);
  test.do(() => riversFn().item(0).selected = false); // Nile
  test.do(() => riversFn().item(1).selected = false); // Amazon
  test.do(() => riversFn().item(2).selected = true); // Yangtze
  test.do(() => riversFn().item(3).selected = false); // Yellow River
  test.do(() => riversFn().item(4).selected = false); // Paraná River
  test.event("click", submitFn, () => outputFn().textContent === "6300 km");
  test.do(() => expect(outputFn().textContent).toBe("6300 km"));
  test.start();
});

it("ajax: select Everest", function (done) {
  let mountainListFn = querySelectorFn("#page\\:mainForm\\:mountainList\\:\\:field");
  let mountainsFn = querySelectorAllFn("#page\\:mainForm\\:mountainList option");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedMountain .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent !== "8848 m",
      () => {
        mountainsFn().item(0).selected = false;
        mountainsFn().item(1).selected = false;
        mountainsFn().item(2).selected = false;
        mountainsFn().item(3).selected = false;
        mountainsFn().item(4).selected = false;
      }, "change", mountainListFn);
  test.do(() => mountainsFn().item(0).selected = true); // Everest
  test.do(() => mountainsFn().item(1).selected = false);
  test.do(() => mountainsFn().item(2).selected = false);
  test.do(() => mountainsFn().item(3).selected = false);
  test.do(() => mountainsFn().item(4).selected = false);
  test.event("change", mountainListFn, () => outputFn().textContent === "8848 m");
  test.do(() => expect(outputFn().textContent).toBe("8848 m"));
  test.start();
});

it("ajax: select Makalu", function (done) {
  let mountainListFn = querySelectorFn("#page\\:mainForm\\:mountainList\\:\\:field");
  let mountainsFn = querySelectorAllFn("#page\\:mainForm\\:mountainList option");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedMountain .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent !== "8481 m",
      () => {
        mountainsFn().item(0).selected = false;
        mountainsFn().item(1).selected = false;
        mountainsFn().item(2).selected = false;
        mountainsFn().item(3).selected = false;
        mountainsFn().item(4).selected = false;
      }, "change", mountainListFn);
  test.do(() => mountainsFn().item(0).selected = false); // Everest
  test.do(() => mountainsFn().item(1).selected = false);
  test.do(() => mountainsFn().item(2).selected = false);
  test.do(() => mountainsFn().item(3).selected = false);
  test.do(() => mountainsFn().item(4).selected = true); // Makalu
  test.event("change", mountainListFn, () => outputFn().textContent === "8481 m");
  test.do(() => expect(outputFn().textContent).toBe("8481 m"));
  test.start();
});
