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

QUnit.test("inputfield with label", function (assert) {
  var $label = jQueryFrame("#page\\:mainForm\\:iNormal > label");
  var $inputField = jQueryFrame("#page\\:mainForm\\:iNormal\\:\\:field");

  assert.equal($label.text(), "Input");
  assert.equal($inputField.val(), "Some Text");

  $inputField.val("abc");
  assert.equal($inputField.val(), "abc");
});

QUnit.test("ajax change event", function (assert) {
  assert.expect(4);
  var done = assert.async();

  var $inputField = jQueryFrame("#page\\:mainForm\\:inputAjax\\:\\:field");
  var $outputField = jQueryFrame("#page\\:mainForm\\:outputAjax span:first");

  assert.equal($inputField.val(), "");
  assert.equal($outputField.text(), "");

  $inputField.val("qwe").trigger("change");
  assert.equal($inputField.val(), "qwe");

  waitForAjax(function () {
    $outputField = jQueryFrame($outputField.selector);
    return $outputField.text() == "qwe";
  }, function () {
    $outputField = jQueryFrame($outputField.selector);
    assert.equal($outputField.text(), "qwe");
    done();
  });
});
