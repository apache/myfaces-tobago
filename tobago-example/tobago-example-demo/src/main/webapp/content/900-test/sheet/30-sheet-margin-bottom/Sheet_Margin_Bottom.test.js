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
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Margin from button to sheet/paging must be the same", function (done) {
  const sheet1pagingFn = querySelectorFn("#page\\:mainForm\\:sheet1 .pagination");
  const button1Fn = elementByIdFn("page:mainForm:button1");
  const sheet2Fn = elementByIdFn("page:mainForm:sheet2");
  const button2Fn = elementByIdFn("page:mainForm:button2");

  const sheet1Button1Gap = button1Fn().offsetTop - (sheet1pagingFn().offsetTop + sheet1pagingFn().offsetHeight);
  const sheet2button2Gap = button2Fn().offsetTop - (sheet2Fn().offsetTop + sheet2Fn().offsetHeight);

  const test = new JasmineTestTool(done);
  test.do(() => expect(sheet1Button1Gap).toEqual(sheet2button2Gap));
  test.start();
});
