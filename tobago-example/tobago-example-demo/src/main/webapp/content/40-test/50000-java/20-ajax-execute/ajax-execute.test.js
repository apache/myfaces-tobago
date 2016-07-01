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
  assert.expect(12);
  var done = assert.async(3);
  var step = 1;

  var $in1;
  var $in2;
  var $in3;
  var $in4;

  var $clearButton = jQueryFrame("#page\\:mainForm\\:clear");
  $clearButton.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $in1 = jQueryFrame("#page\\:mainForm\\:in1\\:\\:field");
      $in2 = jQueryFrame("#page\\:mainForm\\:in2\\:\\:field");
      $in3 = jQueryFrame("#page\\:mainForm\\:in3\\:\\:field");
      $in4 = jQueryFrame("#page\\:mainForm\\:in4\\:\\:field");

      assert.equal($in1.val(), "");
      assert.equal($in2.val(), "");
      assert.equal($in3.val(), "");
      assert.equal($in4.val(), "");

      $in1 = jQueryFrame("#page\\:mainForm\\:in1\\:\\:field");
      $in2 = jQueryFrame("#page\\:mainForm\\:in2\\:\\:field");
      $in3 = jQueryFrame("#page\\:mainForm\\:in3\\:\\:field");
      $in4 = jQueryFrame("#page\\:mainForm\\:in4\\:\\:field");
      $in1.val("a");
      $in2.val("b");
      $in3.val("c");
      $in4.val("d");

      var $submitButton = jQueryFrame("#page\\:mainForm\\:submit");
      $submitButton.click();

      $.ajax({
        type: 'GET',
        url: 'content/40-test/50000-java/20-ajax-execute/ajax-execute.xhtml'
      }).done(function () {

        $in1 = jQueryFrame("#page\\:mainForm\\:in1\\:\\:field");
        $in2 = jQueryFrame("#page\\:mainForm\\:in2\\:\\:field");
        $in3 = jQueryFrame("#page\\:mainForm\\:in3\\:\\:field");
        $in4 = jQueryFrame("#page\\:mainForm\\:in4\\:\\:field");

        assert.equal($in1.val(), "a");
        assert.equal($in2.val(), "b");
        assert.equal($in3.val(), "c");
        assert.equal($in4.val(), "");

        var $reloadButton = jQueryFrame("#page\\:mainForm\\:reload");
        $reloadButton.click();

        done();
      });
    } else if (step == 2) {
      $in1 = jQueryFrame("#page\\:mainForm\\:in1\\:\\:field");
      $in2 = jQueryFrame("#page\\:mainForm\\:in2\\:\\:field");
      $in3 = jQueryFrame("#page\\:mainForm\\:in3\\:\\:field");
      $in4 = jQueryFrame("#page\\:mainForm\\:in4\\:\\:field");

      assert.equal($in1.val(), "a");
      assert.equal($in2.val(), "");
      assert.equal($in3.val(), "c");
      assert.equal($in4.val(), "");
    }

    step++;
    done();
  });
});
