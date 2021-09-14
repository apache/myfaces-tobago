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

it("submit: select cat", function (done) {
  let animalsFn = querySelectorAllFn("#page\\:mainForm\\:animals input");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:animalsOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent.trim() === "",
      () => {
        animalsFn().item(0).checked = false;
        animalsFn().item(1).checked = false;
        animalsFn().item(2).checked = false;
        animalsFn().item(3).checked = false;
      },
      "click", submitFn);
  test.do(() => animalsFn().item(0).checked = true); // Cat
  test.do(() => animalsFn().item(1).checked = false);
  test.do(() => animalsFn().item(2).checked = false);
  test.do(() => animalsFn().item(3).checked = false);
  test.event("click", submitFn, () => outputFn().textContent.trim() === "Cat");
  test.do(() => expect(outputFn().textContent.trim()).toBe("Cat"));
  test.start();
});

it("submit: select fox and rabbit", function (done) {
  let animalsFn = querySelectorAllFn("#page\\:mainForm\\:animals input");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:animalsOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent.trim() === "",
      () => {
        animalsFn().item(0).checked = false;
        animalsFn().item(1).checked = false;
        animalsFn().item(2).checked = false;
        animalsFn().item(3).checked = false;
      },
      "click", submitFn);
  test.do(() => animalsFn().item(0).checked = false); // Cat
  test.do(() => animalsFn().item(1).checked = false);
  test.do(() => animalsFn().item(2).checked = true); // Fox
  test.do(() => animalsFn().item(3).checked = true); // Rabbit
  test.event("click", submitFn, () => outputFn().textContent.trim() === "Fox Rabbit");
  test.do(() => expect(outputFn().textContent.trim()).toBe("Fox Rabbit"));
  test.start();
});

it("ajax: select 'One'", function (done) {
  let numberFn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:0");
  ajaxSelect(done, numberFn, 1);
});

it("ajax: deselect 'One'", function (done) {
  let numberFn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:0");
  ajaxDeselect(done, numberFn, 1);
});

it("ajax: select 'Two'", function (done) {
  let numberFn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:1");
  ajaxSelect(done, numberFn, 2);
});

it("ajax: deselect 'Two'", function (done) {
  let numberFn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:1");
  ajaxDeselect(done, numberFn, 2);
});

it("ajax: select 'Three'", function (done) {
  let numberFn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:2");
  ajaxSelect(done, numberFn, 3);
});

it("ajax: deselect 'Three'", function (done) {
  let numberFn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:2");
  ajaxDeselect(done, numberFn, 3);
});

it("ajax: select 'Four'", function (done) {
  let numberFn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:3");
  ajaxSelect(done, numberFn, 4);
});

it("ajax: deselect 'Four'", function (done) {
  let numberFn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:3");
  ajaxDeselect(done, numberFn, 4);
});

function ajaxSelect(done, numberFn, number) {
  let outputFn = querySelectorFn("#page\\:mainForm\\:resultOutput .form-control-plaintext");
  let newOutputValue = parseInt(outputFn().textContent);
  if (!numberFn().checked) {
    newOutputValue = parseInt(outputFn().textContent) + number;
  }

  const test = new JasmineTestTool(done);
  test.setup(() => parseInt(outputFn().textContent) !== newOutputValue,
      () => numberFn().checked = false,
      "change", numberFn);
  test.do(() => numberFn().checked = true);
  test.event("change", numberFn, () => parseInt(outputFn().textContent) === newOutputValue);
  test.do(() => expect(parseInt(outputFn().textContent)).toBe(newOutputValue));
  test.start();
}

function ajaxDeselect(done, numberFn, number) {
  let outputFn = querySelectorFn("#page\\:mainForm\\:resultOutput .form-control-plaintext");
  let newOutputValue = parseInt(outputFn().textContent);
  if (numberFn().checked) {
    newOutputValue = parseInt(outputFn().textContent) - number;
  }

  const test = new JasmineTestTool(done);
  test.setup(() => parseInt(outputFn().textContent) !== newOutputValue,
      () => numberFn().checked = true,
      "change", numberFn);
  test.do(() => numberFn().checked = false);
  test.event("change", numberFn, () => parseInt(outputFn().textContent) === newOutputValue);
  test.do(() => expect(parseInt(outputFn().textContent)).toBe(newOutputValue));
  test.start();
}
