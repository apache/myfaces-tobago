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

QUnit.test("test numbers of tab-group-index", function (assert) {
  let tabOneFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabOne");
  let tabTwoFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabTwo");
  let tabThreeFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabThree");
  let tabFourFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabFour");
  let tabFiveFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabFive");
  let tabOneContentFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabOne\\:\\:content");
  let tabTwoContentFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabTwo\\:\\:content");
  let tabThreeContentFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabThree\\:\\:content");
  let tabFourContentFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabFour\\:\\:content");
  let tabFiveContentFn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabFive\\:\\:content");

  assert.equal(tabOneFn().dataset.tobagoTabGroupIndex, "0");
  assert.equal(tabTwoFn().dataset.tobagoTabGroupIndex, "1");
  assert.equal(tabThreeFn(), null, "Tab three is not rendered");
  assert.equal(tabFourFn().dataset.tobagoTabGroupIndex, "3");
  assert.equal(tabFiveFn().dataset.tobagoTabGroupIndex, "4");

  assert.equal(tabOneContentFn().dataset.tobagoTabGroupIndex, "0");
  assert.equal(tabTwoContentFn().dataset.tobagoTabGroupIndex, "1");
  assert.equal(tabThreeContentFn(), null, "Tab three content is not rendered");
  assert.equal(tabFiveContentFn().dataset.tobagoTabGroupIndex, "4");

  assert.ok(tabOneFn().classList.contains("tobago-tab-markup-selected"));
  assert.notOk(tabTwoFn().classList.contains("tobago-tab-markup-selected"));
  assert.notOk(tabFourFn().classList.contains("tobago-tab-markup-selected"));
  assert.notOk(tabFiveFn().classList.contains("tobago-tab-markup-selected"));

  assert.ok(tabOneContentFn().classList.contains("active"));
  assert.notOk(tabTwoContentFn().classList.contains("active"));
  assert.equal(tabFourContentFn(), null);
  assert.notOk(tabFiveContentFn().classList.contains("active"));
});
