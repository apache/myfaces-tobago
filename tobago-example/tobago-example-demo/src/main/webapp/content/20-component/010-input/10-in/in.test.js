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

function jQueryFrame(expression) {
  return document.getElementById("page:testframe").contentWindow.jQuery(expression);
}

QUnit.test("inputfield with label", function (assert) {
  var $label = jQueryFrame("#page\\:inormal > label");
  var $inputField = jQueryFrame("#page\\:inormal\\:\\:field");

  assert.equal($label.text(), "Input");
  assert.equal($inputField.val(), "Some Text");

  $inputField.val("abc");
  assert.equal($inputField.val(), "abc");
});

QUnit.test("ajax change event", function (assert) {
  var $inputField = jQueryFrame("#page\\:iajax\\:\\:field");
  var $outputField = jQueryFrame("#page\\:oajax > span:first");

  assert.equal($inputField.val(), "");
  assert.equal($outputField.text(), "");

  $inputField.val("qwe").trigger("change");
  assert.equal($inputField.val(), "qwe");

  var done = assert.async();
  setTimeout(function () {
    assert.equal(jQueryFrame("#page\\:oajax > span:first").text(), "qwe");
    done();
  }, 500);
});
