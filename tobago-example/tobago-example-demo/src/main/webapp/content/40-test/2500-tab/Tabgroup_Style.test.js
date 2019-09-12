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
  let tab1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabOne");
  let tab2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabTwo");
  let tab3Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabThree");
  let tab4Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabFour");
  let tab5Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabFive");
  let tabContent1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabOne\\:\\:content");
  let tabContent2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabTwo\\:\\:content");
  let tabContent3Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabThree\\:\\:content");
  let tabContent4Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabFour\\:\\:content");
  let tabContent5Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:tabFive\\:\\:content");

  assert.equal(tab1Fn().index, 0);
  assert.equal(tab2Fn().index, 1);
  assert.equal(tab3Fn(), null, "Tab three is not rendered");
  assert.equal(tab4Fn().index, 3);
  assert.equal(tab5Fn().index, 4);

  assert.equal(tabContent1Fn().index, 0);
  assert.equal(tabContent2Fn().index, 1);
  assert.equal(tabContent3Fn(), null, "Tab three content is not rendered");
  assert.equal(tabContent4Fn(), null, "Tab four content is not rendered (disabled)");
  assert.equal(tabContent5Fn().index, 4);

  assert.ok(tab1Fn().querySelector(".nav-link").classList.contains("active"));
  assert.notOk(tab2Fn().querySelector(".nav-link").classList.contains("active"));
  assert.notOk(tab4Fn().querySelector(".nav-link").classList.contains("active"));
  assert.notOk(tab5Fn().querySelector(".nav-link").classList.contains("active"));

  assert.ok(tabContent1Fn().classList.contains("active"));
  assert.notOk(tabContent2Fn().classList.contains("active"));
  assert.equal(tabContent4Fn(), null);
  assert.notOk(tabContent5Fn().classList.contains("active"));
});
