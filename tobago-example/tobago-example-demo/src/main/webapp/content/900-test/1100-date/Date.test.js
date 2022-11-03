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
  const inputFieldFn = querySelectorFn("#page\\:mainForm\\:localDateForm\\:localDate\\:\\:field");
  const outPutFn = querySelectorFn("#page\\:mainForm\\:localDateForm\\:localDateOutput .form-control-plaintext");
  const submitButtonFn = querySelectorFn("#page\\:mainForm\\:localDateForm\\:localDateButton");
  const resetButtonFn = querySelectorFn("#page\\:mainForm\\:resetButtonFrom\\:resetButton");
  const date = "2020-07-07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null, null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = date);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === date));
  test.start();
});

it("#2 model=java.time.LocalTime", function (done) {
  const inputFieldFn = querySelectorFn("#page\\:mainForm\\:localTimeForm\\:localTime\\:\\:field");
  const outPutFn = querySelectorFn("#page\\:mainForm\\:localTimeForm\\:localTimeOutput .form-control-plaintext");
  const submitButtonFn = querySelectorFn("#page\\:mainForm\\:localTimeForm\\:localTimeButton");
  const resetButtonFn = querySelectorFn("#page\\:mainForm\\:resetButtonFrom\\:resetButton");
  const time = "07:07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null, null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#3 model=java.time.LocalTime step=1", function (done) {
  const inputFieldFn = querySelectorFn("#page\\:mainForm\\:localTimeStepAForm\\:localTimeStepA\\:\\:field");
  const outPutFn = querySelectorFn("#page\\:mainForm\\:localTimeStepAForm\\:localTimeStepAOutput .form-control-plaintext");
  const submitButtonFn = querySelectorFn("#page\\:mainForm\\:localTimeStepAForm\\:localTimeButtonStepA");
  const resetButtonFn = querySelectorFn("#page\\:mainForm\\:resetButtonFrom\\:resetButton");
  const time = "07:07:07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null, null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#4 model=java.time.LocalTime step=0.001", function (done) {
  const inputFieldFn = querySelectorFn("#page\\:mainForm\\:localTimeStepBForm\\:localTimeStepB\\:\\:field");
  const outPutFn = querySelectorFn("#page\\:mainForm\\:localTimeStepBForm\\:localTimeStepBOutput .form-control-plaintext");
  const submitButtonFn = querySelectorFn("#page\\:mainForm\\:localTimeStepBForm\\:localTimeButtonStepB");
  const resetButtonFn = querySelectorFn("#page\\:mainForm\\:resetButtonFrom\\:resetButton");
  const time = "07:07:07.007";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null, null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#5 model=java.time.LocalDateTime", function (done) {
  const inputFieldFn = querySelectorFn("#page\\:mainForm\\:localDateTimeForm\\:localDateTime\\:\\:field");
  const outPutFn = querySelectorFn("#page\\:mainForm\\:localDateTimeForm\\:localDateTimeOutput .form-control-plaintext");
  const submitButtonFn = querySelectorFn("#page\\:mainForm\\:localDateTimeForm\\:localDateTimeButton");
  const resetButtonFn = querySelectorFn("#page\\:mainForm\\:resetButtonFrom\\:resetButton");
  const time = "2020-07-07T07:07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null, null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#6 model=java.time.LocalDateTime step=1", function (done) {
  const inputFieldFn = querySelectorFn("#page\\:mainForm\\:localDateTimeStepAForm\\:localDateTimeStepA\\:\\:field");
  const outPutFn = querySelectorFn("#page\\:mainForm\\:localDateTimeStepAForm\\:localDateTimeStepAOutput .form-control-plaintext");
  const submitButtonFn = querySelectorFn("#page\\:mainForm\\:localDateTimeStepAForm\\:localDateTimeButtonStepA");
  const resetButtonFn = querySelectorFn("#page\\:mainForm\\:resetButtonFrom\\:resetButton");
  const time = "2020-07-07T07:07:07";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null, null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});

it("#7 model=java.time.LocalDateTime step=0.001", function (done) {
  const inputFieldFn = querySelectorFn("#page\\:mainForm\\:localDateTimeStepBForm\\:localDateTimeStepB\\:\\:field");
  const outPutFn = querySelectorFn("#page\\:mainForm\\:localDateTimeStepBForm\\:localDateTimeStepBOutput .form-control-plaintext");
  const submitButtonFn = querySelectorFn("#page\\:mainForm\\:localDateTimeStepBForm\\:localDateTimeButtonStepB");
  const resetButtonFn = querySelectorFn("#page\\:mainForm\\:resetButtonFrom\\:resetButton");
  const time = "2020-07-07T07:07:07.007";

  const test = new JasmineTestTool(done);
  test.setup(() => outPutFn().textContent !== null, null, "click", resetButtonFn);
  test.do(() => inputFieldFn().value = time);
  test.event("click", submitButtonFn, () => outPutFn().textContent !== "");
  test.do(() => expect(outPutFn().textContent === time));
  test.start();
});
