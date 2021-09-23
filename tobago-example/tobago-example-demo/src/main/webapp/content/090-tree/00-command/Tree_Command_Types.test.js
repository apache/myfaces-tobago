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
import {querySelectorFn} from "/script/tobago-test.js";

it("not implemented yet", function (done) {
  let action1Fn = querySelectorFn("#page\\:mainForm\\:tree\\:2\\:actionCommand");
  let action2Fn = querySelectorFn("#page\\:mainForm\\:tree\\:3\\:actionCommand");
  let actionCount1Fn = querySelectorFn("#page\\:mainForm\\:actionCount1 .form-control-plaintext");
  let actionCount2Fn = querySelectorFn("#page\\:mainForm\\:actionCount2 .form-control-plaintext");

  const action1Count = Number(actionCount1Fn().textContent);
  const action2Count = Number(actionCount2Fn().textContent);

  let test = new JasmineTestTool(done);
  test.event("click", action1Fn, () => Number(actionCount1Fn().textContent) === action1Count + 1);
  test.do(() => expect(Number(actionCount1Fn().textContent)).toEqual(action1Count + 1));
  test.event("click", action2Fn, () => Number(actionCount2Fn().textContent) === action2Count + 1);
  test.do(() => expect(Number(actionCount2Fn().textContent)).toEqual(action2Count + 1));
  test.event("click", action1Fn, () => Number(actionCount1Fn().textContent) === action1Count + 2);
  test.do(() => expect(Number(actionCount1Fn().textContent)).toEqual(action1Count + 2));
  test.event("click", action2Fn, () => Number(actionCount2Fn().textContent) === action2Count + 2);
  test.do(() => expect(Number(actionCount2Fn().textContent)).toEqual(action2Count + 2));
  test.start();
});
