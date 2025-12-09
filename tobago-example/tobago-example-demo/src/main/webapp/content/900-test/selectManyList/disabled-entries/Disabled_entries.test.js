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

it("SelectManyList expanded=false: colors of list entries", function () {
  const entry1 = querySelectorFn("[id='page:mainForm:selectManyList'] tr[data-tobago-value='1'] td[value='1']");
  const entry2 = querySelectorFn("[id='page:mainForm:selectManyList'] tr[data-tobago-value='2'] td[value='2']");

  expect(getComputedStyle(entry1()).backgroundColor).toBe("rgb(255, 255, 255)")
  expect(getComputedStyle(entry2()).backgroundColor).toBe("rgb(233, 236, 239)")
});

it("SelectManyList expanded=true: colors of list entries", function () {
  const entry1 = querySelectorFn("[id='page:mainForm:selectManyListExpanded'] tr[data-tobago-value='1'] td[value='1']");
  const entry2 = querySelectorFn("[id='page:mainForm:selectManyListExpanded'] tr[data-tobago-value='2'] td[value='2']");

  expect(getComputedStyle(entry1()).backgroundColor).toBe("rgb(255, 255, 255)")
  expect(getComputedStyle(entry2()).backgroundColor).toBe("rgb(233, 236, 239)")
});
