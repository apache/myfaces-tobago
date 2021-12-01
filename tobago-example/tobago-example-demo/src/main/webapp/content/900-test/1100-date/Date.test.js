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

it("#1 model=java.time.LocalDate", function (done) {
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:j_id_2f\\:localDate\\:\\:field");
  let outPutFn = querySelectorFn("#page\\:mainForm\\:j_id_2f\\:j_id_2i .form-control-plaintext");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2f\\:localDateButton");
  let resetButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2c\\:j_id_2d");
  const date = "2020-07-07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null,
      null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = date);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === date));
  test.start();
});

it("#2 model=java.time.LocalTime", function (done) {
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:j_id_2k\\:localTime\\:\\:field");
  let outPutFn = querySelectorFn("#page\\:mainForm\\:j_id_2k\\:j_id_2n .form-control-plaintext");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2k\\:localTimeButton");
  let resetButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2c\\:j_id_2d");
  const time = "07:07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null,
      null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#3 model=java.time.LocalTime step=1", function (done) {
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:j_id_2p\\:localTimeStepA\\:\\:field");
  let outPutFn = querySelectorFn("#page\\:mainForm\\:j_id_2p\\:j_id_2s .form-control-plaintext");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2p\\:localTimeButtonStepA");
  let resetButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2c\\:j_id_2d");
  const time = "07:07:07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null,
      null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#4 model=java.time.LocalTime step=0.001", function (done) {
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:j_id_2u\\:localTimeStepB\\:\\:field");
  let outPutFn = querySelectorFn("#page\\:mainForm\\:j_id_2u\\:j_id_2x .form-control-plaintext");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2u\\:localTimeButtonStepB");
  let resetButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2c\\:j_id_2d");
  const time = "07:07:07.007";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null,
      null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#5 model=java.time.LocalDateTime", function (done) {
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:j_id_2z\\:localDateTime\\:\\:field");
  let outPutFn = querySelectorFn("#page\\:mainForm\\:j_id_2z\\:j_id_32 .form-control-plaintext");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2z\\:localDateTimeButton");
  let resetButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2z\\:j_id_2d");
  const time = "2020-07-07T07:07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null,
      null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#6 model=java.time.LocalDateTime step=1", function (done) {
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:j_id_34\\:localDateTimeStepA\\:\\:field");
  let outPutFn = querySelectorFn("#page\\:mainForm\\:j_id_34\\:j_id_37 .form-control-plaintext");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_34\\:localDateTimeButtonStepA");
  let resetButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2z\\:j_id_2d");
  const time = "2020-07-07T07:07:07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null,
      null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#7 model=java.time.LocalDateTime step=0.001", function (done) {
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:j_id_39\\:localDateTimeStepB\\:\\:field");
  let outPutFn = querySelectorFn("#page\\:mainForm\\:j_id_39\\:j_id_3c .form-control-plaintext");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_39\\:localDateTimeButtonStepB");
  let resetButtonFn = querySelectorFn("#page\\:mainForm\\:j_id_2z\\:j_id_2d");
  const time = "2020-07-07T07:07:07.007";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null,
      null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});
