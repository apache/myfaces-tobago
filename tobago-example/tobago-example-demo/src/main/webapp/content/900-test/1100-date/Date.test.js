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

it("#11 model=java.util.Date type=time", function (done) {
  const dateFn = elementByIdFn("page:mainForm:dateTimeForm:dateTime::field");
  const outputFn = querySelectorFn("#page\\:mainForm\\:dateTimeForm\\:dateTimeOutput .form-control-plaintext");
  const submitButtonFn = elementByIdFn("page:mainForm:dateTimeForm:dateTimeButton");
  const resetButtonFn = elementByIdFn("page:mainForm:resetButtonFrom:resetButton");

  const time = "12:34";

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "", null, "click", resetButtonFn);
  test.do(() => dateFn().value = time);
  test.event("click", submitButtonFn, () => outputFn().textContent === time);
  test.do(() => expect(outputFn().textContent === time));
  test.start();
});

it("#12 model=java.util.Date type=time step=1", function (done) {
  const dateFn = elementByIdFn("page:mainForm:dateTimeStep1Form:dateTimeStep1::field");
  const outputFn = querySelectorFn("#page\\:mainForm\\:dateTimeStep1Form\\:dateTimeStep1Output .form-control-plaintext");
  const submitButtonFn = elementByIdFn("page:mainForm:dateTimeStep1Form:dateTimeStep1Button");
  const resetButtonFn = elementByIdFn("page:mainForm:resetButtonFrom:resetButton");

  const time = "12:34:56";

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "", null, "click", resetButtonFn);
  test.do(() => dateFn().value = time);
  test.event("click", submitButtonFn, () => outputFn().textContent === time);
  test.do(() => expect(outputFn().textContent === time));
  test.start();
});

it("#13 model=java.util.Date type=datetime-local", function (done) {
  const dateFn = elementByIdFn("page:mainForm:dateDateTimeForm:dateDateTime::field");
  const outputFn = querySelectorFn("#page\\:mainForm\\:dateDateTimeForm\\:dateDateTimeOutput .form-control-plaintext");
  const submitButtonFn = elementByIdFn("page:mainForm:dateDateTimeForm:dateDateTimeButton");
  const resetButtonFn = elementByIdFn("page:mainForm:resetButtonFrom:resetButton");

  const time = "2010-05-30T23:45";

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "", null, "click", resetButtonFn);
  test.do(() => dateFn().value = time);
  test.event("click", submitButtonFn, () => outputFn().textContent === time);
  test.do(() => expect(outputFn().textContent === time));
  test.start();
});

it("#14 model=java.util.Date type=datetime-local step=1", function (done) {
  const dateFn = elementByIdFn("page:mainForm:dateDateTimeStep1Form:dateDateTimeStep1::field");
  const outputFn = querySelectorFn("#page\\:mainForm\\:dateDateTimeStep1Form\\:dateDateTimeStep1Output .form-control-plaintext");
  const submitButtonFn = elementByIdFn("page:mainForm:dateDateTimeStep1Form:dateDateTimeStep1Button");
  const resetButtonFn = elementByIdFn("page:mainForm:resetButtonFrom:resetButton");

  const time = "2010-05-30T23:45:32";

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "", null, "click", resetButtonFn);
  test.do(() => dateFn().value = time);
  test.event("click", submitButtonFn, () => outputFn().textContent === time);
  test.do(() => expect(outputFn().textContent === time));
  test.start();
});
