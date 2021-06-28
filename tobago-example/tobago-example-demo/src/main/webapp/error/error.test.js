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

it("has no exception", function (done) {
  let errorFn = querySelectorFn("#page\\:mainForm\\:errorSection .tobago-header span");

  const test = new JasmineTestTool(done);
  test.do(() => expect(errorFn() === null
      || errorFn().textContent).not.toEqual("An error has occurred!"));
  test.start();
});

it("has no 404", function (done) {
  let error404Fn = querySelectorFn("#page\\:mainForm\\:pageNotFoundMessage .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.do(() => expect(error404Fn() === null
      || error404Fn().textContent.indexOf("The page was not found!") === -1).toBeTrue());
  test.start();
});
