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
  assert.expect(3);
  var done = assert.async();

  var $timestamp = jQueryFrame("#page\\:mainForm\\:timestamp span");
  var $text = jQueryFrame("#page\\:mainForm\\:outText span");
  var $tip = jQueryFrame("#page\\:mainForm\\:outTip span");
  var $button = jQueryFrame("#page\\:mainForm\\:ajaxButton");

  var timestampValue = $timestamp.text();
  var textValue = $text.text();
  var tipValue = $tip.attr('title');

  $button.click();

  waitForAjax(function () {
    $timestamp = jQueryFrame($timestamp.selector);
    $text = jQueryFrame($text.selector);
    $tip = jQueryFrame($tip.selector);

    return $timestamp.text() !== timestampValue
        && $text.text() === textValue
        && $tip.attr('title') === tipValue;
  }, function () {
    $timestamp = jQueryFrame($timestamp.selector);
    $text = jQueryFrame($text.selector);
    $tip = jQueryFrame($tip.selector);

    assert.notEqual($timestamp.text(), timestampValue);
    assert.equal($text.text(), textValue);
    assert.equal($tip.attr('title'), tipValue);

    done();
  });
});
