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

function getToday() {
  var now = new Date();
  var dd = now.getDate();
  var mm = now.getMonth() + 1;
  var yyyy = now.getFullYear();
  if (dd < 10) {
    dd = '0' + dd
  }
  if (mm < 10) {
    mm = '0' + mm
  }
  return dd + "." + mm + "." + yyyy;
}

QUnit.test("date with label", function (assert) {
  var $label = jQueryFrame("#page\\:mainForm\\:dNormal > label");
  var $dateField = jQueryFrame("#page\\:mainForm\\:dNormal\\:\\:field");
  var $dateButton = jQueryFrame("#page\\:mainForm\\:dNormal button");
  var today = getToday();

  assert.equal($label.text(), "Date");
  assert.equal($dateField.val(), today);

  $dateField.val("32.05.2016");
  $dateButton.click();

  assert.equal($dateField.val(), today);
});

QUnit.test("submit", function (assert) {
  assert.expect(5);
  var done = assert.async();

  var $dateField = jQueryFrame("#page\\:mainForm\\:formSubmit\\:input\\:\\:field");
  var $dateButton = jQueryFrame("#page\\:mainForm\\:formSubmit\\:input button");
  var $outField = jQueryFrame("#page\\:mainForm\\:formSubmit\\:output span");
  var $submitButton = jQueryFrame("#page\\:mainForm\\:formSubmit\\:button");

  assert.equal($dateField.val(), "22.05.2016");
  assert.equal($outField.text(), "22.05.2016");

  $dateButton.click();
  assert.ok(jQueryFrame(".bootstrap-datetimepicker-widget").get(0));
  jQueryFrame(".bootstrap-datetimepicker-widget .day").get(37).click(); // Choose '01.06.2016'.

  assert.equal($dateField.val(), "01.06.2016");
  $submitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $outField = jQueryFrame("#page\\:mainForm\\:formSubmit\\:output span");
    assert.equal($outField.text(), "01.06.2016");
    done();
  });
});

QUnit.test("ajax", function (assert) {
  assert.expect(5);
  var done = assert.async();

  var $dateField = jQueryFrame("#page\\:mainForm\\:ajaxinput\\:\\:field");
  var $dateButton = jQueryFrame("#page\\:mainForm\\:ajaxinput button");
  var $outField = jQueryFrame("#page\\:mainForm\\:outputfield span");
  var today = getToday();

  assert.equal($dateField.val(), "");
  assert.equal($outField.text(), "");

  $dateButton.click();

  assert.ok(jQueryFrame(".bootstrap-datetimepicker-widget").get(0));
  assert.equal($dateField.val(), today);

  waitForAjax(function () {
    $outField = jQueryFrame($outField.selector);
    return $outField.text() == today;
  }, function () {
    $outField = jQueryFrame($outField.selector);
    assert.equal($outField.text(), today);
    done();
  });
});
