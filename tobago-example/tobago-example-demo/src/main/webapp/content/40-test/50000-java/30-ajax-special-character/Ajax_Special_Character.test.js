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

QUnit.test("ajax excecute", function (assert) {
  var timestampFn = jQueryFrameFn("#page\\:mainForm\\:timestamp span");
  var textFn = jQueryFrameFn("#page\\:mainForm\\:outText span");
  var tipFn = jQueryFrameFn("#page\\:mainForm\\:outTip span");
  var buttonFn = jQueryFrameFn("#page\\:mainForm\\:ajaxButton");

  var timestampValue = timestampFn().text();
  var textValue = textFn().text();
  var tipValue = tipFn().attr('title');

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.notEqual(timestampFn().text(), timestampValue);
    assert.equal(textFn().text(), textValue);
    assert.equal(tipFn().attr('title'), tipValue);
  });
  TTT.startTest();
});
