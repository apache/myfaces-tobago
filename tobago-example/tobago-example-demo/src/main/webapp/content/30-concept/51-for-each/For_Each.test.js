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

import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";


it("Add a river and reset.", function (done) {
  let nameFn = querySelectorFn("#page\\:mainForm\\:add\\:inName\\:\\:field");
  let lengthFn = querySelectorFn("#page\\:mainForm\\:add\\:inLength\\:\\:field");
  let dischargeFn = querySelectorFn("#page\\:mainForm\\:add\\:inDischarge\\:\\:field");
  let addFn = querySelectorFn("#page\\:mainForm\\:add\\:buttonAdd");
  let resetFn = querySelectorFn("#page\\:mainForm\\:reset\\:buttonReset");
  let forEachBoxesFn = querySelectorAllFn("#page\\:mainForm\\:forEach tobago-box");
  let uiRepeatSectionsFn = querySelectorAllFn("#page\\:mainForm\\:uiRepeat tobago-section");

  let test = new JasmineTestTool(done);
  test.setup(() => forEachBoxesFn().length === 3,
      null, "click", resetFn);
  test.do(() => expect(forEachBoxesFn().length).toBe(3));
  test.do(() => expect(uiRepeatSectionsFn().length).toBe(3));
  test.do(() => nameFn().value = "Mississippi");
  test.do(() => lengthFn().value = "6275");
  test.do(() => dischargeFn().value = "16200");
  test.event("click", addFn, () => forEachBoxesFn().length === 4 && uiRepeatSectionsFn().length === 4);
  test.do(() => expect(forEachBoxesFn().length).toBe(4));
  test.do(() => expect(uiRepeatSectionsFn().length).toBe(4));
  test.event("click", resetFn, () => forEachBoxesFn().length === 3 && uiRepeatSectionsFn().length === 3);
  test.do(() => expect(forEachBoxesFn().length).toBe(3));
  test.do(() => expect(uiRepeatSectionsFn().length).toBe(3));
  test.start();
});
