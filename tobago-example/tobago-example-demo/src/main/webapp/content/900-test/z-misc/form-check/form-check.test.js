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

it("Required fields", function () {
  testFormChecks(
      elementByIdFn("page:mainForm:selectBooleanCheckbox::field"),
      elementByIdFn("page:mainForm:selectBooleanCheckboxChecked::field"),
      elementByIdFn("page:mainForm:selectBooleanCheckboxDisabled::field"),
      elementByIdFn("page:mainForm:selectBooleanCheckboxCheckedDisabled::field")
  );

  testFormChecks(
      elementByIdFn("page:mainForm:selectBooleanToggle::field"),
      elementByIdFn("page:mainForm:selectBooleanToggleChecked::field"),
      elementByIdFn("page:mainForm:selectBooleanToggleDisabled::field"),
      elementByIdFn("page:mainForm:selectBooleanToggleCheckedDisabled::field")
  );

  testFormChecks(
      elementByIdFn("page:mainForm:selectOneRadio::0"),
      elementByIdFn("page:mainForm:selectOneRadio::1"),
      elementByIdFn("page:mainForm:selectOneRadioDisabled::0"),
      elementByIdFn("page:mainForm:selectOneRadioDisabled::1")
  );

  testFormChecks(
      elementByIdFn("page:mainForm:selectManyCheckbox::0"),
      elementByIdFn("page:mainForm:selectManyCheckbox::1"),
      elementByIdFn("page:mainForm:selectManyCheckbox::2"),
      elementByIdFn("page:mainForm:selectManyCheckbox::3")
  );

  testFormChecks(
      querySelectorFn("input[name='page:mainForm:sheetMulti_data_row_selector_0']"),
      querySelectorFn("input[name='page:mainForm:sheetMulti_data_row_selector_1']"),
      querySelectorFn("input[name='page:mainForm:sheetMulti_data_row_selector_2']"),
      querySelectorFn("input[name='page:mainForm:sheetMulti_data_row_selector_3']"),
  );

  testFormChecks(
      querySelectorFn("input[name='page:mainForm:sheetSingle_data_row_selector_0']"),
      querySelectorFn("input[name='page:mainForm:sheetSingle_data_row_selector_1']"),
      querySelectorFn("input[name='page:mainForm:sheetSingle_data_row_selector_2']"),
      querySelectorFn("input[name='page:mainForm:sheetSingle_data_row_selector_3']"),
  );

  testFormChecks(
      querySelectorFn("input[name='page:mainForm:autoWidthColSelHeadEnabledUnchecked::columnSelector']"),
      querySelectorFn("input[name='page:mainForm:autoWidthColSelHeadEnabledChecked::columnSelector']"),
      querySelectorFn("input[name='page:mainForm:autoWidthColSelHeadDisabledUnchecked::columnSelector']"),
      querySelectorFn("input[name='page:mainForm:autoWidthColSelHeadDisabledChecked::columnSelector']"),
  );

  testFormChecks(
      querySelectorFn("input[name='page:mainForm:customWidthColSelHeadEnabledUnchecked::columnSelector']"),
      querySelectorFn("input[name='page:mainForm:customWidthColSelHeadEnabledChecked::columnSelector']"),
      querySelectorFn("input[name='page:mainForm:customWidthColSelHeadDisabledUnchecked::columnSelector']"),
      querySelectorFn("input[name='page:mainForm:customWidthColSelHeadDisabledChecked::columnSelector']"),
  );
});

function testFormChecks(uncheckedEnabled, checkedEnabled, uncheckedDisabled, checkedDisabled) {
  const white = "rgb(255, 255, 255)";
  const blue = "rgb(13, 110, 253)";
  const gray = "rgb(222, 226, 230)";
  const grayTransparent = "rgba(222, 226, 230, 0.5)";

  expect(window.getComputedStyle(uncheckedEnabled()).backgroundColor).toContain(white);
  expect(window.getComputedStyle(uncheckedEnabled()).borderColor).toContain(gray);
  expect(window.getComputedStyle(uncheckedEnabled()).opacity).toContain(1);

  expect(window.getComputedStyle(checkedEnabled()).backgroundColor).toContain(blue);
  expect(window.getComputedStyle(checkedEnabled()).borderColor).toContain(blue);
  expect(window.getComputedStyle(checkedEnabled()).opacity).toContain(1);

  expect(window.getComputedStyle(uncheckedDisabled()).backgroundColor).toContain(grayTransparent);
  expect(window.getComputedStyle(uncheckedDisabled()).borderColor).toContain(gray);
  expect(window.getComputedStyle(uncheckedDisabled()).opacity).toContain(0.5);

  expect(window.getComputedStyle(checkedDisabled()).backgroundColor).toContain(blue);
  expect(window.getComputedStyle(checkedDisabled()).borderColor).toContain(blue);
  expect(window.getComputedStyle(checkedDisabled()).opacity).toContain(0.5);
}
