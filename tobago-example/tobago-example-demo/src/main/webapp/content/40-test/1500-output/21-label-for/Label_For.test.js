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

import {testFrameQuerySelectorFn} from "/script/tobago-test.js";

QUnit.test("Test for required CSS class", function (assert) {
  assert.expect(13);

  let inLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:inLabel");
  let dateLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:dateLabel");
  let fileLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:fileLabel");
  let textareaLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:textareaLabel");
  let selectBooleanCheckboxLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:selectBooleanCheckboxLabel");
  let selectBooleanToggleLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:selectBooleanToggleLabel");
  let selectOneRadioLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:selectOneRadioLabel");
  let selectManyCheckboxLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:selectManyCheckboxLabel");
  let selectOneChoiceLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:selectOneChoiceLabel");
  let selectOneListboxLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:selectOneListboxLabel");
  let selectManyListboxLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:selectManyListboxLabel");
  let selectManyShuttleLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:selectManyShuttleLabel");
  let starsLabel = testFrameQuerySelectorFn("#page\\:mainForm\\:starsLabel");

  assert.ok(inLabel().classList.contains("tobago-required"));
  assert.ok(dateLabel().classList.contains("tobago-required"));
  assert.ok(fileLabel().classList.contains("tobago-required"));
  assert.ok(textareaLabel().classList.contains("tobago-required"));
  assert.ok(selectBooleanCheckboxLabel().classList.contains("tobago-required"));
  assert.ok(selectBooleanToggleLabel().classList.contains("tobago-required"));
  assert.ok(selectOneRadioLabel().classList.contains("tobago-required"));
  assert.ok(selectManyCheckboxLabel().classList.contains("tobago-required"));
  assert.ok(selectOneChoiceLabel().classList.contains("tobago-required"));
  assert.ok(selectOneListboxLabel().classList.contains("tobago-required"));
  assert.ok(selectManyListboxLabel().classList.contains("tobago-required"));
  assert.ok(selectManyShuttleLabel().classList.contains("tobago-required"));
  assert.ok(starsLabel().classList.contains("tobago-required"));
});
