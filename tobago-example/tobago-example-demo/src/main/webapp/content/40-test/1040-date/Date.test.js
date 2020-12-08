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

QUnit.test("inputfield with label", function (assert) {
  let dateFieldFn = querySelectorFn("#page\\:mainForm\\:dateNoPattern\\:\\:field");
  let dateButtonFn = querySelectorFn("#page\\:mainForm\\:dateNoPattern button");

  assert.equal(dateFieldFn().value, "");

  let datepickerFn = querySelectorAllFn(".bootstrap-datetimepicker-widget");
  assert.notOk(datepickerFn().item(0));

  dateButtonFn().dispatchEvent(new Event("click", {bubbles: true}));

  datepickerFn = querySelectorAllFn(".bootstrap-datetimepicker-widget");
  assert.ok(datepickerFn().item(0));

  assert.notEqual(dateFieldFn().value, "");
});
