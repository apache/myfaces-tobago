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

it("Test for required CSS class", function (done) {
  let inLabel = querySelectorFn("#page\\:mainForm\\:inLabel");
  let dateLabel = querySelectorFn("#page\\:mainForm\\:dateLabel");
  let fileLabel = querySelectorFn("#page\\:mainForm\\:fileLabel");
  let textareaLabel = querySelectorFn("#page\\:mainForm\\:textareaLabel");
  let selectBooleanCheckboxLabel = querySelectorFn("#page\\:mainForm\\:selectBooleanCheckboxLabel");
  let selectBooleanToggleLabel = querySelectorFn("#page\\:mainForm\\:selectBooleanToggleLabel");
  let selectOneRadioLabel = querySelectorFn("#page\\:mainForm\\:selectOneRadioLabel");
  let selectManyCheckboxLabel = querySelectorFn("#page\\:mainForm\\:selectManyCheckboxLabel");
  let selectOneChoiceLabel = querySelectorFn("#page\\:mainForm\\:selectOneChoiceLabel");
  let selectOneListboxLabel = querySelectorFn("#page\\:mainForm\\:selectOneListboxLabel");
  let selectManyListboxLabel = querySelectorFn("#page\\:mainForm\\:selectManyListboxLabel");
  let selectManyShuttleLabel = querySelectorFn("#page\\:mainForm\\:selectManyShuttleLabel");
  let starsLabel = querySelectorFn("#page\\:mainForm\\:starsLabel");
  let labelForIdOne = querySelectorFn("#page\\:mainForm\\:labelForIdOne");
  let labelForIdTwo = querySelectorFn("#page\\:mainForm\\:labelForIdTwo");

  let test = new JasmineTestTool(done);
  test.do(() => expect(inLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(dateLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(fileLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(textareaLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(selectBooleanCheckboxLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(selectBooleanToggleLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(selectOneRadioLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(selectManyCheckboxLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(selectOneChoiceLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(selectOneListboxLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(selectManyListboxLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(selectManyShuttleLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(starsLabel().classList.contains("tobago-required")).toBe(true));
  test.do(() => expect(labelForIdOne().getAttribute("for")).toEqual("page:mainForm:id1::field"));
  test.do(() => expect(labelForIdTwo().getAttribute("for")).toEqual("page:mainForm:id2::field"));
  test.start();
});
