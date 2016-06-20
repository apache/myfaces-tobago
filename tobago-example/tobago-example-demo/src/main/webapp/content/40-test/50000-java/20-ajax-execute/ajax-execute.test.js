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

QUnit.test("ajax excecute", function (assert) {

  var $in1;
  var $in2;
  var $in3;
  var $in4;

  var $clearButton = jQueryFrame("#page\\:clear");
  $clearButton.click();

  $in1 = jQueryFrame("#page\\:in1\\:\\:field");
  $in2 = jQueryFrame("#page\\:in2\\:\\:field");
  $in3 = jQueryFrame("#page\\:in3\\:\\:field");
  $in4 = jQueryFrame("#page\\:in4\\:\\:field");

  assert.equal($in1.val(), "");
  assert.equal($in2.val(), "");
  assert.equal($in3.val(), "");
  assert.equal($in4.val(), "");

  $in1.val("a");
  $in2.val("b");
  $in3.val("c");
  $in4.val("d");

  var $submitButton = jQueryFrame("#page\\:clear");
  $submitButton.click();

  // todo: to be continued...

});
